package com.badlogic.gdx.scenes.scene2d.actions;

/* JADX INFO: loaded from: classes.dex */
public class RotateToAction extends TemporalAction {
    private float end;
    private float start;

    @Override // com.badlogic.gdx.scenes.scene2d.actions.TemporalAction
    protected void begin() {
        this.start = this.actor.getRotation();
    }

    @Override // com.badlogic.gdx.scenes.scene2d.actions.TemporalAction
    protected void update(float percent) {
        this.actor.setRotation(this.start + ((this.end - this.start) * percent));
    }

    public float getRotation() {
        return this.end;
    }

    public void setRotation(float rotation) {
        this.end = rotation;
    }
}
