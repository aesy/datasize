package io.aesy.datasize;

import java.math.BigDecimal;

public interface DataUnit {
    String getAbbreviation();
    String getName();

    // A little leaky abstraction IMO
    BigDecimal bytes();
}
