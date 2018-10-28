package io.aesy.datasize.factory;

import io.aesy.datasize.format.DataSizeFormatter;
import io.aesy.datasize.format.SimpleDataSizeFormatter;

import java.util.Locale;

public class SimpleDataSizeFormatterFactory implements DataSizeFormatterFactory {
    @Override
    public DataSizeFormatter create() {
        return new SimpleDataSizeFormatter();
    }

    @Override
    public DataSizeFormatter create(int precision) {
        return new SimpleDataSizeFormatter(precision);
    }

    @Override
    public DataSizeFormatter create(Locale locale) {
        return new SimpleDataSizeFormatter(locale);
    }

    @Override
    public DataSizeFormatter create(Locale locale, int precision) {
        return new SimpleDataSizeFormatter(locale, precision);
    }
}
