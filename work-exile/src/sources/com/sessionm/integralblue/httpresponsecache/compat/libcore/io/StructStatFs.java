package com.sessionm.integralblue.httpresponsecache.compat.libcore.io;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class StructStatFs {
    public final long f_bavail;
    public final long f_bfree;
    public final long f_blocks;
    public final long f_bsize;
    public final long f_ffree;
    public final long f_files;
    public final long f_frsize;
    public final long f_namemax;

    StructStatFs(long j, long j2, long j3, long j4, long j5, long j6, long j7, long j8) {
        this.f_bsize = j;
        this.f_blocks = j2;
        this.f_bfree = j3;
        this.f_bavail = j4;
        this.f_files = j5;
        this.f_ffree = j6;
        this.f_namemax = j7;
        this.f_frsize = j8;
    }
}
