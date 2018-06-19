package org.aesy.bytes;

import org.aesy.bytes.format.BytesFormatter;
import org.aesy.bytes.format.SmartBytesFormatter;
import org.aesy.bytes.parse.BytesParser;
import org.aesy.bytes.parse.SmartBytesParser;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.Objects;

public class Bytes implements Comparable<Bytes> {
    private final BigDecimal value;
    private final ByteUnit unit;

    private Bytes(BigDecimal value, ByteUnit unit) {
        Objects.requireNonNull(value);
        Objects.requireNonNull(unit);

        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Value must not be less than zero");
        }

        this.value = value;
        this.unit = unit;
    }

    public static Bytes valueOf(long value, ByteUnit unit) {
        return new Bytes(BigDecimal.valueOf(value), unit);
    }

    public static Bytes valueOf(double value, ByteUnit unit) {
        return new Bytes(BigDecimal.valueOf(value), unit);
    }

    public static Bytes valueOf(BigInteger value, ByteUnit unit) {
        return new Bytes(new BigDecimal(value), unit);
    }

    public static Bytes valueOf(BigDecimal value, ByteUnit unit) {
        return new Bytes(value, unit);
    }

    public static Bytes parse(String input) throws ParseException {
        Objects.requireNonNull(input);

        BytesParser parser = new SmartBytesParser();

        return parser.parse(input);
    }

    public BigDecimal getValue() {
        return value;
    }

    public ByteUnit getUnit() {
        return unit;
    }

    @Override
    public int compareTo(Bytes other) {
        Objects.requireNonNull(other);

        BigDecimal thisBytes  = ByteUnit.BYTE.convert(this).getValue();
        BigDecimal otherBytes = ByteUnit.BYTE.convert(other).getValue();

        return thisBytes.compareTo(otherBytes);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        return compareTo((Bytes) object) == 0;
    }

    @Override
    public String toString() {
        BytesFormatter formatter = new SmartBytesFormatter(2);

        return formatter.toShortString(this);
    }
}
