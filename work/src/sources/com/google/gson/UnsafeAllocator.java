package com.google.gson;

import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
abstract class UnsafeAllocator {
    public abstract <T> T newInstance(Class<T> cls) throws Exception;

    UnsafeAllocator() {
    }

    public static UnsafeAllocator create() {
        try {
            Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
            Field f = unsafeClass.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            final Object unsafe = f.get(null);
            final Method allocateInstance = unsafeClass.getMethod("allocateInstance", Class.class);
            return new UnsafeAllocator() { // from class: com.google.gson.UnsafeAllocator.1
                @Override // com.google.gson.UnsafeAllocator
                public <T> T newInstance(Class<T> cls) throws Exception {
                    return (T) allocateInstance.invoke(unsafe, cls);
                }
            };
        } catch (Exception e) {
            try {
                final Method newInstance = ObjectInputStream.class.getDeclaredMethod("newInstance", Class.class, Class.class);
                newInstance.setAccessible(true);
                return new UnsafeAllocator() { // from class: com.google.gson.UnsafeAllocator.2
                    @Override // com.google.gson.UnsafeAllocator
                    public <T> T newInstance(Class<T> cls) throws Exception {
                        return (T) newInstance.invoke(null, cls, Object.class);
                    }
                };
            } catch (Exception e2) {
                try {
                    Method getConstructorId = ObjectStreamClass.class.getDeclaredMethod("getConstructorId", Class.class);
                    getConstructorId.setAccessible(true);
                    final int constructorId = ((Integer) getConstructorId.invoke(null, Object.class)).intValue();
                    final Method newInstance2 = ObjectStreamClass.class.getDeclaredMethod("newInstance", Class.class, Integer.TYPE);
                    newInstance2.setAccessible(true);
                    return new UnsafeAllocator() { // from class: com.google.gson.UnsafeAllocator.3
                        @Override // com.google.gson.UnsafeAllocator
                        public <T> T newInstance(Class<T> cls) throws Exception {
                            return (T) newInstance2.invoke(null, cls, Integer.valueOf(constructorId));
                        }
                    };
                } catch (Exception e3) {
                    return new UnsafeAllocator() { // from class: com.google.gson.UnsafeAllocator.4
                        @Override // com.google.gson.UnsafeAllocator
                        public <T> T newInstance(Class<T> c) {
                            throw new UnsupportedOperationException("Cannot allocate " + c);
                        }
                    };
                }
            }
        }
    }
}
