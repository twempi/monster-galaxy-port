package com.tapjoy;

import java.lang.reflect.Array;

/* JADX INFO: loaded from: classes.dex */
public class TapjoyVideoObject {
    public static final int BUTTON_MAX = 10;
    public int buttonCount;
    public String[][] buttonData = (String[][]) Array.newInstance((Class<?>) String.class, 10, 2);
    public String clickURL;
    public String currencyAmount;
    public String currencyName;
    public String dataLocation;
    public String iconURL;
    public String offerID;
    public String videoAdName;
    public String videoURL;

    public void addButton(String name, String url) {
        this.buttonData[this.buttonCount][0] = name;
        this.buttonData[this.buttonCount][1] = url;
        this.buttonCount++;
    }
}
