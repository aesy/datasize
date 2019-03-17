package io.aesy.datasize.convert;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeEach;

import java.util.Locale;

/**
 * Tests properties unique to {@code SimpleNaturalDataSizeConverter}.
 */
public class SimpleNaturalDataSizeConverterTest implements WithAssertions {
    @BeforeEach
    public void setup() {
        Locale.setDefault(Locale.US);
    }
}
