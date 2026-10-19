package org.codehaus.jackson.map.ext;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.SerializerBase;
import org.codehaus.jackson.map.ser.StdSerializers;
import org.codehaus.jackson.map.ser.ToStringSerializer;
import org.codehaus.jackson.map.util.Provider;

/* JADX INFO: loaded from: classes.dex */
public class CoreXMLSerializers implements Provider<Map.Entry<Class<?>, JsonSerializer<?>>> {
    static final HashMap<Class<?>, JsonSerializer<?>> _serializers = new HashMap<>();

    static {
        ToStringSerializer tss = ToStringSerializer.instance;
        _serializers.put(Duration.class, tss);
        _serializers.put(XMLGregorianCalendar.class, new XMLGregorianCalendarSerializer());
        _serializers.put(QName.class, tss);
    }

    @Override // org.codehaus.jackson.map.util.Provider
    public Collection<Map.Entry<Class<?>, JsonSerializer<?>>> provide() {
        return _serializers.entrySet();
    }

    public static class XMLGregorianCalendarSerializer extends SerializerBase<XMLGregorianCalendar> {
        public XMLGregorianCalendarSerializer() {
            super(XMLGregorianCalendar.class);
        }

        @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.map.JsonSerializer
        public void serialize(XMLGregorianCalendar value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            StdSerializers.CalendarSerializer.instance.serialize((Calendar) value.toGregorianCalendar(), jgen, provider);
        }

        @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.schema.SchemaAware
        public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
            return StdSerializers.CalendarSerializer.instance.getSchema(provider, typeHint);
        }
    }
}
