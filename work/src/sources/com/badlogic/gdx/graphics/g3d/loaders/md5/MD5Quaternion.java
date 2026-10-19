package com.badlogic.gdx.graphics.g3d.loaders.md5;

import com.badlogic.gdx.math.Vector3;

/* JADX INFO: loaded from: classes.dex */
public class MD5Quaternion {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final MD5Quaternion inv;
    private static final MD5Quaternion tmp;
    public float w;
    public float x;
    public float y;
    public float z;

    static {
        $assertionsDisabled = !MD5Quaternion.class.desiredAssertionStatus();
        tmp = new MD5Quaternion();
        inv = new MD5Quaternion();
    }

    public MD5Quaternion() {
    }

    public MD5Quaternion(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        computeW();
    }

    public MD5Quaternion(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public void set(MD5Quaternion q) {
        this.w = q.w;
        this.x = q.x;
        this.y = q.y;
        this.z = q.z;
    }

    public void computeW() {
        float t = ((1.0f - (this.x * this.x)) - (this.y * this.y)) - (this.z * this.z);
        if (t < 0.0f) {
            this.w = 0.0f;
        } else {
            this.w = -((float) Math.sqrt(t));
        }
    }

    public void normalize() {
        float mag = (float) Math.sqrt((this.x * this.x) + (this.y * this.y) + (this.z * this.z) + (this.w * this.w));
        if (mag > 0.0f) {
            float inv2 = 1.0f / mag;
            this.x *= inv2;
            this.y *= inv2;
            this.z *= inv2;
            this.w *= inv2;
        }
    }

    public void multiply(MD5Quaternion q) {
        float tw = (((this.w * q.w) - (this.x * q.x)) - (this.y * q.y)) - (this.z * q.z);
        float tx = (((this.x * q.w) + (this.w * q.x)) + (this.y * q.z)) - (this.z * q.y);
        float ty = (((this.y * q.w) + (this.w * q.y)) + (this.z * q.x)) - (this.x * q.z);
        float tz = (((this.z * q.w) + (this.w * q.z)) + (this.x * q.y)) - (this.y * q.x);
        this.w = tw;
        this.x = tx;
        this.y = ty;
        this.z = tz;
    }

    public void multiply(Vector3 v) {
        float tw = (((-this.x) * v.x) - (this.y * v.y)) - (this.z * v.z);
        float tx = ((this.w * v.x) + (this.y * v.z)) - (this.z * v.y);
        float ty = ((this.w * v.y) + (this.z * v.x)) - (this.x * v.z);
        float tz = ((this.w * v.z) + (this.x * v.y)) - (this.y * v.x);
        this.w = tw;
        this.x = tx;
        this.y = ty;
        this.z = tz;
    }

    public void rotate(Vector3 vec) {
        inv.x = -this.x;
        inv.y = -this.y;
        inv.z = -this.z;
        inv.w = this.w;
        tmp.set(this);
        tmp.multiply(vec);
        tmp.multiply(inv);
        vec.x = tmp.x;
        vec.y = tmp.y;
        vec.z = tmp.z;
    }

    public float dot(MD5Quaternion q) {
        return (this.x * q.x) + (this.y * q.y) + (this.z * q.z) + (this.w * q.w);
    }

    public void slerp(MD5Quaternion q, float t) {
        float k0;
        float k1;
        if (t <= 0.0f) {
            return;
        }
        if (t >= 1.0f) {
            set(q);
            return;
        }
        float cosOmega = dot(q);
        float q1w = q.w;
        float q1x = q.x;
        float q1y = q.y;
        float q1z = q.z;
        if (cosOmega < 0.0f) {
            q1w = -q1w;
            q1x = -q1x;
            q1y = -q1y;
            q1z = -q1z;
            cosOmega = -cosOmega;
        }
        if (!$assertionsDisabled && cosOmega >= 1.1f) {
            throw new AssertionError();
        }
        if (cosOmega > 0.9999f) {
            k0 = 1.0f - t;
            k1 = t;
        } else {
            float sinOmega = (float) Math.sqrt(1.0f - (cosOmega * cosOmega));
            float omega = (float) Math.atan2(sinOmega, cosOmega);
            float oneOverSinOmega = 1.0f / sinOmega;
            k0 = ((float) Math.sin((1.0f - t) * omega)) * oneOverSinOmega;
            k1 = ((float) Math.sin(t * omega)) * oneOverSinOmega;
        }
        this.w = (this.w * k0) + (k1 * q1w);
        this.x = (this.x * k0) + (k1 * q1x);
        this.y = (this.y * k0) + (k1 * q1y);
        this.z = (this.z * k0) + (k1 * q1z);
    }

    public void invert() {
        float d = (this.x * this.x) + (this.y * this.y) + (this.z * this.z) + (this.w * this.w);
        this.x /= d;
        this.y /= d;
        this.z /= d;
        this.w /= d;
    }
}
