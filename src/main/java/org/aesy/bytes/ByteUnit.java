package org.aesy.bytes;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Objects;

public interface ByteUnit {
    ByteUnit BYTE = new ByteUnit() {
        @Override
        public String getAbbreviation() {
            return "B";
        }

        @Override
        public String getName() {
            return "byte";
        }

        @Override
        public int getBase() {
            return 1;
        }

        @Override
        public int getExponent() {
            return 0;
        }

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

    enum SI implements ByteUnit {
        KILOBYTE ("kB", "kilobyte",  10,  3),
        MEGABYTE ("MB", "megabyte",  10,  6),
        GIGABYTE ("GB", "gigabyte",  10,  9),
        TERABYTE ("TB", "terabyte",  10, 12),
        PETABYTE ("PB", "petabyte",  10, 15),
        EXABYTE  ("EB", "exabyte",   10, 18),
        ZETTABYTE("ZB", "zettabyte", 10, 21),
        YOTTABYTE("YB", "yottabyte", 10, 24);

        private final String abbreviation;
        private final String name;
        private final int base;
        private final int exponent;

        SI(String abbreviation, String name, int base, int exponent) {
            this.abbreviation = abbreviation;
            this.name = name;
            this.base = base;
            this.exponent = exponent;
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

        @Override
        public String toString() {
            return abbreviation;
        }
    }

    enum IEC implements ByteUnit {
        KIBIBYTE("KiB", "kibibyte", 2, 10),
        MEBIBYTE("MiB", "mebibyte", 2, 20),
        GIBIBYTE("GiB", "gibibyte", 2, 30),
        TEBIBYTE("TiB", "tebibyte", 2, 40),
        PEBIBYTE("PiB", "pebibyte", 2, 50),
        EXBIBYTE("EiB", "exbibyte", 2, 60),
        ZEBIBYTE("ZiB", "zebibyte", 2, 70),
        YOBIBYTE("YiB", "yobibyte", 2, 80);

        private final String abbreviation;
        private final String name;
        private final int base;
        private final int exponent;

        IEC(String abbreviation, String name, int base, int exponent) {
            this.abbreviation = abbreviation;
            this.name = name;
            this.base = base;
            this.exponent = exponent;
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

        @Override
        public String toString() {
            return abbreviation;
        }
    }

    enum JEDEC implements ByteUnit {
        KILOBYTE ("KB", "kilobyte",  2, 10),
        MEGABYTE ("MB", "megabyte",  2, 20),
        GIGABYTE ("GB", "gigabyte",  2, 30),
        TERABYTE ("TB", "terabyte",  2, 40),
        PETABYTE ("PB", "petabyte",  2, 50),
        EXABYTE  ("EB", "exabyte",   2, 60),
        ZETTABYTE("ZB", "zettabyte", 2, 70),
        YOTTABYTE("YB", "yottabyte", 2, 80);

        private final String abbreviation;
        private final String name;
        private final int base;
        private final int exponent;

        JEDEC(String abbreviation, String name, int base, int exponent) {
            this.abbreviation = abbreviation;
            this.name = name;
            this.base = base;
            this.exponent = exponent;
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

        @Override
        public String toString() {
            return abbreviation;
        }
    }

    String getAbbreviation();
    String getName();
    int getBase();
    int getExponent();

    default Bytes convert(Bytes value) {
        Objects.requireNonNull(value);

        BigDecimal bytes = ByteUnit.BYTE.convert(value).getValue();
        BigDecimal base = BigDecimal.valueOf(getBase());
        BigDecimal newValue = bytes.divide(base.pow(getExponent()), MathContext.UNLIMITED);

        return Bytes.valueOf(newValue, this);
    }
}
