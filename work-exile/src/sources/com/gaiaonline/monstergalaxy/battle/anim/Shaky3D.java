package com.gaiaonline.monstergalaxy.battle.anim;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.ActionResetingPool;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Shaky3D extends GridAction3D {
    private static final ActionResetingPool<Shaky3D> pool = new ActionResetingPool<Shaky3D>(4, 100) { // from class: com.gaiaonline.monstergalaxy.battle.anim.Shaky3D.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.badlogic.gdx.utils.Pool
        public Shaky3D newObject() {
            return new Shaky3D();
        }
    };
    private int range;
    private boolean shakeZ;

    public static Shaky3D $(int gridSizeX, int gridSizeY, int range, boolean shakeZ, float duration) {
        Shaky3D action = (Shaky3D) pool.obtain();
        action.gridSizeX = gridSizeX;
        action.gridSizeY = gridSizeY;
        action.range = range;
        action.shakeZ = shakeZ;
        action.duration = duration;
        action.invDuration = 1.0f / duration;
        action.taken = 0.0f;
        action.done = false;
        return action;
    }

    @Override // com.gaiaonline.monstergalaxy.battle.anim.GridAction3D
    protected Grid3D getTransformation() {
        Grid3DShaky trans = new Grid3DShaky();
        trans.randrange = this.range;
        trans.shakeZ = this.shakeZ;
        return trans;
    }

    @Override // com.gaiaonline.monstergalaxy.battle.anim.GridAction3D, com.badlogic.gdx.scenes.scene2d.AnimationAction, com.badlogic.gdx.scenes.scene2d.Action
    public void finish() {
        super.finish();
        pool.free(this);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public Action copy() {
        Shaky3D action = $(this.gridSizeX, this.gridSizeY, this.range, this.shakeZ, this.duration);
        if (this.interpolator != null) {
            action.setInterpolator(this.interpolator.copy());
        }
        return action;
    }
}
