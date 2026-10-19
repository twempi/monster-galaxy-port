package org.codehaus.jackson.map.introspect;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.annotate.JacksonAnnotation;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonClass;
import org.codehaus.jackson.annotate.JsonContentClass;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonGetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonIgnoreType;
import org.codehaus.jackson.annotate.JsonKeyClass;
import org.codehaus.jackson.annotate.JsonManagedReference;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.annotate.JsonRawValue;
import org.codehaus.jackson.annotate.JsonSetter;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeName;
import org.codehaus.jackson.annotate.JsonValue;
import org.codehaus.jackson.annotate.JsonWriteNullProperties;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.KeyDeserializer;
import org.codehaus.jackson.map.MapperConfig;
import org.codehaus.jackson.map.annotate.JsonCachable;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonFilter;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonTypeIdResolver;
import org.codehaus.jackson.map.annotate.JsonTypeResolver;
import org.codehaus.jackson.map.annotate.JsonView;
import org.codehaus.jackson.map.annotate.NoClass;
import org.codehaus.jackson.map.jsontype.NamedType;
import org.codehaus.jackson.map.jsontype.TypeIdResolver;
import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
import org.codehaus.jackson.map.jsontype.impl.StdTypeResolverBuilder;
import org.codehaus.jackson.map.ser.impl.RawSerializer;
import org.codehaus.jackson.type.JavaType;

/* JADX INFO: loaded from: classes.dex */
public class JacksonAnnotationIntrospector extends AnnotationIntrospector {
    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public boolean isHandled(Annotation ann) {
        Class<? extends Annotation> acls = ann.annotationType();
        return acls.getAnnotation(JacksonAnnotation.class) != null;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public String findEnumValue(Enum<?> value) {
        return value.name();
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public Boolean findCachability(AnnotatedClass ac) {
        JsonCachable ann = (JsonCachable) ac.getAnnotation(JsonCachable.class);
        if (ann == null) {
            return null;
        }
        return ann.value() ? Boolean.TRUE : Boolean.FALSE;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public String findRootName(AnnotatedClass ac) {
        return null;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public String[] findPropertiesToIgnore(AnnotatedClass ac) {
        JsonIgnoreProperties ignore = (JsonIgnoreProperties) ac.getAnnotation(JsonIgnoreProperties.class);
        if (ignore == null) {
            return null;
        }
        return ignore.value();
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public Boolean findIgnoreUnknownProperties(AnnotatedClass ac) {
        JsonIgnoreProperties ignore = (JsonIgnoreProperties) ac.getAnnotation(JsonIgnoreProperties.class);
        if (ignore == null) {
            return null;
        }
        return Boolean.valueOf(ignore.ignoreUnknown());
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public Boolean isIgnorableType(AnnotatedClass ac) {
        JsonIgnoreType ignore = (JsonIgnoreType) ac.getAnnotation(JsonIgnoreType.class);
        if (ignore == null) {
            return null;
        }
        return Boolean.valueOf(ignore.value());
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public Object findFilterId(AnnotatedClass ac) {
        JsonFilter ann = (JsonFilter) ac.getAnnotation(JsonFilter.class);
        if (ann != null) {
            String id = ann.value();
            if (id.length() > 0) {
                return id;
            }
        }
        return null;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public VisibilityChecker<?> findAutoDetectVisibility(AnnotatedClass ac, VisibilityChecker<?> checker) {
        JsonAutoDetect ann = (JsonAutoDetect) ac.getAnnotation(JsonAutoDetect.class);
        return ann == null ? checker : checker.with(ann);
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public AnnotationIntrospector.ReferenceProperty findReferenceType(AnnotatedMember member) {
        JsonManagedReference ref1 = (JsonManagedReference) member.getAnnotation(JsonManagedReference.class);
        if (ref1 != null) {
            return AnnotationIntrospector.ReferenceProperty.managed(ref1.value());
        }
        JsonBackReference ref2 = (JsonBackReference) member.getAnnotation(JsonBackReference.class);
        if (ref2 != null) {
            return AnnotationIntrospector.ReferenceProperty.back(ref2.value());
        }
        return null;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public TypeResolverBuilder<?> findTypeResolver(MapperConfig<?> config, AnnotatedClass ac, JavaType baseType) {
        return _findTypeResolver(config, ac, baseType);
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public TypeResolverBuilder<?> findPropertyTypeResolver(MapperConfig<?> config, AnnotatedMember am, JavaType baseType) {
        if (baseType.isContainerType()) {
            return null;
        }
        return _findTypeResolver(config, am, baseType);
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public TypeResolverBuilder<?> findPropertyContentTypeResolver(MapperConfig<?> config, AnnotatedMember am, JavaType containerType) {
        if (!containerType.isContainerType()) {
            throw new IllegalArgumentException("Must call method with a container type (got " + containerType + ")");
        }
        return _findTypeResolver(config, am, containerType);
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public List<NamedType> findSubtypes(Annotated a) {
        JsonSubTypes t = (JsonSubTypes) a.getAnnotation(JsonSubTypes.class);
        if (t == null) {
            return null;
        }
        JsonSubTypes.Type[] types = t.value();
        ArrayList<NamedType> result = new ArrayList<>(types.length);
        for (JsonSubTypes.Type type : types) {
            result.add(new NamedType(type.value(), type.name()));
        }
        return result;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public String findTypeName(AnnotatedClass ac) {
        JsonTypeName tn = (JsonTypeName) ac.getAnnotation(JsonTypeName.class);
        if (tn == null) {
            return null;
        }
        return tn.value();
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public boolean isIgnorableMethod(AnnotatedMethod m) {
        return _isIgnorable(m);
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public boolean isIgnorableConstructor(AnnotatedConstructor c) {
        return _isIgnorable(c);
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public boolean isIgnorableField(AnnotatedField f) {
        return _isIgnorable(f);
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public Object findSerializer(Annotated a, BeanProperty property) {
        Class<? extends JsonSerializer<?>> serClass;
        JsonSerialize ann = (JsonSerialize) a.getAnnotation(JsonSerialize.class);
        if (ann == null || (serClass = ann.using()) == JsonSerializer.None.class) {
            JsonRawValue annRaw = (JsonRawValue) a.getAnnotation(JsonRawValue.class);
            if (annRaw != null && annRaw.value()) {
                Class<?> cls = a.getRawType();
                return new RawSerializer(cls);
            }
            return null;
        }
        return serClass;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public Class<? extends JsonSerializer<?>> findKeySerializer(Annotated a) {
        Class<? extends JsonSerializer<?>> serClass;
        JsonSerialize ann = (JsonSerialize) a.getAnnotation(JsonSerialize.class);
        if (ann == null || (serClass = ann.keyUsing()) == JsonSerializer.None.class) {
            return null;
        }
        return serClass;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public Class<? extends JsonSerializer<?>> findContentSerializer(Annotated a) {
        Class<? extends JsonSerializer<?>> serClass;
        JsonSerialize ann = (JsonSerialize) a.getAnnotation(JsonSerialize.class);
        if (ann == null || (serClass = ann.contentUsing()) == JsonSerializer.None.class) {
            return null;
        }
        return serClass;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public JsonSerialize.Inclusion findSerializationInclusion(Annotated a, JsonSerialize.Inclusion defValue) {
        JsonSerialize ann = (JsonSerialize) a.getAnnotation(JsonSerialize.class);
        if (ann != null) {
            return ann.include();
        }
        JsonWriteNullProperties oldAnn = (JsonWriteNullProperties) a.getAnnotation(JsonWriteNullProperties.class);
        if (oldAnn == null) {
            return defValue;
        }
        boolean writeNulls = oldAnn.value();
        return writeNulls ? JsonSerialize.Inclusion.ALWAYS : JsonSerialize.Inclusion.NON_NULL;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public Class<?> findSerializationType(Annotated am) {
        Class<?> cls;
        JsonSerialize ann = (JsonSerialize) am.getAnnotation(JsonSerialize.class);
        if (ann == null || (cls = ann.as()) == NoClass.class) {
            return null;
        }
        return cls;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public Class<?> findSerializationKeyType(Annotated am, JavaType baseType) {
        Class<?> cls;
        JsonSerialize ann = (JsonSerialize) am.getAnnotation(JsonSerialize.class);
        if (ann == null || (cls = ann.keyAs()) == NoClass.class) {
            return null;
        }
        return cls;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public Class<?> findSerializationContentType(Annotated am, JavaType baseType) {
        Class<?> cls;
        JsonSerialize ann = (JsonSerialize) am.getAnnotation(JsonSerialize.class);
        if (ann == null || (cls = ann.contentAs()) == NoClass.class) {
            return null;
        }
        return cls;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public JsonSerialize.Typing findSerializationTyping(Annotated a) {
        JsonSerialize ann = (JsonSerialize) a.getAnnotation(JsonSerialize.class);
        if (ann == null) {
            return null;
        }
        return ann.typing();
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public Class<?>[] findSerializationViews(Annotated a) {
        JsonView ann = (JsonView) a.getAnnotation(JsonView.class);
        if (ann == null) {
            return null;
        }
        return ann.value();
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public String[] findSerializationPropertyOrder(AnnotatedClass ac) {
        JsonPropertyOrder order = (JsonPropertyOrder) ac.getAnnotation(JsonPropertyOrder.class);
        if (order == null) {
            return null;
        }
        return order.value();
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public Boolean findSerializationSortAlphabetically(AnnotatedClass ac) {
        JsonPropertyOrder order = (JsonPropertyOrder) ac.getAnnotation(JsonPropertyOrder.class);
        if (order == null) {
            return null;
        }
        return Boolean.valueOf(order.alphabetic());
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public String findGettablePropertyName(AnnotatedMethod am) {
        JsonProperty pann = (JsonProperty) am.getAnnotation(JsonProperty.class);
        if (pann != null) {
            return pann.value();
        }
        JsonGetter ann = (JsonGetter) am.getAnnotation(JsonGetter.class);
        if (ann != null) {
            return ann.value();
        }
        if (am.hasAnnotation(JsonSerialize.class) || am.hasAnnotation(JsonView.class)) {
            return "";
        }
        return null;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public boolean hasAsValueAnnotation(AnnotatedMethod am) {
        JsonValue ann = (JsonValue) am.getAnnotation(JsonValue.class);
        return ann != null && ann.value();
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public String findSerializablePropertyName(AnnotatedField af) {
        JsonProperty pann = (JsonProperty) af.getAnnotation(JsonProperty.class);
        if (pann != null) {
            return pann.value();
        }
        if (af.hasAnnotation(JsonSerialize.class) || af.hasAnnotation(JsonView.class)) {
            return "";
        }
        return null;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public Class<? extends JsonDeserializer<?>> findDeserializer(Annotated a, BeanProperty property) {
        Class<? extends JsonDeserializer<?>> deserClass;
        JsonDeserialize ann = (JsonDeserialize) a.getAnnotation(JsonDeserialize.class);
        if (ann == null || (deserClass = ann.using()) == JsonDeserializer.None.class) {
            return null;
        }
        return deserClass;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public Class<? extends KeyDeserializer> findKeyDeserializer(Annotated a) {
        Class<? extends KeyDeserializer> deserClass;
        JsonDeserialize ann = (JsonDeserialize) a.getAnnotation(JsonDeserialize.class);
        if (ann == null || (deserClass = ann.keyUsing()) == KeyDeserializer.None.class) {
            return null;
        }
        return deserClass;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public Class<? extends JsonDeserializer<?>> findContentDeserializer(Annotated a) {
        Class<? extends JsonDeserializer<?>> deserClass;
        JsonDeserialize ann = (JsonDeserialize) a.getAnnotation(JsonDeserialize.class);
        if (ann == null || (deserClass = ann.contentUsing()) == JsonDeserializer.None.class) {
            return null;
        }
        return deserClass;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public Class<?> findDeserializationType(Annotated am, JavaType baseType, String propName) {
        Class<?> cls;
        Class<?> cls2;
        JsonDeserialize ann = (JsonDeserialize) am.getAnnotation(JsonDeserialize.class);
        if (ann == null || (cls2 = ann.as()) == NoClass.class) {
            JsonClass oldAnn = (JsonClass) am.getAnnotation(JsonClass.class);
            if (oldAnn == null || (cls = oldAnn.value()) == NoClass.class) {
                return null;
            }
            return cls;
        }
        return cls2;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public Class<?> findDeserializationKeyType(Annotated am, JavaType baseKeyType, String propName) {
        Class<?> cls;
        Class<?> cls2;
        JsonDeserialize ann = (JsonDeserialize) am.getAnnotation(JsonDeserialize.class);
        if (ann == null || (cls2 = ann.keyAs()) == NoClass.class) {
            JsonKeyClass oldAnn = (JsonKeyClass) am.getAnnotation(JsonKeyClass.class);
            if (oldAnn == null || (cls = oldAnn.value()) == NoClass.class) {
                return null;
            }
            return cls;
        }
        return cls2;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public Class<?> findDeserializationContentType(Annotated am, JavaType baseContentType, String propName) {
        Class<?> cls;
        Class<?> cls2;
        JsonDeserialize ann = (JsonDeserialize) am.getAnnotation(JsonDeserialize.class);
        if (ann == null || (cls2 = ann.contentAs()) == NoClass.class) {
            JsonContentClass oldAnn = (JsonContentClass) am.getAnnotation(JsonContentClass.class);
            if (oldAnn == null || (cls = oldAnn.value()) == NoClass.class) {
                return null;
            }
            return cls;
        }
        return cls2;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public String findSettablePropertyName(AnnotatedMethod am) {
        JsonProperty pann = (JsonProperty) am.getAnnotation(JsonProperty.class);
        if (pann != null) {
            return pann.value();
        }
        JsonSetter ann = (JsonSetter) am.getAnnotation(JsonSetter.class);
        if (ann != null) {
            return ann.value();
        }
        if (am.hasAnnotation(JsonDeserialize.class) || am.hasAnnotation(JsonView.class)) {
            return "";
        }
        return null;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public boolean hasAnySetterAnnotation(AnnotatedMethod am) {
        return am.hasAnnotation(JsonAnySetter.class);
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public boolean hasAnyGetterAnnotation(AnnotatedMethod am) {
        return am.hasAnnotation(JsonAnyGetter.class);
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public boolean hasCreatorAnnotation(Annotated a) {
        return a.hasAnnotation(JsonCreator.class);
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public String findDeserializablePropertyName(AnnotatedField af) {
        JsonProperty pann = (JsonProperty) af.getAnnotation(JsonProperty.class);
        if (pann != null) {
            return pann.value();
        }
        if (af.hasAnnotation(JsonDeserialize.class) || af.hasAnnotation(JsonView.class)) {
            return "";
        }
        return null;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public String findPropertyNameForParam(AnnotatedParameter param) {
        JsonProperty pann;
        if (param == null || (pann = (JsonProperty) param.getAnnotation(JsonProperty.class)) == null) {
            return null;
        }
        return pann.value();
    }

    protected boolean _isIgnorable(Annotated a) {
        JsonIgnore ann = (JsonIgnore) a.getAnnotation(JsonIgnore.class);
        return ann != null && ann.value();
    }

    protected TypeResolverBuilder<?> _findTypeResolver(MapperConfig<?> config, Annotated ann, JavaType baseType) {
        TypeResolverBuilder<?> b;
        JsonTypeInfo info = (JsonTypeInfo) ann.getAnnotation(JsonTypeInfo.class);
        JsonTypeResolver resAnn = (JsonTypeResolver) ann.getAnnotation(JsonTypeResolver.class);
        if (resAnn != null) {
            if (info == null) {
                return null;
            }
            b = config.typeResolverBuilderInstance(ann, resAnn.value());
        } else {
            if (info == null || info.use() == JsonTypeInfo.Id.NONE) {
                return null;
            }
            b = _constructStdTypeResolverBuilder();
        }
        JsonTypeIdResolver idResInfo = (JsonTypeIdResolver) ann.getAnnotation(JsonTypeIdResolver.class);
        TypeIdResolver idRes = idResInfo == null ? null : config.typeIdResolverInstance(ann, idResInfo.value());
        if (idRes != null) {
            idRes.init(baseType);
        }
        return b.init(info.use(), idRes).inclusion(info.include()).typeProperty(info.property());
    }

    protected StdTypeResolverBuilder _constructStdTypeResolverBuilder() {
        return new StdTypeResolverBuilder();
    }
}
