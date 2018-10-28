package io.aesy.datasize.format;

import io.aesy.datasize.DataSize;

/**
 * A {@code DataSizeFormatter} formats {@code DataSize} objects.
 */
@FunctionalInterface
public interface DataSizeFormatter {
    /**
     * Formats a {@code DataSize} object to produce a string.
     *
     * @param dataSize The {@code DataSize} object to format
     * @return The formatted {@code DataSize} string
     * @throws IllegalArgumentException If the given {@code DataSize} object is null
     */
    String format(DataSize dataSize);
}
