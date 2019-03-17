package io.aesy.datasize.convert;

import io.aesy.datasize.BitUnit;
import io.aesy.datasize.ByteUnit;
import io.aesy.datasize.DataSize;
import io.aesy.datasize.DataUnit;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * Tests properties unique to {@code SmartNaturalDataSizeConverter}.
 */
public class SmartNaturalDataSizeConverterTest implements WithAssertions {
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
    @DisplayName(
        "it should convert to smallest value possible above 1 using the same unit as origin " +
        "object, with the exception for when its below 1000 and can be represented with less " +
        "digits in another unit")
    public void test_convert() {
        DataSizeConverter converter = new SmartNaturalDataSizeConverter();
        Map<DataSize, DataUnit> expected = new IdentityHashMap<>();
        expected.put(DataSize.of(1000, ByteUnit.SI.KILOBYTE), ByteUnit.SI.MEGABYTE);
        expected.put(DataSize.of(1000, ByteUnit.SI.MEGABYTE), ByteUnit.SI.GIGABYTE);
        expected.put(DataSize.of(1000, ByteUnit.SI.GIGABYTE), ByteUnit.SI.TERABYTE);
        expected.put(DataSize.of(1000, ByteUnit.SI.TERABYTE), ByteUnit.SI.PETABYTE);
        expected.put(DataSize.of(1000, ByteUnit.SI.PETABYTE), ByteUnit.SI.EXABYTE);
        expected.put(DataSize.of(1000, ByteUnit.SI.EXABYTE), ByteUnit.SI.ZETTABYTE);
        expected.put(DataSize.of(1000, ByteUnit.SI.ZETTABYTE), ByteUnit.SI.YOTTABYTE);
        expected.put(DataSize.of(1024, ByteUnit.IEC.KIBIBYTE), ByteUnit.IEC.MEBIBYTE);
        expected.put(DataSize.of(1024, ByteUnit.IEC.MEBIBYTE), ByteUnit.IEC.GIBIBYTE);
        expected.put(DataSize.of(1024, ByteUnit.IEC.GIBIBYTE), ByteUnit.IEC.TEBIBYTE);
        expected.put(DataSize.of(1024, ByteUnit.IEC.TEBIBYTE), ByteUnit.IEC.PEBIBYTE);
        expected.put(DataSize.of(1024, ByteUnit.IEC.PEBIBYTE), ByteUnit.IEC.EXBIBYTE);
        expected.put(DataSize.of(1024, ByteUnit.IEC.EXBIBYTE), ByteUnit.IEC.ZEBIBYTE);
        expected.put(DataSize.of(1024, ByteUnit.IEC.ZEBIBYTE), ByteUnit.IEC.YOBIBYTE);
        expected.put(DataSize.of(1024, ByteUnit.JEDEC.KILOBYTE), ByteUnit.JEDEC.MEGABYTE);
        expected.put(DataSize.of(1024, ByteUnit.JEDEC.MEGABYTE), ByteUnit.JEDEC.GIGABYTE);

        for (Map.Entry<DataSize, DataUnit> entry : expected.entrySet()) {
            DataSize bytes = entry.getKey();
            DataUnit unit = entry.getValue();
            DataSize converted = converter.convert(bytes);

            assertThat(converted)
                .isNotNull();

            assertThat(converted.getUnit())
                .isNotNull()
                .isEqualTo(unit);
        }
    }
}
