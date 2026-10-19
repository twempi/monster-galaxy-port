package org.codehaus.jackson.xc;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.SerializerBase;
import org.codehaus.jackson.schema.JsonSchema;
import org.codehaus.jackson.schema.SchemaAware;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class XmlAdapterJsonSerializer extends SerializerBase<Object> implements SchemaAware {
    private final BeanProperty _property;
    private final XmlAdapter<Object, Object> xmlAdapter;

    public XmlAdapterJsonSerializer(XmlAdapter<Object, Object> xmlAdapter, BeanProperty property) {
        super(Object.class);
        this.xmlAdapter = xmlAdapter;
        this._property = property;
    }

    @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.map.JsonSerializer
    public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        try {
            Object adapted = this.xmlAdapter.marshal(value);
            if (adapted == null) {
                provider.getNullValueSerializer().serialize(null, jgen, provider);
            } else {
                Class<?> c = adapted.getClass();
                provider.findTypedValueSerializer(c, true, this._property).serialize(adapted, jgen, provider);
            }
        } catch (Exception e) {
            throw new JsonMappingException("Unable to marshal: " + e.getMessage(), e);
        }
    }

    @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.schema.SchemaAware
    public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
        Object objFindValueSerializer = provider.findValueSerializer(findValueClass(), this._property);
        if (!(objFindValueSerializer instanceof SchemaAware)) {
            JsonNode schemaNode = JsonSchema.getDefaultSchemaNode();
            return schemaNode;
        }
        JsonNode schemaNode2 = ((SchemaAware) objFindValueSerializer).getSchema(provider, null);
        return schemaNode2;
    }

    private Class<?> findValueClass() {
        Type superClass = this.xmlAdapter.getClass().getGenericSuperclass();
        while ((superClass instanceof ParameterizedType) && XmlAdapter.class != ((ParameterizedType) superClass).getRawType()) {
            superClass = ((Class) ((ParameterizedType) superClass).getRawType()).getGenericSuperclass();
        }
        return (Class) ((ParameterizedType) superClass).getActualTypeArguments()[0];
    }
}
