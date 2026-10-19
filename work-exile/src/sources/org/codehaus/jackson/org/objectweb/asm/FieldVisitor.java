package org.codehaus.jackson.org.objectweb.asm;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface FieldVisitor {
    AnnotationVisitor visitAnnotation(String str, boolean z);

    void visitAttribute(Attribute attribute);

    void visitEnd();
}
