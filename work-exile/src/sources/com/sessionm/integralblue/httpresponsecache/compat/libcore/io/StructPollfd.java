package com.sessionm.integralblue.httpresponsecache.compat.libcore.io;

import java.io.FileDescriptor;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class StructPollfd {
    public short events;
    public FileDescriptor fd;
    public short revents;
    public Object userData;

    public String toString() {
        return "StructPollfd[fd=" + this.fd + ",events=" + ((int) this.events) + ",revents=" + ((int) this.revents) + "]";
    }
}
