package com.badlogic.gdx.math;

import java.io.Serializable;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Quaternion implements Serializable {
    private static final float NORMALIZATION_TOLERANCE = 1.0E-5f;
    private static final long serialVersionUID = -7661875440774897168L;
    private static Quaternion tmp1 = new Quaternion(0.0f, 0.0f, 0.0f, 0.0f);
    private static Quaternion tmp2 = new Quaternion(0.0f, 0.0f, 0.0f, 0.0f);
    public float w;
    public float x;
    public float y;
    public float z;

    public Quaternion(float x, float y, float z, float w) {
        set(x, y, z, w);
    }

    Quaternion() {
    }

    public Quaternion(Quaternion quaternion) {
        set(quaternion);
    }

    public Quaternion(Vector3 axis, float angle) {
        set(axis, angle);
    }

    public Quaternion set(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        return this;
    }

    public Quaternion set(Quaternion quaternion) {
        return set(quaternion.x, quaternion.y, quaternion.z, quaternion.w);
    }

    public Quaternion set(Vector3 axis, float angle) {
        float l_ang = (float) Math.toRadians(angle);
        float l_sin = (float) Math.sin(l_ang / 2.0f);
        float l_cos = (float) Math.cos(l_ang / 2.0f);
        return set(axis.x * l_sin, axis.y * l_sin, axis.z * l_sin, l_cos).nor();
    }

    public Quaternion cpy() {
        return new Quaternion(this);
    }

    public float len() {
        return (float) Math.sqrt((this.x * this.x) + (this.y * this.y) + (this.z * this.z) + (this.w * this.w));
    }

    public String toString() {
        return "[" + this.x + "|" + this.y + "|" + this.z + "|" + this.w + "]";
    }

    public Quaternion setEulerAngles(float yaw, float pitch, float roll) {
        float yaw2 = (float) Math.toRadians(yaw);
        float pitch2 = (float) Math.toRadians(pitch);
        float num9 = ((float) Math.toRadians(roll)) * 0.5f;
        float num6 = (float) Math.sin(num9);
        float num5 = (float) Math.cos(num9);
        float num8 = pitch2 * 0.5f;
        float num4 = (float) Math.sin(num8);
        float num3 = (float) Math.cos(num8);
        float num7 = yaw2 * 0.5f;
        float num2 = (float) Math.sin(num7);
        float num = (float) Math.cos(num7);
        this.x = (num * num4 * num5) + (num2 * num3 * num6);
        this.y = ((num2 * num3) * num5) - ((num * num4) * num6);
        this.z = ((num * num3) * num6) - ((num2 * num4) * num5);
        this.w = (num * num3 * num5) + (num2 * num4 * num6);
        return this;
    }

    public float len2() {
        return (this.x * this.x) + (this.y * this.y) + (this.z * this.z) + (this.w * this.w);
    }

    public Quaternion nor() {
        float len = len2();
        if (len != 0.0f && Math.abs(len - 1.0f) > NORMALIZATION_TOLERANCE) {
            float len2 = (float) Math.sqrt(len);
            this.w /= len2;
            this.x /= len2;
            this.y /= len2;
            this.z /= len2;
        }
        return this;
    }

    public Quaternion conjugate() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
        return this;
    }

    public void transform(Vector3 v) {
        tmp2.set(this);
        tmp2.conjugate();
        tmp2.mulLeft(tmp1.set(v.x, v.y, v.z, 0.0f)).mulLeft(this);
        v.x = tmp2.x;
        v.y = tmp2.y;
        v.z = tmp2.z;
    }

    public Quaternion mul(Quaternion q) {
        float newX = (((this.w * q.x) + (this.x * q.w)) + (this.y * q.z)) - (this.z * q.y);
        float newY = (((this.w * q.y) + (this.y * q.w)) + (this.z * q.x)) - (this.x * q.z);
        float newZ = (((this.w * q.z) + (this.z * q.w)) + (this.x * q.y)) - (this.y * q.x);
        float newW = (((this.w * q.w) - (this.x * q.x)) - (this.y * q.y)) - (this.z * q.z);
        this.x = newX;
        this.y = newY;
        this.z = newZ;
        this.w = newW;
        return this;
    }

    public Quaternion mulLeft(Quaternion q) {
        float newX = (((q.w * this.x) + (q.x * this.w)) + (q.y * this.z)) - (q.z * this.y);
        float newY = (((q.w * this.y) + (q.y * this.w)) + (q.z * this.x)) - (q.x * this.z);
        float newZ = (((q.w * this.z) + (q.z * this.w)) + (q.x * this.y)) - (q.y * this.x);
        float newW = (((q.w * this.w) - (q.x * this.x)) - (q.y * this.y)) - (q.z * this.z);
        this.x = newX;
        this.y = newY;
        this.z = newZ;
        this.w = newW;
        return this;
    }

    public void toMatrix(float[] matrix) {
        float xx = this.x * this.x;
        float xy = this.x * this.y;
        float xz = this.x * this.z;
        float xw = this.x * this.w;
        float yy = this.y * this.y;
        float yz = this.y * this.z;
        float yw = this.y * this.w;
        float zz = this.z * this.z;
        float zw = this.z * this.w;
        matrix[0] = 1.0f - ((yy + zz) * 2.0f);
        matrix[4] = (xy - zw) * 2.0f;
        matrix[8] = (xz + yw) * 2.0f;
        matrix[12] = 0.0f;
        matrix[1] = (xy + zw) * 2.0f;
        matrix[5] = 1.0f - ((xx + zz) * 2.0f);
        matrix[9] = (yz - xw) * 2.0f;
        matrix[13] = 0.0f;
        matrix[2] = (xz - yw) * 2.0f;
        matrix[6] = (yz + xw) * 2.0f;
        matrix[10] = 1.0f - ((xx + yy) * 2.0f);
        matrix[14] = 0.0f;
        matrix[3] = 0.0f;
        matrix[7] = 0.0f;
        matrix[11] = 0.0f;
        matrix[15] = 1.0f;
    }

    public static Quaternion idt() {
        return new Quaternion(0.0f, 0.0f, 0.0f, 1.0f);
    }

    public Quaternion setFromAxis(Vector3 axis, float angle) {
        return setFromAxis(axis.x, axis.y, axis.z, angle);
    }

    public Quaternion setFromAxis(float x, float y, float z, float angle) {
        float l_ang = angle * 0.017453292f;
        float l_sin = MathUtils.sin(l_ang / 2.0f);
        float l_cos = MathUtils.cos(l_ang / 2.0f);
        return set(x * l_sin, y * l_sin, z * l_sin, l_cos).nor();
    }

    public Quaternion setFromMatrix(Matrix4 matrix) {
        return setFromAxes(matrix.val[0], matrix.val[4], matrix.val[8], matrix.val[1], matrix.val[5], matrix.val[9], matrix.val[2], matrix.val[6], matrix.val[10]);
    }

    public Quaternion setFromAxes(float xx, float xy, float xz, float yx, float yy, float yz, float zx, float zy, float zz) {
        double z;
        double x;
        double y;
        double w;
        float t = xx + yy + zz;
        if (t >= 0.0f) {
            double s = Math.sqrt(1.0f + t);
            w = 0.5d * s;
            double s2 = 0.5d / s;
            x = ((double) (yz - zy)) * s2;
            y = ((double) (zx - xz)) * s2;
            z = ((double) (xy - yx)) * s2;
        } else if (xx > yy && xx > zz) {
            double s3 = Math.sqrt(((1.0d + ((double) xx)) - ((double) yy)) - ((double) zz));
            x = s3 * 0.5d;
            double s4 = 0.5d / s3;
            y = ((double) (xy + yx)) * s4;
            z = ((double) (zx + xz)) * s4;
            w = ((double) (yz - zy)) * s4;
        } else if (yy > zz) {
            double s5 = Math.sqrt(((1.0d + ((double) yy)) - ((double) xx)) - ((double) zz));
            y = s5 * 0.5d;
            double s6 = 0.5d / s5;
            x = ((double) (xy + yx)) * s6;
            z = ((double) (yz + zy)) * s6;
            w = ((double) (zx - xz)) * s6;
        } else {
            double s7 = Math.sqrt(((1.0d + ((double) zz)) - ((double) xx)) - ((double) yy));
            z = s7 * 0.5d;
            double s8 = 0.5d / s7;
            x = ((double) (zx + xz)) * s8;
            y = ((double) (yz + zy)) * s8;
            w = ((double) (xy - yx)) * s8;
        }
        return set((float) x, (float) y, (float) z, (float) w);
    }

    public Quaternion slerp(Quaternion end, float alpha) {
        if (!equals(end)) {
            float result = dot(end);
            if (result < 0.0d) {
                end.mul(-1.0f);
                result = -result;
            }
            float scale0 = 1.0f - alpha;
            float scale1 = alpha;
            if (1.0f - result > 0.1d) {
                double theta = Math.acos(result);
                double invSinTheta = 1.0d / Math.sin(theta);
                scale0 = (float) (Math.sin(((double) (1.0f - alpha)) * theta) * invSinTheta);
                scale1 = (float) (Math.sin(((double) alpha) * theta) * invSinTheta);
            }
            float x = (this.x * scale0) + (end.x * scale1);
            float y = (this.y * scale0) + (end.y * scale1);
            float z = (this.z * scale0) + (end.z * scale1);
            float w = (this.w * scale0) + (end.w * scale1);
            set(x, y, z, w);
        }
        return this;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Quaternion)) {
            return false;
        }
        Quaternion comp = (Quaternion) o;
        return this.x == comp.x && this.y == comp.y && this.z == comp.z && this.w == comp.w;
    }

    public float dot(Quaternion other) {
        return (this.x * other.x) + (this.y * other.y) + (this.z * other.z) + (this.w * other.w);
    }

    public Quaternion mul(float scalar) {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
        this.w *= scalar;
        return this;
    }
}
