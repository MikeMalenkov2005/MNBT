package org.mnbt;

public class StringTag extends Tag<String> {
    public StringTag(String name, String value) {
        super(TagType.STRING, name, value);
    }

    @Override
    public String toString() {
        return "\"" + super.toString() + "\"";
    }
}
