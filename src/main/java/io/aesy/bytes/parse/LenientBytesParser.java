package io.aesy.bytes.parse;

import io.aesy.bytes.ByteUnit;
import io.aesy.bytes.ByteUnits;
import io.aesy.bytes.Bytes;
import org.petitparser.context.Result;
import org.petitparser.parser.Parser;
import org.petitparser.parser.primitive.CharacterParser;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

/**
 * {@code LenientBytesParser} is a parser that is case insensitive and ignore unexpected, or
 * missing, whitespace characters.
 *
 * <p>
 * The value portion of the input may be a fractional number of any magnitude and precision.
 * The value must be formatted according to the given locale. The unit portion may be formatted
 * either by its' name or its' abbreviation.
 * </p>
 *
 * <p>
 * Due to parsing being case insensitive, the difference between SI and JEDEC units is completely
 * ambiguous. SI units will always be preferred over JEDEC.
 * </p>
 *
 * <p>
 * If no locale is provided, {@code Locale.getDefault(Locale.Category.FORMAT)} is used.
 * </p>
 *
 * <blockquote>
 * <pre>{@code
 * BytesParser parser = new LenientByteParser();
 * Bytes parsed = parser.parse(" 3.14kb ");
 *
 * assertEquals(parsed, Bytes.valueOf(3.14, ByteUnits.SI.KILOBYTE))
 *
 * assertThrows(() -> parser.parse("666 YB :-)"))
 * }</pre>
 * </blockquote>
 */
public class LenientBytesParser implements BytesParser {
    private static final List<ByteUnit> units = ByteUnits.ALL.units();

    private final Parser parser;

    /**
     * Creates a {@code LenientBytesParser} with {@code Locale.getDefault(Locale.Category.FORMAT)}.
     */
    public LenientBytesParser() {
        this(Locale.getDefault(Locale.Category.FORMAT));
    }

    /**
     * Creates a {@code LenientBytesParser} with the desired locale.
     *
     * @param locale The locale to use
     * @throws IllegalArgumentException If the locale object is null
     */
    public LenientBytesParser(Locale locale) {
        this.parser = CharacterParser.whitespace().star()
                                     .seq(DecimalParser.localized(locale))
                                     .seq(CharacterParser.whitespace().star())
                                     .seq(ByteUnitParser.anyOf(units).caseSensitive(false))
                                     .seq(CharacterParser.whitespace().star())
                                     .map(LenientBytesParser::toBytes)
                                     .end();
    }

    /**
     * Parses an input string to produce a {@code Bytes} object.
     *
     * <p>
     * The value portion of the input may be a fractional number of any magnitude and precision.
     * The value must be formatted according to the given locale. The unit portion may be formatted
     * either by its' name or its' abbreviation.
     * </p>
     *
     * <p>
     * This method is case insensitive which make the difference between SI and JEDEC units is
     * completely ambiguous. SI units will always be preferred over JEDEC.
     * </p>
     *
     * @param input The input string to parse
     * @return The produced {@code Bytes} object
     * @throws ParseException If the input string could not be parsed
     * @throws IllegalArgumentException If the input string is null
     */
    @Override
    public Bytes parse(String input) throws ParseException {
        Result result;

        try {
            result = parser.parse(input);
        } catch (IllegalArgumentException exception) {
            // Input is valid but value is negative so constuction failed
            throw new ParseException(input, 0);
        }

        if (result.isSuccess()) {
            return result.get();
        }

        throw new ParseException(input, result.getPosition());
    }

    private static Bytes toBytes(List<Object> input) {
        BigDecimal value = (BigDecimal) input.get(1);
        ByteUnit unit = (ByteUnit) input.get(3);

        return Bytes.valueOf(value, unit);
    }
}
