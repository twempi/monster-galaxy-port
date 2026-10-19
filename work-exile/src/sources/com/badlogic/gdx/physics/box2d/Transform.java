package com.badlogic.gdx.physics.box2d;

import com.badlogic.gdx.math.Vector2;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Transform {
    public static final int COL1_X = 2;
    public static final int COL1_Y = 3;
    public static final int COL2_X = 4;
    public static final int COL2_Y = 5;
    public static final int POS_X = 0;
    public static final int POS_Y = 1;
    private Vector2 position;
    public float[] vals;

    public Transform() {
        this.vals = new float[6];
        this.position = new Vector2();
    }

    public Transform(Vector2 position, float angle) {
        this.vals = new float[6];
        this.position = new Vector2();
        setPosition(position);
        setRotation(angle);
    }

    public Vector2 mul(Vector2 v) {
        float x = this.vals[0] + (this.vals[2] * v.x) + (this.vals[4] * v.y);
        float y = this.vals[1] + (this.vals[3] * v.x) + (this.vals[5] * v.y);
        v.x = x;
        v.y = y;
        return v;
    }

    public Vector2 getPosition() {
        return this.position.set(this.vals[0], this.vals[1]);
    }

    public void setRotation(float angle) {
        float c = (float) Math.cos(angle);
        float s = (float) Math.sin(angle);
        this.vals[2] = c;
        this.vals[4] = -s;
        this.vals[3] = s;
        this.vals[5] = c;
    }

    public void setPosition(Vector2 pos) {
        this.vals[0] = pos.x;
        this.vals[1] = pos.y;
    }
}
