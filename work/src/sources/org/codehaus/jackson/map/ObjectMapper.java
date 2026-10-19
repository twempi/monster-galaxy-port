package org.codehaus.jackson.map;

import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.net.URL;
import java.text.DateFormat;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import org.codehaus.jackson.FormatSchema;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.PrettyPrinter;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.Versioned;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.io.SegmentedStringWriter;
import org.codehaus.jackson.map.deser.BeanDeserializerModifier;
import org.codehaus.jackson.map.deser.StdDeserializationContext;
import org.codehaus.jackson.map.deser.StdDeserializerProvider;
import org.codehaus.jackson.map.introspect.BasicClassIntrospector;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
import org.codehaus.jackson.map.introspect.VisibilityChecker;
import org.codehaus.jackson.map.jsontype.NamedType;
import org.codehaus.jackson.map.jsontype.SubtypeResolver;
import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
import org.codehaus.jackson.map.jsontype.impl.StdSubtypeResolver;
import org.codehaus.jackson.map.jsontype.impl.StdTypeResolverBuilder;
import org.codehaus.jackson.map.ser.BeanSerializerFactory;
import org.codehaus.jackson.map.ser.BeanSerializerModifier;
import org.codehaus.jackson.map.ser.FilterProvider;
import org.codehaus.jackson.map.ser.StdSerializerProvider;
import org.codehaus.jackson.map.type.SimpleType;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.map.type.TypeModifier;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.NullNode;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.node.TreeTraversingParser;
import org.codehaus.jackson.schema.JsonSchema;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jackson.util.ByteArrayBuilder;
import org.codehaus.jackson.util.DefaultPrettyPrinter;
import org.codehaus.jackson.util.TokenBuffer;
import org.codehaus.jackson.util.VersionUtil;

/* JADX INFO: loaded from: classes.dex */
public class ObjectMapper extends ObjectCodec implements Versioned {
    protected DeserializationConfig _deserializationConfig;
    protected DeserializerProvider _deserializerProvider;
    protected final JsonFactory _jsonFactory;
    protected final ConcurrentHashMap<JavaType, JsonDeserializer<Object>> _rootDeserializers;
    protected SerializationConfig _serializationConfig;
    protected SerializerFactory _serializerFactory;
    protected SerializerProvider _serializerProvider;
    protected SubtypeResolver _subtypeResolver;
    protected TypeFactory _typeFactory;
    private static final JavaType JSON_NODE_TYPE = SimpleType.constructUnsafe(JsonNode.class);
    protected static final ClassIntrospector<? extends BeanDescription> DEFAULT_INTROSPECTOR = BasicClassIntrospector.instance;
    protected static final AnnotationIntrospector DEFAULT_ANNOTATION_INTROSPECTOR = new JacksonAnnotationIntrospector();
    protected static final VisibilityChecker<?> STD_VISIBILITY_CHECKER = VisibilityChecker.Std.defaultInstance();

    public enum DefaultTyping {
        JAVA_LANG_OBJECT,
        OBJECT_AND_NON_CONCRETE,
        NON_CONCRETE_AND_ARRAYS,
        NON_FINAL
    }

    public static class DefaultTypeResolverBuilder extends StdTypeResolverBuilder {
        protected final DefaultTyping _appliesFor;

        public DefaultTypeResolverBuilder(DefaultTyping t) {
            this._appliesFor = t;
        }

        @Override // org.codehaus.jackson.map.jsontype.impl.StdTypeResolverBuilder, org.codehaus.jackson.map.jsontype.TypeResolverBuilder
        public TypeDeserializer buildTypeDeserializer(DeserializationConfig config, JavaType baseType, Collection<NamedType> subtypes, BeanProperty property) {
            if (useForType(baseType)) {
                return super.buildTypeDeserializer(config, baseType, subtypes, property);
            }
            return null;
        }

        @Override // org.codehaus.jackson.map.jsontype.impl.StdTypeResolverBuilder, org.codehaus.jackson.map.jsontype.TypeResolverBuilder
        public TypeSerializer buildTypeSerializer(SerializationConfig config, JavaType baseType, Collection<NamedType> subtypes, BeanProperty property) {
            if (useForType(baseType)) {
                return super.buildTypeSerializer(config, baseType, subtypes, property);
            }
            return null;
        }

        public boolean useForType(JavaType t) {
            switch (this._appliesFor) {
                case NON_CONCRETE_AND_ARRAYS:
                    if (t.isArrayType()) {
                        t = t.getContentType();
                    }
                    break;
                case OBJECT_AND_NON_CONCRETE:
                    break;
                case NON_FINAL:
                    if (t.isArrayType()) {
                        t = t.getContentType();
                    }
                    return !t.isFinal();
                default:
                    return t.getRawClass() == Object.class;
            }
            return t.getRawClass() == Object.class || !t.isConcrete();
        }
    }

    public ObjectMapper() {
        this(null, null, null);
    }

    public ObjectMapper(JsonFactory jf) {
        this(jf, null, null);
    }

    @Deprecated
    public ObjectMapper(SerializerFactory sf) {
        this(null, null, null);
        setSerializerFactory(sf);
    }

    public ObjectMapper(JsonFactory jf, SerializerProvider sp, DeserializerProvider dp) {
        this(jf, sp, dp, null, null);
    }

    public ObjectMapper(JsonFactory jf, SerializerProvider sp, DeserializerProvider dp, SerializationConfig sconfig, DeserializationConfig dconfig) {
        this._rootDeserializers = new ConcurrentHashMap<>(64, 0.6f, 2);
        this._jsonFactory = jf == null ? new MappingJsonFactory(this) : jf;
        this._typeFactory = TypeFactory.defaultInstance();
        this._serializationConfig = sconfig == null ? new SerializationConfig(DEFAULT_INTROSPECTOR, DEFAULT_ANNOTATION_INTROSPECTOR, STD_VISIBILITY_CHECKER, null, null, this._typeFactory, null) : sconfig;
        this._deserializationConfig = dconfig == null ? new DeserializationConfig(DEFAULT_INTROSPECTOR, DEFAULT_ANNOTATION_INTROSPECTOR, STD_VISIBILITY_CHECKER, null, null, this._typeFactory, null) : dconfig;
        this._serializerProvider = sp == null ? new StdSerializerProvider() : sp;
        this._deserializerProvider = dp == null ? new StdDeserializerProvider() : dp;
        this._serializerFactory = BeanSerializerFactory.instance;
    }

    @Override // org.codehaus.jackson.Versioned
    public Version version() {
        return VersionUtil.versionFor(getClass());
    }

    public void registerModule(Module module) {
        String name = module.getModuleName();
        if (name == null) {
            throw new IllegalArgumentException("Module without defined name");
        }
        Version version = module.version();
        if (version == null) {
            throw new IllegalArgumentException("Module without defined version");
        }
        module.setupModule(new Module.SetupContext() { // from class: org.codehaus.jackson.map.ObjectMapper.1
            @Override // org.codehaus.jackson.map.Module.SetupContext
            public Version getMapperVersion() {
                return ObjectMapper.this.version();
            }

            @Override // org.codehaus.jackson.map.Module.SetupContext
            public DeserializationConfig getDeserializationConfig() {
                return this.getDeserializationConfig();
            }

            @Override // org.codehaus.jackson.map.Module.SetupContext
            public SerializationConfig getSerializationConfig() {
                return this.getSerializationConfig();
            }

            @Override // org.codehaus.jackson.map.Module.SetupContext
            public void addDeserializers(Deserializers d) {
                this._deserializerProvider = this._deserializerProvider.withAdditionalDeserializers(d);
            }

            @Override // org.codehaus.jackson.map.Module.SetupContext
            public void addKeyDeserializers(KeyDeserializers d) {
                this._deserializerProvider = this._deserializerProvider.withAdditionalKeyDeserializers(d);
            }

            @Override // org.codehaus.jackson.map.Module.SetupContext
            public void addSerializers(Serializers s) {
                this._serializerFactory = this._serializerFactory.withAdditionalSerializers(s);
            }

            @Override // org.codehaus.jackson.map.Module.SetupContext
            public void addKeySerializers(Serializers s) {
                this._serializerFactory = this._serializerFactory.withAdditionalKeySerializers(s);
            }

            @Override // org.codehaus.jackson.map.Module.SetupContext
            public void addBeanSerializerModifier(BeanSerializerModifier modifier) {
                this._serializerFactory = this._serializerFactory.withSerializerModifier(modifier);
            }

            @Override // org.codehaus.jackson.map.Module.SetupContext
            public void addBeanDeserializerModifier(BeanDeserializerModifier modifier) {
                this._deserializerProvider = this._deserializerProvider.withDeserializerModifier(modifier);
            }

            @Override // org.codehaus.jackson.map.Module.SetupContext
            public void addAbstractTypeResolver(AbstractTypeResolver resolver) {
                this._deserializerProvider = this._deserializerProvider.withAbstractTypeResolver(resolver);
            }

            @Override // org.codehaus.jackson.map.Module.SetupContext
            public void addTypeModifier(TypeModifier modifier) {
                TypeFactory f = this._typeFactory;
                this.setTypeFactory(f.withModifier(modifier));
            }

            @Override // org.codehaus.jackson.map.Module.SetupContext
            public void insertAnnotationIntrospector(AnnotationIntrospector ai) {
                this._deserializationConfig.insertAnnotationIntrospector(ai);
                this._serializationConfig.insertAnnotationIntrospector(ai);
            }

            @Override // org.codehaus.jackson.map.Module.SetupContext
            public void appendAnnotationIntrospector(AnnotationIntrospector ai) {
                this._deserializationConfig.appendAnnotationIntrospector(ai);
                this._serializationConfig.appendAnnotationIntrospector(ai);
            }

            @Override // org.codehaus.jackson.map.Module.SetupContext
            public void setMixInAnnotations(Class<?> target, Class<?> mixinSource) {
                this._deserializationConfig.addMixInAnnotations(target, mixinSource);
                this._serializationConfig.addMixInAnnotations(target, mixinSource);
            }
        });
    }

    public ObjectMapper withModule(Module module) {
        registerModule(module);
        return this;
    }

    public SerializationConfig getSerializationConfig() {
        return this._serializationConfig;
    }

    public SerializationConfig copySerializationConfig() {
        return this._serializationConfig.createUnshared(this._subtypeResolver);
    }

    public ObjectMapper setSerializationConfig(SerializationConfig cfg) {
        this._serializationConfig = cfg;
        return this;
    }

    public DeserializationConfig getDeserializationConfig() {
        return this._deserializationConfig;
    }

    public DeserializationConfig copyDeserializationConfig() {
        return this._deserializationConfig.createUnshared(this._subtypeResolver);
    }

    public ObjectMapper setDeserializationConfig(DeserializationConfig cfg) {
        this._deserializationConfig = cfg;
        return this;
    }

    public ObjectMapper setSerializerFactory(SerializerFactory f) {
        this._serializerFactory = f;
        return this;
    }

    public ObjectMapper setSerializerProvider(SerializerProvider p) {
        this._serializerProvider = p;
        return this;
    }

    public SerializerProvider getSerializerProvider() {
        return this._serializerProvider;
    }

    public ObjectMapper setDeserializerProvider(DeserializerProvider p) {
        this._deserializerProvider = p;
        return this;
    }

    public DeserializerProvider getDeserializerProvider() {
        return this._deserializerProvider;
    }

    public VisibilityChecker<?> getVisibilityChecker() {
        return this._serializationConfig.getDefaultVisibilityChecker();
    }

    public void setVisibilityChecker(VisibilityChecker<?> vc) {
        this._deserializationConfig = this._deserializationConfig.withVisibilityChecker(vc);
        this._serializationConfig = this._serializationConfig.withVisibilityChecker(vc);
    }

    public SubtypeResolver getSubtypeResolver() {
        if (this._subtypeResolver == null) {
            this._subtypeResolver = new StdSubtypeResolver();
        }
        return this._subtypeResolver;
    }

    public void setSubtypeResolver(SubtypeResolver r) {
        this._subtypeResolver = r;
    }

    public ObjectMapper setAnnotationIntrospector(AnnotationIntrospector ai) {
        this._serializationConfig = this._serializationConfig.withAnnotationIntrospector(ai);
        this._deserializationConfig = this._deserializationConfig.withAnnotationIntrospector(ai);
        return this;
    }

    public ObjectMapper setPropertyNamingStrategy(PropertyNamingStrategy s) {
        this._serializationConfig = this._serializationConfig.withPropertyNamingStrategy(s);
        this._deserializationConfig = this._deserializationConfig.withPropertyNamingStrategy(s);
        return this;
    }

    public ObjectMapper enableDefaultTyping() {
        return enableDefaultTyping(DefaultTyping.OBJECT_AND_NON_CONCRETE);
    }

    public ObjectMapper enableDefaultTyping(DefaultTyping dti) {
        return enableDefaultTyping(dti, JsonTypeInfo.As.WRAPPER_ARRAY);
    }

    public ObjectMapper enableDefaultTyping(DefaultTyping applicability, JsonTypeInfo.As includeAs) {
        TypeResolverBuilder<?> typer = new DefaultTypeResolverBuilder(applicability);
        return setDefaultTyping(typer.init(JsonTypeInfo.Id.CLASS, null).inclusion(includeAs));
    }

    public ObjectMapper enableDefaultTypingAsProperty(DefaultTyping applicability, String propertyName) {
        TypeResolverBuilder<?> typer = new DefaultTypeResolverBuilder(applicability);
        return setDefaultTyping(typer.init(JsonTypeInfo.Id.CLASS, null).inclusion(JsonTypeInfo.As.PROPERTY).typeProperty(propertyName));
    }

    public ObjectMapper disableDefaultTyping() {
        return setDefaultTyping(null);
    }

    public ObjectMapper setDefaultTyping(TypeResolverBuilder<?> typer) {
        this._deserializationConfig = this._deserializationConfig.withTypeResolverBuilder(typer);
        this._serializationConfig = this._serializationConfig.withTypeResolverBuilder(typer);
        return this;
    }

    public void registerSubtypes(Class<?>... classes) {
        getSubtypeResolver().registerSubtypes(classes);
    }

    public void registerSubtypes(NamedType... types) {
        getSubtypeResolver().registerSubtypes(types);
    }

    public TypeFactory getTypeFactory() {
        return this._typeFactory;
    }

    public ObjectMapper setTypeFactory(TypeFactory f) {
        this._typeFactory = f;
        this._deserializationConfig = this._deserializationConfig.withTypeFactory(f);
        this._serializationConfig = this._serializationConfig.withTypeFactory(f);
        return this;
    }

    public JavaType constructType(Type t) {
        return this._typeFactory.constructType(t);
    }

    public ObjectMapper setNodeFactory(JsonNodeFactory f) {
        this._deserializationConfig = this._deserializationConfig.withNodeFactory(f);
        return this;
    }

    public void setFilters(FilterProvider filterProvider) {
        this._serializationConfig = this._serializationConfig.withFilters(filterProvider);
    }

    public JsonFactory getJsonFactory() {
        return this._jsonFactory;
    }

    public void setDateFormat(DateFormat dateFormat) {
        this._deserializationConfig = this._deserializationConfig.withDateFormat(dateFormat);
        this._serializationConfig = this._serializationConfig.withDateFormat(dateFormat);
    }

    public void setHandlerInstantiator(HandlerInstantiator hi) {
        this._deserializationConfig = this._deserializationConfig.withHandlerInstantiator(hi);
        this._serializationConfig = this._serializationConfig.withHandlerInstantiator(hi);
    }

    public ObjectMapper configure(SerializationConfig.Feature f, boolean state) {
        this._serializationConfig.set(f, state);
        return this;
    }

    public ObjectMapper configure(DeserializationConfig.Feature f, boolean state) {
        this._deserializationConfig.set(f, state);
        return this;
    }

    public ObjectMapper configure(JsonParser.Feature f, boolean state) {
        this._jsonFactory.configure(f, state);
        return this;
    }

    public ObjectMapper configure(JsonGenerator.Feature f, boolean state) {
        this._jsonFactory.configure(f, state);
        return this;
    }

    public JsonNodeFactory getNodeFactory() {
        return this._deserializationConfig.getNodeFactory();
    }

    @Override // org.codehaus.jackson.ObjectCodec
    public <T> T readValue(JsonParser jsonParser, Class<T> cls) throws IOException {
        return (T) _readValue(copyDeserializationConfig(), jsonParser, this._typeFactory.constructType(cls));
    }

    public <T> T readValue(JsonParser jsonParser, Class<T> cls, DeserializationConfig deserializationConfig) throws IOException {
        return (T) _readValue(deserializationConfig, jsonParser, this._typeFactory.constructType(cls));
    }

    @Override // org.codehaus.jackson.ObjectCodec
    public <T> T readValue(JsonParser jsonParser, TypeReference<?> typeReference) throws IOException {
        return (T) _readValue(copyDeserializationConfig(), jsonParser, this._typeFactory.constructType(typeReference));
    }

    public <T> T readValue(JsonParser jsonParser, TypeReference<?> typeReference, DeserializationConfig deserializationConfig) throws IOException {
        return (T) _readValue(deserializationConfig, jsonParser, this._typeFactory.constructType(typeReference));
    }

    @Override // org.codehaus.jackson.ObjectCodec
    public <T> T readValue(JsonParser jsonParser, JavaType javaType) throws IOException {
        return (T) _readValue(copyDeserializationConfig(), jsonParser, javaType);
    }

    public <T> T readValue(JsonParser jsonParser, JavaType javaType, DeserializationConfig deserializationConfig) throws IOException {
        return (T) _readValue(deserializationConfig, jsonParser, javaType);
    }

    @Override // org.codehaus.jackson.ObjectCodec
    public JsonNode readTree(JsonParser jp) throws IOException {
        return readTree(jp, copyDeserializationConfig());
    }

    public JsonNode readTree(JsonParser jp, DeserializationConfig cfg) throws IOException {
        JsonNode n = (JsonNode) _readValue(cfg, jp, JSON_NODE_TYPE);
        return n == null ? NullNode.instance : n;
    }

    public JsonNode readTree(InputStream in) throws IOException {
        JsonNode n = (JsonNode) readValue(in, JSON_NODE_TYPE);
        return n == null ? NullNode.instance : n;
    }

    public JsonNode readTree(Reader r) throws IOException {
        JsonNode n = (JsonNode) readValue(r, JSON_NODE_TYPE);
        return n == null ? NullNode.instance : n;
    }

    public JsonNode readTree(String content) throws IOException {
        JsonNode n = (JsonNode) readValue(content, JSON_NODE_TYPE);
        return n == null ? NullNode.instance : n;
    }

    public <T> MappingIterator<T> readValues(JsonParser jp, JavaType valueType) throws IOException {
        DeserializationConfig config = copyDeserializationConfig();
        DeserializationContext ctxt = _createDeserializationContext(jp, config);
        JsonDeserializer<?> deser = _findRootDeserializer(config, valueType);
        return new MappingIterator<>(valueType, jp, ctxt, deser);
    }

    public <T> MappingIterator<T> readValues(JsonParser jp, Class<?> valueType) throws IOException {
        return readValues(jp, this._typeFactory.constructType(valueType));
    }

    public <T> MappingIterator<T> readValues(JsonParser jp, TypeReference<?> valueTypeRef) throws IOException {
        return readValues(jp, this._typeFactory.constructType(valueTypeRef));
    }

    @Override // org.codehaus.jackson.ObjectCodec
    public void writeValue(JsonGenerator jgen, Object value) throws IOException {
        SerializationConfig config = copySerializationConfig();
        if (config.isEnabled(SerializationConfig.Feature.CLOSE_CLOSEABLE) && (value instanceof Closeable)) {
            _writeCloseableValue(jgen, value, config);
            return;
        }
        this._serializerProvider.serializeValue(config, jgen, value, this._serializerFactory);
        if (config.isEnabled(SerializationConfig.Feature.FLUSH_AFTER_WRITE_VALUE)) {
            jgen.flush();
        }
    }

    public void writeValue(JsonGenerator jgen, Object value, SerializationConfig config) throws IOException {
        if (config.isEnabled(SerializationConfig.Feature.CLOSE_CLOSEABLE) && (value instanceof Closeable)) {
            _writeCloseableValue(jgen, value, config);
            return;
        }
        this._serializerProvider.serializeValue(config, jgen, value, this._serializerFactory);
        if (config.isEnabled(SerializationConfig.Feature.FLUSH_AFTER_WRITE_VALUE)) {
            jgen.flush();
        }
    }

    @Override // org.codehaus.jackson.ObjectCodec
    public void writeTree(JsonGenerator jgen, JsonNode rootNode) throws IOException {
        SerializationConfig config = copySerializationConfig();
        this._serializerProvider.serializeValue(config, jgen, rootNode, this._serializerFactory);
        if (config.isEnabled(SerializationConfig.Feature.FLUSH_AFTER_WRITE_VALUE)) {
            jgen.flush();
        }
    }

    public void writeTree(JsonGenerator jgen, JsonNode rootNode, SerializationConfig cfg) throws IOException {
        this._serializerProvider.serializeValue(cfg, jgen, rootNode, this._serializerFactory);
        if (cfg.isEnabled(SerializationConfig.Feature.FLUSH_AFTER_WRITE_VALUE)) {
            jgen.flush();
        }
    }

    @Override // org.codehaus.jackson.ObjectCodec
    public ObjectNode createObjectNode() {
        return this._deserializationConfig.getNodeFactory().objectNode();
    }

    @Override // org.codehaus.jackson.ObjectCodec
    public ArrayNode createArrayNode() {
        return this._deserializationConfig.getNodeFactory().arrayNode();
    }

    @Override // org.codehaus.jackson.ObjectCodec
    public JsonParser treeAsTokens(JsonNode n) {
        return new TreeTraversingParser(n, this);
    }

    @Override // org.codehaus.jackson.ObjectCodec
    public <T> T treeToValue(JsonNode jsonNode, Class<T> cls) throws IOException {
        return (T) readValue(treeAsTokens(jsonNode), cls);
    }

    public <T extends JsonNode> T valueToTree(Object obj) throws IllegalArgumentException {
        if (obj == null) {
            return null;
        }
        TokenBuffer tokenBuffer = new TokenBuffer(this);
        try {
            writeValue(tokenBuffer, obj);
            JsonParser jsonParserAsParser = tokenBuffer.asParser();
            T t = (T) readTree(jsonParserAsParser);
            jsonParserAsParser.close();
            return t;
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    public boolean canSerialize(Class<?> type) {
        return this._serializerProvider.hasSerializerFor(copySerializationConfig(), type, this._serializerFactory);
    }

    public boolean canDeserialize(JavaType type) {
        return this._deserializerProvider.hasValueDeserializerFor(copyDeserializationConfig(), type);
    }

    public <T> T readValue(File file, Class<T> cls) throws IOException {
        return (T) _readMapAndClose(this._jsonFactory.createJsonParser(file), this._typeFactory.constructType(cls));
    }

    public <T> T readValue(File file, TypeReference typeReference) throws IOException {
        return (T) _readMapAndClose(this._jsonFactory.createJsonParser(file), this._typeFactory.constructType((TypeReference<?>) typeReference));
    }

    public <T> T readValue(File file, JavaType javaType) throws IOException {
        return (T) _readMapAndClose(this._jsonFactory.createJsonParser(file), javaType);
    }

    public <T> T readValue(URL url, Class<T> cls) throws IOException {
        return (T) _readMapAndClose(this._jsonFactory.createJsonParser(url), this._typeFactory.constructType(cls));
    }

    public <T> T readValue(URL url, TypeReference typeReference) throws IOException {
        return (T) _readMapAndClose(this._jsonFactory.createJsonParser(url), this._typeFactory.constructType((TypeReference<?>) typeReference));
    }

    public <T> T readValue(URL url, JavaType javaType) throws IOException {
        return (T) _readMapAndClose(this._jsonFactory.createJsonParser(url), javaType);
    }

    public <T> T readValue(String str, Class<T> cls) throws IOException {
        return (T) _readMapAndClose(this._jsonFactory.createJsonParser(str), this._typeFactory.constructType(cls));
    }

    public <T> T readValue(String str, TypeReference typeReference) throws IOException {
        return (T) _readMapAndClose(this._jsonFactory.createJsonParser(str), this._typeFactory.constructType((TypeReference<?>) typeReference));
    }

    public <T> T readValue(String str, JavaType javaType) throws IOException {
        return (T) _readMapAndClose(this._jsonFactory.createJsonParser(str), javaType);
    }

    public <T> T readValue(Reader reader, Class<T> cls) throws IOException {
        return (T) _readMapAndClose(this._jsonFactory.createJsonParser(reader), this._typeFactory.constructType(cls));
    }

    public <T> T readValue(Reader reader, TypeReference typeReference) throws IOException {
        return (T) _readMapAndClose(this._jsonFactory.createJsonParser(reader), this._typeFactory.constructType((TypeReference<?>) typeReference));
    }

    public <T> T readValue(Reader reader, JavaType javaType) throws IOException {
        return (T) _readMapAndClose(this._jsonFactory.createJsonParser(reader), javaType);
    }

    public <T> T readValue(InputStream inputStream, Class<T> cls) throws IOException {
        return (T) _readMapAndClose(this._jsonFactory.createJsonParser(inputStream), this._typeFactory.constructType(cls));
    }

    public <T> T readValue(InputStream inputStream, TypeReference typeReference) throws IOException {
        return (T) _readMapAndClose(this._jsonFactory.createJsonParser(inputStream), this._typeFactory.constructType((TypeReference<?>) typeReference));
    }

    public <T> T readValue(InputStream inputStream, JavaType javaType) throws IOException {
        return (T) _readMapAndClose(this._jsonFactory.createJsonParser(inputStream), javaType);
    }

    public <T> T readValue(byte[] bArr, Class<T> cls) throws IOException {
        return (T) _readMapAndClose(this._jsonFactory.createJsonParser(bArr), this._typeFactory.constructType(cls));
    }

    public <T> T readValue(byte[] bArr, int i, int i2, Class<T> cls) throws IOException {
        return (T) _readMapAndClose(this._jsonFactory.createJsonParser(bArr, i, i2), this._typeFactory.constructType(cls));
    }

    public <T> T readValue(byte[] bArr, TypeReference typeReference) throws IOException {
        return (T) _readMapAndClose(this._jsonFactory.createJsonParser(bArr), this._typeFactory.constructType((TypeReference<?>) typeReference));
    }

    public <T> T readValue(byte[] bArr, int i, int i2, TypeReference typeReference) throws IOException {
        return (T) _readMapAndClose(this._jsonFactory.createJsonParser(bArr, i, i2), this._typeFactory.constructType((TypeReference<?>) typeReference));
    }

    public <T> T readValue(byte[] bArr, JavaType javaType) throws IOException {
        return (T) _readMapAndClose(this._jsonFactory.createJsonParser(bArr), javaType);
    }

    public <T> T readValue(byte[] bArr, int i, int i2, JavaType javaType) throws IOException {
        return (T) _readMapAndClose(this._jsonFactory.createJsonParser(bArr, i, i2), javaType);
    }

    public <T> T readValue(JsonNode jsonNode, Class<T> cls) throws IOException {
        return (T) _readValue(copyDeserializationConfig(), treeAsTokens(jsonNode), this._typeFactory.constructType(cls));
    }

    public <T> T readValue(JsonNode jsonNode, TypeReference typeReference) throws IOException {
        return (T) _readValue(copyDeserializationConfig(), treeAsTokens(jsonNode), this._typeFactory.constructType((TypeReference<?>) typeReference));
    }

    public <T> T readValue(JsonNode jsonNode, JavaType javaType) throws IOException {
        return (T) _readValue(copyDeserializationConfig(), treeAsTokens(jsonNode), javaType);
    }

    public void writeValue(File resultFile, Object value) throws IOException {
        _configAndWriteValue(this._jsonFactory.createJsonGenerator(resultFile, JsonEncoding.UTF8), value);
    }

    public void writeValue(OutputStream out, Object value) throws IOException {
        _configAndWriteValue(this._jsonFactory.createJsonGenerator(out, JsonEncoding.UTF8), value);
    }

    public void writeValue(Writer w, Object value) throws IOException {
        _configAndWriteValue(this._jsonFactory.createJsonGenerator(w), value);
    }

    public String writeValueAsString(Object value) throws IOException {
        SegmentedStringWriter sw = new SegmentedStringWriter(this._jsonFactory._getBufferRecycler());
        _configAndWriteValue(this._jsonFactory.createJsonGenerator(sw), value);
        return sw.getAndClear();
    }

    public byte[] writeValueAsBytes(Object value) throws IOException {
        ByteArrayBuilder bb = new ByteArrayBuilder(this._jsonFactory._getBufferRecycler());
        _configAndWriteValue(this._jsonFactory.createJsonGenerator(bb, JsonEncoding.UTF8), value);
        byte[] result = bb.toByteArray();
        bb.release();
        return result;
    }

    @Deprecated
    public void writeValueUsingView(JsonGenerator jgen, Object value, Class<?> viewClass) throws IOException {
        _configAndWriteValue(jgen, value, viewClass);
    }

    @Deprecated
    public void writeValueUsingView(Writer w, Object value, Class<?> viewClass) throws IOException {
        _configAndWriteValue(this._jsonFactory.createJsonGenerator(w), value, viewClass);
    }

    @Deprecated
    public void writeValueUsingView(OutputStream out, Object value, Class<?> viewClass) throws IOException {
        _configAndWriteValue(this._jsonFactory.createJsonGenerator(out, JsonEncoding.UTF8), value, viewClass);
    }

    public ObjectWriter writer() {
        return new ObjectWriter(this, copySerializationConfig());
    }

    public ObjectWriter viewWriter(Class<?> serializationView) {
        return new ObjectWriter(this, copySerializationConfig().withView(serializationView));
    }

    public ObjectWriter typedWriter(Class<?> rootType) {
        JavaType t = rootType == null ? null : this._typeFactory.constructType(rootType);
        return new ObjectWriter(this, copySerializationConfig(), t, null);
    }

    public ObjectWriter typedWriter(JavaType rootType) {
        return new ObjectWriter(this, copySerializationConfig(), rootType, null);
    }

    public ObjectWriter typedWriter(TypeReference<?> rootType) {
        JavaType t = rootType == null ? null : this._typeFactory.constructType(rootType);
        return new ObjectWriter(this, copySerializationConfig(), t, null);
    }

    public ObjectWriter prettyPrintingWriter(PrettyPrinter pp) {
        if (pp == null) {
            pp = ObjectWriter.NULL_PRETTY_PRINTER;
        }
        return new ObjectWriter(this, copySerializationConfig(), null, pp);
    }

    public ObjectWriter defaultPrettyPrintingWriter() {
        return new ObjectWriter(this, copySerializationConfig(), null, _defaultPrettyPrinter());
    }

    public ObjectWriter filteredWriter(FilterProvider filterProvider) {
        return new ObjectWriter(this, copySerializationConfig().withFilters(filterProvider));
    }

    public ObjectWriter schemaBasedWriter(FormatSchema schema) {
        return new ObjectWriter(this, copySerializationConfig(), schema);
    }

    public ObjectReader reader() {
        return new ObjectReader(this, copyDeserializationConfig());
    }

    public ObjectReader updatingReader(Object valueToUpdate) {
        JavaType t = this._typeFactory.constructType(valueToUpdate.getClass());
        return new ObjectReader(this, copyDeserializationConfig(), t, valueToUpdate, (FormatSchema) null);
    }

    public ObjectReader reader(JavaType type) {
        return new ObjectReader(this, copyDeserializationConfig(), type, (Object) null, (FormatSchema) null);
    }

    public ObjectReader reader(Class<?> type) {
        return reader(this._typeFactory.constructType(type));
    }

    public ObjectReader reader(TypeReference<?> type) {
        return reader(this._typeFactory.constructType(type));
    }

    public ObjectReader reader(JsonNodeFactory f) {
        return new ObjectReader(this, copyDeserializationConfig()).withNodeFactory(f);
    }

    public ObjectReader schemaBasedReader(FormatSchema schema) {
        return new ObjectReader(this, copyDeserializationConfig(), (JavaType) null, (Object) null, schema);
    }

    public <T> T convertValue(Object obj, Class<T> cls) throws IllegalArgumentException {
        return (T) _convert(obj, this._typeFactory.constructType(cls));
    }

    public <T> T convertValue(Object obj, TypeReference typeReference) throws IllegalArgumentException {
        return (T) _convert(obj, this._typeFactory.constructType((TypeReference<?>) typeReference));
    }

    public <T> T convertValue(Object obj, JavaType javaType) throws IllegalArgumentException {
        return (T) _convert(obj, javaType);
    }

    protected Object _convert(Object fromValue, JavaType toValueType) throws IllegalArgumentException {
        if (fromValue == null) {
            return null;
        }
        TokenBuffer buf = new TokenBuffer(this);
        try {
            writeValue(buf, fromValue);
            JsonParser jp = buf.asParser();
            Object value = readValue(jp, toValueType);
            jp.close();
            return value;
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    public JsonSchema generateJsonSchema(Class<?> t) throws JsonMappingException {
        return generateJsonSchema(t, copySerializationConfig());
    }

    public JsonSchema generateJsonSchema(Class<?> t, SerializationConfig cfg) throws JsonMappingException {
        return this._serializerProvider.generateJsonSchema(t, cfg, this._serializerFactory);
    }

    protected PrettyPrinter _defaultPrettyPrinter() {
        return new DefaultPrettyPrinter();
    }

    protected final void _configAndWriteValue(JsonGenerator jgen, Object value) throws IOException {
        SerializationConfig cfg = copySerializationConfig();
        if (cfg.isEnabled(SerializationConfig.Feature.INDENT_OUTPUT)) {
            jgen.useDefaultPrettyPrinter();
        }
        if (cfg.isEnabled(SerializationConfig.Feature.CLOSE_CLOSEABLE) && (value instanceof Closeable)) {
            _configAndWriteCloseable(jgen, value, cfg);
            return;
        }
        boolean closed = false;
        try {
            this._serializerProvider.serializeValue(cfg, jgen, value, this._serializerFactory);
            closed = true;
            jgen.close();
        } finally {
            if (!closed) {
                try {
                    jgen.close();
                } catch (IOException e) {
                }
            }
        }
    }

    protected final void _configAndWriteValue(JsonGenerator jgen, Object value, Class<?> viewClass) throws IOException {
        SerializationConfig cfg = copySerializationConfig().withView(viewClass);
        if (cfg.isEnabled(SerializationConfig.Feature.INDENT_OUTPUT)) {
            jgen.useDefaultPrettyPrinter();
        }
        if (cfg.isEnabled(SerializationConfig.Feature.CLOSE_CLOSEABLE) && (value instanceof Closeable)) {
            _configAndWriteCloseable(jgen, value, cfg);
            return;
        }
        boolean closed = false;
        try {
            this._serializerProvider.serializeValue(cfg, jgen, value, this._serializerFactory);
            closed = true;
            jgen.close();
        } finally {
            if (!closed) {
                try {
                    jgen.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private final void _configAndWriteCloseable(JsonGenerator jgen, Object value, SerializationConfig cfg) throws IOException {
        Closeable toClose = (Closeable) value;
        try {
            this._serializerProvider.serializeValue(cfg, jgen, value, this._serializerFactory);
            jgen = null;
            jgen.close();
            toClose = null;
            toClose.close();
        } finally {
            if (jgen != null) {
                try {
                    jgen.close();
                } catch (IOException e) {
                }
            }
            if (toClose != null) {
                try {
                    toClose.close();
                } catch (IOException e2) {
                }
            }
        }
    }

    private final void _writeCloseableValue(JsonGenerator jgen, Object value, SerializationConfig cfg) throws IOException {
        Closeable toClose = (Closeable) value;
        try {
            this._serializerProvider.serializeValue(cfg, jgen, value, this._serializerFactory);
            if (cfg.isEnabled(SerializationConfig.Feature.FLUSH_AFTER_WRITE_VALUE)) {
                jgen.flush();
            }
            toClose = null;
            toClose.close();
        } finally {
            if (toClose != null) {
                try {
                    toClose.close();
                } catch (IOException e) {
                }
            }
        }
    }

    protected Object _readValue(DeserializationConfig cfg, JsonParser jp, JavaType valueType) throws IOException {
        Object objDeserialize;
        JsonToken t = _initForReading(jp);
        if (t == JsonToken.VALUE_NULL || t == JsonToken.END_ARRAY || t == JsonToken.END_OBJECT) {
            objDeserialize = null;
        } else {
            DeserializationContext ctxt = _createDeserializationContext(jp, cfg);
            objDeserialize = _findRootDeserializer(cfg, valueType).deserialize(jp, ctxt);
        }
        jp.clearCurrentToken();
        return objDeserialize;
    }

    protected Object _readMapAndClose(JsonParser jp, JavaType valueType) throws IOException {
        Object objDeserialize;
        try {
            JsonToken t = _initForReading(jp);
            if (t == JsonToken.VALUE_NULL || t == JsonToken.END_ARRAY || t == JsonToken.END_OBJECT) {
                objDeserialize = null;
            } else {
                DeserializationConfig cfg = copyDeserializationConfig();
                DeserializationContext ctxt = _createDeserializationContext(jp, cfg);
                objDeserialize = _findRootDeserializer(cfg, valueType).deserialize(jp, ctxt);
            }
            jp.clearCurrentToken();
            return objDeserialize;
        } finally {
            try {
                jp.close();
            } catch (IOException e) {
            }
        }
    }

    protected JsonToken _initForReading(JsonParser jp) throws IOException {
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
        JsonDeserializer<Object> deser2 = this._deserializerProvider.findTypedValueDeserializer(cfg, valueType, null);
        if (deser2 == null) {
            throw new JsonMappingException("Can not find a deserializer for type " + valueType);
        }
        this._rootDeserializers.put(valueType, deser2);
        return deser2;
    }

    protected DeserializationContext _createDeserializationContext(JsonParser jp, DeserializationConfig cfg) {
        return new StdDeserializationContext(cfg, jp, this._deserializerProvider);
    }
}
