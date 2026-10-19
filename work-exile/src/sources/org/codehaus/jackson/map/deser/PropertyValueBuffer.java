package org.codehaus.jackson.map.deser;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class PropertyValueBuffer {
    private PropertyValue _buffered;
    final DeserializationContext _context;
    final Object[] _creatorParameters;
    private int _paramsNeeded;
    final JsonParser _parser;

    public PropertyValueBuffer(JsonParser jp, DeserializationContext ctxt, int paramCount) {
        this._parser = jp;
        this._context = ctxt;
        this._paramsNeeded = paramCount;
        this._creatorParameters = new Object[paramCount];
    }

    protected final Object[] getParameters(Object[] defaults) {
        Object value;
        if (defaults != null) {
            int len = this._creatorParameters.length;
            for (int i = 0; i < len; i++) {
                if (this._creatorParameters[i] == null && (value = defaults[i]) != null) {
                    this._creatorParameters[i] = value;
                }
            }
        }
        return this._creatorParameters;
    }

    protected PropertyValue buffered() {
        return this._buffered;
    }

    public boolean assignParameter(int index, Object value) {
        this._creatorParameters[index] = value;
        int i = this._paramsNeeded - 1;
        this._paramsNeeded = i;
        return i <= 0;
    }

    public void bufferProperty(SettableBeanProperty prop, Object value) {
        this._buffered = new PropertyValue.Regular(this._buffered, value, prop);
    }

    public void bufferAnyProperty(SettableAnyProperty prop, String propName, Object value) {
        this._buffered = new PropertyValue.Any(this._buffered, value, prop, propName);
    }

    public void bufferMapProperty(Object key, Object value) {
        this._buffered = new PropertyValue.Map(this._buffered, value, key);
    }
}
