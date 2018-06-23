package org.aesy.bytes.convert;

import org.aesy.bytes.ByteUnit;
import org.aesy.bytes.ByteUnits;
import org.aesy.bytes.Bytes;

import java.math.BigDecimal;
import java.util.ArrayList;

public class SmartNaturalBytesConverter implements BytesConverter {
    private static final BigDecimal ONE_THOUSAND = BigDecimal.valueOf(1000);

    @Override
    public Bytes convert(Bytes bytes) {
        BigDecimal originalValue = bytes.getValue();
        ByteUnit originalUnit = bytes.getUnit();
        boolean isLessThanOneThousandBytes = ByteUnits.COMMON.BYTE.convert(bytes)
                                                                  .getValue().compareTo(ONE_THOUSAND) < 0;

        if (isLessThanOneThousandBytes) {
            // Values less than 1000 bytes are best viewed as bytes
            return bytes;
        }

        ArrayList<ByteUnit> units = new ArrayList<>();

        if (ByteUnits.SI.has(originalUnit)) {
            // Prefer SI units
            units.addAll(ByteUnits.SI.units());
            units.addAll(ByteUnits.IEC.units());
        } else if (ByteUnits.IEC.has(originalUnit)) {
            // Prefer IEC units
            units.addAll(ByteUnits.IEC.units());
            units.addAll(ByteUnits.SI.units());
        } else if (ByteUnits.JEDEC.has(originalUnit)) {
            // Only use JEDEC units if the original does
            units.addAll(ByteUnits.JEDEC.units());
        } else {
            // Unknown unit type, use original
            return bytes;
        }

        Bytes best = bytes;

        for (ByteUnit unit : units) {
            Bytes newBytes = unit.convert(bytes);
            BigDecimal value1 = newBytes.getValue().stripTrailingZeros();
            BigDecimal value2 = best.getValue().stripTrailingZeros();

            boolean isLessThanOne = value1.compareTo(BigDecimal.ONE) < 0;

            if (isLessThanOne) {
                // Values less than 1.0 not determined readable, skip
                continue;
            }

            boolean isBetterValue = value1.compareTo(value2) < 0;
            boolean hasBetterScale = value1.scale() < value2.scale();
            boolean isLessThanOneThousand = best.getValue().compareTo(ONE_THOUSAND) < 0;

            if (isBetterValue || (isLessThanOneThousand && hasBetterScale)) {
                best = newBytes;
            }
        }

        return best;
    }
}
