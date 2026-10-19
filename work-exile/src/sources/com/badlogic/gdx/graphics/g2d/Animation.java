package com.badlogic.gdx.graphics.g2d;

import java.util.List;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Animation {
    public float frameDuration;
    final TextureRegion[] keyFrames;

    public Animation(float frameDuration, List keyFrames) {
        this.frameDuration = frameDuration;
        this.keyFrames = new TextureRegion[keyFrames.size()];
        int n = keyFrames.size();
        for (int i = 0; i < n; i++) {
            this.keyFrames[i] = (TextureRegion) keyFrames.get(i);
        }
    }

    public Animation(float frameDuration, TextureRegion... keyFrames) {
        this.frameDuration = frameDuration;
        this.keyFrames = keyFrames;
    }

    public TextureRegion getKeyFrame(float stateTime, boolean looping) {
        int frameNumber;
        int frameNumber2 = (int) (stateTime / this.frameDuration);
        if (!looping) {
            frameNumber = Math.min(this.keyFrames.length - 1, frameNumber2);
        } else {
            frameNumber = frameNumber2 % this.keyFrames.length;
        }
        return this.keyFrames[frameNumber];
    }
}
