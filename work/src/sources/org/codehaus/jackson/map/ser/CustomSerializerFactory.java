package org.codehaus.jackson.map.ser;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.SerializerFactory;
import org.codehaus.jackson.map.type.ClassKey;
import org.codehaus.jackson.type.JavaType;

/* JADX INFO: loaded from: classes.dex */
public class CustomSerializerFactory extends BeanSerializerFactory {
    protected HashMap<ClassKey, JsonSerializer<?>> _directClassMappings;
    protected JsonSerializer<?> _enumSerializerOverride;
    protected HashMap<ClassKey, JsonSerializer<?>> _interfaceMappings;
    protected HashMap<ClassKey, JsonSerializer<?>> _transitiveClassMappings;

    public CustomSerializerFactory() {
        this(null);
    }

    public CustomSerializerFactory(SerializerFactory.Config config) {
        super(config);
        this._directClassMappings = null;
        this._transitiveClassMappings = null;
        this._interfaceMappings = null;
    }

    @Override // org.codehaus.jackson.map.ser.BeanSerializerFactory, org.codehaus.jackson.map.SerializerFactory
    public SerializerFactory withConfig(SerializerFactory.Config config) {
        if (getClass() != CustomSerializerFactory.class) {
            throw new IllegalStateException("Subtype of CustomSerializerFactory (" + getClass().getName() + ") has not properly overridden method 'withAdditionalSerializers': can not instantiate subtype with additional serializer definitions");
        }
        return new CustomSerializerFactory(config);
    }

    public <T> void addGenericMapping(Class<? extends T> type, JsonSerializer<T> ser) {
        ClassKey key = new ClassKey(type);
        if (type.isInterface()) {
            if (this._interfaceMappings == null) {
                this._interfaceMappings = new HashMap<>();
            }
            this._interfaceMappings.put(key, ser);
        } else {
            if (this._transitiveClassMappings == null) {
                this._transitiveClassMappings = new HashMap<>();
            }
            this._transitiveClassMappings.put(key, ser);
        }
    }

    public <T> void addSpecificMapping(Class<? extends T> forClass, JsonSerializer<T> ser) {
        ClassKey key = new ClassKey(forClass);
        if (forClass.isInterface()) {
            throw new IllegalArgumentException("Can not add specific mapping for an interface (" + forClass.getName() + ")");
        }
        if (Modifier.isAbstract(forClass.getModifiers())) {
            throw new IllegalArgumentException("Can not add specific mapping for an abstract class (" + forClass.getName() + ")");
        }
        if (this._directClassMappings == null) {
            this._directClassMappings = new HashMap<>();
        }
        this._directClassMappings.put(key, ser);
    }

    public void setEnumSerializer(JsonSerializer<?> enumSer) {
        this._enumSerializerOverride = enumSer;
    }

    @Override // org.codehaus.jackson.map.ser.BeanSerializerFactory, org.codehaus.jackson.map.ser.BasicSerializerFactory, org.codehaus.jackson.map.SerializerFactory
    public JsonSerializer<Object> createSerializer(SerializationConfig config, JavaType type, BeanProperty property) throws JsonMappingException {
        JsonSerializer<?> ser = findCustomSerializer(type.getRawClass(), config);
        return ser != null ? ser : super.createSerializer(config, type, property);
    }

    protected JsonSerializer<?> findCustomSerializer(Class<?> type, SerializationConfig config) {
        ClassKey key = new ClassKey(type);
        if (this._directClassMappings != null) {
            JsonSerializer<?> ser = this._directClassMappings.get(key);
            if (ser != null) {
                return ser;
            }
        }
        if (type.isEnum() && this._enumSerializerOverride != null) {
            return this._enumSerializerOverride;
        }
        if (this._transitiveClassMappings != null) {
            for (Class<?> curr = type; curr != null; curr = curr.getSuperclass()) {
                key.reset(curr);
                JsonSerializer<?> ser2 = this._transitiveClassMappings.get(key);
                if (ser2 != null) {
                    return ser2;
                }
            }
        }
        if (this._interfaceMappings != null) {
            key.reset(type);
            JsonSerializer<?> ser3 = this._interfaceMappings.get(key);
            if (ser3 != null) {
                return ser3;
            }
            for (Class<?> curr2 = type; curr2 != null; curr2 = curr2.getSuperclass()) {
                JsonSerializer<?> ser4 = _findInterfaceMapping(curr2, key);
                if (ser4 != null) {
                    return ser4;
                }
            }
        }
        return null;
    }

    protected JsonSerializer<?> _findInterfaceMapping(Class<?> cls, ClassKey key) {
        for (Class<?> iface : cls.getInterfaces()) {
            key.reset(iface);
            JsonSerializer<?> ser = this._interfaceMappings.get(key);
            if (ser == null) {
                JsonSerializer<?> ser2 = _findInterfaceMapping(iface, key);
                if (ser2 != null) {
                    return ser2;
                }
            } else {
                return ser;
            }
        }
        return null;
    }
}
