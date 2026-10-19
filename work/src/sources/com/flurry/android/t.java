package com.flurry.android;

import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
final class t implements Runnable {
    private /* synthetic */ Map a;
    private /* synthetic */ InstallReceiver b;

    t(InstallReceiver installReceiver, Map map) {
        this.b = installReceiver;
        this.a = map;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v0 */
    /* JADX WARN: Type inference failed for: r2v1 */
    /* JADX WARN: Type inference failed for: r2v10 */
    /* JADX WARN: Type inference failed for: r2v12 */
    /* JADX WARN: Type inference failed for: r2v13 */
    /* JADX WARN: Type inference failed for: r2v2 */
    /* JADX WARN: Type inference failed for: r2v3, types: [java.io.Closeable] */
    /* JADX WARN: Type inference failed for: r2v4, types: [java.io.Closeable] */
    /* JADX WARN: Type inference failed for: r2v5 */
    /* JADX WARN: Type inference failed for: r2v6 */
    /* JADX WARN: Type inference failed for: r2v9 */
    @Override // java.lang.Runnable
    public final void run() throws Throwable {
        ?? r2 = 0;
        r2 = 0;
        Map.Entry entry = null;
        r2 = 0;
        try {
            try {
                File parentFile = this.b.b.getParentFile();
                if (parentFile.mkdirs() || parentFile.exists()) {
                    DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(this.b.b));
                    try {
                        boolean z = true;
                        for (Map.Entry entry2 : this.a.entrySet()) {
                            if (z) {
                                z = false;
                            } else {
                                dataOutputStream.writeUTF("&");
                            }
                            dataOutputStream.writeUTF((String) entry2.getKey());
                            dataOutputStream.writeUTF("=");
                            dataOutputStream.writeUTF((String) entry2.getValue());
                            entry = entry2;
                            z = z;
                        }
                        dataOutputStream.writeShort(0);
                        r.a(dataOutputStream);
                        r2 = entry;
                    } catch (Throwable th) {
                        th = th;
                        r2 = dataOutputStream;
                        ah.b("InstallReceiver", "", th);
                        r.a((Closeable) r2);
                    }
                } else {
                    ah.b("InstallReceiver", "Unable to create persistent dir: " + parentFile);
                    r.a((Closeable) null);
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Throwable th3) {
            th = th3;
        }
    }
}
