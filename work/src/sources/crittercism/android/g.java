package crittercism.android;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import oauth.signpost.OAuth;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.smile.SmileConstants;

/* JADX INFO: loaded from: classes.dex */
public final class g {
    private static final byte[] a;
    private static final byte[] b = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, SmileConstants.TOKEN_KEY_LONG_STRING, 53, 54, 55, 56, 57, 43, 47};
    private static final byte[] c;

    public static class a extends FilterOutputStream {
        private boolean a;
        private int b;
        private byte[] c;
        private int d;
        private int e;
        private boolean f;
        private byte[] g;
        private boolean h;

        @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
        public final void close() throws IOException {
            if (this.b > 0) {
                if (!this.a) {
                    throw new IOException("Base64 input not properly padded.");
                }
                this.out.write(g.a(this.g, this.c, this.b));
                this.b = 0;
            }
            super.close();
            this.c = null;
            this.out = null;
        }

        @Override // java.io.FilterOutputStream, java.io.OutputStream
        public final void write(int i) throws IOException {
            if (this.h) {
                ((FilterOutputStream) this).out.write(i);
                return;
            }
            if (this.a) {
                byte[] bArr = this.c;
                int i2 = this.b;
                this.b = i2 + 1;
                bArr[i2] = (byte) i;
                if (this.b >= this.d) {
                    this.out.write(g.a(this.g, this.c, this.d));
                    this.e += 4;
                    if (this.f && this.e >= 76) {
                        this.out.write(10);
                        this.e = 0;
                    }
                    this.b = 0;
                    return;
                }
                return;
            }
            if (g.c[i & Opcodes.LAND] <= -5) {
                if (g.c[i & Opcodes.LAND] != -5) {
                    throw new IOException("Invalid character in Base64 data.");
                }
                return;
            }
            byte[] bArr2 = this.c;
            int i3 = this.b;
            this.b = i3 + 1;
            bArr2[i3] = (byte) i;
            if (this.b >= this.d) {
                this.out.write(this.g, 0, g.b(this.c, this.g));
                this.b = 0;
            }
        }

        @Override // java.io.FilterOutputStream, java.io.OutputStream
        public final void write(byte[] bArr, int i, int i2) throws IOException {
            if (this.h) {
                ((FilterOutputStream) this).out.write(bArr, i, i2);
                return;
            }
            for (int i3 = 0; i3 < i2; i3++) {
                write(bArr[i + i3]);
            }
        }
    }

    static {
        byte[] bytes;
        try {
            bytes = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".getBytes(OAuth.ENCODING);
        } catch (UnsupportedEncodingException e) {
            bytes = b;
        }
        a = bytes;
        c = new byte[]{-9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 62, -9, -9, -9, 63, SmileConstants.TOKEN_KEY_LONG_STRING, 53, 54, 55, 56, 57, SmileConstants.HEADER_BYTE_1, 59, 60, 61, -9, -9, -9, -1, -9, -9, -9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -9, -9, -9, -9, -9, -9, 26, 27, 28, 29, 30, 31, 32, SmileConstants.TOKEN_LITERAL_NULL, SmileConstants.TOKEN_LITERAL_FALSE, SmileConstants.TOKEN_LITERAL_TRUE, 36, 37, 38, 39, 40, SmileConstants.HEADER_BYTE_2, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -9, -9, -9, -9};
    }

    public static String a(byte[] bArr) {
        return a(bArr, bArr.length);
    }

    private static String a(byte[] bArr, int i) {
        int i2 = (i * 4) / 3;
        byte[] bArr2 = new byte[(i % 3 > 0 ? 4 : 0) + i2 + (i2 / 76)];
        int i3 = i - 2;
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        while (i6 < i3) {
            a(bArr, i6 + 0, 3, bArr2, i5);
            i4 += 4;
            if (i4 == 76) {
                bArr2[i5 + 4] = 10;
                i5++;
                i4 = 0;
            }
            i6 += 3;
            i5 += 4;
        }
        if (i6 < i) {
            a(bArr, i6 + 0, i - i6, bArr2, i5);
            i5 += 4;
        }
        try {
            return new String(bArr2, 0, i5, OAuth.ENCODING);
        } catch (UnsupportedEncodingException e) {
            return new String(bArr2, 0, i5);
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private static byte[] a(byte[] bArr, int i, int i2, byte[] bArr2, int i3) {
        int i4 = (i2 > 2 ? (bArr[i + 2] << 24) >>> 24 : 0) | (i2 > 1 ? (bArr[i + 1] << 24) >>> 16 : 0) | (i2 > 0 ? (bArr[i] << 24) >>> 8 : 0);
        switch (i2) {
            case 1:
                bArr2[i3] = a[i4 >>> 18];
                bArr2[i3 + 1] = a[(i4 >>> 12) & 63];
                bArr2[i3 + 2] = 61;
                bArr2[i3 + 3] = 61;
                return bArr2;
            case 2:
                bArr2[i3] = a[i4 >>> 18];
                bArr2[i3 + 1] = a[(i4 >>> 12) & 63];
                bArr2[i3 + 2] = a[(i4 >>> 6) & 63];
                bArr2[i3 + 3] = 61;
                return bArr2;
            case 3:
                bArr2[i3] = a[i4 >>> 18];
                bArr2[i3 + 1] = a[(i4 >>> 12) & 63];
                bArr2[i3 + 2] = a[(i4 >>> 6) & 63];
                bArr2[i3 + 3] = a[i4 & 63];
                return bArr2;
            default:
                return bArr2;
        }
    }

    static /* synthetic */ byte[] a(byte[] bArr, byte[] bArr2, int i) {
        a(bArr2, 0, i, bArr, 0);
        return bArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int b(byte[] bArr, byte[] bArr2) {
        if (bArr[2] == 61) {
            bArr2[0] = (byte) ((((c[bArr[0]] & 255) << 18) | ((c[bArr[1]] & 255) << 12)) >>> 16);
            return 1;
        }
        if (bArr[3] == 61) {
            int i = ((c[bArr[0]] & 255) << 18) | ((c[bArr[1]] & 255) << 12) | ((c[bArr[2]] & 255) << 6);
            bArr2[0] = (byte) (i >>> 16);
            bArr2[1] = (byte) (i >>> 8);
            return 2;
        }
        try {
            int i2 = ((c[bArr[0]] & 255) << 18) | ((c[bArr[1]] & 255) << 12) | ((c[bArr[2]] & 255) << 6) | (c[bArr[3]] & 255);
            bArr2[0] = (byte) (i2 >> 16);
            bArr2[1] = (byte) (i2 >> 8);
            bArr2[2] = (byte) i2;
            return 3;
        } catch (Exception e) {
            System.out.println(((int) bArr[0]) + ": " + ((int) c[bArr[0]]));
            System.out.println(((int) bArr[1]) + ": " + ((int) c[bArr[1]]));
            System.out.println(((int) bArr[2]) + ": " + ((int) c[bArr[2]]));
            System.out.println(((int) bArr[3]) + ": " + ((int) c[bArr[3]]));
            return -1;
        }
    }
}
