package io.aesy.datasize;

import io.aesy.datasize.convert.DataSizeConverter;
import io.aesy.datasize.convert.CachedDataSizeUnitConverter;
import io.aesy.datasize.convert.SimpleNaturalDataSizeConverter;
import io.aesy.datasize.format.DataSizeFormatter;
import io.aesy.datasize.format.SimpleDataSizeFormatter;
import io.aesy.datasize.parse.DataSizeParser;
import io.aesy.datasize.parse.LenientDataSizeParser;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.Locale;

/**
 * A {@code DataSize} object models a quantity of digital information measured in bits or bytes.
 *
 * <p>
 * It features a number of convenience methods, such as {@code DataSize#format},
 * {@code DataSize#parse} and {@code DataSize#toNaturalUnit}, that make use of separate
 * implementations at the cost of versability. Use the dedicated classes if the convenience
 * featured in this class isn't enough for your usecase.
 * </p>
 *
 * <p>
 * All methods featuring comparisons of any kind, including {@code DataSize#equals}, compare the
 * value of the {@code DataSize} objects as {@code ByteUnit.BYTE}, meaning that
 * {@code DataSize.of(1000, ByteUnit.BYTE)} and {@code DataSize.of(1, ByteUnit.SI.KILOBYTE)} are
 * considered equal.
 * </p>
 *
 * <p>
 * No measures are taken to ensure that values can be represented as whole bits, hence {@code
 * DataSize.of(1.01, ByteUnts.BYTE)} is perfectly valid even though it make little to no sense.
 * If this isn't desirable, it has to be enforced externally.
 * </p>
 *
 * <blockquote>
 * Example usage:
 * <pre>{@code
 * DataSize bytes1 = DataSize.of(1000, ByteUnit.BYTE);
 * DataSize bytes2 = DataSize.of(1, ByteUnit.SI.KILOBYTE);
 *
 * assertTrue(bytes1.equals(bytes2));
 * }</pre>
 * </blockquote>
 */
public final class DataSize implements Comparable<DataSize>, Serializable {
    private static final long serialVersionUID = 2077111414556375345L;

    private final BigDecimal value;
    private final DataUnit unit;

    private DataSize(BigDecimal value, DataUnit unit) {
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Value must not be less than zero");
        }

        this.value = value;
        this.unit = unit;
    }

    /**
     * Creates a new {@code DataSize} object from a byte array.
     *
     * @param bytes The byte array
     * @return A new {@code DataSize} object
     * @throws IllegalArgumentException If the value is less than zero
     */
    public static DataSize of(byte[] bytes) {
        return of(bytes.length, ByteUnit.BYTE);
    }

    /**
     * Creates a new {@code DataSize} object from a value and a unit.
     *
     * @param value The value
     * @param unit The unit
     * @return A new {@code DataSize} object
     * @throws IllegalArgumentException If the value is less than zero
     */
    public static DataSize of(long value, DataUnit unit) {
        return new DataSize(BigDecimal.valueOf(value), unit);
    }

    /**
     * Creates a new {@code DataSize} object from a value and a unit.
     *
     * @param value The value
     * @param unit The unit
     * @return A new {@code DataSize} object
     * @throws IllegalArgumentException If the value is less than zero
     */
    public static DataSize of(double value, DataUnit unit) {
        return new DataSize(BigDecimal.valueOf(value), unit);
    }

    /**
     * Creates a new {@code DataSize} object from a value and a unit.
     *
     * @param value The value
     * @param unit The unit
     * @return A new {@code DataSize} object
     * @throws IllegalArgumentException If the value is less than zero
     */
    public static DataSize of(BigInteger value, DataUnit unit) {
        return new DataSize(new BigDecimal(value), unit);
    }

    /**
     * Creates a new {@code DataSize} object from a value and a unit.
     *
     * @param value The value
     * @param unit The unit
     * @return A new {@code DataSize} object
     * @throws IllegalArgumentException If the value is less than zero
     */
    public static DataSize of(BigDecimal value, DataUnit unit) {
        return new DataSize(value, unit);
    }

    /**
     * Formats a {@code DataSize} object based on a format description to produce a nicely formatted
     * string. The formatted output produced by this is of the form {@literal "#.## <unit>"}, where
     * {@literal "<unit>"} is the abbreviation of the unit associated with this instance.
     *
     * <p>
     * The way the number is formatted is based on
     * {@code Locale.getDefault(Locale.Category.FORMAT)}.
     * </p>
     *
     * @param datasize The DataSize to format
     * @return The formatted {@code DataSize} string
     * @throws IllegalArgumentException If the {@code DataSize} object is null
     */
    public static String format(DataSize datasize, String format) {
        // Create new instance every time in case default locale has changed between calls
        DataSizeFormatter formatter = new SimpleDataSizeFormatter();

        // TODO honor format
        // TODO write test

        return formatter.format(datasize);
    }

    /**
     * Parses an input string to produce a {@code DataSize} object.
     *
     * <p>
     * The method expects a string od the form {@literal "2.42 kB"} or {@literal "2.42 kilobyte"}.
     * Unit names in plural are accepted.
     * </p>
     *
     * <p>
     * The method is locale aware and uses {@code Locale.getDefault(Category.Format)} to
     * determine the format of the expected number. Locale may be changed between calls to this
     * method. If a specific locale is desired, see {@code LenientDataSizeParser}.
     * </p>
     *
     * <p>
     * The parsing performed by this method is case insensitive and ignores unexpected
     * whitespace characters.
     * </p>
     *
     * <p>
     * SI units will always be preferred over JEDEC units, meaning that
     * {@code DataSize.parse("1 kB")} will result in an object equivalent to
     * {@code DataSize.of(1, ByteUnit.SI.KILOBYTE)}. If this behavior is not desired, see
     * a {@code DataSizeParser} implementation that fits your usecase.
     * </p>
     *
     * @param input The input string to parse
     * @return The produced {@code DataSize} object
     * @throws ParseException If the input string could not be parsed
     * @throws IllegalArgumentException If the input string is null
     * @see LenientDataSizeParser
     */
    public static DataSize parse(String input) throws ParseException {
        // Create new instance every time in case default locale has changed between calls
        DataSizeParser parser = new LenientDataSizeParser();

        return parser.parse(input);
    }

    /**
     * Returns the greater of two {@code DataSize} objects. If the given objects are equal, the
     * first argument is returned.
     *
     * @param first The first object
     * @param second The second object
     * @return The greater of the given objects
     */
    public static DataSize max(DataSize first, DataSize second) {
        if (first.compareTo(second) >= 0) {
            return first;
        }

        return second;
    }

    /**
     * Returns the smaller of two {@code DataSize} objects. If the given objects are equal, the
     * first argument is returned.
     *
     * @param first The first object
     * @param second The second object
     * @return The lesser of the given objects
     */
    public static DataSize min(DataSize first, DataSize second) {
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
    public DataUnit getUnit() {
        return unit;
    }

    /**
     * Returns a new {@code DataSize} object whose value is greater than this according to the
     * value of the given {@code DataSize} object.
     *
     * <p>
     * The resulting object will be represented by the same unit as this object.
     * </p>
     *
     * @param other The given {@code DataSize} object
     * @return A new {@code DataSize} object
     */
    public DataSize add(DataSize other) {
        BigDecimal otherValue = other.toUnit(unit).value;

        return of(value.add(otherValue), unit);
    }

    /**
     * Returns a new {@code DataSize} object whose value is lesser than this according to the
     * value of the given {@code DataSize} object.
     *
     * <p>
     * The resulting objects' value is capped at zero and will be represented by the same unit as
     * this object.
     * </p>
     *
     * @param other The given {@code DataSize} object
     * @return A new {@code DataSize} object
     */
    public DataSize subtract(DataSize other) {
        BigDecimal otherValue = other.toUnit(unit).value;

        return of(value.subtract(otherValue).max(BigDecimal.ZERO), unit);
    }

    /**
     * Returns a new {@code DataSize} object whose value is 1 greater than this.
     *
     * <p>
     * The resulting object will be represented using the same unit as this.
     * </p>
     *
     * @return A new {@code DataSize} object
     */
    public DataSize increment() {
        return of(value.add(BigDecimal.ONE), unit);
    }

    /**
     * Returns a new {@code DataSize} object whose value is 1 less than this.
     *
     * <p>
     * The resulting objects' value is capped at zero and will be represented using the same
     * unit as this.
     * </p>
     *
     * @return A new {@code DataSize} object
     */
    public DataSize decrement() {
        return of(value.subtract(BigDecimal.ONE).max(BigDecimal.ZERO), unit);
    }

    /**
     * Returns a new {@code DataSize} object, containing the given {@code ByteUnit}, equal to this.
     *
     * @param unit The unit
     * @return A new {@code DataSize} object
     */
    public DataSize toUnit(DataUnit unit) {
        DataSizeConverter converter = new CachedDataSizeUnitConverter(unit);

        return converter.convert(this);
    }

    /**
     * Converts this object to a more human readable unit.
     *
     * <p>
     * The resulting unit will be of the same standard, such as SI, JEDEC or IEC as this object,
     * unless this objects' unit is {@code ByteUnit.BYTE} by which the resulting unit may be
     * of any available {@code ByteUnit} type.
     * </p>
     *
     * @return A new {@code DataSize} object that is equal to this.
     * @see SimpleNaturalDataSizeConverter
     */
    public DataSize toNaturalUnit() {
        DataSizeConverter converter = new SimpleNaturalDataSizeConverter();

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
     * Two {@code DataSize} objects are considered equal if both of their values as
     * {@code ByteUnit.BYTE} are exactly equal.
     * </p>
     *
     * @param other The object to be compared
     * @return A negative integer, zero, or a positive integer as this object is less than, equal
     *     to, or greater than the specified object
     * @throws IllegalArgumentException If the specified object is null
     */
    @Override
    public int compareTo(DataSize other) {
        BigDecimal otherValue = other.toUnit(unit).value;

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
     * <li>they both are, or inherit from, {@code DataSize}</li>
     * <li>they have the same value when converted to {@code ByteUnit.BYTE}</li>
     * </ul>
     *
     * @param other The reference object to compare with
     * @return {@code true} If this object is equal to the other object; {@code false} otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (other == null || !getClass().isAssignableFrom(other.getClass())) {
            return false;
        }

        return compareTo((DataSize) other) == 0;
    }

    @Override
    public int hashCode() {
        return toUnit(ByteUnit.BYTE).value
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
        DataSizeFormatter formatter = new SimpleDataSizeFormatter(Locale.US, 2);

        return formatter.format(this);
    }

    private void readObject(ObjectInputStream input)
        throws ClassNotFoundException, IOException, NoSuchFieldException, IllegalAccessException {
        BigDecimal value = (BigDecimal) input.readObject();
        DataUnit unit = (DataUnit) input.readObject();

        Field valueField = getClass().getDeclaredField("value");
        boolean wasValueAccessible = valueField.isAccessible();
        valueField.setAccessible(true);
        valueField.set(this, value);
        valueField.setAccessible(wasValueAccessible);

        Field unitField = getClass().getDeclaredField("unit");
        boolean wasUnitAccessible = unitField.isAccessible();
        unitField.setAccessible(true);
        unitField.set(this, unit);
        unitField.setAccessible(wasUnitAccessible);
    }

    private void writeObject(ObjectOutputStream output) throws IOException {
        output.writeObject(value);
        output.writeObject(unit);
    }
}
