package io.aesy.datasize.factory;

import io.aesy.datasize.parse.DataSizeParser;

import java.util.Locale;

public interface DataSizeParserFactory {
    DataSizeParser create();
    DataSizeParser create(Locale locale);
}
