package org.codehaus.jackson.map;

import java.io.IOException;
import org.codehaus.jackson.JsonParser;

/* JADX INFO: loaded from: classes.dex */
public abstract class JsonDeserializer<T> {

    public static abstract class None extends JsonDeserializer<Object> {
    }

    public abstract T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException;

    public T deserialize(JsonParser jp, DeserializationContext ctxt, T intoValue) throws IOException {
        throw new UnsupportedOperationException();
    }

    public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
        return typeDeserializer.deserializeTypedFromAny(jp, ctxt);
    }

    public T getNullValue() {
        return null;
    }
}
