package com.gaiaonline.monstergalaxy.platform;

import com.badlogic.gdx.graphics.Color;
import com.gaiaonline.monstergalaxy.model.shop.DealListener;

/* JADX INFO: loaded from: classes.dex */
public interface AndroidPlatformHelper {
    void addOptionsView(String str);

    void addTextField(String str, int i, int i2, int i3, int i4, boolean z, boolean z2, Integer num, String str2, boolean z3, boolean z4);

    void addTextView(int i, int i2, int i3, String str, Color color);

    void buyProduct(String str);

    void composeEmail(String str, String str2);

    void composeSms(String str);

    void dispose();

    DealListener getDealListener();

    void getDealRewards();

    String getTextfieldValue(String str);

    void hideNativeProgressDialog();

    boolean isSMSCapable();

    void openMarketProductPage();

    void removeOptionsView();

    void removeTextField(String str);

    void removeTextView();

    void requestFocus(String str);

    void setDealListener(DealListener dealListener);

    void setTextViewText(String str);

    void setTextfieldValue(String str, String str2);

    void showAdColonyVideo();

    void showNativeProgressDialog();

    void showTapJoyOffers();

    void showW3iOfferWall();

    void switchKeyboard(boolean z);
}
