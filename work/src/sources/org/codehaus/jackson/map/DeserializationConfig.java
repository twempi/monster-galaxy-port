package org.codehaus.jackson.map;

import java.text.DateFormat;
import java.util.HashMap;
import org.codehaus.jackson.Base64Variant;
import org.codehaus.jackson.Base64Variants;
import org.codehaus.jackson.map.introspect.Annotated;
import org.codehaus.jackson.map.introspect.AnnotatedClass;
import org.codehaus.jackson.map.introspect.NopAnnotationIntrospector;
import org.codehaus.jackson.map.introspect.VisibilityChecker;
import org.codehaus.jackson.map.jsontype.SubtypeResolver;
import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
import org.codehaus.jackson.map.type.ClassKey;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.map.util.LinkedNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.type.JavaType;

/* JADX INFO: loaded from: classes.dex */
public class DeserializationConfig extends MapperConfig<DeserializationConfig> {
    protected static final int DEFAULT_FEATURE_FLAGS = Feature.collectDefaults();
    protected AbstractTypeResolver _abstractTypeResolver;
    protected int _featureFlags;
    protected JsonNodeFactory _nodeFactory;
    protected LinkedNode<DeserializationProblemHandler> _problemHandlers;

    @Override // org.codehaus.jackson.map.MapperConfig
    public /* bridge */ /* synthetic */ MapperConfig createUnshared(TypeResolverBuilder x0, VisibilityChecker x1, SubtypeResolver x2) {
        return createUnshared((TypeResolverBuilder<?>) x0, (VisibilityChecker<?>) x1, x2);
    }

    @Override // org.codehaus.jackson.map.MapperConfig
    public /* bridge */ /* synthetic */ MapperConfig withClassIntrospector(ClassIntrospector x0) {
        return withClassIntrospector((ClassIntrospector<? extends BeanDescription>) x0);
    }

    @Override // org.codehaus.jackson.map.MapperConfig
    public /* bridge */ /* synthetic */ MapperConfig withTypeResolverBuilder(TypeResolverBuilder x0) {
        return withTypeResolverBuilder((TypeResolverBuilder<?>) x0);
    }

    @Override // org.codehaus.jackson.map.MapperConfig
    public /* bridge */ /* synthetic */ MapperConfig withVisibilityChecker(VisibilityChecker x0) {
        return withVisibilityChecker((VisibilityChecker<?>) x0);
    }

    public enum Feature {
        USE_ANNOTATIONS(true),
        AUTO_DETECT_SETTERS(true),
        AUTO_DETECT_CREATORS(true),
        AUTO_DETECT_FIELDS(true),
        USE_GETTERS_AS_SETTERS(true),
        CAN_OVERRIDE_ACCESS_MODIFIERS(true),
        USE_BIG_DECIMAL_FOR_FLOATS(false),
        USE_BIG_INTEGER_FOR_INTS(false),
        READ_ENUMS_USING_TO_STRING(false),
        FAIL_ON_UNKNOWN_PROPERTIES(true),
        FAIL_ON_NULL_FOR_PRIMITIVES(false),
        FAIL_ON_NUMBERS_FOR_ENUMS(false),
        WRAP_EXCEPTIONS(true),
        WRAP_ROOT_VALUE(false),
        ACCEPT_EMPTY_STRING_AS_NULL_OBJECT(false),
        ACCEPT_SINGLE_VALUE_AS_ARRAY(false);

        final boolean _defaultState;

        public static int collectDefaults() {
            int flags = 0;
            Feature[] arr$ = values();
            for (Feature f : arr$) {
                if (f.enabledByDefault()) {
                    flags |= f.getMask();
                }
            }
            return flags;
        }

        Feature(boolean defaultState) {
            this._defaultState = defaultState;
        }

        public boolean enabledByDefault() {
            return this._defaultState;
        }

        public int getMask() {
            return 1 << ordinal();
        }
    }

    public DeserializationConfig(ClassIntrospector<? extends BeanDescription> intr, AnnotationIntrospector annIntr, VisibilityChecker<?> vc, SubtypeResolver subtypeResolver, PropertyNamingStrategy propertyNamingStrategy, TypeFactory typeFactory, HandlerInstantiator handlerInstantiator) {
        super(intr, annIntr, vc, subtypeResolver, propertyNamingStrategy, typeFactory, handlerInstantiator);
        this._featureFlags = DEFAULT_FEATURE_FLAGS;
        this._nodeFactory = JsonNodeFactory.instance;
    }

    protected DeserializationConfig(DeserializationConfig src) {
        this(src, src._base);
    }

    private DeserializationConfig(DeserializationConfig src, HashMap<ClassKey, Class<?>> mixins, SubtypeResolver str) {
        this(src, src._base);
        this._mixInAnnotations = mixins;
        this._subtypeResolver = str;
    }

    protected DeserializationConfig(DeserializationConfig src, MapperConfig.Base base) {
        super(src, base, src._subtypeResolver);
        this._featureFlags = DEFAULT_FEATURE_FLAGS;
        this._featureFlags = src._featureFlags;
        this._abstractTypeResolver = src._abstractTypeResolver;
        this._problemHandlers = src._problemHandlers;
        this._nodeFactory = src._nodeFactory;
    }

    protected DeserializationConfig(DeserializationConfig src, JsonNodeFactory f) {
        super(src);
        this._featureFlags = DEFAULT_FEATURE_FLAGS;
        this._featureFlags = src._featureFlags;
        this._abstractTypeResolver = src._abstractTypeResolver;
        this._problemHandlers = src._problemHandlers;
        this._nodeFactory = f;
    }

    @Override // org.codehaus.jackson.map.MapperConfig
    public DeserializationConfig withClassIntrospector(ClassIntrospector<? extends BeanDescription> ci) {
        return new DeserializationConfig(this, this._base.withClassIntrospector(ci));
    }

    @Override // org.codehaus.jackson.map.MapperConfig
    public DeserializationConfig withAnnotationIntrospector(AnnotationIntrospector ai) {
        return new DeserializationConfig(this, this._base.withAnnotationIntrospector(ai));
    }

    @Override // org.codehaus.jackson.map.MapperConfig
    public DeserializationConfig withVisibilityChecker(VisibilityChecker<?> vc) {
        return new DeserializationConfig(this, this._base.withVisibilityChecker(vc));
    }

    @Override // org.codehaus.jackson.map.MapperConfig
    public DeserializationConfig withTypeResolverBuilder(TypeResolverBuilder<?> trb) {
        return new DeserializationConfig(this, this._base.withTypeResolverBuilder(trb));
    }

    @Override // org.codehaus.jackson.map.MapperConfig
    public DeserializationConfig withSubtypeResolver(SubtypeResolver str) {
        DeserializationConfig cfg = new DeserializationConfig(this);
        cfg._subtypeResolver = str;
        return cfg;
    }

    @Override // org.codehaus.jackson.map.MapperConfig
    public DeserializationConfig withPropertyNamingStrategy(PropertyNamingStrategy pns) {
        return new DeserializationConfig(this, this._base.withPropertyNamingStrategy(pns));
    }

    @Override // org.codehaus.jackson.map.MapperConfig
    public DeserializationConfig withTypeFactory(TypeFactory tf) {
        return tf == this._base.getTypeFactory() ? this : new DeserializationConfig(this, this._base.withTypeFactory(tf));
    }

    @Override // org.codehaus.jackson.map.MapperConfig
    public DeserializationConfig withDateFormat(DateFormat df) {
        return df == this._base.getDateFormat() ? this : new DeserializationConfig(this, this._base.withDateFormat(df));
    }

    @Override // org.codehaus.jackson.map.MapperConfig
    public DeserializationConfig withHandlerInstantiator(HandlerInstantiator hi) {
        return hi == this._base.getHandlerInstantiator() ? this : new DeserializationConfig(this, this._base.withHandlerInstantiator(hi));
    }

    public DeserializationConfig withNodeFactory(JsonNodeFactory f) {
        return new DeserializationConfig(this, f);
    }

    public void enable(Feature f) {
        this._featureFlags |= f.getMask();
    }

    public void disable(Feature f) {
        this._featureFlags &= f.getMask() ^ (-1);
    }

    public void set(Feature f, boolean state) {
        if (state) {
            enable(f);
        } else {
            disable(f);
        }
    }

    public final boolean isEnabled(Feature f) {
        return (this._featureFlags & f.getMask()) != 0;
    }

    @Override // org.codehaus.jackson.map.MapperConfig
    public void fromAnnotations(Class<?> cls) {
        AnnotationIntrospector ai = getAnnotationIntrospector();
        AnnotatedClass ac = AnnotatedClass.construct(cls, ai, null);
        VisibilityChecker<?> prevVc = getDefaultVisibilityChecker();
        this._base = this._base.withVisibilityChecker(ai.findAutoDetectVisibility(ac, prevVc));
    }

    @Override // org.codehaus.jackson.map.MapperConfig
    public DeserializationConfig createUnshared(SubtypeResolver subtypeResolver) {
        HashMap<ClassKey, Class<?>> mixins = this._mixInAnnotations;
        this._mixInAnnotationsShared = true;
        return new DeserializationConfig(this, mixins, subtypeResolver);
    }

    @Override // org.codehaus.jackson.map.MapperConfig
    public AnnotationIntrospector getAnnotationIntrospector() {
        return isEnabled(Feature.USE_ANNOTATIONS) ? super.getAnnotationIntrospector() : NopAnnotationIntrospector.instance;
    }

    @Override // org.codehaus.jackson.map.MapperConfig
    public <T extends BeanDescription> T introspectClassAnnotations(Class<?> cls) {
        return (T) getClassIntrospector().forClassAnnotations(this, cls, this);
    }

    @Override // org.codehaus.jackson.map.MapperConfig
    public <T extends BeanDescription> T introspectDirectClassAnnotations(Class<?> cls) {
        return (T) getClassIntrospector().forDirectClassAnnotations(this, cls, this);
    }

    @Override // org.codehaus.jackson.map.MapperConfig
    public boolean isAnnotationProcessingEnabled() {
        return isEnabled(Feature.USE_ANNOTATIONS);
    }

    @Override // org.codehaus.jackson.map.MapperConfig
    public boolean canOverrideAccessModifiers() {
        return isEnabled(Feature.CAN_OVERRIDE_ACCESS_MODIFIERS);
    }

    public LinkedNode<DeserializationProblemHandler> getProblemHandlers() {
        return this._problemHandlers;
    }

    public void addHandler(DeserializationProblemHandler h) {
        if (!LinkedNode.contains(this._problemHandlers, h)) {
            this._problemHandlers = new LinkedNode<>(h, this._problemHandlers);
        }
    }

    public void clearHandlers() {
        this._problemHandlers = null;
    }

    public Base64Variant getBase64Variant() {
        return Base64Variants.getDefaultVariant();
    }

    public final JsonNodeFactory getNodeFactory() {
        return this._nodeFactory;
    }

    public <T extends BeanDescription> T introspect(JavaType javaType) {
        return (T) getClassIntrospector().forDeserialization(this, javaType, this);
    }

    public <T extends BeanDescription> T introspectForCreation(JavaType javaType) {
        return (T) getClassIntrospector().forCreation(this, javaType, this);
    }

    public JsonDeserializer<Object> deserializerInstance(Annotated annotated, Class<? extends JsonDeserializer<?>> deserClass) {
        JsonDeserializer<?> deser;
        HandlerInstantiator hi = getHandlerInstantiator();
        return (hi == null || (deser = hi.deserializerInstance(this, annotated, deserClass)) == null) ? (JsonDeserializer) ClassUtil.createInstance(deserClass, canOverrideAccessModifiers()) : deser;
    }

    public KeyDeserializer keyDeserializerInstance(Annotated annotated, Class<? extends KeyDeserializer> keyDeserClass) {
        KeyDeserializer keyDeser;
        HandlerInstantiator hi = getHandlerInstantiator();
        return (hi == null || (keyDeser = hi.keyDeserializerInstance(this, annotated, keyDeserClass)) == null) ? (KeyDeserializer) ClassUtil.createInstance(keyDeserClass, canOverrideAccessModifiers()) : keyDeser;
    }

    @Override // org.codehaus.jackson.map.MapperConfig
    @Deprecated
    public DeserializationConfig createUnshared(TypeResolverBuilder<?> typer, VisibilityChecker<?> vc, SubtypeResolver str) {
        return createUnshared(str).withTypeResolverBuilder(typer).withVisibilityChecker(vc);
    }

    @Deprecated
    public void setNodeFactory(JsonNodeFactory nf) {
        this._nodeFactory = nf;
    }

    @Deprecated
    public AbstractTypeResolver getAbstractTypeResolver() {
        return this._abstractTypeResolver;
    }

    @Deprecated
    public void setAbstractTypeResolver(AbstractTypeResolver atr) {
        this._abstractTypeResolver = atr;
    }
}
