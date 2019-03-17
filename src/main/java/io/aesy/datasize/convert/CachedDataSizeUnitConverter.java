package io.aesy.datasize.convert;

import io.aesy.datasize.DataSize;
import io.aesy.datasize.DataUnit;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;

/**
 * A {@code CachedDataSizeUnitConverter} converts {@code DataSize} objects to other units.
 *
 * <p>
 * It globally caches and reuses the ratio between unit conversions for faster conversions.
 * </p>
 *
 * <blockquote>
 * Example usage:
 * <pre>{@code
 * DataSize dataSize = DataSize.of(Math.PI, ByteUnits.SI.KILOBYTE);
 * DataSize converted = new CachedDataSizeUnitConverter(ByteUnit.SI.MEGABYTE).convert(dataSize);
 *
 * assertEquals(converted.getUnit(), ByteUnit.SI.MEGABYTE);
 * }</pre>
 * </blockquote>
 */
public class CachedDataSizeUnitConverter implements DataSizeConverter {
    private static final Map<Conversion, BigDecimal> cache = new HashMap<>();

    private final DataUnit toUnit;

    /**
     * Creates a {@code CachedDataSizeUnitConverter}.
     *
     * @param unit The unit to convert to
     */
    public CachedDataSizeUnitConverter(DataUnit unit) {
        this.toUnit = unit;
    }

    /**
     * Converts a {@code DataSize} object to another unit.
     *
     * @param dataSize The {@code DataSize} object whose unit to convert
     * @return The converted {@code DataSize} object
     * @throws IllegalArgumentException If the given {@code DataSize} object is null
     */
    @Override
    public DataSize convert(DataSize dataSize) {
        BigDecimal value = dataSize.getValue();
        DataUnit fromUnit = dataSize.getUnit();

        BigDecimal newValue = getRatio(fromUnit, toUnit).multiply(value);

        return DataSize.of(newValue, toUnit);
    }

    private BigDecimal getRatio(DataUnit first, DataUnit second) {
        Conversion conversion = new Conversion(first, second);

        if (cache.containsKey(conversion)) {
            return cache.get(conversion);
        }

        BigDecimal ratio = first.bytes()
                                .divide(second.bytes(), MathContext.UNLIMITED);

        cache.put(conversion, ratio);

        return ratio;
    }

    private static final class Conversion {
        private final DataUnit from;
        private final DataUnit to;

        private Conversion(DataUnit from, DataUnit to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }

            Conversion that = (Conversion) obj;

            if (!from.equals(that.from)) {
                return false;
            }

            return to.equals(that.to);
        }

        @Override
        public int hashCode() {
            return 31 * from.hashCode() + to.hashCode();
        }
    }
}
