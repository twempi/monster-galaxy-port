package org.codehaus.jackson.map.deser;

import java.io.IOException;
import java.lang.reflect.Method;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.annotate.JsonCachable;
import org.codehaus.jackson.map.introspect.AnnotatedMethod;
import org.codehaus.jackson.map.util.ClassUtil;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
@JsonCachable
public class EnumDeserializer extends StdScalarDeserializer<Enum<?>> {
    final EnumResolver<?> _resolver;

    public EnumDeserializer(EnumResolver<?> res) {
        super(Enum.class);
        this._resolver = res;
    }

    public static JsonDeserializer<?> deserializerForCreator(DeserializationConfig config, Class<?> enumClass, AnnotatedMethod factory) {
        if (factory.getParameterType(0) != String.class) {
            throw new IllegalArgumentException("Parameter #0 type for factory method (" + factory + ") not suitable, must be java.lang.String");
        }
        if (config.isEnabled(DeserializationConfig.Feature.CAN_OVERRIDE_ACCESS_MODIFIERS)) {
            ClassUtil.checkAndFixAccess(factory.getMember());
        }
        return new FactoryBasedDeserializer(enumClass, factory);
    }

    @Override // org.codehaus.jackson.map.JsonDeserializer
    public Enum<?> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonToken curr = jp.getCurrentToken();
        if (curr == JsonToken.VALUE_STRING) {
            String name = jp.getText();
            Enum<?> result = this._resolver.findEnum(name);
            if (result == null) {
                throw ctxt.weirdStringException(this._resolver.getEnumClass(), "value not one of declared Enum instance names");
            }
            return result;
        }
        if (curr == JsonToken.VALUE_NUMBER_INT) {
            if (ctxt.isEnabled(DeserializationConfig.Feature.FAIL_ON_NUMBERS_FOR_ENUMS)) {
                throw ctxt.mappingException("Not allowed to deserialize Enum value out of JSON number (disable DeserializationConfig.Feature.FAIL_ON_NUMBERS_FOR_ENUMS to allow)");
            }
            int index = jp.getIntValue();
            Enum<?> result2 = this._resolver.getEnum(index);
            if (result2 == null) {
                throw ctxt.weirdNumberException(this._resolver.getEnumClass(), "index value outside legal index range [0.." + this._resolver.lastValidIndex() + "]");
            }
            return result2;
        }
        throw ctxt.mappingException(this._resolver.getEnumClass());
    }

    protected static class FactoryBasedDeserializer extends StdScalarDeserializer<Object> {
        protected final Class<?> _enumClass;
        protected final Method _factory;

        public FactoryBasedDeserializer(Class<?> cls, AnnotatedMethod f) {
            super(Enum.class);
            this._enumClass = cls;
            this._factory = f.getAnnotated();
        }

        @Override // org.codehaus.jackson.map.JsonDeserializer
        public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws Throwable {
            JsonToken curr = jp.getCurrentToken();
            if (curr != JsonToken.VALUE_STRING) {
                throw ctxt.mappingException(this._enumClass);
            }
            String value = jp.getText();
            try {
                return this._factory.invoke(this._enumClass, value);
            } catch (Exception e) {
                ClassUtil.unwrapAndThrowAsIAE(e);
                return null;
            }
        }
    }
}
