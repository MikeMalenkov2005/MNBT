# MNBT

MNBT is a simple library for easy work with NBT data format

## Usage

```java

// Use NBT in Java
RootTag rt = new RootTag();
rt.put(new StringTag("name", "value"));

// Read NBT with NBTInputStream
NBTInputStream in = new NBTInputStream(new FileInputStream("path/to/the/file.nbt"));
Tag<?> tag = in.read();
for (Tag<?> t : (CompoundTag)tag) {
    rt.put(t);
}

// Write NBT with NBTOutputStream
NBTOutputStream out = new NBTOutputStream(new FileOutputStream("path/to/the/file.nbt"));
out.write(rt);

```
