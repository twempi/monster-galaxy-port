package com.badlogic.gdx.graphics.g3d.keyframed;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Animator;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.loaders.md5.MD5Animation;
import com.badlogic.gdx.graphics.g3d.loaders.md5.MD5Animator;
import com.badlogic.gdx.graphics.g3d.loaders.md5.MD5Joints;
import com.badlogic.gdx.graphics.g3d.loaders.md5.MD5Model;
import com.badlogic.gdx.graphics.g3d.loaders.md5.MD5Renderer;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import java.util.ArrayList;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class KeyframedModel {
    private static ObjectMap<String, KeyframeAnimation> animations = null;
    private String assetName;
    private Material[] materials;
    private ArrayList<String> animationRefs = new ArrayList<>();
    private KeyframeAnimator animator = null;
    private Mesh[] target = null;
    private boolean[] visible = null;
    private int numMeshes = 0;
    private ArrayList<String> taggedJointNames = new ArrayList<>();

    public Animator getAnimator() {
        return this.animator;
    }

    public void setMaterials(Material[] mats) {
        this.materials = new Material[mats.length];
        for (int i = 0; i < mats.length; i++) {
            this.materials[i] = mats[i];
        }
    }

    public void setTaggedJoints(ArrayList<String> joints) {
        this.taggedJointNames = joints;
    }

    public KeyframeAnimation sampleAnimationFromMD5(MD5Model md5model, MD5Renderer md5renderer, MD5Animator md5animator, MD5Animation md5animation, float sampleRate, String modelAsset, String animKey) {
        this.assetName = modelAsset;
        this.numMeshes = md5model.meshes.length;
        boolean cached = false;
        if (this.animator == null) {
            this.animator = new KeyframeAnimator(this.numMeshes, sampleRate);
            this.target = new Mesh[this.numMeshes];
            this.visible = new boolean[this.numMeshes];
            for (int i = 0; i < this.visible.length; i++) {
                this.visible[i] = true;
            }
        }
        if (animations == null) {
            animations = new ObjectMap<>();
        }
        String key = modelAsset + "_" + animKey;
        KeyframeAnimation a = null;
        if (animations.containsKey(key)) {
            KeyframeAnimation a2 = animations.get(key);
            a = a2;
            a.addRef();
            cached = true;
        }
        this.animationRefs.add(key);
        md5animator.setAnimation(md5animation, Animator.WrapMode.Clamp);
        float len = md5animation.frames.length * md5animation.secondsPerFrame;
        int numSamples = ((int) (len / sampleRate)) + 1;
        if (!cached) {
            a = new KeyframeAnimation(md5animation.name, numSamples, len, sampleRate);
            animations.put(key, a);
        }
        md5animator.update(0.1f);
        md5renderer.setSkeleton(md5animator.getSkeleton());
        int i2 = 0;
        float t = 0.0f;
        while (t < len) {
            Keyframe k = null;
            if (!cached) {
                k = new Keyframe();
                k.vertices = new float[this.numMeshes][];
                k.indices = new short[this.numMeshes][];
                if (this.taggedJointNames.size() > 0) {
                    k.taggedJointPos = new Vector3[this.taggedJointNames.size()];
                    k.taggedJoint = new Quaternion[this.taggedJointNames.size()];
                }
            }
            for (int m = 0; m < this.numMeshes; m++) {
                float[] vertices = md5renderer.getVertices(m);
                short[] indices = md5renderer.getIndices(m);
                int numVertices = vertices.length;
                int numIndices = indices.length;
                if (!cached) {
                    k.vertices[m] = new float[vertices.length];
                    k.vertices[m] = (float[]) vertices.clone();
                    k.indices[m] = new short[indices.length];
                    k.indices[m] = (short[]) indices.clone();
                }
                if (this.target[m] == null) {
                    this.animator.setKeyframeDimensions(m, numVertices, numIndices);
                    this.animator.setNumTaggedJoints(this.taggedJointNames.size());
                    VertexAttributes attribs = md5renderer.getMesh().getVertexAttributes();
                    this.target[m] = new Mesh(false, numVertices, numIndices, attribs);
                    if (this.target[m].getVertexSize() / 4 != 8) {
                        throw new GdxRuntimeException("Mesh vertexattributes != 8 - is this a valid MD5 source mesh?");
                    }
                }
            }
            if (!cached) {
                MD5Joints skel = md5animator.getSkeleton();
                for (int tj = 0; tj < this.taggedJointNames.size(); tj++) {
                    String name = this.taggedJointNames.get(tj);
                    for (int j = 0; j < skel.numJoints; j++) {
                        if (name.equals(skel.names[j])) {
                            int idx = j * 8;
                            float x = skel.joints[idx + 1];
                            float y = skel.joints[idx + 2];
                            float z = skel.joints[idx + 3];
                            k.taggedJointPos[tj] = new Vector3(x, y, z);
                            float qx = skel.joints[idx + 4];
                            float qy = skel.joints[idx + 5];
                            float qz = skel.joints[idx + 6];
                            float qw = skel.joints[idx + 7];
                            k.taggedJoint[tj] = new Quaternion(qx, qy, qz, qw);
                            break;
                        }
                    }
                }
                a.keyframes[i2] = k;
            }
            md5animator.update(sampleRate);
            md5renderer.setSkeleton(md5animator.getSkeleton());
            i2++;
            t += sampleRate;
        }
        if (cached) {
        }
        return a;
    }

    public void getJointData(int tagIndex, Vector3 pos, Quaternion orient) {
        Keyframe kf = this.animator.getInterpolatedKeyframe();
        pos.set(kf.taggedJointPos[tagIndex]);
        orient.x = kf.taggedJoint[tagIndex].x;
        orient.y = kf.taggedJoint[tagIndex].y;
        orient.z = kf.taggedJoint[tagIndex].z;
        orient.w = kf.taggedJoint[tagIndex].w;
    }

    public void setAnimation(String animKey, Animator.WrapMode wrapMode) {
        KeyframeAnimation anim = getAnimation(animKey);
        if (anim != null) {
            this.animator.setAnimation(anim, wrapMode);
            this.animator.getInterpolatedKeyframe().indicesSet = false;
            this.animator.getInterpolatedKeyframe().indicesSent = false;
        }
    }

    public KeyframeAnimation getAnimation(String animKey) {
        return animations.get(this.assetName + "_" + animKey);
    }

    public void update(float dt) {
        if (this.animator != null) {
            this.animator.update(dt);
            if (this.animator.hasAnimation()) {
                Keyframe ikf = this.animator.getInterpolatedKeyframe();
                if (this.animator.getCurrentWrapMode() != Animator.WrapMode.SingleFrame || !ikf.indicesSent) {
                    for (int i = 0; i < this.numMeshes; i++) {
                        this.target[i].setVertices(ikf.vertices[i]);
                        if (!ikf.indicesSent) {
                            this.target[i].setIndices(ikf.indices[i]);
                        }
                    }
                    ikf.indicesSent = true;
                }
            }
        }
    }

    public void render() {
        for (int i = 0; i < this.numMeshes; i++) {
            Material mat = this.materials[i];
            if (mat != null) {
                if (mat.Texture != null) {
                    mat.Texture.bind();
                }
                mat.set(1028);
            }
            if (this.visible[i]) {
                this.target[i].render(4, 0, this.target[i].getNumIndices());
            }
        }
    }

    public void setMeshVisible(int idx, boolean visible) {
        this.visible[idx] = visible;
    }

    public void dispose() {
        for (String key : this.animationRefs) {
            KeyframeAnimation anim = animations.get(key);
            if (anim.removeRef() == 0) {
                animations.remove(key);
            }
        }
        Mesh[] arr$ = this.target;
        for (Mesh m : arr$) {
            if (m != null) {
                m.dispose();
            }
        }
    }
}
