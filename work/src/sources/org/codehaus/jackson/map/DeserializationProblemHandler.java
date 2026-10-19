package org.codehaus.jackson.map;

import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public abstract class DeserializationProblemHandler {
    public boolean handleUnknownProperty(DeserializationContext ctxt, JsonDeserializer<?> deserializer, Object beanOrClass, String propertyName) throws IOException {
        return false;
    }
}
