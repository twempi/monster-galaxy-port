package com.badlogic.gdx.utils;

import java.lang.reflect.Constructor;

/* JADX INFO: loaded from: classes.dex */
public class ReflectionPool<T> extends Pool<T> {
    private final Class<T> type;

    public ReflectionPool(Class<T> type) {
        this.type = type;
    }

    public ReflectionPool(Class<T> type, int initialCapacity, int max) {
        super(initialCapacity, max);
        this.type = type;
    }

    public ReflectionPool(Class<T> type, int initialCapacity) {
        super(initialCapacity);
        this.type = type;
    }

    @Override // com.badlogic.gdx.utils.Pool
    protected T newObject() {
        Constructor<T> declaredConstructor;
        try {
            return this.type.newInstance();
        } catch (Exception ex) {
            try {
                declaredConstructor = this.type.getConstructor((Class[]) null);
            } catch (Exception e) {
                try {
                    declaredConstructor = this.type.getDeclaredConstructor((Class[]) null);
                    declaredConstructor.setAccessible(true);
                } catch (NoSuchMethodException e2) {
                    throw new RuntimeException("Class cannot be created (missing no-arg constructor): " + this.type.getName());
                }
            }
            try {
                return declaredConstructor.newInstance(new Object[0]);
            } catch (Exception e3) {
                throw new GdxRuntimeException("Unable to create new instance: " + this.type.getName(), ex);
            }
        }
    }
}
