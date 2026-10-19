package com.millennialmedia.android;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
class OverlaySettings implements Parcelable {
    public static final String ADURL = "OVERLAY_adurl";
    public static final String BANNER_TYPE = "OVERLAY_type";
    public static final Parcelable.Creator<OverlaySettings> CREATOR = new Parcelable.Creator<OverlaySettings>() { // from class: com.millennialmedia.android.OverlaySettings.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OverlaySettings createFromParcel(Parcel in) {
            return new OverlaySettings(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OverlaySettings[] newArray(int size) {
            return new OverlaySettings[size];
        }
    };
    static final String PROPERTIES_ACCELEROMETER = "enableNativeAccelerometer";
    static final String PROPERTIES_CUSTOM_CLOSE = "useCustomClose";
    static final String PROPERTIES_DELAY_ENABLE_BOTTOM_BAR = "delayEnableBottombar";
    static final String PROPERTIES_DELAY_SHOW_BOTTOM_BAR = "delayShowBottombar";
    static final String PROPERTIES_ENABLE_BOTTOM = "enableBottombar";
    static final String PROPERTIES_ORIENTATION = "orientation";
    static final String PROPERTIES_SHOW_BOTTOM = "showBottombar";
    static final String PROPERTIES_SHOW_TITLE = "showTitlebar";
    static final String PROPERTIES_TITLE = "title";
    static final String PROPERTIES_TRANSITION = "transition";
    static final String PROPERTIES_TRANSITION_DURATION = "transitionDuration";
    static final String PROPERTIES_TRANSPARENT = "transparent";
    public static final String STATE = "OVERLAY_state";
    static final String STATE_DEFAULT = "default";
    static final String STATE_EXPANDED = "expanded";
    static final String STATE_HIDDEN = "hidden";
    static final String STATE_LOADING = "loading";
    String adUrl;
    boolean canAccelerate;
    long delayEnableBottombar;
    long delayShowBottombar;
    boolean isBannerAd;
    String orientation;
    String overlayTitle;
    String overlayTransition;
    boolean shouldEnableBottomBar;
    boolean shouldLaunchToOverlay;
    boolean shouldMakeOverlayTransparent;
    int shouldResizeOverlay;
    boolean shouldShowBottomBar;
    boolean shouldShowCustomClose;
    boolean shouldShowTitlebar;
    String state;
    long transitionTime;

    OverlaySettings() {
        reset();
        this.state = STATE_LOADING;
    }

    OverlaySettings(Parcel in) {
        try {
            boolean[] booleanValues = new boolean[8];
            in.readBooleanArray(booleanValues);
            this.shouldLaunchToOverlay = booleanValues[0];
            this.shouldShowTitlebar = booleanValues[1];
            this.shouldShowBottomBar = booleanValues[2];
            this.shouldEnableBottomBar = booleanValues[3];
            this.shouldMakeOverlayTransparent = booleanValues[4];
            this.canAccelerate = booleanValues[5];
            this.shouldShowCustomClose = booleanValues[6];
            this.isBannerAd = booleanValues[7];
            this.shouldResizeOverlay = in.readInt();
            this.overlayTitle = in.readString();
            this.overlayTransition = in.readString();
            this.transitionTime = in.readLong();
            this.transitionTime = this.transitionTime >= 0 ? this.transitionTime : 0L;
            this.orientation = in.readString();
            this.state = in.readString();
            this.delayEnableBottombar = in.readLong();
            this.delayShowBottombar = in.readLong();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void reset() {
        this.shouldLaunchToOverlay = true;
        this.shouldShowTitlebar = false;
        this.shouldShowBottomBar = true;
        this.shouldEnableBottomBar = true;
        this.shouldShowCustomClose = true;
        this.shouldMakeOverlayTransparent = false;
        this.shouldResizeOverlay = 0;
        this.overlayTitle = "Advertisement";
        this.overlayTransition = "bottomtotop";
        this.transitionTime = 600L;
        this.canAccelerate = false;
        this.orientation = "both";
        this.delayEnableBottombar = 0L;
        this.delayShowBottombar = 0L;
    }

    void log() {
        MMAdViewSDK.Log.v("shouldResizeOverlay: %d transitionTime: %d overlayTransition: %s overlayTitle: %s shouldEnableBottomBar: %b shouldShowBottomBar: %b shouldAccelerate: %b shouldMakeOverlayTransparent: %b shouldShowCustomClose: %b Orientation: %s DelayEnableBottomBar: %d, DelayShowBottomBar: %d", Integer.valueOf(this.shouldResizeOverlay), Long.valueOf(this.transitionTime), this.overlayTransition, this.overlayTitle, Boolean.valueOf(this.shouldEnableBottomBar), Boolean.valueOf(this.shouldShowBottomBar), Boolean.valueOf(this.canAccelerate), Boolean.valueOf(this.shouldMakeOverlayTransparent), Boolean.valueOf(this.shouldShowCustomClose), this.orientation, Long.valueOf(this.delayEnableBottombar), Long.valueOf(this.delayShowBottombar));
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        boolean[] booleanValues = {this.shouldLaunchToOverlay, this.shouldShowTitlebar, this.shouldShowBottomBar, this.shouldEnableBottomBar, this.shouldMakeOverlayTransparent, this.canAccelerate, this.shouldShowCustomClose, this.isBannerAd};
        dest.writeBooleanArray(booleanValues);
        dest.writeInt(this.shouldResizeOverlay);
        dest.writeString(this.overlayTitle);
        dest.writeString(this.overlayTransition);
        dest.writeLong(this.transitionTime);
        dest.writeString(this.orientation);
        dest.writeString(this.state);
        dest.writeLong(this.delayEnableBottombar);
        dest.writeLong(this.delayShowBottombar);
    }
}
