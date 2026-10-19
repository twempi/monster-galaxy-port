package org.codehaus.jackson.map.type;

import org.codehaus.jackson.type.JavaType;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class MapType extends MapLikeType {
    private MapType(Class<?> mapType, JavaType keyT, JavaType valueT) {
        super(mapType, keyT, valueT);
    }

    public static MapType construct(Class<?> rawType, JavaType keyT, JavaType valueT) {
        return new MapType(rawType, keyT, valueT);
    }

    @Override // org.codehaus.jackson.map.type.MapLikeType, org.codehaus.jackson.type.JavaType
    protected JavaType _narrow(Class<?> subclass) {
        return new MapType(subclass, this._keyType, this._valueType);
    }

    @Override // org.codehaus.jackson.map.type.MapLikeType, org.codehaus.jackson.type.JavaType
    public JavaType narrowContentsBy(Class<?> contentClass) {
        return contentClass == this._valueType.getRawClass() ? this : new MapType(this._class, this._keyType, this._valueType.narrowBy(contentClass)).copyHandlers(this);
    }

    @Override // org.codehaus.jackson.map.type.MapLikeType, org.codehaus.jackson.type.JavaType
    public JavaType widenContentsBy(Class<?> contentClass) {
        return contentClass == this._valueType.getRawClass() ? this : new MapType(this._class, this._keyType, this._valueType.widenBy(contentClass)).copyHandlers(this);
    }

    @Override // org.codehaus.jackson.map.type.MapLikeType
    public JavaType narrowKey(Class<?> keySubclass) {
        return keySubclass == this._keyType.getRawClass() ? this : new MapType(this._class, this._keyType.narrowBy(keySubclass), this._valueType).copyHandlers(this);
    }

    @Override // org.codehaus.jackson.map.type.MapLikeType
    public JavaType widenKey(Class<?> keySubclass) {
        return keySubclass == this._keyType.getRawClass() ? this : new MapType(this._class, this._keyType.widenBy(keySubclass), this._valueType).copyHandlers(this);
    }

    @Override // org.codehaus.jackson.map.type.MapLikeType, org.codehaus.jackson.type.JavaType
    public MapType withTypeHandler(Object h) {
        MapType newInstance = new MapType(this._class, this._keyType, this._valueType);
        newInstance._typeHandler = h;
        return newInstance;
    }

    @Override // org.codehaus.jackson.map.type.MapLikeType, org.codehaus.jackson.type.JavaType
    public MapType withContentTypeHandler(Object h) {
        return new MapType(this._class, this._keyType, this._valueType.withTypeHandler(h));
    }

    @Override // org.codehaus.jackson.map.type.MapLikeType, org.codehaus.jackson.type.JavaType
    public String toString() {
        return "[map type; class " + this._class.getName() + ", " + this._keyType + " -> " + this._valueType + "]";
    }
}
