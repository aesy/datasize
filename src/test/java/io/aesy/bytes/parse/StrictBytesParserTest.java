package io.aesy.bytes.parse;

import io.aesy.bytes.ByteUnit;
import io.aesy.bytes.ByteUnits;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

/**
 * Tests properties unique to {@code StrictBytesParser}.
 */
public class StrictBytesParserTest implements WithAssertions {
    @Test
    @DisplayName("it should not accept any superfluous whitespace")
    public void test_superfluous_whitespace() {
        BytesParser parser = new StrictBytesParser();

        for (ByteUnit unit : ByteUnits.ALL.units()) {
            String input1 = String.format(" 1 %s", unit.getAbbreviation());
            String input2 = String.format("1  %s", unit.getAbbreviation());
            String input3 = String.format("1 %s ", unit.getAbbreviation());

            assertThatThrownBy(() -> parser.parse(input1))
                .isInstanceOf(ParseException.class);

            assertThatThrownBy(() -> parser.parse(input2))
                .isInstanceOf(ParseException.class);

            assertThatThrownBy(() -> parser.parse(input3))
                .isInstanceOf(ParseException.class);
        }
    }

    @Test
    @DisplayName("it should not accept any missing whitespace")
    public void test_missing_whitespace() {
        BytesParser parser = new StrictBytesParser();

        for (ByteUnit unit : ByteUnits.ALL.units()) {
            String input1 = String.format("1%s", unit.getAbbreviation());
            String input2 = String.format("1%s", unit.getName());

            assertThatThrownBy(() -> parser.parse(input1))
                .isInstanceOf(ParseException.class);

            assertThatThrownBy(() -> parser.parse(input2))
                .isInstanceOf(ParseException.class);
        }
    }

    @Test
    @DisplayName("it should be case sensitive")
    public void test_case() {
        BytesParser parser = new StrictBytesParser();

        for (ByteUnit unit : ByteUnits.ALL.units()) {
            String input1 = String.format("1 %s", unit.getAbbreviation().toLowerCase());
            String input2 = String.format("1 %s", unit.getName().toUpperCase());

            if (!unit.equals(ByteUnits.BIT)) {
                // Don't test bit as it's already lowercase and will not throw
                assertThatThrownBy(() -> parser.parse(input1))
                    .isInstanceOf(ParseException.class);
            }

            assertThatThrownBy(() -> parser.parse(input2))
                .isInstanceOf(ParseException.class);
        }
    }
}
