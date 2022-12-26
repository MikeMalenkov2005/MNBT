package org.mnbt.io;

import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.mnbt.CompoundTag;
import org.mnbt.EndTag;
import org.mnbt.InvalidTagException;
import org.mnbt.ListTag;
import org.mnbt.Tag;

public class NBTOutputStream implements Closeable {
    private final DataOutputStream out;

    public NBTOutputStream(DataOutputStream out) {
        this.out = out;
    }

    public NBTOutputStream(OutputStream out) {
        this(new DataOutputStream(out));
    }

    public void write(Tag<?> tag) throws IOException, InvalidTagException {
        if (tag == null) throw new NullPointerException();
        out.writeByte(tag.id);
        if (tag.id == Tag.END) return;
        byte[] nameRaw = tag.name == null ? new byte[0] : tag.name.getBytes();
        out.writeShort(nameRaw.length);
        out.write(nameRaw);
        switch(tag.id) {
            case Tag.BYTE -> {
                if (tag.value instanceof Byte)
                    out.writeByte((Byte)tag.value);
                else throw new InvalidTagException(tag.value.getClass(), tag.id);
            }
            case Tag.SHORT -> {
                if (tag.value instanceof Short)
                    out.writeShort((Short)tag.value);
                else throw new InvalidTagException(tag.value.getClass(), tag.id);
            }
            case Tag.INT -> {
                if (tag.value instanceof Integer)
                    out.writeInt((Integer)tag.value);
                else throw new InvalidTagException(tag.value.getClass(), tag.id);
            }
            case Tag.LONG -> {
                if (tag.value instanceof Long)
                    out.writeLong((Long)tag.value);
                else throw new InvalidTagException(tag.value.getClass(), tag.id);
            }
            case Tag.FLOAT -> {
                if (tag.value instanceof Float)
                    out.writeFloat((Float)tag.value);
                else throw new InvalidTagException(tag.value.getClass(), tag.id);
            }
            case Tag.DOUBLE -> {
                if (tag.value instanceof Double)
                    out.writeDouble((Double)tag.value);
                else throw new InvalidTagException(tag.value.getClass(), tag.id);
            }
            case Tag.BYTE_ARRAY -> {
                if (tag.value instanceof byte[]) {
                    out.writeInt(((byte[])tag.value).length);
                    out.write((byte[])tag.value);
                }
                else throw new InvalidTagException(tag.value.getClass(), tag.id);
            }
            case Tag.STRING -> {
                if (tag.value instanceof String) {
                    out.writeShort((short) ((String)tag.value).length());
                    out.write(((String)tag.value).getBytes());
                }
                else throw new InvalidTagException(tag.value.getClass(), tag.id);
            }
            case Tag.LIST -> {
                if (tag instanceof ListTag)
                    writeList((ListTag<?>)tag);
                else throw new InvalidTagException(tag.value.getClass(), tag.id);
            }
            case Tag.COMPOUND -> {
                if (tag instanceof CompoundTag)
                    writeCompound((CompoundTag)tag);
                else throw new InvalidTagException(tag.value.getClass(), tag.id);
            }
            case Tag.INT_ARRAY -> {
                if (tag.value instanceof int[]) {
                    out.writeInt(((int[])tag.value).length);
                    for (int v : (int[])tag.value) {
                        out.writeInt(v);
                    }
                }
                else throw new InvalidTagException(tag.value.getClass(), tag.id);
            }
            case Tag.LONG_ARRAY -> {
                if (tag.value instanceof long[]) {
                    out.writeInt(((long[])tag.value).length);
                    for (long v : (long[])tag.value) {
                        out.writeLong(v);
                    }
                }
                else throw new InvalidTagException(tag.value.getClass(), tag.id);
            }
            default -> throw new InvalidTagException(tag.id);
        }
    }

    @Override
    public void close() throws IOException {
        out.close();
    }

    private void writeCompound(CompoundTag tag) throws IOException, InvalidTagException {
        for (Tag<?> t : tag) {
            if (t.id == Tag.END) continue;
            write(t);
        }
        write(new EndTag());
    }

    private void writeList(ListTag<?> tag) throws IOException, InvalidTagException {
        if (tag.childId < 0 || tag.childId > 12) throw new InvalidTagException(tag.childId);
        out.writeByte(tag.childId);
        out.writeInt(tag.size());
        if (tag.size() == 0 || tag.isEmpty()) return;
        for (Object child : tag) {
            switch (tag.childId) {
                case Tag.END -> {
                    if (child instanceof Byte)
                        out.writeByte(0);
                    else throw new InvalidTagException(child.getClass(), tag.childId);
                }
                case Tag.BYTE -> {
                    if (child instanceof Byte)
                        out.writeByte((Byte)child);
                    else throw new InvalidTagException(child.getClass(), tag.childId);
                }
                case Tag.SHORT -> {
                    if (child instanceof Short)
                        out.writeShort((Short)child);
                    else throw new InvalidTagException(child.getClass(), tag.childId);
                }
                case Tag.INT -> {
                    if (child instanceof Integer)
                        out.writeInt((Integer)child);
                    else throw new InvalidTagException(child.getClass(), tag.childId);
                }
                case Tag.LONG -> {
                    if (child instanceof Long)
                        out.writeLong((Long)child);
                    else throw new InvalidTagException(child.getClass(), tag.childId);
                }
                case Tag.FLOAT -> {
                    if (child instanceof Float)
                        out.writeFloat((Float)child);
                    else throw new InvalidTagException(child.getClass(), tag.childId);
                }
                case Tag.DOUBLE -> {
                    if (child instanceof Double)
                        out.writeDouble((Double)child);
                    else throw new InvalidTagException(child.getClass(), tag.childId);
                }
                case Tag.BYTE_ARRAY -> {
                    if (child instanceof byte[]) {
                        out.writeInt(((byte[])child).length);
                        out.write((byte[])child);
                    }
                    else throw new InvalidTagException(child.getClass(), tag.childId);
                }
                case Tag.STRING -> {
                    if (child instanceof String) {
                        out.writeShort((short) ((String)child).length());
                        out.write(((String)child).getBytes());
                    }
                    else throw new InvalidTagException(child.getClass(), tag.childId);
                }
                case Tag.LIST -> {
                    if (child instanceof ListTag)
                        writeList((ListTag<?>)child);
                    else throw new InvalidTagException(child.getClass(), tag.childId);
                }
                case Tag.COMPOUND -> {
                    if (child instanceof CompoundTag)
                        writeCompound((CompoundTag)child);
                    else throw new InvalidTagException(child.getClass(), tag.childId);
                }
                case Tag.INT_ARRAY -> {
                    if (child instanceof int[]) {
                        out.writeInt(((int[])child).length);
                        for (int v : (int[])child) {
                            out.writeInt(v);
                        }
                    }
                    else throw new InvalidTagException(child.getClass(), tag.childId);
                }
                case Tag.LONG_ARRAY -> {
                    if (child instanceof long[]) {
                        out.writeInt(((long[])child).length);
                        for (long v : (long[])child) {
                            out.writeLong(v);
                        }
                    }
                    else throw new InvalidTagException(child.getClass(), tag.childId);
                }
                default -> throw new InvalidTagException(tag.childId);
            }
        }
    }
}
