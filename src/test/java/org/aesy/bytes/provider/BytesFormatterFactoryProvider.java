package org.aesy.bytes.provider;

import org.aesy.bytes.factory.SimpleBytesFormatterFactory;
import org.aesy.bytes.factory.SmartBytesFormatterFactory;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

/**
 * Argument provider of factories for all {@code BytesFormatter} implementations.
 */
public class BytesFormatterFactoryProvider implements ArgumentsProvider {
    @Override
    public Stream<Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
            new SimpleBytesFormatterFactory(),
            new SmartBytesFormatterFactory()
        ).map(Arguments::of);
    }
}
