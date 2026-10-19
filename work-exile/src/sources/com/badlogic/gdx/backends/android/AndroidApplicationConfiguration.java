package com.badlogic.gdx.backends.android;

import com.badlogic.gdx.backends.android.surfaceview.FillResolutionStrategy;
import com.badlogic.gdx.backends.android.surfaceview.ResolutionStrategy;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class AndroidApplicationConfiguration {
    public boolean useGL20 = false;
    public int r = 5;
    public int g = 6;
    public int b = 5;
    public int a = 0;
    public int depth = 16;
    public int stencil = 0;
    public int numSamples = 0;
    public boolean useAccelerometer = true;
    public boolean useCompass = true;
    public int touchSleepTime = 0;
    public boolean useWakelock = false;
    public ResolutionStrategy resolutionStrategy = new FillResolutionStrategy();
}
