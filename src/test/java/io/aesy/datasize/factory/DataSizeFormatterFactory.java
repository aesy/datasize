package io.aesy.datasize.factory;

import io.aesy.datasize.format.DataSizeFormatter;

import java.util.Locale;

public interface DataSizeFormatterFactory {
    DataSizeFormatter create();
    DataSizeFormatter create(int precision);
    DataSizeFormatter create(Locale locale);
    DataSizeFormatter create(Locale locale, int precision);
}
