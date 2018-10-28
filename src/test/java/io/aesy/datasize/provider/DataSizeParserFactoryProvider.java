package io.aesy.datasize.provider;

import io.aesy.datasize.factory.LenientDataSizeParserFactory;
import io.aesy.datasize.factory.StrictDataSizeParserFactory;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

/**
 * Argument provider of factories for all {@code DataSizeParser} implementations.
 */
public class DataSizeParserFactoryProvider implements ArgumentsProvider {
    @Override
    public Stream<Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
            new LenientDataSizeParserFactory(),
            new StrictDataSizeParserFactory()
        ).map(Arguments::of);
    }
}
