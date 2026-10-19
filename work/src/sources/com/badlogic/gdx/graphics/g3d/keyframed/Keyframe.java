package com.badlogic.gdx.graphics.g3d.keyframed;

import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

/* JADX INFO: loaded from: classes.dex */
public class Keyframe {
    public float[][] vertices = (float[][]) null;
    public short[][] indices = (short[][]) null;
    public boolean indicesSet = false;
    public boolean indicesSent = false;
    public Vector3[] taggedJointPos = null;
    public Quaternion[] taggedJoint = null;
}
