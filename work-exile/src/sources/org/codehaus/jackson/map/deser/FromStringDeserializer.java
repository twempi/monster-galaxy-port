package org.codehaus.jackson.map.deser;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Pattern;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public abstract class FromStringDeserializer<T> extends StdScalarDeserializer<T> {
    protected abstract T _deserialize(String str, DeserializationContext deserializationContext) throws IOException;

    protected FromStringDeserializer(Class<?> vc) {
        super(vc);
    }

    public static Iterable<FromStringDeserializer<?>> all() {
        ArrayList<FromStringDeserializer<?>> all = new ArrayList<>();
        all.add(new UUIDDeserializer());
        all.add(new URLDeserializer());
        all.add(new URIDeserializer());
        all.add(new CurrencyDeserializer());
        all.add(new PatternDeserializer());
        all.add(new LocaleDeserializer());
        all.add(new InetAddressDeserializer());
        all.add(new TimeZoneDeserializer());
        return all;
    }

    @Override // org.codehaus.jackson.map.JsonDeserializer
    public final T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        if (jsonParser.getCurrentToken() == JsonToken.VALUE_STRING) {
            String strTrim = jsonParser.getText().trim();
            if (strTrim.length() == 0) {
                return null;
            }
            try {
                T t_deserialize = _deserialize(strTrim, deserializationContext);
                if (t_deserialize != null) {
                    return t_deserialize;
                }
            } catch (IllegalArgumentException e) {
            }
            throw deserializationContext.weirdStringException(this._valueClass, "not a valid textual representation");
        }
        if (jsonParser.getCurrentToken() == JsonToken.VALUE_EMBEDDED_OBJECT) {
            T t = (T) jsonParser.getEmbeddedObject();
            if (t != null) {
                return this._valueClass.isAssignableFrom(t.getClass()) ? t : _deserializeEmbedded(t, deserializationContext);
            }
            return null;
        }
        throw deserializationContext.mappingException(this._valueClass);
    }

    protected T _deserializeEmbedded(Object ob, DeserializationContext ctxt) throws IOException {
        throw ctxt.mappingException("Don't know how to convert embedded Object of type " + ob.getClass().getName() + " into " + this._valueClass.getName());
    }

    public static class UUIDDeserializer extends FromStringDeserializer<UUID> {
        public UUIDDeserializer() {
            super(UUID.class);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.codehaus.jackson.map.deser.FromStringDeserializer
        public UUID _deserialize(String value, DeserializationContext ctxt) throws IOException {
            return UUID.fromString(value);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.codehaus.jackson.map.deser.FromStringDeserializer
        public UUID _deserializeEmbedded(Object ob, DeserializationContext ctxt) throws IOException {
            if (ob instanceof byte[]) {
                byte[] bytes = (byte[]) ob;
                if (bytes.length != 16) {
                    ctxt.mappingException("Can only construct UUIDs from 16 byte arrays; got " + bytes.length + " bytes");
                }
                DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes));
                long l1 = in.readLong();
                long l2 = in.readLong();
                return new UUID(l1, l2);
            }
            super._deserializeEmbedded(ob, ctxt);
            return null;
        }
    }

    public static class URLDeserializer extends FromStringDeserializer<URL> {
        public URLDeserializer() {
            super(URL.class);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.codehaus.jackson.map.deser.FromStringDeserializer
        public URL _deserialize(String value, DeserializationContext ctxt) throws IOException {
            return new URL(value);
        }
    }

    public static class URIDeserializer extends FromStringDeserializer<URI> {
        public URIDeserializer() {
            super(URI.class);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.codehaus.jackson.map.deser.FromStringDeserializer
        public URI _deserialize(String value, DeserializationContext ctxt) throws IllegalArgumentException {
            return URI.create(value);
        }
    }

    public static class CurrencyDeserializer extends FromStringDeserializer<Currency> {
        public CurrencyDeserializer() {
            super(Currency.class);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.codehaus.jackson.map.deser.FromStringDeserializer
        public Currency _deserialize(String value, DeserializationContext ctxt) throws IllegalArgumentException {
            return Currency.getInstance(value);
        }
    }

    public static class PatternDeserializer extends FromStringDeserializer<Pattern> {
        public PatternDeserializer() {
            super(Pattern.class);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.codehaus.jackson.map.deser.FromStringDeserializer
        public Pattern _deserialize(String value, DeserializationContext ctxt) throws IllegalArgumentException {
            return Pattern.compile(value);
        }
    }

    protected static class LocaleDeserializer extends FromStringDeserializer<Locale> {
        public LocaleDeserializer() {
            super(Locale.class);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.codehaus.jackson.map.deser.FromStringDeserializer
        public Locale _deserialize(String value, DeserializationContext ctxt) throws IOException {
            int ix = value.indexOf(95);
            if (ix < 0) {
                return new Locale(value);
            }
            String first = value.substring(0, ix);
            String value2 = value.substring(ix + 1);
            int ix2 = value2.indexOf(95);
            if (ix2 < 0) {
                return new Locale(first, value2);
            }
            String second = value2.substring(0, ix2);
            return new Locale(first, second, value2.substring(ix2 + 1));
        }
    }

    protected static class InetAddressDeserializer extends FromStringDeserializer<InetAddress> {
        public InetAddressDeserializer() {
            super(InetAddress.class);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.codehaus.jackson.map.deser.FromStringDeserializer
        public InetAddress _deserialize(String value, DeserializationContext ctxt) throws IOException {
            return InetAddress.getByName(value);
        }
    }

    protected static class TimeZoneDeserializer extends FromStringDeserializer<TimeZone> {
        public TimeZoneDeserializer() {
            super(TimeZone.class);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.codehaus.jackson.map.deser.FromStringDeserializer
        public TimeZone _deserialize(String value, DeserializationContext ctxt) throws IOException {
            return TimeZone.getTimeZone(value);
        }
    }
}
