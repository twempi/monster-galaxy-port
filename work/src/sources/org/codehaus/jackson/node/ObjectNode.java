package org.codehaus.jackson.node;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;

/* JADX INFO: loaded from: classes.dex */
public class ObjectNode extends ContainerNode {
    protected LinkedHashMap<String, JsonNode> _children;

    public ObjectNode(JsonNodeFactory nc) {
        super(nc);
        this._children = null;
    }

    @Override // org.codehaus.jackson.node.ContainerNode, org.codehaus.jackson.node.BaseJsonNode, org.codehaus.jackson.JsonNode
    public JsonToken asToken() {
        return JsonToken.START_OBJECT;
    }

    @Override // org.codehaus.jackson.JsonNode
    public boolean isObject() {
        return true;
    }

    @Override // org.codehaus.jackson.node.ContainerNode, org.codehaus.jackson.JsonNode
    public int size() {
        if (this._children == null) {
            return 0;
        }
        return this._children.size();
    }

    @Override // org.codehaus.jackson.JsonNode
    public Iterator<JsonNode> getElements() {
        return this._children == null ? ContainerNode.NoNodesIterator.instance() : this._children.values().iterator();
    }

    @Override // org.codehaus.jackson.node.ContainerNode, org.codehaus.jackson.JsonNode
    public JsonNode get(int index) {
        return null;
    }

    @Override // org.codehaus.jackson.node.ContainerNode, org.codehaus.jackson.JsonNode
    public JsonNode get(String fieldName) {
        if (this._children != null) {
            return this._children.get(fieldName);
        }
        return null;
    }

    @Override // org.codehaus.jackson.JsonNode
    public Iterator<String> getFieldNames() {
        return this._children == null ? ContainerNode.NoStringsIterator.instance() : this._children.keySet().iterator();
    }

    @Override // org.codehaus.jackson.JsonNode
    public JsonNode path(int index) {
        return MissingNode.getInstance();
    }

    @Override // org.codehaus.jackson.JsonNode
    public JsonNode path(String fieldName) {
        JsonNode n;
        return (this._children == null || (n = this._children.get(fieldName)) == null) ? MissingNode.getInstance() : n;
    }

    @Override // org.codehaus.jackson.JsonNode
    public Iterator<Map.Entry<String, JsonNode>> getFields() {
        return this._children == null ? NoFieldsIterator.instance : this._children.entrySet().iterator();
    }

    @Override // org.codehaus.jackson.JsonNode
    public ObjectNode with(String propertyName) {
        if (this._children == null) {
            this._children = new LinkedHashMap<>();
        } else {
            JsonNode n = this._children.get(propertyName);
            if (n != null) {
                if (n instanceof ObjectNode) {
                    return (ObjectNode) n;
                }
                throw new UnsupportedOperationException("Property '" + propertyName + "' has value that is not of type ObjectNode (but " + n.getClass().getName() + ")");
            }
        }
        ObjectNode result = objectNode();
        this._children.put(propertyName, result);
        return result;
    }

    @Override // org.codehaus.jackson.node.ContainerNode, org.codehaus.jackson.node.BaseJsonNode, org.codehaus.jackson.JsonNode
    public JsonNode findValue(String fieldName) {
        if (this._children != null) {
            for (Map.Entry<String, JsonNode> entry : this._children.entrySet()) {
                if (fieldName.equals(entry.getKey())) {
                    return entry.getValue();
                }
                JsonNode value = entry.getValue().findValue(fieldName);
                if (value != null) {
                    return value;
                }
            }
        }
        return null;
    }

    @Override // org.codehaus.jackson.node.ContainerNode, org.codehaus.jackson.node.BaseJsonNode, org.codehaus.jackson.JsonNode
    public List<JsonNode> findValues(String fieldName, List<JsonNode> foundSoFar) {
        if (this._children != null) {
            for (Map.Entry<String, JsonNode> entry : this._children.entrySet()) {
                if (fieldName.equals(entry.getKey())) {
                    if (foundSoFar == null) {
                        foundSoFar = new ArrayList<>();
                    }
                    foundSoFar.add(entry.getValue());
                } else {
                    foundSoFar = entry.getValue().findValues(fieldName, foundSoFar);
                }
            }
        }
        return foundSoFar;
    }

    @Override // org.codehaus.jackson.node.ContainerNode, org.codehaus.jackson.node.BaseJsonNode, org.codehaus.jackson.JsonNode
    public List<String> findValuesAsText(String fieldName, List<String> foundSoFar) {
        if (this._children != null) {
            for (Map.Entry<String, JsonNode> entry : this._children.entrySet()) {
                if (fieldName.equals(entry.getKey())) {
                    if (foundSoFar == null) {
                        foundSoFar = new ArrayList<>();
                    }
                    foundSoFar.add(entry.getValue().getValueAsText());
                } else {
                    foundSoFar = entry.getValue().findValuesAsText(fieldName, foundSoFar);
                }
            }
        }
        return foundSoFar;
    }

    @Override // org.codehaus.jackson.node.ContainerNode, org.codehaus.jackson.node.BaseJsonNode, org.codehaus.jackson.JsonNode
    public ObjectNode findParent(String fieldName) {
        if (this._children != null) {
            for (Map.Entry<String, JsonNode> entry : this._children.entrySet()) {
                if (!fieldName.equals(entry.getKey())) {
                    JsonNode value = entry.getValue().findParent(fieldName);
                    if (value != null) {
                        return (ObjectNode) value;
                    }
                } else {
                    return this;
                }
            }
        }
        return null;
    }

    @Override // org.codehaus.jackson.node.ContainerNode, org.codehaus.jackson.node.BaseJsonNode, org.codehaus.jackson.JsonNode
    public List<JsonNode> findParents(String fieldName, List<JsonNode> foundSoFar) {
        if (this._children != null) {
            for (Map.Entry<String, JsonNode> entry : this._children.entrySet()) {
                if (fieldName.equals(entry.getKey())) {
                    if (foundSoFar == null) {
                        foundSoFar = new ArrayList<>();
                    }
                    foundSoFar.add(this);
                } else {
                    foundSoFar = entry.getValue().findParents(fieldName, foundSoFar);
                }
            }
        }
        return foundSoFar;
    }

    @Override // org.codehaus.jackson.node.BaseJsonNode, org.codehaus.jackson.map.JsonSerializable
    public final void serialize(JsonGenerator jg, SerializerProvider provider) throws IOException {
        jg.writeStartObject();
        if (this._children != null) {
            for (Map.Entry<String, JsonNode> en : this._children.entrySet()) {
                jg.writeFieldName(en.getKey());
                ((BaseJsonNode) en.getValue()).serialize(jg, provider);
            }
        }
        jg.writeEndObject();
    }

    @Override // org.codehaus.jackson.node.BaseJsonNode, org.codehaus.jackson.map.JsonSerializableWithType
    public void serializeWithType(JsonGenerator jg, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
        typeSer.writeTypePrefixForObject(this, jg);
        if (this._children != null) {
            for (Map.Entry<String, JsonNode> en : this._children.entrySet()) {
                jg.writeFieldName(en.getKey());
                ((BaseJsonNode) en.getValue()).serialize(jg, provider);
            }
        }
        typeSer.writeTypeSuffixForObject(this, jg);
    }

    public JsonNode put(String fieldName, JsonNode value) {
        if (value == null) {
            value = nullNode();
        }
        return _put(fieldName, value);
    }

    public JsonNode remove(String fieldName) {
        if (this._children != null) {
            return this._children.remove(fieldName);
        }
        return null;
    }

    public ObjectNode remove(Collection<String> fieldNames) {
        if (this._children != null) {
            for (String fieldName : fieldNames) {
                this._children.remove(fieldName);
            }
        }
        return this;
    }

    @Override // org.codehaus.jackson.node.ContainerNode
    public ObjectNode removeAll() {
        this._children = null;
        return this;
    }

    public JsonNode putAll(Map<String, JsonNode> properties) {
        if (this._children == null) {
            this._children = new LinkedHashMap<>(properties);
        } else {
            for (Map.Entry<String, JsonNode> en : properties.entrySet()) {
                JsonNode n = en.getValue();
                if (n == null) {
                    n = nullNode();
                }
                this._children.put(en.getKey(), n);
            }
        }
        return this;
    }

    public JsonNode putAll(ObjectNode other) {
        int len = other.size();
        if (len > 0) {
            if (this._children == null) {
                this._children = new LinkedHashMap<>(len);
            }
            other.putContentsTo(this._children);
        }
        return this;
    }

    public ObjectNode retain(Collection<String> fieldNames) {
        if (this._children != null) {
            Iterator<Map.Entry<String, JsonNode>> entries = this._children.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, JsonNode> entry = entries.next();
                if (!fieldNames.contains(entry.getKey())) {
                    entries.remove();
                }
            }
        }
        return this;
    }

    public ObjectNode retain(String... fieldNames) {
        return retain(Arrays.asList(fieldNames));
    }

    public ArrayNode putArray(String fieldName) {
        ArrayNode n = arrayNode();
        _put(fieldName, n);
        return n;
    }

    public ObjectNode putObject(String fieldName) {
        ObjectNode n = objectNode();
        _put(fieldName, n);
        return n;
    }

    public void putPOJO(String fieldName, Object pojo) {
        _put(fieldName, POJONode(pojo));
    }

    public void putNull(String fieldName) {
        _put(fieldName, nullNode());
    }

    public void put(String fieldName, int v) {
        _put(fieldName, numberNode(v));
    }

    public void put(String fieldName, long v) {
        _put(fieldName, numberNode(v));
    }

    public void put(String fieldName, float v) {
        _put(fieldName, numberNode(v));
    }

    public void put(String fieldName, double v) {
        _put(fieldName, numberNode(v));
    }

    public void put(String fieldName, BigDecimal v) {
        if (v == null) {
            putNull(fieldName);
        } else {
            _put(fieldName, numberNode(v));
        }
    }

    public void put(String fieldName, String v) {
        if (v == null) {
            putNull(fieldName);
        } else {
            _put(fieldName, textNode(v));
        }
    }

    public void put(String fieldName, boolean v) {
        _put(fieldName, booleanNode(v));
    }

    public void put(String fieldName, byte[] v) {
        if (v == null) {
            putNull(fieldName);
        } else {
            _put(fieldName, binaryNode(v));
        }
    }

    protected void putContentsTo(Map<String, JsonNode> dst) {
        if (this._children != null) {
            for (Map.Entry<String, JsonNode> en : this._children.entrySet()) {
                dst.put(en.getKey(), en.getValue());
            }
        }
    }

    @Override // org.codehaus.jackson.JsonNode
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o != null && o.getClass() == getClass()) {
            ObjectNode other = (ObjectNode) o;
            if (other.size() != size()) {
                return false;
            }
            if (this._children == null) {
                return true;
            }
            for (Map.Entry<String, JsonNode> en : this._children.entrySet()) {
                String key = en.getKey();
                JsonNode value = en.getValue();
                JsonNode otherValue = other.get(key);
                if (otherValue == null || !otherValue.equals(value)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public int hashCode() {
        if (this._children == null) {
            return -1;
        }
        return this._children.hashCode();
    }

    @Override // org.codehaus.jackson.JsonNode
    public String toString() {
        StringBuilder sb = new StringBuilder((size() << 4) + 32);
        sb.append("{");
        if (this._children != null) {
            int count = 0;
            for (Map.Entry<String, JsonNode> en : this._children.entrySet()) {
                if (count > 0) {
                    sb.append(",");
                }
                count++;
                TextNode.appendQuoted(sb, en.getKey());
                sb.append(':');
                sb.append(en.getValue().toString());
            }
        }
        sb.append("}");
        return sb.toString();
    }

    private final JsonNode _put(String fieldName, JsonNode value) {
        if (this._children == null) {
            this._children = new LinkedHashMap<>();
        }
        return this._children.put(fieldName, value);
    }

    protected static class NoFieldsIterator implements Iterator<Map.Entry<String, JsonNode>> {
        static final NoFieldsIterator instance = new NoFieldsIterator();

        private NoFieldsIterator() {
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return false;
        }

        @Override // java.util.Iterator
        public Map.Entry<String, JsonNode> next() {
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new IllegalStateException();
        }
    }
}
