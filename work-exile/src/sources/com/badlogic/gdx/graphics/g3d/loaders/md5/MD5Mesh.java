package com.badlogic.gdx.graphics.g3d.loaders.md5;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class MD5Mesh {
    public int floatsPerVertex;
    public int floatsPerWeight;
    public short[] indices;
    public int numTriangles;
    public int numVertices;
    public int numWeights;
    public String shader;
    public float[] vertices;
    public float[] weights;
    static Vector3 bn = new Vector3();
    static Vector3 _A = new Vector3();
    static Vector3 _B = new Vector3();
    static Vector3 _n = new Vector3();
    static MD5Quaternion quat = new MD5Quaternion();
    static Vector3 vn = new Vector3();

    public float[] createVertexArray() {
        return createVertexArray(5);
    }

    public float[] createVertexArray(int stride) {
        float[] verts = new float[this.numVertices * stride];
        int vertex_stride = this.floatsPerVertex;
        int j = 0;
        for (int i = 0; i < this.vertices.length; i += vertex_stride) {
            int j2 = j + 3;
            int j3 = j2 + 1;
            verts[j2] = this.vertices[i];
            j = j3 + 1;
            verts[j3] = this.vertices[i + 1];
            if (stride == 8) {
                j += 3;
            }
        }
        return verts;
    }

    public short[] getIndices() {
        return this.indices;
    }

    public void calculateVerticesN(MD5Joints skeleton, float[] verts, BoundingBox bbox) {
        calculateVerticesN(skeleton, this.weights, this.vertices, verts, this.floatsPerVertex, this.floatsPerWeight, bbox);
    }

    public void calculateVertices(MD5Joints skeleton, float[] verts, BoundingBox bbox) {
        calculateVertices(skeleton, this.weights, this.vertices, verts, this.floatsPerVertex, this.floatsPerWeight, bbox);
    }

    public static void calculateVerticesN(MD5Joints skeleton, float[] weights, float[] vertices, float[] verts, int vstride, int wstride, BoundingBox bbox) {
        int vertexOffset = 2;
        int k = 0;
        while (vertexOffset < vertices.length) {
            float finalX = 0.0f;
            float finalY = 0.0f;
            float finalZ = 0.0f;
            int weightOffset = (int) vertices[vertexOffset];
            int weightCount = (int) vertices[vertexOffset + 1];
            bn.set(vertices[vertexOffset + 2], vertices[vertexOffset + 3], vertices[vertexOffset + 4]);
            int weightOffset2 = weightOffset * wstride;
            for (int j = 0; j < weightCount; j++) {
                int weightOffset3 = weightOffset2 + 1;
                int jointOffset = ((int) weights[weightOffset2]) << 3;
                int weightOffset4 = weightOffset3 + 1;
                float bias = weights[weightOffset3];
                int weightOffset5 = weightOffset4 + 1;
                float vx = weights[weightOffset4];
                int weightOffset6 = weightOffset5 + 1;
                float vy = weights[weightOffset5];
                int weightOffset7 = weightOffset6 + 1;
                float vz = weights[weightOffset6];
                Vector3 vector3 = vn;
                int weightOffset8 = weightOffset7 + 1;
                float f = weights[weightOffset7];
                int weightOffset9 = weightOffset8 + 1;
                float f2 = weights[weightOffset8];
                weightOffset2 = weightOffset9 + 1;
                vector3.set(f, f2, weights[weightOffset9]);
                float qx = skeleton.joints[jointOffset + 4];
                float qy = skeleton.joints[jointOffset + 5];
                float qz = skeleton.joints[jointOffset + 6];
                float qw = skeleton.joints[jointOffset + 7];
                quat.x = qx;
                quat.y = qy;
                quat.z = qz;
                quat.w = qw;
                quat.rotate(vn);
                vn.mul(bias);
                bn.add(vn);
                float ix = -qx;
                float iy = -qy;
                float iz = -qz;
                float tw = (((-qx) * vx) - (qy * vy)) - (qz * vz);
                float tx = ((qw * vx) + (qy * vz)) - (qz * vy);
                float ty = ((qw * vy) + (qz * vx)) - (qx * vz);
                float tz = ((qw * vz) + (qx * vy)) - (qy * vx);
                finalX += (skeleton.joints[jointOffset + 1] + ((((tx * qw) + (tw * ix)) + (ty * iz)) - (tz * iy))) * bias;
                finalY += (skeleton.joints[jointOffset + 2] + ((((ty * qw) + (tw * iy)) + (tz * ix)) - (tx * iz))) * bias;
                finalZ += (skeleton.joints[jointOffset + 3] + ((((tz * qw) + (tw * iz)) + (tx * iy)) - (ty * ix))) * bias;
            }
            bbox.ext(finalX, finalY, finalZ);
            int k2 = k + 1;
            verts[k] = finalX;
            int k3 = k2 + 1;
            verts[k2] = finalY;
            verts[k3] = finalZ;
            int k4 = k3 + 1 + 2;
            bn.nor();
            int k5 = k4 + 1;
            verts[k4] = bn.x;
            int k6 = k5 + 1;
            verts[k5] = bn.y;
            verts[k6] = bn.z;
            vertexOffset += vstride;
            k = k6 + 1;
        }
    }

    public static void calculateVertices(MD5Joints skeleton, float[] weights, float[] vertices, float[] verts, int vstride, int wstride, BoundingBox bbox) {
        int vertexOffset = 2;
        int k = 0;
        while (vertexOffset < vertices.length) {
            float finalX = 0.0f;
            float finalY = 0.0f;
            float finalZ = 0.0f;
            int weightOffset = (int) vertices[vertexOffset];
            int weightCount = (int) vertices[vertexOffset + 1];
            int weightOffset2 = weightOffset + (weightOffset << 2);
            int j = 0;
            while (true) {
                int weightOffset3 = weightOffset2;
                if (j < weightCount) {
                    int weightOffset4 = weightOffset3 + 1;
                    int jointOffset = ((int) weights[weightOffset3]) << 3;
                    int weightOffset5 = weightOffset4 + 1;
                    float bias = weights[weightOffset4];
                    int weightOffset6 = weightOffset5 + 1;
                    float vx = weights[weightOffset5];
                    int weightOffset7 = weightOffset6 + 1;
                    float vy = weights[weightOffset6];
                    weightOffset2 = weightOffset7 + 1;
                    float vz = weights[weightOffset7];
                    float qx = skeleton.joints[jointOffset + 4];
                    float qy = skeleton.joints[jointOffset + 5];
                    float qz = skeleton.joints[jointOffset + 6];
                    float qw = skeleton.joints[jointOffset + 7];
                    float ix = -qx;
                    float iy = -qy;
                    float iz = -qz;
                    float tw = (((-qx) * vx) - (qy * vy)) - (qz * vz);
                    float tx = ((qw * vx) + (qy * vz)) - (qz * vy);
                    float ty = ((qw * vy) + (qz * vx)) - (qx * vz);
                    float tz = ((qw * vz) + (qx * vy)) - (qy * vx);
                    finalX += (skeleton.joints[jointOffset + 1] + ((((tx * qw) + (tw * ix)) + (ty * iz)) - (tz * iy))) * bias;
                    finalY += (skeleton.joints[jointOffset + 2] + ((((ty * qw) + (tw * iy)) + (tz * ix)) - (tx * iz))) * bias;
                    finalZ += (skeleton.joints[jointOffset + 3] + ((((tz * qw) + (tw * iz)) + (tx * iy)) - (ty * ix))) * bias;
                    j++;
                }
            }
            bbox.ext(finalX, finalY, finalZ);
            int k2 = k + 1;
            verts[k] = finalX;
            int k3 = k2 + 1;
            verts[k2] = finalY;
            verts[k3] = finalZ;
            k = k3 + 1 + 2;
            vertexOffset += vstride;
        }
    }

    public void calculateVerticesJni(MD5Joints skeleton, float[] verts) {
        MD5Jni.calculateVertices(skeleton.joints, this.weights, this.vertices, verts, this.numVertices);
    }

    public void calculateNormalsBind(MD5Joints bindPoseSkeleton, float[] verts) {
        calculateNormalsBind(bindPoseSkeleton, this.weights, this.vertices, this.indices, verts, this.floatsPerVertex, this.floatsPerWeight);
    }

    private static Vector3 calcNor(Vector3 v1, Vector3 v2, Vector3 v3) {
        _A = v2.cpy();
        _A.sub(v1);
        _B = v3.cpy();
        _B.sub(v2);
        _n = _A.crs(_B).nor();
        return _n;
    }

    public static void calculateNormalsBind(MD5Joints skeleton, float[] weights, float[] vertices, short[] indices, float[] verts, int vstride, int wstride) {
        int vertexOffset = 2;
        int k = 0;
        while (vertexOffset < vertices.length) {
            float finalX = 0.0f;
            float finalY = 0.0f;
            float finalZ = 0.0f;
            int weightOffset = (int) vertices[vertexOffset];
            int weightCount = (int) vertices[vertexOffset + 1];
            int weightOffset2 = weightOffset * wstride;
            int j = 0;
            while (true) {
                int weightOffset3 = weightOffset2;
                if (j < weightCount) {
                    int weightOffset4 = weightOffset3 + 1;
                    int jointOffset = ((int) weights[weightOffset3]) << 3;
                    int weightOffset5 = weightOffset4 + 1;
                    float bias = weights[weightOffset4];
                    int weightOffset6 = weightOffset5 + 1;
                    float vx = weights[weightOffset5];
                    int weightOffset7 = weightOffset6 + 1;
                    float vy = weights[weightOffset6];
                    float vz = weights[weightOffset7];
                    weightOffset2 = weightOffset7 + 1 + 3;
                    float qx = skeleton.joints[jointOffset + 4];
                    float qy = skeleton.joints[jointOffset + 5];
                    float qz = skeleton.joints[jointOffset + 6];
                    float qw = skeleton.joints[jointOffset + 7];
                    float ix = -qx;
                    float iy = -qy;
                    float iz = -qz;
                    float tw = (((-qx) * vx) - (qy * vy)) - (qz * vz);
                    float tx = ((qw * vx) + (qy * vz)) - (qz * vy);
                    float ty = ((qw * vy) + (qz * vx)) - (qx * vz);
                    float tz = ((qw * vz) + (qx * vy)) - (qy * vx);
                    finalX += (skeleton.joints[jointOffset + 1] + ((((tx * qw) + (tw * ix)) + (ty * iz)) - (tz * iy))) * bias;
                    finalY += (skeleton.joints[jointOffset + 2] + ((((ty * qw) + (tw * iy)) + (tz * ix)) - (tx * iz))) * bias;
                    finalZ += (skeleton.joints[jointOffset + 3] + ((((tz * qw) + (tw * iz)) + (tx * iy)) - (ty * ix))) * bias;
                    j++;
                }
            }
            int k2 = k + 1;
            verts[k] = finalX;
            int k3 = k2 + 1;
            verts[k2] = finalY;
            verts[k3] = finalZ;
            k = k3 + 1 + 2 + 3;
            vertexOffset += vstride;
        }
        for (int i = 0; i < indices.length; i += 3) {
            short i1 = indices[i];
            short i2 = indices[i + 1];
            short i3 = indices[i + 2];
            int vo1 = i1 * 8;
            int vo2 = i2 * 8;
            int vo3 = i3 * 8;
            Vector3 v1 = new Vector3(verts[vo1], verts[vo1 + 1], verts[vo1 + 2]);
            Vector3 v2 = new Vector3(verts[vo2], verts[vo2 + 1], verts[vo2 + 2]);
            Vector3 v3 = new Vector3(verts[vo3], verts[vo3 + 1], verts[vo3 + 2]);
            Vector3 fn = calcNor(v1, v2, v3);
            int ovo1 = i1 * 7;
            int ovo2 = i2 * 7;
            int ovo3 = i3 * 7;
            int i4 = ovo1 + 4;
            vertices[i4] = vertices[i4] + fn.x;
            int i5 = ovo1 + 5;
            vertices[i5] = vertices[i5] + fn.y;
            int i6 = ovo1 + 6;
            vertices[i6] = vertices[i6] + fn.z;
            int i7 = ovo2 + 4;
            vertices[i7] = vertices[i7] + fn.x;
            int i8 = ovo2 + 5;
            vertices[i8] = vertices[i8] + fn.y;
            int i9 = ovo2 + 6;
            vertices[i9] = vertices[i9] + fn.z;
            int i10 = ovo3 + 4;
            vertices[i10] = vertices[i10] + fn.x;
            int i11 = ovo3 + 5;
            vertices[i11] = vertices[i11] + fn.y;
            int i12 = ovo3 + 6;
            vertices[i12] = vertices[i12] + fn.z;
        }
        for (int i13 = 0; i13 < indices.length; i13 += 3) {
            short i14 = indices[i13];
            int ovo4 = i14 * 7;
            int ovo5 = indices[i13 + 1] * 7;
            int ovo6 = indices[i13 + 2] * 7;
            vn.set(vertices[ovo4 + 4], vertices[ovo4 + 5], vertices[ovo4 + 6]);
            vn.nor();
            vertices[ovo4 + 4] = vn.x;
            vertices[ovo4 + 5] = vn.y;
            vertices[ovo4 + 6] = vn.z;
            vn.set(vertices[ovo5 + 4], vertices[ovo5 + 5], vertices[ovo5 + 6]);
            vn.nor();
            vertices[ovo5 + 4] = vn.x;
            vertices[ovo5 + 5] = vn.y;
            vertices[ovo5 + 6] = vn.z;
            vn.set(vertices[ovo6 + 4], vertices[ovo6 + 5], vertices[ovo6 + 6]);
            vn.nor();
            vertices[ovo6 + 4] = vn.x;
            vertices[ovo6 + 5] = vn.y;
            vertices[ovo6 + 6] = vn.z;
        }
        int vertexOffset2 = 2;
        while (vertexOffset2 < vertices.length) {
            int weightOffset8 = (int) vertices[vertexOffset2];
            int weightCount2 = (int) vertices[vertexOffset2 + 1];
            int weightOffset9 = weightOffset8 * wstride;
            for (int j2 = 0; j2 < weightCount2; j2++) {
                int weightOffset10 = weightOffset9 + 1;
                int jointOffset2 = ((int) weights[weightOffset9]) << 3;
                float qx2 = skeleton.joints[jointOffset2 + 4];
                float qy2 = skeleton.joints[jointOffset2 + 5];
                float qz2 = skeleton.joints[jointOffset2 + 6];
                float qw2 = skeleton.joints[jointOffset2 + 7];
                float vnx = vertices[vertexOffset2 + 2];
                float vny = vertices[vertexOffset2 + 3];
                float vnz = vertices[vertexOffset2 + 4];
                vn.set(vnx, vny, vnz);
                quat.x = qx2;
                quat.y = qy2;
                quat.z = qz2;
                quat.w = qw2;
                quat.invert();
                quat.rotate(vn);
                int weightOffset11 = weightOffset10 + 1;
                weights[weightOffset10] = weights[weightOffset10] + vn.x;
                int weightOffset12 = weightOffset11 + 1;
                weights[weightOffset11] = weights[weightOffset11] + vn.y;
                weightOffset9 = weightOffset12 + 1;
                weights[weightOffset12] = weights[weightOffset12] + vn.z;
            }
            vertexOffset2 += vstride;
        }
        int i15 = 0;
        while (i15 < weights.length) {
            vn.set(weights[i15 + 5], weights[i15 + 6], weights[i15 + 7]);
            vn.nor();
            weights[i15 + 5] = vn.x;
            weights[i15 + 6] = vn.y;
            weights[i15 + 7] = vn.z;
            i15 += wstride;
        }
    }

    public void read(DataInputStream in) throws IOException {
        this.shader = in.readUTF();
        this.numVertices = in.readInt();
        this.numWeights = in.readInt();
        this.numTriangles = in.readInt();
        this.floatsPerVertex = in.readInt();
        this.floatsPerWeight = in.readInt();
        this.vertices = new float[this.numVertices * this.floatsPerVertex];
        this.indices = new short[this.numTriangles * 3];
        this.weights = new float[this.numWeights * this.floatsPerWeight];
        for (int i = 0; i < this.vertices.length; i++) {
            this.vertices[i] = in.readFloat();
        }
        for (int i2 = 0; i2 < this.indices.length; i2++) {
            this.indices[i2] = in.readShort();
        }
        for (int i3 = 0; i3 < this.weights.length; i3++) {
            this.weights[i3] = in.readFloat();
        }
    }

    public void write(DataOutputStream out) throws IOException {
        out.writeUTF(this.shader);
        out.writeInt(this.numVertices);
        out.writeInt(this.numWeights);
        out.writeInt(this.numTriangles);
        out.writeInt(this.floatsPerVertex);
        out.writeInt(this.floatsPerWeight);
        for (int i = 0; i < this.vertices.length; i++) {
            out.writeFloat(this.vertices[i]);
        }
        for (int i2 = 0; i2 < this.indices.length; i2++) {
            out.writeShort(this.indices[i2]);
        }
        for (int i3 = 0; i3 < this.weights.length; i3++) {
            out.writeFloat(this.weights[i3]);
        }
    }
}
