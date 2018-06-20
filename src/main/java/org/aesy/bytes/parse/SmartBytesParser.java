package org.aesy.bytes.parse;

import org.aesy.bytes.ByteUnit;
import org.aesy.bytes.Bytes;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.*;

public class SmartBytesParser implements BytesParser {
    public static final List<ByteUnit> ORDER_PREFER_SI_OVER_JEDEC;
    public static final List<ByteUnit> ORDER_PREFER_JEDEC_OVER_SI;

    private static final List<ByteUnit> DEFAULT_ORDER;
    private static final boolean DEFAULT_STRICT;

    static {
        ORDER_PREFER_SI_OVER_JEDEC = new ArrayList<>();
        ORDER_PREFER_SI_OVER_JEDEC.add(ByteUnit.BYTE);
        ORDER_PREFER_SI_OVER_JEDEC.addAll(Arrays.asList(ByteUnit.SI.values()));
        ORDER_PREFER_SI_OVER_JEDEC.addAll(Arrays.asList(ByteUnit.IEC.values()));
        ORDER_PREFER_SI_OVER_JEDEC.addAll(Arrays.asList(ByteUnit.JEDEC.values()));

        ORDER_PREFER_JEDEC_OVER_SI = new ArrayList<>();
        ORDER_PREFER_JEDEC_OVER_SI.add(ByteUnit.BYTE);
        ORDER_PREFER_JEDEC_OVER_SI.addAll(Arrays.asList(ByteUnit.JEDEC.values()));
        ORDER_PREFER_JEDEC_OVER_SI.addAll(Arrays.asList(ByteUnit.IEC.values()));
        ORDER_PREFER_JEDEC_OVER_SI.addAll(Arrays.asList(ByteUnit.SI.values()));

        DEFAULT_ORDER = ORDER_PREFER_SI_OVER_JEDEC;
        DEFAULT_STRICT = false;
    }

    private boolean strict;
    private List<ByteUnit> order;
    private Locale locale;
    private NumberFormat decimalParser;

    public SmartBytesParser() {
        setLocale(Locale.getDefault(Locale.Category.FORMAT));
        setOrder(DEFAULT_ORDER);
        setStrict(DEFAULT_STRICT);
    }

    public void setStrict(boolean strict) {
        this.strict = strict;
    }

    public void setOrder(List<ByteUnit> order) {
        Objects.requireNonNull(order);

        this.order = order;
    }

    public void setLocale(Locale locale) {
        Objects.requireNonNull(locale);

        this.locale = locale;

        DecimalFormat decimalParser = (DecimalFormat) DecimalFormat.getNumberInstance(locale);
        decimalParser.setParseBigDecimal(true);
        this.decimalParser = decimalParser;
    }

    @Override
    public Bytes parse(String input) throws ParseException {
        Objects.requireNonNull(input);

        int i = -1;

        for (ByteUnit unit : order) {
            ParsePosition position = new ParsePosition(0);
            BigDecimal value = (BigDecimal) decimalParser.parse(input, position);
            int index = position.getIndex();
            int errorIndex = position.getErrorIndex();

            if (errorIndex > -1) {
                i = errorIndex;
                continue;
            }

            String actualEnding = input.substring(index);
            String expectedEnding = unit.getAbbreviation();

            if (!strict) {
                actualEnding = actualEnding.toLowerCase(locale);
                expectedEnding = expectedEnding.toLowerCase(locale);
            }

            int a = actualEnding.indexOf(expectedEnding);

            if (a == -1) {
                // Couldn't parse unit
                continue;
            }

            String s = input.substring(index, index + a);

            if (!s.trim().isEmpty()) {
                index += s.length() - s.trim().length();
                i = index;
                // Couldn't parse number to end
                continue;
            }

            String b = input.substring(index + s.length() - s.trim().length());

            if (!b.trim().isEmpty()) {
                index += s.length() + actualEnding.length() - actualEnding.trim().length();
                i = index;
                // Couldn't parse number to end
                continue;
            }

            return Bytes.valueOf(value, unit);
        }

        System.out.println(String.format("%s Error at index %d '(...)%s'", input, i, input.substring(i)));
        throw new ParseException(input, i);
    }

    public static void main(String[] args) {

    }
}
