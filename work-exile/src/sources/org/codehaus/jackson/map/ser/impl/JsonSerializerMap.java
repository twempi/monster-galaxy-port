package org.codehaus.jackson.map.ser.impl;

import java.util.Map;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ser.SerializerCache;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class JsonSerializerMap {
    private final Bucket[] _buckets;
    private final int _size;

    public JsonSerializerMap(Map<SerializerCache.TypeKey, JsonSerializer<Object>> serializers) {
        int size = findSize(serializers.size());
        this._size = size;
        int hashMask = size - 1;
        Bucket[] buckets = new Bucket[size];
        for (Map.Entry<SerializerCache.TypeKey, JsonSerializer<Object>> entry : serializers.entrySet()) {
            SerializerCache.TypeKey key = entry.getKey();
            int index = key.hashCode() & hashMask;
            buckets[index] = new Bucket(buckets[index], key, entry.getValue());
        }
        this._buckets = buckets;
    }

    private static final int findSize(int size) {
        int needed = size <= 64 ? size + size : size + (size >> 2);
        int result = 8;
        while (result < needed) {
            result += result;
        }
        return result;
    }

    public int size() {
        return this._size;
    }

    public JsonSerializer<Object> find(SerializerCache.TypeKey key) {
        int index = key.hashCode() & (this._buckets.length - 1);
        Bucket bucket = this._buckets[index];
        if (bucket == null) {
            return null;
        }
        if (key.equals(bucket.key)) {
            return bucket.value;
        }
        do {
            bucket = bucket.next;
            if (bucket == null) {
                return null;
            }
        } while (!key.equals(bucket.key));
        return bucket.value;
    }

    private static final class Bucket {
        public final SerializerCache.TypeKey key;
        public final Bucket next;
        public final JsonSerializer<Object> value;

        public Bucket(Bucket next, SerializerCache.TypeKey key, JsonSerializer<Object> value) {
            this.next = next;
            this.key = key;
            this.value = value;
        }
    }
}
