package org.codehaus.jackson.map.ser;

import org.codehaus.jackson.map.TypeSerializer;

/* JADX INFO: loaded from: classes.dex */
public abstract class ContainerSerializerBase<T> extends SerializerBase<T> {
    public abstract ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer typeSerializer);

    protected ContainerSerializerBase(Class<T> t) {
        super(t);
    }

    protected ContainerSerializerBase(Class<?> t, boolean dummy) {
        super(t, dummy);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public ContainerSerializerBase<?> withValueTypeSerializer(TypeSerializer vts) {
        return vts == null ? this : _withValueTypeSerializer(vts);
    }
}
