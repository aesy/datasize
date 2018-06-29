package io.aesy.bytes.factory;

import io.aesy.bytes.parse.BytesParser;
import io.aesy.bytes.parse.StrictBytesParser;

import java.util.Locale;

public class StrictBytesParserFactory implements BytesParserFactory {
    @Override
    public BytesParser create() {
        return new StrictBytesParser();
    }

    @Override
    public BytesParser create(Locale locale) {
        return new StrictBytesParser(locale);
    }
}
