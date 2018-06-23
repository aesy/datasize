package org.aesy.bytes.factory;

import org.aesy.bytes.format.BytesFormatter;
import org.aesy.bytes.format.SimpleBytesFormatter;

import java.util.Locale;

public class SimpleBytesFormatterFactory implements BytesFormatterFactory {
    @Override
    public BytesFormatter create() {
        return new SimpleBytesFormatter();
    }

    @Override
    public BytesFormatter create(int precision) {
        return new SimpleBytesFormatter(precision);
    }

    @Override
    public BytesFormatter create(Locale locale) {
        return new SimpleBytesFormatter(locale);
    }

    @Override
    public BytesFormatter create(Locale locale, int precision) {
        return new SimpleBytesFormatter(locale, precision);
    }
}
