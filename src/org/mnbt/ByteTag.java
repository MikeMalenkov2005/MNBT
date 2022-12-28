package org.mnbt;

public class ByteTag extends Tag<Byte> {
    public ByteTag(String name, Byte value) {
        super(TagType.BYTE, name, value);
    }

    @Override
    public String toString() {
        return super.toString() + "b";
    }
}
