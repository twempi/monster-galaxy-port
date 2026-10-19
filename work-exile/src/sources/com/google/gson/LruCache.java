package com.google.gson;

import java.util.LinkedHashMap;
import java.util.Map;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
final class LruCache<K, V> extends LinkedHashMap<K, V> implements Cache<K, V> {
    private static final long serialVersionUID = 1;
    private final int maxCapacity;

    public LruCache(int maxCapacity) {
        super(maxCapacity, 0.7f, true);
        this.maxCapacity = maxCapacity;
    }

    @Override // com.google.gson.Cache
    public synchronized void addElement(K key, V value) {
        put(key, value);
    }

    @Override // com.google.gson.Cache
    public synchronized V getElement(K key) {
        return get(key);
    }

    @Override // com.google.gson.Cache
    public synchronized V removeElement(K k) {
        return (V) remove(k);
    }

    @Override // java.util.LinkedHashMap
    protected boolean removeEldestEntry(Map.Entry<K, V> entry) {
        return size() > this.maxCapacity;
    }
}
