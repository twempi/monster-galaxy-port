package com.google.gson;

import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes.dex */
final class JsonObjectDeserializationVisitor<T> extends JsonDeserializationVisitor<T> {
    JsonObjectDeserializationVisitor(JsonElement json, Type type, ObjectNavigator objectNavigator, FieldNamingStrategy2 fieldNamingPolicy, ObjectConstructor objectConstructor, ParameterizedTypeHandlerMap<JsonDeserializer<?>> deserializers, JsonDeserializationContext context) {
        super(json, type, objectNavigator, fieldNamingPolicy, objectConstructor, deserializers, context);
    }

    @Override // com.google.gson.JsonDeserializationVisitor
    protected T constructTarget() {
        return (T) this.objectConstructor.construct(this.targetType);
    }

    @Override // com.google.gson.ObjectNavigator.Visitor
    public void startVisitingObject(Object node) {
    }

    @Override // com.google.gson.ObjectNavigator.Visitor
    public void visitArray(Object array, Type componentType) {
        throw new JsonParseException("Expecting object but found array: " + array);
    }

    @Override // com.google.gson.ObjectNavigator.Visitor
    public void visitObjectField(FieldAttributes f, Type typeOfF, Object obj) {
        try {
            if (!this.json.isJsonObject()) {
                throw new JsonParseException("Expecting object found: " + this.json);
            }
            JsonObject jsonObject = this.json.getAsJsonObject();
            String fName = getFieldName(f);
            JsonElement jsonChild = jsonObject.get(fName);
            if (jsonChild != null) {
                Object child = visitChildAsObject(typeOfF, jsonChild);
                f.set(obj, child);
            } else {
                f.set(obj, null);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override // com.google.gson.ObjectNavigator.Visitor
    public void visitArrayField(FieldAttributes f, Type typeOfF, Object obj) {
        try {
            if (!this.json.isJsonObject()) {
                throw new JsonParseException("Expecting object found: " + this.json);
            }
            JsonObject jsonObject = this.json.getAsJsonObject();
            String fName = getFieldName(f);
            JsonArray jsonChild = (JsonArray) jsonObject.get(fName);
            if (jsonChild != null) {
                Object array = visitChildAsArray(typeOfF, jsonChild);
                f.set(obj, array);
            } else {
                f.set(obj, null);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private String getFieldName(FieldAttributes f) {
        return this.fieldNamingPolicy.translateName(f);
    }

    @Override // com.google.gson.ObjectNavigator.Visitor
    public boolean visitFieldUsingCustomHandler(FieldAttributes f, Type declaredTypeOfField, Object parent) {
        try {
            String fName = getFieldName(f);
            if (!this.json.isJsonObject()) {
                throw new JsonParseException("Expecting object found: " + this.json);
            }
            JsonElement child = this.json.getAsJsonObject().get(fName);
            boolean isPrimitive = Primitives.isPrimitive(declaredTypeOfField);
            if (child == null) {
                return true;
            }
            if (child.isJsonNull()) {
                if (isPrimitive) {
                    return true;
                }
                f.set(parent, null);
                return true;
            }
            ObjectTypePair objTypePair = new ObjectTypePair(null, declaredTypeOfField, false);
            Pair<JsonDeserializer<?>, ObjectTypePair> pair = objTypePair.getMatchingHandler(this.deserializers);
            if (pair == null) {
                return false;
            }
            Object value = invokeCustomDeserializer(child, pair);
            if (value == null && isPrimitive) {
                return true;
            }
            f.set(parent, value);
            return true;
        } catch (IllegalAccessException e) {
            throw new RuntimeException();
        }
    }

    @Override // com.google.gson.ObjectNavigator.Visitor
    public void visitPrimitive(Object obj) {
        if (!this.json.isJsonPrimitive()) {
            throw new JsonParseException("Type information is unavailable, and the target object is not a primitive: " + this.json);
        }
        this.target = (T) this.json.getAsJsonPrimitive().getAsObject();
    }
}
