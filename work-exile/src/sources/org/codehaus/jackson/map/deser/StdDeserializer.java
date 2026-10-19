package org.codehaus.jackson.map.deser;

import com.badlogic.gdx.Input;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.codehaus.jackson.Base64Variants;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.io.NumberInput;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.DeserializerProvider;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ResolvableDeserializer;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.annotate.JacksonStdImpl;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.util.TokenBuffer;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public abstract class StdDeserializer<T> extends JsonDeserializer<T> {
    protected final Class<?> _valueClass;

    protected StdDeserializer(Class<?> vc) {
        this._valueClass = vc;
    }

    protected StdDeserializer(JavaType valueType) {
        this._valueClass = valueType == null ? null : valueType.getRawClass();
    }

    public Class<?> getValueClass() {
        return this._valueClass;
    }

    public JavaType getValueType() {
        return null;
    }

    protected boolean isDefaultSerializer(JsonDeserializer<?> deserializer) {
        return (deserializer == null || deserializer.getClass().getAnnotation(JacksonStdImpl.class) == null) ? false : true;
    }

    @Override // org.codehaus.jackson.map.JsonDeserializer
    public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
        return typeDeserializer.deserializeTypedFromAny(jp, ctxt);
    }

    protected final boolean _parseBooleanPrimitive(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.VALUE_TRUE) {
            return true;
        }
        if (t != JsonToken.VALUE_FALSE && t != JsonToken.VALUE_NULL) {
            if (t == JsonToken.VALUE_NUMBER_INT) {
                return jp.getIntValue() != 0;
            }
            if (t == JsonToken.VALUE_STRING) {
                String text = jp.getText().trim();
                if ("true".equals(text)) {
                    return true;
                }
                if ("false".equals(text) || text.length() == 0) {
                    return Boolean.FALSE.booleanValue();
                }
                throw ctxt.weirdStringException(this._valueClass, "only \"true\" or \"false\" recognized");
            }
            throw ctxt.mappingException(this._valueClass);
        }
        return false;
    }

    protected final Boolean _parseBoolean(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.VALUE_TRUE) {
            return Boolean.TRUE;
        }
        if (t == JsonToken.VALUE_FALSE) {
            return Boolean.FALSE;
        }
        if (t == JsonToken.VALUE_NULL) {
            return null;
        }
        if (t == JsonToken.VALUE_NUMBER_INT) {
            return jp.getIntValue() == 0 ? Boolean.FALSE : Boolean.TRUE;
        }
        if (t == JsonToken.VALUE_STRING) {
            String text = jp.getText().trim();
            if ("true".equals(text)) {
                return Boolean.TRUE;
            }
            if ("false".equals(text) || text.length() == 0) {
                return Boolean.FALSE;
            }
            throw ctxt.weirdStringException(this._valueClass, "only \"true\" or \"false\" recognized");
        }
        throw ctxt.mappingException(this._valueClass);
    }

    protected final Short _parseShort(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.VALUE_NULL) {
            return null;
        }
        if (t == JsonToken.VALUE_NUMBER_INT || t == JsonToken.VALUE_NUMBER_FLOAT) {
            return Short.valueOf(jp.getShortValue());
        }
        int value = _parseIntPrimitive(jp, ctxt);
        if (value < -32768 || value > 32767) {
            throw ctxt.weirdStringException(this._valueClass, "overflow, value can not be represented as 16-bit value");
        }
        return Short.valueOf((short) value);
    }

    protected final short _parseShortPrimitive(JsonParser jp, DeserializationContext ctxt) throws IOException {
        int value = _parseIntPrimitive(jp, ctxt);
        if (value < -32768 || value > 32767) {
            throw ctxt.weirdStringException(this._valueClass, "overflow, value can not be represented as 16-bit value");
        }
        return (short) value;
    }

    protected final int _parseIntPrimitive(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.VALUE_NUMBER_INT || t == JsonToken.VALUE_NUMBER_FLOAT) {
            return jp.getIntValue();
        }
        if (t == JsonToken.VALUE_STRING) {
            String text = jp.getText().trim();
            try {
                int len = text.length();
                if (len > 9) {
                    long l = Long.parseLong(text);
                    if (l < -2147483648L || l > 2147483647L) {
                        throw ctxt.weirdStringException(this._valueClass, "Overflow: numeric value (" + text + ") out of range of int (-2147483648 - 2147483647)");
                    }
                    return (int) l;
                }
                if (len != 0) {
                    return NumberInput.parseInt(text);
                }
                return 0;
            } catch (IllegalArgumentException e) {
                throw ctxt.weirdStringException(this._valueClass, "not a valid int value");
            }
        }
        if (t != JsonToken.VALUE_NULL) {
            throw ctxt.mappingException(this._valueClass);
        }
        return 0;
    }

    protected final Integer _parseInteger(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.VALUE_NUMBER_INT || t == JsonToken.VALUE_NUMBER_FLOAT) {
            return Integer.valueOf(jp.getIntValue());
        }
        if (t == JsonToken.VALUE_STRING) {
            String text = jp.getText().trim();
            try {
                int len = text.length();
                if (len > 9) {
                    long l = Long.parseLong(text);
                    if (l < -2147483648L || l > 2147483647L) {
                        throw ctxt.weirdStringException(this._valueClass, "Overflow: numeric value (" + text + ") out of range of Integer (-2147483648 - 2147483647)");
                    }
                    return Integer.valueOf((int) l);
                }
                if (len != 0) {
                    return Integer.valueOf(NumberInput.parseInt(text));
                }
                return null;
            } catch (IllegalArgumentException e) {
                throw ctxt.weirdStringException(this._valueClass, "not a valid Integer value");
            }
        }
        if (t != JsonToken.VALUE_NULL) {
            throw ctxt.mappingException(this._valueClass);
        }
        return null;
    }

    protected final Long _parseLong(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.VALUE_NUMBER_INT || t == JsonToken.VALUE_NUMBER_FLOAT) {
            return Long.valueOf(jp.getLongValue());
        }
        if (t == JsonToken.VALUE_STRING) {
            String text = jp.getText().trim();
            if (text.length() == 0) {
                return null;
            }
            try {
                return Long.valueOf(NumberInput.parseLong(text));
            } catch (IllegalArgumentException e) {
                throw ctxt.weirdStringException(this._valueClass, "not a valid Long value");
            }
        }
        if (t != JsonToken.VALUE_NULL) {
            throw ctxt.mappingException(this._valueClass);
        }
        return null;
    }

    protected final long _parseLongPrimitive(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.VALUE_NUMBER_INT || t == JsonToken.VALUE_NUMBER_FLOAT) {
            return jp.getLongValue();
        }
        if (t == JsonToken.VALUE_STRING) {
            String text = jp.getText().trim();
            if (text.length() == 0) {
                return 0L;
            }
            try {
                return NumberInput.parseLong(text);
            } catch (IllegalArgumentException e) {
                throw ctxt.weirdStringException(this._valueClass, "not a valid long value");
            }
        }
        if (t != JsonToken.VALUE_NULL) {
            throw ctxt.mappingException(this._valueClass);
        }
        return 0L;
    }

    protected final Float _parseFloat(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.VALUE_NUMBER_INT || t == JsonToken.VALUE_NUMBER_FLOAT) {
            return Float.valueOf(jp.getFloatValue());
        }
        if (t == JsonToken.VALUE_STRING) {
            String text = jp.getText().trim();
            if (text.length() == 0) {
                return null;
            }
            switch (text.charAt(0)) {
                case Input.Keys.Q /* 45 */:
                    if ("-Infinity".equals(text) || "-INF".equals(text)) {
                        return Float.valueOf(Float.NEGATIVE_INFINITY);
                    }
                    break;
                case Input.Keys.BACKSLASH /* 73 */:
                    if ("Infinity".equals(text) || "INF".equals(text)) {
                        return Float.valueOf(Float.POSITIVE_INFINITY);
                    }
                    break;
                case Input.Keys.NUM /* 78 */:
                    if ("NaN".equals(text)) {
                        return Float.valueOf(Float.NaN);
                    }
                    break;
            }
            try {
                return Float.valueOf(Float.parseFloat(text));
            } catch (IllegalArgumentException e) {
                throw ctxt.weirdStringException(this._valueClass, "not a valid Float value");
            }
        }
        if (t != JsonToken.VALUE_NULL) {
            throw ctxt.mappingException(this._valueClass);
        }
        return null;
    }

    protected final float _parseFloatPrimitive(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.VALUE_NUMBER_INT || t == JsonToken.VALUE_NUMBER_FLOAT) {
            return jp.getFloatValue();
        }
        if (t == JsonToken.VALUE_STRING) {
            String text = jp.getText().trim();
            if (text.length() == 0) {
                return 0.0f;
            }
            switch (text.charAt(0)) {
                case Input.Keys.Q /* 45 */:
                    if ("-Infinity".equals(text) || "-INF".equals(text)) {
                        return Float.NEGATIVE_INFINITY;
                    }
                    break;
                case Input.Keys.BACKSLASH /* 73 */:
                    if ("Infinity".equals(text) || "INF".equals(text)) {
                        return Float.POSITIVE_INFINITY;
                    }
                    break;
                case Input.Keys.NUM /* 78 */:
                    if ("NaN".equals(text)) {
                        return Float.NaN;
                    }
                    break;
            }
            try {
                return Float.parseFloat(text);
            } catch (IllegalArgumentException e) {
                throw ctxt.weirdStringException(this._valueClass, "not a valid float value");
            }
        }
        if (t != JsonToken.VALUE_NULL) {
            throw ctxt.mappingException(this._valueClass);
        }
        return 0.0f;
    }

    protected final Double _parseDouble(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.VALUE_NUMBER_INT || t == JsonToken.VALUE_NUMBER_FLOAT) {
            return Double.valueOf(jp.getDoubleValue());
        }
        if (t == JsonToken.VALUE_STRING) {
            String text = jp.getText().trim();
            if (text.length() == 0) {
                return null;
            }
            switch (text.charAt(0)) {
                case Input.Keys.Q /* 45 */:
                    if ("-Infinity".equals(text) || "-INF".equals(text)) {
                        return Double.valueOf(Double.NEGATIVE_INFINITY);
                    }
                    break;
                case Input.Keys.BACKSLASH /* 73 */:
                    if ("Infinity".equals(text) || "INF".equals(text)) {
                        return Double.valueOf(Double.POSITIVE_INFINITY);
                    }
                    break;
                case Input.Keys.NUM /* 78 */:
                    if ("NaN".equals(text)) {
                        return Double.valueOf(Double.NaN);
                    }
                    break;
            }
            try {
                return Double.valueOf(parseDouble(text));
            } catch (IllegalArgumentException e) {
                throw ctxt.weirdStringException(this._valueClass, "not a valid Double value");
            }
        }
        if (t != JsonToken.VALUE_NULL) {
            throw ctxt.mappingException(this._valueClass);
        }
        return null;
    }

    protected final double _parseDoublePrimitive(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.VALUE_NUMBER_INT || t == JsonToken.VALUE_NUMBER_FLOAT) {
            return jp.getDoubleValue();
        }
        if (t == JsonToken.VALUE_STRING) {
            String text = jp.getText().trim();
            if (text.length() == 0) {
                return 0.0d;
            }
            switch (text.charAt(0)) {
                case Input.Keys.Q /* 45 */:
                    if ("-Infinity".equals(text) || "-INF".equals(text)) {
                        return Double.NEGATIVE_INFINITY;
                    }
                    break;
                case Input.Keys.BACKSLASH /* 73 */:
                    if ("Infinity".equals(text) || "INF".equals(text)) {
                        return Double.POSITIVE_INFINITY;
                    }
                    break;
                case Input.Keys.NUM /* 78 */:
                    if ("NaN".equals(text)) {
                        return Double.NaN;
                    }
                    break;
            }
            try {
                return parseDouble(text);
            } catch (IllegalArgumentException e) {
                throw ctxt.weirdStringException(this._valueClass, "not a valid double value");
            }
        }
        if (t != JsonToken.VALUE_NULL) {
            throw ctxt.mappingException(this._valueClass);
        }
        return 0.0d;
    }

    protected Date _parseDate(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonToken t = jp.getCurrentToken();
        try {
            if (t == JsonToken.VALUE_NUMBER_INT) {
                return new Date(jp.getLongValue());
            }
            if (t == JsonToken.VALUE_STRING) {
                String str = jp.getText().trim();
                if (str.length() == 0) {
                    return null;
                }
                return ctxt.parseDate(str);
            }
            throw ctxt.mappingException(this._valueClass);
        } catch (IllegalArgumentException iae) {
            throw ctxt.weirdStringException(this._valueClass, "not a valid representation (error: " + iae.getMessage() + ")");
        }
    }

    protected static final double parseDouble(String numStr) throws NumberFormatException {
        if (NumberInput.NASTY_SMALL_DOUBLE.equals(numStr)) {
            return Double.MIN_NORMAL;
        }
        return Double.parseDouble(numStr);
    }

    protected JsonDeserializer<Object> findDeserializer(DeserializationConfig config, DeserializerProvider provider, JavaType type, BeanProperty property) throws JsonMappingException {
        JsonDeserializer<Object> deser = provider.findValueDeserializer(config, type, property);
        return deser;
    }

    protected void handleUnknownProperty(JsonParser jp, DeserializationContext ctxt, Object instanceOrClass, String propName) throws IOException {
        if (instanceOrClass == null) {
            instanceOrClass = getValueClass();
        }
        if (!ctxt.handleUnknownProperty(jp, this, instanceOrClass, propName)) {
            reportUnknownProperty(ctxt, instanceOrClass, propName);
            jp.skipChildren();
        }
    }

    protected void reportUnknownProperty(DeserializationContext ctxt, Object instanceOrClass, String fieldName) throws IOException {
        if (ctxt.isEnabled(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES)) {
            throw ctxt.unknownFieldException(instanceOrClass, fieldName);
        }
    }

    protected static abstract class PrimitiveOrWrapperDeserializer<T> extends StdScalarDeserializer<T> {
        final T _nullValue;

        protected PrimitiveOrWrapperDeserializer(Class<T> vc, T nvl) {
            super(vc);
            this._nullValue = nvl;
        }

        @Override // org.codehaus.jackson.map.JsonDeserializer
        public final T getNullValue() {
            return this._nullValue;
        }
    }

    @JacksonStdImpl
    public static final class StringDeserializer extends StdScalarDeserializer<String> {
        public StringDeserializer() {
            super(String.class);
        }

        @Override // org.codehaus.jackson.map.JsonDeserializer
        public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            JsonToken curr = jp.getCurrentToken();
            if (curr == JsonToken.VALUE_STRING) {
                return jp.getText();
            }
            if (curr == JsonToken.VALUE_EMBEDDED_OBJECT) {
                Object ob = jp.getEmbeddedObject();
                if (ob == null) {
                    return null;
                }
                if (ob instanceof byte[]) {
                    return Base64Variants.getDefaultVariant().encode((byte[]) ob, false);
                }
                return ob.toString();
            }
            if (curr.isScalarValue()) {
                return jp.getText();
            }
            throw ctxt.mappingException(this._valueClass);
        }

        @Override // org.codehaus.jackson.map.deser.StdScalarDeserializer, org.codehaus.jackson.map.deser.StdDeserializer, org.codehaus.jackson.map.JsonDeserializer
        public String deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
            return deserialize(jp, ctxt);
        }
    }

    @JacksonStdImpl
    public static final class ClassDeserializer extends StdScalarDeserializer<Class<?>> {
        public ClassDeserializer() {
            super(Class.class);
        }

        @Override // org.codehaus.jackson.map.JsonDeserializer
        public Class<?> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            JsonToken curr = jp.getCurrentToken();
            if (curr == JsonToken.VALUE_STRING) {
                try {
                    return Class.forName(jp.getText());
                } catch (ClassNotFoundException e) {
                    throw ctxt.instantiationException(this._valueClass, e);
                }
            }
            throw ctxt.mappingException(this._valueClass);
        }
    }

    @JacksonStdImpl
    public static final class BooleanDeserializer extends PrimitiveOrWrapperDeserializer<Boolean> {
        public BooleanDeserializer(Class<Boolean> cls, Boolean nvl) {
            super(cls, nvl);
        }

        @Override // org.codehaus.jackson.map.JsonDeserializer
        public Boolean deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return _parseBoolean(jp, ctxt);
        }

        @Override // org.codehaus.jackson.map.deser.StdScalarDeserializer, org.codehaus.jackson.map.deser.StdDeserializer, org.codehaus.jackson.map.JsonDeserializer
        public Boolean deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
            return _parseBoolean(jp, ctxt);
        }
    }

    @JacksonStdImpl
    public static final class ByteDeserializer extends PrimitiveOrWrapperDeserializer<Byte> {
        public ByteDeserializer(Class<Byte> cls, Byte nvl) {
            super(cls, nvl);
        }

        @Override // org.codehaus.jackson.map.JsonDeserializer
        public Byte deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            int value = _parseIntPrimitive(jp, ctxt);
            if (value < -128 || value > 127) {
                throw ctxt.weirdStringException(this._valueClass, "overflow, value can not be represented as 8-bit value");
            }
            return Byte.valueOf((byte) value);
        }
    }

    @JacksonStdImpl
    public static final class ShortDeserializer extends PrimitiveOrWrapperDeserializer<Short> {
        public ShortDeserializer(Class<Short> cls, Short nvl) {
            super(cls, nvl);
        }

        @Override // org.codehaus.jackson.map.JsonDeserializer
        public Short deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return _parseShort(jp, ctxt);
        }
    }

    @JacksonStdImpl
    public static final class CharacterDeserializer extends PrimitiveOrWrapperDeserializer<Character> {
        public CharacterDeserializer(Class<Character> cls, Character nvl) {
            super(cls, nvl);
        }

        @Override // org.codehaus.jackson.map.JsonDeserializer
        public Character deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            JsonToken t = jp.getCurrentToken();
            if (t == JsonToken.VALUE_NUMBER_INT) {
                int value = jp.getIntValue();
                if (value >= 0 && value <= 65535) {
                    return Character.valueOf((char) value);
                }
            } else if (t == JsonToken.VALUE_STRING) {
                String text = jp.getText();
                if (text.length() == 1) {
                    return Character.valueOf(text.charAt(0));
                }
            }
            throw ctxt.mappingException(this._valueClass);
        }
    }

    @JacksonStdImpl
    public static final class IntegerDeserializer extends PrimitiveOrWrapperDeserializer<Integer> {
        public IntegerDeserializer(Class<Integer> cls, Integer nvl) {
            super(cls, nvl);
        }

        @Override // org.codehaus.jackson.map.JsonDeserializer
        public Integer deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return _parseInteger(jp, ctxt);
        }

        @Override // org.codehaus.jackson.map.deser.StdScalarDeserializer, org.codehaus.jackson.map.deser.StdDeserializer, org.codehaus.jackson.map.JsonDeserializer
        public Integer deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
            return _parseInteger(jp, ctxt);
        }
    }

    @JacksonStdImpl
    public static final class LongDeserializer extends PrimitiveOrWrapperDeserializer<Long> {
        public LongDeserializer(Class<Long> cls, Long nvl) {
            super(cls, nvl);
        }

        @Override // org.codehaus.jackson.map.JsonDeserializer
        public Long deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return _parseLong(jp, ctxt);
        }
    }

    @JacksonStdImpl
    public static final class FloatDeserializer extends PrimitiveOrWrapperDeserializer<Float> {
        public FloatDeserializer(Class<Float> cls, Float nvl) {
            super(cls, nvl);
        }

        @Override // org.codehaus.jackson.map.JsonDeserializer
        public Float deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return _parseFloat(jp, ctxt);
        }
    }

    @JacksonStdImpl
    public static final class DoubleDeserializer extends PrimitiveOrWrapperDeserializer<Double> {
        public DoubleDeserializer(Class<Double> cls, Double nvl) {
            super(cls, nvl);
        }

        @Override // org.codehaus.jackson.map.JsonDeserializer
        public Double deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return _parseDouble(jp, ctxt);
        }

        @Override // org.codehaus.jackson.map.deser.StdScalarDeserializer, org.codehaus.jackson.map.deser.StdDeserializer, org.codehaus.jackson.map.JsonDeserializer
        public Double deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
            return _parseDouble(jp, ctxt);
        }
    }

    @JacksonStdImpl
    public static final class NumberDeserializer extends StdScalarDeserializer<Number> {
        public NumberDeserializer() {
            super(Number.class);
        }

        @Override // org.codehaus.jackson.map.JsonDeserializer
        public Number deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            Number numberValueOf;
            JsonToken t = jp.getCurrentToken();
            if (t == JsonToken.VALUE_NUMBER_INT) {
                if (ctxt.isEnabled(DeserializationConfig.Feature.USE_BIG_INTEGER_FOR_INTS)) {
                    return jp.getBigIntegerValue();
                }
                return jp.getNumberValue();
            }
            if (t == JsonToken.VALUE_NUMBER_FLOAT) {
                if (ctxt.isEnabled(DeserializationConfig.Feature.USE_BIG_DECIMAL_FOR_FLOATS)) {
                    return jp.getDecimalValue();
                }
                return Double.valueOf(jp.getDoubleValue());
            }
            if (t == JsonToken.VALUE_STRING) {
                String text = jp.getText().trim();
                try {
                    if (text.indexOf(46) >= 0) {
                        if (ctxt.isEnabled(DeserializationConfig.Feature.USE_BIG_DECIMAL_FOR_FLOATS)) {
                            numberValueOf = new BigDecimal(text);
                        } else {
                            numberValueOf = new Double(text);
                        }
                    } else if (ctxt.isEnabled(DeserializationConfig.Feature.USE_BIG_INTEGER_FOR_INTS)) {
                        numberValueOf = new BigInteger(text);
                    } else {
                        long value = Long.parseLong(text);
                        if (value <= 2147483647L && value >= -2147483648L) {
                            numberValueOf = Integer.valueOf((int) value);
                        } else {
                            numberValueOf = Long.valueOf(value);
                        }
                    }
                    return numberValueOf;
                } catch (IllegalArgumentException e) {
                    throw ctxt.weirdStringException(this._valueClass, "not a valid number");
                }
            }
            throw ctxt.mappingException(this._valueClass);
        }

        @Override // org.codehaus.jackson.map.deser.StdScalarDeserializer, org.codehaus.jackson.map.deser.StdDeserializer, org.codehaus.jackson.map.JsonDeserializer
        public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
            switch (jp.getCurrentToken()) {
                case VALUE_NUMBER_INT:
                case VALUE_NUMBER_FLOAT:
                case VALUE_STRING:
                    return deserialize(jp, ctxt);
                default:
                    return typeDeserializer.deserializeTypedFromScalar(jp, ctxt);
            }
        }
    }

    public static final class AtomicBooleanDeserializer extends StdScalarDeserializer<AtomicBoolean> {
        public AtomicBooleanDeserializer() {
            super(AtomicBoolean.class);
        }

        @Override // org.codehaus.jackson.map.JsonDeserializer
        public AtomicBoolean deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return new AtomicBoolean(_parseBooleanPrimitive(jp, ctxt));
        }
    }

    public static class AtomicReferenceDeserializer extends StdScalarDeserializer<AtomicReference<?>> implements ResolvableDeserializer {
        protected final BeanProperty _property;
        protected final JavaType _referencedType;
        protected JsonDeserializer<?> _valueDeserializer;

        public AtomicReferenceDeserializer(JavaType referencedType, BeanProperty property) {
            super(AtomicReference.class);
            this._referencedType = referencedType;
            this._property = property;
        }

        @Override // org.codehaus.jackson.map.JsonDeserializer
        public AtomicReference<?> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return new AtomicReference<>(this._valueDeserializer.deserialize(jp, ctxt));
        }

        @Override // org.codehaus.jackson.map.ResolvableDeserializer
        public void resolve(DeserializationConfig config, DeserializerProvider provider) throws JsonMappingException {
            this._valueDeserializer = provider.findValueDeserializer(config, this._referencedType, this._property);
        }
    }

    @JacksonStdImpl
    public static class BigDecimalDeserializer extends StdScalarDeserializer<BigDecimal> {
        public BigDecimalDeserializer() {
            super(BigDecimal.class);
        }

        @Override // org.codehaus.jackson.map.JsonDeserializer
        public BigDecimal deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            JsonToken t = jp.getCurrentToken();
            if (t == JsonToken.VALUE_NUMBER_INT || t == JsonToken.VALUE_NUMBER_FLOAT) {
                return jp.getDecimalValue();
            }
            if (t == JsonToken.VALUE_STRING) {
                String text = jp.getText().trim();
                if (text.length() == 0) {
                    return null;
                }
                try {
                    return new BigDecimal(text);
                } catch (IllegalArgumentException e) {
                    throw ctxt.weirdStringException(this._valueClass, "not a valid representation");
                }
            }
            throw ctxt.mappingException(this._valueClass);
        }
    }

    @JacksonStdImpl
    public static class BigIntegerDeserializer extends StdScalarDeserializer<BigInteger> {
        public BigIntegerDeserializer() {
            super(BigInteger.class);
        }

        @Override // org.codehaus.jackson.map.JsonDeserializer
        public BigInteger deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            JsonToken t = jp.getCurrentToken();
            if (t == JsonToken.VALUE_NUMBER_INT) {
                switch (jp.getNumberType()) {
                    case INT:
                    case LONG:
                        return BigInteger.valueOf(jp.getLongValue());
                }
            }
            if (t == JsonToken.VALUE_NUMBER_FLOAT) {
                return jp.getDecimalValue().toBigInteger();
            }
            if (t != JsonToken.VALUE_STRING) {
                throw ctxt.mappingException(this._valueClass);
            }
            String text = jp.getText().trim();
            if (text.length() == 0) {
                return null;
            }
            try {
                return new BigInteger(text);
            } catch (IllegalArgumentException e) {
                throw ctxt.weirdStringException(this._valueClass, "not a valid representation");
            }
        }
    }

    @JacksonStdImpl
    public static class CalendarDeserializer extends StdScalarDeserializer<Calendar> {
        Class<? extends Calendar> _calendarClass;

        public CalendarDeserializer() {
            this(null);
        }

        public CalendarDeserializer(Class<? extends Calendar> cc) {
            super(Calendar.class);
            this._calendarClass = cc;
        }

        @Override // org.codehaus.jackson.map.JsonDeserializer
        public Calendar deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            Date d = _parseDate(jp, ctxt);
            if (d == null) {
                return null;
            }
            if (this._calendarClass == null) {
                return ctxt.constructCalendar(d);
            }
            try {
                Calendar c = this._calendarClass.newInstance();
                c.setTimeInMillis(d.getTime());
                return c;
            } catch (Exception e) {
                throw ctxt.instantiationException(this._calendarClass, e);
            }
        }
    }

    public static class SqlDateDeserializer extends StdScalarDeserializer<java.sql.Date> {
        public SqlDateDeserializer() {
            super(java.sql.Date.class);
        }

        @Override // org.codehaus.jackson.map.JsonDeserializer
        public java.sql.Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            Date d = _parseDate(jp, ctxt);
            if (d == null) {
                return null;
            }
            return new java.sql.Date(d.getTime());
        }
    }

    public static class StackTraceElementDeserializer extends StdScalarDeserializer<StackTraceElement> {
        public StackTraceElementDeserializer() {
            super(StackTraceElement.class);
        }

        @Override // org.codehaus.jackson.map.JsonDeserializer
        public StackTraceElement deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            if (jp.getCurrentToken() == JsonToken.START_OBJECT) {
                String className = "";
                String methodName = "";
                String fileName = "";
                int lineNumber = -1;
                while (true) {
                    JsonToken t = jp.nextValue();
                    if (t != JsonToken.END_OBJECT) {
                        String propName = jp.getCurrentName();
                        if ("className".equals(propName)) {
                            className = jp.getText();
                        } else if ("fileName".equals(propName)) {
                            fileName = jp.getText();
                        } else if ("lineNumber".equals(propName)) {
                            if (t.isNumeric()) {
                                lineNumber = jp.getIntValue();
                            } else {
                                throw JsonMappingException.from(jp, "Non-numeric token (" + t + ") for property 'lineNumber'");
                            }
                        } else if ("methodName".equals(propName)) {
                            methodName = jp.getText();
                        } else if (!"nativeMethod".equals(propName)) {
                            handleUnknownProperty(jp, ctxt, this._valueClass, propName);
                        }
                    } else {
                        return new StackTraceElement(className, methodName, fileName, lineNumber);
                    }
                }
            } else {
                throw ctxt.mappingException(this._valueClass);
            }
        }
    }

    @JacksonStdImpl
    public static class TokenBufferDeserializer extends StdScalarDeserializer<TokenBuffer> {
        public TokenBufferDeserializer() {
            super(TokenBuffer.class);
        }

        @Override // org.codehaus.jackson.map.JsonDeserializer
        public TokenBuffer deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            TokenBuffer tb = new TokenBuffer(jp.getCodec());
            tb.copyCurrentStructure(jp);
            return tb;
        }
    }
}
