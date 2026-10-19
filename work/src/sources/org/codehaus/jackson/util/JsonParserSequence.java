package org.codehaus.jackson.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

/* JADX INFO: loaded from: classes.dex */
public class JsonParserSequence extends JsonParserDelegate {
    protected int _nextParser;
    protected final JsonParser[] _parsers;

    protected JsonParserSequence(JsonParser[] parsers) {
        super(parsers[0]);
        this._parsers = parsers;
        this._nextParser = 1;
    }

    public static JsonParserSequence createFlattened(JsonParser first, JsonParser second) {
        if (!(first instanceof JsonParserSequence) && !(second instanceof JsonParserSequence)) {
            return new JsonParserSequence(new JsonParser[]{first, second});
        }
        ArrayList<JsonParser> p = new ArrayList<>();
        if (first instanceof JsonParserSequence) {
            ((JsonParserSequence) first).addFlattenedActiveParsers(p);
        } else {
            p.add(first);
        }
        if (second instanceof JsonParserSequence) {
            ((JsonParserSequence) second).addFlattenedActiveParsers(p);
        } else {
            p.add(second);
        }
        return new JsonParserSequence((JsonParser[]) p.toArray(new JsonParser[p.size()]));
    }

    protected void addFlattenedActiveParsers(List<JsonParser> result) {
        int len = this._parsers.length;
        for (int i = this._nextParser - 1; i < len; i++) {
            JsonParser p = this._parsers[i];
            if (p instanceof JsonParserSequence) {
                ((JsonParserSequence) p).addFlattenedActiveParsers(result);
            } else {
                result.add(p);
            }
        }
    }

    @Override // org.codehaus.jackson.util.JsonParserDelegate, org.codehaus.jackson.JsonParser, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        do {
            this.delegate.close();
        } while (switchToNext());
    }

    @Override // org.codehaus.jackson.util.JsonParserDelegate, org.codehaus.jackson.JsonParser
    public JsonToken nextToken() throws IOException {
        JsonToken t = this.delegate.nextToken();
        if (t != null) {
            return t;
        }
        while (switchToNext()) {
            JsonToken t2 = this.delegate.nextToken();
            if (t2 != null) {
                return t2;
            }
        }
        return null;
    }

    public int containedParsersCount() {
        return this._parsers.length;
    }

    protected boolean switchToNext() {
        if (this._nextParser >= this._parsers.length) {
            return false;
        }
        JsonParser[] jsonParserArr = this._parsers;
        int i = this._nextParser;
        this._nextParser = i + 1;
        this.delegate = jsonParserArr[i];
        return true;
    }
}
