package com.flurry.android;

/* JADX INFO: loaded from: classes.dex */
public final class OfferInSdk {
    long a;
    p b;
    String c;
    String d;
    int e;
    AdImage f;

    OfferInSdk(long j, p pVar, AdImage adImage, String str, String str2, int i) {
        this.a = j;
        this.b = pVar;
        this.c = str;
        this.f = adImage;
        this.d = str2;
        this.e = i;
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[id=" + this.a).append(",name=" + this.c + "]");
        return sb.toString();
    }
}
