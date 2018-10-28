package io.aesy.datasize.factory;

import io.aesy.datasize.format.DataSizeFormatter;
import io.aesy.datasize.format.SmartDataSizeFormatter;

import java.util.Locale;

public class SmartDataSizeFormatterFactory implements DataSizeFormatterFactory {
    @Override
    public DataSizeFormatter create() {
        return new SmartDataSizeFormatter();
    }

    @Override
    public DataSizeFormatter create(int precision) {
        return new SmartDataSizeFormatter(precision);
    }

    @Override
    public DataSizeFormatter create(Locale locale) {
        return new SmartDataSizeFormatter(locale);
    }

    @Override
    public DataSizeFormatter create(Locale locale, int precision) {
        return new SmartDataSizeFormatter(locale, precision);
    }
}
