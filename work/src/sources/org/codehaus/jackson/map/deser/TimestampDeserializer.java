package org.codehaus.jackson.map.deser;

import java.io.IOException;
import java.sql.Timestamp;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;

/* JADX INFO: loaded from: classes.dex */
public class TimestampDeserializer extends StdScalarDeserializer<Timestamp> {
    public TimestampDeserializer() {
        super(Timestamp.class);
    }

    @Override // org.codehaus.jackson.map.JsonDeserializer
    public Timestamp deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        return new Timestamp(_parseDate(jp, ctxt).getTime());
    }
}
