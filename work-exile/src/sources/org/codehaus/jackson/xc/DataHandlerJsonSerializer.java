package org.codehaus.jackson.xc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import javax.activation.DataHandler;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.SerializerBase;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class DataHandlerJsonSerializer extends SerializerBase<DataHandler> {
    public DataHandlerJsonSerializer() {
        super(DataHandler.class);
    }

    @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.map.JsonSerializer
    public void serialize(DataHandler value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[Opcodes.ACC_SYNTHETIC];
        InputStream in = value.getInputStream();
        for (int len = in.read(buffer); len > 0; len = in.read(buffer)) {
            out.write(buffer, 0, len);
        }
        jgen.writeBinary(out.toByteArray());
    }

    @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.schema.SchemaAware
    public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
        ObjectNode o = createSchemaNode("array", true);
        ObjectNode itemSchema = createSchemaNode("string");
        o.put("items", itemSchema);
        return o;
    }
}
