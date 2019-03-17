package io.aesy.datasize.parse;

import io.aesy.datasize.BitUnit;
import io.aesy.datasize.ByteUnit;
import io.aesy.datasize.DataSize;
import io.aesy.datasize.DataUnit;
import io.aesy.datasize.factory.DataSizeParserFactory;
import io.aesy.datasize.provider.DataSizeParserFactoryProvider;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Stream;

/**
 * Tests all identical properties of all {@code DataSizeParser} implementations.
 */
public class GenericDataSizeParserTest implements WithAssertions {
    private static final Collection<DataUnit> ALL_UNITS;

    static {
        ALL_UNITS = new ArrayList<>();
        ALL_UNITS.addAll(BitUnit.values());
        ALL_UNITS.addAll(ByteUnit.values());
    }

    @BeforeEach
    public void setup() {
        Locale.setDefault(Locale.US);
    }

    @ParameterizedTest
    @ArgumentsSource(DataSizeParserFactoryProvider.class)
    @DisplayName("it should throw IllegalArgumentException if passed null values")
    public void test_npe(DataSizeParserFactory parserFactory) {
        assertThatThrownBy(() -> parserFactory.create(null))
            .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> parserFactory.create().parse(null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ArgumentsSource(DataSizeParserFactoryProvider.class)
    @DisplayName("it should produce a DataSize object")
    public void test_return(DataSizeParserFactory parserFactory) throws ParseException {
        DataSizeParser parser = parserFactory.create();
        DataSize dataSize = parser.parse("1 B");

        assertThat(dataSize)
            .isNotNull();
    }

    @ParameterizedTest
    @ArgumentsSource(DataSizeParserFactoryProvider.class)
    @DisplayName("it should be able to parse any positive number, including zero")
    public void test_positive_number(DataSizeParserFactory parserFactory) throws ParseException {
        DataSizeParser parser = parserFactory.create();

        List<String> strings = Arrays.asList("0", "0.1", "1", "0.0000000000000000001",
                                             Double.toString(Double.MIN_VALUE),
                                             Double.toString(Double.MAX_VALUE));

        for (String string : strings) {
            String input = String.format("%s B", string);

            DataSize dataSize = parser.parse(input);

            assertThat(dataSize)
                .isNotNull();

            assertThat(dataSize.getValue())
                .isNotNull()
                .isEqualTo(new BigDecimal(string));
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DataSizeParserFactoryProvider.class)
    @DisplayName("it should throw ParseException if passed negative number")
    public void test_negative_number(DataSizeParserFactory parserFactory) {
        DataSizeParser parser = parserFactory.create();

        Stream.of("-0.1", "-1", "-1.0")
              .map(string -> String.format("%s B", string))
              .forEach(input -> {
                  assertThatThrownBy(() -> parser.parse(input))
                      .isInstanceOf(ParseException.class);
              });
    }

    @ParameterizedTest
    @ArgumentsSource(DataSizeParserFactoryProvider.class)
    @DisplayName("it should throw ParseException if passed wholly invalid input")
    public void test_invalid_input(DataSizeParserFactory parserFactory) {
        DataSizeParser parser = parserFactory.create();

        Stream.of("", " ", "B", "0", "NaN B", "Infinity B", "B 0", "null")
              .forEach(s -> {
                  assertThatThrownBy(() -> parser.parse(s))
                      .isInstanceOf(ParseException.class);
              });
    }

    @ParameterizedTest
    @ArgumentsSource(DataSizeParserFactoryProvider.class)
    @DisplayName("it should be locale aware")
    public void test_locale(DataSizeParserFactory parserFactory) throws ParseException {
        double value = 3141592.65358;
        Locale[] locales = Locale.getAvailableLocales();

        for (Locale locale : locales) {
            DataSizeParser dataSizeParser = parserFactory.create(locale);
            NumberFormat decimalFormatter = NumberFormat.getNumberInstance(locale);
            decimalFormatter.setMaximumFractionDigits(5);

            String input = String.format("%s %s", decimalFormatter.format(value), ByteUnit.BYTE);
            DataSize result = dataSizeParser.parse(input);

            assertThat(result)
                .isNotNull();

            assertThat(result.getValue())
                .isNotNull()
                .isEqualByComparingTo(BigDecimal.valueOf(value));

            assertThat(result.getUnit())
                .isNotNull()
                .isEqualTo(ByteUnit.BYTE);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DataSizeParserFactoryProvider.class)
    @DisplayName("it should be able to parse unit names")
    public void test_unit_names(DataSizeParserFactory parserFactory) throws ParseException {
        DataSizeParser parser = parserFactory.create();

        for (DataUnit unit : ALL_UNITS) {
            String input = String.format("1 %s", unit.getName());

            DataSize result = parser.parse(input);

            assertThat(result)
                .isNotNull();

            assertThat(result.getUnit().getName())
                .isNotNull()
                .isEqualTo(unit.getName());
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DataSizeParserFactoryProvider.class)
    @DisplayName("it should be able to parse unit abbreviations")
    public void test_unit_abbreviations(DataSizeParserFactory parserFactory) throws ParseException {
        DataSizeParser parser = parserFactory.create();

        for (DataUnit unit : ALL_UNITS) {
            String input = String.format("1 %s", unit.getAbbreviation());

            DataSize result = parser.parse(input);

            assertThat(result)
                .isNotNull();

            assertThat(result.getUnit().getAbbreviation())
                .isNotNull()
                .isEqualToIgnoringCase(unit.getAbbreviation());
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DataSizeParserFactoryProvider.class)
    @DisplayName("it should favor SI units over JEDEC")
    public void test_si_over_jedec(DataSizeParserFactory parserFactory) throws ParseException {
        DataSizeParser parser = parserFactory.create();

        for (DataUnit unit : ByteUnit.JEDEC.values()) {
            String input1 = String.format("1 %s", unit.getAbbreviation());
            String input2 = String.format("1 %s", unit.getName());

            DataSize result1 = parser.parse(input1);
            DataSize result2 = parser.parse(input2);

            assertThat(result1)
                .isNotNull();

            assertThat(result2)
                .isNotNull();

            assertThat(result1.getUnit())
                .isNotNull()
                .satisfies(ByteUnit.SI.values()::contains);

            assertThat(result2.getUnit())
                .isNotNull()
                .satisfies(ByteUnit.SI.values()::contains);
        }
    }
}
