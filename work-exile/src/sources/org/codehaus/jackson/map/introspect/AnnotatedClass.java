package org.codehaus.jackson.map.introspect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.ClassIntrospector;
import org.codehaus.jackson.map.util.Annotations;
import org.codehaus.jackson.map.util.ArrayBuilders;
import org.codehaus.jackson.map.util.ClassUtil;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class AnnotatedClass extends Annotated {
    private static final AnnotationMap[] NO_ANNOTATION_MAPS = new AnnotationMap[0];
    protected final AnnotationIntrospector _annotationIntrospector;
    protected final Class<?> _class;
    protected AnnotationMap _classAnnotations;
    protected List<AnnotatedConstructor> _constructors;
    protected List<AnnotatedMethod> _creatorMethods;
    protected AnnotatedConstructor _defaultConstructor;
    protected List<AnnotatedField> _fields;
    protected List<AnnotatedField> _ignoredFields;
    protected List<AnnotatedMethod> _ignoredMethods;
    protected AnnotatedMethodMap _memberMethods;
    protected final ClassIntrospector.MixInResolver _mixInResolver;
    protected final Class<?> _primaryMixIn;
    protected final Collection<Class<?>> _superTypes;

    private AnnotatedClass(Class<?> cls, List<Class<?>> superTypes, AnnotationIntrospector aintr, ClassIntrospector.MixInResolver mir) {
        this._class = cls;
        this._superTypes = superTypes;
        this._annotationIntrospector = aintr;
        this._mixInResolver = mir;
        this._primaryMixIn = this._mixInResolver == null ? null : this._mixInResolver.findMixInClassFor(this._class);
    }

    public static AnnotatedClass construct(Class<?> cls, AnnotationIntrospector aintr, ClassIntrospector.MixInResolver mir) {
        List<Class<?>> st = ClassUtil.findSuperTypes(cls, null);
        AnnotatedClass ac = new AnnotatedClass(cls, st, aintr, mir);
        ac.resolveClassAnnotations();
        return ac;
    }

    public static AnnotatedClass constructWithoutSuperTypes(Class<?> cls, AnnotationIntrospector aintr, ClassIntrospector.MixInResolver mir) {
        List<Class<?>> empty = Collections.emptyList();
        AnnotatedClass ac = new AnnotatedClass(cls, empty, aintr, mir);
        ac.resolveClassAnnotations();
        return ac;
    }

    @Override // org.codehaus.jackson.map.introspect.Annotated
    public Class<?> getAnnotated() {
        return this._class;
    }

    @Override // org.codehaus.jackson.map.introspect.Annotated
    public int getModifiers() {
        return this._class.getModifiers();
    }

    @Override // org.codehaus.jackson.map.introspect.Annotated
    public String getName() {
        return this._class.getName();
    }

    @Override // org.codehaus.jackson.map.introspect.Annotated
    public <A extends Annotation> A getAnnotation(Class<A> cls) {
        if (this._classAnnotations == null) {
            return null;
        }
        return (A) this._classAnnotations.get(cls);
    }

    @Override // org.codehaus.jackson.map.introspect.Annotated
    public Type getGenericType() {
        return this._class;
    }

    @Override // org.codehaus.jackson.map.introspect.Annotated
    public Class<?> getRawType() {
        return this._class;
    }

    public Annotations getAnnotations() {
        return this._classAnnotations;
    }

    public boolean hasAnnotations() {
        return this._classAnnotations.size() > 0;
    }

    public AnnotatedConstructor getDefaultConstructor() {
        return this._defaultConstructor;
    }

    public List<AnnotatedConstructor> getConstructors() {
        return this._constructors == null ? Collections.emptyList() : this._constructors;
    }

    public List<AnnotatedMethod> getStaticMethods() {
        return this._creatorMethods == null ? Collections.emptyList() : this._creatorMethods;
    }

    public Iterable<AnnotatedMethod> memberMethods() {
        return this._memberMethods;
    }

    public Iterable<AnnotatedMethod> ignoredMemberMethods() {
        return this._ignoredMethods == null ? Collections.emptyList() : this._ignoredMethods;
    }

    public int getMemberMethodCount() {
        return this._memberMethods.size();
    }

    public AnnotatedMethod findMethod(String name, Class<?>[] paramTypes) {
        return this._memberMethods.find(name, paramTypes);
    }

    public int getFieldCount() {
        if (this._fields == null) {
            return 0;
        }
        return this._fields.size();
    }

    public Iterable<AnnotatedField> fields() {
        return this._fields == null ? Collections.emptyList() : this._fields;
    }

    public Iterable<AnnotatedField> ignoredFields() {
        return this._ignoredFields == null ? Collections.emptyList() : this._ignoredFields;
    }

    protected void resolveClassAnnotations() {
        this._classAnnotations = new AnnotationMap();
        if (this._primaryMixIn != null) {
            _addClassMixIns(this._classAnnotations, this._class, this._primaryMixIn);
        }
        Annotation[] arr$ = this._class.getDeclaredAnnotations();
        for (Annotation a : arr$) {
            if (this._annotationIntrospector.isHandled(a)) {
                this._classAnnotations.addIfNotPresent(a);
            }
        }
        for (Class<?> cls : this._superTypes) {
            _addClassMixIns(this._classAnnotations, cls);
            Annotation[] arr$2 = cls.getDeclaredAnnotations();
            for (Annotation a2 : arr$2) {
                if (this._annotationIntrospector.isHandled(a2)) {
                    this._classAnnotations.addIfNotPresent(a2);
                }
            }
        }
        _addClassMixIns(this._classAnnotations, Object.class);
    }

    protected void _addClassMixIns(AnnotationMap annotations, Class<?> toMask) {
        if (this._mixInResolver != null) {
            _addClassMixIns(annotations, toMask, this._mixInResolver.findMixInClassFor(toMask));
        }
    }

    protected void _addClassMixIns(AnnotationMap annotations, Class<?> toMask, Class<?> mixin) {
        if (mixin != null) {
            Annotation[] arr$ = mixin.getDeclaredAnnotations();
            for (Annotation a : arr$) {
                if (this._annotationIntrospector.isHandled(a)) {
                    annotations.addIfNotPresent(a);
                }
            }
            for (Class<?> parent : ClassUtil.findSuperTypes(mixin, toMask)) {
                Annotation[] arr$2 = parent.getDeclaredAnnotations();
                for (Annotation a2 : arr$2) {
                    if (this._annotationIntrospector.isHandled(a2)) {
                        annotations.addIfNotPresent(a2);
                    }
                }
            }
        }
    }

    public void resolveCreators(boolean includeAll) {
        this._constructors = null;
        for (Constructor<?> ctor : this._class.getDeclaredConstructors()) {
            switch (ctor.getParameterTypes().length) {
                case 0:
                    this._defaultConstructor = _constructConstructor(ctor, true);
                    break;
                default:
                    if (includeAll) {
                        if (this._constructors == null) {
                            this._constructors = new ArrayList();
                        }
                        this._constructors.add(_constructConstructor(ctor, false));
                    }
                    break;
            }
        }
        if (this._primaryMixIn != null && (this._defaultConstructor != null || this._constructors != null)) {
            _addConstructorMixIns(this._primaryMixIn);
        }
        if (this._annotationIntrospector != null) {
            if (this._defaultConstructor != null && this._annotationIntrospector.isIgnorableConstructor(this._defaultConstructor)) {
                this._defaultConstructor = null;
            }
            if (this._constructors != null) {
                int i = this._constructors.size();
                while (true) {
                    i--;
                    if (i >= 0) {
                        if (this._annotationIntrospector.isIgnorableConstructor(this._constructors.get(i))) {
                            this._constructors.remove(i);
                        }
                    }
                }
            }
        }
        this._creatorMethods = null;
        if (includeAll) {
            Method[] arr$ = this._class.getDeclaredMethods();
            for (Method m : arr$) {
                if (Modifier.isStatic(m.getModifiers())) {
                    int argCount = m.getParameterTypes().length;
                    if (argCount >= 1) {
                        if (this._creatorMethods == null) {
                            this._creatorMethods = new ArrayList();
                        }
                        this._creatorMethods.add(_constructCreatorMethod(m));
                    }
                }
            }
            if (this._primaryMixIn != null && this._creatorMethods != null) {
                _addFactoryMixIns(this._primaryMixIn);
            }
            if (this._annotationIntrospector != null && this._creatorMethods != null) {
                int i2 = this._creatorMethods.size();
                while (true) {
                    i2--;
                    if (i2 >= 0) {
                        if (this._annotationIntrospector.isIgnorableMethod(this._creatorMethods.get(i2))) {
                            this._creatorMethods.remove(i2);
                        }
                    } else {
                        return;
                    }
                }
            }
        }
    }

    protected void _addConstructorMixIns(Class<?> mixin) {
        MemberKey[] ctorKeys = null;
        int ctorCount = this._constructors == null ? 0 : this._constructors.size();
        for (Constructor<?> ctor : mixin.getDeclaredConstructors()) {
            switch (ctor.getParameterTypes().length) {
                case 0:
                    if (this._defaultConstructor != null) {
                        _addMixOvers(ctor, this._defaultConstructor, false);
                    }
                    break;
                default:
                    if (ctorKeys == null) {
                        ctorKeys = new MemberKey[ctorCount];
                        for (int i = 0; i < ctorCount; i++) {
                            ctorKeys[i] = new MemberKey(this._constructors.get(i).getAnnotated());
                        }
                    }
                    MemberKey key = new MemberKey(ctor);
                    for (int i2 = 0; i2 < ctorCount; i2++) {
                        if (key.equals(ctorKeys[i2])) {
                            _addMixOvers(ctor, this._constructors.get(i2), true);
                        }
                        break;
                    }
                    break;
            }
        }
    }

    protected void _addFactoryMixIns(Class<?> mixin) {
        MemberKey[] methodKeys = null;
        int methodCount = this._creatorMethods.size();
        Method[] arr$ = mixin.getDeclaredMethods();
        for (Method m : arr$) {
            if (Modifier.isStatic(m.getModifiers()) && m.getParameterTypes().length != 0) {
                if (methodKeys == null) {
                    methodKeys = new MemberKey[methodCount];
                    for (int i = 0; i < methodCount; i++) {
                        methodKeys[i] = new MemberKey(this._creatorMethods.get(i).getAnnotated());
                    }
                }
                MemberKey key = new MemberKey(m);
                for (int i2 = 0; i2 < methodCount; i2++) {
                    if (key.equals(methodKeys[i2])) {
                        _addMixOvers(m, this._creatorMethods.get(i2), true);
                        break;
                    }
                }
            }
        }
    }

    public void resolveMemberMethods(MethodFilter methodFilter, boolean collectIgnored) {
        Class<?> mixin;
        this._memberMethods = new AnnotatedMethodMap();
        AnnotatedMethodMap mixins = new AnnotatedMethodMap();
        _addMemberMethods(this._class, methodFilter, this._memberMethods, this._primaryMixIn, mixins);
        for (Class<?> cls : this._superTypes) {
            Class<?> mixin2 = this._mixInResolver == null ? null : this._mixInResolver.findMixInClassFor(cls);
            _addMemberMethods(cls, methodFilter, this._memberMethods, mixin2, mixins);
        }
        if (this._mixInResolver != null && (mixin = this._mixInResolver.findMixInClassFor(Object.class)) != null) {
            _addMethodMixIns(methodFilter, this._memberMethods, mixin, mixins);
        }
        if (this._annotationIntrospector != null) {
            if (!mixins.isEmpty()) {
                for (AnnotatedMethod mixIn : mixins) {
                    try {
                        Method m = Object.class.getDeclaredMethod(mixIn.getName(), mixIn.getParameterClasses());
                        if (m != null) {
                            AnnotatedMethod am = _constructMethod(m);
                            _addMixOvers(mixIn.getAnnotated(), am, false);
                            this._memberMethods.add(am);
                        }
                    } catch (Exception e) {
                    }
                }
            }
            Iterator<AnnotatedMethod> it = this._memberMethods.iterator();
            while (it.hasNext()) {
                AnnotatedMethod am2 = it.next();
                if (this._annotationIntrospector.isIgnorableMethod(am2)) {
                    it.remove();
                    if (collectIgnored) {
                        this._ignoredMethods = ArrayBuilders.addToList(this._ignoredMethods, am2);
                    }
                }
            }
        }
    }

    protected void _addMemberMethods(Class<?> cls, MethodFilter methodFilter, AnnotatedMethodMap methods, Class<?> mixInCls, AnnotatedMethodMap mixIns) {
        if (mixInCls != null) {
            _addMethodMixIns(methodFilter, methods, mixInCls, mixIns);
        }
        if (cls != null) {
            Method[] arr$ = cls.getDeclaredMethods();
            for (Method m : arr$) {
                if (_isIncludableMethod(m, methodFilter)) {
                    AnnotatedMethod old = methods.find(m);
                    if (old == null) {
                        AnnotatedMethod newM = _constructMethod(m);
                        methods.add(newM);
                        AnnotatedMethod old2 = mixIns.remove(m);
                        if (old2 != null) {
                            _addMixOvers(old2.getAnnotated(), newM, false);
                        }
                    } else {
                        _addMixUnders(m, old);
                        if (old.getDeclaringClass().isInterface() && !m.getDeclaringClass().isInterface()) {
                            methods.add(old.withMethod(m));
                        }
                    }
                }
            }
        }
    }

    protected void _addMethodMixIns(MethodFilter methodFilter, AnnotatedMethodMap methods, Class<?> mixInCls, AnnotatedMethodMap mixIns) {
        Method[] arr$ = mixInCls.getDeclaredMethods();
        for (Method m : arr$) {
            if (_isIncludableMethod(m, methodFilter)) {
                AnnotatedMethod am = methods.find(m);
                if (am != null) {
                    _addMixUnders(m, am);
                } else {
                    mixIns.add(_constructMethod(m));
                }
            }
        }
    }

    public void resolveFields(boolean collectIgnored) {
        LinkedHashMap<String, AnnotatedField> foundFields = new LinkedHashMap<>();
        _addFields(foundFields, this._class);
        if (this._annotationIntrospector != null) {
            Iterator<Map.Entry<String, AnnotatedField>> it = foundFields.entrySet().iterator();
            while (it.hasNext()) {
                AnnotatedField f = it.next().getValue();
                if (this._annotationIntrospector.isIgnorableField(f)) {
                    it.remove();
                    if (collectIgnored) {
                        this._ignoredFields = ArrayBuilders.addToList(this._ignoredFields, f);
                    }
                }
            }
        }
        if (foundFields.isEmpty()) {
            this._fields = Collections.emptyList();
        } else {
            this._fields = new ArrayList(foundFields.size());
            this._fields.addAll(foundFields.values());
        }
    }

    protected void _addFields(Map<String, AnnotatedField> fields, Class<?> c) {
        Class<?> mixin;
        Class<?> parent = c.getSuperclass();
        if (parent != null) {
            _addFields(fields, parent);
            Field[] arr$ = c.getDeclaredFields();
            for (Field f : arr$) {
                if (_isIncludableField(f)) {
                    fields.put(f.getName(), _constructField(f));
                }
            }
            if (this._mixInResolver != null && (mixin = this._mixInResolver.findMixInClassFor(c)) != null) {
                _addFieldMixIns(mixin, fields);
            }
        }
    }

    protected void _addFieldMixIns(Class<?> mixin, Map<String, AnnotatedField> fields) {
        Field[] arr$ = mixin.getDeclaredFields();
        for (Field mixinField : arr$) {
            if (_isIncludableField(mixinField)) {
                String name = mixinField.getName();
                AnnotatedField maskedField = fields.get(name);
                if (maskedField != null) {
                    Annotation[] arr$2 = mixinField.getDeclaredAnnotations();
                    for (Annotation a : arr$2) {
                        if (this._annotationIntrospector.isHandled(a)) {
                            maskedField.addOrOverride(a);
                        }
                    }
                }
            }
        }
    }

    protected AnnotatedMethod _constructMethod(Method m) {
        return this._annotationIntrospector == null ? new AnnotatedMethod(m, _emptyAnnotationMap(), null) : new AnnotatedMethod(m, _collectRelevantAnnotations(m.getDeclaredAnnotations()), null);
    }

    protected AnnotatedConstructor _constructConstructor(Constructor<?> ctor, boolean defaultCtor) {
        if (this._annotationIntrospector == null) {
            return new AnnotatedConstructor(ctor, _emptyAnnotationMap(), _emptyAnnotationMaps(ctor.getParameterTypes().length));
        }
        return new AnnotatedConstructor(ctor, _collectRelevantAnnotations(ctor.getDeclaredAnnotations()), defaultCtor ? null : _collectRelevantAnnotations(ctor.getParameterAnnotations()));
    }

    protected AnnotatedMethod _constructCreatorMethod(Method m) {
        return this._annotationIntrospector == null ? new AnnotatedMethod(m, _emptyAnnotationMap(), _emptyAnnotationMaps(m.getParameterTypes().length)) : new AnnotatedMethod(m, _collectRelevantAnnotations(m.getDeclaredAnnotations()), _collectRelevantAnnotations(m.getParameterAnnotations()));
    }

    protected AnnotatedField _constructField(Field f) {
        return this._annotationIntrospector == null ? new AnnotatedField(f, _emptyAnnotationMap()) : new AnnotatedField(f, _collectRelevantAnnotations(f.getDeclaredAnnotations()));
    }

    protected AnnotationMap[] _collectRelevantAnnotations(Annotation[][] anns) {
        int len = anns.length;
        AnnotationMap[] result = new AnnotationMap[len];
        for (int i = 0; i < len; i++) {
            result[i] = _collectRelevantAnnotations(anns[i]);
        }
        return result;
    }

    protected AnnotationMap _collectRelevantAnnotations(Annotation[] anns) {
        AnnotationMap annMap = new AnnotationMap();
        if (anns != null) {
            for (Annotation a : anns) {
                if (this._annotationIntrospector.isHandled(a)) {
                    annMap.add(a);
                }
            }
        }
        return annMap;
    }

    private AnnotationMap _emptyAnnotationMap() {
        return new AnnotationMap();
    }

    private AnnotationMap[] _emptyAnnotationMaps(int count) {
        if (count == 0) {
            return NO_ANNOTATION_MAPS;
        }
        AnnotationMap[] maps = new AnnotationMap[count];
        for (int i = 0; i < count; i++) {
            maps[i] = _emptyAnnotationMap();
        }
        return maps;
    }

    protected boolean _isIncludableMethod(Method m, MethodFilter filter) {
        return ((filter != null && !filter.includeMethod(m)) || m.isSynthetic() || m.isBridge()) ? false : true;
    }

    private boolean _isIncludableField(Field f) {
        if (f.isSynthetic()) {
            return false;
        }
        int mods = f.getModifiers();
        return (Modifier.isStatic(mods) || Modifier.isTransient(mods)) ? false : true;
    }

    protected void _addMixOvers(Constructor<?> mixin, AnnotatedConstructor target, boolean addParamAnnotations) {
        Annotation[] arr$ = mixin.getDeclaredAnnotations();
        for (Annotation a : arr$) {
            if (this._annotationIntrospector.isHandled(a)) {
                target.addOrOverride(a);
            }
        }
        if (addParamAnnotations) {
            Annotation[][] pa = mixin.getParameterAnnotations();
            int len = pa.length;
            for (int i = 0; i < len; i++) {
                for (Annotation annotation : pa[i]) {
                    target.addOrOverrideParam(i, annotation);
                }
            }
        }
    }

    protected void _addMixOvers(Method mixin, AnnotatedMethod target, boolean addParamAnnotations) {
        Annotation[] arr$ = mixin.getDeclaredAnnotations();
        for (Annotation a : arr$) {
            if (this._annotationIntrospector.isHandled(a)) {
                target.addOrOverride(a);
            }
        }
        if (addParamAnnotations) {
            Annotation[][] pa = mixin.getParameterAnnotations();
            int len = pa.length;
            for (int i = 0; i < len; i++) {
                for (Annotation annotation : pa[i]) {
                    target.addOrOverrideParam(i, annotation);
                }
            }
        }
    }

    protected void _addMixUnders(Method src, AnnotatedMethod target) {
        Annotation[] arr$ = src.getDeclaredAnnotations();
        for (Annotation a : arr$) {
            if (this._annotationIntrospector.isHandled(a)) {
                target.addIfNotPresent(a);
            }
        }
    }

    public String toString() {
        return "[AnnotedClass " + this._class.getName() + "]";
    }
}
