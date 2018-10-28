package io.aesy.datasize.factory;

import io.aesy.datasize.parse.DataSizeParser;
import io.aesy.datasize.parse.LenientDataSizeParser;

import java.util.Locale;

public class LenientDataSizeParserFactory implements DataSizeParserFactory {
    @Override
    public DataSizeParser create() {
        return new LenientDataSizeParser();
    }

    @Override
    public DataSizeParser create(Locale locale) {
        return new LenientDataSizeParser(locale);
    }
}
