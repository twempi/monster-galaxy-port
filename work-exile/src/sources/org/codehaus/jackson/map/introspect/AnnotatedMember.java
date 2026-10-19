package org.codehaus.jackson.map.introspect;

import java.lang.reflect.Member;
import org.codehaus.jackson.map.util.ClassUtil;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public abstract class AnnotatedMember extends Annotated {
    public abstract Class<?> getDeclaringClass();

    public abstract Member getMember();

    protected AnnotatedMember() {
    }

    public final void fixAccess() {
        ClassUtil.checkAndFixAccess(getMember());
    }
}
