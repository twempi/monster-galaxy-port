package com.google.gson;

import com.google.gson.annotations.Expose;

/* JADX INFO: loaded from: classes.dex */
final class ExposeAnnotationDeserializationExclusionStrategy implements ExclusionStrategy {
    ExposeAnnotationDeserializationExclusionStrategy() {
    }

    @Override // com.google.gson.ExclusionStrategy
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }

    @Override // com.google.gson.ExclusionStrategy
    public boolean shouldSkipField(FieldAttributes f) {
        Expose annotation = (Expose) f.getAnnotation(Expose.class);
        return annotation == null || !annotation.deserialize();
    }
}
