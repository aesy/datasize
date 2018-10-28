package io.aesy.datasize.convert;

import io.aesy.datasize.DataSize;
import io.aesy.datasize.DataUnit;

import java.math.BigDecimal;

/**
 * A {@code DataSizeConverter} converts {@code DataSize} objects to other {@code DataSize} objects.
 */
public class SmartNaturalDataSizeConverter implements DataSizeConverter {
    private static final BigDecimal EIGHT = BigDecimal.valueOf(8L);
    private static final BigDecimal ONE_THOUSAND = BigDecimal.valueOf(1000L);

    /**
     * Converts a {@code DataSize} object to another {@code DataSize} object.
     *
     * @param dataSize The DataSize to convert
     * @return The converted {@code DataSize} object
     * @throws IllegalArgumentException If the {@code DataSize} object is null
     */
    @Override
    public DataSize convert(DataSize dataSize) {
        BigDecimal originalValue = dataSize.getValue();
        DataUnit originalUnit = dataSize.getUnit();

        return dataSize;

        // if (!ByteUnit.ALL.units().contains(originalUnit)) {
        //     Unknown unit, use original
        //     return dataSize;
        // }
        //
        // DataSize inBits = ByteUnit.BIT.convert(bytes);
        //
        // if (inBits.getValue().compareTo(EIGHT) < 0) {
        //     // Values below this limit are best viewed as bits
        //     return inBits;
        // }
        //
        // DataSize inBytes = ByteUnit.BYTE.convert(bytes);
        //
        // if (inBytes.getValue().compareTo(ONE_THOUSAND) < 0) {
        //     // Values below this limit are best viewed as bytes
        //     return inBytes;
        // }
        //
        // ArrayList<ByteUnit> units = new ArrayList<>();
        //
        // if (ByteUnit.SI.has(originalUnit)) {
        //     // Prefer SI units
        //     units.addAll(ByteUnit.SI.units());
        //     units.addAll(ByteUnit.IEC.units());
        // } else if (ByteUnit.IEC.has(originalUnit)) {
        //     // Prefer IEC units
        //     units.addAll(ByteUnit.IEC.units());
        //     units.addAll(ByteUnit.SI.units());
        // } else if (ByteUnit.JEDEC.has(originalUnit)) {
        //     // Only use JEDEC units if the input object does
        //     units.addAll(ByteUnit.JEDEC.units());
        // } else {
        //     // Convert to anything, but prefer SI
        //     units.addAll(ByteUnit.SI.units());
        //     units.addAll(ByteUnit.IEC.units());
        // }
        //
        // DataSize best = bytes;
        //
        // for (ByteUnit unit : units) {
        //     DataSize newBytes = bytes.toUnit(unit);
        //     BigDecimal value1 = newBytes.getValue();
        //     BigDecimal value2 = best.getValue();
        //
        //     boolean isLessThanOne = value1.compareTo(BigDecimal.ONE) < 0;
        //
        //     if (isLessThanOne) {
        //         // Values less than 1.0 not determined readable, skip
        //         continue;
        //     }
        //
        //     boolean isBetterValue = value1.compareTo(value2) < 0;
        //     boolean hasBetterScale = value1.toPlainString().length() <
        //                              value2.toPlainString().length();
        //     boolean isLessThanOneThousand = best.getValue().compareTo(ONE_THOUSAND) < 0;
        //
        //     if (isBetterValue || (isLessThanOneThousand && hasBetterScale)) {
        //         best = newBytes;
        //     }
        // }
        //
        // return best;
    }
}
