package com.badlogic.gdx.utils;

import com.badlogic.gdx.math.MathUtils;

/* JADX INFO: loaded from: classes.dex */
public class BooleanArray {
    public boolean[] items;
    public boolean ordered;
    public int size;

    public BooleanArray() {
        this(true, 16);
    }

    public BooleanArray(int capacity) {
        this(true, capacity);
    }

    public BooleanArray(boolean ordered, int capacity) {
        this.ordered = ordered;
        this.items = new boolean[capacity];
    }

    public BooleanArray(BooleanArray array) {
        this.ordered = array.ordered;
        this.size = array.size;
        this.items = new boolean[this.size];
        System.arraycopy(array.items, 0, this.items, 0, this.size);
    }

    public BooleanArray(boolean[] array) {
        this(true, array);
    }

    public BooleanArray(boolean ordered, boolean[] array) {
        this(ordered, array.length);
        this.size = array.length;
        System.arraycopy(array, 0, this.items, 0, this.size);
    }

    public void add(boolean value) {
        boolean[] items = this.items;
        if (this.size == items.length) {
            items = resize(Math.max(8, (int) (this.size * 1.75f)));
        }
        int i = this.size;
        this.size = i + 1;
        items[i] = value;
    }

    public void addAll(BooleanArray array) {
        addAll(array, 0, array.size);
    }

    public void addAll(BooleanArray array, int offset, int length) {
        if (offset + length > array.size) {
            throw new IllegalArgumentException("offset + length must be <= size: " + offset + " + " + length + " <= " + array.size);
        }
        addAll(array.items, offset, length);
    }

    public void addAll(boolean[] array) {
        addAll(array, 0, array.length);
    }

    public void addAll(boolean[] array, int offset, int length) {
        boolean[] items = this.items;
        int sizeNeeded = (this.size + length) - offset;
        if (sizeNeeded >= items.length) {
            items = resize(Math.max(8, (int) (sizeNeeded * 1.75f)));
        }
        System.arraycopy(array, offset, items, this.size, length);
        this.size += length;
    }

    public boolean get(int index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException(String.valueOf(index));
        }
        return this.items[index];
    }

    public void set(int index, boolean value) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException(String.valueOf(index));
        }
        this.items[index] = value;
    }

    public void insert(int index, boolean value) {
        boolean[] items = this.items;
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

    public void swap(int first, int second) {
        if (first >= this.size) {
            throw new IndexOutOfBoundsException(String.valueOf(first));
        }
        if (second >= this.size) {
            throw new IndexOutOfBoundsException(String.valueOf(second));
        }
        boolean[] items = this.items;
        boolean firstValue = items[first];
        items[first] = items[second];
        items[second] = firstValue;
    }

    public boolean removeIndex(int index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException(String.valueOf(index));
        }
        boolean[] items = this.items;
        boolean value = items[index];
        this.size--;
        if (this.ordered) {
            System.arraycopy(items, index + 1, items, index, this.size - index);
        } else {
            items[index] = items[this.size];
        }
        return value;
    }

    public boolean pop() {
        boolean[] zArr = this.items;
        int i = this.size - 1;
        this.size = i;
        return zArr[i];
    }

    public boolean peek() {
        return this.items[this.size - 1];
    }

    public boolean first() {
        return this.items[0];
    }

    public void clear() {
        this.size = 0;
    }

    public void shrink() {
        resize(this.size);
    }

    public boolean[] ensureCapacity(int additionalCapacity) {
        int sizeNeeded = this.size + additionalCapacity;
        if (sizeNeeded >= this.items.length) {
            resize(Math.max(8, sizeNeeded));
        }
        return this.items;
    }

    protected boolean[] resize(int newSize) {
        boolean[] newItems = new boolean[newSize];
        boolean[] items = this.items;
        System.arraycopy(items, 0, newItems, 0, Math.min(this.size, newItems.length));
        this.items = newItems;
        return newItems;
    }

    public void reverse() {
        int lastIndex = this.size - 1;
        int n = this.size / 2;
        for (int i = 0; i < n; i++) {
            int ii = lastIndex - i;
            boolean temp = this.items[i];
            this.items[i] = this.items[ii];
            this.items[ii] = temp;
        }
    }

    public void shuffle() {
        for (int i = this.size - 1; i >= 0; i--) {
            int ii = MathUtils.random(i);
            boolean temp = this.items[i];
            this.items[i] = this.items[ii];
            this.items[ii] = temp;
        }
    }

    public void truncate(int newSize) {
        if (this.size > newSize) {
            this.size = newSize;
        }
    }

    public boolean random() {
        if (this.size == 0) {
            return false;
        }
        return this.items[MathUtils.random(0, this.size - 1)];
    }

    public boolean[] toArray() {
        boolean[] array = new boolean[this.size];
        System.arraycopy(this.items, 0, array, 0, this.size);
        return array;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof BooleanArray)) {
            return false;
        }
        BooleanArray array = (BooleanArray) object;
        int n = this.size;
        if (n != array.size) {
            return false;
        }
        for (int i = 0; i < n; i++) {
            if (this.items[i] != array.items[i]) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        if (this.size == 0) {
            return "[]";
        }
        boolean[] items = this.items;
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

    public String toString(String separator) {
        if (this.size == 0) {
            return "";
        }
        boolean[] items = this.items;
        StringBuilder buffer = new StringBuilder(32);
        buffer.append(items[0]);
        for (int i = 1; i < this.size; i++) {
            buffer.append(separator);
            buffer.append(items[i]);
        }
        return buffer.toString();
    }
}
