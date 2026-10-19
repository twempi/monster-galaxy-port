package com.badlogic.gdx.math;

import java.io.Serializable;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Rectangle implements Serializable {
    private static final long serialVersionUID = 5733252015138115702L;
    public float height;
    public float width;
    public float x;
    public float y;

    public Rectangle() {
    }

    public Rectangle(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Rectangle(Rectangle rect) {
        this.x = rect.x;
        this.y = rect.y;
        this.width = rect.width;
        this.height = rect.height;
    }

    public float getX() {
        return this.x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return this.width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return this.height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public boolean contains(Rectangle rectangle) {
        float xmin = rectangle.x;
        float xmax = xmin + rectangle.width;
        float ymin = rectangle.y;
        float ymax = ymin + rectangle.height;
        return ((xmin > this.x && xmin < this.x + this.width) || (xmax > this.x && xmax < this.x + this.width)) && ((ymin > this.y && ymin < this.y + this.height) || (ymax > this.y && ymax < this.y + this.height));
    }

    public boolean overlaps(Rectangle rectangle) {
        return this.x <= rectangle.x + rectangle.width && this.x + this.width >= rectangle.x && this.y <= rectangle.y + rectangle.height && this.y + this.height >= rectangle.y;
    }

    public void set(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean contains(float x, float y) {
        return this.x < x && this.x + this.width > x && this.y < y && this.y + this.height > y;
    }

    public void set(Rectangle rect) {
        this.x = rect.x;
        this.y = rect.y;
        this.width = rect.width;
        this.height = rect.height;
    }

    public void merge(Rectangle rect) {
        float minX = Math.min(this.x, rect.x);
        float maxX = Math.max(this.x + this.width, rect.x + rect.width);
        this.x = minX;
        this.width = maxX - minX;
        float minY = Math.min(this.y, rect.y);
        float maxY = Math.max(this.y + this.height, rect.y + rect.height);
        this.y = minY;
        this.height = maxY - minY;
    }

    public String toString() {
        return this.x + "," + this.y + "," + this.width + "," + this.height;
    }
}
