package com.badlogic.gdx.utils;

import com.badlogic.gdx.math.MathUtils;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.codehaus.jackson.org.objectweb.asm.signature.SignatureVisitor;

/* JADX INFO: loaded from: classes.dex */
public class ObjectIntMap<K> {
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
    int[] valueTable;
    private Values values;

    public ObjectIntMap() {
        this(32, 0.8f);
    }

    public ObjectIntMap(int initialCapacity) {
        this(initialCapacity, 0.8f);
    }

    public ObjectIntMap(int i, float f) {
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
        this.valueTable = new int[this.keyTable.length];
    }

    public void put(K key, int value) {
        if (key == null) {
            throw new IllegalArgumentException("key cannot be null.");
        }
        K[] keyTable = this.keyTable;
        int hashCode = key.hashCode();
        int index1 = hashCode & this.mask;
        K key1 = keyTable[index1];
        if (key.equals(key1)) {
            this.valueTable[index1] = value;
            return;
        }
        int index2 = hash2(hashCode);
        K key2 = keyTable[index2];
        if (key.equals(key2)) {
            this.valueTable[index2] = value;
            return;
        }
        int index3 = hash3(hashCode);
        K key3 = keyTable[index3];
        if (key.equals(key3)) {
            this.valueTable[index3] = value;
            return;
        }
        int i = this.capacity;
        int n = i + this.stashSize;
        while (i < n) {
            if (!key.equals(keyTable[i])) {
                i++;
            } else {
                this.valueTable[i] = value;
                return;
            }
        }
        if (key1 == null) {
            keyTable[index1] = key;
            this.valueTable[index1] = value;
            int i2 = this.size;
            this.size = i2 + 1;
            if (i2 >= this.threshold) {
                resize(this.capacity << 1);
                return;
            }
            return;
        }
        if (key2 == null) {
            keyTable[index2] = key;
            this.valueTable[index2] = value;
            int i3 = this.size;
            this.size = i3 + 1;
            if (i3 >= this.threshold) {
                resize(this.capacity << 1);
                return;
            }
            return;
        }
        if (key3 == null) {
            keyTable[index3] = key;
            this.valueTable[index3] = value;
            int i4 = this.size;
            this.size = i4 + 1;
            if (i4 >= this.threshold) {
                resize(this.capacity << 1);
                return;
            }
            return;
        }
        push(key, value, index1, key1, index2, key2, index3, key3);
    }

    public void putAll(ObjectIntMap<K> map) {
        for (Entry<K> entry : map.entries()) {
            put(entry.key, entry.value);
        }
    }

    private void putResize(K key, int value) {
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

    private void push(K insertKey, int insertValue, int index1, K key1, int index2, K key2, int index3, K key3) {
        K evictedKey;
        int evictedValue;
        K[] keyTable = this.keyTable;
        int[] valueTable = this.valueTable;
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

    private void putStash(K key, int value) {
        if (this.stashSize == this.stashCapacity) {
            resize(this.capacity << 1);
            put(key, value);
            return;
        }
        int index = this.capacity + this.stashSize;
        this.keyTable[index] = key;
        this.valueTable[index] = value;
        this.stashSize++;
        this.size++;
    }

    public int get(K key, int defaultValue) {
        int hashCode = key.hashCode();
        int index = hashCode & this.mask;
        if (!key.equals(this.keyTable[index])) {
            index = hash2(hashCode);
            if (!key.equals(this.keyTable[index])) {
                index = hash3(hashCode);
                if (!key.equals(this.keyTable[index])) {
                    return getStash(key, defaultValue);
                }
            }
        }
        return this.valueTable[index];
    }

    private int getStash(K key, int defaultValue) {
        K[] keyTable = this.keyTable;
        int i = this.capacity;
        int n = i + this.stashSize;
        while (i < n) {
            if (!key.equals(keyTable[i])) {
                i++;
            } else {
                int defaultValue2 = this.valueTable[i];
                return defaultValue2;
            }
        }
        return defaultValue;
    }

    public int getAndIncrement(K key, int defaultValue, int increment) {
        int hashCode = key.hashCode();
        int index = hashCode & this.mask;
        if (!key.equals(this.keyTable[index])) {
            index = hash2(hashCode);
            if (!key.equals(this.keyTable[index])) {
                index = hash3(hashCode);
                if (!key.equals(this.keyTable[index])) {
                    return getAndIncrementStash(key, defaultValue, increment);
                }
            }
        }
        int value = this.valueTable[index];
        this.valueTable[index] = value + increment;
        return value;
    }

    private int getAndIncrementStash(K key, int defaultValue, int increment) {
        K[] keyTable = this.keyTable;
        int i = this.capacity;
        int n = i + this.stashSize;
        while (i < n) {
            if (!key.equals(keyTable[i])) {
                i++;
            } else {
                int value = this.valueTable[i];
                this.valueTable[i] = value + increment;
                return value;
            }
        }
        put(key, defaultValue + increment);
        return defaultValue;
    }

    public int remove(K key, int defaultValue) {
        int hashCode = key.hashCode();
        int index = hashCode & this.mask;
        if (key.equals(this.keyTable[index])) {
            this.keyTable[index] = null;
            int oldValue = this.valueTable[index];
            this.size--;
            return oldValue;
        }
        int index2 = hash2(hashCode);
        if (key.equals(this.keyTable[index2])) {
            this.keyTable[index2] = null;
            int oldValue2 = this.valueTable[index2];
            this.size--;
            return oldValue2;
        }
        int index3 = hash3(hashCode);
        if (key.equals(this.keyTable[index3])) {
            this.keyTable[index3] = null;
            int oldValue3 = this.valueTable[index3];
            this.size--;
            return oldValue3;
        }
        int oldValue4 = removeStash(key, defaultValue);
        return oldValue4;
    }

    int removeStash(K key, int defaultValue) {
        K[] keyTable = this.keyTable;
        int i = this.capacity;
        int n = i + this.stashSize;
        while (i < n) {
            if (!key.equals(keyTable[i])) {
                i++;
            } else {
                int oldValue = this.valueTable[i];
                removeStashIndex(i);
                this.size--;
                return oldValue;
            }
        }
        return defaultValue;
    }

    void removeStashIndex(int index) {
        this.stashSize--;
        int lastIndex = this.capacity + this.stashSize;
        if (index < lastIndex) {
            this.keyTable[index] = this.keyTable[lastIndex];
            this.valueTable[index] = this.valueTable[lastIndex];
        }
    }

    public void clear() {
        K[] keyTable = this.keyTable;
        int[] iArr = this.valueTable;
        int i = this.capacity + this.stashSize;
        while (true) {
            int i2 = i;
            i = i2 - 1;
            if (i2 > 0) {
                keyTable[i] = null;
            } else {
                this.size = 0;
                this.stashSize = 0;
                return;
            }
        }
    }

    public boolean containsValue(int value) {
        int[] valueTable = this.valueTable;
        int i = this.capacity + this.stashSize;
        do {
            int i2 = i;
            i = i2 - 1;
            if (i2 <= 0) {
                return false;
            }
        } while (valueTable[i] != value);
        return true;
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

    public K findKey(int value) {
        int[] valueTable = this.valueTable;
        int i = this.capacity + this.stashSize;
        do {
            int i2 = i;
            i = i2 - 1;
            if (i2 <= 0) {
                return null;
            }
        } while (valueTable[i] != value);
        return this.keyTable[i];
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
        int[] iArr = this.valueTable;
        this.keyTable = (K[]) new Object[this.stashCapacity + i];
        this.valueTable = new int[this.stashCapacity + i];
        this.size = 0;
        this.stashSize = 0;
        for (int i3 = 0; i3 < i2; i3++) {
            K k = kArr[i3];
            if (k != null) {
                putResize(k, iArr[i3]);
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
        int[] valueTable = this.valueTable;
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

    public Entries<K> entries() {
        if (this.entries == null) {
            this.entries = new Entries(this);
        } else {
            this.entries.reset();
        }
        return this.entries;
    }

    public Values values() {
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

    public static class Entry<K> {
        public K key;
        public int value;

        public String toString() {
            return this.key + "=" + this.value;
        }
    }

    private static class MapIterator<K> {
        int currentIndex;
        public boolean hasNext;
        final ObjectIntMap<K> map;
        int nextIndex;

        public MapIterator(ObjectIntMap<K> map) {
            this.map = map;
            reset();
        }

        public void reset() {
            this.currentIndex = -1;
            this.nextIndex = -1;
            findNextIndex();
        }

        void findNextIndex() {
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
            }
            this.currentIndex = -1;
            this.map.size--;
        }
    }

    public static class Entries<K> extends MapIterator<K> implements Iterable<Entry<K>>, Iterator<Entry<K>> {
        private Entry<K> entry;

        @Override // com.badlogic.gdx.utils.ObjectIntMap.MapIterator, java.util.Iterator
        public /* bridge */ /* synthetic */ void remove() {
            super.remove();
        }

        @Override // com.badlogic.gdx.utils.ObjectIntMap.MapIterator
        public /* bridge */ /* synthetic */ void reset() {
            super.reset();
        }

        public Entries(ObjectIntMap<K> map) {
            super(map);
            this.entry = new Entry<>();
        }

        @Override // java.util.Iterator
        public Entry<K> next() {
            if (!this.hasNext) {
                throw new NoSuchElementException();
            }
            K[] keyTable = this.map.keyTable;
            this.entry.key = keyTable[this.nextIndex];
            this.entry.value = this.map.valueTable[this.nextIndex];
            this.currentIndex = this.nextIndex;
            findNextIndex();
            return this.entry;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.hasNext;
        }

        @Override // java.lang.Iterable
        public Iterator<Entry<K>> iterator() {
            return this;
        }
    }

    public static class Values extends MapIterator<Object> {
        @Override // com.badlogic.gdx.utils.ObjectIntMap.MapIterator, java.util.Iterator
        public /* bridge */ /* synthetic */ void remove() {
            super.remove();
        }

        @Override // com.badlogic.gdx.utils.ObjectIntMap.MapIterator
        public /* bridge */ /* synthetic */ void reset() {
            super.reset();
        }

        public Values(ObjectIntMap<?> map) {
            super(map);
        }

        public boolean hasNext() {
            return this.hasNext;
        }

        public int next() {
            int value = this.map.valueTable[this.nextIndex];
            this.currentIndex = this.nextIndex;
            findNextIndex();
            return value;
        }

        public IntArray toArray() {
            IntArray array = new IntArray(true, this.map.size);
            while (this.hasNext) {
                array.add(next());
            }
            return array;
        }
    }

    public static class Keys<K> extends MapIterator<K> implements Iterable<K>, Iterator<K> {
        @Override // com.badlogic.gdx.utils.ObjectIntMap.MapIterator, java.util.Iterator
        public /* bridge */ /* synthetic */ void remove() {
            super.remove();
        }

        @Override // com.badlogic.gdx.utils.ObjectIntMap.MapIterator
        public /* bridge */ /* synthetic */ void reset() {
            super.reset();
        }

        public Keys(ObjectIntMap<K> map) {
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
            findNextIndex();
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
