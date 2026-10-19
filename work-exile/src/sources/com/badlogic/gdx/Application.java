package com.badlogic.gdx;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface Application {
    public static final int LOG_ERROR = 2;
    public static final int LOG_INFO = 1;
    public static final int LOG_NONE = 0;

    public enum ApplicationType {
        Android,
        Desktop,
        Applet,
        WebGL
    }

    void error(String str, String str2);

    void error(String str, String str2, Exception exc);

    void exit();

    Audio getAudio();

    Files getFiles();

    Graphics getGraphics();

    Input getInput();

    long getJavaHeap();

    long getNativeHeap();

    Preferences getPreferences(String str);

    ApplicationType getType();

    int getVersion();

    void log(String str, String str2);

    void log(String str, String str2, Exception exc);

    void postRunnable(Runnable runnable);

    void setLogLevel(int i);
}
