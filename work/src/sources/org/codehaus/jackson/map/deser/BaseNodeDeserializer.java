package org.codehaus.jackson.map.deser;

import java.io.IOException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;

/* JADX INFO: compiled from: JsonNodeDeserializer.java */
/* JADX INFO: loaded from: classes.dex */
abstract class BaseNodeDeserializer<N extends JsonNode> extends StdDeserializer<N> {
    public BaseNodeDeserializer(Class<N> nodeClass) {
        super((Class<?>) nodeClass);
    }

    @Override // org.codehaus.jackson.map.deser.StdDeserializer, org.codehaus.jackson.map.JsonDeserializer
    public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
        return typeDeserializer.deserializeTypedFromAny(jp, ctxt);
    }

    protected void _reportProblem(JsonParser jp, String msg) throws JsonMappingException {
        throw new JsonMappingException(msg, jp.getTokenLocation());
    }

    protected void _handleDuplicateField(String fieldName, ObjectNode objectNode, JsonNode oldValue, JsonNode newValue) throws JsonProcessingException {
    }

    protected final ObjectNode deserializeObject(JsonParser jp, DeserializationContext ctxt) throws IOException {
        ObjectNode node = ctxt.getNodeFactory().objectNode();
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.START_OBJECT) {
            t = jp.nextToken();
        }
        while (t == JsonToken.FIELD_NAME) {
            String fieldName = jp.getCurrentName();
            jp.nextToken();
            JsonNode value = deserializeAny(jp, ctxt);
            JsonNode old = node.put(fieldName, value);
            if (old != null) {
                _handleDuplicateField(fieldName, node, old, value);
            }
            t = jp.nextToken();
        }
        return node;
    }

    protected final ArrayNode deserializeArray(JsonParser jp, DeserializationContext ctxt) throws IOException {
        ArrayNode node = ctxt.getNodeFactory().arrayNode();
        while (jp.nextToken() != JsonToken.END_ARRAY) {
            node.add(deserializeAny(jp, ctxt));
        }
        return node;
    }

    protected final JsonNode deserializeAny(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNodeFactory nodeFactory = ctxt.getNodeFactory();
        switch (jp.getCurrentToken()) {
            case START_OBJECT:
            case FIELD_NAME:
                return deserializeObject(jp, ctxt);
            case START_ARRAY:
                return deserializeArray(jp, ctxt);
            case VALUE_STRING:
                return nodeFactory.textNode(jp.getText());
            case VALUE_NUMBER_INT:
                JsonParser.NumberType nt = jp.getNumberType();
                if (nt == JsonParser.NumberType.BIG_INTEGER || ctxt.isEnabled(DeserializationConfig.Feature.USE_BIG_INTEGER_FOR_INTS)) {
                    return nodeFactory.numberNode(jp.getBigIntegerValue());
                }
                if (nt == JsonParser.NumberType.INT) {
                    return nodeFactory.numberNode(jp.getIntValue());
                }
                return nodeFactory.numberNode(jp.getLongValue());
            case VALUE_NUMBER_FLOAT:
                if (jp.getNumberType() == JsonParser.NumberType.BIG_DECIMAL || ctxt.isEnabled(DeserializationConfig.Feature.USE_BIG_DECIMAL_FOR_FLOATS)) {
                    return nodeFactory.numberNode(jp.getDecimalValue());
                }
                return nodeFactory.numberNode(jp.getDoubleValue());
            case VALUE_TRUE:
                return nodeFactory.booleanNode(true);
            case VALUE_FALSE:
                return nodeFactory.booleanNode(false);
            case VALUE_NULL:
                return nodeFactory.nullNode();
            default:
                throw ctxt.mappingException(getValueClass());
        }
    }
}
