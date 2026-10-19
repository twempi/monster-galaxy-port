package com.google.gson;

import java.lang.reflect.Type;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
abstract class BaseMapTypeAdapter implements JsonSerializer<Map<?, ?>>, JsonDeserializer<Map<?, ?>> {
    BaseMapTypeAdapter() {
    }

    protected static final JsonElement serialize(JsonSerializationContext context, Object src, Type srcType) {
        JsonSerializationContextDefault contextImpl = (JsonSerializationContextDefault) context;
        return contextImpl.serialize(src, srcType, false);
    }

    protected static final Map<Object, Object> constructMapType(Type mapType, JsonDeserializationContext context) {
        JsonDeserializationContextDefault contextImpl = (JsonDeserializationContextDefault) context;
        ObjectConstructor objectConstructor = contextImpl.getObjectConstructor();
        return (Map) objectConstructor.construct(mapType);
    }
}
