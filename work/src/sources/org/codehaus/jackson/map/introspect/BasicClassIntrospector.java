package org.codehaus.jackson.map.introspect;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.BeanDescription;
import org.codehaus.jackson.map.ClassIntrospector;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.MapperConfig;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.type.JavaType;

/* JADX INFO: loaded from: classes.dex */
public class BasicClassIntrospector extends ClassIntrospector<BasicBeanDescription> {
    public static final GetterMethodFilter DEFAULT_GETTER_FILTER = new GetterMethodFilter();
    public static final SetterMethodFilter DEFAULT_SETTER_FILTER = new SetterMethodFilter();
    public static final SetterAndGetterMethodFilter DEFAULT_SETTER_AND_GETTER_FILTER = new SetterAndGetterMethodFilter();
    public static final BasicClassIntrospector instance = new BasicClassIntrospector();

    @Override // org.codehaus.jackson.map.ClassIntrospector
    public /* bridge */ /* synthetic */ BeanDescription forClassAnnotations(MapperConfig x0, Class x1, ClassIntrospector.MixInResolver x2) {
        return forClassAnnotations((MapperConfig<?>) x0, (Class<?>) x1, x2);
    }

    @Override // org.codehaus.jackson.map.ClassIntrospector
    public /* bridge */ /* synthetic */ BeanDescription forDirectClassAnnotations(MapperConfig x0, Class x1, ClassIntrospector.MixInResolver x2) {
        return forDirectClassAnnotations((MapperConfig<?>) x0, (Class<?>) x1, x2);
    }

    public static class GetterMethodFilter implements MethodFilter {
        private GetterMethodFilter() {
        }

        @Override // org.codehaus.jackson.map.introspect.MethodFilter
        public boolean includeMethod(Method m) {
            return ClassUtil.hasGetterSignature(m);
        }
    }

    public static class SetterMethodFilter implements MethodFilter {
        @Override // org.codehaus.jackson.map.introspect.MethodFilter
        public boolean includeMethod(Method m) {
            if (Modifier.isStatic(m.getModifiers())) {
                return false;
            }
            int pcount = m.getParameterTypes().length;
            switch (pcount) {
                case 1:
                    return true;
                case 2:
                    return true;
                default:
                    return false;
            }
        }
    }

    public static final class SetterAndGetterMethodFilter extends SetterMethodFilter {
        @Override // org.codehaus.jackson.map.introspect.BasicClassIntrospector.SetterMethodFilter, org.codehaus.jackson.map.introspect.MethodFilter
        public boolean includeMethod(Method m) {
            if (super.includeMethod(m)) {
                return true;
            }
            if (!ClassUtil.hasGetterSignature(m)) {
                return false;
            }
            Class<?> rt = m.getReturnType();
            return Collection.class.isAssignableFrom(rt) || Map.class.isAssignableFrom(rt);
        }
    }

    @Override // org.codehaus.jackson.map.ClassIntrospector
    public BasicBeanDescription forSerialization(SerializationConfig cfg, JavaType type, ClassIntrospector.MixInResolver r) {
        AnnotationIntrospector ai = cfg.getAnnotationIntrospector();
        AnnotatedClass ac = AnnotatedClass.construct(type.getRawClass(), ai, r);
        ac.resolveMemberMethods(getSerializationMethodFilter(cfg), false);
        ac.resolveCreators(true);
        ac.resolveFields(false);
        return new BasicBeanDescription(cfg, type, ac);
    }

    @Override // org.codehaus.jackson.map.ClassIntrospector
    public BasicBeanDescription forDeserialization(DeserializationConfig cfg, JavaType type, ClassIntrospector.MixInResolver r) {
        boolean useAnnotations = cfg.isAnnotationProcessingEnabled();
        AnnotationIntrospector ai = cfg.getAnnotationIntrospector();
        Class<?> rawClass = type.getRawClass();
        if (!useAnnotations) {
            ai = null;
        }
        AnnotatedClass ac = AnnotatedClass.construct(rawClass, ai, r);
        ac.resolveMemberMethods(getDeserializationMethodFilter(cfg), true);
        ac.resolveCreators(true);
        ac.resolveFields(true);
        return new BasicBeanDescription(cfg, type, ac);
    }

    @Override // org.codehaus.jackson.map.ClassIntrospector
    public BasicBeanDescription forCreation(DeserializationConfig cfg, JavaType type, ClassIntrospector.MixInResolver r) {
        boolean useAnnotations = cfg.isAnnotationProcessingEnabled();
        AnnotationIntrospector ai = cfg.getAnnotationIntrospector();
        Class<?> rawClass = type.getRawClass();
        if (!useAnnotations) {
            ai = null;
        }
        AnnotatedClass ac = AnnotatedClass.construct(rawClass, ai, r);
        ac.resolveCreators(true);
        return new BasicBeanDescription(cfg, type, ac);
    }

    @Override // org.codehaus.jackson.map.ClassIntrospector
    public BasicBeanDescription forClassAnnotations(MapperConfig<?> cfg, Class<?> c, ClassIntrospector.MixInResolver r) {
        boolean useAnnotations = cfg.isAnnotationProcessingEnabled();
        AnnotationIntrospector ai = cfg.getAnnotationIntrospector();
        if (!useAnnotations) {
            ai = null;
        }
        AnnotatedClass ac = AnnotatedClass.construct(c, ai, r);
        return new BasicBeanDescription(cfg, cfg.constructType(c), ac);
    }

    @Override // org.codehaus.jackson.map.ClassIntrospector
    public BasicBeanDescription forDirectClassAnnotations(MapperConfig<?> cfg, Class<?> c, ClassIntrospector.MixInResolver r) {
        boolean useAnnotations = cfg.isAnnotationProcessingEnabled();
        AnnotationIntrospector ai = cfg.getAnnotationIntrospector();
        if (!useAnnotations) {
            ai = null;
        }
        AnnotatedClass ac = AnnotatedClass.constructWithoutSuperTypes(c, ai, r);
        return new BasicBeanDescription(cfg, cfg.constructType(c), ac);
    }

    protected MethodFilter getSerializationMethodFilter(SerializationConfig cfg) {
        return DEFAULT_GETTER_FILTER;
    }

    protected MethodFilter getDeserializationMethodFilter(DeserializationConfig cfg) {
        return cfg.isEnabled(DeserializationConfig.Feature.USE_GETTERS_AS_SETTERS) ? DEFAULT_SETTER_AND_GETTER_FILTER : DEFAULT_SETTER_FILTER;
    }
}
