package org.codehaus.jackson.xc;

import java.io.IOException;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.deser.StdDeserializer;
import org.codehaus.jackson.node.ArrayNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class DomElementJsonDeserializer extends StdDeserializer<Element> {
    private final DocumentBuilder builder;

    public DomElementJsonDeserializer() {
        super((Class<?>) Element.class);
        try {
            DocumentBuilderFactory bf = DocumentBuilderFactory.newInstance();
            bf.setNamespaceAware(true);
            this.builder = bf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException();
        }
    }

    public DomElementJsonDeserializer(DocumentBuilder builder) {
        super((Class<?>) Element.class);
        this.builder = builder;
    }

    @Override // org.codehaus.jackson.map.JsonDeserializer
    public Element deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        Document document = this.builder.newDocument();
        return fromNode(document, jp.readValueAsTree());
    }

    protected Element fromNode(Document document, JsonNode jsonNode) throws IOException {
        String ns = jsonNode.get("namespace") != null ? jsonNode.get("namespace").getValueAsText() : null;
        String name = jsonNode.get("name") != null ? jsonNode.get("name").getValueAsText() : null;
        if (name == null) {
            throw new JsonMappingException("No name for DOM element was provided in the JSON object.");
        }
        Element element = document.createElementNS(ns, name);
        JsonNode attributesNode = jsonNode.get("attributes");
        if (attributesNode != null && (attributesNode instanceof ArrayNode)) {
            Iterator<JsonNode> atts = attributesNode.getElements();
            while (atts.hasNext()) {
                JsonNode node = atts.next();
                String ns2 = node.get("namespace") != null ? node.get("namespace").getValueAsText() : null;
                String name2 = node.get("name") != null ? node.get("name").getValueAsText() : null;
                String value = node.get("$") != null ? node.get("$").getValueAsText() : null;
                if (name2 != null) {
                    element.setAttributeNS(ns2, name2, value);
                }
            }
        }
        JsonNode childsNode = jsonNode.get("children");
        if (childsNode != null && (childsNode instanceof ArrayNode)) {
            Iterator<JsonNode> els = childsNode.getElements();
            while (els.hasNext()) {
                JsonNode node2 = els.next();
                String name3 = node2.get("name") != null ? node2.get("name").getValueAsText() : null;
                String value2 = node2.get("$") != null ? node2.get("$").getValueAsText() : null;
                if (value2 != null) {
                    element.appendChild(document.createTextNode(value2));
                } else if (name3 != null) {
                    element.appendChild(fromNode(document, node2));
                }
            }
        }
        return element;
    }
}
