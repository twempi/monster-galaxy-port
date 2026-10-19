package org.codehaus.jackson.map.jsontype.impl;

import java.util.EnumMap;
import java.util.EnumSet;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.type.JavaType;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class ClassNameIdResolver extends TypeIdResolverBase {
    public ClassNameIdResolver(JavaType baseType, TypeFactory typeFactory) {
        super(baseType, typeFactory);
    }

    @Override // org.codehaus.jackson.map.jsontype.TypeIdResolver
    public JsonTypeInfo.Id getMechanism() {
        return JsonTypeInfo.Id.CLASS;
    }

    public void registerSubtype(Class<?> type, String name) {
    }

    @Override // org.codehaus.jackson.map.jsontype.TypeIdResolver
    public String idFromValue(Object value) {
        return _idFrom(value, value.getClass());
    }

    @Override // org.codehaus.jackson.map.jsontype.TypeIdResolver
    public String idFromValueAndType(Object value, Class<?> type) {
        return _idFrom(value, type);
    }

    @Override // org.codehaus.jackson.map.jsontype.TypeIdResolver
    public JavaType typeFromId(String id) {
        if (id.indexOf(60) > 0) {
            return TypeFactory.fromCanonical(id);
        }
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            Class<?> cls = Class.forName(id, true, loader);
            return this._typeFactory.constructSpecializedType(this._baseType, cls);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Invalid type id '" + id + "' (for id type 'Id.class'): no such class found");
        } catch (Exception e2) {
            throw new IllegalArgumentException("Invalid type id '" + id + "' (for id type 'Id.class'): " + e2.getMessage(), e2);
        }
    }

    protected final String _idFrom(Object value, Class<?> cls) {
        if (Enum.class.isAssignableFrom(cls) && !cls.isEnum()) {
            cls = cls.getSuperclass();
        }
        String str = cls.getName();
        if (str.startsWith("java.util")) {
            if (value instanceof EnumSet) {
                Class<?> enumClass = ClassUtil.findEnumType((EnumSet<?>) value);
                return TypeFactory.defaultInstance().constructCollectionType(EnumSet.class, enumClass).toCanonical();
            }
            if (value instanceof EnumMap) {
                Class<?> enumClass2 = ClassUtil.findEnumType((EnumMap<?, ?>) value);
                return TypeFactory.defaultInstance().constructMapType(EnumMap.class, enumClass2, Object.class).toCanonical();
            }
            String end = str.substring(9);
            if ((end.startsWith(".Arrays$") || end.startsWith(".Collections$")) && str.indexOf("List") >= 0) {
                return "java.util.ArrayList";
            }
            return str;
        }
        return str;
    }
}
