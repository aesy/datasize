package io.aesy.datasize.parse;

import io.aesy.datasize.BitUnit;
import io.aesy.datasize.ByteUnit;
import io.aesy.datasize.DataUnit;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

/**
 * Tests properties unique to {@code LenientDataSizeParser}.
 */
public class LenientDataSizeParserTest implements WithAssertions {
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
    @DisplayName("it should accept superfluous whitespace")
    public void test_superfluous_whitespace() {
        DataSizeParser parser = new LenientDataSizeParser();

        for (DataUnit unit : ALL_UNITS) {
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
        DataSizeParser parser = new LenientDataSizeParser();

        for (DataUnit unit : ALL_UNITS) {
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
        DataSizeParser parser = new LenientDataSizeParser();

        for (DataUnit unit : ALL_UNITS) {
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
