package com.badlogic.gdx.graphics.g3d.loaders.md5;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class MD5Model {
    public MD5Joints baseSkeleton;
    public MD5Mesh[] meshes;
    public int numJoints;

    public int getNumVertices() {
        int numVertices = 0;
        for (int i = 0; i < this.meshes.length; i++) {
            numVertices += this.meshes[i].numVertices;
        }
        return numVertices;
    }

    public int getNumTriangles() {
        int numTriangles = 0;
        for (int i = 0; i < this.meshes.length; i++) {
            numTriangles += this.meshes[i].numTriangles;
        }
        return numTriangles;
    }

    public void read(DataInputStream in) throws IOException {
        this.numJoints = in.readInt();
        this.baseSkeleton = new MD5Joints();
        this.baseSkeleton.read(in);
        int numMeshes = in.readInt();
        this.meshes = new MD5Mesh[numMeshes];
        for (int i = 0; i < numMeshes; i++) {
            this.meshes[i] = new MD5Mesh();
            this.meshes[i].read(in);
        }
    }

    public void write(DataOutputStream out) throws IOException {
        out.writeInt(this.numJoints);
        this.baseSkeleton.write(out);
        out.writeInt(this.meshes.length);
        for (int i = 0; i < this.meshes.length; i++) {
            this.meshes[i].write(out);
        }
    }
}
