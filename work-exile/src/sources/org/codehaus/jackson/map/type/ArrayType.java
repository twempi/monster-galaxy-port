package org.codehaus.jackson.map.type;

import java.lang.reflect.Array;
import org.codehaus.jackson.type.JavaType;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class ArrayType extends TypeBase {
    final JavaType _componentType;
    final Object _emptyArray;

    private ArrayType(JavaType componentType, Object emptyInstance) {
        super(emptyInstance.getClass(), componentType.hashCode());
        this._componentType = componentType;
        this._emptyArray = emptyInstance;
    }

    public static ArrayType construct(JavaType componentType) {
        Object emptyInstance = Array.newInstance(componentType.getRawClass(), 0);
        return new ArrayType(componentType, emptyInstance);
    }

    @Override // org.codehaus.jackson.type.JavaType
    public ArrayType withTypeHandler(Object h) {
        ArrayType newInstance = new ArrayType(this._componentType, this._emptyArray);
        newInstance._typeHandler = h;
        return newInstance;
    }

    @Override // org.codehaus.jackson.type.JavaType
    public ArrayType withContentTypeHandler(Object h) {
        return new ArrayType(this._componentType.withTypeHandler(h), this._emptyArray);
    }

    @Override // org.codehaus.jackson.map.type.TypeBase
    protected String buildCanonicalName() {
        return this._class.getName();
    }

    @Override // org.codehaus.jackson.type.JavaType
    protected JavaType _narrow(Class<?> subclass) {
        if (!subclass.isArray()) {
            throw new IllegalArgumentException("Incompatible narrowing operation: trying to narrow " + toString() + " to class " + subclass.getName());
        }
        Class<?> newCompClass = subclass.getComponentType();
        JavaType newCompType = TypeFactory.defaultInstance().constructType(newCompClass);
        return construct(newCompType);
    }

    @Override // org.codehaus.jackson.type.JavaType
    public JavaType narrowContentsBy(Class<?> contentClass) {
        return contentClass == this._componentType.getRawClass() ? this : construct(this._componentType.narrowBy(contentClass)).copyHandlers(this);
    }

    @Override // org.codehaus.jackson.type.JavaType
    public JavaType widenContentsBy(Class<?> contentClass) {
        return contentClass == this._componentType.getRawClass() ? this : construct(this._componentType.widenBy(contentClass)).copyHandlers(this);
    }

    @Override // org.codehaus.jackson.type.JavaType
    public boolean isArrayType() {
        return true;
    }

    @Override // org.codehaus.jackson.type.JavaType
    public boolean isAbstract() {
        return false;
    }

    @Override // org.codehaus.jackson.type.JavaType
    public boolean isConcrete() {
        return true;
    }

    @Override // org.codehaus.jackson.type.JavaType
    public boolean hasGenericTypes() {
        return this._componentType.hasGenericTypes();
    }

    @Override // org.codehaus.jackson.type.JavaType
    public String containedTypeName(int index) {
        if (index == 0) {
            return "E";
        }
        return null;
    }

    @Override // org.codehaus.jackson.type.JavaType
    public boolean isContainerType() {
        return true;
    }

    @Override // org.codehaus.jackson.type.JavaType
    public JavaType getContentType() {
        return this._componentType;
    }

    @Override // org.codehaus.jackson.type.JavaType
    public int containedTypeCount() {
        return 1;
    }

    @Override // org.codehaus.jackson.type.JavaType
    public JavaType containedType(int index) {
        if (index == 0) {
            return this._componentType;
        }
        return null;
    }

    @Override // org.codehaus.jackson.map.type.TypeBase, org.codehaus.jackson.type.JavaType
    public StringBuilder getGenericSignature(StringBuilder sb) {
        sb.append('[');
        return this._componentType.getGenericSignature(sb);
    }

    @Override // org.codehaus.jackson.map.type.TypeBase, org.codehaus.jackson.type.JavaType
    public StringBuilder getErasedSignature(StringBuilder sb) {
        sb.append('[');
        return this._componentType.getErasedSignature(sb);
    }

    @Override // org.codehaus.jackson.type.JavaType
    public String toString() {
        return "[array type, component type: " + this._componentType + "]";
    }

    @Override // org.codehaus.jackson.type.JavaType
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || o.getClass() != getClass()) {
            return false;
        }
        ArrayType other = (ArrayType) o;
        return this._componentType.equals(other._componentType);
    }
}
