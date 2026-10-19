package org.codehaus.jackson.map.ser.impl;

import java.io.IOException;
import java.util.Collection;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ResolvableSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.annotate.JacksonStdImpl;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
@JacksonStdImpl
public class StringCollectionSerializer extends StaticListSerializerBase<Collection<String>> implements ResolvableSerializer {
    protected JsonSerializer<String> _serializer;

    public StringCollectionSerializer(BeanProperty property) {
        super(Collection.class, property);
    }

    @Override // org.codehaus.jackson.map.ser.impl.StaticListSerializerBase
    protected JsonNode contentSchema() {
        return createSchemaNode("string", true);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.codehaus.jackson.map.ResolvableSerializer
    public void resolve(SerializerProvider provider) throws JsonMappingException {
        JsonSerializer jsonSerializerFindValueSerializer = provider.findValueSerializer(String.class, this._property);
        if (!isDefaultSerializer(jsonSerializerFindValueSerializer)) {
            this._serializer = jsonSerializerFindValueSerializer;
        }
    }

    @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.map.JsonSerializer
    public void serialize(Collection<String> value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartArray();
        if (this._serializer == null) {
            serializeContents(value, jgen, provider);
        } else {
            serializeUsingCustom(value, jgen, provider);
        }
        jgen.writeEndArray();
    }

    @Override // org.codehaus.jackson.map.JsonSerializer
    public void serializeWithType(Collection<String> value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
        typeSer.writeTypePrefixForArray(value, jgen);
        if (this._serializer == null) {
            serializeContents(value, jgen, provider);
        } else {
            serializeUsingCustom(value, jgen, provider);
        }
        typeSer.writeTypeSuffixForArray(value, jgen);
    }

    private final void serializeContents(Collection<String> value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        if (this._serializer != null) {
            serializeUsingCustom(value, jgen, provider);
            return;
        }
        int i = 0;
        for (String str : value) {
            if (str == null) {
                try {
                    provider.defaultSerializeNull(jgen);
                } catch (Exception e) {
                    wrapAndThrow(provider, e, value, i);
                }
            } else {
                jgen.writeString(str);
            }
            i++;
        }
    }

    private void serializeUsingCustom(Collection<String> value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        JsonSerializer<String> ser = this._serializer;
        for (String str : value) {
            if (str == null) {
                try {
                    provider.defaultSerializeNull(jgen);
                } catch (Exception e) {
                    wrapAndThrow(provider, e, value, 0);
                }
            } else {
                ser.serialize(str, jgen, provider);
            }
        }
    }
}
