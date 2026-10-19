package org.codehaus.jackson.util;

import java.io.IOException;
import java.util.Arrays;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.PrettyPrinter;
import org.codehaus.jackson.impl.Indenter;

/* JADX INFO: loaded from: classes.dex */
public class DefaultPrettyPrinter implements PrettyPrinter {
    protected Indenter _arrayIndenter = new FixedSpaceIndenter();
    protected Indenter _objectIndenter = new Lf2SpacesIndenter();
    protected boolean _spacesInObjectEntries = true;
    protected int _nesting = 0;

    public void indentArraysWith(Indenter i) {
        if (i == null) {
            i = new NopIndenter();
        }
        this._arrayIndenter = i;
    }

    public void indentObjectsWith(Indenter i) {
        if (i == null) {
            i = new NopIndenter();
        }
        this._objectIndenter = i;
    }

    public void spacesInObjectEntries(boolean b) {
        this._spacesInObjectEntries = b;
    }

    @Override // org.codehaus.jackson.PrettyPrinter
    public void writeRootValueSeparator(JsonGenerator jg) throws IOException {
        jg.writeRaw(' ');
    }

    @Override // org.codehaus.jackson.PrettyPrinter
    public void writeStartObject(JsonGenerator jg) throws IOException {
        jg.writeRaw('{');
        if (!this._objectIndenter.isInline()) {
            this._nesting++;
        }
    }

    @Override // org.codehaus.jackson.PrettyPrinter
    public void beforeObjectEntries(JsonGenerator jg) throws IOException {
        this._objectIndenter.writeIndentation(jg, this._nesting);
    }

    @Override // org.codehaus.jackson.PrettyPrinter
    public void writeObjectFieldValueSeparator(JsonGenerator jg) throws IOException {
        if (this._spacesInObjectEntries) {
            jg.writeRaw(" : ");
        } else {
            jg.writeRaw(':');
        }
    }

    @Override // org.codehaus.jackson.PrettyPrinter
    public void writeObjectEntrySeparator(JsonGenerator jg) throws IOException {
        jg.writeRaw(',');
        this._objectIndenter.writeIndentation(jg, this._nesting);
    }

    @Override // org.codehaus.jackson.PrettyPrinter
    public void writeEndObject(JsonGenerator jg, int nrOfEntries) throws IOException {
        if (!this._objectIndenter.isInline()) {
            this._nesting--;
        }
        if (nrOfEntries > 0) {
            this._objectIndenter.writeIndentation(jg, this._nesting);
        } else {
            jg.writeRaw(' ');
        }
        jg.writeRaw('}');
    }

    @Override // org.codehaus.jackson.PrettyPrinter
    public void writeStartArray(JsonGenerator jg) throws IOException {
        if (!this._arrayIndenter.isInline()) {
            this._nesting++;
        }
        jg.writeRaw('[');
    }

    @Override // org.codehaus.jackson.PrettyPrinter
    public void beforeArrayValues(JsonGenerator jg) throws IOException {
        this._arrayIndenter.writeIndentation(jg, this._nesting);
    }

    @Override // org.codehaus.jackson.PrettyPrinter
    public void writeArrayValueSeparator(JsonGenerator jg) throws IOException {
        jg.writeRaw(',');
        this._arrayIndenter.writeIndentation(jg, this._nesting);
    }

    @Override // org.codehaus.jackson.PrettyPrinter
    public void writeEndArray(JsonGenerator jg, int nrOfValues) throws IOException {
        if (!this._arrayIndenter.isInline()) {
            this._nesting--;
        }
        if (nrOfValues > 0) {
            this._arrayIndenter.writeIndentation(jg, this._nesting);
        } else {
            jg.writeRaw(' ');
        }
        jg.writeRaw(']');
    }

    public static class NopIndenter implements Indenter {
        @Override // org.codehaus.jackson.impl.Indenter
        public void writeIndentation(JsonGenerator jg, int level) {
        }

        @Override // org.codehaus.jackson.impl.Indenter
        public boolean isInline() {
            return true;
        }
    }

    public static class FixedSpaceIndenter implements Indenter {
        @Override // org.codehaus.jackson.impl.Indenter
        public void writeIndentation(JsonGenerator jg, int level) throws IOException {
            jg.writeRaw(' ');
        }

        @Override // org.codehaus.jackson.impl.Indenter
        public boolean isInline() {
            return true;
        }
    }

    public static class Lf2SpacesIndenter implements Indenter {
        static final char[] SPACES;
        static final int SPACE_COUNT = 64;
        static final String SYSTEM_LINE_SEPARATOR;

        static {
            String lf = null;
            try {
                lf = System.getProperty("line.separator");
            } catch (Throwable th) {
            }
            if (lf == null) {
                lf = "\n";
            }
            SYSTEM_LINE_SEPARATOR = lf;
            SPACES = new char[64];
            Arrays.fill(SPACES, ' ');
        }

        @Override // org.codehaus.jackson.impl.Indenter
        public boolean isInline() {
            return false;
        }

        @Override // org.codehaus.jackson.impl.Indenter
        public void writeIndentation(JsonGenerator jg, int level) throws IOException {
            jg.writeRaw(SYSTEM_LINE_SEPARATOR);
            int level2 = level + level;
            while (level2 > 64) {
                jg.writeRaw(SPACES, 0, 64);
                level2 -= SPACES.length;
            }
            jg.writeRaw(SPACES, 0, level2);
        }
    }
}
