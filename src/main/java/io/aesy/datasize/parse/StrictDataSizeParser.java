package io.aesy.datasize.parse;

import io.aesy.datasize.BitUnit;
import io.aesy.datasize.ByteUnit;
import io.aesy.datasize.DataSize;
import io.aesy.datasize.DataUnit;
import org.petitparser.context.Result;
import org.petitparser.parser.Parser;
import org.petitparser.parser.primitive.CharacterParser;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * {@code StrictDataSizeParser} is a parser that is case sensitive and don't accept any unexpected,
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
 * </p>
 * <ul>
 *   <li>{@literal "1 kB"} will resolve to {@code ByteUnit.SI.KILOBYTE}</li>
 *   <li>{@literal "1 KB"} will resolve to {@code ByteUnit.JEDEC.KILOBYTE}</li>
 *   <li>{@literal "1 MB"} and so on will resolve to the SI variations</li>
 * </ul>
 *
 * <p>
 * If no locale is provided, {@code Locale.getDefault(Locale.Category.FORMAT)} is used.
 * </p>
 *
 * <blockquote>
 * <pre>{@code
 * DataSizeParser parser = new StrictDataSizeParser();
 * DataSize parsed = parser.parse("3.14 KiB");
 *
 * assertEquals(parsed, DataSize.of(3.14, ByteUnit.IEC.KIBIBYTE))
 *
 * assertThrows(() -> parser.parse("-3.14 KiB"));
 * }</pre>
 * </blockquote>
 */
public class StrictDataSizeParser implements DataSizeParser {
    private static final List<DataUnit> units;

    static {
        units = new ArrayList<>();
        units.add(BitUnit.BIT);
        units.addAll(BitUnit.SI.values());
        units.addAll(BitUnit.IEC.values());
        units.addAll(BitUnit.JEDEC.values());
        units.add(ByteUnit.BYTE);
        units.addAll(ByteUnit.SI.values());
        units.addAll(ByteUnit.IEC.values());
        units.addAll(ByteUnit.JEDEC.values());
    }

    private final Parser parser;

    /**
     * Creates a {@code StrictDataSizeParser} with {@code Locale.getDefault(Locale.Category.FORMAT)}.
     */
    public StrictDataSizeParser() {
        this(Locale.getDefault(Locale.Category.FORMAT));
    }

    /**
     * Creates a {@code StrictDataSizeParser} with the desired locale.
     *
     * @param locale The locale to use
     * @throws IllegalArgumentException If the locale object is null
     */
    public StrictDataSizeParser(Locale locale) {
        this.parser = DecimalParser.localized(locale)
                                   .seq(CharacterParser.whitespace())
                                   .seq(ByteUnitParser.anyOf(units).caseSensitive(true))
                                   .map(StrictDataSizeParser::toDataSize)
                                   .end();
    }

    /**
     * Parses an input string to produce a {@code DataSize} object.
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
     * </p>
     * <ul>
     *   <li>{@literal "1 kB"} will resolve to {@code ByteUnit.SI.KILOBYTE}</li>
     *   <li>{@literal "1 KB"} will resolve to {@code ByteUnit.JEDEC.KILOBYTE}</li>
     *   <li>{@literal "1 MB"} and so on will resolve to the SI variations</li>
     * </ul>
     *
     * @param input The input string to parse
     * @return The produced {@code DataSize} object
     * @throws ParseException If the input string could not be parsed
     * @throws IllegalArgumentException If the input string is null
     */
    @Override
    public DataSize parse(String input) throws ParseException {
        Result result;

        try {
            result = parser.parse(input);
        } catch (IllegalArgumentException exception) {
            // Input is valid but value is negative so construction failed
            throw new ParseException(input, 0);
        }

        if (result.isSuccess()) {
            return result.get();
        }

        throw new ParseException(input, result.getPosition());
    }

    private static DataSize toDataSize(List<Object> input) {
        BigDecimal value = (BigDecimal) input.get(0);
        DataUnit unit = (DataUnit) input.get(2);

        return DataSize.of(value, unit);
    }
}
