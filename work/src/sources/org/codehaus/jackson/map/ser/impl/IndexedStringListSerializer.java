package org.codehaus.jackson.map.ser.impl;

import java.io.IOException;
import java.util.List;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ResolvableSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.annotate.JacksonStdImpl;

/* JADX INFO: loaded from: classes.dex */
@JacksonStdImpl
public final class IndexedStringListSerializer extends StaticListSerializerBase<List<String>> implements ResolvableSerializer {
    protected JsonSerializer<String> _serializer;

    public IndexedStringListSerializer(BeanProperty property) {
        super(List.class, property);
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
    public void serialize(List<String> value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartArray();
        if (this._serializer == null) {
            serializeContents(value, jgen, provider);
        } else {
            serializeUsingCustom(value, jgen, provider);
        }
        jgen.writeEndArray();
    }

    @Override // org.codehaus.jackson.map.JsonSerializer
    public void serializeWithType(List<String> value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
        typeSer.writeTypePrefixForArray(value, jgen);
        if (this._serializer == null) {
            serializeContents(value, jgen, provider);
        } else {
            serializeUsingCustom(value, jgen, provider);
        }
        typeSer.writeTypeSuffixForArray(value, jgen);
    }

    private final void serializeContents(List<String> value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        int i = 0;
        try {
            int len = value.size();
            while (i < len) {
                String str = value.get(i);
                if (str == null) {
                    provider.defaultSerializeNull(jgen);
                } else {
                    jgen.writeString(str);
                }
                i++;
            }
        } catch (Exception e) {
            wrapAndThrow(provider, e, value, i);
        }
    }

    private final void serializeUsingCustom(List<String> value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        int i = 0;
        try {
            int len = value.size();
            JsonSerializer<String> ser = this._serializer;
            i = 0;
            while (i < len) {
                String str = value.get(i);
                if (str == null) {
                    provider.defaultSerializeNull(jgen);
                } else {
                    ser.serialize(str, jgen, provider);
                }
                i++;
            }
        } catch (Exception e) {
            wrapAndThrow(provider, e, value, i);
        }
    }
}
