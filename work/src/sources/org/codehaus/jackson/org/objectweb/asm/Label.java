package org.codehaus.jackson.org.objectweb.asm;

/* JADX INFO: loaded from: classes.dex */
public class Label {
    int a;
    int b;
    int c;
    private int d;
    private int[] e;
    int f;
    int g;
    Frame h;
    Label i;
    public Object info;
    Edge j;
    Label k;

    private void a(int i, int i2) {
        if (this.e == null) {
            this.e = new int[6];
        }
        if (this.d >= this.e.length) {
            int[] iArr = new int[this.e.length + 6];
            System.arraycopy(this.e, 0, iArr, 0, this.e.length);
            this.e = iArr;
        }
        int[] iArr2 = this.e;
        int i3 = this.d;
        this.d = i3 + 1;
        iArr2[i3] = i;
        int[] iArr3 = this.e;
        int i4 = this.d;
        this.d = i4 + 1;
        iArr3[i4] = i2;
    }

    Label a() {
        return this.h == null ? this : this.h.b;
    }

    void a(long j, int i) {
        if ((this.a & 1024) == 0) {
            this.a |= 1024;
            this.e = new int[((i - 1) / 32) + 1];
        }
        int[] iArr = this.e;
        int i2 = (int) (j >>> 32);
        iArr[i2] = iArr[i2] | ((int) j);
    }

    void a(MethodWriter methodWriter, ByteVector byteVector, int i, boolean z) {
        if ((this.a & 2) != 0) {
            if (z) {
                byteVector.putInt(this.c - i);
                return;
            } else {
                byteVector.putShort(this.c - i);
                return;
            }
        }
        if (z) {
            a((-1) - i, byteVector.b);
            byteVector.putInt(-1);
        } else {
            a(i, byteVector.b);
            byteVector.putShort(-1);
        }
    }

    boolean a(long j) {
        return ((this.a & 1024) == 0 || (this.e[(int) (j >>> 32)] & ((int) j)) == 0) ? false : true;
    }

    boolean a(Label label) {
        if ((this.a & 1024) == 0 || (label.a & 1024) == 0) {
            return false;
        }
        for (int i = 0; i < this.e.length; i++) {
            if ((this.e[i] & label.e[i]) != 0) {
                return true;
            }
        }
        return false;
    }

    boolean a(MethodWriter methodWriter, int i, byte[] bArr) {
        int i2 = 0;
        this.a |= 2;
        this.c = i;
        boolean z = false;
        while (i2 < this.d) {
            int i3 = i2 + 1;
            int i4 = this.e[i2];
            i2 = i3 + 1;
            int i5 = this.e[i3];
            if (i4 >= 0) {
                int i6 = i - i4;
                if (i6 < -32768 || i6 > 32767) {
                    int i7 = bArr[i5 - 1] & 255;
                    if (i7 <= 168) {
                        bArr[i5 - 1] = (byte) (i7 + 49);
                    } else {
                        bArr[i5 - 1] = (byte) (i7 + 20);
                    }
                    z = true;
                }
                bArr[i5] = (byte) (i6 >>> 8);
                bArr[i5 + 1] = (byte) i6;
            } else {
                int i8 = i4 + i + 1;
                int i9 = i5 + 1;
                bArr[i5] = (byte) (i8 >>> 24);
                int i10 = i9 + 1;
                bArr[i9] = (byte) (i8 >>> 16);
                bArr[i10] = (byte) (i8 >>> 8);
                bArr[i10 + 1] = (byte) i8;
            }
        }
        return z;
    }

    /* JADX WARN: Code duplicated, block: B:15:0x003f  */
    void b(Label label, long j, int i) {
        Label label2;
        Edge edge;
        while (this != null) {
            Label label3 = this.k;
            this.k = null;
            if (label != null) {
                if ((this.a & 2048) != 0) {
                    this = label3;
                } else {
                    this.a |= 2048;
                    if ((this.a & 256) != 0 && !this.a(label)) {
                        Edge edge2 = new Edge();
                        edge2.a = this.f;
                        edge2.b = label.j.b;
                        edge2.c = this.j;
                        this.j = edge2;
                    }
                    label2 = label3;
                    for (edge = this.j; edge != null; edge = edge.c) {
                        if (((this.a & 128) != 0 || edge != this.j.c) && edge.b.k == null) {
                            edge.b.k = label2;
                            label2 = edge.b;
                        }
                    }
                    this = label2;
                }
            } else if (this.a(j)) {
                this = label3;
            } else {
                this.a(j, i);
                label2 = label3;
                while (edge != null) {
                    if ((this.a & 128) != 0) {
                        edge.b.k = label2;
                        label2 = edge.b;
                    } else {
                        edge.b.k = label2;
                        label2 = edge.b;
                    }
                }
                this = label2;
            }
        }
    }

    public int getOffset() {
        if ((this.a & 2) == 0) {
            throw new IllegalStateException("Label offset position has not been resolved yet");
        }
        return this.c;
    }

    public String toString() {
        return new StringBuffer().append("L").append(System.identityHashCode(this)).toString();
    }
}
