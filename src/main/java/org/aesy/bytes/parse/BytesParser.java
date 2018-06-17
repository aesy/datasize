package org.aesy.bytes.parse;

import org.aesy.bytes.Bytes;

import java.text.ParseException;

public interface BytesParser {
    Bytes parse(String input) throws ParseException;
}
