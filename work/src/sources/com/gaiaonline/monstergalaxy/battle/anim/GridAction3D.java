package com.gaiaonline.monstergalaxy.battle.anim;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/* JADX INFO: loaded from: classes.dex */
public abstract class GridAction3D extends Action3D {
    protected int gridSizeX;
    protected int gridSizeY;
    private Texture lastTexture;
    protected Grid3D transformation;

    protected abstract Grid3D getTransformation();

    @Override // com.gaiaonline.monstergalaxy.battle.anim.Action3D, com.badlogic.gdx.scenes.scene2d.Action
    public void setActor(Actor actor) {
        super.setActor(actor);
        this.transformation = getTransformation();
        resetTextureIfNeeded();
        this.transformation.setGridSize(this.gridSizeX, this.gridSizeY);
        this.transformation.setPosition(actor.getX(), actor.getY());
        this.transformation.setImageSize((int) actor.getWidth(), (int) actor.getHeight());
        this.transformation.initialize();
    }

    private void resetTextureIfNeeded() {
        if (this.lastTexture != ((MogaActor) getActor()).mogaImage.getTexture()) {
            this.lastTexture = ((MogaActor) getActor()).mogaImage.getTexture();
            this.transformation.setTexture(this.lastTexture);
        }
    }

    @Override // com.badlogic.gdx.scenes.scene2d.actions.TemporalAction
    protected void update(float percent) {
        resetTextureIfNeeded();
    }

    @Override // com.badlogic.gdx.scenes.scene2d.actions.TemporalAction
    public void finish() {
        super.finish();
        this.lastTexture = null;
    }

    @Override // com.gaiaonline.monstergalaxy.battle.anim.Action3D
    public void render(SpriteBatch bacther) {
        this.transformation.render();
    }
}
