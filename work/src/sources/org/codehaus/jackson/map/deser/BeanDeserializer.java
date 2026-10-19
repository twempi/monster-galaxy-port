package org.codehaus.jackson.map.deser;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.DeserializerProvider;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ResolvableDeserializer;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.annotate.JsonCachable;
import org.codehaus.jackson.map.deser.impl.BeanPropertyMap;
import org.codehaus.jackson.map.introspect.AnnotatedClass;
import org.codehaus.jackson.map.type.ClassKey;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.util.TokenBuffer;

/* JADX INFO: loaded from: classes.dex */
@JsonCachable
public class BeanDeserializer extends StdDeserializer<Object> implements ResolvableDeserializer {
    protected final SettableAnyProperty _anySetter;
    protected final Map<String, SettableBeanProperty> _backRefs;
    protected final BeanPropertyMap _beanProperties;
    protected final JavaType _beanType;
    protected final Constructor<?> _defaultConstructor;
    protected final Creator.Delegating _delegatingCreator;
    protected final AnnotatedClass _forClass;
    protected final HashSet<String> _ignorableProps;
    protected final boolean _ignoreAllUnknown;
    protected final Creator.NumberBased _numberCreator;
    protected final BeanProperty _property;
    protected final Creator.PropertyBased _propertyBasedCreator;
    protected final Creator.StringBased _stringCreator;
    protected HashMap<ClassKey, JsonDeserializer<Object>> _subDeserializers;

    public BeanDeserializer(AnnotatedClass forClass, JavaType type, BeanProperty property, CreatorContainer creators, BeanPropertyMap properties, Map<String, SettableBeanProperty> backRefs, HashSet<String> ignorableProps, boolean ignoreAllUnknown, SettableAnyProperty anySetter) {
        super(type);
        this._forClass = forClass;
        this._beanType = type;
        this._property = property;
        this._beanProperties = properties;
        this._backRefs = backRefs;
        this._ignorableProps = ignorableProps;
        this._ignoreAllUnknown = ignoreAllUnknown;
        this._anySetter = anySetter;
        this._stringCreator = creators.stringCreator();
        this._numberCreator = creators.numberCreator();
        this._delegatingCreator = creators.delegatingCreator();
        this._propertyBasedCreator = creators.propertyBasedCreator();
        if (this._delegatingCreator != null || this._propertyBasedCreator != null) {
            this._defaultConstructor = null;
        } else {
            this._defaultConstructor = creators.getDefaultConstructor();
        }
    }

    protected BeanDeserializer(BeanDeserializer src) {
        super(src._beanType);
        this._forClass = src._forClass;
        this._beanType = src._beanType;
        this._property = src._property;
        this._beanProperties = src._beanProperties;
        this._backRefs = src._backRefs;
        this._ignorableProps = src._ignorableProps;
        this._ignoreAllUnknown = src._ignoreAllUnknown;
        this._anySetter = src._anySetter;
        this._defaultConstructor = src._defaultConstructor;
        this._stringCreator = src._stringCreator;
        this._numberCreator = src._numberCreator;
        this._delegatingCreator = src._delegatingCreator;
        this._propertyBasedCreator = src._propertyBasedCreator;
    }

    public boolean hasProperty(String propertyName) {
        return this._beanProperties.find(propertyName) != null;
    }

    public int getPropertyCount() {
        return this._beanProperties.size();
    }

    @Override // org.codehaus.jackson.map.ResolvableDeserializer
    public void resolve(DeserializationConfig config, DeserializerProvider provider) throws JsonMappingException {
        SettableBeanProperty backProp;
        Iterator<SettableBeanProperty> it = this._beanProperties.allProperties();
        while (it.hasNext()) {
            SettableBeanProperty prop = it.next();
            if (!prop.hasValueDeserializer()) {
                prop.setValueDeserializer(findDeserializer(config, provider, prop.getType(), prop));
            }
            String refName = prop.getManagedReferenceName();
            if (refName != null) {
                JsonDeserializer<?> valueDeser = prop._valueDeserializer;
                boolean isContainer = false;
                if (valueDeser instanceof BeanDeserializer) {
                    backProp = ((BeanDeserializer) valueDeser).findBackReference(refName);
                } else {
                    if (!(valueDeser instanceof ContainerDeserializer)) {
                        if (valueDeser instanceof AbstractDeserializer) {
                            throw new IllegalArgumentException("Can not handle managed/back reference for abstract types (property " + this._beanType.getRawClass().getName() + "." + prop.getName() + ")");
                        }
                        throw new IllegalArgumentException("Can not handle managed/back reference '" + refName + "': type for value deserializer is not BeanDeserializer or ContainerDeserializer, but " + valueDeser.getClass().getName());
                    }
                    JsonDeserializer<?> contentDeser = ((ContainerDeserializer) valueDeser).getContentDeserializer();
                    if (!(contentDeser instanceof BeanDeserializer)) {
                        throw new IllegalArgumentException("Can not handle managed/back reference '" + refName + "': value deserializer is of type ContainerDeserializer, but content type is not handled by a BeanDeserializer  (instead it's of type " + contentDeser.getClass().getName() + ")");
                    }
                    backProp = ((BeanDeserializer) contentDeser).findBackReference(refName);
                    isContainer = true;
                }
                if (backProp == null) {
                    throw new IllegalArgumentException("Can not handle managed/back reference '" + refName + "': no back reference property found from type " + prop.getType());
                }
                JavaType referredType = this._beanType;
                JavaType backRefType = backProp.getType();
                if (!backRefType.getRawClass().isAssignableFrom(referredType.getRawClass())) {
                    throw new IllegalArgumentException("Can not handle managed/back reference '" + refName + "': back reference type (" + backRefType.getRawClass().getName() + ") not compatible with managed type (" + referredType.getRawClass().getName() + ")");
                }
                this._beanProperties.replace(new SettableBeanProperty.ManagedReferenceProperty(refName, prop, backProp, this._forClass.getAnnotations(), isContainer));
            }
        }
        if (this._anySetter != null && !this._anySetter.hasValueDeserializer()) {
            this._anySetter.setValueDeserializer(findDeserializer(config, provider, this._anySetter.getType(), this._anySetter.getProperty()));
        }
        if (this._delegatingCreator != null) {
            BeanProperty property = new BeanProperty.Std(null, this._delegatingCreator.getValueType(), this._forClass.getAnnotations(), this._delegatingCreator.getCreator());
            JsonDeserializer<Object> deser = findDeserializer(config, provider, this._delegatingCreator.getValueType(), property);
            this._delegatingCreator.setDeserializer(deser);
        }
        if (this._propertyBasedCreator != null) {
            for (SettableBeanProperty prop2 : this._propertyBasedCreator.properties()) {
                if (!prop2.hasValueDeserializer()) {
                    prop2.setValueDeserializer(findDeserializer(config, provider, prop2.getType(), prop2));
                }
            }
        }
    }

    @Override // org.codehaus.jackson.map.JsonDeserializer
    public final Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.START_OBJECT) {
            jp.nextToken();
            return deserializeFromObject(jp, ctxt);
        }
        switch (t) {
            case VALUE_STRING:
                return deserializeFromString(jp, ctxt);
            case VALUE_NUMBER_INT:
            case VALUE_NUMBER_FLOAT:
                return deserializeFromNumber(jp, ctxt);
            case VALUE_EMBEDDED_OBJECT:
                return jp.getEmbeddedObject();
            case VALUE_TRUE:
            case VALUE_FALSE:
            case START_ARRAY:
                return deserializeUsingCreator(jp, ctxt);
            case FIELD_NAME:
            case END_OBJECT:
                return deserializeFromObject(jp, ctxt);
            default:
                throw ctxt.mappingException(getBeanClass());
        }
    }

    @Override // org.codehaus.jackson.map.JsonDeserializer
    public Object deserialize(JsonParser jp, DeserializationContext ctxt, Object bean) throws Exception {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.START_OBJECT) {
            t = jp.nextToken();
        }
        while (t == JsonToken.FIELD_NAME) {
            String propName = jp.getCurrentName();
            SettableBeanProperty prop = this._beanProperties.find(propName);
            jp.nextToken();
            if (prop != null) {
                try {
                    prop.deserializeAndSet(jp, ctxt, bean);
                } catch (Exception e) {
                    wrapAndThrow(e, bean, propName, ctxt);
                }
            } else if (this._ignorableProps != null && this._ignorableProps.contains(propName)) {
                jp.skipChildren();
            } else if (this._anySetter != null) {
                this._anySetter.deserializeAndSet(jp, ctxt, bean, propName);
            } else {
                handleUnknownProperty(jp, ctxt, bean, propName);
            }
            t = jp.nextToken();
        }
        return bean;
    }

    @Override // org.codehaus.jackson.map.deser.StdDeserializer, org.codehaus.jackson.map.JsonDeserializer
    public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
        return typeDeserializer.deserializeTypedFromObject(jp, ctxt);
    }

    public final Class<?> getBeanClass() {
        return this._beanType.getRawClass();
    }

    @Override // org.codehaus.jackson.map.deser.StdDeserializer
    public JavaType getValueType() {
        return this._beanType;
    }

    public Iterator<SettableBeanProperty> properties() {
        if (this._beanProperties == null) {
            throw new IllegalStateException("Can only call before BeanDeserializer has been resolved");
        }
        return this._beanProperties.allProperties();
    }

    public SettableBeanProperty findBackReference(String logicalName) {
        if (this._backRefs == null) {
            return null;
        }
        return this._backRefs.get(logicalName);
    }

    public Object deserializeFromObject(JsonParser jp, DeserializationContext ctxt) throws Throwable {
        if (this._defaultConstructor == null) {
            if (this._propertyBasedCreator != null) {
                return _deserializeUsingPropertyBased(jp, ctxt);
            }
            if (this._delegatingCreator != null) {
                return this._delegatingCreator.deserialize(jp, ctxt);
            }
            if (this._beanType.isAbstract()) {
                throw JsonMappingException.from(jp, "Can not instantiate abstract type " + this._beanType + " (need to add/enable type information?)");
            }
            throw JsonMappingException.from(jp, "No suitable constructor found for type " + this._beanType + ": can not instantiate from JSON object (need to add/enable type information?)");
        }
        Object bean = constructDefaultInstance();
        while (jp.getCurrentToken() != JsonToken.END_OBJECT) {
            String propName = jp.getCurrentName();
            jp.nextToken();
            SettableBeanProperty prop = this._beanProperties.find(propName);
            if (prop != null) {
                try {
                    prop.deserializeAndSet(jp, ctxt, bean);
                } catch (Exception e) {
                    wrapAndThrow(e, bean, propName, ctxt);
                }
            } else if (this._ignorableProps != null && this._ignorableProps.contains(propName)) {
                jp.skipChildren();
            } else if (this._anySetter != null) {
                try {
                    this._anySetter.deserializeAndSet(jp, ctxt, bean, propName);
                } catch (Exception e2) {
                    wrapAndThrow(e2, bean, propName, ctxt);
                }
            } else {
                handleUnknownProperty(jp, ctxt, bean, propName);
            }
            jp.nextToken();
        }
        return bean;
    }

    public Object deserializeFromString(JsonParser jp, DeserializationContext ctxt) throws IOException {
        if (this._stringCreator != null) {
            return this._stringCreator.construct(jp.getText());
        }
        if (this._delegatingCreator != null) {
            return this._delegatingCreator.deserialize(jp, ctxt);
        }
        if (ctxt.isEnabled(DeserializationConfig.Feature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT) && jp.getTextLength() == 0) {
            return null;
        }
        throw ctxt.instantiationException(getBeanClass(), "no suitable creator method found to deserialize from JSON String");
    }

    public Object deserializeFromNumber(JsonParser jp, DeserializationContext ctxt) throws IOException {
        if (this._numberCreator != null) {
            switch (jp.getNumberType()) {
                case INT:
                    return this._numberCreator.construct(jp.getIntValue());
                case LONG:
                    return this._numberCreator.construct(jp.getLongValue());
            }
        }
        if (this._delegatingCreator != null) {
            return this._delegatingCreator.deserialize(jp, ctxt);
        }
        throw ctxt.instantiationException(getBeanClass(), "no suitable creator method found to deserialize from JSON Number");
    }

    public Object deserializeUsingCreator(JsonParser jp, DeserializationContext ctxt) throws Throwable {
        if (this._delegatingCreator != null) {
            try {
                return this._delegatingCreator.deserialize(jp, ctxt);
            } catch (Exception e) {
                wrapInstantiationProblem(e, ctxt);
            }
        }
        throw ctxt.mappingException(getBeanClass());
    }

    protected final Object _deserializeUsingPropertyBased(JsonParser jp, DeserializationContext ctxt) throws Throwable {
        Creator.PropertyBased creator = this._propertyBasedCreator;
        PropertyValueBuffer buffer = creator.startBuilding(jp, ctxt);
        TokenBuffer unknown = null;
        JsonToken t = jp.getCurrentToken();
        while (t == JsonToken.FIELD_NAME) {
            String propName = jp.getCurrentName();
            jp.nextToken();
            SettableBeanProperty prop = creator.findCreatorProperty(propName);
            if (prop != null) {
                Object value = prop.deserialize(jp, ctxt);
                if (buffer.assignParameter(prop.getCreatorIndex(), value)) {
                    jp.nextToken();
                    try {
                        Object bean = creator.build(buffer);
                        if (bean.getClass() != this._beanType.getRawClass()) {
                            return handlePolymorphic(jp, ctxt, bean, unknown);
                        }
                        if (unknown != null) {
                            bean = handleUnknownProperties(ctxt, bean, unknown);
                        }
                        return deserialize(jp, ctxt, bean);
                    } catch (Exception e) {
                        wrapAndThrow(e, this._beanType.getRawClass(), propName, ctxt);
                    }
                } else {
                    continue;
                }
            } else {
                SettableBeanProperty prop2 = this._beanProperties.find(propName);
                if (prop2 != null) {
                    buffer.bufferProperty(prop2, prop2.deserialize(jp, ctxt));
                } else if (this._ignorableProps != null && this._ignorableProps.contains(propName)) {
                    jp.skipChildren();
                } else if (this._anySetter != null) {
                    buffer.bufferAnyProperty(this._anySetter, propName, this._anySetter.deserialize(jp, ctxt));
                } else {
                    if (unknown == null) {
                        unknown = new TokenBuffer(jp.getCodec());
                    }
                    unknown.writeFieldName(propName);
                    unknown.copyCurrentStructure(jp);
                }
            }
            t = jp.nextToken();
        }
        try {
            Object bean2 = creator.build(buffer);
            if (unknown != null) {
                if (bean2.getClass() != this._beanType.getRawClass()) {
                    return handlePolymorphic(null, ctxt, bean2, unknown);
                }
                return handleUnknownProperties(ctxt, bean2, unknown);
            }
            return bean2;
        } catch (Exception e2) {
            wrapInstantiationProblem(e2, ctxt);
            return null;
        }
    }

    @Override // org.codehaus.jackson.map.deser.StdDeserializer
    protected void handleUnknownProperty(JsonParser jp, DeserializationContext ctxt, Object beanOrClass, String propName) throws IOException {
        if (this._ignoreAllUnknown || (this._ignorableProps != null && this._ignorableProps.contains(propName))) {
            jp.skipChildren();
        } else {
            super.handleUnknownProperty(jp, ctxt, beanOrClass, propName);
        }
    }

    protected Object handleUnknownProperties(DeserializationContext ctxt, Object bean, TokenBuffer unknownTokens) throws IOException {
        unknownTokens.writeEndObject();
        JsonParser bufferParser = unknownTokens.asParser();
        while (bufferParser.nextToken() != JsonToken.END_OBJECT) {
            String propName = bufferParser.getCurrentName();
            bufferParser.nextToken();
            handleUnknownProperty(bufferParser, ctxt, bean, propName);
        }
        return bean;
    }

    protected Object handlePolymorphic(JsonParser jp, DeserializationContext ctxt, Object bean, TokenBuffer unknownTokens) throws Exception {
        JsonDeserializer<Object> subDeser = _findSubclassDeserializer(ctxt, bean, unknownTokens);
        if (subDeser != null) {
            if (unknownTokens != null) {
                unknownTokens.writeEndObject();
                JsonParser p2 = unknownTokens.asParser();
                p2.nextToken();
                bean = subDeser.deserialize(p2, ctxt, bean);
            }
            if (jp != null) {
                bean = subDeser.deserialize(jp, ctxt, bean);
            }
            return bean;
        }
        if (unknownTokens != null) {
            bean = handleUnknownProperties(ctxt, bean, unknownTokens);
        }
        if (jp != null) {
            bean = deserialize(jp, ctxt, bean);
        }
        return bean;
    }

    protected JsonDeserializer<Object> _findSubclassDeserializer(DeserializationContext ctxt, Object bean, TokenBuffer unknownTokens) throws IOException {
        JsonDeserializer<Object> subDeser;
        synchronized (this) {
            subDeser = this._subDeserializers == null ? null : this._subDeserializers.get(new ClassKey(bean.getClass()));
        }
        if (subDeser != null) {
            return subDeser;
        }
        DeserializerProvider deserProv = ctxt.getDeserializerProvider();
        if (deserProv != null) {
            JavaType type = ctxt.constructType(bean.getClass());
            subDeser = deserProv.findValueDeserializer(ctxt.getConfig(), type, this._property);
            if (subDeser != null) {
                synchronized (this) {
                    if (this._subDeserializers == null) {
                        this._subDeserializers = new HashMap<>();
                    }
                    this._subDeserializers.put(new ClassKey(bean.getClass()), subDeser);
                }
            }
        }
        return subDeser;
    }

    protected Object constructDefaultInstance() throws Throwable {
        try {
            return this._defaultConstructor.newInstance(new Object[0]);
        } catch (Exception e) {
            ClassUtil.unwrapAndThrowAsIAE(e);
            return null;
        }
    }

    public void wrapAndThrow(Throwable t, Object bean, String fieldName, DeserializationContext ctxt) throws Throwable {
        while ((t instanceof InvocationTargetException) && t.getCause() != null) {
            t = t.getCause();
        }
        if (t instanceof Error) {
            throw ((Error) t);
        }
        boolean wrap = ctxt == null || ctxt.isEnabled(DeserializationConfig.Feature.WRAP_EXCEPTIONS);
        if (t instanceof IOException) {
            if (!wrap || !(t instanceof JsonMappingException)) {
                throw ((IOException) t);
            }
        } else if (!wrap && (t instanceof RuntimeException)) {
            throw ((RuntimeException) t);
        }
        throw JsonMappingException.wrapWithPath(t, bean, fieldName);
    }

    public void wrapAndThrow(Throwable t, Object bean, int index, DeserializationContext ctxt) throws Throwable {
        while ((t instanceof InvocationTargetException) && t.getCause() != null) {
            t = t.getCause();
        }
        if (t instanceof Error) {
            throw ((Error) t);
        }
        boolean wrap = ctxt == null || ctxt.isEnabled(DeserializationConfig.Feature.WRAP_EXCEPTIONS);
        if (t instanceof IOException) {
            if (!wrap || !(t instanceof JsonMappingException)) {
                throw ((IOException) t);
            }
        } else if (!wrap && (t instanceof RuntimeException)) {
            throw ((RuntimeException) t);
        }
        throw JsonMappingException.wrapWithPath(t, bean, index);
    }

    protected void wrapInstantiationProblem(Throwable t, DeserializationContext ctxt) throws Throwable {
        while ((t instanceof InvocationTargetException) && t.getCause() != null) {
            t = t.getCause();
        }
        if (t instanceof Error) {
            throw ((Error) t);
        }
        boolean wrap = ctxt == null || ctxt.isEnabled(DeserializationConfig.Feature.WRAP_EXCEPTIONS);
        if (t instanceof IOException) {
            throw ((IOException) t);
        }
        if (!wrap && (t instanceof RuntimeException)) {
            throw ((RuntimeException) t);
        }
        throw ctxt.instantiationException(this._beanType.getRawClass(), t);
    }

    @Deprecated
    public void wrapAndThrow(Throwable t, Object bean, String fieldName) throws Throwable {
        wrapAndThrow(t, bean, fieldName, (DeserializationContext) null);
    }

    @Deprecated
    public void wrapAndThrow(Throwable t, Object bean, int index) throws Throwable {
        wrapAndThrow(t, bean, index, (DeserializationContext) null);
    }
}
