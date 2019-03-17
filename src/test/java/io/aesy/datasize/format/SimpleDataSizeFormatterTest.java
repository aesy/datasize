package io.aesy.datasize.format;

import io.aesy.datasize.BitUnit;
import io.aesy.datasize.ByteUnit;
import io.aesy.datasize.DataSize;
import io.aesy.datasize.DataUnit;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

/**
 * Tests properties unique to {@code SimpleDataSizeFormatter}.
 */
public class SimpleDataSizeFormatterTest implements WithAssertions {
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

    @Test
    @DisplayName("it should end with the unit type abbreviation")
    public void test_unit() {
        DataSizeFormatter formatter = new SimpleDataSizeFormatter();

        for (DataUnit unit : ALL_UNITS) {
            DataSize dataSize = DataSize.of(Math.E, unit);
            String result = formatter.format(dataSize);

            assertThat(result)
                .isNotBlank()
                .endsWith(unit.getAbbreviation());
        }
    }
}
