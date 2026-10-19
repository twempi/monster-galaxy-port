package com.badlogic.gdx.graphics.glutils;

import com.badlogic.gdx.utils.BufferUtils;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

/* JADX INFO: loaded from: classes.dex */
public class IndexArray implements IndexData {
    static final IntBuffer tmpHandle = BufferUtils.newIntBuffer(1);
    ShortBuffer buffer;
    ByteBuffer byteBuffer;

    public IndexArray(int maxIndices) {
        this.byteBuffer = BufferUtils.newUnsafeByteBuffer(maxIndices * 2);
        this.buffer = this.byteBuffer.asShortBuffer();
        this.buffer.flip();
        this.byteBuffer.flip();
    }

    @Override // com.badlogic.gdx.graphics.glutils.IndexData
    public int getNumIndices() {
        return this.buffer.limit();
    }

    @Override // com.badlogic.gdx.graphics.glutils.IndexData
    public int getNumMaxIndices() {
        return this.buffer.capacity();
    }

    @Override // com.badlogic.gdx.graphics.glutils.IndexData
    public void setIndices(short[] indices, int offset, int count) {
        this.buffer.clear();
        this.buffer.put(indices, offset, count);
        this.buffer.flip();
        this.byteBuffer.position(0);
        this.byteBuffer.limit(count << 1);
    }

    @Override // com.badlogic.gdx.graphics.glutils.IndexData
    public ShortBuffer getBuffer() {
        return this.buffer;
    }

    @Override // com.badlogic.gdx.graphics.glutils.IndexData
    public void bind() {
    }

    @Override // com.badlogic.gdx.graphics.glutils.IndexData
    public void unbind() {
    }

    @Override // com.badlogic.gdx.graphics.glutils.IndexData
    public void invalidate() {
    }

    @Override // com.badlogic.gdx.graphics.glutils.IndexData, com.badlogic.gdx.utils.Disposable
    public void dispose() {
        BufferUtils.disposeUnsafeByteBuffer(this.byteBuffer);
    }
}
