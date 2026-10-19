package com.badlogic.gdx.graphics.g3d.decals;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;
import java.util.Comparator;
import java.util.Iterator;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class CameraGroupStrategy implements GroupStrategy {
    private static final int GROUP_BLEND = 1;
    private static final int GROUP_OPAQUE = 0;
    Camera camera;
    ShaderProgram shader;
    Pool<Array<Decal>> arrayPool = new Pool<Array<Decal>>(16) { // from class: com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.badlogic.gdx.utils.Pool
        public Array<Decal> newObject() {
            return new Array<>();
        }
    };
    Array<Array<Decal>> usedArrays = new Array<>();
    ObjectMap<DecalMaterial, Array<Decal>> materialGroups = new ObjectMap<>();
    private final Comparator<Decal> cameraSorter = new Comparator<Decal>() { // from class: com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy.2
        @Override // java.util.Comparator
        public int compare(Decal o1, Decal o2) {
            float dist1 = CameraGroupStrategy.this.camera.position.dst2(o1.position);
            float dist2 = CameraGroupStrategy.this.camera.position.dst2(o2.position);
            return (int) Math.signum(dist2 - dist1);
        }
    };

    public CameraGroupStrategy(Camera camera) {
        this.camera = camera;
        createDefaultShader();
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Camera getCamera() {
        return this.camera;
    }

    @Override // com.badlogic.gdx.graphics.g3d.decals.GroupStrategy
    public int decideGroup(Decal decal) {
        return decal.getMaterial().isOpaque() ? 0 : 1;
    }

    @Override // com.badlogic.gdx.graphics.g3d.decals.GroupStrategy
    public void beforeGroup(int group, Array<Decal> contents) {
        if (group == 1) {
            Gdx.gl.glEnable(3042);
            contents.sort(this.cameraSorter);
            return;
        }
        int n = contents.size;
        for (int i = 0; i < n; i++) {
            Decal decal = contents.get(i);
            Array<Decal> materialGroup = this.materialGroups.get(decal.material);
            if (materialGroup == null) {
                materialGroup = this.arrayPool.obtain();
                materialGroup.clear();
                this.usedArrays.add(materialGroup);
                this.materialGroups.put(decal.material, materialGroup);
            }
            materialGroup.add(decal);
        }
        contents.clear();
        Iterator<Array<Decal>> it = this.materialGroups.values().iterator();
        while (it.hasNext()) {
            contents.addAll(it.next());
        }
        this.materialGroups.clear();
        this.arrayPool.free(this.usedArrays);
        this.usedArrays.clear();
    }

    @Override // com.badlogic.gdx.graphics.g3d.decals.GroupStrategy
    public void afterGroup(int group) {
        if (group == 1) {
            Gdx.gl.glDisable(3042);
        }
    }

    @Override // com.badlogic.gdx.graphics.g3d.decals.GroupStrategy
    public void beforeGroups() {
        Gdx.gl.glEnable(3553);
        if (this.shader != null) {
            this.shader.begin();
            this.shader.setUniformMatrix("u_projectionViewMatrix", this.camera.combined);
            this.shader.setUniformi("u_texture", 0);
        } else {
            Gdx.gl10.glMatrixMode(GL10.GL_PROJECTION);
            Gdx.gl10.glLoadMatrixf(this.camera.projection.val, 0);
            Gdx.gl10.glMatrixMode(GL10.GL_MODELVIEW);
            Gdx.gl10.glLoadMatrixf(this.camera.view.val, 0);
        }
    }

    @Override // com.badlogic.gdx.graphics.g3d.decals.GroupStrategy
    public void afterGroups() {
        if (this.shader != null) {
            this.shader.end();
        }
        Gdx.gl.glDisable(3553);
    }

    private void createDefaultShader() {
        if (Gdx.graphics.isGL20Available()) {
            this.shader = new ShaderProgram("attribute vec4 a_position;\nattribute vec4 a_color;\nattribute vec2 a_texCoord0;\nuniform mat4 u_projectionViewMatrix;\nvarying vec4 v_color;\nvarying vec2 v_texCoords;\n\nvoid main()\n{\n   v_color = a_color;\n   v_texCoords = a_texCoord0;\n   gl_Position =  u_projectionViewMatrix * a_position;\n}\n", "#ifdef GL_ES\nprecision mediump float;\n#endif\nvarying vec4 v_color;\nvarying vec2 v_texCoords;\nuniform sampler2D u_texture;\nvoid main()\n{\n  gl_FragColor = v_color * texture2D(u_texture, v_texCoords);\n}");
            if (!this.shader.isCompiled()) {
                throw new IllegalArgumentException("couldn't compile shader: " + this.shader.getLog());
            }
        }
    }

    @Override // com.badlogic.gdx.graphics.g3d.decals.GroupStrategy
    public ShaderProgram getGroupShader(int group) {
        return this.shader;
    }
}
