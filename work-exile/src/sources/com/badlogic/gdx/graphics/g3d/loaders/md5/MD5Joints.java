package com.badlogic.gdx.graphics.g3d.loaders.md5;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class MD5Joints {
    private static final int stride = 8;
    public float[] joints;
    public String[] names;
    public int numJoints;

    public void read(DataInputStream in) throws IOException {
        int numNames = in.readInt();
        this.names = new String[numNames];
        for (int i = 0; i < numNames; i++) {
            this.names[i] = in.readUTF();
        }
        this.numJoints = in.readInt();
        this.joints = new float[this.numJoints * 8];
        for (int i2 = 0; i2 < this.numJoints * 8; i2++) {
            this.joints[i2] = in.readFloat();
        }
    }

    public void write(DataOutputStream out) throws IOException {
        out.writeInt(this.names.length);
        for (int i = 0; i < this.names.length; i++) {
            out.writeUTF(this.names[i]);
        }
        out.writeInt(this.numJoints);
        for (int i2 = 0; i2 < this.numJoints * 8; i2++) {
            out.writeFloat(this.joints[i2]);
        }
    }
}
