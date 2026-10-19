package org.codehaus.jackson.map.deser;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.AbstractTypeResolver;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializerFactory;
import org.codehaus.jackson.map.DeserializerProvider;
import org.codehaus.jackson.map.Deserializers;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.KeyDeserializer;
import org.codehaus.jackson.map.KeyDeserializers;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.introspect.AnnotatedClass;
import org.codehaus.jackson.map.introspect.AnnotatedConstructor;
import org.codehaus.jackson.map.introspect.AnnotatedField;
import org.codehaus.jackson.map.introspect.AnnotatedMember;
import org.codehaus.jackson.map.introspect.AnnotatedMethod;
import org.codehaus.jackson.map.introspect.AnnotatedParameter;
import org.codehaus.jackson.map.introspect.BasicBeanDescription;
import org.codehaus.jackson.map.introspect.VisibilityChecker;
import org.codehaus.jackson.map.type.ArrayType;
import org.codehaus.jackson.map.type.CollectionLikeType;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.MapLikeType;
import org.codehaus.jackson.map.type.MapType;
import org.codehaus.jackson.map.util.ArrayBuilders;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.type.JavaType;

/* JADX INFO: loaded from: classes.dex */
public class BeanDeserializerFactory extends BasicDeserializerFactory {
    private static final Class<?>[] INIT_CAUSE_PARAMS = {Throwable.class};
    public static final BeanDeserializerFactory instance = new BeanDeserializerFactory(null);
    protected final DeserializerFactory.Config _factoryConfig;

    public static class ConfigImpl extends DeserializerFactory.Config {
        protected final AbstractTypeResolver[] _abstractTypeResolvers;
        protected final Deserializers[] _additionalDeserializers;
        protected final KeyDeserializers[] _additionalKeyDeserializers;
        protected final BeanDeserializerModifier[] _modifiers;
        protected static final KeyDeserializers[] NO_KEY_DESERIALIZERS = new KeyDeserializers[0];
        protected static final BeanDeserializerModifier[] NO_MODIFIERS = new BeanDeserializerModifier[0];
        protected static final AbstractTypeResolver[] NO_ABSTRACT_TYPE_RESOLVERS = new AbstractTypeResolver[0];

        public ConfigImpl() {
            this(null, null, null, null);
        }

        protected ConfigImpl(Deserializers[] allAdditionalDeserializers, KeyDeserializers[] allAdditionalKeyDeserializers, BeanDeserializerModifier[] modifiers, AbstractTypeResolver[] atr) {
            this._additionalDeserializers = allAdditionalDeserializers == null ? BeanDeserializerFactory.NO_DESERIALIZERS : allAdditionalDeserializers;
            this._additionalKeyDeserializers = allAdditionalKeyDeserializers == null ? NO_KEY_DESERIALIZERS : allAdditionalKeyDeserializers;
            this._modifiers = modifiers == null ? NO_MODIFIERS : modifiers;
            this._abstractTypeResolvers = atr == null ? NO_ABSTRACT_TYPE_RESOLVERS : atr;
        }

        @Override // org.codehaus.jackson.map.DeserializerFactory.Config
        public DeserializerFactory.Config withAdditionalDeserializers(Deserializers additional) {
            if (additional == null) {
                throw new IllegalArgumentException("Can not pass null Deserializers");
            }
            Deserializers[] all = (Deserializers[]) ArrayBuilders.insertInListNoDup(this._additionalDeserializers, additional);
            return new ConfigImpl(all, this._additionalKeyDeserializers, this._modifiers, this._abstractTypeResolvers);
        }

        @Override // org.codehaus.jackson.map.DeserializerFactory.Config
        public DeserializerFactory.Config withAdditionalKeyDeserializers(KeyDeserializers additional) {
            if (additional == null) {
                throw new IllegalArgumentException("Can not pass null KeyDeserializers");
            }
            KeyDeserializers[] all = (KeyDeserializers[]) ArrayBuilders.insertInListNoDup(this._additionalKeyDeserializers, additional);
            return new ConfigImpl(this._additionalDeserializers, all, this._modifiers, this._abstractTypeResolvers);
        }

        @Override // org.codehaus.jackson.map.DeserializerFactory.Config
        public DeserializerFactory.Config withDeserializerModifier(BeanDeserializerModifier modifier) {
            if (modifier == null) {
                throw new IllegalArgumentException("Can not pass null modifier");
            }
            BeanDeserializerModifier[] all = (BeanDeserializerModifier[]) ArrayBuilders.insertInListNoDup(this._modifiers, modifier);
            return new ConfigImpl(this._additionalDeserializers, this._additionalKeyDeserializers, all, this._abstractTypeResolvers);
        }

        @Override // org.codehaus.jackson.map.DeserializerFactory.Config
        public DeserializerFactory.Config withAbstractTypeResolver(AbstractTypeResolver resolver) {
            if (resolver == null) {
                throw new IllegalArgumentException("Can not pass null resolver");
            }
            AbstractTypeResolver[] all = (AbstractTypeResolver[]) ArrayBuilders.insertInListNoDup(this._abstractTypeResolvers, resolver);
            return new ConfigImpl(this._additionalDeserializers, this._additionalKeyDeserializers, this._modifiers, all);
        }

        @Override // org.codehaus.jackson.map.DeserializerFactory.Config
        public boolean hasDeserializers() {
            return this._additionalDeserializers.length > 0;
        }

        @Override // org.codehaus.jackson.map.DeserializerFactory.Config
        public boolean hasKeyDeserializers() {
            return this._additionalKeyDeserializers.length > 0;
        }

        @Override // org.codehaus.jackson.map.DeserializerFactory.Config
        public boolean hasDeserializerModifiers() {
            return this._modifiers.length > 0;
        }

        @Override // org.codehaus.jackson.map.DeserializerFactory.Config
        public boolean hasAbstractTypeResolvers() {
            return this._abstractTypeResolvers.length > 0;
        }

        @Override // org.codehaus.jackson.map.DeserializerFactory.Config
        public Iterable<Deserializers> deserializers() {
            return ArrayBuilders.arrayAsIterable(this._additionalDeserializers);
        }

        @Override // org.codehaus.jackson.map.DeserializerFactory.Config
        public Iterable<KeyDeserializers> keyDeserializers() {
            return ArrayBuilders.arrayAsIterable(this._additionalKeyDeserializers);
        }

        @Override // org.codehaus.jackson.map.DeserializerFactory.Config
        public Iterable<BeanDeserializerModifier> deserializerModifiers() {
            return ArrayBuilders.arrayAsIterable(this._modifiers);
        }

        @Override // org.codehaus.jackson.map.DeserializerFactory.Config
        public Iterable<AbstractTypeResolver> abstractTypeResolvers() {
            return ArrayBuilders.arrayAsIterable(this._abstractTypeResolvers);
        }
    }

    @Deprecated
    public BeanDeserializerFactory() {
        this(null);
    }

    public BeanDeserializerFactory(DeserializerFactory.Config config) {
        this._factoryConfig = config == null ? new ConfigImpl() : config;
    }

    @Override // org.codehaus.jackson.map.DeserializerFactory
    public final DeserializerFactory.Config getConfig() {
        return this._factoryConfig;
    }

    @Override // org.codehaus.jackson.map.deser.BasicDeserializerFactory, org.codehaus.jackson.map.DeserializerFactory
    public DeserializerFactory withConfig(DeserializerFactory.Config config) {
        if (this._factoryConfig != config) {
            if (getClass() != BeanDeserializerFactory.class) {
                throw new IllegalStateException("Subtype of BeanDeserializerFactory (" + getClass().getName() + ") has not properly overridden method 'withAdditionalDeserializers': can not instantiate subtype with additional deserializer definitions");
            }
            return new BeanDeserializerFactory(config);
        }
        return this;
    }

    @Override // org.codehaus.jackson.map.DeserializerFactory
    public KeyDeserializer createKeyDeserializer(DeserializationConfig config, JavaType type, BeanProperty property) throws JsonMappingException {
        if (this._factoryConfig.hasKeyDeserializers()) {
            BasicBeanDescription beanDesc = (BasicBeanDescription) config.introspectClassAnnotations(type.getRawClass());
            for (KeyDeserializers d : this._factoryConfig.keyDeserializers()) {
                KeyDeserializer deser = d.findKeyDeserializer(type, config, beanDesc, property);
                if (deser != null) {
                    return deser;
                }
            }
        }
        return null;
    }

    @Override // org.codehaus.jackson.map.deser.BasicDeserializerFactory
    protected JsonDeserializer<?> _findCustomArrayDeserializer(ArrayType type, DeserializationConfig config, DeserializerProvider provider, BeanProperty property, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer) throws JsonMappingException {
        for (Deserializers d : this._factoryConfig.deserializers()) {
            JsonDeserializer<?> deser = d.findArrayDeserializer(type, config, provider, property, elementTypeDeserializer, elementDeserializer);
            if (deser != null) {
                return deser;
            }
        }
        return null;
    }

    @Override // org.codehaus.jackson.map.deser.BasicDeserializerFactory
    protected JsonDeserializer<?> _findCustomCollectionDeserializer(CollectionType type, DeserializationConfig config, DeserializerProvider provider, BasicBeanDescription beanDesc, BeanProperty property, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer) throws JsonMappingException {
        for (Deserializers d : this._factoryConfig.deserializers()) {
            JsonDeserializer<?> deser = d.findCollectionDeserializer(type, config, provider, beanDesc, property, elementTypeDeserializer, elementDeserializer);
            if (deser != null) {
                return deser;
            }
        }
        return null;
    }

    @Override // org.codehaus.jackson.map.deser.BasicDeserializerFactory
    protected JsonDeserializer<?> _findCustomCollectionLikeDeserializer(CollectionLikeType type, DeserializationConfig config, DeserializerProvider provider, BasicBeanDescription beanDesc, BeanProperty property, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer) throws JsonMappingException {
        for (Deserializers d : this._factoryConfig.deserializers()) {
            JsonDeserializer<?> deser = d.findCollectionLikeDeserializer(type, config, provider, beanDesc, property, elementTypeDeserializer, elementDeserializer);
            if (deser != null) {
                return deser;
            }
        }
        return null;
    }

    @Override // org.codehaus.jackson.map.deser.BasicDeserializerFactory
    protected JsonDeserializer<?> _findCustomEnumDeserializer(Class<?> type, DeserializationConfig config, BasicBeanDescription beanDesc, BeanProperty property) throws JsonMappingException {
        for (Deserializers d : this._factoryConfig.deserializers()) {
            JsonDeserializer<?> deser = d.findEnumDeserializer(type, config, beanDesc, property);
            if (deser != null) {
                return deser;
            }
        }
        return null;
    }

    @Override // org.codehaus.jackson.map.deser.BasicDeserializerFactory
    protected JsonDeserializer<?> _findCustomMapDeserializer(MapType type, DeserializationConfig config, DeserializerProvider provider, BasicBeanDescription beanDesc, BeanProperty property, KeyDeserializer keyDeserializer, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer) throws JsonMappingException {
        for (Deserializers d : this._factoryConfig.deserializers()) {
            JsonDeserializer<?> deser = d.findMapDeserializer(type, config, provider, beanDesc, property, keyDeserializer, elementTypeDeserializer, elementDeserializer);
            if (deser != null) {
                return deser;
            }
        }
        return null;
    }

    @Override // org.codehaus.jackson.map.deser.BasicDeserializerFactory
    protected JsonDeserializer<?> _findCustomMapLikeDeserializer(MapLikeType type, DeserializationConfig config, DeserializerProvider provider, BasicBeanDescription beanDesc, BeanProperty property, KeyDeserializer keyDeserializer, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer) throws JsonMappingException {
        for (Deserializers d : this._factoryConfig.deserializers()) {
            JsonDeserializer<?> deser = d.findMapLikeDeserializer(type, config, provider, beanDesc, property, keyDeserializer, elementTypeDeserializer, elementDeserializer);
            if (deser != null) {
                return deser;
            }
        }
        return null;
    }

    @Override // org.codehaus.jackson.map.deser.BasicDeserializerFactory
    protected JsonDeserializer<?> _findCustomTreeNodeDeserializer(Class<? extends JsonNode> type, DeserializationConfig config, BeanProperty property) throws JsonMappingException {
        for (Deserializers d : this._factoryConfig.deserializers()) {
            JsonDeserializer<?> deser = d.findTreeNodeDeserializer(type, config, property);
            if (deser != null) {
                return deser;
            }
        }
        return null;
    }

    protected JsonDeserializer<Object> _findCustomBeanDeserializer(JavaType type, DeserializationConfig config, DeserializerProvider provider, BasicBeanDescription beanDesc, BeanProperty property) throws JsonMappingException {
        for (Deserializers d : this._factoryConfig.deserializers()) {
            JsonDeserializer<?> deser = d.findBeanDeserializer(type, config, provider, beanDesc, property);
            if (deser != null) {
                return deser;
            }
        }
        return null;
    }

    @Override // org.codehaus.jackson.map.DeserializerFactory
    public JsonDeserializer<Object> createBeanDeserializer(DeserializationConfig config, DeserializerProvider p, JavaType type, BeanProperty property) throws JsonMappingException {
        JavaType concreteType;
        if (type.isAbstract()) {
            type = mapAbstractType(config, type);
        }
        BasicBeanDescription beanDesc = (BasicBeanDescription) config.introspect(type);
        JsonDeserializer<Object> ad = findDeserializerFromAnnotation(config, beanDesc.getClassInfo(), property);
        if (ad == null) {
            JavaType newType = modifyTypeByAnnotation(config, beanDesc.getClassInfo(), type, null);
            if (newType.getRawClass() != type.getRawClass()) {
                type = newType;
                beanDesc = (BasicBeanDescription) config.introspect(type);
            }
            JsonDeserializer<Object> custom = _findCustomBeanDeserializer(type, config, p, beanDesc, property);
            if (custom != null) {
                return custom;
            }
            if (type.isThrowable()) {
                return buildThrowableDeserializer(config, type, beanDesc, property);
            }
            if (type.isAbstract() && (concreteType = materializeAbstractType(config, beanDesc)) != null) {
                return buildBeanDeserializer(config, concreteType, (BasicBeanDescription) config.introspect(concreteType), property);
            }
            JsonDeserializer<Object> deser = findStdBeanDeserializer(config, p, type, property);
            if (deser != null) {
                return deser;
            }
            if (!isPotentialBeanType(type.getRawClass())) {
                return null;
            }
            if (type.isAbstract()) {
                return new AbstractDeserializer(type);
            }
            return buildBeanDeserializer(config, type, beanDesc, property);
        }
        return ad;
    }

    @Override // org.codehaus.jackson.map.deser.BasicDeserializerFactory
    protected JavaType mapAbstractType(DeserializationConfig config, JavaType type) throws JsonMappingException {
        while (true) {
            JavaType next = _mapAbstractType2(config, type);
            if (next == null) {
                return type;
            }
            Class<?> prevCls = type.getRawClass();
            Class<?> nextCls = next.getRawClass();
            if (prevCls == nextCls || !prevCls.isAssignableFrom(nextCls)) {
                throw new IllegalArgumentException("Invalid abstract type resolution from " + type + " to " + next + ": latter is not a subtype of former");
            }
            type = next;
        }
    }

    protected JavaType _mapAbstractType2(DeserializationConfig config, JavaType type) throws JsonMappingException {
        JavaType concrete;
        Class<?> currClass = type.getRawClass();
        if (this._factoryConfig.hasAbstractTypeResolvers()) {
            Iterator<AbstractTypeResolver> it = this._factoryConfig.abstractTypeResolvers().iterator();
            while (it.hasNext()) {
                JavaType concrete2 = it.next().findTypeMapping(config, type);
                if (concrete2 != null && concrete2.getRawClass() != currClass) {
                    return concrete2;
                }
            }
        }
        AbstractTypeResolver resolver = config.getAbstractTypeResolver();
        if (resolver == null || (concrete = resolver.findTypeMapping(config, type)) == null || concrete.getRawClass() == currClass) {
            return null;
        }
        return concrete;
    }

    protected JavaType materializeAbstractType(DeserializationConfig config, BasicBeanDescription beanDesc) throws JsonMappingException {
        JavaType concrete;
        AbstractTypeResolver resolver = config.getAbstractTypeResolver();
        if (resolver == null && !this._factoryConfig.hasAbstractTypeResolvers()) {
            return null;
        }
        JavaType abstractType = beanDesc.getType();
        AnnotationIntrospector intr = config.getAnnotationIntrospector();
        if (intr.findTypeResolver(config, beanDesc.getClassInfo(), abstractType) != null) {
            return null;
        }
        if (resolver == null || (concrete = resolver.resolveAbstractType(config, abstractType)) == null) {
            for (AbstractTypeResolver r : this._factoryConfig.abstractTypeResolvers()) {
                JavaType concrete2 = r.resolveAbstractType(config, abstractType);
                if (concrete2 != null) {
                    return concrete2;
                }
            }
            return null;
        }
        return concrete;
    }

    public JsonDeserializer<Object> buildBeanDeserializer(DeserializationConfig config, JavaType type, BasicBeanDescription beanDesc, BeanProperty property) throws JsonMappingException {
        BeanDeserializerBuilder builder = constructBeanDeserializerBuilder(beanDesc);
        builder.setCreators(findDeserializerCreators(config, beanDesc));
        addBeanProps(config, beanDesc, builder);
        addReferenceProperties(config, beanDesc, builder);
        if (this._factoryConfig.hasDeserializerModifiers()) {
            for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
                builder = mod.updateBuilder(config, beanDesc, builder);
            }
        }
        JsonDeserializer<?> deserializer = builder.build(property);
        if (this._factoryConfig.hasDeserializerModifiers()) {
            for (BeanDeserializerModifier mod2 : this._factoryConfig.deserializerModifiers()) {
                deserializer = mod2.modifyDeserializer(config, beanDesc, deserializer);
            }
        }
        return deserializer;
    }

    public JsonDeserializer<Object> buildThrowableDeserializer(DeserializationConfig config, JavaType type, BasicBeanDescription beanDesc, BeanProperty property) throws JsonMappingException {
        SettableBeanProperty prop;
        BeanDeserializerBuilder builder = constructBeanDeserializerBuilder(beanDesc);
        builder.setCreators(findDeserializerCreators(config, beanDesc));
        addBeanProps(config, beanDesc, builder);
        AnnotatedMethod am = beanDesc.findMethod("initCause", INIT_CAUSE_PARAMS);
        if (am != null && (prop = constructSettableProperty(config, beanDesc, "cause", am)) != null) {
            builder.addProperty(prop);
        }
        builder.addIgnorable("localizedMessage");
        builder.addIgnorable("message");
        if (this._factoryConfig.hasDeserializerModifiers()) {
            for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
                builder = mod.updateBuilder(config, beanDesc, builder);
            }
        }
        JsonDeserializer<?> deserializer = builder.build(property);
        if (deserializer instanceof BeanDeserializer) {
            deserializer = new ThrowableDeserializer((BeanDeserializer) deserializer);
        }
        if (this._factoryConfig.hasDeserializerModifiers()) {
            for (BeanDeserializerModifier mod2 : this._factoryConfig.deserializerModifiers()) {
                deserializer = mod2.modifyDeserializer(config, beanDesc, deserializer);
            }
        }
        return deserializer;
    }

    protected BeanDeserializerBuilder constructBeanDeserializerBuilder(BasicBeanDescription beanDesc) {
        return new BeanDeserializerBuilder(beanDesc);
    }

    protected CreatorContainer findDeserializerCreators(DeserializationConfig config, BasicBeanDescription beanDesc) throws JsonMappingException {
        Constructor<?> defaultCtor;
        boolean fixAccess = config.isEnabled(DeserializationConfig.Feature.CAN_OVERRIDE_ACCESS_MODIFIERS);
        CreatorContainer creators = new CreatorContainer(beanDesc, fixAccess);
        AnnotationIntrospector intr = config.getAnnotationIntrospector();
        if (beanDesc.getType().isConcrete() && (defaultCtor = beanDesc.findDefaultConstructor()) != null) {
            if (fixAccess) {
                ClassUtil.checkAndFixAccess(defaultCtor);
            }
            creators.setDefaultConstructor(defaultCtor);
        }
        VisibilityChecker<?> vchecker = config.getDefaultVisibilityChecker();
        if (!config.isEnabled(DeserializationConfig.Feature.AUTO_DETECT_CREATORS)) {
            vchecker = vchecker.withCreatorVisibility(JsonAutoDetect.Visibility.NONE);
        }
        VisibilityChecker<?> vchecker2 = config.getAnnotationIntrospector().findAutoDetectVisibility(beanDesc.getClassInfo(), vchecker);
        _addDeserializerConstructors(config, beanDesc, vchecker2, intr, creators);
        _addDeserializerFactoryMethods(config, beanDesc, vchecker2, intr, creators);
        return creators;
    }

    protected void _addDeserializerConstructors(DeserializationConfig config, BasicBeanDescription beanDesc, VisibilityChecker<?> vchecker, AnnotationIntrospector intr, CreatorContainer creators) throws JsonMappingException {
        for (AnnotatedConstructor ctor : beanDesc.getConstructors()) {
            int argCount = ctor.getParameterCount();
            if (argCount >= 1) {
                boolean isCreator = intr.hasCreatorAnnotation(ctor);
                boolean isVisible = vchecker.isCreatorVisible(ctor);
                if (argCount == 1) {
                    AnnotatedParameter param = ctor.getParameter(0);
                    String name = intr.findPropertyNameForParam(param);
                    if (name == null || name.length() == 0) {
                        Class<?> type = ctor.getParameterClass(0);
                        if (type == String.class) {
                            if (isCreator || isVisible) {
                                creators.addStringConstructor(ctor);
                            }
                        } else if (type == Integer.TYPE || type == Integer.class) {
                            if (isCreator || isVisible) {
                                creators.addIntConstructor(ctor);
                            }
                        } else if (type == Long.TYPE || type == Long.class) {
                            if (isCreator || isVisible) {
                                creators.addLongConstructor(ctor);
                            }
                        } else if (isCreator) {
                            creators.addDelegatingConstructor(ctor);
                        }
                    } else {
                        creators.addPropertyConstructor(ctor, new SettableBeanProperty[]{constructCreatorProperty(config, beanDesc, name, 0, param)});
                    }
                } else if (isCreator || isVisible) {
                    boolean annotationFound = false;
                    boolean notAnnotatedParamFound = false;
                    SettableBeanProperty[] properties = new SettableBeanProperty[argCount];
                    for (int i = 0; i < argCount; i++) {
                        AnnotatedParameter param2 = ctor.getParameter(i);
                        String name2 = param2 == null ? null : intr.findPropertyNameForParam(param2);
                        notAnnotatedParamFound |= name2 == null || name2.length() == 0;
                        annotationFound |= !notAnnotatedParamFound;
                        if (notAnnotatedParamFound && (annotationFound || isCreator)) {
                            throw new IllegalArgumentException("Argument #" + i + " of constructor " + ctor + " has no property name annotation; must have name when multiple-paramater constructor annotated as Creator");
                        }
                        properties[i] = constructCreatorProperty(config, beanDesc, name2, i, param2);
                    }
                    if (annotationFound) {
                        creators.addPropertyConstructor(ctor, properties);
                    }
                }
            }
        }
    }

    /* JADX WARN: Code duplicated, block: B:42:0x009c  */
    protected void _addDeserializerFactoryMethods(DeserializationConfig config, BasicBeanDescription beanDesc, VisibilityChecker<?> vchecker, AnnotationIntrospector intr, CreatorContainer creators) throws JsonMappingException {
        int i;
        String name;
        for (AnnotatedMethod factory : beanDesc.getFactoryMethods()) {
            int argCount = factory.getParameterCount();
            if (argCount >= 1) {
                boolean isCreator = intr.hasCreatorAnnotation(factory);
                if (argCount == 1) {
                    String name2 = intr.findPropertyNameForParam(factory.getParameter(0));
                    if (name2 == null || name2.length() == 0) {
                        Class<?> type = factory.getParameterClass(0);
                        if (type == String.class) {
                            if (isCreator || vchecker.isCreatorVisible(factory)) {
                                creators.addStringFactory(factory);
                            }
                        } else if (type == Integer.TYPE || type == Integer.class) {
                            if (isCreator || vchecker.isCreatorVisible(factory)) {
                                creators.addIntFactory(factory);
                            }
                        } else if (type == Long.TYPE || type == Long.class) {
                            if (isCreator || vchecker.isCreatorVisible(factory)) {
                                creators.addLongFactory(factory);
                            }
                        } else if (intr.hasCreatorAnnotation(factory)) {
                            creators.addDelegatingFactory(factory);
                        }
                    } else {
                        SettableBeanProperty[] properties = new SettableBeanProperty[argCount];
                        for (i = 0; i < argCount; i++) {
                            AnnotatedParameter param = factory.getParameter(i);
                            name = intr.findPropertyNameForParam(param);
                            if (name != null || name.length() == 0) {
                                throw new IllegalArgumentException("Argument #" + i + " of factory method " + factory + " has no property name annotation; must have when multiple-paramater static method annotated as Creator");
                            }
                            properties[i] = constructCreatorProperty(config, beanDesc, name, i, param);
                        }
                        creators.addPropertyFactory(factory, properties);
                    }
                } else if (intr.hasCreatorAnnotation(factory)) {
                    SettableBeanProperty[] properties2 = new SettableBeanProperty[argCount];
                    while (i < argCount) {
                        AnnotatedParameter param2 = factory.getParameter(i);
                        name = intr.findPropertyNameForParam(param2);
                        if (name != null) {
                        }
                        throw new IllegalArgumentException("Argument #" + i + " of factory method " + factory + " has no property name annotation; must have when multiple-paramater static method annotated as Creator");
                    }
                    creators.addPropertyFactory(factory, properties2);
                } else {
                    continue;
                }
            }
        }
    }

    protected void addBeanProps(DeserializationConfig config, BasicBeanDescription beanDesc, BeanDeserializerBuilder builder) throws JsonMappingException {
        VisibilityChecker<?> vchecker = config.getDefaultVisibilityChecker();
        if (!config.isEnabled(DeserializationConfig.Feature.AUTO_DETECT_SETTERS)) {
            vchecker = vchecker.withSetterVisibility(JsonAutoDetect.Visibility.NONE);
        }
        if (!config.isEnabled(DeserializationConfig.Feature.AUTO_DETECT_FIELDS)) {
            vchecker = vchecker.withFieldVisibility(JsonAutoDetect.Visibility.NONE);
        }
        VisibilityChecker<?> vchecker2 = config.getAnnotationIntrospector().findAutoDetectVisibility(beanDesc.getClassInfo(), vchecker);
        Map<String, AnnotatedMethod> setters = beanDesc.findSetters(vchecker2);
        AnnotatedMethod anySetter = beanDesc.findAnySetter();
        AnnotationIntrospector intr = config.getAnnotationIntrospector();
        Boolean B = intr.findIgnoreUnknownProperties(beanDesc.getClassInfo());
        if (B != null) {
            boolean ignoreAny = B.booleanValue();
            builder.setIgnoreUnknownProperties(ignoreAny);
        }
        HashSet<String> ignored = ArrayBuilders.arrayToSet(intr.findPropertiesToIgnore(beanDesc.getClassInfo()));
        for (String propName : ignored) {
            builder.addIgnorable(propName);
        }
        AnnotatedClass ac = beanDesc.getClassInfo();
        for (AnnotatedMethod am : ac.ignoredMemberMethods()) {
            String name = beanDesc.okNameForSetter(am);
            if (name != null) {
                builder.addIgnorable(name);
            }
        }
        for (AnnotatedField af : ac.ignoredFields()) {
            builder.addIgnorable(af.getName());
        }
        HashMap<Class<?>, Boolean> ignoredTypes = new HashMap<>();
        for (Map.Entry<String, AnnotatedMethod> en : setters.entrySet()) {
            String name2 = en.getKey();
            if (!ignored.contains(name2)) {
                AnnotatedMethod setter = en.getValue();
                Class<?> type = setter.getParameterClass(0);
                if (isIgnorableType(config, beanDesc, type, ignoredTypes)) {
                    builder.addIgnorable(name2);
                } else {
                    SettableBeanProperty prop = constructSettableProperty(config, beanDesc, name2, setter);
                    if (prop != null) {
                        builder.addProperty(prop);
                    }
                }
            }
        }
        if (anySetter != null) {
            builder.setAnySetter(constructAnySetter(config, beanDesc, anySetter));
        }
        HashSet<String> addedProps = new HashSet<>(setters.keySet());
        LinkedHashMap<String, AnnotatedField> fieldsByProp = beanDesc.findDeserializableFields(vchecker2, addedProps);
        for (Map.Entry<String, AnnotatedField> en2 : fieldsByProp.entrySet()) {
            String name3 = en2.getKey();
            if (!ignored.contains(name3) && !builder.hasProperty(name3)) {
                AnnotatedField field = en2.getValue();
                Class<?> type2 = field.getRawType();
                if (isIgnorableType(config, beanDesc, type2, ignoredTypes)) {
                    builder.addIgnorable(name3);
                } else {
                    SettableBeanProperty prop2 = constructSettableProperty(config, beanDesc, name3, field);
                    if (prop2 != null) {
                        builder.addProperty(prop2);
                        addedProps.add(name3);
                    }
                }
            }
        }
        if (config.isEnabled(DeserializationConfig.Feature.USE_GETTERS_AS_SETTERS)) {
            Map<String, AnnotatedMethod> getters = beanDesc.findGetters(vchecker2, addedProps);
            for (Map.Entry<String, AnnotatedMethod> en3 : getters.entrySet()) {
                AnnotatedMethod getter = en3.getValue();
                Class<?> rt = getter.getRawType();
                if (Collection.class.isAssignableFrom(rt) || Map.class.isAssignableFrom(rt)) {
                    String name4 = en3.getKey();
                    if (!ignored.contains(name4) && !builder.hasProperty(name4)) {
                        builder.addProperty(constructSetterlessProperty(config, beanDesc, name4, getter));
                        addedProps.add(name4);
                    }
                }
            }
        }
    }

    protected void addReferenceProperties(DeserializationConfig config, BasicBeanDescription beanDesc, BeanDeserializerBuilder builder) throws JsonMappingException {
        Map<String, AnnotatedMember> refs = beanDesc.findBackReferenceProperties();
        if (refs != null) {
            for (Map.Entry<String, AnnotatedMember> en : refs.entrySet()) {
                String name = en.getKey();
                AnnotatedMember m = en.getValue();
                if (m instanceof AnnotatedMethod) {
                    builder.addBackReferenceProperty(name, constructSettableProperty(config, beanDesc, m.getName(), (AnnotatedMethod) m));
                } else {
                    builder.addBackReferenceProperty(name, constructSettableProperty(config, beanDesc, m.getName(), (AnnotatedField) m));
                }
            }
        }
    }

    protected SettableAnyProperty constructAnySetter(DeserializationConfig config, BasicBeanDescription beanDesc, AnnotatedMethod setter) throws JsonMappingException {
        if (config.isEnabled(DeserializationConfig.Feature.CAN_OVERRIDE_ACCESS_MODIFIERS)) {
            setter.fixAccess();
        }
        JavaType type = beanDesc.bindingsForBeanType().resolveType(setter.getParameterType(1));
        BeanProperty.Std property = new BeanProperty.Std(setter.getName(), type, beanDesc.getClassAnnotations(), setter);
        JavaType type2 = resolveType(config, beanDesc, type, setter, property);
        JsonDeserializer<Object> deser = findDeserializerFromAnnotation(config, setter, property);
        if (deser != null) {
            SettableAnyProperty prop = new SettableAnyProperty(property, setter, type2);
            prop.setValueDeserializer(deser);
            return prop;
        }
        return new SettableAnyProperty(property, setter, modifyTypeByAnnotation(config, setter, type2, property.getName()));
    }

    protected SettableBeanProperty constructSettableProperty(DeserializationConfig config, BasicBeanDescription beanDesc, String name, AnnotatedMethod setter) throws JsonMappingException {
        if (config.isEnabled(DeserializationConfig.Feature.CAN_OVERRIDE_ACCESS_MODIFIERS)) {
            setter.fixAccess();
        }
        JavaType t0 = beanDesc.bindingsForBeanType().resolveType(setter.getParameterType(0));
        BeanProperty.Std property = new BeanProperty.Std(name, t0, beanDesc.getClassAnnotations(), setter);
        JavaType type = resolveType(config, beanDesc, t0, setter, property);
        if (type != t0) {
            property = property.withType(type);
        }
        JsonDeserializer<Object> propDeser = findDeserializerFromAnnotation(config, setter, property);
        JavaType type2 = modifyTypeByAnnotation(config, setter, type, name);
        TypeDeserializer typeDeser = (TypeDeserializer) type2.getTypeHandler();
        SettableBeanProperty prop = new SettableBeanProperty.MethodProperty(name, type2, typeDeser, beanDesc.getClassAnnotations(), setter);
        if (propDeser != null) {
            prop.setValueDeserializer(propDeser);
        }
        AnnotationIntrospector.ReferenceProperty ref = config.getAnnotationIntrospector().findReferenceType(setter);
        if (ref != null && ref.isManagedReference()) {
            prop.setManagedReferenceName(ref.getName());
        }
        return prop;
    }

    protected SettableBeanProperty constructSettableProperty(DeserializationConfig config, BasicBeanDescription beanDesc, String name, AnnotatedField field) throws JsonMappingException {
        if (config.isEnabled(DeserializationConfig.Feature.CAN_OVERRIDE_ACCESS_MODIFIERS)) {
            field.fixAccess();
        }
        JavaType t0 = beanDesc.bindingsForBeanType().resolveType(field.getGenericType());
        BeanProperty.Std property = new BeanProperty.Std(name, t0, beanDesc.getClassAnnotations(), field);
        JavaType type = resolveType(config, beanDesc, t0, field, property);
        if (type != t0) {
            property = property.withType(type);
        }
        JsonDeserializer<Object> propDeser = findDeserializerFromAnnotation(config, field, property);
        JavaType type2 = modifyTypeByAnnotation(config, field, type, name);
        TypeDeserializer typeDeser = (TypeDeserializer) type2.getTypeHandler();
        SettableBeanProperty prop = new SettableBeanProperty.FieldProperty(name, type2, typeDeser, beanDesc.getClassAnnotations(), field);
        if (propDeser != null) {
            prop.setValueDeserializer(propDeser);
        }
        AnnotationIntrospector.ReferenceProperty ref = config.getAnnotationIntrospector().findReferenceType(field);
        if (ref != null && ref.isManagedReference()) {
            prop.setManagedReferenceName(ref.getName());
        }
        return prop;
    }

    protected SettableBeanProperty constructSetterlessProperty(DeserializationConfig config, BasicBeanDescription beanDesc, String name, AnnotatedMethod getter) throws JsonMappingException {
        if (config.isEnabled(DeserializationConfig.Feature.CAN_OVERRIDE_ACCESS_MODIFIERS)) {
            getter.fixAccess();
        }
        JavaType type = getter.getType(beanDesc.bindingsForBeanType());
        BeanProperty.Std property = new BeanProperty.Std(name, type, beanDesc.getClassAnnotations(), getter);
        JsonDeserializer<Object> propDeser = findDeserializerFromAnnotation(config, getter, property);
        JavaType type2 = modifyTypeByAnnotation(config, getter, type, name);
        TypeDeserializer typeDeser = (TypeDeserializer) type2.getTypeHandler();
        SettableBeanProperty prop = new SettableBeanProperty.SetterlessProperty(name, type2, typeDeser, beanDesc.getClassAnnotations(), getter);
        if (propDeser != null) {
            prop.setValueDeserializer(propDeser);
        }
        return prop;
    }

    protected boolean isPotentialBeanType(Class<?> type) {
        String typeStr = ClassUtil.canBeABeanType(type);
        if (typeStr != null) {
            throw new IllegalArgumentException("Can not deserialize Class " + type.getName() + " (of type " + typeStr + ") as a Bean");
        }
        if (ClassUtil.isProxyType(type)) {
            throw new IllegalArgumentException("Can not deserialize Proxy class " + type.getName() + " as a Bean");
        }
        String typeStr2 = ClassUtil.isLocalType(type);
        if (typeStr2 != null) {
            throw new IllegalArgumentException("Can not deserialize Class " + type.getName() + " (of type " + typeStr2 + ") as a Bean");
        }
        return true;
    }

    protected boolean isIgnorableType(DeserializationConfig config, BasicBeanDescription beanDesc, Class<?> type, Map<Class<?>, Boolean> ignoredTypes) {
        Boolean status = ignoredTypes.get(type);
        if (status == null) {
            BasicBeanDescription desc = (BasicBeanDescription) config.introspectClassAnnotations(type);
            status = config.getAnnotationIntrospector().isIgnorableType(desc.getClassInfo());
            if (status == null) {
                status = Boolean.FALSE;
            }
        }
        return status.booleanValue();
    }
}
