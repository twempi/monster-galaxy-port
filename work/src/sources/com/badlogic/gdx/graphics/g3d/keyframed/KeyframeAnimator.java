package com.badlogic.gdx.graphics.g3d.keyframed;

import com.badlogic.gdx.graphics.g3d.Animator;
import com.badlogic.gdx.graphics.g3d.loaders.md5.MD5Quaternion;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

/* JADX INFO: loaded from: classes.dex */
public class KeyframeAnimator extends Animator {
    static MD5Quaternion jointAOrient = new MD5Quaternion();
    static MD5Quaternion jointBOrient = new MD5Quaternion();
    public static final int sStride = 8;
    private Keyframe A = null;
    private Keyframe B = null;
    private Keyframe R;
    private float invSampleRate;
    private int numMeshes;

    public Keyframe getInterpolatedKeyframe() {
        return this.R;
    }

    public KeyframeAnimator(int numMeshes, float sampleRate) {
        this.R = null;
        this.numMeshes = 0;
        this.invSampleRate = 0.0f;
        this.numMeshes = numMeshes;
        this.R = new Keyframe();
        this.R.vertices = new float[numMeshes][];
        this.R.indices = new short[numMeshes][];
        this.invSampleRate = 1.0f / sampleRate;
    }

    public void setKeyframeDimensions(int idx, int numVertices, int numIndices) {
        this.R.vertices[idx] = new float[numVertices];
        this.R.indices[idx] = new short[numIndices];
    }

    public void setNumTaggedJoints(int num) {
        this.R.taggedJointPos = new Vector3[num];
        for (int i = 0; i < num; i++) {
            this.R.taggedJointPos[i] = new Vector3();
        }
        this.R.taggedJoint = new Quaternion[num];
        for (int i2 = 0; i2 < num; i2++) {
            this.R.taggedJoint[i2] = new Quaternion(0.0f, 0.0f, 0.0f, 0.0f);
        }
    }

    @Override // com.badlogic.gdx.graphics.g3d.Animator
    protected void setInterpolationFrames() {
        this.A = ((KeyframeAnimation) this.mCurrentAnim).keyframes[this.mCurrentFrameIdx];
        this.B = ((KeyframeAnimation) this.mCurrentAnim).keyframes[this.mNextFrameIdx];
    }

    @Override // com.badlogic.gdx.graphics.g3d.Animator
    protected void interpolate() {
        if (this.mWrapMode != Animator.WrapMode.SingleFrame || !this.R.indicesSet) {
            float t = this.mFrameDelta * this.invSampleRate;
            for (int i = 0; i < this.numMeshes; i++) {
                float[] Rvertices = this.R.vertices[i];
                float[] Avertices = this.A.vertices[i];
                float[] Bvertices = this.B.vertices[i];
                for (int n = 0; n < Avertices.length; n += 8) {
                    float Ax = Avertices[n];
                    float Bx = Bvertices[n];
                    float Rx = Ax + ((Bx - Ax) * t);
                    float Ay = Avertices[n + 1];
                    float By = Bvertices[n + 1];
                    float Ry = Ay + ((By - Ay) * t);
                    float Az = Avertices[n + 2];
                    float Bz = Bvertices[n + 2];
                    float Rz = Az + ((Bz - Az) * t);
                    Rvertices[n] = Rx;
                    Rvertices[n + 1] = Ry;
                    Rvertices[n + 2] = Rz;
                    Rvertices[n + 3] = Avertices[n + 3];
                    Rvertices[n + 4] = Avertices[n + 4];
                    float Ax2 = Avertices[n + 5];
                    float Bx2 = Bvertices[n + 5];
                    float Rx2 = Ax2 + ((Bx2 - Ax2) * t);
                    float Ay2 = Avertices[n + 6];
                    float By2 = Bvertices[n + 6];
                    float Ry2 = Ay2 + ((By2 - Ay2) * t);
                    float Az2 = Avertices[n + 7];
                    float Bz2 = Bvertices[n + 7];
                    float Rz2 = Az2 + ((Bz2 - Az2) * t);
                    Rvertices[n + 5] = Rx2;
                    Rvertices[n + 6] = Ry2;
                    Rvertices[n + 7] = Rz2;
                }
                if (!this.R.indicesSet) {
                    for (int n2 = 0; n2 < this.A.indices[i].length; n2++) {
                        this.R.indices[i][n2] = this.A.indices[i][n2];
                    }
                }
            }
            this.R.indicesSet = true;
            if (this.A.taggedJoint != null) {
                interpolateJoints(t);
            }
        }
    }

    private void interpolateJoints(float t) {
        for (int tj = 0; tj < this.A.taggedJoint.length; tj++) {
            float PAX = this.A.taggedJointPos[tj].x;
            float PAY = this.A.taggedJointPos[tj].y;
            float PAZ = this.A.taggedJointPos[tj].z;
            float PBX = this.B.taggedJointPos[tj].x;
            float PBY = this.B.taggedJointPos[tj].y;
            float PBZ = this.B.taggedJointPos[tj].z;
            this.R.taggedJointPos[tj].x = ((PBX - PAX) * t) + PAX;
            this.R.taggedJointPos[tj].y = ((PBY - PAY) * t) + PAY;
            this.R.taggedJointPos[tj].z = ((PBZ - PAZ) * t) + PAZ;
            jointAOrient.x = this.A.taggedJoint[tj].x;
            jointAOrient.y = this.A.taggedJoint[tj].y;
            jointAOrient.z = this.A.taggedJoint[tj].z;
            jointAOrient.w = this.A.taggedJoint[tj].w;
            jointBOrient.x = this.B.taggedJoint[tj].x;
            jointBOrient.y = this.B.taggedJoint[tj].y;
            jointBOrient.z = this.B.taggedJoint[tj].z;
            jointBOrient.w = this.B.taggedJoint[tj].w;
            jointAOrient.slerp(jointBOrient, t);
            this.R.taggedJoint[tj].x = jointAOrient.x;
            this.R.taggedJoint[tj].y = jointAOrient.y;
            this.R.taggedJoint[tj].z = jointAOrient.z;
            this.R.taggedJoint[tj].w = jointAOrient.w;
        }
    }

    public boolean hasAnimation() {
        return this.mCurrentAnim != null;
    }
}
