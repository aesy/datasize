package org.aesy.bytes.format;

import org.aesy.bytes.Bytes;
import org.aesy.bytes.convert.BytesConverter;
import org.aesy.bytes.convert.SmartNaturalBytesConverter;

import java.math.RoundingMode;
import java.util.Locale;
import java.util.Objects;

/**
 * A {@code SmartBytesFormatter} formats {@code Bytes} objects.
 *
 * <p>
 * The formatted output produced by this implementation is optimized for readability.
 * Unlike {@code SimpleBytesFormatter}, {@code SmartBytesFormatter} may convert the
 * unit of the formatted object to something more suitable in terms of readability.
 * {@code SmartNaturalBytesConverter} is being used internally to determine the most
 * human-readable unit.
 * </p>
 *
 * <p>
 * Locale and precision can be configured. The precision determines the maximum
 * amount of fraction digits shown in the output. The value is rounded using a
 * {@code RoundingMode.HALF_UP} rounding mode. The unit will be formatted as
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
 * Bytes bytes = Bytes.valueOf(1024, ByteUnit.IEC.KIBIBYTE);
 * String formatted = new SmartBytesFormatter(Locale.US, 2).format(bytes);
 *
 * assertEquals(formatted, "1 MiB");
 * }</pre>
 * </blockquote>
 * </p>
 *
 * @see SimpleBytesFormatter
 * @see SmartNaturalBytesConverter
 * @see RoundingMode#HALF_UP
 */
public class SmartBytesFormatter implements BytesFormatter {
    private static final int DEFAULT_PRECISION = 2;
    private static final BytesConverter humanReadableConverter = new SmartNaturalBytesConverter();

    private final BytesFormatter formatter;

    /**
     * Creates a {@code SmartBytesFormatter} with {@code Locale.getDefault(Locale.Category.FORMAT)}
     * and a default precision of {@literal 2}.
     */
    public SmartBytesFormatter() {
        this(Locale.getDefault(Locale.Category.FORMAT), DEFAULT_PRECISION);
    }

    /**
     * Creates a {@code SmartBytesFormatter} with the desired locale and a default precision of {@literal 2}.
     *
     * @param locale The locale to use
     * @throws NullPointerException If the locale object is null
     */
    public SmartBytesFormatter(Locale locale) {
        this(locale, DEFAULT_PRECISION);
    }

    /**
     * Creates a {@code SmartBytesFormatter} with {@code Locale.getDefault(Locale.Category.FORMAT)}
     * and the desired precision.
     *
     * @param precision The precision to use
     * @throws IllegalArgumentException If precision is less than zero
     */
    public SmartBytesFormatter(int precision) {
        this(Locale.getDefault(Locale.Category.FORMAT), precision);
    }

    /**
     * Creates a {@code SmartBytesFormatter} with the desired locale and precision.
     *
     * @param locale    The locale to use
     * @param precision The precision to use
     * @throws IllegalArgumentException If precision is less than zero
     * @throws NullPointerException If the locale object is null
     */
    public SmartBytesFormatter(Locale locale, int precision) {
        Objects.requireNonNull(locale);

        this.formatter = new SimpleBytesFormatter(locale, precision);
    }

    /**
     * Formats a {@code Bytes} object to produce a string based on the set locale
     * and precision.
     *
     * <p>
     * The value is rounded using a {@code RoundingMode.HALF_UP}
     * rounding mode. The unit may be converted to a more human-readable type
     * and will be formatted as its' abbreviation with a space between the value
     * and the unit. {@code SmartNaturalBytesConverter} is being used internally
     * to determine the most human-readable unit.
     * </p>
     *
     * @param bytes The Bytes to format
     * @return The formatted Bytes string
     * @throws NullPointerException If the bytes object is null
     * @see SmartNaturalBytesConverter
     */
    @Override
    public String format(Bytes bytes) {
        Objects.requireNonNull(bytes);

        return formatter.format(humanReadableConverter.convert(bytes));
    }
}
