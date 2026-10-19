package com.badlogic.gdx.graphics;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class OrthographicCamera extends Camera {
    public float zoom = 1.0f;
    private final Vector3 tmp = new Vector3();

    public OrthographicCamera() {
        this.near = 0.0f;
    }

    public OrthographicCamera(float viewportWidth, float viewportHeight) {
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
        this.near = 0.0f;
        update();
    }

    public OrthographicCamera(float viewportWidth, float viewportHeight, float diamondAngle) {
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
        this.near = 0.0f;
        findDirectionForIsoView(diamondAngle, 1.0E-8f, 20);
        update();
    }

    public void findDirectionForIsoView(float targetAngle, float epsilon, int maxIterations) {
        float start = targetAngle - 5.0f;
        float end = targetAngle + 5.0f;
        float mid = targetAngle;
        int iterations = 0;
        float aMid = 0.0f;
        while (Math.abs(targetAngle - aMid) > epsilon) {
            int iterations2 = iterations + 1;
            if (iterations >= maxIterations) {
                break;
            }
            aMid = calculateAngle(mid);
            if (targetAngle < aMid) {
                end = mid;
            } else {
                start = mid;
            }
            mid = start + ((end - start) / 2.0f);
            iterations = iterations2;
        }
        this.position.set(calculateDirection(mid));
        this.position.y = -this.position.y;
        lookAt(0.0f, 0.0f, 0.0f);
        normalizeUp();
    }

    private float calculateAngle(float a) {
        Vector3 camPos = calculateDirection(a);
        this.position.set(camPos.mul(30.0f));
        lookAt(0.0f, 0.0f, 0.0f);
        normalizeUp();
        update();
        Vector3 orig = new Vector3(0.0f, 0.0f, 0.0f);
        Vector3 vec = new Vector3(1.0f, 0.0f, 0.0f);
        project(orig);
        project(vec);
        Vector2 d = new Vector2(vec.x - orig.x, -(vec.y - orig.y));
        return d.angle();
    }

    private Vector3 calculateDirection(float angle) {
        Matrix4 transform = new Matrix4();
        Vector3 dir = new Vector3(-1.0f, 0.0f, 1.0f).nor();
        transform.setToRotation(new Vector3(1.0f, 0.0f, 1.0f).nor(), angle);
        dir.mul(transform).nor();
        return dir;
    }

    @Override // com.badlogic.gdx.graphics.Camera
    public void update() {
        this.projection.setToOrtho((this.zoom * (-this.viewportWidth)) / 2.0f, (this.zoom * this.viewportWidth) / 2.0f, (this.zoom * (-this.viewportHeight)) / 2.0f, (this.zoom * this.viewportHeight) / 2.0f, Math.abs(this.near), Math.abs(this.far));
        this.view.setToLookAt(this.position, this.tmp.set(this.position).add(this.direction), this.up);
        this.combined.set(this.projection);
        Matrix4.mul(this.combined.val, this.view.val);
        this.invProjectionView.set(this.combined);
        Matrix4.inv(this.invProjectionView.val);
        this.frustum.update(this.invProjectionView);
    }
}
