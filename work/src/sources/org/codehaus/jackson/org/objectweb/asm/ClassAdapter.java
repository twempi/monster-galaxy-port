package org.codehaus.jackson.org.objectweb.asm;

/* JADX INFO: loaded from: classes.dex */
public class ClassAdapter implements ClassVisitor {
    protected ClassVisitor cv;

    public ClassAdapter(ClassVisitor classVisitor) {
        this.cv = classVisitor;
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.ClassVisitor
    public void visit(int i, int i2, String str, String str2, String str3, String[] strArr) {
        this.cv.visit(i, i2, str, str2, str3, strArr);
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.ClassVisitor
    public AnnotationVisitor visitAnnotation(String str, boolean z) {
        return this.cv.visitAnnotation(str, z);
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.ClassVisitor
    public void visitAttribute(Attribute attribute) {
        this.cv.visitAttribute(attribute);
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.ClassVisitor
    public void visitEnd() {
        this.cv.visitEnd();
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.ClassVisitor
    public FieldVisitor visitField(int i, String str, String str2, String str3, Object obj) {
        return this.cv.visitField(i, str, str2, str3, obj);
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.ClassVisitor
    public void visitInnerClass(String str, String str2, String str3, int i) {
        this.cv.visitInnerClass(str, str2, str3, i);
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.ClassVisitor
    public MethodVisitor visitMethod(int i, String str, String str2, String str3, String[] strArr) {
        return this.cv.visitMethod(i, str, str2, str3, strArr);
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.ClassVisitor
    public void visitOuterClass(String str, String str2, String str3) {
        this.cv.visitOuterClass(str, str2, str3);
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.ClassVisitor
    public void visitSource(String str, String str2) {
        this.cv.visitSource(str, str2);
    }
}
