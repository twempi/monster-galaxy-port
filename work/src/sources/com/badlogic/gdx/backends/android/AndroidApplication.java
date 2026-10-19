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
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.android.surfaceview.FillResolutionStrategy;
import com.badlogic.gdx.backends.android.surfaceview.GLSurfaceViewCupcake;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Clipboard;
import com.badlogic.gdx.utils.GdxNativesLoader;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
public class AndroidApplication extends Activity implements Application {
    protected AndroidAudio audio;
    AndroidClipboard clipboard;
    protected AndroidFiles files;
    protected AndroidGraphics graphics;
    protected Handler handler;
    protected AndroidInput input;
    protected ApplicationListener listener;
    protected AndroidNet net;
    protected boolean firstResume = true;
    protected final Array<Runnable> runnables = new Array<>();
    protected final Array<Runnable> executedRunnables = new Array<>();
    protected PowerManager.WakeLock wakeLock = null;
    protected int logLevel = 2;

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
        this.input = new AndroidInput(this, this, this.graphics.view, config);
        this.audio = new AndroidAudio(this, config);
        this.files = new AndroidFiles(getAssets(), getFilesDir().getAbsolutePath());
        this.net = new AndroidNet(this);
        this.listener = listener;
        this.handler = new Handler();
        Gdx.app = this;
        Gdx.input = getInput();
        Gdx.audio = getAudio();
        Gdx.files = getFiles();
        Gdx.graphics = getGraphics();
        Gdx.net = getNet();
        try {
            requestWindowFeature(1);
        } catch (Exception ex) {
            log("AndroidApplication", "Content already displayed, cannot request FEATURE_NO_TITLE", ex);
        }
        getWindow().setFlags(1024, 1024);
        getWindow().clearFlags(2048);
        setContentView(this.graphics.getView(), createLayoutParams());
        createWakeLock(config);
        hideStatusBar(config);
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

    protected void hideStatusBar(AndroidApplicationConfiguration config) {
        if (config.hideStatusBar && getVersion() >= 11) {
            View rootView = getWindow().getDecorView();
            try {
                Method m = View.class.getMethod("setSystemUiVisibility", Integer.TYPE);
                m.invoke(rootView, 0);
                m.invoke(rootView, 1);
            } catch (Exception e) {
                log("AndroidApplication", "Can't hide status bar", e);
            }
        }
    }

    public View initializeForView(ApplicationListener listener, boolean useGL2IfAvailable) {
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useGL20 = useGL2IfAvailable;
        return initializeForView(listener, config);
    }

    public View initializeForView(ApplicationListener listener, AndroidApplicationConfiguration config) {
        this.graphics = new AndroidGraphics(this, config, config.resolutionStrategy == null ? new FillResolutionStrategy() : config.resolutionStrategy);
        this.input = new AndroidInput(this, this, this.graphics.view, config);
        this.audio = new AndroidAudio(this, config);
        this.files = new AndroidFiles(getAssets(), getFilesDir().getAbsolutePath());
        this.net = new AndroidNet(this);
        this.listener = listener;
        this.handler = new Handler();
        Gdx.app = this;
        Gdx.input = getInput();
        Gdx.audio = getAudio();
        Gdx.files = getFiles();
        Gdx.graphics = getGraphics();
        Gdx.net = getNet();
        createWakeLock(config);
        hideStatusBar(config);
        return this.graphics.getView();
    }

    @Override // android.app.Activity
    protected void onPause() {
        if (this.wakeLock != null) {
            this.wakeLock.release();
        }
        boolean isContinuous = this.graphics.isContinuousRendering();
        this.graphics.setContinuousRendering(true);
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
        this.graphics.setContinuousRendering(isContinuous);
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
        Gdx.net = getNet();
        ((AndroidInput) getInput()).registerSensorListeners();
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
        return new AndroidPreferences(getSharedPreferences(name, 0));
    }

    @Override // com.badlogic.gdx.Application
    public Clipboard getClipboard() {
        if (this.clipboard == null) {
            this.clipboard = new AndroidClipboard(this);
        }
        return this.clipboard;
    }

    @Override // com.badlogic.gdx.Application
    public void postRunnable(Runnable runnable) {
        synchronized (this.runnables) {
            this.runnables.add(runnable);
            Gdx.graphics.requestRendering();
        }
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        boolean keyboardAvailable = config.hardKeyboardHidden == 1;
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
}
