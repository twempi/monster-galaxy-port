package com.gaiaonline.monstergalaxy.battle.anim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.ActionResetingPool;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Flip extends Action3D {
    private static final ActionResetingPool<Flip> pool = new ActionResetingPool<Flip>(4, 100) { // from class: com.gaiaonline.monstergalaxy.battle.anim.Flip.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.badlogic.gdx.utils.Pool
        public Flip newObject() {
            return new Flip();
        }
    };
    private float angle;
    private Decal decal;
    private DecalBatch decalBatch;
    private PerspectiveCamera flipCam;
    private float rotationAngle;
    private float targetAngle;
    private boolean toLeft;

    public static Flip $(boolean toLeft, float duration) {
        Flip action = (Flip) pool.obtain();
        action.toLeft = toLeft;
        action.targetAngle = toLeft ? Opcodes.GETFIELD : -180;
        action.duration = duration;
        action.invDuration = 1.0f / duration;
        action.taken = 0.0f;
        action.done = false;
        return action;
    }

    @Override // com.gaiaonline.monstergalaxy.battle.anim.Action3D, com.badlogic.gdx.scenes.scene2d.Action
    public void setTarget(Actor actor) {
        super.setTarget(actor);
        prepare((MogaActor) actor);
    }

    @Override // com.gaiaonline.monstergalaxy.battle.anim.Action3D, com.badlogic.gdx.scenes.scene2d.Action
    public void act(float delta) {
        float alpha = createInterpolatedAlpha(delta);
        if (this.done) {
            ((MogaActor) this.target).setAction3D(null);
            ((MogaActor) this.target).region.flip(true, false);
        } else {
            this.angle = (this.targetAngle * alpha) - this.rotationAngle;
            this.rotationAngle += this.angle;
        }
    }

    @Override // com.badlogic.gdx.scenes.scene2d.AnimationAction, com.badlogic.gdx.scenes.scene2d.Action
    public void finish() {
        super.finish();
        this.decalBatch.dispose();
        pool.free(this);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public Action copy() {
        Flip action = $(this.toLeft, this.duration);
        if (this.interpolator != null) {
            action.setInterpolator(this.interpolator.copy());
        }
        return action;
    }

    @Override // com.gaiaonline.monstergalaxy.battle.anim.Action3D
    public void render(SpriteBatch batch) {
        batch.end();
        this.flipCam.apply(Gdx.gl10);
        this.decal.rotateY(this.angle);
        this.decalBatch.add(this.decal);
        this.decalBatch.flush();
        batch.begin();
    }

    private void prepare(MogaActor actor) {
        if (this.flipCam == null) {
            this.flipCam = new PerspectiveCamera(45.0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            this.flipCam.near = 0.1f;
            this.flipCam.far = 1000.0f;
            this.flipCam.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 385.0f * ResolutionManager.getScaleFactor());
            this.flipCam.direction.set(0.0f, 0.0f, -1.0f);
            this.flipCam.update();
        }
        this.decal = Decal.newDecal(actor.mogaImage, true);
        this.decal.setPosition(actor.x + (actor.width / 2.0f), actor.y + (actor.height / 2.0f), 0.0f);
        this.decal.setScale(ResolutionManager.getScaleFactor());
        this.decalBatch = new DecalBatch();
    }
}
