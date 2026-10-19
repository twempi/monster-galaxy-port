package com.badlogic.gdx.utils;

import com.badlogic.gdx.math.MathUtils;
import java.util.Arrays;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class LongArray {
    public long[] items;
    public boolean ordered;
    public int size;

    public LongArray() {
        this(true, 16);
    }

    public LongArray(int capacity) {
        this(true, capacity);
    }

    public LongArray(boolean ordered, int capacity) {
        this.ordered = ordered;
        this.items = new long[capacity];
    }

    public LongArray(LongArray array) {
        this.ordered = array.ordered;
        this.size = array.size;
        this.items = new long[this.size];
        System.arraycopy(array.items, 0, this.items, 0, this.size);
    }

    public void add(long value) {
        long[] items = this.items;
        if (this.size == items.length) {
            items = resize(Math.max(8, (int) (this.size * 1.75f)));
        }
        int i = this.size;
        this.size = i + 1;
        items[i] = value;
    }

    public void addAll(LongArray array) {
        addAll(array, 0, array.size);
    }

    public void addAll(LongArray array, int offset, int length) {
        if (offset + length > array.size) {
            throw new IllegalArgumentException("offset + length must be <= size: " + offset + " + " + length + " <= " + array.size);
        }
        addAll(array.items, offset, length);
    }

    public void addAll(long[] array) {
        addAll(array, 0, array.length);
    }

    public void addAll(long[] array, int offset, int length) {
        long[] items = this.items;
        int sizeNeeded = (this.size + length) - offset;
        if (sizeNeeded >= items.length) {
            items = resize(Math.max(8, (int) (sizeNeeded * 1.75f)));
        }
        System.arraycopy(array, offset, items, this.size, length);
        this.size += length;
    }

    public long get(int index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException(String.valueOf(index));
        }
        return this.items[index];
    }

    public void set(int index, long value) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException(String.valueOf(index));
        }
        this.items[index] = value;
    }

    public void insert(int index, long value) {
        long[] items = this.items;
        if (this.size == items.length) {
            items = resize(Math.max(8, (int) (this.size * 1.75f)));
        }
        if (this.ordered) {
            System.arraycopy(items, index, items, index + 1, this.size - index);
        } else {
            items[this.size] = items[index];
        }
        this.size++;
        items[index] = value;
    }

    public boolean contains(long value) {
        int i = this.size - 1;
        long[] items = this.items;
        int i2 = i;
        while (i2 >= 0) {
            int i3 = i2 - 1;
            if (items[i2] == value) {
                return true;
            }
            i2 = i3;
        }
        return false;
    }

    public int indexOf(long value) {
        long[] items = this.items;
        int n = this.size;
        for (int i = 0; i < n; i++) {
            if (items[i] == value) {
                return i;
            }
        }
        return -1;
    }

    public boolean removeValue(long value) {
        long[] items = this.items;
        int n = this.size;
        for (int i = 0; i < n; i++) {
            if (items[i] == value) {
                removeIndex(i);
                return true;
            }
        }
        return false;
    }

    public long removeIndex(int index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException(String.valueOf(index));
        }
        long[] items = this.items;
        long value = items[index];
        this.size--;
        if (this.ordered) {
            System.arraycopy(items, index + 1, items, index, this.size - index);
        } else {
            items[index] = items[this.size];
        }
        return value;
    }

    public long pop() {
        long[] jArr = this.items;
        int i = this.size - 1;
        this.size = i;
        return jArr[i];
    }

    public long peek() {
        return this.items[this.size - 1];
    }

    public void clear() {
        this.size = 0;
    }

    public void shrink() {
        resize(this.size);
    }

    public long[] ensureCapacity(int additionalCapacity) {
        int sizeNeeded = this.size + additionalCapacity;
        if (sizeNeeded >= this.items.length) {
            resize(Math.max(8, sizeNeeded));
        }
        return this.items;
    }

    protected long[] resize(int newSize) {
        long[] newItems = new long[newSize];
        long[] items = this.items;
        System.arraycopy(items, 0, newItems, 0, Math.min(items.length, newItems.length));
        this.items = newItems;
        return newItems;
    }

    public void sort() {
        Arrays.sort(this.items, 0, this.size);
    }

    public void reverse() {
        int lastIndex = this.size - 1;
        int n = this.size / 2;
        for (int i = 0; i < n; i++) {
            int ii = lastIndex - i;
            long temp = this.items[i];
            this.items[i] = this.items[ii];
            this.items[ii] = temp;
        }
    }

    public void shuffle() {
        for (int i = this.size - 1; i >= 0; i--) {
            int ii = MathUtils.random(i);
            long temp = this.items[i];
            this.items[i] = this.items[ii];
            this.items[ii] = temp;
        }
    }

    public String toString() {
        if (this.size == 0) {
            return "[]";
        }
        long[] items = this.items;
        StringBuilder buffer = new StringBuilder(32);
        buffer.append('[');
        buffer.append(items[0]);
        for (int i = 1; i < this.size; i++) {
            buffer.append(", ");
            buffer.append(items[i]);
        }
        buffer.append(']');
        return buffer.toString();
    }
}
