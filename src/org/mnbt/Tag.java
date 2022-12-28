package org.mnbt;

public abstract class Tag<T> {
    public final TagType id;
    public final String name;
    public T value;
    
    protected Tag(TagType id, String name, T value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }

    public final <V> V valueOfType(Class<V> type) throws InvalidTagException {
        return TagType.valueOfType(this.value, id, type);
    }

    @Override
    public String toString() {
        return value == null ? "" : value.toString();
    }
}
