package org.codehaus.jackson.map.deser;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Collection;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.annotate.JacksonStdImpl;
import org.codehaus.jackson.type.JavaType;

/* JADX INFO: loaded from: classes.dex */
@JacksonStdImpl
public class CollectionDeserializer extends ContainerDeserializer<Collection<Object>> {
    protected final JavaType _collectionType;
    final Constructor<Collection<Object>> _defaultCtor;
    final JsonDeserializer<Object> _valueDeserializer;
    final TypeDeserializer _valueTypeDeserializer;

    public CollectionDeserializer(JavaType collectionType, JsonDeserializer<Object> valueDeser, TypeDeserializer valueTypeDeser, Constructor<Collection<Object>> ctor) {
        super(collectionType.getRawClass());
        this._collectionType = collectionType;
        this._valueDeserializer = valueDeser;
        this._valueTypeDeserializer = valueTypeDeser;
        if (ctor == null) {
            throw new IllegalArgumentException("No default constructor found for container class " + collectionType.getRawClass().getName());
        }
        this._defaultCtor = ctor;
    }

    @Override // org.codehaus.jackson.map.deser.ContainerDeserializer
    public JavaType getContentType() {
        return this._collectionType.getContentType();
    }

    @Override // org.codehaus.jackson.map.deser.ContainerDeserializer
    public JsonDeserializer<Object> getContentDeserializer() {
        return this._valueDeserializer;
    }

    @Override // org.codehaus.jackson.map.JsonDeserializer
    public Collection<Object> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        try {
            Collection<Object> result = this._defaultCtor.newInstance(new Object[0]);
            return deserialize(jp, ctxt, result);
        } catch (Exception e) {
            throw ctxt.instantiationException(this._collectionType.getRawClass(), e);
        }
    }

    @Override // org.codehaus.jackson.map.JsonDeserializer
    public Collection<Object> deserialize(JsonParser jp, DeserializationContext ctxt, Collection<Object> result) throws IOException {
        Object value;
        if (!jp.isExpectedStartArrayToken()) {
            return handleNonArray(jp, ctxt, result);
        }
        JsonDeserializer<Object> valueDes = this._valueDeserializer;
        TypeDeserializer typeDeser = this._valueTypeDeserializer;
        while (true) {
            JsonToken t = jp.nextToken();
            if (t != JsonToken.END_ARRAY) {
                if (t == JsonToken.VALUE_NULL) {
                    value = null;
                } else if (typeDeser == null) {
                    value = valueDes.deserialize(jp, ctxt);
                } else {
                    value = valueDes.deserializeWithType(jp, ctxt, typeDeser);
                }
                result.add(value);
            } else {
                return result;
            }
        }
    }

    @Override // org.codehaus.jackson.map.deser.StdDeserializer, org.codehaus.jackson.map.JsonDeserializer
    public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
        return typeDeserializer.deserializeTypedFromArray(jp, ctxt);
    }

    private final Collection<Object> handleNonArray(JsonParser jp, DeserializationContext ctxt, Collection<Object> result) throws IOException {
        Object value;
        if (!ctxt.isEnabled(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
            throw ctxt.mappingException(this._collectionType.getRawClass());
        }
        JsonDeserializer<Object> valueDes = this._valueDeserializer;
        TypeDeserializer typeDeser = this._valueTypeDeserializer;
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.VALUE_NULL) {
            value = null;
        } else if (typeDeser == null) {
            value = valueDes.deserialize(jp, ctxt);
        } else {
            value = valueDes.deserializeWithType(jp, ctxt, typeDeser);
        }
        result.add(value);
        return result;
    }
}
