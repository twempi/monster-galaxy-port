package com.badlogic.gdx.math;

import java.io.Serializable;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Vector2 implements Serializable {
    private static final long serialVersionUID = 913902788239530931L;
    private static final Vector2 tmp = new Vector2();
    public float x;
    public float y;

    public Vector2() {
    }

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2(Vector2 v) {
        set(v);
    }

    public Vector2 cpy() {
        return new Vector2(this);
    }

    public float len() {
        return (float) Math.sqrt((this.x * this.x) + (this.y * this.y));
    }

    public float len2() {
        return (this.x * this.x) + (this.y * this.y);
    }

    public Vector2 set(Vector2 v) {
        this.x = v.x;
        this.y = v.y;
        return this;
    }

    public Vector2 set(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector2 sub(Vector2 v) {
        this.x -= v.x;
        this.y -= v.y;
        return this;
    }

    public Vector2 nor() {
        float len = len();
        if (len != 0.0f) {
            this.x /= len;
            this.y /= len;
        }
        return this;
    }

    public Vector2 add(Vector2 v) {
        this.x += v.x;
        this.y += v.y;
        return this;
    }

    public Vector2 add(float x, float y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public float dot(Vector2 v) {
        return (this.x * v.x) + (this.y * v.y);
    }

    public Vector2 mul(float scalar) {
        this.x *= scalar;
        this.y *= scalar;
        return this;
    }

    public float dst(Vector2 v) {
        float x_d = v.x - this.x;
        float y_d = v.y - this.y;
        return (float) Math.sqrt((x_d * x_d) + (y_d * y_d));
    }

    public float dst(float x, float y) {
        float x_d = x - this.x;
        float y_d = y - this.y;
        return (float) Math.sqrt((x_d * x_d) + (y_d * y_d));
    }

    public float dst2(Vector2 v) {
        float x_d = v.x - this.x;
        float y_d = v.y - this.y;
        return (x_d * x_d) + (y_d * y_d);
    }

    public String toString() {
        return "[" + this.x + ":" + this.y + "]";
    }

    public Vector2 sub(float x, float y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    public Vector2 tmp() {
        return tmp.set(this);
    }

    public Vector2 mul(Matrix3 mat) {
        float x = (this.x * mat.vals[0]) + (this.y * mat.vals[3]) + mat.vals[6];
        float y = (this.x * mat.vals[1]) + (this.y * mat.vals[4]) + mat.vals[7];
        this.x = x;
        this.y = y;
        return this;
    }

    public float crs(Vector2 v) {
        return (this.x * v.y) - (this.y * v.x);
    }

    public float crs(float x, float y) {
        return (this.x * y) - (this.y * x);
    }

    public float angle() {
        float angle = ((float) Math.atan2(this.y, this.x)) * 57.295776f;
        return angle < 0.0f ? angle + 360.0f : angle;
    }

    public Vector2 rotate(float angle) {
        float rad = angle * 0.017453292f;
        float cos = (float) Math.cos(rad);
        float sin = (float) Math.sin(rad);
        float newX = (this.x * cos) - (this.y * sin);
        float newY = (this.x * sin) + (this.y * cos);
        this.x = newX;
        this.y = newY;
        return this;
    }

    public Vector2 lerp(Vector2 target, float alpha) {
        Vector2 r = mul(1.0f - alpha);
        r.add(target.tmp().mul(alpha));
        return r;
    }
}
