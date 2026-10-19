package org.codehaus.jackson.map.deser.impl;

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
import org.codehaus.jackson.map.deser.ContainerDeserializer;
import org.codehaus.jackson.type.JavaType;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
@JacksonStdImpl
public final class StringCollectionDeserializer extends ContainerDeserializer<Collection<String>> {
    protected final JavaType _collectionType;
    final Constructor<Collection<String>> _defaultCtor;
    protected final boolean _isDefaultDeserializer;
    protected final JsonDeserializer<String> _valueDeserializer;

    /* JADX WARN: Multi-variable type inference failed */
    public StringCollectionDeserializer(JavaType collectionType, JsonDeserializer<?> jsonDeserializer, Constructor<?> constructor) {
        super(collectionType.getRawClass());
        this._collectionType = collectionType;
        this._valueDeserializer = jsonDeserializer;
        this._defaultCtor = constructor;
        this._isDefaultDeserializer = isDefaultSerializer(jsonDeserializer);
    }

    @Override // org.codehaus.jackson.map.deser.ContainerDeserializer
    public JavaType getContentType() {
        return this._collectionType.getContentType();
    }

    @Override // org.codehaus.jackson.map.deser.ContainerDeserializer
    public JsonDeserializer<Object> getContentDeserializer() {
        JsonDeserializer<?> deser = this._valueDeserializer;
        return deser;
    }

    @Override // org.codehaus.jackson.map.JsonDeserializer
    public Collection<String> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        try {
            Collection<String> result = this._defaultCtor.newInstance(new Object[0]);
            return deserialize(jp, ctxt, result);
        } catch (Exception e) {
            throw ctxt.instantiationException(this._collectionType.getRawClass(), e);
        }
    }

    @Override // org.codehaus.jackson.map.JsonDeserializer
    public Collection<String> deserialize(JsonParser jp, DeserializationContext ctxt, Collection<String> result) throws IOException {
        if (!jp.isExpectedStartArrayToken()) {
            return handleNonArray(jp, ctxt, result);
        }
        if (!this._isDefaultDeserializer) {
            return deserializeUsingCustom(jp, ctxt, result);
        }
        while (true) {
            JsonToken t = jp.nextToken();
            if (t != JsonToken.END_ARRAY) {
                result.add(t == JsonToken.VALUE_NULL ? null : jp.getText());
            } else {
                return result;
            }
        }
    }

    private Collection<String> deserializeUsingCustom(JsonParser jp, DeserializationContext ctxt, Collection<String> result) throws IOException {
        String value;
        JsonDeserializer<String> deser = this._valueDeserializer;
        while (true) {
            JsonToken t = jp.nextToken();
            if (t != JsonToken.END_ARRAY) {
                if (t == JsonToken.VALUE_NULL) {
                    value = null;
                } else {
                    String value2 = deser.deserialize(jp, ctxt);
                    value = value2;
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

    private final Collection<String> handleNonArray(JsonParser jp, DeserializationContext ctxt, Collection<String> result) throws IOException {
        String value;
        if (!ctxt.isEnabled(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
            throw ctxt.mappingException(this._collectionType.getRawClass());
        }
        JsonDeserializer<String> valueDes = this._valueDeserializer;
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.VALUE_NULL) {
            value = null;
        } else {
            value = valueDes == null ? jp.getText() : valueDes.deserialize(jp, ctxt);
        }
        result.add(value);
        return result;
    }
}
