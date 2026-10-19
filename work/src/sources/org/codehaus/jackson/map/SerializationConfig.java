package org.codehaus.jackson.map;

import java.text.DateFormat;
import java.util.HashMap;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.introspect.Annotated;
import org.codehaus.jackson.map.introspect.AnnotatedClass;
import org.codehaus.jackson.map.introspect.VisibilityChecker;
import org.codehaus.jackson.map.jsontype.SubtypeResolver;
import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
import org.codehaus.jackson.map.ser.FilterProvider;
import org.codehaus.jackson.map.type.ClassKey;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.type.JavaType;

/* JADX INFO: loaded from: classes.dex */
public class SerializationConfig extends MapperConfig<SerializationConfig> {
    protected static final int DEFAULT_FEATURE_FLAGS = Feature.collectDefaults();
    protected int _featureFlags;
    protected FilterProvider _filterProvider;
    protected JsonSerialize.Inclusion _serializationInclusion;
    protected Class<?> _serializationView;

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
        AUTO_DETECT_GETTERS(true),
        AUTO_DETECT_IS_GETTERS(true),
        AUTO_DETECT_FIELDS(true),
        CAN_OVERRIDE_ACCESS_MODIFIERS(true),
        WRITE_NULL_PROPERTIES(true),
        USE_STATIC_TYPING(false),
        DEFAULT_VIEW_INCLUSION(true),
        WRAP_ROOT_VALUE(false),
        INDENT_OUTPUT(false),
        SORT_PROPERTIES_ALPHABETICALLY(false),
        FAIL_ON_EMPTY_BEANS(true),
        WRAP_EXCEPTIONS(true),
        CLOSE_CLOSEABLE(false),
        FLUSH_AFTER_WRITE_VALUE(true),
        WRITE_DATES_AS_TIMESTAMPS(true),
        WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS(false),
        WRITE_ENUMS_USING_TO_STRING(false),
        WRITE_NULL_MAP_VALUES(true);

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

    public SerializationConfig(ClassIntrospector<? extends BeanDescription> intr, AnnotationIntrospector annIntr, VisibilityChecker<?> vc, SubtypeResolver subtypeResolver, PropertyNamingStrategy propertyNamingStrategy, TypeFactory typeFactory, HandlerInstantiator handlerInstantiator) {
        super(intr, annIntr, vc, subtypeResolver, propertyNamingStrategy, typeFactory, handlerInstantiator);
        this._featureFlags = DEFAULT_FEATURE_FLAGS;
        this._serializationInclusion = null;
        this._filterProvider = null;
    }

    protected SerializationConfig(SerializationConfig src) {
        this(src, src._base);
    }

    protected SerializationConfig(SerializationConfig src, HashMap<ClassKey, Class<?>> mixins, SubtypeResolver str) {
        this(src, src._base);
        this._mixInAnnotations = mixins;
        this._subtypeResolver = str;
    }

    protected SerializationConfig(SerializationConfig src, MapperConfig.Base base) {
        super(src, base, src._subtypeResolver);
        this._featureFlags = DEFAULT_FEATURE_FLAGS;
        this._serializationInclusion = null;
        this._featureFlags = src._featureFlags;
        this._serializationInclusion = src._serializationInclusion;
        this._serializationView = src._serializationView;
        this._filterProvider = src._filterProvider;
    }

    protected SerializationConfig(SerializationConfig src, FilterProvider filters) {
        super(src);
        this._featureFlags = DEFAULT_FEATURE_FLAGS;
        this._serializationInclusion = null;
        this._featureFlags = src._featureFlags;
        this._serializationInclusion = src._serializationInclusion;
        this._serializationView = src._serializationView;
        this._filterProvider = filters;
    }

    protected SerializationConfig(SerializationConfig src, Class<?> view) {
        super(src);
        this._featureFlags = DEFAULT_FEATURE_FLAGS;
        this._serializationInclusion = null;
        this._featureFlags = src._featureFlags;
        this._serializationInclusion = src._serializationInclusion;
        this._serializationView = view;
        this._filterProvider = src._filterProvider;
    }

    @Override // org.codehaus.jackson.map.MapperConfig
    public SerializationConfig withClassIntrospector(ClassIntrospector<? extends BeanDescription> ci) {
        return new SerializationConfig(this, this._base.withClassIntrospector(ci));
    }

    @Override // org.codehaus.jackson.map.MapperConfig
    public SerializationConfig withAnnotationIntrospector(AnnotationIntrospector ai) {
        return new SerializationConfig(this, this._base.withAnnotationIntrospector(ai));
    }

    @Override // org.codehaus.jackson.map.MapperConfig
    public SerializationConfig withVisibilityChecker(VisibilityChecker<?> vc) {
        return new SerializationConfig(this, this._base.withVisibilityChecker(vc));
    }

    @Override // org.codehaus.jackson.map.MapperConfig
    public SerializationConfig withTypeResolverBuilder(TypeResolverBuilder<?> trb) {
        return new SerializationConfig(this, this._base.withTypeResolverBuilder(trb));
    }

    @Override // org.codehaus.jackson.map.MapperConfig
    public SerializationConfig withSubtypeResolver(SubtypeResolver str) {
        SerializationConfig cfg = new SerializationConfig(this);
        cfg._subtypeResolver = str;
        return cfg;
    }

    @Override // org.codehaus.jackson.map.MapperConfig
    public SerializationConfig withPropertyNamingStrategy(PropertyNamingStrategy pns) {
        return new SerializationConfig(this, this._base.withPropertyNamingStrategy(pns));
    }

    @Override // org.codehaus.jackson.map.MapperConfig
    public SerializationConfig withTypeFactory(TypeFactory tf) {
        return new SerializationConfig(this, this._base.withTypeFactory(tf));
    }

    @Override // org.codehaus.jackson.map.MapperConfig
    public SerializationConfig withDateFormat(DateFormat df) {
        SerializationConfig cfg = new SerializationConfig(this, this._base.withDateFormat(df));
        cfg.set(Feature.WRITE_DATES_AS_TIMESTAMPS, df == null);
        return cfg;
    }

    @Override // org.codehaus.jackson.map.MapperConfig
    public SerializationConfig withHandlerInstantiator(HandlerInstantiator hi) {
        return new SerializationConfig(this, this._base.withHandlerInstantiator(hi));
    }

    public SerializationConfig withFilters(FilterProvider filterProvider) {
        return new SerializationConfig(this, filterProvider);
    }

    public SerializationConfig withView(Class<?> view) {
        return new SerializationConfig(this, view);
    }

    @Override // org.codehaus.jackson.map.MapperConfig
    public void fromAnnotations(Class<?> cls) {
        AnnotationIntrospector ai = getAnnotationIntrospector();
        AnnotatedClass ac = AnnotatedClass.construct(cls, ai, null);
        this._base = this._base.withVisibilityChecker(ai.findAutoDetectVisibility(ac, getDefaultVisibilityChecker()));
        JsonSerialize.Inclusion incl = ai.findSerializationInclusion(ac, null);
        if (incl != this._serializationInclusion) {
            setSerializationInclusion(incl);
        }
        JsonSerialize.Typing typing = ai.findSerializationTyping(ac);
        if (typing != null) {
            set(Feature.USE_STATIC_TYPING, typing == JsonSerialize.Typing.STATIC);
        }
    }

    @Override // org.codehaus.jackson.map.MapperConfig
    public SerializationConfig createUnshared(SubtypeResolver subtypeResolver) {
        HashMap<ClassKey, Class<?>> mixins = this._mixInAnnotations;
        this._mixInAnnotationsShared = true;
        return new SerializationConfig(this, mixins, subtypeResolver);
    }

    @Override // org.codehaus.jackson.map.MapperConfig
    public AnnotationIntrospector getAnnotationIntrospector() {
        return isEnabled(Feature.USE_ANNOTATIONS) ? super.getAnnotationIntrospector() : AnnotationIntrospector.nopInstance();
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

    public Class<?> getSerializationView() {
        return this._serializationView;
    }

    public JsonSerialize.Inclusion getSerializationInclusion() {
        if (this._serializationInclusion != null) {
            return this._serializationInclusion;
        }
        return isEnabled(Feature.WRITE_NULL_PROPERTIES) ? JsonSerialize.Inclusion.ALWAYS : JsonSerialize.Inclusion.NON_NULL;
    }

    public void setSerializationInclusion(JsonSerialize.Inclusion props) {
        this._serializationInclusion = props;
        if (props == JsonSerialize.Inclusion.NON_NULL) {
            disable(Feature.WRITE_NULL_PROPERTIES);
        } else {
            enable(Feature.WRITE_NULL_PROPERTIES);
        }
    }

    public FilterProvider getFilterProvider() {
        return this._filterProvider;
    }

    public <T extends BeanDescription> T introspect(JavaType javaType) {
        return (T) getClassIntrospector().forSerialization(this, javaType, this);
    }

    public JsonSerializer<Object> serializerInstance(Annotated annotated, Class<? extends JsonSerializer<?>> serClass) {
        JsonSerializer<?> ser;
        HandlerInstantiator hi = getHandlerInstantiator();
        return (hi == null || (ser = hi.serializerInstance(this, annotated, serClass)) == null) ? (JsonSerializer) ClassUtil.createInstance(serClass, canOverrideAccessModifiers()) : ser;
    }

    @Override // org.codehaus.jackson.map.MapperConfig
    @Deprecated
    public SerializationConfig createUnshared(TypeResolverBuilder<?> typer, VisibilityChecker<?> vc, SubtypeResolver str) {
        return createUnshared(str).withTypeResolverBuilder(typer).withVisibilityChecker(vc);
    }

    @Override // org.codehaus.jackson.map.MapperConfig
    @Deprecated
    public final void setDateFormat(DateFormat df) {
        super.setDateFormat(df);
        set(Feature.WRITE_DATES_AS_TIMESTAMPS, df == null);
    }

    @Deprecated
    public void setSerializationView(Class<?> view) {
        this._serializationView = view;
    }

    public String toString() {
        return "[SerializationConfig: flags=0x" + Integer.toHexString(this._featureFlags) + "]";
    }
}
