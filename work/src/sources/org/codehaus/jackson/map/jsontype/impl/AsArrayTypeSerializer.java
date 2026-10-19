package org.codehaus.jackson.map.jsontype.impl;

import java.io.IOException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.jsontype.TypeIdResolver;

/* JADX INFO: loaded from: classes.dex */
public class AsArrayTypeSerializer extends TypeSerializerBase {
    public AsArrayTypeSerializer(TypeIdResolver idRes, BeanProperty property) {
        super(idRes, property);
    }

    @Override // org.codehaus.jackson.map.jsontype.impl.TypeSerializerBase, org.codehaus.jackson.map.TypeSerializer
    public JsonTypeInfo.As getTypeInclusion() {
        return JsonTypeInfo.As.WRAPPER_ARRAY;
    }

    @Override // org.codehaus.jackson.map.TypeSerializer
    public void writeTypePrefixForObject(Object value, JsonGenerator jgen) throws IOException {
        jgen.writeStartArray();
        jgen.writeString(this._idResolver.idFromValue(value));
        jgen.writeStartObject();
    }

    @Override // org.codehaus.jackson.map.TypeSerializer
    public void writeTypePrefixForObject(Object value, JsonGenerator jgen, Class<?> type) throws IOException {
        jgen.writeStartArray();
        jgen.writeString(this._idResolver.idFromValueAndType(value, type));
        jgen.writeStartObject();
    }

    @Override // org.codehaus.jackson.map.TypeSerializer
    public void writeTypePrefixForArray(Object value, JsonGenerator jgen) throws IOException {
        jgen.writeStartArray();
        jgen.writeString(this._idResolver.idFromValue(value));
        jgen.writeStartArray();
    }

    @Override // org.codehaus.jackson.map.TypeSerializer
    public void writeTypePrefixForArray(Object value, JsonGenerator jgen, Class<?> type) throws IOException {
        jgen.writeStartArray();
        jgen.writeString(this._idResolver.idFromValueAndType(value, type));
        jgen.writeStartArray();
    }

    @Override // org.codehaus.jackson.map.TypeSerializer
    public void writeTypePrefixForScalar(Object value, JsonGenerator jgen) throws IOException {
        jgen.writeStartArray();
        jgen.writeString(this._idResolver.idFromValue(value));
    }

    @Override // org.codehaus.jackson.map.TypeSerializer
    public void writeTypePrefixForScalar(Object value, JsonGenerator jgen, Class<?> type) throws IOException {
        jgen.writeStartArray();
        jgen.writeString(this._idResolver.idFromValueAndType(value, type));
    }

    @Override // org.codehaus.jackson.map.TypeSerializer
    public void writeTypeSuffixForObject(Object value, JsonGenerator jgen) throws IOException {
        jgen.writeEndObject();
        jgen.writeEndArray();
    }

    @Override // org.codehaus.jackson.map.TypeSerializer
    public void writeTypeSuffixForArray(Object value, JsonGenerator jgen) throws IOException {
        jgen.writeEndArray();
        jgen.writeEndArray();
    }

    @Override // org.codehaus.jackson.map.TypeSerializer
    public void writeTypeSuffixForScalar(Object value, JsonGenerator jgen) throws IOException {
        jgen.writeEndArray();
    }
}
