package com.badlogic.gdx.graphics.g2d;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

/* JADX INFO: loaded from: classes.dex */
public class Animation {
    public static final int LOOP = 2;
    public static final int LOOP_PINGPONG = 4;
    public static final int LOOP_RANDOM = 5;
    public static final int LOOP_REVERSED = 3;
    public static final int NORMAL = 0;
    public static final int REVERSED = 1;
    public final float animationDuration;
    public final float frameDuration;
    final TextureRegion[] keyFrames;
    private int playMode;

    public Animation(float frameDuration, Array<? extends TextureRegion> keyFrames) {
        this.playMode = 0;
        this.frameDuration = frameDuration;
        this.animationDuration = keyFrames.size * frameDuration;
        this.keyFrames = new TextureRegion[keyFrames.size];
        int n = keyFrames.size;
        for (int i = 0; i < n; i++) {
            this.keyFrames[i] = keyFrames.get(i);
        }
        this.playMode = 0;
    }

    public Animation(float frameDuration, Array<? extends TextureRegion> keyFrames, int playType) {
        this.playMode = 0;
        this.frameDuration = frameDuration;
        this.animationDuration = keyFrames.size * frameDuration;
        this.keyFrames = new TextureRegion[keyFrames.size];
        int n = keyFrames.size;
        for (int i = 0; i < n; i++) {
            this.keyFrames[i] = keyFrames.get(i);
        }
        this.playMode = playType;
    }

    public Animation(float frameDuration, TextureRegion... keyFrames) {
        this.playMode = 0;
        this.frameDuration = frameDuration;
        this.animationDuration = keyFrames.length * frameDuration;
        this.keyFrames = keyFrames;
        this.playMode = 0;
    }

    public TextureRegion getKeyFrame(float stateTime, boolean looping) {
        if (looping && (this.playMode == 0 || this.playMode == 1)) {
            if (this.playMode == 0) {
                this.playMode = 2;
            } else {
                this.playMode = 3;
            }
        } else if (!looping && this.playMode != 0 && this.playMode != 1) {
            if (this.playMode == 3) {
                this.playMode = 1;
            } else {
                this.playMode = 2;
            }
        }
        return getKeyFrame(stateTime);
    }

    public TextureRegion getKeyFrame(float stateTime) {
        int frameNumber;
        int frameNumber2 = (int) (stateTime / this.frameDuration);
        switch (this.playMode) {
            case 0:
                frameNumber = Math.min(this.keyFrames.length - 1, frameNumber2);
                break;
            case 1:
                frameNumber = Math.max((this.keyFrames.length - frameNumber2) - 1, 0);
                break;
            case 2:
                frameNumber = frameNumber2 % this.keyFrames.length;
                break;
            case 3:
                frameNumber = (this.keyFrames.length - (frameNumber2 % this.keyFrames.length)) - 1;
                break;
            case 4:
                frameNumber = frameNumber2 % (this.keyFrames.length * 2);
                if (frameNumber >= this.keyFrames.length) {
                    frameNumber = (this.keyFrames.length - 1) - (frameNumber - this.keyFrames.length);
                }
                break;
            case 5:
                frameNumber = MathUtils.random(this.keyFrames.length - 1);
                break;
            default:
                frameNumber = Math.min(this.keyFrames.length - 1, frameNumber2);
                break;
        }
        return this.keyFrames[frameNumber];
    }

    public void setPlayMode(int playMode) {
        this.playMode = playMode;
    }

    public boolean isAnimationFinished(float stateTime) {
        int frameNumber = (int) (stateTime / this.frameDuration);
        return this.keyFrames.length + (-1) < frameNumber;
    }
}
