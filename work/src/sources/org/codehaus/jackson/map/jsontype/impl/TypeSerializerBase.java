package org.codehaus.jackson.map.jsontype.impl;

import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.jsontype.TypeIdResolver;

/* JADX INFO: loaded from: classes.dex */
public abstract class TypeSerializerBase extends TypeSerializer {
    protected final TypeIdResolver _idResolver;
    protected final BeanProperty _property;

    @Override // org.codehaus.jackson.map.TypeSerializer
    public abstract JsonTypeInfo.As getTypeInclusion();

    protected TypeSerializerBase(TypeIdResolver idRes, BeanProperty property) {
        this._idResolver = idRes;
        this._property = property;
    }

    @Override // org.codehaus.jackson.map.TypeSerializer
    public String getPropertyName() {
        return null;
    }

    @Override // org.codehaus.jackson.map.TypeSerializer
    public TypeIdResolver getTypeIdResolver() {
        return this._idResolver;
    }
}
