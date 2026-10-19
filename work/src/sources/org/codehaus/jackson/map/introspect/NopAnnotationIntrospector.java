package org.codehaus.jackson.map.introspect;

import java.lang.annotation.Annotation;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.KeyDeserializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.JavaType;

/* JADX INFO: loaded from: classes.dex */
public class NopAnnotationIntrospector extends AnnotationIntrospector {
    public static final NopAnnotationIntrospector instance = new NopAnnotationIntrospector();

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public boolean isHandled(Annotation ann) {
        return false;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public String findEnumValue(Enum<?> value) {
        return null;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public Boolean findCachability(AnnotatedClass ac) {
        return null;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public String findRootName(AnnotatedClass ac) {
        return null;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public String[] findPropertiesToIgnore(AnnotatedClass ac) {
        return null;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public Boolean findIgnoreUnknownProperties(AnnotatedClass ac) {
        return null;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public boolean isIgnorableConstructor(AnnotatedConstructor c) {
        return false;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public boolean isIgnorableMethod(AnnotatedMethod m) {
        return false;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public boolean isIgnorableField(AnnotatedField f) {
        return false;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public Object findSerializer(Annotated am, BeanProperty property) {
        return null;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public Class<?> findSerializationType(Annotated a) {
        return null;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public JsonSerialize.Typing findSerializationTyping(Annotated a) {
        return null;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public Class<?>[] findSerializationViews(Annotated a) {
        return null;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public String[] findSerializationPropertyOrder(AnnotatedClass ac) {
        return null;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public Boolean findSerializationSortAlphabetically(AnnotatedClass ac) {
        return null;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public String findGettablePropertyName(AnnotatedMethod am) {
        return null;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public boolean hasAsValueAnnotation(AnnotatedMethod am) {
        return false;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public String findDeserializablePropertyName(AnnotatedField af) {
        return null;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public Class<?> findDeserializationContentType(Annotated am, JavaType t, String propName) {
        return null;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public Class<?> findDeserializationKeyType(Annotated am, JavaType t, String propName) {
        return null;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public Class<?> findDeserializationType(Annotated am, JavaType t, String propName) {
        return null;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public Object findDeserializer(Annotated am, BeanProperty property) {
        return null;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public Class<KeyDeserializer> findKeyDeserializer(Annotated am) {
        return null;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public Class<JsonDeserializer<?>> findContentDeserializer(Annotated am) {
        return null;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public String findPropertyNameForParam(AnnotatedParameter param) {
        return null;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public String findSerializablePropertyName(AnnotatedField af) {
        return null;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public String findSettablePropertyName(AnnotatedMethod am) {
        return null;
    }
}
