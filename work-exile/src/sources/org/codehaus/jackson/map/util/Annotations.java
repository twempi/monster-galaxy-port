package org.codehaus.jackson.map.util;

import java.lang.annotation.Annotation;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface Annotations {
    <A extends Annotation> A get(Class<A> cls);

    int size();
}
