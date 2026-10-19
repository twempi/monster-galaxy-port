package org.codehaus.jackson.org.objectweb.asm;

/* JADX INFO: loaded from: classes.dex */
final class AnnotationWriter implements AnnotationVisitor {
    private final ClassWriter a;
    private int b;
    private final boolean c;
    private final ByteVector d;
    private final ByteVector e;
    private final int f;
    AnnotationWriter g;
    AnnotationWriter h;

    AnnotationWriter(ClassWriter classWriter, boolean z, ByteVector byteVector, ByteVector byteVector2, int i) {
        this.a = classWriter;
        this.c = z;
        this.d = byteVector;
        this.e = byteVector2;
        this.f = i;
    }

    static void a(AnnotationWriter[] annotationWriterArr, int i, ByteVector byteVector) {
        int length = ((annotationWriterArr.length - i) * 2) + 1;
        for (int i2 = i; i2 < annotationWriterArr.length; i2++) {
            length += annotationWriterArr[i2] == null ? 0 : annotationWriterArr[i2].a();
        }
        byteVector.putInt(length).putByte(annotationWriterArr.length - i);
        while (i < annotationWriterArr.length) {
            AnnotationWriter annotationWriter = annotationWriterArr[i];
            AnnotationWriter annotationWriter2 = null;
            int i3 = 0;
            while (annotationWriter != null) {
                i3++;
                annotationWriter.visitEnd();
                annotationWriter.h = annotationWriter2;
                AnnotationWriter annotationWriter3 = annotationWriter;
                annotationWriter = annotationWriter.g;
                annotationWriter2 = annotationWriter3;
            }
            byteVector.putShort(i3);
            while (annotationWriter2 != null) {
                byteVector.putByteArray(annotationWriter2.d.a, 0, annotationWriter2.d.b);
                annotationWriter2 = annotationWriter2.h;
            }
            i++;
        }
    }

    int a() {
        int i = 0;
        while (this != null) {
            i += this.d.b;
            this = this.g;
        }
        return i;
    }

    void a(ByteVector byteVector) {
        AnnotationWriter annotationWriter = null;
        int i = 2;
        int i2 = 0;
        for (AnnotationWriter annotationWriter2 = this; annotationWriter2 != null; annotationWriter2 = annotationWriter2.g) {
            i2++;
            i += annotationWriter2.d.b;
            annotationWriter2.visitEnd();
            annotationWriter2.h = annotationWriter;
            annotationWriter = annotationWriter2;
        }
        byteVector.putInt(i);
        byteVector.putShort(i2);
        while (annotationWriter != null) {
            byteVector.putByteArray(annotationWriter.d.a, 0, annotationWriter.d.b);
            annotationWriter = annotationWriter.h;
        }
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.AnnotationVisitor
    public void visit(String str, Object obj) {
        int i = 0;
        this.b++;
        if (this.c) {
            this.d.putShort(this.a.newUTF8(str));
        }
        if (obj instanceof String) {
            this.d.b(Opcodes.DREM, this.a.newUTF8((String) obj));
            return;
        }
        if (obj instanceof Byte) {
            this.d.b(66, this.a.a((int) ((Byte) obj).byteValue()).a);
            return;
        }
        if (obj instanceof Boolean) {
            this.d.b(90, this.a.a(((Boolean) obj).booleanValue() ? 1 : 0).a);
            return;
        }
        if (obj instanceof Character) {
            this.d.b(67, this.a.a((int) ((Character) obj).charValue()).a);
            return;
        }
        if (obj instanceof Short) {
            this.d.b(83, this.a.a((int) ((Short) obj).shortValue()).a);
            return;
        }
        if (obj instanceof Type) {
            this.d.b(99, this.a.newUTF8(((Type) obj).getDescriptor()));
            return;
        }
        if (obj instanceof byte[]) {
            byte[] bArr = (byte[]) obj;
            this.d.b(91, bArr.length);
            while (i < bArr.length) {
                this.d.b(66, this.a.a((int) bArr[i]).a);
                i++;
            }
            return;
        }
        if (obj instanceof boolean[]) {
            boolean[] zArr = (boolean[]) obj;
            this.d.b(91, zArr.length);
            for (boolean z : zArr) {
                this.d.b(90, this.a.a(z ? 1 : 0).a);
            }
            return;
        }
        if (obj instanceof short[]) {
            short[] sArr = (short[]) obj;
            this.d.b(91, sArr.length);
            while (i < sArr.length) {
                this.d.b(83, this.a.a((int) sArr[i]).a);
                i++;
            }
            return;
        }
        if (obj instanceof char[]) {
            char[] cArr = (char[]) obj;
            this.d.b(91, cArr.length);
            while (i < cArr.length) {
                this.d.b(67, this.a.a((int) cArr[i]).a);
                i++;
            }
            return;
        }
        if (obj instanceof int[]) {
            int[] iArr = (int[]) obj;
            this.d.b(91, iArr.length);
            while (i < iArr.length) {
                this.d.b(73, this.a.a(iArr[i]).a);
                i++;
            }
            return;
        }
        if (obj instanceof long[]) {
            long[] jArr = (long[]) obj;
            this.d.b(91, jArr.length);
            while (i < jArr.length) {
                this.d.b(74, this.a.a(jArr[i]).a);
                i++;
            }
            return;
        }
        if (obj instanceof float[]) {
            float[] fArr = (float[]) obj;
            this.d.b(91, fArr.length);
            while (i < fArr.length) {
                this.d.b(70, this.a.a(fArr[i]).a);
                i++;
            }
            return;
        }
        if (!(obj instanceof double[])) {
            Item itemA = this.a.a(obj);
            this.d.b(".s.IFJDCS".charAt(itemA.b), itemA.a);
            return;
        }
        double[] dArr = (double[]) obj;
        this.d.b(91, dArr.length);
        while (i < dArr.length) {
            this.d.b(68, this.a.a(dArr[i]).a);
            i++;
        }
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.AnnotationVisitor
    public AnnotationVisitor visitAnnotation(String str, String str2) {
        this.b++;
        if (this.c) {
            this.d.putShort(this.a.newUTF8(str));
        }
        this.d.b(64, this.a.newUTF8(str2)).putShort(0);
        return new AnnotationWriter(this.a, true, this.d, this.d, this.d.b - 2);
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.AnnotationVisitor
    public AnnotationVisitor visitArray(String str) {
        this.b++;
        if (this.c) {
            this.d.putShort(this.a.newUTF8(str));
        }
        this.d.b(91, 0);
        return new AnnotationWriter(this.a, false, this.d, this.d, this.d.b - 2);
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.AnnotationVisitor
    public void visitEnd() {
        if (this.e != null) {
            byte[] bArr = this.e.a;
            bArr[this.f] = (byte) (this.b >>> 8);
            bArr[this.f + 1] = (byte) this.b;
        }
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.AnnotationVisitor
    public void visitEnum(String str, String str2, String str3) {
        this.b++;
        if (this.c) {
            this.d.putShort(this.a.newUTF8(str));
        }
        this.d.b(101, this.a.newUTF8(str2)).putShort(this.a.newUTF8(str3));
    }
}
