package org.codehaus.jackson.map.type;

import java.util.Map;
import org.codehaus.jackson.type.JavaType;

/* JADX INFO: loaded from: classes.dex */
public class MapLikeType extends TypeBase {
    protected final JavaType _keyType;
    protected final JavaType _valueType;

    protected MapLikeType(Class<?> mapType, JavaType keyT, JavaType valueT) {
        super(mapType, keyT.hashCode() ^ valueT.hashCode());
        this._keyType = keyT;
        this._valueType = valueT;
    }

    public static MapLikeType construct(Class<?> rawType, JavaType keyT, JavaType valueT) {
        return new MapLikeType(rawType, keyT, valueT);
    }

    @Override // org.codehaus.jackson.type.JavaType
    protected JavaType _narrow(Class<?> subclass) {
        return new MapLikeType(subclass, this._keyType, this._valueType);
    }

    @Override // org.codehaus.jackson.type.JavaType
    public JavaType narrowContentsBy(Class<?> contentClass) {
        return contentClass == this._valueType.getRawClass() ? this : new MapLikeType(this._class, this._keyType, this._valueType.narrowBy(contentClass)).copyHandlers(this);
    }

    @Override // org.codehaus.jackson.type.JavaType
    public JavaType widenContentsBy(Class<?> contentClass) {
        return contentClass == this._valueType.getRawClass() ? this : new MapLikeType(this._class, this._keyType, this._valueType.widenBy(contentClass)).copyHandlers(this);
    }

    public JavaType narrowKey(Class<?> keySubclass) {
        return keySubclass == this._keyType.getRawClass() ? this : new MapLikeType(this._class, this._keyType.narrowBy(keySubclass), this._valueType).copyHandlers(this);
    }

    public JavaType widenKey(Class<?> keySubclass) {
        return keySubclass == this._keyType.getRawClass() ? this : new MapLikeType(this._class, this._keyType.widenBy(keySubclass), this._valueType).copyHandlers(this);
    }

    @Override // org.codehaus.jackson.type.JavaType
    public MapLikeType withTypeHandler(Object h) {
        MapLikeType newInstance = new MapLikeType(this._class, this._keyType, this._valueType);
        newInstance._typeHandler = h;
        return newInstance;
    }

    @Override // org.codehaus.jackson.type.JavaType
    public MapLikeType withContentTypeHandler(Object h) {
        return new MapLikeType(this._class, this._keyType, this._valueType.withTypeHandler(h));
    }

    @Override // org.codehaus.jackson.map.type.TypeBase
    protected String buildCanonicalName() {
        StringBuilder sb = new StringBuilder();
        sb.append(this._class.getName());
        if (this._keyType != null) {
            sb.append('<');
            sb.append(this._keyType.toCanonical());
            sb.append(',');
            sb.append(this._valueType.toCanonical());
            sb.append('>');
        }
        return sb.toString();
    }

    @Override // org.codehaus.jackson.type.JavaType
    public boolean isContainerType() {
        return true;
    }

    @Override // org.codehaus.jackson.type.JavaType
    public boolean isMapLikeType() {
        return true;
    }

    @Override // org.codehaus.jackson.type.JavaType
    public JavaType getKeyType() {
        return this._keyType;
    }

    @Override // org.codehaus.jackson.type.JavaType
    public JavaType getContentType() {
        return this._valueType;
    }

    @Override // org.codehaus.jackson.type.JavaType
    public int containedTypeCount() {
        return 2;
    }

    @Override // org.codehaus.jackson.type.JavaType
    public JavaType containedType(int index) {
        if (index == 0) {
            return this._keyType;
        }
        if (index == 1) {
            return this._valueType;
        }
        return null;
    }

    @Override // org.codehaus.jackson.type.JavaType
    public String containedTypeName(int index) {
        if (index == 0) {
            return "K";
        }
        if (index == 1) {
            return "V";
        }
        return null;
    }

    @Override // org.codehaus.jackson.map.type.TypeBase, org.codehaus.jackson.type.JavaType
    public StringBuilder getErasedSignature(StringBuilder sb) {
        return _classSignature(this._class, sb, true);
    }

    @Override // org.codehaus.jackson.map.type.TypeBase, org.codehaus.jackson.type.JavaType
    public StringBuilder getGenericSignature(StringBuilder sb) {
        _classSignature(this._class, sb, false);
        sb.append('<');
        this._keyType.getGenericSignature(sb);
        this._valueType.getGenericSignature(sb);
        sb.append(">;");
        return sb;
    }

    public boolean isTrueMapType() {
        return Map.class.isAssignableFrom(this._class);
    }

    @Override // org.codehaus.jackson.type.JavaType
    public String toString() {
        return "[map-like type; class " + this._class.getName() + ", " + this._keyType + " -> " + this._valueType + "]";
    }

    @Override // org.codehaus.jackson.type.JavaType
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o != null && o.getClass() == getClass()) {
            MapLikeType other = (MapLikeType) o;
            return this._class == other._class && this._keyType.equals(other._keyType) && this._valueType.equals(other._valueType);
        }
        return false;
    }
}
