package org.aesy.bytes.format;

import org.aesy.bytes.Bytes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

public class SimpleBytesFormatter implements BytesFormatter {
    private static final int DEFAULT_PRECISION = 2;

    private Locale locale;
    private int precision;

    public SimpleBytesFormatter() {
        this(Locale.getDefault(Locale.Category.FORMAT), DEFAULT_PRECISION);
    }

    public SimpleBytesFormatter(Locale locale) {
        this(locale, DEFAULT_PRECISION);
    }

    public SimpleBytesFormatter(int precision) {
        this(Locale.getDefault(Locale.Category.FORMAT), precision);
    }

    public SimpleBytesFormatter(Locale locale, int precision) {
        Objects.requireNonNull(locale);

        if (precision < 0) {
            throw new IllegalArgumentException("Precision must not be less than zero");
        }

        this.locale = locale;
        this.precision = precision;
    }

    @Override
    public String format(Bytes bytes) {
        Objects.requireNonNull(bytes);

        BigDecimal value = bytes.getValue();
        int scale = value.stripTrailingZeros().scale();

        NumberFormat formatter = DecimalFormat.getNumberInstance(locale);
        formatter.setRoundingMode(RoundingMode.HALF_UP);
        formatter.setMinimumFractionDigits(Math.max(0, Math.min(precision, scale)));
        formatter.setMaximumFractionDigits(precision);

        String formattedValue = formatter.format(value);
        String unitAbbreviation = bytes.getUnit().getAbbreviation();

        return String.format("%s %s", formattedValue, unitAbbreviation);
    }
}
