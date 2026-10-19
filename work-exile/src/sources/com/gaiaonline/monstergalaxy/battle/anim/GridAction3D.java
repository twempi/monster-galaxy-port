package com.gaiaonline.monstergalaxy.battle.anim;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public abstract class GridAction3D extends Action3D {
    protected int gridSizeX;
    protected int gridSizeY;
    private Texture lastTexture;
    protected Grid3D transformation;

    protected abstract Grid3D getTransformation();

    @Override // com.gaiaonline.monstergalaxy.battle.anim.Action3D, com.badlogic.gdx.scenes.scene2d.Action
    public void setTarget(Actor actor) {
        super.setTarget(actor);
        this.transformation = getTransformation();
        resetTextureIfNeeded();
        this.transformation.setGridSize(this.gridSizeX, this.gridSizeY);
        this.transformation.setPosition(this.target.x, this.target.y);
        this.transformation.setImageSize((int) this.target.width, (int) this.target.height);
        this.transformation.initialize();
    }

    private void resetTextureIfNeeded() {
        if (this.lastTexture != ((MogaActor) this.target).mogaImage.getTexture()) {
            this.lastTexture = ((MogaActor) this.target).mogaImage.getTexture();
            this.transformation.setTexture(this.lastTexture);
        }
    }

    @Override // com.gaiaonline.monstergalaxy.battle.anim.Action3D, com.badlogic.gdx.scenes.scene2d.Action
    public void act(float delta) {
        resetTextureIfNeeded();
        super.act(delta);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.AnimationAction, com.badlogic.gdx.scenes.scene2d.Action
    public void finish() {
        super.finish();
        this.lastTexture = null;
    }

    @Override // com.gaiaonline.monstergalaxy.battle.anim.Action3D
    public void render(SpriteBatch bacther) {
        this.transformation.render();
    }
}
