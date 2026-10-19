package com.google.gson;

/* JADX INFO: loaded from: classes.dex */
final class NullExclusionStrategy implements ExclusionStrategy {
    NullExclusionStrategy() {
    }

    @Override // com.google.gson.ExclusionStrategy
    public boolean shouldSkipField(FieldAttributes f) {
        return false;
    }

    @Override // com.google.gson.ExclusionStrategy
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }
}
