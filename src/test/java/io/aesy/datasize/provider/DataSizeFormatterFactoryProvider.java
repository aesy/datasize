package io.aesy.datasize.provider;

import io.aesy.datasize.factory.SimpleDataSizeFormatterFactory;
import io.aesy.datasize.factory.SmartDataSizeFormatterFactory;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

/**
 * Argument provider of factories for all {@code DataSizeFormatter} implementations.
 */
public class DataSizeFormatterFactoryProvider implements ArgumentsProvider {
    @Override
    public Stream<Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
            new SimpleDataSizeFormatterFactory(),
            new SmartDataSizeFormatterFactory()
        ).map(Arguments::of);
    }
}
