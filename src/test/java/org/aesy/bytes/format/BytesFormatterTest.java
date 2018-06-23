package org.aesy.bytes.format;

import org.aesy.bytes.ByteUnits;
import org.aesy.bytes.Bytes;
import org.aesy.bytes.factory.BytesFormatterFactory;
import org.aesy.bytes.provider.BytesFormatterFactoryProvider;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Tests all identical properties of all {@code BytesFormatter} implementations.
 */
public class BytesFormatterTest implements WithAssertions {
    @ParameterizedTest
    @ArgumentsSource(BytesFormatterFactoryProvider.class)
    @DisplayName("it should throw NullPointerException if passed null values")
    public void test_npe(BytesFormatterFactory formatterFactory) {
        assertThatThrownBy(() -> formatterFactory.create(null))
            .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> formatterFactory.create(null, 0))
            .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> formatterFactory.create().format(null))
            .isInstanceOf(NullPointerException.class);
    }

    @ParameterizedTest
    @ArgumentsSource(BytesFormatterFactoryProvider.class)
    @DisplayName("it should throw IllegalArgumentException if passed precision less than zero")
    public void test_lessThanZero(BytesFormatterFactory formatterFactory) {
        assertThatThrownBy(() -> formatterFactory.create(-1))
            .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> formatterFactory.create(Locale.getDefault(), -1))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ArgumentsSource(BytesFormatterFactoryProvider.class)
    @DisplayName("it should produce a non-empty string")
    public void test_return(BytesFormatterFactory formatterFactory) {
        BytesFormatter formatter = formatterFactory.create();
        Bytes bytes = Bytes.valueOf(0, ByteUnits.COMMON.BYTE);

        assertThat(bytes.toString())
            .isNotBlank();
    }

    @ParameterizedTest
    @ArgumentsSource(BytesFormatterFactoryProvider.class)
    @DisplayName("it should cap the amount of fraction digits and round the value")
    public void test_decimals(BytesFormatterFactory formatterFactory) {
        double value = Math.PI;
        String string = String.valueOf(value);
        DecimalFormat decimalFormatter = new DecimalFormat();
        int decimals = string.length() - 2;
        Bytes bytes = Bytes.valueOf(value, ByteUnits.COMMON.BYTE);

        for (int i = 0; i < decimals; i++) {
            decimalFormatter.setMaximumFractionDigits(i);
            String expected = decimalFormatter.format(value);

            BytesFormatter bytesFormatter = formatterFactory.create(i);
            String result = bytesFormatter.format(bytes);

            assertThat(result)
                .isNotBlank()
                .startsWith(expected);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(BytesFormatterFactoryProvider.class)
    @DisplayName("it should show decimal zeroes if not exactly even")
    public void test_zeroes(BytesFormatterFactory formatterFactory) {
        Bytes bytes = Bytes.valueOf(new BigDecimal("1.001"), ByteUnits.COMMON.BYTE);
        BytesFormatter formatter = formatterFactory.create(2);
        String result = formatter.format(bytes);

        assertThat(result)
            .isNotBlank()
            .startsWith("1.00");
    }

    @ParameterizedTest
    @ArgumentsSource(BytesFormatterFactoryProvider.class)
    @DisplayName("it should format value based on locale")
    public void test_locale(BytesFormatterFactory formatterFactory) {
        double value = Math.PI;
        Bytes bytes = Bytes.valueOf(value, ByteUnits.COMMON.BYTE);
        Locale[] locales = Locale.getAvailableLocales();

        for (Locale locale : locales) {
            BytesFormatter bytesFormatter = formatterFactory.create(locale, 5);
            NumberFormat decimalFormatter = DecimalFormat.getNumberInstance(locale);
            decimalFormatter.setMaximumFractionDigits(5);

            String expected = decimalFormatter.format(value);
            String result = bytesFormatter.format(bytes);

            assertThat(result)
                .isNotBlank()
                .startsWith(expected);
        }
    }
}
