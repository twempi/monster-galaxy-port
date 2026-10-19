package com.google.gson;

import java.lang.reflect.Type;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
final class ObjectTypePair {
    private Object obj;
    private final boolean preserveType;
    final Type type;

    ObjectTypePair(Object obj, Type type, boolean preserveType) {
        this.obj = obj;
        this.type = type;
        this.preserveType = preserveType;
    }

    Object getObject() {
        return this.obj;
    }

    void setObject(Object obj) {
        this.obj = obj;
    }

    Type getType() {
        return this.type;
    }

    public String toString() {
        return String.format("preserveType: %b, type: %s, obj: %s", Boolean.valueOf(this.preserveType), this.type, this.obj);
    }

    <HANDLER> Pair<HANDLER, ObjectTypePair> getMatchingHandler(ParameterizedTypeHandlerMap<HANDLER> handlers) {
        if (!this.preserveType && this.obj != null) {
            ObjectTypePair moreSpecificType = toMoreSpecificType();
            HANDLER handler = handlers.getHandlerFor(moreSpecificType.type);
            if (handler != null) {
                return new Pair<>(handler, moreSpecificType);
            }
        }
        HANDLER handler2 = handlers.getHandlerFor(this.type);
        if (handler2 == null) {
            return null;
        }
        return new Pair<>(handler2, this);
    }

    ObjectTypePair toMoreSpecificType() {
        Type actualType;
        return (this.preserveType || this.obj == null || (actualType = getActualTypeIfMoreSpecific(this.type, this.obj.getClass())) == this.type) ? this : new ObjectTypePair(this.obj, actualType, this.preserveType);
    }

    Type getMoreSpecificType() {
        return (this.preserveType || this.obj == null) ? this.type : getActualTypeIfMoreSpecific(this.type, this.obj.getClass());
    }

    static Type getActualTypeIfMoreSpecific(Type type, Class<?> actualClass) {
        if (type instanceof Class) {
            Class<?> typeAsClass = (Class) type;
            if (typeAsClass.isAssignableFrom(actualClass)) {
                type = actualClass;
            }
            if (type == Object.class) {
                return actualClass;
            }
            return type;
        }
        return type;
    }

    public int hashCode() {
        if (this.obj == null) {
            return 31;
        }
        return this.obj.hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ObjectTypePair other = (ObjectTypePair) obj;
        if (this.obj == null) {
            if (other.obj != null) {
                return false;
            }
        } else if (this.obj != other.obj) {
            return false;
        }
        if (this.type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!this.type.equals(other.type)) {
            return false;
        }
        return this.preserveType == other.preserveType;
    }

    public boolean isPreserveType() {
        return this.preserveType;
    }
}
