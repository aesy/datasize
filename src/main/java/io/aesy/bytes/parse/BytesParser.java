package io.aesy.bytes.parse;

import io.aesy.bytes.Bytes;

import java.text.ParseException;

/**
 * A {@code BytesParser} parses a string to produce a {@code Bytes} object.
 */
public interface BytesParser {
    /**
     * Parses an input string to produce a {@code Bytes} object.
     *
     * @param input The input string to parse
     * @return The produced {@code Bytes} object
     * @throws ParseException If the input string could not be parsed
     * @throws IllegalArgumentException If the input string is null
     */
    Bytes parse(String input) throws ParseException;
}
