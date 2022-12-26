package org.mnbt;

public class ShortTag extends Tag<Short> {
    public ShortTag(String name, Short value) {
        super(SHORT, name, value);
    }

    @Override
    public String toString() {
        return super.toString() + "s";
    }
}
