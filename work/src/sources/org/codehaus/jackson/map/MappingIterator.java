package org.codehaus.jackson.map;

import java.io.IOException;
import java.util.Iterator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonStreamContext;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.type.JavaType;

/* JADX INFO: loaded from: classes.dex */
public class MappingIterator<T> implements Iterator<T> {
    protected static final MappingIterator<?> EMPTY_ITERATOR = new MappingIterator<>(null, null, null, null);
    protected final DeserializationContext _context;
    protected final JsonDeserializer<T> _deserializer;
    protected final JsonParser _parser;
    protected final JavaType _type;

    /* JADX WARN: Multi-variable type inference failed */
    protected MappingIterator(JavaType type, JsonParser jp, DeserializationContext ctxt, JsonDeserializer<?> jsonDeserializer) {
        this._type = type;
        this._parser = jp;
        this._context = ctxt;
        this._deserializer = jsonDeserializer;
        if (jp != null && jp.getCurrentToken() == JsonToken.START_ARRAY) {
            JsonStreamContext sc = jp.getParsingContext();
            if (!sc.inRoot()) {
                jp.clearCurrentToken();
            }
        }
    }

    protected static <T> MappingIterator<T> emptyIterator() {
        return (MappingIterator<T>) EMPTY_ITERATOR;
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        try {
            return hasNextValue();
        } catch (JsonMappingException e) {
            throw new RuntimeJsonMappingException(e.getMessage(), e);
        } catch (IOException e2) {
            throw new RuntimeException(e2.getMessage(), e2);
        }
    }

    @Override // java.util.Iterator
    public T next() {
        try {
            return nextValue();
        } catch (JsonMappingException e) {
            throw new RuntimeJsonMappingException(e.getMessage(), e);
        } catch (IOException e2) {
            throw new RuntimeException(e2.getMessage(), e2);
        }
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException();
    }

    public boolean hasNextValue() throws IOException {
        if (this._parser == null) {
            return false;
        }
        if (this._parser.getCurrentToken() == null) {
            JsonToken t = this._parser.nextToken();
            if (t == null) {
                this._parser.close();
                return false;
            }
            if (t == JsonToken.END_ARRAY) {
                return false;
            }
        }
        return true;
    }

    public T nextValue() throws IOException {
        T result = this._deserializer.deserialize(this._parser, this._context);
        this._parser.clearCurrentToken();
        return result;
    }
}
