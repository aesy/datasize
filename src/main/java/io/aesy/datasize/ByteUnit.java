package io.aesy.datasize;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("javadocVariable")
public class ByteUnit implements DataUnit {
    public static final DataUnit BYTE;

    private static final MathContext CONTEXT;

    static {
        CONTEXT = MathContext.UNLIMITED;
        BYTE = new ByteUnit("B", "byte", 2, 0);
    }

    private final String abbreviation;
    private final String name;
    private final BigDecimal bytes;

    protected ByteUnit(String abbreviation, String name, int base, int exponent) {
        this.abbreviation = abbreviation;
        this.name = name;
        this.bytes = BigDecimal.valueOf(base)
                               .pow(exponent, CONTEXT);
    }

    @Override
    public String getAbbreviation() {
        return abbreviation;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public BigDecimal bytes() {
        return bytes;
    }

    @Override
    public String toString() {
        return abbreviation;
    }

    public static final class SI {
        public static final DataUnit KILOBYTE = new ByteUnit("kB", "kilobyte", 10, 3);
        public static final DataUnit MEGABYTE = new ByteUnit("MB", "megabyte", 10, 6);
        public static final DataUnit GIGABYTE = new ByteUnit("GB", "gigabyte", 10, 9);
        public static final DataUnit TERABYTE = new ByteUnit("TB", "terabyte", 10, 12);
        public static final DataUnit PETABYTE = new ByteUnit("PB", "petabyte", 10, 15);
        public static final DataUnit EXABYTE = new ByteUnit("EB", "exabyte", 10, 18);
        public static final DataUnit ZETTABYTE = new ByteUnit("ZB", "zettabyte", 10, 21);
        public static final DataUnit YOTTABYTE = new ByteUnit("YB", "yottabyte", 10, 24);

        private SI() {}

        public static List<DataUnit> values() {
            return Arrays.asList(KILOBYTE, MEGABYTE, GIGABYTE, TERABYTE, PETABYTE, EXABYTE,
                                 ZETTABYTE, YOTTABYTE);
        }
    }

    public static final class IEC {
        public static final DataUnit KIBIBYTE = new ByteUnit("KiB", "kibibyte", 2, 10);
        public static final DataUnit MEBIBYTE = new ByteUnit("MiB", "mebibyte", 2, 20);
        public static final DataUnit GIBIBYTE = new ByteUnit("GiB", "gibibyte", 2, 30);
        public static final DataUnit TEBIBYTE = new ByteUnit("TiB", "tebibyte", 2, 40);
        public static final DataUnit PEBIBYTE = new ByteUnit("PiB", "pebibyte", 2, 50);
        public static final DataUnit EXBIBYTE = new ByteUnit("EiB", "exbibyte", 2, 60);
        public static final DataUnit ZEBIBYTE = new ByteUnit("ZiB", "zebibyte", 2, 70);
        public static final DataUnit YOBIBYTE = new ByteUnit("YiB", "yobibyte", 2, 80);

        private IEC() {}

        public static List<DataUnit> values() {
            return Arrays.asList(KIBIBYTE, MEBIBYTE, GIBIBYTE, TEBIBYTE, PEBIBYTE, EXBIBYTE,
                                 ZEBIBYTE, YOBIBYTE);
        }
    }

    public static final class JEDEC {
        public static final DataUnit KILOBYTE = new ByteUnit("KB", "kilobyte", 2, 10);
        public static final DataUnit MEGABYTE = new ByteUnit("MB", "megabyte", 2, 20);
        public static final DataUnit GIGABYTE = new ByteUnit("GB", "gigabyte", 2, 30);

        private JEDEC() {}

        public static List<DataUnit> values() {
            return Arrays.asList(KILOBYTE, MEGABYTE, GIGABYTE);
        }
    }
}
