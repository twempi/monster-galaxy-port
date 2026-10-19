package org.codehaus.jackson.map.jsontype.impl;

import java.io.IOException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.jsontype.TypeIdResolver;

/* JADX INFO: loaded from: classes.dex */
public class AsPropertyTypeSerializer extends AsArrayTypeSerializer {
    protected final String _typePropertyName;

    public AsPropertyTypeSerializer(TypeIdResolver idRes, BeanProperty property, String propName) {
        super(idRes, property);
        this._typePropertyName = propName;
    }

    @Override // org.codehaus.jackson.map.jsontype.impl.TypeSerializerBase, org.codehaus.jackson.map.TypeSerializer
    public String getPropertyName() {
        return this._typePropertyName;
    }

    @Override // org.codehaus.jackson.map.jsontype.impl.AsArrayTypeSerializer, org.codehaus.jackson.map.jsontype.impl.TypeSerializerBase, org.codehaus.jackson.map.TypeSerializer
    public JsonTypeInfo.As getTypeInclusion() {
        return JsonTypeInfo.As.PROPERTY;
    }

    @Override // org.codehaus.jackson.map.jsontype.impl.AsArrayTypeSerializer, org.codehaus.jackson.map.TypeSerializer
    public void writeTypePrefixForObject(Object value, JsonGenerator jgen) throws IOException {
        jgen.writeStartObject();
        jgen.writeStringField(this._typePropertyName, this._idResolver.idFromValue(value));
    }

    @Override // org.codehaus.jackson.map.jsontype.impl.AsArrayTypeSerializer, org.codehaus.jackson.map.TypeSerializer
    public void writeTypePrefixForObject(Object value, JsonGenerator jgen, Class<?> type) throws IOException {
        jgen.writeStartObject();
        jgen.writeStringField(this._typePropertyName, this._idResolver.idFromValueAndType(value, type));
    }

    @Override // org.codehaus.jackson.map.jsontype.impl.AsArrayTypeSerializer, org.codehaus.jackson.map.TypeSerializer
    public void writeTypeSuffixForObject(Object value, JsonGenerator jgen) throws IOException {
        jgen.writeEndObject();
    }
}
