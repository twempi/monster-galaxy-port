package com.google.gson;

import com.google.gson.internal.C$Gson$Preconditions;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes.dex */
abstract class JsonDeserializationVisitor<T> implements ObjectNavigator.Visitor {
    protected boolean constructed = false;
    protected final JsonDeserializationContext context;
    protected final ParameterizedTypeHandlerMap<JsonDeserializer<?>> deserializers;
    protected final FieldNamingStrategy2 fieldNamingPolicy;
    protected final JsonElement json;
    protected final ObjectConstructor objectConstructor;
    protected final ObjectNavigator objectNavigator;
    protected T target;
    protected final Type targetType;

    protected abstract T constructTarget();

    JsonDeserializationVisitor(JsonElement json, Type targetType, ObjectNavigator objectNavigator, FieldNamingStrategy2 fieldNamingPolicy, ObjectConstructor objectConstructor, ParameterizedTypeHandlerMap<JsonDeserializer<?>> deserializers, JsonDeserializationContext context) {
        this.targetType = targetType;
        this.objectNavigator = objectNavigator;
        this.fieldNamingPolicy = fieldNamingPolicy;
        this.objectConstructor = objectConstructor;
        this.deserializers = deserializers;
        this.json = (JsonElement) C$Gson$Preconditions.checkNotNull(json);
        this.context = context;
    }

    @Override // com.google.gson.ObjectNavigator.Visitor
    public T getTarget() {
        if (!this.constructed) {
            this.target = constructTarget();
            this.constructed = true;
        }
        return this.target;
    }

    @Override // com.google.gson.ObjectNavigator.Visitor
    public void start(ObjectTypePair node) {
    }

    @Override // com.google.gson.ObjectNavigator.Visitor
    public void end(ObjectTypePair node) {
    }

    @Override // com.google.gson.ObjectNavigator.Visitor
    public final boolean visitUsingCustomHandler(ObjectTypePair objectTypePair) {
        Pair<JsonDeserializer<?>, ObjectTypePair> matchingHandler = objectTypePair.getMatchingHandler(this.deserializers);
        if (matchingHandler == null) {
            return false;
        }
        this.target = (T) invokeCustomDeserializer(this.json, matchingHandler);
        this.constructed = true;
        return true;
    }

    protected Object invokeCustomDeserializer(JsonElement element, Pair<JsonDeserializer<?>, ObjectTypePair> pair) {
        if (element == null || element.isJsonNull()) {
            return null;
        }
        Type objType = pair.second.type;
        return pair.first.deserialize(element, objType, this.context);
    }

    final Object visitChildAsObject(Type childType, JsonElement jsonChild) {
        JsonDeserializationVisitor<?> childVisitor = new JsonObjectDeserializationVisitor<>(jsonChild, childType, this.objectNavigator, this.fieldNamingPolicy, this.objectConstructor, this.deserializers, this.context);
        return visitChild(childType, childVisitor);
    }

    final Object visitChildAsArray(Type childType, JsonArray jsonChild) {
        JsonDeserializationVisitor<?> childVisitor = new JsonArrayDeserializationVisitor<>(jsonChild.getAsJsonArray(), childType, this.objectNavigator, this.fieldNamingPolicy, this.objectConstructor, this.deserializers, this.context);
        return visitChild(childType, childVisitor);
    }

    private Object visitChild(Type type, JsonDeserializationVisitor<?> childVisitor) {
        this.objectNavigator.accept(new ObjectTypePair(null, type, false), childVisitor);
        return childVisitor.getTarget();
    }
}
