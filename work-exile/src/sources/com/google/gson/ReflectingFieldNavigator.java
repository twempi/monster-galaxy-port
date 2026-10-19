package com.google.gson;

import com.google.gson.internal.C$Gson$Preconditions;
import com.google.gson.internal.C$Gson$Types;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import twitter4j.internal.http.HttpResponseCode;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
final class ReflectingFieldNavigator {
    private static final Cache<Type, List<FieldAttributes>> fieldsCache = new LruCache(HttpResponseCode.INTERNAL_SERVER_ERROR);
    private final ExclusionStrategy exclusionStrategy;

    ReflectingFieldNavigator(ExclusionStrategy exclusionStrategy) {
        this.exclusionStrategy = (ExclusionStrategy) C$Gson$Preconditions.checkNotNull(exclusionStrategy);
    }

    void visitFieldsReflectively(ObjectTypePair objTypePair, ObjectNavigator.Visitor visitor) {
        Type moreSpecificType = objTypePair.getMoreSpecificType();
        Object obj = objTypePair.getObject();
        for (FieldAttributes fieldAttributes : getAllFields(moreSpecificType, objTypePair.getType())) {
            if (!this.exclusionStrategy.shouldSkipField(fieldAttributes) && !this.exclusionStrategy.shouldSkipClass(fieldAttributes.getDeclaredClass())) {
                Type resolvedTypeOfField = fieldAttributes.getResolvedType();
                boolean visitedWithCustomHandler = visitor.visitFieldUsingCustomHandler(fieldAttributes, resolvedTypeOfField, obj);
                if (!visitedWithCustomHandler) {
                    if (C$Gson$Types.isArray(resolvedTypeOfField)) {
                        visitor.visitArrayField(fieldAttributes, resolvedTypeOfField, obj);
                    } else {
                        visitor.visitObjectField(fieldAttributes, resolvedTypeOfField, obj);
                    }
                }
            }
        }
    }

    private List<FieldAttributes> getAllFields(Type type, Type declaredType) {
        List<FieldAttributes> fields = fieldsCache.getElement(type);
        if (fields == null) {
            fields = new ArrayList<>();
            for (Class<?> curr : getInheritanceHierarchy(type)) {
                Field[] currentClazzFields = curr.getDeclaredFields();
                AccessibleObject.setAccessible(currentClazzFields, true);
                for (Field f : currentClazzFields) {
                    fields.add(new FieldAttributes(curr, f, declaredType));
                }
            }
            fieldsCache.addElement(type, fields);
        }
        return fields;
    }

    private List<Class<?>> getInheritanceHierarchy(Type type) {
        List<Class<?>> classes = new ArrayList<>();
        Class<?> topLevelClass = C$Gson$Types.getRawType(type);
        for (Class<?> curr = topLevelClass; curr != null && !curr.equals(Object.class); curr = curr.getSuperclass()) {
            if (!curr.isSynthetic()) {
                classes.add(curr);
            }
        }
        return classes;
    }
}
