package com.google.gson;

import com.google.gson.internal.C$Gson$Types;
import java.lang.reflect.Array;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes.dex */
final class JsonArrayDeserializationVisitor<T> extends JsonDeserializationVisitor<T> {
    JsonArrayDeserializationVisitor(JsonArray jsonArray, Type arrayType, ObjectNavigator objectNavigator, FieldNamingStrategy2 fieldNamingPolicy, ObjectConstructor objectConstructor, ParameterizedTypeHandlerMap<JsonDeserializer<?>> deserializers, JsonDeserializationContext context) {
        super(jsonArray, arrayType, objectNavigator, fieldNamingPolicy, objectConstructor, deserializers, context);
    }

    @Override // com.google.gson.JsonDeserializationVisitor
    protected T constructTarget() {
        if (this.json.isJsonArray()) {
            return C$Gson$Types.isArray(this.targetType) ? (T) this.objectConstructor.constructArray(C$Gson$Types.getArrayComponentType(this.targetType), this.json.getAsJsonArray().size()) : (T) this.objectConstructor.construct(C$Gson$Types.getRawType(this.targetType));
        }
        throw new JsonParseException("Expecting array found: " + this.json);
    }

    @Override // com.google.gson.ObjectNavigator.Visitor
    public void visitArray(Object array, Type arrayType) {
        Object objVisitChildAsObject;
        if (!this.json.isJsonArray()) {
            throw new JsonParseException("Expecting array found: " + this.json);
        }
        JsonArray jsonArray = this.json.getAsJsonArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonElement jsonChild = jsonArray.get(i);
            if (jsonChild == null || jsonChild.isJsonNull()) {
                objVisitChildAsObject = null;
            } else if (jsonChild instanceof JsonObject) {
                objVisitChildAsObject = visitChildAsObject(C$Gson$Types.getArrayComponentType(arrayType), jsonChild);
            } else if (jsonChild instanceof JsonArray) {
                objVisitChildAsObject = visitChildAsArray(C$Gson$Types.getArrayComponentType(arrayType), jsonChild.getAsJsonArray());
            } else if (jsonChild instanceof JsonPrimitive) {
                objVisitChildAsObject = visitChildAsObject(C$Gson$Types.getArrayComponentType(arrayType), jsonChild.getAsJsonPrimitive());
            } else {
                throw new IllegalStateException();
            }
            Array.set(array, i, objVisitChildAsObject);
        }
    }

    @Override // com.google.gson.ObjectNavigator.Visitor
    public void startVisitingObject(Object node) {
        throw new JsonParseException("Expecting array but found object: " + node);
    }

    @Override // com.google.gson.ObjectNavigator.Visitor
    public void visitArrayField(FieldAttributes f, Type typeOfF, Object obj) {
        throw new JsonParseException("Expecting array but found array field " + f.getName() + ": " + obj);
    }

    @Override // com.google.gson.ObjectNavigator.Visitor
    public void visitObjectField(FieldAttributes f, Type typeOfF, Object obj) {
        throw new JsonParseException("Expecting array but found object field " + f.getName() + ": " + obj);
    }

    @Override // com.google.gson.ObjectNavigator.Visitor
    public boolean visitFieldUsingCustomHandler(FieldAttributes f, Type actualTypeOfField, Object parent) {
        throw new JsonParseException("Expecting array but found field " + f.getName() + ": " + parent);
    }

    @Override // com.google.gson.ObjectNavigator.Visitor
    public void visitPrimitive(Object primitive) {
        throw new JsonParseException("Type information is unavailable, and the target is not a primitive: " + this.json);
    }
}
