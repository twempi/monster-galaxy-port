package com.sessionm.integralblue.httpresponsecache.compat.libcore.io;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: classes.dex */
public final class StructLinger {
    public final int l_linger;
    public final int l_onoff;

    public StructLinger(int i, int i2) {
        this.l_onoff = i;
        this.l_linger = i2;
    }

    public boolean isOn() {
        return this.l_onoff != 0;
    }

    public String toString() {
        return "StructLinger[l_onoff=" + this.l_onoff + ",l_linger=" + this.l_linger + "]";
    }
}
