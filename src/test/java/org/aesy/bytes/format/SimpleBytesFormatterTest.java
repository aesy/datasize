package org.aesy.bytes.format;

import org.aesy.bytes.ByteUnit;
import org.aesy.bytes.Bytes;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class SimpleBytesFormatterTest implements WithAssertions {
    @Test
    @DisplayName("it should throw NullPointerException if passed null values")
    public void test_npe() {
        assertThatThrownBy(() -> new SimpleBytesFormatter(null))
            .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> new SimpleBytesFormatter(null, 0))
            .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> new SimpleBytesFormatter().format(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("it should throw IllegalArgumentException if passed precision less than zero")
    public void test_lessThanZero() {
        assertThatThrownBy(() -> new SimpleBytesFormatter(-1))
            .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> new SimpleBytesFormatter(Locale.getDefault(), -1))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("it should produce a string")
    public void test_return() {
        BytesFormatter formatter = new SimpleBytesFormatter();
        Bytes bytes = Bytes.valueOf(0, ByteUnit.SI.KILOBYTE);

        assertThat(bytes.toString())
            .isNotBlank();
    }

    @Test
    @DisplayName("it should cap the amount of fraction digits and round the value")
    public void test_decimals() {
        double value = Math.PI;
        String string = String.valueOf(value);
        DecimalFormat decimalFormatter = new DecimalFormat();
        int decimals = string.length() - 2;
        Bytes bytes = Bytes.valueOf(value, ByteUnit.BYTE);

        for (int i = 0; i < decimals; i++) {
            decimalFormatter.setMaximumFractionDigits(i);
            String expected = decimalFormatter.format(value);

            BytesFormatter bytesFormatter = new SimpleBytesFormatter(i);
            String result = bytesFormatter.format(bytes);

            assertThat(result)
                .isNotBlank()
                .startsWith(expected);
        }
    }

    @Test
    @DisplayName("it should show decimal zeroes if not exactly even")
    public void test_zeroes() {
        Bytes bytes = Bytes.valueOf(new BigDecimal("1.001"), ByteUnit.BYTE);
        BytesFormatter formatter = new SimpleBytesFormatter(2);
        String result = formatter.format(bytes);

        assertThat(result)
            .isNotBlank()
            .startsWith("1.00");
    }

    @Test
    @DisplayName("it should end with the unit type")
    public void test_unit() {
        BytesFormatter formatter = new SimpleBytesFormatter(0);

        for (ByteUnit.SI unit : ByteUnit.SI.values()) {
            Bytes bytes = Bytes.valueOf(Math.E, unit);
            String result = formatter.format(bytes);

            assertThat(result)
                .isNotBlank()
                .endsWith(unit.getAbbreviation());
        }
    }

    @Test
    @DisplayName("it should format value based on locale")
    public void test_locale() {
        double value = Math.PI * 100_000;
        Bytes bytes = Bytes.valueOf(value, ByteUnit.BYTE);
        Locale[] locales = Locale.getAvailableLocales();

        for (Locale locale : locales) {
            BytesFormatter bytesFormatter = new SimpleBytesFormatter(locale, 5);
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
