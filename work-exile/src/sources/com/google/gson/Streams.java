package com.google.gson;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;
import java.io.EOFException;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
final class Streams {
    Streams() {
    }

    static JsonElement parse(JsonReader reader) throws JsonParseException {
        boolean isEmpty = true;
        try {
            reader.peek();
            isEmpty = false;
            return parseRecursive(reader);
        } catch (MalformedJsonException e) {
            throw new JsonSyntaxException(e);
        } catch (EOFException e2) {
            if (isEmpty) {
                return JsonNull.createJsonNull();
            }
            throw new JsonIOException(e2);
        } catch (IOException e3) {
            throw new JsonIOException(e3);
        } catch (NumberFormatException e4) {
            throw new JsonSyntaxException(e4);
        }
    }

    private static JsonElement parseRecursive(JsonReader reader) throws IOException {
        switch (reader.peek()) {
            case STRING:
                return new JsonPrimitive(reader.nextString());
            case NUMBER:
                String number = reader.nextString();
                return new JsonPrimitive(JsonPrimitive.stringToNumber(number));
            case BOOLEAN:
                return new JsonPrimitive(Boolean.valueOf(reader.nextBoolean()));
            case NULL:
                reader.nextNull();
                return JsonNull.createJsonNull();
            case BEGIN_ARRAY:
                JsonArray array = new JsonArray();
                reader.beginArray();
                while (reader.hasNext()) {
                    array.add(parseRecursive(reader));
                }
                reader.endArray();
                return array;
            case BEGIN_OBJECT:
                JsonObject object = new JsonObject();
                reader.beginObject();
                while (reader.hasNext()) {
                    object.add(reader.nextName(), parseRecursive(reader));
                }
                reader.endObject();
                return object;
            default:
                throw new IllegalArgumentException();
        }
    }

    static void write(JsonElement element, boolean serializeNulls, JsonWriter writer) throws IOException {
        if (element == null || element.isJsonNull()) {
            if (serializeNulls) {
                writer.nullValue();
                return;
            }
            return;
        }
        if (element.isJsonPrimitive()) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();
            if (primitive.isNumber()) {
                writer.value(primitive.getAsNumber());
                return;
            } else if (primitive.isBoolean()) {
                writer.value(primitive.getAsBoolean());
                return;
            } else {
                writer.value(primitive.getAsString());
                return;
            }
        }
        if (element.isJsonArray()) {
            writer.beginArray();
            for (JsonElement e : element.getAsJsonArray()) {
                if (e.isJsonNull()) {
                    writer.nullValue();
                } else {
                    write(e, serializeNulls, writer);
                }
            }
            writer.endArray();
            return;
        }
        if (element.isJsonObject()) {
            writer.beginObject();
            for (Map.Entry<String, JsonElement> e2 : element.getAsJsonObject().entrySet()) {
                JsonElement value = e2.getValue();
                if (serializeNulls || !value.isJsonNull()) {
                    writer.name(e2.getKey());
                    write(value, serializeNulls, writer);
                }
            }
            writer.endObject();
            return;
        }
        throw new IllegalArgumentException("Couldn't write " + element.getClass());
    }

    static Writer writerForAppendable(Appendable appendable) {
        return appendable instanceof Writer ? (Writer) appendable : new AppendableWriter(appendable);
    }

    private static class AppendableWriter extends Writer {
        private final Appendable appendable;
        private final CurrentWrite currentWrite;

        private AppendableWriter(Appendable appendable) {
            this.currentWrite = new CurrentWrite();
            this.appendable = appendable;
        }

        @Override // java.io.Writer
        public void write(char[] chars, int offset, int length) throws IOException {
            this.currentWrite.chars = chars;
            this.appendable.append(this.currentWrite, offset, offset + length);
        }

        @Override // java.io.Writer
        public void write(int i) throws IOException {
            this.appendable.append((char) i);
        }

        @Override // java.io.Writer, java.io.Flushable
        public void flush() {
        }

        @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
        }

        static class CurrentWrite implements CharSequence {
            char[] chars;

            CurrentWrite() {
            }

            @Override // java.lang.CharSequence
            public int length() {
                return this.chars.length;
            }

            @Override // java.lang.CharSequence
            public char charAt(int i) {
                return this.chars[i];
            }

            @Override // java.lang.CharSequence
            public CharSequence subSequence(int start, int end) {
                return new String(this.chars, start, end - start);
            }
        }
    }
}
