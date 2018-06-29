package io.aesy.bytes.parse;

import io.aesy.bytes.ByteUnit;
import io.aesy.bytes.ByteUnits;
import io.aesy.bytes.Bytes;
import io.aesy.bytes.factory.BytesParserFactory;
import io.aesy.bytes.provider.BytesParserFactoryProvider;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

/**
 * Tests all identical properties of all {@code BytesParser} implementations.
 */
public class GenericBytesParserTest implements WithAssertions {
    @ParameterizedTest
    @ArgumentsSource(BytesParserFactoryProvider.class)
    @DisplayName("it should throw IllegalArgumentException if passed null values")
    public void test_npe(BytesParserFactory parserFactory) {
        assertThatThrownBy(() -> parserFactory.create(null))
            .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> parserFactory.create().parse(null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ArgumentsSource(BytesParserFactoryProvider.class)
    @DisplayName("it should produce a Bytes object")
    public void test_return(BytesParserFactory parserFactory) throws ParseException {
        BytesParser parser = parserFactory.create();
        Bytes bytes = parser.parse("1 B");

        assertThat(bytes)
            .isNotNull();
    }

    @ParameterizedTest
    @ArgumentsSource(BytesParserFactoryProvider.class)
    @DisplayName("it should be able to parse any positive number, including zero")
    public void test_positive_number(BytesParserFactory parserFactory) throws ParseException {
        BytesParser parser = parserFactory.create();

        List<String> strings = Arrays.asList("0", "0.1", "1", "0.0000000000000000001",
                                             Double.toString(Double.MIN_VALUE),
                                             Double.toString(Double.MAX_VALUE));

        for (String string : strings) {
            String input = String.format("%s B", string);

            Bytes bytes = parser.parse(input);

            assertThat(bytes)
                .isNotNull();

            assertThat(bytes.getValue())
                .isNotNull()
                .isEqualTo(new BigDecimal(string));
        }
    }

    @ParameterizedTest
    @ArgumentsSource(BytesParserFactoryProvider.class)
    @DisplayName("it should throw ParseException if passed negative number")
    public void test_negative_number(BytesParserFactory parserFactory) {
        BytesParser parser = parserFactory.create();

        Stream.of("-0.1", "-1", "-1.0")
              .map(string -> String.format("%s B", string))
              .forEach(input -> {
                  assertThatThrownBy(() -> parser.parse(input))
                      .isInstanceOf(ParseException.class);
              });
    }

    @ParameterizedTest
    @ArgumentsSource(BytesParserFactoryProvider.class)
    @DisplayName("it should throw ParseException if passed wholly invalid input")
    public void test_invalid_input(BytesParserFactory parserFactory) {
        BytesParser parser = parserFactory.create();

        Stream.of("", " ", "B", "0", "NaN B", "Infinity B", "B 0", "null")
              .forEach(s -> {
                  assertThatThrownBy(() -> parser.parse(s))
                      .isInstanceOf(ParseException.class);
              });
    }

    @ParameterizedTest
    @ArgumentsSource(BytesParserFactoryProvider.class)
    @DisplayName("it should be locale aware")
    public void test_locale(BytesParserFactory parserFactory) throws ParseException {
        double value = 3141592.65358;
        Locale[] locales = Locale.getAvailableLocales();

        for (Locale locale : locales) {
            BytesParser bytesParser = parserFactory.create(locale);
            NumberFormat decimalFormatter = DecimalFormat.getNumberInstance(locale);
            decimalFormatter.setMaximumFractionDigits(5);

            String input = String.format("%s %s", decimalFormatter.format(value), ByteUnits.BYTE);
            Bytes result = bytesParser.parse(input);

            assertThat(result)
                .isNotNull();

            assertThat(result.getValue())
                .isNotNull()
                .isEqualByComparingTo(BigDecimal.valueOf(value));

            assertThat(result.getUnit())
                .isNotNull()
                .isEqualTo(ByteUnits.BYTE);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(BytesParserFactoryProvider.class)
    @DisplayName("it should be able to parse unit names")
    public void test_unit_names(BytesParserFactory parserFactory) throws ParseException {
        BytesParser parser = parserFactory.create();

        for (ByteUnit unit : ByteUnits.ALL.units()) {
            String input = String.format("1 %s", unit.getName());

            Bytes result = parser.parse(input);

            assertThat(result)
                .isNotNull();

            assertThat(result.getUnit().getName())
                .isNotNull()
                .isEqualTo(unit.getName());
        }
    }

    @ParameterizedTest
    @ArgumentsSource(BytesParserFactoryProvider.class)
    @DisplayName("it should be able to parse unit abbreviations")
    public void test_unit_abbreviations(BytesParserFactory parserFactory) throws ParseException {
        BytesParser parser = parserFactory.create();

        for (ByteUnit unit : ByteUnits.ALL.units()) {
            String input = String.format("1 %s", unit.getAbbreviation());

            Bytes result = parser.parse(input);

            assertThat(result)
                .isNotNull();

            assertThat(result.getUnit().getAbbreviation())
                .isNotNull()
                .isEqualTo(unit.getAbbreviation());
        }
    }

    @ParameterizedTest
    @ArgumentsSource(BytesParserFactoryProvider.class)
    @DisplayName("it should favor SI units over JEDEC")
    public void test_si_over_jedec(BytesParserFactory parserFactory) throws ParseException {
        BytesParser parser = parserFactory.create();

        for (ByteUnit unit : ByteUnits.JEDEC.units()) {
            String input1 = String.format("1 %s", unit.getAbbreviation());
            String input2 = String.format("1 %s", unit.getName());

            Bytes result1 = parser.parse(input1);
            Bytes result2 = parser.parse(input2);

            assertThat(result1)
                .isNotNull();

            assertThat(result2)
                .isNotNull();

            assertThat(result1.getUnit())
                .isNotNull()
                .satisfies(ByteUnits.SI::has);

            assertThat(result2.getUnit())
                .isNotNull()
                .satisfies(ByteUnits.SI::has);
        }
    }
}
