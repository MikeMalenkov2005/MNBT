package org.mnbt;

public class IntTag extends Tag<Integer> {
    public IntTag(String name, Integer value) {
        super(TagType.INT, name, value);
    }
}
