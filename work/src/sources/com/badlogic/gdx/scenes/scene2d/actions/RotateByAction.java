package com.badlogic.gdx.scenes.scene2d.actions;

/* JADX INFO: loaded from: classes.dex */
public class RotateByAction extends RelativeTemporalAction {
    private float amount;

    @Override // com.badlogic.gdx.scenes.scene2d.actions.RelativeTemporalAction
    protected void updateRelative(float percentDelta) {
        this.actor.rotate(this.amount * percentDelta);
    }

    public float getAmount() {
        return this.amount;
    }

    public void setAmount(float rotationAmount) {
        this.amount = rotationAmount;
    }
}
