package com.badlogic.gdx.physics.box2d.joints;

import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class GearJointDef extends JointDef {
    public Joint joint1 = null;
    public Joint joint2 = null;
    public float ratio = 1.0f;

    public GearJointDef() {
        this.type = JointDef.JointType.GearJoint;
    }
}
