package com.badlogic.gdx.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public abstract class Camera {
    public final Vector3 position = new Vector3();
    public final Vector3 direction = new Vector3(0.0f, 0.0f, -1.0f);
    public final Vector3 up = new Vector3(0.0f, 1.0f, 0.0f);
    public final Matrix4 projection = new Matrix4();
    public final Matrix4 view = new Matrix4();
    public final Matrix4 combined = new Matrix4();
    public final Matrix4 invProjectionView = new Matrix4();
    public float near = 1.0f;
    public float far = 100.0f;
    public float viewportWidth = 0.0f;
    public float viewportHeight = 0.0f;
    public final Frustum frustum = new Frustum();
    private final Matrix4 tmpMat = new Matrix4();
    private final Vector3 tmpVec = new Vector3();
    final Vector3 right = new Vector3();
    final Ray ray = new Ray(new Vector3(), new Vector3());

    public abstract void update();

    public void apply(GL10 gl) {
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadMatrixf(this.projection.val, 0);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadMatrixf(this.view.val, 0);
    }

    public void lookAt(float x, float y, float z) {
        this.direction.set(x, y, z).sub(this.position).nor();
    }

    public void normalizeUp() {
        this.right.set(this.direction).crs(this.up).nor();
        this.up.set(this.right).crs(this.direction).nor();
    }

    public void rotate(float angle, float axisX, float axisY, float axisZ) {
        this.tmpMat.setToRotation(this.tmpVec.set(axisX, axisY, axisZ), angle);
        this.direction.mul(this.tmpMat).nor();
        this.up.mul(this.tmpMat).nor();
    }

    public void translate(float x, float y, float z) {
        this.position.add(x, y, z);
    }

    public void unproject(Vector3 vec, float viewportX, float viewportY, float viewportWidth, float viewportHeight) {
        float x = vec.x;
        float y = vec.y;
        float y2 = (Gdx.graphics.getHeight() - y) - 1.0f;
        vec.x = ((2.0f * (x - viewportX)) / viewportWidth) - 1.0f;
        vec.y = ((2.0f * (y2 - viewportY)) / viewportHeight) - 1.0f;
        vec.z = (vec.z * 2.0f) - 1.0f;
        vec.prj(this.invProjectionView);
    }

    public void unproject(Vector3 vec) {
        unproject(vec, 0.0f, 0.0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void project(Vector3 vec) {
        project(vec, 0.0f, 0.0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void project(Vector3 vec, float viewportX, float viewportY, float viewportWidth, float viewportHeight) {
        vec.prj(this.combined);
        vec.x = (((vec.x + 1.0f) * viewportWidth) / 2.0f) + viewportX;
        vec.y = (((vec.y + 1.0f) * viewportHeight) / 2.0f) + viewportY;
        vec.z = (vec.z + 1.0f) / 2.0f;
    }

    public Ray getPickRay(float x, float y, float viewportX, float viewportY, float viewportWidth, float viewportHeight) {
        unproject(this.ray.origin.set(x, y, 0.0f), viewportX, viewportY, viewportWidth, viewportHeight);
        unproject(this.ray.direction.set(x, y, 1.0f), viewportX, viewportY, viewportWidth, viewportHeight);
        this.ray.direction.sub(this.ray.origin).nor();
        return this.ray;
    }

    public Ray getPickRay(float x, float y) {
        return getPickRay(x, y, 0.0f, 0.0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }
}
