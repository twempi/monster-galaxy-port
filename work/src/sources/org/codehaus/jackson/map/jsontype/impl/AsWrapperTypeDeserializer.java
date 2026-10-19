package org.codehaus.jackson.map.jsontype.impl;

import java.io.IOException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.jsontype.TypeIdResolver;
import org.codehaus.jackson.type.JavaType;

/* JADX INFO: loaded from: classes.dex */
public class AsWrapperTypeDeserializer extends TypeDeserializerBase {
    public AsWrapperTypeDeserializer(JavaType bt, TypeIdResolver idRes, BeanProperty property) {
        super(bt, idRes, property);
    }

    @Override // org.codehaus.jackson.map.jsontype.impl.TypeDeserializerBase, org.codehaus.jackson.map.TypeDeserializer
    public JsonTypeInfo.As getTypeInclusion() {
        return JsonTypeInfo.As.WRAPPER_OBJECT;
    }

    @Override // org.codehaus.jackson.map.TypeDeserializer
    public Object deserializeTypedFromObject(JsonParser jp, DeserializationContext ctxt) throws IOException {
        return _deserialize(jp, ctxt);
    }

    @Override // org.codehaus.jackson.map.TypeDeserializer
    public Object deserializeTypedFromArray(JsonParser jp, DeserializationContext ctxt) throws IOException {
        return _deserialize(jp, ctxt);
    }

    @Override // org.codehaus.jackson.map.TypeDeserializer
    public Object deserializeTypedFromScalar(JsonParser jp, DeserializationContext ctxt) throws IOException {
        return _deserialize(jp, ctxt);
    }

    @Override // org.codehaus.jackson.map.TypeDeserializer
    public Object deserializeTypedFromAny(JsonParser jp, DeserializationContext ctxt) throws IOException {
        return _deserialize(jp, ctxt);
    }

    private final Object _deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        if (jp.getCurrentToken() != JsonToken.START_OBJECT) {
            throw ctxt.wrongTokenException(jp, JsonToken.START_OBJECT, "need JSON Object to contain As.WRAPPER_OBJECT type information for class " + baseTypeName());
        }
        if (jp.nextToken() != JsonToken.FIELD_NAME) {
            throw ctxt.wrongTokenException(jp, JsonToken.FIELD_NAME, "need JSON String that contains type id (for subtype of " + baseTypeName() + ")");
        }
        JsonDeserializer<Object> deser = _findDeserializer(ctxt, jp.getText());
        jp.nextToken();
        Object value = deser.deserialize(jp, ctxt);
        if (jp.nextToken() != JsonToken.END_OBJECT) {
            throw ctxt.wrongTokenException(jp, JsonToken.END_OBJECT, "expected closing END_OBJECT after type information and deserialized value");
        }
        return value;
    }
}
