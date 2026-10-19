package com.google.gson;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
final class ModifierBasedExclusionStrategy implements ExclusionStrategy {
    private final Collection<Integer> modifiers = new HashSet();

    public ModifierBasedExclusionStrategy(int... modifiers) {
        if (modifiers != null) {
            for (int modifier : modifiers) {
                this.modifiers.add(Integer.valueOf(modifier));
            }
        }
    }

    @Override // com.google.gson.ExclusionStrategy
    public boolean shouldSkipField(FieldAttributes f) {
        Iterator<Integer> it = this.modifiers.iterator();
        while (it.hasNext()) {
            int modifier = it.next().intValue();
            if (f.hasModifier(modifier)) {
                return true;
            }
        }
        return false;
    }

    @Override // com.google.gson.ExclusionStrategy
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }
}
