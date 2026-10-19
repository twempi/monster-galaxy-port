package com.millennialmedia.android;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
class MMError extends Error {
    public static final int CACHE_NOT_EMPTY = 17;
    public static final int DISPLAY_AD_ALREADY_DISPLAYED = 23;
    public static final int DISPLAY_AD_EXPIRED = 21;
    public static final int DISPLAY_AD_NOT_FOUND = 22;
    public static final int DISPLAY_AD_NOT_PERMITTED = 24;
    public static final int DISPLAY_AD_NOT_READY = 20;
    public static final int INNER_EXCEPTION = 2;
    public static final int INVALID_PARAMETER = 1;
    public static final int MAIN_THREAD_REQUIRED = 3;
    public static final int REQUEST_ALREADY_CACHING = 13;
    public static final int REQUEST_BAD_RESPONSE = 15;
    public static final int REQUEST_IN_PROGRESS = 12;
    public static final int REQUEST_NOT_FILLED = 14;
    public static final int REQUEST_NOT_PERMITTED = 16;
    public static final int REQUEST_NO_NETWORK = 11;
    public static final int REQUEST_TIMEOUT = 10;
    public static final int UNKNOWN_ERROR = 100;
    private int code;

    MMError(Exception e) {
        super(e);
        this.code = 2;
    }

    MMError(int code) {
        super(getErrorCodeMessage(code));
        this.code = code;
    }

    MMError(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    private static String getErrorCodeMessage(int error) {
        switch (error) {
            case 1:
                return "An invalid parameter was given.";
            case 3:
                return "Only the main thread can access an MMAdView.";
            case 10:
                return "The request timed out.";
            case 11:
                return "There is no network available.";
            case 12:
                return "A request is already in progress.";
            case 13:
                return "A request is already being cached.";
            case 14:
                return "The request was not filled by the server.";
            case 15:
                return "The server replied with unknown or bad content.";
            case 16:
                return "The system is not permitting a request at this time, try again later.";
            case 17:
                return "Previously fetched ad exists in the playback queue";
            case 20:
                return "There is no fetched ad to display.";
            case 21:
                return "The ad to display has expired.";
            case 22:
                return "The ad could not be found on disk.";
            case 23:
                return "The ad has already been displayed.";
            case 24:
                return "The system is not permitting a cached ad to be shown at this time, try again later.";
            case 100:
                return "An unknown error occured.";
            default:
                return "No error.";
        }
    }
}
