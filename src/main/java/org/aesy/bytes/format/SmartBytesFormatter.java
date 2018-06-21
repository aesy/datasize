package org.aesy.bytes.format;

import org.aesy.bytes.Bytes;
import org.aesy.bytes.convert.BytesConverter;
import org.aesy.bytes.convert.NaturalBytesConverter;

import java.util.Locale;
import java.util.Objects;

public class SmartBytesFormatter implements BytesFormatter {
    private static final int DEFAULT_PRECISION = 2;
    private static final BytesConverter humanReadableConverter = new NaturalBytesConverter();

    private final BytesFormatter formatter;

    public SmartBytesFormatter() {
        this(Locale.getDefault(Locale.Category.FORMAT), DEFAULT_PRECISION);
    }

    public SmartBytesFormatter(Locale locale) {
        this(locale, DEFAULT_PRECISION);
    }

    public SmartBytesFormatter(int precision) {
        this(Locale.getDefault(Locale.Category.FORMAT), precision);
    }

    public SmartBytesFormatter(Locale locale, int precision) {
        Objects.requireNonNull(locale);

        this.formatter = new SimpleBytesFormatter(locale, precision);
    }

    @Override
    public String format(Bytes bytes) {
        Objects.requireNonNull(bytes);

        return formatter.format(humanReadableConverter.convert(bytes));
    }
}
