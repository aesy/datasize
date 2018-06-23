package org.aesy.bytes.format;

import org.aesy.bytes.Bytes;

/**
 * A {@code BytesFormatter} formats {@code Bytes} objects.
 */
public interface BytesFormatter {
    /**
     * Formats a {@code Bytes} object to produce a string.
     *
     * @param bytes The Bytes to format
     * @return The formatted Bytes string
     * @throws NullPointerException If the bytes object is null
     */
    String format(Bytes bytes);
}
