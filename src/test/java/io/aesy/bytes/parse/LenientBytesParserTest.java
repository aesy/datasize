package io.aesy.bytes.parse;

import io.aesy.bytes.ByteUnit;
import io.aesy.bytes.ByteUnits;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests properties unique to {@code LenientBytesParser}.
 */
public class LenientBytesParserTest implements WithAssertions {
    @Test
    @DisplayName("it should accept superfluous whitespace")
    public void test_superfluous_whitespace() {
        BytesParser parser = new LenientBytesParser();

        for (ByteUnit unit : ByteUnits.ALL.units()) {
            String input1 = String.format(" 1 %s", unit.getAbbreviation());
            String input2 = String.format("1  %s", unit.getAbbreviation());
            String input3 = String.format("1 %s ", unit.getAbbreviation());

            assertThatCode(() -> parser.parse(input1))
                .doesNotThrowAnyException();

            assertThatCode(() -> parser.parse(input2))
                .doesNotThrowAnyException();

            assertThatCode(() -> parser.parse(input3))
                .doesNotThrowAnyException();
        }
    }

    @Test
    @DisplayName("it should accept missing whitespace")
    public void test_missing_whitespace() {
        BytesParser parser = new LenientBytesParser();

        for (ByteUnit unit : ByteUnits.ALL.units()) {
            String input1 = String.format("1%s", unit.getAbbreviation());
            String input2 = String.format("1%s", unit.getName());

            assertThatCode(() -> parser.parse(input1))
                .doesNotThrowAnyException();

            assertThatCode(() -> parser.parse(input2))
                .doesNotThrowAnyException();
        }
    }

    @Test
    @DisplayName("it should be case insensitive")
    public void test_case() {
        BytesParser parser = new LenientBytesParser();

        for (ByteUnit unit : ByteUnits.ALL.units()) {
            String input1 = String.format("1 %s", unit.getAbbreviation().toLowerCase());
            String input2 = String.format("1 %s", unit.getAbbreviation().toUpperCase());
            String input3 = String.format("1 %s", unit.getName().toUpperCase());
            String input4 = String.format("1 %s", unit.getName().toUpperCase());

            assertThatCode(() -> parser.parse(input1))
                .doesNotThrowAnyException();

            assertThatCode(() -> parser.parse(input2))
                .doesNotThrowAnyException();

            assertThatCode(() -> parser.parse(input3))
                .doesNotThrowAnyException();

            assertThatCode(() -> parser.parse(input4))
                .doesNotThrowAnyException();
        }
    }
}
