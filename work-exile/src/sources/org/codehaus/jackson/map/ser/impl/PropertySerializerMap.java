package org.codehaus.jackson.map.ser.impl;

import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.type.JavaType;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public abstract class PropertySerializerMap {
    protected abstract PropertySerializerMap newWith(Class<?> cls, JsonSerializer<Object> jsonSerializer);

    public abstract JsonSerializer<Object> serializerFor(Class<?> cls);

    public final SerializerAndMapResult findAndAddSerializer(Class<?> type, SerializerProvider provider, BeanProperty property) throws JsonMappingException {
        JsonSerializer<Object> serializer = provider.findValueSerializer(type, property);
        return new SerializerAndMapResult(serializer, newWith(type, serializer));
    }

    public final SerializerAndMapResult findAndAddSerializer(JavaType type, SerializerProvider provider, BeanProperty property) throws JsonMappingException {
        JsonSerializer<Object> serializer = provider.findValueSerializer(type, property);
        return new SerializerAndMapResult(serializer, newWith(type.getRawClass(), serializer));
    }

    public static PropertySerializerMap emptyMap() {
        return Empty.instance;
    }

    public static final class SerializerAndMapResult {
        public final PropertySerializerMap map;
        public final JsonSerializer<Object> serializer;

        public SerializerAndMapResult(JsonSerializer<Object> serializer, PropertySerializerMap map) {
            this.serializer = serializer;
            this.map = map;
        }
    }

    private static final class TypeAndSerializer {
        public final JsonSerializer<Object> serializer;
        public final Class<?> type;

        public TypeAndSerializer(Class<?> type, JsonSerializer<Object> serializer) {
            this.type = type;
            this.serializer = serializer;
        }
    }

    private static final class Empty extends PropertySerializerMap {
        protected static final Empty instance = new Empty();

        private Empty() {
        }

        @Override // org.codehaus.jackson.map.ser.impl.PropertySerializerMap
        public JsonSerializer<Object> serializerFor(Class<?> type) {
            return null;
        }

        @Override // org.codehaus.jackson.map.ser.impl.PropertySerializerMap
        protected PropertySerializerMap newWith(Class<?> type, JsonSerializer<Object> serializer) {
            return new Single(type, serializer);
        }
    }

    private static final class Single extends PropertySerializerMap {
        private final JsonSerializer<Object> _serializer;
        private final Class<?> _type;

        public Single(Class<?> type, JsonSerializer<Object> serializer) {
            this._type = type;
            this._serializer = serializer;
        }

        @Override // org.codehaus.jackson.map.ser.impl.PropertySerializerMap
        public JsonSerializer<Object> serializerFor(Class<?> type) {
            if (type == this._type) {
                return this._serializer;
            }
            return null;
        }

        @Override // org.codehaus.jackson.map.ser.impl.PropertySerializerMap
        protected PropertySerializerMap newWith(Class<?> type, JsonSerializer<Object> serializer) {
            return new Double(this._type, this._serializer, type, serializer);
        }
    }

    private static final class Double extends PropertySerializerMap {
        private final JsonSerializer<Object> _serializer1;
        private final JsonSerializer<Object> _serializer2;
        private final Class<?> _type1;
        private final Class<?> _type2;

        public Double(Class<?> type1, JsonSerializer<Object> serializer1, Class<?> type2, JsonSerializer<Object> serializer2) {
            this._type1 = type1;
            this._serializer1 = serializer1;
            this._type2 = type2;
            this._serializer2 = serializer2;
        }

        @Override // org.codehaus.jackson.map.ser.impl.PropertySerializerMap
        public JsonSerializer<Object> serializerFor(Class<?> type) {
            if (type == this._type1) {
                return this._serializer1;
            }
            if (type == this._type2) {
                return this._serializer2;
            }
            return null;
        }

        @Override // org.codehaus.jackson.map.ser.impl.PropertySerializerMap
        protected PropertySerializerMap newWith(Class<?> type, JsonSerializer<Object> serializer) {
            TypeAndSerializer[] ts = {new TypeAndSerializer(this._type1, this._serializer1), new TypeAndSerializer(this._type2, this._serializer2)};
            return new Multi(ts);
        }
    }

    private static final class Multi extends PropertySerializerMap {
        private static final int MAX_ENTRIES = 8;
        private final TypeAndSerializer[] _entries;

        public Multi(TypeAndSerializer[] entries) {
            this._entries = entries;
        }

        @Override // org.codehaus.jackson.map.ser.impl.PropertySerializerMap
        public JsonSerializer<Object> serializerFor(Class<?> type) {
            int len = this._entries.length;
            for (int i = 0; i < len; i++) {
                TypeAndSerializer entry = this._entries[i];
                if (entry.type == type) {
                    return entry.serializer;
                }
            }
            return null;
        }

        @Override // org.codehaus.jackson.map.ser.impl.PropertySerializerMap
        protected PropertySerializerMap newWith(Class<?> type, JsonSerializer<Object> serializer) {
            int len = this._entries.length;
            if (len != 8) {
                TypeAndSerializer[] entries = new TypeAndSerializer[len + 1];
                System.arraycopy(this._entries, 0, entries, 0, len);
                entries[len] = new TypeAndSerializer(type, serializer);
                return new Multi(entries);
            }
            return this;
        }
    }
}
