package com.google.gson;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
final class Pair<FIRST, SECOND> {
    public final FIRST first;
    public final SECOND second;

    public Pair(FIRST first, SECOND second) {
        this.first = first;
        this.second = second;
    }

    public int hashCode() {
        return ((this.first != null ? this.first.hashCode() : 0) * 17) + ((this.second != null ? this.second.hashCode() : 0) * 17);
    }

    public boolean equals(Object o) {
        if (!(o instanceof Pair)) {
            return false;
        }
        Pair<?, ?> that = (Pair) o;
        return equal(this.first, that.first) && equal(this.second, that.second);
    }

    private static boolean equal(Object a, Object b) {
        return a == b || (a != null && a.equals(b));
    }

    public String toString() {
        return String.format("{%s,%s}", this.first, this.second);
    }
}
