package com.badlogic.gdx.scenes.scene2d.actions;

/* JADX INFO: loaded from: classes.dex */
public class MoveToAction extends TemporalAction {
    private float endX;
    private float endY;
    private float startX;
    private float startY;

    @Override // com.badlogic.gdx.scenes.scene2d.actions.TemporalAction
    protected void begin() {
        this.startX = this.actor.getX();
        this.startY = this.actor.getY();
    }

    @Override // com.badlogic.gdx.scenes.scene2d.actions.TemporalAction
    protected void update(float percent) {
        this.actor.setPosition(this.startX + ((this.endX - this.startX) * percent), this.startY + ((this.endY - this.startY) * percent));
    }

    public void setPosition(float x, float y) {
        this.endX = x;
        this.endY = y;
    }

    public float getX() {
        return this.endX;
    }

    public void setX(float x) {
        this.endX = x;
    }

    public float getY() {
        return this.endY;
    }

    public void setY(float y) {
        this.endY = y;
    }
}
