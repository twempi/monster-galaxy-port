package org.codehaus.jackson.org.objectweb.asm;

/* JADX INFO: loaded from: classes.dex */
public class MethodAdapter implements MethodVisitor {
    protected MethodVisitor mv;

    public MethodAdapter(MethodVisitor methodVisitor) {
        this.mv = methodVisitor;
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitAnnotation(String str, boolean z) {
        return this.mv.visitAnnotation(str, z);
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitAnnotationDefault() {
        return this.mv.visitAnnotationDefault();
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.MethodVisitor
    public void visitAttribute(Attribute attribute) {
        this.mv.visitAttribute(attribute);
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.MethodVisitor
    public void visitCode() {
        this.mv.visitCode();
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.MethodVisitor
    public void visitEnd() {
        this.mv.visitEnd();
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.MethodVisitor
    public void visitFieldInsn(int i, String str, String str2, String str3) {
        this.mv.visitFieldInsn(i, str, str2, str3);
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.MethodVisitor
    public void visitFrame(int i, int i2, Object[] objArr, int i3, Object[] objArr2) {
        this.mv.visitFrame(i, i2, objArr, i3, objArr2);
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.MethodVisitor
    public void visitIincInsn(int i, int i2) {
        this.mv.visitIincInsn(i, i2);
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.MethodVisitor
    public void visitInsn(int i) {
        this.mv.visitInsn(i);
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.MethodVisitor
    public void visitIntInsn(int i, int i2) {
        this.mv.visitIntInsn(i, i2);
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.MethodVisitor
    public void visitJumpInsn(int i, Label label) {
        this.mv.visitJumpInsn(i, label);
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.MethodVisitor
    public void visitLabel(Label label) {
        this.mv.visitLabel(label);
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.MethodVisitor
    public void visitLdcInsn(Object obj) {
        this.mv.visitLdcInsn(obj);
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.MethodVisitor
    public void visitLineNumber(int i, Label label) {
        this.mv.visitLineNumber(i, label);
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.MethodVisitor
    public void visitLocalVariable(String str, String str2, String str3, Label label, Label label2, int i) {
        this.mv.visitLocalVariable(str, str2, str3, label, label2, i);
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.MethodVisitor
    public void visitLookupSwitchInsn(Label label, int[] iArr, Label[] labelArr) {
        this.mv.visitLookupSwitchInsn(label, iArr, labelArr);
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.MethodVisitor
    public void visitMaxs(int i, int i2) {
        this.mv.visitMaxs(i, i2);
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.MethodVisitor
    public void visitMethodInsn(int i, String str, String str2, String str3) {
        this.mv.visitMethodInsn(i, str, str2, str3);
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.MethodVisitor
    public void visitMultiANewArrayInsn(String str, int i) {
        this.mv.visitMultiANewArrayInsn(str, i);
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitParameterAnnotation(int i, String str, boolean z) {
        return this.mv.visitParameterAnnotation(i, str, z);
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.MethodVisitor
    public void visitTableSwitchInsn(int i, int i2, Label label, Label[] labelArr) {
        this.mv.visitTableSwitchInsn(i, i2, label, labelArr);
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.MethodVisitor
    public void visitTryCatchBlock(Label label, Label label2, Label label3, String str) {
        this.mv.visitTryCatchBlock(label, label2, label3, str);
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.MethodVisitor
    public void visitTypeInsn(int i, String str) {
        this.mv.visitTypeInsn(i, str);
    }

    @Override // org.codehaus.jackson.org.objectweb.asm.MethodVisitor
    public void visitVarInsn(int i, int i2) {
        this.mv.visitVarInsn(i, i2);
    }
}
