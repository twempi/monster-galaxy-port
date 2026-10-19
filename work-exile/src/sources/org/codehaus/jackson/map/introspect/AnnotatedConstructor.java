package org.codehaus.jackson.map.introspect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import org.codehaus.jackson.map.type.TypeBindings;
import org.codehaus.jackson.type.JavaType;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class AnnotatedConstructor extends AnnotatedWithParams {
    protected final Constructor<?> _constructor;

    public AnnotatedConstructor(Constructor<?> constructor, AnnotationMap classAnn, AnnotationMap[] paramAnn) {
        super(classAnn, paramAnn);
        if (constructor == null) {
            throw new IllegalArgumentException("Null constructor not allowed");
        }
        this._constructor = constructor;
    }

    @Override // org.codehaus.jackson.map.introspect.Annotated
    public Constructor<?> getAnnotated() {
        return this._constructor;
    }

    @Override // org.codehaus.jackson.map.introspect.Annotated
    public int getModifiers() {
        return this._constructor.getModifiers();
    }

    @Override // org.codehaus.jackson.map.introspect.Annotated
    public String getName() {
        return this._constructor.getName();
    }

    @Override // org.codehaus.jackson.map.introspect.Annotated
    public Type getGenericType() {
        return getRawType();
    }

    @Override // org.codehaus.jackson.map.introspect.Annotated
    public Class<?> getRawType() {
        return this._constructor.getDeclaringClass();
    }

    @Override // org.codehaus.jackson.map.introspect.Annotated
    public JavaType getType(TypeBindings bindings) {
        return getType(bindings, this._constructor.getTypeParameters());
    }

    @Override // org.codehaus.jackson.map.introspect.AnnotatedWithParams
    public AnnotatedParameter getParameter(int index) {
        return new AnnotatedParameter(this, getParameterType(index), this._paramAnnotations[index]);
    }

    @Override // org.codehaus.jackson.map.introspect.AnnotatedWithParams
    public int getParameterCount() {
        return this._constructor.getParameterTypes().length;
    }

    @Override // org.codehaus.jackson.map.introspect.AnnotatedWithParams
    public Class<?> getParameterClass(int index) {
        Class<?>[] types = this._constructor.getParameterTypes();
        if (index >= types.length) {
            return null;
        }
        return types[index];
    }

    @Override // org.codehaus.jackson.map.introspect.AnnotatedWithParams
    public Type getParameterType(int index) {
        Type[] types = this._constructor.getGenericParameterTypes();
        if (index >= types.length) {
            return null;
        }
        return types[index];
    }

    @Override // org.codehaus.jackson.map.introspect.AnnotatedMember
    public Class<?> getDeclaringClass() {
        return this._constructor.getDeclaringClass();
    }

    @Override // org.codehaus.jackson.map.introspect.AnnotatedMember
    public Member getMember() {
        return this._constructor;
    }

    public String toString() {
        return "[constructor for " + getName() + ", annotations: " + this._annotations + "]";
    }
}
