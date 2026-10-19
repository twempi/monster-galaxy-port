package com.badlogic.gdx.math;

import com.badlogic.gdx.utils.NumberUtils;
import java.io.Serializable;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Vector3 implements Serializable {
    private static final long serialVersionUID = 3840054589595372522L;
    public float x;
    public float y;
    public float z;
    private static Vector3 tmp = new Vector3();
    private static Vector3 tmp2 = new Vector3();
    private static Vector3 tmp3 = new Vector3();
    public static Vector3 X = new Vector3(1.0f, 0.0f, 0.0f);
    public static Vector3 Y = new Vector3(0.0f, 1.0f, 0.0f);
    public static Vector3 Z = new Vector3(0.0f, 0.0f, 1.0f);

    public Vector3() {
    }

    public Vector3(float x, float y, float z) {
        set(x, y, z);
    }

    public Vector3(Vector3 vector) {
        set(vector);
    }

    public Vector3(float[] values) {
        set(values[0], values[1], values[2]);
    }

    public Vector3 set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Vector3 set(Vector3 vector) {
        return set(vector.x, vector.y, vector.z);
    }

    public Vector3 set(float[] values) {
        return set(values[0], values[1], values[2]);
    }

    public Vector3 cpy() {
        return new Vector3(this);
    }

    public Vector3 tmp() {
        return tmp.set(this);
    }

    public Vector3 tmp2() {
        return tmp2.set(this);
    }

    Vector3 tmp3() {
        return tmp3.set(this);
    }

    public Vector3 add(Vector3 vector) {
        return add(vector.x, vector.y, vector.z);
    }

    public Vector3 add(float x, float y, float z) {
        return set(this.x + x, this.y + y, this.z + z);
    }

    public Vector3 add(float values) {
        return set(this.x + values, this.y + values, this.z + values);
    }

    public Vector3 sub(Vector3 a_vec) {
        return sub(a_vec.x, a_vec.y, a_vec.z);
    }

    public Vector3 sub(float x, float y, float z) {
        return set(this.x - x, this.y - y, this.z - z);
    }

    public Vector3 sub(float value) {
        return set(this.x - value, this.y - value, this.z - value);
    }

    public Vector3 mul(float value) {
        return set(this.x * value, this.y * value, this.z * value);
    }

    public Vector3 div(float value) {
        float d = 1.0f / value;
        return set(this.x * d, this.y * d, this.z * d);
    }

    public float len() {
        return (float) Math.sqrt((this.x * this.x) + (this.y * this.y) + (this.z * this.z));
    }

    public float len2() {
        return (this.x * this.x) + (this.y * this.y) + (this.z * this.z);
    }

    public boolean idt(Vector3 vector) {
        return this.x == vector.x && this.y == vector.y && this.z == vector.z;
    }

    public float dst(Vector3 vector) {
        float a = vector.x - this.x;
        float b = vector.y - this.y;
        float c = vector.z - this.z;
        return (float) Math.sqrt((a * a) + (b * b) + (c * c));
    }

    public Vector3 nor() {
        float len = len();
        return len == 0.0f ? this : div(len);
    }

    public float dot(Vector3 vector) {
        return (this.x * vector.x) + (this.y * vector.y) + (this.z * vector.z);
    }

    public Vector3 crs(Vector3 vector) {
        return set((this.y * vector.z) - (this.z * vector.y), (this.z * vector.x) - (this.x * vector.z), (this.x * vector.y) - (this.y * vector.x));
    }

    public Vector3 crs(float x, float y, float z) {
        return set((this.y * z) - (this.z * y), (this.z * x) - (this.x * z), (this.x * y) - (this.y * x));
    }

    public Vector3 mul(Matrix4 matrix) {
        float[] l_mat = matrix.val;
        return set((this.x * l_mat[0]) + (this.y * l_mat[4]) + (this.z * l_mat[8]) + l_mat[12], (this.x * l_mat[1]) + (this.y * l_mat[5]) + (this.z * l_mat[9]) + l_mat[13], (this.x * l_mat[2]) + (this.y * l_mat[6]) + (this.z * l_mat[10]) + l_mat[14]);
    }

    public Vector3 prj(Matrix4 matrix) {
        float[] l_mat = matrix.val;
        float l_w = (this.x * l_mat[3]) + (this.y * l_mat[7]) + (this.z * l_mat[11]) + l_mat[15];
        return set(((((this.x * l_mat[0]) + (this.y * l_mat[4])) + (this.z * l_mat[8])) + l_mat[12]) / l_w, ((((this.x * l_mat[1]) + (this.y * l_mat[5])) + (this.z * l_mat[9])) + l_mat[13]) / l_w, ((((this.x * l_mat[2]) + (this.y * l_mat[6])) + (this.z * l_mat[10])) + l_mat[14]) / l_w);
    }

    public Vector3 rot(Matrix4 matrix) {
        float[] l_mat = matrix.val;
        return set((this.x * l_mat[0]) + (this.y * l_mat[4]) + (this.z * l_mat[8]), (this.x * l_mat[1]) + (this.y * l_mat[5]) + (this.z * l_mat[9]), (this.x * l_mat[2]) + (this.y * l_mat[6]) + (this.z * l_mat[10]));
    }

    public boolean isUnit() {
        return len() == 1.0f;
    }

    public boolean isZero() {
        return this.x == 0.0f && this.y == 0.0f && this.z == 0.0f;
    }

    public Vector3 lerp(Vector3 target, float alpha) {
        Vector3 r = mul(1.0f - alpha);
        r.add(target.tmp().mul(alpha));
        return r;
    }

    public Vector3 slerp(Vector3 target, float alpha) {
        float dot = dot(target);
        if (dot > 0.99995d || dot < 0.9995d) {
            add(target.tmp().sub(this).mul(alpha));
            nor();
            return this;
        }
        if (dot > 1.0f) {
            dot = 1.0f;
        }
        if (dot < -1.0f) {
            dot = -1.0f;
        }
        float theta0 = (float) Math.acos(dot);
        float theta = theta0 * alpha;
        Vector3 v2 = target.tmp().sub(this.x * dot, this.y * dot, this.z * dot);
        v2.nor();
        return mul((float) Math.cos(theta)).add(v2.mul((float) Math.sin(theta))).nor();
    }

    public String toString() {
        return this.x + "," + this.y + "," + this.z;
    }

    public float dot(float x, float y, float z) {
        return (this.x * x) + (this.y * y) + (this.z * z);
    }

    public float dst2(Vector3 point) {
        float a = point.x - this.x;
        float b = point.y - this.y;
        float c = point.z - this.z;
        return (a * a) + (b * b) + (c * c);
    }

    public float dst2(float x, float y, float z) {
        float a = x - this.x;
        float b = y - this.y;
        float c = z - this.z;
        return (a * a) + (b * b) + (c * c);
    }

    public float dst(float x, float y, float z) {
        return (float) Math.sqrt(dst2(x, y, z));
    }

    public int hashCode() {
        int result = NumberUtils.floatToIntBits(this.x) + 31;
        return (((result * 31) + NumberUtils.floatToIntBits(this.y)) * 31) + NumberUtils.floatToIntBits(this.z);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass()) {
            Vector3 other = (Vector3) obj;
            return NumberUtils.floatToIntBits(this.x) == NumberUtils.floatToIntBits(other.x) && NumberUtils.floatToIntBits(this.y) == NumberUtils.floatToIntBits(other.y) && NumberUtils.floatToIntBits(this.z) == NumberUtils.floatToIntBits(other.z);
        }
        return false;
    }

    public Vector3 scale(float scalarX, float scalarY, float scalarZ) {
        this.x *= scalarX;
        this.y *= scalarY;
        this.z *= scalarZ;
        return this;
    }
}
