package io.aesy.datasize.format;

import io.aesy.datasize.DataSize;
import io.aesy.datasize.convert.DataSizeConverter;
import io.aesy.datasize.convert.SmartNaturalDataSizeConverter;

import java.math.RoundingMode;
import java.util.Locale;

/**
 * A {@code SmartDataSizeFormatter} formats {@code DataSize} objects.
 *
 * <p>
 * The formatted output produced by this implementation is optimized for readability. Unlike {@code
 * SimpleDataSizeFormatter}, {@code SmartDataSizeFormatter} may convert the unit of the formatted
 * object to something more suitable in terms of readability. {@code SmartNaturalDataSizeConverter}
 * is being used internally to determine the most human-readable unit.
 * </p>
 *
 * <p>
 * Locale and precision can be configured. The precision determines the maximum amount of fraction
 * digits shown in the output. The value is rounded using a {@code RoundingMode.HALF_UP} rounding
 * mode. The unit will be formatted as its' abbreviation with a space between the value and the
 * unit.
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
 * DataSize bytes = DataSize.of(1024, ByteUnit.IEC.KIBIBYTE);
 * String formatted = new SmartDataSizeFormatter(Locale.US, 2).format(bytes);
 *
 * assertEquals(formatted, "1 MiB");
 * }</pre>
 * </blockquote>
 *
 * @see SimpleDataSizeFormatter
 * @see SmartNaturalDataSizeConverter
 * @see RoundingMode#HALF_UP
 */
public class SmartDataSizeFormatter implements DataSizeFormatter {
    private static final int DEFAULT_PRECISION = 2;
    private static final DataSizeConverter humanReadableConverter = new SmartNaturalDataSizeConverter();

    private final DataSizeFormatter formatter;

    /**
     * Creates a {@code SmartDataSizeFormatter} with
     * {@code Locale.getDefault(Locale.Category.FORMAT)} and a default precision of {@literal 2}.
     */
    public SmartDataSizeFormatter() {
        this(Locale.getDefault(Locale.Category.FORMAT), DEFAULT_PRECISION);
    }

    /**
     * Creates a {@code SmartDataSizeFormatter} with the desired locale and a default precision of
     * {@literal 2}.
     *
     * @param locale The locale to use
     * @throws IllegalArgumentException If the locale object is null
     */
    public SmartDataSizeFormatter(Locale locale) {
        this(locale, DEFAULT_PRECISION);
    }

    /**
     * Creates a {@code SmartDataSizeFormatter} with
     * {@code Locale.getDefault(Locale.Category.FORMAT)} and the desired precision. If precision
     * is less than zero, not rounding will be performed.
     *
     * @param precision The precision to use
     */
    public SmartDataSizeFormatter(int precision) {
        this(Locale.getDefault(Locale.Category.FORMAT), precision);
    }

    /**
     * Creates a {@code SmartDataSizeFormatter} with the desired locale and precision.
     *
     * @param locale The locale to use
     * @param precision The precision to use
     * @throws IllegalArgumentException If the locale object is null
     */
    public SmartDataSizeFormatter(Locale locale, int precision) {
        this.formatter = new SimpleDataSizeFormatter(locale, precision);
    }

    /**
     * Formats a {@code DataSize} object to produce a string based on the set locale and precision.
     *
     * <p>
     * The value is rounded using a {@code RoundingMode.HALF_UP} rounding mode. The unit may be
     * converted to a more human-readable type and will be formatted as its' abbreviation with a
     * space between the value and the unit. {@code SmartNaturalDataSizeConverter} is being used
     * internally to determine the most human-readable unit.
     * </p>
     *
     * @param dataSize The DataSize to format
     * @return The formatted {@code DataSize} string
     * @throws IllegalArgumentException If the {@code DataSize} object is null
     * @see SmartNaturalDataSizeConverter
     */
    @Override
    public String format(DataSize dataSize) {
        return formatter.format(humanReadableConverter.convert(dataSize));
    }
}
