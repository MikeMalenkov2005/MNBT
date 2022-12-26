package org.mnbt;

public class ByteTag extends Tag<Byte> {
    public ByteTag(String name, Byte value) {
        super(BYTE, name, value);
    }

    @Override
    public String toString() {
        return super.toString() + "b";
    }
}
