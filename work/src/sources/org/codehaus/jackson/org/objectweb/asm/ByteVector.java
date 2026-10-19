package org.codehaus.jackson.org.objectweb.asm;

/* JADX INFO: loaded from: classes.dex */
public class ByteVector {
    byte[] a;
    int b;

    public ByteVector() {
        this.a = new byte[64];
    }

    public ByteVector(int i) {
        this.a = new byte[i];
    }

    private void a(int i) {
        int length = this.a.length * 2;
        int i2 = this.b + i;
        if (length <= i2) {
            length = i2;
        }
        byte[] bArr = new byte[length];
        System.arraycopy(this.a, 0, bArr, 0, this.b);
        this.a = bArr;
    }

    ByteVector a(int i, int i2) {
        int i3 = this.b;
        if (i3 + 2 > this.a.length) {
            a(2);
        }
        byte[] bArr = this.a;
        int i4 = i3 + 1;
        bArr[i3] = (byte) i;
        bArr[i4] = (byte) i2;
        this.b = i4 + 1;
        return this;
    }

    ByteVector b(int i, int i2) {
        int i3 = this.b;
        if (i3 + 3 > this.a.length) {
            a(3);
        }
        byte[] bArr = this.a;
        int i4 = i3 + 1;
        bArr[i3] = (byte) i;
        int i5 = i4 + 1;
        bArr[i4] = (byte) (i2 >>> 8);
        bArr[i5] = (byte) i2;
        this.b = i5 + 1;
        return this;
    }

    public ByteVector putByte(int i) {
        int i2 = this.b;
        if (i2 + 1 > this.a.length) {
            a(1);
        }
        this.a[i2] = (byte) i;
        this.b = i2 + 1;
        return this;
    }

    public ByteVector putByteArray(byte[] bArr, int i, int i2) {
        if (this.b + i2 > this.a.length) {
            a(i2);
        }
        if (bArr != null) {
            System.arraycopy(bArr, i, this.a, this.b, i2);
        }
        this.b += i2;
        return this;
    }

    public ByteVector putInt(int i) {
        int i2 = this.b;
        if (i2 + 4 > this.a.length) {
            a(4);
        }
        byte[] bArr = this.a;
        int i3 = i2 + 1;
        bArr[i2] = (byte) (i >>> 24);
        int i4 = i3 + 1;
        bArr[i3] = (byte) (i >>> 16);
        int i5 = i4 + 1;
        bArr[i4] = (byte) (i >>> 8);
        bArr[i5] = (byte) i;
        this.b = i5 + 1;
        return this;
    }

    public ByteVector putLong(long j) {
        int i = this.b;
        if (i + 8 > this.a.length) {
            a(8);
        }
        byte[] bArr = this.a;
        int i2 = (int) (j >>> 32);
        int i3 = i + 1;
        bArr[i] = (byte) (i2 >>> 24);
        int i4 = i3 + 1;
        bArr[i3] = (byte) (i2 >>> 16);
        int i5 = i4 + 1;
        bArr[i4] = (byte) (i2 >>> 8);
        int i6 = i5 + 1;
        bArr[i5] = (byte) i2;
        int i7 = (int) j;
        int i8 = i6 + 1;
        bArr[i6] = (byte) (i7 >>> 24);
        int i9 = i8 + 1;
        bArr[i8] = (byte) (i7 >>> 16);
        int i10 = i9 + 1;
        bArr[i9] = (byte) (i7 >>> 8);
        bArr[i10] = (byte) i7;
        this.b = i10 + 1;
        return this;
    }

    public ByteVector putShort(int i) {
        int i2 = this.b;
        if (i2 + 2 > this.a.length) {
            a(2);
        }
        byte[] bArr = this.a;
        int i3 = i2 + 1;
        bArr[i2] = (byte) (i >>> 8);
        bArr[i3] = (byte) i;
        this.b = i3 + 1;
        return this;
    }

    public ByteVector putUTF8(String str) {
        int i;
        byte[] bArr;
        int i2;
        int length = str.length();
        int i3 = this.b;
        if (i3 + 2 + length > this.a.length) {
            a(length + 2);
        }
        byte[] bArr2 = this.a;
        int i4 = i3 + 1;
        bArr2[i3] = (byte) (length >>> 8);
        int i5 = i4 + 1;
        bArr2[i4] = (byte) length;
        int i6 = 0;
        while (i6 < length) {
            char cCharAt = str.charAt(i6);
            if (cCharAt < 1 || cCharAt > 127) {
                int i7 = i6;
                for (int i8 = i6; i8 < length; i8++) {
                    char cCharAt2 = str.charAt(i8);
                    i7 = (cCharAt2 < 1 || cCharAt2 > 127) ? cCharAt2 > 2047 ? i7 + 3 : i7 + 2 : i7 + 1;
                }
                bArr2[this.b] = (byte) (i7 >>> 8);
                bArr2[this.b + 1] = (byte) i7;
                if (this.b + 2 + i7 > bArr2.length) {
                    this.b = i5;
                    a(i7 + 2);
                    bArr = this.a;
                } else {
                    bArr = bArr2;
                }
                while (i6 < length) {
                    char cCharAt3 = str.charAt(i6);
                    if (cCharAt3 >= 1 && cCharAt3 <= 127) {
                        i2 = i5 + 1;
                        bArr[i5] = (byte) cCharAt3;
                    } else if (cCharAt3 > 2047) {
                        int i9 = i5 + 1;
                        bArr[i5] = (byte) (((cCharAt3 >> '\f') & 15) | 224);
                        int i10 = i9 + 1;
                        bArr[i9] = (byte) (((cCharAt3 >> 6) & 63) | 128);
                        i2 = i10 + 1;
                        bArr[i10] = (byte) ((cCharAt3 & '?') | 128);
                    } else {
                        int i11 = i5 + 1;
                        bArr[i5] = (byte) (((cCharAt3 >> 6) & 31) | 192);
                        i2 = i11 + 1;
                        bArr[i11] = (byte) ((cCharAt3 & '?') | 128);
                    }
                    i6++;
                    i5 = i2;
                }
                i = i5;
                this.b = i;
                return this;
            }
            bArr2[i5] = (byte) cCharAt;
            i6++;
            i5++;
        }
        i = i5;
        this.b = i;
        return this;
    }
}
