package com.google.gson;

import java.io.IOException;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class JsonNull extends JsonElement {
    private static final JsonNull INSTANCE = new JsonNull();

    @Override // com.google.gson.JsonElement
    protected void toString(Appendable sb, Escaper escaper) throws IOException {
        sb.append("null");
    }

    public int hashCode() {
        return JsonNull.class.hashCode();
    }

    public boolean equals(Object other) {
        return this == other || (other instanceof JsonNull);
    }

    static JsonNull createJsonNull() {
        return INSTANCE;
    }
}
