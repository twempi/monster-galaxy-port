package com.badlogic.gdx.backends.android;

import android.os.Build;
import android.os.Bundle;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.android.surfaceview.GLBaseSurfaceViewLW;

/* JADX INFO: loaded from: classes.dex */
public abstract class AndroidLiveWallpaperService extends WallpaperService {
    static boolean DEBUG = false;
    protected static volatile int runningEngines = 0;
    final String TAG = "AndroidLiveWallpaperService";

    public abstract AndroidApplicationConfiguration createConfig();

    public abstract ApplicationListener createListener();

    public abstract void offsetChange(ApplicationListener applicationListener, float f, float f2, float f3, float f4, int i, int i2);

    @Override // android.service.wallpaper.WallpaperService, android.app.Service
    public void onCreate() {
        if (DEBUG) {
            Log.d("AndroidLiveWallpaperService", " > LibdgxWallpaperService - onCreate()");
        }
        super.onCreate();
    }

    @Override // android.service.wallpaper.WallpaperService
    public WallpaperService.Engine onCreateEngine() {
        return new AndroidWallpaperEngine(createListener(), createConfig());
    }

    @Override // android.service.wallpaper.WallpaperService, android.app.Service
    public void onDestroy() {
        if (DEBUG) {
            Log.d("AndroidLiveWallpaperService", " > LibdgxWallpaperService - onDestroy()");
        }
        super.onDestroy();
    }

    public class AndroidWallpaperEngine extends WallpaperService.Engine {
        protected final AndroidLiveWallpaper app;
        protected final ApplicationListener listener;
        protected GLBaseSurfaceViewLW view;

        public AndroidWallpaperEngine(ApplicationListener listener, AndroidApplicationConfiguration config) {
            super(AndroidLiveWallpaperService.this);
            if (AndroidLiveWallpaperService.DEBUG) {
                AndroidLiveWallpaperService.this.getClass();
                Log.d("AndroidLiveWallpaperService", " > MyEngine() " + hashCode());
            }
            this.app = new AndroidLiveWallpaper(AndroidLiveWallpaperService.this, this);
            this.app.initialize(listener, config);
            this.listener = listener;
            this.view = ((AndroidGraphicsLiveWallpaper) this.app.getGraphics()).getView();
            if (config.getTouchEventsForLiveWallpaper && Integer.parseInt(Build.VERSION.SDK) < 9) {
                setTouchEventsEnabled(true);
            }
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public Bundle onCommand(String pAction, int pX, int pY, int pZ, Bundle pExtras, boolean pResultRequested) {
            if (AndroidLiveWallpaperService.DEBUG) {
                Log.d("AndroidLiveWallpaperService", " > onCommand(" + pAction + " " + pX + " " + pY + " " + pZ + " " + pExtras + " " + pResultRequested + ")");
            }
            return super.onCommand(pAction, pX, pY, pZ, pExtras, pResultRequested);
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public void onCreate(SurfaceHolder surfaceHolder) {
            AndroidLiveWallpaperService.runningEngines++;
            if (AndroidLiveWallpaperService.DEBUG) {
                Log.d("AndroidLiveWallpaperService", " > onCreate() " + hashCode() + ", running: " + AndroidLiveWallpaperService.runningEngines);
            }
            super.onCreate(surfaceHolder);
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public void onDestroy() {
            AndroidLiveWallpaperService.runningEngines--;
            if (AndroidLiveWallpaperService.DEBUG) {
                AndroidLiveWallpaperService.this.getClass();
                Log.d("AndroidLiveWallpaperService", " > onDestroy() " + hashCode() + ", running: " + AndroidLiveWallpaperService.runningEngines);
            }
            this.view.onDestroy();
            super.onDestroy();
        }

        public void onPause() {
            if (AndroidLiveWallpaperService.DEBUG) {
                AndroidLiveWallpaperService.this.getClass();
                Log.d("AndroidLiveWallpaperService", " > onPause() " + hashCode() + ", running: " + AndroidLiveWallpaperService.runningEngines);
            }
            this.app.onPause();
            this.view.onPause();
        }

        public void onResume() {
            if (AndroidLiveWallpaperService.DEBUG) {
                AndroidLiveWallpaperService.this.getClass();
                Log.d("AndroidLiveWallpaperService", " > onResume() " + hashCode() + ", running: " + AndroidLiveWallpaperService.runningEngines);
            }
            this.app.onResume();
            this.view.onResume();
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            if (AndroidLiveWallpaperService.DEBUG) {
                AndroidLiveWallpaperService.this.getClass();
                Log.d("AndroidLiveWallpaperService", " > onSurfaceChanged() " + isPreview() + " " + hashCode() + ", running: " + AndroidLiveWallpaperService.runningEngines);
            }
            super.onSurfaceChanged(holder, format, width, height);
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public void onSurfaceCreated(SurfaceHolder holder) {
            if (AndroidLiveWallpaperService.DEBUG) {
                AndroidLiveWallpaperService.this.getClass();
                Log.d("AndroidLiveWallpaperService", " > onSurfaceCreated() " + hashCode() + ", running: " + AndroidLiveWallpaperService.runningEngines);
            }
            super.onSurfaceCreated(holder);
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            if (AndroidLiveWallpaperService.DEBUG) {
                AndroidLiveWallpaperService.this.getClass();
                Log.d("AndroidLiveWallpaperService", " > onSurfaceDestroyed() " + hashCode() + ", running: " + AndroidLiveWallpaperService.runningEngines);
            }
            super.onSurfaceDestroyed(holder);
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public void onVisibilityChanged(boolean visible) {
            if (AndroidLiveWallpaperService.DEBUG) {
                AndroidLiveWallpaperService.this.getClass();
                Log.d("AndroidLiveWallpaperService", " > onVisibilityChanged(" + visible + ") " + hashCode());
            }
            if (visible) {
                onResume();
            } else {
                onPause();
            }
            super.onVisibilityChanged(visible);
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public void onTouchEvent(MotionEvent event) {
            this.app.input.onTouch(null, event);
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public void onOffsetsChanged(final float xOffset, final float yOffset, final float xOffsetStep, final float yOffsetStep, final int xPixelOffset, final int yPixelOffset) {
            if (AndroidLiveWallpaperService.DEBUG) {
                AndroidLiveWallpaperService.this.getClass();
                Log.d("AndroidLiveWallpaperService", " > onOffsetChanged(" + xOffset + " " + yOffset + " " + xOffsetStep + " " + yOffsetStep + " " + xPixelOffset + " " + yPixelOffset + ") " + hashCode());
            }
            this.app.postRunnable(new Runnable() { // from class: com.badlogic.gdx.backends.android.AndroidLiveWallpaperService.AndroidWallpaperEngine.1
                @Override // java.lang.Runnable
                public void run() {
                    AndroidLiveWallpaperService.this.offsetChange(AndroidWallpaperEngine.this.listener, xOffset, yOffset, xOffsetStep, yOffsetStep, xPixelOffset, yPixelOffset);
                }
            });
            super.onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep, xPixelOffset, yPixelOffset);
        }
    }
}
