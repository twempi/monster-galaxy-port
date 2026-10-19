package com.google.gson;

import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes.dex */
final class JsonSerializationContextDefault implements JsonSerializationContext {
    private final MemoryRefStack ancestors = new MemoryRefStack();
    private final FieldNamingStrategy2 fieldNamingPolicy;
    private final ObjectNavigator objectNavigator;
    private final boolean serializeNulls;
    private final ParameterizedTypeHandlerMap<JsonSerializer<?>> serializers;

    JsonSerializationContextDefault(ObjectNavigator objectNavigator, FieldNamingStrategy2 fieldNamingPolicy, boolean serializeNulls, ParameterizedTypeHandlerMap<JsonSerializer<?>> serializers) {
        this.objectNavigator = objectNavigator;
        this.fieldNamingPolicy = fieldNamingPolicy;
        this.serializeNulls = serializeNulls;
        this.serializers = serializers;
    }

    @Override // com.google.gson.JsonSerializationContext
    public JsonElement serialize(Object src) {
        return src == null ? JsonNull.createJsonNull() : serialize(src, src.getClass(), false);
    }

    @Override // com.google.gson.JsonSerializationContext
    public JsonElement serialize(Object src, Type typeOfSrc) {
        return serialize(src, typeOfSrc, true);
    }

    JsonElement serialize(Object src, Type typeOfSrc, boolean preserveType) {
        if (src == null) {
            return JsonNull.createJsonNull();
        }
        JsonSerializationVisitor visitor = new JsonSerializationVisitor(this.objectNavigator, this.fieldNamingPolicy, this.serializeNulls, this.serializers, this, this.ancestors);
        this.objectNavigator.accept(new ObjectTypePair(src, typeOfSrc, preserveType), visitor);
        return visitor.getJsonElement();
    }
}
