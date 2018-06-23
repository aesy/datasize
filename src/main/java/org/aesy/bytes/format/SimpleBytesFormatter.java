package org.aesy.bytes.format;

import org.aesy.bytes.Bytes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

/**
 * A {@code SimpleBytesFormatter} formats {@code Bytes} objects.
 *
 * <p>
 * The formatted output produced by this implementation is a simple representation
 * of the object similar to that produced by {@code Bytes#toString}, except more
 * configurable. Locale and precision can be configured. The precision determines
 * the maximum amount of fraction digits shown in the output. The value is rounded
 * using a {@code RoundingMode.HALF_UP} rounding mode. The unit will be formatted as
 * its' abbreviation with a space between the value and the unit.
 * </p>
 *
 * <p>
 * If no locale is provided, {@code Locale.getDefault(Locale.Category.FORMAT)} is used.
 * If no precision is provided, a maximum count of 2 decimals is used.
 * </p>
 *
 * <p>
 * <blockquote>
 * Example usage:
 * <pre>{@code
 * Bytes bytes = Bytes.valueOf(Math.PI, ByteUnit.SI.KILOBYTE);
 * String formatted = new SimpleBytesFormatter(Locale.US, 2).format(bytes);
 *
 * assertEquals(formatted, "3.14 kB");
 * }</pre>
 * </blockquote>
 * </p>
 *
 * @see Bytes#toString
 * @see RoundingMode#HALF_UP
 */
public class SimpleBytesFormatter implements BytesFormatter {
    private static final int DEFAULT_PRECISION = 2;

    private final Locale locale;
    private final int precision;

    /**
     * Creates a {@code SimpleBytesFormatter} with {@code Locale.getDefault(Locale.Category.FORMAT)}
     * and a default precision of {@literal 2}.
     */
    public SimpleBytesFormatter() {
        this(Locale.getDefault(Locale.Category.FORMAT), DEFAULT_PRECISION);
    }

    /**
     * Creates a {@code SimpleBytesFormatter} with the desired locale and a default
     * precision of {@literal 2}.
     *
     * @param locale The locale to use
     * @throws NullPointerException If the locale object is null
     */
    public SimpleBytesFormatter(Locale locale) {
        this(locale, DEFAULT_PRECISION);
    }

    /**
     * Creates a {@code SimpleBytesFormatter} with {@code Locale.getDefault(Locale.Category.FORMAT)}
     * and the desired precision.
     *
     * @param precision The precision to use
     * @throws IllegalArgumentException If precision is less than zero
     */
    public SimpleBytesFormatter(int precision) {
        this(Locale.getDefault(Locale.Category.FORMAT), precision);
    }

    /**
     * Creates a {@code SimpleBytesFormatter} with the desired locale and precision.
     *
     * @param locale The locale to use
     * @param precision The precision to use
     * @throws IllegalArgumentException If precision is less than zero
     * @throws NullPointerException If the locale object is null
     */
    public SimpleBytesFormatter(Locale locale, int precision) {
        Objects.requireNonNull(locale);

        if (precision < 0) {
            throw new IllegalArgumentException("Precision must not be less than zero");
        }

        this.locale = locale;
        this.precision = precision;
    }

    /**
     * Formats a {@code Bytes} object to produce a string based on the set locale
     * and precision.
     *
     * <p>
     * The value is rounded using a {@code RoundingMode.HALF_UP}
     * rounding mode and the unit will be formatted as its' abbreviation with a
     * space between the value and the unit.
     * </p>
     *
     * @param bytes The Bytes to format
     * @return The formatted Bytes string
     * @throws NullPointerException If the bytes object is null
     */
    @Override
    public String format(Bytes bytes) {
        Objects.requireNonNull(bytes);

        BigDecimal value = bytes.getValue();
        int scale = value.stripTrailingZeros().scale();

        NumberFormat formatter = DecimalFormat.getNumberInstance(locale);
        formatter.setRoundingMode(RoundingMode.HALF_UP);
        formatter.setMinimumFractionDigits(Math.max(0, Math.min(precision, scale)));
        formatter.setMaximumFractionDigits(precision);

        String formattedValue = formatter.format(value);
        String unitAbbreviation = bytes.getUnit().getAbbreviation();

        return String.format("%s %s", formattedValue, unitAbbreviation);
    }
}
