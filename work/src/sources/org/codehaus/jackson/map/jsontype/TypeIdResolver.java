package org.codehaus.jackson.map.jsontype;

import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.type.JavaType;

/* JADX INFO: loaded from: classes.dex */
public interface TypeIdResolver {
    JsonTypeInfo.Id getMechanism();

    String idFromValue(Object obj);

    String idFromValueAndType(Object obj, Class<?> cls);

    void init(JavaType javaType);

    JavaType typeFromId(String str);
}
