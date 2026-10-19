package org.codehaus.jackson.map.deser;

import java.io.IOException;
import java.lang.reflect.Array;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.annotate.JacksonStdImpl;
import org.codehaus.jackson.map.type.ArrayType;
import org.codehaus.jackson.map.util.ObjectBuffer;
import org.codehaus.jackson.type.JavaType;

/* JADX INFO: loaded from: classes.dex */
@JacksonStdImpl
public class ArrayDeserializer extends ContainerDeserializer<Object[]> {
    protected final JavaType _arrayType;
    protected final Class<?> _elementClass;
    protected final JsonDeserializer<Object> _elementDeserializer;
    final TypeDeserializer _elementTypeDeserializer;
    protected final boolean _untyped;

    @Deprecated
    public ArrayDeserializer(ArrayType arrayType, JsonDeserializer<Object> elemDeser) {
        this(arrayType, elemDeser, null);
    }

    public ArrayDeserializer(ArrayType arrayType, JsonDeserializer<Object> elemDeser, TypeDeserializer elemTypeDeser) {
        super(Object[].class);
        this._arrayType = arrayType;
        this._elementClass = arrayType.getContentType().getRawClass();
        this._untyped = this._elementClass == Object.class;
        this._elementDeserializer = elemDeser;
        this._elementTypeDeserializer = elemTypeDeser;
    }

    @Override // org.codehaus.jackson.map.deser.ContainerDeserializer
    public JavaType getContentType() {
        return this._arrayType.getContentType();
    }

    @Override // org.codehaus.jackson.map.deser.ContainerDeserializer
    public JsonDeserializer<Object> getContentDeserializer() {
        return this._elementDeserializer;
    }

    @Override // org.codehaus.jackson.map.JsonDeserializer
    public Object[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        Object[] result;
        Object objDeserializeWithType;
        if (!jp.isExpectedStartArrayToken()) {
            return handleNonArray(jp, ctxt);
        }
        ObjectBuffer buffer = ctxt.leaseObjectBuffer();
        Object[] chunk = buffer.resetAndStart();
        int ix = 0;
        TypeDeserializer typeDeser = this._elementTypeDeserializer;
        while (true) {
            JsonToken t = jp.nextToken();
            if (t == JsonToken.END_ARRAY) {
                break;
            }
            if (t == JsonToken.VALUE_NULL) {
                objDeserializeWithType = null;
            } else if (typeDeser == null) {
                objDeserializeWithType = this._elementDeserializer.deserialize(jp, ctxt);
            } else {
                objDeserializeWithType = this._elementDeserializer.deserializeWithType(jp, ctxt, typeDeser);
            }
            if (ix >= chunk.length) {
                chunk = buffer.appendCompletedChunk(chunk);
                ix = 0;
            }
            chunk[ix] = objDeserializeWithType;
            ix++;
        }
        if (this._untyped) {
            result = buffer.completeAndClearBuffer(chunk, ix);
        } else {
            result = buffer.completeAndClearBuffer(chunk, ix, this._elementClass);
        }
        ctxt.returnObjectBuffer(buffer);
        return result;
    }

    @Override // org.codehaus.jackson.map.deser.StdDeserializer, org.codehaus.jackson.map.JsonDeserializer
    public Object[] deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
        return (Object[]) typeDeserializer.deserializeTypedFromArray(jp, ctxt);
    }

    protected Byte[] deserializeFromBase64(JsonParser jp, DeserializationContext ctxt) throws IOException {
        byte[] b = jp.getBinaryValue(ctxt.getBase64Variant());
        Byte[] result = new Byte[b.length];
        int len = b.length;
        for (int i = 0; i < len; i++) {
            result[i] = Byte.valueOf(b[i]);
        }
        return result;
    }

    private final Object[] handleNonArray(JsonParser jp, DeserializationContext ctxt) throws IOException {
        Object objDeserializeWithType;
        Object[] result;
        if (!ctxt.isEnabled(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
            if (jp.getCurrentToken() == JsonToken.VALUE_STRING && this._elementClass == Byte.class) {
                return deserializeFromBase64(jp, ctxt);
            }
            throw ctxt.mappingException(this._arrayType.getRawClass());
        }
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.VALUE_NULL) {
            objDeserializeWithType = null;
        } else if (this._elementTypeDeserializer == null) {
            objDeserializeWithType = this._elementDeserializer.deserialize(jp, ctxt);
        } else {
            objDeserializeWithType = this._elementDeserializer.deserializeWithType(jp, ctxt, this._elementTypeDeserializer);
        }
        if (this._untyped) {
            result = new Object[1];
        } else {
            result = (Object[]) Array.newInstance(this._elementClass, 1);
        }
        result[0] = objDeserializeWithType;
        return result;
    }
}
