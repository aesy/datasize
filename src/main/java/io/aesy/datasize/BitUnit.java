package io.aesy.datasize;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("javadocVariable")
public final class BitUnit implements DataUnit {
    public static final DataUnit BIT;

    private static final MathContext CONTEXT;

    static {
        CONTEXT = MathContext.UNLIMITED;
        BIT = new BitUnit("bit", "bit", 2, 0);
    }

    private final String abbreviation;
    private final String name;
    private final BigDecimal bytes;

    private BitUnit(String abbreviation, String name, int base, int exponent) {
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
        public static final DataUnit KILOBIT = new BitUnit("kbit", "kilobit", 10, 3);
        public static final DataUnit MEGABIT = new BitUnit("mbit", "megabit", 10, 6);
        public static final DataUnit GIGABIT = new BitUnit("gbit", "gigabit", 10, 9);
        public static final DataUnit TERABIT = new BitUnit("tbit", "terabit", 10, 12);
        public static final DataUnit PETABIT = new BitUnit("pbit", "petabit", 10, 15);
        public static final DataUnit EXABIT = new BitUnit("ebit", "exabit", 10, 18);
        public static final DataUnit ZETTABIT = new BitUnit("zbit", "zettabit", 10, 21);
        public static final DataUnit YOTTABIT = new BitUnit("ybit", "yottabit", 10, 24);

        private SI() {}

        public static List<DataUnit> values() {
            return Arrays.asList(KILOBIT, MEGABIT, GIGABIT, TERABIT, PETABIT, EXABIT, ZETTABIT,
                                 YOTTABIT);
        }
    }

    public static final class IEC {
        public static final DataUnit KIBIBIT = new BitUnit("Kibit", "kibibit", 2, 10);
        public static final DataUnit MEBIBIT = new BitUnit("Mibit", "mebibit", 2, 20);
        public static final DataUnit GIBIBIT = new BitUnit("Gibit", "gibibit", 2, 30);
        public static final DataUnit TEBIBIT = new BitUnit("Tibit", "tebibit", 2, 40);
        public static final DataUnit PEBIBIT = new BitUnit("Pibit", "pebibit", 2, 50);
        public static final DataUnit EXBIBIT = new BitUnit("Eibit", "exbibit", 2, 60);
        public static final DataUnit ZEBIBIT = new BitUnit("Zibit", "zebibit", 2, 70);
        public static final DataUnit YOBIBIT = new BitUnit("Yibit", "yobibit", 2, 80);

        private IEC() {}

        public static List<DataUnit> values() {
            return Arrays.asList(KIBIBIT, MEBIBIT, GIBIBIT, TEBIBIT, PEBIBIT, EXBIBIT, ZEBIBIT,
                                 YOBIBIT);
        }
    }

    public static final class JEDEC {
        public static final DataUnit KILOBIT = new BitUnit("Kbit", "kilobit", 2, 10);
        public static final DataUnit MEGABIT = new BitUnit("Mbit", "megabit", 2, 20);
        public static final DataUnit GIGABIT = new BitUnit("Gbit", "gigabit", 2, 30);

        private JEDEC() {}

        public static List<DataUnit> values() {
            return Arrays.asList(KILOBIT, MEGABIT, GIGABIT);
        }
    }
}
