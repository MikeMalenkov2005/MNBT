package org.mnbt;

public class ByteArrayTag extends Tag<byte[]> {
    public ByteArrayTag(String name, byte[] value) {
        super(BYTE_ARRAY, name, value);
    }

    @Override
    public String toString() {
        if (value == null)
            return "";
        int iMax = value.length - 1;
        if (iMax == -1)
            return "[B;]";

        StringBuilder b = new StringBuilder();
        b.append("[B;");
        for (int i = 0; ; i++) {
            b.append(value[i]);
            b.append('b');
            if (i == iMax)
                return b.append(']').toString();
            b.append(", ");
        }
    }
}
