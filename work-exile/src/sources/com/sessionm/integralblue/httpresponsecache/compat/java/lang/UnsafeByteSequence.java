package com.sessionm.integralblue.httpresponsecache.compat.java.lang;

import com.sessionm.integralblue.httpresponsecache.compat.Strings;
import java.nio.charset.Charset;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class UnsafeByteSequence {
    private byte[] bytes;
    private int count;

    public UnsafeByteSequence(int i) {
        this.bytes = new byte[i];
    }

    public int size() {
        return this.count;
    }

    public void rewind() {
        this.count = 0;
    }

    public void write(byte[] bArr, int i, int i2) {
        if (this.count + i2 >= this.bytes.length) {
            byte[] bArr2 = new byte[(this.count + i2) * 2];
            System.arraycopy(this.bytes, 0, bArr2, 0, this.count);
            this.bytes = bArr2;
        }
        System.arraycopy(bArr, i, this.bytes, this.count, i2);
        this.count += i2;
    }

    public void write(int i) {
        if (this.count == this.bytes.length) {
            byte[] bArr = new byte[this.count * 2];
            System.arraycopy(this.bytes, 0, bArr, 0, this.count);
            this.bytes = bArr;
        }
        byte[] bArr2 = this.bytes;
        int i2 = this.count;
        this.count = i2 + 1;
        bArr2[i2] = (byte) i;
    }

    public byte[] toByteArray() {
        if (this.count == this.bytes.length) {
            return this.bytes;
        }
        byte[] bArr = new byte[this.count];
        System.arraycopy(this.bytes, 0, bArr, 0, this.count);
        return bArr;
    }

    public String toString(Charset charset) {
        return Strings.construct(this.bytes, 0, this.count, charset);
    }
}
