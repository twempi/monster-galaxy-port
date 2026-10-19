package org.codehaus.jackson.impl;

import java.io.IOException;
import org.codehaus.jackson.JsonGenerator;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface Indenter {
    boolean isInline();

    void writeIndentation(JsonGenerator jsonGenerator, int i) throws IOException;
}
