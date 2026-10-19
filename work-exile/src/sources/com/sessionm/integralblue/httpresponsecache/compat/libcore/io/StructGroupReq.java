package com.sessionm.integralblue.httpresponsecache.compat.libcore.io;

import java.net.InetAddress;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class StructGroupReq {
    public final InetAddress gr_group;
    public final int gr_interface;

    public StructGroupReq(int i, InetAddress inetAddress) {
        this.gr_interface = i;
        this.gr_group = inetAddress;
    }

    public String toString() {
        return "StructGroupReq[gr_interface=" + this.gr_interface + ",gr_group=" + this.gr_group + "]";
    }
}
