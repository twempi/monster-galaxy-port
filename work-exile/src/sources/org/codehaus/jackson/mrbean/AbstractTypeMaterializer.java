package org.codehaus.jackson.mrbean;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.Versioned;
import org.codehaus.jackson.map.AbstractTypeResolver;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.util.VersionUtil;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class AbstractTypeMaterializer extends AbstractTypeResolver implements Versioned {
    protected static final int DEFAULT_FEATURE_FLAGS = Feature.collectDefaults();
    public static final String DEFAULT_PACKAGE_FOR_GENERATED = "org.codehaus.jackson.generated.";
    protected final MyClassLoader _classLoader;
    protected String _defaultPackage;
    protected int _featureFlags;

    public enum Feature {
        FAIL_ON_UNMATERIALIZED_METHOD(false);

        final boolean _defaultState;

        protected static int collectDefaults() {
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

    public AbstractTypeMaterializer() {
        this(null);
    }

    public AbstractTypeMaterializer(ClassLoader parentClassLoader) {
        this._featureFlags = DEFAULT_FEATURE_FLAGS;
        this._defaultPackage = DEFAULT_PACKAGE_FOR_GENERATED;
        this._classLoader = new MyClassLoader(parentClassLoader == null ? getClass().getClassLoader() : parentClassLoader);
    }

    @Override // org.codehaus.jackson.Versioned
    public Version version() {
        return VersionUtil.versionFor(getClass());
    }

    public final boolean isEnabled(Feature f) {
        return (this._featureFlags & f.getMask()) != 0;
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

    public void setDefaultPackage(String defPkg) {
        if (!defPkg.endsWith(".")) {
            defPkg = defPkg + ".";
        }
        this._defaultPackage = defPkg;
    }

    @Override // org.codehaus.jackson.map.AbstractTypeResolver
    public JavaType resolveAbstractType(DeserializationConfig config, JavaType type) {
        if (type.isContainerType() || type.isPrimitive() || type.isEnumType() || type.isThrowable()) {
            return null;
        }
        return config.constructType(materializeClass(config, type.getRawClass()));
    }

    protected Class<?> materializeClass(DeserializationConfig config, Class<?> cls) {
        String newName = this._defaultPackage + cls.getName();
        BeanBuilder builder = new BeanBuilder(config, cls);
        byte[] bytecode = builder.implement(isEnabled(Feature.FAIL_ON_UNMATERIALIZED_METHOD)).build(newName);
        Class<?> result = this._classLoader.loadAndResolve(newName, bytecode, cls);
        return result;
    }

    private static class MyClassLoader extends ClassLoader {
        public MyClassLoader(ClassLoader parent) {
            super(parent);
        }

        public Class<?> loadAndResolve(String className, byte[] byteCode, Class<?> targetClass) throws IllegalArgumentException {
            Class<?> old = findLoadedClass(className);
            if (old == null || !targetClass.isAssignableFrom(old)) {
                try {
                    Class<?> impl = defineClass(className, byteCode, 0, byteCode.length);
                    resolveClass(impl);
                    return impl;
                } catch (LinkageError e) {
                    throw new IllegalArgumentException("Failed to load class '" + className + "': " + e.getMessage(), e);
                }
            }
            return old;
        }
    }
}
