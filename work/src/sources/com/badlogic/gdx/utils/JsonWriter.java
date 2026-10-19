package com.badlogic.gdx.utils;

import java.io.IOException;
import java.io.Writer;
import java.util.regex.Pattern;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;

/* JADX INFO: loaded from: classes.dex */
public class JsonWriter extends Writer {
    private JsonObject current;
    private boolean named;
    Writer writer;
    private final Array<JsonObject> stack = new Array<>();
    private OutputType outputType = OutputType.json;

    public JsonWriter(Writer writer) {
        this.writer = writer;
    }

    public void setOutputType(OutputType outputType) {
        this.outputType = outputType;
    }

    public JsonWriter name(String name) throws IOException {
        if (this.current == null || this.current.array) {
            throw new IllegalStateException("Current item must be an object.");
        }
        if (!this.current.needsComma) {
            this.current.needsComma = true;
        } else {
            this.writer.write(44);
        }
        this.writer.write(this.outputType.quoteName(name));
        this.writer.write(58);
        this.named = true;
        return this;
    }

    public JsonWriter object() throws IOException {
        if (this.current != null) {
            if (this.current.array) {
                if (!this.current.needsComma) {
                    this.current.needsComma = true;
                } else {
                    this.writer.write(44);
                }
            } else {
                if (!this.named && !this.current.array) {
                    throw new IllegalStateException("Name must be set.");
                }
                this.named = false;
            }
        }
        Array<JsonObject> array = this.stack;
        JsonObject jsonObject = new JsonObject(false);
        this.current = jsonObject;
        array.add(jsonObject);
        return this;
    }

    public JsonWriter array() throws IOException {
        if (this.current != null) {
            if (this.current.array) {
                if (!this.current.needsComma) {
                    this.current.needsComma = true;
                } else {
                    this.writer.write(44);
                }
            } else {
                if (!this.named && !this.current.array) {
                    throw new IllegalStateException("Name must be set.");
                }
                this.named = false;
            }
        }
        Array<JsonObject> array = this.stack;
        JsonObject jsonObject = new JsonObject(true);
        this.current = jsonObject;
        array.add(jsonObject);
        return this;
    }

    public JsonWriter value(Object value) throws IOException {
        if (this.current != null) {
            if (this.current.array) {
                if (!this.current.needsComma) {
                    this.current.needsComma = true;
                } else {
                    this.writer.write(44);
                }
            } else {
                if (!this.named) {
                    throw new IllegalStateException("Name must be set.");
                }
                this.named = false;
            }
        }
        if (value == null || (value instanceof Number) || (value instanceof Boolean)) {
            this.writer.write(String.valueOf(value));
        } else {
            this.writer.write(this.outputType.quoteValue(value.toString()));
        }
        return this;
    }

    public JsonWriter object(String name) throws IOException {
        return name(name).object();
    }

    public JsonWriter array(String name) throws IOException {
        return name(name).array();
    }

    public JsonWriter set(String name, Object value) throws IOException {
        return name(name).value(value);
    }

    public JsonWriter pop() throws IOException {
        if (this.named) {
            throw new IllegalStateException("Expected an object, array, or value since a name was set.");
        }
        this.stack.pop().close();
        this.current = this.stack.size == 0 ? null : this.stack.peek();
        return this;
    }

    @Override // java.io.Writer
    public void write(char[] cbuf, int off, int len) throws IOException {
        this.writer.write(cbuf, off, len);
    }

    @Override // java.io.Writer, java.io.Flushable
    public void flush() throws IOException {
        this.writer.flush();
    }

    @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        while (this.stack.size > 0) {
            pop();
        }
        this.writer.close();
    }

    private class JsonObject {
        final boolean array;
        boolean needsComma;

        JsonObject(boolean array) throws IOException {
            this.array = array;
            JsonWriter.this.writer.write(array ? 91 : Opcodes.LSHR);
        }

        void close() throws IOException {
            JsonWriter.this.writer.write(this.array ? 93 : Opcodes.LUSHR);
        }
    }

    public enum OutputType {
        json,
        javascript,
        minimal;

        private static Pattern javascriptPattern = Pattern.compile("[a-zA-Z_$][a-zA-Z_$0-9]*");
        private static Pattern minimalPattern = Pattern.compile("[a-zA-Z0-9_$][^:}\\], ]*");

        public String quoteValue(String value) {
            String value2 = value.replace("\\", "\\\\");
            return (this != minimal || value2.equals("true") || value2.equals("false") || value2.equals("null") || !minimalPattern.matcher(value2).matches()) ? '\"' + value2.replace("\"", "\\\"") + '\"' : value2;
        }

        public String quoteName(String value) {
            String value2 = value.replace("\\", "\\\\");
            switch (this) {
                case minimal:
                    return !minimalPattern.matcher(value2).matches() ? '\"' + value2.replace("\"", "\\\"") + '\"' : value2;
                case javascript:
                    return !javascriptPattern.matcher(value2).matches() ? '\"' + value2.replace("\"", "\\\"") + '\"' : value2;
                default:
                    return '\"' + value2.replace("\"", "\\\"") + '\"';
            }
        }
    }
}
