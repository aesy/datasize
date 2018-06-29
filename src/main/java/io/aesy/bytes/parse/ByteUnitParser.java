package io.aesy.bytes.parse;

import io.aesy.bytes.ByteUnit;
import io.aesy.bytes.ByteUnits;
import org.petitparser.context.Context;
import org.petitparser.context.Result;
import org.petitparser.parser.Parser;

import java.util.Collections;
import java.util.List;

class ByteUnitParser extends Parser {
    private final List<ByteUnit> units;
    private final String message;
    private boolean caseSensitive;

    ByteUnitParser(List<ByteUnit> units, String message) {
        this.units = units;
        this.message = message;
        this.caseSensitive = true;
    }

    static ByteUnitParser of(ByteUnit unit) {
        return new ByteUnitParser(Collections.singletonList(unit),
                                  String.format("%s expected", unit));
    }

    static ByteUnitParser of(ByteUnit unit, String message) {
        return new ByteUnitParser(Collections.singletonList(unit), message);
    }

    static ByteUnitParser anyOf(List<ByteUnit> units) {
        return new ByteUnitParser(units, String.format("one of %s expected", units));
    }

    static ByteUnitParser anyOf(List<ByteUnit> units, String message) {
        return new ByteUnitParser(units, message);
    }

    public ByteUnitParser caseSensitive(boolean on) {
        this.caseSensitive = on;

        return this;
    }

    @Override
    public Result parseOn(Context context) {
        String buffer = context.getBuffer();
        String input = buffer;
        int start = context.getPosition();

        if (!caseSensitive) {
            input = input.toLowerCase();
        }

        for (ByteUnit unit : units) {
            String name = unit.getName();
            String abbreviation = unit.getAbbreviation();

            if (!caseSensitive) {
                if (unit.equals(ByteUnits.SI.KILOBYTE) && buffer.startsWith("KB", start)) {
                    // It's impossible to differentiate between kB (SI) & KB (JEDEC) in lowercase,
                    // so if it matches JEDEC exactly, then skip SI since it would otherwise be
                    // accepted as a match
                    continue;
                }

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
