package org.codehaus.jackson.map.introspect;

import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import org.codehaus.jackson.map.type.TypeBindings;
import org.codehaus.jackson.type.JavaType;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class AnnotatedMethod extends AnnotatedWithParams {
    protected final Method _method;
    protected Class<?>[] _paramTypes;

    public AnnotatedMethod(Method method, AnnotationMap classAnn, AnnotationMap[] paramAnnotations) {
        super(classAnn, paramAnnotations);
        this._method = method;
    }

    public AnnotatedMethod withMethod(Method m) {
        return new AnnotatedMethod(m, this._annotations, this._paramAnnotations);
    }

    @Override // org.codehaus.jackson.map.introspect.Annotated
    public Method getAnnotated() {
        return this._method;
    }

    @Override // org.codehaus.jackson.map.introspect.Annotated
    public int getModifiers() {
        return this._method.getModifiers();
    }

    @Override // org.codehaus.jackson.map.introspect.Annotated
    public String getName() {
        return this._method.getName();
    }

    @Override // org.codehaus.jackson.map.introspect.Annotated
    public Type getGenericType() {
        return this._method.getGenericReturnType();
    }

    @Override // org.codehaus.jackson.map.introspect.Annotated
    public Class<?> getRawType() {
        return this._method.getReturnType();
    }

    @Override // org.codehaus.jackson.map.introspect.Annotated
    public JavaType getType(TypeBindings bindings) {
        return getType(bindings, this._method.getTypeParameters());
    }

    @Override // org.codehaus.jackson.map.introspect.AnnotatedMember
    public Class<?> getDeclaringClass() {
        return this._method.getDeclaringClass();
    }

    @Override // org.codehaus.jackson.map.introspect.AnnotatedMember
    public Member getMember() {
        return this._method;
    }

    @Override // org.codehaus.jackson.map.introspect.AnnotatedWithParams
    public AnnotatedParameter getParameter(int index) {
        return new AnnotatedParameter(this, getParameterType(index), this._paramAnnotations[index]);
    }

    @Override // org.codehaus.jackson.map.introspect.AnnotatedWithParams
    public int getParameterCount() {
        return getParameterTypes().length;
    }

    public Type[] getParameterTypes() {
        return this._method.getGenericParameterTypes();
    }

    @Override // org.codehaus.jackson.map.introspect.AnnotatedWithParams
    public Class<?> getParameterClass(int index) {
        Class<?>[] types = this._method.getParameterTypes();
        if (index >= types.length) {
            return null;
        }
        return types[index];
    }

    @Override // org.codehaus.jackson.map.introspect.AnnotatedWithParams
    public Type getParameterType(int index) {
        Type[] types = this._method.getGenericParameterTypes();
        if (index >= types.length) {
            return null;
        }
        return types[index];
    }

    public Class<?>[] getParameterClasses() {
        if (this._paramTypes == null) {
            this._paramTypes = this._method.getParameterTypes();
        }
        return this._paramTypes;
    }

    public String getFullName() {
        return getDeclaringClass().getName() + "#" + getName() + "(" + getParameterCount() + " params)";
    }

    public String toString() {
        return "[method " + getName() + ", annotations: " + this._annotations + "]";
    }
}
