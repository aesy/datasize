package io.aesy.bytes.convert;

import io.aesy.bytes.ByteUnit;
import io.aesy.bytes.ByteUnits;
import io.aesy.bytes.Bytes;

import java.math.BigDecimal;
import java.util.ArrayList;

public class SmartNaturalBytesConverter implements BytesConverter {
    private static final BigDecimal SIXTEEN = BigDecimal.valueOf(16);
    private static final BigDecimal ONE_THOUSAND = BigDecimal.valueOf(1000);

    @Override
    public Bytes convert(Bytes bytes) {
        BigDecimal originalValue = bytes.getValue();
        ByteUnit originalUnit = bytes.getUnit();

        Bytes inBytes = ByteUnits.BYTE.convert(bytes);

        if (inBytes.getValue().compareTo(ONE_THOUSAND) < 0) {
            // Values below this limit are best viewed as bytes
            return inBytes;
        }

        Bytes inBits = ByteUnits.BIT.convert(inBytes);

        if (inBits.getValue().compareTo(SIXTEEN) < 0) {
            // Values below this limit are best viewed as bits
            return inBits;
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
            // Only use JEDEC units if the input object does
            units.addAll(ByteUnits.JEDEC.units());
        } else {
            // Convert to anything, but prefer SI
            units.addAll(ByteUnits.SI.units());
            units.addAll(ByteUnits.IEC.units());
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
