package org.codehaus.jackson.map.deser;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Map;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.DeserializerProvider;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.KeyDeserializer;
import org.codehaus.jackson.map.ResolvableDeserializer;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.annotate.JacksonStdImpl;
import org.codehaus.jackson.map.util.ArrayBuilders;
import org.codehaus.jackson.type.JavaType;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
@JacksonStdImpl
public class MapDeserializer extends ContainerDeserializer<Map<Object, Object>> implements ResolvableDeserializer {
    protected final Constructor<Map<Object, Object>> _defaultCtor;
    protected HashSet<String> _ignorableProperties;
    protected final KeyDeserializer _keyDeserializer;
    protected final JavaType _mapType;
    protected Creator.PropertyBased _propertyBasedCreator;
    protected final JsonDeserializer<Object> _valueDeserializer;
    protected final TypeDeserializer _valueTypeDeserializer;

    public MapDeserializer(JavaType mapType, Constructor<Map<Object, Object>> defCtor, KeyDeserializer keyDeser, JsonDeserializer<Object> valueDeser, TypeDeserializer valueTypeDeser) {
        super(Map.class);
        this._mapType = mapType;
        this._defaultCtor = defCtor;
        this._keyDeserializer = keyDeser;
        this._valueDeserializer = valueDeser;
        this._valueTypeDeserializer = valueTypeDeser;
    }

    public void setCreators(CreatorContainer creators) {
        this._propertyBasedCreator = creators.propertyBasedCreator();
    }

    public void setIgnorableProperties(String[] ignorable) {
        this._ignorableProperties = (ignorable == null || ignorable.length == 0) ? null : ArrayBuilders.arrayToSet(ignorable);
    }

    @Override // org.codehaus.jackson.map.deser.ContainerDeserializer
    public JavaType getContentType() {
        return this._mapType.getContentType();
    }

    @Override // org.codehaus.jackson.map.deser.ContainerDeserializer
    public JsonDeserializer<Object> getContentDeserializer() {
        return this._valueDeserializer;
    }

    @Override // org.codehaus.jackson.map.ResolvableDeserializer
    public void resolve(DeserializationConfig config, DeserializerProvider provider) throws JsonMappingException {
        if (this._propertyBasedCreator != null) {
            for (SettableBeanProperty prop : this._propertyBasedCreator.properties()) {
                prop.setValueDeserializer(findDeserializer(config, provider, prop.getType(), prop));
            }
        }
    }

    @Override // org.codehaus.jackson.map.JsonDeserializer
    public Map<Object, Object> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonToken t = jp.getCurrentToken();
        if (t != JsonToken.START_OBJECT && t != JsonToken.FIELD_NAME && t != JsonToken.END_OBJECT) {
            throw ctxt.mappingException(getMapClass());
        }
        if (this._propertyBasedCreator != null) {
            return _deserializeUsingCreator(jp, ctxt);
        }
        if (this._defaultCtor == null) {
            throw ctxt.instantiationException(getMapClass(), "No default constructor found");
        }
        try {
            Map<Object, Object> result = this._defaultCtor.newInstance(new Object[0]);
            _readAndBind(jp, ctxt, result);
            return result;
        } catch (Exception e) {
            throw ctxt.instantiationException(getMapClass(), e);
        }
    }

    @Override // org.codehaus.jackson.map.JsonDeserializer
    public Map<Object, Object> deserialize(JsonParser jp, DeserializationContext ctxt, Map<Object, Object> result) throws IOException {
        JsonToken t = jp.getCurrentToken();
        if (t != JsonToken.START_OBJECT && t != JsonToken.FIELD_NAME) {
            throw ctxt.mappingException(getMapClass());
        }
        _readAndBind(jp, ctxt, result);
        return result;
    }

    @Override // org.codehaus.jackson.map.deser.StdDeserializer, org.codehaus.jackson.map.JsonDeserializer
    public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
        return typeDeserializer.deserializeTypedFromObject(jp, ctxt);
    }

    public final Class<?> getMapClass() {
        return this._mapType.getRawClass();
    }

    @Override // org.codehaus.jackson.map.deser.StdDeserializer
    public JavaType getValueType() {
        return this._mapType;
    }

    protected final void _readAndBind(JsonParser jp, DeserializationContext ctxt, Map<Object, Object> result) throws IOException {
        Object value;
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.START_OBJECT) {
            t = jp.nextToken();
        }
        KeyDeserializer keyDes = this._keyDeserializer;
        JsonDeserializer<Object> valueDes = this._valueDeserializer;
        TypeDeserializer typeDeser = this._valueTypeDeserializer;
        while (t == JsonToken.FIELD_NAME) {
            String fieldName = jp.getCurrentName();
            Object key = keyDes == null ? fieldName : keyDes.deserializeKey(fieldName, ctxt);
            JsonToken t2 = jp.nextToken();
            if (this._ignorableProperties != null && this._ignorableProperties.contains(fieldName)) {
                jp.skipChildren();
            } else {
                if (t2 == JsonToken.VALUE_NULL) {
                    value = null;
                } else if (typeDeser == null) {
                    value = valueDes.deserialize(jp, ctxt);
                } else {
                    value = valueDes.deserializeWithType(jp, ctxt, typeDeser);
                }
                result.put(key, value);
            }
            t = jp.nextToken();
        }
    }

    public Map<Object, Object> _deserializeUsingCreator(JsonParser jp, DeserializationContext ctxt) throws Throwable {
        Object value;
        Creator.PropertyBased creator = this._propertyBasedCreator;
        PropertyValueBuffer buffer = creator.startBuilding(jp, ctxt);
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.START_OBJECT) {
            t = jp.nextToken();
        }
        JsonDeserializer<Object> valueDes = this._valueDeserializer;
        TypeDeserializer typeDeser = this._valueTypeDeserializer;
        while (t == JsonToken.FIELD_NAME) {
            String propName = jp.getCurrentName();
            JsonToken t2 = jp.nextToken();
            if (this._ignorableProperties != null && this._ignorableProperties.contains(propName)) {
                jp.skipChildren();
            } else {
                SettableBeanProperty prop = creator.findCreatorProperty(propName);
                if (prop != null) {
                    Object value2 = prop.deserialize(jp, ctxt);
                    if (buffer.assignParameter(prop.getCreatorIndex(), value2)) {
                        jp.nextToken();
                        try {
                            Map<Object, Object> result = (Map) creator.build(buffer);
                            _readAndBind(jp, ctxt, result);
                            return result;
                        } catch (Exception e) {
                            wrapAndThrow(e, this._mapType.getRawClass());
                            return null;
                        }
                    }
                } else {
                    String fieldName = jp.getCurrentName();
                    Object key = this._keyDeserializer == null ? fieldName : this._keyDeserializer.deserializeKey(fieldName, ctxt);
                    if (t2 == JsonToken.VALUE_NULL) {
                        value = null;
                    } else if (typeDeser == null) {
                        value = valueDes.deserialize(jp, ctxt);
                    } else {
                        value = valueDes.deserializeWithType(jp, ctxt, typeDeser);
                    }
                    buffer.bufferMapProperty(key, value);
                }
            }
            t = jp.nextToken();
        }
        try {
            return (Map) creator.build(buffer);
        } catch (Exception e2) {
            wrapAndThrow(e2, this._mapType.getRawClass());
            return null;
        }
    }

    protected void wrapAndThrow(Throwable t, Object ref) throws Throwable {
        while ((t instanceof InvocationTargetException) && t.getCause() != null) {
            t = t.getCause();
        }
        if (t instanceof Error) {
            throw ((Error) t);
        }
        if ((t instanceof IOException) && !(t instanceof JsonMappingException)) {
            throw ((IOException) t);
        }
        throw JsonMappingException.wrapWithPath(t, ref, (String) null);
    }
}
