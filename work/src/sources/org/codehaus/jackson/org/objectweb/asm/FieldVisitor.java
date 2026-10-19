package org.codehaus.jackson.org.objectweb.asm;

/* JADX INFO: loaded from: classes.dex */
public interface FieldVisitor {
    AnnotationVisitor visitAnnotation(String str, boolean z);

    void visitAttribute(Attribute attribute);

    void visitEnd();
}
