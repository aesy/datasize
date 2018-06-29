package io.aesy.bytes.factory;

import io.aesy.bytes.format.BytesFormatter;

import java.util.Locale;

public interface BytesFormatterFactory {
    BytesFormatter create();
    BytesFormatter create(int precision);
    BytesFormatter create(Locale locale);
    BytesFormatter create(Locale locale, int precision);
}
