package io.aesy.datasize.factory;

import io.aesy.datasize.convert.DataSizeConverter;
import io.aesy.datasize.convert.SmartNaturalDataSizeConverter;

public class SmartNaturalDataSizeConverterFactory implements NaturalDataSizeConverterFactory {
    @Override
    public DataSizeConverter create() {
        return new SmartNaturalDataSizeConverter();
    }
}
