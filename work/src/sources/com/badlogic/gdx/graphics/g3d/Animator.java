package com.badlogic.gdx.graphics.g3d;

/* JADX INFO: loaded from: classes.dex */
public abstract class Animator {
    protected float mAnimPos = 0.0f;
    protected float mAnimLen = 0.0f;
    protected WrapMode mWrapMode = WrapMode.Clamp;
    protected int mCurrentFrameIdx = -1;
    protected int mNextFrameIdx = -1;
    protected float mFrameDelta = 0.0f;
    protected Animation mCurrentAnim = null;

    public enum WrapMode {
        Loop,
        Clamp,
        SingleFrame
    }

    protected abstract void interpolate();

    protected abstract void setInterpolationFrames();

    public void setAnimation(Animation anim, WrapMode mode) {
        this.mCurrentAnim = anim;
        this.mWrapMode = mode;
        this.mFrameDelta = 0.0f;
        this.mAnimPos = 0.0f;
        this.mCurrentFrameIdx = -1;
        this.mNextFrameIdx = -1;
        if (this.mCurrentAnim != null) {
            this.mAnimLen = this.mCurrentAnim.getLength();
        }
    }

    public Animation getCurrentAnimation() {
        return this.mCurrentAnim;
    }

    public WrapMode getCurrentWrapMode() {
        return this.mWrapMode;
    }

    public void update(float dt) {
        if (this.mCurrentAnim != null) {
            if (this.mWrapMode != WrapMode.SingleFrame) {
                this.mAnimPos += dt;
                if (this.mAnimPos > this.mAnimLen) {
                    if (this.mWrapMode == WrapMode.Loop) {
                        this.mAnimPos = 0.0f;
                    } else if (this.mWrapMode == WrapMode.Clamp) {
                        this.mAnimPos = this.mAnimLen;
                    }
                }
            }
            float animPos = this.mAnimPos / this.mAnimLen;
            int numFrames = this.mCurrentAnim.getNumFrames();
            int currentFrameIdx = Math.min(numFrames - 1, (int) (numFrames * animPos));
            if (currentFrameIdx != this.mCurrentFrameIdx) {
                if (currentFrameIdx < numFrames - 1) {
                    this.mNextFrameIdx = currentFrameIdx + 1;
                } else {
                    switch (this.mWrapMode) {
                        case Loop:
                        case SingleFrame:
                            this.mNextFrameIdx = 0;
                            break;
                        case Clamp:
                            this.mNextFrameIdx = currentFrameIdx;
                            break;
                    }
                }
                this.mFrameDelta = 0.0f;
                this.mCurrentFrameIdx = currentFrameIdx;
            }
            this.mFrameDelta += dt;
            setInterpolationFrames();
            interpolate();
        }
    }
}
