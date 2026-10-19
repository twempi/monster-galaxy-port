package com.badlogic.gdx.physics.box2d;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class JointDef {
    public JointType type = JointType.Unknown;
    public Body bodyA = null;
    public Body bodyB = null;
    public boolean collideConnected = false;

    public enum JointType {
        Unknown(0),
        RevoluteJoint(1),
        PrismaticJoint(2),
        DistanceJoint(3),
        PulleyJoint(4),
        MouseJoint(5),
        GearJoint(6),
        LineJoint(7),
        WeldJoint(8),
        FrictionJoint(9);

        private int value;
        public static JointType[] valueTypes = {Unknown, RevoluteJoint, PrismaticJoint, DistanceJoint, PulleyJoint, MouseJoint, GearJoint, LineJoint, WeldJoint, FrictionJoint};

        JointType(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }
}
