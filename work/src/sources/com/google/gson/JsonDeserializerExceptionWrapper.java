package com.google.gson;

import com.google.gson.internal.C$Gson$Preconditions;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes.dex */
final class JsonDeserializerExceptionWrapper<T> implements JsonDeserializer<T> {
    private final JsonDeserializer<T> delegate;

    JsonDeserializerExceptionWrapper(JsonDeserializer<T> delegate) {
        this.delegate = (JsonDeserializer) C$Gson$Preconditions.checkNotNull(delegate);
    }

    @Override // com.google.gson.JsonDeserializer
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            return this.delegate.deserialize(json, typeOfT, context);
        } catch (JsonParseException e) {
            throw e;
        } catch (Exception e2) {
            StringBuilder errorMsg = new StringBuilder().append("The JsonDeserializer ").append(this.delegate).append(" failed to deserialize json object ").append(json).append(" given the type ").append(typeOfT);
            throw new JsonParseException(errorMsg.toString(), e2);
        }
    }

    public String toString() {
        return this.delegate.toString();
    }
}
