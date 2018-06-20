package org.aesy.bytes;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;

/**
 * For more thorough testing of parsing, see the test cases for the classes dedicated to parsing.
 */
public class BytesTest implements WithAssertions {
    @Test
    @DisplayName("it should throw NullPointerException if passed null values")
    public void test_npe() {
        assertThatThrownBy(() -> Bytes.valueOf((BigInteger) null, ByteUnit.BYTE))
            .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> Bytes.valueOf((BigDecimal) null, ByteUnit.BYTE))
            .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> Bytes.valueOf(BigDecimal.ONE, null))
            .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> Bytes.valueOf((BigInteger) null, null))
            .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> Bytes.valueOf((BigDecimal) null, null))
            .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> Bytes.valueOf(0, ByteUnit.BYTE).compareTo(null))
            .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> Bytes.parse(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("it should throw IllegalArgumentException if passed value less than zero")
    public void test_lessThanZero() {
        assertThatThrownBy(() -> Bytes.valueOf(BigInteger.valueOf(-1), ByteUnit.BYTE))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("it should hold a value and a unit")
    public void test_value_unit() {
        BigDecimal value = BigDecimal.ZERO;
        ByteUnit unit = ByteUnit.BYTE;
        Bytes bytes = Bytes.valueOf(value, unit);

        assertThat(bytes.getValue())
            .isNotNull()
            .isEqualTo(value);

        assertThat(bytes.getUnit())
            .isNotNull()
            .isEqualTo(unit);
    }

    @Test
    @DisplayName("it should be able to parse its own string representation")
    public void test_parse_toString() throws ParseException {
        Bytes bytes = Bytes.valueOf(1.23, ByteUnit.BYTE);

        assertThat(Bytes.parse(bytes.toString()))
            .isEqualTo(bytes);
    }

    @Test
    @DisplayName("it should be comparable similarly to numbers and be equal if and only if their values as byte are equal")
    public void test_compareTo() {
        Bytes nothing = Bytes.valueOf(0, ByteUnit.BYTE);
        Bytes bytes = Bytes.valueOf(2048, ByteUnit.BYTE);
        Bytes kilobytes = Bytes.valueOf(2048, ByteUnit.JEDEC.KILOBYTE);
        Bytes megabytes = Bytes.valueOf(2, ByteUnit.JEDEC.MEGABYTE);
        Bytes gigabytes = Bytes.valueOf(1, ByteUnit.JEDEC.GIGABYTE);

        assertThat(kilobytes)
            .isEqualByComparingTo(megabytes)
            .isLessThan(gigabytes)
            .isGreaterThan(bytes)
            .isGreaterThan(nothing);
    }

    @Test
    @DisplayName("it should be equal to another instance if and only if their values as byte are equal")
    public void test_equals() {
        Bytes nothing = Bytes.valueOf(0, ByteUnit.BYTE);
        Bytes bytes = Bytes.valueOf(2048, ByteUnit.BYTE);
        Bytes kilobytes = Bytes.valueOf(2048, ByteUnit.JEDEC.KILOBYTE);
        Bytes megabytes = Bytes.valueOf(2, ByteUnit.JEDEC.MEGABYTE);

        assertThat(kilobytes)
            .isNotEqualTo(null)
            .isNotEqualTo(new Object())
            .isNotEqualTo(bytes)
            .isEqualTo(kilobytes)
            .isEqualTo(megabytes);
    }

    @Test
    @DisplayName("it should have a string representation which is nicely formatted")
    public void test_toString() {
        double value = 15.389203948;
        ByteUnit unit = ByteUnit.SI.KILOBYTE;
        Bytes bytes = Bytes.valueOf(value, unit);

        assertThat(bytes.toString())
            .isNotNull()
            .isNotBlank()
            .isEqualTo(String.format("%.2f %s", value, unit));
    }
}
