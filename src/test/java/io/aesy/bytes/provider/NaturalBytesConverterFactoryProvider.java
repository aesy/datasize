package io.aesy.bytes.provider;

import io.aesy.bytes.factory.SimpleNaturalBytesConverterFactory;
import io.aesy.bytes.factory.SmartNaturalBytesConverterFactory;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

/**
 * Argument provider of factories for all {@code BytesConverter} implementations that convert
 * to a more natural representation unit.
 */
public class NaturalBytesConverterFactoryProvider implements ArgumentsProvider {
    @Override
    public Stream<Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
            new SimpleNaturalBytesConverterFactory(),
            new SmartNaturalBytesConverterFactory()
        ).map(Arguments::of);
    }
}
