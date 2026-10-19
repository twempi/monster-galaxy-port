package com.badlogic.gdx.utils;

import com.badlogic.gdx.math.MathUtils;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Array<T> implements Iterable<T> {
    public T[] items;
    private ArrayIterator iterator;
    public boolean ordered;
    public int size;

    public Array() {
        this(true, 16);
    }

    public Array(int capacity) {
        this(true, capacity);
    }

    public Array(boolean z, int i) {
        this.ordered = z;
        this.items = (T[]) new Object[i];
    }

    public Array(boolean z, int i, Class<T> cls) {
        this.ordered = z;
        this.items = (T[]) ((Object[]) java.lang.reflect.Array.newInstance((Class<?>) cls, i));
    }

    public Array(Class<T> arrayType) {
        this(false, 16, arrayType);
    }

    public Array(Array array) {
        this(array.ordered, array.size, array.items.getClass().getComponentType());
        this.size = array.size;
        System.arraycopy(array.items, 0, this.items, 0, this.size);
    }

    public void add(T value) {
        T[] items = this.items;
        if (this.size == items.length) {
            items = resize(Math.max(8, (int) (this.size * 1.75f)));
        }
        int i = this.size;
        this.size = i + 1;
        items[i] = value;
    }

    public void addAll(Array array) {
        addAll(array, 0, array.size);
    }

    public void addAll(Array array, int offset, int length) {
        if (offset + length > array.size) {
            throw new IllegalArgumentException("offset + length must be <= size: " + offset + " + " + length + " <= " + array.size);
        }
        addAll(array.items, offset, length);
    }

    public void addAll(T[] array) {
        addAll(array, 0, array.length);
    }

    public void addAll(T[] array, int offset, int length) {
        T[] items = this.items;
        int sizeNeeded = (this.size + length) - offset;
        if (sizeNeeded >= items.length) {
            items = resize(Math.max(8, (int) (sizeNeeded * 1.75f)));
        }
        System.arraycopy(array, offset, items, this.size, length);
        this.size += length;
    }

    public T get(int index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException(String.valueOf(index));
        }
        return this.items[index];
    }

    public void set(int index, T value) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException(String.valueOf(index));
        }
        this.items[index] = value;
    }

    public void insert(int index, T value) {
        T[] items = this.items;
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

    public boolean contains(T value, boolean identity) {
        int i;
        Object[] items = this.items;
        int i2 = this.size - 1;
        if (identity || value == null) {
            do {
                i = i2;
                if (i >= 0) {
                    i2 = i - 1;
                }
            } while (items[i] != value);
            return true;
        }
        do {
            i = i2;
            if (i >= 0) {
                i2 = i - 1;
            }
        } while (!value.equals(items[i]));
        return true;
        return false;
    }

    public int indexOf(T value, boolean identity) {
        Object[] items = this.items;
        if (identity || value == null) {
            int n = this.size;
            for (int i = 0; i < n; i++) {
                if (items[i] == value) {
                    return i;
                }
            }
        } else {
            int n2 = this.size;
            for (int i2 = 0; i2 < n2; i2++) {
                if (value.equals(items[i2])) {
                    return i2;
                }
            }
        }
        return -1;
    }

    public boolean removeValue(T value, boolean identity) {
        Object[] items = this.items;
        if (identity || value == null) {
            int n = this.size;
            for (int i = 0; i < n; i++) {
                if (items[i] == value) {
                    removeIndex(i);
                    return true;
                }
            }
        } else {
            int n2 = this.size;
            for (int i2 = 0; i2 < n2; i2++) {
                if (value.equals(items[i2])) {
                    removeIndex(i2);
                    return true;
                }
            }
        }
        return false;
    }

    public T removeIndex(int index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException(String.valueOf(index));
        }
        T[] tArr = this.items;
        T value = tArr[index];
        this.size--;
        if (this.ordered) {
            System.arraycopy(tArr, index + 1, tArr, index, this.size - index);
        } else {
            tArr[index] = tArr[this.size];
        }
        tArr[this.size] = null;
        return value;
    }

    public T pop() {
        this.size--;
        T item = this.items[this.size];
        this.items[this.size] = null;
        return item;
    }

    public T peek() {
        return this.items[this.size - 1];
    }

    public void clear() {
        Object[] items = this.items;
        int n = this.size;
        for (int i = 0; i < n; i++) {
            items[i] = null;
        }
        this.size = 0;
    }

    public void shrink() {
        resize(this.size);
    }

    public T[] ensureCapacity(int additionalCapacity) {
        int sizeNeeded = this.size + additionalCapacity;
        if (sizeNeeded >= this.items.length) {
            resize(Math.max(8, sizeNeeded));
        }
        return this.items;
    }

    protected T[] resize(int i) {
        T[] tArr = this.items;
        T[] tArr2 = (T[]) ((Object[]) java.lang.reflect.Array.newInstance(tArr.getClass().getComponentType(), i));
        System.arraycopy(tArr, 0, tArr2, 0, Math.min(tArr.length, tArr2.length));
        this.items = tArr2;
        return tArr2;
    }

    public void sort() {
        Sort.instance().sort(this.items, 0, this.size);
    }

    public void sort(Comparator<T> comparator) {
        Sort.instance().sort(this.items, comparator, 0, this.size);
    }

    public void reverse() {
        int lastIndex = this.size - 1;
        int n = this.size / 2;
        for (int i = 0; i < n; i++) {
            int ii = lastIndex - i;
            T temp = this.items[i];
            this.items[i] = this.items[ii];
            this.items[ii] = temp;
        }
    }

    public void shuffle() {
        for (int i = this.size - 1; i >= 0; i--) {
            int ii = MathUtils.random(i);
            T temp = this.items[i];
            this.items[i] = this.items[ii];
            this.items[ii] = temp;
        }
    }

    @Override // java.lang.Iterable
    public Iterator<T> iterator() {
        if (this.iterator == null) {
            this.iterator = new ArrayIterator(this);
        }
        this.iterator.index = 0;
        return this.iterator;
    }

    public T[] toArray() {
        return (T[]) toArray(this.items.getClass().getComponentType());
    }

    public <V> V[] toArray(Class<V> cls) {
        V[] vArr = (V[]) ((Object[]) java.lang.reflect.Array.newInstance((Class<?>) cls, this.size));
        System.arraycopy(this.items, 0, vArr, 0, this.size);
        return vArr;
    }

    public String toString() {
        if (this.size == 0) {
            return "[]";
        }
        Object[] items = this.items;
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

    public static class ArrayIterator<T> implements Iterator<T> {
        private final Array<T> array;
        int index;

        public ArrayIterator(Array<T> array) {
            this.array = array;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.index < this.array.size;
        }

        @Override // java.util.Iterator
        public T next() {
            if (this.index >= this.array.size) {
                throw new NoSuchElementException(String.valueOf(this.index));
            }
            T[] tArr = this.array.items;
            int i = this.index;
            this.index = i + 1;
            return tArr[i];
        }

        @Override // java.util.Iterator
        public void remove() {
            this.index--;
            this.array.removeIndex(this.index);
        }

        public void reset() {
            this.index = 0;
        }
    }

    public static class ArrayIterable<T> implements Iterable<T> {
        private ArrayIterator<T> iterator;

        public ArrayIterable(Array<T> array) {
            this.iterator = new ArrayIterator<>(array);
        }

        @Override // java.lang.Iterable
        public Iterator<T> iterator() {
            this.iterator.reset();
            return this.iterator;
        }
    }
}
