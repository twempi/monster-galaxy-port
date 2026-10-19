package org.codehaus.jackson.map.ser;

import com.sessionm.ui.SessionMActivity;
import java.io.IOException;
import java.lang.reflect.Type;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ResolvableSerializer;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.annotate.JacksonStdImpl;
import org.codehaus.jackson.node.ObjectNode;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class ArraySerializers {
    private ArraySerializers() {
    }

    public static abstract class AsArraySerializer<T> extends ContainerSerializerBase<T> {
        protected final BeanProperty _property;
        protected final TypeSerializer _valueTypeSerializer;

        protected abstract void serializeContents(T t, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException;

        protected AsArraySerializer(Class<T> cls, TypeSerializer vts, BeanProperty property) {
            super(cls);
            this._valueTypeSerializer = vts;
            this._property = property;
        }

        @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.map.JsonSerializer
        public final void serialize(T value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeStartArray();
            serializeContents(value, jgen, provider);
            jgen.writeEndArray();
        }

        @Override // org.codehaus.jackson.map.JsonSerializer
        public final void serializeWithType(T value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
            typeSer.writeTypePrefixForArray(value, jgen);
            serializeContents(value, jgen, provider);
            typeSer.writeTypeSuffixForArray(value, jgen);
        }
    }

    @JacksonStdImpl
    public static final class StringArraySerializer extends AsArraySerializer<String[]> implements ResolvableSerializer {
        protected JsonSerializer<Object> _elementSerializer;

        public StringArraySerializer(BeanProperty prop) {
            super(String[].class, null, prop);
        }

        @Override // org.codehaus.jackson.map.ser.ContainerSerializerBase
        public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer vts) {
            return this;
        }

        @Override // org.codehaus.jackson.map.ser.ArraySerializers.AsArraySerializer
        public void serializeContents(String[] value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            int len = value.length;
            if (len != 0) {
                if (this._elementSerializer != null) {
                    serializeContentsSlow(value, jgen, provider, this._elementSerializer);
                    return;
                }
                for (int i = 0; i < len; i++) {
                    String str = value[i];
                    if (str == null) {
                        jgen.writeNull();
                    } else {
                        jgen.writeString(value[i]);
                    }
                }
            }
        }

        private void serializeContentsSlow(String[] value, JsonGenerator jgen, SerializerProvider provider, JsonSerializer<Object> ser) throws IOException {
            int len = value.length;
            for (int i = 0; i < len; i++) {
                String str = value[i];
                if (str == null) {
                    provider.defaultSerializeNull(jgen);
                } else {
                    ser.serialize(value[i], jgen, provider);
                }
            }
        }

        @Override // org.codehaus.jackson.map.ResolvableSerializer
        public void resolve(SerializerProvider provider) throws JsonMappingException {
            JsonSerializer<Object> ser = provider.findValueSerializer(String.class, this._property);
            if (ser != null && ser.getClass().getAnnotation(JacksonStdImpl.class) == null) {
                this._elementSerializer = ser;
            }
        }

        @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.schema.SchemaAware
        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            ObjectNode o = createSchemaNode("array", true);
            o.put("items", createSchemaNode("string"));
            return o;
        }
    }

    @JacksonStdImpl
    public static final class BooleanArraySerializer extends AsArraySerializer<boolean[]> {
        public BooleanArraySerializer() {
            super(boolean[].class, null, null);
        }

        @Override // org.codehaus.jackson.map.ser.ContainerSerializerBase
        public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer vts) {
            return this;
        }

        @Override // org.codehaus.jackson.map.ser.ArraySerializers.AsArraySerializer
        public void serializeContents(boolean[] value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            for (boolean z : value) {
                jgen.writeBoolean(z);
            }
        }

        @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.schema.SchemaAware
        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            ObjectNode o = createSchemaNode("array", true);
            o.put("items", createSchemaNode("boolean"));
            return o;
        }
    }

    @JacksonStdImpl
    public static final class ByteArraySerializer extends SerializerBase<byte[]> {
        public ByteArraySerializer() {
            super(byte[].class);
        }

        @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.map.JsonSerializer
        public void serialize(byte[] value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeBinary(value);
        }

        @Override // org.codehaus.jackson.map.JsonSerializer
        public void serializeWithType(byte[] value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
            typeSer.writeTypePrefixForScalar(value, jgen);
            jgen.writeBinary(value);
            typeSer.writeTypeSuffixForScalar(value, jgen);
        }

        @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.schema.SchemaAware
        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            ObjectNode o = createSchemaNode("array", true);
            ObjectNode itemSchema = createSchemaNode("string");
            o.put("items", itemSchema);
            return o;
        }
    }

    @JacksonStdImpl
    public static final class ShortArraySerializer extends AsArraySerializer<short[]> {
        public ShortArraySerializer() {
            this(null);
        }

        public ShortArraySerializer(TypeSerializer vts) {
            super(short[].class, vts, null);
        }

        @Override // org.codehaus.jackson.map.ser.ContainerSerializerBase
        public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer vts) {
            return new ShortArraySerializer(vts);
        }

        @Override // org.codehaus.jackson.map.ser.ArraySerializers.AsArraySerializer
        public void serializeContents(short[] value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            for (short s : value) {
                jgen.writeNumber((int) s);
            }
        }

        @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.schema.SchemaAware
        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            ObjectNode o = createSchemaNode("array", true);
            o.put("items", createSchemaNode("integer"));
            return o;
        }
    }

    @JacksonStdImpl
    public static final class CharArraySerializer extends SerializerBase<char[]> {
        public CharArraySerializer() {
            super(char[].class);
        }

        @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.map.JsonSerializer
        public void serialize(char[] value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            if (provider.isEnabled(SerializationConfig.Feature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS)) {
                jgen.writeStartArray();
                _writeArrayContents(jgen, value);
                jgen.writeEndArray();
                return;
            }
            jgen.writeString(value, 0, value.length);
        }

        @Override // org.codehaus.jackson.map.JsonSerializer
        public void serializeWithType(char[] value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
            if (provider.isEnabled(SerializationConfig.Feature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS)) {
                typeSer.writeTypePrefixForArray(value, jgen);
                _writeArrayContents(jgen, value);
                typeSer.writeTypeSuffixForArray(value, jgen);
            } else {
                typeSer.writeTypePrefixForScalar(value, jgen);
                jgen.writeString(value, 0, value.length);
                typeSer.writeTypeSuffixForScalar(value, jgen);
            }
        }

        private final void _writeArrayContents(JsonGenerator jgen, char[] value) throws IOException {
            int len = value.length;
            for (int i = 0; i < len; i++) {
                jgen.writeString(value, i, 1);
            }
        }

        @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.schema.SchemaAware
        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            ObjectNode o = createSchemaNode("array", true);
            ObjectNode itemSchema = createSchemaNode("string");
            itemSchema.put(SessionMActivity.INTENT_PARAM_TYPE, "string");
            o.put("items", itemSchema);
            return o;
        }
    }

    @JacksonStdImpl
    public static final class IntArraySerializer extends AsArraySerializer<int[]> {
        public IntArraySerializer() {
            super(int[].class, null, null);
        }

        @Override // org.codehaus.jackson.map.ser.ContainerSerializerBase
        public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer vts) {
            return this;
        }

        @Override // org.codehaus.jackson.map.ser.ArraySerializers.AsArraySerializer
        public void serializeContents(int[] value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            for (int i : value) {
                jgen.writeNumber(i);
            }
        }

        @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.schema.SchemaAware
        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            ObjectNode o = createSchemaNode("array", true);
            o.put("items", createSchemaNode("integer"));
            return o;
        }
    }

    @JacksonStdImpl
    public static final class LongArraySerializer extends AsArraySerializer<long[]> {
        public LongArraySerializer() {
            this(null);
        }

        public LongArraySerializer(TypeSerializer vts) {
            super(long[].class, vts, null);
        }

        @Override // org.codehaus.jackson.map.ser.ContainerSerializerBase
        public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer vts) {
            return new LongArraySerializer(vts);
        }

        @Override // org.codehaus.jackson.map.ser.ArraySerializers.AsArraySerializer
        public void serializeContents(long[] value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            for (long j : value) {
                jgen.writeNumber(j);
            }
        }

        @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.schema.SchemaAware
        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            ObjectNode o = createSchemaNode("array", true);
            o.put("items", createSchemaNode("number", true));
            return o;
        }
    }

    @JacksonStdImpl
    public static final class FloatArraySerializer extends AsArraySerializer<float[]> {
        public FloatArraySerializer() {
            this(null);
        }

        public FloatArraySerializer(TypeSerializer vts) {
            super(float[].class, vts, null);
        }

        @Override // org.codehaus.jackson.map.ser.ContainerSerializerBase
        public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer vts) {
            return new FloatArraySerializer(vts);
        }

        @Override // org.codehaus.jackson.map.ser.ArraySerializers.AsArraySerializer
        public void serializeContents(float[] value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            for (float f : value) {
                jgen.writeNumber(f);
            }
        }

        @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.schema.SchemaAware
        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            ObjectNode o = createSchemaNode("array", true);
            o.put("items", createSchemaNode("number"));
            return o;
        }
    }

    @JacksonStdImpl
    public static final class DoubleArraySerializer extends AsArraySerializer<double[]> {
        public DoubleArraySerializer() {
            super(double[].class, null, null);
        }

        @Override // org.codehaus.jackson.map.ser.ContainerSerializerBase
        public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer vts) {
            return this;
        }

        @Override // org.codehaus.jackson.map.ser.ArraySerializers.AsArraySerializer
        public void serializeContents(double[] value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            for (double d : value) {
                jgen.writeNumber(d);
            }
        }

        @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.schema.SchemaAware
        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            ObjectNode o = createSchemaNode("array", true);
            o.put("items", createSchemaNode("number"));
            return o;
        }
    }
}
