package io.aesy.datasize.factory;

import io.aesy.datasize.convert.DataSizeConverter;
import io.aesy.datasize.convert.SimpleNaturalDataSizeConverter;

public class SimpleNaturalDataSizeConverterFactory implements NaturalDataSizeConverterFactory {
    @Override
    public DataSizeConverter create() {
        return new SimpleNaturalDataSizeConverter();
    }
}
