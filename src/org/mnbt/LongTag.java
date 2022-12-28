package org.mnbt;

public class LongTag extends Tag<Long> {
    public LongTag(String name, Long value) {
        super(TagType.LONG, name, value);
    }

    @Override
    public String toString() {
        return super.toString() + "L";
    }
}
