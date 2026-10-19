package org.codehaus.jackson.map.util;

import java.lang.reflect.Array;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class ObjectBuffer {
    static final int INITIAL_CHUNK_SIZE = 12;
    static final int MAX_CHUNK_SIZE = 262144;
    static final int SMALL_CHUNK_SIZE = 16384;
    private Node _bufferHead;
    private Node _bufferTail;
    private int _bufferedEntryCount;
    private Object[] _freeBuffer;

    public Object[] resetAndStart() {
        _reset();
        return this._freeBuffer == null ? new Object[12] : this._freeBuffer;
    }

    public Object[] appendCompletedChunk(Object[] fullChunk) {
        int len;
        Node next = new Node(fullChunk);
        if (this._bufferHead == null) {
            this._bufferTail = next;
            this._bufferHead = next;
        } else {
            this._bufferTail.linkNext(next);
            this._bufferTail = next;
        }
        int len2 = fullChunk.length;
        this._bufferedEntryCount += len2;
        if (len2 < 16384) {
            len = len2 + len2;
        } else {
            len = len2 + (len2 >> 2);
        }
        return new Object[len];
    }

    public Object[] completeAndClearBuffer(Object[] lastChunk, int lastChunkEntries) {
        int totalSize = lastChunkEntries + this._bufferedEntryCount;
        Object[] result = new Object[totalSize];
        _copyTo(result, totalSize, lastChunk, lastChunkEntries);
        return result;
    }

    public <T> T[] completeAndClearBuffer(Object[] objArr, int i, Class<T> cls) {
        int i2 = i + this._bufferedEntryCount;
        T[] tArr = (T[]) ((Object[]) Array.newInstance((Class<?>) cls, i2));
        _copyTo(tArr, i2, objArr, i);
        _reset();
        return tArr;
    }

    public void completeAndClearBuffer(Object[] lastChunk, int lastChunkEntries, List<Object> resultList) {
        for (Node n = this._bufferHead; n != null; n = n.next()) {
            Object[] curr = n.getData();
            for (Object obj : curr) {
                resultList.add(obj);
            }
        }
        for (int i = 0; i < lastChunkEntries; i++) {
            resultList.add(lastChunk[i]);
        }
    }

    public int initialCapacity() {
        if (this._freeBuffer == null) {
            return 0;
        }
        return this._freeBuffer.length;
    }

    public int bufferedSize() {
        return this._bufferedEntryCount;
    }

    protected void _reset() {
        if (this._bufferTail != null) {
            this._freeBuffer = this._bufferTail.getData();
        }
        this._bufferTail = null;
        this._bufferHead = null;
        this._bufferedEntryCount = 0;
    }

    protected final void _copyTo(Object resultArray, int totalSize, Object[] lastChunk, int lastChunkEntries) {
        int ptr = 0;
        for (Node n = this._bufferHead; n != null; n = n.next()) {
            Object[] curr = n.getData();
            int len = curr.length;
            System.arraycopy(curr, 0, resultArray, ptr, len);
            ptr += len;
        }
        System.arraycopy(lastChunk, 0, resultArray, ptr, lastChunkEntries);
        int ptr2 = ptr + lastChunkEntries;
        if (ptr2 != totalSize) {
            throw new IllegalStateException("Should have gotten " + totalSize + " entries, got " + ptr2);
        }
    }

    static final class Node {
        final Object[] _data;
        Node _next;

        public Node(Object[] data) {
            this._data = data;
        }

        public Object[] getData() {
            return this._data;
        }

        public Node next() {
            return this._next;
        }

        public void linkNext(Node next) {
            if (this._next != null) {
                throw new IllegalStateException();
            }
            this._next = next;
        }
    }
}
