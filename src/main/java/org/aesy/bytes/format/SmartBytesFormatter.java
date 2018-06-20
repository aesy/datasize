package org.aesy.bytes.format;

import org.aesy.bytes.ByteUnit;
import org.aesy.bytes.Bytes;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

public class SmartBytesFormatter implements BytesFormatter {
    private static final List<ByteUnit> SI = Arrays.asList(ByteUnit.SI.values());
    private static final List<ByteUnit> IEC = Arrays.asList(ByteUnit.IEC.values());
    private static final List<ByteUnit> JEDEC = Arrays.asList(ByteUnit.JEDEC.values());
    private static final BigDecimal ONE_THOUSAND = BigDecimal.valueOf(1000);
    private static final int DEFAULT_PRECISION = 2;

    private Locale locale;
    private int precision;

    public SmartBytesFormatter() {
        this(Locale.getDefault(Locale.Category.FORMAT), DEFAULT_PRECISION);
    }

    public SmartBytesFormatter(Locale locale) {
        this(locale, DEFAULT_PRECISION);
    }

    public SmartBytesFormatter(int precision) {
        this(Locale.getDefault(Locale.Category.FORMAT), precision);
    }

    public SmartBytesFormatter(Locale locale, int precision) {
        Objects.requireNonNull(locale);

        this.locale = locale;
        this.precision = precision;
    }

    @Override
    public String toShortString(Bytes bytes) {
        Objects.requireNonNull(bytes);

        BigDecimal value = bytes.getValue();
        int scale = value.stripTrailingZeros().scale();

        NumberFormat formatter = DecimalFormat.getNumberInstance(locale);
        formatter.setMinimumFractionDigits(Math.max(0, Math.min(precision, scale)));
        formatter.setMaximumFractionDigits(precision);

        String formattedValue = formatter.format(value);
        String unitAbbreviation = bytes.getUnit().getAbbreviation();

        return String.format("%s %s", formattedValue, unitAbbreviation);
    }

    @Override
    public String toLongString(Bytes bytes) {
        Objects.requireNonNull(bytes);

        BigDecimal value = bytes.getValue();
        int scale = value.stripTrailingZeros().scale();

        NumberFormat formatter = DecimalFormat.getNumberInstance(locale);
        formatter.setMinimumFractionDigits(Math.max(0, Math.min(precision, scale)));
        formatter.setMaximumFractionDigits(precision);

        String formattedValue = formatter.format(value);
        String unitName = bytes.getUnit().getName();

        return String.format("%s %s", formattedValue, unitName);
    }

    @Override
    public String toHumanReadableShortString(Bytes bytes) {
        Objects.requireNonNull(bytes);

        return toShortString(convertToHumanReadableBytes(bytes));
    }

    @Override
    public String toHumanReadableLongString(Bytes bytes) {
        Objects.requireNonNull(bytes);

        return toLongString(convertToHumanReadableBytes(bytes));
    }

    private Bytes convertToHumanReadableBytes(Bytes bytes) {
        Bytes originalBytes = bytes;
        BigDecimal originalValue = originalBytes.getValue();
        ByteUnit originalUnit = originalBytes.getUnit();
        boolean isLessThanOneThousandBytes = ByteUnit.BYTE.convert(originalBytes)
                                                          .getValue().compareTo(ONE_THOUSAND) < 0;

        if (isLessThanOneThousandBytes) {
            // Values less than 1000 bytes are best viewed as bytes
            return originalBytes;
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
            // Only use JEDEC units if original does to reduce ambiguity
            units.addAll(JEDEC);
        } else {
            // Unknown unit type, just use original
            return originalBytes;
        }

        Bytes best = originalBytes;

        for (ByteUnit unit : units) {
            Bytes newBytes = unit.convert(originalBytes);
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

    public static void main(String[] args) {
        Locale.setDefault(Locale.Category.FORMAT, Locale.US);
        BytesFormatter formatter = new SmartBytesFormatter();

        Bytes c1 = ByteUnit.SI.MEGABYTE.convert(Bytes.valueOf(1024, ByteUnit.IEC.KIBIBYTE));
        Bytes c2 = ByteUnit.IEC.MEBIBYTE.convert(Bytes.valueOf(1024, ByteUnit.IEC.KIBIBYTE));

        System.out.println(c1.getValue().scale());
        System.out.println(c2.getValue().scale());

        System.out.println(formatter.toHumanReadableShortString(c1));
        System.out.println(formatter.toHumanReadableShortString(c2));

        System.out.println(formatter.toShortString(Bytes.valueOf(new BigDecimal("1.00"), ByteUnit.BYTE)));
    }
}
