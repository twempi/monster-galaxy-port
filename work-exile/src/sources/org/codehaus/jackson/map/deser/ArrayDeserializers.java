package org.codehaus.jackson.map.deser;

import java.io.IOException;
import java.util.HashMap;
import org.codehaus.jackson.Base64Variants;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.annotate.JacksonStdImpl;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.map.util.ArrayBuilders;
import org.codehaus.jackson.map.util.ObjectBuffer;
import org.codehaus.jackson.type.JavaType;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class ArrayDeserializers {
    static final ArrayDeserializers instance = new ArrayDeserializers();
    HashMap<JavaType, JsonDeserializer<Object>> _allDeserializers = new HashMap<>();

    private ArrayDeserializers() {
        add(Boolean.TYPE, new BooleanDeser());
        add(Byte.TYPE, new ByteDeser());
        add(Short.TYPE, new ShortDeser());
        add(Integer.TYPE, new IntDeser());
        add(Long.TYPE, new LongDeser());
        add(Float.TYPE, new FloatDeser());
        add(Double.TYPE, new DoubleDeser());
        add(String.class, new StringDeser());
        add(Character.TYPE, new CharDeser());
    }

    public static HashMap<JavaType, JsonDeserializer<Object>> getAll() {
        return instance._allDeserializers;
    }

    private void add(Class<?> cls, JsonDeserializer<?> deser) {
        this._allDeserializers.put(TypeFactory.defaultInstance().constructType(cls), deser);
    }

    public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
        return typeDeserializer.deserializeTypedFromArray(jp, ctxt);
    }

    static abstract class ArrayDeser<T> extends StdDeserializer<T> {
        protected ArrayDeser(Class<T> cls) {
            super((Class<?>) cls);
        }

        @Override // org.codehaus.jackson.map.deser.StdDeserializer, org.codehaus.jackson.map.JsonDeserializer
        public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
            return typeDeserializer.deserializeTypedFromArray(jp, ctxt);
        }
    }

    @JacksonStdImpl
    static final class StringDeser extends ArrayDeser<String[]> {
        public StringDeser() {
            super(String[].class);
        }

        @Override // org.codehaus.jackson.map.JsonDeserializer
        public String[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            if (!jp.isExpectedStartArrayToken()) {
                return handleNonArray(jp, ctxt);
            }
            ObjectBuffer buffer = ctxt.leaseObjectBuffer();
            Object[] chunk = buffer.resetAndStart();
            int ix = 0;
            while (true) {
                JsonToken t = jp.nextToken();
                if (t != JsonToken.END_ARRAY) {
                    String value = t == JsonToken.VALUE_NULL ? null : jp.getText();
                    if (ix >= chunk.length) {
                        chunk = buffer.appendCompletedChunk(chunk);
                        ix = 0;
                    }
                    chunk[ix] = value;
                    ix++;
                } else {
                    String[] strArr = (String[]) buffer.completeAndClearBuffer(chunk, ix, String.class);
                    ctxt.returnObjectBuffer(buffer);
                    return strArr;
                }
            }
        }

        private final String[] handleNonArray(JsonParser jp, DeserializationContext ctxt) throws IOException {
            if (!ctxt.isEnabled(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
                throw ctxt.mappingException(this._valueClass);
            }
            String[] strArr = new String[1];
            strArr[0] = jp.getCurrentToken() == JsonToken.VALUE_NULL ? null : jp.getText();
            return strArr;
        }
    }

    @JacksonStdImpl
    static final class CharDeser extends ArrayDeser<char[]> {
        public CharDeser() {
            super(char[].class);
        }

        @Override // org.codehaus.jackson.map.JsonDeserializer
        public char[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            JsonToken t = jp.getCurrentToken();
            if (t == JsonToken.VALUE_STRING) {
                char[] buffer = jp.getTextCharacters();
                int offset = jp.getTextOffset();
                int len = jp.getTextLength();
                char[] result = new char[len];
                System.arraycopy(buffer, offset, result, 0, len);
                return result;
            }
            if (jp.isExpectedStartArrayToken()) {
                StringBuilder sb = new StringBuilder(64);
                while (true) {
                    JsonToken t2 = jp.nextToken();
                    if (t2 != JsonToken.END_ARRAY) {
                        if (t2 != JsonToken.VALUE_STRING) {
                            throw ctxt.mappingException(Character.TYPE);
                        }
                        String str = jp.getText();
                        if (str.length() != 1) {
                            throw JsonMappingException.from(jp, "Can not convert a JSON String of length " + str.length() + " into a char element of char array");
                        }
                        sb.append(str.charAt(0));
                    } else {
                        char[] result2 = sb.toString().toCharArray();
                        return result2;
                    }
                }
            } else {
                if (t == JsonToken.VALUE_EMBEDDED_OBJECT) {
                    Object ob = jp.getEmbeddedObject();
                    if (ob == null) {
                        return null;
                    }
                    if (ob instanceof char[]) {
                        char[] result3 = (char[]) ob;
                        return result3;
                    }
                    if (ob instanceof String) {
                        char[] result4 = ((String) ob).toCharArray();
                        return result4;
                    }
                    if (ob instanceof byte[]) {
                        char[] result5 = Base64Variants.getDefaultVariant().encode((byte[]) ob, false).toCharArray();
                        return result5;
                    }
                }
                throw ctxt.mappingException(this._valueClass);
            }
        }
    }

    @JacksonStdImpl
    static final class BooleanDeser extends ArrayDeser<boolean[]> {
        public BooleanDeser() {
            super(boolean[].class);
        }

        @Override // org.codehaus.jackson.map.JsonDeserializer
        public boolean[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            if (!jp.isExpectedStartArrayToken()) {
                return handleNonArray(jp, ctxt);
            }
            ArrayBuilders.BooleanBuilder builder = ctxt.getArrayBuilders().getBooleanBuilder();
            boolean[] chunk = builder.resetAndStart();
            int ix = 0;
            while (jp.nextToken() != JsonToken.END_ARRAY) {
                boolean value = _parseBooleanPrimitive(jp, ctxt);
                if (ix >= chunk.length) {
                    chunk = builder.appendCompletedChunk(chunk, ix);
                    ix = 0;
                }
                chunk[ix] = value;
                ix++;
            }
            return builder.completeAndClearBuffer(chunk, ix);
        }

        private final boolean[] handleNonArray(JsonParser jp, DeserializationContext ctxt) throws IOException {
            if (!ctxt.isEnabled(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
                throw ctxt.mappingException(this._valueClass);
            }
            return new boolean[]{_parseBooleanPrimitive(jp, ctxt)};
        }
    }

    @JacksonStdImpl
    static final class ByteDeser extends ArrayDeser<byte[]> {
        public ByteDeser() {
            super(byte[].class);
        }

        @Override // org.codehaus.jackson.map.JsonDeserializer
        public byte[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            byte value;
            JsonToken t = jp.getCurrentToken();
            if (t == JsonToken.VALUE_STRING) {
                return jp.getBinaryValue(ctxt.getBase64Variant());
            }
            if (t == JsonToken.VALUE_EMBEDDED_OBJECT) {
                Object ob = jp.getEmbeddedObject();
                if (ob == null) {
                    return null;
                }
                if (ob instanceof byte[]) {
                    return (byte[]) ob;
                }
            }
            if (!jp.isExpectedStartArrayToken()) {
                return handleNonArray(jp, ctxt);
            }
            ArrayBuilders.ByteBuilder builder = ctxt.getArrayBuilders().getByteBuilder();
            byte[] chunk = builder.resetAndStart();
            int ix = 0;
            while (true) {
                JsonToken t2 = jp.nextToken();
                if (t2 != JsonToken.END_ARRAY) {
                    if (t2 == JsonToken.VALUE_NUMBER_INT || t2 == JsonToken.VALUE_NUMBER_FLOAT) {
                        value = jp.getByteValue();
                    } else {
                        if (t2 != JsonToken.VALUE_NULL) {
                            throw ctxt.mappingException(this._valueClass.getComponentType());
                        }
                        value = 0;
                    }
                    if (ix >= chunk.length) {
                        chunk = builder.appendCompletedChunk(chunk, ix);
                        ix = 0;
                    }
                    chunk[ix] = value;
                    ix++;
                } else {
                    return builder.completeAndClearBuffer(chunk, ix);
                }
            }
        }

        private final byte[] handleNonArray(JsonParser jp, DeserializationContext ctxt) throws IOException {
            byte value;
            if (!ctxt.isEnabled(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
                throw ctxt.mappingException(this._valueClass);
            }
            JsonToken t = jp.getCurrentToken();
            if (t == JsonToken.VALUE_NUMBER_INT || t == JsonToken.VALUE_NUMBER_FLOAT) {
                value = jp.getByteValue();
            } else {
                if (t != JsonToken.VALUE_NULL) {
                    throw ctxt.mappingException(this._valueClass.getComponentType());
                }
                value = 0;
            }
            return new byte[]{value};
        }
    }

    @JacksonStdImpl
    static final class ShortDeser extends ArrayDeser<short[]> {
        public ShortDeser() {
            super(short[].class);
        }

        @Override // org.codehaus.jackson.map.JsonDeserializer
        public short[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            if (!jp.isExpectedStartArrayToken()) {
                return handleNonArray(jp, ctxt);
            }
            ArrayBuilders.ShortBuilder builder = ctxt.getArrayBuilders().getShortBuilder();
            short[] chunk = builder.resetAndStart();
            int ix = 0;
            while (jp.nextToken() != JsonToken.END_ARRAY) {
                short value = _parseShortPrimitive(jp, ctxt);
                if (ix >= chunk.length) {
                    chunk = builder.appendCompletedChunk(chunk, ix);
                    ix = 0;
                }
                chunk[ix] = value;
                ix++;
            }
            return builder.completeAndClearBuffer(chunk, ix);
        }

        private final short[] handleNonArray(JsonParser jp, DeserializationContext ctxt) throws IOException {
            if (!ctxt.isEnabled(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
                throw ctxt.mappingException(this._valueClass);
            }
            return new short[]{_parseShortPrimitive(jp, ctxt)};
        }
    }

    @JacksonStdImpl
    static final class IntDeser extends ArrayDeser<int[]> {
        public IntDeser() {
            super(int[].class);
        }

        @Override // org.codehaus.jackson.map.JsonDeserializer
        public int[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            if (!jp.isExpectedStartArrayToken()) {
                return handleNonArray(jp, ctxt);
            }
            ArrayBuilders.IntBuilder builder = ctxt.getArrayBuilders().getIntBuilder();
            int[] chunk = builder.resetAndStart();
            int ix = 0;
            while (jp.nextToken() != JsonToken.END_ARRAY) {
                int value = _parseIntPrimitive(jp, ctxt);
                if (ix >= chunk.length) {
                    chunk = builder.appendCompletedChunk(chunk, ix);
                    ix = 0;
                }
                chunk[ix] = value;
                ix++;
            }
            return builder.completeAndClearBuffer(chunk, ix);
        }

        private final int[] handleNonArray(JsonParser jp, DeserializationContext ctxt) throws IOException {
            if (!ctxt.isEnabled(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
                throw ctxt.mappingException(this._valueClass);
            }
            return new int[]{_parseIntPrimitive(jp, ctxt)};
        }
    }

    @JacksonStdImpl
    static final class LongDeser extends ArrayDeser<long[]> {
        public LongDeser() {
            super(long[].class);
        }

        @Override // org.codehaus.jackson.map.JsonDeserializer
        public long[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            if (!jp.isExpectedStartArrayToken()) {
                return handleNonArray(jp, ctxt);
            }
            ArrayBuilders.LongBuilder builder = ctxt.getArrayBuilders().getLongBuilder();
            long[] chunk = builder.resetAndStart();
            int ix = 0;
            while (jp.nextToken() != JsonToken.END_ARRAY) {
                long value = _parseLongPrimitive(jp, ctxt);
                if (ix >= chunk.length) {
                    chunk = builder.appendCompletedChunk(chunk, ix);
                    ix = 0;
                }
                chunk[ix] = value;
                ix++;
            }
            return builder.completeAndClearBuffer(chunk, ix);
        }

        private final long[] handleNonArray(JsonParser jp, DeserializationContext ctxt) throws IOException {
            if (!ctxt.isEnabled(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
                throw ctxt.mappingException(this._valueClass);
            }
            return new long[]{_parseLongPrimitive(jp, ctxt)};
        }
    }

    @JacksonStdImpl
    static final class FloatDeser extends ArrayDeser<float[]> {
        public FloatDeser() {
            super(float[].class);
        }

        @Override // org.codehaus.jackson.map.JsonDeserializer
        public float[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            if (!jp.isExpectedStartArrayToken()) {
                return handleNonArray(jp, ctxt);
            }
            ArrayBuilders.FloatBuilder builder = ctxt.getArrayBuilders().getFloatBuilder();
            float[] chunk = builder.resetAndStart();
            int ix = 0;
            while (jp.nextToken() != JsonToken.END_ARRAY) {
                float value = _parseFloatPrimitive(jp, ctxt);
                if (ix >= chunk.length) {
                    chunk = builder.appendCompletedChunk(chunk, ix);
                    ix = 0;
                }
                chunk[ix] = value;
                ix++;
            }
            return builder.completeAndClearBuffer(chunk, ix);
        }

        private final float[] handleNonArray(JsonParser jp, DeserializationContext ctxt) throws IOException {
            if (!ctxt.isEnabled(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
                throw ctxt.mappingException(this._valueClass);
            }
            return new float[]{_parseFloatPrimitive(jp, ctxt)};
        }
    }

    @JacksonStdImpl
    static final class DoubleDeser extends ArrayDeser<double[]> {
        public DoubleDeser() {
            super(double[].class);
        }

        @Override // org.codehaus.jackson.map.JsonDeserializer
        public double[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            if (!jp.isExpectedStartArrayToken()) {
                return handleNonArray(jp, ctxt);
            }
            ArrayBuilders.DoubleBuilder builder = ctxt.getArrayBuilders().getDoubleBuilder();
            double[] chunk = builder.resetAndStart();
            int ix = 0;
            while (jp.nextToken() != JsonToken.END_ARRAY) {
                double value = _parseDoublePrimitive(jp, ctxt);
                if (ix >= chunk.length) {
                    chunk = builder.appendCompletedChunk(chunk, ix);
                    ix = 0;
                }
                chunk[ix] = value;
                ix++;
            }
            return builder.completeAndClearBuffer(chunk, ix);
        }

        private final double[] handleNonArray(JsonParser jp, DeserializationContext ctxt) throws IOException {
            if (!ctxt.isEnabled(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
                throw ctxt.mappingException(this._valueClass);
            }
            return new double[]{_parseDoublePrimitive(jp, ctxt)};
        }
    }
}
