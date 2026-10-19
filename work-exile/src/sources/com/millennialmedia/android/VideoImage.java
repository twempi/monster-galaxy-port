package com.millennialmedia.android;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import com.sessionm.ui.SessionMActivity;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
class VideoImage implements Parcelable, Externalizable {
    public static final Parcelable.Creator<VideoImage> CREATOR = new Parcelable.Creator<VideoImage>() { // from class: com.millennialmedia.android.VideoImage.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VideoImage createFromParcel(Parcel in) {
            return new VideoImage(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VideoImage[] newArray(int size) {
            return new VideoImage[size];
        }
    };
    static final long serialVersionUID = 808334584720834205L;
    String[] activity;
    int anchor;
    int anchor2;
    long appearanceDelay;
    ImageButton button;
    long contentLength;
    long fadeDuration;
    float fromAlpha;
    String imageUrl;
    long inactivityTimeout;
    RelativeLayout.LayoutParams layoutParams;
    String linkUrl;
    String overlayOrientation;
    int paddingBottom;
    int paddingLeft;
    int paddingRight;
    int paddingTop;
    int position;
    int position2;
    float toAlpha;

    String getImageName() {
        Uri temp;
        if (this.imageUrl == null || (temp = Uri.parse(this.imageUrl)) == null || temp.getLastPathSegment() == null) {
            return null;
        }
        return temp.getLastPathSegment().replaceFirst("\\.[^\\.]*$", ".dat");
    }

    public VideoImage() {
        this.paddingTop = 0;
        this.paddingBottom = 0;
        this.paddingLeft = 0;
        this.paddingRight = 0;
        this.fromAlpha = 1.0f;
        this.toAlpha = 1.0f;
        this.fadeDuration = 1000L;
    }

    VideoImage(JSONObject imageObject) {
        this.paddingTop = 0;
        this.paddingBottom = 0;
        this.paddingLeft = 0;
        this.paddingRight = 0;
        this.fromAlpha = 1.0f;
        this.toAlpha = 1.0f;
        this.fadeDuration = 1000L;
        deserializeFromObj(imageObject);
    }

    VideoImage(Parcel in) {
        this.paddingTop = 0;
        this.paddingBottom = 0;
        this.paddingLeft = 0;
        this.paddingRight = 0;
        this.fromAlpha = 1.0f;
        this.toAlpha = 1.0f;
        this.fadeDuration = 1000L;
        try {
            this.imageUrl = in.readString();
            this.contentLength = in.readLong();
            int len = in.readInt();
            this.activity = new String[len];
            in.readStringArray(this.activity);
            this.linkUrl = in.readString();
            this.overlayOrientation = in.readString();
            this.paddingTop = in.readInt();
            this.paddingBottom = in.readInt();
            this.paddingLeft = in.readInt();
            this.paddingRight = in.readInt();
            this.position = in.readInt();
            this.anchor = in.readInt();
            this.position2 = in.readInt();
            this.anchor2 = in.readInt();
            this.appearanceDelay = in.readLong();
            this.inactivityTimeout = in.readLong();
            this.fromAlpha = in.readFloat();
            this.toAlpha = in.readFloat();
            this.fadeDuration = in.readLong();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deserializeFromObj(JSONObject imageObject) {
        if (imageObject != null) {
            this.imageUrl = imageObject.optString("image", null);
            this.contentLength = imageObject.optLong("contentLength");
            JSONArray jsonArray = imageObject.optJSONArray("activity");
            if (jsonArray != null) {
                this.activity = new String[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    this.activity[i] = jsonArray.optString(i);
                }
            } else {
                this.activity = new String[0];
            }
            this.linkUrl = imageObject.optString(SessionMActivity.INTENT_PARAM_URL, null);
            this.overlayOrientation = imageObject.optString("overlayOrientation", null);
            this.position = imageObject.optInt("android-layout");
            this.anchor = imageObject.optInt("android-layoutAnchor");
            this.position2 = imageObject.optInt("android-layout2");
            this.anchor2 = imageObject.optInt("android-layoutAnchor2");
            this.paddingTop = imageObject.optInt("android-paddingTop");
            this.paddingLeft = imageObject.optInt("android-paddingLeft");
            this.paddingRight = imageObject.optInt("android-paddingRight");
            this.paddingBottom = imageObject.optInt("android-paddingBottom");
            this.appearanceDelay = (long) (imageObject.optDouble("appearanceDelay", 0.0d) * 1000.0d);
            this.inactivityTimeout = (long) (imageObject.optDouble("inactivityTimeout", 0.0d) * 1000.0d);
            JSONObject opacityObject = imageObject.optJSONObject("opacity");
            if (opacityObject != null) {
                this.fromAlpha = (float) opacityObject.optDouble("start", 1.0d);
                this.toAlpha = (float) opacityObject.optDouble("end", 1.0d);
                this.fadeDuration = (long) (opacityObject.optDouble("fadeDuration", 1.0d) * 1000.0d);
            }
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageUrl);
        dest.writeLong(this.contentLength);
        dest.writeInt(this.activity.length);
        dest.writeStringArray(this.activity);
        dest.writeString(this.linkUrl);
        dest.writeString(this.overlayOrientation);
        dest.writeInt(this.paddingTop);
        dest.writeInt(this.paddingBottom);
        dest.writeInt(this.paddingLeft);
        dest.writeInt(this.paddingRight);
        dest.writeInt(this.position);
        dest.writeInt(this.anchor);
        dest.writeInt(this.position2);
        dest.writeInt(this.anchor2);
        dest.writeLong(this.appearanceDelay);
        dest.writeLong(this.inactivityTimeout);
        dest.writeFloat(this.fromAlpha);
        dest.writeFloat(this.toAlpha);
        dest.writeLong(this.fadeDuration);
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput input) throws IOException, ClassNotFoundException {
        this.imageUrl = (String) input.readObject();
        this.contentLength = input.readLong();
        int count = input.readInt();
        this.activity = new String[count];
        for (int i = 0; i < count; i++) {
            this.activity[i] = (String) input.readObject();
        }
        this.linkUrl = (String) input.readObject();
        this.overlayOrientation = (String) input.readObject();
        this.paddingTop = input.readInt();
        this.paddingBottom = input.readInt();
        this.paddingLeft = input.readInt();
        this.paddingRight = input.readInt();
        this.position = input.readInt();
        this.anchor = input.readInt();
        this.position2 = input.readInt();
        this.anchor2 = input.readInt();
        this.appearanceDelay = input.readLong();
        this.inactivityTimeout = input.readLong();
        this.fromAlpha = input.readFloat();
        this.toAlpha = input.readFloat();
        this.fadeDuration = input.readLong();
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput output) throws IOException {
        output.writeObject(this.imageUrl);
        output.writeLong(this.contentLength);
        output.writeInt(this.activity.length);
        String[] arr$ = this.activity;
        for (String temp : arr$) {
            output.writeObject(temp);
        }
        output.writeObject(this.linkUrl);
        output.writeObject(this.overlayOrientation);
        output.writeInt(this.paddingTop);
        output.writeInt(this.paddingBottom);
        output.writeInt(this.paddingLeft);
        output.writeInt(this.paddingRight);
        output.writeInt(this.position);
        output.writeInt(this.anchor);
        output.writeInt(this.position2);
        output.writeInt(this.anchor2);
        output.writeLong(this.appearanceDelay);
        output.writeLong(this.inactivityTimeout);
        output.writeFloat(this.fromAlpha);
        output.writeFloat(this.toAlpha);
        output.writeLong(this.fadeDuration);
    }
}
