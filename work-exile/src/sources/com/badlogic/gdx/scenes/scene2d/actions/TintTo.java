package com.badlogic.gdx.scenes.scene2d.actions;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.AnimationAction;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class TintTo extends AnimationAction {
    private static final ActionResetingPool<TintTo> pool = new ActionResetingPool<TintTo>(4, 100) { // from class: com.badlogic.gdx.scenes.scene2d.actions.TintTo.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.badlogic.gdx.utils.Pool
        public TintTo newObject() {
            return new TintTo();
        }
    };
    protected Float a;
    protected float startA;
    protected float startB;
    protected float startG;
    protected float startR;
    protected float r = 0.0f;
    protected float g = 0.0f;
    protected float b = 0.0f;
    protected float deltaR = 0.0f;
    protected float deltaG = 0.0f;
    protected float deltaB = 0.0f;
    protected float deltaA = 0.0f;

    public static TintTo $(float r, float g, float b, float duration) {
        TintTo action = (TintTo) pool.obtain();
        action.r = r;
        action.g = g;
        action.b = b;
        action.a = null;
        action.duration = duration;
        action.invDuration = 1.0f / duration;
        return action;
    }

    public static TintTo $(float r, float g, float b, Float a, float duration) {
        TintTo action = $(r, g, b, duration);
        action.a = a;
        return action;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public void setTarget(Actor actor) {
        this.target = actor;
        this.startR = this.target.color.r;
        this.deltaR = this.r - this.target.color.r;
        this.startG = this.target.color.g;
        this.deltaG = this.r - this.target.color.g;
        this.startB = this.target.color.b;
        this.deltaB = this.r - this.target.color.b;
        if (this.a != null) {
            this.startA = this.target.color.a;
            this.deltaA = this.a.floatValue() - this.target.color.a;
        }
        this.taken = 0.0f;
        this.done = false;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public void act(float delta) {
        float alpha = createInterpolatedAlpha(delta);
        if (this.done) {
            this.target.color.r = this.r;
            this.target.color.g = this.g;
            this.target.color.b = this.b;
            if (this.a != null) {
                this.target.color.a = this.a.floatValue();
                return;
            }
            return;
        }
        this.target.color.r = this.startR + (this.deltaR * alpha);
        this.target.color.g = this.startG + (this.deltaG * alpha);
        this.target.color.b = this.startB + (this.deltaB * alpha);
        if (this.a != null) {
            this.target.color.a = this.startA + (this.deltaA * alpha);
        }
    }

    @Override // com.badlogic.gdx.scenes.scene2d.AnimationAction, com.badlogic.gdx.scenes.scene2d.Action
    public void finish() {
        super.finish();
        pool.free(this);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public Action copy() {
        TintTo tintTo = $(this.r, this.g, this.b, this.a, this.duration);
        if (this.interpolator != null) {
            tintTo.setInterpolator(this.interpolator.copy());
        }
        return tintTo;
    }
}
