package com.google.gson;

import com.google.gson.internal.C$Gson$Preconditions;
import java.util.Collection;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
final class DisjunctionExclusionStrategy implements ExclusionStrategy {
    private final Collection<ExclusionStrategy> strategies;

    DisjunctionExclusionStrategy(Collection<ExclusionStrategy> strategies) {
        this.strategies = (Collection) C$Gson$Preconditions.checkNotNull(strategies);
    }

    @Override // com.google.gson.ExclusionStrategy
    public boolean shouldSkipField(FieldAttributes f) {
        for (ExclusionStrategy strategy : this.strategies) {
            if (strategy.shouldSkipField(f)) {
                return true;
            }
        }
        return false;
    }

    @Override // com.google.gson.ExclusionStrategy
    public boolean shouldSkipClass(Class<?> clazz) {
        for (ExclusionStrategy strategy : this.strategies) {
            if (strategy.shouldSkipClass(clazz)) {
                return true;
            }
        }
        return false;
    }
}
