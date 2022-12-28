package org.mnbt;

public class ShortTag extends Tag<Short> {
    public ShortTag(String name, Short value) {
        super(TagType.SHORT, name, value);
    }

    @Override
    public String toString() {
        return super.toString() + "s";
    }
}
