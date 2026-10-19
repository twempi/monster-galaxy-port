package com.gaiaonline.monstergalaxy.battle.anim;

import com.badlogic.gdx.math.Vector2;

/* JADX INFO: loaded from: classes.dex */
public class FadeOutTiles extends TiledGrid3D {
    private final int start;
    private Vector2 tmpVector = new Vector2();

    public FadeOutTiles(int start) {
        this.start = start;
    }

    @Override // com.gaiaonline.monstergalaxy.battle.anim.TiledGrid3D
    public void updateVertices(float progress) {
        float distance;
        for (int i = 0; i < this.gridSizeX; i++) {
            try {
                for (int j = 0; j < this.gridSizeY; j++) {
                    if (this.start == 0) {
                        distance = transUpperRight(i, j, progress);
                    } else {
                        distance = transLowerLeft(i, j, progress);
                    }
                    if (distance == 0.0f) {
                        turnOffTile(i, j);
                    } else if (distance < 1.0f) {
                        transformTile(i, j, distance);
                    } else {
                        turnOnTile(i, j);
                    }
                }
            } catch (Exception e) {
                return;
            }
        }
        if (this.mesh != null && this.vertices != null) {
            this.mesh.setVertices(this.vertices);
        }
    }

    private float transLowerLeft(int x, int y, float progress) {
        this.tmpVector.x = this.gridSizeX;
        this.tmpVector.y = this.gridSizeY;
        this.tmpVector.mul(progress);
        if (this.tmpVector.x + this.tmpVector.y == 0.0f) {
            return 1.0f;
        }
        return (float) Math.pow((x + y) / (this.tmpVector.x + this.tmpVector.y), 6.0d);
    }

    private float transUpperRight(int x, int y, float progress) {
        this.tmpVector.x = this.gridSizeX;
        this.tmpVector.y = this.gridSizeY;
        this.tmpVector.mul(1.0f - progress);
        if (this.tmpVector.x + this.tmpVector.y == 0.0f) {
            return 1.0f;
        }
        return (float) Math.pow((this.tmpVector.x + this.tmpVector.y) / (x + y), 6.0d);
    }

    private void turnOnTile(int x, int y) {
        setTile(x, y, getOriginalTile(x, y));
    }

    private void turnOffTile(int x, int y) {
        Quad3 coords = new Quad3();
        setTile(x, y, coords);
    }

    private void transformTile(int x, int y, float distance) {
        Quad3 coords = getOriginalTile(x, y);
        coords.bl.x += (26.666f / 2.0f) * (1.0f - distance);
        coords.bl.y += (26.666f / 2.0f) * (1.0f - distance);
        coords.br.x -= (26.666f / 2.0f) * (1.0f - distance);
        coords.br.y += (26.666f / 2.0f) * (1.0f - distance);
        coords.tl.x += (26.666f / 2.0f) * (1.0f - distance);
        coords.tl.y -= (26.666f / 2.0f) * (1.0f - distance);
        coords.tr.x -= (26.666f / 2.0f) * (1.0f - distance);
        coords.tr.y -= (26.666f / 2.0f) * (1.0f - distance);
        setTile(x, y, coords);
    }
}
