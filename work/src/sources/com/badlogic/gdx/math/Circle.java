package com.badlogic.gdx.math;

import java.io.Serializable;

/* JADX INFO: loaded from: classes.dex */
public class Circle implements Serializable {
    public float radius;
    public float x;
    public float y;

    public Circle() {
    }

    public Circle(float x, float y, float radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public Circle(Vector2 position, float radius) {
        this.x = position.x;
        this.y = position.y;
        this.radius = radius;
    }

    public boolean contains(float x, float y) {
        float x2 = this.x - x;
        float y2 = this.y - y;
        return (x2 * x2) + (y2 * y2) <= this.radius * this.radius;
    }

    public boolean contains(Vector2 point) {
        float x = this.x - point.x;
        float y = this.y - point.y;
        return (x * x) + (y * y) <= this.radius * this.radius;
    }

    public void set(float x, float y, float radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }
}
