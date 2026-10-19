package com.badlogic.gdx.math;

import com.badlogic.gdx.Gdx;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Polygon {
    private boolean dirty;
    private float originX;
    private float originY;
    private float rotation;
    private final float[] vertices;
    private float x;
    private float y;
    private float scaleX = 1.0f;
    private float scaleY = 1.0f;
    private Rectangle bounds = new Rectangle();

    public Polygon(float[] vertices) {
        if (vertices.length < 6) {
            throw new IllegalArgumentException("polygons must contain at least 3 points.");
        }
        this.vertices = vertices;
    }

    public float[] getVertices() {
        if (!this.dirty) {
            return this.vertices;
        }
        float[] vertices = this.vertices;
        int numFloats = vertices.length;
        float translateX = this.x + this.originX;
        float translateY = this.y + this.originY;
        float cos = MathUtils.cosDeg(this.rotation);
        float sin = MathUtils.sinDeg(this.rotation);
        for (int i = 0; i < numFloats; i += 2) {
            float x = vertices[i] - translateX;
            float y = vertices[i + 1] - translateY;
            if (this.scaleX != 1.0f || this.scaleY != 1.0f) {
                x *= this.scaleX;
                y *= this.scaleY;
            }
            if (this.rotation != 0.0f) {
                x = (cos * x) - (sin * y);
                y = (sin * x) + (cos * y);
            }
            vertices[i] = x + translateX;
            vertices[i + 1] = y + translateY;
        }
        this.dirty = false;
        return vertices;
    }

    public void setOrigin(float originX, float originY) {
        this.originX = originX;
        this.originY = originY;
        this.dirty = true;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        this.dirty = true;
    }

    public void translate(float x, float y) {
        this.x += x;
        this.y += y;
        this.dirty = true;
    }

    public void setRotation(float degrees) {
        this.rotation = degrees;
        this.dirty = true;
    }

    public void rotate(float degrees) {
        this.rotation += degrees;
        this.dirty = true;
    }

    public void setScale(float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.dirty = true;
    }

    public void scale(float amount) {
        this.scaleX += amount;
        this.scaleY += amount;
        this.dirty = true;
    }

    public float area() {
        float area = 0.0f;
        float[] vertices = getVertices();
        int numFloats = vertices.length;
        for (int i = 0; i < numFloats; i += 2) {
            int x1 = i;
            int y1 = i + 1;
            int x2 = (i + 2) % numFloats;
            int y2 = (i + 3) % numFloats;
            area = (area + (vertices[x1] * vertices[y2])) - (vertices[x2] * vertices[y1]);
        }
        return area * 0.5f;
    }

    public Rectangle getBoundingRectangle() {
        float[] vertices = getVertices();
        float minX = vertices[0];
        float minY = vertices[1];
        float maxX = vertices[0];
        float maxY = vertices[1];
        int numFloats = vertices.length;
        for (int i = 2; i < numFloats; i += 2) {
            if (minX > vertices[i]) {
                minX = vertices[i];
            }
            if (minY > vertices[i + 1]) {
                minY = vertices[i + 1];
            }
            if (maxX < vertices[i]) {
                maxX = vertices[i];
            }
            if (maxY < vertices[i + 1]) {
                maxY = vertices[i + 1];
            }
        }
        this.bounds.x = minX;
        this.bounds.y = minY;
        this.bounds.width = maxX - minX;
        this.bounds.height = maxY - minY;
        return this.bounds;
    }

    public boolean contains(float x, float y) {
        float[] vertices = getVertices();
        int numFloats = vertices.length;
        int intersects = 0;
        for (int i = 0; i < numFloats; i += 2) {
            float x1 = vertices[i];
            float y1 = vertices[i + 1];
            float x2 = vertices[(i + 2) % numFloats];
            float y2 = vertices[(i + 3) % numFloats];
            Gdx.app.log("Poly Test: ", "Testing Point (" + x + ", " + y + ") against (" + x1 + ", " + y1 + ") and (" + x2 + ", " + y2 + ")");
            if (((y1 <= y && y < y2) || (y2 <= y && y < y1)) && x < (((x2 - x1) / (y2 - y1)) * (y - y1)) + x1) {
                intersects++;
            }
        }
        return (intersects & 1) == 1;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getOriginX() {
        return this.originX;
    }

    public float getOriginY() {
        return this.originY;
    }

    public float getRotation() {
        return this.rotation;
    }

    public float getScaleX() {
        return this.scaleX;
    }

    public float getScaleY() {
        return this.scaleY;
    }
}
