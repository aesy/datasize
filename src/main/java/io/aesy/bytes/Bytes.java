package io.aesy.bytes;

import io.aesy.bytes.convert.BytesConverter;
import io.aesy.bytes.convert.SimpleNaturalBytesConverter;
import io.aesy.bytes.format.BytesFormatter;
import io.aesy.bytes.format.SimpleBytesFormatter;
import io.aesy.bytes.parse.BytesParser;
import io.aesy.bytes.parse.LenientBytesParser;

import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.Locale;

/**
 * A {@code Bytes} object models a quantity of digital information measured in bits or bytes.
 *
 * <p>
 * It also features a number of convenience methods that make use of separate implementations at the
 * cost of versability. Use the dedicated classes if the convenience featured in this class isn't
 * enough for your usecase.
 * </p>
 *
 * <p>
 * All methods featuring comparisons of any kind, including {@code Bytes#equals}, compare the value
 * of the {@code Bytes} objects as {@code ByteUnits.BYTE}, meaning that {@code Bytes.valueOf(1000,
 * ByteUnits.BYTE)} and {@code Bytes.valueOf(1, ByteUnits.SI.KILOBYTE)} are considered equal.
 * </p>
 *
 * <p>
 * No measures are taken to ensure that values can be represented as whole bits, hence {@code
 * Bytes.valueOf(1.01, ByteUnts.BYTE)} is perfectly valid even though it make little to no sense.
 * </p>
 *
 * <blockquote>
 * Example usage:
 * <pre>{@code
 * Bytes bytes1 = Bytes.valueOf(1000, ByteUnits.BYTE);
 * Bytes bytes2 = Bytes.valueOf(1, ByteUnits.SI.KILOBYTE);
 *
 * assertTrue(bytes1.equals(bytes2));
 * }</pre>
 * </blockquote>
 */
public class Bytes implements Comparable<Bytes>, Serializable {
    private static final long serialVersionUID = 3040088566269704711L;

    private final BigDecimal value;
    private final ByteUnit unit;

    protected Bytes(BigDecimal value, ByteUnit unit) {
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Value must not be less than zero");
        }

        this.value = value;
        this.unit = unit;
    }

    /**
     * Creates a new {@code Bytes} object from a byte array.
     *
     * @param bytes The byte array
     * @return A new {@code Bytes} object
     * @throws IllegalArgumentException If the value is less than zero
     */
    public static Bytes valueOf(byte[] bytes) {
        return valueOf(bytes.length, ByteUnits.BYTE);
    }

    /**
     * Creates a new {@code Bytes} object from a value and a unit.
     *
     * @param value The value
     * @param unit The unit
     * @return A new {@code Bytes} object
     * @throws IllegalArgumentException If the value is less than zero
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
     * @throws IllegalArgumentException If the value is less than zero
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
     * @throws IllegalArgumentException If the value is less than zero
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
     * @throws IllegalArgumentException If the value is less than zero
     */
    public static Bytes valueOf(BigDecimal value, ByteUnit unit) {
        return new Bytes(value, unit);
    }

    /**
     * Formats a {@code Bytes} object based on a format description to produce a nicely formatted
     * string. The formatted output produced by this is of the form {@literal "#.## <unit>"}, where
     * {@literal "<unit>"} is the abbreviation of the unit associated with this instance.
     *
     * <p>
     * The formatting uses {@code SimpleBytesFormatter} internally, which formats the object The
     * unit based on {@code Locale.getDefault(Locale.Category.FORMAT)}.
     * </p>
     *
     * @param bytes The Bytes to format
     * @return The formatted {@code Bytes} string
     * @throws IllegalArgumentException If the bytes object is null
     */
    public static String format(Bytes bytes) {
        // Create new instance every time in case default locale has changed
        BytesFormatter formatter = new SimpleBytesFormatter();

        return formatter.format(bytes);
    }

    /**
     * Parses an input string to produce a {@code Bytes} object.
     *
     * <p>
     * The method expects a string of the form {@literal "2.42 kB"} or {@literal "2.42 kilobyte"}.
     * Unit names in plural are accepted.
     * </p>
     *
     * <p>
     * The method is locale aware and uses {@code Locale.getDefault(Category.Format)} for
     * determining the format of the expected number. Locale may be changed between calls to this
     * method. If a specific locale is desired, see {@code LenientBytesParser}.
     * </p>
     *
     * <p>
     * The method uses {@code LenientBytesParser} internally, which is case insensitive and ignore
     * unexpected whitespace characters.
     * </p>
     *
     * <p>
     * SI units will always be preferred over JEDEC units, meaning that {@code Bytes.parse("1 kB")}
     * will result in an object equivalent to {@code Bytes.valueOf(1, ByteUnits.SI.KILOBYTE)}. If
     * this behavior is not desired, see a {@code BytesParser} implementation that fits your
     * usecase.
     * </p>
     *
     * @param input The input string to parse
     * @return The produced {@code Bytes} object
     * @throws ParseException If the input string could not be parsed
     * @throws IllegalArgumentException If the input string is null
     * @see LenientBytesParser
     */
    public static Bytes parse(String input) throws ParseException {
        // Create new instance every time in case default locale has changed
        BytesParser parser = new LenientBytesParser();

        return parser.parse(input);
    }

    /**
     * Returns the greater of two {@code Bytes} objects. If the given objects are equal, the first
     * argument is returned.
     *
     * @param first The first object
     * @param second The second object
     * @return The lesser of the given objects
     */
    public static Bytes max(Bytes first, Bytes second) {
        if (first.compareTo(second) >= 0) {
            return first;
        }

        return second;
    }

    /**
     * Returns the smaller of two {@code Bytes} objects. If the given objects are equal, the first
     * argument is returned.
     *
     * @param first The first object
     * @param second The second object
     * @return The lesser of the given objects
     */
    public static Bytes min(Bytes first, Bytes second) {
        if (first.compareTo(second) <= 0) {
            return first;
        }

        return second;
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
     * Returns a new {@code Bytes} object whose value is greater than this according to the value of
     * the given {@code Bytes} object.
     *
     * <p>
     * The resulting object will be represented by the same unit as this object.
     * </p>
     *
     * @param other The given {@code Bytes} object
     * @return A new {@code Bytes} object
     */
    public Bytes add(Bytes other) {
        BigDecimal otherValue = other.toUnit(unit).getValue();

        return Bytes.valueOf(value.add(otherValue), unit);
    }

    /**
     * Returns a new {@code Bytes} object whose value is lesser than this according to the value of
     * the given {@code Bytes} object.
     *
     * <p>
     * The resulting objects' value is capped at zero and will be represented by the same unit as
     * this object.
     * </p>
     *
     * @param other The given {@code Bytes} object
     * @return A new {@code Bytes} object
     */
    public Bytes subtract(Bytes other) {
        BigDecimal otherValue = other.toUnit(unit).getValue();

        return Bytes.valueOf(value.subtract(otherValue).max(BigDecimal.ZERO), unit);
    }

    /**
     * Returns a new {@code Bytes} object whose value is 1 greater than this.
     *
     * <p>
     * The resulting object will always be represented using the same unit as this.
     * </p>
     *
     * @return A new {@code Bytes} object
     */
    public Bytes increment() {
        return Bytes.valueOf(value.add(BigDecimal.ONE), unit);
    }

    /**
     * Returns a new {@code Bytes} object whose value is 1 less than this.
     *
     * <p>
     * The resulting objects' value is capped at zero and will always be represented using the same
     * unit as this.
     * </p>
     *
     * @return A new {@code Bytes} object
     */
    public Bytes decrement() {
        return Bytes.valueOf(value.subtract(BigDecimal.ONE).max(BigDecimal.ZERO), unit);
    }

    /**
     * Returns a new {@code Bytes} object, containing the given {@code ByteUnit}, equal to this.
     *
     * @param unit The unit
     * @return A new {@code Bytes} object
     */
    public Bytes toUnit(ByteUnit unit) {
        return unit.convert(this);
    }

    /**
     * Converts this object to a more human readable unit.
     *
     * <p>
     * The conversion uses {@code SimpleNaturalBytesConverter} internally, which only converts to
     * units of the same {@code ByteUnit.Standard} as this, unless this objects' unit is {@code
     * ByteUnits.BYTE} by which the resulting unit may be of any available {@code ByteUnit} type.
     * </p>
     *
     * @return A new {@code Bytes} object that is equal to this.
     * @see SimpleNaturalBytesConverter
     */
    public Bytes toNaturalUnit() {
        BytesConverter converter = new SimpleNaturalBytesConverter();

        return converter.convert(this);
    }

    /**
     * Compares this object with the specified object for order.
     *
     * <p>
     * Returns a negative integer, zero, or a positive integer as this object is less than, equal
     * to, or greater than the specified object.
     * </p>
     *
     * <p>
     * Two {@code Bytes} objects are considered equal if both of their values as {@code
     * ByteUnits.BYTE} are exactly equal.
     * </p>
     *
     * @param other The object to be compared
     * @return A negative integer, zero, or a positive integer as this object is less than, equal
     *     to, or greater than the specified object
     * @throws IllegalArgumentException If the specified object is null
     */
    @Override
    public int compareTo(Bytes other) {
        BigDecimal otherValue = other.toUnit(unit).getValue();

        return value.compareTo(otherValue);
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * <p>
     * Two objects are considered equal if and only if:
     * </p>
     * <ul>
     * <li>neither object is null</li>
     * <li>they both are, or inherit from, {@code Bytes}</li>
     * <li>they have the same value when converted to {@code ByteUnits.BYTE}</li>
     * </ul>
     *
     * @param other The reference object with which to compare
     * @return {@code true} If this object is the same as the other object; {@code false} otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (other == null || !getClass().isAssignableFrom(other.getClass())) {
            return false;
        }

        return compareTo((Bytes) other) == 0;
    }

    @Override
    public int hashCode() {
        return toUnit(ByteUnits.BYTE)
            .getValue()
            .stripTrailingZeros()
            .hashCode();
    }

    /**
     * Returns a string representation of this object.
     *
     * <p>
     * The string representation is concise and easy to read - the value will be rounded off and the
     * unit will be represented by its' abbreviation. The value will use american format.
     * </p>
     *
     * @return A string representation of this object
     */
    @Override
    public String toString() {
        BytesFormatter formatter = new SimpleBytesFormatter(Locale.US, 2);

        return formatter.format(this);
    }

    private void readObject(ObjectInputStream input)
        throws ClassNotFoundException, IOException, NoSuchFieldException, IllegalAccessException {
        String string = input.readUTF();
        Bytes bytes;

        try {
            bytes = parse(string);
        } catch (ParseException exception) {
            throw new IOException(exception);
        }

        Field unitField = getClass().getDeclaredField("unit");
        boolean wasUnitAccessible = unitField.isAccessible();
        unitField.setAccessible(true);
        unitField.set(this, bytes.unit);
        unitField.setAccessible(wasUnitAccessible);

        Field valueField = getClass().getDeclaredField("value");
        boolean wasValueAccessible = valueField.isAccessible();
        valueField.setAccessible(true);
        valueField.set(this, bytes.value);
        valueField.setAccessible(wasValueAccessible);
    }

    private void writeObject(ObjectOutputStream output) throws IOException {
        BytesFormatter formatter = new SimpleBytesFormatter(Locale.US, -1);

        // This method is limited to 2147483647 decimal digits
        output.writeUTF(formatter.format(this));
    }
}
