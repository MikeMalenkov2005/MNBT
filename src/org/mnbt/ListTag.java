package org.mnbt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListTag<T> extends Tag<List<T>> implements Iterable<T> {
    public final byte childId;
    
    public ListTag(byte childType, String name, List<T> value) {
        super(LIST, name, value);
        this.childId = childType;
    }

    public ListTag(byte childType, String name) {
        this(childType, name, new ArrayList<>());
    }

    public int size() {
        return value.size();
    }

    public boolean isEmpty() {
        return value.isEmpty();
    }

    public boolean contains(Object o) {
        return value.contains(o);
    }

    public Iterator<T> iterator() {
        return value.iterator();
    }

    public boolean add(T e) {
        return value.add(e);
    }

    public boolean remove(Object o) {
        return value.remove(o);
    }

    public void clear() {
        value.clear();
    }

    public T get(int index) {
        return value.get(index);
    }

    public T set(int index, T element) {
        return value.set(index, element);
    }

    public void add(int index, T element) {
        value.add(index, element);
    }

    public T remove(int index) {
        return value.remove(index);
    }

    public int indexOf(Object o) {
        return value.indexOf(o);
    }

    @Override
    public String toString() {
        if (value == null)
            return "";
        int iMax = size() - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        b.append("[");
        for (int i = 0; ; i++) {
            if (childId == BYTE_ARRAY && get(i) instanceof byte[]) {
                b.append(new ByteArrayTag(null, (byte[])get(i)));
            } else if (childId == INT_ARRAY && get(i) instanceof int[]) {
                b.append(new IntArrayTag(null, (int[])get(i)));
            } else if (childId == LONG_ARRAY && get(i) instanceof long[]) {
                b.append(new LongArrayTag(null, (long[])get(i)));
            } else {
                b.append(get(i));
                switch (childId) {
                    case END -> b.append('b');
                    case BYTE -> b.append('b');
                    case SHORT -> b.append('s');
                    case LONG -> b.append('L');
                    case FLOAT -> b.append('f');
                }
            }
            if (i == iMax)
                return b.append(']').toString();
            b.append(", ");
        }
    }
}
