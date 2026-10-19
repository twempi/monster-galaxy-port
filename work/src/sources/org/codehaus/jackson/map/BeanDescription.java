package org.codehaus.jackson.map;

import java.util.Collection;
import java.util.LinkedHashMap;
import org.codehaus.jackson.map.introspect.AnnotatedMethod;
import org.codehaus.jackson.map.introspect.VisibilityChecker;
import org.codehaus.jackson.map.type.TypeBindings;
import org.codehaus.jackson.map.util.Annotations;
import org.codehaus.jackson.type.JavaType;

/* JADX INFO: loaded from: classes.dex */
public abstract class BeanDescription {
    protected final JavaType _type;

    public abstract TypeBindings bindingsForBeanType();

    public abstract LinkedHashMap<String, AnnotatedMethod> findGetters(VisibilityChecker<?> visibilityChecker, Collection<String> collection);

    public abstract LinkedHashMap<String, AnnotatedMethod> findSetters(VisibilityChecker<?> visibilityChecker);

    public abstract Annotations getClassAnnotations();

    public abstract boolean hasKnownClassAnnotations();

    protected BeanDescription(JavaType type) {
        this._type = type;
    }

    public JavaType getType() {
        return this._type;
    }

    public Class<?> getBeanClass() {
        return this._type.getRawClass();
    }
}
