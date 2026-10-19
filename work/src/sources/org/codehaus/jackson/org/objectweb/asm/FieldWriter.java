package org.codehaus.jackson.org.objectweb.asm;

/* JADX INFO: loaded from: classes.dex */
final class FieldWriter implements FieldVisitor {
    FieldWriter a;
    private final ClassWriter b;
    private final int c;
    private final int d;
    private final int e;
    private int f;
    private int g;
    private AnnotationWriter h;
    private AnnotationWriter i;
    private Attribute j;

    FieldWriter(ClassWriter classWriter, int i, String str, String str2, String str3, Object obj) {
        if (classWriter.y == null) {
            classWriter.y = this;
        } else {
            classWriter.z.a = this;
        }
        classWriter.z = this;
        this.b = classWriter;
        this.c = i;
        this.d = classWriter.newUTF8(str);
        this.e = classWriter.newUTF8(str2);
        if (str3 != null) {
            this.f = classWriter.newUTF8(str3);
        }
        if (obj != null) {
            this.g = classWriter.a(obj).a;
        }
    }

    int a() {
        int iA;
        int iA2 = 8;
        if (this.g != 0) {
            this.b.newUTF8("ConstantValue");
            iA2 = 16;
        }
        if ((this.c & Opcodes.ACC_SYNTHETIC) != 0 && ((this.b.b & 65535) < 49 || (this.c & 262144) != 0)) {
            this.b.newUTF8("Synthetic");
            iA2 += 6;
        }
        if ((this.c & Opcodes.ACC_DEPRECATED) != 0) {
            this.b.newUTF8("Deprecated");
            iA2 += 6;
        }
        if (this.f != 0) {
            this.b.newUTF8("Signature");
            iA2 += 8;
        }
        if (this.h != null) {
            this.b.newUTF8("RuntimeVisibleAnnotations");
            iA2 += this.h.a() + 8;
        }
        if (this.i != null) {
            this.b.newUTF8("RuntimeInvisibleAnnotations");
            iA = iA2 + this.i.a() + 8;
        } else {
            iA = iA2;
        }
        return this.j != null ? iA + this.j.a(this.b, null, 0, -1, -1) : iA;
    }

    void a(ByteVector byteVector) {
        byteVector.putShort(((393216 | ((this.c & 262144) / 64)) ^ (-1)) & this.c).putShort(this.d).putShort(this.e);
        int iA = this.g != 0 ? 1 : 0;
        if ((this.c & Opcodes.ACC_SYNTHETIC) != 0 && ((this.b.b & 65535) < 49 || (this.c & 262144) != 0)) {
            iA++;
        }
        if ((this.c & Opcodes.ACC_DEPRECATED) != 0) {
            iA++;
        }
        if (this.f != 0) {
            iA++;
        }
        if (this.h != null) {
            iA++;
        }
        if (this.i != null) {
            iA++;
        }
        if (this.j != null) {
            iA += this.j.a();
        }
        byteVector.putShort(iA);
        if (this.g != 0) {
            byteVector.putShort(this.b.newUTF8("ConstantValue"));
            byteVector.putInt(2).putShort(this.g);
        }
        if ((this.c & Opcodes.ACC_SYNTHETIC) != 0 && ((this.b.b & 65535) < 49 || (this.c & 262144) != 0)) {
            byteVector.putShort(this.b.newUTF8("Synthetic")).putInt(0);
        }
        if ((this.c & Opcodes.ACC_DEPRECATED) != 0) {
            byteVector.putShort(this.b.newUTF8("Deprecated")).putInt(0);
        }
        if (this.f != 0) {
            byteVector.putShort(this.b.newUTF8("Signature"));
            byteVector.putInt(2).putShort(this.f);
        }
        if (this.h != null) {
            byteVector.putShort(this.b.newUTF8("RuntimeVisibleAnnotations"));
            this.h.a(byteVector);
        }
        if (this.i != null) {
            byteVector.putShort(this.b.newUTF8("RuntimeInvisibleAnnotations"));
            this.i.a(byteVector);
        }
        if (this.j != null) {
            this.j.a(this.b, null, 0, -1, -1, byteVector);
        }
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.FieldVisitor
    public AnnotationVisitor visitAnnotation(String str, boolean z) {
        ByteVector byteVector = new ByteVector();
        byteVector.putShort(this.b.newUTF8(str)).putShort(0);
        AnnotationWriter annotationWriter = new AnnotationWriter(this.b, true, byteVector, byteVector, 2);
        if (z) {
            annotationWriter.g = this.h;
            this.h = annotationWriter;
        } else {
            annotationWriter.g = this.i;
            this.i = annotationWriter;
        }
        return annotationWriter;
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.FieldVisitor
    public void visitAttribute(Attribute attribute) {
        attribute.a = this.j;
        this.j = attribute;
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.FieldVisitor
    public void visitEnd() {
    }
}
