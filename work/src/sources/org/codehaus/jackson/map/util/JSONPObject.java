package org.codehaus.jackson.map.util;

import java.io.IOException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.JsonSerializableWithType;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;

/* JADX INFO: loaded from: classes.dex */
public class JSONPObject implements JsonSerializableWithType {
    protected final String _function;
    protected final JavaType _serializationType;
    protected final Object _value;

    public JSONPObject(String function, Object value) {
        this(function, value, (JavaType) null);
    }

    public JSONPObject(String function, Object value, JavaType asType) {
        this._function = function;
        this._value = value;
        this._serializationType = asType;
    }

    @Deprecated
    public JSONPObject(String function, Object value, Class<?> rawType) {
        this._function = function;
        this._value = value;
        this._serializationType = rawType == null ? null : TypeFactory.defaultInstance().constructType(rawType);
    }

    @Override // org.codehaus.jackson.map.JsonSerializableWithType
    public void serializeWithType(JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
        serialize(jgen, provider);
    }

    @Override // org.codehaus.jackson.map.JsonSerializable
    public void serialize(JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeRaw(this._function);
        jgen.writeRaw('(');
        if (this._value == null) {
            provider.defaultSerializeNull(jgen);
        } else if (this._serializationType != null) {
            provider.findTypedValueSerializer(this._serializationType, true, (BeanProperty) null).serialize(this._value, jgen, provider);
        } else {
            Class<?> cls = this._value.getClass();
            provider.findTypedValueSerializer(cls, true, (BeanProperty) null).serialize(this._value, jgen, provider);
        }
        jgen.writeRaw(')');
    }

    public String getFunction() {
        return this._function;
    }

    public Object getValue() {
        return this._value;
    }

    public JavaType getSerializationType() {
        return this._serializationType;
    }
}
