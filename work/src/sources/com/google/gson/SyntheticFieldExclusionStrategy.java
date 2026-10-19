package com.google.gson;

/* JADX INFO: loaded from: classes.dex */
final class SyntheticFieldExclusionStrategy implements ExclusionStrategy {
    private final boolean skipSyntheticFields;

    SyntheticFieldExclusionStrategy(boolean skipSyntheticFields) {
        this.skipSyntheticFields = skipSyntheticFields;
    }

    @Override // com.google.gson.ExclusionStrategy
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }

    @Override // com.google.gson.ExclusionStrategy
    public boolean shouldSkipField(FieldAttributes f) {
        return this.skipSyntheticFields && f.isSynthetic();
    }
}
