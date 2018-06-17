package org.aesy.bytes.format;

import org.aesy.bytes.Bytes;

public interface BytesFormatter {
    String toShortString(Bytes bytes);
    String toLongString(Bytes bytes);
    String toHumanReadableShortString(Bytes bytes);
    String toHumanReadableLongString(Bytes bytes);
}
