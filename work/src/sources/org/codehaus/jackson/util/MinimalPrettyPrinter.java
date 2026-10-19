package org.codehaus.jackson.util;

import java.io.IOException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.PrettyPrinter;

/* JADX INFO: loaded from: classes.dex */
public class MinimalPrettyPrinter implements PrettyPrinter {
    public static final String DEFAULT_ROOT_VALUE_SEPARATOR = " ";
    protected String _rootValueSeparator = " ";

    public void setRootValueSeparator(String sep) {
        this._rootValueSeparator = sep;
    }

    @Override // org.codehaus.jackson.PrettyPrinter
    public void writeRootValueSeparator(JsonGenerator jg) throws IOException {
        if (this._rootValueSeparator != null) {
            jg.writeRaw(this._rootValueSeparator);
        }
    }

    @Override // org.codehaus.jackson.PrettyPrinter
    public void writeStartObject(JsonGenerator jg) throws IOException {
        jg.writeRaw('{');
    }

    @Override // org.codehaus.jackson.PrettyPrinter
    public void beforeObjectEntries(JsonGenerator jg) throws IOException {
    }

    @Override // org.codehaus.jackson.PrettyPrinter
    public void writeObjectFieldValueSeparator(JsonGenerator jg) throws IOException {
        jg.writeRaw(':');
    }

    @Override // org.codehaus.jackson.PrettyPrinter
    public void writeObjectEntrySeparator(JsonGenerator jg) throws IOException {
        jg.writeRaw(',');
    }

    @Override // org.codehaus.jackson.PrettyPrinter
    public void writeEndObject(JsonGenerator jg, int nrOfEntries) throws IOException {
        jg.writeRaw('}');
    }

    @Override // org.codehaus.jackson.PrettyPrinter
    public void writeStartArray(JsonGenerator jg) throws IOException {
        jg.writeRaw('[');
    }

    @Override // org.codehaus.jackson.PrettyPrinter
    public void beforeArrayValues(JsonGenerator jg) throws IOException {
    }

    @Override // org.codehaus.jackson.PrettyPrinter
    public void writeArrayValueSeparator(JsonGenerator jg) throws IOException {
        jg.writeRaw(',');
    }

    @Override // org.codehaus.jackson.PrettyPrinter
    public void writeEndArray(JsonGenerator jg, int nrOfValues) throws IOException {
        jg.writeRaw(']');
    }
}
