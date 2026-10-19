package com.kontagent.util;

import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class ListUtil {
    public static String listToString(List<Integer> elements) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Integer element : elements) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append(element.toString());
        }
        return stringBuilder.toString();
    }
}
