package com.badlogic.gdx.utils;

import com.badlogic.gdx.math.MathUtils;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public class FloatArray {
    public float[] items;
    public boolean ordered;
    public int size;

    public FloatArray() {
        this(true, 16);
    }

    public FloatArray(int capacity) {
        this(true, capacity);
    }

    public FloatArray(boolean ordered, int capacity) {
        this.ordered = ordered;
        this.items = new float[capacity];
    }

    public FloatArray(FloatArray array) {
        this.ordered = array.ordered;
        this.size = array.size;
        this.items = new float[this.size];
        System.arraycopy(array.items, 0, this.items, 0, this.size);
    }

    public FloatArray(float[] array) {
        this(true, array);
    }

    public FloatArray(boolean ordered, float[] array) {
        this(ordered, array.length);
        this.size = array.length;
        System.arraycopy(array, 0, this.items, 0, this.size);
    }

    public void add(float value) {
        float[] items = this.items;
        if (this.size == items.length) {
            items = resize(Math.max(8, (int) (this.size * 1.75f)));
        }
        int i = this.size;
        this.size = i + 1;
        items[i] = value;
    }

    public void addAll(FloatArray array) {
        addAll(array, 0, array.size);
    }

    public void addAll(FloatArray array, int offset, int length) {
        if (offset + length > array.size) {
            throw new IllegalArgumentException("offset + length must be <= size: " + offset + " + " + length + " <= " + array.size);
        }
        addAll(array.items, offset, length);
    }

    public void addAll(float[] array) {
        addAll(array, 0, array.length);
    }

    public void addAll(float[] array, int offset, int length) {
        float[] items = this.items;
        int sizeNeeded = (this.size + length) - offset;
        if (sizeNeeded >= items.length) {
            items = resize(Math.max(8, (int) (sizeNeeded * 1.75f)));
        }
        System.arraycopy(array, offset, items, this.size, length);
        this.size += length;
    }

    public float get(int index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException(String.valueOf(index));
        }
        return this.items[index];
    }

    public void set(int index, float value) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException(String.valueOf(index));
        }
        this.items[index] = value;
    }

    public void insert(int index, float value) {
        float[] items = this.items;
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
        float[] items = this.items;
        float firstValue = items[first];
        items[first] = items[second];
        items[second] = firstValue;
    }

    public boolean contains(float value) {
        int i = this.size - 1;
        float[] items = this.items;
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

    public int indexOf(float value) {
        float[] items = this.items;
        int n = this.size;
        for (int i = 0; i < n; i++) {
            if (items[i] == value) {
                return i;
            }
        }
        return -1;
    }

    public int lastIndexOf(char value) {
        float[] items = this.items;
        for (int i = this.size - 1; i >= 0; i--) {
            if (items[i] == value) {
                return i;
            }
        }
        return -1;
    }

    public boolean removeValue(float value) {
        float[] items = this.items;
        int n = this.size;
        for (int i = 0; i < n; i++) {
            if (items[i] == value) {
                removeIndex(i);
                return true;
            }
        }
        return false;
    }

    public float removeIndex(int index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException(String.valueOf(index));
        }
        float[] items = this.items;
        float value = items[index];
        this.size--;
        if (this.ordered) {
            System.arraycopy(items, index + 1, items, index, this.size - index);
        } else {
            items[index] = items[this.size];
        }
        return value;
    }

    public float pop() {
        float[] fArr = this.items;
        int i = this.size - 1;
        this.size = i;
        return fArr[i];
    }

    public float peek() {
        return this.items[this.size - 1];
    }

    public float first() {
        return this.items[0];
    }

    public void clear() {
        this.size = 0;
    }

    public void shrink() {
        resize(this.size);
    }

    public float[] ensureCapacity(int additionalCapacity) {
        int sizeNeeded = this.size + additionalCapacity;
        if (sizeNeeded >= this.items.length) {
            resize(Math.max(8, sizeNeeded));
        }
        return this.items;
    }

    protected float[] resize(int newSize) {
        float[] newItems = new float[newSize];
        float[] items = this.items;
        System.arraycopy(items, 0, newItems, 0, Math.min(this.size, newItems.length));
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
            float temp = this.items[i];
            this.items[i] = this.items[ii];
            this.items[ii] = temp;
        }
    }

    public void shuffle() {
        for (int i = this.size - 1; i >= 0; i--) {
            int ii = MathUtils.random(i);
            float temp = this.items[i];
            this.items[i] = this.items[ii];
            this.items[ii] = temp;
        }
    }

    public void truncate(int newSize) {
        if (this.size > newSize) {
            this.size = newSize;
        }
    }

    public float random() {
        if (this.size == 0) {
            return 0.0f;
        }
        return this.items[MathUtils.random(0, this.size - 1)];
    }

    public float[] toArray() {
        float[] array = new float[this.size];
        System.arraycopy(this.items, 0, array, 0, this.size);
        return array;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof FloatArray)) {
            return false;
        }
        FloatArray array = (FloatArray) object;
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
        float[] items = this.items;
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
        float[] items = this.items;
        StringBuilder buffer = new StringBuilder(32);
        buffer.append(items[0]);
        for (int i = 1; i < this.size; i++) {
            buffer.append(separator);
            buffer.append(items[i]);
        }
        return buffer.toString();
    }
}
