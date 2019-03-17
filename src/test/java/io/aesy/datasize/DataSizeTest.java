package io.aesy.datasize;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.stream.IntStream;

public class DataSizeTest implements WithAssertions {
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
        DataSize dataSize = DataSize.of(value, unit);

        dataSize.add(DataSize.of(Math.PI, ByteUnit.SI.KILOBYTE))
             .subtract(DataSize.of(Math.E, ByteUnit.JEDEC.GIGABYTE))
             .increment()
             .increment()
             .decrement()
             .toNaturalUnit()
             .toUnit(ByteUnit.SI.YOTTABYTE);

        assertThat(dataSize.getValue())
            .isNotNull()
            .isEqualTo(value);

        assertThat(dataSize.getUnit())
            .isNotNull()
            .isEqualTo(unit);
    }

    @Disabled("to be implemented in future release")
    @Test
    @DisplayName("it should be able to parse its own string representation")
    public void test_parse() throws ParseException {
        DataSize dataSize = DataSize.of(1.23, ByteUnit.BYTE);

        assertThat(DataSize.parse(dataSize.toString()))
            .isEqualTo(dataSize);
    }

    @Test
    @DisplayName("it should hold a value and a unit")
    public void test_value_unit() {
        BigDecimal value = BigDecimal.ZERO;
        DataUnit unit = ByteUnit.BYTE;
        DataSize dataSize = DataSize.of(value, unit);

        assertThat(dataSize.getValue())
            .isNotNull()
            .isEqualTo(value);

        assertThat(dataSize.getUnit())
            .isNotNull()
            .isEqualTo(unit);
    }

    @Test
    @DisplayName("it should be able to pick the greater of two objects")
    public void test_max() {
        DataSize dataSize1 = DataSize.of(1024, ByteUnit.BYTE);
        DataSize dataSize2 = DataSize.of(1, ByteUnit.SI.KILOBYTE);

        DataSize max1 = DataSize.max(dataSize1, dataSize2);
        DataSize max2 = DataSize.max(dataSize2, dataSize1);

        assertThat(max1)
            .isNotNull()
            .isSameAs(dataSize1);

        assertThat(max2)
            .isNotNull()
            .isSameAs(dataSize1);
    }

    @Test
    @DisplayName("it should be able to pick the lesser of two objects")
    public void test_min() {
        DataSize dataSize1 = DataSize.of(1024, ByteUnit.BYTE);
        DataSize dataSize2 = DataSize.of(1, ByteUnit.SI.KILOBYTE);

        DataSize min1 = DataSize.min(dataSize1, dataSize2);
        DataSize min2 = DataSize.min(dataSize2, dataSize1);

        assertThat(min1)
            .isNotNull()
            .isSameAs(dataSize2);

        assertThat(min2)
            .isNotNull()
            .isSameAs(dataSize2);
    }

    @Test
    @DisplayName("it should have simple math functions such as add")
    public void test_add() {
        DataSize dataSize1 = DataSize.of(1024, ByteUnit.BYTE);
        DataSize dataSize2 = DataSize.of(1, ByteUnit.IEC.KIBIBYTE);

        DataSize sum = dataSize1.add(dataSize2);

        assertThat(sum)
            .isNotNull()
            .isEqualTo(DataSize.of(2, ByteUnit.IEC.KIBIBYTE));
    }

    @Test
    @DisplayName("it should have simple math functions such as subtract")
    public void test_subtract() {
        DataSize dataSize1 = DataSize.of(1024, ByteUnit.BYTE);
        DataSize dataSize2 = DataSize.of(1, ByteUnit.SI.KILOBYTE);

        DataSize diff = dataSize1.subtract(dataSize2);

        assertThat(diff)
            .isNotNull()
            .isEqualTo(DataSize.of(24, ByteUnit.BYTE));
    }

    @Test
    @DisplayName("it should never be less than zero")
    public void test_subtract_lessThanZero() {
        DataSize dataSize1 = DataSize.of(0, ByteUnit.BYTE);
        DataSize dataSize2 = DataSize.of(1, ByteUnit.BYTE);

        DataSize diff1 = dataSize1.subtract(dataSize2);
        DataSize diff2 = dataSize1.decrement();

        assertThat(diff1)
            .isNotNull()
            .isEqualTo(DataSize.of(0, ByteUnit.BYTE))
            .isEqualTo(diff2);
    }

    @Test
    @DisplayName("it should increment")
    public void test_increment() {
        for (DataUnit unit : ALL_UNITS) {
            IntStream.range(0, 10)
                     .forEach(i -> {
                         DataSize dataSize = DataSize.of(i, unit).increment();

                         assertThat(dataSize.getValue())
                             .isNotNull()
                             .isEqualByComparingTo(BigDecimal.valueOf(i + 1));

                         assertThat(dataSize.getUnit())
                             .isNotNull()
                             .isEqualTo(unit);
                     });
        }
    }

    @Test
    @DisplayName("it should decrement")
    public void test_decrement() {
        for (DataUnit unit : ALL_UNITS) {
            IntStream.range(0, 10)
                     .forEach(i -> {
                         DataSize dataSize = DataSize.of(i, unit).decrement();

                         assertThat(dataSize.getValue())
                             .isNotNull()
                             .isEqualByComparingTo(BigDecimal.valueOf(Math.max(0, i - 1)));

                         assertThat(dataSize.getUnit())
                             .isNotNull()
                             .isEqualTo(unit);
                     });
        }
    }

    @Test
    @DisplayName("it should be convertable to other units")
    public void test_toUnit() {
        DataSize dataSize = DataSize.of(1, ByteUnit.BYTE);

        for (DataUnit unit : ALL_UNITS) {
            DataSize converted = dataSize.toUnit(unit);

            assertThat(converted.getUnit())
                .isNotNull()
                .isEqualTo(unit);
        }
    }

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
        DataSize dataSize = DataSize.of(value, unit);

        assertThat(dataSize.toString())
            .isNotBlank()
            .isEqualTo(String.format("%.2f %s", value, unit));
    }

    @Disabled("to be implemented in future release")
    @Test
    @DisplayName("it should be serializable")
    public void test_serialize() throws IOException, ClassNotFoundException {
        BigDecimal value = new BigDecimal("0.000000000000000000000000001");
        DataUnit unit = ByteUnit.JEDEC.MEGABYTE;

        DataSize dataSize1 = DataSize.of(value, unit);
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(byteOutput);
        objectOutput.writeObject(dataSize1);
        objectOutput.close();
        byteOutput.close();

        byte[] data = byteOutput.toByteArray();

        ByteArrayInputStream byteInput = new ByteArrayInputStream(data);
        ObjectInput objectInput = new ObjectInputStream(byteInput);
        DataSize dataSize2 = (DataSize) objectInput.readObject();
        objectInput.close();
        byteInput.close();

        assertThat(dataSize2)
            .isNotNull()
            .isEqualTo(dataSize1);

        assertThat(dataSize2.getValue())
            .isNotNull()
            .isEqualTo(value);

        assertThat(dataSize2.getUnit())
            .isNotNull()
            .isEqualTo(unit);
    }
}
