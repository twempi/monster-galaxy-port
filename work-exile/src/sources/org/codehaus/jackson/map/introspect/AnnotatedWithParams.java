package org.codehaus.jackson.map.introspect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import org.codehaus.jackson.map.type.TypeBindings;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public abstract class AnnotatedWithParams extends AnnotatedMember {
    protected final AnnotationMap _annotations;
    protected final AnnotationMap[] _paramAnnotations;

    public abstract AnnotatedParameter getParameter(int i);

    public abstract Class<?> getParameterClass(int i);

    public abstract int getParameterCount();

    public abstract Type getParameterType(int i);

    protected AnnotatedWithParams(AnnotationMap classAnn, AnnotationMap[] paramAnnotations) {
        this._annotations = classAnn;
        this._paramAnnotations = paramAnnotations;
    }

    public final void addOrOverride(Annotation a) {
        this._annotations.add(a);
    }

    public final void addOrOverrideParam(int paramIndex, Annotation a) {
        AnnotationMap old = this._paramAnnotations[paramIndex];
        if (old == null) {
            old = new AnnotationMap();
            this._paramAnnotations[paramIndex] = old;
        }
        old.add(a);
    }

    public final void addIfNotPresent(Annotation a) {
        this._annotations.addIfNotPresent(a);
    }

    protected JavaType getType(TypeBindings bindings, TypeVariable<?>[] typeParams) {
        if (typeParams != null && typeParams.length > 0) {
            bindings = bindings.childInstance();
            for (TypeVariable<?> var : typeParams) {
                String name = var.getName();
                bindings._addPlaceholder(name);
                Type lowerBound = var.getBounds()[0];
                JavaType type = lowerBound == null ? TypeFactory.unknownType() : bindings.resolveType(lowerBound);
                bindings.addBinding(var.getName(), type);
            }
        }
        return bindings.resolveType(getGenericType());
    }

    @Override // org.codehaus.jackson.map.introspect.Annotated
    public final <A extends Annotation> A getAnnotation(Class<A> cls) {
        return (A) this._annotations.get(cls);
    }

    public final AnnotationMap getParameterAnnotations(int index) {
        if (this._paramAnnotations == null || index < 0 || index > this._paramAnnotations.length) {
            return null;
        }
        return this._paramAnnotations[index];
    }

    public final JavaType resolveParameterType(int index, TypeBindings bindings) {
        return bindings.resolveType(getParameterType(index));
    }

    public final int getAnnotationCount() {
        return this._annotations.size();
    }
}
