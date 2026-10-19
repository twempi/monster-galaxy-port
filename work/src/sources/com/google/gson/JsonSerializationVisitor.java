package com.google.gson;

import com.google.gson.internal.C$Gson$Preconditions;
import com.google.gson.internal.C$Gson$Types;
import java.lang.reflect.Array;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes.dex */
final class JsonSerializationVisitor implements ObjectNavigator.Visitor {
    private final MemoryRefStack ancestors;
    private final JsonSerializationContext context;
    private final FieldNamingStrategy2 fieldNamingPolicy;
    private final ObjectNavigator objectNavigator;
    private JsonElement root;
    private final boolean serializeNulls;
    private final ParameterizedTypeHandlerMap<JsonSerializer<?>> serializers;

    JsonSerializationVisitor(ObjectNavigator objectNavigator, FieldNamingStrategy2 fieldNamingPolicy, boolean serializeNulls, ParameterizedTypeHandlerMap<JsonSerializer<?>> serializers, JsonSerializationContext context, MemoryRefStack ancestors) {
        this.objectNavigator = objectNavigator;
        this.fieldNamingPolicy = fieldNamingPolicy;
        this.serializeNulls = serializeNulls;
        this.serializers = serializers;
        this.context = context;
        this.ancestors = ancestors;
    }

    @Override // com.google.gson.ObjectNavigator.Visitor
    public Object getTarget() {
        return null;
    }

    @Override // com.google.gson.ObjectNavigator.Visitor
    public void start(ObjectTypePair node) {
        if (node != null) {
            if (this.ancestors.contains(node)) {
                throw new CircularReferenceException(node);
            }
            this.ancestors.push(node);
        }
    }

    @Override // com.google.gson.ObjectNavigator.Visitor
    public void end(ObjectTypePair node) {
        if (node != null) {
            this.ancestors.pop();
        }
    }

    @Override // com.google.gson.ObjectNavigator.Visitor
    public void startVisitingObject(Object node) {
        assignToRoot(new JsonObject());
    }

    @Override // com.google.gson.ObjectNavigator.Visitor
    public void visitArray(Object array, Type arrayType) {
        assignToRoot(new JsonArray());
        int length = Array.getLength(array);
        Type componentType = C$Gson$Types.getArrayComponentType(arrayType);
        for (int i = 0; i < length; i++) {
            Object child = Array.get(array, i);
            addAsArrayElement(new ObjectTypePair(child, componentType, false));
        }
    }

    @Override // com.google.gson.ObjectNavigator.Visitor
    public void visitArrayField(FieldAttributes f, Type typeOfF, Object obj) {
        try {
            if (isFieldNull(f, obj)) {
                if (this.serializeNulls) {
                    addChildAsElement(f, JsonNull.createJsonNull());
                }
            } else {
                Object array = getFieldValue(f, obj);
                addAsChildOfObject(f, new ObjectTypePair(array, typeOfF, false));
            }
        } catch (CircularReferenceException e) {
            throw e.createDetailedException(f);
        }
    }

    @Override // com.google.gson.ObjectNavigator.Visitor
    public void visitObjectField(FieldAttributes f, Type typeOfF, Object obj) {
        try {
            if (isFieldNull(f, obj)) {
                if (this.serializeNulls) {
                    addChildAsElement(f, JsonNull.createJsonNull());
                }
            } else {
                Object fieldValue = getFieldValue(f, obj);
                addAsChildOfObject(f, new ObjectTypePair(fieldValue, typeOfF, false));
            }
        } catch (CircularReferenceException e) {
            throw e.createDetailedException(f);
        }
    }

    @Override // com.google.gson.ObjectNavigator.Visitor
    public void visitPrimitive(Object obj) {
        JsonElement json = obj == null ? JsonNull.createJsonNull() : new JsonPrimitive(obj);
        assignToRoot(json);
    }

    private void addAsChildOfObject(FieldAttributes f, ObjectTypePair fieldValuePair) {
        JsonElement childElement = getJsonElementForChild(fieldValuePair);
        addChildAsElement(f, childElement);
    }

    private void addChildAsElement(FieldAttributes f, JsonElement childElement) {
        this.root.getAsJsonObject().add(this.fieldNamingPolicy.translateName(f), childElement);
    }

    private void addAsArrayElement(ObjectTypePair elementTypePair) {
        if (elementTypePair.getObject() == null) {
            this.root.getAsJsonArray().add(JsonNull.createJsonNull());
        } else {
            JsonElement childElement = getJsonElementForChild(elementTypePair);
            this.root.getAsJsonArray().add(childElement);
        }
    }

    private JsonElement getJsonElementForChild(ObjectTypePair fieldValueTypePair) {
        JsonSerializationVisitor childVisitor = new JsonSerializationVisitor(this.objectNavigator, this.fieldNamingPolicy, this.serializeNulls, this.serializers, this.context, this.ancestors);
        this.objectNavigator.accept(fieldValueTypePair, childVisitor);
        return childVisitor.getJsonElement();
    }

    @Override // com.google.gson.ObjectNavigator.Visitor
    public boolean visitUsingCustomHandler(ObjectTypePair objTypePair) {
        try {
            Object obj = objTypePair.getObject();
            if (obj == null) {
                if (!this.serializeNulls) {
                    return true;
                }
                assignToRoot(JsonNull.createJsonNull());
                return true;
            }
            JsonElement element = findAndInvokeCustomSerializer(objTypePair);
            if (element != null) {
                assignToRoot(element);
                return true;
            }
            return false;
        } catch (CircularReferenceException e) {
            throw e.createDetailedException(null);
        }
    }

    private JsonElement findAndInvokeCustomSerializer(ObjectTypePair objTypePair) {
        Pair<JsonSerializer<?>, ObjectTypePair> pair = objTypePair.getMatchingHandler(this.serializers);
        if (pair == null) {
            return null;
        }
        JsonSerializer<?> serializer = pair.first;
        ObjectTypePair objTypePair2 = pair.second;
        ObjectTypePair objTypePair3 = objTypePair2;
        start(objTypePair3);
        try {
            JsonElement element = serializer.serialize(objTypePair3.getObject(), objTypePair3.getType(), this.context);
            if (element == null) {
                element = JsonNull.createJsonNull();
            }
            return element;
        } finally {
            end(objTypePair3);
        }
    }

    @Override // com.google.gson.ObjectNavigator.Visitor
    public boolean visitFieldUsingCustomHandler(FieldAttributes f, Type declaredTypeOfField, Object parent) {
        try {
            C$Gson$Preconditions.checkState(this.root.isJsonObject());
            Object obj = f.get(parent);
            if (obj == null) {
                if (!this.serializeNulls) {
                    return true;
                }
                addChildAsElement(f, JsonNull.createJsonNull());
                return true;
            }
            ObjectTypePair objTypePair = new ObjectTypePair(obj, declaredTypeOfField, false);
            JsonElement child = findAndInvokeCustomSerializer(objTypePair);
            if (child == null) {
                return false;
            }
            addChildAsElement(f, child);
            return true;
        } catch (CircularReferenceException e) {
            throw e.createDetailedException(f);
        } catch (IllegalAccessException e2) {
            throw new RuntimeException();
        }
    }

    private void assignToRoot(JsonElement newRoot) {
        this.root = (JsonElement) C$Gson$Preconditions.checkNotNull(newRoot);
    }

    private boolean isFieldNull(FieldAttributes f, Object obj) {
        return getFieldValue(f, obj) == null;
    }

    private Object getFieldValue(FieldAttributes f, Object obj) {
        try {
            return f.get(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public JsonElement getJsonElement() {
        return this.root;
    }
}
