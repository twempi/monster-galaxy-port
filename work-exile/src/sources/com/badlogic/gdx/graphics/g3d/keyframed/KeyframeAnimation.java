package com.badlogic.gdx.graphics.g3d.keyframed;

import com.badlogic.gdx.graphics.g3d.Animation;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class KeyframeAnimation extends Animation {
    public Keyframe[] keyframes;
    public float length;
    public String name;
    public int refs = 1;
    public float sampleRate;

    public KeyframeAnimation(String name, int frames, float length, float sampleRate) {
        this.name = name;
        this.keyframes = new Keyframe[frames];
        this.length = length;
        this.sampleRate = sampleRate;
    }

    @Override // com.badlogic.gdx.graphics.g3d.Animation
    public float getLength() {
        return this.length;
    }

    @Override // com.badlogic.gdx.graphics.g3d.Animation
    public int getNumFrames() {
        return this.keyframes.length;
    }

    public void addRef() {
        this.refs++;
    }

    public int removeRef() {
        int i = this.refs - 1;
        this.refs = i;
        return i;
    }
}
