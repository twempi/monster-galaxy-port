package com.badlogic.gdx.math;

import com.badlogic.gdx.utils.NumberUtils;
import java.io.Serializable;

/* JADX INFO: loaded from: classes.dex */
public class Vector2 implements Serializable {
    private static final long serialVersionUID = 913902788239530931L;
    public float x;
    public float y;
    public static final Vector2 tmp = new Vector2();
    public static final Vector2 tmp2 = new Vector2();
    public static final Vector2 tmp3 = new Vector2();
    public static final Vector2 X = new Vector2(1.0f, 0.0f);
    public static final Vector2 Y = new Vector2(0.0f, 1.0f);
    public static final Vector2 Zero = new Vector2(0.0f, 0.0f);

    public Vector2() {
    }

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2(Vector2 v) {
        set(v);
    }

    public Vector2 cpy() {
        return new Vector2(this);
    }

    public float len() {
        return (float) Math.sqrt((this.x * this.x) + (this.y * this.y));
    }

    public float len2() {
        return (this.x * this.x) + (this.y * this.y);
    }

    public Vector2 set(Vector2 v) {
        this.x = v.x;
        this.y = v.y;
        return this;
    }

    public Vector2 set(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector2 sub(Vector2 v) {
        this.x -= v.x;
        this.y -= v.y;
        return this;
    }

    public Vector2 nor() {
        float len = len();
        if (len != 0.0f) {
            this.x /= len;
            this.y /= len;
        }
        return this;
    }

    public Vector2 add(Vector2 v) {
        this.x += v.x;
        this.y += v.y;
        return this;
    }

    public Vector2 add(float x, float y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public float dot(Vector2 v) {
        return (this.x * v.x) + (this.y * v.y);
    }

    public Vector2 mul(float scalar) {
        this.x *= scalar;
        this.y *= scalar;
        return this;
    }

    public Vector2 mul(float x, float y) {
        this.x *= x;
        this.y *= y;
        return this;
    }

    public Vector2 div(float value) {
        return mul(1.0f / value);
    }

    public Vector2 div(float vx, float vy) {
        return mul(1.0f / vx, 1.0f / vy);
    }

    public Vector2 div(Vector2 other) {
        return mul(1.0f / other.x, 1.0f / other.y);
    }

    public float dst(Vector2 v) {
        float x_d = v.x - this.x;
        float y_d = v.y - this.y;
        return (float) Math.sqrt((x_d * x_d) + (y_d * y_d));
    }

    public float dst(float x, float y) {
        float x_d = x - this.x;
        float y_d = y - this.y;
        return (float) Math.sqrt((x_d * x_d) + (y_d * y_d));
    }

    public float dst2(Vector2 v) {
        float x_d = v.x - this.x;
        float y_d = v.y - this.y;
        return (x_d * x_d) + (y_d * y_d);
    }

    public float dst2(float x, float y) {
        float x_d = x - this.x;
        float y_d = y - this.y;
        return (x_d * x_d) + (y_d * y_d);
    }

    public String toString() {
        return "[" + this.x + ":" + this.y + "]";
    }

    public Vector2 sub(float x, float y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    public Vector2 tmp() {
        return tmp.set(this);
    }

    public Vector2 mul(Matrix3 mat) {
        float x = (this.x * mat.val[0]) + (this.y * mat.val[3]) + mat.val[6];
        float y = (this.x * mat.val[1]) + (this.y * mat.val[4]) + mat.val[7];
        this.x = x;
        this.y = y;
        return this;
    }

    public float crs(Vector2 v) {
        return (this.x * v.y) - (this.y * v.x);
    }

    public float crs(float x, float y) {
        return (this.x * y) - (this.y * x);
    }

    public float angle() {
        float angle = ((float) Math.atan2(this.y, this.x)) * 57.295776f;
        return angle < 0.0f ? angle + 360.0f : angle;
    }

    public void setAngle(float angle) {
        set(len(), 0.0f);
        rotate(angle);
    }

    public Vector2 rotate(float degrees) {
        float rad = degrees * 0.017453292f;
        float cos = (float) Math.cos(rad);
        float sin = (float) Math.sin(rad);
        float newX = (this.x * cos) - (this.y * sin);
        float newY = (this.x * sin) + (this.y * cos);
        this.x = newX;
        this.y = newY;
        return this;
    }

    public Vector2 lerp(Vector2 target, float alpha) {
        Vector2 r = mul(1.0f - alpha);
        r.add(target.tmp().mul(alpha));
        return r;
    }

    public int hashCode() {
        int result = NumberUtils.floatToIntBits(this.x) + 31;
        return (result * 31) + NumberUtils.floatToIntBits(this.y);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass()) {
            Vector2 other = (Vector2) obj;
            return NumberUtils.floatToIntBits(this.x) == NumberUtils.floatToIntBits(other.x) && NumberUtils.floatToIntBits(this.y) == NumberUtils.floatToIntBits(other.y);
        }
        return false;
    }

    public boolean epsilonEquals(Vector2 obj, float epsilon) {
        return obj != null && Math.abs(obj.x - this.x) <= epsilon && Math.abs(obj.y - this.y) <= epsilon;
    }
}
