package org.mnbt;

public class DoubleTag extends Tag<Double> {
    public DoubleTag(String name, Double value) {
        super(TagType.DOUBLE, name, value);
    }
}
