package com.badlogic.gdx.math.collision;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import java.io.Serializable;
import java.util.List;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class BoundingBox implements Serializable {
    private static final long serialVersionUID = -1286036817192127343L;
    final Vector3 cnt;
    final Vector3[] crn;
    boolean crn_dirty;
    final Vector3 dim;
    public final Vector3 max;
    public final Vector3 min;

    public Vector3 getCenter() {
        return this.cnt;
    }

    protected void updateCorners() {
        if (this.crn_dirty) {
            this.crn[0].set(this.min.x, this.min.y, this.min.z);
            this.crn[1].set(this.max.x, this.min.y, this.min.z);
            this.crn[2].set(this.max.x, this.max.y, this.min.z);
            this.crn[3].set(this.min.x, this.max.y, this.min.z);
            this.crn[4].set(this.min.x, this.min.y, this.max.z);
            this.crn[5].set(this.max.x, this.min.y, this.max.z);
            this.crn[6].set(this.max.x, this.max.y, this.max.z);
            this.crn[7].set(this.min.x, this.max.y, this.max.z);
            this.crn_dirty = false;
        }
    }

    public Vector3[] getCorners() {
        updateCorners();
        return this.crn;
    }

    public Vector3 getDimensions() {
        return this.dim;
    }

    public Vector3 getMin() {
        return this.min;
    }

    public synchronized Vector3 getMax() {
        return this.max;
    }

    public BoundingBox() {
        this.crn = new Vector3[8];
        this.min = new Vector3();
        this.max = new Vector3();
        this.cnt = new Vector3();
        this.dim = new Vector3();
        this.crn_dirty = true;
        this.crn_dirty = true;
        for (int l_idx = 0; l_idx < 8; l_idx++) {
            this.crn[l_idx] = new Vector3();
        }
        clr();
    }

    public BoundingBox(BoundingBox bounds) {
        this.crn = new Vector3[8];
        this.min = new Vector3();
        this.max = new Vector3();
        this.cnt = new Vector3();
        this.dim = new Vector3();
        this.crn_dirty = true;
        this.crn_dirty = true;
        for (int l_idx = 0; l_idx < 8; l_idx++) {
            this.crn[l_idx] = new Vector3();
        }
        set(bounds);
    }

    public BoundingBox(Vector3 minimum, Vector3 maximum) {
        this.crn = new Vector3[8];
        this.min = new Vector3();
        this.max = new Vector3();
        this.cnt = new Vector3();
        this.dim = new Vector3();
        this.crn_dirty = true;
        this.crn_dirty = true;
        for (int l_idx = 0; l_idx < 8; l_idx++) {
            this.crn[l_idx] = new Vector3();
        }
        set(minimum, maximum);
    }

    public BoundingBox set(BoundingBox bounds) {
        this.crn_dirty = true;
        return set(bounds.min, bounds.max);
    }

    public BoundingBox set(Vector3 minimum, Vector3 maximum) {
        this.min.set(minimum.x < maximum.x ? minimum.x : maximum.x, minimum.y < maximum.y ? minimum.y : maximum.y, minimum.z < maximum.z ? minimum.z : maximum.z);
        this.max.set(minimum.x > maximum.x ? minimum.x : maximum.x, minimum.y > maximum.y ? minimum.y : maximum.y, minimum.z > maximum.z ? minimum.z : maximum.z);
        this.cnt.set(this.min).add(this.max).mul(0.5f);
        this.dim.set(this.max).sub(this.min);
        this.crn_dirty = true;
        return this;
    }

    public BoundingBox set(Vector3[] points) {
        inf();
        for (Vector3 l_point : points) {
            ext(l_point);
        }
        this.crn_dirty = true;
        return this;
    }

    public BoundingBox set(List<Vector3> points) {
        inf();
        for (Vector3 l_point : points) {
            ext(l_point);
        }
        this.crn_dirty = true;
        return this;
    }

    public BoundingBox inf() {
        this.min.set(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
        this.max.set(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);
        this.cnt.set(0.0f, 0.0f, 0.0f);
        this.dim.set(0.0f, 0.0f, 0.0f);
        this.crn_dirty = true;
        return this;
    }

    public BoundingBox ext(Vector3 point) {
        this.crn_dirty = true;
        return set(this.min.set(min(this.min.x, point.x), min(this.min.y, point.y), min(this.min.z, point.z)), this.max.set(Math.max(this.max.x, point.x), Math.max(this.max.y, point.y), Math.max(this.max.z, point.z)));
    }

    public BoundingBox clr() {
        this.crn_dirty = true;
        return set(this.min.set(0.0f, 0.0f, 0.0f), this.max.set(0.0f, 0.0f, 0.0f));
    }

    public boolean isValid() {
        return (this.min.x == this.max.x && this.min.y == this.max.y && this.min.z == this.max.z) ? false : true;
    }

    public BoundingBox ext(BoundingBox a_bounds) {
        this.crn_dirty = true;
        return set(this.min.set(min(this.min.x, a_bounds.min.x), min(this.min.y, a_bounds.min.y), min(this.min.z, a_bounds.min.z)), this.max.set(max(this.max.x, a_bounds.max.x), max(this.max.y, a_bounds.max.y), max(this.max.z, a_bounds.max.z)));
    }

    public BoundingBox mul(Matrix4 matrix) {
        updateCorners();
        inf();
        Vector3[] arr$ = this.crn;
        for (Vector3 l_pnt : arr$) {
            l_pnt.mul(matrix);
            this.min.set(min(this.min.x, l_pnt.x), min(this.min.y, l_pnt.y), min(this.min.z, l_pnt.z));
            this.max.set(max(this.max.x, l_pnt.x), max(this.max.y, l_pnt.y), max(this.max.z, l_pnt.z));
        }
        this.crn_dirty = true;
        return set(this.min, this.max);
    }

    public boolean contains(BoundingBox bounds) {
        if (isValid()) {
            return this.min.x <= bounds.max.x && this.min.y <= bounds.max.y && this.min.z <= bounds.max.z && this.max.x >= bounds.min.x && this.max.y >= bounds.min.y && this.max.z >= bounds.min.z;
        }
        return true;
    }

    public boolean contains(Vector3 v) {
        return this.min.x <= v.x && this.max.x >= v.x && this.min.y <= v.y && this.max.y >= v.y && this.min.z <= v.z && this.max.z >= v.z;
    }

    public String toString() {
        return "[" + this.min + "|" + this.max + "]";
    }

    public BoundingBox ext(float x, float y, float z) {
        this.crn_dirty = true;
        return set(this.min.set(min(this.min.x, x), min(this.min.y, y), min(this.min.z, z)), this.max.set(max(this.max.x, x), max(this.max.y, y), max(this.max.z, z)));
    }

    static float min(float a, float b) {
        return a > b ? b : a;
    }

    static float max(float a, float b) {
        return a > b ? a : b;
    }
}
