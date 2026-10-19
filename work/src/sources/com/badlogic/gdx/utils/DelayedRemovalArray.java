package com.badlogic.gdx.utils;

import java.util.Comparator;

/* JADX INFO: loaded from: classes.dex */
public class DelayedRemovalArray<T> extends Array<T> {
    private boolean iterating;
    private IntArray remove;

    public DelayedRemovalArray() {
        this.remove = new IntArray(0);
    }

    public DelayedRemovalArray(Array array) {
        super(array);
        this.remove = new IntArray(0);
    }

    public DelayedRemovalArray(boolean ordered, int capacity, Class<T> arrayType) {
        super(ordered, capacity, arrayType);
        this.remove = new IntArray(0);
    }

    public DelayedRemovalArray(boolean ordered, int capacity) {
        super(ordered, capacity);
        this.remove = new IntArray(0);
    }

    public DelayedRemovalArray(boolean ordered, T[] array) {
        super(ordered, array);
        this.remove = new IntArray(0);
    }

    public DelayedRemovalArray(Class<T> arrayType) {
        super(arrayType);
        this.remove = new IntArray(0);
    }

    public DelayedRemovalArray(int capacity) {
        super(capacity);
        this.remove = new IntArray(0);
    }

    public DelayedRemovalArray(T[] array) {
        super(array);
        this.remove = new IntArray(0);
    }

    public void begin() {
        this.iterating = true;
    }

    public void end() {
        this.iterating = false;
        int n = this.remove.size;
        for (int i = 0; i < n; i++) {
            removeIndex(this.remove.pop());
        }
    }

    private void remove(int index) {
        int n = this.remove.size;
        for (int i = 0; i < n; i++) {
            int removeIndex = this.remove.get(i);
            if (index != removeIndex) {
                if (index < removeIndex) {
                    this.remove.insert(i, index);
                    return;
                }
            } else {
                return;
            }
        }
        this.remove.add(index);
    }

    @Override // com.badlogic.gdx.utils.Array
    public boolean removeValue(T value, boolean identity) {
        if (this.iterating) {
            int index = indexOf(value, identity);
            if (index == -1) {
                return false;
            }
            remove(index);
            return true;
        }
        return super.removeValue(value, identity);
    }

    @Override // com.badlogic.gdx.utils.Array
    public T removeIndex(int i) {
        if (!this.iterating) {
            return (T) super.removeIndex(i);
        }
        remove(i);
        return get(i);
    }

    @Override // com.badlogic.gdx.utils.Array
    public void set(int index, T value) {
        if (this.iterating) {
            throw new IllegalStateException("Invalid between begin/end.");
        }
        super.set(index, value);
    }

    @Override // com.badlogic.gdx.utils.Array
    public void insert(int index, T value) {
        if (this.iterating) {
            throw new IllegalStateException("Invalid between begin/end.");
        }
        super.insert(index, value);
    }

    @Override // com.badlogic.gdx.utils.Array
    public void swap(int first, int second) {
        if (this.iterating) {
            throw new IllegalStateException("Invalid between begin/end.");
        }
        super.swap(first, second);
    }

    @Override // com.badlogic.gdx.utils.Array
    public T pop() {
        if (this.iterating) {
            throw new IllegalStateException("Invalid between begin/end.");
        }
        return (T) super.pop();
    }

    @Override // com.badlogic.gdx.utils.Array
    public void clear() {
        if (this.iterating) {
            throw new IllegalStateException("Invalid between begin/end.");
        }
        super.clear();
    }

    @Override // com.badlogic.gdx.utils.Array
    public void sort() {
        if (this.iterating) {
            throw new IllegalStateException("Invalid between begin/end.");
        }
        super.sort();
    }

    @Override // com.badlogic.gdx.utils.Array
    public void sort(Comparator<T> comparator) {
        if (this.iterating) {
            throw new IllegalStateException("Invalid between begin/end.");
        }
        super.sort(comparator);
    }

    @Override // com.badlogic.gdx.utils.Array
    public void reverse() {
        if (this.iterating) {
            throw new IllegalStateException("Invalid between begin/end.");
        }
        super.reverse();
    }

    @Override // com.badlogic.gdx.utils.Array
    public void shuffle() {
        if (this.iterating) {
            throw new IllegalStateException("Invalid between begin/end.");
        }
        super.shuffle();
    }

    @Override // com.badlogic.gdx.utils.Array
    public void truncate(int newSize) {
        if (this.iterating) {
            throw new IllegalStateException("Invalid between begin/end.");
        }
        super.truncate(newSize);
    }
}
