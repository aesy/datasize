package io.aesy.bytes.convert;

import io.aesy.bytes.ByteUnit;
import io.aesy.bytes.ByteUnits;
import io.aesy.bytes.Bytes;
import io.aesy.bytes.factory.NaturalBytesConverterFactory;
import io.aesy.bytes.provider.NaturalBytesConverterFactoryProvider;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.math.BigDecimal;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * Tests all identical properties of all {@code BytesConverter} implementations that convert to a
 * more natural representation unit.
 */
public class GenericNaturalBytesConverterTest implements WithAssertions {
    @ParameterizedTest
    @ArgumentsSource(NaturalBytesConverterFactoryProvider.class)
    @DisplayName("it should convert to bit if below 8 bits")
    public void test_bits(NaturalBytesConverterFactory converterFactory) {
        BytesConverter converter = converterFactory.create();
        Bytes noBits = Bytes.valueOf(0, ByteUnits.BIT);
        Bytes sevenBits = Bytes.valueOf(7, ByteUnits.BIT);

        for (ByteUnit unit : ByteUnits.ALL.units()) {
            Bytes result1 = converter.convert(noBits.toUnit(unit));
            Bytes result2 = converter.convert(sevenBits.toUnit(unit));

            assertThat(result1)
                .isNotNull()
                .isEqualTo(noBits);

            assertThat(result2)
                .isNotNull()
                .isEqualTo(sevenBits);

            assertThat(result1.getUnit())
                .isNotNull()
                .isEqualTo(ByteUnits.BIT)
                .isEqualTo(result2.getUnit());
        }
    }

    @ParameterizedTest
    @ArgumentsSource(NaturalBytesConverterFactoryProvider.class)
    @DisplayName("it should convert to byte if below 1000 bytes")
    public void test_bytes(NaturalBytesConverterFactory converterFactory) {
        BytesConverter converter = converterFactory.create();
        Bytes noBytes = Bytes.valueOf(8, ByteUnits.BYTE);
        Bytes belowThousandBytes = Bytes.valueOf(999, ByteUnits.BYTE);

        List<ByteUnit> units = ByteUnits.ALL.units();
        units.remove(ByteUnits.BIT);

        for (ByteUnit unit : units) {
            Bytes result1 = converter.convert(noBytes.toUnit(unit));
            Bytes result2 = converter.convert(belowThousandBytes.toUnit(unit));

            assertThat(result1)
                .isNotNull()
                .isEqualTo(noBytes);

            assertThat(result2)
                .isNotNull()
                .isEqualTo(belowThousandBytes);

            assertThat(result1.getUnit())
                .isNotNull()
                .isEqualTo(ByteUnits.BYTE)
                .isEqualTo(result2.getUnit());
        }
    }

    @ParameterizedTest
    @ArgumentsSource(NaturalBytesConverterFactoryProvider.class)
    @DisplayName("it should convert to any unit if it's in bits or bytes and over 1000 bytes, " +
                 "and prefer the value closest and greater than 1.0")
    public void test_any_unit(NaturalBytesConverterFactory converterFactory) {
        BytesConverter converter = converterFactory.create();
        Map<Bytes, ByteUnit> expected = new IdentityHashMap<>();
        expected.put(Bytes.valueOf(7, ByteUnits.BIT), ByteUnits.BIT);
        expected.put(Bytes.valueOf(8, ByteUnits.BIT), ByteUnits.BYTE);
        expected.put(Bytes.valueOf(1000, ByteUnits.BYTE), ByteUnits.SI.KILOBYTE);
        expected.put(Bytes.valueOf(1024, ByteUnits.BYTE), ByteUnits.IEC.KIBIBYTE);
        expected.put(Bytes.valueOf(1025, ByteUnits.BYTE), ByteUnits.IEC.KIBIBYTE);
        expected.put(Bytes.valueOf(new BigDecimal("1000000000"), ByteUnits.BYTE), ByteUnits.SI.GIGABYTE);

        for (Map.Entry<Bytes, ByteUnit> entry : expected.entrySet()) {
            Bytes bytes = entry.getKey();
            ByteUnit unit = entry.getValue();
            Bytes converted = converter.convert(bytes);

            assertThat(converted)
                .isNotNull();

            assertThat(converted.getUnit())
                .isNotNull()
                .isEqualTo(unit);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(NaturalBytesConverterFactoryProvider.class)
    @DisplayName("it should return the input object if unit is of unknown type")
    public void test_unknown_unit(NaturalBytesConverterFactory converterFactory) {
        BytesConverter converter = converterFactory.create();
        ByteUnit unknownUnit = new ByteUnit() {
            @Override
            public String getAbbreviation() {
                return "U";
            }

            @Override
            public String getName() {
                return "unknown";
            }

            @Override
            public int getBase() {
                return 0;
            }

            @Override
            public int getExponent() {
                return 0;
            }

            @Override
            public Standard getStandard() {
                return null;
            }

            @Override
            public Bytes convert(Bytes value) {
                throw new IllegalStateException();
            }
        };

        Bytes bytes = Bytes.valueOf(1, unknownUnit);
        Bytes result = converter.convert(bytes);

        assertThat(result)
            .isNotNull()
            .isEqualTo(result)
            .isSameAs(bytes);
    }
}
