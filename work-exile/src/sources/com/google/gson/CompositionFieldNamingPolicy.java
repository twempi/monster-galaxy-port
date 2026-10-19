package com.google.gson;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collection;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
abstract class CompositionFieldNamingPolicy extends RecursiveFieldNamingPolicy {
    private final RecursiveFieldNamingPolicy[] fieldPolicies;

    public CompositionFieldNamingPolicy(RecursiveFieldNamingPolicy... fieldNamingPolicies) {
        if (fieldNamingPolicies == null) {
            throw new NullPointerException("naming policies can not be null.");
        }
        this.fieldPolicies = fieldNamingPolicies;
    }

    @Override // com.google.gson.RecursiveFieldNamingPolicy
    protected String translateName(String target, Type fieldType, Collection<Annotation> annotations) {
        RecursiveFieldNamingPolicy[] arr$ = this.fieldPolicies;
        for (RecursiveFieldNamingPolicy policy : arr$) {
            target = policy.translateName(target, fieldType, annotations);
        }
        return target;
    }
}
