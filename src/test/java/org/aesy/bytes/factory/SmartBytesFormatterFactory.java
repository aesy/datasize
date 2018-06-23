package org.aesy.bytes.factory;

import org.aesy.bytes.format.BytesFormatter;
import org.aesy.bytes.format.SmartBytesFormatter;

import java.util.Locale;

public class SmartBytesFormatterFactory implements BytesFormatterFactory {
    @Override
    public BytesFormatter create() {
        return new SmartBytesFormatter();
    }

    @Override
    public BytesFormatter create(int precision) {
        return new SmartBytesFormatter(precision);
    }

    @Override
    public BytesFormatter create(Locale locale) {
        return new SmartBytesFormatter(locale);
    }

    @Override
    public BytesFormatter create(Locale locale, int precision) {
        return new SmartBytesFormatter(locale, precision);
    }
}
