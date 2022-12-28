package org.mnbt.io;

import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.mnbt.CompoundTag;
import org.mnbt.InvalidTagException;
import org.mnbt.ListTag;
import org.mnbt.Tag;
import org.mnbt.TagType;

public class NBTOutputStream implements Closeable {
    private final DataOutputStream out;

    public NBTOutputStream(OutputStream out) {
        this.out = out instanceof DataOutputStream ? (DataOutputStream)out : new DataOutputStream(out);
    }

    public void writeTag(Tag<?> tag) throws IOException, InvalidTagException {
        if (tag == null) throw new NullPointerException();
        out.writeByte(tag.id.ordinal());
        if (tag.id == TagType.END) return;
        byte[] nameRaw = tag.name == null ? new byte[0] : tag.name.getBytes();
        out.writeShort(nameRaw.length);
        out.write(nameRaw);
        writeTagValue(tag);
    }

    private void writeTagValue(Tag<?> tag) throws IOException, InvalidTagException {
        switch(tag.id) {
            case END -> out.flush();
            case BYTE -> out.writeByte(tag.valueOfType(Byte.class));
            case SHORT -> out.writeShort(tag.valueOfType(Short.class));
            case INT -> out.writeInt(tag.valueOfType(Integer.class));
            case LONG -> out.writeLong(tag.valueOfType(Long.class));
            case FLOAT -> out.writeFloat(tag.valueOfType(Float.class));
            case DOUBLE -> out.writeDouble(tag.valueOfType(Double.class));
            case BYTE_ARRAY -> {
                byte[] value = tag.valueOfType(byte[].class);
                out.writeInt(value.length);
                out.write(value);
            }
            case STRING -> {
                String value = tag.valueOfType(String.class);
                out.writeShort(value.length());
                out.write(value.getBytes());
            }
            case LIST -> writeListValue(TagType.valueOfType(tag, tag.id, ListTag.class));
            case COMPOUND -> writeCompoundValue(TagType.valueOfType(tag, tag.id, CompoundTag.class));
            case INT_ARRAY -> writeIntArray(tag.valueOfType(int[].class));
            case LONG_ARRAY -> writeLongArray(tag.valueOfType(long[].class));
        }
    }

    private void writeIntArray(int[] arr) throws IOException {
        out.writeInt(arr.length);
        for (int v : arr) {
            out.writeInt(v);
        }
    }

    private void writeLongArray(long[] arr) throws IOException {
        out.writeInt(arr.length);
        for (long v : arr) {
            out.writeLong(v);
        }
    }

    @Override
    public void close() throws IOException {
        out.close();
    }

    private void writeCompoundValue(CompoundTag tag) throws IOException, InvalidTagException {
        for (Tag<?> child : tag) {
            if (child.id == TagType.END) continue;
            writeTag(child);
        }
        out.writeByte(0);
    }

    private void writeListValue(ListTag<?> tag) throws IOException, InvalidTagException {
        out.writeByte(tag.childId.ordinal());
        out.writeInt(tag.size());
        if (tag.size() == 0 || tag.isEmpty()) return;
        for (var child : tag) {
            switch(tag.childId) {
                case END -> out.writeByte(0);
                case BYTE -> out.writeByte(TagType.valueOfType(child, tag.childId, Byte.class));
                case SHORT -> out.writeShort(TagType.valueOfType(child, tag.childId, Short.class));
                case INT -> out.writeInt(TagType.valueOfType(child, tag.childId, Integer.class));
                case LONG -> out.writeLong(TagType.valueOfType(child, tag.childId, Long.class));
                case FLOAT -> out.writeFloat(TagType.valueOfType(child, tag.childId, Float.class));
                case DOUBLE -> out.writeDouble(TagType.valueOfType(child, tag.childId, Double.class));
                case BYTE_ARRAY -> {
                    byte[] value = TagType.valueOfType(child, tag.childId, byte[].class);
                    out.writeInt(value.length);
                    out.write(value);
                }
                case STRING -> {
                    String value = TagType.valueOfType(child, tag.childId, String.class);
                    out.writeShort(value.length());
                    out.write(value.getBytes());
                }
                case LIST -> writeListValue(TagType.valueOfType(child, tag.childId, ListTag.class));
                case COMPOUND -> writeCompoundValue(TagType.valueOfType(child, tag.childId, CompoundTag.class));
                case INT_ARRAY -> writeIntArray(TagType.valueOfType(child, tag.childId, int[].class));
                case LONG_ARRAY -> writeLongArray(TagType.valueOfType(child, tag.childId, long[].class));
            }
        }
    }
}
