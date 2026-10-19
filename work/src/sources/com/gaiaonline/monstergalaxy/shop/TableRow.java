package com.gaiaonline.monstergalaxy.shop;

import com.badlogic.gdx.math.Vector2;
import com.gaiaonline.monstergalaxy.screen.ScreenGroup;

/* JADX INFO: loaded from: classes.dex */
public class TableRow<T> extends ScreenGroup {
    public static final Vector2 CELL_SIZE = new Vector2(404.0f, 70.0f);
    private T data;
    protected Listener<T> listener;

    public interface Listener<T> {
        void itemSelected(T t);
    }

    public TableRow(T data) {
        this.data = data;
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenElement
    public Vector2 getSize() {
        return CELL_SIZE;
    }

    public T getData() {
        return this.data;
    }

    public void setListener(Listener<T> listener) {
        this.listener = listener;
    }
}
