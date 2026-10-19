package com.gaiaonline.monstergalaxy.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.gaiaonline.monstergalaxy.graphics.util.PieMesh;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class PieMeshElement extends ScreenElement {
    private float initialCompletionRate;
    private PieMesh pieMesh;
    private Texture texture;

    public PieMeshElement(float radius) {
        this.anchorPoint = ScreenElement.RelPoint.CENTER;
        setSize(radius * 2.0f, 2.0f * radius);
    }

    public PieMeshElement(float radius, Texture texture) {
        this(radius);
        this.texture = texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void setCompletionRate(float completionRate) {
        if (this.pieMesh != null) {
            this.pieMesh.setCompletionRate(completionRate);
        } else {
            this.initialCompletionRate = completionRate;
        }
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenElement
    protected void present(Vector2 pos, SpriteBatch batcher) {
        if (this.pieMesh == null) {
            this.pieMesh = new PieMesh(pos.x + (this.size.x / 2.0f), pos.y + (this.size.y / 2.0f), this.size.x / 2.0f);
            this.pieMesh.setCompletionRate(this.initialCompletionRate);
        }
        batcher.disableBlending();
        this.texture.bind();
        try {
            this.pieMesh.render(6);
        } catch (Exception e) {
            Gdx.app.log("ERROR", "Rendering PieMesh", e);
        }
        batcher.enableBlending();
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenElement
    protected void dispose() {
        if (this.pieMesh != null) {
            this.pieMesh.dispose();
        }
        if (this.texture != null) {
            this.texture.dispose();
        }
        this.texture = null;
    }
}
