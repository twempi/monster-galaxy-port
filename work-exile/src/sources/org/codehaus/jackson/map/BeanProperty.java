package org.codehaus.jackson.map;

import java.lang.annotation.Annotation;
import org.codehaus.jackson.map.introspect.AnnotatedMember;
import org.codehaus.jackson.map.util.Annotations;
import org.codehaus.jackson.type.JavaType;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface BeanProperty {
    <A extends Annotation> A getAnnotation(Class<A> cls);

    <A extends Annotation> A getContextAnnotation(Class<A> cls);

    AnnotatedMember getMember();

    String getName();

    JavaType getType();

    public static class Std implements BeanProperty {
        protected final Annotations _contextAnnotations;
        protected final AnnotatedMember _member;
        protected final String _name;
        protected final JavaType _type;

        public Std(String name, JavaType type, Annotations contextAnnotations, AnnotatedMember member) {
            this._name = name;
            this._type = type;
            this._member = member;
            this._contextAnnotations = contextAnnotations;
        }

        public Std withType(JavaType type) {
            return new Std(this._name, type, this._contextAnnotations, this._member);
        }

        @Override // org.codehaus.jackson.map.BeanProperty
        public <A extends Annotation> A getAnnotation(Class<A> cls) {
            return (A) this._member.getAnnotation(cls);
        }

        @Override // org.codehaus.jackson.map.BeanProperty
        public <A extends Annotation> A getContextAnnotation(Class<A> cls) {
            if (this._contextAnnotations == null) {
                return null;
            }
            return (A) this._contextAnnotations.get(cls);
        }

        @Override // org.codehaus.jackson.map.BeanProperty
        public String getName() {
            return this._name;
        }

        @Override // org.codehaus.jackson.map.BeanProperty
        public JavaType getType() {
            return this._type;
        }

        @Override // org.codehaus.jackson.map.BeanProperty
        public AnnotatedMember getMember() {
            return this._member;
        }
    }
}
