package crittercism.android;

import java.util.Vector;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public abstract class j {
    protected String a;
    protected Vector b;

    public final void a(Object obj) {
        try {
            this.b.add(obj);
        } catch (Exception e) {
        }
    }

    public final void a(Vector vector) {
        try {
            this.b.addAll(vector);
        } catch (Exception e) {
        }
    }

    public final Vector e() {
        return this.b;
    }

    public final void f() {
        try {
            this.b.clear();
        } catch (Exception e) {
        }
    }
}
