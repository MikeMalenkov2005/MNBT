package org.mnbt;

public class InvalidTagException extends Exception {
    public InvalidTagException(byte id) {
        super(String.format("NBT tag with id %d is not supported!", id));
    }

    public InvalidTagException(Class valueClass, byte id) {
        super(String.format("NBT tag value type %s doesn't match with its id %d!", valueClass.toString(), id));
    }
}
