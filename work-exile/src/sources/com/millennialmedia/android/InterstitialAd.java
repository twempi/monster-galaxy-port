package com.millennialmedia.android;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
class InterstitialAd extends CachedAd implements Parcelable, Externalizable {
    public static final Parcelable.Creator<InterstitialAd> CREATOR = new Parcelable.Creator<InterstitialAd>() { // from class: com.millennialmedia.android.InterstitialAd.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public InterstitialAd createFromParcel(Parcel in) {
            return new InterstitialAd(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public InterstitialAd[] newArray(int size) {
            return new InterstitialAd[size];
        }
    };
    static final long serialVersionUID = 5158660334173309853L;
    String baseUrl;
    String content;

    public InterstitialAd() {
    }

    InterstitialAd(Parcel in) {
        super(in);
        try {
            this.content = in.readString();
            this.baseUrl = in.readString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // com.millennialmedia.android.CachedAd
    int getType() {
        return 2;
    }

    @Override // com.millennialmedia.android.CachedAd
    String getTypeString() {
        return "Interstitial";
    }

    @Override // com.millennialmedia.android.CachedAd
    boolean saveAssets(Context context) {
        return true;
    }

    @Override // com.millennialmedia.android.CachedAd
    boolean download(Context context) {
        return true;
    }

    @Override // com.millennialmedia.android.CachedAd
    boolean isOnDisk(Context context) {
        return true;
    }

    @Override // com.millennialmedia.android.CachedAd
    boolean canShow(Context context, MMAdView adView, boolean checkDeferredViewStart) {
        if (checkDeferredViewStart) {
            return this.content != null && this.content.length() > 0 && this.baseUrl != null && this.baseUrl.length() > 0 && HandShake.sharedHandShake(context).canDisplayCachedAd(adView.adType, this.deferredViewStart);
        }
        return this.content != null && this.content.length() > 0 && this.baseUrl != null && this.baseUrl.length() > 0;
    }

    @Override // com.millennialmedia.android.CachedAd
    void show(Context context, MMAdView adView) {
        adView.controller.setWebViewContent(this.content, this.baseUrl, adView, null, false);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.millennialmedia.android.CachedAd, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.content);
        dest.writeString(this.baseUrl);
    }

    @Override // com.millennialmedia.android.CachedAd, java.io.Externalizable
    public void readExternal(ObjectInput input) throws IOException, ClassNotFoundException {
        super.readExternal(input);
        this.content = (String) input.readObject();
        this.baseUrl = (String) input.readObject();
    }

    @Override // com.millennialmedia.android.CachedAd, java.io.Externalizable
    public void writeExternal(ObjectOutput output) throws IOException {
        super.writeExternal(output);
        output.writeObject(this.content);
        output.writeObject(this.baseUrl);
    }
}
