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
 * {@code StrictBytesParser} is a parser that is case sensitive and don't accept any unexpected
 * characters, including whitespace. TODO
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
