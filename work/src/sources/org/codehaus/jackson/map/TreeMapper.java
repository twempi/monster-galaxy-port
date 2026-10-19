package org.codehaus.jackson.map;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.NullNode;

/* JADX INFO: loaded from: classes.dex */
@Deprecated
public class TreeMapper extends JsonNodeFactory {
    protected ObjectMapper _objectMapper;

    public TreeMapper() {
        this(null);
    }

    public TreeMapper(ObjectMapper m) {
        this._objectMapper = m;
    }

    public JsonFactory getJsonFactory() {
        return objectMapper().getJsonFactory();
    }

    public JsonNode readTree(JsonParser jp) throws IOException {
        JsonToken t = jp.getCurrentToken();
        if (t == null) {
            JsonToken t2 = jp.nextToken();
            if (t2 == null) {
                return null;
            }
        }
        return objectMapper().readTree(jp);
    }

    public JsonNode readTree(File src) throws IOException {
        JsonNode n = (JsonNode) objectMapper().readValue(src, JsonNode.class);
        return n == null ? NullNode.instance : n;
    }

    public JsonNode readTree(URL src) throws IOException {
        JsonNode n = (JsonNode) objectMapper().readValue(src, JsonNode.class);
        return n == null ? NullNode.instance : n;
    }

    public JsonNode readTree(InputStream src) throws IOException {
        JsonNode n = (JsonNode) objectMapper().readValue(src, JsonNode.class);
        return n == null ? NullNode.instance : n;
    }

    public JsonNode readTree(Reader src) throws IOException {
        JsonNode n = (JsonNode) objectMapper().readValue(src, JsonNode.class);
        return n == null ? NullNode.instance : n;
    }

    public JsonNode readTree(String jsonContent) throws IOException {
        JsonNode n = (JsonNode) objectMapper().readValue(jsonContent, JsonNode.class);
        return n == null ? NullNode.instance : n;
    }

    public JsonNode readTree(byte[] jsonContent) throws IOException {
        JsonNode n = (JsonNode) objectMapper().readValue(jsonContent, 0, jsonContent.length, JsonNode.class);
        return n == null ? NullNode.instance : n;
    }

    public void writeTree(JsonNode rootNode, File dst) throws IOException {
        objectMapper().writeValue(dst, rootNode);
    }

    public void writeTree(JsonNode rootNode, Writer dst) throws IOException {
        objectMapper().writeValue(dst, rootNode);
    }

    public void writeTree(JsonNode rootNode, OutputStream dst) throws IOException {
        objectMapper().writeValue(dst, rootNode);
    }

    protected synchronized ObjectMapper objectMapper() {
        if (this._objectMapper == null) {
            this._objectMapper = new ObjectMapper();
        }
        return this._objectMapper;
    }
}
