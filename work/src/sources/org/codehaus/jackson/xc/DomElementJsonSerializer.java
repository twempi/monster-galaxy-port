package org.codehaus.jackson.xc;

import java.io.IOException;
import java.lang.reflect.Type;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.SerializerBase;
import org.codehaus.jackson.node.ObjectNode;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* JADX INFO: loaded from: classes.dex */
public class DomElementJsonSerializer extends SerializerBase<Element> {
    public DomElementJsonSerializer() {
        super(Element.class);
    }

    @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.map.JsonSerializer
    public void serialize(Element value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        jgen.writeStringField("name", value.getTagName());
        if (value.getNamespaceURI() != null) {
            jgen.writeStringField("namespace", value.getNamespaceURI());
        }
        NamedNodeMap attributes = value.getAttributes();
        if (attributes != null && attributes.getLength() > 0) {
            jgen.writeArrayFieldStart("attributes");
            for (int i = 0; i < attributes.getLength(); i++) {
                Attr attribute = (Attr) attributes.item(i);
                jgen.writeStartObject();
                jgen.writeStringField("$", attribute.getValue());
                jgen.writeStringField("name", attribute.getName());
                String ns = attribute.getNamespaceURI();
                if (ns != null) {
                    jgen.writeStringField("namespace", ns);
                }
                jgen.writeEndObject();
            }
            jgen.writeEndArray();
        }
        NodeList children = value.getChildNodes();
        if (children != null && children.getLength() > 0) {
            jgen.writeArrayFieldStart("children");
            for (int i2 = 0; i2 < children.getLength(); i2++) {
                Node child = children.item(i2);
                switch (child.getNodeType()) {
                    case 1:
                        serialize((Element) child, jgen, provider);
                        break;
                    case 3:
                    case 4:
                        jgen.writeStartObject();
                        jgen.writeStringField("$", child.getNodeValue());
                        jgen.writeEndObject();
                        break;
                }
            }
            jgen.writeEndArray();
        }
        jgen.writeEndObject();
    }

    @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.schema.SchemaAware
    public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
        ObjectNode o = createSchemaNode("object", true);
        o.put("name", createSchemaNode("string"));
        o.put("namespace", createSchemaNode("string", true));
        o.put("attributes", createSchemaNode("array", true));
        o.put("children", createSchemaNode("array", true));
        return o;
    }
}
