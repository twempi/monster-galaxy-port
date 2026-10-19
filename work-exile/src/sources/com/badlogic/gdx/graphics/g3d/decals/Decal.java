package com.badlogic.gdx.graphics.g3d.decals;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Decal {
    public static final int C1 = 3;
    public static final int C2 = 9;
    public static final int C3 = 15;
    public static final int C4 = 21;
    public static final int SIZE = 24;
    public static final int U1 = 4;
    public static final int U2 = 10;
    public static final int U3 = 16;
    public static final int U4 = 22;
    public static final int V1 = 5;
    public static final int V2 = 11;
    public static final int V3 = 17;
    public static final int V4 = 23;
    private static final int VERTEX_SIZE = 6;
    public static final int X1 = 0;
    public static final int X2 = 6;
    public static final int X3 = 12;
    public static final int X4 = 18;
    public static final int Y1 = 1;
    public static final int Y2 = 7;
    public static final int Y3 = 13;
    public static final int Y4 = 19;
    public static final int Z1 = 2;
    public static final int Z2 = 8;
    public static final int Z3 = 14;
    public static final int Z4 = 20;
    public int value;
    private static Vector3 tmp = new Vector3();
    private static Vector3 tmp2 = new Vector3();
    static final Vector3 dir = new Vector3();
    protected static Quaternion rotator = new Quaternion(0.0f, 0.0f, 0.0f, 0.0f);
    protected static final Vector3 X_AXIS = new Vector3(1.0f, 0.0f, 0.0f);
    protected static final Vector3 Y_AXIS = new Vector3(0.0f, 1.0f, 0.0f);
    protected static final Vector3 Z_AXIS = new Vector3(0.0f, 0.0f, 1.0f);
    protected float[] vertices = new float[24];
    protected Vector3 position = new Vector3();
    protected Quaternion rotation = Quaternion.idt();
    protected Vector2 scale = new Vector2(1.0f, 1.0f);
    public Vector2 transformationOffset = null;
    protected Vector2 dimensions = new Vector2();
    protected DecalMaterial material = new DecalMaterial();
    protected boolean updated = false;

    protected Decal() {
    }

    public void setColor(float r, float g, float b, float a) {
        int intBits = (((int) (255.0f * a)) << 24) | (((int) (255.0f * b)) << 16) | (((int) (255.0f * g)) << 8) | ((int) (255.0f * r));
        float color = Float.intBitsToFloat((-16777217) & intBits);
        this.vertices[3] = color;
        this.vertices[9] = color;
        this.vertices[15] = color;
        this.vertices[21] = color;
    }

    public void rotateX(float angle) {
        rotator.set(X_AXIS, angle);
        this.rotation.mul(rotator);
        this.updated = false;
    }

    public void rotateY(float angle) {
        rotator.set(Y_AXIS, angle);
        this.rotation.mul(rotator);
        this.updated = false;
    }

    public void rotateZ(float angle) {
        rotator.set(Z_AXIS, angle);
        this.rotation.mul(rotator);
        this.updated = false;
    }

    public void setRotation(Vector3 dir2, Vector3 up) {
        tmp.set(up).crs(dir2).nor();
        tmp2.set(dir2).crs(tmp).nor();
        this.rotation.setFromAxes(tmp.x, tmp.y, tmp.z, tmp2.x, tmp2.y, tmp2.z, dir2.x, dir2.y, dir2.z);
        this.updated = false;
    }

    public Quaternion getRotation() {
        return this.rotation;
    }

    public void translateX(float units) {
        this.position.x += units;
        this.updated = false;
    }

    public void setX(float x) {
        this.position.x = x;
        this.updated = false;
    }

    public float getX() {
        return this.position.x;
    }

    public void translateY(float units) {
        this.position.y += units;
        this.updated = false;
    }

    public void setY(float y) {
        this.position.y = y;
        this.updated = false;
    }

    public float getY() {
        return this.position.y;
    }

    public void translateZ(float units) {
        this.position.z += units;
        this.updated = false;
    }

    public void setZ(float z) {
        this.position.z = z;
        this.updated = false;
    }

    public float getZ() {
        return this.position.z;
    }

    public void translate(float x, float y, float z) {
        this.position.add(x, y, z);
        this.updated = false;
    }

    public void setPosition(float x, float y, float z) {
        this.position.set(x, y, z);
        this.updated = false;
    }

    public Vector3 getPosition() {
        return this.position;
    }

    public void setScaleX(float scale) {
        this.scale.x = scale;
        this.updated = false;
    }

    public float getScaleX() {
        return this.scale.x;
    }

    public void setScaleY(float scale) {
        this.scale.y += scale;
        this.updated = false;
    }

    public float getScaleY() {
        return this.scale.y;
    }

    public void setScale(float scaleX, float scaleY) {
        this.scale.set(scaleX, scaleY);
        this.updated = false;
    }

    public void setScale(float scale) {
        this.scale.set(scale, scale);
        this.updated = false;
    }

    public void setWidth(float width) {
        this.dimensions.x = width;
        this.updated = false;
    }

    public float getWidth() {
        return this.dimensions.x;
    }

    public void setHeight(float height) {
        this.dimensions.y = height;
        this.updated = false;
    }

    public float getHeight() {
        return this.dimensions.y;
    }

    public void setDimensions(float width, float height) {
        this.dimensions.set(width, height);
        this.updated = false;
    }

    public float[] getVertices() {
        return this.vertices;
    }

    protected void update() {
        if (!this.updated) {
            resetVertices();
            transformVertices();
        }
    }

    protected void transformVertices() {
        float ty;
        float tx;
        if (this.transformationOffset != null) {
            tx = -this.transformationOffset.x;
            ty = -this.transformationOffset.y;
        } else {
            ty = 0.0f;
            tx = 0.0f;
        }
        float x = (this.vertices[0] + tx) * this.scale.x;
        float y = (this.vertices[1] + ty) * this.scale.y;
        float z = this.vertices[2];
        this.vertices[0] = ((this.rotation.w * x) + (this.rotation.y * z)) - (this.rotation.z * y);
        this.vertices[1] = ((this.rotation.w * y) + (this.rotation.z * x)) - (this.rotation.x * z);
        this.vertices[2] = ((this.rotation.w * z) + (this.rotation.x * y)) - (this.rotation.y * x);
        float w = (((-this.rotation.x) * x) - (this.rotation.y * y)) - (this.rotation.z * z);
        this.rotation.conjugate();
        float x2 = this.vertices[0];
        float y2 = this.vertices[1];
        float z2 = this.vertices[2];
        this.vertices[0] = (((this.rotation.x * w) + (this.rotation.w * x2)) + (this.rotation.z * y2)) - (this.rotation.y * z2);
        this.vertices[1] = (((this.rotation.y * w) + (this.rotation.w * y2)) + (this.rotation.x * z2)) - (this.rotation.z * x2);
        this.vertices[2] = (((this.rotation.z * w) + (this.rotation.w * z2)) + (this.rotation.y * x2)) - (this.rotation.x * y2);
        this.rotation.conjugate();
        float[] fArr = this.vertices;
        fArr[0] = fArr[0] + (this.position.x - tx);
        float[] fArr2 = this.vertices;
        fArr2[1] = fArr2[1] + (this.position.y - ty);
        float[] fArr3 = this.vertices;
        fArr3[2] = fArr3[2] + this.position.z;
        float x3 = (this.vertices[6] + tx) * this.scale.x;
        float y3 = (this.vertices[7] + ty) * this.scale.y;
        float z3 = this.vertices[8];
        this.vertices[6] = ((this.rotation.w * x3) + (this.rotation.y * z3)) - (this.rotation.z * y3);
        this.vertices[7] = ((this.rotation.w * y3) + (this.rotation.z * x3)) - (this.rotation.x * z3);
        this.vertices[8] = ((this.rotation.w * z3) + (this.rotation.x * y3)) - (this.rotation.y * x3);
        float w2 = (((-this.rotation.x) * x3) - (this.rotation.y * y3)) - (this.rotation.z * z3);
        this.rotation.conjugate();
        float x4 = this.vertices[6];
        float y4 = this.vertices[7];
        float z4 = this.vertices[8];
        this.vertices[6] = (((this.rotation.x * w2) + (this.rotation.w * x4)) + (this.rotation.z * y4)) - (this.rotation.y * z4);
        this.vertices[7] = (((this.rotation.y * w2) + (this.rotation.w * y4)) + (this.rotation.x * z4)) - (this.rotation.z * x4);
        this.vertices[8] = (((this.rotation.z * w2) + (this.rotation.w * z4)) + (this.rotation.y * x4)) - (this.rotation.x * y4);
        this.rotation.conjugate();
        float[] fArr4 = this.vertices;
        fArr4[6] = fArr4[6] + (this.position.x - tx);
        float[] fArr5 = this.vertices;
        fArr5[7] = fArr5[7] + (this.position.y - ty);
        float[] fArr6 = this.vertices;
        fArr6[8] = fArr6[8] + this.position.z;
        float x5 = (this.vertices[12] + tx) * this.scale.x;
        float y5 = (this.vertices[13] + ty) * this.scale.y;
        float z5 = this.vertices[14];
        this.vertices[12] = ((this.rotation.w * x5) + (this.rotation.y * z5)) - (this.rotation.z * y5);
        this.vertices[13] = ((this.rotation.w * y5) + (this.rotation.z * x5)) - (this.rotation.x * z5);
        this.vertices[14] = ((this.rotation.w * z5) + (this.rotation.x * y5)) - (this.rotation.y * x5);
        float w3 = (((-this.rotation.x) * x5) - (this.rotation.y * y5)) - (this.rotation.z * z5);
        this.rotation.conjugate();
        float x6 = this.vertices[12];
        float y6 = this.vertices[13];
        float z6 = this.vertices[14];
        this.vertices[12] = (((this.rotation.x * w3) + (this.rotation.w * x6)) + (this.rotation.z * y6)) - (this.rotation.y * z6);
        this.vertices[13] = (((this.rotation.y * w3) + (this.rotation.w * y6)) + (this.rotation.x * z6)) - (this.rotation.z * x6);
        this.vertices[14] = (((this.rotation.z * w3) + (this.rotation.w * z6)) + (this.rotation.y * x6)) - (this.rotation.x * y6);
        this.rotation.conjugate();
        float[] fArr7 = this.vertices;
        fArr7[12] = fArr7[12] + (this.position.x - tx);
        float[] fArr8 = this.vertices;
        fArr8[13] = fArr8[13] + (this.position.y - ty);
        float[] fArr9 = this.vertices;
        fArr9[14] = fArr9[14] + this.position.z;
        float x7 = (this.vertices[18] + tx) * this.scale.x;
        float y7 = (this.vertices[19] + ty) * this.scale.y;
        float z7 = this.vertices[20];
        this.vertices[18] = ((this.rotation.w * x7) + (this.rotation.y * z7)) - (this.rotation.z * y7);
        this.vertices[19] = ((this.rotation.w * y7) + (this.rotation.z * x7)) - (this.rotation.x * z7);
        this.vertices[20] = ((this.rotation.w * z7) + (this.rotation.x * y7)) - (this.rotation.y * x7);
        float w4 = (((-this.rotation.x) * x7) - (this.rotation.y * y7)) - (this.rotation.z * z7);
        this.rotation.conjugate();
        float x8 = this.vertices[18];
        float y8 = this.vertices[19];
        float z8 = this.vertices[20];
        this.vertices[18] = (((this.rotation.x * w4) + (this.rotation.w * x8)) + (this.rotation.z * y8)) - (this.rotation.y * z8);
        this.vertices[19] = (((this.rotation.y * w4) + (this.rotation.w * y8)) + (this.rotation.x * z8)) - (this.rotation.z * x8);
        this.vertices[20] = (((this.rotation.z * w4) + (this.rotation.w * z8)) + (this.rotation.y * x8)) - (this.rotation.x * y8);
        this.rotation.conjugate();
        float[] fArr10 = this.vertices;
        fArr10[18] = fArr10[18] + (this.position.x - tx);
        float[] fArr11 = this.vertices;
        fArr11[19] = fArr11[19] + (this.position.y - ty);
        float[] fArr12 = this.vertices;
        fArr12[20] = fArr12[20] + this.position.z;
        this.updated = true;
    }

    protected void resetVertices() {
        float left = (-this.dimensions.x) / 2.0f;
        float right = left + this.dimensions.x;
        float top = this.dimensions.y / 2.0f;
        float bottom = top - this.dimensions.y;
        this.vertices[0] = left;
        this.vertices[1] = top;
        this.vertices[2] = 0.0f;
        this.vertices[6] = right;
        this.vertices[7] = top;
        this.vertices[8] = 0.0f;
        this.vertices[12] = left;
        this.vertices[13] = bottom;
        this.vertices[14] = 0.0f;
        this.vertices[18] = right;
        this.vertices[19] = bottom;
        this.vertices[20] = 0.0f;
        this.updated = false;
    }

    protected void updateUVs() {
        TextureRegion tr = this.material.textureRegion;
        this.vertices[4] = tr.getU();
        this.vertices[5] = tr.getV();
        this.vertices[10] = tr.getU2();
        this.vertices[11] = tr.getV();
        this.vertices[16] = tr.getU();
        this.vertices[17] = tr.getV2();
        this.vertices[22] = tr.getU2();
        this.vertices[23] = tr.getV2();
    }

    public void setTextureRegion(TextureRegion textureRegion) {
        this.material.textureRegion = textureRegion;
        updateUVs();
    }

    public TextureRegion getTextureRegion() {
        return this.material.textureRegion;
    }

    public void setBlending(int srcBlendFactor, int dstBlendFactor) {
        this.material.srcBlendFactor = srcBlendFactor;
        this.material.dstBlendFactor = dstBlendFactor;
    }

    protected DecalMaterial getMaterial() {
        return this.material;
    }

    public void lookAt(Vector3 position, Vector3 up) {
        dir.set(position).sub(this.position).nor();
        setRotation(dir, Vector3.Y);
    }

    public static Decal newDecal(TextureRegion textureRegion) {
        return newDecal(textureRegion.getRegionWidth(), textureRegion.getRegionHeight(), textureRegion, -1, -1);
    }

    public static Decal newDecal(TextureRegion textureRegion, boolean hasTransparency) {
        return newDecal(textureRegion.getRegionWidth(), textureRegion.getRegionHeight(), textureRegion, hasTransparency ? 770 : -1, hasTransparency ? 771 : -1);
    }

    public static Decal newDecal(float width, float height, TextureRegion textureRegion) {
        return newDecal(width, height, textureRegion, -1, -1);
    }

    public static Decal newDecal(float width, float height, TextureRegion textureRegion, boolean hasTransparency) {
        return newDecal(width, height, textureRegion, hasTransparency ? 770 : -1, hasTransparency ? 771 : -1);
    }

    public static Decal newDecal(float width, float height, TextureRegion textureRegion, int srcBlendFactor, int dstBlendFactor) {
        Decal decal = new Decal();
        decal.setTextureRegion(textureRegion);
        decal.setBlending(srcBlendFactor, dstBlendFactor);
        decal.dimensions.x = width;
        decal.dimensions.y = height;
        decal.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        return decal;
    }
}
