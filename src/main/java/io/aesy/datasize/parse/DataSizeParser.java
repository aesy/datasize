package io.aesy.datasize.parse;

import io.aesy.datasize.DataSize;

import java.text.ParseException;

/**
 * A {@code DataSizeParser} parses a string to produce a {@code DataSize} object.
 */
@FunctionalInterface
public interface DataSizeParser {
    /**
     * Parses an input string to produce a {@code DataSize} object.
     *
     * @param input The input string to parse
     * @return The produced {@code DataSize} object
     * @throws ParseException If the given input string could not be parsed
     * @throws IllegalArgumentException If the given input string is null
     */
    DataSize parse(String input) throws ParseException;
}
