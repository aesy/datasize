package io.aesy.datasize.parse;

import io.aesy.datasize.BitUnit;
import io.aesy.datasize.ByteUnit;
import io.aesy.datasize.DataUnit;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

/**
 * Tests properties unique to {@code StrictDataSizeParser}.
 */
public class StrictDataSizeParserTest implements WithAssertions {
    private static final Collection<DataUnit> ALL_UNITS;

    static {
        ALL_UNITS = new ArrayList<>();
        ALL_UNITS.add(ByteUnit.BYTE);
        ALL_UNITS.add(BitUnit.BIT);
        ALL_UNITS.addAll(ByteUnit.SI.values());
        ALL_UNITS.addAll(ByteUnit.IEC.values());
        ALL_UNITS.addAll(ByteUnit.JEDEC.values());
        ALL_UNITS.addAll(BitUnit.SI.values());
        ALL_UNITS.addAll(BitUnit.IEC.values());
        ALL_UNITS.addAll(BitUnit.JEDEC.values());
    }

    @BeforeEach
    public void setup() {
        Locale.setDefault(Locale.US);
    }

    @Test
    @DisplayName("it should not accept any superfluous whitespace")
    public void test_superfluous_whitespace() {
        DataSizeParser parser = new StrictDataSizeParser();

        for (DataUnit unit : ALL_UNITS) {
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
        DataSizeParser parser = new StrictDataSizeParser();

        for (DataUnit unit : ALL_UNITS) {
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
        DataSizeParser parser = new StrictDataSizeParser();

        Collection<DataUnit> units = new ArrayList<>();
        units.add(ByteUnit.BYTE);
        units.addAll(ByteUnit.SI.values());
        units.addAll(ByteUnit.IEC.values());
        units.addAll(ByteUnit.JEDEC.values());

        for (DataUnit unit : units) {
            String input1 = String.format("1 %s", unit.getAbbreviation().toLowerCase());
            String input2 = String.format("1 %s", unit.getName().toUpperCase());

            assertThatThrownBy(() -> parser.parse(input1))
                .isInstanceOf(ParseException.class);

            assertThatThrownBy(() -> parser.parse(input2))
                .isInstanceOf(ParseException.class);
        }
    }
}
