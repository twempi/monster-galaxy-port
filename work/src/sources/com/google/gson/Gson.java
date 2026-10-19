package com.google.gson;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class Gson {
    static final boolean DEFAULT_JSON_NON_EXECUTABLE = false;
    private static final String JSON_NON_EXECUTABLE_PREFIX = ")]}'\n";
    private final ExclusionStrategy deserializationExclusionStrategy;
    private final ParameterizedTypeHandlerMap<JsonDeserializer<?>> deserializers;
    private final FieldNamingStrategy2 fieldNamingPolicy;
    private final boolean generateNonExecutableJson;
    private final boolean htmlSafe;
    private final MappedObjectConstructor objectConstructor;
    private final boolean prettyPrinting;
    private final ExclusionStrategy serializationExclusionStrategy;
    private final boolean serializeNulls;
    private final ParameterizedTypeHandlerMap<JsonSerializer<?>> serializers;
    static final AnonymousAndLocalClassExclusionStrategy DEFAULT_ANON_LOCAL_CLASS_EXCLUSION_STRATEGY = new AnonymousAndLocalClassExclusionStrategy();
    static final SyntheticFieldExclusionStrategy DEFAULT_SYNTHETIC_FIELD_EXCLUSION_STRATEGY = new SyntheticFieldExclusionStrategy(true);
    static final ModifierBasedExclusionStrategy DEFAULT_MODIFIER_BASED_EXCLUSION_STRATEGY = new ModifierBasedExclusionStrategy(128, 8);
    static final FieldNamingStrategy2 DEFAULT_NAMING_POLICY = new SerializedNameAnnotationInterceptingNamingPolicy(new JavaFieldNamingPolicy());
    private static final ExclusionStrategy DEFAULT_EXCLUSION_STRATEGY = createExclusionStrategy();

    public Gson() {
        this(DEFAULT_EXCLUSION_STRATEGY, DEFAULT_EXCLUSION_STRATEGY, DEFAULT_NAMING_POLICY, new MappedObjectConstructor(DefaultTypeAdapters.getDefaultInstanceCreators()), false, DefaultTypeAdapters.getAllDefaultSerializers(), DefaultTypeAdapters.getAllDefaultDeserializers(), false, true, false);
    }

    Gson(ExclusionStrategy deserializationExclusionStrategy, ExclusionStrategy serializationExclusionStrategy, FieldNamingStrategy2 fieldNamingPolicy, MappedObjectConstructor objectConstructor, boolean serializeNulls, ParameterizedTypeHandlerMap<JsonSerializer<?>> serializers, ParameterizedTypeHandlerMap<JsonDeserializer<?>> deserializers, boolean generateNonExecutableGson, boolean htmlSafe, boolean prettyPrinting) {
        this.deserializationExclusionStrategy = deserializationExclusionStrategy;
        this.serializationExclusionStrategy = serializationExclusionStrategy;
        this.fieldNamingPolicy = fieldNamingPolicy;
        this.objectConstructor = objectConstructor;
        this.serializeNulls = serializeNulls;
        this.serializers = serializers;
        this.deserializers = deserializers;
        this.generateNonExecutableJson = generateNonExecutableGson;
        this.htmlSafe = htmlSafe;
        this.prettyPrinting = prettyPrinting;
    }

    private static ExclusionStrategy createExclusionStrategy() {
        List<ExclusionStrategy> strategies = new LinkedList<>();
        strategies.add(DEFAULT_ANON_LOCAL_CLASS_EXCLUSION_STRATEGY);
        strategies.add(DEFAULT_SYNTHETIC_FIELD_EXCLUSION_STRATEGY);
        strategies.add(DEFAULT_MODIFIER_BASED_EXCLUSION_STRATEGY);
        return new DisjunctionExclusionStrategy(strategies);
    }

    public JsonElement toJsonTree(Object src) {
        return src == null ? JsonNull.createJsonNull() : toJsonTree(src, src.getClass());
    }

    public JsonElement toJsonTree(Object src, Type typeOfSrc) {
        JsonSerializationContextDefault context = new JsonSerializationContextDefault(new ObjectNavigator(this.serializationExclusionStrategy), this.fieldNamingPolicy, this.serializeNulls, this.serializers);
        return context.serialize(src, typeOfSrc);
    }

    public String toJson(Object src) {
        return src == null ? toJson((JsonElement) JsonNull.createJsonNull()) : toJson(src, src.getClass());
    }

    public String toJson(Object src, Type typeOfSrc) {
        StringWriter writer = new StringWriter();
        toJson(toJsonTree(src, typeOfSrc), (Appendable) writer);
        return writer.toString();
    }

    public void toJson(Object src, Appendable writer) throws JsonIOException {
        if (src != null) {
            toJson(src, src.getClass(), writer);
        } else {
            toJson((JsonElement) JsonNull.createJsonNull(), writer);
        }
    }

    public void toJson(Object src, Type typeOfSrc, Appendable writer) throws JsonIOException {
        JsonElement jsonElement = toJsonTree(src, typeOfSrc);
        toJson(jsonElement, writer);
    }

    public void toJson(Object src, Type typeOfSrc, JsonWriter writer) throws JsonIOException {
        toJson(toJsonTree(src, typeOfSrc), writer);
    }

    public String toJson(JsonElement jsonElement) {
        StringWriter writer = new StringWriter();
        toJson(jsonElement, (Appendable) writer);
        return writer.toString();
    }

    public void toJson(JsonElement jsonElement, Appendable writer) throws JsonIOException {
        try {
            if (this.generateNonExecutableJson) {
                writer.append(JSON_NON_EXECUTABLE_PREFIX);
            }
            JsonWriter jsonWriter = new JsonWriter(Streams.writerForAppendable(writer));
            if (this.prettyPrinting) {
                jsonWriter.setIndent("  ");
            }
            toJson(jsonElement, jsonWriter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void toJson(JsonElement jsonElement, JsonWriter writer) throws JsonIOException {
        boolean oldLenient = writer.isLenient();
        writer.setLenient(true);
        boolean oldHtmlSafe = writer.isHtmlSafe();
        writer.setHtmlSafe(this.htmlSafe);
        try {
            try {
                Streams.write(jsonElement, this.serializeNulls, writer);
                writer.setLenient(oldLenient);
                writer.setHtmlSafe(oldHtmlSafe);
            } catch (IOException e) {
                throw new JsonIOException(e);
            }
        } catch (Throwable th) {
            writer.setLenient(oldLenient);
            writer.setHtmlSafe(oldHtmlSafe);
            throw th;
        }
    }

    public <T> T fromJson(String str, Class<T> cls) throws JsonSyntaxException {
        return (T) Primitives.wrap(cls).cast(fromJson(str, (Type) cls));
    }

    public <T> T fromJson(String str, Type type) throws JsonSyntaxException {
        if (str == null) {
            return null;
        }
        return (T) fromJson(new StringReader(str), type);
    }

    public <T> T fromJson(Reader reader, Class<T> cls) throws JsonSyntaxException, JsonIOException {
        JsonReader jsonReader = new JsonReader(reader);
        Object objFromJson = fromJson(jsonReader, cls);
        assertFullConsumption(objFromJson, jsonReader);
        return (T) Primitives.wrap(cls).cast(objFromJson);
    }

    public <T> T fromJson(Reader reader, Type type) throws JsonSyntaxException, JsonIOException {
        JsonReader jsonReader = new JsonReader(reader);
        T t = (T) fromJson(jsonReader, type);
        assertFullConsumption(t, jsonReader);
        return t;
    }

    private static void assertFullConsumption(Object obj, JsonReader reader) {
        if (obj != null) {
            try {
                if (reader.peek() != JsonToken.END_DOCUMENT) {
                    throw new JsonIOException("JSON document was not fully consumed.");
                }
            } catch (MalformedJsonException e) {
                throw new JsonSyntaxException(e);
            } catch (IOException e2) {
                throw new JsonIOException(e2);
            }
        }
    }

    public <T> T fromJson(JsonReader jsonReader, Type type) throws JsonSyntaxException, JsonIOException {
        boolean zIsLenient = jsonReader.isLenient();
        jsonReader.setLenient(true);
        try {
            return (T) fromJson(Streams.parse(jsonReader), type);
        } finally {
            jsonReader.setLenient(zIsLenient);
        }
    }

    public <T> T fromJson(JsonElement jsonElement, Class<T> cls) throws JsonSyntaxException {
        return (T) Primitives.wrap(cls).cast(fromJson(jsonElement, (Type) cls));
    }

    public <T> T fromJson(JsonElement jsonElement, Type type) throws JsonSyntaxException {
        if (jsonElement == null) {
            return null;
        }
        return (T) new JsonDeserializationContextDefault(new ObjectNavigator(this.deserializationExclusionStrategy), this.fieldNamingPolicy, this.deserializers, this.objectConstructor).deserialize(jsonElement, type);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("{").append("serializeNulls:").append(this.serializeNulls).append(",serializers:").append(this.serializers).append(",deserializers:").append(this.deserializers).append(",instanceCreators:").append(this.objectConstructor).append("}");
        return sb.toString();
    }
}
