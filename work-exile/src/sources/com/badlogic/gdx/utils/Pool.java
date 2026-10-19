package com.badlogic.gdx.utils;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public abstract class Pool<T> {
    private final Array<T> freeObjects;
    public final int max;

    protected abstract T newObject();

    public Pool() {
        this(16, Integer.MAX_VALUE);
    }

    public Pool(int initialCapacity) {
        this(initialCapacity, Integer.MAX_VALUE);
    }

    public Pool(int initialCapacity, int max) {
        this.freeObjects = new Array<>(false, initialCapacity);
        this.max = max;
    }

    public T obtain() {
        return this.freeObjects.size == 0 ? newObject() : this.freeObjects.pop();
    }

    public void free(T object) {
        if (object == null) {
            throw new IllegalArgumentException("object cannot be null.");
        }
        if (this.freeObjects.size < this.max) {
            this.freeObjects.add(object);
        }
    }

    public void free(Array<T> objects) {
        int n = Math.min(objects.size, this.max - this.freeObjects.size);
        for (int i = 0; i < n; i++) {
            this.freeObjects.add(objects.get(i));
        }
    }

    public void clear() {
        this.freeObjects.clear();
    }
}
