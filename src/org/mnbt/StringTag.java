package org.mnbt;

public class StringTag extends Tag<String> {
    public StringTag(String name, String value) {
        super(STRING, name, value);
    }

    @Override
    public String toString() {
        return "\"" + super.toString() + "\"";
    }
}
