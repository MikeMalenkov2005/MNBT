package org.mnbt;

import java.util.Map;

public class RootTag extends CompoundTag {
    public RootTag(Map<String, Tag<?>> value) {
        super(null, value);
    }
    
    public RootTag() {
        super(null);
    }
}
