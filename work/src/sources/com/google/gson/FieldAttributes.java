package com.google.gson;

import com.google.gson.internal.C$Gson$Preconditions;
import com.google.gson.internal.C$Gson$Types;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import org.codehaus.jackson.util.BufferRecycler;

/* JADX INFO: loaded from: classes.dex */
public final class FieldAttributes {
    private static final Cache<Pair<Class<?>, String>, Collection<Annotation>> ANNOTATION_CACHE = new LruCache(getMaxCacheSize());
    private static final String MAX_CACHE_PROPERTY_NAME = "com.google.gson.annotation_cache_size_hint";
    private Collection<Annotation> annotations;
    private final Class<?> declaredType;
    private final Class<?> declaringClazz;
    private final Field field;
    private Type genericType;
    private final boolean isSynthetic;
    private final int modifiers;
    private final String name;
    private final Type resolvedType;

    FieldAttributes(Class<?> declaringClazz, Field f, Type declaringType) {
        this.declaringClazz = (Class) C$Gson$Preconditions.checkNotNull(declaringClazz);
        this.name = f.getName();
        this.declaredType = f.getType();
        this.isSynthetic = f.isSynthetic();
        this.modifiers = f.getModifiers();
        this.field = f;
        this.resolvedType = getTypeInfoForField(f, declaringType);
    }

    private static int getMaxCacheSize() {
        try {
            String propertyValue = System.getProperty(MAX_CACHE_PROPERTY_NAME, String.valueOf(BufferRecycler.DEFAULT_WRITE_CONCAT_BUFFER_LEN));
            return Integer.parseInt(propertyValue);
        } catch (NumberFormatException e) {
            return BufferRecycler.DEFAULT_WRITE_CONCAT_BUFFER_LEN;
        }
    }

    public Class<?> getDeclaringClass() {
        return this.declaringClazz;
    }

    public String getName() {
        return this.name;
    }

    public Type getDeclaredType() {
        if (this.genericType == null) {
            this.genericType = this.field.getGenericType();
        }
        return this.genericType;
    }

    public Class<?> getDeclaredClass() {
        return this.declaredType;
    }

    public <T extends Annotation> T getAnnotation(Class<T> cls) {
        return (T) getAnnotationFromArray(getAnnotations(), cls);
    }

    public Collection<Annotation> getAnnotations() {
        if (this.annotations == null) {
            Pair<Class<?>, String> key = new Pair<>(this.declaringClazz, this.name);
            this.annotations = ANNOTATION_CACHE.getElement(key);
            if (this.annotations == null) {
                this.annotations = Collections.unmodifiableCollection(Arrays.asList(this.field.getAnnotations()));
                ANNOTATION_CACHE.addElement(key, this.annotations);
            }
        }
        return this.annotations;
    }

    public boolean hasModifier(int modifier) {
        return (this.modifiers & modifier) != 0;
    }

    void set(Object instance, Object value) throws IllegalAccessException {
        this.field.set(instance, value);
    }

    Object get(Object instance) throws IllegalAccessException {
        return this.field.get(instance);
    }

    boolean isSynthetic() {
        return this.isSynthetic;
    }

    @Deprecated
    Field getFieldObject() {
        return this.field;
    }

    Type getResolvedType() {
        return this.resolvedType;
    }

    private static <T extends Annotation> T getAnnotationFromArray(Collection<Annotation> annotations, Class<T> annotation) {
        Iterator<Annotation> it = annotations.iterator();
        while (it.hasNext()) {
            T t = (T) it.next();
            if (t.annotationType() == annotation) {
                return t;
            }
        }
        return null;
    }

    static Type getTypeInfoForField(Field f, Type typeDefiningF) {
        Class<?> rawType = C$Gson$Types.getRawType(typeDefiningF);
        return !f.getDeclaringClass().isAssignableFrom(rawType) ? f.getGenericType() : C$Gson$Types.resolve(typeDefiningF, rawType, f.getGenericType());
    }
}
