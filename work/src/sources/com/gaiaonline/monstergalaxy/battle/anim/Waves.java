package com.gaiaonline.monstergalaxy.battle.anim;

/* JADX INFO: loaded from: classes.dex */
public class Waves extends GridAction3D {
    private float amplitude;
    private boolean horizontal;
    private boolean vertical;
    private int waves;

    public void setWaves(int waves) {
        this.waves = waves;
    }

    public void setAmplitude(float amplitude) {
        this.amplitude = amplitude;
    }

    public void setHorizontal(boolean horizontal) {
        this.horizontal = horizontal;
    }

    public void setVertical(boolean vertical) {
        this.vertical = vertical;
    }

    public void setGridSizeX(int gridSizeX) {
        this.gridSizeX = gridSizeX;
    }

    public void setGridSizeY(int gridSizeY) {
        this.gridSizeY = gridSizeY;
    }

    @Override // com.gaiaonline.monstergalaxy.battle.anim.GridAction3D
    protected Grid3D getTransformation() {
        Grid3DWaves trans = new Grid3DWaves();
        trans.waves = this.waves;
        trans.amplitude = this.amplitude;
        trans.horizontal = this.horizontal;
        trans.vertical = this.vertical;
        return trans;
    }
}
