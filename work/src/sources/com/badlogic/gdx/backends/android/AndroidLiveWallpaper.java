package com.badlogic.gdx.backends.android;

import android.os.Build;
import android.os.Debug;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.android.surfaceview.FillResolutionStrategy;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Clipboard;
import com.badlogic.gdx.utils.GdxNativesLoader;

/* JADX INFO: loaded from: classes.dex */
class AndroidLiveWallpaper implements Application {
    protected AndroidAudio audio;
    AndroidClipboard clipboard;
    private WallpaperService.Engine engine;
    protected AndroidFiles files;
    protected AndroidGraphicsLiveWallpaper graphics;
    protected AndroidInput input;
    protected ApplicationListener listener;
    protected AndroidNet net;
    protected WallpaperService service;
    protected boolean firstResume = true;
    protected final Array<Runnable> runnables = new Array<>();
    protected final Array<Runnable> executedRunnables = new Array<>();
    protected int logLevel = 2;

    static {
        GdxNativesLoader.load();
    }

    public AndroidLiveWallpaper(WallpaperService service, WallpaperService.Engine engine) {
        this.service = service;
        this.engine = engine;
    }

    public void initialize(ApplicationListener listener, AndroidApplicationConfiguration config) {
        this.graphics = new AndroidGraphicsLiveWallpaper(this, config.useGL20, config.resolutionStrategy == null ? new FillResolutionStrategy() : config.resolutionStrategy);
        this.input = new AndroidInput(this, getService(), null, config);
        this.audio = new AndroidAudio(getService(), config);
        this.files = new AndroidFiles(getService().getAssets());
        this.listener = listener;
        Gdx.app = this;
        Gdx.input = getInput();
        Gdx.audio = getAudio();
        Gdx.files = getFiles();
        Gdx.graphics = getGraphics();
    }

    public void onPause() {
        this.graphics.pause();
        if (this.audio != null) {
            this.audio.pause();
        }
        this.input.unregisterSensorListeners();
    }

    public void onResume() {
        Gdx.app = this;
        Gdx.input = getInput();
        Gdx.audio = getAudio();
        Gdx.files = getFiles();
        Gdx.graphics = getGraphics();
        ((AndroidInput) getInput()).registerSensorListeners();
        if (this.audio != null) {
            this.audio.resume();
        }
        if (!this.firstResume) {
            this.graphics.resume();
        } else {
            this.firstResume = false;
        }
    }

    public void onDestroy() {
        this.graphics.destroy();
    }

    public WallpaperService getService() {
        return this.service;
    }

    public WallpaperService.Engine getEngine() {
        return this.engine;
    }

    public ApplicationListener getListener() {
        return this.listener;
    }

    @Override // com.badlogic.gdx.Application
    public void postRunnable(Runnable runnable) {
        synchronized (this.runnables) {
            this.runnables.add(runnable);
        }
    }

    @Override // com.badlogic.gdx.Application
    public Audio getAudio() {
        return this.audio;
    }

    @Override // com.badlogic.gdx.Application
    public Files getFiles() {
        return this.files;
    }

    @Override // com.badlogic.gdx.Application
    public Graphics getGraphics() {
        return this.graphics;
    }

    @Override // com.badlogic.gdx.Application
    public Input getInput() {
        return this.input;
    }

    @Override // com.badlogic.gdx.Application
    public Net getNet() {
        return this.net;
    }

    @Override // com.badlogic.gdx.Application
    public Application.ApplicationType getType() {
        return Application.ApplicationType.Android;
    }

    @Override // com.badlogic.gdx.Application
    public int getVersion() {
        return Integer.parseInt(Build.VERSION.SDK);
    }

    @Override // com.badlogic.gdx.Application
    public long getJavaHeap() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

    @Override // com.badlogic.gdx.Application
    public long getNativeHeap() {
        return Debug.getNativeHeapAllocatedSize();
    }

    @Override // com.badlogic.gdx.Application
    public Preferences getPreferences(String name) {
        return new AndroidPreferences(this.service.getSharedPreferences(name, 0));
    }

    @Override // com.badlogic.gdx.Application
    public Clipboard getClipboard() {
        if (this.clipboard == null) {
            this.clipboard = new AndroidClipboard(this.service);
        }
        return this.clipboard;
    }

    @Override // com.badlogic.gdx.Application
    public void debug(String tag, String message) {
        if (this.logLevel >= 3) {
            Log.d(tag, message);
        }
    }

    @Override // com.badlogic.gdx.Application
    public void debug(String tag, String message, Throwable exception) {
        if (this.logLevel >= 3) {
            Log.d(tag, message, exception);
        }
    }

    @Override // com.badlogic.gdx.Application
    public void log(String tag, String message) {
        if (this.logLevel >= 2) {
            Log.i(tag, message);
        }
    }

    @Override // com.badlogic.gdx.Application
    public void log(String tag, String message, Exception exception) {
        if (this.logLevel >= 2) {
            Log.i(tag, message, exception);
        }
    }

    @Override // com.badlogic.gdx.Application
    public void error(String tag, String message) {
        if (this.logLevel >= 1) {
            Log.e(tag, message);
        }
    }

    @Override // com.badlogic.gdx.Application
    public void error(String tag, String message, Throwable exception) {
        if (this.logLevel >= 1) {
            Log.e(tag, message, exception);
        }
    }

    @Override // com.badlogic.gdx.Application
    public void setLogLevel(int logLevel) {
        this.logLevel = logLevel;
    }

    @Override // com.badlogic.gdx.Application
    public void exit() {
    }
}
