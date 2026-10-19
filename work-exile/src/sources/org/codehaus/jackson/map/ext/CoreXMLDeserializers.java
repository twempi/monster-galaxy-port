package org.codehaus.jackson.map.ext;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.deser.FromStringDeserializer;
import org.codehaus.jackson.map.deser.StdDeserializer;
import org.codehaus.jackson.map.deser.StdScalarDeserializer;
import org.codehaus.jackson.map.util.Provider;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class CoreXMLDeserializers implements Provider<StdDeserializer<?>> {
    static final DatatypeFactory _dataTypeFactory;

    static {
        try {
            _dataTypeFactory = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override // org.codehaus.jackson.map.util.Provider
    public Collection<StdDeserializer<?>> provide() {
        return Arrays.asList(new DurationDeserializer(), new GregorianCalendarDeserializer(), new QNameDeserializer());
    }

    public static class DurationDeserializer extends FromStringDeserializer<Duration> {
        public DurationDeserializer() {
            super(Duration.class);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.codehaus.jackson.map.deser.FromStringDeserializer
        public Duration _deserialize(String value, DeserializationContext ctxt) throws IllegalArgumentException {
            return CoreXMLDeserializers._dataTypeFactory.newDuration(value);
        }
    }

    public static class GregorianCalendarDeserializer extends StdScalarDeserializer<XMLGregorianCalendar> {
        public GregorianCalendarDeserializer() {
            super(XMLGregorianCalendar.class);
        }

        @Override // org.codehaus.jackson.map.JsonDeserializer
        public XMLGregorianCalendar deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            Date d = _parseDate(jp, ctxt);
            if (d == null) {
                return null;
            }
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(d);
            return CoreXMLDeserializers._dataTypeFactory.newXMLGregorianCalendar(calendar);
        }
    }

    public static class QNameDeserializer extends FromStringDeserializer<QName> {
        public QNameDeserializer() {
            super(QName.class);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.codehaus.jackson.map.deser.FromStringDeserializer
        public QName _deserialize(String value, DeserializationContext ctxt) throws IllegalArgumentException {
            return QName.valueOf(value);
        }
    }
}
