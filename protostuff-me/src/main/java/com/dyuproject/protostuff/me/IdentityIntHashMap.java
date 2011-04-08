package com.dyuproject.protostuff.me;

import java.util.Hashtable;

public class IdentityIntHashMap {
    private final Hashtable table;
    public IdentityIntHashMap(int initialCapacity) {
        table = new Hashtable(initialCapacity);
    }

    public IdentityIntHashMap() {
        table = new Hashtable();
    }

    public void put(Object value, int i) {
        table.put(new IDKey(value), new Integer(i));
    }

    public Integer get(Object value) {
        return (Integer)table.get(new IDKey(value));
    }

    public static final class IDKey {
        private final Object value;

        public IDKey(Object o) {
            this.value = o;
        }

        public int hashCode() {
            return System.identityHashCode(value);
        }

        public boolean equals(Object obj) {
            if(!(obj instanceof IDKey)) {
                return false;
            }
            IDKey otherKey = (IDKey)obj;
            return otherKey.value == this.value;
        }

    }
}
