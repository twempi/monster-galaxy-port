package com.flurry.android;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
final class i {
    private String a;
    private Map b;
    private long c;
    private boolean d;
    private long e;

    public i(String str, Map map, long j, boolean z) {
        this.a = str;
        this.b = map;
        this.c = j;
        this.d = z;
    }

    public final boolean a(String str) {
        return this.d && this.e == 0 && this.a.equals(str);
    }

    public final void a(long j) {
        this.e = j - this.c;
        ah.a("FlurryAgent", "Ended event '" + this.a + "' (" + this.c + ") after " + this.e + "ms");
    }

    public final byte[] a() throws Throwable {
        DataOutputStream dataOutputStream;
        Throwable th;
        DataOutputStream dataOutputStream2;
        byte[] byteArray;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            try {
                dataOutputStream.writeUTF(this.a);
                if (this.b == null) {
                    dataOutputStream.writeShort(0);
                } else {
                    dataOutputStream.writeShort(this.b.size());
                    for (Map.Entry entry : this.b.entrySet()) {
                        dataOutputStream.writeUTF(r.a((String) entry.getKey(), 255));
                        dataOutputStream.writeUTF(r.a((String) entry.getValue(), 255));
                    }
                }
                dataOutputStream.writeLong(this.c);
                dataOutputStream.writeLong(this.e);
                dataOutputStream.flush();
                byteArray = byteArrayOutputStream.toByteArray();
                r.a(dataOutputStream);
            } catch (IOException e) {
                dataOutputStream2 = dataOutputStream;
                try {
                    byteArray = new byte[0];
                    r.a(dataOutputStream2);
                } catch (Throwable th2) {
                    th = th2;
                    dataOutputStream = dataOutputStream2;
                    r.a(dataOutputStream);
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                r.a(dataOutputStream);
                throw th;
            }
        } catch (IOException e2) {
            dataOutputStream2 = null;
        } catch (Throwable th4) {
            dataOutputStream = null;
            th = th4;
        }
        return byteArray;
    }
}
