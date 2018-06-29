package io.aesy.bytes.provider;

import io.aesy.bytes.factory.LenientBytesParserFactory;
import io.aesy.bytes.factory.StrictBytesParserFactory;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

/**
 * Argument provider of factories for all {@code BytesParser} implementations.
 */
public class BytesParserFactoryProvider implements ArgumentsProvider {
    @Override
    public Stream<Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
            new LenientBytesParserFactory(),
            new StrictBytesParserFactory()
        ).map(Arguments::of);
    }
}
