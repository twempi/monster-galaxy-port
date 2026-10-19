package org.codehaus.jackson.org.objectweb.asm;

import com.badlogic.gdx.Input;
import com.flurry.android.CallbackEvent;
import org.codehaus.jackson.smile.SmileConstants;
import twitter4j.internal.http.HttpResponseCode;

/* JADX INFO: loaded from: classes.dex */
class MethodWriter implements MethodVisitor {
    private int A;
    private Handler B;
    private Handler C;
    private int D;
    private ByteVector E;
    private int F;
    private ByteVector G;
    private int H;
    private ByteVector I;
    private Attribute J;
    private boolean K;
    private int L;
    private final int M;
    private Label N;
    private Label O;
    private Label P;
    private int Q;
    private int R;
    private int S;
    MethodWriter a;
    final ClassWriter b;
    private int c;
    private final int d;
    private final int e;
    private final String f;
    String g;
    int h;
    int i;
    int j;
    int[] k;
    private ByteVector l;
    private AnnotationWriter m;
    private AnnotationWriter n;
    private AnnotationWriter[] o;
    private AnnotationWriter[] p;
    private Attribute q;
    private ByteVector r = new ByteVector();
    private int s;
    private int t;
    private int u;
    private ByteVector v;
    private int w;
    private int[] x;
    private int y;
    private int[] z;

    MethodWriter(ClassWriter classWriter, int i, String str, String str2, String str3, String[] strArr, boolean z, boolean z2) {
        if (classWriter.A == null) {
            classWriter.A = this;
        } else {
            classWriter.B.a = this;
        }
        classWriter.B = this;
        this.b = classWriter;
        this.c = i;
        this.d = classWriter.newUTF8(str);
        this.e = classWriter.newUTF8(str2);
        this.f = str2;
        this.g = str3;
        if (strArr != null && strArr.length > 0) {
            this.j = strArr.length;
            this.k = new int[this.j];
            for (int i2 = 0; i2 < this.j; i2++) {
                this.k[i2] = classWriter.newClass(strArr[i2]);
            }
        }
        this.M = z2 ? 0 : z ? 1 : 2;
        if (z || z2) {
            if (z2 && "<init>".equals(str)) {
                this.c |= 262144;
            }
            int argumentsAndReturnSizes = Type.getArgumentsAndReturnSizes(this.f) >> 2;
            this.t = (i & 8) != 0 ? argumentsAndReturnSizes - 1 : argumentsAndReturnSizes;
            this.N = new Label();
            this.N.a |= 8;
            visitLabel(this.N);
        }
    }

    static int a(byte[] bArr, int i) {
        return ((bArr[i] & 255) << 24) | ((bArr[i + 1] & 255) << 16) | ((bArr[i + 2] & 255) << 8) | (bArr[i + 3] & 255);
    }

    static int a(int[] iArr, int[] iArr2, int i, int i2) {
        int i3 = i2 - i;
        for (int i4 = 0; i4 < iArr.length; i4++) {
            if (i < iArr[i4] && iArr[i4] <= i2) {
                i3 += iArr2[i4];
            } else if (i2 < iArr[i4] && iArr[i4] <= i) {
                i3 -= iArr2[i4];
            }
        }
        return i3;
    }

    private void a(int i, int i2) {
        while (i < i2) {
            int i3 = this.z[i];
            int i4 = (-268435456) & i3;
            if (i4 == 0) {
                int i5 = i3 & 1048575;
                switch (i3 & 267386880) {
                    case 24117248:
                        this.v.putByte(7).putShort(this.b.newClass(this.b.E[i5].g));
                        break;
                    case 25165824:
                        this.v.putByte(8).putShort(this.b.E[i5].c);
                        break;
                    default:
                        this.v.putByte(i5);
                        break;
                }
            } else {
                StringBuffer stringBuffer = new StringBuffer();
                int i6 = i4 >> 28;
                while (true) {
                    int i7 = i6 - 1;
                    if (i6 > 0) {
                        stringBuffer.append('[');
                        i6 = i7;
                    } else {
                        if ((i3 & 267386880) != 24117248) {
                            switch (i3 & 15) {
                                case 1:
                                    stringBuffer.append('I');
                                    break;
                                case 2:
                                    stringBuffer.append('F');
                                    break;
                                case 3:
                                    stringBuffer.append('D');
                                    break;
                                case 4:
                                case 5:
                                case 6:
                                case 7:
                                case 8:
                                default:
                                    stringBuffer.append('J');
                                    break;
                                case 9:
                                    stringBuffer.append('Z');
                                    break;
                                case 10:
                                    stringBuffer.append('B');
                                    break;
                                case 11:
                                    stringBuffer.append('C');
                                    break;
                                case 12:
                                    stringBuffer.append('S');
                                    break;
                            }
                        } else {
                            stringBuffer.append('L');
                            stringBuffer.append(this.b.E[i3 & 1048575].g);
                            stringBuffer.append(';');
                        }
                        this.v.putByte(7).putShort(this.b.newClass(stringBuffer.toString()));
                    }
                }
            }
            i++;
        }
    }

    private void a(int i, int i2, int i3) {
        int i4 = i2 + 3 + i3;
        if (this.z == null || this.z.length < i4) {
            this.z = new int[i4];
        }
        this.z[0] = i;
        this.z[1] = i2;
        this.z[2] = i3;
        this.y = 3;
    }

    private void a(int i, Label label) {
        Edge edge = new Edge();
        edge.a = i;
        edge.b = label;
        edge.c = this.P.j;
        this.P.j = edge;
    }

    private void a(Object obj) {
        if (obj instanceof String) {
            this.v.putByte(7).putShort(this.b.newClass((String) obj));
        } else if (obj instanceof Integer) {
            this.v.putByte(((Integer) obj).intValue());
        } else {
            this.v.putByte(8).putShort(((Label) obj).c);
        }
    }

    private void a(Label label, Label[] labelArr) {
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(Opcodes.LOOKUPSWITCH, 0, (ClassWriter) null, (Item) null);
                a(0, label);
                label.a().a |= 16;
                for (int i = 0; i < labelArr.length; i++) {
                    a(0, labelArr[i]);
                    labelArr[i].a().a |= 16;
                }
            } else {
                this.Q--;
                a(this.Q, label);
                for (Label label2 : labelArr) {
                    a(this.Q, label2);
                }
            }
            e();
        }
    }

    static void a(byte[] bArr, int i, int i2) {
        bArr[i] = (byte) (i2 >>> 8);
        bArr[i + 1] = (byte) i2;
    }

    static void a(int[] iArr, int[] iArr2, Label label) {
        if ((label.a & 4) == 0) {
            label.c = a(iArr, iArr2, 0, label.c);
            label.a |= 4;
        }
    }

    static short b(byte[] bArr, int i) {
        return (short) (((bArr[i] & 255) << 8) | (bArr[i + 1] & 255));
    }

    private void b() {
        if (this.x != null) {
            if (this.v == null) {
                this.v = new ByteVector();
            }
            c();
            this.u++;
        }
        this.x = this.z;
        this.z = null;
    }

    private void b(Frame frame) {
        int i = 0;
        int[] iArr = frame.c;
        int[] iArr2 = frame.d;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        while (i2 < iArr.length) {
            int i5 = iArr[i2];
            if (i5 == 16777216) {
                i4++;
            } else {
                i3 += i4 + 1;
                i4 = 0;
            }
            if (i5 == 16777220 || i5 == 16777219) {
                i2++;
            }
            i2++;
        }
        int i6 = 0;
        int i7 = 0;
        while (i6 < iArr2.length) {
            int i8 = iArr2[i6];
            i7++;
            if (i8 == 16777220 || i8 == 16777219) {
                i6++;
            }
            i6++;
        }
        a(frame.b.c, i3, i7);
        int i9 = 0;
        while (i3 > 0) {
            int i10 = iArr[i9];
            int[] iArr3 = this.z;
            int i11 = this.y;
            this.y = i11 + 1;
            iArr3[i11] = i10;
            if (i10 == 16777220 || i10 == 16777219) {
                i9++;
            }
            i9++;
            i3--;
        }
        while (i < iArr2.length) {
            int i12 = iArr2[i];
            int[] iArr4 = this.z;
            int i13 = this.y;
            this.y = i13 + 1;
            iArr4[i13] = i12;
            if (i12 == 16777220 || i12 == 16777219) {
                i++;
            }
            i++;
        }
        b();
    }

    static int c(byte[] bArr, int i) {
        return ((bArr[i] & 255) << 8) | (bArr[i + 1] & 255);
    }

    private void c() {
        int i;
        char c;
        int i2;
        int i3 = this.z[1];
        int i4 = this.z[2];
        if ((this.b.b & 65535) < 50) {
            this.v.putShort(this.z[0]).putShort(i3);
            a(3, i3 + 3);
            this.v.putShort(i4);
            a(i3 + 3, i3 + 3 + i4);
        }
        int i5 = this.x[1];
        int i6 = this.u == 0 ? this.z[0] : (this.z[0] - this.x[0]) - 1;
        if (i4 == 0) {
            i = i3 - i5;
            switch (i) {
                case -3:
                case -2:
                case -1:
                    c = 248;
                    i5 = i3;
                    break;
                case 0:
                    c = i6 < 64 ? (char) 0 : (char) 251;
                    break;
                case 1:
                case 2:
                case 3:
                    c = 252;
                    break;
                default:
                    c = 255;
                    break;
            }
            i2 = i5;
        } else if (i3 == i5 && i4 == 1) {
            c = i6 >= 63 ? (char) 247 : '@';
            i = 0;
            i2 = i5;
        } else {
            i = 0;
            c = 255;
            i2 = i5;
        }
        if (c != 255) {
            int i7 = 3;
            for (int i8 = 0; i8 < i2; i8++) {
                if (this.z[i7] != this.x[i7]) {
                    c = 255;
                } else {
                    i7++;
                }
            }
        }
        switch (c) {
            case 0:
                this.v.putByte(i6);
                break;
            case '@':
                this.v.putByte(i6 + 64);
                a(i3 + 3, i3 + 4);
                break;
            case Input.Keys.F4 /* 247 */:
                this.v.putByte(Input.Keys.F4).putShort(i6);
                a(i3 + 3, i3 + 4);
                break;
            case Input.Keys.F5 /* 248 */:
                this.v.putByte(i + Input.Keys.F8).putShort(i6);
                break;
            case Input.Keys.F8 /* 251 */:
                this.v.putByte(Input.Keys.F8).putShort(i6);
                break;
            case 252:
                this.v.putByte(i + Input.Keys.F8).putShort(i6);
                a(i2 + 3, i3 + 3);
                break;
            default:
                this.v.putByte(255).putShort(i6).putShort(i3);
                a(3, i3 + 3);
                this.v.putShort(i4);
                a(i3 + 3, i3 + 3 + i4);
                break;
        }
    }

    private void d() {
        int i;
        int iB;
        int i2;
        int iB2;
        int i3;
        byte[] bArr = this.r.a;
        int[] iArr = new int[0];
        int[] iArr2 = new int[0];
        boolean[] zArr = new boolean[this.r.b];
        int i4 = 3;
        while (true) {
            if (i4 == 3) {
                i4 = 2;
            }
            int i5 = i4;
            int iA = 0;
            while (iA < bArr.length) {
                int i6 = bArr[iA] & 255;
                int i7 = 0;
                switch (ClassWriter.a[i6]) {
                    case 0:
                    case 4:
                        iA++;
                        break;
                    case 1:
                    case 3:
                    case 10:
                        iA += 2;
                        break;
                    case 2:
                    case 5:
                    case 6:
                    case 11:
                    case 12:
                        iA += 3;
                        break;
                    case 7:
                        iA += 5;
                        break;
                    case 8:
                        if (i6 > 201) {
                            i6 = i6 < 218 ? i6 - 49 : i6 - 20;
                            iB2 = c(bArr, iA + 1) + iA;
                        } else {
                            iB2 = b(bArr, iA + 1) + iA;
                        }
                        int iA2 = a(iArr, iArr2, iA, iB2);
                        if ((iA2 < -32768 || iA2 > 32767) && !zArr[iA]) {
                            i3 = (i6 == 167 || i6 == 168) ? 2 : 5;
                            zArr[iA] = true;
                        } else {
                            i3 = 0;
                        }
                        iA += 3;
                        i7 = i3;
                        break;
                    case 9:
                        iA += 5;
                        break;
                    case 13:
                        if (i5 == 1) {
                            i7 = -(a(iArr, iArr2, 0, iA) & 3);
                        } else if (!zArr[iA]) {
                            i7 = iA & 3;
                            zArr[iA] = true;
                        }
                        int i8 = (iA + 4) - (iA & 3);
                        iA = i8 + (((a(bArr, i8 + 8) - a(bArr, i8 + 4)) + 1) * 4) + 12;
                        break;
                    case 14:
                        if (i5 == 1) {
                            i7 = -(a(iArr, iArr2, 0, iA) & 3);
                        } else if (!zArr[iA]) {
                            i7 = iA & 3;
                            zArr[iA] = true;
                        }
                        int i9 = (iA + 4) - (iA & 3);
                        iA = i9 + (a(bArr, i9 + 4) * 8) + 8;
                        break;
                    case 15:
                    default:
                        iA += 4;
                        break;
                    case 16:
                        iA = (bArr[iA + 1] & 255) == 132 ? iA + 6 : iA + 4;
                        break;
                }
                if (i7 != 0) {
                    int[] iArr3 = new int[iArr.length + 1];
                    int[] iArr4 = new int[iArr2.length + 1];
                    System.arraycopy(iArr, 0, iArr3, 0, iArr.length);
                    System.arraycopy(iArr2, 0, iArr4, 0, iArr2.length);
                    iArr3[iArr.length] = iA;
                    iArr4[iArr2.length] = i7;
                    if (i7 > 0) {
                        i5 = 3;
                        iArr2 = iArr4;
                        iArr = iArr3;
                    } else {
                        iArr2 = iArr4;
                        iArr = iArr3;
                    }
                }
            }
            if (i5 < 3) {
                i5--;
            }
            if (i5 == 0) {
                ByteVector byteVector = new ByteVector(this.r.b);
                int i10 = 0;
                while (i10 < this.r.b) {
                    int i11 = bArr[i10] & 255;
                    switch (ClassWriter.a[i11]) {
                        case 0:
                        case 4:
                            byteVector.putByte(i11);
                            i10++;
                            continue;
                        case 1:
                        case 3:
                        case 10:
                            byteVector.putByteArray(bArr, i10, 2);
                            i10 += 2;
                            continue;
                        case 2:
                        case 5:
                        case 6:
                        case 11:
                        case 12:
                            byteVector.putByteArray(bArr, i10, 3);
                            i10 += 3;
                            continue;
                        case 7:
                            byteVector.putByteArray(bArr, i10, 5);
                            i10 += 5;
                            continue;
                        case 8:
                            if (i11 > 201) {
                                i11 = i11 < 218 ? i11 - 49 : i11 - 20;
                                iB = c(bArr, i10 + 1) + i10;
                            } else {
                                iB = b(bArr, i10 + 1) + i10;
                            }
                            int iA3 = a(iArr, iArr2, i10, iB);
                            if (zArr[i10]) {
                                if (i11 == 167) {
                                    byteVector.putByte(HttpResponseCode.OK);
                                    i2 = iA3;
                                } else if (i11 == 168) {
                                    byteVector.putByte(CallbackEvent.ADS_LOADED_FROM_CACHE);
                                    i2 = iA3;
                                } else {
                                    byteVector.putByte(i11 <= 166 ? ((i11 + 1) ^ 1) - 1 : i11 ^ 1);
                                    byteVector.putShort(8);
                                    byteVector.putByte(HttpResponseCode.OK);
                                    i2 = iA3 - 3;
                                }
                                byteVector.putInt(i2);
                            } else {
                                byteVector.putByte(i11);
                                byteVector.putShort(iA3);
                            }
                            i10 += 3;
                            continue;
                        case 9:
                            int iA4 = a(iArr, iArr2, i10, a(bArr, i10 + 1) + i10);
                            byteVector.putByte(i11);
                            byteVector.putInt(iA4);
                            i10 += 5;
                            continue;
                        case 13:
                            int i12 = (i10 + 4) - (i10 & 3);
                            byteVector.putByte(Opcodes.TABLESWITCH);
                            byteVector.putByteArray(null, 0, (4 - (byteVector.b % 4)) % 4);
                            int iA5 = a(bArr, i12) + i10;
                            int i13 = i12 + 4;
                            byteVector.putInt(a(iArr, iArr2, i10, iA5));
                            int iA6 = a(bArr, i13);
                            int i14 = i13 + 4;
                            byteVector.putInt(iA6);
                            int iA7 = (a(bArr, i14) - iA6) + 1;
                            int i15 = i14 + 4;
                            byteVector.putInt(a(bArr, i15 - 4));
                            i = i15;
                            int i16 = iA7;
                            while (i16 > 0) {
                                byteVector.putInt(a(iArr, iArr2, i10, i10 + a(bArr, i)));
                                i16--;
                                i += 4;
                            }
                            break;
                        case 14:
                            int i17 = (i10 + 4) - (i10 & 3);
                            byteVector.putByte(Opcodes.LOOKUPSWITCH);
                            byteVector.putByteArray(null, 0, (4 - (byteVector.b % 4)) % 4);
                            int i18 = i17 + 4;
                            byteVector.putInt(a(iArr, iArr2, i10, a(bArr, i17) + i10));
                            int iA8 = a(bArr, i18);
                            byteVector.putInt(iA8);
                            i = i18 + 4;
                            int i19 = iA8;
                            while (i19 > 0) {
                                byteVector.putInt(a(bArr, i));
                                int i20 = i + 4;
                                byteVector.putInt(a(iArr, iArr2, i10, i10 + a(bArr, i20)));
                                i19--;
                                i = i20 + 4;
                            }
                            break;
                        case 15:
                        default:
                            byteVector.putByteArray(bArr, i10, 4);
                            i10 += 4;
                            continue;
                        case 16:
                            if ((bArr[i10 + 1] & 255) == 132) {
                                byteVector.putByteArray(bArr, i10, 6);
                                i10 += 6;
                            } else {
                                byteVector.putByteArray(bArr, i10, 4);
                                i10 += 4;
                                continue;
                            }
                            break;
                    }
                    i10 = i;
                }
                if (this.u > 0) {
                    if (this.M == 0) {
                        this.u = 0;
                        this.v = null;
                        this.x = null;
                        this.z = null;
                        Frame frame = new Frame();
                        frame.b = this.N;
                        frame.a(this.b, this.c, Type.getArgumentTypes(this.f), this.t);
                        b(frame);
                        for (Label label = this.N; label != null; label = label.i) {
                            int i21 = label.c - 3;
                            if ((label.a & 32) != 0 || (i21 >= 0 && zArr[i21])) {
                                a(iArr, iArr2, label);
                                b(label.h);
                            }
                        }
                    } else {
                        this.b.I = true;
                    }
                }
                for (Handler handler = this.B; handler != null; handler = handler.f) {
                    a(iArr, iArr2, handler.a);
                    a(iArr, iArr2, handler.b);
                    a(iArr, iArr2, handler.c);
                }
                int i22 = 0;
                while (true) {
                    int i23 = i22;
                    if (i23 >= 2) {
                        if (this.I != null) {
                            byte[] bArr2 = this.I.a;
                            for (int i24 = 0; i24 < this.I.b; i24 += 4) {
                                a(bArr2, i24, a(iArr, iArr2, 0, c(bArr2, i24)));
                            }
                        }
                        for (Attribute attribute = this.J; attribute != null; attribute = attribute.a) {
                            Label[] labels = attribute.getLabels();
                            if (labels != null) {
                                for (int length = labels.length - 1; length >= 0; length--) {
                                    a(iArr, iArr2, labels[length]);
                                }
                            }
                        }
                        this.r = byteVector;
                        return;
                    }
                    ByteVector byteVector2 = i23 == 0 ? this.E : this.G;
                    if (byteVector2 != null) {
                        byte[] bArr3 = byteVector2.a;
                        for (int i25 = 0; i25 < byteVector2.b; i25 += 10) {
                            int iC = c(bArr3, i25);
                            int iA9 = a(iArr, iArr2, 0, iC);
                            a(bArr3, i25, iA9);
                            a(bArr3, i25 + 2, a(iArr, iArr2, 0, iC + c(bArr3, i25 + 2)) - iA9);
                        }
                    }
                    i22 = i23 + 1;
                }
            } else {
                i4 = i5;
            }
        }
    }

    private void e() {
        if (this.M == 0) {
            Label label = new Label();
            label.h = new Frame();
            label.h.b = label;
            label.a(this, this.r.b, this.r.a);
            this.O.i = label;
            this.O = label;
        } else {
            this.P.g = this.R;
        }
        this.P = null;
    }

    final int a() {
        int length;
        if (this.h != 0) {
            return this.i + 6;
        }
        if (this.K) {
            d();
        }
        int iA = 8;
        if (this.r.b > 0) {
            this.b.newUTF8("Code");
            int i = this.r.b + 18 + (this.A * 8) + 8;
            if (this.E != null) {
                this.b.newUTF8("LocalVariableTable");
                i += this.E.b + 8;
            }
            if (this.G != null) {
                this.b.newUTF8("LocalVariableTypeTable");
                i += this.G.b + 8;
            }
            if (this.I != null) {
                this.b.newUTF8("LineNumberTable");
                i += this.I.b + 8;
            }
            if (this.v != null) {
                this.b.newUTF8((this.b.b & 65535) >= 50 ? "StackMapTable" : "StackMap");
                iA = i + this.v.b + 8;
            } else {
                iA = i;
            }
            if (this.J != null) {
                iA += this.J.a(this.b, this.r.a, this.r.b, this.s, this.t);
            }
        }
        if (this.j > 0) {
            this.b.newUTF8("Exceptions");
            iA += (this.j * 2) + 8;
        }
        if ((this.c & Opcodes.ACC_SYNTHETIC) != 0 && ((this.b.b & 65535) < 49 || (this.c & 262144) != 0)) {
            this.b.newUTF8("Synthetic");
            iA += 6;
        }
        if ((this.c & Opcodes.ACC_DEPRECATED) != 0) {
            this.b.newUTF8("Deprecated");
            iA += 6;
        }
        if (this.g != null) {
            this.b.newUTF8("Signature");
            this.b.newUTF8(this.g);
            iA += 8;
        }
        if (this.l != null) {
            this.b.newUTF8("AnnotationDefault");
            iA += this.l.b + 6;
        }
        if (this.m != null) {
            this.b.newUTF8("RuntimeVisibleAnnotations");
            iA += this.m.a() + 8;
        }
        if (this.n != null) {
            this.b.newUTF8("RuntimeInvisibleAnnotations");
            iA += this.n.a() + 8;
        }
        if (this.o != null) {
            this.b.newUTF8("RuntimeVisibleParameterAnnotations");
            length = iA + ((this.o.length - this.S) * 2) + 7;
            int length2 = this.o.length;
            while (true) {
                length2--;
                if (length2 < this.S) {
                    break;
                }
                length += this.o[length2] == null ? 0 : this.o[length2].a();
            }
        } else {
            length = iA;
        }
        if (this.p != null) {
            this.b.newUTF8("RuntimeInvisibleParameterAnnotations");
            length += ((this.p.length - this.S) * 2) + 7;
            int length3 = this.p.length;
            while (true) {
                length3--;
                if (length3 < this.S) {
                    break;
                }
                length += this.p[length3] == null ? 0 : this.p[length3].a();
            }
        }
        int i2 = length;
        return this.q != null ? i2 + this.q.a(this.b, null, 0, -1, -1) : i2;
    }

    final void a(ByteVector byteVector) {
        byteVector.putShort(((393216 | ((this.c & 262144) / 64)) ^ (-1)) & this.c).putShort(this.d).putShort(this.e);
        if (this.h != 0) {
            byteVector.putByteArray(this.b.J.b, this.h, this.i);
            return;
        }
        int iA = this.r.b > 0 ? 1 : 0;
        if (this.j > 0) {
            iA++;
        }
        if ((this.c & Opcodes.ACC_SYNTHETIC) != 0 && ((this.b.b & 65535) < 49 || (this.c & 262144) != 0)) {
            iA++;
        }
        if ((this.c & Opcodes.ACC_DEPRECATED) != 0) {
            iA++;
        }
        if (this.g != null) {
            iA++;
        }
        if (this.l != null) {
            iA++;
        }
        if (this.m != null) {
            iA++;
        }
        if (this.n != null) {
            iA++;
        }
        if (this.o != null) {
            iA++;
        }
        if (this.p != null) {
            iA++;
        }
        if (this.q != null) {
            iA += this.q.a();
        }
        byteVector.putShort(iA);
        if (this.r.b > 0) {
            int i = this.r.b + 12 + (this.A * 8);
            if (this.E != null) {
                i += this.E.b + 8;
            }
            if (this.G != null) {
                i += this.G.b + 8;
            }
            if (this.I != null) {
                i += this.I.b + 8;
            }
            int iA2 = this.v != null ? i + this.v.b + 8 : i;
            if (this.J != null) {
                iA2 += this.J.a(this.b, this.r.a, this.r.b, this.s, this.t);
            }
            byteVector.putShort(this.b.newUTF8("Code")).putInt(iA2);
            byteVector.putShort(this.s).putShort(this.t);
            byteVector.putInt(this.r.b).putByteArray(this.r.a, 0, this.r.b);
            byteVector.putShort(this.A);
            if (this.A > 0) {
                for (Handler handler = this.B; handler != null; handler = handler.f) {
                    byteVector.putShort(handler.a.c).putShort(handler.b.c).putShort(handler.c.c).putShort(handler.e);
                }
            }
            int iA3 = this.E != null ? 1 : 0;
            if (this.G != null) {
                iA3++;
            }
            if (this.I != null) {
                iA3++;
            }
            if (this.v != null) {
                iA3++;
            }
            if (this.J != null) {
                iA3 += this.J.a();
            }
            byteVector.putShort(iA3);
            if (this.E != null) {
                byteVector.putShort(this.b.newUTF8("LocalVariableTable"));
                byteVector.putInt(this.E.b + 2).putShort(this.D);
                byteVector.putByteArray(this.E.a, 0, this.E.b);
            }
            if (this.G != null) {
                byteVector.putShort(this.b.newUTF8("LocalVariableTypeTable"));
                byteVector.putInt(this.G.b + 2).putShort(this.F);
                byteVector.putByteArray(this.G.a, 0, this.G.b);
            }
            if (this.I != null) {
                byteVector.putShort(this.b.newUTF8("LineNumberTable"));
                byteVector.putInt(this.I.b + 2).putShort(this.H);
                byteVector.putByteArray(this.I.a, 0, this.I.b);
            }
            if (this.v != null) {
                byteVector.putShort(this.b.newUTF8((this.b.b & 65535) >= 50 ? "StackMapTable" : "StackMap"));
                byteVector.putInt(this.v.b + 2).putShort(this.u);
                byteVector.putByteArray(this.v.a, 0, this.v.b);
            }
            if (this.J != null) {
                this.J.a(this.b, this.r.a, this.r.b, this.t, this.s, byteVector);
            }
        }
        if (this.j > 0) {
            byteVector.putShort(this.b.newUTF8("Exceptions")).putInt((this.j * 2) + 2);
            byteVector.putShort(this.j);
            for (int i2 = 0; i2 < this.j; i2++) {
                byteVector.putShort(this.k[i2]);
            }
        }
        if ((this.c & Opcodes.ACC_SYNTHETIC) != 0 && ((this.b.b & 65535) < 49 || (this.c & 262144) != 0)) {
            byteVector.putShort(this.b.newUTF8("Synthetic")).putInt(0);
        }
        if ((this.c & Opcodes.ACC_DEPRECATED) != 0) {
            byteVector.putShort(this.b.newUTF8("Deprecated")).putInt(0);
        }
        if (this.g != null) {
            byteVector.putShort(this.b.newUTF8("Signature")).putInt(2).putShort(this.b.newUTF8(this.g));
        }
        if (this.l != null) {
            byteVector.putShort(this.b.newUTF8("AnnotationDefault"));
            byteVector.putInt(this.l.b);
            byteVector.putByteArray(this.l.a, 0, this.l.b);
        }
        if (this.m != null) {
            byteVector.putShort(this.b.newUTF8("RuntimeVisibleAnnotations"));
            this.m.a(byteVector);
        }
        if (this.n != null) {
            byteVector.putShort(this.b.newUTF8("RuntimeInvisibleAnnotations"));
            this.n.a(byteVector);
        }
        if (this.o != null) {
            byteVector.putShort(this.b.newUTF8("RuntimeVisibleParameterAnnotations"));
            AnnotationWriter.a(this.o, this.S, byteVector);
        }
        if (this.p != null) {
            byteVector.putShort(this.b.newUTF8("RuntimeInvisibleParameterAnnotations"));
            AnnotationWriter.a(this.p, this.S, byteVector);
        }
        if (this.q != null) {
            this.q.a(this.b, null, 0, -1, -1, byteVector);
        }
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitAnnotation(String str, boolean z) {
        ByteVector byteVector = new ByteVector();
        byteVector.putShort(this.b.newUTF8(str)).putShort(0);
        AnnotationWriter annotationWriter = new AnnotationWriter(this.b, true, byteVector, byteVector, 2);
        if (z) {
            annotationWriter.g = this.m;
            this.m = annotationWriter;
        } else {
            annotationWriter.g = this.n;
            this.n = annotationWriter;
        }
        return annotationWriter;
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitAnnotationDefault() {
        this.l = new ByteVector();
        return new AnnotationWriter(this.b, false, this.l, null, 0);
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.MethodVisitor
    public void visitAttribute(Attribute attribute) {
        if (attribute.isCodeAttribute()) {
            attribute.a = this.J;
            this.J = attribute;
        } else {
            attribute.a = this.q;
            this.q = attribute;
        }
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.MethodVisitor
    public void visitCode() {
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.MethodVisitor
    public void visitEnd() {
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.MethodVisitor
    public void visitFieldInsn(int i, String str, String str2, String str3) {
        int i2;
        int i3 = 1;
        Item itemA = this.b.a(str, str2, str3);
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(i, 0, this.b, itemA);
            } else {
                char cCharAt = str3.charAt(0);
                switch (i) {
                    case Opcodes.GETSTATIC /* 178 */:
                        i2 = ((cCharAt == 'D' || cCharAt == 'J') ? 2 : 1) + this.Q;
                        break;
                    case Opcodes.PUTSTATIC /* 179 */:
                        i2 = ((cCharAt == 'D' || cCharAt == 'J') ? -2 : -1) + this.Q;
                        break;
                    case Opcodes.GETFIELD /* 180 */:
                        int i4 = this.Q;
                        if (cCharAt != 'D' && cCharAt != 'J') {
                            i3 = 0;
                        }
                        i2 = i3 + i4;
                        break;
                    default:
                        i2 = this.Q + ((cCharAt == 'D' || cCharAt == 'J') ? -3 : -2);
                        break;
                }
                if (i2 > this.R) {
                    this.R = i2;
                }
                this.Q = i2;
            }
        }
        this.r.b(i, itemA.a);
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.MethodVisitor
    public void visitFrame(int i, int i2, Object[] objArr, int i3, Object[] objArr2) {
        int i4;
        int i5 = 0;
        if (this.M == 0) {
            return;
        }
        if (i == -1) {
            a(this.r.b, i2, i3);
            for (int i6 = 0; i6 < i2; i6++) {
                if (objArr[i6] instanceof String) {
                    int[] iArr = this.z;
                    int i7 = this.y;
                    this.y = i7 + 1;
                    iArr[i7] = this.b.c((String) objArr[i6]) | 24117248;
                } else if (objArr[i6] instanceof Integer) {
                    int[] iArr2 = this.z;
                    int i8 = this.y;
                    this.y = i8 + 1;
                    iArr2[i8] = ((Integer) objArr[i6]).intValue();
                } else {
                    int[] iArr3 = this.z;
                    int i9 = this.y;
                    this.y = i9 + 1;
                    iArr3[i9] = this.b.a("", ((Label) objArr[i6]).c) | 25165824;
                }
            }
            while (i5 < i3) {
                if (objArr2[i5] instanceof String) {
                    int[] iArr4 = this.z;
                    int i10 = this.y;
                    this.y = i10 + 1;
                    iArr4[i10] = this.b.c((String) objArr2[i5]) | 24117248;
                } else if (objArr2[i5] instanceof Integer) {
                    int[] iArr5 = this.z;
                    int i11 = this.y;
                    this.y = i11 + 1;
                    iArr5[i11] = ((Integer) objArr2[i5]).intValue();
                } else {
                    int[] iArr6 = this.z;
                    int i12 = this.y;
                    this.y = i12 + 1;
                    iArr6[i12] = this.b.a("", ((Label) objArr2[i5]).c) | 25165824;
                }
                i5++;
            }
            b();
            return;
        }
        if (this.v == null) {
            this.v = new ByteVector();
            i4 = this.r.b;
        } else {
            i4 = (this.r.b - this.w) - 1;
            if (i4 < 0) {
                if (i != 3) {
                    throw new IllegalStateException();
                }
                return;
            }
        }
        switch (i) {
            case 0:
                this.v.putByte(255).putShort(i4).putShort(i2);
                for (int i13 = 0; i13 < i2; i13++) {
                    a(objArr[i13]);
                }
                this.v.putShort(i3);
                while (i5 < i3) {
                    a(objArr2[i5]);
                    i5++;
                }
                break;
            case 1:
                this.v.putByte(i2 + Input.Keys.F8).putShort(i4);
                for (int i14 = 0; i14 < i2; i14++) {
                    a(objArr[i14]);
                }
                break;
            case 2:
                this.v.putByte(251 - i2).putShort(i4);
                break;
            case 3:
                if (i4 < 64) {
                    this.v.putByte(i4);
                } else {
                    this.v.putByte(Input.Keys.F8).putShort(i4);
                }
                break;
            case 4:
                if (i4 < 64) {
                    this.v.putByte(i4 + 64);
                } else {
                    this.v.putByte(Input.Keys.F4).putShort(i4);
                }
                a(objArr2[0]);
                break;
        }
        this.w = this.r.b;
        this.u++;
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.MethodVisitor
    public void visitIincInsn(int i, int i2) {
        int i3;
        if (this.P != null && this.M == 0) {
            this.P.h.a(132, i, (ClassWriter) null, (Item) null);
        }
        if (this.M != 2 && (i3 = i + 1) > this.t) {
            this.t = i3;
        }
        if (i > 255 || i2 > 127 || i2 < -128) {
            this.r.putByte(SmileConstants.MIN_BUFFER_FOR_POSSIBLE_SHORT_STRING).b(132, i).putShort(i2);
        } else {
            this.r.putByte(132).a(i, i2);
        }
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.MethodVisitor
    public void visitInsn(int i) {
        this.r.putByte(i);
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(i, 0, (ClassWriter) null, (Item) null);
            } else {
                int i2 = this.Q + Frame.a[i];
                if (i2 > this.R) {
                    this.R = i2;
                }
                this.Q = i2;
            }
            if ((i < 172 || i > 177) && i != 191) {
                return;
            }
            e();
        }
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.MethodVisitor
    public void visitIntInsn(int i, int i2) {
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(i, i2, (ClassWriter) null, (Item) null);
            } else if (i != 188) {
                int i3 = this.Q + 1;
                if (i3 > this.R) {
                    this.R = i3;
                }
                this.Q = i3;
            }
        }
        if (i == 17) {
            this.r.b(i, i2);
        } else {
            this.r.a(i, i2);
        }
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.MethodVisitor
    public void visitJumpInsn(int i, Label label) {
        Label label2 = null;
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(i, 0, (ClassWriter) null, (Item) null);
                label.a().a |= 16;
                a(0, label);
                if (i != 167) {
                    label2 = new Label();
                }
            } else if (i == 168) {
                if ((label.a & 512) == 0) {
                    label.a |= 512;
                    this.L++;
                }
                this.P.a |= 128;
                a(this.Q + 1, label);
                label2 = new Label();
            } else {
                this.Q += Frame.a[i];
                a(this.Q, label);
            }
        }
        if ((label.a & 2) == 0 || label.c - this.r.b >= -32768) {
            this.r.putByte(i);
            label.a(this, this.r, this.r.b - 1, false);
        } else {
            if (i == 167) {
                this.r.putByte(HttpResponseCode.OK);
            } else if (i == 168) {
                this.r.putByte(CallbackEvent.ADS_LOADED_FROM_CACHE);
            } else {
                if (label2 != null) {
                    label2.a |= 16;
                }
                this.r.putByte(i <= 166 ? ((i + 1) ^ 1) - 1 : i ^ 1);
                this.r.putShort(8);
                this.r.putByte(HttpResponseCode.OK);
            }
            label.a(this, this.r, this.r.b - 1, true);
        }
        if (this.P != null) {
            if (label2 != null) {
                visitLabel(label2);
            }
            if (i == 167) {
                e();
            }
        }
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.MethodVisitor
    public void visitLabel(Label label) {
        this.K |= label.a(this, this.r.b, this.r.a);
        if ((label.a & 1) != 0) {
            return;
        }
        if (this.M != 0) {
            if (this.M == 1) {
                if (this.P != null) {
                    this.P.g = this.R;
                    a(this.Q, label);
                }
                this.P = label;
                this.Q = 0;
                this.R = 0;
                if (this.O != null) {
                    this.O.i = label;
                }
                this.O = label;
                return;
            }
            return;
        }
        if (this.P != null) {
            if (label.c == this.P.c) {
                this.P.a |= label.a & 16;
                label.h = this.P.h;
                return;
            }
            a(0, label);
        }
        this.P = label;
        if (label.h == null) {
            label.h = new Frame();
            label.h.b = label;
        }
        if (this.O != null) {
            if (label.c == this.O.c) {
                this.O.a |= label.a & 16;
                label.h = this.O.h;
                this.P = this.O;
                return;
            }
            this.O.i = label;
        }
        this.O = label;
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.MethodVisitor
    public void visitLdcInsn(Object obj) {
        Item itemA = this.b.a(obj);
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(18, 0, this.b, itemA);
            } else {
                int i = (itemA.b == 5 || itemA.b == 6) ? this.Q + 2 : this.Q + 1;
                if (i > this.R) {
                    this.R = i;
                }
                this.Q = i;
            }
        }
        int i2 = itemA.a;
        if (itemA.b == 5 || itemA.b == 6) {
            this.r.b(20, i2);
        } else if (i2 >= 256) {
            this.r.b(19, i2);
        } else {
            this.r.a(18, i2);
        }
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.MethodVisitor
    public void visitLineNumber(int i, Label label) {
        if (this.I == null) {
            this.I = new ByteVector();
        }
        this.H++;
        this.I.putShort(label.c);
        this.I.putShort(i);
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.MethodVisitor
    public void visitLocalVariable(String str, String str2, String str3, Label label, Label label2, int i) {
        int i2 = 2;
        if (str3 != null) {
            if (this.G == null) {
                this.G = new ByteVector();
            }
            this.F++;
            this.G.putShort(label.c).putShort(label2.c - label.c).putShort(this.b.newUTF8(str)).putShort(this.b.newUTF8(str3)).putShort(i);
        }
        if (this.E == null) {
            this.E = new ByteVector();
        }
        this.D++;
        this.E.putShort(label.c).putShort(label2.c - label.c).putShort(this.b.newUTF8(str)).putShort(this.b.newUTF8(str2)).putShort(i);
        if (this.M != 2) {
            char cCharAt = str2.charAt(0);
            if (cCharAt != 'J' && cCharAt != 'D') {
                i2 = 1;
            }
            int i3 = i2 + i;
            if (i3 > this.t) {
                this.t = i3;
            }
        }
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.MethodVisitor
    public void visitLookupSwitchInsn(Label label, int[] iArr, Label[] labelArr) {
        int i = this.r.b;
        this.r.putByte(Opcodes.LOOKUPSWITCH);
        this.r.putByteArray(null, 0, (4 - (this.r.b % 4)) % 4);
        label.a(this, this.r, i, true);
        this.r.putInt(labelArr.length);
        for (int i2 = 0; i2 < labelArr.length; i2++) {
            this.r.putInt(iArr[i2]);
            labelArr[i2].a(this, this.r, i, true);
        }
        a(label, labelArr);
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.MethodVisitor
    public void visitMaxs(int i, int i2) {
        Label label;
        if (this.M != 0) {
            if (this.M != 1) {
                this.s = i;
                this.t = i2;
                return;
            }
            for (Handler handler = this.B; handler != null; handler = handler.f) {
                Label label2 = handler.c;
                Label label3 = handler.b;
                for (Label label4 = handler.a; label4 != label3; label4 = label4.i) {
                    Edge edge = new Edge();
                    edge.a = Integer.MAX_VALUE;
                    edge.b = label2;
                    if ((label4.a & 128) == 0) {
                        edge.c = label4.j;
                        label4.j = edge;
                    } else {
                        edge.c = label4.j.c.c;
                        label4.j.c.c = edge;
                    }
                }
            }
            if (this.L > 0) {
                this.N.b(null, 1L, this.L);
                int i3 = 0;
                for (Label label5 = this.N; label5 != null; label5 = label5.i) {
                    if ((label5.a & 128) != 0) {
                        Label label6 = label5.j.c.b;
                        if ((label6.a & 1024) == 0) {
                            i3++;
                            label6.b(null, ((((long) i3) / 32) << 32) | (1 << (i3 % 32)), this.L);
                        }
                    }
                }
                for (Label label7 = this.N; label7 != null; label7 = label7.i) {
                    if ((label7.a & 128) != 0) {
                        for (Label label8 = this.N; label8 != null; label8 = label8.i) {
                            label8.a &= -2049;
                        }
                        label7.j.c.b.b(label7, 0L, this.L);
                    }
                }
            }
            Label label9 = this.N;
            int i4 = 0;
            while (label9 != null) {
                Label label10 = label9.k;
                int i5 = label9.f;
                int i6 = label9.g + i5;
                if (i6 <= i4) {
                    i6 = i4;
                }
                Edge edge2 = label9.j;
                Edge edge3 = (label9.a & 128) != 0 ? edge2.c : edge2;
                while (edge3 != null) {
                    Label label11 = edge3.b;
                    if ((label11.a & 8) == 0) {
                        label11.f = edge3.a == Integer.MAX_VALUE ? 1 : edge3.a + i5;
                        label11.a |= 8;
                        label11.k = label10;
                        label = label11;
                    } else {
                        label = label10;
                    }
                    edge3 = edge3.c;
                    label10 = label;
                }
                label9 = label10;
                i4 = i6;
            }
            this.s = i4;
            return;
        }
        for (Handler handler2 = this.B; handler2 != null; handler2 = handler2.f) {
            Label labelA = handler2.a.a();
            Label labelA2 = handler2.c.a();
            Label labelA3 = handler2.b.a();
            int iC = 24117248 | this.b.c(handler2.d == null ? "java/lang/Throwable" : handler2.d);
            labelA2.a |= 16;
            for (Label label12 = labelA; label12 != labelA3; label12 = label12.i) {
                Edge edge4 = new Edge();
                edge4.a = iC;
                edge4.b = labelA2;
                edge4.c = label12.j;
                label12.j = edge4;
            }
        }
        Frame frame = this.N.h;
        frame.a(this.b, this.c, Type.getArgumentTypes(this.f), this.t);
        b(frame);
        Label label13 = this.N;
        int i7 = 0;
        while (label13 != null) {
            Label label14 = label13.k;
            label13.k = null;
            Frame frame2 = label13.h;
            if ((label13.a & 16) != 0) {
                label13.a |= 32;
            }
            label13.a |= 64;
            int length = frame2.d.length + label13.g;
            if (length <= i7) {
                length = i7;
            }
            Edge edge5 = label13.j;
            while (edge5 != null) {
                Label labelA4 = edge5.b.a();
                if (frame2.a(this.b, labelA4.h, edge5.a) && labelA4.k == null) {
                    labelA4.k = label14;
                } else {
                    labelA4 = label14;
                }
                edge5 = edge5.c;
                label14 = labelA4;
            }
            label13 = label14;
            i7 = length;
        }
        int iMax = i7;
        for (Label label15 = this.N; label15 != null; label15 = label15.i) {
            Frame frame3 = label15.h;
            if ((label15.a & 32) != 0) {
                b(frame3);
            }
            if ((label15.a & 64) == 0) {
                Label label16 = label15.i;
                int i8 = label15.c;
                int i9 = (label16 == null ? this.r.b : label16.c) - 1;
                if (i9 >= i8) {
                    iMax = Math.max(iMax, 1);
                    for (int i10 = i8; i10 < i9; i10++) {
                        this.r.a[i10] = 0;
                    }
                    this.r.a[i9] = -65;
                    a(i8, 0, 1);
                    int[] iArr = this.z;
                    int i11 = this.y;
                    this.y = i11 + 1;
                    iArr[i11] = this.b.c("java/lang/Throwable") | 24117248;
                    b();
                }
            }
        }
        this.s = iMax;
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.MethodVisitor
    public void visitMethodInsn(int i, String str, String str2, String str3) {
        int argumentsAndReturnSizes;
        boolean z = i == 185;
        Item itemA = i == 186 ? this.b.a(str2, str3) : this.b.a(str, str2, str3, z);
        int argumentsAndReturnSizes2 = itemA.c;
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(i, 0, this.b, itemA);
            } else {
                if (argumentsAndReturnSizes2 == 0) {
                    argumentsAndReturnSizes = Type.getArgumentsAndReturnSizes(str3);
                    itemA.c = argumentsAndReturnSizes;
                } else {
                    argumentsAndReturnSizes = argumentsAndReturnSizes2;
                }
                int i2 = (i == 184 || i == 186) ? (this.Q - (argumentsAndReturnSizes >> 2)) + (argumentsAndReturnSizes & 3) + 1 : (this.Q - (argumentsAndReturnSizes >> 2)) + (argumentsAndReturnSizes & 3);
                if (i2 > this.R) {
                    this.R = i2;
                }
                this.Q = i2;
                argumentsAndReturnSizes2 = argumentsAndReturnSizes;
            }
        }
        if (z) {
            if (argumentsAndReturnSizes2 == 0) {
                argumentsAndReturnSizes2 = Type.getArgumentsAndReturnSizes(str3);
                itemA.c = argumentsAndReturnSizes2;
            }
            this.r.b(Opcodes.INVOKEINTERFACE, itemA.a).a(argumentsAndReturnSizes2 >> 2, 0);
            return;
        }
        this.r.b(i, itemA.a);
        if (i == 186) {
            this.r.putShort(0);
        }
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.MethodVisitor
    public void visitMultiANewArrayInsn(String str, int i) {
        Item itemA = this.b.a(str);
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(Opcodes.MULTIANEWARRAY, i, this.b, itemA);
            } else {
                this.Q += 1 - i;
            }
        }
        this.r.b(Opcodes.MULTIANEWARRAY, itemA.a).putByte(i);
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitParameterAnnotation(int i, String str, boolean z) {
        ByteVector byteVector = new ByteVector();
        if ("Ljava/lang/Synthetic;".equals(str)) {
            this.S = Math.max(this.S, i + 1);
            return new AnnotationWriter(this.b, false, byteVector, null, 0);
        }
        byteVector.putShort(this.b.newUTF8(str)).putShort(0);
        AnnotationWriter annotationWriter = new AnnotationWriter(this.b, true, byteVector, byteVector, 2);
        if (z) {
            if (this.o == null) {
                this.o = new AnnotationWriter[Type.getArgumentTypes(this.f).length];
            }
            annotationWriter.g = this.o[i];
            this.o[i] = annotationWriter;
            return annotationWriter;
        }
        if (this.p == null) {
            this.p = new AnnotationWriter[Type.getArgumentTypes(this.f).length];
        }
        annotationWriter.g = this.p[i];
        this.p[i] = annotationWriter;
        return annotationWriter;
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.MethodVisitor
    public void visitTableSwitchInsn(int i, int i2, Label label, Label[] labelArr) {
        int i3 = this.r.b;
        this.r.putByte(Opcodes.TABLESWITCH);
        this.r.putByteArray(null, 0, (4 - (this.r.b % 4)) % 4);
        label.a(this, this.r, i3, true);
        this.r.putInt(i).putInt(i2);
        for (Label label2 : labelArr) {
            label2.a(this, this.r, i3, true);
        }
        a(label, labelArr);
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.MethodVisitor
    public void visitTryCatchBlock(Label label, Label label2, Label label3, String str) {
        this.A++;
        Handler handler = new Handler();
        handler.a = label;
        handler.b = label2;
        handler.c = label3;
        handler.d = str;
        handler.e = str != null ? this.b.newClass(str) : 0;
        if (this.C == null) {
            this.B = handler;
        } else {
            this.C.f = handler;
        }
        this.C = handler;
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.MethodVisitor
    public void visitTypeInsn(int i, String str) {
        Item itemA = this.b.a(str);
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(i, this.r.b, this.b, itemA);
            } else if (i == 187) {
                int i2 = this.Q + 1;
                if (i2 > this.R) {
                    this.R = i2;
                }
                this.Q = i2;
            }
        }
        this.r.b(i, itemA.a);
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.MethodVisitor
    public void visitVarInsn(int i, int i2) {
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(i, i2, (ClassWriter) null, (Item) null);
            } else if (i == 169) {
                this.P.a |= 256;
                this.P.f = this.Q;
                e();
            } else {
                int i3 = this.Q + Frame.a[i];
                if (i3 > this.R) {
                    this.R = i3;
                }
                this.Q = i3;
            }
        }
        if (this.M != 2) {
            int i4 = (i == 22 || i == 24 || i == 55 || i == 57) ? i2 + 2 : i2 + 1;
            if (i4 > this.t) {
                this.t = i4;
            }
        }
        if (i2 < 4 && i != 169) {
            this.r.putByte(i < 54 ? ((i - 21) << 2) + 26 + i2 : ((i - 54) << 2) + 59 + i2);
        } else if (i2 >= 256) {
            this.r.putByte(SmileConstants.MIN_BUFFER_FOR_POSSIBLE_SHORT_STRING).b(i, i2);
        } else {
            this.r.a(i, i2);
        }
        if (i < 54 || this.M != 0 || this.A <= 0) {
            return;
        }
        visitLabel(new Label());
    }
}
