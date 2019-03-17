package io.aesy.datasize.convert;

import io.aesy.datasize.BitUnit;
import io.aesy.datasize.ByteUnit;
import io.aesy.datasize.DataSize;
import io.aesy.datasize.DataUnit;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A {@code SimpleNaturalDataSizeConverter} converts {@code DataSize} objects to other
 * {@code DataSize} objects with more human readable units.
 *
 * <p>
 * The resulting unit will be of the same standard, such as SI, JEDEC or IEC as this object,
 * unless this objects' unit is {@code ByteUnit.BYTE} or {@code BitUnit.BIT} by which the
 * resulting unit may be of any available {@code DataUnit} type.
 * </p>
 *
 * <blockquote>
 * Example usage:
 * <pre>{@code
 * DataSize dataSize = DataSize.of(1024, ByteUnits.BYTE);
 * String converted = new SimpleNaturalDataSizeConverter().convert(dataSize);
 *
 * assertEquals(converted.getUnit(), ByteUnit.SI.KILOBYTE");
 * }</pre>
 * </blockquote>
 */
public class SimpleNaturalDataSizeConverter implements DataSizeConverter {
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
    * Creates a {@code SimpleNaturalDataSizeConverter}.
    */
    public SimpleNaturalDataSizeConverter() {}

    /**
     * Converts a {@code DataSize} object to another {@code DataSize} object.
     *
     *  <p>
     *  The resulting unit will be of the same standard, such as SI, JEDEC or IEC as this object,
     *  unless this objects' unit is {@code ByteUnit.BYTE} or {@code BitUnit.BIT} by which the
     *  resulting unit may be of any available {@code ByteUnit} type.
     *  </p>
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

        Collection<DataUnit> units = ALL_KNOWN_UNITS;

        if (!BitUnit.BIT.equals(originalUnit) && !ByteUnit.BYTE.equals(originalUnit)) {
            // Only convert to same unit type as input object
            units = ALL_KNOWN_UNITS
                .stream()
                .filter(unit -> unit.getClass().isAssignableFrom(originalUnit.getClass()))
                .collect(Collectors.toList());
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
