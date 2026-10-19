package com.badlogic.gdx.graphics.g3d.loaders.obj;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class ObjLoader {
    public static Mesh loadObj(InputStream in) {
        return loadObj(in, false);
    }

    public static Mesh loadObj(InputStream in, boolean flipV) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuffer b = new StringBuffer();
            for (String l = reader.readLine(); l != null; l = reader.readLine()) {
                b.append(l);
                b.append("\n");
            }
            String line = b.toString();
            reader.close();
            return loadObjFromString(line, flipV);
        } catch (Exception e) {
            return null;
        }
    }

    public static Mesh loadObjFromString(String obj) {
        return loadObjFromString(obj, false);
    }

    public static Mesh loadObjFromString(String obj, boolean flipV) {
        int vi;
        String[] lines = obj.split("\n");
        float[] vertices = new float[lines.length * 3];
        float[] normals = new float[lines.length * 3];
        float[] uv = new float[lines.length * 3];
        int numVertices = 0;
        int numNormals = 0;
        int numUV = 0;
        int numFaces = 0;
        int[] facesVerts = new int[lines.length * 3];
        int[] facesNormals = new int[lines.length * 3];
        int[] facesUV = new int[lines.length * 3];
        int vertexIndex = 0;
        int normalIndex = 0;
        int uvIndex = 0;
        int faceIndex = 0;
        for (String line : lines) {
            if (line.startsWith("v ")) {
                String[] tokens = line.split("[ ]+");
                vertices[vertexIndex] = Float.parseFloat(tokens[1]);
                vertices[vertexIndex + 1] = Float.parseFloat(tokens[2]);
                vertices[vertexIndex + 2] = Float.parseFloat(tokens[3]);
                vertexIndex += 3;
                numVertices++;
            } else if (line.startsWith("vn ")) {
                String[] tokens2 = line.split("[ ]+");
                normals[normalIndex] = Float.parseFloat(tokens2[1]);
                normals[normalIndex + 1] = Float.parseFloat(tokens2[2]);
                normals[normalIndex + 2] = Float.parseFloat(tokens2[3]);
                normalIndex += 3;
                numNormals++;
            } else if (line.startsWith("vt")) {
                String[] tokens3 = line.split("[ ]+");
                uv[uvIndex] = Float.parseFloat(tokens3[1]);
                uv[uvIndex + 1] = flipV ? 1.0f - Float.parseFloat(tokens3[2]) : Float.parseFloat(tokens3[2]);
                uvIndex += 2;
                numUV++;
            } else if (line.startsWith("f ")) {
                String[] tokens4 = line.split("[ ]+");
                String[] parts = tokens4[1].split("/");
                facesVerts[faceIndex] = getIndex(parts[0], numVertices);
                if (parts.length > 2) {
                    facesNormals[faceIndex] = getIndex(parts[2], numNormals);
                }
                if (parts.length > 1) {
                    facesUV[faceIndex] = getIndex(parts[1], numUV);
                }
                int faceIndex2 = faceIndex + 1;
                String[] parts2 = tokens4[2].split("/");
                facesVerts[faceIndex2] = getIndex(parts2[0], numVertices);
                if (parts2.length > 2) {
                    facesNormals[faceIndex2] = getIndex(parts2[2], numNormals);
                }
                if (parts2.length > 1) {
                    facesUV[faceIndex2] = getIndex(parts2[1], numUV);
                }
                int faceIndex3 = faceIndex2 + 1;
                String[] parts3 = tokens4[3].split("/");
                facesVerts[faceIndex3] = getIndex(parts3[0], numVertices);
                if (parts3.length > 2) {
                    facesNormals[faceIndex3] = getIndex(parts3[2], numNormals);
                }
                if (parts3.length > 1) {
                    facesUV[faceIndex3] = getIndex(parts3[1], numUV);
                }
                faceIndex = faceIndex3 + 1;
                numFaces++;
            }
        }
        float[] verts = new float[((numUV > 0 ? 2 : 0) + (numNormals > 0 ? 3 : 0) + 3) * numFaces * 3];
        int i = 0;
        int vi2 = 0;
        while (true) {
            int vi3 = vi2;
            if (i >= numFaces * 3) {
                break;
            }
            int vertexIdx = facesVerts[i] * 3;
            int vi4 = vi3 + 1;
            verts[vi3] = vertices[vertexIdx];
            int vi5 = vi4 + 1;
            verts[vi4] = vertices[vertexIdx + 1];
            int vi6 = vi5 + 1;
            verts[vi5] = vertices[vertexIdx + 2];
            if (numNormals > 0) {
                int normalIdx = facesNormals[i] * 3;
                int vi7 = vi6 + 1;
                verts[vi6] = normals[normalIdx];
                int vi8 = vi7 + 1;
                verts[vi7] = normals[normalIdx + 1];
                vi = vi8 + 1;
                verts[vi8] = normals[normalIdx + 2];
            } else {
                vi = vi6;
            }
            if (numUV > 0) {
                int uvIdx = facesUV[i] * 2;
                int vi9 = vi + 1;
                verts[vi] = uv[uvIdx];
                vi = vi9 + 1;
                verts[vi9] = uv[uvIdx + 1];
            }
            vi2 = vi;
            i++;
        }
        ArrayList<VertexAttribute> attributes = new ArrayList<>();
        attributes.add(new VertexAttribute(0, 3, ShaderProgram.POSITION_ATTRIBUTE));
        if (numNormals > 0) {
            attributes.add(new VertexAttribute(2, 3, ShaderProgram.NORMAL_ATTRIBUTE));
        }
        if (numUV > 0) {
            attributes.add(new VertexAttribute(3, 2, "a_texCoord0"));
        }
        Mesh mesh = new Mesh(true, numFaces * 3, 0, (VertexAttribute[]) attributes.toArray(new VertexAttribute[attributes.size()]));
        mesh.setVertices(verts);
        return mesh;
    }

    private static int getIndex(String index, int size) {
        if (index == null || index.length() == 0) {
            return 0;
        }
        int idx = Integer.parseInt(index);
        if (idx < 0) {
            return size + idx;
        }
        return idx - 1;
    }
}
