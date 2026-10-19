package org.codehaus.jackson.map.type;

import org.codehaus.jackson.type.JavaType;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class CollectionType extends CollectionLikeType {
    private CollectionType(Class<?> collT, JavaType elemT) {
        super(collT, elemT);
    }

    @Override // org.codehaus.jackson.map.type.CollectionLikeType, org.codehaus.jackson.type.JavaType
    protected JavaType _narrow(Class<?> subclass) {
        return new CollectionType(subclass, this._elementType);
    }

    @Override // org.codehaus.jackson.map.type.CollectionLikeType, org.codehaus.jackson.type.JavaType
    public JavaType narrowContentsBy(Class<?> contentClass) {
        return contentClass == this._elementType.getRawClass() ? this : new CollectionType(this._class, this._elementType.narrowBy(contentClass)).copyHandlers(this);
    }

    @Override // org.codehaus.jackson.map.type.CollectionLikeType, org.codehaus.jackson.type.JavaType
    public JavaType widenContentsBy(Class<?> contentClass) {
        return contentClass == this._elementType.getRawClass() ? this : new CollectionType(this._class, this._elementType.widenBy(contentClass)).copyHandlers(this);
    }

    public static CollectionType construct(Class<?> rawType, JavaType elemT) {
        return new CollectionType(rawType, elemT);
    }

    @Override // org.codehaus.jackson.map.type.CollectionLikeType, org.codehaus.jackson.type.JavaType
    public CollectionType withTypeHandler(Object h) {
        CollectionType newInstance = new CollectionType(this._class, this._elementType);
        newInstance._typeHandler = h;
        return newInstance;
    }

    @Override // org.codehaus.jackson.map.type.CollectionLikeType, org.codehaus.jackson.type.JavaType
    public CollectionType withContentTypeHandler(Object h) {
        return new CollectionType(this._class, this._elementType.withTypeHandler(h));
    }

    @Override // org.codehaus.jackson.map.type.CollectionLikeType, org.codehaus.jackson.type.JavaType
    public String toString() {
        return "[collection type; class " + this._class.getName() + ", contains " + this._elementType + "]";
    }
}
