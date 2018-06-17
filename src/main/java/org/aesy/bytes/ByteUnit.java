package org.aesy.bytes;

public interface ByteUnit {
    String getAbbreviation();
    String getName();
    int getBase();
    int getExponent();
    Bytes convert(Bytes value);
}
