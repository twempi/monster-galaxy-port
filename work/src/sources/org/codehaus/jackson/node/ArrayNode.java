package org.codehaus.jackson.node;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;

/* JADX INFO: loaded from: classes.dex */
public final class ArrayNode extends ContainerNode {
    protected ArrayList<JsonNode> _children;

    public ArrayNode(JsonNodeFactory nc) {
        super(nc);
    }

    @Override // org.codehaus.jackson.node.ContainerNode, org.codehaus.jackson.node.BaseJsonNode, org.codehaus.jackson.JsonNode
    public JsonToken asToken() {
        return JsonToken.START_ARRAY;
    }

    @Override // org.codehaus.jackson.JsonNode
    public boolean isArray() {
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
        return this._children == null ? ContainerNode.NoNodesIterator.instance() : this._children.iterator();
    }

    @Override // org.codehaus.jackson.node.ContainerNode, org.codehaus.jackson.JsonNode
    public JsonNode get(int index) {
        if (index < 0 || this._children == null || index >= this._children.size()) {
            return null;
        }
        return this._children.get(index);
    }

    @Override // org.codehaus.jackson.node.ContainerNode, org.codehaus.jackson.JsonNode
    public JsonNode get(String fieldName) {
        return null;
    }

    @Override // org.codehaus.jackson.JsonNode
    public JsonNode path(String fieldName) {
        return MissingNode.getInstance();
    }

    @Override // org.codehaus.jackson.JsonNode
    public JsonNode path(int index) {
        return (index < 0 || this._children == null || index >= this._children.size()) ? MissingNode.getInstance() : this._children.get(index);
    }

    @Override // org.codehaus.jackson.node.BaseJsonNode, org.codehaus.jackson.map.JsonSerializable
    public final void serialize(JsonGenerator jg, SerializerProvider provider) throws IOException {
        jg.writeStartArray();
        if (this._children != null) {
            for (JsonNode n : this._children) {
                ((BaseJsonNode) n).writeTo(jg);
            }
        }
        jg.writeEndArray();
    }

    @Override // org.codehaus.jackson.node.BaseJsonNode, org.codehaus.jackson.map.JsonSerializableWithType
    public void serializeWithType(JsonGenerator jg, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
        typeSer.writeTypePrefixForArray(this, jg);
        if (this._children != null) {
            for (JsonNode n : this._children) {
                ((BaseJsonNode) n).writeTo(jg);
            }
        }
        typeSer.writeTypeSuffixForArray(this, jg);
    }

    @Override // org.codehaus.jackson.node.ContainerNode, org.codehaus.jackson.node.BaseJsonNode, org.codehaus.jackson.JsonNode
    public JsonNode findValue(String fieldName) {
        if (this._children != null) {
            for (JsonNode node : this._children) {
                JsonNode value = node.findValue(fieldName);
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
            for (JsonNode node : this._children) {
                foundSoFar = node.findValues(fieldName, foundSoFar);
            }
        }
        return foundSoFar;
    }

    @Override // org.codehaus.jackson.node.ContainerNode, org.codehaus.jackson.node.BaseJsonNode, org.codehaus.jackson.JsonNode
    public List<String> findValuesAsText(String fieldName, List<String> foundSoFar) {
        if (this._children != null) {
            for (JsonNode node : this._children) {
                foundSoFar = node.findValuesAsText(fieldName, foundSoFar);
            }
        }
        return foundSoFar;
    }

    @Override // org.codehaus.jackson.node.ContainerNode, org.codehaus.jackson.node.BaseJsonNode, org.codehaus.jackson.JsonNode
    public ObjectNode findParent(String fieldName) {
        if (this._children != null) {
            for (JsonNode node : this._children) {
                JsonNode parent = node.findParent(fieldName);
                if (parent != null) {
                    return (ObjectNode) parent;
                }
            }
        }
        return null;
    }

    @Override // org.codehaus.jackson.node.ContainerNode, org.codehaus.jackson.node.BaseJsonNode, org.codehaus.jackson.JsonNode
    public List<JsonNode> findParents(String fieldName, List<JsonNode> foundSoFar) {
        if (this._children != null) {
            for (JsonNode node : this._children) {
                foundSoFar = node.findParents(fieldName, foundSoFar);
            }
        }
        return foundSoFar;
    }

    public JsonNode set(int index, JsonNode value) {
        if (value == null) {
            value = nullNode();
        }
        return _set(index, value);
    }

    public void add(JsonNode value) {
        if (value == null) {
            value = nullNode();
        }
        _add(value);
    }

    public JsonNode addAll(ArrayNode other) {
        int len = other.size();
        if (len > 0) {
            if (this._children == null) {
                this._children = new ArrayList<>(len + 2);
            }
            other.addContentsTo(this._children);
        }
        return this;
    }

    public JsonNode addAll(Collection<JsonNode> nodes) {
        int len = nodes.size();
        if (len > 0) {
            if (this._children == null) {
                this._children = new ArrayList<>(nodes);
            } else {
                this._children.addAll(nodes);
            }
        }
        return this;
    }

    public void insert(int index, JsonNode value) {
        if (value == null) {
            value = nullNode();
        }
        _insert(index, value);
    }

    public JsonNode remove(int index) {
        if (index < 0 || this._children == null || index >= this._children.size()) {
            return null;
        }
        return this._children.remove(index);
    }

    @Override // org.codehaus.jackson.node.ContainerNode
    public ArrayNode removeAll() {
        this._children = null;
        return this;
    }

    public ArrayNode addArray() {
        ArrayNode n = arrayNode();
        _add(n);
        return n;
    }

    public ObjectNode addObject() {
        ObjectNode n = objectNode();
        _add(n);
        return n;
    }

    public void addPOJO(Object value) {
        if (value == null) {
            addNull();
        } else {
            _add(POJONode(value));
        }
    }

    public void addNull() {
        _add(nullNode());
    }

    public void add(int v) {
        _add(numberNode(v));
    }

    public void add(long v) {
        _add(numberNode(v));
    }

    public void add(float v) {
        _add(numberNode(v));
    }

    public void add(double v) {
        _add(numberNode(v));
    }

    public void add(BigDecimal v) {
        if (v == null) {
            addNull();
        } else {
            _add(numberNode(v));
        }
    }

    public void add(String v) {
        if (v == null) {
            addNull();
        } else {
            _add(textNode(v));
        }
    }

    public void add(boolean v) {
        _add(booleanNode(v));
    }

    public void add(byte[] v) {
        if (v == null) {
            addNull();
        } else {
            _add(binaryNode(v));
        }
    }

    public ArrayNode insertArray(int index) {
        ArrayNode n = arrayNode();
        _insert(index, n);
        return n;
    }

    public ObjectNode insertObject(int index) {
        ObjectNode n = objectNode();
        _insert(index, n);
        return n;
    }

    public void insertPOJO(int index, Object value) {
        if (value == null) {
            insertNull(index);
        } else {
            _insert(index, POJONode(value));
        }
    }

    public void insertNull(int index) {
        _insert(index, nullNode());
    }

    public void insert(int index, int v) {
        _insert(index, numberNode(v));
    }

    public void insert(int index, long v) {
        _insert(index, numberNode(v));
    }

    public void insert(int index, float v) {
        _insert(index, numberNode(v));
    }

    public void insert(int index, double v) {
        _insert(index, numberNode(v));
    }

    public void insert(int index, BigDecimal v) {
        if (v == null) {
            insertNull(index);
        } else {
            _insert(index, numberNode(v));
        }
    }

    public void insert(int index, String v) {
        if (v == null) {
            insertNull(index);
        } else {
            _insert(index, textNode(v));
        }
    }

    public void insert(int index, boolean v) {
        _insert(index, booleanNode(v));
    }

    public void insert(int index, byte[] v) {
        if (v == null) {
            insertNull(index);
        } else {
            _insert(index, binaryNode(v));
        }
    }

    protected void addContentsTo(List<JsonNode> dst) {
        if (this._children != null) {
            for (JsonNode n : this._children) {
                dst.add(n);
            }
        }
    }

    @Override // org.codehaus.jackson.JsonNode
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o != null && o.getClass() == getClass()) {
            ArrayNode other = (ArrayNode) o;
            if (this._children == null || this._children.size() == 0) {
                return other.size() == 0;
            }
            return other._sameChildren(this._children);
        }
        return false;
    }

    public int hashCode() {
        if (this._children == null) {
            return 1;
        }
        int hash = this._children.size();
        for (JsonNode n : this._children) {
            if (n != null) {
                hash ^= n.hashCode();
            }
        }
        return hash;
    }

    @Override // org.codehaus.jackson.JsonNode
    public String toString() {
        StringBuilder sb = new StringBuilder((size() << 4) + 16);
        sb.append('[');
        if (this._children != null) {
            int len = this._children.size();
            for (int i = 0; i < len; i++) {
                if (i > 0) {
                    sb.append(',');
                }
                sb.append(this._children.get(i).toString());
            }
        }
        sb.append(']');
        return sb.toString();
    }

    public JsonNode _set(int index, JsonNode value) {
        if (this._children == null || index < 0 || index >= this._children.size()) {
            throw new IndexOutOfBoundsException("Illegal index " + index + ", array size " + size());
        }
        return this._children.set(index, value);
    }

    private void _add(JsonNode node) {
        if (this._children == null) {
            this._children = new ArrayList<>();
        }
        this._children.add(node);
    }

    private void _insert(int index, JsonNode node) {
        if (this._children == null) {
            this._children = new ArrayList<>();
            this._children.add(node);
        } else if (index < 0) {
            this._children.add(0, node);
        } else if (index >= this._children.size()) {
            this._children.add(node);
        } else {
            this._children.add(index, node);
        }
    }

    private boolean _sameChildren(ArrayList<JsonNode> otherChildren) {
        int len = otherChildren.size();
        if (size() != len) {
            return false;
        }
        for (int i = 0; i < len; i++) {
            if (!this._children.get(i).equals(otherChildren.get(i))) {
                return false;
            }
        }
        return true;
    }
}
