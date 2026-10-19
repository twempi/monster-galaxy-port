package com.gaiaonline.monstergalaxy.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.gaiaonline.monstergalaxy.graphics.util.ActorUtil;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class StageScreenElement extends ScreenElement {
    protected Stage stage;

    public StageScreenElement() {
        setSize(480.0f, 320.0f);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenElement
    protected void present(Vector2 pos, SpriteBatch batcher) {
        if (this.stage != null && this.visible) {
            this.stage.act(Gdx.graphics.getDeltaTime());
            batcher.end();
            this.stage.draw();
            batcher.begin();
        }
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenElement
    protected void dispose() {
        if (this.stage != null) {
            ActorUtil.disposeStageRecursive(this.stage);
        }
    }
}
