package org.codehaus.jackson.map.introspect;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import org.codehaus.jackson.map.util.Annotations;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class AnnotationMap implements Annotations {
    protected HashMap<Class<? extends Annotation>, Annotation> _annotations;

    @Override // org.codehaus.jackson.map.util.Annotations
    public <A extends Annotation> A get(Class<A> cls) {
        if (this._annotations == null) {
            return null;
        }
        return (A) this._annotations.get(cls);
    }

    @Override // org.codehaus.jackson.map.util.Annotations
    public int size() {
        if (this._annotations == null) {
            return 0;
        }
        return this._annotations.size();
    }

    public void addIfNotPresent(Annotation ann) {
        if (this._annotations == null || !this._annotations.containsKey(ann.annotationType())) {
            _add(ann);
        }
    }

    public void add(Annotation ann) {
        _add(ann);
    }

    public String toString() {
        return this._annotations == null ? "[null]" : this._annotations.toString();
    }

    protected final void _add(Annotation ann) {
        if (this._annotations == null) {
            this._annotations = new HashMap<>();
        }
        this._annotations.put(ann.annotationType(), ann);
    }
}
