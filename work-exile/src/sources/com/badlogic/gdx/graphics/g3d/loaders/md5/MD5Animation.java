package com.badlogic.gdx.graphics.g3d.loaders.md5;

import com.badlogic.gdx.graphics.g3d.Animation;
import com.badlogic.gdx.math.collision.BoundingBox;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class MD5Animation extends Animation {
    static MD5Quaternion jointAOrient = new MD5Quaternion();
    static MD5Quaternion jointBOrient = new MD5Quaternion();
    public BoundingBox[] bounds;
    public int frameRate;
    public MD5Joints[] frames;
    public String name;
    public float secondsPerFrame;

    public void write(DataOutputStream out) throws IOException {
        out.writeUTF(this.name);
        out.writeInt(this.frameRate);
        out.writeFloat(this.secondsPerFrame);
        out.writeInt(this.frames.length);
        for (int i = 0; i < this.frames.length; i++) {
            this.frames[i].write(out);
        }
        out.writeInt(this.bounds.length);
        for (int i2 = 0; i2 < this.bounds.length; i2++) {
            out.writeFloat(this.bounds[i2].min.x);
            out.writeFloat(this.bounds[i2].min.y);
            out.writeFloat(this.bounds[i2].min.z);
            out.writeFloat(this.bounds[i2].max.x);
            out.writeFloat(this.bounds[i2].max.y);
            out.writeFloat(this.bounds[i2].max.z);
        }
    }

    public void read(DataInputStream in) throws IOException {
        this.name = in.readUTF();
        this.frameRate = in.readInt();
        this.secondsPerFrame = in.readFloat();
        int numFrames = in.readInt();
        this.frames = new MD5Joints[numFrames];
        for (int i = 0; i < numFrames; i++) {
            this.frames[i] = new MD5Joints();
            this.frames[i].read(in);
        }
        int numBounds = in.readInt();
        this.bounds = new BoundingBox[numBounds];
        for (int i2 = 0; i2 < numBounds; i2++) {
            this.bounds[i2] = new BoundingBox();
            this.bounds[i2].min.x = in.readFloat();
            this.bounds[i2].min.y = in.readFloat();
            this.bounds[i2].min.z = in.readFloat();
            this.bounds[i2].max.x = in.readFloat();
            this.bounds[i2].max.y = in.readFloat();
            this.bounds[i2].max.z = in.readFloat();
        }
    }

    public static void interpolate(MD5Joints skeletonA, MD5Joints skeletonB, MD5Joints skeletonOut, float t) {
        int i = 0;
        int idx = 0;
        while (i < skeletonA.numJoints) {
            float jointAPosX = skeletonA.joints[idx + 1];
            float jointAPosY = skeletonA.joints[idx + 2];
            float jointAPosZ = skeletonA.joints[idx + 3];
            jointAOrient.x = skeletonA.joints[idx + 4];
            jointAOrient.y = skeletonA.joints[idx + 5];
            jointAOrient.z = skeletonA.joints[idx + 6];
            jointAOrient.w = skeletonA.joints[idx + 7];
            float jointBPosX = skeletonB.joints[idx + 1];
            float jointBPosY = skeletonB.joints[idx + 2];
            float jointBPosZ = skeletonB.joints[idx + 3];
            jointBOrient.x = skeletonB.joints[idx + 4];
            jointBOrient.y = skeletonB.joints[idx + 5];
            jointBOrient.z = skeletonB.joints[idx + 6];
            jointBOrient.w = skeletonB.joints[idx + 7];
            skeletonOut.joints[idx] = skeletonA.joints[idx];
            skeletonOut.joints[idx + 1] = ((jointBPosX - jointAPosX) * t) + jointAPosX;
            skeletonOut.joints[idx + 2] = ((jointBPosY - jointAPosY) * t) + jointAPosY;
            skeletonOut.joints[idx + 3] = ((jointBPosZ - jointAPosZ) * t) + jointAPosZ;
            jointAOrient.slerp(jointBOrient, t);
            skeletonOut.joints[idx + 4] = jointAOrient.x;
            skeletonOut.joints[idx + 5] = jointAOrient.y;
            skeletonOut.joints[idx + 6] = jointAOrient.z;
            skeletonOut.joints[idx + 7] = jointAOrient.w;
            i++;
            idx += 8;
        }
    }

    @Override // com.badlogic.gdx.graphics.g3d.Animation
    public float getLength() {
        return this.frames.length * this.secondsPerFrame;
    }

    @Override // com.badlogic.gdx.graphics.g3d.Animation
    public int getNumFrames() {
        return this.frames.length;
    }
}
