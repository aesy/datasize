package org.aesy.bytes;

import java.util.List;

public interface ByteUnit {
    interface Standard {
        boolean has(ByteUnit unit);
        List<ByteUnit> units();
    }

    String getAbbreviation();
    String getName();
    int getBase();
    int getExponent();
    Standard getStandard();
    Bytes convert(Bytes value);
}
