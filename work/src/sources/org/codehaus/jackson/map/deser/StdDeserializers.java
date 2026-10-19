package org.codehaus.jackson.map.deser;

import java.util.GregorianCalendar;
import java.util.HashMap;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;

/* JADX INFO: loaded from: classes.dex */
class StdDeserializers {
    final HashMap<JavaType, JsonDeserializer<Object>> _deserializers = new HashMap<>();

    private StdDeserializers() {
        add(new UntypedObjectDeserializer());
        StdDeserializer<?> strDeser = new StdDeserializer.StringDeserializer();
        add(strDeser, String.class);
        add(strDeser, CharSequence.class);
        add(new StdDeserializer.ClassDeserializer());
        add(new StdDeserializer.BooleanDeserializer(Boolean.class, null));
        add(new StdDeserializer.ByteDeserializer(Byte.class, null));
        add(new StdDeserializer.ShortDeserializer(Short.class, null));
        add(new StdDeserializer.CharacterDeserializer(Character.class, null));
        add(new StdDeserializer.IntegerDeserializer(Integer.class, null));
        add(new StdDeserializer.LongDeserializer(Long.class, null));
        add(new StdDeserializer.FloatDeserializer(Float.class, null));
        add(new StdDeserializer.DoubleDeserializer(Double.class, null));
        add(new StdDeserializer.BooleanDeserializer(Boolean.TYPE, Boolean.FALSE));
        add(new StdDeserializer.ByteDeserializer(Byte.TYPE, (byte) 0));
        add(new StdDeserializer.ShortDeserializer(Short.TYPE, (short) 0));
        add(new StdDeserializer.CharacterDeserializer(Character.TYPE, (char) 0));
        add(new StdDeserializer.IntegerDeserializer(Integer.TYPE, 0));
        add(new StdDeserializer.LongDeserializer(Long.TYPE, 0L));
        add(new StdDeserializer.FloatDeserializer(Float.TYPE, Float.valueOf(0.0f)));
        add(new StdDeserializer.DoubleDeserializer(Double.TYPE, Double.valueOf(0.0d)));
        add(new StdDeserializer.NumberDeserializer());
        add(new StdDeserializer.BigDecimalDeserializer());
        add(new StdDeserializer.BigIntegerDeserializer());
        add(new DateDeserializer());
        add(new StdDeserializer.SqlDateDeserializer());
        add(new TimestampDeserializer());
        add(new StdDeserializer.CalendarDeserializer());
        add(new StdDeserializer.CalendarDeserializer(GregorianCalendar.class), GregorianCalendar.class);
        for (StdDeserializer<?> deser : FromStringDeserializer.all()) {
            add(deser);
        }
        add(new StdDeserializer.StackTraceElementDeserializer());
        add(new StdDeserializer.TokenBufferDeserializer());
        add(new StdDeserializer.AtomicBooleanDeserializer());
    }

    public static HashMap<JavaType, JsonDeserializer<Object>> constructAll() {
        return new StdDeserializers()._deserializers;
    }

    private void add(StdDeserializer<?> stdDeser) {
        add(stdDeser, stdDeser.getValueClass());
    }

    private void add(StdDeserializer<?> stdDeser, Class<?> valueClass) {
        this._deserializers.put(TypeFactory.defaultInstance().constructType(valueClass), stdDeser);
    }
}
