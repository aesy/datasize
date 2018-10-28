package io.aesy.datasize.convert;

import io.aesy.datasize.DataSize;
import io.aesy.datasize.DataUnit;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;

public class CachedDataSizeUnitConverter implements DataSizeConverter {
    private final Map<Conversion, BigDecimal> cache;
    private final DataUnit toUnit;

    public CachedDataSizeUnitConverter(DataUnit unit) {
        this.cache = new HashMap<>();
        this.toUnit = unit;
    }

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

        Conversion(DataUnit from, DataUnit to) {
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