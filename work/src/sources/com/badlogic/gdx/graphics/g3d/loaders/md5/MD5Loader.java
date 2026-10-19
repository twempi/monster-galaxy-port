package com.badlogic.gdx.graphics.g3d.loaders.md5;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class MD5Loader {
    static MD5Quaternion thisOrient = new MD5Quaternion();
    static MD5Quaternion parentOrient = new MD5Quaternion();
    static Vector3 parentPos = new Vector3();

    public static MD5Model loadModel(InputStream in, boolean allocateNormals) {
        int version;
        BufferedReader reader = new BufferedReader(new InputStreamReader(in), 1024);
        MD5Model model = new MD5Model();
        List<String> tokens = new ArrayList<>(10);
        MD5Quaternion quat = new MD5Quaternion();
        int floatsPerVert = allocateNormals ? 4 + 3 : 4;
        int floatsPerWeight = allocateNormals ? 5 + 3 : 5;
        int currMesh = 0;
        while (true) {
            try {
                String line = reader.readLine();
                if (line == null) {
                    return model;
                }
                MD5Tokenizer.tokenize(line, tokens);
                if (tokens.size() != 0) {
                    if (tokens.get(0).equals("MD5Version") && (version = parseInt(tokens.get(1))) != 10) {
                        throw new IllegalArgumentException("Not a valid MD5 file, go version " + version + ", need 10");
                    }
                    if (tokens.get(0).equals("numJoints")) {
                        int numJoints = parseInt(tokens.get(1));
                        model.baseSkeleton = new MD5Joints();
                        model.baseSkeleton.names = new String[numJoints];
                        model.baseSkeleton.numJoints = numJoints;
                        model.baseSkeleton.joints = new float[numJoints * 8];
                    }
                    if (tokens.get(0).equals("numMeshes")) {
                        int numMeshes = parseInt(tokens.get(1));
                        model.meshes = new MD5Mesh[numMeshes];
                    }
                    if (tokens.get(0).equals("joints")) {
                        int i = 0;
                        while (i < model.baseSkeleton.numJoints) {
                            line = reader.readLine();
                            MD5Tokenizer.tokenize(line, tokens);
                            if (tokens.size() == 0) {
                                i--;
                            } else {
                                int jointIdx = i << 3;
                                model.baseSkeleton.names[i] = tokens.get(0);
                                model.baseSkeleton.joints[jointIdx] = parseInt(tokens.get(1));
                                model.baseSkeleton.joints[jointIdx + 1] = parseFloat(tokens.get(3));
                                model.baseSkeleton.joints[jointIdx + 2] = parseFloat(tokens.get(4));
                                model.baseSkeleton.joints[jointIdx + 3] = parseFloat(tokens.get(5));
                                quat.x = parseFloat(tokens.get(8));
                                quat.y = parseFloat(tokens.get(9));
                                quat.z = parseFloat(tokens.get(10));
                                quat.computeW();
                                model.baseSkeleton.joints[jointIdx + 4] = quat.x;
                                model.baseSkeleton.joints[jointIdx + 5] = quat.y;
                                model.baseSkeleton.joints[jointIdx + 6] = quat.z;
                                model.baseSkeleton.joints[jointIdx + 7] = quat.w;
                            }
                            i++;
                        }
                    }
                    if (tokens.get(0).equals("mesh") && tokens.get(1).equals("{")) {
                        MD5Mesh mesh = new MD5Mesh();
                        mesh.floatsPerVertex = floatsPerVert;
                        mesh.floatsPerWeight = floatsPerWeight;
                        int currMesh2 = currMesh + 1;
                        try {
                            model.meshes[currMesh] = mesh;
                            while (!line.contains("}")) {
                                line = reader.readLine();
                                MD5Tokenizer.tokenize(line, tokens);
                                if (tokens.size() != 0) {
                                    if (tokens.get(0).equals("shader")) {
                                        mesh.shader = tokens.get(1);
                                    }
                                    if (tokens.get(0).equals("numverts")) {
                                        mesh.numVertices = parseInt(tokens.get(1));
                                        mesh.vertices = new float[mesh.numVertices * floatsPerVert];
                                    }
                                    if (tokens.get(0).equals("numtris")) {
                                        mesh.indices = new short[parseInt(tokens.get(1)) * 3];
                                        mesh.numTriangles = mesh.indices.length / 3;
                                    }
                                    if (tokens.get(0).equals("numweights")) {
                                        mesh.numWeights = parseInt(tokens.get(1));
                                        mesh.weights = new float[mesh.numWeights * floatsPerWeight];
                                    }
                                    if (tokens.get(0).equals("vert")) {
                                        int vertIndex = parseInt(tokens.get(1));
                                        int idx = vertIndex * floatsPerVert;
                                        int idx2 = idx + 1;
                                        mesh.vertices[idx] = parseFloat(tokens.get(3));
                                        int idx3 = idx2 + 1;
                                        mesh.vertices[idx2] = parseFloat(tokens.get(4));
                                        int idx4 = idx3 + 1;
                                        mesh.vertices[idx3] = parseFloat(tokens.get(6));
                                        int idx5 = idx4 + 1;
                                        mesh.vertices[idx4] = parseFloat(tokens.get(7));
                                        if (allocateNormals) {
                                            int idx6 = idx5 + 1;
                                            mesh.vertices[idx5] = 0.0f;
                                            int idx7 = idx6 + 1;
                                            mesh.vertices[idx6] = 0.0f;
                                            int i2 = idx7 + 1;
                                            mesh.vertices[idx7] = 0.0f;
                                        }
                                    }
                                    if (tokens.get(0).equals("tri")) {
                                        int triIndex = parseInt(tokens.get(1));
                                        int idx8 = triIndex * 3;
                                        int idx9 = idx8 + 1;
                                        mesh.indices[idx8] = Short.parseShort(tokens.get(2));
                                        int idx10 = idx9 + 1;
                                        mesh.indices[idx9] = Short.parseShort(tokens.get(3));
                                        int i3 = idx10 + 1;
                                        mesh.indices[idx10] = Short.parseShort(tokens.get(4));
                                    }
                                    if (tokens.get(0).equals("weight")) {
                                        int weightIndex = parseInt(tokens.get(1));
                                        int idx11 = weightIndex * floatsPerWeight;
                                        int idx12 = idx11 + 1;
                                        mesh.weights[idx11] = parseInt(tokens.get(2));
                                        int idx13 = idx12 + 1;
                                        mesh.weights[idx12] = parseFloat(tokens.get(3));
                                        int idx14 = idx13 + 1;
                                        mesh.weights[idx13] = parseFloat(tokens.get(5));
                                        int idx15 = idx14 + 1;
                                        mesh.weights[idx14] = parseFloat(tokens.get(6));
                                        int i4 = idx15 + 1;
                                        mesh.weights[idx15] = parseFloat(tokens.get(7));
                                    }
                                }
                            }
                            currMesh = currMesh2;
                        } catch (Exception e) {
                            ex = e;
                        }
                    }
                }
            } catch (Exception e2) {
                ex = e2;
            }
            ex.printStackTrace();
            return null;
        }
    }

    public static MD5Animation loadAnimation(InputStream in) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        List<String> tokens = new ArrayList<>();
        MD5Animation animation = new MD5Animation();
        JointInfo[] jointInfos = null;
        BaseFrameJoint[] baseFrame = null;
        float[] animFrameData = null;
        while (true) {
            try {
                String line = reader.readLine();
                if (line == null) {
                    return animation;
                }
                MD5Tokenizer.tokenize(line, tokens);
                if (tokens.size() != 0) {
                    if (tokens.get(0).equals("MD5Version") && !tokens.get(1).equals("10")) {
                        throw new IllegalArgumentException("Not a valid MD5 animation file, version is " + tokens.get(1) + ", expected 10");
                    }
                    if (tokens.get(0).equals("numFrames")) {
                        int numFrames = parseInt(tokens.get(1));
                        animation.frames = new MD5Joints[numFrames];
                        animation.bounds = new BoundingBox[numFrames];
                    }
                    if (tokens.get(0).equals("numJoints")) {
                        int numJoints = parseInt(tokens.get(1));
                        for (int i = 0; i < animation.frames.length; i++) {
                            animation.frames[i] = new MD5Joints();
                            animation.frames[i].numJoints = numJoints;
                            animation.frames[i].names = new String[numJoints];
                            animation.frames[i].joints = new float[numJoints * 8];
                        }
                        jointInfos = new JointInfo[numJoints];
                        baseFrame = new BaseFrameJoint[numJoints];
                    }
                    if (tokens.get(0).equals("frameRate")) {
                        int frameRate = parseInt(tokens.get(1));
                        animation.frameRate = frameRate;
                        animation.secondsPerFrame = 1.0f / frameRate;
                    }
                    if (tokens.get(0).equals("numAnimatedComponents")) {
                        int numAnimatedComponents = parseInt(tokens.get(1));
                        animFrameData = new float[numAnimatedComponents];
                    }
                    if (tokens.get(0).equals("hierarchy")) {
                        int i2 = 0;
                        while (i2 < jointInfos.length) {
                            MD5Tokenizer.tokenize(reader.readLine(), tokens);
                            if (tokens.size() == 0 || tokens.get(0).equals("//")) {
                                i2--;
                            } else {
                                JointInfo jointInfo = new JointInfo();
                                jointInfo.name = tokens.get(0);
                                jointInfo.parent = parseInt(tokens.get(1));
                                jointInfo.flags = parseInt(tokens.get(2));
                                jointInfo.startIndex = parseInt(tokens.get(3));
                                jointInfos[i2] = jointInfo;
                            }
                            i2++;
                        }
                    }
                    if (tokens.get(0).equals("bounds")) {
                        int i3 = 0;
                        while (i3 < animation.bounds.length) {
                            MD5Tokenizer.tokenize(reader.readLine(), tokens);
                            if (tokens.size() == 0) {
                                i3--;
                            } else {
                                BoundingBox bounds = new BoundingBox();
                                bounds.min.x = parseFloat(tokens.get(1));
                                bounds.min.y = parseFloat(tokens.get(2));
                                bounds.min.z = parseFloat(tokens.get(3));
                                bounds.max.x = parseFloat(tokens.get(6));
                                bounds.max.y = parseFloat(tokens.get(7));
                                bounds.max.z = parseFloat(tokens.get(8));
                                animation.bounds[i3] = bounds;
                            }
                            i3++;
                        }
                    }
                    if (tokens.get(0).equals("baseframe")) {
                        int i4 = 0;
                        while (i4 < baseFrame.length) {
                            MD5Tokenizer.tokenize(reader.readLine(), tokens);
                            if (tokens.size() == 0) {
                                i4--;
                            } else {
                                BaseFrameJoint joint = new BaseFrameJoint();
                                joint.pos.x = parseFloat(tokens.get(1));
                                joint.pos.y = parseFloat(tokens.get(2));
                                joint.pos.z = parseFloat(tokens.get(3));
                                joint.orient.x = parseFloat(tokens.get(6));
                                joint.orient.y = parseFloat(tokens.get(7));
                                joint.orient.z = parseFloat(tokens.get(8));
                                joint.orient.computeW();
                                baseFrame[i4] = joint;
                            }
                            i4++;
                        }
                    }
                    if (tokens.get(0).equals("frame")) {
                        int frameIndex = parseInt(tokens.get(1));
                        int i5 = 0;
                        MD5Tokenizer.tokenize(reader.readLine(), tokens);
                        while (!tokens.get(0).equals("}")) {
                            int j = 0;
                            int i6 = i5;
                            while (j < tokens.size()) {
                                animFrameData[i6] = parseFloat(tokens.get(j));
                                j++;
                                i6++;
                            }
                            MD5Tokenizer.tokenize(reader.readLine(), tokens);
                            i5 = i6;
                        }
                        buildFrameSkeleton(jointInfos, baseFrame, animFrameData, animation, frameIndex);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }
    }

    private static float parseFloat(String value) {
        int count;
        float front = 0.0f;
        float back = 0.0f;
        float sign = 1.0f;
        boolean isBack = false;
        int len = value.length();
        int i = 0;
        int count2 = 1;
        while (i < len) {
            char c = value.charAt(i);
            if (c == '-') {
                sign = -1.0f;
                count = count2;
            } else if (c == '+') {
                count = count2;
            } else if (c == '.' || c == ',') {
                isBack = true;
                count = count2;
            } else {
                float val = c - '0';
                if (!isBack) {
                    front = (10.0f * front) + val;
                    count = count2;
                } else {
                    count = count2 + 1;
                    back += (1.0f / ((float) Math.pow(10.0d, count2))) * val;
                }
            }
            i++;
            count2 = count;
        }
        return (front + back) * sign;
    }

    private static int parseInt(String value) {
        int front = 0;
        int sign = 1;
        int len = value.length();
        for (int i = 0; i < len; i++) {
            char c = value.charAt(i);
            if (c == '-') {
                sign = -1;
            } else if (c == '+') {
                continue;
            } else {
                if (c == '.' || c == ',') {
                    break;
                }
                int val = c - '0';
                front = (front * 10) + val;
            }
        }
        return sign * front;
    }

    private static void buildFrameSkeleton(JointInfo[] jointInfos, BaseFrameJoint[] baseFrame, float[] animFrameData, MD5Animation animation, int frameIndex) {
        MD5Joints skelFrame = animation.frames[frameIndex];
        for (int i = 0; i < jointInfos.length; i++) {
            BaseFrameJoint baseJoint = baseFrame[i];
            Vector3 animatedPos = new Vector3();
            MD5Quaternion animatedOrient = new MD5Quaternion();
            int j = 0;
            animatedPos.set(baseJoint.pos);
            animatedOrient.set(baseJoint.orient);
            if ((jointInfos[i].flags & 1) != 0) {
                animatedPos.x = animFrameData[jointInfos[i].startIndex + 0];
                j = 0 + 1;
            }
            if ((jointInfos[i].flags & 2) != 0) {
                animatedPos.y = animFrameData[jointInfos[i].startIndex + j];
                j++;
            }
            if ((jointInfos[i].flags & 4) != 0) {
                animatedPos.z = animFrameData[jointInfos[i].startIndex + j];
                j++;
            }
            if ((jointInfos[i].flags & 8) != 0) {
                animatedOrient.x = animFrameData[jointInfos[i].startIndex + j];
                j++;
            }
            if ((jointInfos[i].flags & 16) != 0) {
                animatedOrient.y = animFrameData[jointInfos[i].startIndex + j];
                j++;
            }
            if ((jointInfos[i].flags & 32) != 0) {
                animatedOrient.z = animFrameData[jointInfos[i].startIndex + j];
                int i2 = j + 1;
            }
            animatedOrient.computeW();
            int thisJointIdx = i << 3;
            skelFrame.names[i] = jointInfos[i].name;
            skelFrame.joints[thisJointIdx] = jointInfos[i].parent;
            if (jointInfos[i].parent < 0) {
                skelFrame.joints[thisJointIdx + 1] = animatedPos.x;
                skelFrame.joints[thisJointIdx + 2] = animatedPos.y;
                skelFrame.joints[thisJointIdx + 3] = animatedPos.z;
                skelFrame.joints[thisJointIdx + 4] = animatedOrient.x;
                skelFrame.joints[thisJointIdx + 5] = animatedOrient.y;
                skelFrame.joints[thisJointIdx + 6] = animatedOrient.z;
                skelFrame.joints[thisJointIdx + 7] = animatedOrient.w;
            } else {
                int parentJointIdx = jointInfos[i].parent << 3;
                parentPos.x = skelFrame.joints[parentJointIdx + 1];
                parentPos.y = skelFrame.joints[parentJointIdx + 2];
                parentPos.z = skelFrame.joints[parentJointIdx + 3];
                parentOrient.x = skelFrame.joints[parentJointIdx + 4];
                parentOrient.y = skelFrame.joints[parentJointIdx + 5];
                parentOrient.z = skelFrame.joints[parentJointIdx + 6];
                parentOrient.w = skelFrame.joints[parentJointIdx + 7];
                parentOrient.rotate(animatedPos);
                skelFrame.joints[thisJointIdx + 1] = animatedPos.x + parentPos.x;
                skelFrame.joints[thisJointIdx + 2] = animatedPos.y + parentPos.y;
                skelFrame.joints[thisJointIdx + 3] = animatedPos.z + parentPos.z;
                parentOrient.multiply(animatedOrient);
                parentOrient.normalize();
                skelFrame.joints[thisJointIdx + 4] = parentOrient.x;
                skelFrame.joints[thisJointIdx + 5] = parentOrient.y;
                skelFrame.joints[thisJointIdx + 6] = parentOrient.z;
                skelFrame.joints[thisJointIdx + 7] = parentOrient.w;
            }
        }
    }

    static class JointInfo {
        public int flags;
        public String name;
        public int parent;
        public int startIndex;

        JointInfo() {
        }
    }

    static class BaseFrameJoint {
        public final Vector3 pos = new Vector3();
        public final MD5Quaternion orient = new MD5Quaternion();

        BaseFrameJoint() {
        }
    }
}
