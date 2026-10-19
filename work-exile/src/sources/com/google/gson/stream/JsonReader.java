package com.google.gson.stream;

import com.badlogic.gdx.Input;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class JsonReader implements Closeable {
    private static final char[] NON_EXECUTE_PREFIX = ")]}'\n".toCharArray();
    private boolean hasToken;
    private final Reader in;
    private String name;
    private boolean skipping;
    private JsonToken token;
    private String value;
    private boolean lenient = false;
    private final char[] buffer = new char[1024];
    private int pos = 0;
    private int limit = 0;
    private final List<JsonScope> stack = new ArrayList();

    public JsonReader(Reader in) {
        push(JsonScope.EMPTY_DOCUMENT);
        this.skipping = false;
        if (in == null) {
            throw new NullPointerException("in == null");
        }
        this.in = in;
    }

    public void setLenient(boolean lenient) {
        this.lenient = lenient;
    }

    public boolean isLenient() {
        return this.lenient;
    }

    public void beginArray() throws IOException {
        expect(JsonToken.BEGIN_ARRAY);
    }

    public void endArray() throws IOException {
        expect(JsonToken.END_ARRAY);
    }

    public void beginObject() throws IOException {
        expect(JsonToken.BEGIN_OBJECT);
    }

    public void endObject() throws IOException {
        expect(JsonToken.END_OBJECT);
    }

    private void expect(JsonToken expected) throws IOException {
        quickPeek();
        if (this.token != expected) {
            throw new IllegalStateException("Expected " + expected + " but was " + peek());
        }
        advance();
    }

    public boolean hasNext() throws IOException {
        quickPeek();
        return (this.token == JsonToken.END_OBJECT || this.token == JsonToken.END_ARRAY) ? false : true;
    }

    public JsonToken peek() throws IOException {
        quickPeek();
        if (this.token == null) {
            decodeLiteral();
        }
        return this.token;
    }

    private JsonToken quickPeek() throws IOException {
        if (this.hasToken) {
            return this.token;
        }
        switch (peekStack()) {
            case EMPTY_DOCUMENT:
                if (this.lenient) {
                    consumeNonExecutePrefix();
                }
                replaceTop(JsonScope.NONEMPTY_DOCUMENT);
                JsonToken firstToken = nextValue();
                if (!this.lenient && firstToken != JsonToken.BEGIN_ARRAY && firstToken != JsonToken.BEGIN_OBJECT) {
                    syntaxError("Expected JSON document to start with '[' or '{'");
                    return firstToken;
                }
                return firstToken;
            case EMPTY_ARRAY:
                return nextInArray(true);
            case NONEMPTY_ARRAY:
                return nextInArray(false);
            case EMPTY_OBJECT:
                return nextInObject(true);
            case DANGLING_NAME:
                return objectValue();
            case NONEMPTY_OBJECT:
                return nextInObject(false);
            case NONEMPTY_DOCUMENT:
                try {
                    JsonToken token = nextValue();
                    if (this.lenient) {
                        return token;
                    }
                    throw syntaxError("Expected EOF");
                } catch (EOFException e) {
                    this.hasToken = true;
                    JsonToken jsonToken = JsonToken.END_DOCUMENT;
                    this.token = jsonToken;
                    return jsonToken;
                }
            case CLOSED:
                throw new IllegalStateException("JsonReader is closed");
            default:
                throw new AssertionError();
        }
    }

    private void consumeNonExecutePrefix() throws IOException {
        nextNonWhitespace();
        this.pos--;
        if (this.pos + NON_EXECUTE_PREFIX.length <= this.limit || fillBuffer(NON_EXECUTE_PREFIX.length)) {
            for (int i = 0; i < NON_EXECUTE_PREFIX.length; i++) {
                if (this.buffer[this.pos + i] != NON_EXECUTE_PREFIX[i]) {
                    return;
                }
            }
            this.pos += NON_EXECUTE_PREFIX.length;
        }
    }

    private JsonToken advance() throws IOException {
        quickPeek();
        JsonToken result = this.token;
        this.hasToken = false;
        this.token = null;
        this.value = null;
        this.name = null;
        return result;
    }

    public String nextName() throws IOException {
        quickPeek();
        if (this.token != JsonToken.NAME) {
            throw new IllegalStateException("Expected a name but was " + peek());
        }
        String result = this.name;
        advance();
        return result;
    }

    public String nextString() throws IOException {
        peek();
        if (this.value == null || (this.token != JsonToken.STRING && this.token != JsonToken.NUMBER)) {
            throw new IllegalStateException("Expected a string but was " + peek());
        }
        String result = this.value;
        advance();
        return result;
    }

    public boolean nextBoolean() throws IOException {
        boolean result;
        quickPeek();
        if (this.value == null || this.token == JsonToken.STRING) {
            throw new IllegalStateException("Expected a boolean but was " + peek());
        }
        if (this.value.equalsIgnoreCase("true")) {
            result = true;
        } else if (this.value.equalsIgnoreCase("false")) {
            result = false;
        } else {
            throw new IllegalStateException("Not a boolean: " + this.value);
        }
        advance();
        return result;
    }

    public void nextNull() throws IOException {
        quickPeek();
        if (this.value == null || this.token == JsonToken.STRING) {
            throw new IllegalStateException("Expected null but was " + peek());
        }
        if (!this.value.equalsIgnoreCase("null")) {
            throw new IllegalStateException("Not a null: " + this.value);
        }
        advance();
    }

    public double nextDouble() throws IOException {
        quickPeek();
        if (this.value == null) {
            throw new IllegalStateException("Expected a double but was " + peek());
        }
        double result = Double.parseDouble(this.value);
        if (result >= 1.0d && this.value.startsWith("0")) {
            throw new NumberFormatException("JSON forbids octal prefixes: " + this.value);
        }
        if (!this.lenient && (Double.isNaN(result) || Double.isInfinite(result))) {
            throw new NumberFormatException("JSON forbids NaN and infinities: " + this.value);
        }
        advance();
        return result;
    }

    public long nextLong() throws IOException {
        long result;
        quickPeek();
        if (this.value == null) {
            throw new IllegalStateException("Expected a long but was " + peek());
        }
        try {
            result = Long.parseLong(this.value);
        } catch (NumberFormatException e) {
            double asDouble = Double.parseDouble(this.value);
            result = (long) asDouble;
            if (result != asDouble) {
                throw new NumberFormatException(this.value);
            }
        }
        if (result >= 1 && this.value.startsWith("0")) {
            throw new NumberFormatException("JSON forbids octal prefixes: " + this.value);
        }
        advance();
        return result;
    }

    public int nextInt() throws IOException {
        int result;
        quickPeek();
        if (this.value == null) {
            throw new IllegalStateException("Expected an int but was " + peek());
        }
        try {
            result = Integer.parseInt(this.value);
        } catch (NumberFormatException e) {
            double asDouble = Double.parseDouble(this.value);
            result = (int) asDouble;
            if (result != asDouble) {
                throw new NumberFormatException(this.value);
            }
        }
        if (result >= 1 && this.value.startsWith("0")) {
            throw new NumberFormatException("JSON forbids octal prefixes: " + this.value);
        }
        advance();
        return result;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.hasToken = false;
        this.value = null;
        this.token = null;
        this.stack.clear();
        this.stack.add(JsonScope.CLOSED);
        this.in.close();
    }

    public void skipValue() throws IOException {
        this.skipping = true;
        int count = 0;
        do {
            try {
                JsonToken token = advance();
                if (token == JsonToken.BEGIN_ARRAY || token == JsonToken.BEGIN_OBJECT) {
                    count++;
                } else if (token == JsonToken.END_ARRAY || token == JsonToken.END_OBJECT) {
                    count--;
                }
            } finally {
                this.skipping = false;
            }
        } while (count != 0);
    }

    private JsonScope peekStack() {
        return this.stack.get(this.stack.size() - 1);
    }

    private JsonScope pop() {
        return this.stack.remove(this.stack.size() - 1);
    }

    private void push(JsonScope newTop) {
        this.stack.add(newTop);
    }

    private void replaceTop(JsonScope newTop) {
        this.stack.set(this.stack.size() - 1, newTop);
    }

    private JsonToken nextInArray(boolean firstElement) throws IOException {
        if (firstElement) {
            replaceTop(JsonScope.NONEMPTY_ARRAY);
        } else {
            switch (nextNonWhitespace()) {
                case Input.Keys.P /* 44 */:
                    break;
                case Input.Keys.SHIFT_LEFT /* 59 */:
                    checkLenient();
                    break;
                case 93:
                    pop();
                    this.hasToken = true;
                    JsonToken jsonToken = JsonToken.END_ARRAY;
                    this.token = jsonToken;
                    return jsonToken;
                default:
                    throw syntaxError("Unterminated array");
            }
        }
        switch (nextNonWhitespace()) {
            case Input.Keys.P /* 44 */:
            case Input.Keys.SHIFT_LEFT /* 59 */:
                break;
            case 93:
                if (firstElement) {
                    pop();
                    this.hasToken = true;
                    JsonToken jsonToken2 = JsonToken.END_ARRAY;
                    this.token = jsonToken2;
                    return jsonToken2;
                }
                break;
            default:
                this.pos--;
                return nextValue();
        }
        checkLenient();
        this.pos--;
        this.hasToken = true;
        this.value = "null";
        JsonToken jsonToken3 = JsonToken.NULL;
        this.token = jsonToken3;
        return jsonToken3;
    }

    private JsonToken nextInObject(boolean firstElement) throws IOException {
        if (firstElement) {
            switch (nextNonWhitespace()) {
                case Opcodes.LUSHR /* 125 */:
                    pop();
                    this.hasToken = true;
                    JsonToken jsonToken = JsonToken.END_OBJECT;
                    this.token = jsonToken;
                    return jsonToken;
                default:
                    this.pos--;
                    break;
            }
        } else {
            switch (nextNonWhitespace()) {
                case Input.Keys.P /* 44 */:
                case Input.Keys.SHIFT_LEFT /* 59 */:
                    break;
                case Opcodes.LUSHR /* 125 */:
                    pop();
                    this.hasToken = true;
                    JsonToken jsonToken2 = JsonToken.END_OBJECT;
                    this.token = jsonToken2;
                    return jsonToken2;
                default:
                    throw syntaxError("Unterminated object");
            }
        }
        int quote = nextNonWhitespace();
        switch (quote) {
            case Input.Keys.K /* 39 */:
                checkLenient();
            case Input.Keys.F /* 34 */:
                this.name = nextString((char) quote);
                replaceTop(JsonScope.DANGLING_NAME);
                this.hasToken = true;
                JsonToken jsonToken3 = JsonToken.NAME;
                this.token = jsonToken3;
                return jsonToken3;
            default:
                checkLenient();
                this.pos--;
                this.name = nextLiteral();
                if (this.name.length() == 0) {
                    throw syntaxError("Expected name");
                }
                replaceTop(JsonScope.DANGLING_NAME);
                this.hasToken = true;
                JsonToken jsonToken4 = JsonToken.NAME;
                this.token = jsonToken4;
                return jsonToken4;
        }
    }

    private JsonToken objectValue() throws IOException {
        switch (nextNonWhitespace()) {
            case 58:
                break;
            case Input.Keys.SHIFT_LEFT /* 59 */:
            case Input.Keys.SHIFT_RIGHT /* 60 */:
            default:
                throw syntaxError("Expected ':'");
            case Input.Keys.TAB /* 61 */:
                checkLenient();
                if ((this.pos < this.limit || fillBuffer(1)) && this.buffer[this.pos] == '>') {
                    this.pos++;
                }
                break;
        }
        replaceTop(JsonScope.NONEMPTY_OBJECT);
        return nextValue();
    }

    private JsonToken nextValue() throws IOException {
        int c = nextNonWhitespace();
        switch (c) {
            case Input.Keys.F /* 34 */:
                break;
            case Input.Keys.K /* 39 */:
                checkLenient();
                break;
            case 91:
                push(JsonScope.EMPTY_ARRAY);
                this.hasToken = true;
                JsonToken jsonToken = JsonToken.BEGIN_ARRAY;
                this.token = jsonToken;
                return jsonToken;
            case Opcodes.LSHR /* 123 */:
                push(JsonScope.EMPTY_OBJECT);
                this.hasToken = true;
                JsonToken jsonToken2 = JsonToken.BEGIN_OBJECT;
                this.token = jsonToken2;
                return jsonToken2;
            default:
                this.pos--;
                return readLiteral();
        }
        this.value = nextString((char) c);
        this.hasToken = true;
        JsonToken jsonToken3 = JsonToken.STRING;
        this.token = jsonToken3;
        return jsonToken3;
    }

    private boolean fillBuffer(int minimum) throws IOException {
        if (this.limit != this.pos) {
            this.limit -= this.pos;
            System.arraycopy(this.buffer, this.pos, this.buffer, 0, this.limit);
        } else {
            this.limit = 0;
        }
        this.pos = 0;
        do {
            int total = this.in.read(this.buffer, this.limit, this.buffer.length - this.limit);
            if (total == -1) {
                return false;
            }
            this.limit += total;
        } while (this.limit < minimum);
        return true;
    }

    private int nextNonWhitespace() throws IOException {
        while (true) {
            if (this.pos < this.limit || fillBuffer(1)) {
                char[] cArr = this.buffer;
                int i = this.pos;
                this.pos = i + 1;
                char c = cArr[i];
                switch (c) {
                    case '\t':
                    case '\n':
                    case '\r':
                    case ' ':
                        break;
                    case Input.Keys.G /* 35 */:
                        checkLenient();
                        skipToEndOfLine();
                        break;
                    case '/':
                        if (this.pos != this.limit || fillBuffer(1)) {
                            checkLenient();
                            char peek = this.buffer[this.pos];
                            switch (peek) {
                                case Input.Keys.N /* 42 */:
                                    this.pos++;
                                    if (!skipTo("*/")) {
                                        throw syntaxError("Unterminated comment");
                                    }
                                    this.pos += 2;
                                    break;
                                    break;
                                case '/':
                                    this.pos++;
                                    skipToEndOfLine();
                                    break;
                                default:
                                    break;
                            }
                        }
                        return c;
                    default:
                        return c;
                }
            } else {
                throw new EOFException("End of input");
            }
        }
    }

    private void checkLenient() throws IOException {
        if (!this.lenient) {
            throw syntaxError("Use JsonReader.setLenient(true) to accept malformed JSON");
        }
    }

    private void skipToEndOfLine() throws IOException {
        char c;
        do {
            if (this.pos < this.limit || fillBuffer(1)) {
                char[] cArr = this.buffer;
                int i = this.pos;
                this.pos = i + 1;
                c = cArr[i];
                if (c == '\r') {
                    return;
                }
            } else {
                return;
            }
        } while (c != '\n');
    }

    private boolean skipTo(String toFind) throws IOException {
        while (true) {
            if (this.pos + toFind.length() >= this.limit && !fillBuffer(toFind.length())) {
                return false;
            }
            for (int c = 0; c < toFind.length(); c++) {
                if (this.buffer[this.pos + c] != toFind.charAt(c)) {
                    this.pos++;
                }
            }
            return true;
        }
    }

    private String nextString(char quote) throws IOException {
        StringBuilder builder = null;
        do {
            int start = this.pos;
            while (this.pos < this.limit) {
                char[] cArr = this.buffer;
                int i = this.pos;
                this.pos = i + 1;
                char c = cArr[i];
                if (c == quote) {
                    if (this.skipping) {
                        return "skipped!";
                    }
                    if (builder == null) {
                        return new String(this.buffer, start, (this.pos - start) - 1);
                    }
                    builder.append(this.buffer, start, (this.pos - start) - 1);
                    return builder.toString();
                }
                if (c == '\\') {
                    if (builder == null) {
                        builder = new StringBuilder();
                    }
                    builder.append(this.buffer, start, (this.pos - start) - 1);
                    builder.append(readEscapeCharacter());
                    start = this.pos;
                }
            }
            if (builder == null) {
                builder = new StringBuilder();
            }
            builder.append(this.buffer, start, this.pos - start);
        } while (fillBuffer(1));
        throw syntaxError("Unterminated string");
    }

    private String nextLiteral() throws IOException {
        StringBuilder builder = null;
        do {
            int start = this.pos;
            while (true) {
                if (this.pos < this.limit) {
                    char[] cArr = this.buffer;
                    int i = this.pos;
                    this.pos = i + 1;
                    switch (cArr[i]) {
                        case '\t':
                        case '\n':
                        case '\f':
                        case '\r':
                        case ' ':
                        case Input.Keys.P /* 44 */:
                        case ':':
                        case '[':
                        case ']':
                        case Opcodes.LSHR /* 123 */:
                        case Opcodes.LUSHR /* 125 */:
                            break;
                        case Input.Keys.G /* 35 */:
                        case '/':
                        case Input.Keys.SHIFT_LEFT /* 59 */:
                        case Input.Keys.TAB /* 61 */:
                        case '\\':
                            checkLenient();
                            break;
                        default:
                            break;
                    }
                    this.pos--;
                    if (this.skipping) {
                        return "skipped!";
                    }
                    if (builder == null) {
                        return new String(this.buffer, start, this.pos - start);
                    }
                    builder.append(this.buffer, start, this.pos - start);
                    return builder.toString();
                }
                if (builder == null) {
                    builder = new StringBuilder();
                }
                builder.append(this.buffer, start, this.pos - start);
            }
        } while (fillBuffer(1));
        return builder.toString();
    }

    public String toString() {
        return getClass().getSimpleName() + " near " + ((Object) getSnippet());
    }

    private char readEscapeCharacter() throws IOException {
        if (this.pos == this.limit && !fillBuffer(1)) {
            throw syntaxError("Unterminated escape sequence");
        }
        char[] cArr = this.buffer;
        int i = this.pos;
        this.pos = i + 1;
        char escaped = cArr[i];
        switch (escaped) {
            case 'b':
                return '\b';
            case 'f':
                return '\f';
            case 'n':
                return '\n';
            case Opcodes.FREM /* 114 */:
                return '\r';
            case Opcodes.INEG /* 116 */:
                return '\t';
            case Opcodes.LNEG /* 117 */:
                if (this.pos + 4 > this.limit && !fillBuffer(4)) {
                    throw syntaxError("Unterminated escape sequence");
                }
                String hex = new String(this.buffer, this.pos, 4);
                this.pos += 4;
                return (char) Integer.parseInt(hex, 16);
            default:
                return escaped;
        }
    }

    private JsonToken readLiteral() throws IOException {
        String literal = nextLiteral();
        if (literal.length() == 0) {
            throw syntaxError("Expected literal value");
        }
        this.value = literal;
        this.hasToken = true;
        this.token = null;
        return null;
    }

    private void decodeLiteral() throws IOException {
        if (this.value.equalsIgnoreCase("null")) {
            this.token = JsonToken.NULL;
            return;
        }
        if (this.value.equalsIgnoreCase("true") || this.value.equalsIgnoreCase("false")) {
            this.token = JsonToken.BOOLEAN;
            return;
        }
        try {
            Double.parseDouble(this.value);
            this.token = JsonToken.NUMBER;
        } catch (NumberFormatException e) {
            checkLenient();
            this.token = JsonToken.STRING;
        }
    }

    private IOException syntaxError(String message) throws IOException {
        throw new MalformedJsonException(message + " near " + ((Object) getSnippet()));
    }

    private CharSequence getSnippet() {
        StringBuilder snippet = new StringBuilder();
        int beforePos = Math.min(this.pos, 20);
        snippet.append(this.buffer, this.pos - beforePos, beforePos);
        int afterPos = Math.min(this.limit - this.pos, 20);
        snippet.append(this.buffer, this.pos, afterPos);
        return snippet;
    }
}
