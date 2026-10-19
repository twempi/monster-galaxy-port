package com.badlogic.gdx.utils;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayDeque;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class JsonWriter extends Writer {
    private JsonObject current;
    private final ArrayDeque<JsonObject> stack = new ArrayDeque<>();
    Writer writer;

    public JsonWriter(Writer writer) {
        this.writer = writer;
    }

    public JsonWriter object() throws IOException {
        if (this.current != null) {
            if (!this.current.array) {
                throw new RuntimeException("Current item must be an array.");
            }
            if (!this.current.needsComma) {
                this.current.needsComma = true;
            } else {
                this.writer.write(",");
            }
        }
        ArrayDeque<JsonObject> arrayDeque = this.stack;
        JsonObject jsonObject = new JsonObject(false);
        this.current = jsonObject;
        arrayDeque.push(jsonObject);
        return this;
    }

    public JsonWriter array() throws IOException {
        if (this.current != null) {
            if (!this.current.array) {
                throw new RuntimeException("Current item must be an array.");
            }
            if (!this.current.needsComma) {
                this.current.needsComma = true;
            } else {
                this.writer.write(",");
            }
        }
        ArrayDeque<JsonObject> arrayDeque = this.stack;
        JsonObject jsonObject = new JsonObject(true);
        this.current = jsonObject;
        arrayDeque.push(jsonObject);
        return this;
    }

    public JsonWriter add(Object value) throws IOException {
        if (!this.current.array) {
            throw new RuntimeException("Current item must be an array.");
        }
        if (!this.current.needsComma) {
            this.current.needsComma = true;
        } else {
            this.writer.write(",");
        }
        if (value == null || (value instanceof Number) || (value instanceof Boolean)) {
            this.writer.write(String.valueOf(value));
        } else {
            this.writer.write("\"");
            this.writer.write(value.toString());
            this.writer.write("\"");
        }
        return this;
    }

    public JsonWriter object(String name) throws IOException {
        if (this.current == null || this.current.array) {
            throw new RuntimeException("Current item must be an object.");
        }
        if (!this.current.needsComma) {
            this.current.needsComma = true;
        } else {
            this.writer.write(",");
        }
        this.writer.write("\"");
        this.writer.write(name);
        this.writer.write("\":");
        ArrayDeque<JsonObject> arrayDeque = this.stack;
        JsonObject jsonObject = new JsonObject(false);
        this.current = jsonObject;
        arrayDeque.push(jsonObject);
        return this;
    }

    public JsonWriter array(String name) throws IOException {
        if (this.current == null || this.current.array) {
            throw new RuntimeException("Current item must be an object.");
        }
        if (!this.current.needsComma) {
            this.current.needsComma = true;
        } else {
            this.writer.write(",");
        }
        this.writer.write("\"");
        this.writer.write(name);
        this.writer.write("\":");
        ArrayDeque<JsonObject> arrayDeque = this.stack;
        JsonObject jsonObject = new JsonObject(true);
        this.current = jsonObject;
        arrayDeque.push(jsonObject);
        return this;
    }

    public JsonWriter set(String name, Object value) throws IOException {
        if (this.current == null || this.current.array) {
            throw new RuntimeException("Current item must be an object.");
        }
        if (!this.current.needsComma) {
            this.current.needsComma = true;
        } else {
            this.writer.write(",");
        }
        this.writer.write("\"");
        this.writer.write(name);
        if (value == null || (value instanceof Number) || (value instanceof Boolean)) {
            this.writer.write("\":");
            this.writer.write(String.valueOf(value));
        } else {
            this.writer.write("\":\"");
            this.writer.write(value.toString().replace("\\", "\\\\"));
            this.writer.write("\"");
        }
        return this;
    }

    public JsonWriter pop() throws IOException {
        this.stack.pop().close();
        this.current = this.stack.peek();
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
        while (!this.stack.isEmpty()) {
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
}
