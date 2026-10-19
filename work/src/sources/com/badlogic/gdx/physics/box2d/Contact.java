package com.badlogic.gdx.physics.box2d;

import com.badlogic.gdx.math.Vector2;

/* JADX INFO: loaded from: classes.dex */
public class Contact {
    protected long addr;
    protected World world;
    protected final WorldManifold worldManifold = new WorldManifold();
    private final float[] tmp = new float[6];

    private native int jniGetChildIndexA(long j);

    private native int jniGetChildIndexB(long j);

    private native long jniGetFixtureA(long j);

    private native long jniGetFixtureB(long j);

    private native float jniGetFriction(long j);

    private native float jniGetRestitution(long j);

    private native int jniGetWorldManifold(long j, float[] fArr);

    private native boolean jniIsEnabled(long j);

    private native boolean jniIsTouching(long j);

    private native void jniResetFriction(long j);

    private native void jniResetRestitution(long j);

    private native void jniSetEnabled(long j, boolean z);

    private native void jniSetFriction(long j, float f);

    private native void jniSetRestitution(long j, float f);

    protected Contact(World world, long addr) {
        this.addr = addr;
        this.world = world;
    }

    public WorldManifold getWorldManifold() {
        int numContactPoints = jniGetWorldManifold(this.addr, this.tmp);
        this.worldManifold.numContactPoints = numContactPoints;
        this.worldManifold.normal.set(this.tmp[0], this.tmp[1]);
        for (int i = 0; i < numContactPoints; i++) {
            Vector2 point = this.worldManifold.points[i];
            point.x = this.tmp[(i * 2) + 2];
            point.y = this.tmp[(i * 2) + 2 + 1];
        }
        return this.worldManifold;
    }

    public boolean isTouching() {
        return jniIsTouching(this.addr);
    }

    public void setEnabled(boolean flag) {
        jniSetEnabled(this.addr, flag);
    }

    public boolean isEnabled() {
        return jniIsEnabled(this.addr);
    }

    public Fixture getFixtureA() {
        return this.world.fixtures.get(jniGetFixtureA(this.addr));
    }

    public Fixture getFixtureB() {
        return this.world.fixtures.get(jniGetFixtureB(this.addr));
    }

    public int getChildIndexA() {
        return jniGetChildIndexA(this.addr);
    }

    public int getChildIndexB() {
        return jniGetChildIndexB(this.addr);
    }

    public void setFriction(float friction) {
        jniSetFriction(this.addr, friction);
    }

    public float getFriction() {
        return jniGetFriction(this.addr);
    }

    public void resetFriction() {
        jniResetFriction(this.addr);
    }

    public void setRestitution(float restitution) {
        jniSetRestitution(this.addr, restitution);
    }

    public float getRestitution() {
        return jniGetRestitution(this.addr);
    }

    public void ResetRestitution() {
        jniResetRestitution(this.addr);
    }
}
