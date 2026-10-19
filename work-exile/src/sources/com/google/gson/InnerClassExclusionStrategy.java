package com.google.gson;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
final class InnerClassExclusionStrategy implements ExclusionStrategy {
    InnerClassExclusionStrategy() {
    }

    @Override // com.google.gson.ExclusionStrategy
    public boolean shouldSkipField(FieldAttributes f) {
        return isInnerClass(f.getDeclaredClass());
    }

    @Override // com.google.gson.ExclusionStrategy
    public boolean shouldSkipClass(Class<?> clazz) {
        return isInnerClass(clazz);
    }

    private boolean isInnerClass(Class<?> clazz) {
        return clazz.isMemberClass() && !isStatic(clazz);
    }

    private boolean isStatic(Class<?> clazz) {
        return (clazz.getModifiers() & 8) != 0;
    }
}
