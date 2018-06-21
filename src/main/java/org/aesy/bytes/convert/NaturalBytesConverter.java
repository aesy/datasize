package org.aesy.bytes.convert;

import org.aesy.bytes.ByteUnit;
import org.aesy.bytes.Bytes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NaturalBytesConverter implements BytesConverter {
    private static final List<ByteUnit> SI = Arrays.asList(ByteUnit.SI.values());
    private static final List<ByteUnit> IEC = Arrays.asList(ByteUnit.IEC.values());
    private static final List<ByteUnit> JEDEC = Arrays.asList(ByteUnit.JEDEC.values());
    private static final BigDecimal ONE_THOUSAND = BigDecimal.valueOf(1000);

    @Override
    public Bytes convert(Bytes bytes) {
        BigDecimal originalValue = bytes.getValue();
        ByteUnit originalUnit = bytes.getUnit();
        boolean isLessThanOneThousandBytes = ByteUnit.BYTE.convert(bytes)
                                                          .getValue().compareTo(ONE_THOUSAND) < 0;

        if (isLessThanOneThousandBytes) {
            // Values less than 1000 bytes are best viewed as bytes
            return bytes;
        }

        ArrayList<ByteUnit> units = new ArrayList<>();

        if (SI.contains(originalUnit)) {
            // Prefer SI units
            units.addAll(SI);
            units.addAll(IEC);
        } else if (IEC.contains(originalUnit)) {
            // Prefer IEC units
            units.addAll(IEC);
            units.addAll(SI);
        } else if (JEDEC.contains(originalUnit)) {
            // Only use JEDEC units if the original does
            units.addAll(JEDEC);
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
