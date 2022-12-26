package org.mnbt.io;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.mnbt.ByteArrayTag;
import org.mnbt.ByteTag;
import org.mnbt.CompoundTag;
import org.mnbt.DoubleTag;
import org.mnbt.EndTag;
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

public class NBTInputStream implements Closeable {
    private final DataInputStream in;
    
    public NBTInputStream(DataInputStream in) {
        this.in = in;
    }

    public NBTInputStream(InputStream in) {
        this(new DataInputStream(in));
    }

    public Tag<?> read() throws IOException, InvalidTagException {
        byte id = in.readByte();
        if (id < 0 || id > 12) throw new InvalidTagException(id);
        if (id == Tag.END) return new EndTag();
        short nameLength = in.readShort();
        byte nameRaw[] = new byte[nameLength];
        in.read(nameRaw);
        String name = new String(nameRaw);
        return switch (id) {
            case Tag.BYTE -> new ByteTag(name, in.readByte());
            case Tag.SHORT -> new ShortTag(name, in.readShort());
            case Tag.INT -> new IntTag(name, in.readInt());
            case Tag.LONG -> new LongTag(name, in.readLong());
            case Tag.FLOAT -> new FloatTag(name, in.readFloat());
            case Tag.DOUBLE -> new DoubleTag(name, in.readDouble());
            case Tag.BYTE_ARRAY -> {
                byte buf[] = new byte[in.readInt()];
                in.read(buf);
                yield new ByteArrayTag(name, buf);
            }
            case Tag.STRING -> {
                byte buf[] = new byte[in.readShort()];
                in.read(buf);
                yield new StringTag(name, new String(buf));
            }
            case Tag.LIST -> readList(name);
            case Tag.COMPOUND -> readCompound(name);
            case Tag.INT_ARRAY -> {
                int[] buf = new int[in.readInt()];
                for (int i = 0; i < buf.length; i++) {
                    buf[i] = in.readInt();
                }
                yield new IntArrayTag(name, buf);
            }
            case Tag.LONG_ARRAY -> {
                long[] buf = new long[in.readInt()];
                for (int i = 0; i < buf.length; i++) {
                    buf[i] = in.readLong();
                }
                yield new LongArrayTag(name, buf);
            }
            default -> throw new InvalidTagException(id);
        };
    }

    @Override
    public void close() throws IOException {
        in.close();
    }

    private CompoundTag readCompound(String name) throws IOException, InvalidTagException {
        CompoundTag tag = (name == null || name.length() == 0) ? new RootTag() : new CompoundTag(name);
        Tag<?> child = read();
        while (child.id != Tag.END) {
            tag.put(child);
            child = read();
        }
        return tag;
    }

    private ListTag<?> readList(String name) throws IOException, InvalidTagException {
        byte childId = in.readByte();
        int size = in.readInt();
        ListTag<?> tag = switch (childId) {
            case Tag.END -> new ListTag<Byte>(childId, name);
            case Tag.BYTE -> new ListTag<Byte>(childId, name);
            case Tag.SHORT -> new ListTag<Short>(childId, name);
            case Tag.INT -> new ListTag<Integer>(childId, name);
            case Tag.LONG -> new ListTag<Long>(childId, name);
            case Tag.FLOAT -> new ListTag<Float>(childId, name);
            case Tag.DOUBLE -> new ListTag<Double>(childId, name);
            case Tag.BYTE_ARRAY -> new ListTag<byte[]>(childId, name);
            case Tag.STRING -> new ListTag<String>(childId, name);
            case Tag.LIST -> new ListTag<ListTag<?>>(childId, name);
            case Tag.COMPOUND -> new ListTag<CompoundTag>(childId, name);
            case Tag.INT_ARRAY -> new ListTag<int[]>(childId, name);
            case Tag.LONG_ARRAY -> new ListTag<long[]>(childId, name);
            default -> throw new InvalidTagException(childId);
        };
        for (int i = 0; i < size; i++) {
            ((ListTag<Object>) tag).add(switch (childId) {
                case Tag.END -> (Byte) in.readByte();
                case Tag.BYTE -> (Byte) in.readByte();
                case Tag.SHORT -> (Short) in.readShort();
                case Tag.INT -> (Integer) in.readInt();
                case Tag.LONG -> (Long) in.readLong();
                case Tag.FLOAT -> (Float) in.readFloat();
                case Tag.DOUBLE -> (Double) in.readDouble();
                case Tag.BYTE_ARRAY -> {
                    byte[] buf = new byte[in.readInt()];
                    in.read(buf);
                    yield buf;
                }
                case Tag.STRING -> {
                    byte buf[] = new byte[in.readShort()];
                    in.read(buf);
                    yield new String(buf);
                }
                case Tag.LIST -> readList(null);
                case Tag.COMPOUND -> readCompound(null);
                case Tag.INT_ARRAY -> {
                    int[] buf = new int[in.readInt()];
                    for (int j = 0; j < buf.length; j++) {
                        buf[j] = in.readInt();
                    }
                    yield buf;
                }
                case Tag.LONG_ARRAY -> {
                    long[] buf = new long[in.readInt()];
                    for (int j = 0; j < buf.length; j++) {
                        buf[j] = in.readLong();
                    }
                    yield buf;
                }
                default -> throw new InvalidTagException(childId);
            });
        }
        return tag;
    }
}
