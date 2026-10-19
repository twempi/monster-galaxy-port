package com.badlogic.gdx.graphics.g2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.NumberUtils;

/* JADX INFO: loaded from: classes.dex */
public class PolygonSprite {
    private boolean dirty;
    private float height;
    private float originX;
    private float originY;
    PolygonRegion region;
    private float rotation;
    private float[] vertices;
    private float width;
    private float x;
    private float y;
    private float scaleX = 1.0f;
    private float scaleY = 1.0f;
    private Rectangle bounds = new Rectangle();
    private final Color color = new Color(1.0f, 1.0f, 1.0f, 1.0f);

    public PolygonSprite(PolygonRegion region) {
        setRegion(region);
        setColor(1.0f, 1.0f, 1.0f, 1.0f);
        setSize(region.getRegion().getRegionWidth(), region.getRegion().getRegionHeight());
        setOrigin(this.width / 2.0f, this.height / 2.0f);
    }

    public PolygonSprite(PolygonSprite sprite) {
        set(sprite);
    }

    public void set(PolygonSprite sprite) {
        if (sprite == null) {
            throw new IllegalArgumentException("sprite cannot be null.");
        }
        setRegion(sprite.region);
        this.x = sprite.x;
        this.y = sprite.y;
        this.width = sprite.width;
        this.height = sprite.height;
        this.originX = sprite.originX;
        this.originY = sprite.originY;
        this.rotation = sprite.rotation;
        this.scaleX = sprite.scaleX;
        this.scaleY = sprite.scaleY;
        this.color.set(sprite.color);
        this.dirty = sprite.dirty;
    }

    public void setBounds(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.dirty = true;
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
        this.dirty = true;
    }

    public void setPosition(float x, float y) {
        translate(x - this.x, y - this.y);
    }

    public void setX(float x) {
        translateX(x - this.x);
    }

    public void setY(float y) {
        translateY(y - this.y);
    }

    public void translateX(float xAmount) {
        this.x += xAmount;
        if (!this.dirty) {
            float[] vertices = this.vertices;
            for (int i = 0; i < vertices.length; i += 5) {
                vertices[i] = vertices[i] + xAmount;
            }
        }
    }

    public void translateY(float yAmount) {
        this.y += yAmount;
        if (!this.dirty) {
            float[] vertices = this.vertices;
            for (int i = 0; i < vertices.length; i += 5) {
                int i2 = i + 1;
                vertices[i2] = vertices[i2] + yAmount;
            }
        }
    }

    public void translate(float xAmount, float yAmount) {
        this.x += xAmount;
        this.y += yAmount;
        if (!this.dirty) {
            float[] vertices = this.vertices;
            for (int i = 0; i < vertices.length; i += 5) {
                vertices[i] = vertices[i] + xAmount;
                int i2 = i + 1;
                vertices[i2] = vertices[i2] + yAmount;
            }
        }
    }

    public void setColor(Color tint) {
        float color = tint.toFloatBits();
        float[] vertices = this.vertices;
        for (int i = 0; i < vertices.length; i += 5) {
            vertices[i + 2] = color;
        }
    }

    public void setColor(float r, float g, float b, float a) {
        int intBits = (((int) (255.0f * a)) << 24) | (((int) (255.0f * b)) << 16) | (((int) (255.0f * g)) << 8) | ((int) (255.0f * r));
        float color = NumberUtils.intToFloatColor(intBits);
        float[] vertices = this.vertices;
        for (int i = 0; i < vertices.length; i += 5) {
            vertices[i + 2] = color;
        }
    }

    public void setOrigin(float originX, float originY) {
        this.originX = originX;
        this.originY = originY;
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

    public void setScale(float scaleXY) {
        this.scaleX = scaleXY;
        this.scaleY = scaleXY;
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

    public float[] getVertices() {
        if (this.dirty) {
            this.dirty = false;
            float worldOriginX = this.x + this.originX;
            float worldOriginY = this.y + this.originY;
            float sX = this.width / this.region.getRegion().getRegionWidth();
            float sY = this.height / this.region.getRegion().getRegionHeight();
            float[] localVertices = this.region.getLocalVertices();
            float cos = MathUtils.cosDeg(this.rotation);
            float sin = MathUtils.sinDeg(this.rotation);
            for (int i = 0; i < localVertices.length; i += 2) {
                float fx = localVertices[i] * sX;
                float fy = localVertices[i + 1] * sY;
                float fx2 = fx - this.originX;
                float fy2 = fy - this.originY;
                if (this.scaleX != 1.0f || this.scaleY != 1.0d) {
                    fx2 *= this.scaleX;
                    fy2 *= this.scaleY;
                }
                float rx = (cos * fx2) - (sin * fy2);
                float ry = (sin * fx2) + (cos * fy2);
                this.vertices[(i / 2) * 5] = rx + worldOriginX;
                this.vertices[((i / 2) * 5) + 1] = ry + worldOriginY;
            }
        }
        return this.vertices;
    }

    public Rectangle getBoundingRectangle() {
        float[] vertices = getVertices();
        float minx = vertices[0];
        float miny = vertices[1];
        float maxx = vertices[0];
        float maxy = vertices[1];
        for (int i = 0; i < vertices.length; i += 5) {
            if (minx > vertices[i]) {
                minx = vertices[i];
            }
            if (maxx < vertices[i]) {
                maxx = vertices[i];
            }
            if (miny > vertices[i + 1]) {
                miny = vertices[i + 1];
            }
            if (maxy < vertices[i + 1]) {
                maxy = vertices[i + 1];
            }
        }
        this.bounds.x = minx;
        this.bounds.y = miny;
        this.bounds.width = maxx - minx;
        this.bounds.height = maxy - miny;
        return this.bounds;
    }

    public void draw(PolygonSpriteBatch spriteBatch) {
        spriteBatch.draw(this.region, getVertices(), 0, this.vertices.length);
    }

    public void draw(PolygonSpriteBatch spriteBatch, float alphaModulation) {
        Color color = getColor();
        float oldAlpha = color.a;
        color.a *= alphaModulation;
        setColor(color);
        draw(spriteBatch);
        color.a = oldAlpha;
        setColor(color);
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return this.height;
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

    public Color getColor() {
        int intBits = NumberUtils.floatToIntColor(this.vertices[2]);
        Color color = this.color;
        color.r = (intBits & 255) / 255.0f;
        color.g = ((intBits >>> 8) & 255) / 255.0f;
        color.b = ((intBits >>> 16) & 255) / 255.0f;
        color.a = ((intBits >>> 24) & 255) / 255.0f;
        return color;
    }

    public void setRegion(PolygonRegion region) {
        this.region = region;
        float[] localVertices = region.getLocalVertices();
        float[] localTextureCoords = region.getTextureCoords();
        if (this.vertices == null || localVertices.length != this.vertices.length) {
            this.vertices = new float[(localVertices.length / 2) * 5];
        }
        for (int i = 0; i < localVertices.length / 2; i++) {
            this.vertices[i * 5] = localVertices[i * 2];
            this.vertices[(i * 5) + 1] = localVertices[(i * 2) + 1];
            this.vertices[(i * 5) + 2] = this.color.toFloatBits();
            this.vertices[(i * 5) + 3] = localTextureCoords[i * 2];
            this.vertices[(i * 5) + 4] = localTextureCoords[(i * 2) + 1];
        }
    }
}
