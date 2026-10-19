package com.badlogic.gdx.scenes.scene2d.actions;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;
import com.badlogic.gdx.utils.GdxRuntimeException;

/* JADX INFO: loaded from: classes.dex */
public class LayoutAction extends Action {
    private boolean enabled;

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public void setActor(Actor actor) {
        if (actor != null && !(actor instanceof Layout)) {
            throw new GdxRuntimeException("Actor must implement layout: " + actor);
        }
        super.setActor(actor);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public boolean act(float delta) {
        ((Layout) this.actor).setLayoutEnabled(this.enabled);
        return true;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setLayoutEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
