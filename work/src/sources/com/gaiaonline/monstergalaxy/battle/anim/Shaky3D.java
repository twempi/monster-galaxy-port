package com.gaiaonline.monstergalaxy.battle.anim;

/* JADX INFO: loaded from: classes.dex */
public class Shaky3D extends GridAction3D {
    private int range;
    private boolean shakeZ;

    public void setGridSizeX(int gridSizeX) {
        this.gridSizeX = gridSizeX;
    }

    public void setGridSizeY(int gridSizeY) {
        this.gridSizeY = gridSizeY;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public void setShakeZ(boolean shakeZ) {
        this.shakeZ = shakeZ;
    }

    @Override // com.gaiaonline.monstergalaxy.battle.anim.GridAction3D
    protected Grid3D getTransformation() {
        Grid3DShaky trans = new Grid3DShaky();
        trans.randrange = this.range;
        trans.shakeZ = this.shakeZ;
        return trans;
    }
}
