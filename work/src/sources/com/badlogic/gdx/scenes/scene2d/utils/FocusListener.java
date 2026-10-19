package com.badlogic.gdx.scenes.scene2d.utils;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

/* JADX INFO: loaded from: classes.dex */
public abstract class FocusListener implements EventListener {
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // com.badlogic.gdx.scenes.scene2d.EventListener
    public boolean handle(Event event) {
        if (event instanceof FocusEvent) {
            FocusEvent focusEvent = (FocusEvent) event;
            switch (focusEvent.getType()) {
                case keyboard:
                    keyboardFocusChanged(focusEvent, event.getTarget(), focusEvent.isFocused());
                    break;
                case scroll:
                    scrollFocusChanged(focusEvent, event.getTarget(), focusEvent.isFocused());
                    break;
            }
        }
        return false;
    }

    public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
    }

    public void scrollFocusChanged(FocusEvent event, Actor actor, boolean focused) {
    }

    public static class FocusEvent extends Event {
        private boolean focused;
        private Type type;

        public enum Type {
            keyboard,
            scroll
        }

        public boolean isFocused() {
            return this.focused;
        }

        public void setFocused(boolean focused) {
            this.focused = focused;
        }

        public Type getType() {
            return this.type;
        }

        public void setType(Type focusType) {
            this.type = focusType;
        }
    }
}
