package io.aesy.bytes.convert;

import io.aesy.bytes.Bytes;

/**
 * A {@code BytesConverter} converts {@code Bytes} objects to other {@code Bytes} objects.
 */
public interface BytesConverter {
    /**
     * Converts a {@code Bytes} object to another {@code Bytes} object.
     *
     * @param bytes The Bytes to convert
     * @return The converted {@code Bytes} object
     * @throws IllegalArgumentException If the bytes object is null
     */
    Bytes convert(Bytes bytes);
}
