package com.badlogic.gdx.physics.box2d;

import com.badlogic.gdx.math.Vector2;

/* JADX INFO: loaded from: classes.dex */
public abstract class Joint {
    protected long addr;
    protected JointEdge jointEdgeA;
    protected JointEdge jointEdgeB;
    private final World world;
    private final float[] tmp = new float[2];
    private final Vector2 anchorA = new Vector2();
    private final Vector2 anchorB = new Vector2();
    private final Vector2 reactionForce = new Vector2();

    private native void jniGetAnchorA(long j, float[] fArr);

    private native void jniGetAnchorB(long j, float[] fArr);

    private native long jniGetBodyA(long j);

    private native long jniGetBodyB(long j);

    private native void jniGetReactionForce(long j, float f, float[] fArr);

    private native float jniGetReactionTorque(long j, float f);

    private native int jniGetType(long j);

    private native boolean jniIsActive(long j);

    protected Joint(World world, long addr) {
        this.world = world;
        this.addr = addr;
    }

    public JointDef.JointType getType() {
        int type = jniGetType(this.addr);
        return (type <= 0 || type >= JointDef.JointType.valueTypes.length) ? JointDef.JointType.Unknown : JointDef.JointType.valueTypes[type];
    }

    public Body getBodyA() {
        return this.world.bodies.get(jniGetBodyA(this.addr));
    }

    public Body getBodyB() {
        return this.world.bodies.get(jniGetBodyB(this.addr));
    }

    public Vector2 getAnchorA() {
        jniGetAnchorA(this.addr, this.tmp);
        this.anchorA.x = this.tmp[0];
        this.anchorA.y = this.tmp[1];
        return this.anchorA;
    }

    public Vector2 getAnchorB() {
        jniGetAnchorB(this.addr, this.tmp);
        this.anchorB.x = this.tmp[0];
        this.anchorB.y = this.tmp[1];
        return this.anchorB;
    }

    public Vector2 getReactionForce(float inv_dt) {
        jniGetReactionForce(this.addr, inv_dt, this.tmp);
        this.reactionForce.x = this.tmp[0];
        this.reactionForce.y = this.tmp[1];
        return this.reactionForce;
    }

    public float getReactionTorque(float inv_dt) {
        return jniGetReactionTorque(this.addr, inv_dt);
    }

    public boolean isActive() {
        return jniIsActive(this.addr);
    }
}
