package com.google.gson;

import com.google.gson.internal.C$Gson$Types;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes.dex */
final class ObjectNavigator {
    private final ExclusionStrategy exclusionStrategy;
    private final ReflectingFieldNavigator reflectingFieldNavigator;

    public interface Visitor {
        void end(ObjectTypePair objectTypePair);

        Object getTarget();

        void start(ObjectTypePair objectTypePair);

        void startVisitingObject(Object obj);

        void visitArray(Object obj, Type type);

        void visitArrayField(FieldAttributes fieldAttributes, Type type, Object obj);

        boolean visitFieldUsingCustomHandler(FieldAttributes fieldAttributes, Type type, Object obj);

        void visitObjectField(FieldAttributes fieldAttributes, Type type, Object obj);

        void visitPrimitive(Object obj);

        boolean visitUsingCustomHandler(ObjectTypePair objectTypePair);
    }

    ObjectNavigator(ExclusionStrategy strategy) {
        this.exclusionStrategy = strategy == null ? new NullExclusionStrategy() : strategy;
        this.reflectingFieldNavigator = new ReflectingFieldNavigator(this.exclusionStrategy);
    }

    public void accept(ObjectTypePair objTypePair, Visitor visitor) {
        if (!this.exclusionStrategy.shouldSkipClass(C$Gson$Types.getRawType(objTypePair.type))) {
            boolean visitedWithCustomHandler = visitor.visitUsingCustomHandler(objTypePair);
            if (!visitedWithCustomHandler) {
                Object obj = objTypePair.getObject();
                Object objectToVisit = obj == null ? visitor.getTarget() : obj;
                if (objectToVisit != null) {
                    objTypePair.setObject(objectToVisit);
                    visitor.start(objTypePair);
                    try {
                        if (C$Gson$Types.isArray(objTypePair.type)) {
                            visitor.visitArray(objectToVisit, objTypePair.type);
                        } else if (objTypePair.type == Object.class && isPrimitiveOrString(objectToVisit)) {
                            visitor.visitPrimitive(objectToVisit);
                            visitor.getTarget();
                        } else {
                            visitor.startVisitingObject(objectToVisit);
                            this.reflectingFieldNavigator.visitFieldsReflectively(objTypePair, visitor);
                        }
                    } finally {
                        visitor.end(objTypePair);
                    }
                }
            }
        }
    }

    private static boolean isPrimitiveOrString(Object objectToVisit) {
        Class<?> realClazz = objectToVisit.getClass();
        return realClazz == Object.class || realClazz == String.class || Primitives.unwrap(realClazz).isPrimitive();
    }
}
