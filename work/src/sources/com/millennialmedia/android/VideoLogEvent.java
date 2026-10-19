package com.millennialmedia.android;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
class VideoLogEvent implements Parcelable, Externalizable {
    public static final Parcelable.Creator<VideoLogEvent> CREATOR = new Parcelable.Creator<VideoLogEvent>() { // from class: com.millennialmedia.android.VideoLogEvent.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VideoLogEvent createFromParcel(Parcel in) {
            return new VideoLogEvent(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VideoLogEvent[] newArray(int size) {
            return new VideoLogEvent[size];
        }
    };
    static final long serialVersionUID = 795553873017368584L;
    String[] activities;
    long position;

    public VideoLogEvent() {
    }

    VideoLogEvent(JSONObject logObject) {
        deserializeFromObj(logObject);
    }

    VideoLogEvent(Parcel in) {
        try {
            this.position = in.readLong();
            int len = in.readInt();
            this.activities = new String[len];
            in.readStringArray(this.activities);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deserializeFromObj(JSONObject logObject) {
        if (logObject != null) {
            this.position = logObject.optInt("time") * 1000;
            JSONArray jsonArray = logObject.optJSONArray("urls");
            if (jsonArray != null) {
                this.activities = new String[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    this.activities[i] = jsonArray.optString(i);
                }
                return;
            }
            this.activities = new String[0];
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.position);
        dest.writeInt(this.activities.length);
        dest.writeStringArray(this.activities);
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput input) throws IOException, ClassNotFoundException {
        this.position = input.readLong();
        int count = input.readInt();
        this.activities = new String[count];
        for (int i = 0; i < count; i++) {
            this.activities[i] = (String) input.readObject();
        }
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput output) throws IOException {
        output.writeLong(this.position);
        output.writeInt(this.activities.length);
        String[] arr$ = this.activities;
        for (String temp : arr$) {
            output.writeObject(temp);
        }
    }
}
