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
 * {@code LenientDataSizeParser} is a parser that is case insensitive and ignore unexpected, or
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
 * DataSizeParser parser = new LenientByteParser();
 * DataSize parsed = parser.parse(" 3.14kb ");
 *
 * assertEquals(parsed, DataSize.of(3.14, ByteUnit.SI.KILOBYTE))
 *
 * assertThrows(() -> parser.parse("666 YB :-)"))
 * }</pre>
 * </blockquote>
 */
public class LenientDataSizeParser implements DataSizeParser {
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
     * Creates a {@code LenientDataSizeParser} with
     * {@code Locale.getDefault(Locale.Category.FORMAT)}.
     */
    public LenientDataSizeParser() {
        this(Locale.getDefault(Locale.Category.FORMAT));
    }

    /**
     * Creates a {@code LenientDataSizeParser} with the desired locale.
     *
     * @param locale The locale to use
     * @throws IllegalArgumentException If the locale object is null
     */
    public LenientDataSizeParser(Locale locale) {
        this.parser = CharacterParser.whitespace().star()
                                     .seq(DecimalParser.localized(locale))
                                     .seq(CharacterParser.whitespace().star())
                                     .seq(ByteUnitParser.anyOf(units).caseSensitive(false))
                                     .seq(CharacterParser.whitespace().star())
                                     .map(LenientDataSizeParser::toDataSize)
                                     .end();
    }

    /**
     * Parses an input string to produce a {@code DataSize} object.
     *
     * <p>
     * The value portion of the input may be a fractional number of any magnitude and
     * precision. The value must be formatted according to the given locale. The unit portion may
     * be formatted either by its' name or its' abbreviation.
     * </p>
     *
     * <p>
     * This method is case insensitive which make the difference between SI and JEDEC units is
     * completely ambiguous. SI units will always be preferred over JEDEC.
     * </p>
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
        BigDecimal value = (BigDecimal) input.get(1);
        DataUnit unit = (DataUnit) input.get(3);

        return DataSize.of(value, unit);
    }
}
