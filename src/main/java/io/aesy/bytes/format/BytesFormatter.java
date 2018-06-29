package io.aesy.bytes.format;

import io.aesy.bytes.Bytes;

/**
 * A {@code BytesFormatter} formats {@code Bytes} objects.
 */
public interface BytesFormatter {
    /**
     * Formats a {@code Bytes} object to produce a string.
     *
     * @param bytes The Bytes to format
     * @return The formatted {@code Bytes} string
     * @throws IllegalArgumentException If the bytes object is null
     */
    String format(Bytes bytes);
}
