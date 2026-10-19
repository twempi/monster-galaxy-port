package com.badlogic.gdx.scenes.scene2d.actions;

/* JADX INFO: loaded from: classes.dex */
public class TintToAction extends TemporalAction {
    private Float endA;
    private float endB;
    private float endG;
    private float endR;
    private float startA;
    private float startB;
    private float startG;
    private float startR;

    @Override // com.badlogic.gdx.scenes.scene2d.actions.TemporalAction
    protected void update(float percent) {
        getActor().getColor().r = this.startR + ((this.endR - this.startR) * percent);
        getActor().getColor().g = this.startG + ((this.endG - this.startG) * percent);
        getActor().getColor().b = this.startB + ((this.endB - this.startB) * percent);
        if (this.endA != null) {
            getActor().getColor().a = this.startA + ((this.endA.floatValue() - this.startA) * percent);
        }
    }

    @Override // com.badlogic.gdx.scenes.scene2d.actions.TemporalAction
    protected void begin() {
        super.begin();
        this.startR = this.actor.getColor().r;
        this.startG = this.actor.getColor().g;
        this.startB = this.actor.getColor().b;
        this.startA = this.actor.getColor().a;
    }

    public void setTint(float r, float g, float b, Float a) {
        this.endR = r;
        this.endG = g;
        this.endB = b;
        this.endA = a;
    }
}
