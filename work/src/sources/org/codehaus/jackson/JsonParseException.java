package org.codehaus.jackson;

/* JADX INFO: loaded from: classes.dex */
public class JsonParseException extends JsonProcessingException {
    static final long serialVersionUID = 123;

    public JsonParseException(String msg, JsonLocation loc) {
        super(msg, loc);
    }

    public JsonParseException(String msg, JsonLocation loc, Throwable root) {
        super(msg, loc, root);
    }
}
