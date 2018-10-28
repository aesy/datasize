package io.aesy.datasize.factory;

import io.aesy.datasize.parse.DataSizeParser;
import io.aesy.datasize.parse.StrictDataSizeParser;

import java.util.Locale;

public class StrictDataSizeParserFactory implements DataSizeParserFactory {
    @Override
    public DataSizeParser create() {
        return new StrictDataSizeParser();
    }

    @Override
    public DataSizeParser create(Locale locale) {
        return new StrictDataSizeParser(locale);
    }
}
