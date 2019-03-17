package io.aesy.datasize;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

    public static List<DataUnit> values() {
        List<DataUnit> units = new ArrayList<>();
        units.add(BYTE);
        units.addAll(SI.values());
        units.addAll(IEC.values());
        units.addAll(JEDEC.values());

        return Collections.unmodifiableList(units);
    }

    public static class SI extends ByteUnit {
        public static final DataUnit KILOBYTE = new SI("kB", "kilobyte", 3);
        public static final DataUnit MEGABYTE = new SI("MB", "megabyte", 6);
        public static final DataUnit GIGABYTE = new SI("GB", "gigabyte", 9);
        public static final DataUnit TERABYTE = new SI("TB", "terabyte", 12);
        public static final DataUnit PETABYTE = new SI("PB", "petabyte", 15);
        public static final DataUnit EXABYTE = new SI("EB", "exabyte", 18);
        public static final DataUnit ZETTABYTE = new SI("ZB", "zettabyte", 21);
        public static final DataUnit YOTTABYTE = new SI("YB", "yottabyte", 24);

        private static final List<DataUnit> ALL;

        static {
            ALL = Collections.unmodifiableList(
                Arrays.asList(KILOBYTE, MEGABYTE, GIGABYTE, TERABYTE, PETABYTE, EXABYTE, ZETTABYTE, YOTTABYTE));
        }

        protected SI(String abbreviation, String name, int exponent) {
            super(abbreviation, name, 10, exponent);
        }

        public static List<DataUnit> values() {
            return ALL;
        }
    }

    public static class IEC extends ByteUnit {
        public static final DataUnit KIBIBYTE = new IEC("KiB", "kibibyte", 10);
        public static final DataUnit MEBIBYTE = new IEC("MiB", "mebibyte", 20);
        public static final DataUnit GIBIBYTE = new IEC("GiB", "gibibyte", 30);
        public static final DataUnit TEBIBYTE = new IEC("TiB", "tebibyte", 40);
        public static final DataUnit PEBIBYTE = new IEC("PiB", "pebibyte", 50);
        public static final DataUnit EXBIBYTE = new IEC("EiB", "exbibyte", 60);
        public static final DataUnit ZEBIBYTE = new IEC("ZiB", "zebibyte", 70);
        public static final DataUnit YOBIBYTE = new IEC("YiB", "yobibyte", 80);

        private static final List<DataUnit> ALL;

        static {
            ALL = Collections.unmodifiableList(
                Arrays.asList(KIBIBYTE, MEBIBYTE, GIBIBYTE, TEBIBYTE, PEBIBYTE, EXBIBYTE, ZEBIBYTE, YOBIBYTE));
        }

        protected IEC(String abbreviation, String name, int exponent) {
            super(abbreviation, name, 2, exponent);
        }

        public static List<DataUnit> values() {
            return ALL;
        }
    }

    public static class JEDEC extends ByteUnit {
        public static final DataUnit KILOBYTE = new JEDEC("KB", "kilobyte", 10);
        public static final DataUnit MEGABYTE = new JEDEC("MB", "megabyte", 20);
        public static final DataUnit GIGABYTE = new JEDEC("GB", "gigabyte", 30);

        private static final List<DataUnit> ALL;

        static {
            ALL = Collections.unmodifiableList(Arrays.asList(KILOBYTE, MEGABYTE, GIGABYTE));
        }

        protected JEDEC(String abbreviation, String name, int exponent) {
            super(abbreviation, name, 2, exponent);
        }

        public static List<DataUnit> values() {
            return ALL;
        }
    }
}
