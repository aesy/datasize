package io.aesy.bytes.factory;

import io.aesy.bytes.parse.BytesParser;
import io.aesy.bytes.parse.LenientBytesParser;

import java.util.Locale;

public class LenientBytesParserFactory implements BytesParserFactory {
    @Override
    public BytesParser create() {
        return new LenientBytesParser();
    }

    @Override
    public BytesParser create(Locale locale) {
        return new LenientBytesParser(locale);
    }
}
