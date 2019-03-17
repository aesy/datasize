package io.aesy.datasize;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("javadocVariable")
public class BitUnit implements DataUnit {
    public static final DataUnit BIT;

    private static final MathContext CONTEXT;

    static {
        CONTEXT = MathContext.UNLIMITED;
        BIT = new BitUnit("bit", "bit", 2, 0);
    }

    private final String abbreviation;
    private final String name;
    private final BigDecimal bytes;

    protected BitUnit(String abbreviation, String name, int base, int exponent) {
        this.abbreviation = abbreviation;
        this.name = name;
        this.bytes = BigDecimal.valueOf(base)
                               .pow(exponent, CONTEXT)
                               .divide(BigDecimal.valueOf(8), MathContext.UNLIMITED);
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
        units.add(BIT);
        units.addAll(SI.values());
        units.addAll(IEC.values());
        units.addAll(JEDEC.values());

        return Collections.unmodifiableList(units);
    }

    public static class SI extends BitUnit {
        public static final DataUnit KILOBIT = new SI("kbit", "kilobit", 3);
        public static final DataUnit MEGABIT = new SI("mbit", "megabit", 6);
        public static final DataUnit GIGABIT = new SI("gbit", "gigabit", 9);
        public static final DataUnit TERABIT = new SI("tbit", "terabit", 12);
        public static final DataUnit PETABIT = new SI("pbit", "petabit", 15);
        public static final DataUnit EXABIT = new SI("ebit", "exabit", 18);
        public static final DataUnit ZETTABIT = new SI("zbit", "zettabit", 21);
        public static final DataUnit YOTTABIT = new SI("ybit", "yottabit", 24);

        private static final List<DataUnit> ALL;

        static {
            ALL = Collections.unmodifiableList(
                Arrays.asList(KILOBIT, MEGABIT, GIGABIT, TERABIT, PETABIT, EXABIT, ZETTABIT, YOTTABIT));
        }

        protected SI(String abbreviation, String name, int exponent) {
            super(abbreviation, name, 10, exponent);
        }

        public static List<DataUnit> values() {
            return ALL;
        }
    }

    public static class IEC extends BitUnit {
        public static final DataUnit KIBIBIT = new IEC("Kibit", "kibibit", 10);
        public static final DataUnit MEBIBIT = new IEC("Mibit", "mebibit", 20);
        public static final DataUnit GIBIBIT = new IEC("Gibit", "gibibit", 30);
        public static final DataUnit TEBIBIT = new IEC("Tibit", "tebibit", 40);
        public static final DataUnit PEBIBIT = new IEC("Pibit", "pebibit", 50);
        public static final DataUnit EXBIBIT = new IEC("Eibit", "exbibit", 60);
        public static final DataUnit ZEBIBIT = new IEC("Zibit", "zebibit", 70);
        public static final DataUnit YOBIBIT = new IEC("Yibit", "yobibit", 80);

        private static final List<DataUnit> ALL;

        static {
            ALL = Collections.unmodifiableList(
                Arrays.asList(KIBIBIT, MEBIBIT, GIBIBIT, TEBIBIT, PEBIBIT, EXBIBIT, ZEBIBIT, YOBIBIT));
        }

        protected IEC(String abbreviation, String name, int exponent) {
            super(abbreviation, name, 2, exponent);
        }
        public static List<DataUnit> values() {
            return ALL;
        }
    }

    public static class JEDEC extends BitUnit {
        public static final DataUnit KILOBIT = new JEDEC("Kbit", "kilobit", 10);
        public static final DataUnit MEGABIT = new JEDEC("Mbit", "megabit", 20);
        public static final DataUnit GIGABIT = new JEDEC("Gbit", "gigabit", 30);

        private static final List<DataUnit> ALL;

        static {
            ALL = Collections.unmodifiableList(Arrays.asList(KILOBIT, MEGABIT, GIGABIT));
        }

        protected JEDEC(String abbreviation, String name, int exponent) {
            super(abbreviation, name, 2, exponent);
        }

        public static List<DataUnit> values() {
            return ALL;
        }
    }
}
