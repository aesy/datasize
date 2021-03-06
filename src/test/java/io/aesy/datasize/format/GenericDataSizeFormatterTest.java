package io.aesy.datasize.format;

import io.aesy.datasize.ByteUnit;
import io.aesy.datasize.DataSize;
import io.aesy.datasize.factory.DataSizeFormatterFactory;
import io.aesy.datasize.provider.DataSizeFormatterFactoryProvider;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Tests all identical properties of all {@code DataSizeFormatter} implementations.
 */
public class GenericDataSizeFormatterTest implements WithAssertions {
    @BeforeEach
    public void setup() {
        Locale.setDefault(Locale.US);
    }

    @ParameterizedTest
    @ArgumentsSource(DataSizeFormatterFactoryProvider.class)
    @DisplayName("it should throw IllegalArgumentException if passed null values")
    @DisabledIfSystemProperty(named = "se.eris.notnull.instrument", matches = "false")
    public void test_npe(DataSizeFormatterFactory formatterFactory) {
        assertThatThrownBy(() -> formatterFactory.create(null))
            .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> formatterFactory.create(null, 0))
            .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> formatterFactory.create().format(null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ArgumentsSource(DataSizeFormatterFactoryProvider.class)
    @DisplayName("it should produce a non-empty string")
    public void test_return(DataSizeFormatterFactory formatterFactory) {
        DataSizeFormatter formatter = formatterFactory.create();
        DataSize dataSize = DataSize.of(0, ByteUnit.BYTE);

        assertThat(formatter.format(dataSize))
            .isNotBlank();
    }

    @ParameterizedTest
    @ArgumentsSource(DataSizeFormatterFactoryProvider.class)
    @DisplayName("it should cap the amount of fraction digits and round the value based on " +
                 "precision")
    public void test_cap_decimals(DataSizeFormatterFactory formatterFactory) {
        double value = Math.PI;
        DecimalFormat decimalFormatter = new DecimalFormat();
        DataSize dataSize = DataSize.of(value, ByteUnit.BYTE);

        for (int i = 0; i < 10; i++) {
            decimalFormatter.setMaximumFractionDigits(i);
            String expected = decimalFormatter.format(value);

            DataSizeFormatter dataSizeFormatter = formatterFactory.create(i);
            String result = dataSizeFormatter.format(dataSize);

            assertThat(result)
                .isNotBlank()
                .startsWith(expected);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DataSizeFormatterFactoryProvider.class)
    @DisplayName("it should display all fraction digits if precision is below zero")
    public void test_all_decimals(DataSizeFormatterFactory formatterFactory) {
        double value = Math.PI;
        String expected = String.valueOf(value);
        DataSize dataSize = DataSize.of(value, ByteUnit.BYTE);

        DataSizeFormatter dataSizeFormatter = formatterFactory.create(Locale.US, -1);
        String result = dataSizeFormatter.format(dataSize);

        assertThat(result)
            .isNotBlank()
            .startsWith(expected);
    }

    @ParameterizedTest
    @ArgumentsSource(DataSizeFormatterFactoryProvider.class)
    @DisplayName("it should show decimal zeroes if not exactly even")
    public void test_zeroes(DataSizeFormatterFactory formatterFactory) {
        DataSize dataSize = DataSize.of(new BigDecimal("1.001"), ByteUnit.BYTE);
        DataSizeFormatter formatter = formatterFactory.create(2);
        String result = formatter.format(dataSize);

        assertThat(result)
            .isNotBlank()
            .startsWith("1.00");
    }

    @ParameterizedTest
    @ArgumentsSource(DataSizeFormatterFactoryProvider.class)
    @DisplayName("it should be locale aware")
    public void test_locale(DataSizeFormatterFactory formatterFactory) {
        double value = Math.PI;
        DataSize dataSize = DataSize.of(value, ByteUnit.BYTE);
        Locale[] locales = Locale.getAvailableLocales();

        for (Locale locale : locales) {
            DataSizeFormatter dataSizeFormatter = formatterFactory.create(locale, 5);
            NumberFormat decimalFormatter = NumberFormat.getNumberInstance(locale);
            decimalFormatter.setMaximumFractionDigits(5);

            String expected = decimalFormatter.format(value);
            String result = dataSizeFormatter.format(dataSize);

            assertThat(result)
                .isNotBlank()
                .startsWith(expected);
        }
    }
}
