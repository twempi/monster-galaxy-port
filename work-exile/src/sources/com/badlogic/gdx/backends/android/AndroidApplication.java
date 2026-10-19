package com.badlogic.gdx.backends.android;

import android.app.Activity;
import android.content.res.Configuration;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Debug;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.android.surfaceview.FillResolutionStrategy;
import com.badlogic.gdx.backends.android.surfaceview.GLSurfaceViewCupcake;
import com.badlogic.gdx.utils.GdxNativesLoader;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class AndroidApplication extends Activity implements Application {
    protected AndroidAudio audio;
    protected AndroidFiles files;
    protected AndroidGraphics graphics;
    protected Handler handler;
    protected AndroidInput input;
    protected ApplicationListener listener;
    protected boolean firstResume = true;
    protected final List<Runnable> runnables = new ArrayList();
    protected PowerManager.WakeLock wakeLock = null;
    protected int logLevel = 1;

    static {
        GdxNativesLoader.load();
    }

    public void initialize(ApplicationListener listener, boolean useGL2IfAvailable) {
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useGL20 = useGL2IfAvailable;
        initialize(listener, config);
    }

    public void initialize(ApplicationListener listener, AndroidApplicationConfiguration config) {
        this.graphics = new AndroidGraphics(this, config, config.resolutionStrategy == null ? new FillResolutionStrategy() : config.resolutionStrategy);
        this.input = new AndroidInput(this, this.graphics.view, config);
        this.audio = new AndroidAudio(this);
        this.files = new AndroidFiles(getAssets());
        this.listener = listener;
        this.handler = new Handler();
        Gdx.app = this;
        Gdx.input = getInput();
        Gdx.audio = getAudio();
        Gdx.files = getFiles();
        Gdx.graphics = getGraphics();
        try {
            requestWindowFeature(1);
        } catch (Exception ex) {
            log("AndroidApplication", "Content already displayed, cannot request FEATURE_NO_TITLE", ex);
        }
        getWindow().setFlags(1024, 1024);
        getWindow().clearFlags(2048);
        setContentView(this.graphics.getView(), createLayoutParams());
        createWakeLock(config);
    }

    protected FrameLayout.LayoutParams createLayoutParams() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
        layoutParams.gravity = 17;
        return layoutParams;
    }

    protected void createWakeLock(AndroidApplicationConfiguration config) {
        if (config.useWakelock) {
            PowerManager powerManager = (PowerManager) getSystemService("power");
            this.wakeLock = powerManager.newWakeLock(26, "libgdx wakelock");
        }
    }

    public View initializeForView(ApplicationListener listener, boolean useGL2IfAvailable) {
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useGL20 = useGL2IfAvailable;
        return initializeForView(listener, config);
    }

    public View initializeForView(ApplicationListener listener, AndroidApplicationConfiguration config) {
        this.graphics = new AndroidGraphics(this, config, config.resolutionStrategy == null ? new FillResolutionStrategy() : config.resolutionStrategy);
        this.input = new AndroidInput(this, this.graphics.view, config);
        this.audio = new AndroidAudio(this);
        this.files = new AndroidFiles(getAssets());
        this.listener = listener;
        this.handler = new Handler();
        Gdx.app = this;
        Gdx.input = getInput();
        Gdx.audio = getAudio();
        Gdx.files = getFiles();
        Gdx.graphics = getGraphics();
        createWakeLock(config);
        return this.graphics.getView();
    }

    @Override // android.app.Activity
    protected void onPause() {
        if (this.wakeLock != null) {
            this.wakeLock.release();
        }
        this.graphics.pause();
        this.input.unregisterSensorListeners();
        int[] realId = this.input.realId;
        for (int i = 0; i < realId.length; i++) {
            realId[i] = -1;
        }
        if (isFinishing()) {
            this.graphics.clearManagedCaches();
            this.graphics.destroy();
        }
        if (this.graphics != null && this.graphics.view != null) {
            if (this.graphics.view instanceof GLSurfaceViewCupcake) {
                ((GLSurfaceViewCupcake) this.graphics.view).onPause();
            }
            if (this.graphics.view instanceof GLSurfaceView) {
                ((GLSurfaceView) this.graphics.view).onPause();
            }
        }
        super.onPause();
    }

    @Override // android.app.Activity
    protected void onResume() {
        if (this.wakeLock != null) {
            this.wakeLock.acquire();
        }
        Gdx.app = this;
        Gdx.input = getInput();
        Gdx.audio = getAudio();
        Gdx.files = getFiles();
        Gdx.graphics = getGraphics();
        ((AndroidInput) getInput()).registerSensorListeners();
        if (this.audio != null) {
            this.audio.resume();
        }
        if (this.graphics != null && this.graphics.view != null) {
            if (this.graphics.view instanceof GLSurfaceViewCupcake) {
                ((GLSurfaceViewCupcake) this.graphics.view).onResume();
            }
            if (this.graphics.view instanceof GLSurfaceView) {
                ((GLSurfaceView) this.graphics.view).onResume();
            }
        }
        if (!this.firstResume) {
            this.graphics.resume();
        } else {
            this.firstResume = false;
        }
        super.onResume();
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
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
        return new AndroidPreferences(getSharedPreferences(name, 0));
    }

    @Override // com.badlogic.gdx.Application
    public void postRunnable(Runnable runnable) {
        synchronized (this.runnables) {
            this.runnables.add(runnable);
        }
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        boolean keyboardAvailable = config.keyboardHidden == 1;
        this.input.keyboardAvailable = keyboardAvailable;
    }

    @Override // com.badlogic.gdx.Application
    public void exit() {
        this.handler.post(new Runnable() { // from class: com.badlogic.gdx.backends.android.AndroidApplication.1
            @Override // java.lang.Runnable
            public void run() {
                AndroidApplication.this.finish();
            }
        });
    }

    @Override // com.badlogic.gdx.Application
    public void log(String tag, String message) {
        if (this.logLevel >= 1) {
            Log.i(tag, message);
        }
    }

    @Override // com.badlogic.gdx.Application
    public void log(String tag, String message, Exception exception) {
        if (this.logLevel >= 1) {
            Log.i(tag, message, exception);
        }
    }

    @Override // com.badlogic.gdx.Application
    public void error(String tag, String message) {
        if (this.logLevel >= 2) {
            Log.e(tag, message);
        }
    }

    @Override // com.badlogic.gdx.Application
    public void error(String tag, String message, Exception exception) {
        if (this.logLevel >= 2) {
            Log.e(tag, message, exception);
        }
    }

    @Override // com.badlogic.gdx.Application
    public void setLogLevel(int logLevel) {
        this.logLevel = logLevel;
    }
}
