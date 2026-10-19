package org.codehaus.jackson.map.deser;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.codehaus.jackson.io.NumberInput;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.KeyDeserializer;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public abstract class StdKeyDeserializer extends KeyDeserializer {
    protected final Class<?> _keyClass;

    protected abstract Object _parse(String str, DeserializationContext deserializationContext) throws Exception;

    protected StdKeyDeserializer(Class<?> cls) {
        this._keyClass = cls;
    }

    @Override // org.codehaus.jackson.map.KeyDeserializer
    public final Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
        if (key == null) {
            return null;
        }
        try {
            Object result = _parse(key, ctxt);
            if (result != null) {
                return result;
            }
            throw ctxt.weirdKeyException(this._keyClass, key, "not a valid representation");
        } catch (Exception re) {
            throw ctxt.weirdKeyException(this._keyClass, key, "not a valid representation: " + re.getMessage());
        }
    }

    public Class<?> getKeyClass() {
        return this._keyClass;
    }

    protected int _parseInt(String key) throws IllegalArgumentException {
        return Integer.parseInt(key);
    }

    protected long _parseLong(String key) throws IllegalArgumentException {
        return Long.parseLong(key);
    }

    protected double _parseDouble(String key) throws IllegalArgumentException {
        return NumberInput.parseDouble(key);
    }

    static final class BoolKD extends StdKeyDeserializer {
        BoolKD() {
            super(Boolean.class);
        }

        @Override // org.codehaus.jackson.map.deser.StdKeyDeserializer
        public Boolean _parse(String key, DeserializationContext ctxt) throws JsonMappingException {
            if ("true".equals(key)) {
                return Boolean.TRUE;
            }
            if ("false".equals(key)) {
                return Boolean.FALSE;
            }
            throw ctxt.weirdKeyException(this._keyClass, key, "value not 'true' or 'false'");
        }
    }

    static final class ByteKD extends StdKeyDeserializer {
        ByteKD() {
            super(Byte.class);
        }

        @Override // org.codehaus.jackson.map.deser.StdKeyDeserializer
        public Byte _parse(String key, DeserializationContext ctxt) throws JsonMappingException {
            int value = _parseInt(key);
            if (value < -128 || value > 127) {
                throw ctxt.weirdKeyException(this._keyClass, key, "overflow, value can not be represented as 8-bit value");
            }
            return Byte.valueOf((byte) value);
        }
    }

    static final class ShortKD extends StdKeyDeserializer {
        ShortKD() {
            super(Integer.class);
        }

        @Override // org.codehaus.jackson.map.deser.StdKeyDeserializer
        public Short _parse(String key, DeserializationContext ctxt) throws JsonMappingException {
            int value = _parseInt(key);
            if (value < -32768 || value > 32767) {
                throw ctxt.weirdKeyException(this._keyClass, key, "overflow, value can not be represented as 16-bit value");
            }
            return Short.valueOf((short) value);
        }
    }

    static final class CharKD extends StdKeyDeserializer {
        CharKD() {
            super(Character.class);
        }

        @Override // org.codehaus.jackson.map.deser.StdKeyDeserializer
        public Character _parse(String key, DeserializationContext ctxt) throws JsonMappingException {
            if (key.length() == 1) {
                return Character.valueOf(key.charAt(0));
            }
            throw ctxt.weirdKeyException(this._keyClass, key, "can only convert 1-character Strings");
        }
    }

    static final class IntKD extends StdKeyDeserializer {
        IntKD() {
            super(Integer.class);
        }

        @Override // org.codehaus.jackson.map.deser.StdKeyDeserializer
        public Integer _parse(String key, DeserializationContext ctxt) throws JsonMappingException {
            return Integer.valueOf(_parseInt(key));
        }
    }

    static final class LongKD extends StdKeyDeserializer {
        LongKD() {
            super(Long.class);
        }

        @Override // org.codehaus.jackson.map.deser.StdKeyDeserializer
        public Long _parse(String key, DeserializationContext ctxt) throws JsonMappingException {
            return Long.valueOf(_parseLong(key));
        }
    }

    static final class DoubleKD extends StdKeyDeserializer {
        DoubleKD() {
            super(Double.class);
        }

        @Override // org.codehaus.jackson.map.deser.StdKeyDeserializer
        public Double _parse(String key, DeserializationContext ctxt) throws JsonMappingException {
            return Double.valueOf(_parseDouble(key));
        }
    }

    static final class FloatKD extends StdKeyDeserializer {
        FloatKD() {
            super(Float.class);
        }

        @Override // org.codehaus.jackson.map.deser.StdKeyDeserializer
        public Float _parse(String key, DeserializationContext ctxt) throws JsonMappingException {
            return Float.valueOf((float) _parseDouble(key));
        }
    }

    static final class EnumKD extends StdKeyDeserializer {
        final EnumResolver<?> _resolver;

        EnumKD(EnumResolver<?> er) {
            super(er.getEnumClass());
            this._resolver = er;
        }

        @Override // org.codehaus.jackson.map.deser.StdKeyDeserializer
        public Enum<?> _parse(String key, DeserializationContext ctxt) throws JsonMappingException {
            Enum<?> e = this._resolver.findEnum(key);
            if (e == null) {
                throw ctxt.weirdKeyException(this._keyClass, key, "not one of values for Enum class");
            }
            return e;
        }
    }

    static final class StringCtorKeyDeserializer extends StdKeyDeserializer {
        final Constructor<?> _ctor;

        public StringCtorKeyDeserializer(Constructor<?> ctor) {
            super(ctor.getDeclaringClass());
            this._ctor = ctor;
        }

        @Override // org.codehaus.jackson.map.deser.StdKeyDeserializer
        public Object _parse(String key, DeserializationContext ctxt) throws Exception {
            return this._ctor.newInstance(key);
        }
    }

    static final class StringFactoryKeyDeserializer extends StdKeyDeserializer {
        final Method _factoryMethod;

        public StringFactoryKeyDeserializer(Method fm) {
            super(fm.getDeclaringClass());
            this._factoryMethod = fm;
        }

        @Override // org.codehaus.jackson.map.deser.StdKeyDeserializer
        public Object _parse(String key, DeserializationContext ctxt) throws Exception {
            return this._factoryMethod.invoke(null, key);
        }
    }
}
