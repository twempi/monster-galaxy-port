package org.codehaus.jackson.map.ser;

import java.io.IOException;
import java.lang.reflect.Type;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ResolvableSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.schema.JsonSchema;
import org.codehaus.jackson.schema.SchemaAware;
import org.codehaus.jackson.type.JavaType;

/* JADX INFO: loaded from: classes.dex */
public class BeanSerializer extends SerializerBase<Object> implements ResolvableSerializer, SchemaAware {
    protected static final BeanPropertyWriter[] NO_PROPS = new BeanPropertyWriter[0];
    protected final AnyGetterWriter _anyGetterWriter;
    protected final BeanPropertyWriter[] _filteredProps;
    protected final Object _propertyFilterId;
    protected final BeanPropertyWriter[] _props;

    public BeanSerializer(JavaType type, BeanPropertyWriter[] properties, BeanPropertyWriter[] filteredProperties, AnyGetterWriter anyGetterWriter, Object filterId) {
        super(type);
        this._props = properties;
        this._filteredProps = filteredProperties;
        this._anyGetterWriter = anyGetterWriter;
        this._propertyFilterId = filterId;
    }

    public BeanSerializer(Class<?> rawType, BeanPropertyWriter[] properties, BeanPropertyWriter[] filteredProperties, AnyGetterWriter anyGetterWriter, Object filterId) {
        super(rawType);
        this._props = properties;
        this._filteredProps = filteredProperties;
        this._anyGetterWriter = anyGetterWriter;
        this._propertyFilterId = filterId;
    }

    protected BeanSerializer(BeanSerializer src) {
        this((Class<?>) src._handledType, src._props, src._filteredProps, src._anyGetterWriter, src._propertyFilterId);
    }

    public static BeanSerializer createDummy(Class<?> forType) {
        return new BeanSerializer(forType, NO_PROPS, (BeanPropertyWriter[]) null, (AnyGetterWriter) null, (Object) null);
    }

    @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.map.JsonSerializer
    public final void serialize(Object bean, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        if (this._propertyFilterId != null) {
            serializeFieldsFiltered(bean, jgen, provider);
        } else {
            serializeFields(bean, jgen, provider);
        }
        jgen.writeEndObject();
    }

    @Override // org.codehaus.jackson.map.JsonSerializer
    public void serializeWithType(Object bean, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
        typeSer.writeTypePrefixForObject(bean, jgen);
        if (this._propertyFilterId != null) {
            serializeFieldsFiltered(bean, jgen, provider);
        } else {
            serializeFields(bean, jgen, provider);
        }
        typeSer.writeTypeSuffixForObject(bean, jgen);
    }

    protected void serializeFields(Object bean, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        BeanPropertyWriter[] props;
        if (this._filteredProps != null && provider.getSerializationView() != null) {
            props = this._filteredProps;
        } else {
            props = this._props;
        }
        int i = 0;
        try {
            int len = props.length;
            while (i < len) {
                BeanPropertyWriter prop = props[i];
                if (prop != null) {
                    prop.serializeAsField(bean, jgen, provider);
                }
                i++;
            }
            if (this._anyGetterWriter != null) {
                this._anyGetterWriter.getAndSerialize(bean, jgen, provider);
            }
        } catch (Exception e) {
            String name = i == props.length ? "[anySetter]" : props[i].getName();
            wrapAndThrow(provider, e, bean, name);
        } catch (StackOverflowError e2) {
            JsonMappingException mapE = new JsonMappingException("Infinite recursion (StackOverflowError)");
            String name2 = i == props.length ? "[anySetter]" : props[i].getName();
            mapE.prependPath(new JsonMappingException.Reference(bean, name2));
            throw mapE;
        }
    }

    protected void serializeFieldsFiltered(Object bean, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        BeanPropertyWriter[] props;
        if (this._filteredProps != null && provider.getSerializationView() != null) {
            props = this._filteredProps;
        } else {
            props = this._props;
        }
        BeanPropertyFilter filter = findFilter(provider);
        int i = 0;
        try {
            int len = props.length;
            while (i < len) {
                BeanPropertyWriter prop = props[i];
                if (prop != null) {
                    filter.serializeAsField(bean, jgen, provider, prop);
                }
                i++;
            }
            if (this._anyGetterWriter != null) {
                this._anyGetterWriter.getAndSerialize(bean, jgen, provider);
            }
        } catch (Exception e) {
            String name = i == props.length ? "[anySetter]" : props[i].getName();
            wrapAndThrow(provider, e, bean, name);
        } catch (StackOverflowError e2) {
            JsonMappingException mapE = new JsonMappingException("Infinite recursion (StackOverflowError)");
            String name2 = i == props.length ? "[anySetter]" : props[i].getName();
            mapE.prependPath(new JsonMappingException.Reference(bean, name2));
            throw mapE;
        }
    }

    protected BeanPropertyFilter findFilter(SerializerProvider provider) throws JsonMappingException {
        Object filterId = this._propertyFilterId;
        FilterProvider filters = provider.getFilterProvider();
        if (filters == null) {
            throw new JsonMappingException("Can not resolve BeanPropertyFilter with id '" + filterId + "'; no FilterProvider configured");
        }
        BeanPropertyFilter filter = filters.findFilter(filterId);
        if (filter == null) {
            throw new JsonMappingException("No filter configured with id '" + filterId + "' (type " + filterId.getClass().getName() + ")");
        }
        return filter;
    }

    @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.schema.SchemaAware
    public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
        ObjectNode o = createSchemaNode("object", true);
        ObjectNode propertiesNode = o.objectNode();
        for (int i = 0; i < this._props.length; i++) {
            BeanPropertyWriter prop = this._props[i];
            JavaType propType = prop.getSerializationType();
            Type hint = propType == null ? prop.getGenericPropertyType() : propType.getRawClass();
            Object serializer = prop.getSerializer();
            if (serializer == null) {
                Class<?> serType = prop.getRawSerializationType();
                if (serType == null) {
                    serType = prop.getPropertyType();
                }
                serializer = provider.findValueSerializer(serType, prop);
            }
            JsonNode schemaNode = serializer instanceof SchemaAware ? ((SchemaAware) serializer).getSchema(provider, hint) : JsonSchema.getDefaultSchemaNode();
            propertiesNode.put(prop.getName(), schemaNode);
        }
        o.put("properties", propertiesNode);
        return o;
    }

    /* JADX WARN: Code duplicated, block: B:20:0x0040 A[PHI: r6
      0x0040: PHI (r6v1 'type' org.codehaus.jackson.type.JavaType) = (r6v0 'type' org.codehaus.jackson.type.JavaType), (r6v2 'type' org.codehaus.jackson.type.JavaType) binds: [B:12:0x0020, B:14:0x002e] A[DONT_GENERATE, DONT_INLINE]] */
    @Override // org.codehaus.jackson.map.ResolvableSerializer
    public void resolve(SerializerProvider provider) throws JsonMappingException {
        JsonSerializer<Object> ser;
        BeanPropertyWriter w2;
        TypeSerializer typeSer;
        int filteredCount = this._filteredProps == null ? 0 : this._filteredProps.length;
        int len = this._props.length;
        for (int i = 0; i < len; i++) {
            BeanPropertyWriter prop = this._props[i];
            if (!prop.hasSerializer()) {
                JavaType type = prop.getSerializationType();
                if (type == null) {
                    type = provider.constructType(prop.getGenericPropertyType());
                    if (!type.isFinal()) {
                        if (type.isContainerType() || type.containedTypeCount() > 0) {
                            prop.setNonTrivialBaseType(type);
                        }
                    } else {
                        ser = provider.findValueSerializer(type, prop);
                        if (type.isContainerType() && (typeSer = (TypeSerializer) type.getContentType().getTypeHandler()) != null && (ser instanceof ContainerSerializerBase)) {
                            JsonSerializer<Object> ser2 = ((ContainerSerializerBase) ser).withValueTypeSerializer(typeSer);
                            ser = ser2;
                        }
                        this._props[i] = prop.withSerializer(ser);
                        if (i >= filteredCount && (w2 = this._filteredProps[i]) != null) {
                            this._filteredProps[i] = w2.withSerializer(ser);
                        }
                    }
                } else {
                    ser = provider.findValueSerializer(type, prop);
                    if (type.isContainerType()) {
                        JsonSerializer<Object> ser3 = ((ContainerSerializerBase) ser).withValueTypeSerializer(typeSer);
                        ser = ser3;
                    }
                    this._props[i] = prop.withSerializer(ser);
                    if (i >= filteredCount) {
                    }
                }
            }
        }
        if (this._anyGetterWriter != null) {
            this._anyGetterWriter.resolve(provider);
        }
    }

    public String toString() {
        return "BeanSerializer for " + handledType().getName();
    }
}
