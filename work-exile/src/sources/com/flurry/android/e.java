package com.flurry.android;

import java.io.DataInput;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
final class e extends aj {
    String a;
    byte b;
    byte c;
    c d;

    e() {
    }

    e(DataInput dataInput) {
        this.a = dataInput.readUTF();
        this.b = dataInput.readByte();
        this.c = dataInput.readByte();
    }

    public final String toString() {
        return "{name: " + this.a + ", blockId: " + ((int) this.b) + ", themeId: " + ((int) this.c);
    }
}
