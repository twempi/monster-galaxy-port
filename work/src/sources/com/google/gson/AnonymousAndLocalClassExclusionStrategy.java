package com.google.gson;

/* JADX INFO: loaded from: classes.dex */
final class AnonymousAndLocalClassExclusionStrategy implements ExclusionStrategy {
    AnonymousAndLocalClassExclusionStrategy() {
    }

    @Override // com.google.gson.ExclusionStrategy
    public boolean shouldSkipField(FieldAttributes f) {
        return isAnonymousOrLocal(f.getDeclaredClass());
    }

    @Override // com.google.gson.ExclusionStrategy
    public boolean shouldSkipClass(Class<?> clazz) {
        return isAnonymousOrLocal(clazz);
    }

    private boolean isAnonymousOrLocal(Class<?> clazz) {
        return !Enum.class.isAssignableFrom(clazz) && (clazz.isAnonymousClass() || clazz.isLocalClass());
    }
}
