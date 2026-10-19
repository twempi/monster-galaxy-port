package org.codehaus.jackson.map;

import java.io.IOException;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public abstract class DeserializationProblemHandler {
    public boolean handleUnknownProperty(DeserializationContext ctxt, JsonDeserializer<?> deserializer, Object beanOrClass, String propertyName) throws IOException {
        return false;
    }
}
