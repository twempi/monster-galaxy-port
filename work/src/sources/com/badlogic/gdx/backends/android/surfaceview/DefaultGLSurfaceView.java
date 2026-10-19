package com.badlogic.gdx.backends.android.surfaceview;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/* JADX INFO: loaded from: classes.dex */
public class DefaultGLSurfaceView extends GLSurfaceView {
    final ResolutionStrategy resolutionStrategy;

    public DefaultGLSurfaceView(Context context, ResolutionStrategy resolutionStrategy) {
        super(context);
        this.resolutionStrategy = resolutionStrategy;
    }

    public DefaultGLSurfaceView(Context context, AttributeSet attrs, ResolutionStrategy resolutionStrategy) {
        super(context, attrs);
        this.resolutionStrategy = resolutionStrategy;
    }

    @Override // android.view.SurfaceView, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        ResolutionStrategy.MeasuredDimension measures = this.resolutionStrategy.calcMeasures(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measures.width, measures.height);
    }
}
