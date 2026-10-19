package com.google.gson;

import com.google.gson.annotations.SerializedName;

/* JADX INFO: loaded from: classes.dex */
final class SerializedNameAnnotationInterceptingNamingPolicy implements FieldNamingStrategy2 {
    private final FieldNamingStrategy2 delegate;

    SerializedNameAnnotationInterceptingNamingPolicy(FieldNamingStrategy2 delegate) {
        this.delegate = delegate;
    }

    @Override // com.google.gson.FieldNamingStrategy2
    public String translateName(FieldAttributes f) {
        SerializedName serializedName = (SerializedName) f.getAnnotation(SerializedName.class);
        return serializedName == null ? this.delegate.translateName(f) : serializedName.value();
    }
}
