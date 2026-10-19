package org.codehaus.jackson.org.objectweb.asm;

/* JADX INFO: loaded from: classes.dex */
public class Attribute {
    Attribute a;
    byte[] b;
    public final String type;

    protected Attribute(String str) {
        this.type = str;
    }

    final int a() {
        int i = 0;
        while (this != null) {
            i++;
            this = this.a;
        }
        return i;
    }

    final int a(ClassWriter classWriter, byte[] bArr, int i, int i2, int i3) {
        int i4 = 0;
        Attribute attribute = this;
        while (attribute != null) {
            classWriter.newUTF8(attribute.type);
            int i5 = attribute.write(classWriter, bArr, i, i2, i3).b + 6 + i4;
            attribute = attribute.a;
            i4 = i5;
        }
        return i4;
    }

    final void a(ClassWriter classWriter, byte[] bArr, int i, int i2, int i3, ByteVector byteVector) {
        for (Attribute attribute = this; attribute != null; attribute = attribute.a) {
            ByteVector byteVectorWrite = attribute.write(classWriter, bArr, i, i2, i3);
            byteVector.putShort(classWriter.newUTF8(attribute.type)).putInt(byteVectorWrite.b);
            byteVector.putByteArray(byteVectorWrite.a, 0, byteVectorWrite.b);
        }
    }

    protected Label[] getLabels() {
        return null;
    }

    public boolean isCodeAttribute() {
        return false;
    }

    public boolean isUnknown() {
        return true;
    }

    protected Attribute read(ClassReader classReader, int i, int i2, char[] cArr, int i3, Label[] labelArr) {
        Attribute attribute = new Attribute(this.type);
        attribute.b = new byte[i2];
        System.arraycopy(classReader.b, i, attribute.b, 0, i2);
        return attribute;
    }

    protected ByteVector write(ClassWriter classWriter, byte[] bArr, int i, int i2, int i3) {
        ByteVector byteVector = new ByteVector();
        byteVector.a = this.b;
        byteVector.b = this.b.length;
        return byteVector;
    }
}
