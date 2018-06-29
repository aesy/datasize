package io.aesy.bytes.factory;

import io.aesy.bytes.parse.BytesParser;

import java.util.Locale;

public interface BytesParserFactory {
    BytesParser create();
    BytesParser create(Locale locale);
}
