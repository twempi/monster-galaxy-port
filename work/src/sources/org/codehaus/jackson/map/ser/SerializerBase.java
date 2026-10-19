package org.codehaus.jackson.map.ser;

import com.sessionm.ui.SessionMActivity;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JacksonStdImpl;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.schema.SchemaAware;
import org.codehaus.jackson.type.JavaType;

/* JADX INFO: loaded from: classes.dex */
public abstract class SerializerBase<T> extends JsonSerializer<T> implements SchemaAware {
    protected final Class<T> _handledType;

    public abstract JsonNode getSchema(SerializerProvider serializerProvider, Type type) throws JsonMappingException;

    @Override // org.codehaus.jackson.map.JsonSerializer
    public abstract void serialize(T t, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException;

    protected SerializerBase(Class<T> t) {
        this._handledType = t;
    }

    protected SerializerBase(JavaType javaType) {
        this._handledType = (Class<T>) javaType.getRawClass();
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected SerializerBase(Class<?> cls, boolean dummy) {
        this._handledType = cls;
    }

    @Override // org.codehaus.jackson.map.JsonSerializer
    public final Class<T> handledType() {
        return this._handledType;
    }

    protected ObjectNode createObjectNode() {
        return JsonNodeFactory.instance.objectNode();
    }

    protected ObjectNode createSchemaNode(String type) {
        ObjectNode schema = createObjectNode();
        schema.put(SessionMActivity.INTENT_PARAM_TYPE, type);
        return schema;
    }

    protected ObjectNode createSchemaNode(String type, boolean isOptional) {
        ObjectNode schema = createSchemaNode(type);
        if (!isOptional) {
            schema.put("required", !isOptional);
        }
        return schema;
    }

    protected boolean isDefaultSerializer(JsonSerializer<?> serializer) {
        return (serializer == null || serializer.getClass().getAnnotation(JacksonStdImpl.class) == null) ? false : true;
    }

    public void wrapAndThrow(SerializerProvider provider, Throwable t, Object bean, String fieldName) throws Throwable {
        while ((t instanceof InvocationTargetException) && t.getCause() != null) {
            t = t.getCause();
        }
        if (t instanceof Error) {
            throw ((Error) t);
        }
        boolean wrap = provider == null || provider.isEnabled(SerializationConfig.Feature.WRAP_EXCEPTIONS);
        if (t instanceof IOException) {
            if (!wrap || !(t instanceof JsonMappingException)) {
                throw ((IOException) t);
            }
        } else if (!wrap && (t instanceof RuntimeException)) {
            throw ((RuntimeException) t);
        }
        throw JsonMappingException.wrapWithPath(t, bean, fieldName);
    }

    public void wrapAndThrow(SerializerProvider provider, Throwable t, Object bean, int index) throws Throwable {
        while ((t instanceof InvocationTargetException) && t.getCause() != null) {
            t = t.getCause();
        }
        if (t instanceof Error) {
            throw ((Error) t);
        }
        boolean wrap = provider == null || provider.isEnabled(SerializationConfig.Feature.WRAP_EXCEPTIONS);
        if (t instanceof IOException) {
            if (!wrap || !(t instanceof JsonMappingException)) {
                throw ((IOException) t);
            }
        } else if (!wrap && (t instanceof RuntimeException)) {
            throw ((RuntimeException) t);
        }
        throw JsonMappingException.wrapWithPath(t, bean, index);
    }

    @Deprecated
    public void wrapAndThrow(Throwable t, Object bean, String fieldName) throws Throwable {
        wrapAndThrow((SerializerProvider) null, t, bean, fieldName);
    }

    @Deprecated
    public void wrapAndThrow(Throwable t, Object bean, int index) throws Throwable {
        wrapAndThrow((SerializerProvider) null, t, bean, index);
    }
}
