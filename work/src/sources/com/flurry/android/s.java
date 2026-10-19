package com.flurry.android;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.chartboost.sdk.CBAPIConnection;

/* JADX INFO: loaded from: classes.dex */
final class s extends LinearLayout {
    public s(CatalogActivity catalogActivity, Context context) {
        super(context);
        setBackgroundColor(-1);
        AdImage adImageL = catalogActivity.e.l();
        if (adImageL != null) {
            ImageView imageView = new ImageView(context);
            imageView.setId(CBAPIConnection.MIN_TIMEOUT);
            byte[] bArr = adImageL.e;
            if (bArr != null) {
                imageView.setImageBitmap(BitmapFactory.decodeByteArray(bArr, 0, bArr.length));
            }
            r.a(context, imageView, r.a(context, adImageL.b), r.a(context, adImageL.c));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
            layoutParams.setMargins(0, 0, 0, -3);
            setGravity(3);
            addView(imageView, layoutParams);
        }
    }
}
