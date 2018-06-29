package io.aesy.bytes;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.stream.IntStream;

public class BytesTest implements WithAssertions {
    @Test
    @DisplayName("it should throw IllegalArgumentException if passed null values")
    public void test_npe() {
        assertThatThrownBy(() -> Bytes.valueOf((BigInteger) null, ByteUnits.BYTE))
            .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> Bytes.valueOf((BigDecimal) null, ByteUnits.BYTE))
            .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> Bytes.valueOf(BigDecimal.ONE, null))
            .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> Bytes.valueOf((BigInteger) null, null))
            .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> Bytes.valueOf((BigDecimal) null, null))
            .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> Bytes.valueOf(0, ByteUnits.BYTE).compareTo(null))
            .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> Bytes.parse(null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("it should throw IllegalArgumentException if passed value less than zero")
    public void test_iae_lessThanZero() {
        assertThatThrownBy(() -> Bytes.valueOf(BigInteger.valueOf(-1), ByteUnits.BYTE))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("it should be immutable")
    public void test_immutable() {
        BigDecimal value = BigDecimal.valueOf(1.23);
        ByteUnit unit = ByteUnits.BYTE;
        Bytes bytes = Bytes.valueOf(value, unit);

        bytes.add(Bytes.valueOf(Math.PI, ByteUnits.SI.KILOBYTE))
             .subtract(Bytes.valueOf(Math.E, ByteUnits.JEDEC.GIGABYTE))
             .increment()
             .increment()
             .decrement()
             .toNaturalUnit()
             .toUnit(ByteUnits.SI.YOTTABYTE);

        assertThat(bytes.getValue())
            .isNotNull()
            .isEqualTo(value);

        assertThat(bytes.getUnit())
            .isNotNull()
            .isEqualTo(unit);
    }

    @Test
    @DisplayName("it should be able to parse its own string representation")
    public void test_parse() throws ParseException {
        Bytes bytes = Bytes.valueOf(1.23, ByteUnits.BYTE);

        assertThat(Bytes.parse(bytes.toString()))
            .isEqualTo(bytes);
    }

    @Test
    @DisplayName("it should hold a value and a unit")
    public void test_value_unit() {
        BigDecimal value = BigDecimal.ZERO;
        ByteUnit unit = ByteUnits.BYTE;
        Bytes bytes = Bytes.valueOf(value, unit);

        assertThat(bytes.getValue())
            .isNotNull()
            .isEqualTo(value);

        assertThat(bytes.getUnit())
            .isNotNull()
            .isEqualTo(unit);
    }

    @Test
    @DisplayName("it should be able to pick the greater of two objects")
    public void test_max() {
        Bytes bytes1 = Bytes.valueOf(1024, ByteUnits.BYTE);
        Bytes bytes2 = Bytes.valueOf(1, ByteUnits.SI.KILOBYTE);

        Bytes max1 = Bytes.max(bytes1, bytes2);
        Bytes max2 = Bytes.max(bytes2, bytes1);

        assertThat(max1)
            .isNotNull()
            .isSameAs(bytes1);

        assertThat(max2)
            .isNotNull()
            .isSameAs(bytes1);
    }

    @Test
    @DisplayName("it should be able to pick the lesser of two objects")
    public void test_min() {
        Bytes bytes1 = Bytes.valueOf(1024, ByteUnits.BYTE);
        Bytes bytes2 = Bytes.valueOf(1, ByteUnits.SI.KILOBYTE);

        Bytes min1 = Bytes.min(bytes1, bytes2);
        Bytes min2 = Bytes.min(bytes2, bytes1);

        assertThat(min1)
            .isNotNull()
            .isSameAs(bytes2);

        assertThat(min2)
            .isNotNull()
            .isSameAs(bytes2);
    }

    @Test
    @DisplayName("it should have simple math functions such as add")
    public void test_add() {
        Bytes bytes1 = Bytes.valueOf(1024, ByteUnits.BYTE);
        Bytes bytes2 = Bytes.valueOf(1, ByteUnits.IEC.KIBIBYTE);

        Bytes sum = bytes1.add(bytes2);

        assertThat(sum)
            .isNotNull()
            .isEqualTo(Bytes.valueOf(2, ByteUnits.IEC.KIBIBYTE));
    }

    @Test
    @DisplayName("it should have simple math functions such as subtract")
    public void test_subtract() {
        Bytes bytes1 = Bytes.valueOf(1024, ByteUnits.BYTE);
        Bytes bytes2 = Bytes.valueOf(1, ByteUnits.SI.KILOBYTE);

        Bytes diff = bytes1.subtract(bytes2);

        assertThat(diff)
            .isNotNull()
            .isEqualTo(Bytes.valueOf(24, ByteUnits.BYTE));
    }

    @Test
    @DisplayName("it should never be less than zero")
    public void test_subtract_lessThanZero() {
        Bytes bytes1 = Bytes.valueOf(0, ByteUnits.BYTE);
        Bytes bytes2 = Bytes.valueOf(1, ByteUnits.BYTE);

        Bytes diff1 = bytes1.subtract(bytes2);
        Bytes diff2 = bytes1.decrement();

        assertThat(diff1)
            .isNotNull()
            .isEqualTo(Bytes.valueOf(0, ByteUnits.BYTE))
            .isEqualTo(diff2);
    }

    @Test
    @DisplayName("it should increment")
    public void test_increment() {
        for (ByteUnit unit : ByteUnits.ALL.units()) {
            IntStream.range(0, 10)
                     .forEach(i -> {
                         Bytes bytes = Bytes.valueOf(i, unit)
                                            .increment();

                         assertThat(bytes.getValue())
                             .isNotNull()
                             .isEqualTo(BigDecimal.valueOf(i + 1));

                         assertThat(bytes.getUnit())
                             .isNotNull()
                             .isEqualTo(unit);
                     });
        }
    }

    @Test
    @DisplayName("it should decrement")
    public void test_decrement() {
        for (ByteUnit unit : ByteUnits.ALL.units()) {
            IntStream.range(0, 10)
                     .forEach(i -> {
                         Bytes bytes = Bytes.valueOf(i, unit)
                                            .decrement();

                         assertThat(bytes.getValue())
                             .isNotNull()
                             .isEqualTo(BigDecimal.valueOf(Math.max(0, i - 1)));

                         assertThat(bytes.getUnit())
                             .isNotNull()
                             .isEqualTo(unit);
                     });
        }
    }

    @Test
    @DisplayName("it should be convertable to other units")
    public void test_toUnit() {
        Bytes bytes = Bytes.valueOf(1, ByteUnits.BYTE);

        for (ByteUnit unit : ByteUnits.ALL.units()) {
            Bytes converted = bytes.toUnit(unit);

            assertThat(converted.getUnit())
                .isNotNull()
                .isEqualTo(unit);
        }
    }

    @Test
    @DisplayName("it should be comparable similarly to numbers and be equal if and only if their " +
                 "values as byte are equal")
    public void test_compareTo() {
        Bytes nothing = Bytes.valueOf(0, ByteUnits.BYTE);
        Bytes bytes = Bytes.valueOf(2048, ByteUnits.BYTE);
        Bytes kilobytes = Bytes.valueOf(2048, ByteUnits.JEDEC.KILOBYTE);
        Bytes megabytes = Bytes.valueOf(2, ByteUnits.JEDEC.MEGABYTE);
        Bytes gigabytes = Bytes.valueOf(1, ByteUnits.JEDEC.GIGABYTE);

        assertThat(kilobytes)
            .isEqualByComparingTo(megabytes)
            .isLessThan(gigabytes)
            .isGreaterThan(bytes)
            .isGreaterThan(nothing);
    }

    @Test
    @DisplayName("it should be equal to another instance if and only if their values as byte are " +
                 "equal")
    public void test_equals() {
        Bytes nothing = Bytes.valueOf(0, ByteUnits.BYTE);
        Bytes bytes = Bytes.valueOf(2048, ByteUnits.BYTE);
        Bytes kilobytes = Bytes.valueOf(2048, ByteUnits.JEDEC.KILOBYTE);
        Bytes megabytes = Bytes.valueOf(2, ByteUnits.JEDEC.MEGABYTE);

        assertThat(kilobytes)
            .isNotNull()
            .isNotEqualTo(new Object())
            .isNotEqualTo(bytes)
            .isEqualTo(kilobytes)
            .isEqualTo(megabytes);
    }

    @Test
    @DisplayName("it should have a string representation which is nicely formatted")
    public void test_toString() {
        double value = 15.389203948;
        ByteUnit unit = ByteUnits.SI.KILOBYTE;
        Bytes bytes = Bytes.valueOf(value, unit);

        assertThat(bytes.toString())
            .isNotBlank()
            .isEqualTo(String.format("%.2f %s", value, unit));
    }

    @Test
    @DisplayName("it should be serializable")
    public void test_serialize() throws IOException, ClassNotFoundException {
        BigDecimal value = new BigDecimal("0.000000000000000000000000001");
        ByteUnit unit = ByteUnits.SI.KILOBYTE;

        Bytes bytes1 = Bytes.valueOf(value, unit);
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        ObjectOutputStream objectOutput = new ObjectOutputStream(byteOutput);
        objectOutput.writeObject(bytes1);
        objectOutput.close();
        byteOutput.close();

        byte[] data = byteOutput.toByteArray();

        ByteArrayInputStream byteInput = new ByteArrayInputStream(data);
        ObjectInputStream objectInput = new ObjectInputStream(byteInput);
        Bytes bytes2 = (Bytes) objectInput.readObject();
        objectInput.close();
        byteInput.close();

        assertThat(bytes2)
            .isNotNull()
            .isEqualTo(bytes1);

        assertThat(bytes2.getValue())
            .isNotNull()
            .isEqualTo(value);

        assertThat(bytes2.getUnit())
            .isNotNull()
            .isEqualTo(unit);
    }
}
