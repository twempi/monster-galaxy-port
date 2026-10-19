package com.badlogic.gdx.graphics.g2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.NumberUtils;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Sprite extends TextureRegion {
    public static final int C1 = 2;
    public static final int C2 = 7;
    public static final int C3 = 12;
    public static final int C4 = 17;
    static final int SPRITE_SIZE = 20;
    public static final int U1 = 3;
    public static final int U2 = 8;
    public static final int U3 = 13;
    public static final int U4 = 18;
    public static final int V1 = 4;
    public static final int V2 = 9;
    public static final int V3 = 14;
    public static final int V4 = 19;
    static final int VERTEX_SIZE = 5;
    public static final int X1 = 0;
    public static final int X2 = 5;
    public static final int X3 = 10;
    public static final int X4 = 15;
    public static final int Y1 = 1;
    public static final int Y2 = 6;
    public static final int Y3 = 11;
    public static final int Y4 = 16;
    private Rectangle bounds;
    private final Color color;
    private boolean dirty;
    float height;
    private float originX;
    private float originY;
    private float rotation;
    private float scaleX;
    private float scaleY;
    final float[] vertices;
    float width;
    private float x;
    private float y;

    public Sprite() {
        this.vertices = new float[20];
        this.color = new Color(1.0f, 1.0f, 1.0f, 1.0f);
        this.scaleX = 1.0f;
        this.scaleY = 1.0f;
        this.dirty = true;
        this.bounds = new Rectangle();
        setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public Sprite(Texture texture) {
        this(texture, 0, 0, texture.getWidth(), texture.getHeight());
    }

    public Sprite(Texture texture, int srcWidth, int srcHeight) {
        this(texture, 0, 0, srcWidth, srcHeight);
    }

    public Sprite(Texture texture, int srcX, int srcY, int srcWidth, int srcHeight) {
        this.vertices = new float[20];
        this.color = new Color(1.0f, 1.0f, 1.0f, 1.0f);
        this.scaleX = 1.0f;
        this.scaleY = 1.0f;
        this.dirty = true;
        this.bounds = new Rectangle();
        if (texture == null) {
            throw new IllegalArgumentException("texture cannot be null.");
        }
        this.texture = texture;
        setRegion(srcX, srcY, srcWidth, srcHeight);
        setColor(1.0f, 1.0f, 1.0f, 1.0f);
        setSize(Math.abs(srcWidth), Math.abs(srcHeight));
        setOrigin(this.width / 2.0f, this.height / 2.0f);
    }

    public Sprite(TextureRegion region) {
        this.vertices = new float[20];
        this.color = new Color(1.0f, 1.0f, 1.0f, 1.0f);
        this.scaleX = 1.0f;
        this.scaleY = 1.0f;
        this.dirty = true;
        this.bounds = new Rectangle();
        setRegion(region);
        setColor(1.0f, 1.0f, 1.0f, 1.0f);
        setSize(Math.abs(region.getRegionWidth()), Math.abs(region.getRegionHeight()));
        setOrigin(this.width / 2.0f, this.height / 2.0f);
    }

    public Sprite(TextureRegion region, int srcX, int srcY, int srcWidth, int srcHeight) {
        this.vertices = new float[20];
        this.color = new Color(1.0f, 1.0f, 1.0f, 1.0f);
        this.scaleX = 1.0f;
        this.scaleY = 1.0f;
        this.dirty = true;
        this.bounds = new Rectangle();
        setRegion(region, srcX, srcY, srcWidth, srcHeight);
        setColor(1.0f, 1.0f, 1.0f, 1.0f);
        setSize(Math.abs(srcWidth), Math.abs(srcHeight));
        setOrigin(this.width / 2.0f, this.height / 2.0f);
    }

    public Sprite(Sprite sprite) {
        this.vertices = new float[20];
        this.color = new Color(1.0f, 1.0f, 1.0f, 1.0f);
        this.scaleX = 1.0f;
        this.scaleY = 1.0f;
        this.dirty = true;
        this.bounds = new Rectangle();
        set(sprite);
    }

    public void set(Sprite sprite) {
        if (sprite == null) {
            throw new IllegalArgumentException("sprite cannot be null.");
        }
        System.arraycopy(sprite.vertices, 0, this.vertices, 0, 20);
        this.texture = sprite.texture;
        this.x = sprite.x;
        this.y = sprite.y;
        this.width = sprite.width;
        this.height = sprite.height;
        this.originX = sprite.originX;
        this.originY = sprite.originY;
        this.rotation = sprite.rotation;
        this.scaleX = sprite.scaleX;
        this.scaleY = sprite.scaleY;
        this.dirty = sprite.dirty;
    }

    public void setBounds(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        if (!this.dirty) {
            float x2 = x + width;
            float y2 = y + height;
            float[] vertices = this.vertices;
            vertices[0] = x;
            vertices[1] = y;
            vertices[5] = x;
            vertices[6] = y2;
            vertices[10] = x2;
            vertices[11] = y2;
            vertices[15] = x2;
            vertices[16] = y;
            if (this.rotation != 0.0f || this.scaleX != 1.0f || this.scaleY != 1.0f) {
                this.dirty = true;
            }
        }
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
        if (!this.dirty) {
            float x2 = this.x + width;
            float y2 = this.y + height;
            float[] vertices = this.vertices;
            vertices[0] = this.x;
            vertices[1] = this.y;
            vertices[5] = this.x;
            vertices[6] = y2;
            vertices[10] = x2;
            vertices[11] = y2;
            vertices[15] = x2;
            vertices[16] = this.y;
            if (this.rotation != 0.0f || this.scaleX != 1.0f || this.scaleY != 1.0f) {
                this.dirty = true;
            }
        }
    }

    public void setPosition(float x, float y) {
        translate(x - this.x, y - this.y);
    }

    public void translate(float xAmount, float yAmount) {
        this.x += xAmount;
        this.y += yAmount;
        if (!this.dirty) {
            float[] vertices = this.vertices;
            vertices[0] = vertices[0] + xAmount;
            vertices[1] = vertices[1] + yAmount;
            vertices[5] = vertices[5] + xAmount;
            vertices[6] = vertices[6] + yAmount;
            vertices[10] = vertices[10] + xAmount;
            vertices[11] = vertices[11] + yAmount;
            vertices[15] = vertices[15] + xAmount;
            vertices[16] = vertices[16] + yAmount;
        }
    }

    public void setColor(Color tint) {
        float color = tint.toFloatBits();
        float[] vertices = this.vertices;
        vertices[2] = color;
        vertices[7] = color;
        vertices[12] = color;
        vertices[17] = color;
    }

    public void setColor(float r, float g, float b, float a) {
        int intBits = (((int) (255.0f * a)) << 24) | (((int) (255.0f * b)) << 16) | (((int) (255.0f * g)) << 8) | ((int) (255.0f * r));
        float color = NumberUtils.intBitsToFloat((-16777217) & intBits);
        float[] vertices = this.vertices;
        vertices[2] = color;
        vertices[7] = color;
        vertices[12] = color;
        vertices[17] = color;
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

    public void rotate90(boolean clockwise) {
        float[] vertices = this.vertices;
        if (clockwise) {
            float temp = vertices[4];
            vertices[4] = vertices[19];
            vertices[19] = vertices[14];
            vertices[14] = vertices[9];
            vertices[9] = temp;
            float temp2 = vertices[3];
            vertices[3] = vertices[18];
            vertices[18] = vertices[13];
            vertices[13] = vertices[8];
            vertices[8] = temp2;
            return;
        }
        float temp3 = vertices[4];
        vertices[4] = vertices[9];
        vertices[9] = vertices[14];
        vertices[14] = vertices[19];
        vertices[19] = temp3;
        float temp4 = vertices[3];
        vertices[3] = vertices[8];
        vertices[8] = vertices[13];
        vertices[13] = vertices[18];
        vertices[18] = temp4;
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
            float[] vertices = this.vertices;
            float localX = -this.originX;
            float localY = -this.originY;
            float localX2 = localX + this.width;
            float localY2 = localY + this.height;
            float worldOriginX = this.x - localX;
            float worldOriginY = this.y - localY;
            if (this.scaleX != 1.0f || this.scaleY != 1.0f) {
                localX *= this.scaleX;
                localY *= this.scaleY;
                localX2 *= this.scaleX;
                localY2 *= this.scaleY;
            }
            if (this.rotation != 0.0f) {
                float cos = MathUtils.cosDeg(this.rotation);
                float sin = MathUtils.sinDeg(this.rotation);
                float localXCos = localX * cos;
                float localXSin = localX * sin;
                float localYCos = localY * cos;
                float localYSin = localY * sin;
                float localX2Cos = localX2 * cos;
                float localX2Sin = localX2 * sin;
                float localY2Cos = localY2 * cos;
                float localY2Sin = localY2 * sin;
                float x1 = (localXCos - localYSin) + worldOriginX;
                float y1 = localYCos + localXSin + worldOriginY;
                vertices[0] = x1;
                vertices[1] = y1;
                float x2 = (localXCos - localY2Sin) + worldOriginX;
                float y2 = localY2Cos + localXSin + worldOriginY;
                vertices[5] = x2;
                vertices[6] = y2;
                float x3 = (localX2Cos - localY2Sin) + worldOriginX;
                float y3 = localY2Cos + localX2Sin + worldOriginY;
                vertices[10] = x3;
                vertices[11] = y3;
                vertices[15] = (x3 - x2) + x1;
                vertices[16] = y3 - (y2 - y1);
            } else {
                float x4 = localX + worldOriginX;
                float y4 = localY + worldOriginY;
                float x5 = localX2 + worldOriginX;
                float y5 = localY2 + worldOriginY;
                vertices[0] = x4;
                vertices[1] = y4;
                vertices[5] = x4;
                vertices[6] = y5;
                vertices[10] = x5;
                vertices[11] = y5;
                vertices[15] = x5;
                vertices[16] = y4;
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
        if (minx > vertices[5]) {
            minx = vertices[5];
        }
        if (minx > vertices[10]) {
            minx = vertices[10];
        }
        if (minx > vertices[15]) {
            minx = vertices[15];
        }
        if (maxx < vertices[5]) {
            maxx = vertices[5];
        }
        if (maxx < vertices[10]) {
            maxx = vertices[10];
        }
        if (maxx < vertices[15]) {
            maxx = vertices[15];
        }
        if (miny > vertices[6]) {
            miny = vertices[6];
        }
        if (miny > vertices[11]) {
            miny = vertices[11];
        }
        if (miny > vertices[16]) {
            miny = vertices[16];
        }
        if (maxy < vertices[6]) {
            maxy = vertices[6];
        }
        if (maxy < vertices[11]) {
            maxy = vertices[11];
        }
        if (maxy < vertices[16]) {
            maxy = vertices[16];
        }
        this.bounds.x = minx;
        this.bounds.y = miny;
        this.bounds.width = maxx - minx;
        this.bounds.height = maxy - miny;
        return this.bounds;
    }

    public void draw(SpriteBatch spriteBatch) {
        spriteBatch.draw(this.texture, getVertices(), 0, 20);
    }

    public void draw(SpriteBatch spriteBatch, float alphaModulation) {
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
        float f = this.vertices[2];
        int intBits = NumberUtils.floatToRawIntBits(this.vertices[2]);
        Color color = this.color;
        color.r = (intBits & 255) / 255.0f;
        color.g = ((intBits >>> 8) & 255) / 255.0f;
        color.b = ((intBits >>> 16) & 255) / 255.0f;
        color.a = ((intBits >>> 24) & 255) / 255.0f;
        return color;
    }

    @Override // com.badlogic.gdx.graphics.g2d.TextureRegion
    public void setRegion(float u, float v, float u2, float v2) {
        this.u = u;
        this.v = v;
        this.u2 = u2;
        this.v2 = v2;
        float[] vertices = this.vertices;
        vertices[3] = u;
        vertices[4] = v2;
        vertices[8] = u;
        vertices[9] = v;
        vertices[13] = u2;
        vertices[14] = v;
        vertices[18] = u2;
        vertices[19] = v2;
    }

    @Override // com.badlogic.gdx.graphics.g2d.TextureRegion
    public void setU(float u) {
        this.u = u;
        this.vertices[3] = u;
        this.vertices[8] = u;
    }

    @Override // com.badlogic.gdx.graphics.g2d.TextureRegion
    public void setV(float v) {
        this.v = v;
        this.vertices[9] = v;
        this.vertices[14] = v;
    }

    @Override // com.badlogic.gdx.graphics.g2d.TextureRegion
    public void setU2(float u2) {
        this.u2 = u2;
        this.vertices[13] = u2;
        this.vertices[18] = u2;
    }

    @Override // com.badlogic.gdx.graphics.g2d.TextureRegion
    public void setV2(float v2) {
        this.v2 = v2;
        this.vertices[4] = v2;
        this.vertices[19] = v2;
    }

    @Override // com.badlogic.gdx.graphics.g2d.TextureRegion
    public void flip(boolean x, boolean y) {
        super.flip(x, y);
        float[] vertices = this.vertices;
        if (x) {
            float u = vertices[3];
            float u2 = vertices[13];
            this.u = u;
            this.u2 = u2;
            vertices[3] = u2;
            vertices[8] = u2;
            vertices[13] = u;
            vertices[18] = u;
        }
        if (y) {
            float v = vertices[9];
            float v2 = vertices[4];
            this.v = v;
            this.v2 = v2;
            vertices[4] = v;
            vertices[9] = v2;
            vertices[14] = v2;
            vertices[19] = v;
        }
    }

    @Override // com.badlogic.gdx.graphics.g2d.TextureRegion
    public void scroll(float xAmount, float yAmount) {
        float[] vertices = this.vertices;
        if (xAmount != 0.0f) {
            float u = (vertices[3] + xAmount) % 1.0f;
            float u2 = u + (this.width / this.texture.getWidth());
            this.u = u;
            this.u2 = u2;
            vertices[3] = u;
            vertices[8] = u;
            vertices[13] = u2;
            vertices[18] = u2;
        }
        if (yAmount != 0.0f) {
            float v = (vertices[9] + yAmount) % 1.0f;
            float v2 = v + (this.height / this.texture.getHeight());
            this.v = v;
            this.v2 = v2;
            vertices[4] = v2;
            vertices[9] = v;
            vertices[14] = v;
            vertices[19] = v2;
        }
    }
}
