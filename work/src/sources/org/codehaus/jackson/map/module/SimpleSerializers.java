package org.codehaus.jackson.map.module;

import java.util.HashMap;
import org.codehaus.jackson.map.BeanDescription;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.Serializers;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.type.ArrayType;
import org.codehaus.jackson.map.type.ClassKey;
import org.codehaus.jackson.map.type.CollectionLikeType;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.MapLikeType;
import org.codehaus.jackson.map.type.MapType;
import org.codehaus.jackson.type.JavaType;

/* JADX INFO: loaded from: classes.dex */
public class SimpleSerializers implements Serializers {
    protected HashMap<ClassKey, JsonSerializer<?>> _classMappings = null;
    protected HashMap<ClassKey, JsonSerializer<?>> _interfaceMappings = null;

    public void addSerializer(JsonSerializer<?> ser) {
        Class<?> cls = ser.handledType();
        if (cls == null || cls == Object.class) {
            throw new IllegalArgumentException("JsonSerializer of type " + ser.getClass().getName() + " does not define valid handledType() (use alternative registration method?)");
        }
        _addSerializer(cls, ser);
    }

    public <T> void addSerializer(Class<? extends T> type, JsonSerializer<T> ser) {
        _addSerializer(type, ser);
    }

    private void _addSerializer(Class<?> cls, JsonSerializer<?> ser) {
        ClassKey key = new ClassKey(cls);
        if (cls.isInterface()) {
            if (this._interfaceMappings == null) {
                this._interfaceMappings = new HashMap<>();
            }
            this._interfaceMappings.put(key, ser);
        } else {
            if (this._classMappings == null) {
                this._classMappings = new HashMap<>();
            }
            this._classMappings.put(key, ser);
        }
    }

    @Override // org.codehaus.jackson.map.Serializers
    public JsonSerializer<?> findSerializer(SerializationConfig config, JavaType type, BeanDescription beanDesc, BeanProperty property) {
        JsonSerializer<?> ser;
        Class<?> cls = type.getRawClass();
        ClassKey key = new ClassKey(cls);
        if (cls.isInterface()) {
            if (this._interfaceMappings != null) {
                JsonSerializer<?> ser2 = this._interfaceMappings.get(key);
                if (ser2 != null) {
                    return ser2;
                }
            }
        } else if (this._classMappings != null) {
            JsonSerializer<?> ser3 = this._classMappings.get(key);
            if (ser3 != null) {
                return ser3;
            }
            for (Class<?> curr = cls; curr != null; curr = curr.getSuperclass()) {
                key.reset(curr);
                JsonSerializer<?> ser4 = this._classMappings.get(key);
                if (ser4 != null) {
                    return ser4;
                }
            }
        }
        if (this._interfaceMappings != null) {
            JsonSerializer<?> ser5 = _findInterfaceMapping(cls, key);
            if (ser5 != null) {
                return ser5;
            }
            if (!cls.isInterface()) {
                do {
                    cls = cls.getSuperclass();
                    if (cls != null) {
                        ser = _findInterfaceMapping(cls, key);
                    }
                } while (ser == null);
                return ser;
            }
        }
        return null;
    }

    @Override // org.codehaus.jackson.map.Serializers
    public JsonSerializer<?> findArraySerializer(SerializationConfig config, ArrayType type, BeanDescription beanDesc, BeanProperty property, TypeSerializer elementTypeSerializer, JsonSerializer<Object> elementValueSerializer) {
        return findSerializer(config, type, beanDesc, property);
    }

    @Override // org.codehaus.jackson.map.Serializers
    public JsonSerializer<?> findCollectionSerializer(SerializationConfig config, CollectionType type, BeanDescription beanDesc, BeanProperty property, TypeSerializer elementTypeSerializer, JsonSerializer<Object> elementValueSerializer) {
        return findSerializer(config, type, beanDesc, property);
    }

    @Override // org.codehaus.jackson.map.Serializers
    public JsonSerializer<?> findCollectionLikeSerializer(SerializationConfig config, CollectionLikeType type, BeanDescription beanDesc, BeanProperty property, TypeSerializer elementTypeSerializer, JsonSerializer<Object> elementValueSerializer) {
        return findSerializer(config, type, beanDesc, property);
    }

    @Override // org.codehaus.jackson.map.Serializers
    public JsonSerializer<?> findMapSerializer(SerializationConfig config, MapType type, BeanDescription beanDesc, BeanProperty property, JsonSerializer<Object> keySerializer, TypeSerializer elementTypeSerializer, JsonSerializer<Object> elementValueSerializer) {
        return findSerializer(config, type, beanDesc, property);
    }

    @Override // org.codehaus.jackson.map.Serializers
    public JsonSerializer<?> findMapLikeSerializer(SerializationConfig config, MapLikeType type, BeanDescription beanDesc, BeanProperty property, JsonSerializer<Object> keySerializer, TypeSerializer elementTypeSerializer, JsonSerializer<Object> elementValueSerializer) {
        return findSerializer(config, type, beanDesc, property);
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
