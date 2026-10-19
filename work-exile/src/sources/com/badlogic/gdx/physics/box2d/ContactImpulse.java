package com.badlogic.gdx.physics.box2d;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class ContactImpulse {
    long addr;
    final World world;
    float[] tmp = new float[2];
    final float[] normalImpulses = new float[2];
    final float[] tangentImpulses = new float[2];

    private native void jniGetNormalImpulses(long j, float[] fArr);

    private native void jniGetTangentImpulses(long j, float[] fArr);

    protected ContactImpulse(World world, long addr) {
        this.world = world;
        this.addr = addr;
    }

    public float[] getNormalImpulses() {
        jniGetNormalImpulses(this.addr, this.normalImpulses);
        return this.normalImpulses;
    }

    public float[] getTangentImpulses() {
        jniGetTangentImpulses(this.addr, this.tangentImpulses);
        return this.tangentImpulses;
    }
}
