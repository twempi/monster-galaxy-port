package com.gaiaonline.monstergalaxy.battle.anim;

import com.badlogic.gdx.math.Vector3;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Grid3DShaky extends Grid3D {
    int randrange;
    boolean shakeZ;

    @Override // com.gaiaonline.monstergalaxy.battle.anim.Grid3D
    protected void updateVertices(float deltaTime) {
        for (int i = 0; i < this.gridSizeX + 1; i++) {
            for (int j = 0; j < this.gridSizeY + 1; j++) {
                Vector3 v = getOriginalVertex(i, j);
                v.x = (float) (((double) v.x) + ((Math.random() - 0.5d) * ((double) this.randrange)));
                v.y = (float) (((double) v.y) + ((Math.random() - 0.5d) * ((double) this.randrange)));
                if (this.shakeZ) {
                    v.z = (float) (((double) v.z) + (Math.random() * ((double) this.randrange)));
                }
                setVertex(i, j, v);
            }
        }
    }
}
