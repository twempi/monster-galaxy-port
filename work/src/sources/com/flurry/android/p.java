package com.flurry.android;

import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
final class p {
    final String a;
    v b;
    long c;
    List d;
    private byte e;

    p(p pVar, long j) {
        this(pVar.a, pVar.e, j);
        this.b = pVar.b;
        this.c = pVar.c;
    }

    p(String str, byte b, long j) {
        this.d = new ArrayList();
        this.a = str;
        this.e = b;
        this.d.add(new f((byte) 1, j));
    }

    final void a(f fVar) {
        this.d.add(fVar);
    }

    final long a() {
        return ((f) this.d.get(0)).b;
    }

    final void a(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(this.a);
        dataOutput.writeByte(this.e);
        if (this.b == null) {
            dataOutput.writeLong(0L);
            dataOutput.writeLong(0L);
            dataOutput.writeByte(0);
        } else {
            dataOutput.writeLong(this.b.a);
            dataOutput.writeLong(this.b.e);
            byte[] bArr = this.b.g;
            dataOutput.writeByte(bArr.length);
            dataOutput.write(bArr);
        }
        dataOutput.writeShort(this.d.size());
        for (f fVar : this.d) {
            dataOutput.writeByte(fVar.a);
            dataOutput.writeLong(fVar.b);
        }
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{hook: " + this.a + ", ad: " + this.b.d + ", transitions: [");
        Iterator it = this.d.iterator();
        while (it.hasNext()) {
            sb.append((f) it.next());
            sb.append(",");
        }
        sb.append("]}");
        return sb.toString();
    }
}
