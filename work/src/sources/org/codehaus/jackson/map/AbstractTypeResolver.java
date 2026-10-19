package org.codehaus.jackson.map;

import org.codehaus.jackson.type.JavaType;

/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractTypeResolver {
    public JavaType findTypeMapping(DeserializationConfig config, JavaType type) {
        return null;
    }

    public JavaType resolveAbstractType(DeserializationConfig config, JavaType type) {
        return null;
    }
}
