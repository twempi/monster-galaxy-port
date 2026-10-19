package org.codehaus.jackson.map.deser;

import java.io.IOException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class JsonNodeDeserializer extends BaseNodeDeserializer<JsonNode> {

    @Deprecated
    public static final JsonNodeDeserializer instance = new JsonNodeDeserializer();

    @Override // org.codehaus.jackson.map.deser.BaseNodeDeserializer, org.codehaus.jackson.map.deser.StdDeserializer, org.codehaus.jackson.map.JsonDeserializer
    public /* bridge */ /* synthetic */ Object deserializeWithType(JsonParser x0, DeserializationContext x1, TypeDeserializer x2) throws IOException {
        return super.deserializeWithType(x0, x1, x2);
    }

    protected JsonNodeDeserializer() {
        super(JsonNode.class);
    }

    public static JsonDeserializer<? extends JsonNode> getDeserializer(Class<?> nodeClass) {
        if (nodeClass == ObjectNode.class) {
            return ObjectDeserializer.getInstance();
        }
        if (nodeClass == ArrayNode.class) {
            return ArrayDeserializer.getInstance();
        }
        return instance;
    }

    @Override // org.codehaus.jackson.map.JsonDeserializer
    public JsonNode deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        return deserializeAny(jp, ctxt);
    }

    static final class ObjectDeserializer extends BaseNodeDeserializer<ObjectNode> {
        protected static final ObjectDeserializer _instance = new ObjectDeserializer();

        protected ObjectDeserializer() {
            super(ObjectNode.class);
        }

        public static ObjectDeserializer getInstance() {
            return _instance;
        }

        @Override // org.codehaus.jackson.map.JsonDeserializer
        public ObjectNode deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            if (jp.getCurrentToken() == JsonToken.START_OBJECT) {
                jp.nextToken();
                return deserializeObject(jp, ctxt);
            }
            if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {
                return deserializeObject(jp, ctxt);
            }
            throw ctxt.mappingException(ObjectNode.class);
        }
    }

    static final class ArrayDeserializer extends BaseNodeDeserializer<ArrayNode> {
        protected static final ArrayDeserializer _instance = new ArrayDeserializer();

        protected ArrayDeserializer() {
            super(ArrayNode.class);
        }

        public static ArrayDeserializer getInstance() {
            return _instance;
        }

        @Override // org.codehaus.jackson.map.JsonDeserializer
        public ArrayNode deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            if (jp.isExpectedStartArrayToken()) {
                return deserializeArray(jp, ctxt);
            }
            throw ctxt.mappingException(ArrayNode.class);
        }
    }
}
