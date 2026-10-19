package org.codehaus.jackson.map.ser;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ResolvableSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.annotate.JacksonStdImpl;
import org.codehaus.jackson.map.util.EnumValues;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.schema.JsonSchema;
import org.codehaus.jackson.schema.SchemaAware;
import org.codehaus.jackson.type.JavaType;

/* JADX INFO: loaded from: classes.dex */
@JacksonStdImpl
public class EnumMapSerializer extends ContainerSerializerBase<EnumMap<? extends Enum<?>, ?>> implements ResolvableSerializer {
    protected final EnumValues _keyEnums;
    protected final BeanProperty _property;
    protected final boolean _staticTyping;
    protected JsonSerializer<Object> _valueSerializer;
    protected final JavaType _valueType;
    protected final TypeSerializer _valueTypeSerializer;

    @Deprecated
    public EnumMapSerializer(JavaType valueType, boolean staticTyping, EnumValues keyEnums, TypeSerializer vts, BeanProperty property) {
        this(valueType, staticTyping, keyEnums, vts, property, null);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public EnumMapSerializer(JavaType valueType, boolean staticTyping, EnumValues keyEnums, TypeSerializer vts, BeanProperty property, JsonSerializer<Object> valueSerializer) {
        super(EnumMap.class, false);
        boolean z = false;
        if (staticTyping || (valueType != null && valueType.isFinal())) {
            z = true;
        }
        this._staticTyping = z;
        this._valueType = valueType;
        this._keyEnums = keyEnums;
        this._valueTypeSerializer = vts;
        this._property = property;
        this._valueSerializer = valueSerializer;
    }

    @Override // org.codehaus.jackson.map.ser.ContainerSerializerBase
    public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer vts) {
        return new EnumMapSerializer(this._valueType, this._staticTyping, this._keyEnums, vts, this._property);
    }

    @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.map.JsonSerializer
    public void serialize(EnumMap<? extends Enum<?>, ?> value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        if (!value.isEmpty()) {
            serializeContents(value, jgen, provider);
        }
        jgen.writeEndObject();
    }

    @Override // org.codehaus.jackson.map.JsonSerializer
    public void serializeWithType(EnumMap<? extends Enum<?>, ?> value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
        typeSer.writeTypePrefixForObject(value, jgen);
        if (!value.isEmpty()) {
            serializeContents(value, jgen, provider);
        }
        typeSer.writeTypeSuffixForObject(value, jgen);
    }

    protected void serializeContents(EnumMap<? extends Enum<?>, ?> value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        JsonSerializer<Object> currSerializer;
        if (this._valueSerializer != null) {
            serializeContentsUsing(value, jgen, provider, this._valueSerializer);
            return;
        }
        JsonSerializer<Object> prevSerializer = null;
        Class<?> prevClass = null;
        EnumValues keyEnums = this._keyEnums;
        Iterator i$ = value.entrySet().iterator();
        while (i$.hasNext()) {
            Map.Entry<? extends Enum<?>, ?> entry = (Map.Entry) i$.next();
            Enum<?> key = (Enum) entry.getKey();
            if (keyEnums == null) {
                SerializerBase<?> ser = (SerializerBase) provider.findValueSerializer(key.getDeclaringClass(), this._property);
                keyEnums = ((EnumSerializer) ser).getEnumValues();
            }
            jgen.writeFieldName(keyEnums.serializedValueFor(key));
            Object valueElem = entry.getValue();
            if (valueElem == null) {
                provider.defaultSerializeNull(jgen);
            } else {
                Class<?> cc = valueElem.getClass();
                if (cc == prevClass) {
                    currSerializer = prevSerializer;
                } else {
                    currSerializer = provider.findValueSerializer(cc, this._property);
                    prevSerializer = currSerializer;
                    prevClass = cc;
                }
                try {
                    currSerializer.serialize(valueElem, jgen, provider);
                } catch (Exception e) {
                    wrapAndThrow(provider, e, value, ((Enum) entry.getKey()).name());
                }
            }
        }
    }

    protected void serializeContentsUsing(EnumMap<? extends Enum<?>, ?> value, JsonGenerator jgen, SerializerProvider provider, JsonSerializer<Object> valueSer) throws IOException {
        EnumValues keyEnums = this._keyEnums;
        Iterator i$ = value.entrySet().iterator();
        while (i$.hasNext()) {
            Map.Entry<? extends Enum<?>, ?> entry = (Map.Entry) i$.next();
            Enum<?> key = (Enum) entry.getKey();
            if (keyEnums == null) {
                SerializerBase<?> ser = (SerializerBase) provider.findValueSerializer(key.getDeclaringClass(), this._property);
                keyEnums = ((EnumSerializer) ser).getEnumValues();
            }
            jgen.writeFieldName(keyEnums.serializedValueFor(key));
            Object valueElem = entry.getValue();
            if (valueElem == null) {
                provider.defaultSerializeNull(jgen);
            } else {
                try {
                    valueSer.serialize(valueElem, jgen, provider);
                } catch (Exception e) {
                    wrapAndThrow(provider, e, value, ((Enum) entry.getKey()).name());
                }
            }
        }
    }

    @Override // org.codehaus.jackson.map.ResolvableSerializer
    public void resolve(SerializerProvider provider) throws JsonMappingException {
        if (this._staticTyping && this._valueSerializer == null) {
            this._valueSerializer = provider.findValueSerializer(this._valueType, this._property);
        }
    }

    @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.schema.SchemaAware
    public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
        ObjectNode o = createSchemaNode("object", true);
        if (typeHint instanceof ParameterizedType) {
            Type[] typeArgs = ((ParameterizedType) typeHint).getActualTypeArguments();
            if (typeArgs.length == 2) {
                JavaType enumType = provider.constructType(typeArgs[0]);
                JavaType valueType = provider.constructType(typeArgs[1]);
                ObjectNode propsNode = JsonNodeFactory.instance.objectNode();
                for (Enum<?> enumValue : (Enum[]) enumType.getRawClass().getEnumConstants()) {
                    Object objFindValueSerializer = provider.findValueSerializer(valueType.getRawClass(), this._property);
                    JsonNode schemaNode = objFindValueSerializer instanceof SchemaAware ? ((SchemaAware) objFindValueSerializer).getSchema(provider, null) : JsonSchema.getDefaultSchemaNode();
                    propsNode.put(provider.getConfig().getAnnotationIntrospector().findEnumValue(enumValue), schemaNode);
                }
                o.put("properties", propsNode);
            }
        }
        return o;
    }
}
