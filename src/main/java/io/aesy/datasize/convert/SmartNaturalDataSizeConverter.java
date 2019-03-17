package io.aesy.datasize.convert;

import io.aesy.datasize.BitUnit;
import io.aesy.datasize.ByteUnit;
import io.aesy.datasize.DataSize;
import io.aesy.datasize.DataUnit;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

/**
 * A {@code DataSizeConverter} converts {@code DataSize} objects to other {@code DataSize} objects.
 */
public class SmartNaturalDataSizeConverter implements DataSizeConverter {
    private static final BigDecimal EIGHT;
    private static final BigDecimal ONE_THOUSAND;
    private static final Collection<DataUnit> ALL_KNOWN_UNITS;

    static {
        EIGHT = BigDecimal.valueOf(8);
        ONE_THOUSAND = BigDecimal.valueOf(1000);

        Collection<DataUnit> units = new ArrayList<>();
        units.addAll(BitUnit.values());
        units.addAll(ByteUnit.values());
        ALL_KNOWN_UNITS = units;
    }

    /**
     * Converts a {@code DataSize} object to another {@code DataSize} object.
     *
     * @param dataSize The DataSize to convert
     * @return The converted {@code DataSize} object
     * @throws IllegalArgumentException If the {@code DataSize} object is null
     */
    @Override
    public DataSize convert(DataSize dataSize) {
        DataUnit originalUnit = dataSize.getUnit();

        if (!ALL_KNOWN_UNITS.contains(originalUnit)) {
            // Unknown unit, use original
            return dataSize;
        }

        DataSize inBits = dataSize.toUnit(BitUnit.BIT);

        if (inBits.getValue().compareTo(EIGHT) < 0) {
            // Values below this limit are best viewed as bits
            return inBits;
        }

        DataSize inDataSize = dataSize.toUnit(ByteUnit.BYTE);

        if (inDataSize.getValue().compareTo(ONE_THOUSAND) < 0) {
            // Values below this limit are best viewed as bytes
            return inDataSize;
        }

        DataSize best = dataSize;

        for (DataUnit unit : ALL_KNOWN_UNITS) {
            DataSize newBytes = dataSize.toUnit(unit);
            BigDecimal value1 = newBytes.getValue();
            BigDecimal value2 = best.getValue();

            boolean isLessThanOne = value1.compareTo(BigDecimal.ONE) < 0;

            if (isLessThanOne) {
                // Values less than 1.0 not determined readable, skip
                continue;
            }

            int comparison = value1.compareTo(value2);
            boolean isBetterValue = comparison < 0;
            boolean isSameValue = comparison == 0;
            boolean isSameUnitType = unit.getClass().equals(originalUnit.getClass());
            boolean hasBetterScale = value1.toPlainString().length() < value2.toPlainString().length();
            boolean isLessThanOneThousand = value1.compareTo(ONE_THOUSAND) < 0;

            if ((isBetterValue || (isSameValue && isSameUnitType)) ||
                (isLessThanOneThousand && hasBetterScale))
            {
                best = newBytes;
            }
        }

        return best;
    }
}
