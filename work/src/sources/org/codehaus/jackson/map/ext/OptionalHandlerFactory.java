package org.codehaus.jackson.map.ext;

import java.util.Collection;
import java.util.Map;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializerProvider;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.deser.StdDeserializer;
import org.codehaus.jackson.map.util.Provider;
import org.codehaus.jackson.type.JavaType;

/* JADX INFO: loaded from: classes.dex */
public class OptionalHandlerFactory {
    private static final String CLASS_NAME_DOM_DOCUMENT = "org.w3c.dom.Node";
    private static final String CLASS_NAME_DOM_NODE = "org.w3c.dom.Node";
    private static final String DESERIALIZERS_FOR_JAVAX_XML = "org.codehaus.jackson.map.ext.CoreXMLDeserializers";
    private static final String DESERIALIZERS_FOR_JODA_DATETIME = "org.codehaus.jackson.map.ext.JodaDeserializers";
    private static final String DESERIALIZER_FOR_DOM_DOCUMENT = "org.codehaus.jackson.map.ext.DOMDeserializer$DocumentDeserializer";
    private static final String DESERIALIZER_FOR_DOM_NODE = "org.codehaus.jackson.map.ext.DOMDeserializer$NodeDeserializer";
    private static final String PACKAGE_PREFIX_JAVAX_XML = "javax.xml.";
    private static final String PACKAGE_PREFIX_JODA_DATETIME = "org.joda.time.";
    private static final String SERIALIZERS_FOR_JAVAX_XML = "org.codehaus.jackson.map.ext.CoreXMLSerializers";
    private static final String SERIALIZERS_FOR_JODA_DATETIME = "org.codehaus.jackson.map.ext.JodaSerializers";
    private static final String SERIALIZER_FOR_DOM_NODE = "org.codehaus.jackson.map.ext.DOMSerializer";
    public static final OptionalHandlerFactory instance = new OptionalHandlerFactory();

    protected OptionalHandlerFactory() {
    }

    public JsonSerializer<?> findSerializer(SerializationConfig config, JavaType type) {
        String factoryName;
        Class<?> rawType = type.getRawClass();
        String className = rawType.getName();
        if (className.startsWith(PACKAGE_PREFIX_JODA_DATETIME)) {
            factoryName = SERIALIZERS_FOR_JODA_DATETIME;
        } else if (className.startsWith(PACKAGE_PREFIX_JAVAX_XML) || hasSupertypeStartingWith(rawType, PACKAGE_PREFIX_JAVAX_XML)) {
            factoryName = SERIALIZERS_FOR_JAVAX_XML;
        } else {
            if (doesImplement(rawType, "org.w3c.dom.Node")) {
                return (JsonSerializer) instantiate(SERIALIZER_FOR_DOM_NODE);
            }
            return null;
        }
        Object ob = instantiate(factoryName);
        if (ob == null) {
            return null;
        }
        Provider<Map.Entry<Class<?>, JsonSerializer<?>>> prov = (Provider) ob;
        Collection<Map.Entry<Class<?>, JsonSerializer<?>>> entries = prov.provide();
        for (Map.Entry<Class<?>, JsonSerializer<?>> entry : entries) {
            if (rawType == entry.getKey()) {
                return entry.getValue();
            }
        }
        for (Map.Entry<Class<?>, JsonSerializer<?>> entry2 : entries) {
            if (entry2.getKey().isAssignableFrom(rawType)) {
                return entry2.getValue();
            }
        }
        return null;
    }

    public JsonDeserializer<?> findDeserializer(JavaType type, DeserializationConfig config, DeserializerProvider p) {
        String factoryName;
        Class<?> rawType = type.getRawClass();
        String className = rawType.getName();
        if (className.startsWith(PACKAGE_PREFIX_JODA_DATETIME)) {
            factoryName = DESERIALIZERS_FOR_JODA_DATETIME;
        } else if (className.startsWith(PACKAGE_PREFIX_JAVAX_XML) || hasSupertypeStartingWith(rawType, PACKAGE_PREFIX_JAVAX_XML)) {
            factoryName = DESERIALIZERS_FOR_JAVAX_XML;
        } else {
            if (doesImplement(rawType, "org.w3c.dom.Node")) {
                return (JsonDeserializer) instantiate(DESERIALIZER_FOR_DOM_DOCUMENT);
            }
            if (doesImplement(rawType, "org.w3c.dom.Node")) {
                return (JsonDeserializer) instantiate(DESERIALIZER_FOR_DOM_NODE);
            }
            return null;
        }
        Object ob = instantiate(factoryName);
        if (ob == null) {
            return null;
        }
        Provider<StdDeserializer<?>> prov = (Provider) ob;
        Collection<StdDeserializer<?>> entries = prov.provide();
        for (StdDeserializer<?> deser : entries) {
            if (rawType == deser.getValueClass()) {
                return deser;
            }
        }
        for (StdDeserializer<?> deser2 : entries) {
            if (deser2.getValueClass().isAssignableFrom(rawType)) {
                return deser2;
            }
        }
        return null;
    }

    private Object instantiate(String className) {
        try {
            return Class.forName(className).newInstance();
        } catch (Exception | LinkageError e) {
            return null;
        }
    }

    private boolean doesImplement(Class<?> actualType, String classNameToImplement) {
        for (Class<?> type = actualType; type != null; type = type.getSuperclass()) {
            if (type.getName().equals(classNameToImplement) || hasInterface(type, classNameToImplement)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasInterface(Class<?> type, String interfaceToImplement) {
        Class<?>[] interfaces = type.getInterfaces();
        for (Class<?> iface : interfaces) {
            if (iface.getName().equals(interfaceToImplement)) {
                return true;
            }
        }
        for (Class<?> iface2 : interfaces) {
            if (hasInterface(iface2, interfaceToImplement)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasSupertypeStartingWith(Class<?> rawType, String prefix) {
        for (Class<?> supertype = rawType.getSuperclass(); supertype != null; supertype = supertype.getSuperclass()) {
            if (supertype.getName().startsWith(prefix)) {
                return true;
            }
        }
        for (Class<?> cls = rawType; cls != null; cls = cls.getSuperclass()) {
            if (hasInterfaceStartingWith(cls, prefix)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasInterfaceStartingWith(Class<?> type, String prefix) {
        Class<?>[] interfaces = type.getInterfaces();
        for (Class<?> iface : interfaces) {
            if (iface.getName().startsWith(prefix)) {
                return true;
            }
        }
        for (Class<?> iface2 : interfaces) {
            if (hasInterfaceStartingWith(iface2, prefix)) {
                return true;
            }
        }
        return false;
    }
}
