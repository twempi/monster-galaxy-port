package com.sessionm.a;

import java.util.Comparator;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: classes.dex */
public class d implements Comparator<String> {
    @Override // java.util.Comparator
    public int compare(String str, String str2) {
        String[] strArrSplit = str.split("=");
        String str3 = strArrSplit[0];
        String str4 = strArrSplit.length > 1 ? strArrSplit[1] : "";
        for (int i = 2; i < strArrSplit.length; i++) {
            str4 = str4 + "=" + strArrSplit[i];
        }
        String[] strArrSplit2 = str2.split("=");
        String str5 = strArrSplit2[0];
        String str6 = strArrSplit2.length > 1 ? strArrSplit2[1] : "";
        for (int i2 = 2; i2 < strArrSplit2.length; i2++) {
            str6 = str6 + "=" + strArrSplit2[i2];
        }
        return str3.equals(str5) ? str4.compareTo(str6) : str3.compareTo(str5);
    }
}
