package org.aesy.bytes.convert;

import org.aesy.bytes.Bytes;

/**
 * A {@code BytesConverter} converts {@code Bytes} objects to other
 * {@code Bytes} objects.
 */
public interface BytesConverter {
    /**
     * Converts a {@code Bytes} object to another {@code Bytes} object.
     *
     * @param bytes The Bytes to convert
     * @return The converted Bytes object
     * @throws NullPointerException If the bytes object is null
     */
    Bytes convert(Bytes bytes);
}
