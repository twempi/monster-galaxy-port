package org.codehaus.jackson.map.ser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.SerializerFactory;
import org.codehaus.jackson.map.Serializers;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.introspect.AnnotatedClass;
import org.codehaus.jackson.map.introspect.AnnotatedField;
import org.codehaus.jackson.map.introspect.AnnotatedMember;
import org.codehaus.jackson.map.introspect.AnnotatedMethod;
import org.codehaus.jackson.map.introspect.BasicBeanDescription;
import org.codehaus.jackson.map.introspect.VisibilityChecker;
import org.codehaus.jackson.map.jsontype.NamedType;
import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
import org.codehaus.jackson.map.type.TypeBindings;
import org.codehaus.jackson.map.util.ArrayBuilders;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.type.JavaType;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class BeanSerializerFactory extends BasicSerializerFactory {
    public static final BeanSerializerFactory instance = new BeanSerializerFactory(null);
    protected final SerializerFactory.Config _factoryConfig;

    public static class ConfigImpl extends SerializerFactory.Config {
        protected final Serializers[] _additionalKeySerializers;
        protected final Serializers[] _additionalSerializers;
        protected final BeanSerializerModifier[] _modifiers;
        protected static final Serializers[] NO_SERIALIZERS = new Serializers[0];
        protected static final BeanSerializerModifier[] NO_MODIFIERS = new BeanSerializerModifier[0];

        public ConfigImpl() {
            this(null, null, null);
        }

        protected ConfigImpl(Serializers[] allAdditionalSerializers, Serializers[] allAdditionalKeySerializers, BeanSerializerModifier[] modifiers) {
            this._additionalSerializers = allAdditionalSerializers == null ? NO_SERIALIZERS : allAdditionalSerializers;
            this._additionalKeySerializers = allAdditionalKeySerializers == null ? NO_SERIALIZERS : allAdditionalKeySerializers;
            this._modifiers = modifiers == null ? NO_MODIFIERS : modifiers;
        }

        @Override // org.codehaus.jackson.map.SerializerFactory.Config
        public SerializerFactory.Config withAdditionalSerializers(Serializers additional) {
            if (additional == null) {
                throw new IllegalArgumentException("Can not pass null Serializers");
            }
            Serializers[] all = (Serializers[]) ArrayBuilders.insertInListNoDup(this._additionalSerializers, additional);
            return new ConfigImpl(all, this._additionalKeySerializers, this._modifiers);
        }

        @Override // org.codehaus.jackson.map.SerializerFactory.Config
        public SerializerFactory.Config withAdditionalKeySerializers(Serializers additional) {
            if (additional == null) {
                throw new IllegalArgumentException("Can not pass null Serializers");
            }
            Serializers[] all = (Serializers[]) ArrayBuilders.insertInListNoDup(this._additionalKeySerializers, additional);
            return new ConfigImpl(this._additionalSerializers, all, this._modifiers);
        }

        @Override // org.codehaus.jackson.map.SerializerFactory.Config
        public SerializerFactory.Config withSerializerModifier(BeanSerializerModifier modifier) {
            if (modifier == null) {
                throw new IllegalArgumentException("Can not pass null modifier");
            }
            BeanSerializerModifier[] modifiers = (BeanSerializerModifier[]) ArrayBuilders.insertInListNoDup(this._modifiers, modifier);
            return new ConfigImpl(this._additionalSerializers, this._additionalKeySerializers, modifiers);
        }

        @Override // org.codehaus.jackson.map.SerializerFactory.Config
        public boolean hasSerializers() {
            return this._additionalSerializers.length > 0;
        }

        @Override // org.codehaus.jackson.map.SerializerFactory.Config
        public boolean hasKeySerializers() {
            return this._additionalKeySerializers.length > 0;
        }

        @Override // org.codehaus.jackson.map.SerializerFactory.Config
        public boolean hasSerializerModifiers() {
            return this._modifiers.length > 0;
        }

        @Override // org.codehaus.jackson.map.SerializerFactory.Config
        public Iterable<Serializers> serializers() {
            return ArrayBuilders.arrayAsIterable(this._additionalSerializers);
        }

        @Override // org.codehaus.jackson.map.SerializerFactory.Config
        public Iterable<Serializers> keySerializers() {
            return ArrayBuilders.arrayAsIterable(this._additionalKeySerializers);
        }

        @Override // org.codehaus.jackson.map.SerializerFactory.Config
        public Iterable<BeanSerializerModifier> serializerModifiers() {
            return ArrayBuilders.arrayAsIterable(this._modifiers);
        }
    }

    @Deprecated
    protected BeanSerializerFactory() {
        this(null);
    }

    protected BeanSerializerFactory(SerializerFactory.Config config) {
        this._factoryConfig = config == null ? new ConfigImpl() : config;
    }

    @Override // org.codehaus.jackson.map.SerializerFactory
    public SerializerFactory.Config getConfig() {
        return this._factoryConfig;
    }

    @Override // org.codehaus.jackson.map.SerializerFactory
    public SerializerFactory withConfig(SerializerFactory.Config config) {
        if (this._factoryConfig != config) {
            if (getClass() != BeanSerializerFactory.class) {
                throw new IllegalStateException("Subtype of BeanSerializerFactory (" + getClass().getName() + ") has not properly overridden method 'withAdditionalSerializers': can not instantiate subtype with additional serializer definitions");
            }
            return new BeanSerializerFactory(config);
        }
        return this;
    }

    @Override // org.codehaus.jackson.map.ser.BasicSerializerFactory
    protected Iterable<Serializers> customSerializers() {
        return this._factoryConfig.serializers();
    }

    @Override // org.codehaus.jackson.map.ser.BasicSerializerFactory, org.codehaus.jackson.map.SerializerFactory
    public JsonSerializer<Object> createSerializer(SerializationConfig config, JavaType origType, BeanProperty property) throws JsonMappingException {
        BasicBeanDescription beanDesc = (BasicBeanDescription) config.introspect(origType);
        JsonSerializer<?> ser = findSerializerFromAnnotation(config, beanDesc.getClassInfo(), property);
        if (ser != null) {
            return ser;
        }
        JavaType type = modifyTypeByAnnotation(config, beanDesc.getClassInfo(), origType);
        boolean staticTyping = type != origType;
        if (origType.isContainerType()) {
            return buildContainerSerializer(config, type, beanDesc, property, staticTyping);
        }
        for (Serializers serializers : this._factoryConfig.serializers()) {
            JsonSerializer<?> ser2 = serializers.findSerializer(config, type, beanDesc, property);
            if (ser2 != null) {
                return ser2;
            }
        }
        JsonSerializer<?> ser3 = findSerializerByLookup(type, config, beanDesc, property, staticTyping);
        if (ser3 != null) {
            return ser3;
        }
        JsonSerializer<?> ser4 = findSerializerByPrimaryType(type, config, beanDesc, property, staticTyping);
        if (ser4 != null) {
            return ser4;
        }
        JsonSerializer<?> ser5 = findBeanSerializer(config, type, beanDesc, property);
        if (ser5 == null) {
            ser5 = super.findSerializerByAddonType(config, type, beanDesc, property, staticTyping);
        }
        return ser5;
    }

    @Override // org.codehaus.jackson.map.SerializerFactory
    public JsonSerializer<Object> createKeySerializer(SerializationConfig config, JavaType type, BeanProperty property) {
        if (!this._factoryConfig.hasKeySerializers()) {
            return null;
        }
        BasicBeanDescription beanDesc = (BasicBeanDescription) config.introspectClassAnnotations(type.getRawClass());
        JsonSerializer<?> ser = null;
        for (Serializers serializers : this._factoryConfig.keySerializers()) {
            ser = serializers.findSerializer(config, type, beanDesc, property);
            if (ser != null) {
                return ser;
            }
        }
        return ser;
    }

    public JsonSerializer<Object> findBeanSerializer(SerializationConfig config, JavaType type, BasicBeanDescription beanDesc, BeanProperty property) throws JsonMappingException {
        if (!isPotentialBeanType(type.getRawClass())) {
            return null;
        }
        JsonSerializer<?> jsonSerializerConstructBeanSerializer = constructBeanSerializer(config, beanDesc, property);
        if (this._factoryConfig.hasSerializerModifiers()) {
            for (BeanSerializerModifier mod : this._factoryConfig.serializerModifiers()) {
                jsonSerializerConstructBeanSerializer = mod.modifySerializer(config, beanDesc, jsonSerializerConstructBeanSerializer);
            }
            return jsonSerializerConstructBeanSerializer;
        }
        return jsonSerializerConstructBeanSerializer;
    }

    public TypeSerializer findPropertyTypeSerializer(JavaType baseType, SerializationConfig config, AnnotatedMember accessor, BeanProperty property) throws JsonMappingException {
        AnnotationIntrospector ai = config.getAnnotationIntrospector();
        TypeResolverBuilder<?> b = ai.findPropertyTypeResolver(config, accessor, baseType);
        if (b == null) {
            return createTypeSerializer(config, baseType, property);
        }
        Collection<NamedType> subtypes = config.getSubtypeResolver().collectAndResolveSubtypes(accessor, config, ai);
        return b.buildTypeSerializer(config, baseType, subtypes, property);
    }

    public TypeSerializer findPropertyContentTypeSerializer(JavaType containerType, SerializationConfig config, AnnotatedMember accessor, BeanProperty property) throws JsonMappingException {
        JavaType contentType = containerType.getContentType();
        AnnotationIntrospector ai = config.getAnnotationIntrospector();
        TypeResolverBuilder<?> b = ai.findPropertyContentTypeResolver(config, accessor, containerType);
        if (b == null) {
            return createTypeSerializer(config, contentType, property);
        }
        Collection<NamedType> subtypes = config.getSubtypeResolver().collectAndResolveSubtypes(accessor, config, ai);
        return b.buildTypeSerializer(config, contentType, subtypes, property);
    }

    protected JsonSerializer<Object> constructBeanSerializer(SerializationConfig config, BasicBeanDescription beanDesc, BeanProperty property) throws JsonMappingException {
        List<BeanPropertyWriter> props;
        if (beanDesc.getBeanClass() == Object.class) {
            throw new IllegalArgumentException("Can not create bean serializer for Object.class");
        }
        BeanSerializerBuilder builder = constructBeanSerializerBuilder(beanDesc);
        List<BeanPropertyWriter> props2 = findBeanProperties(config, beanDesc);
        AnnotatedMethod anyGetter = beanDesc.findAnyGetter();
        if (this._factoryConfig.hasSerializerModifiers()) {
            if (props2 == null) {
                props2 = new ArrayList<>();
            }
            for (BeanSerializerModifier mod : this._factoryConfig.serializerModifiers()) {
                props2 = mod.changeProperties(config, beanDesc, props2);
            }
        }
        if (props2 == null || props2.size() == 0) {
            if (anyGetter == null) {
                if (beanDesc.hasKnownClassAnnotations()) {
                    return builder.createDummy();
                }
                return null;
            }
            props = Collections.emptyList();
        } else {
            props = sortBeanProperties(config, beanDesc, filterBeanProperties(config, beanDesc, props2));
        }
        if (this._factoryConfig.hasSerializerModifiers()) {
            for (BeanSerializerModifier mod2 : this._factoryConfig.serializerModifiers()) {
                props = mod2.orderProperties(config, beanDesc, props);
            }
        }
        builder.setProperties(props);
        builder.setFilterId(findFilterId(config, beanDesc));
        if (anyGetter != null) {
            JavaType type = anyGetter.getType(beanDesc.bindingsForBeanType());
            boolean staticTyping = config.isEnabled(SerializationConfig.Feature.USE_STATIC_TYPING);
            JavaType valueType = type.getContentType();
            TypeSerializer typeSer = createTypeSerializer(config, valueType, property);
            MapSerializer mapSer = MapSerializer.construct(null, type, staticTyping, typeSer, property, null, null);
            builder.setAnyGetter(new AnyGetterWriter(anyGetter, mapSer));
        }
        processViews(config, builder);
        if (this._factoryConfig.hasSerializerModifiers()) {
            for (BeanSerializerModifier mod3 : this._factoryConfig.serializerModifiers()) {
                builder = mod3.updateBuilder(config, beanDesc, builder);
            }
        }
        return builder.build();
    }

    protected BeanPropertyWriter constructFilteredBeanWriter(BeanPropertyWriter writer, Class<?>[] inViews) {
        return FilteredBeanPropertyWriter.constructViewBased(writer, inViews);
    }

    protected PropertyBuilder constructPropertyBuilder(SerializationConfig config, BasicBeanDescription beanDesc) {
        return new PropertyBuilder(config, beanDesc);
    }

    protected BeanSerializerBuilder constructBeanSerializerBuilder(BasicBeanDescription beanDesc) {
        return new BeanSerializerBuilder(beanDesc);
    }

    protected Object findFilterId(SerializationConfig config, BasicBeanDescription beanDesc) {
        return config.getAnnotationIntrospector().findFilterId(beanDesc.getClassInfo());
    }

    protected boolean isPotentialBeanType(Class<?> type) {
        return ClassUtil.canBeABeanType(type) == null && !ClassUtil.isProxyType(type);
    }

    protected List<BeanPropertyWriter> findBeanProperties(SerializationConfig config, BasicBeanDescription beanDesc) throws JsonMappingException {
        VisibilityChecker<?> vchecker = config.getDefaultVisibilityChecker();
        if (!config.isEnabled(SerializationConfig.Feature.AUTO_DETECT_GETTERS)) {
            vchecker = vchecker.withGetterVisibility(JsonAutoDetect.Visibility.NONE);
        }
        if (!config.isEnabled(SerializationConfig.Feature.AUTO_DETECT_IS_GETTERS)) {
            vchecker = vchecker.withIsGetterVisibility(JsonAutoDetect.Visibility.NONE);
        }
        if (!config.isEnabled(SerializationConfig.Feature.AUTO_DETECT_FIELDS)) {
            vchecker = vchecker.withFieldVisibility(JsonAutoDetect.Visibility.NONE);
        }
        AnnotationIntrospector intr = config.getAnnotationIntrospector();
        VisibilityChecker<?> vchecker2 = intr.findAutoDetectVisibility(beanDesc.getClassInfo(), vchecker);
        LinkedHashMap<String, AnnotatedMethod> methodsByProp = beanDesc.findGetters(vchecker2, null);
        LinkedHashMap<String, AnnotatedField> fieldsByProp = beanDesc.findSerializableFields(vchecker2, methodsByProp.keySet());
        removeIgnorableTypes(config, beanDesc, methodsByProp);
        removeIgnorableTypes(config, beanDesc, fieldsByProp);
        if (methodsByProp.isEmpty() && fieldsByProp.isEmpty()) {
            return null;
        }
        boolean staticTyping = usesStaticTyping(config, beanDesc, null, null);
        PropertyBuilder pb = constructPropertyBuilder(config, beanDesc);
        ArrayList<BeanPropertyWriter> props = new ArrayList<>(methodsByProp.size());
        TypeBindings typeBind = beanDesc.bindingsForBeanType();
        for (Map.Entry<String, AnnotatedField> en : fieldsByProp.entrySet()) {
            AnnotationIntrospector.ReferenceProperty prop = intr.findReferenceType(en.getValue());
            if (prop == null || !prop.isBackReference()) {
                props.add(_constructWriter(config, typeBind, pb, staticTyping, en.getKey(), en.getValue()));
            }
        }
        for (Map.Entry<String, AnnotatedMethod> en2 : methodsByProp.entrySet()) {
            AnnotationIntrospector.ReferenceProperty prop2 = intr.findReferenceType(en2.getValue());
            if (prop2 == null || !prop2.isBackReference()) {
                props.add(_constructWriter(config, typeBind, pb, staticTyping, en2.getKey(), en2.getValue()));
            }
        }
        return props;
    }

    protected List<BeanPropertyWriter> filterBeanProperties(SerializationConfig config, BasicBeanDescription beanDesc, List<BeanPropertyWriter> props) {
        AnnotationIntrospector intr = config.getAnnotationIntrospector();
        AnnotatedClass ac = beanDesc.getClassInfo();
        String[] ignored = intr.findPropertiesToIgnore(ac);
        if (ignored != null && ignored.length > 0) {
            HashSet<String> ignoredSet = ArrayBuilders.arrayToSet(ignored);
            Iterator<BeanPropertyWriter> it = props.iterator();
            while (it.hasNext()) {
                if (ignoredSet.contains(it.next().getName())) {
                    it.remove();
                }
            }
        }
        return props;
    }

    protected List<BeanPropertyWriter> sortBeanProperties(SerializationConfig config, BasicBeanDescription beanDesc, List<BeanPropertyWriter> props) {
        boolean sort;
        List<String> creatorProps = beanDesc.findCreatorPropertyNames();
        AnnotationIntrospector intr = config.getAnnotationIntrospector();
        AnnotatedClass ac = beanDesc.getClassInfo();
        String[] propOrder = intr.findSerializationPropertyOrder(ac);
        Boolean alpha = intr.findSerializationSortAlphabetically(ac);
        if (alpha == null) {
            sort = config.isEnabled(SerializationConfig.Feature.SORT_PROPERTIES_ALPHABETICALLY);
        } else {
            sort = alpha.booleanValue();
        }
        if (sort || !creatorProps.isEmpty() || propOrder != null) {
            return _sortBeanProperties(props, creatorProps, propOrder, sort);
        }
        return props;
    }

    protected void processViews(SerializationConfig config, BeanSerializerBuilder builder) {
        List<BeanPropertyWriter> props = builder.getProperties();
        boolean includeByDefault = config.isEnabled(SerializationConfig.Feature.DEFAULT_VIEW_INCLUSION);
        int propCount = props.size();
        int viewsFound = 0;
        BeanPropertyWriter[] filtered = new BeanPropertyWriter[propCount];
        for (int i = 0; i < propCount; i++) {
            BeanPropertyWriter bpw = props.get(i);
            Class<?>[] views = bpw.getViews();
            if (views == null) {
                if (includeByDefault) {
                    filtered[i] = bpw;
                }
            } else {
                viewsFound++;
                filtered[i] = constructFilteredBeanWriter(bpw, views);
            }
        }
        if (!includeByDefault || viewsFound != 0) {
            builder.setFilteredProperties(filtered);
        }
    }

    protected <T extends AnnotatedMember> void removeIgnorableTypes(SerializationConfig config, BasicBeanDescription beanDesc, Map<String, T> props) {
        if (!props.isEmpty()) {
            AnnotationIntrospector intr = config.getAnnotationIntrospector();
            Iterator<Map.Entry<String, T>> it = props.entrySet().iterator();
            HashMap<Class<?>, Boolean> ignores = new HashMap<>();
            while (it.hasNext()) {
                Map.Entry<String, T> entry = it.next();
                Class<?> type = entry.getValue().getRawType();
                Boolean result = ignores.get(type);
                if (result == null) {
                    BasicBeanDescription desc = (BasicBeanDescription) config.introspectClassAnnotations(type);
                    AnnotatedClass ac = desc.getClassInfo();
                    result = intr.isIgnorableType(ac);
                    if (result == null) {
                        result = Boolean.FALSE;
                    }
                    ignores.put(type, result);
                }
                if (result.booleanValue()) {
                    it.remove();
                }
            }
        }
    }

    protected BeanPropertyWriter _constructWriter(SerializationConfig config, TypeBindings typeContext, PropertyBuilder pb, boolean staticTyping, String name, AnnotatedMember accessor) throws JsonMappingException {
        if (config.isEnabled(SerializationConfig.Feature.CAN_OVERRIDE_ACCESS_MODIFIERS)) {
            accessor.fixAccess();
        }
        JavaType type = accessor.getType(typeContext);
        BeanProperty.Std property = new BeanProperty.Std(name, type, pb.getClassAnnotations(), accessor);
        JsonSerializer<Object> annotatedSerializer = findSerializerFromAnnotation(config, accessor, property);
        TypeSerializer contentTypeSer = null;
        if (ClassUtil.isCollectionMapOrArray(type.getRawClass())) {
            contentTypeSer = findPropertyContentTypeSerializer(type, config, accessor, property);
        }
        TypeSerializer typeSer = findPropertyTypeSerializer(type, config, accessor, property);
        BeanPropertyWriter pbw = pb.buildWriter(name, type, annotatedSerializer, typeSer, contentTypeSer, accessor, staticTyping);
        AnnotationIntrospector intr = config.getAnnotationIntrospector();
        pbw.setViews(intr.findSerializationViews(accessor));
        return pbw;
    }

    protected List<BeanPropertyWriter> _sortBeanProperties(List<BeanPropertyWriter> props, List<String> creatorProps, String[] propertyOrder, boolean sort) {
        Map<String, BeanPropertyWriter> all;
        int size = props.size();
        if (sort) {
            all = new TreeMap<>();
        } else {
            all = new LinkedHashMap<>(size * 2);
        }
        for (BeanPropertyWriter w : props) {
            all.put(w.getName(), w);
        }
        Map<String, BeanPropertyWriter> ordered = new LinkedHashMap<>(size * 2);
        if (propertyOrder != null) {
            for (String name : propertyOrder) {
                BeanPropertyWriter w2 = all.get(name);
                if (w2 != null) {
                    ordered.put(name, w2);
                }
            }
        }
        for (String name2 : creatorProps) {
            BeanPropertyWriter w3 = all.get(name2);
            if (w3 != null) {
                ordered.put(name2, w3);
            }
        }
        ordered.putAll(all);
        return new ArrayList(ordered.values());
    }
}
