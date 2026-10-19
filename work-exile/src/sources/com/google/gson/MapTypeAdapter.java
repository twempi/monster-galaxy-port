package com.google.gson;

import com.google.gson.internal.C$Gson$Types;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
final class MapTypeAdapter extends BaseMapTypeAdapter {
    @Override // com.google.gson.JsonSerializer
    public /* bridge */ /* synthetic */ JsonElement serialize(Map<?, ?> map, Type x1, JsonSerializationContext x2) {
        return serialize2((Map) map, x1, x2);
    }

    MapTypeAdapter() {
    }

    /* JADX INFO: renamed from: serialize, reason: avoid collision after fix types in other method */
    public JsonElement serialize2(Map src, Type typeOfSrc, JsonSerializationContext context) {
        JsonElement valueElement;
        JsonObject map = new JsonObject();
        Type childGenericType = null;
        if (typeOfSrc instanceof ParameterizedType) {
            Class<?> rawTypeOfSrc = C$Gson$Types.getRawType(typeOfSrc);
            childGenericType = C$Gson$Types.getMapKeyAndValueTypes(typeOfSrc, rawTypeOfSrc)[1];
        }
        for (Map.Entry entry : src.entrySet()) {
            Object value = entry.getValue();
            if (value == null) {
                valueElement = JsonNull.createJsonNull();
            } else {
                Type childType = childGenericType == null ? value.getClass() : childGenericType;
                valueElement = serialize(context, value, childType);
            }
            map.add(String.valueOf(entry.getKey()), valueElement);
        }
        return map;
    }

    @Override // com.google.gson.JsonDeserializer
    public Map<?, ?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Map<?, ?> mapConstructMapType = constructMapType(typeOfT, context);
        Type[] keyAndValueTypes = C$Gson$Types.getMapKeyAndValueTypes(typeOfT, C$Gson$Types.getRawType(typeOfT));
        for (Map.Entry<String, JsonElement> entry : json.getAsJsonObject().entrySet()) {
            Object key = context.deserialize(new JsonPrimitive(entry.getKey()), keyAndValueTypes[0]);
            Object value = context.deserialize(entry.getValue(), keyAndValueTypes[1]);
            mapConstructMapType.put(key, value);
        }
        return mapConstructMapType;
    }

    public String toString() {
        return MapTypeAdapter.class.getSimpleName();
    }
}
