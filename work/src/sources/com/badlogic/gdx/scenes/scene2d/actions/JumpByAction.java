package com.badlogic.gdx.scenes.scene2d.actions;

import com.badlogic.gdx.scenes.scene2d.Actor;

/* JADX INFO: loaded from: classes.dex */
public class JumpByAction extends TemporalAction {
    protected float deltaX;
    protected float deltaY;
    private float height;
    private int jumps;
    protected float startX;
    protected float startY;

    public void setX(int x) {
        this.deltaX = x;
    }

    public void setY(int y) {
        this.deltaY = y;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setJumps(int jumps) {
        this.jumps = jumps;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.actions.TemporalAction
    protected void update(float percent) {
        if (percent == 1.0f) {
            getActor().setX(this.startX + this.deltaX);
            getActor().setY(this.startY + this.deltaY);
        } else {
            getActor().setX(this.startX + (this.deltaX * percent));
            this.actor.setY(this.startY + (this.height * ((float) Math.abs(Math.sin(3.1415927f * percent * this.jumps)))));
        }
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public void setActor(Actor actor) {
        super.setActor(actor);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.actions.TemporalAction
    protected void begin() {
        super.begin();
        this.startX = getActor().getX();
        this.startY = getActor().getY();
    }
}
