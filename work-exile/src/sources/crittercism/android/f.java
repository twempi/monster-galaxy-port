package crittercism.android;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class f extends Exception {
    private static final long serialVersionUID = 1;
    private a a;

    public enum a {
        NO_INTERNET,
        CONN_TIMEOUT,
        UNKNOWN_HOST,
        WTF,
        FILE_NOT_FOUND
    }

    public f(String str, a aVar) {
        super(str);
        this.a = aVar;
    }
}
