package org.codehaus.jackson.map;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import org.codehaus.jackson.FormatSchema;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.Versioned;
import org.codehaus.jackson.map.deser.StdDeserializationContext;
import org.codehaus.jackson.map.type.SimpleType;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.NullNode;
import org.codehaus.jackson.node.TreeTraversingParser;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jackson.util.VersionUtil;

/* JADX INFO: loaded from: classes.dex */
public class ObjectReader extends ObjectCodec implements Versioned {
    private static final JavaType JSON_NODE_TYPE = SimpleType.constructUnsafe(JsonNode.class);
    protected final DeserializationConfig _config;
    protected final JsonFactory _jsonFactory;
    protected final DeserializerProvider _provider;
    protected final ConcurrentHashMap<JavaType, JsonDeserializer<Object>> _rootDeserializers;
    protected final FormatSchema _schema;
    protected final Object _valueToUpdate;
    protected final JavaType _valueType;

    protected ObjectReader(ObjectMapper mapper, DeserializationConfig config) {
        this(mapper, config, (JavaType) null, (Object) null, (FormatSchema) null);
    }

    protected ObjectReader(ObjectMapper mapper, DeserializationConfig config, JavaType valueType, Object valueToUpdate, FormatSchema schema) {
        this._config = config;
        this._rootDeserializers = mapper._rootDeserializers;
        this._provider = mapper._deserializerProvider;
        this._jsonFactory = mapper._jsonFactory;
        this._valueType = valueType;
        this._valueToUpdate = valueToUpdate;
        if (valueToUpdate != null && valueType.isArrayType()) {
            throw new IllegalArgumentException("Can not update an array value");
        }
        this._schema = schema;
    }

    protected ObjectReader(ObjectReader base, DeserializationConfig config, JavaType valueType, Object valueToUpdate, FormatSchema schema) {
        this._config = config;
        this._rootDeserializers = base._rootDeserializers;
        this._provider = base._provider;
        this._jsonFactory = base._jsonFactory;
        this._valueType = valueType;
        this._valueToUpdate = valueToUpdate;
        if (valueToUpdate != null && valueType.isArrayType()) {
            throw new IllegalArgumentException("Can not update an array value");
        }
        this._schema = schema;
    }

    @Override // org.codehaus.jackson.Versioned
    public Version version() {
        return VersionUtil.versionFor(getClass());
    }

    public ObjectReader withType(JavaType valueType) {
        return valueType == this._valueType ? this : new ObjectReader(this, this._config, valueType, this._valueToUpdate, this._schema);
    }

    public ObjectReader withType(Class<?> valueType) {
        return withType(this._config.constructType(valueType));
    }

    public ObjectReader withType(Type valueType) {
        return withType(this._config.getTypeFactory().constructType(valueType));
    }

    public ObjectReader withType(TypeReference<?> valueTypeRef) {
        return withType(this._config.getTypeFactory().constructType(valueTypeRef.getType()));
    }

    public ObjectReader withNodeFactory(JsonNodeFactory f) {
        return f == this._config.getNodeFactory() ? this : new ObjectReader(this, this._config.withNodeFactory(f), this._valueType, this._valueToUpdate, this._schema);
    }

    public ObjectReader withValueToUpdate(Object value) {
        if (value != this._valueToUpdate) {
            if (value == null) {
                throw new IllegalArgumentException("cat not update null value");
            }
            JavaType t = this._config.constructType(value.getClass());
            return new ObjectReader(this, this._config, t, value, this._schema);
        }
        return this;
    }

    public ObjectReader withSchema(FormatSchema schema) {
        return this._schema == schema ? this : new ObjectReader(this, this._config, this._valueType, this._valueToUpdate, schema);
    }

    public <T> T readValue(JsonParser jsonParser) throws IOException {
        return (T) _bind(jsonParser);
    }

    @Override // org.codehaus.jackson.ObjectCodec
    public JsonNode readTree(JsonParser jp) throws IOException {
        return _bindAsTree(jp);
    }

    public <T> T readValue(InputStream inputStream) throws IOException {
        return (T) _bindAndClose(this._jsonFactory.createJsonParser(inputStream));
    }

    public <T> T readValue(Reader reader) throws IOException {
        return (T) _bindAndClose(this._jsonFactory.createJsonParser(reader));
    }

    public <T> T readValue(String str) throws IOException {
        return (T) _bindAndClose(this._jsonFactory.createJsonParser(str));
    }

    public <T> T readValue(byte[] bArr) throws IOException {
        return (T) _bindAndClose(this._jsonFactory.createJsonParser(bArr));
    }

    public <T> T readValue(byte[] bArr, int i, int i2) throws IOException {
        return (T) _bindAndClose(this._jsonFactory.createJsonParser(bArr, i, i2));
    }

    public <T> T readValue(File file) throws IOException {
        return (T) _bindAndClose(this._jsonFactory.createJsonParser(file));
    }

    public <T> T readValue(URL url) throws IOException {
        return (T) _bindAndClose(this._jsonFactory.createJsonParser(url));
    }

    public <T> T readValue(JsonNode jsonNode) throws IOException {
        return (T) _bindAndClose(treeAsTokens(jsonNode));
    }

    public JsonNode readTree(InputStream in) throws IOException {
        return _bindAndCloseAsTree(this._jsonFactory.createJsonParser(in));
    }

    public JsonNode readTree(Reader r) throws IOException {
        return _bindAndCloseAsTree(this._jsonFactory.createJsonParser(r));
    }

    public JsonNode readTree(String content) throws IOException {
        return _bindAndCloseAsTree(this._jsonFactory.createJsonParser(content));
    }

    public <T> MappingIterator<T> readValues(JsonParser jp) throws IOException {
        DeserializationContext ctxt = _createDeserializationContext(jp, this._config);
        return new MappingIterator<>(this._valueType, jp, ctxt, _findRootDeserializer(this._config, this._valueType));
    }

    public <T> MappingIterator<T> readValues(InputStream src) throws IOException {
        JsonParser jp = this._jsonFactory.createJsonParser(src);
        if (this._schema != null) {
            jp.setSchema(this._schema);
        }
        DeserializationContext ctxt = _createDeserializationContext(jp, this._config);
        return new MappingIterator<>(this._valueType, jp, ctxt, _findRootDeserializer(this._config, this._valueType));
    }

    public <T> MappingIterator<T> readValues(Reader src) throws IOException {
        JsonParser jp = this._jsonFactory.createJsonParser(src);
        if (this._schema != null) {
            jp.setSchema(this._schema);
        }
        DeserializationContext ctxt = _createDeserializationContext(jp, this._config);
        return new MappingIterator<>(this._valueType, jp, ctxt, _findRootDeserializer(this._config, this._valueType));
    }

    public <T> MappingIterator<T> readValues(String json) throws IOException {
        JsonParser jp = this._jsonFactory.createJsonParser(json);
        if (this._schema != null) {
            jp.setSchema(this._schema);
        }
        DeserializationContext ctxt = _createDeserializationContext(jp, this._config);
        return new MappingIterator<>(this._valueType, jp, ctxt, _findRootDeserializer(this._config, this._valueType));
    }

    public <T> MappingIterator<T> readValues(byte[] src, int offset, int length) throws IOException {
        JsonParser jp = this._jsonFactory.createJsonParser(src, offset, length);
        if (this._schema != null) {
            jp.setSchema(this._schema);
        }
        DeserializationContext ctxt = _createDeserializationContext(jp, this._config);
        return new MappingIterator<>(this._valueType, jp, ctxt, _findRootDeserializer(this._config, this._valueType));
    }

    public <T> MappingIterator<T> readValues(File src) throws IOException {
        JsonParser jp = this._jsonFactory.createJsonParser(src);
        if (this._schema != null) {
            jp.setSchema(this._schema);
        }
        DeserializationContext ctxt = _createDeserializationContext(jp, this._config);
        return new MappingIterator<>(this._valueType, jp, ctxt, _findRootDeserializer(this._config, this._valueType));
    }

    public <T> MappingIterator<T> readValues(URL src) throws IOException {
        JsonParser jp = this._jsonFactory.createJsonParser(src);
        if (this._schema != null) {
            jp.setSchema(this._schema);
        }
        DeserializationContext ctxt = _createDeserializationContext(jp, this._config);
        return new MappingIterator<>(this._valueType, jp, ctxt, _findRootDeserializer(this._config, this._valueType));
    }

    protected Object _bind(JsonParser jp) throws IOException {
        Object result;
        JsonToken t = _initForReading(jp);
        if (t == JsonToken.VALUE_NULL || t == JsonToken.END_ARRAY || t == JsonToken.END_OBJECT) {
            result = this._valueToUpdate;
        } else {
            DeserializationContext ctxt = _createDeserializationContext(jp, this._config);
            if (this._valueToUpdate == null) {
                result = _findRootDeserializer(this._config, this._valueType).deserialize(jp, ctxt);
            } else {
                _findRootDeserializer(this._config, this._valueType).deserialize(jp, ctxt, this._valueToUpdate);
                result = this._valueToUpdate;
            }
        }
        jp.clearCurrentToken();
        return result;
    }

    protected Object _bindAndClose(JsonParser jp) throws IOException {
        Object result;
        if (this._schema != null) {
            jp.setSchema(this._schema);
        }
        try {
            JsonToken t = _initForReading(jp);
            if (t == JsonToken.VALUE_NULL || t == JsonToken.END_ARRAY || t == JsonToken.END_OBJECT) {
                result = this._valueToUpdate;
            } else {
                DeserializationContext ctxt = _createDeserializationContext(jp, this._config);
                if (this._valueToUpdate == null) {
                    result = _findRootDeserializer(this._config, this._valueType).deserialize(jp, ctxt);
                } else {
                    _findRootDeserializer(this._config, this._valueType).deserialize(jp, ctxt, this._valueToUpdate);
                    result = this._valueToUpdate;
                }
            }
            return result;
        } finally {
            try {
                jp.close();
            } catch (IOException e) {
            }
        }
    }

    protected JsonNode _bindAsTree(JsonParser jp) throws IOException {
        JsonNode result;
        JsonToken t = _initForReading(jp);
        if (t == JsonToken.VALUE_NULL || t == JsonToken.END_ARRAY || t == JsonToken.END_OBJECT) {
            result = NullNode.instance;
        } else {
            DeserializationContext ctxt = _createDeserializationContext(jp, this._config);
            result = (JsonNode) _findRootDeserializer(this._config, JSON_NODE_TYPE).deserialize(jp, ctxt);
        }
        jp.clearCurrentToken();
        return result;
    }

    protected JsonNode _bindAndCloseAsTree(JsonParser jp) throws IOException {
        if (this._schema != null) {
            jp.setSchema(this._schema);
        }
        try {
            return _bindAsTree(jp);
        } finally {
            try {
                jp.close();
            } catch (IOException e) {
            }
        }
    }

    protected static JsonToken _initForReading(JsonParser jp) throws IOException {
        JsonToken t = jp.getCurrentToken();
        if (t == null && (t = jp.nextToken()) == null) {
            throw new EOFException("No content to map to Object due to end of input");
        }
        return t;
    }

    protected JsonDeserializer<Object> _findRootDeserializer(DeserializationConfig cfg, JavaType valueType) throws JsonMappingException {
        JsonDeserializer<Object> deser = this._rootDeserializers.get(valueType);
        if (deser != null) {
            return deser;
        }
        JsonDeserializer<Object> deser2 = this._provider.findTypedValueDeserializer(cfg, valueType, null);
        if (deser2 == null) {
            throw new JsonMappingException("Can not find a deserializer for type " + valueType);
        }
        this._rootDeserializers.put(valueType, deser2);
        return deser2;
    }

    protected DeserializationContext _createDeserializationContext(JsonParser jp, DeserializationConfig cfg) {
        return new StdDeserializationContext(cfg, jp, this._provider);
    }

    @Override // org.codehaus.jackson.ObjectCodec
    public JsonNode createArrayNode() {
        return this._config.getNodeFactory().arrayNode();
    }

    @Override // org.codehaus.jackson.ObjectCodec
    public JsonNode createObjectNode() {
        return this._config.getNodeFactory().objectNode();
    }

    @Override // org.codehaus.jackson.ObjectCodec
    public <T> T readValue(JsonParser jsonParser, Class<T> cls) throws IOException {
        return (T) withType((Class<?>) cls).readValue(jsonParser);
    }

    @Override // org.codehaus.jackson.ObjectCodec
    public <T> T readValue(JsonParser jsonParser, TypeReference<?> typeReference) throws IOException {
        return (T) withType(typeReference).readValue(jsonParser);
    }

    @Override // org.codehaus.jackson.ObjectCodec
    public <T> T readValue(JsonParser jsonParser, JavaType javaType) throws IOException {
        return (T) withType(javaType).readValue(jsonParser);
    }

    @Override // org.codehaus.jackson.ObjectCodec
    public JsonParser treeAsTokens(JsonNode n) {
        return new TreeTraversingParser(n, this);
    }

    @Override // org.codehaus.jackson.ObjectCodec
    public <T> T treeToValue(JsonNode jsonNode, Class<T> cls) throws IOException {
        return (T) readValue(treeAsTokens(jsonNode), cls);
    }

    @Override // org.codehaus.jackson.ObjectCodec
    public void writeTree(JsonGenerator jgen, JsonNode rootNode) throws IOException {
        throw new UnsupportedOperationException("Not implemented for ObjectReader");
    }

    @Override // org.codehaus.jackson.ObjectCodec
    public void writeValue(JsonGenerator jgen, Object value) throws IOException {
        throw new UnsupportedOperationException("Not implemented for ObjectReader");
    }
}
