package io.aesy.bytes.convert;

import io.aesy.bytes.ByteUnit;
import io.aesy.bytes.ByteUnits;
import io.aesy.bytes.Bytes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * A {@code BytesConverter} converts {@code Bytes} objects to other {@code Bytes} objects.
 */
public class SimpleNaturalBytesConverter implements BytesConverter {
    private static final BigDecimal EIGHT = BigDecimal.valueOf(8);
    private static final BigDecimal ONE_THOUSAND = BigDecimal.valueOf(1000);

    /**
     * Converts a {@code Bytes} object to another {@code Bytes} object.
     *
     * @param bytes The Bytes to convert
     * @return The converted {@code Bytes} object
     * @throws IllegalArgumentException If the bytes object is null
     */
    @Override
    public Bytes convert(Bytes bytes) {
        BigDecimal originalValue = bytes.getValue();
        ByteUnit originalUnit = bytes.getUnit();

        if (!ByteUnits.ALL.units().contains(originalUnit)) {
            // Unknown unit, use original
            return bytes;
        }

        Bytes inBits = ByteUnits.BIT.convert(bytes);

        if (inBits.getValue().compareTo(EIGHT) < 0) {
            // Values below this limit are best viewed as bits
            return inBits;
        }

        Bytes inBytes = ByteUnits.BYTE.convert(bytes);

        if (inBytes.getValue().compareTo(ONE_THOUSAND) < 0) {
            // Values below this limit are best viewed as bytes
            return inBytes;
        }

        List<ByteUnit> units = new ArrayList<>();

        // Only convert to same unit type as input object
        if (ByteUnits.SI.has(originalUnit)) {
            units.addAll(ByteUnits.SI.units());
        } else if (ByteUnits.IEC.has(originalUnit)) {
            units.addAll(ByteUnits.IEC.units());
        } else if (ByteUnits.JEDEC.has(originalUnit)) {
            units.addAll(ByteUnits.JEDEC.units());
        } else {
            // Unless in bits/bytes, then convert to anything
            units.addAll(ByteUnits.SI.units());
            units.addAll(ByteUnits.IEC.units());
        }

        Bytes best = bytes;

        for (ByteUnit unit : units) {
            Bytes newBytes = bytes.toUnit(unit);
            BigDecimal value1 = newBytes.getValue();
            BigDecimal value2 = best.getValue();

            boolean isLessThanOne = value1.compareTo(BigDecimal.ONE) < 0;

            if (isLessThanOne) {
                // Values less than 1.0 not determined readable, skip
                continue;
            }

            boolean isBetterValue = value1.compareTo(value2) < 0;

            if (isBetterValue) {
                best = newBytes;
            }
        }

        return best;
    }
}
