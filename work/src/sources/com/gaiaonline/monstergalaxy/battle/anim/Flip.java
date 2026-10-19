package com.gaiaonline.monstergalaxy.battle.anim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;

/* JADX INFO: loaded from: classes.dex */
public class Flip extends Action3D {
    private float angle;
    private Decal decal;
    private DecalBatch decalBatch;
    private PerspectiveCamera flipCam;
    private float rotationAngle;
    private float targetAngle;

    public void setToLeft(boolean toLeft) {
        this.targetAngle = toLeft ? Opcodes.GETFIELD : -180;
    }

    @Override // com.gaiaonline.monstergalaxy.battle.anim.Action3D, com.badlogic.gdx.scenes.scene2d.Action
    public void setActor(Actor actor) {
        super.setActor(actor);
    }

    @Override // com.gaiaonline.monstergalaxy.battle.anim.Action3D, com.badlogic.gdx.scenes.scene2d.actions.TemporalAction
    protected void begin() {
        super.begin();
        this.rotationAngle = 0.0f;
        this.angle = 0.0f;
        prepare((MogaActor) this.actor);
    }

    @Override // com.gaiaonline.monstergalaxy.battle.anim.Action3D, com.badlogic.gdx.scenes.scene2d.actions.TemporalAction
    protected void end() {
        super.end();
        ((MogaActor) getActor()).setAction3D(null);
        ((TextureRegionDrawable) ((MogaActor) getActor()).getDrawable()).getRegion().flip(true, false);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.actions.TemporalAction
    protected void update(float percent) {
        this.angle = (this.targetAngle * percent) - this.rotationAngle;
        this.rotationAngle += this.angle;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.actions.TemporalAction
    public void finish() {
        super.finish();
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
        this.decal.setPosition(actor.getX() + (actor.getWidth() / 2.0f), actor.getY() + (actor.getHeight() / 2.0f), 0.0f);
        this.decal.setScale(ResolutionManager.getScaleFactor());
        this.decalBatch = new DecalBatch();
    }
}
