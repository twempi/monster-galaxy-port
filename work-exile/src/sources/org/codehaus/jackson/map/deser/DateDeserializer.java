package org.codehaus.jackson.map.deser;

import java.io.IOException;
import java.util.Date;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class DateDeserializer extends StdScalarDeserializer<Date> {
    public DateDeserializer() {
        super(Date.class);
    }

    @Override // org.codehaus.jackson.map.JsonDeserializer
    public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        return _parseDate(jp, ctxt);
    }
}
