package io.aesy.datasize.parse;

import io.aesy.datasize.DataUnit;
import org.petitparser.context.Context;
import org.petitparser.context.Result;
import org.petitparser.parser.Parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* package-private */ class ByteUnitParser extends Parser {
    private final List<DataUnit> units;
    private final String message;
    private boolean caseSensitive;

    /* package-private */ ByteUnitParser(List<DataUnit> units, String message) {
        this.units = new ArrayList<>(units);
        this.message = message;
        this.caseSensitive = true;
    }

    public static ByteUnitParser of(DataUnit unit) {
        return new ByteUnitParser(Collections.singletonList(unit),
                                  String.format("%s expected", unit));
    }

    public static ByteUnitParser of(DataUnit unit, String message) {
        return new ByteUnitParser(Collections.singletonList(unit), message);
    }

    public static ByteUnitParser anyOf(List<DataUnit> units) {
        return new ByteUnitParser(units, String.format("one of %s expected", units));
    }

    public static ByteUnitParser anyOf(List<DataUnit> units, String message) {
        return new ByteUnitParser(units, message);
    }

    public ByteUnitParser caseSensitive(boolean on) {
        this.caseSensitive = on;

        return this;
    }

    @Override
    public Result parseOn(Context context) {
        String input = context.getBuffer();
        int start = context.getPosition();

        if (!caseSensitive) {
            input = input.toLowerCase();
        }

        for (DataUnit unit : units) {
            String name = unit.getName();
            String abbreviation = unit.getAbbreviation();

            if (!caseSensitive) {
                name = name.toLowerCase();
                abbreviation = abbreviation.toLowerCase();
            }

            int nameIndex = input.indexOf(name, start);

            if (nameIndex == start) {
                return context.success(unit, nameIndex + name.length());
            }

            int abbrIndex = input.indexOf(abbreviation, start);

            if (abbrIndex == start) {
                return context.success(unit, abbrIndex + abbreviation.length());
            }
        }

        return context.failure(message);
    }

    @Override
    public Parser copy() {
        return new ByteUnitParser(units, message);
    }
}
