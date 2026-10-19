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
import org.codehaus.jackson.util.JsonParserSequence;
import org.codehaus.jackson.util.TokenBuffer;

/* JADX INFO: loaded from: classes.dex */
public class AsPropertyTypeDeserializer extends AsArrayTypeDeserializer {
    protected final String _typePropertyName;

    public AsPropertyTypeDeserializer(JavaType bt, TypeIdResolver idRes, BeanProperty property, String typePropName) {
        super(bt, idRes, property);
        this._typePropertyName = typePropName;
    }

    @Override // org.codehaus.jackson.map.jsontype.impl.AsArrayTypeDeserializer, org.codehaus.jackson.map.jsontype.impl.TypeDeserializerBase, org.codehaus.jackson.map.TypeDeserializer
    public JsonTypeInfo.As getTypeInclusion() {
        return JsonTypeInfo.As.PROPERTY;
    }

    @Override // org.codehaus.jackson.map.jsontype.impl.TypeDeserializerBase, org.codehaus.jackson.map.TypeDeserializer
    public String getPropertyName() {
        return this._typePropertyName;
    }

    @Override // org.codehaus.jackson.map.jsontype.impl.AsArrayTypeDeserializer, org.codehaus.jackson.map.TypeDeserializer
    public Object deserializeTypedFromObject(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.START_OBJECT) {
            t = jp.nextToken();
        } else if (t != JsonToken.FIELD_NAME) {
            throw ctxt.wrongTokenException(jp, JsonToken.START_OBJECT, "need JSON Object to contain As.PROPERTY type information (for class " + baseTypeName() + ")");
        }
        TokenBuffer tb = null;
        while (t == JsonToken.FIELD_NAME) {
            String name = jp.getCurrentName();
            jp.nextToken();
            if (this._typePropertyName.equals(name)) {
                String typeId = jp.getText();
                JsonDeserializer<Object> deser = _findDeserializer(ctxt, typeId);
                if (tb != null) {
                    jp = JsonParserSequence.createFlattened(tb.asParser(jp), jp);
                }
                jp.nextToken();
                return deser.deserialize(jp, ctxt);
            }
            if (tb == null) {
                tb = new TokenBuffer(null);
            }
            tb.writeFieldName(name);
            tb.copyCurrentStructure(jp);
            t = jp.nextToken();
        }
        throw ctxt.wrongTokenException(jp, JsonToken.FIELD_NAME, "missing property '" + this._typePropertyName + "' that is to contain type id  (for class " + baseTypeName() + ")");
    }

    @Override // org.codehaus.jackson.map.jsontype.impl.AsArrayTypeDeserializer, org.codehaus.jackson.map.TypeDeserializer
    public Object deserializeTypedFromAny(JsonParser jp, DeserializationContext ctxt) throws IOException {
        return jp.getCurrentToken() == JsonToken.START_ARRAY ? super.deserializeTypedFromArray(jp, ctxt) : deserializeTypedFromObject(jp, ctxt);
    }
}
