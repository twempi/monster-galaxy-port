package com.millennialmedia.android;

import android.content.Context;
import android.os.Parcel;
import java.io.Externalizable;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONObject;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
abstract class CachedAd implements Externalizable {
    static final int INTERSTITIAL = 2;
    static final int NATIVE = 3;
    static final int VIDEO = 1;
    static final long serialVersionUID = 316862728709355974L;
    String acid;
    String contentUrl;
    long deferredViewStart;
    boolean downloadAllOrNothing;
    int downloadPriority;
    Date expiration;
    String id;
    MMAdView.Request request;
    boolean storedOnSdCard;

    abstract boolean canShow(Context context, MMAdView mMAdView, boolean z);

    abstract boolean download(Context context);

    abstract int getType();

    abstract String getTypeString();

    abstract boolean isOnDisk(Context context);

    abstract boolean saveAssets(Context context);

    abstract void show(Context context, MMAdView mMAdView);

    CachedAd() {
        this.downloadAllOrNothing = false;
        this.deferredViewStart = System.currentTimeMillis();
    }

    protected CachedAd(Parcel in) {
        this.downloadAllOrNothing = false;
        try {
            this.id = in.readString();
            this.acid = in.readString();
            this.expiration = (Date) in.readSerializable();
            this.deferredViewStart = in.readLong();
            boolean[] yo = new boolean[2];
            in.readBooleanArray(yo);
            this.downloadAllOrNothing = yo[0];
            this.storedOnSdCard = yo[1];
            this.contentUrl = in.readString();
            this.downloadPriority = in.readInt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.acid);
        dest.writeSerializable(this.expiration);
        dest.writeLong(this.deferredViewStart);
        dest.writeBooleanArray(new boolean[]{this.downloadAllOrNothing, this.storedOnSdCard});
        dest.writeString(this.contentUrl);
        dest.writeInt(this.downloadPriority);
    }

    public boolean equals(Object cachedAd) {
        if (this == cachedAd) {
            return true;
        }
        return this.id.equals(((CachedAd) cachedAd).id);
    }

    protected void deserializeFromObj(JSONObject videoObject) {
        this.id = videoObject.optString("id", null);
        this.acid = videoObject.optString("vid", null);
        this.contentUrl = videoObject.optString("content-url", null);
        String exp = videoObject.optString("expiration", null);
        if (exp != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ZZZZ");
            try {
                this.expiration = format.parse(exp);
            } catch (ParseException e) {
                MMAdViewSDK.Log.e(e);
            }
        }
    }

    boolean isExpired() {
        return this.expiration != null && this.expiration.getTime() <= System.currentTimeMillis();
    }

    boolean isValid() {
        return this.id != null && this.id.length() > 0 && this.contentUrl != null && this.contentUrl.length() > 0;
    }

    void delete(Context context) {
        File dir = AdCache.getCacheDirectory(context);
        if (dir != null && dir.isDirectory()) {
            try {
                File[] files = dir.listFiles(new FileFilter() { // from class: com.millennialmedia.android.CachedAd.1
                    @Override // java.io.FileFilter
                    public boolean accept(File file) {
                        return file.isFile() && file.getName().startsWith(CachedAd.this.id);
                    }
                });
                MMAdViewSDK.Log.v("Deleting %d files for %s.", Integer.valueOf(files.length), this.id);
                for (File file : files) {
                    file.delete();
                }
            } catch (Exception e) {
                MMAdViewSDK.Log.v(e);
            }
        }
    }

    static CachedAd parseJSON(String json) {
        if (MMAdViewSDK.logLevel >= 4) {
            MMAdViewSDK.Log.v("Received cached ad.");
            int length = json.length();
            if (length > 1000) {
                int e = 999;
                int s = 0;
                while (e < length) {
                    MMAdViewSDK.Log.v(json.substring(s, e));
                    s = e;
                    e += 1000;
                    if (e > length) {
                        e = length - 1;
                        break;
                    }
                }
                MMAdViewSDK.Log.v(json.substring(s, e));
            } else {
                MMAdViewSDK.Log.v(json);
            }
        }
        if (json.length() <= 0) {
            return null;
        }
        CachedAd ad = new VideoAd(json);
        return ad;
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput input) throws IOException, ClassNotFoundException {
        this.id = (String) input.readObject();
        this.acid = (String) input.readObject();
        this.expiration = (Date) input.readObject();
        this.deferredViewStart = input.readLong();
        this.contentUrl = (String) input.readObject();
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput output) throws IOException {
        output.writeObject(this.id);
        output.writeObject(this.acid);
        output.writeObject(this.expiration);
        output.writeLong(this.deferredViewStart);
        output.writeObject(this.contentUrl);
    }
}
