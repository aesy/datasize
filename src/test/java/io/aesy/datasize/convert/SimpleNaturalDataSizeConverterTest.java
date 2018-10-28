package io.aesy.datasize.convert;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Locale;

/**
 * Tests properties unique to {@code SimpleNaturalBytesConverter}.
 */
public class SimpleNaturalDataSizeConverterTest implements WithAssertions {
    @BeforeEach
    public void setup() {
        Locale.setDefault(Locale.US);
    }

    @Test
    @DisplayName("it should convert to smallest value possible above 1 using the same unit as " +
                 "origin object")
    public void test_convert() {
        // TODO
    }
}
