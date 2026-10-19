package org.codehaus.jackson.map.ext;

import java.io.IOException;
import java.lang.reflect.Type;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.SerializerBase;
import org.w3c.dom.Node;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class DOMSerializer extends SerializerBase<Node> {
    protected final DOMImplementationLS _domImpl;

    public DOMSerializer() {
        super(Node.class);
        try {
            DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
            this._domImpl = (DOMImplementationLS) registry.getDOMImplementation("LS");
        } catch (Exception e) {
            throw new IllegalStateException("Could not instantiate DOMImplementationRegistry: " + e.getMessage(), e);
        }
    }

    @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.map.JsonSerializer
    public void serialize(Node value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        if (this._domImpl == null) {
            throw new IllegalStateException("Could not find DOM LS");
        }
        LSSerializer writer = this._domImpl.createLSSerializer();
        jgen.writeString(writer.writeToString(value));
    }

    @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.schema.SchemaAware
    public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
        return createSchemaNode("string", true);
    }
}
