package com.badlogic.gdx.utils;

import java.util.Comparator;

/* JADX INFO: loaded from: classes.dex */
public class SnapshotArray<T> extends Array<T> {
    private T[] recycled;
    private T[] snapshot;
    private int snapshots;

    public SnapshotArray() {
    }

    public SnapshotArray(Array array) {
        super(array);
    }

    public SnapshotArray(boolean ordered, int capacity, Class<T> arrayType) {
        super(ordered, capacity, arrayType);
    }

    public SnapshotArray(boolean ordered, int capacity) {
        super(ordered, capacity);
    }

    public SnapshotArray(boolean ordered, T[] array) {
        super(ordered, array);
    }

    public SnapshotArray(Class<T> arrayType) {
        super(arrayType);
    }

    public SnapshotArray(int capacity) {
        super(capacity);
    }

    public SnapshotArray(T[] array) {
        super(array);
    }

    public T[] begin() {
        this.snapshot = this.items;
        this.snapshots++;
        return this.items;
    }

    public void end() {
        this.snapshots = Math.max(0, this.snapshots - 1);
        if (this.snapshot != null) {
            if (this.snapshot != this.items && this.snapshots == 0) {
                this.recycled = this.snapshot;
                int n = this.recycled.length;
                for (int i = 0; i < n; i++) {
                    this.recycled[i] = null;
                }
            }
            this.snapshot = null;
        }
    }

    private void modified() {
        if (this.snapshot != null && this.snapshot == this.items) {
            if (this.recycled != null && this.recycled.length >= this.size) {
                System.arraycopy(this.items, 0, this.recycled, 0, this.size);
                this.items = this.recycled;
                this.recycled = null;
                return;
            }
            resize(this.items.length);
        }
    }

    @Override // com.badlogic.gdx.utils.Array
    public void set(int index, T value) {
        modified();
        super.set(index, value);
    }

    @Override // com.badlogic.gdx.utils.Array
    public void insert(int index, T value) {
        modified();
        super.insert(index, value);
    }

    @Override // com.badlogic.gdx.utils.Array
    public void swap(int first, int second) {
        modified();
        super.swap(first, second);
    }

    @Override // com.badlogic.gdx.utils.Array
    public boolean removeValue(T value, boolean identity) {
        modified();
        return super.removeValue(value, identity);
    }

    @Override // com.badlogic.gdx.utils.Array
    public T removeIndex(int i) {
        modified();
        return (T) super.removeIndex(i);
    }

    @Override // com.badlogic.gdx.utils.Array
    public T pop() {
        modified();
        return (T) super.pop();
    }

    @Override // com.badlogic.gdx.utils.Array
    public void clear() {
        modified();
        super.clear();
    }

    @Override // com.badlogic.gdx.utils.Array
    public void sort() {
        modified();
        super.sort();
    }

    @Override // com.badlogic.gdx.utils.Array
    public void sort(Comparator<T> comparator) {
        modified();
        super.sort(comparator);
    }

    @Override // com.badlogic.gdx.utils.Array
    public void reverse() {
        modified();
        super.reverse();
    }

    @Override // com.badlogic.gdx.utils.Array
    public void shuffle() {
        modified();
        super.shuffle();
    }

    @Override // com.badlogic.gdx.utils.Array
    public void truncate(int newSize) {
        modified();
        super.truncate(newSize);
    }
}
