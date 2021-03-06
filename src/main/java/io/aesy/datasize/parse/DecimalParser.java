package io.aesy.datasize.parse;

import org.petitparser.context.Context;
import org.petitparser.context.Result;
import org.petitparser.parser.Parser;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

/* package-private */ class DecimalParser extends Parser {
    private final Locale locale;
    private final String message;

    /* package-private */ DecimalParser(Locale locale, String message) {
        this.locale = locale;
        this.message = message;
    }

    public static DecimalParser localized(Locale locale) {
        return new DecimalParser(locale, String.format("number expected in locale: %s", locale));
    }

    public static DecimalParser localized(Locale locale, String message) {
        return new DecimalParser(locale, message);
    }

    @Override
    public Result parseOn(Context context) {
        DecimalFormat decimalParser = (DecimalFormat) NumberFormat.getNumberInstance(locale);
        decimalParser.setParseBigDecimal(true);
        String buffer = context.getBuffer();
        int start = context.getPosition();

        ParsePosition position = new ParsePosition(start);
        Number result = decimalParser.parse(buffer, position);

        if (result instanceof Double) {
            result = BigDecimal.valueOf((Double) result);
        }

        int errorIndex = position.getErrorIndex();

        if (errorIndex > -1) {
            return context.failure(message, errorIndex);
        }

        return context.success(result, position.getIndex());
    }

    @Override
    public Parser copy() {
        return new DecimalParser(locale, message);
    }
}
