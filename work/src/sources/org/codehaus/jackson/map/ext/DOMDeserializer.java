package org.codehaus.jackson.map.ext;

import java.io.StringReader;
import javax.xml.parsers.DocumentBuilderFactory;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.deser.FromStringDeserializer;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

/* JADX INFO: loaded from: classes.dex */
public abstract class DOMDeserializer<T> extends FromStringDeserializer<T> {
    static final DocumentBuilderFactory _parserFactory = DocumentBuilderFactory.newInstance();

    @Override // org.codehaus.jackson.map.deser.FromStringDeserializer
    public abstract T _deserialize(String str, DeserializationContext deserializationContext);

    static {
        _parserFactory.setNamespaceAware(true);
    }

    protected DOMDeserializer(Class<T> cls) {
        super(cls);
    }

    protected final Document parse(String value) throws IllegalArgumentException {
        try {
            return _parserFactory.newDocumentBuilder().parse(new InputSource(new StringReader(value)));
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to parse JSON String as XML: " + e.getMessage(), e);
        }
    }

    public static class NodeDeserializer extends DOMDeserializer<Node> {
        public NodeDeserializer() {
            super(Node.class);
        }

        @Override // org.codehaus.jackson.map.ext.DOMDeserializer, org.codehaus.jackson.map.deser.FromStringDeserializer
        public Node _deserialize(String value, DeserializationContext ctxt) throws IllegalArgumentException {
            return parse(value);
        }
    }

    public static class DocumentDeserializer extends DOMDeserializer<Document> {
        public DocumentDeserializer() {
            super(Document.class);
        }

        @Override // org.codehaus.jackson.map.ext.DOMDeserializer, org.codehaus.jackson.map.deser.FromStringDeserializer
        public Document _deserialize(String value, DeserializationContext ctxt) throws IllegalArgumentException {
            return parse(value);
        }
    }
}
