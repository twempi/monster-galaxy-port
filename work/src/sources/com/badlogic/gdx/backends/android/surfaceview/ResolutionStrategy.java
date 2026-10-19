package com.badlogic.gdx.backends.android.surfaceview;

/* JADX INFO: loaded from: classes.dex */
public interface ResolutionStrategy {
    MeasuredDimension calcMeasures(int i, int i2);

    public static class MeasuredDimension {
        public final int height;
        public final int width;

        public MeasuredDimension(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }
}
