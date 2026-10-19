package org.codehaus.jackson.map.introspect;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.BeanDescription;
import org.codehaus.jackson.map.MapperConfig;
import org.codehaus.jackson.map.PropertyNamingStrategy;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.type.TypeBindings;
import org.codehaus.jackson.map.util.Annotations;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.type.JavaType;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class BasicBeanDescription extends BeanDescription {
    protected final AnnotationIntrospector _annotationIntrospector;
    protected TypeBindings _bindings;
    protected final AnnotatedClass _classInfo;
    protected final MapperConfig<?> _config;

    public BasicBeanDescription(MapperConfig<?> config, JavaType type, AnnotatedClass ac) {
        super(type);
        this._config = config;
        this._annotationIntrospector = config.getAnnotationIntrospector();
        this._classInfo = ac;
    }

    @Override // org.codehaus.jackson.map.BeanDescription
    public boolean hasKnownClassAnnotations() {
        return this._classInfo.hasAnnotations();
    }

    @Override // org.codehaus.jackson.map.BeanDescription
    public Annotations getClassAnnotations() {
        return this._classInfo.getAnnotations();
    }

    @Override // org.codehaus.jackson.map.BeanDescription
    public TypeBindings bindingsForBeanType() {
        if (this._bindings == null) {
            this._bindings = new TypeBindings(this._config.getTypeFactory(), this._type);
        }
        return this._bindings;
    }

    public AnnotatedClass getClassInfo() {
        return this._classInfo;
    }

    public AnnotatedMethod findMethod(String name, Class<?>[] paramTypes) {
        return this._classInfo.findMethod(name, paramTypes);
    }

    public Object instantiateBean(boolean fixAccess) throws Throwable {
        AnnotatedConstructor ac = this._classInfo.getDefaultConstructor();
        if (ac == null) {
            return null;
        }
        if (fixAccess) {
            ac.fixAccess();
        }
        try {
            return ac.getAnnotated().newInstance(new Object[0]);
        } catch (Exception e) {
            Throwable t = e;
            while (t.getCause() != null) {
                t = t.getCause();
            }
            if (t instanceof Error) {
                throw ((Error) t);
            }
            if (t instanceof RuntimeException) {
                throw ((RuntimeException) t);
            }
            throw new IllegalArgumentException("Failed to instantiate bean of type " + this._classInfo.getAnnotated().getName() + ": (" + t.getClass().getName() + ") " + t.getMessage(), t);
        }
    }

    @Override // org.codehaus.jackson.map.BeanDescription
    public LinkedHashMap<String, AnnotatedMethod> findGetters(VisibilityChecker<?> visibilityChecker, Collection<String> ignoredProperties) {
        LinkedHashMap<String, AnnotatedMethod> results = new LinkedHashMap<>();
        PropertyNamingStrategy naming = this._config.getPropertyNamingStrategy();
        for (AnnotatedMethod am : this._classInfo.memberMethods()) {
            if (am.getParameterCount() == 0) {
                String propName = this._annotationIntrospector.findGettablePropertyName(am);
                if (propName != null) {
                    if (propName.length() == 0) {
                        propName = okNameForAnyGetter(am, am.getName());
                        if (propName == null) {
                            propName = am.getName();
                        }
                        if (naming != null) {
                            propName = naming.nameForGetterMethod(this._config, am, propName);
                        }
                    }
                } else {
                    String propName2 = am.getName();
                    if (propName2.startsWith("get")) {
                        if (visibilityChecker.isGetterVisible(am)) {
                            propName = okNameForGetter(am, propName2);
                            if (propName != null && !this._annotationIntrospector.hasAnyGetterAnnotation(am)) {
                                if (naming != null) {
                                    propName = naming.nameForGetterMethod(this._config, am, propName);
                                }
                            }
                        } else {
                            continue;
                        }
                    } else if (visibilityChecker.isIsGetterVisible(am)) {
                        propName = okNameForIsGetter(am, propName2);
                        if (propName != null) {
                            continue;
                        }
                    } else {
                        continue;
                    }
                }
                if (ignoredProperties == null || !ignoredProperties.contains(propName)) {
                    AnnotatedMethod old = results.put(propName, am);
                    if (old != null) {
                        String oldDesc = old.getFullName();
                        String newDesc = am.getFullName();
                        throw new IllegalArgumentException("Conflicting getter definitions for property \"" + propName + "\": " + oldDesc + " vs " + newDesc);
                    }
                }
            }
        }
        return results;
    }

    public AnnotatedMethod findJsonValueMethod() {
        AnnotatedMethod found = null;
        for (AnnotatedMethod am : this._classInfo.memberMethods()) {
            if (this._annotationIntrospector.hasAsValueAnnotation(am)) {
                if (found != null) {
                    throw new IllegalArgumentException("Multiple methods with active 'as-value' annotation (" + found.getName() + "(), " + am.getName() + ")");
                }
                if (!ClassUtil.hasGetterSignature(am.getAnnotated())) {
                    throw new IllegalArgumentException("Method " + am.getName() + "() marked with an 'as-value' annotation, but does not have valid getter signature (non-static, takes no args, returns a value)");
                }
                found = am;
            }
        }
        return found;
    }

    public Constructor<?> findDefaultConstructor() {
        AnnotatedConstructor ac = this._classInfo.getDefaultConstructor();
        if (ac == null) {
            return null;
        }
        return ac.getAnnotated();
    }

    public List<AnnotatedConstructor> getConstructors() {
        return this._classInfo.getConstructors();
    }

    public List<AnnotatedMethod> getFactoryMethods() {
        List<AnnotatedMethod> candidates = this._classInfo.getStaticMethods();
        if (!candidates.isEmpty()) {
            ArrayList<AnnotatedMethod> result = new ArrayList<>();
            for (AnnotatedMethod am : candidates) {
                if (isFactoryMethod(am)) {
                    result.add(am);
                }
            }
            return result;
        }
        return candidates;
    }

    public Constructor<?> findSingleArgConstructor(Class<?>... argTypes) {
        for (AnnotatedConstructor ac : this._classInfo.getConstructors()) {
            if (ac.getParameterCount() == 1) {
                Class<?> actArg = ac.getParameterClass(0);
                for (Class<?> expArg : argTypes) {
                    if (expArg == actArg) {
                        return ac.getAnnotated();
                    }
                }
            }
        }
        return null;
    }

    public Method findFactoryMethod(Class<?>... expArgTypes) {
        for (AnnotatedMethod am : this._classInfo.getStaticMethods()) {
            if (isFactoryMethod(am)) {
                Class<?> actualArgType = am.getParameterClass(0);
                for (Class<?> expArgType : expArgTypes) {
                    if (actualArgType.isAssignableFrom(expArgType)) {
                        return am.getAnnotated();
                    }
                }
            }
        }
        return null;
    }

    protected boolean isFactoryMethod(AnnotatedMethod am) {
        Class<?> rt = am.getRawType();
        if (getBeanClass().isAssignableFrom(rt)) {
            return this._annotationIntrospector.hasCreatorAnnotation(am) || "valueOf".equals(am.getName());
        }
        return false;
    }

    public List<String> findCreatorPropertyNames() {
        String name;
        List<String> names = null;
        int i = 0;
        while (i < 2) {
            List<? extends AnnotatedWithParams> l = i == 0 ? getConstructors() : getFactoryMethods();
            for (AnnotatedWithParams creator : l) {
                int argCount = creator.getParameterCount();
                if (argCount >= 1 && (name = this._annotationIntrospector.findPropertyNameForParam(creator.getParameter(0))) != null) {
                    if (names == null) {
                        names = new ArrayList<>();
                    }
                    names.add(name);
                    for (int p = 1; p < argCount; p++) {
                        names.add(this._annotationIntrospector.findPropertyNameForParam(creator.getParameter(p)));
                    }
                }
            }
            i++;
        }
        if (names == null) {
            List<String> names2 = Collections.emptyList();
            return names2;
        }
        return names;
    }

    public LinkedHashMap<String, AnnotatedField> findSerializableFields(VisibilityChecker<?> vchecker, Collection<String> ignoredProperties) {
        return _findPropertyFields(vchecker, ignoredProperties, true);
    }

    public JsonSerialize.Inclusion findSerializationInclusion(JsonSerialize.Inclusion defValue) {
        return this._annotationIntrospector.findSerializationInclusion(this._classInfo, defValue);
    }

    @Override // org.codehaus.jackson.map.BeanDescription
    public LinkedHashMap<String, AnnotatedMethod> findSetters(VisibilityChecker<?> vchecker) {
        LinkedHashMap<String, AnnotatedMethod> results = new LinkedHashMap<>();
        PropertyNamingStrategy naming = this._config.getPropertyNamingStrategy();
        for (AnnotatedMethod am : this._classInfo.memberMethods()) {
            if (am.getParameterCount() == 1) {
                String propName = this._annotationIntrospector.findSettablePropertyName(am);
                if (propName != null) {
                    if (propName.length() == 0) {
                        propName = okNameForSetter(am);
                        if (propName == null) {
                            propName = am.getName();
                        }
                        if (naming != null) {
                            propName = naming.nameForSetterMethod(this._config, am, propName);
                        }
                    }
                } else if (vchecker.isSetterVisible(am) && (propName = okNameForSetter(am)) != null) {
                    if (naming != null) {
                        propName = naming.nameForSetterMethod(this._config, am, propName);
                    }
                }
                AnnotatedMethod old = results.put(propName, am);
                if (old == null) {
                    continue;
                } else {
                    if (old.getDeclaringClass() == am.getDeclaringClass()) {
                        String oldDesc = old.getFullName();
                        String newDesc = am.getFullName();
                        throw new IllegalArgumentException("Conflicting setter definitions for property \"" + propName + "\": " + oldDesc + " vs " + newDesc);
                    }
                    results.put(propName, old);
                }
            }
        }
        return results;
    }

    public AnnotatedMethod findAnySetter() throws IllegalArgumentException {
        AnnotatedMethod found = null;
        for (AnnotatedMethod am : this._classInfo.memberMethods()) {
            if (this._annotationIntrospector.hasAnySetterAnnotation(am)) {
                if (found != null) {
                    throw new IllegalArgumentException("Multiple methods with 'any-setter' annotation (" + found.getName() + "(), " + am.getName() + ")");
                }
                int pcount = am.getParameterCount();
                if (pcount != 2) {
                    throw new IllegalArgumentException("Invalid 'any-setter' annotation on method " + am.getName() + "(): takes " + pcount + " parameters, should take 2");
                }
                Class<?> type = am.getParameterClass(0);
                if (type != String.class && type != Object.class) {
                    throw new IllegalArgumentException("Invalid 'any-setter' annotation on method " + am.getName() + "(): first argument not of type String or Object, but " + type.getName());
                }
                found = am;
            }
        }
        return found;
    }

    public AnnotatedMethod findAnyGetter() throws IllegalArgumentException {
        AnnotatedMethod found = null;
        for (AnnotatedMethod am : this._classInfo.memberMethods()) {
            if (this._annotationIntrospector.hasAnyGetterAnnotation(am)) {
                if (found != null) {
                    throw new IllegalArgumentException("Multiple methods with 'any-getter' annotation (" + found.getName() + "(), " + am.getName() + ")");
                }
                Class<?> type = am.getRawType();
                if (!Map.class.isAssignableFrom(type)) {
                    throw new IllegalArgumentException("Invalid 'any-getter' annotation on method " + am.getName() + "(): return type is not instance of java.util.Map");
                }
                found = am;
            }
        }
        return found;
    }

    public Map<String, AnnotatedMember> findBackReferenceProperties() {
        AnnotationIntrospector.ReferenceProperty prop;
        HashMap<String, AnnotatedMember> result = null;
        for (AnnotatedMethod am : this._classInfo.memberMethods()) {
            if (am.getParameterCount() == 1 && (prop = this._annotationIntrospector.findReferenceType(am)) != null && prop.isBackReference()) {
                if (result == null) {
                    result = new HashMap<>();
                }
                if (result.put(prop.getName(), am) != null) {
                    throw new IllegalArgumentException("Multiple back-reference properties with name '" + prop.getName() + "'");
                }
            }
        }
        for (AnnotatedField af : this._classInfo.fields()) {
            AnnotationIntrospector.ReferenceProperty prop2 = this._annotationIntrospector.findReferenceType(af);
            if (prop2 != null && prop2.isBackReference()) {
                if (result == null) {
                    result = new HashMap<>();
                }
                if (result.put(prop2.getName(), af) != null) {
                    throw new IllegalArgumentException("Multiple back-reference properties with name '" + prop2.getName() + "'");
                }
            }
        }
        return result;
    }

    public LinkedHashMap<String, AnnotatedField> findDeserializableFields(VisibilityChecker<?> vchecker, Collection<String> ignoredProperties) {
        return _findPropertyFields(vchecker, ignoredProperties, false);
    }

    public String okNameForAnyGetter(AnnotatedMethod am, String name) {
        String str = okNameForIsGetter(am, name);
        if (str == null) {
            return okNameForGetter(am, name);
        }
        return str;
    }

    public String okNameForGetter(AnnotatedMethod am, String name) {
        if (!name.startsWith("get")) {
            return null;
        }
        if ("getCallbacks".equals(name)) {
            if (isCglibGetCallbacks(am)) {
                return null;
            }
        } else if ("getMetaClass".equals(name) && isGroovyMetaClassGetter(am)) {
            return null;
        }
        return mangleGetterName(am, name.substring(3));
    }

    public String okNameForIsGetter(AnnotatedMethod am, String name) {
        if (!name.startsWith("is")) {
            return null;
        }
        Class<?> rt = am.getRawType();
        if (rt == Boolean.class || rt == Boolean.TYPE) {
            return mangleGetterName(am, name.substring(2));
        }
        return null;
    }

    protected String mangleGetterName(Annotated a, String basename) {
        return manglePropertyName(basename);
    }

    protected boolean isCglibGetCallbacks(AnnotatedMethod am) {
        Class<?> rt = am.getRawType();
        if (rt == null || !rt.isArray()) {
            return false;
        }
        Class<?> compType = rt.getComponentType();
        Package pkg = compType.getPackage();
        if (pkg == null) {
            return false;
        }
        String pname = pkg.getName();
        return pname.startsWith("net.sf.cglib") || pname.startsWith("org.hibernate.repackage.cglib");
    }

    protected boolean isGroovyMetaClassSetter(AnnotatedMethod am) {
        Class<?> argType = am.getParameterClass(0);
        Package pkg = argType.getPackage();
        return pkg != null && pkg.getName().startsWith("groovy.lang");
    }

    protected boolean isGroovyMetaClassGetter(AnnotatedMethod am) {
        Package pkg;
        Class<?> rt = am.getRawType();
        return (rt == null || rt.isArray() || (pkg = rt.getPackage()) == null || !pkg.getName().startsWith("groovy.lang")) ? false : true;
    }

    public String okNameForSetter(AnnotatedMethod am) {
        String name;
        String name2 = am.getName();
        if (!name2.startsWith("set") || (name = mangleSetterName(am, name2.substring(3))) == null) {
            return null;
        }
        if ("metaClass".equals(name) && isGroovyMetaClassSetter(am)) {
            return null;
        }
        return name;
    }

    protected String mangleSetterName(Annotated a, String basename) {
        return manglePropertyName(basename);
    }

    public LinkedHashMap<String, AnnotatedField> _findPropertyFields(VisibilityChecker<?> vchecker, Collection<String> ignoredProperties, boolean forSerialization) {
        LinkedHashMap<String, AnnotatedField> results = new LinkedHashMap<>();
        PropertyNamingStrategy naming = this._config.getPropertyNamingStrategy();
        for (AnnotatedField af : this._classInfo.fields()) {
            String propName = forSerialization ? this._annotationIntrospector.findSerializablePropertyName(af) : this._annotationIntrospector.findDeserializablePropertyName(af);
            if (propName != null) {
                if (propName.length() == 0) {
                    propName = af.getName();
                    if (naming != null) {
                        propName = naming.nameForField(this._config, af, propName);
                    }
                }
            } else if (vchecker.isFieldVisible(af)) {
                propName = af.getName();
                if (naming != null) {
                    propName = naming.nameForField(this._config, af, propName);
                }
            } else {
                continue;
            }
            if (ignoredProperties == null || !ignoredProperties.contains(propName)) {
                AnnotatedField old = results.put(propName, af);
                if (old != null && old.getDeclaringClass() == af.getDeclaringClass()) {
                    String oldDesc = old.getFullName();
                    String newDesc = af.getFullName();
                    throw new IllegalArgumentException("Multiple fields representing property \"" + propName + "\": " + oldDesc + " vs " + newDesc);
                }
            }
        }
        return results;
    }

    public static String manglePropertyName(String basename) {
        int len = basename.length();
        if (len == 0) {
            return null;
        }
        StringBuilder sb = null;
        for (int i = 0; i < len; i++) {
            char upper = basename.charAt(i);
            char lower = Character.toLowerCase(upper);
            if (upper == lower) {
                break;
            }
            if (sb == null) {
                sb = new StringBuilder(basename);
            }
            sb.setCharAt(i, lower);
        }
        return sb != null ? sb.toString() : basename;
    }

    public static String descFor(AnnotatedElement elem) {
        if (elem instanceof Class) {
            return "class " + ((Class) elem).getName();
        }
        if (elem instanceof Method) {
            Method m = (Method) elem;
            return "method " + m.getName() + " (from class " + m.getDeclaringClass().getName() + ")";
        }
        if (elem instanceof Constructor) {
            Constructor<?> ctor = (Constructor) elem;
            return "constructor() (from class " + ctor.getDeclaringClass().getName() + ")";
        }
        return "unknown type [" + elem.getClass() + "]";
    }
}
