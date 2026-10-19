package com.badlogic.gdx.graphics.g3d.loaders.md5;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class MD5AnimationInfo {
    float lastTime;
    int maxFrame;
    float maxTime;
    int currFrame = 0;
    int nextFrame = 1;

    public MD5AnimationInfo(int maxFrame, float maxTime) {
        this.maxFrame = maxFrame;
        this.maxTime = maxTime;
    }

    public void reset() {
        reset(this.maxFrame, this.maxTime);
    }

    public void reset(int maxFrame, float maxTime) {
        this.maxFrame = maxFrame;
        this.maxTime = maxTime;
        this.currFrame = 0;
        this.nextFrame = 1;
        this.lastTime = 0.0f;
    }

    public void update(float delta) {
        this.lastTime += delta;
        if (this.lastTime >= this.maxTime) {
            this.currFrame++;
            this.nextFrame++;
            this.lastTime = 0.0f;
            if (this.currFrame >= this.maxFrame) {
                this.currFrame = 0;
            }
            if (this.nextFrame >= this.maxFrame) {
                this.nextFrame = 0;
            }
        }
    }

    public int getCurrentFrame() {
        return this.currFrame;
    }

    public int getNextFrame() {
        return this.nextFrame;
    }

    public float getInterpolation() {
        return this.lastTime / this.maxTime;
    }
}
