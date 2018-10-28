package io.aesy.datasize.provider;

import io.aesy.datasize.factory.SimpleNaturalDataSizeConverterFactory;
import io.aesy.datasize.factory.SmartNaturalDataSizeConverterFactory;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

/**
 * Argument provider of factories for all {@code DataSizeConverter} implementations that convert
 * to a more natural representation unit.
 */
public class NaturalDataSizeConverterFactoryProvider implements ArgumentsProvider {
    @Override
    public Stream<Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
            new SimpleNaturalDataSizeConverterFactory(),
            new SmartNaturalDataSizeConverterFactory()
        ).map(Arguments::of);
    }
}
