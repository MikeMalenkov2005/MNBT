package org.mnbt;

public enum TagType {
    END,
    BYTE,
    SHORT,
    INT,
    LONG,
    FLOAT,
    DOUBLE,
    BYTE_ARRAY,
    STRING,
    LIST,
    COMPOUND,
    INT_ARRAY,
    LONG_ARRAY;

    public static final <T, V> T valueOfType(V value, TagType id, Class<T> type) throws InvalidTagException {
        if (value == null) throw new NullPointerException();
        if (type.isAssignableFrom(value.getClass())) return type.cast(value);
        throw new InvalidTagException(value.getClass(), id);
    }
}
