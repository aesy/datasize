package io.aesy.bytes.factory;

import io.aesy.bytes.convert.BytesConverter;
import io.aesy.bytes.convert.SmartNaturalBytesConverter;

public class SmartNaturalBytesConverterFactory implements NaturalBytesConverterFactory {
    @Override
    public BytesConverter create() {
        return new SmartNaturalBytesConverter();
    }
}
