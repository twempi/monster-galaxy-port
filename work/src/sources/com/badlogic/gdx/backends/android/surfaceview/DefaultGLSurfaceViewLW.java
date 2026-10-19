package com.badlogic.gdx.backends.android.surfaceview;

import android.service.wallpaper.WallpaperService;
import android.util.AttributeSet;

/* JADX INFO: loaded from: classes.dex */
public class DefaultGLSurfaceViewLW extends GLBaseSurfaceViewLW {
    final ResolutionStrategy resolutionStrategy;

    public DefaultGLSurfaceViewLW(WallpaperService.Engine engine, ResolutionStrategy resolutionStrategy) {
        super(engine);
        this.resolutionStrategy = resolutionStrategy;
    }

    public DefaultGLSurfaceViewLW(WallpaperService.Engine engine, AttributeSet attrs, ResolutionStrategy resolutionStrategy) {
        super(engine, attrs);
        this.resolutionStrategy = resolutionStrategy;
    }
}
