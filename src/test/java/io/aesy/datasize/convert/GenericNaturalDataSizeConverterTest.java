package io.aesy.datasize.convert;

import io.aesy.datasize.BitUnit;
import io.aesy.datasize.ByteUnit;
import io.aesy.datasize.DataSize;
import io.aesy.datasize.DataUnit;
import io.aesy.datasize.factory.NaturalDataSizeConverterFactory;
import io.aesy.datasize.provider.NaturalDataSizeConverterFactoryProvider;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.math.BigDecimal;
import java.util.*;

/**
 * Tests all identical properties of all {@code DataSizeConverter} implementations that convert to a
 * more natural representation unit.
 */
public class GenericNaturalDataSizeConverterTest implements WithAssertions {
    private static final Collection<DataUnit> ALL_UNITS;

    static {
        ALL_UNITS = new ArrayList<>();
        ALL_UNITS.add(ByteUnit.BYTE);
        ALL_UNITS.add(BitUnit.BIT);
        ALL_UNITS.addAll(ByteUnit.SI.values());
        ALL_UNITS.addAll(ByteUnit.IEC.values());
        ALL_UNITS.addAll(ByteUnit.JEDEC.values());
        ALL_UNITS.addAll(BitUnit.SI.values());
        ALL_UNITS.addAll(BitUnit.IEC.values());
        ALL_UNITS.addAll(BitUnit.JEDEC.values());
    }

    @BeforeEach
    public void setup() {
        Locale.setDefault(Locale.US);
    }

    @ParameterizedTest
    @ArgumentsSource(NaturalDataSizeConverterFactoryProvider.class)
    @DisplayName("it should convert to bit if below 8 bits")
    public void test_bits(NaturalDataSizeConverterFactory converterFactory) {
        DataSizeConverter converter = converterFactory.create();
        DataSize noBits = DataSize.of(0, BitUnit.BIT);
        DataSize sevenBits = DataSize.of(7, BitUnit.BIT);

        for (DataUnit unit : ALL_UNITS) {
            DataSize result1 = converter.convert(noBits.toUnit(unit));
            DataSize result2 = converter.convert(sevenBits.toUnit(unit));

            assertThat(result1)
                .isNotNull()
                .isEqualTo(noBits);

            assertThat(result2)
                .isNotNull()
                .isEqualTo(sevenBits);

            assertThat(result1.getUnit())
                .isNotNull()
                .isEqualTo(BitUnit.BIT)
                .isEqualTo(result2.getUnit());
        }
    }

    @ParameterizedTest
    @ArgumentsSource(NaturalDataSizeConverterFactoryProvider.class)
    @DisplayName("it should convert to byte if below 1000 bytes")
    public void test_bytes(NaturalDataSizeConverterFactory converterFactory) {
        DataSizeConverter converter = converterFactory.create();
        DataSize noBytes = DataSize.of(8, ByteUnit.BYTE);
        DataSize belowThousandBytes = DataSize.of(999, ByteUnit.BYTE);

        Collection<DataUnit> units = new ArrayList<>(ALL_UNITS);
        units.remove(BitUnit.BIT);

        for (DataUnit unit : units) {
            DataSize result1 = converter.convert(noBytes.toUnit(unit));
            DataSize result2 = converter.convert(belowThousandBytes.toUnit(unit));

            assertThat(result1)
                .isNotNull()
                .isEqualTo(noBytes);

            assertThat(result2)
                .isNotNull()
                .isEqualTo(belowThousandBytes);

            assertThat(result1.getUnit())
                .isNotNull()
                .isEqualTo(ByteUnit.BYTE)
                .isEqualTo(result2.getUnit());
        }
    }

    @ParameterizedTest
    @ArgumentsSource(NaturalDataSizeConverterFactoryProvider.class)
    @DisplayName("it should convert to any unit if it's in bits or bytes and over 1000 bytes, " +
                 "and prefer the value closest and greater than 1.0")
    public void test_any_unit(NaturalDataSizeConverterFactory converterFactory) {
        DataSizeConverter converter = converterFactory.create();
        Map<DataSize, DataUnit> expected = new IdentityHashMap<>();
        expected.put(DataSize.of(7, BitUnit.BIT), BitUnit.BIT);
        expected.put(DataSize.of(8, BitUnit.BIT), ByteUnit.BYTE);
        expected.put(DataSize.of(1000, ByteUnit.BYTE), ByteUnit.SI.KILOBYTE);
        expected.put(DataSize.of(1024, ByteUnit.BYTE), ByteUnit.IEC.KIBIBYTE);
        expected.put(DataSize.of(1025, ByteUnit.BYTE), ByteUnit.IEC.KIBIBYTE);
        expected.put(DataSize.of(new BigDecimal("1000000000"), ByteUnit.BYTE), ByteUnit.SI.GIGABYTE);

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

    @ParameterizedTest
    @ArgumentsSource(NaturalDataSizeConverterFactoryProvider.class)
    @DisplayName("it should return the input object if unit is of unknown type")
    public void test_unknown_unit(NaturalDataSizeConverterFactory converterFactory) {
        DataSizeConverter converter = converterFactory.create();
        DataUnit unknownUnit = new DataUnit() {
            @Override
            public String getAbbreviation() {
                return "U";
            }

            @Override
            public String getName() {
                return "unknown";
            }

            @Override
            public BigDecimal bytes() {
                return BigDecimal.ZERO;
            }
        };

        DataSize bytes = DataSize.of(1, unknownUnit);
        DataSize result = converter.convert(bytes);

        assertThat(result)
            .isNotNull()
            .isEqualTo(result)
            .isSameAs(bytes);
    }
}
