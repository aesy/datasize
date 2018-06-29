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
 * {@code LenientBytesParser} is a parser that is case insensitive and ignore unexpected whitespace
 * characters. TODO
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
