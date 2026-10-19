package org.codehaus.jackson.map.ser;

import com.sessionm.ui.SessionMActivity;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializable;
import org.codehaus.jackson.map.JsonSerializableWithType;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.annotate.JacksonStdImpl;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.schema.JsonSerializableSchema;
import org.codehaus.jackson.util.TokenBuffer;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class StdSerializers {
    protected StdSerializers() {
    }

    protected static abstract class NonTypedScalarSerializer<T> extends ScalarSerializerBase<T> {
        protected NonTypedScalarSerializer(Class<T> t) {
            super(t);
        }

        @Override // org.codehaus.jackson.map.ser.ScalarSerializerBase, org.codehaus.jackson.map.JsonSerializer
        public final void serializeWithType(T value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
            serialize(value, jgen, provider);
        }
    }

    @JacksonStdImpl
    public static final class BooleanSerializer extends NonTypedScalarSerializer<Boolean> {
        final boolean _forPrimitive;

        public BooleanSerializer(boolean forPrimitive) {
            super(Boolean.class);
            this._forPrimitive = forPrimitive;
        }

        @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.map.JsonSerializer
        public void serialize(Boolean value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeBoolean(value.booleanValue());
        }

        @Override // org.codehaus.jackson.map.ser.ScalarSerializerBase, org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.schema.SchemaAware
        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode("boolean", !this._forPrimitive);
        }
    }

    @JacksonStdImpl
    public static final class StringSerializer extends NonTypedScalarSerializer<String> {
        public StringSerializer() {
            super(String.class);
        }

        @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.map.JsonSerializer
        public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeString(value);
        }

        @Override // org.codehaus.jackson.map.ser.ScalarSerializerBase, org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.schema.SchemaAware
        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode("string", true);
        }
    }

    @JacksonStdImpl
    public static final class IntegerSerializer extends NonTypedScalarSerializer<Integer> {
        public IntegerSerializer() {
            super(Integer.class);
        }

        @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.map.JsonSerializer
        public void serialize(Integer value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeNumber(value.intValue());
        }

        @Override // org.codehaus.jackson.map.ser.ScalarSerializerBase, org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.schema.SchemaAware
        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode("integer", true);
        }
    }

    @JacksonStdImpl
    public static final class IntLikeSerializer extends ScalarSerializerBase<Number> {
        static final IntLikeSerializer instance = new IntLikeSerializer();

        public IntLikeSerializer() {
            super(Number.class);
        }

        @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.map.JsonSerializer
        public void serialize(Number value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeNumber(value.intValue());
        }

        @Override // org.codehaus.jackson.map.ser.ScalarSerializerBase, org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.schema.SchemaAware
        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode("integer", true);
        }
    }

    @JacksonStdImpl
    public static final class LongSerializer extends ScalarSerializerBase<Long> {
        static final LongSerializer instance = new LongSerializer();

        public LongSerializer() {
            super(Long.class);
        }

        @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.map.JsonSerializer
        public void serialize(Long value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeNumber(value.longValue());
        }

        @Override // org.codehaus.jackson.map.ser.ScalarSerializerBase, org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.schema.SchemaAware
        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode("number", true);
        }
    }

    @JacksonStdImpl
    public static final class FloatSerializer extends ScalarSerializerBase<Float> {
        static final FloatSerializer instance = new FloatSerializer();

        public FloatSerializer() {
            super(Float.class);
        }

        @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.map.JsonSerializer
        public void serialize(Float value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeNumber(value.floatValue());
        }

        @Override // org.codehaus.jackson.map.ser.ScalarSerializerBase, org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.schema.SchemaAware
        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode("number", true);
        }
    }

    @JacksonStdImpl
    public static final class DoubleSerializer extends NonTypedScalarSerializer<Double> {
        static final DoubleSerializer instance = new DoubleSerializer();

        public DoubleSerializer() {
            super(Double.class);
        }

        @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.map.JsonSerializer
        public void serialize(Double value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeNumber(value.doubleValue());
        }

        @Override // org.codehaus.jackson.map.ser.ScalarSerializerBase, org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.schema.SchemaAware
        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode("number", true);
        }
    }

    @JacksonStdImpl
    public static final class NumberSerializer extends ScalarSerializerBase<Number> {
        public static final NumberSerializer instance = new NumberSerializer();

        public NumberSerializer() {
            super(Number.class);
        }

        @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.map.JsonSerializer
        public void serialize(Number value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            if (value instanceof BigDecimal) {
                jgen.writeNumber((BigDecimal) value);
                return;
            }
            if (value instanceof BigInteger) {
                jgen.writeNumber((BigInteger) value);
                return;
            }
            if (value instanceof Double) {
                jgen.writeNumber(((Double) value).doubleValue());
            } else if (value instanceof Float) {
                jgen.writeNumber(((Float) value).floatValue());
            } else {
                jgen.writeNumber(value.toString());
            }
        }

        @Override // org.codehaus.jackson.map.ser.ScalarSerializerBase, org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.schema.SchemaAware
        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode("number", true);
        }
    }

    @JacksonStdImpl
    public static final class CalendarSerializer extends ScalarSerializerBase<Calendar> {
        public static final CalendarSerializer instance = new CalendarSerializer();

        public CalendarSerializer() {
            super(Calendar.class);
        }

        @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.map.JsonSerializer
        public void serialize(Calendar value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            provider.defaultSerializeDateValue(value.getTimeInMillis(), jgen);
        }

        @Override // org.codehaus.jackson.map.ser.ScalarSerializerBase, org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.schema.SchemaAware
        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode(provider.isEnabled(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS) ? "number" : "string", true);
        }
    }

    @JacksonStdImpl
    public static final class UtilDateSerializer extends ScalarSerializerBase<Date> {
        public static final UtilDateSerializer instance = new UtilDateSerializer();

        public UtilDateSerializer() {
            super(Date.class);
        }

        @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.map.JsonSerializer
        public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            provider.defaultSerializeDateValue(value, jgen);
        }

        @Override // org.codehaus.jackson.map.ser.ScalarSerializerBase, org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.schema.SchemaAware
        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode(provider.isEnabled(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS) ? "number" : "string", true);
        }
    }

    @JacksonStdImpl
    public static final class SqlDateSerializer extends ScalarSerializerBase<java.sql.Date> {
        public SqlDateSerializer() {
            super(java.sql.Date.class);
        }

        @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.map.JsonSerializer
        public void serialize(java.sql.Date value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeString(value.toString());
        }

        @Override // org.codehaus.jackson.map.ser.ScalarSerializerBase, org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.schema.SchemaAware
        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode("string", true);
        }
    }

    @JacksonStdImpl
    public static final class SqlTimeSerializer extends ScalarSerializerBase<Time> {
        public SqlTimeSerializer() {
            super(Time.class);
        }

        @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.map.JsonSerializer
        public void serialize(Time value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeString(value.toString());
        }

        @Override // org.codehaus.jackson.map.ser.ScalarSerializerBase, org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.schema.SchemaAware
        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode("string", true);
        }
    }

    @JacksonStdImpl
    public static final class SerializableSerializer extends SerializerBase<JsonSerializable> {
        protected static final SerializableSerializer instance = new SerializableSerializer();

        private SerializableSerializer() {
            super(JsonSerializable.class);
        }

        @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.map.JsonSerializer
        public void serialize(JsonSerializable value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            value.serialize(jgen, provider);
        }

        @Override // org.codehaus.jackson.map.JsonSerializer
        public final void serializeWithType(JsonSerializable value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
            if (value instanceof JsonSerializableWithType) {
                ((JsonSerializableWithType) value).serializeWithType(jgen, provider, typeSer);
            } else {
                serialize(value, jgen, provider);
            }
        }

        @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.schema.SchemaAware
        public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
            ObjectNode objectNode = createObjectNode();
            String schemaType = "any";
            String objectProperties = null;
            String itemDefinition = null;
            if (typeHint != null) {
                Class<?> rawClass = TypeFactory.type(typeHint).getRawClass();
                if (rawClass.isAnnotationPresent(JsonSerializableSchema.class)) {
                    JsonSerializableSchema schemaInfo = (JsonSerializableSchema) rawClass.getAnnotation(JsonSerializableSchema.class);
                    schemaType = schemaInfo.schemaType();
                    if (!"##irrelevant".equals(schemaInfo.schemaObjectPropertiesDefinition())) {
                        objectProperties = schemaInfo.schemaObjectPropertiesDefinition();
                    }
                    if (!"##irrelevant".equals(schemaInfo.schemaItemDefinition())) {
                        itemDefinition = schemaInfo.schemaItemDefinition();
                    }
                }
            }
            objectNode.put(SessionMActivity.INTENT_PARAM_TYPE, schemaType);
            if (objectProperties != null) {
                try {
                    objectNode.put("properties", (JsonNode) new ObjectMapper().readValue(objectProperties, JsonNode.class));
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }
            if (itemDefinition != null) {
                try {
                    objectNode.put("items", (JsonNode) new ObjectMapper().readValue(itemDefinition, JsonNode.class));
                } catch (IOException e2) {
                    throw new IllegalStateException(e2);
                }
            }
            return objectNode;
        }
    }

    @JacksonStdImpl
    public static final class SerializableWithTypeSerializer extends SerializerBase<JsonSerializableWithType> {
        protected static final SerializableWithTypeSerializer instance = new SerializableWithTypeSerializer();

        private SerializableWithTypeSerializer() {
            super(JsonSerializableWithType.class);
        }

        @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.map.JsonSerializer
        public void serialize(JsonSerializableWithType value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            value.serialize(jgen, provider);
        }

        @Override // org.codehaus.jackson.map.JsonSerializer
        public final void serializeWithType(JsonSerializableWithType value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
            value.serializeWithType(jgen, provider, typeSer);
        }

        @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.schema.SchemaAware
        public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
            ObjectNode objectNode = createObjectNode();
            String schemaType = "any";
            String objectProperties = null;
            String itemDefinition = null;
            if (typeHint != null) {
                Class<?> rawClass = TypeFactory.rawClass(typeHint);
                if (rawClass.isAnnotationPresent(JsonSerializableSchema.class)) {
                    JsonSerializableSchema schemaInfo = (JsonSerializableSchema) rawClass.getAnnotation(JsonSerializableSchema.class);
                    schemaType = schemaInfo.schemaType();
                    if (!"##irrelevant".equals(schemaInfo.schemaObjectPropertiesDefinition())) {
                        objectProperties = schemaInfo.schemaObjectPropertiesDefinition();
                    }
                    if (!"##irrelevant".equals(schemaInfo.schemaItemDefinition())) {
                        itemDefinition = schemaInfo.schemaItemDefinition();
                    }
                }
            }
            objectNode.put(SessionMActivity.INTENT_PARAM_TYPE, schemaType);
            if (objectProperties != null) {
                try {
                    objectNode.put("properties", (JsonNode) new ObjectMapper().readValue(objectProperties, JsonNode.class));
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }
            if (itemDefinition != null) {
                try {
                    objectNode.put("items", (JsonNode) new ObjectMapper().readValue(itemDefinition, JsonNode.class));
                } catch (IOException e2) {
                    throw new IllegalStateException(e2);
                }
            }
            return objectNode;
        }
    }

    @JacksonStdImpl
    public static final class TokenBufferSerializer extends SerializerBase<TokenBuffer> {
        public TokenBufferSerializer() {
            super(TokenBuffer.class);
        }

        @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.map.JsonSerializer
        public void serialize(TokenBuffer value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            value.serialize(jgen);
        }

        @Override // org.codehaus.jackson.map.JsonSerializer
        public final void serializeWithType(TokenBuffer value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
            typeSer.writeTypePrefixForScalar(value, jgen);
            serialize(value, jgen, provider);
            typeSer.writeTypeSuffixForScalar(value, jgen);
        }

        @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.schema.SchemaAware
        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode("any", true);
        }
    }
}
