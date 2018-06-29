package io.aesy.bytes.parse;

import org.petitparser.context.Context;
import org.petitparser.context.Result;
import org.petitparser.parser.Parser;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.Locale;

class DecimalParser extends Parser {
    private final Locale locale;
    private final String message;

    DecimalParser(Locale locale, String message) {
        this.locale = locale;
        this.message = message;
    }

    static DecimalParser localized(Locale locale) {
        return new DecimalParser(locale, String.format("number expected in locale: %s", locale));
    }

    static DecimalParser localized(Locale locale, String message) {
        return new DecimalParser(locale, message);
    }

    @Override
    public Result parseOn(Context context) {
        DecimalFormat decimalParser = (DecimalFormat) DecimalFormat.getNumberInstance(locale);
        decimalParser.setParseBigDecimal(true);
        String buffer = context.getBuffer();
        int start = context.getPosition();

        ParsePosition position = new ParsePosition(start);
        BigDecimal result = (BigDecimal) decimalParser.parse(buffer, position);

        if (position.getErrorIndex() > -1) {
            return context.failure(message);
        }

        return context.success(result, position.getIndex());
    }

    @Override
    public Parser copy() {
        return new DecimalParser(locale, message);
    }
}
