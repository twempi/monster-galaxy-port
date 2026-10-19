package org.codehaus.jackson.map.deser;

import java.io.IOException;
import java.util.EnumSet;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.TypeDeserializer;

/* JADX INFO: loaded from: classes.dex */
public final class EnumSetDeserializer extends StdDeserializer<EnumSet<?>> {
    final Class<Enum> _enumClass;
    final EnumDeserializer _enumDeserializer;

    public EnumSetDeserializer(EnumResolver enumRes) {
        super((Class<?>) EnumSet.class);
        this._enumDeserializer = new EnumDeserializer(enumRes);
        this._enumClass = enumRes.getEnumClass();
    }

    @Override // org.codehaus.jackson.map.JsonDeserializer
    public EnumSet<?> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        if (!jp.isExpectedStartArrayToken()) {
            throw ctxt.mappingException(EnumSet.class);
        }
        EnumSet<?> enumSetConstructSet = constructSet();
        while (true) {
            JsonToken t = jp.nextToken();
            if (t != JsonToken.END_ARRAY) {
                if (t == JsonToken.VALUE_NULL) {
                    throw ctxt.mappingException(this._enumClass);
                }
                Enum<?> value = this._enumDeserializer.deserialize(jp, ctxt);
                enumSetConstructSet.add(value);
            } else {
                return enumSetConstructSet;
            }
        }
    }

    @Override // org.codehaus.jackson.map.deser.StdDeserializer, org.codehaus.jackson.map.JsonDeserializer
    public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
        return typeDeserializer.deserializeTypedFromArray(jp, ctxt);
    }

    private EnumSet constructSet() {
        return EnumSet.noneOf(this._enumClass);
    }
}
