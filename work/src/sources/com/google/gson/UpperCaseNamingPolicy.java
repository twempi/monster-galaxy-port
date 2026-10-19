package com.google.gson;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collection;

/* JADX INFO: loaded from: classes.dex */
final class UpperCaseNamingPolicy extends RecursiveFieldNamingPolicy {
    UpperCaseNamingPolicy() {
    }

    @Override // com.google.gson.RecursiveFieldNamingPolicy
    protected String translateName(String target, Type fieldType, Collection<Annotation> annotations) {
        return target.toUpperCase();
    }
}
