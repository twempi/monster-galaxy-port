package org.codehaus.jackson.map.type;

import java.util.Collection;
import org.codehaus.jackson.type.JavaType;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class CollectionLikeType extends TypeBase {
    protected final JavaType _elementType;

    protected CollectionLikeType(Class<?> collT, JavaType elemT) {
        super(collT, elemT.hashCode());
        this._elementType = elemT;
    }

    @Override // org.codehaus.jackson.type.JavaType
    protected JavaType _narrow(Class<?> subclass) {
        return new CollectionLikeType(subclass, this._elementType);
    }

    @Override // org.codehaus.jackson.type.JavaType
    public JavaType narrowContentsBy(Class<?> contentClass) {
        return contentClass == this._elementType.getRawClass() ? this : new CollectionLikeType(this._class, this._elementType.narrowBy(contentClass)).copyHandlers(this);
    }

    @Override // org.codehaus.jackson.type.JavaType
    public JavaType widenContentsBy(Class<?> contentClass) {
        return contentClass == this._elementType.getRawClass() ? this : new CollectionLikeType(this._class, this._elementType.widenBy(contentClass)).copyHandlers(this);
    }

    public static CollectionLikeType construct(Class<?> rawType, JavaType elemT) {
        return new CollectionLikeType(rawType, elemT);
    }

    @Override // org.codehaus.jackson.type.JavaType
    public CollectionLikeType withTypeHandler(Object h) {
        CollectionLikeType newInstance = new CollectionLikeType(this._class, this._elementType);
        newInstance._typeHandler = h;
        return newInstance;
    }

    @Override // org.codehaus.jackson.type.JavaType
    public CollectionLikeType withContentTypeHandler(Object h) {
        return new CollectionLikeType(this._class, this._elementType.withTypeHandler(h));
    }

    @Override // org.codehaus.jackson.type.JavaType
    public boolean isContainerType() {
        return true;
    }

    @Override // org.codehaus.jackson.type.JavaType
    public boolean isCollectionLikeType() {
        return true;
    }

    @Override // org.codehaus.jackson.type.JavaType
    public JavaType getContentType() {
        return this._elementType;
    }

    @Override // org.codehaus.jackson.type.JavaType
    public int containedTypeCount() {
        return 1;
    }

    @Override // org.codehaus.jackson.type.JavaType
    public JavaType containedType(int index) {
        if (index == 0) {
            return this._elementType;
        }
        return null;
    }

    @Override // org.codehaus.jackson.type.JavaType
    public String containedTypeName(int index) {
        if (index == 0) {
            return "E";
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
        this._elementType.getGenericSignature(sb);
        sb.append(">;");
        return sb;
    }

    @Override // org.codehaus.jackson.map.type.TypeBase
    protected String buildCanonicalName() {
        StringBuilder sb = new StringBuilder();
        sb.append(this._class.getName());
        if (this._elementType != null) {
            sb.append('<');
            sb.append(this._elementType.toCanonical());
            sb.append('>');
        }
        return sb.toString();
    }

    public boolean isTrueCollectionType() {
        return Collection.class.isAssignableFrom(this._class);
    }

    @Override // org.codehaus.jackson.type.JavaType
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o != null && o.getClass() == getClass()) {
            CollectionLikeType other = (CollectionLikeType) o;
            return this._class == other._class && this._elementType.equals(other._elementType);
        }
        return false;
    }

    @Override // org.codehaus.jackson.type.JavaType
    public String toString() {
        return "[collection-like type; class " + this._class.getName() + ", contains " + this._elementType + "]";
    }
}
