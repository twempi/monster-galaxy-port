package org.codehaus.jackson.map.jsontype.impl;

import org.codehaus.jackson.map.jsontype.TypeIdResolver;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;

/* JADX INFO: loaded from: classes.dex */
public abstract class TypeIdResolverBase implements TypeIdResolver {
    protected final JavaType _baseType;
    protected final TypeFactory _typeFactory;

    protected TypeIdResolverBase(JavaType baseType, TypeFactory typeFactory) {
        this._baseType = baseType;
        this._typeFactory = typeFactory;
    }

    @Override // org.codehaus.jackson.map.jsontype.TypeIdResolver
    public void init(JavaType bt) {
    }
}
