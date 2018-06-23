package org.aesy.bytes;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;

/**
 * {@code ByteUnits} contains a set of {@code ByteUnit.Standard} which in
 * turn contain a set of {@code ByteUnit}.
 */
public final class ByteUnits {
    public static final Common COMMON = new Common();
    public static final SI SI = new SI();
    public static final IEC IEC = new IEC();
    public static final JEDEC JEDEC = new JEDEC();

    public static final ByteUnit.Standard ALL = new BasicStandard() {
        @Override
        public boolean has(ByteUnit unit) {
            return true;
        }

        @Override
        public List<ByteUnit> units() {
            Set<ByteUnit> units = new HashSet<>();
            units.addAll(SI.units());
            units.addAll(IEC.units());
            units.addAll(JEDEC.units());

            return new ArrayList<>(units);
        }
    };

    public static class Common extends BasicStandard {
        // TODO bit

        /**
         * A unit that represents a byte, or 8 bits.
         */
        public final ByteUnit BYTE = new BasicByteUnit("B", "byte", 1, 0, COMMON) {
            @Override
            public Bytes convert(Bytes value) {
                Objects.requireNonNull(value);

                ByteUnit unit = value.getUnit();
                BigDecimal base = BigDecimal.valueOf(unit.getBase());
                int exponent = unit.getExponent();

                BigDecimal newValue = value.getValue()
                                           .multiply(base.pow(exponent));

                return Bytes.valueOf(newValue, this);
            }
        };

        private Common() {}

        @Override
        public List<ByteUnit> units() {
            return Arrays.asList(BYTE);
        }
    }

    public static class SI extends BasicStandard {
        public final ByteUnit BYTE      = COMMON.BYTE;
        public final ByteUnit KILOBYTE  = new BasicByteUnit("kB", "kilobyte", 10, 3, this);
        public final ByteUnit MEGABYTE  = new BasicByteUnit("MB", "megabyte", 10, 6, this);
        public final ByteUnit GIGABYTE  = new BasicByteUnit("GB", "gigabyte", 10, 9, this);
        public final ByteUnit TERABYTE  = new BasicByteUnit("TB", "terabyte", 10, 12, this);
        public final ByteUnit PETABYTE  = new BasicByteUnit("PB", "petabyte", 10, 15, this);
        public final ByteUnit EXABYTE   = new BasicByteUnit("EB", "exabyte", 10, 18, this);
        public final ByteUnit ZETTABYTE = new BasicByteUnit("ZB", "zettabyte", 10, 21, this);
        public final ByteUnit YOTTABYTE = new BasicByteUnit("YB", "yottabyte", 10, 24, this);

        private SI() {}

        public List<ByteUnit> units() {
            return Arrays.asList(BYTE, KILOBYTE, MEGABYTE, GIGABYTE, TERABYTE, PETABYTE, EXABYTE, ZETTABYTE, YOTTABYTE);
        }
    }

    public static class IEC extends BasicStandard {
        public final ByteUnit BYTE     = COMMON.BYTE;
        public final ByteUnit KIBIBYTE = new BasicByteUnit("KiB", "kibibyte", 2, 10, this);
        public final ByteUnit MEBIBYTE = new BasicByteUnit("MiB", "mebibyte", 2, 20, this);
        public final ByteUnit GIBIBYTE = new BasicByteUnit("GiB", "gibibyte", 2, 30, this);
        public final ByteUnit TEBIBYTE = new BasicByteUnit("TiB", "tebibyte", 2, 40, this);
        public final ByteUnit PEBIBYTE = new BasicByteUnit("PiB", "pebibyte", 2, 50, this);
        public final ByteUnit EXBIBYTE = new BasicByteUnit("EiB", "exbibyte", 2, 60, this);
        public final ByteUnit ZEBIBYTE = new BasicByteUnit("ZiB", "zebibyte", 2, 70, this);
        public final ByteUnit YOBIBYTE = new BasicByteUnit("YiB", "yobibyte", 2, 80, this);

        private IEC() {}

        @Override
        public List<ByteUnit> units() {
            return Arrays.asList(BYTE, KIBIBYTE, MEBIBYTE, GIBIBYTE, TEBIBYTE, PEBIBYTE, EXBIBYTE, ZEBIBYTE, YOBIBYTE);
        }
    }

    public static class JEDEC extends BasicStandard {
        public final ByteUnit BYTE     = COMMON.BYTE;
        public final ByteUnit KILOBYTE = new BasicByteUnit("KB", "kilobyte", 2, 10, this);
        public final ByteUnit MEGABYTE = new BasicByteUnit("MB", "megabyte", 2, 20, this);
        public final ByteUnit GIGABYTE = new BasicByteUnit("GB", "gigabyte", 2, 30, this);

        private JEDEC() {}

        @Override
        public List<ByteUnit> units() {
            return Arrays.asList(BYTE, KILOBYTE, MEGABYTE, GIGABYTE);
        }
    }

    /**
     * Basic ByteUnit.Standard implementation fit for most cases.
     */
    private static class BasicStandard implements ByteUnit.Standard {
        private BasicStandard() {}

        @Override
        public boolean has(ByteUnit unit) {
            Objects.requireNonNull(unit);

            return units().contains(unit);
        }

        @Override
        public List<ByteUnit> units() {
            return Collections.emptyList();
        }

        @Override
        public String toString() {
            return getClass().getName();
        }
    }

    /**
     * Basic ByteUnit implementation fit for most cases.
     */
    private static class BasicByteUnit implements ByteUnit {
        private final String abbreviation;
        private final String name;
        private final int base;
        private final int exponent;
        private final ByteUnit.Standard standard;

        private BasicByteUnit(String abbreviation, String name, int base, int exponent, ByteUnit.Standard standard) {
            this.abbreviation = abbreviation;
            this.name = name;
            this.base = base;
            this.exponent = exponent;
            this.standard = standard;
        }

        public String getAbbreviation() {
            return abbreviation;
        }

        public String getName() {
            return name;
        }

        public int getBase() {
            return base;
        }

        public int getExponent() {
            return exponent;
        }

        public ByteUnit.Standard getStandard() {
            return standard;
        }

        public Bytes convert(Bytes value) {
            Objects.requireNonNull(value);

            BigDecimal bytes = ByteUnits.COMMON.BYTE.convert(value).getValue();
            BigDecimal base = BigDecimal.valueOf(getBase());
            BigDecimal newValue = bytes.divide(base.pow(getExponent()), MathContext.UNLIMITED);

            return Bytes.valueOf(newValue, this);
        }

        @Override
        public String toString() {
            return getAbbreviation();
        }
    }

    // TODO BasicBitUnit
}
