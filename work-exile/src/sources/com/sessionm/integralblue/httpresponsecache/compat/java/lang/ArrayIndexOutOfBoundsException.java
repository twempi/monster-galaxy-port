package com.sessionm.integralblue.httpresponsecache.compat.java.lang;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class ArrayIndexOutOfBoundsException extends java.lang.ArrayIndexOutOfBoundsException {
    private static final long serialVersionUID = -5116101128118950844L;

    public ArrayIndexOutOfBoundsException() {
    }

    public ArrayIndexOutOfBoundsException(int i) {
        super("index=" + i);
    }

    public ArrayIndexOutOfBoundsException(String str) {
        super(str);
    }

    public ArrayIndexOutOfBoundsException(int i, int i2) {
        super("length=" + i + "; index=" + i2);
    }

    public ArrayIndexOutOfBoundsException(int i, int i2, int i3) {
        super("length=" + i + "; regionStart=" + i2 + "; regionLength=" + i3);
    }
}
