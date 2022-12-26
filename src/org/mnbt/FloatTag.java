package org.mnbt;

public class FloatTag extends Tag<Float> {
    public FloatTag(String name, Float value) {
        super(FLOAT, name, value);
    }

    @Override
    public String toString() {
        return super.toString() + "f";
    }
}
