package com.badlogic.gdx.utils;

import com.badlogic.gdx.math.MathUtils;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.codehaus.jackson.org.objectweb.asm.signature.SignatureVisitor;

/* JADX INFO: loaded from: classes.dex */
public class ObjectMap<K, V> {
    private static final int PRIME1 = -1105259343;
    private static final int PRIME2 = -1262997959;
    private static final int PRIME3 = -825114047;
    int capacity;
    private Entries entries;
    private int hashShift;
    K[] keyTable;
    private Keys keys;
    private float loadFactor;
    private int mask;
    private int pushIterations;
    public int size;
    private int stashCapacity;
    int stashSize;
    private int threshold;
    V[] valueTable;
    private Values values;

    public ObjectMap() {
        this(32, 0.8f);
    }

    public ObjectMap(int initialCapacity) {
        this(initialCapacity, 0.8f);
    }

    public ObjectMap(int i, float f) {
        if (i < 0) {
            throw new IllegalArgumentException("initialCapacity must be >= 0: " + i);
        }
        if (this.capacity > 1073741824) {
            throw new IllegalArgumentException("initialCapacity is too large: " + i);
        }
        this.capacity = MathUtils.nextPowerOfTwo(i);
        if (f <= 0.0f) {
            throw new IllegalArgumentException("loadFactor must be > 0: " + f);
        }
        this.loadFactor = f;
        this.threshold = (int) (this.capacity * f);
        this.mask = this.capacity - 1;
        this.hashShift = 31 - Integer.numberOfTrailingZeros(this.capacity);
        this.stashCapacity = Math.max(3, ((int) Math.ceil(Math.log(this.capacity))) * 2);
        this.pushIterations = Math.max(Math.min(this.capacity, 8), ((int) Math.sqrt(this.capacity)) / 8);
        this.keyTable = (K[]) new Object[this.capacity + this.stashCapacity];
        this.valueTable = (V[]) new Object[this.keyTable.length];
    }

    public V put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("key cannot be null.");
        }
        return put_internal(key, value);
    }

    private V put_internal(K key, V value) {
        K[] keyTable = this.keyTable;
        int hashCode = key.hashCode();
        int index1 = hashCode & this.mask;
        K key1 = keyTable[index1];
        if (key.equals(key1)) {
            V oldValue = this.valueTable[index1];
            this.valueTable[index1] = value;
            return oldValue;
        }
        int index2 = hash2(hashCode);
        K key2 = keyTable[index2];
        if (key.equals(key2)) {
            V oldValue2 = this.valueTable[index2];
            this.valueTable[index2] = value;
            return oldValue2;
        }
        int index3 = hash3(hashCode);
        K key3 = keyTable[index3];
        if (key.equals(key3)) {
            V oldValue3 = this.valueTable[index3];
            this.valueTable[index3] = value;
            return oldValue3;
        }
        int i = this.capacity;
        int n = i + this.stashSize;
        while (i < n) {
            if (!key.equals(keyTable[i])) {
                i++;
            } else {
                V oldValue4 = this.valueTable[i];
                this.valueTable[i] = value;
                return oldValue4;
            }
        }
        if (key1 == null) {
            keyTable[index1] = key;
            this.valueTable[index1] = value;
            int i2 = this.size;
            this.size = i2 + 1;
            if (i2 >= this.threshold) {
                resize(this.capacity << 1);
            }
            return null;
        }
        if (key2 == null) {
            keyTable[index2] = key;
            this.valueTable[index2] = value;
            int i3 = this.size;
            this.size = i3 + 1;
            if (i3 >= this.threshold) {
                resize(this.capacity << 1);
            }
            return null;
        }
        if (key3 == null) {
            keyTable[index3] = key;
            this.valueTable[index3] = value;
            int i4 = this.size;
            this.size = i4 + 1;
            if (i4 >= this.threshold) {
                resize(this.capacity << 1);
            }
            return null;
        }
        push(key, value, index1, key1, index2, key2, index3, key3);
        return null;
    }

    public void putAll(ObjectMap<K, V> map) {
        for (Entry<K, V> entry : map.entries()) {
            put(entry.key, entry.value);
        }
    }

    private void putResize(K key, V value) {
        int hashCode = key.hashCode();
        int index1 = hashCode & this.mask;
        K key1 = this.keyTable[index1];
        if (key1 == null) {
            this.keyTable[index1] = key;
            this.valueTable[index1] = value;
            int i = this.size;
            this.size = i + 1;
            if (i >= this.threshold) {
                resize(this.capacity << 1);
                return;
            }
            return;
        }
        int index2 = hash2(hashCode);
        K key2 = this.keyTable[index2];
        if (key2 == null) {
            this.keyTable[index2] = key;
            this.valueTable[index2] = value;
            int i2 = this.size;
            this.size = i2 + 1;
            if (i2 >= this.threshold) {
                resize(this.capacity << 1);
                return;
            }
            return;
        }
        int index3 = hash3(hashCode);
        K key3 = this.keyTable[index3];
        if (key3 == null) {
            this.keyTable[index3] = key;
            this.valueTable[index3] = value;
            int i3 = this.size;
            this.size = i3 + 1;
            if (i3 >= this.threshold) {
                resize(this.capacity << 1);
                return;
            }
            return;
        }
        push(key, value, index1, key1, index2, key2, index3, key3);
    }

    private void push(K insertKey, V insertValue, int index1, K key1, int index2, K key2, int index3, K key3) {
        K evictedKey;
        V evictedValue;
        K[] keyTable = this.keyTable;
        V[] valueTable = this.valueTable;
        int mask = this.mask;
        int i = 0;
        int pushIterations = this.pushIterations;
        while (true) {
            switch (MathUtils.random(2)) {
                case 0:
                    evictedKey = key1;
                    evictedValue = valueTable[index1];
                    keyTable[index1] = insertKey;
                    valueTable[index1] = insertValue;
                    break;
                case 1:
                    evictedKey = key2;
                    evictedValue = valueTable[index2];
                    keyTable[index2] = insertKey;
                    valueTable[index2] = insertValue;
                    break;
                default:
                    evictedKey = key3;
                    evictedValue = valueTable[index3];
                    keyTable[index3] = insertKey;
                    valueTable[index3] = insertValue;
                    break;
            }
            int hashCode = evictedKey.hashCode();
            index1 = hashCode & mask;
            key1 = keyTable[index1];
            if (key1 == null) {
                keyTable[index1] = evictedKey;
                valueTable[index1] = evictedValue;
                int i2 = this.size;
                this.size = i2 + 1;
                if (i2 >= this.threshold) {
                    resize(this.capacity << 1);
                    return;
                }
                return;
            }
            index2 = hash2(hashCode);
            key2 = keyTable[index2];
            if (key2 == null) {
                keyTable[index2] = evictedKey;
                valueTable[index2] = evictedValue;
                int i3 = this.size;
                this.size = i3 + 1;
                if (i3 >= this.threshold) {
                    resize(this.capacity << 1);
                    return;
                }
                return;
            }
            index3 = hash3(hashCode);
            key3 = keyTable[index3];
            if (key3 == null) {
                keyTable[index3] = evictedKey;
                valueTable[index3] = evictedValue;
                int i4 = this.size;
                this.size = i4 + 1;
                if (i4 >= this.threshold) {
                    resize(this.capacity << 1);
                    return;
                }
                return;
            }
            i++;
            if (i != pushIterations) {
                insertKey = evictedKey;
                insertValue = evictedValue;
            } else {
                putStash(evictedKey, evictedValue);
                return;
            }
        }
    }

    private void putStash(K key, V value) {
        if (this.stashSize == this.stashCapacity) {
            resize(this.capacity << 1);
            put_internal(key, value);
            return;
        }
        int index = this.capacity + this.stashSize;
        this.keyTable[index] = key;
        this.valueTable[index] = value;
        this.stashSize++;
        this.size++;
    }

    public V get(K key) {
        int hashCode = key.hashCode();
        int index = hashCode & this.mask;
        if (!key.equals(this.keyTable[index])) {
            index = hash2(hashCode);
            if (!key.equals(this.keyTable[index])) {
                index = hash3(hashCode);
                if (!key.equals(this.keyTable[index])) {
                    return getStash(key);
                }
            }
        }
        return this.valueTable[index];
    }

    private V getStash(K key) {
        K[] keyTable = this.keyTable;
        int i = this.capacity;
        int n = i + this.stashSize;
        while (i < n) {
            if (key.equals(keyTable[i])) {
                return this.valueTable[i];
            }
            i++;
        }
        return null;
    }

    public V remove(K key) {
        int hashCode = key.hashCode();
        int index = hashCode & this.mask;
        if (key.equals(this.keyTable[index])) {
            this.keyTable[index] = null;
            V oldValue = this.valueTable[index];
            this.valueTable[index] = null;
            this.size--;
            return oldValue;
        }
        int index2 = hash2(hashCode);
        if (key.equals(this.keyTable[index2])) {
            this.keyTable[index2] = null;
            V oldValue2 = this.valueTable[index2];
            this.valueTable[index2] = null;
            this.size--;
            return oldValue2;
        }
        int index3 = hash3(hashCode);
        if (key.equals(this.keyTable[index3])) {
            this.keyTable[index3] = null;
            V oldValue3 = this.valueTable[index3];
            this.valueTable[index3] = null;
            this.size--;
            return oldValue3;
        }
        V oldValue4 = removeStash(key);
        return oldValue4;
    }

    V removeStash(K key) {
        K[] keyTable = this.keyTable;
        int i = this.capacity;
        int n = i + this.stashSize;
        while (i < n) {
            if (!key.equals(keyTable[i])) {
                i++;
            } else {
                V oldValue = this.valueTable[i];
                removeStashIndex(i);
                this.size--;
                return oldValue;
            }
        }
        return null;
    }

    void removeStashIndex(int index) {
        this.stashSize--;
        int lastIndex = this.capacity + this.stashSize;
        if (index < lastIndex) {
            this.keyTable[index] = this.keyTable[lastIndex];
            this.valueTable[index] = this.valueTable[lastIndex];
            this.valueTable[lastIndex] = null;
            return;
        }
        this.valueTable[index] = null;
    }

    public void clear() {
        K[] keyTable = this.keyTable;
        V[] valueTable = this.valueTable;
        int i = this.capacity + this.stashSize;
        while (true) {
            int i2 = i;
            i = i2 - 1;
            if (i2 > 0) {
                keyTable[i] = null;
                valueTable[i] = null;
            } else {
                this.size = 0;
                this.stashSize = 0;
                return;
            }
        }
    }

    public boolean containsValue(Object value, boolean identity) {
        V[] valueTable = this.valueTable;
        if (value == null) {
            K[] keyTable = this.keyTable;
            int i = this.capacity + this.stashSize;
            while (true) {
                int i2 = i;
                i = i2 - 1;
                if (i2 <= 0) {
                    break;
                }
                if (keyTable[i] != null && valueTable[i] == null) {
                    return true;
                }
            }
        } else {
            if (identity) {
                int i3 = this.capacity + this.stashSize;
                do {
                    int i4 = i3;
                    i3 = i4 - 1;
                    if (i4 > 0) {
                    }
                } while (valueTable[i3] != value);
                return true;
            }
            int i5 = this.capacity + this.stashSize;
            do {
                int i6 = i5;
                i5 = i6 - 1;
                if (i6 > 0) {
                }
            } while (!value.equals(valueTable[i5]));
            return true;
        }
        return false;
    }

    public boolean containsKey(K key) {
        int hashCode = key.hashCode();
        int index = hashCode & this.mask;
        if (!key.equals(this.keyTable[index])) {
            int index2 = hash2(hashCode);
            if (!key.equals(this.keyTable[index2])) {
                int index3 = hash3(hashCode);
                if (!key.equals(this.keyTable[index3])) {
                    return containsKeyStash(key);
                }
            }
        }
        return true;
    }

    private boolean containsKeyStash(K key) {
        K[] keyTable = this.keyTable;
        int i = this.capacity;
        int n = i + this.stashSize;
        while (i < n) {
            if (key.equals(keyTable[i])) {
                return true;
            }
            i++;
        }
        return false;
    }

    public K findKey(Object value, boolean identity) {
        V[] valueTable = this.valueTable;
        if (value == null) {
            K[] keyTable = this.keyTable;
            int i = this.capacity + this.stashSize;
            while (true) {
                int i2 = i;
                i = i2 - 1;
                if (i2 <= 0) {
                    break;
                }
                if (keyTable[i] != null && valueTable[i] == null) {
                    return keyTable[i];
                }
            }
        } else {
            if (identity) {
                int i3 = this.capacity + this.stashSize;
                do {
                    int i4 = i3;
                    i3 = i4 - 1;
                    if (i4 > 0) {
                    }
                } while (valueTable[i3] != value);
                return this.keyTable[i3];
            }
            int i5 = this.capacity + this.stashSize;
            do {
                int i6 = i5;
                i5 = i6 - 1;
                if (i6 > 0) {
                }
            } while (!value.equals(valueTable[i5]));
            return this.keyTable[i5];
        }
        return null;
    }

    public void ensureCapacity(int additionalCapacity) {
        int sizeNeeded = this.size + additionalCapacity;
        if (sizeNeeded >= this.threshold) {
            resize(MathUtils.nextPowerOfTwo((int) (sizeNeeded / this.loadFactor)));
        }
    }

    private void resize(int i) {
        int i2 = this.capacity + this.stashSize;
        this.capacity = i;
        this.threshold = (int) (i * this.loadFactor);
        this.mask = i - 1;
        this.hashShift = 31 - Integer.numberOfTrailingZeros(i);
        this.stashCapacity = Math.max(3, ((int) Math.ceil(Math.log(i))) * 2);
        this.pushIterations = Math.max(Math.min(i, 8), ((int) Math.sqrt(i)) / 8);
        K[] kArr = this.keyTable;
        V[] vArr = this.valueTable;
        this.keyTable = (K[]) new Object[this.stashCapacity + i];
        this.valueTable = (V[]) new Object[this.stashCapacity + i];
        this.size = 0;
        this.stashSize = 0;
        for (int i3 = 0; i3 < i2; i3++) {
            K k = kArr[i3];
            if (k != null) {
                putResize(k, vArr[i3]);
            }
        }
    }

    private int hash2(int h) {
        int h2 = h * PRIME2;
        return ((h2 >>> this.hashShift) ^ h2) & this.mask;
    }

    private int hash3(int h) {
        int h2 = h * PRIME3;
        return ((h2 >>> this.hashShift) ^ h2) & this.mask;
    }

    public String toString() {
        if (this.size == 0) {
            return "{}";
        }
        StringBuilder buffer = new StringBuilder(32);
        buffer.append('{');
        K[] keyTable = this.keyTable;
        V[] valueTable = this.valueTable;
        int i = keyTable.length;
        while (true) {
            int i2 = i;
            i = i2 - 1;
            if (i2 > 0) {
                K key = keyTable[i];
                if (key != null) {
                    buffer.append(key);
                    buffer.append(SignatureVisitor.INSTANCEOF);
                    buffer.append(valueTable[i]);
                    break;
                }
            } else {
                break;
            }
        }
        while (true) {
            int i3 = i;
            i = i3 - 1;
            if (i3 > 0) {
                K key2 = keyTable[i];
                if (key2 != null) {
                    buffer.append(", ");
                    buffer.append(key2);
                    buffer.append(SignatureVisitor.INSTANCEOF);
                    buffer.append(valueTable[i]);
                }
            } else {
                buffer.append('}');
                return buffer.toString();
            }
        }
    }

    public Entries<K, V> entries() {
        if (this.entries == null) {
            this.entries = new Entries(this);
        } else {
            this.entries.reset();
        }
        return this.entries;
    }

    public Values<V> values() {
        if (this.values == null) {
            this.values = new Values(this);
        } else {
            this.values.reset();
        }
        return this.values;
    }

    public Keys<K> keys() {
        if (this.keys == null) {
            this.keys = new Keys(this);
        } else {
            this.keys.reset();
        }
        return this.keys;
    }

    public static class Entry<K, V> {
        public K key;
        public V value;

        public String toString() {
            return this.key + "=" + this.value;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class MapIterator<K, V> {
        int currentIndex;
        public boolean hasNext;
        final ObjectMap<K, V> map;
        int nextIndex;

        public MapIterator(ObjectMap<K, V> map) {
            this.map = map;
            reset();
        }

        public void reset() {
            this.currentIndex = -1;
            this.nextIndex = -1;
            advance();
        }

        void advance() {
            this.hasNext = false;
            K[] keyTable = this.map.keyTable;
            int n = this.map.capacity + this.map.stashSize;
            do {
                int i = this.nextIndex + 1;
                this.nextIndex = i;
                if (i >= n) {
                    return;
                }
            } while (keyTable[this.nextIndex] == null);
            this.hasNext = true;
        }

        public void remove() {
            if (this.currentIndex < 0) {
                throw new IllegalStateException("next must be called before remove.");
            }
            if (this.currentIndex >= this.map.capacity) {
                this.map.removeStashIndex(this.currentIndex);
            } else {
                this.map.keyTable[this.currentIndex] = null;
                this.map.valueTable[this.currentIndex] = null;
            }
            this.currentIndex = -1;
            this.map.size--;
        }
    }

    public static class Entries<K, V> extends MapIterator<K, V> implements Iterable<Entry<K, V>>, Iterator<Entry<K, V>> {
        Entry<K, V> entry;

        @Override // com.badlogic.gdx.utils.ObjectMap.MapIterator, java.util.Iterator
        public /* bridge */ /* synthetic */ void remove() {
            super.remove();
        }

        @Override // com.badlogic.gdx.utils.ObjectMap.MapIterator
        public /* bridge */ /* synthetic */ void reset() {
            super.reset();
        }

        public Entries(ObjectMap<K, V> map) {
            super(map);
            this.entry = new Entry<>();
        }

        @Override // java.util.Iterator
        public Entry<K, V> next() {
            if (!this.hasNext) {
                throw new NoSuchElementException();
            }
            K[] keyTable = this.map.keyTable;
            this.entry.key = keyTable[this.nextIndex];
            this.entry.value = this.map.valueTable[this.nextIndex];
            this.currentIndex = this.nextIndex;
            advance();
            return this.entry;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.hasNext;
        }

        @Override // java.lang.Iterable
        public Iterator<Entry<K, V>> iterator() {
            return this;
        }
    }

    public static class Values<V> extends MapIterator<Object, V> implements Iterable<V>, Iterator<V> {
        @Override // com.badlogic.gdx.utils.ObjectMap.MapIterator, java.util.Iterator
        public /* bridge */ /* synthetic */ void remove() {
            super.remove();
        }

        @Override // com.badlogic.gdx.utils.ObjectMap.MapIterator
        public /* bridge */ /* synthetic */ void reset() {
            super.reset();
        }

        public Values(ObjectMap<?, V> map) {
            super(map);
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.hasNext;
        }

        @Override // java.util.Iterator
        public V next() {
            V value = this.map.valueTable[this.nextIndex];
            this.currentIndex = this.nextIndex;
            advance();
            return value;
        }

        @Override // java.lang.Iterable
        public Iterator<V> iterator() {
            return this;
        }

        public Array<V> toArray() {
            Array<V> array = new Array<>(true, this.map.size);
            while (this.hasNext) {
                array.add(next());
            }
            return array;
        }

        public void toArray(Array<V> array) {
            while (this.hasNext) {
                array.add(next());
            }
        }
    }

    public static class Keys<K> extends MapIterator<K, Object> implements Iterable<K>, Iterator<K> {
        @Override // com.badlogic.gdx.utils.ObjectMap.MapIterator, java.util.Iterator
        public /* bridge */ /* synthetic */ void remove() {
            super.remove();
        }

        @Override // com.badlogic.gdx.utils.ObjectMap.MapIterator
        public /* bridge */ /* synthetic */ void reset() {
            super.reset();
        }

        public Keys(ObjectMap<K, ?> map) {
            super(map);
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.hasNext;
        }

        @Override // java.util.Iterator
        public K next() {
            K key = this.map.keyTable[this.nextIndex];
            this.currentIndex = this.nextIndex;
            advance();
            return key;
        }

        @Override // java.lang.Iterable
        public Iterator<K> iterator() {
            return this;
        }

        public Array<K> toArray() {
            Array<K> array = new Array<>(true, this.map.size);
            while (this.hasNext) {
                array.add(next());
            }
            return array;
        }
    }
}
