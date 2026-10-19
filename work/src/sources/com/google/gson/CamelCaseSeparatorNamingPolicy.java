package com.google.gson;

import com.google.gson.internal.C$Gson$Preconditions;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collection;

/* JADX INFO: loaded from: classes.dex */
final class CamelCaseSeparatorNamingPolicy extends RecursiveFieldNamingPolicy {
    private final String separatorString;

    public CamelCaseSeparatorNamingPolicy(String separatorString) {
        C$Gson$Preconditions.checkNotNull(separatorString);
        C$Gson$Preconditions.checkArgument(!"".equals(separatorString));
        this.separatorString = separatorString;
    }

    @Override // com.google.gson.RecursiveFieldNamingPolicy
    protected String translateName(String target, Type fieldType, Collection<Annotation> annnotations) {
        StringBuilder translation = new StringBuilder();
        for (int i = 0; i < target.length(); i++) {
            char character = target.charAt(i);
            if (Character.isUpperCase(character) && translation.length() != 0) {
                translation.append(this.separatorString);
            }
            translation.append(character);
        }
        return translation.toString();
    }
}
