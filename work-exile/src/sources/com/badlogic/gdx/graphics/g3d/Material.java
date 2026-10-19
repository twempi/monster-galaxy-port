package com.badlogic.gdx.graphics.g3d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.TextureRef;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Material {
    private static final float[] tmp = new float[4];
    public String Name;
    public ShaderProgram Shader;
    public TextureRef Texture = null;
    public String TexturePath = "";
    public Color Ambient = null;
    public Color Diffuse = null;
    public Color Specular = null;
    public Color Emissive = null;
    public int BlendSourceFactor = 0;
    public int BlendDestFactor = 0;

    public Material(String name) {
        this.Name = name;
    }

    private void setTmpArray(float r, float g, float b, float a) {
        tmp[0] = r;
        tmp[1] = g;
        tmp[2] = b;
        tmp[3] = a;
    }

    public void set(int face) {
        GL10 gl = Gdx.graphics.getGL10();
        if (this.Ambient != null) {
            setTmpArray(this.Ambient.r, this.Ambient.g, this.Ambient.b, this.Ambient.a);
            gl.glMaterialfv(face, GL10.GL_AMBIENT, tmp, 0);
        }
        if (this.Diffuse != null) {
            setTmpArray(this.Diffuse.r, this.Diffuse.g, this.Diffuse.b, this.Diffuse.a);
            gl.glMaterialfv(face, GL10.GL_DIFFUSE, tmp, 0);
        }
        if (this.BlendSourceFactor > 0) {
            gl.glBlendFunc(this.BlendSourceFactor, this.BlendDestFactor);
            gl.glEnable(3042);
        } else {
            gl.glDisable(3042);
        }
    }

    public boolean read(DataInputStream i) throws IOException {
        this.Name = i.readUTF();
        this.TexturePath = i.readUTF();
        boolean hasAmbient = i.readBoolean();
        if (hasAmbient) {
            float r = i.readFloat();
            float g = i.readFloat();
            float b = i.readFloat();
            float a = i.readFloat();
            this.Ambient = new Color(r, g, b, a);
        }
        boolean hasDiffuse = i.readBoolean();
        if (hasDiffuse) {
            float r2 = i.readFloat();
            float g2 = i.readFloat();
            float b2 = i.readFloat();
            float a2 = i.readFloat();
            this.Diffuse = new Color(r2, g2, b2, a2);
        }
        this.BlendSourceFactor = i.readInt();
        this.BlendDestFactor = i.readInt();
        return true;
    }

    public boolean write(DataOutputStream o) throws IOException {
        o.writeUTF(this.Name);
        String filename = this.Texture.Name.substring(this.Texture.Name.lastIndexOf("\\") + 1);
        o.writeUTF(filename);
        o.writeBoolean(this.Ambient != null);
        if (this.Ambient != null) {
            o.writeFloat(this.Ambient.r);
            o.writeFloat(this.Ambient.g);
            o.writeFloat(this.Ambient.b);
            o.writeFloat(this.Ambient.a);
        }
        o.writeBoolean(this.Diffuse != null);
        if (this.Diffuse != null) {
            o.writeFloat(this.Diffuse.r);
            o.writeFloat(this.Diffuse.g);
            o.writeFloat(this.Diffuse.b);
            o.writeFloat(this.Diffuse.a);
        }
        o.writeInt(this.BlendSourceFactor);
        o.writeInt(this.BlendDestFactor);
        return true;
    }
}
