package com.badlogic.gdx.physics.box2d;

/* JADX INFO: loaded from: classes.dex */
public class JointEdge {
    public final Joint joint;
    public final Body other;

    protected JointEdge(Body other, Joint joint) {
        this.other = other;
        this.joint = joint;
    }
}
