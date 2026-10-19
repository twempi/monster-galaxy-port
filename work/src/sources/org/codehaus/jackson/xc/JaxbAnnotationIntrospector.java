package org.codehaus.jackson.xc;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.ref.SoftReference;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.Versioned;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.KeyDeserializer;
import org.codehaus.jackson.map.MapperConfig;
import org.codehaus.jackson.map.annotate.JsonCachable;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.introspect.Annotated;
import org.codehaus.jackson.map.introspect.AnnotatedClass;
import org.codehaus.jackson.map.introspect.AnnotatedConstructor;
import org.codehaus.jackson.map.introspect.AnnotatedField;
import org.codehaus.jackson.map.introspect.AnnotatedMember;
import org.codehaus.jackson.map.introspect.AnnotatedMethod;
import org.codehaus.jackson.map.introspect.AnnotatedParameter;
import org.codehaus.jackson.map.introspect.VisibilityChecker;
import org.codehaus.jackson.map.jsontype.NamedType;
import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
import org.codehaus.jackson.map.jsontype.impl.StdTypeResolverBuilder;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.util.VersionUtil;

/* JADX INFO: loaded from: classes.dex */
public class JaxbAnnotationIntrospector extends AnnotationIntrospector implements Versioned {
    protected static final String MARKER_FOR_DEFAULT = "##default";
    private static final ThreadLocal<SoftReference<PropertyDescriptors>> _propertyDescriptors = new ThreadLocal<>();
    protected final JsonDeserializer<?> _dataHandlerDeserializer;
    protected final JsonSerializer<?> _dataHandlerSerializer;
    protected final String _jaxbPackageName = XmlElement.class.getPackage().getName();

    public JaxbAnnotationIntrospector() {
        JsonSerializer<?> dataHandlerSerializer = null;
        JsonDeserializer<?> dataHandlerDeserializer = null;
        try {
            dataHandlerSerializer = (JsonSerializer) Class.forName("org.codehaus.jackson.xc.DataHandlerJsonSerializer").newInstance();
            dataHandlerDeserializer = (JsonDeserializer) Class.forName("org.codehaus.jackson.xc.DataHandlerJsonDeserializer").newInstance();
        } catch (Throwable th) {
        }
        this._dataHandlerSerializer = dataHandlerSerializer;
        this._dataHandlerDeserializer = dataHandlerDeserializer;
    }

    @Override // org.codehaus.jackson.Versioned
    public Version version() {
        return VersionUtil.versionFor(getClass());
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public boolean isHandled(Annotation ann) {
        Class<?> cls = ann.annotationType();
        Package pkg = cls.getPackage();
        String pkgName = pkg != null ? pkg.getName() : cls.getName();
        return pkgName.startsWith(this._jaxbPackageName) || cls == JsonCachable.class;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public Boolean findCachability(AnnotatedClass ac) {
        JsonCachable ann = (JsonCachable) ac.getAnnotation(JsonCachable.class);
        if (ann != null) {
            return ann.value() ? Boolean.TRUE : Boolean.FALSE;
        }
        return null;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public String findRootName(AnnotatedClass ac) {
        XmlRootElement elem = findRootElementAnnotation(ac);
        if (elem == null) {
            return null;
        }
        String name = elem.name();
        return MARKER_FOR_DEFAULT.equals(name) ? "" : name;
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
    public Boolean isIgnorableType(AnnotatedClass ac) {
        return null;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public VisibilityChecker<?> findAutoDetectVisibility(AnnotatedClass ac, VisibilityChecker<?> checker) {
        XmlAccessType at = findAccessType(ac);
        if (at != null) {
            switch (AnonymousClass1.$SwitchMap$javax$xml$bind$annotation$XmlAccessType[at.ordinal()]) {
                case 1:
                    return checker.withFieldVisibility(JsonAutoDetect.Visibility.ANY).withSetterVisibility(JsonAutoDetect.Visibility.NONE).withGetterVisibility(JsonAutoDetect.Visibility.NONE).withIsGetterVisibility(JsonAutoDetect.Visibility.NONE);
                case 2:
                    return checker.withFieldVisibility(JsonAutoDetect.Visibility.NONE).withSetterVisibility(JsonAutoDetect.Visibility.NONE).withGetterVisibility(JsonAutoDetect.Visibility.NONE).withIsGetterVisibility(JsonAutoDetect.Visibility.NONE);
                case 3:
                    return checker.withFieldVisibility(JsonAutoDetect.Visibility.NONE).withSetterVisibility(JsonAutoDetect.Visibility.PUBLIC_ONLY).withGetterVisibility(JsonAutoDetect.Visibility.PUBLIC_ONLY).withIsGetterVisibility(JsonAutoDetect.Visibility.PUBLIC_ONLY);
                case 4:
                    return checker.withFieldVisibility(JsonAutoDetect.Visibility.PUBLIC_ONLY).withSetterVisibility(JsonAutoDetect.Visibility.PUBLIC_ONLY).withGetterVisibility(JsonAutoDetect.Visibility.PUBLIC_ONLY).withIsGetterVisibility(JsonAutoDetect.Visibility.PUBLIC_ONLY);
                default:
                    return checker;
            }
        }
        return checker;
    }

    /* JADX INFO: renamed from: org.codehaus.jackson.xc.JaxbAnnotationIntrospector$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$javax$xml$bind$annotation$XmlAccessType = new int[XmlAccessType.values().length];

        static {
            try {
                $SwitchMap$javax$xml$bind$annotation$XmlAccessType[XmlAccessType.FIELD.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$javax$xml$bind$annotation$XmlAccessType[XmlAccessType.NONE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$javax$xml$bind$annotation$XmlAccessType[XmlAccessType.PROPERTY.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$javax$xml$bind$annotation$XmlAccessType[XmlAccessType.PUBLIC_MEMBER.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    protected XmlAccessType findAccessType(Annotated ac) {
        XmlAccessorType at = findAnnotation(XmlAccessorType.class, ac, true, true, true);
        if (at == null) {
            return null;
        }
        return at.value();
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public TypeResolverBuilder<?> findTypeResolver(MapperConfig<?> config, AnnotatedClass ac, JavaType baseType) {
        return null;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public TypeResolverBuilder<?> findPropertyTypeResolver(MapperConfig<?> config, AnnotatedMember am, JavaType baseType) {
        if (baseType.isContainerType()) {
            return null;
        }
        return _typeResolverFromXmlElements(am);
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public TypeResolverBuilder<?> findPropertyContentTypeResolver(MapperConfig<?> config, AnnotatedMember am, JavaType containerType) {
        if (!containerType.isContainerType()) {
            throw new IllegalArgumentException("Must call method with a container type (got " + containerType + ")");
        }
        return _typeResolverFromXmlElements(am);
    }

    protected TypeResolverBuilder<?> _typeResolverFromXmlElements(AnnotatedMember am) {
        XmlElements elems = findAnnotation(XmlElements.class, am, false, false, false);
        XmlElementRefs elemRefs = findAnnotation(XmlElementRefs.class, am, false, false, false);
        if (elems == null && elemRefs == null) {
            return null;
        }
        TypeResolverBuilder<?> b = new StdTypeResolverBuilder();
        return b.init(JsonTypeInfo.Id.NAME, null).inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public List<NamedType> findSubtypes(Annotated a) {
        XmlRootElement rootElement;
        XmlElements elems = findAnnotation(XmlElements.class, a, false, false, false);
        if (elems != null) {
            ArrayList<NamedType> result = new ArrayList<>();
            XmlElement[] arr$ = elems.value();
            for (XmlElement elem : arr$) {
                String name = elem.name();
                if (MARKER_FOR_DEFAULT.equals(name)) {
                    name = null;
                }
                result.add(new NamedType(elem.type(), name));
            }
            return result;
        }
        XmlElementRefs elemRefs = findAnnotation(XmlElementRefs.class, a, false, false, false);
        if (elemRefs != null) {
            ArrayList<NamedType> result2 = new ArrayList<>();
            XmlElementRef[] arr$2 = elemRefs.value();
            for (XmlElementRef elemRef : arr$2) {
                Class<?> refType = elemRef.type();
                if (!JAXBElement.class.isAssignableFrom(refType)) {
                    String name2 = elemRef.name();
                    if ((name2 == null || MARKER_FOR_DEFAULT.equals(name2)) && (rootElement = refType.getAnnotation(XmlRootElement.class)) != null) {
                        name2 = rootElement.name();
                    }
                    if (name2 == null || MARKER_FOR_DEFAULT.equals(name2)) {
                        name2 = Introspector.decapitalize(refType.getSimpleName());
                    }
                    result2.add(new NamedType(refType, name2));
                }
            }
            return result2;
        }
        return null;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public String findTypeName(AnnotatedClass ac) {
        XmlType type = findAnnotation(XmlType.class, ac, false, false, false);
        if (type != null) {
            String name = type.name();
            if (!MARKER_FOR_DEFAULT.equals(name)) {
                return name;
            }
        }
        return null;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public boolean isIgnorableMethod(AnnotatedMethod m) {
        return m.getAnnotation(XmlTransient.class) != null;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public boolean isIgnorableConstructor(AnnotatedConstructor c) {
        return false;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public boolean isIgnorableField(AnnotatedField f) {
        return f.getAnnotation(XmlTransient.class) != null;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public JsonSerializer<?> findSerializer(Annotated am, BeanProperty property) {
        XmlAdapter<Object, Object> adapter = findAdapter(am, true);
        if (adapter != null) {
            return new XmlAdapterJsonSerializer(adapter, property);
        }
        Class<?> type = am.getRawType();
        if (type != null && this._dataHandlerSerializer != null && isDataHandler(type)) {
            return this._dataHandlerSerializer;
        }
        return null;
    }

    private boolean isDataHandler(Class<?> type) {
        return (type == null || Object.class == type || (!"javax.activation.DataHandler".equals(type.getName()) && !isDataHandler(type.getSuperclass()))) ? false : true;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public Class<?> findSerializationType(Annotated a) {
        XmlElement annotation = findAnnotation(XmlElement.class, a, false, false, false);
        if (annotation == null || annotation.type() == XmlElement.DEFAULT.class) {
            return null;
        }
        Class<?> rawPropType = a.getRawType();
        if (isIndexedType(rawPropType)) {
            return null;
        }
        return annotation.type();
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public JsonSerialize.Inclusion findSerializationInclusion(Annotated a, JsonSerialize.Inclusion defValue) {
        XmlElementWrapper w = a.getAnnotation(XmlElementWrapper.class);
        if (w != null) {
            return w.nillable() ? JsonSerialize.Inclusion.ALWAYS : JsonSerialize.Inclusion.NON_NULL;
        }
        XmlElement e = a.getAnnotation(XmlElement.class);
        if (e != null) {
            return e.nillable() ? JsonSerialize.Inclusion.ALWAYS : JsonSerialize.Inclusion.NON_NULL;
        }
        return defValue;
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
        String[] order;
        XmlType type = findAnnotation(XmlType.class, ac, true, true, true);
        if (type == null || (order = type.propOrder()) == null || order.length == 0) {
            return null;
        }
        PropertyDescriptors props = getDescriptors(ac.getRawType());
        int len = order.length;
        for (int i = 0; i < len; i++) {
            String propName = order[i];
            if (props.findByPropertyName(propName) == null && propName.length() != 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("get");
                sb.append(Character.toUpperCase(propName.charAt(0)));
                if (propName.length() > 1) {
                    sb.append(propName.substring(1));
                }
                PropertyDescriptor desc = props.findByMethodName(sb.toString());
                if (desc != null) {
                    order[i] = desc.getName();
                }
            }
        }
        return order;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public Boolean findSerializationSortAlphabetically(AnnotatedClass ac) {
        XmlAccessorOrder order = findAnnotation(XmlAccessorOrder.class, ac, true, true, true);
        if (order == null) {
            return null;
        }
        return Boolean.valueOf(order.value() == XmlAccessOrder.ALPHABETICAL);
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public String findGettablePropertyName(AnnotatedMethod am) {
        PropertyDescriptor desc = findPropertyDescriptor(am);
        if (desc != null) {
            return findJaxbSpecifiedPropertyName(desc);
        }
        return null;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public boolean hasAsValueAnnotation(AnnotatedMethod am) {
        return false;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public String findEnumValue(Enum<?> e) {
        Class<E> declaringClass = e.getDeclaringClass();
        String enumValue = e.name();
        try {
            XmlEnumValue xmlEnumValue = declaringClass.getDeclaredField(enumValue).getAnnotation(XmlEnumValue.class);
            return xmlEnumValue != null ? xmlEnumValue.value() : enumValue;
        } catch (NoSuchFieldException e1) {
            throw new IllegalStateException("Could not locate Enum entry '" + enumValue + "' (Enum class " + declaringClass.getName() + ")", e1);
        }
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public String findSerializablePropertyName(AnnotatedField af) {
        if (isInvisible(af)) {
            return null;
        }
        Field field = af.getAnnotated();
        String name = findJaxbPropertyName(field, field.getType(), "");
        return name == null ? field.getName() : name;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public JsonDeserializer<?> findDeserializer(Annotated am, BeanProperty property) {
        XmlAdapter<Object, Object> adapter = findAdapter(am, false);
        if (adapter != null) {
            return new XmlAdapterJsonDeserializer(adapter, property);
        }
        Class<?> type = am.getRawType();
        if (type != null && this._dataHandlerDeserializer != null && isDataHandler(type)) {
            return this._dataHandlerDeserializer;
        }
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
    public Class<?> findDeserializationType(Annotated a, JavaType baseType, String propName) {
        if (baseType.isContainerType()) {
            return null;
        }
        return _doFindDeserializationType(a, baseType, propName);
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public Class<?> findDeserializationKeyType(Annotated am, JavaType baseKeyType, String propName) {
        return null;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public Class<?> findDeserializationContentType(Annotated a, JavaType baseContentType, String propName) {
        Class<?> type = _doFindDeserializationType(a, baseContentType, propName);
        return type;
    }

    protected Class<?> _doFindDeserializationType(Annotated a, JavaType baseType, String propName) {
        Class<?> type;
        if (a.hasAnnotation(XmlJavaTypeAdapter.class)) {
            return null;
        }
        XmlElement annotation = findAnnotation(XmlElement.class, a, false, false, false);
        if (annotation == null || (type = annotation.type()) == XmlElement.DEFAULT.class) {
            if ((a instanceof AnnotatedMethod) && propName != null) {
                AnnotatedMethod am = (AnnotatedMethod) a;
                XmlElement annotation2 = findFieldAnnotation(XmlElement.class, am.getDeclaringClass(), propName);
                if (annotation2 != null && annotation2.type() != XmlElement.DEFAULT.class) {
                    return annotation2.type();
                }
            }
            return null;
        }
        return type;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public String findSettablePropertyName(AnnotatedMethod am) {
        PropertyDescriptor desc = findPropertyDescriptor(am);
        if (desc != null) {
            return findJaxbSpecifiedPropertyName(desc);
        }
        return null;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public boolean hasAnySetterAnnotation(AnnotatedMethod am) {
        return false;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public boolean hasCreatorAnnotation(Annotated am) {
        return false;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public String findDeserializablePropertyName(AnnotatedField af) {
        if (isInvisible(af)) {
            return null;
        }
        Field field = af.getAnnotated();
        String name = findJaxbPropertyName(field, field.getType(), "");
        return name == null ? field.getName() : name;
    }

    @Override // org.codehaus.jackson.map.AnnotationIntrospector
    public String findPropertyNameForParam(AnnotatedParameter param) {
        return null;
    }

    protected boolean isInvisible(AnnotatedField f) {
        boolean invisible = true;
        Annotation[] arr$ = f.getAnnotated().getDeclaredAnnotations();
        for (Annotation annotation : arr$) {
            if (isHandled(annotation)) {
                invisible = false;
            }
        }
        if (invisible) {
            XmlAccessType accessType = XmlAccessType.PUBLIC_MEMBER;
            XmlAccessorType at = findAnnotation(XmlAccessorType.class, f, true, true, true);
            if (at != null) {
                accessType = at.value();
            }
            return (accessType == XmlAccessType.FIELD || (accessType == XmlAccessType.PUBLIC_MEMBER && Modifier.isPublic(f.getAnnotated().getModifiers()))) ? false : true;
        }
        return invisible;
    }

    protected <A extends Annotation> A findAnnotation(Class<A> cls, Annotated annotated, boolean z, boolean z2, boolean z3) {
        Class<?> declaringClass;
        A a;
        PropertyDescriptor propertyDescriptorFindPropertyDescriptor;
        A a2;
        AnonymousClass1 anonymousClass1 = null;
        if (!(annotated instanceof AnnotatedMethod) || (propertyDescriptorFindPropertyDescriptor = findPropertyDescriptor((AnnotatedMethod) annotated)) == null || (a2 = (A) new AnnotatedProperty(propertyDescriptorFindPropertyDescriptor, anonymousClass1).getAnnotation(cls)) == null) {
            AnnotatedElement annotated2 = annotated.getAnnotated();
            if (annotated instanceof AnnotatedParameter) {
                AnnotatedParameter annotatedParameter = (AnnotatedParameter) annotated;
                A a3 = (A) annotatedParameter.getAnnotation(cls);
                if (a3 == null) {
                    declaringClass = annotatedParameter.getMember().getDeclaringClass();
                } else {
                    return a3;
                }
            } else {
                A a4 = (A) annotated2.getAnnotation(cls);
                if (a4 == null) {
                    if (annotated2 instanceof Member) {
                        declaringClass = ((Member) annotated2).getDeclaringClass();
                        if (z2 && (a = (A) declaringClass.getAnnotation(cls)) != null) {
                            return a;
                        }
                    } else if (annotated2 instanceof Class) {
                        declaringClass = (Class) annotated2;
                    } else {
                        throw new IllegalStateException("Unsupported annotated member: " + annotated.getClass().getName());
                    }
                } else {
                    return a4;
                }
            }
            if (declaringClass != null) {
                if (z3) {
                    for (Class<? super Object> superclass = declaringClass.getSuperclass(); superclass != null && superclass != Object.class; superclass = superclass.getSuperclass()) {
                        A a5 = (A) superclass.getAnnotation(cls);
                        if (a5 != null) {
                            return a5;
                        }
                    }
                }
                if (z) {
                    return (A) declaringClass.getPackage().getAnnotation(cls);
                }
            }
            return null;
        }
        return a2;
    }

    protected <A extends Annotation> A findFieldAnnotation(Class<A> cls, Class<?> cls2, String str) {
        do {
            for (Field field : cls2.getDeclaredFields()) {
                if (str.equals(field.getName())) {
                    return (A) field.getAnnotation(cls);
                }
            }
            if (cls2.isInterface() || cls2 == Object.class) {
                break;
            }
            cls2 = cls2.getSuperclass();
        } while (cls2 != null);
        return null;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.beans.IntrospectionException */
    protected PropertyDescriptors getDescriptors(Class<?> forClass) {
        SoftReference<PropertyDescriptors> ref = _propertyDescriptors.get();
        PropertyDescriptors descriptors = ref == null ? null : ref.get();
        if (descriptors == null || descriptors.getBeanClass() != forClass) {
            try {
                descriptors = PropertyDescriptors.find(forClass);
                _propertyDescriptors.set(new SoftReference<>(descriptors));
            } catch (IntrospectionException e) {
                throw new IllegalArgumentException("Problem introspecting bean properties: " + e.getMessage(), e);
            }
        }
        return descriptors;
    }

    protected PropertyDescriptor findPropertyDescriptor(AnnotatedMethod m) {
        PropertyDescriptors descs = getDescriptors(m.getDeclaringClass());
        PropertyDescriptor desc = descs.findByMethodName(m.getName());
        return desc;
    }

    protected String findJaxbSpecifiedPropertyName(PropertyDescriptor prop) {
        return findJaxbPropertyName(new AnnotatedProperty(prop, null), prop.getPropertyType(), prop.getName());
    }

    protected static String findJaxbPropertyName(AnnotatedElement ae, Class<?> aeType, String defaultName) {
        XmlRootElement rootElement;
        XmlElementWrapper elementWrapper = ae.getAnnotation(XmlElementWrapper.class);
        if (elementWrapper != null) {
            String name = elementWrapper.name();
            return !MARKER_FOR_DEFAULT.equals(name) ? name : defaultName;
        }
        XmlAttribute attribute = ae.getAnnotation(XmlAttribute.class);
        if (attribute != null) {
            String name2 = attribute.name();
            return MARKER_FOR_DEFAULT.equals(name2) ? defaultName : name2;
        }
        XmlElement element = ae.getAnnotation(XmlElement.class);
        if (element != null) {
            String name3 = element.name();
            return MARKER_FOR_DEFAULT.equals(name3) ? defaultName : name3;
        }
        XmlElementRef elementRef = ae.getAnnotation(XmlElementRef.class);
        if (elementRef != null) {
            String name4 = elementRef.name();
            if (MARKER_FOR_DEFAULT.equals(name4)) {
                if (aeType != null && (rootElement = aeType.getAnnotation(XmlRootElement.class)) != null) {
                    String name5 = rootElement.name();
                    if (MARKER_FOR_DEFAULT.equals(name5)) {
                        return Introspector.decapitalize(aeType.getSimpleName());
                    }
                    return name5;
                }
            } else {
                return name4;
            }
        }
        XmlValue valueInfo = ae.getAnnotation(XmlValue.class);
        if (valueInfo != null) {
            return "value";
        }
        return null;
    }

    private XmlRootElement findRootElementAnnotation(AnnotatedClass ac) {
        return findAnnotation(XmlRootElement.class, ac, true, false, true);
    }

    protected XmlAdapter<Object, Object> findAdapter(Annotated am, boolean forSerialization) {
        XmlAdapter<Object, Object> adapter;
        Class<?> potentialAdaptee;
        XmlJavaTypeAdapter adapterInfo;
        XmlAdapter<Object, Object> adapter2;
        if (am instanceof AnnotatedClass) {
            return findAdapterForClass((AnnotatedClass) am, forSerialization);
        }
        Class<?> memberType = am.getRawType();
        if (memberType == Void.TYPE && (am instanceof AnnotatedMethod)) {
            memberType = ((AnnotatedMethod) am).getParameterClass(0);
        }
        Member member = (Member) am.getAnnotated();
        if (member == null || (potentialAdaptee = member.getDeclaringClass()) == null || (adapterInfo = (XmlJavaTypeAdapter) potentialAdaptee.getAnnotation(XmlJavaTypeAdapter.class)) == null || (adapter2 = checkAdapter(adapterInfo, memberType)) == null) {
            XmlJavaTypeAdapter adapterInfo2 = (XmlJavaTypeAdapter) findAnnotation(XmlJavaTypeAdapter.class, am, true, false, false);
            if (adapterInfo2 == null || (adapter = checkAdapter(adapterInfo2, memberType)) == null) {
                XmlJavaTypeAdapters adapters = findAnnotation(XmlJavaTypeAdapters.class, am, true, false, false);
                if (adapters != null) {
                    XmlJavaTypeAdapter[] arr$ = adapters.value();
                    for (XmlJavaTypeAdapter info : arr$) {
                        XmlAdapter<Object, Object> adapter3 = checkAdapter(info, memberType);
                        if (adapter3 != null) {
                            return adapter3;
                        }
                    }
                }
                return null;
            }
            return adapter;
        }
        return adapter2;
    }

    private final XmlAdapter<Object, Object> checkAdapter(XmlJavaTypeAdapter adapterInfo, Class<?> typeNeeded) {
        Class<?> adaptedType = adapterInfo.type();
        if (adaptedType != XmlJavaTypeAdapter.DEFAULT.class && !adaptedType.isAssignableFrom(typeNeeded)) {
            return null;
        }
        Class<? extends XmlAdapter> cls = adapterInfo.value();
        return (XmlAdapter) ClassUtil.createInstance(cls, false);
    }

    protected XmlAdapter<Object, Object> findAdapterForClass(AnnotatedClass ac, boolean forSerialization) {
        XmlJavaTypeAdapter adapterInfo = ac.getAnnotated().getAnnotation(XmlJavaTypeAdapter.class);
        if (adapterInfo == null) {
            return null;
        }
        Class<? extends XmlAdapter> cls = adapterInfo.value();
        return (XmlAdapter) ClassUtil.createInstance(cls, false);
    }

    protected boolean isIndexedType(Class<?> raw) {
        return raw.isArray() || Collection.class.isAssignableFrom(raw) || Map.class.isAssignableFrom(raw);
    }

    private static class AnnotatedProperty implements AnnotatedElement {
        private final PropertyDescriptor pd;

        /* synthetic */ AnnotatedProperty(PropertyDescriptor x0, AnonymousClass1 x1) {
            this(x0);
        }

        private AnnotatedProperty(PropertyDescriptor pd) {
            this.pd = pd;
        }

        @Override // java.lang.reflect.AnnotatedElement
        public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
            Method m = this.pd.getReadMethod();
            if (m != null && m.isAnnotationPresent(annotationClass)) {
                return true;
            }
            Method m2 = this.pd.getWriteMethod();
            return m2 != null && m2.isAnnotationPresent(annotationClass);
        }

        @Override // java.lang.reflect.AnnotatedElement
        public <T extends Annotation> T getAnnotation(Class<T> cls) {
            T t;
            Method readMethod = this.pd.getReadMethod();
            if (readMethod == null || (t = (T) readMethod.getAnnotation(cls)) == null) {
                Method writeMethod = this.pd.getWriteMethod();
                if (writeMethod != null) {
                    return (T) writeMethod.getAnnotation(cls);
                }
                return null;
            }
            return t;
        }

        @Override // java.lang.reflect.AnnotatedElement
        public Annotation[] getAnnotations() {
            throw new UnsupportedOperationException();
        }

        @Override // java.lang.reflect.AnnotatedElement
        public Annotation[] getDeclaredAnnotations() {
            throw new UnsupportedOperationException();
        }
    }

    protected static final class PropertyDescriptors {
        private Map<String, PropertyDescriptor> _byMethodName;
        private Map<String, PropertyDescriptor> _byPropertyName;
        private final Class<?> _forClass;
        private final List<PropertyDescriptor> _properties;

        public PropertyDescriptors(Class<?> forClass, List<PropertyDescriptor> properties) {
            this._forClass = forClass;
            this._properties = properties;
        }

        public Class<?> getBeanClass() {
            return this._forClass;
        }

        public PropertyDescriptor findByPropertyName(String name) {
            if (this._byPropertyName == null) {
                this._byPropertyName = new HashMap(this._properties.size());
                for (PropertyDescriptor desc : this._properties) {
                    this._byPropertyName.put(desc.getName(), desc);
                }
            }
            return this._byPropertyName.get(name);
        }

        public PropertyDescriptor findByMethodName(String name) {
            if (this._byMethodName == null) {
                this._byMethodName = new HashMap(this._properties.size());
                for (PropertyDescriptor desc : this._properties) {
                    Method getter = desc.getReadMethod();
                    if (getter != null) {
                        this._byMethodName.put(getter.getName(), desc);
                    }
                    Method setter = desc.getWriteMethod();
                    if (setter != null) {
                        this._byMethodName.put(setter.getName(), desc);
                    }
                }
            }
            return this._byMethodName.get(name);
        }

        /* JADX INFO: Thrown type has an unknown type hierarchy: java.beans.IntrospectionException */
        public static PropertyDescriptors find(Class<?> forClass) throws IntrospectionException {
            List<PropertyDescriptor> descriptors;
            String name;
            BeanInfo beanInfo = Introspector.getBeanInfo(forClass);
            PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
            if (pds.length == 0) {
                descriptors = Collections.emptyList();
            } else {
                descriptors = new ArrayList<>();
                Map<String, PropertyDescriptor> partials = null;
                PropertyDescriptor[] arr$ = beanInfo.getPropertyDescriptors();
                for (PropertyDescriptor pd : arr$) {
                    Method read = pd.getReadMethod();
                    if (read != null && read.getAnnotation(XmlTransient.class) != null) {
                        read = null;
                    }
                    String readName = read == null ? null : JaxbAnnotationIntrospector.findJaxbPropertyName(read, pd.getPropertyType(), null);
                    Method write = pd.getWriteMethod();
                    if (write != null && write.getAnnotation(XmlTransient.class) != null) {
                        write = null;
                    }
                    if (read != null || write != null) {
                        String writeName = write == null ? null : JaxbAnnotationIntrospector.findJaxbPropertyName(write, pd.getPropertyType(), null);
                        if (write == null) {
                            if (readName == null) {
                                readName = pd.getName();
                            }
                            partials = _processReadMethod(partials, read, readName, descriptors);
                        } else if (read == null) {
                            if (writeName == null) {
                                writeName = pd.getName();
                            }
                            partials = _processWriteMethod(partials, write, writeName, descriptors);
                        } else if (readName != null && writeName != null && !readName.equals(writeName)) {
                            partials = _processWriteMethod(_processReadMethod(partials, read, readName, descriptors), write, writeName, descriptors);
                        } else {
                            if (readName != null) {
                                name = readName;
                            } else if (writeName != null) {
                                name = writeName;
                            } else {
                                name = pd.getName();
                            }
                            descriptors.add(new PropertyDescriptor(name, read, write));
                        }
                    }
                }
            }
            return new PropertyDescriptors(forClass, descriptors);
        }

        private static Map<String, PropertyDescriptor> _processReadMethod(Map<String, PropertyDescriptor> partials, Method method, String propertyName, List<PropertyDescriptor> pds) throws IntrospectionException {
            if (partials == null) {
                partials = new HashMap<>();
            } else {
                PropertyDescriptor pd = partials.get(propertyName);
                if (pd != null) {
                    pd.setReadMethod(method);
                    if (pd.getWriteMethod() != null) {
                        pds.add(pd);
                        partials.remove(propertyName);
                    }
                }
                return partials;
            }
            partials.put(propertyName, new PropertyDescriptor(propertyName, method, (Method) null));
            return partials;
        }

        private static Map<String, PropertyDescriptor> _processWriteMethod(Map<String, PropertyDescriptor> partials, Method method, String propertyName, List<PropertyDescriptor> pds) throws IntrospectionException {
            if (partials == null) {
                partials = new HashMap<>();
            } else {
                PropertyDescriptor pd = partials.get(propertyName);
                if (pd != null) {
                    pd.setWriteMethod(method);
                    if (pd.getReadMethod() != null) {
                        pds.add(pd);
                        partials.remove(propertyName);
                    }
                }
                return partials;
            }
            partials.put(propertyName, new PropertyDescriptor(propertyName, (Method) null, method));
            return partials;
        }
    }
}
