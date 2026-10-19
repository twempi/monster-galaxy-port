package org.codehaus.jackson.schema;

import com.sessionm.ui.SessionMActivity;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class JsonSchema {
    private final ObjectNode schema;

    @JsonCreator
    public JsonSchema(ObjectNode schema) {
        this.schema = schema;
    }

    @JsonValue
    public ObjectNode getSchemaNode() {
        return this.schema;
    }

    public String toString() {
        return this.schema.toString();
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o != null && (o instanceof JsonSchema)) {
            JsonSchema other = (JsonSchema) o;
            if (this.schema == null) {
                return other.schema == null;
            }
            return this.schema.equals(other.schema);
        }
        return false;
    }

    public static JsonNode getDefaultSchemaNode() {
        ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
        objectNode.put(SessionMActivity.INTENT_PARAM_TYPE, "any");
        return objectNode;
    }
}
