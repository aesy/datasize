package io.aesy.datasize.convert;

import io.aesy.datasize.DataSize;

/**
 * A {@code DataSizeConverter} converts {@code DataSize} objects to other {@code DataSize} objects.
 */
@FunctionalInterface
public interface DataSizeConverter {
    /**
     * Converts a {@code DataSize} object to another {@code DataSize} object.
     *
     * @param dataSize The {@code DataSize} object  to convert
     * @return The converted {@code DataSize} object
     * @throws IllegalArgumentException If the given {@code DataSize} object is null
     */
    DataSize convert(DataSize dataSize);
}
