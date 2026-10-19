package org.codehaus.jackson.io;

import java.io.Writer;
import org.codehaus.jackson.util.BufferRecycler;
import org.codehaus.jackson.util.TextBuffer;

/* JADX INFO: loaded from: classes.dex */
public final class SegmentedStringWriter extends Writer {
    protected final TextBuffer _buffer;

    public SegmentedStringWriter(BufferRecycler br) {
        this._buffer = new TextBuffer(br);
    }

    @Override // java.io.Writer, java.lang.Appendable
    public Writer append(char c) {
        write(c);
        return this;
    }

    @Override // java.io.Writer, java.lang.Appendable
    public Writer append(CharSequence csq) {
        String str = csq.toString();
        this._buffer.append(str, 0, str.length());
        return this;
    }

    @Override // java.io.Writer, java.lang.Appendable
    public Writer append(CharSequence csq, int start, int end) {
        String str = csq.subSequence(start, end).toString();
        this._buffer.append(str, 0, str.length());
        return this;
    }

    @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
    }

    @Override // java.io.Writer, java.io.Flushable
    public void flush() {
    }

    @Override // java.io.Writer
    public void write(char[] cbuf) {
        this._buffer.append(cbuf, 0, cbuf.length);
    }

    @Override // java.io.Writer
    public void write(char[] cbuf, int off, int len) {
        this._buffer.append(cbuf, off, len);
    }

    @Override // java.io.Writer
    public void write(int c) {
        this._buffer.append((char) c);
    }

    @Override // java.io.Writer
    public void write(String str) {
        this._buffer.append(str, 0, str.length());
    }

    @Override // java.io.Writer
    public void write(String str, int off, int len) {
        this._buffer.append(str, 0, str.length());
    }

    public String getAndClear() {
        String result = this._buffer.contentsAsString();
        this._buffer.releaseBuffers();
        return result;
    }
}
