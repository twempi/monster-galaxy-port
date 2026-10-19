package org.codehaus.jackson.map.ser.impl;

import java.io.IOException;
import java.net.InetAddress;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.ser.ScalarSerializerBase;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class InetAddressSerializer extends ScalarSerializerBase<InetAddress> {
    public static final InetAddressSerializer instance = new InetAddressSerializer();

    public InetAddressSerializer() {
        super(InetAddress.class);
    }

    @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.map.JsonSerializer
    public void serialize(InetAddress value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        String str = value.toString().trim();
        int ix = str.indexOf(47);
        if (ix >= 0) {
            if (ix == 0) {
                str = str.substring(1);
            } else {
                str = str.substring(0, ix);
            }
        }
        jgen.writeString(str);
    }

    @Override // org.codehaus.jackson.map.ser.ScalarSerializerBase, org.codehaus.jackson.map.JsonSerializer
    public void serializeWithType(InetAddress value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
        typeSer.writeTypePrefixForScalar(value, jgen, InetAddress.class);
        serialize(value, jgen, provider);
        typeSer.writeTypeSuffixForScalar(value, jgen);
    }
}
