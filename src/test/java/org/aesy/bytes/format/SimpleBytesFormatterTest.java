package org.aesy.bytes.format;

import org.aesy.bytes.ByteUnit;
import org.aesy.bytes.ByteUnits;
import org.aesy.bytes.Bytes;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests properties unique to {@code SimpleBytesFormatter}.
 */
public class SimpleBytesFormatterTest implements WithAssertions {
    @Test
    @DisplayName("it should end with the unit type abbreviation")
    public void test_unit() {
        BytesFormatter formatter = new SimpleBytesFormatter();

        for (ByteUnit unit : ByteUnits.SI.units()) {
            Bytes bytes = Bytes.valueOf(Math.E, unit);
            String result = formatter.format(bytes);

            assertThat(result)
                .isNotBlank()
                .endsWith(unit.getAbbreviation());
        }
    }
}
