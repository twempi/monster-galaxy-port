package org.codehaus.jackson.node;

import java.util.Iterator;
import java.util.Map;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonStreamContext;
import org.codehaus.jackson.JsonToken;

/* JADX INFO: loaded from: classes.dex */
abstract class NodeCursor extends JsonStreamContext {
    final NodeCursor _parent;

    public abstract boolean currentHasChildren();

    public abstract JsonNode currentNode();

    public abstract JsonToken endToken();

    @Override // org.codehaus.jackson.JsonStreamContext
    public abstract String getCurrentName();

    public abstract JsonToken nextToken();

    public abstract JsonToken nextValue();

    public NodeCursor(int contextType, NodeCursor p) {
        this._type = contextType;
        this._index = -1;
        this._parent = p;
    }

    @Override // org.codehaus.jackson.JsonStreamContext
    public final NodeCursor getParent() {
        return this._parent;
    }

    public final NodeCursor iterateChildren() {
        JsonNode n = currentNode();
        if (n == null) {
            throw new IllegalStateException("No current node");
        }
        if (n.isArray()) {
            return new Array(n, this);
        }
        if (n.isObject()) {
            return new Object(n, this);
        }
        throw new IllegalStateException("Current node of type " + n.getClass().getName());
    }

    protected static final class RootValue extends NodeCursor {
        protected boolean _done;
        JsonNode _node;

        @Override // org.codehaus.jackson.node.NodeCursor, org.codehaus.jackson.JsonStreamContext
        public /* bridge */ /* synthetic */ JsonStreamContext getParent() {
            return super.getParent();
        }

        public RootValue(JsonNode n, NodeCursor p) {
            super(0, p);
            this._done = false;
            this._node = n;
        }

        @Override // org.codehaus.jackson.node.NodeCursor, org.codehaus.jackson.JsonStreamContext
        public String getCurrentName() {
            return null;
        }

        @Override // org.codehaus.jackson.node.NodeCursor
        public JsonToken nextToken() {
            if (!this._done) {
                this._done = true;
                return this._node.asToken();
            }
            this._node = null;
            return null;
        }

        @Override // org.codehaus.jackson.node.NodeCursor
        public JsonToken nextValue() {
            return nextToken();
        }

        @Override // org.codehaus.jackson.node.NodeCursor
        public JsonToken endToken() {
            return null;
        }

        @Override // org.codehaus.jackson.node.NodeCursor
        public JsonNode currentNode() {
            return this._node;
        }

        @Override // org.codehaus.jackson.node.NodeCursor
        public boolean currentHasChildren() {
            return false;
        }
    }

    protected static final class Array extends NodeCursor {
        Iterator<JsonNode> _contents;
        JsonNode _currentNode;

        @Override // org.codehaus.jackson.node.NodeCursor, org.codehaus.jackson.JsonStreamContext
        public /* bridge */ /* synthetic */ JsonStreamContext getParent() {
            return super.getParent();
        }

        public Array(JsonNode n, NodeCursor p) {
            super(1, p);
            this._contents = n.getElements();
        }

        @Override // org.codehaus.jackson.node.NodeCursor, org.codehaus.jackson.JsonStreamContext
        public String getCurrentName() {
            return null;
        }

        @Override // org.codehaus.jackson.node.NodeCursor
        public JsonToken nextToken() {
            if (!this._contents.hasNext()) {
                this._currentNode = null;
                return null;
            }
            this._currentNode = this._contents.next();
            return this._currentNode.asToken();
        }

        @Override // org.codehaus.jackson.node.NodeCursor
        public JsonToken nextValue() {
            return nextToken();
        }

        @Override // org.codehaus.jackson.node.NodeCursor
        public JsonToken endToken() {
            return JsonToken.END_ARRAY;
        }

        @Override // org.codehaus.jackson.node.NodeCursor
        public JsonNode currentNode() {
            return this._currentNode;
        }

        @Override // org.codehaus.jackson.node.NodeCursor
        public boolean currentHasChildren() {
            return ((ContainerNode) currentNode()).size() > 0;
        }
    }

    protected static final class Object extends NodeCursor {
        Iterator<Map.Entry<String, JsonNode>> _contents;
        Map.Entry<String, JsonNode> _current;
        boolean _needEntry;

        @Override // org.codehaus.jackson.node.NodeCursor, org.codehaus.jackson.JsonStreamContext
        public /* bridge */ /* synthetic */ JsonStreamContext getParent() {
            return super.getParent();
        }

        public Object(JsonNode n, NodeCursor p) {
            super(2, p);
            this._contents = ((ObjectNode) n).getFields();
            this._needEntry = true;
        }

        @Override // org.codehaus.jackson.node.NodeCursor, org.codehaus.jackson.JsonStreamContext
        public String getCurrentName() {
            if (this._current == null) {
                return null;
            }
            return this._current.getKey();
        }

        @Override // org.codehaus.jackson.node.NodeCursor
        public JsonToken nextToken() {
            if (this._needEntry) {
                if (!this._contents.hasNext()) {
                    this._current = null;
                    return null;
                }
                this._needEntry = false;
                this._current = this._contents.next();
                return JsonToken.FIELD_NAME;
            }
            this._needEntry = true;
            return this._current.getValue().asToken();
        }

        @Override // org.codehaus.jackson.node.NodeCursor
        public JsonToken nextValue() {
            JsonToken t = nextToken();
            if (t == JsonToken.FIELD_NAME) {
                return nextToken();
            }
            return t;
        }

        @Override // org.codehaus.jackson.node.NodeCursor
        public JsonToken endToken() {
            return JsonToken.END_OBJECT;
        }

        @Override // org.codehaus.jackson.node.NodeCursor
        public JsonNode currentNode() {
            if (this._current == null) {
                return null;
            }
            return this._current.getValue();
        }

        @Override // org.codehaus.jackson.node.NodeCursor
        public boolean currentHasChildren() {
            return ((ContainerNode) currentNode()).size() > 0;
        }
    }
}
