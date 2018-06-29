package io.aesy.bytes.factory;

import io.aesy.bytes.convert.BytesConverter;
import io.aesy.bytes.convert.SimpleNaturalBytesConverter;

public class SimpleNaturalBytesConverterFactory implements NaturalBytesConverterFactory {
    @Override
    public BytesConverter create() {
        return new SimpleNaturalBytesConverter();
    }
}
