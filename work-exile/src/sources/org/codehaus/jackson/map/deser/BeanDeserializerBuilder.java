package org.codehaus.jackson.map.deser;

import java.util.HashMap;
import java.util.HashSet;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.deser.impl.BeanPropertyMap;
import org.codehaus.jackson.map.introspect.BasicBeanDescription;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class BeanDeserializerBuilder {
    protected SettableAnyProperty _anySetter;
    protected HashMap<String, SettableBeanProperty> _backRefProperties;
    protected final BasicBeanDescription _beanDesc;
    protected CreatorContainer _creators;
    protected HashSet<String> _ignorableProps;
    protected boolean _ignoreAllUnknown;
    protected final HashMap<String, SettableBeanProperty> _properties = new HashMap<>();

    public BeanDeserializerBuilder(BasicBeanDescription beanDesc) {
        this._beanDesc = beanDesc;
    }

    public void setCreators(CreatorContainer creators) {
        this._creators = creators;
    }

    public void addOrReplaceProperty(SettableBeanProperty prop, boolean allowOverride) {
        this._properties.put(prop.getName(), prop);
    }

    public void addProperty(SettableBeanProperty prop) {
        SettableBeanProperty old = this._properties.put(prop.getName(), prop);
        if (old != null && old != prop) {
            throw new IllegalArgumentException("Duplicate property '" + prop.getName() + "' for " + this._beanDesc.getType());
        }
    }

    public void addBackReferenceProperty(String referenceName, SettableBeanProperty prop) {
        if (this._backRefProperties == null) {
            this._backRefProperties = new HashMap<>(4);
        }
        this._backRefProperties.put(referenceName, prop);
    }

    public void addIgnorable(String propName) {
        if (this._ignorableProps == null) {
            this._ignorableProps = new HashSet<>();
        }
        this._ignorableProps.add(propName);
    }

    public boolean hasProperty(String propertyName) {
        return this._properties.containsKey(propertyName);
    }

    public SettableBeanProperty removeProperty(String name) {
        return this._properties.remove(name);
    }

    public void setAnySetter(SettableAnyProperty s) {
        if (this._anySetter != null && s != null) {
            throw new IllegalStateException("_anySetter already set to non-null");
        }
        this._anySetter = s;
    }

    public void setIgnoreUnknownProperties(boolean ignore) {
        this._ignoreAllUnknown = ignore;
    }

    public JsonDeserializer<?> build(BeanProperty forProperty) {
        BeanPropertyMap propertyMap = new BeanPropertyMap(this._properties.values());
        propertyMap.assignIndexes();
        return new BeanDeserializer(this._beanDesc.getClassInfo(), this._beanDesc.getType(), forProperty, this._creators, propertyMap, this._backRefProperties, this._ignorableProps, this._ignoreAllUnknown, this._anySetter);
    }
}
