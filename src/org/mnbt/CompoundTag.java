package org.mnbt;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CompoundTag extends Tag<Map<String, Tag<?>>> implements Iterable<Tag<?>> {
    public CompoundTag(String name, Map<String, Tag<?>> value) {
        super(COMPOUND, name, value);
    }

    public CompoundTag(String name) {
        super(COMPOUND, name, new HashMap<>());
    }

    public int size() {
        return value.size();
    }

    public boolean isEmpty() {
        return value.isEmpty();
    }

    public Tag<?> put(Tag<?> tag) {
        return value.put(tag.name, tag);
    }

    public Tag<?> get(String name) {
        return value.get(name);
    }

    public boolean remove(Tag<?> tag) {
        return value.remove(tag.name, tag);
    }

    public Tag<?> remove(String name) {
        return value.remove(name);
    }

    public void clear() {
        value.clear();
    }

    public boolean contains(Tag<?> tag) {
        return value.containsValue(tag);
    }

    public boolean contains(String name) {
        return value.containsKey(name) && value.get(name) != null;
    }

    @Override
    public Iterator<Tag<?>> iterator() {
        return value.values().iterator();
    }

    @Override
    public String toString() {
        if (value == null)
            return "";
        if (size() == 0)
            return "{}";

        StringBuilder b = new StringBuilder();
        b.append("{");
        boolean f = false;
        for (Tag<?> child : this) {
            if (f) b.append(", ");
            else f = true;
            b.append(child.name);
            b.append(": ");
            b.append(child);
        }
        b.append('}');
        return b.toString();
    }
}
