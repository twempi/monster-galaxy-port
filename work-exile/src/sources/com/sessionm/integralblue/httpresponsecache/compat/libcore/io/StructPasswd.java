package com.sessionm.integralblue.httpresponsecache.compat.libcore.io;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class StructPasswd {
    public String pw_dir;
    public int pw_gid;
    public String pw_name;
    public String pw_shell;
    public int pw_uid;

    public StructPasswd(String str, int i, int i2, String str2, String str3) {
        this.pw_name = str;
        this.pw_uid = i;
        this.pw_gid = i2;
        this.pw_dir = str2;
        this.pw_shell = str3;
    }
}
