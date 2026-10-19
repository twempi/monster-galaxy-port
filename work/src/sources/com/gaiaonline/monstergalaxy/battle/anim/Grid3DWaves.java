package com.gaiaonline.monstergalaxy.battle.anim;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

/* JADX INFO: loaded from: classes.dex */
public class Grid3DWaves extends Grid3D {
    private static final float AMPLITUDE_RATE = 1.0f;
    float amplitude;
    boolean horizontal;
    private float totalTime = 0.0f;
    boolean vertical;
    int waves;

    @Override // com.gaiaonline.monstergalaxy.battle.anim.Grid3D
    protected void updateVertices(float deltaTime) {
        this.totalTime += deltaTime / 10.0f;
        for (int i = 0; i < this.gridSizeX + 1; i++) {
            for (int j = 0; j < this.gridSizeY + 1; j++) {
                Vector3 v = getOriginalVertex(i, j);
                if (this.vertical) {
                    v.x += MathUtils.sin((this.totalTime * 3.1415927f * this.waves * 2.0f) + ((v.y - this.offsetY) * 0.01f)) * this.amplitude * AMPLITUDE_RATE;
                }
                if (this.horizontal) {
                    v.y += MathUtils.sin((this.totalTime * 3.1415927f * this.waves * 2.0f) + ((v.x - this.offsetX) * 0.01f)) * this.amplitude * AMPLITUDE_RATE;
                }
                setVertex(i, j, v);
            }
        }
    }
}
