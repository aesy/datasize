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
 * {@code StrictBytesParser} is a parser that is case sensitive and don't accept any unexpected,
 * or missing, characters - including whitespace.
 *
 * <p>
 * The value portion of the input may be a fractional number of any magnitude and precision.
 * The value must be formatted according to the given locale. The unit may be formatted
 * either by its' name or its' abbreviation. The unit name must be all lowercase.
 * </p>
 *
 * <p>
 * Due to the ambiguousness between SI and JEDEC, SI units will most commonly be preferred over
 * JEDEC units.
 * <ul>
 *   <li>{@literal "1 kB"} will resolve to {@code ByteUnits.SI.KILOBYTE}</li>
 *   <li>{@literal "1 KB"} will resolve to {@code ByteUnits.JEDEC.KILOBYTE}</li>
 *   <li>{@literal "1 MB"} and so on will resolve to the SI variations</li>
 * </ul>
 * </p>
 *
 * <p>
 * If no locale is provided, {@code Locale.getDefault(Locale.Category.FORMAT)} is used.
 * </p>
 *
 * <blockquote>
 * <pre>{@code
 * BytesParser parser = new StrictBytesParser();
 * Bytes parsed = parser.parse("3.14 KiB");
 *
 * assertEquals(parsed, Bytes.valueOf(3.14, ByteUnits.IEC.KIBIBYTE))
 *
 * assertThrows(() -> parser.parse("-3.14 KiB"));
 * }</pre>
 * </blockquote>
 */
public class StrictBytesParser implements BytesParser {
    private static final List<ByteUnit> units = ByteUnits.ALL.units();

    private final Parser parser;

    /**
     * Creates a {@code StrictBytesParser} with {@code Locale.getDefault(Locale.Category.FORMAT)}.
     */
    public StrictBytesParser() {
        this(Locale.getDefault(Locale.Category.FORMAT));
    }

    /**
     * Creates a {@code StrictBytesParser} with the desired locale.
     *
     * @param locale The locale to use
     * @throws IllegalArgumentException If the locale object is null
     */
    public StrictBytesParser(Locale locale) {
        this.parser = DecimalParser.localized(locale)
                                   .seq(CharacterParser.whitespace())
                                   .seq(ByteUnitParser.anyOf(units).caseSensitive(true))
                                   .map(StrictBytesParser::toBytes)
                                   .end();
    }

    /**
     * Parses an input string to produce a {@code Bytes} object.
     *
     * <p>
     * The value portion of the input may be a fractional number of any magnitude and precision.
     * The value must be formatted according to the given locale. The unit may be formatted
     * either by its' name or its' abbreviation. The unit name must be all lowercase.
     * </p>
     *
     * <p>
     * Due to the ambiguousness between SI and JEDEC, SI units will most commonly be preferred over
     * JEDEC units.
     * <ul>
     *   <li>{@literal "1 kB"} will resolve to {@code ByteUnits.SI.KILOBYTE}</li>
     *   <li>{@literal "1 KB"} will resolve to {@code ByteUnits.JEDEC.KILOBYTE}</li>
     *   <li>{@literal "1 MB"} and so on will resolve to the SI variations</li>
     * </ul>
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
        BigDecimal value = (BigDecimal) input.get(0);
        ByteUnit unit = (ByteUnit) input.get(2);

        return Bytes.valueOf(value, unit);
    }
}
