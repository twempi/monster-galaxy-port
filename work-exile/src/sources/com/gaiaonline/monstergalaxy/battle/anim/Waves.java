package com.gaiaonline.monstergalaxy.battle.anim;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.ActionResetingPool;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Waves extends GridAction3D {
    private static final ActionResetingPool<Waves> pool = new ActionResetingPool<Waves>(4, 100) { // from class: com.gaiaonline.monstergalaxy.battle.anim.Waves.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.badlogic.gdx.utils.Pool
        public Waves newObject() {
            return new Waves();
        }
    };
    private float amplitude;
    private boolean horizontal;
    private boolean vertical;
    private int waves;

    public static Waves $(int gridSizeX, int gridSizeY, int waves, float amplitude, boolean horizontal, boolean vertical, float duration) {
        Waves action = (Waves) pool.obtain();
        action.gridSizeX = gridSizeX;
        action.gridSizeY = gridSizeY;
        action.waves = waves;
        action.amplitude = amplitude;
        action.horizontal = horizontal;
        action.vertical = vertical;
        action.duration = duration;
        action.invDuration = 1.0f / duration;
        action.taken = 0.0f;
        action.done = false;
        return action;
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

    @Override // com.gaiaonline.monstergalaxy.battle.anim.GridAction3D, com.badlogic.gdx.scenes.scene2d.AnimationAction, com.badlogic.gdx.scenes.scene2d.Action
    public void finish() {
        super.finish();
        pool.free(this);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public Action copy() {
        Waves action = $(this.gridSizeX, this.gridSizeY, this.waves, this.amplitude, this.horizontal, this.vertical, this.duration);
        if (this.interpolator != null) {
            action.setInterpolator(this.interpolator.copy());
        }
        return action;
    }
}
