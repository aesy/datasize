package io.aesy.bytes;

import java.util.List;

public interface ByteUnit {
    String getAbbreviation();
    String getName();
    int getBase();
    int getExponent();
    Standard getStandard();
    Bytes convert(Bytes value);

    interface Standard {
        boolean has(ByteUnit unit);
        List<ByteUnit> units();
    }
}
