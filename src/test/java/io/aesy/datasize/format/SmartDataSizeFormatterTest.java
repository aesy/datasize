package io.aesy.datasize.format;

import io.aesy.datasize.BitUnit;
import io.aesy.datasize.ByteUnit;
import io.aesy.datasize.DataUnit;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

/**
 * Tests properties unique to {@code SmartDataSizeFormatter}.
 */
public class SmartDataSizeFormatterTest implements WithAssertions {
    private static final Collection<DataUnit> ALL_UNITS;

    static {
        ALL_UNITS = new ArrayList<>();
        ALL_UNITS.addAll(BitUnit.values());
        ALL_UNITS.addAll(ByteUnit.values());
    }

    @BeforeEach
    public void setup() {
        Locale.setDefault(Locale.US);
    }
}
