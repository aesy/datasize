package io.aesy.datasize;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.*;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.Locale;

public class DataSizeTest implements WithAssertions {
    @BeforeEach
    public void setup() {
        Locale.setDefault(Locale.US);
    }

    @Test
    @DisplayName("it should throw IllegalArgumentException if passed null values")
    public void test_npe() {
        assertThatThrownBy(() -> DataSize.of((BigInteger) null, ByteUnit.BYTE))
            .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> DataSize.of((BigDecimal) null, ByteUnit.BYTE))
            .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> DataSize.of(BigDecimal.ONE, null))
            .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> DataSize.of((BigInteger) null, null))
            .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> DataSize.of((BigDecimal) null, null))
            .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> DataSize.of(0, ByteUnit.BYTE).compareTo(null))
            .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> DataSize.parse(null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("it should throw IllegalArgumentException if passed value less than zero")
    public void test_iae_lessThanZero() {
        assertThatThrownBy(() -> DataSize.of(BigInteger.valueOf(-1), ByteUnit.BYTE))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("it should be immutable")
    public void test_immutable() {
        BigDecimal value = BigDecimal.valueOf(1.23);
        DataUnit unit = ByteUnit.BYTE;
        DataSize bytes = DataSize.of(value, unit);

        bytes.add(DataSize.of(Math.PI, ByteUnit.SI.KILOBYTE))
             .subtract(DataSize.of(Math.E, ByteUnit.JEDEC.GIGABYTE))
             .increment()
             .increment()
             .decrement()
             .toNaturalUnit()
             .toUnit(ByteUnit.SI.YOTTABYTE);

        assertThat(bytes.getValue())
            .isNotNull()
            .isEqualTo(value);

        assertThat(bytes.getUnit())
            .isNotNull()
            .isEqualTo(unit);
    }

    @Disabled("to be implemented in future release")
    @Test
    @DisplayName("it should be able to parse its own string representation")
    public void test_parse() throws ParseException {
        DataSize bytes = DataSize.of(1.23, ByteUnit.BYTE);

        assertThat(DataSize.parse(bytes.toString()))
            .isEqualTo(bytes);
    }

    @Test
    @DisplayName("it should hold a value and a unit")
    public void test_value_unit() {
        BigDecimal value = BigDecimal.ZERO;
        DataUnit unit = ByteUnit.BYTE;
        DataSize bytes = DataSize.of(value, unit);

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
        DataSize bytes1 = DataSize.of(1024, ByteUnit.BYTE);
        DataSize bytes2 = DataSize.of(1, ByteUnit.SI.KILOBYTE);

        DataSize max1 = DataSize.max(bytes1, bytes2);
        DataSize max2 = DataSize.max(bytes2, bytes1);

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
        DataSize bytes1 = DataSize.of(1024, ByteUnit.BYTE);
        DataSize bytes2 = DataSize.of(1, ByteUnit.SI.KILOBYTE);

        DataSize min1 = DataSize.min(bytes1, bytes2);
        DataSize min2 = DataSize.min(bytes2, bytes1);

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
        DataSize bytes1 = DataSize.of(1024, ByteUnit.BYTE);
        DataSize bytes2 = DataSize.of(1, ByteUnit.IEC.KIBIBYTE);

        DataSize sum = bytes1.add(bytes2);

        assertThat(sum)
            .isNotNull()
            .isEqualTo(DataSize.of(2, ByteUnit.IEC.KIBIBYTE));
    }

    @Test
    @DisplayName("it should have simple math functions such as subtract")
    public void test_subtract() {
        DataSize bytes1 = DataSize.of(1024, ByteUnit.BYTE);
        DataSize bytes2 = DataSize.of(1, ByteUnit.SI.KILOBYTE);

        DataSize diff = bytes1.subtract(bytes2);

        assertThat(diff)
            .isNotNull()
            .isEqualTo(DataSize.of(24, ByteUnit.BYTE));
    }

    @Test
    @DisplayName("it should never be less than zero")
    public void test_subtract_lessThanZero() {
        DataSize bytes1 = DataSize.of(0, ByteUnit.BYTE);
        DataSize bytes2 = DataSize.of(1, ByteUnit.BYTE);

        DataSize diff1 = bytes1.subtract(bytes2);
        DataSize diff2 = bytes1.decrement();

        assertThat(diff1)
            .isNotNull()
            .isEqualTo(DataSize.of(0, ByteUnit.BYTE))
            .isEqualTo(diff2);
    }

    // @Test
    // @DisplayName("it should increment")
    // public void test_increment() {
    //     for (DataUnit unit : ByteUnit.ALL.units()) {
    //         IntStream.range(0, 10)
    //                  .forEach(i -> {
    //                      DataSize bytes = DataSize.of(i, unit)
    //                                         .increment();
    //
    //                      assertThat(bytes.getValue())
    //                          .isNotNull()
    //                          .isEqualByComparingTo(BigDecimal.valueOf(i + 1));
    //
    //                      assertThat(bytes.getUnit())
    //                          .isNotNull()
    //                          .isEqualTo(unit);
    //                  });
    //     }
    // }

    // @Test
    // @DisplayName("it should decrement")
    // public void test_decrement() {
    //     for (DataUnit unit : ByteUnit.ALL.units()) {
    //         IntStream.range(0, 10)
    //                  .forEach(i -> {
    //                      DataSize bytes = DataSize.of(i, unit)
    //                                         .decrement();
    //
    //                      assertThat(bytes.getValue())
    //                          .isNotNull()
    //                          .isEqualByComparingTo(BigDecimal.valueOf(Math.max(0, i - 1)));
    //
    //                      assertThat(bytes.getUnit())
    //                          .isNotNull()
    //                          .isEqualTo(unit);
    //                  });
    //     }
    // }

    // @Test
    // @DisplayName("it should be convertable to other units")
    // public void test_toUnit() {
    //     DataSize bytes = DataSize.of(1, ByteUnit.BYTE);
    //
    //     for (DataUnit unit : ByteUnit.ALL.units()) {
    //         DataSize converted = bytes.toUnit(unit);
    //
    //         assertThat(converted.getUnit())
    //             .isNotNull()
    //             .isEqualTo(unit);
    //     }
    // }

    @Test
    @DisplayName("it should be comparable similarly to numbers and be equal if and only if their " +
                 "values as byte are equal")
    public void test_compareTo() {
        DataSize nothing = DataSize.of(0, ByteUnit.BYTE);
        DataSize bytes = DataSize.of(2048, ByteUnit.BYTE);
        DataSize kilobytes = DataSize.of(2048, ByteUnit.JEDEC.KILOBYTE);
        DataSize megabytes = DataSize.of(2, ByteUnit.JEDEC.MEGABYTE);
        DataSize gigabytes = DataSize.of(1, ByteUnit.JEDEC.GIGABYTE);

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
        DataSize nothing = DataSize.of(0, ByteUnit.BYTE);
        DataSize bytes = DataSize.of(2048, ByteUnit.BYTE);
        DataSize kilobytes = DataSize.of(2048, ByteUnit.JEDEC.KILOBYTE);
        DataSize megabytes = DataSize.of(2, ByteUnit.JEDEC.MEGABYTE);

        assertThat(kilobytes)
            .isNotNull()
            .isNotEqualTo(new Object())
            .isNotEqualTo(nothing)
            .isNotEqualTo(bytes)
            .isEqualTo(kilobytes)
            .isEqualTo(megabytes);
    }

    @Test
    @DisplayName("it should have a string representation which is nicely formatted")
    public void test_toString() {
        double value = 15.389203948;
        DataUnit unit = ByteUnit.SI.KILOBYTE;
        DataSize bytes = DataSize.of(value, unit);

        assertThat(bytes.toString())
            .isNotBlank()
            .isEqualTo(String.format("%.2f %s", value, unit));
    }

    @Disabled("to be implemented in future release")
    @Test
    @DisplayName("it should be serializable")
    public void test_serialize() throws IOException, ClassNotFoundException {
        BigDecimal value = new BigDecimal("0.000000000000000000000000001");
        DataUnit unit = ByteUnit.JEDEC.MEGABYTE;

        DataSize bytes1 = DataSize.of(value, unit);
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(byteOutput);
        objectOutput.writeObject(bytes1);
        objectOutput.close();
        byteOutput.close();

        byte[] data = byteOutput.toByteArray();

        ByteArrayInputStream byteInput = new ByteArrayInputStream(data);
        ObjectInput objectInput = new ObjectInputStream(byteInput);
        DataSize bytes2 = (DataSize) objectInput.readObject();
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
