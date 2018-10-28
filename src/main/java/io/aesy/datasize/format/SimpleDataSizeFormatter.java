package io.aesy.datasize.format;

import io.aesy.datasize.DataSize;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * A {@code SimpleDataSizeFormatter} formats {@code DataSize} objects.
 *
 * <p>
 * The formatted output produced by this implementation is a simple representation of the object
 * similar to that produced by {@code DataSize#toString}, except more configurable. Locale and
 * precision can be configured. The precision determines the maximum amount of fraction digits shown
 * in the output. The value is rounded using a {@code RoundingMode.HALF_UP} rounding mode. The unit
 * will be formatted as its' abbreviation with a space between the value and the unit.
 * </p>
 *
 * <p>
 * If no locale is provided, {@code Locale.getDefault(Locale.Category.FORMAT)} is used. If no
 * precision is provided, a maximum count of 2 decimals is used.
 * </p>
 *
 * <blockquote>
 * Example usage:
 * <pre>{@code
 * DataSize dataSize = DataSize.of(Math.PI, ByteUnits.SI.KILOBYTE);
 * String formatted = new SimpleDataSizeFormatter(Locale.US, 2).format(dataSize);
 *
 * assertEquals(formatted, "3.14 kB");
 * }</pre>
 * </blockquote>
 *
 * @see DataSize#toString
 * @see RoundingMode#HALF_UP
 */
public class SimpleDataSizeFormatter implements DataSizeFormatter {
    private static final int DEFAULT_PRECISION = 2;

    private final Locale locale;
    private final int precision;

    /**
     * Creates a {@code SimpleDataSizeFormatter} with
     * {@code Locale.getDefault(Locale.Category.FORMAT)} and a default precision of {@literal 2}.
     */
    public SimpleDataSizeFormatter() {
        this(Locale.getDefault(Locale.Category.FORMAT), DEFAULT_PRECISION);
    }

    /**
     * Creates a {@code SimpleDataSizeFormatter} with the desired locale and a default precision of
     * {@literal 2}.
     *
     * @param locale The locale to use
     * @throws IllegalArgumentException If the locale object is null
     */
    public SimpleDataSizeFormatter(Locale locale) {
        this(locale, DEFAULT_PRECISION);
    }

    /**
     * Creates a {@code SimpleDataSizeFormatter} with
     * {@code Locale.getDefault(Locale.Category.FORMAT)} and the desired precision. If precision
     * is less than zero, not rounding will be performed.
     *
     * @param precision The precision to use
     */
    public SimpleDataSizeFormatter(int precision) {
        this(Locale.getDefault(Locale.Category.FORMAT), precision);
    }

    /**
     * Creates a {@code SimpleDataSizeFormatter} with the desired locale and precision.
     *
     * @param locale The locale to use
     * @param precision The precision to use
     * @throws IllegalArgumentException If the locale object is null
     */
    public SimpleDataSizeFormatter(Locale locale, int precision) {
        this.locale = locale;
        this.precision = precision;
    }

    /**
     * Formats a {@code DataSize} object to produce a string based on the set locale and precision.
     *
     * <p>
     * The value is rounded using a {@code RoundingMode.HALF_UP} rounding mode and the unit will be
     * formatted as its' abbreviation with a space between the value and the unit.
     * </p>
     *
     * @param dataSize The DataSize to format
     * @return The formatted {@code DataSize} string
     * @throws IllegalArgumentException If the dataSize object is null
     */
    @Override
    public String format(DataSize dataSize) {
        BigDecimal value = dataSize.getValue();
        int scale = value.stripTrailingZeros().scale();

        NumberFormat formatter = NumberFormat.getNumberInstance(locale);
        formatter.setRoundingMode(RoundingMode.HALF_UP);

        if (precision < 0) {
            // We want to show ALL decimal digits but we are limited to 2147483647 as
            // there is no way to tell the NumberFormat instance to disable all rounding.
            formatter.setMaximumFractionDigits(Integer.MAX_VALUE);
        } else {
            formatter.setMinimumFractionDigits(Math.max(0, Math.min(precision, scale)));
            formatter.setMaximumFractionDigits(precision);
        }

        String formattedValue = formatter.format(value);
        String unitAbbreviation = dataSize.getUnit().getAbbreviation();

        return String.format("%s %s", formattedValue, unitAbbreviation);
    }
}
