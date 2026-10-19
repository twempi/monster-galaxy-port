package org.codehaus.jackson.map.deser.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.codehaus.jackson.map.deser.SettableBeanProperty;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class BeanPropertyMap {
    private final Bucket[] _buckets;
    private final int _hashMask;
    private final int _size;

    public BeanPropertyMap(Collection<SettableBeanProperty> properties) {
        this._size = properties.size();
        int bucketCount = findSize(this._size);
        this._hashMask = bucketCount - 1;
        Bucket[] buckets = new Bucket[bucketCount];
        for (SettableBeanProperty property : properties) {
            String key = property.getName();
            int index = key.hashCode() & this._hashMask;
            buckets[index] = new Bucket(buckets[index], key, property);
        }
        this._buckets = buckets;
    }

    public void assignIndexes() {
        int index = 0;
        Bucket[] arr$ = this._buckets;
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            Bucket bucket = arr$[i$];
            int index2 = index;
            while (bucket != null) {
                bucket.value.assignIndex(index2);
                bucket = bucket.next;
                index2++;
            }
            i$++;
            index = index2;
        }
    }

    private static final int findSize(int size) {
        int needed = size <= 32 ? size + size : size + (size >> 2);
        int result = 2;
        while (result < needed) {
            result += result;
        }
        return result;
    }

    public int size() {
        return this._size;
    }

    public Iterator<SettableBeanProperty> allProperties() {
        return new IteratorImpl(this._buckets);
    }

    public SettableBeanProperty find(String key) {
        int index = key.hashCode() & this._hashMask;
        Bucket bucket = this._buckets[index];
        if (bucket == null) {
            return null;
        }
        if (bucket.key == key) {
            return bucket.value;
        }
        do {
            bucket = bucket.next;
            if (bucket == null) {
                return _findWithEquals(key, index);
            }
        } while (bucket.key != key);
        return bucket.value;
    }

    public void replace(SettableBeanProperty property) {
        Bucket tail;
        String name = property.getName();
        int index = name.hashCode() & (this._buckets.length - 1);
        boolean found = false;
        Bucket bucket = this._buckets[index];
        Bucket tail2 = null;
        while (bucket != null) {
            if (!found && bucket.key.equals(name)) {
                found = true;
                tail = tail2;
            } else {
                tail = new Bucket(tail2, bucket.key, bucket.value);
            }
            bucket = bucket.next;
            tail2 = tail;
        }
        if (!found) {
            throw new NoSuchElementException("No entry '" + property + "' found, can't replace");
        }
        this._buckets[index] = new Bucket(tail2, name, property);
    }

    private SettableBeanProperty _findWithEquals(String key, int index) {
        for (Bucket bucket = this._buckets[index]; bucket != null; bucket = bucket.next) {
            if (key.equals(bucket.key)) {
                return bucket.value;
            }
        }
        return null;
    }

    private static final class Bucket {
        public final String key;
        public final Bucket next;
        public final SettableBeanProperty value;

        public Bucket(Bucket next, String key, SettableBeanProperty value) {
            this.next = next;
            this.key = key;
            this.value = value;
        }
    }

    private static final class IteratorImpl implements Iterator<SettableBeanProperty> {
        private final Bucket[] _buckets;
        private Bucket _currentBucket;
        private int _nextBucketIndex;

        public IteratorImpl(Bucket[] buckets) {
            int i;
            this._buckets = buckets;
            int len = this._buckets.length;
            int i2 = 0;
            while (i2 < len) {
                i = i2 + 1;
                Bucket b = this._buckets[i2];
                if (b != null) {
                    this._currentBucket = b;
                    this._nextBucketIndex = i;
                }
                i2 = i;
            }
            i = i2;
            this._nextBucketIndex = i;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this._currentBucket != null;
        }

        @Override // java.util.Iterator
        public SettableBeanProperty next() {
            Bucket curr = this._currentBucket;
            if (curr == null) {
                throw new NoSuchElementException();
            }
            Bucket b = curr.next;
            while (b == null && this._nextBucketIndex < this._buckets.length) {
                Bucket[] bucketArr = this._buckets;
                int i = this._nextBucketIndex;
                this._nextBucketIndex = i + 1;
                b = bucketArr[i];
            }
            this._currentBucket = b;
            return curr.value;
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
