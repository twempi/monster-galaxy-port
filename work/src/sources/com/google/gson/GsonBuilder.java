package com.google.gson;

import com.google.gson.internal.C$Gson$Preconditions;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public final class GsonBuilder {
    private String datePattern;
    private int dateStyle;
    private final ParameterizedTypeHandlerMap<JsonDeserializer<?>> deserializers;
    private boolean escapeHtmlChars;
    private boolean excludeFieldsWithoutExposeAnnotation;
    private FieldNamingStrategy2 fieldNamingPolicy;
    private boolean generateNonExecutableJson;
    private double ignoreVersionsAfter;
    private final ParameterizedTypeHandlerMap<InstanceCreator<?>> instanceCreators;
    private LongSerializationPolicy longSerializationPolicy;
    private ModifierBasedExclusionStrategy modifierBasedExclusionStrategy;
    private boolean prettyPrinting;
    private boolean serializeInnerClasses;
    private boolean serializeNulls;
    private boolean serializeSpecialFloatingPointValues;
    private final ParameterizedTypeHandlerMap<JsonSerializer<?>> serializers;
    private int timeStyle;
    private static final MapAsArrayTypeAdapter COMPLEX_KEY_MAP_TYPE_ADAPTER = new MapAsArrayTypeAdapter();
    private static final InnerClassExclusionStrategy innerClassExclusionStrategy = new InnerClassExclusionStrategy();
    private static final ExposeAnnotationDeserializationExclusionStrategy exposeAnnotationDeserializationExclusionStrategy = new ExposeAnnotationDeserializationExclusionStrategy();
    private static final ExposeAnnotationSerializationExclusionStrategy exposeAnnotationSerializationExclusionStrategy = new ExposeAnnotationSerializationExclusionStrategy();
    private final Set<ExclusionStrategy> serializeExclusionStrategies = new HashSet();
    private final Set<ExclusionStrategy> deserializeExclusionStrategies = new HashSet();

    public GsonBuilder() {
        this.deserializeExclusionStrategies.add(Gson.DEFAULT_ANON_LOCAL_CLASS_EXCLUSION_STRATEGY);
        this.deserializeExclusionStrategies.add(Gson.DEFAULT_SYNTHETIC_FIELD_EXCLUSION_STRATEGY);
        this.serializeExclusionStrategies.add(Gson.DEFAULT_ANON_LOCAL_CLASS_EXCLUSION_STRATEGY);
        this.serializeExclusionStrategies.add(Gson.DEFAULT_SYNTHETIC_FIELD_EXCLUSION_STRATEGY);
        this.ignoreVersionsAfter = -1.0d;
        this.serializeInnerClasses = true;
        this.prettyPrinting = false;
        this.escapeHtmlChars = true;
        this.modifierBasedExclusionStrategy = Gson.DEFAULT_MODIFIER_BASED_EXCLUSION_STRATEGY;
        this.excludeFieldsWithoutExposeAnnotation = false;
        this.longSerializationPolicy = LongSerializationPolicy.DEFAULT;
        this.fieldNamingPolicy = Gson.DEFAULT_NAMING_POLICY;
        this.instanceCreators = new ParameterizedTypeHandlerMap<>();
        this.serializers = new ParameterizedTypeHandlerMap<>();
        this.deserializers = new ParameterizedTypeHandlerMap<>();
        this.serializeNulls = false;
        this.dateStyle = 2;
        this.timeStyle = 2;
        this.serializeSpecialFloatingPointValues = false;
        this.generateNonExecutableJson = false;
    }

    public GsonBuilder setVersion(double ignoreVersionsAfter) {
        this.ignoreVersionsAfter = ignoreVersionsAfter;
        return this;
    }

    public GsonBuilder excludeFieldsWithModifiers(int... modifiers) {
        this.modifierBasedExclusionStrategy = new ModifierBasedExclusionStrategy(modifiers);
        return this;
    }

    public GsonBuilder generateNonExecutableJson() {
        this.generateNonExecutableJson = true;
        return this;
    }

    public GsonBuilder excludeFieldsWithoutExposeAnnotation() {
        this.excludeFieldsWithoutExposeAnnotation = true;
        return this;
    }

    public GsonBuilder serializeNulls() {
        this.serializeNulls = true;
        return this;
    }

    public GsonBuilder enableComplexMapKeySerialization() {
        registerTypeHierarchyAdapter(Map.class, COMPLEX_KEY_MAP_TYPE_ADAPTER);
        return this;
    }

    public GsonBuilder disableInnerClassSerialization() {
        this.serializeInnerClasses = false;
        return this;
    }

    public GsonBuilder setLongSerializationPolicy(LongSerializationPolicy serializationPolicy) {
        this.longSerializationPolicy = serializationPolicy;
        return this;
    }

    public GsonBuilder setFieldNamingPolicy(FieldNamingPolicy namingConvention) {
        return setFieldNamingStrategy(namingConvention.getFieldNamingPolicy());
    }

    public GsonBuilder setFieldNamingStrategy(FieldNamingStrategy fieldNamingStrategy) {
        return setFieldNamingStrategy(new FieldNamingStrategy2Adapter(fieldNamingStrategy));
    }

    GsonBuilder setFieldNamingStrategy(FieldNamingStrategy2 fieldNamingStrategy) {
        this.fieldNamingPolicy = new SerializedNameAnnotationInterceptingNamingPolicy(fieldNamingStrategy);
        return this;
    }

    public GsonBuilder setExclusionStrategies(ExclusionStrategy... strategies) {
        List<ExclusionStrategy> strategyList = Arrays.asList(strategies);
        this.serializeExclusionStrategies.addAll(strategyList);
        this.deserializeExclusionStrategies.addAll(strategyList);
        return this;
    }

    public GsonBuilder addSerializationExclusionStrategy(ExclusionStrategy strategy) {
        this.serializeExclusionStrategies.add(strategy);
        return this;
    }

    public GsonBuilder addDeserializationExclusionStrategy(ExclusionStrategy strategy) {
        this.deserializeExclusionStrategies.add(strategy);
        return this;
    }

    public GsonBuilder setPrettyPrinting() {
        this.prettyPrinting = true;
        return this;
    }

    public GsonBuilder disableHtmlEscaping() {
        this.escapeHtmlChars = false;
        return this;
    }

    public GsonBuilder setDateFormat(String pattern) {
        this.datePattern = pattern;
        return this;
    }

    public GsonBuilder setDateFormat(int style) {
        this.dateStyle = style;
        this.datePattern = null;
        return this;
    }

    public GsonBuilder setDateFormat(int dateStyle, int timeStyle) {
        this.dateStyle = dateStyle;
        this.timeStyle = timeStyle;
        this.datePattern = null;
        return this;
    }

    public GsonBuilder registerTypeAdapter(Type type, Object typeAdapter) {
        C$Gson$Preconditions.checkArgument((typeAdapter instanceof JsonSerializer) || (typeAdapter instanceof JsonDeserializer) || (typeAdapter instanceof InstanceCreator));
        if (typeAdapter instanceof InstanceCreator) {
            registerInstanceCreator(type, (InstanceCreator) typeAdapter);
        }
        if (typeAdapter instanceof JsonSerializer) {
            registerSerializer(type, (JsonSerializer) typeAdapter);
        }
        if (typeAdapter instanceof JsonDeserializer) {
            registerDeserializer(type, (JsonDeserializer) typeAdapter);
        }
        return this;
    }

    private <T> GsonBuilder registerInstanceCreator(Type typeOfT, InstanceCreator<? extends T> instanceCreator) {
        this.instanceCreators.register(typeOfT, instanceCreator);
        return this;
    }

    private <T> GsonBuilder registerSerializer(Type typeOfT, JsonSerializer<T> serializer) {
        this.serializers.register(typeOfT, serializer);
        return this;
    }

    private <T> GsonBuilder registerDeserializer(Type typeOfT, JsonDeserializer<T> deserializer) {
        this.deserializers.register(typeOfT, new JsonDeserializerExceptionWrapper(deserializer));
        return this;
    }

    public GsonBuilder registerTypeHierarchyAdapter(Class<?> baseType, Object typeAdapter) {
        C$Gson$Preconditions.checkArgument((typeAdapter instanceof JsonSerializer) || (typeAdapter instanceof JsonDeserializer) || (typeAdapter instanceof InstanceCreator));
        if (typeAdapter instanceof InstanceCreator) {
            registerInstanceCreatorForTypeHierarchy(baseType, (InstanceCreator) typeAdapter);
        }
        if (typeAdapter instanceof JsonSerializer) {
            registerSerializerForTypeHierarchy(baseType, (JsonSerializer) typeAdapter);
        }
        if (typeAdapter instanceof JsonDeserializer) {
            registerDeserializerForTypeHierarchy(baseType, (JsonDeserializer) typeAdapter);
        }
        return this;
    }

    private <T> GsonBuilder registerInstanceCreatorForTypeHierarchy(Class<?> classOfT, InstanceCreator<? extends T> instanceCreator) {
        this.instanceCreators.registerForTypeHierarchy(classOfT, instanceCreator);
        return this;
    }

    private <T> GsonBuilder registerSerializerForTypeHierarchy(Class<?> classOfT, JsonSerializer<T> serializer) {
        this.serializers.registerForTypeHierarchy(classOfT, serializer);
        return this;
    }

    private <T> GsonBuilder registerDeserializerForTypeHierarchy(Class<?> classOfT, JsonDeserializer<T> deserializer) {
        this.deserializers.registerForTypeHierarchy(classOfT, new JsonDeserializerExceptionWrapper(deserializer));
        return this;
    }

    public GsonBuilder serializeSpecialFloatingPointValues() {
        this.serializeSpecialFloatingPointValues = true;
        return this;
    }

    public Gson create() {
        List<ExclusionStrategy> deserializationStrategies = new LinkedList<>(this.deserializeExclusionStrategies);
        List<ExclusionStrategy> serializationStrategies = new LinkedList<>(this.serializeExclusionStrategies);
        deserializationStrategies.add(this.modifierBasedExclusionStrategy);
        serializationStrategies.add(this.modifierBasedExclusionStrategy);
        if (!this.serializeInnerClasses) {
            deserializationStrategies.add(innerClassExclusionStrategy);
            serializationStrategies.add(innerClassExclusionStrategy);
        }
        if (this.ignoreVersionsAfter != -1.0d) {
            VersionExclusionStrategy versionExclusionStrategy = new VersionExclusionStrategy(this.ignoreVersionsAfter);
            deserializationStrategies.add(versionExclusionStrategy);
            serializationStrategies.add(versionExclusionStrategy);
        }
        if (this.excludeFieldsWithoutExposeAnnotation) {
            deserializationStrategies.add(exposeAnnotationDeserializationExclusionStrategy);
            serializationStrategies.add(exposeAnnotationSerializationExclusionStrategy);
        }
        ParameterizedTypeHandlerMap<JsonSerializer<?>> customSerializers = DefaultTypeAdapters.DEFAULT_HIERARCHY_SERIALIZERS.copyOf();
        customSerializers.register(this.serializers.copyOf());
        ParameterizedTypeHandlerMap<JsonDeserializer<?>> customDeserializers = DefaultTypeAdapters.DEFAULT_HIERARCHY_DESERIALIZERS.copyOf();
        customDeserializers.register(this.deserializers.copyOf());
        addTypeAdaptersForDate(this.datePattern, this.dateStyle, this.timeStyle, customSerializers, customDeserializers);
        customSerializers.registerIfAbsent(DefaultTypeAdapters.getDefaultSerializers(this.serializeSpecialFloatingPointValues, this.longSerializationPolicy));
        customDeserializers.registerIfAbsent(DefaultTypeAdapters.getDefaultDeserializers());
        ParameterizedTypeHandlerMap<InstanceCreator<?>> customInstanceCreators = this.instanceCreators.copyOf();
        customInstanceCreators.registerIfAbsent(DefaultTypeAdapters.getDefaultInstanceCreators());
        customSerializers.makeUnmodifiable();
        customDeserializers.makeUnmodifiable();
        this.instanceCreators.makeUnmodifiable();
        MappedObjectConstructor objConstructor = new MappedObjectConstructor(customInstanceCreators);
        Gson gson = new Gson(new DisjunctionExclusionStrategy(deserializationStrategies), new DisjunctionExclusionStrategy(serializationStrategies), this.fieldNamingPolicy, objConstructor, this.serializeNulls, customSerializers, customDeserializers, this.generateNonExecutableJson, this.escapeHtmlChars, this.prettyPrinting);
        return gson;
    }

    private static void addTypeAdaptersForDate(String datePattern, int dateStyle, int timeStyle, ParameterizedTypeHandlerMap<JsonSerializer<?>> serializers, ParameterizedTypeHandlerMap<JsonDeserializer<?>> deserializers) {
        DefaultTypeAdapters.DefaultDateTypeAdapter dateTypeAdapter = null;
        if (datePattern != null && !"".equals(datePattern.trim())) {
            dateTypeAdapter = new DefaultTypeAdapters.DefaultDateTypeAdapter(datePattern);
        } else if (dateStyle != 2 && timeStyle != 2) {
            dateTypeAdapter = new DefaultTypeAdapters.DefaultDateTypeAdapter(dateStyle, timeStyle);
        }
        if (dateTypeAdapter != null) {
            registerIfAbsent(Date.class, serializers, dateTypeAdapter);
            registerIfAbsent(Date.class, deserializers, dateTypeAdapter);
            registerIfAbsent(Timestamp.class, serializers, dateTypeAdapter);
            registerIfAbsent(Timestamp.class, deserializers, dateTypeAdapter);
            registerIfAbsent(java.sql.Date.class, serializers, dateTypeAdapter);
            registerIfAbsent(java.sql.Date.class, deserializers, dateTypeAdapter);
        }
    }

    private static <T> void registerIfAbsent(Class<?> type, ParameterizedTypeHandlerMap<T> adapters, T adapter) {
        if (!adapters.hasSpecificHandlerFor(type)) {
            adapters.register(type, adapter);
        }
    }
}
