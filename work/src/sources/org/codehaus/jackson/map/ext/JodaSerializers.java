package org.codehaus.jackson.map.ext;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.SerializerBase;
import org.codehaus.jackson.map.util.Provider;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/* JADX INFO: loaded from: classes.dex */
public class JodaSerializers implements Provider<Map.Entry<Class<?>, JsonSerializer<?>>> {
    static final HashMap<Class<?>, JsonSerializer<?>> _serializers = new HashMap<>();

    static {
        _serializers.put(DateTime.class, new DateTimeSerializer());
        _serializers.put(LocalDateTime.class, new LocalDateTimeSerializer());
        _serializers.put(LocalDate.class, new LocalDateSerializer());
        _serializers.put(DateMidnight.class, new DateMidnightSerializer());
    }

    @Override // org.codehaus.jackson.map.util.Provider
    public Collection<Map.Entry<Class<?>, JsonSerializer<?>>> provide() {
        return _serializers.entrySet();
    }

    protected static abstract class JodaSerializer<T> extends SerializerBase<T> {
        static final DateTimeFormatter _localDateTimeFormat = ISODateTimeFormat.dateTime();
        static final DateTimeFormatter _localDateFormat = ISODateTimeFormat.date();

        protected JodaSerializer(Class<T> cls) {
            super(cls);
        }

        protected String printLocalDateTime(ReadablePartial dateValue) throws IOException {
            return _localDateTimeFormat.print(dateValue);
        }

        protected String printLocalDate(ReadablePartial dateValue) throws IOException {
            return _localDateFormat.print(dateValue);
        }

        protected String printLocalDate(ReadableInstant dateValue) throws IOException {
            return _localDateFormat.print(dateValue);
        }
    }

    public static final class DateTimeSerializer extends JodaSerializer<DateTime> {
        public DateTimeSerializer() {
            super(DateTime.class);
        }

        @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.map.JsonSerializer
        public void serialize(DateTime value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            if (provider.isEnabled(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS)) {
                jgen.writeNumber(value.getMillis());
            } else {
                jgen.writeString(value.toString());
            }
        }

        @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.schema.SchemaAware
        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode(provider.isEnabled(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS) ? "number" : "string", true);
        }
    }

    public static final class LocalDateTimeSerializer extends JodaSerializer<LocalDateTime> {
        public LocalDateTimeSerializer() {
            super(LocalDateTime.class);
        }

        @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.map.JsonSerializer
        public void serialize(LocalDateTime dt, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            if (provider.isEnabled(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS)) {
                jgen.writeStartArray();
                jgen.writeNumber(dt.year().get());
                jgen.writeNumber(dt.monthOfYear().get());
                jgen.writeNumber(dt.dayOfMonth().get());
                jgen.writeNumber(dt.hourOfDay().get());
                jgen.writeNumber(dt.minuteOfHour().get());
                jgen.writeNumber(dt.secondOfMinute().get());
                jgen.writeNumber(dt.millisOfSecond().get());
                jgen.writeEndArray();
                return;
            }
            jgen.writeString(printLocalDateTime(dt));
        }

        @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.schema.SchemaAware
        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode(provider.isEnabled(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS) ? "array" : "string", true);
        }
    }

    public static final class LocalDateSerializer extends JodaSerializer<LocalDate> {
        public LocalDateSerializer() {
            super(LocalDate.class);
        }

        @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.map.JsonSerializer
        public void serialize(LocalDate dt, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            if (provider.isEnabled(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS)) {
                jgen.writeStartArray();
                jgen.writeNumber(dt.year().get());
                jgen.writeNumber(dt.monthOfYear().get());
                jgen.writeNumber(dt.dayOfMonth().get());
                jgen.writeEndArray();
                return;
            }
            jgen.writeString(printLocalDate((ReadablePartial) dt));
        }

        @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.schema.SchemaAware
        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode(provider.isEnabled(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS) ? "array" : "string", true);
        }
    }

    public static final class DateMidnightSerializer extends JodaSerializer<DateMidnight> {
        public DateMidnightSerializer() {
            super(DateMidnight.class);
        }

        @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.map.JsonSerializer
        public void serialize(DateMidnight dt, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            if (provider.isEnabled(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS)) {
                jgen.writeStartArray();
                jgen.writeNumber(dt.year().get());
                jgen.writeNumber(dt.monthOfYear().get());
                jgen.writeNumber(dt.dayOfMonth().get());
                jgen.writeEndArray();
                return;
            }
            jgen.writeString(printLocalDate((ReadableInstant) dt));
        }

        @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.schema.SchemaAware
        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode(provider.isEnabled(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS) ? "array" : "string", true);
        }
    }
}
