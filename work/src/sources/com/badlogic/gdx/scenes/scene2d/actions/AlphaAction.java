package com.badlogic.gdx.scenes.scene2d.actions;

import com.badlogic.gdx.graphics.Color;

/* JADX INFO: loaded from: classes.dex */
public class AlphaAction extends TemporalAction {
    private Color color;
    private float end;
    private float start;

    @Override // com.badlogic.gdx.scenes.scene2d.actions.TemporalAction
    protected void begin() {
        if (this.color == null) {
            this.color = this.actor.getColor();
        }
        this.start = this.color.a;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.actions.TemporalAction
    protected void update(float percent) {
        this.color.a = this.start + ((this.end - this.start) * percent);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.actions.TemporalAction, com.badlogic.gdx.scenes.scene2d.Action, com.badlogic.gdx.utils.Pool.Poolable
    public void reset() {
        super.reset();
        this.color = null;
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public float getAlpha() {
        return this.end;
    }

    public void setAlpha(float alpha) {
        this.end = alpha;
    }
}
