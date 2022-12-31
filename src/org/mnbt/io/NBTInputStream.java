package org.mnbt.io;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.mnbt.ByteArrayTag;
import org.mnbt.ByteTag;
import org.mnbt.CompoundTag;
import org.mnbt.DoubleTag;
import org.mnbt.FloatTag;
import org.mnbt.IntArrayTag;
import org.mnbt.IntTag;
import org.mnbt.InvalidTagException;
import org.mnbt.ListTag;
import org.mnbt.LongArrayTag;
import org.mnbt.LongTag;
import org.mnbt.RootTag;
import org.mnbt.ShortTag;
import org.mnbt.StringTag;
import org.mnbt.Tag;
import org.mnbt.TagType;

public class NBTInputStream implements Closeable {
    private final DataInputStream in;

    public NBTInputStream(InputStream in) {
        this.in = in instanceof DataInputStream ? (DataInputStream) in : new DataInputStream(in);
    }

    public Tag<?> readTag() throws IOException, InvalidTagException {
        TagType id = readTagId();
        if (id == TagType.END) return null;
        short nameLength = in.readShort();
        String name = nameLength > 0 ? new String(readByteArray(nameLength)) : null;
        return readTagValue(id, name);
    }

    @Override
    public void close() throws IOException {
        in.close();
    }

    private TagType readTagId() throws IOException, InvalidTagException {
        byte rawId = in.readByte();
        if (rawId < 0 || rawId >= TagType.values().length) throw new InvalidTagException(rawId);
        return TagType.values()[rawId];
    }

    private Tag<?> readTagValue(TagType id, String name) throws IOException, InvalidTagException {
        return switch (id) {
            case END -> null;
            case BYTE -> new ByteTag(name, in.readByte());
            case SHORT -> new ShortTag(name, in.readShort());
            case INT -> new IntTag(name, in.readInt());
            case LONG -> new LongTag(name, in.readLong());
            case FLOAT -> new FloatTag(name, in.readFloat());
            case DOUBLE -> new DoubleTag(name, in.readDouble());
            case BYTE_ARRAY -> new ByteArrayTag(name, readByteArray(in.readInt()));
            case STRING -> new StringTag(name, new String(readByteArray(in.readShort())));
            case LIST -> readListValue(name);
            case COMPOUND -> readCompoundValue(name);
            case INT_ARRAY -> new IntArrayTag(name, readIntArray(in.readInt()));
            case LONG_ARRAY -> new LongArrayTag(name, readLongArray(in.readInt()));
        };
    }

    private byte[] readByteArray(int length) throws IOException {
        byte buf[] = new byte[length];
        in.read(buf);
        return buf;
    }

    private int[] readIntArray(int length) throws IOException {
        int[] buf = new int[length];
        for (int i = 0; i < buf.length; i++) {
            buf[i] = in.readInt();
        }
        return buf;
    }

    private long[] readLongArray(int length) throws IOException {
        long[] buf = new long[length];
        for (int i = 0; i < buf.length; i++) {
            buf[i] = in.readLong();
        }
        return buf;
    }

    private CompoundTag readCompoundValue(String name) throws IOException, InvalidTagException {
        CompoundTag tag = (name == null || name.length() == 0) ? new RootTag() : new CompoundTag(name);
        Tag<?> child = readTag();
        while (child != null) {
            tag.put(child);
            child = readTag();
        }
        return tag;
    }

    private ListTag<?> readListValue(String name) throws IOException, InvalidTagException {
        TagType childId = readTagId();
        int size = in.readInt();
        return switch (childId) {
            case END -> readListChildren(childId, name, size, Byte.class);
            case BYTE -> readListChildren(childId, name, size, Byte.class);
            case SHORT -> readListChildren(childId, name, size, Short.class);
            case INT -> readListChildren(childId, name, size, Integer.class);
            case LONG -> readListChildren(childId, name, size, Long.class);
            case FLOAT -> readListChildren(childId, name, size, Float.class);
            case DOUBLE -> readListChildren(childId, name, size, Double.class);
            case BYTE_ARRAY -> readListChildren(childId, name, size, byte[].class);
            case STRING -> readListChildren(childId, name, size, String.class);
            case LIST -> readListChildren(childId, name, size, List.class);
            case COMPOUND -> readListChildren(childId, name, size, Map.class);
            case INT_ARRAY -> readListChildren(childId, name, size, int[].class);
            case LONG_ARRAY -> readListChildren(childId, name, size, long[].class);
        };
    }

    private <T> ListTag<T> readListChildren(TagType childId, String name, int size, Class<T> type) throws IOException, InvalidTagException {
        ListTag<T> tag = new ListTag<>(childId, name);
        for (int i = 0; i < size; i++) {
            Tag<?> childTag = readTagValue(childId, null);
            tag.add(childTag == null ? type.cast(0) : childTag.valueOfType(type));
        }
        return tag;
    }
}
