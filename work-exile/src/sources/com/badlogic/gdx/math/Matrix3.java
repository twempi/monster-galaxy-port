package com.badlogic.gdx.math;

import com.badlogic.gdx.utils.GdxRuntimeException;
import java.io.Serializable;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Matrix3 implements Serializable {
    private static final float DEGREE_TO_RAD = 0.017453292f;
    private static final long serialVersionUID = 7907569533774959788L;
    public float[] vals = new float[9];

    public Matrix3() {
        idt();
    }

    public Matrix3 idt() {
        this.vals[0] = 1.0f;
        this.vals[1] = 0.0f;
        this.vals[2] = 0.0f;
        this.vals[3] = 0.0f;
        this.vals[4] = 1.0f;
        this.vals[5] = 0.0f;
        this.vals[6] = 0.0f;
        this.vals[7] = 0.0f;
        this.vals[8] = 1.0f;
        return this;
    }

    public Matrix3 mul(Matrix3 m) {
        float v00 = (this.vals[0] * m.vals[0]) + (this.vals[3] * m.vals[1]) + (this.vals[6] * m.vals[2]);
        float v01 = (this.vals[0] * m.vals[3]) + (this.vals[3] * m.vals[4]) + (this.vals[6] * m.vals[5]);
        float v02 = (this.vals[0] * m.vals[6]) + (this.vals[3] * m.vals[7]) + (this.vals[6] * m.vals[8]);
        float v10 = (this.vals[1] * m.vals[0]) + (this.vals[4] * m.vals[1]) + (this.vals[7] * m.vals[2]);
        float v11 = (this.vals[1] * m.vals[3]) + (this.vals[4] * m.vals[4]) + (this.vals[7] * m.vals[5]);
        float v12 = (this.vals[1] * m.vals[6]) + (this.vals[4] * m.vals[7]) + (this.vals[7] * m.vals[8]);
        float v20 = (this.vals[2] * m.vals[0]) + (this.vals[5] * m.vals[1]) + (this.vals[8] * m.vals[2]);
        float v21 = (this.vals[2] * m.vals[3]) + (this.vals[5] * m.vals[4]) + (this.vals[8] * m.vals[5]);
        float v22 = (this.vals[2] * m.vals[6]) + (this.vals[5] * m.vals[7]) + (this.vals[8] * m.vals[8]);
        this.vals[0] = v00;
        this.vals[1] = v10;
        this.vals[2] = v20;
        this.vals[3] = v01;
        this.vals[4] = v11;
        this.vals[5] = v21;
        this.vals[6] = v02;
        this.vals[7] = v12;
        this.vals[8] = v22;
        return this;
    }

    public Matrix3 setToRotation(float angle) {
        float angle2 = angle * 0.017453292f;
        float cos = (float) Math.cos(angle2);
        float sin = (float) Math.sin(angle2);
        this.vals[0] = cos;
        this.vals[1] = sin;
        this.vals[2] = 0.0f;
        this.vals[3] = -sin;
        this.vals[4] = cos;
        this.vals[5] = 0.0f;
        this.vals[6] = 0.0f;
        this.vals[7] = 0.0f;
        this.vals[8] = 1.0f;
        return this;
    }

    public Matrix3 setToTranslation(float x, float y) {
        this.vals[0] = 1.0f;
        this.vals[1] = 0.0f;
        this.vals[2] = 0.0f;
        this.vals[3] = 0.0f;
        this.vals[4] = 1.0f;
        this.vals[5] = 0.0f;
        this.vals[6] = x;
        this.vals[7] = y;
        this.vals[8] = 1.0f;
        return this;
    }

    public Matrix3 setToScaling(float sx, float sy) {
        this.vals[0] = sx;
        this.vals[1] = 0.0f;
        this.vals[2] = 0.0f;
        this.vals[3] = 0.0f;
        this.vals[4] = sy;
        this.vals[5] = 0.0f;
        this.vals[6] = 0.0f;
        this.vals[7] = 0.0f;
        this.vals[8] = 1.0f;
        return this;
    }

    public String toString() {
        return "[" + this.vals[0] + "|" + this.vals[3] + "|" + this.vals[6] + "]\n[" + this.vals[1] + "|" + this.vals[4] + "|" + this.vals[7] + "]\n[" + this.vals[2] + "|" + this.vals[5] + "|" + this.vals[8] + "]";
    }

    public float det() {
        return ((((((this.vals[0] * this.vals[4]) * this.vals[8]) + ((this.vals[3] * this.vals[7]) * this.vals[2])) + ((this.vals[6] * this.vals[1]) * this.vals[5])) - ((this.vals[0] * this.vals[7]) * this.vals[5])) - ((this.vals[3] * this.vals[1]) * this.vals[8])) - ((this.vals[6] * this.vals[4]) * this.vals[2]);
    }

    public Matrix3 inv() {
        float det = det();
        if (det == 0.0f) {
            throw new GdxRuntimeException("Can't invert a singular matrix");
        }
        float inv_det = 1.0f / det;
        float[] tmp = {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
        tmp[0] = (this.vals[4] * this.vals[8]) - (this.vals[5] * this.vals[7]);
        tmp[1] = (this.vals[2] * this.vals[7]) - (this.vals[1] * this.vals[8]);
        tmp[2] = (this.vals[1] * this.vals[5]) - (this.vals[2] * this.vals[4]);
        tmp[3] = (this.vals[5] * this.vals[6]) - (this.vals[3] * this.vals[8]);
        tmp[4] = (this.vals[0] * this.vals[8]) - (this.vals[2] * this.vals[6]);
        tmp[5] = (this.vals[2] * this.vals[3]) - (this.vals[0] * this.vals[5]);
        tmp[6] = (this.vals[3] * this.vals[7]) - (this.vals[4] * this.vals[6]);
        tmp[7] = (this.vals[1] * this.vals[6]) - (this.vals[0] * this.vals[7]);
        tmp[8] = (this.vals[0] * this.vals[4]) - (this.vals[1] * this.vals[3]);
        this.vals[0] = tmp[0] * inv_det;
        this.vals[1] = tmp[1] * inv_det;
        this.vals[2] = tmp[2] * inv_det;
        this.vals[3] = tmp[3] * inv_det;
        this.vals[4] = tmp[4] * inv_det;
        this.vals[5] = tmp[5] * inv_det;
        this.vals[6] = tmp[6] * inv_det;
        this.vals[7] = tmp[7] * inv_det;
        this.vals[8] = tmp[8] * inv_det;
        return this;
    }

    public Matrix3 set(Matrix3 mat) {
        this.vals[0] = mat.vals[0];
        this.vals[1] = mat.vals[1];
        this.vals[2] = mat.vals[2];
        this.vals[3] = mat.vals[3];
        this.vals[4] = mat.vals[4];
        this.vals[5] = mat.vals[5];
        this.vals[6] = mat.vals[6];
        this.vals[7] = mat.vals[7];
        this.vals[8] = mat.vals[8];
        return this;
    }

    public Matrix3 trn(Vector3 vector) {
        float[] fArr = this.vals;
        fArr[6] = fArr[6] + vector.x;
        float[] fArr2 = this.vals;
        fArr2[7] = fArr2[7] + vector.y;
        return this;
    }

    public Matrix3 trn(float x, float y) {
        float[] fArr = this.vals;
        fArr[6] = fArr[6] + x;
        float[] fArr2 = this.vals;
        fArr2[7] = fArr2[7] + y;
        return this;
    }

    public float[] getValues() {
        return this.vals;
    }
}
