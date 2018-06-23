package org.aesy.bytes;

import org.aesy.bytes.convert.BytesConverter;
import org.aesy.bytes.convert.SimpleNaturalBytesConverter;
import org.aesy.bytes.format.BytesFormatter;
import org.aesy.bytes.format.SimpleBytesFormatter;
import org.aesy.bytes.parse.BytesParser;
import org.aesy.bytes.parse.LenientBytesParser;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.Locale;
import java.util.Objects;

// TODO make serializeable

/**
 * A {@code Bytes} object models a quantity of digital information measured in
 * bits or bytes.
 *
 * <p>
 * It also features a number of convenience methods that make use of separate
 * implementations at the cost of versability. Use the dedicated classes if the
 * convenience featured in this class isn't enough for your usecase.
 * </p>
 *
 * <p>
 * All methods featuring comparisons of any kind, including {@code Bytes#equals},
 * compare the value of the {@code Bytes} objects as {@literal ByteUnit.COMMON.BYTE},
 * meaning that {@code Bytes.valueOf(1000, ByteUnit.COMMON.BYTE)} and
 * {@code Bytes.valueOf(1, ByteUnit.SI.KILOBYTE)} are considered equal.
 * </p>
 *
 * <p>
 * <blockquote>
 * Example usage:
 * <pre>{@code
 * Bytes bytes1 = Bytes.valueOf(1000, ByteUnit.COMMON.BYTE);
 * Bytes bytes2 = Bytes.valueOf(1, ByteUnit.SI.KILOBYTE);
 *
 * assertTrue(bytes1.equals(bytes2);
 * }</pre>
 * </blockquote>
 * </p>
 */
public class Bytes implements Comparable<Bytes> {
    private final BigDecimal value;
    private final ByteUnit unit;

    private Bytes(BigDecimal value, ByteUnit unit) {
        Objects.requireNonNull(value);
        Objects.requireNonNull(unit);

        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Value must not be less than zero");
        }

        this.value = value;
        this.unit = unit;
    }

    /**
     * Creates a new {@code Bytes} object from a value and a unit.
     *
     * @param value The value
     * @param unit The unit
     * @return A new {@code Bytes} object
     */
    public static Bytes valueOf(long value, ByteUnit unit) {
        return new Bytes(BigDecimal.valueOf(value), unit);
    }

    /**
     * Creates a new {@code Bytes} object from a value and a unit.
     *
     * @param value The value
     * @param unit The unit
     * @return A new {@code Bytes} object
     */
    public static Bytes valueOf(double value, ByteUnit unit) {
        return new Bytes(BigDecimal.valueOf(value), unit);
    }

    /**
     * Creates a new {@code Bytes} object from a value and a unit.
     *
     * @param value The value
     * @param unit The unit
     * @return A new {@code Bytes} object
     */
    public static Bytes valueOf(BigInteger value, ByteUnit unit) {
        return new Bytes(new BigDecimal(value), unit);
    }

    /**
     * Creates a new {@code Bytes} object from a value and a unit.
     *
     * @param value The value
     * @param unit The unit
     * @return A new {@code Bytes} object
     */
    public static Bytes valueOf(BigDecimal value, ByteUnit unit) {
        return new Bytes(value, unit);
    }

    /**
     * Formats a {@code Bytes} object based on a format description to produce
     * a nicely formatted string.
     *
     * <p>
     * The formatting uses {@code SimpleBytesFormatter} internally, which formats
     * the object The unit based on {@code Locale.getDefault(Locale.Category.FORMAT)}.
     * The unit will be formatted as its' abbreviation with a space between the
     * value and the unit.
     * </p>
     *
     * @param bytes The Bytes to format
     * @return The formatted Bytes string
     * @throws NullPointerException If the bytes object is null
     */
    public static String format(Bytes bytes) {
        Objects.requireNonNull(bytes);

        // Create new instance every time in case default locale has changed
        BytesFormatter formatter = new SimpleBytesFormatter();

        return formatter.format(bytes);
    }

    /**
     * Parses an input string to produce a {@code Bytes} object.
     *
     * <p>
     * The method expects a string of the form {@literal "2.42 kB"} or
     * {@literal "2.42 kilobyte"}. Unit names in plural are accepted.
     *
     * <p>
     * The method is locale aware and uses
     * {@code Locale.getDefault(Category.Format)} for determining the format
     * of the expected number. Locale may be changed between calls to this method.
     * If a specific locale is desired, see {@code LenientBytesParser}.
     * </p>
     *
     * <p>
     * The method uses {@code LenientBytesParser} internally, which is case
     * insensitive and ignore unexpected whitespace characters.
     * </p>
     *
     * <p>
     * SI units will always be preferred over JEDEC units, meaning that
     * {@code Bytes.parse("1 kB")} will result in an object equivalent to
     * {@code Bytes.valueOf(1, ByteUnit.SI.KILOBYTE)}. If this behavior is not
     * desired, see a {@code BytesParser} implementation that fits your usecase.
     * </p>
     *
     * @param input The input string to parse
     * @return The produced Bytes object
     * @throws ParseException If the input string could not be parsed
     * @throws NullPointerException If the input string is null
     * @see LenientBytesParser
     */
    public static Bytes parse(String input) throws ParseException {
        Objects.requireNonNull(input);

        // Create new instance every time in case default locale has changed
        BytesParser parser = new LenientBytesParser();

        return parser.parse(input);
    }

    /**
     * Gets the value in the unit associated with this object.
     *
     * @return The value
     */
    public BigDecimal getValue() {
        return value;
    }

    /**
     * Gets the unit associated with this object.
     *
     * @return The unit
     */
    public ByteUnit getUnit() {
        return unit;
    }

    /**
     * Converts this object to a more human readable unit.
     *
     * <p>
     * The conversion uses {@code SimpleNaturalBytesConverter} internally,
     * which only converts to units of the same {@code ByteUnit.Standard} as
     * this, unless this objects' unit is {@code ByteUnit.COMMON.Byte} by which
     * the resulting unit may be of any available {@code ByteUnit} type.
     * </p>
     *
     * @return A new {@code Bytes} object that is equal to this.
     * @see SimpleNaturalBytesConverter
     */
    public Bytes toNaturalUnit() {
        BytesConverter converter = new SimpleNaturalBytesConverter(null); // TODO

        return converter.convert(this);
    }

    /**
     * Compares this object with the specified object for order.
     *
     * <p>
     * Returns a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * </p>
     *
     * <p>
     * Two {@code Bytes} objects are considered equal if both of their values
     * as {@code ByteUnit.COMMON.BYTE} are exactly equal.
     * </p>
     *
     * @param other The object to be compared
     * @return A negative integer, zero, or a positive integer as this object
     *         is less than, equal to, or greater than the specified object
     * @throws NullPointerException If the specified object is null
     */
    @Override
    public int compareTo(Bytes other) {
        Objects.requireNonNull(other);

        // TODO could save some calculations here by converting the other unit to this
        BigDecimal thisBytes  = ByteUnits.COMMON.BYTE.convert(this).getValue();
        BigDecimal otherBytes = ByteUnits.COMMON.BYTE.convert(other).getValue();

        return thisBytes.compareTo(otherBytes);
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * <p>
     * Two objects are considered equal if and only if:
     * <ul>
     *   <li>neither object is null</li>
     *   <li>they both are, or inherit from, {@code Bytes}</li>
     *   <li>they have the same value when converted to {@code ByteUnit.COMMON.BYTE}</li>
     * </ul>
     * </p>
     *
     * @param other The reference object with which to compare
     * @return {@code true} If this object is the same as the other object;
     *         {@code false} otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        return compareTo((Bytes) other) == 0;
    }

    /**
     * Returns a string representation of this object.
     *
     * <p>
     * The string representation is
     * concise and easy to read - the value will be rounded off and the unit will
     * be represented by its' abbreviation. The value will use american format.
     * </p>
     *
     * @return A string representation of the object
     */
    @Override
    public String toString() {
        BytesFormatter formatter = new SimpleBytesFormatter(Locale.US, 2);

        return formatter.format(this);
    }
}
