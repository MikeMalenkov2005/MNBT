package org.mnbt;

public class LongArrayTag extends Tag<long[]> {
    public LongArrayTag(String name, long[] value) {
        super(LONG_ARRAY, name, value);
    }

    @Override
    public String toString() {
        if (value == null)
            return "";
        int iMax = value.length - 1;
        if (iMax == -1)
            return "[L;]";

        StringBuilder b = new StringBuilder();
        b.append("[L;");
        for (int i = 0; ; i++) {
            b.append(value[i]);
            b.append('L');
            if (i == iMax)
                return b.append(']').toString();
            b.append(", ");
        }
    }
}
