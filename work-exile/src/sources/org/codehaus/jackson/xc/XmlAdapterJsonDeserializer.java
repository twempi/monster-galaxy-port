package org.codehaus.jackson.xc;

import java.io.IOException;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.deser.StdDeserializer;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class XmlAdapterJsonDeserializer extends StdDeserializer<Object> {
    protected static final JavaType ADAPTER_TYPE = TypeFactory.defaultInstance().uncheckedSimpleType(XmlAdapter.class);
    protected JsonDeserializer<?> _deserializer;
    protected final BeanProperty _property;
    protected final JavaType _valueType;
    protected final XmlAdapter<Object, Object> _xmlAdapter;

    public XmlAdapterJsonDeserializer(XmlAdapter<Object, Object> xmlAdapter, BeanProperty property) {
        super((Class<?>) Object.class);
        this._property = property;
        this._xmlAdapter = xmlAdapter;
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        JavaType type = typeFactory.constructType(xmlAdapter.getClass());
        JavaType[] rawTypes = typeFactory.findTypeParameters(type, XmlAdapter.class);
        this._valueType = (rawTypes == null || rawTypes.length == 0) ? TypeFactory.unknownType() : rawTypes[0];
    }

    @Override // org.codehaus.jackson.map.JsonDeserializer
    public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonDeserializer<?> deser = this._deserializer;
        if (deser == null) {
            DeserializationConfig config = ctxt.getConfig();
            deser = ctxt.getDeserializerProvider().findValueDeserializer(config, this._valueType, this._property);
            this._deserializer = deser;
        }
        Object boundObject = deser.deserialize(jp, ctxt);
        try {
            return this._xmlAdapter.unmarshal(boundObject);
        } catch (Exception e) {
            throw new JsonMappingException("Unable to unmarshal (to type " + this._valueType + "): " + e.getMessage(), e);
        }
    }

    @Override // org.codehaus.jackson.map.deser.StdDeserializer, org.codehaus.jackson.map.JsonDeserializer
    public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
        return typeDeserializer.deserializeTypedFromAny(jp, ctxt);
    }
}
