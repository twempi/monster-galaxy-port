package com.badlogic.gdx.graphics.g3d.loaders.md5;

import com.badlogic.gdx.graphics.g3d.Animation;
import com.badlogic.gdx.graphics.g3d.Animator;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class MD5Animator extends Animator {
    protected MD5Joints mCurrentFrame = null;
    protected MD5Joints mNextFrame = null;
    protected MD5Joints mSkeleton = null;

    public void setSkeleton(MD5Joints skeleton) {
        this.mSkeleton = skeleton;
    }

    public MD5Joints getSkeleton() {
        return this.mSkeleton;
    }

    @Override // com.badlogic.gdx.graphics.g3d.Animator
    public void setAnimation(Animation anim, Animator.WrapMode wrapMode) {
        super.setAnimation(anim, wrapMode);
        if (anim != null) {
            MD5Joints mD5Joints = ((MD5Animation) anim).frames[0];
            this.mNextFrame = mD5Joints;
            this.mSkeleton = mD5Joints;
            this.mCurrentFrame = mD5Joints;
        }
    }

    @Override // com.badlogic.gdx.graphics.g3d.Animator
    protected void setInterpolationFrames() {
        this.mCurrentFrame = ((MD5Animation) this.mCurrentAnim).frames[this.mCurrentFrameIdx];
        this.mNextFrame = ((MD5Animation) this.mCurrentAnim).frames[this.mNextFrameIdx];
    }

    @Override // com.badlogic.gdx.graphics.g3d.Animator
    protected void interpolate() {
        MD5Animation.interpolate(this.mCurrentFrame, this.mNextFrame, this.mSkeleton, this.mFrameDelta);
    }
}
