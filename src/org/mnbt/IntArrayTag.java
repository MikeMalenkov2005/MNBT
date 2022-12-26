package org.mnbt;

public class IntArrayTag extends Tag<int[]> {
    public IntArrayTag(String name, int[] value) {
        super(INT_ARRAY, name, value);
    }

    @Override
    public String toString() {
        if (value == null)
            return "";
        int iMax = value.length - 1;
        if (iMax == -1)
            return "[I;]";

        StringBuilder b = new StringBuilder();
        b.append("[I;");
        for (int i = 0; ; i++) {
            b.append(value[i]);
            if (i == iMax)
                return b.append(']').toString();
            b.append(", ");
        }
    }
}
