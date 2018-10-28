package io.aesy.datasize.convert;

import io.aesy.datasize.BitUnit;
import io.aesy.datasize.ByteUnit;
import io.aesy.datasize.DataSize;
import io.aesy.datasize.DataUnit;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A {@code DataSizeConverter} converts {@code DataSize} objects to other {@code DataSize} objects.
 */
public class SimpleNaturalDataSizeConverter implements DataSizeConverter {
    private static final BigDecimal EIGHT;
    private static final BigDecimal ONE_THOUSAND;
    private static final List<DataUnit> BYTE_UNITS;
    private static final List<DataUnit> BIT_UNITS;

    static {
        EIGHT = BigDecimal.valueOf(8L);
        ONE_THOUSAND = BigDecimal.valueOf(1000L);

        BYTE_UNITS = new ArrayList<>();
        BYTE_UNITS.add(ByteUnit.BYTE);
        BYTE_UNITS.addAll(ByteUnit.SI.values());
        BYTE_UNITS.addAll(ByteUnit.IEC.values());
        BYTE_UNITS.addAll(ByteUnit.JEDEC.values());

        BIT_UNITS = new ArrayList<>();
        BIT_UNITS.add(BitUnit.BIT);
        BIT_UNITS.addAll(BitUnit.SI.values());
        BIT_UNITS.addAll(BitUnit.IEC.values());
        BIT_UNITS.addAll(BitUnit.JEDEC.values());
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

        if (!BYTE_UNITS.contains(originalUnit)) {
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

        Collection<DataUnit> units = new ArrayList<>();

        // Only convert to same unit type as input object
        if (ByteUnit.SI.values().contains(originalUnit)) {
            units.addAll(ByteUnit.SI.values());
        } else if (ByteUnit.IEC.values().contains(originalUnit)) {
            units.addAll(ByteUnit.IEC.values());
        } else if (ByteUnit.JEDEC.values().contains(originalUnit)) {
            units.addAll(ByteUnit.JEDEC.values());
        } else {
            // Unless in bits/bytes, then convert to anything
            units.addAll(ByteUnit.SI.values());
            units.addAll(ByteUnit.IEC.values());
        }

        DataSize best = dataSize;

        for (DataUnit unit : units) {
            DataSize newDataSize = dataSize.toUnit(unit);
            BigDecimal value1 = newDataSize.getValue();
            BigDecimal value2 = best.getValue();

            boolean isLessThanOne = value1.compareTo(BigDecimal.ONE) < 0;

            if (isLessThanOne) {
                // Values less than 1.0 not determined readable, skip
                continue;
            }

            boolean isBetterValue = value1.compareTo(value2) < 0;

            if (isBetterValue) {
                best = newDataSize;
            }
        }

        return best;
    }
}
