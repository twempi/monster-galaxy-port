package com.gaiaonline.monstergalaxy.battle.effects;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import java.io.File;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class CCParticleEffect implements Disposable {
    private final Array<CCParticleEmitter> emitters = new Array<>(8);

    public void start() {
        int n = this.emitters.size;
        for (int i = 0; i < n; i++) {
            this.emitters.get(i).start();
        }
    }

    public void draw(SpriteBatch spriteBatch, float delta) {
        int n = this.emitters.size;
        for (int i = 0; i < n; i++) {
            this.emitters.get(i).draw(spriteBatch, delta);
        }
    }

    public boolean isComplete() {
        int n = this.emitters.size;
        for (int i = 0; i < n; i++) {
            CCParticleEmitter emitter = this.emitters.get(i);
            if (emitter.isContinuous() || !emitter.isComplete()) {
                return false;
            }
        }
        return true;
    }

    public void setPosition(float x, float y) {
        int n = this.emitters.size;
        for (int i = 0; i < n; i++) {
            this.emitters.get(i).setPosition(x, y);
        }
    }

    public void setFlip(boolean flipX, boolean flipY) {
        int n = this.emitters.size;
        for (int i = 0; i < n; i++) {
            this.emitters.get(i).setFlip(flipX, flipY);
        }
    }

    public Array<CCParticleEmitter> getEmitters() {
        return this.emitters;
    }

    public CCParticleEmitter findEmitter(String name) {
        int n = this.emitters.size;
        for (int i = 0; i < n; i++) {
            CCParticleEmitter emitter = this.emitters.get(i);
            if (emitter.getName().equals(name)) {
                return emitter;
            }
        }
        return null;
    }

    public void loadEmitterImages(TextureAtlas atlas) {
        int n = this.emitters.size;
        for (int i = 0; i < n; i++) {
            CCParticleEmitter emitter = this.emitters.get(i);
            String imagePath = emitter.getImagePath();
            if (imagePath != null) {
                String imageName = new File(imagePath.replace('\\', '/')).getName();
                int lastDotIndex = imageName.lastIndexOf(46);
                if (lastDotIndex != -1) {
                    imageName = imageName.substring(0, lastDotIndex);
                }
                Sprite sprite = atlas.createSprite(imageName);
                if (sprite == null) {
                    throw new IllegalArgumentException("SpriteSheet missing image: " + imageName);
                }
                emitter.setSprite(sprite);
            }
        }
    }

    public void loadEmitterImages(FileHandle imagesDir) {
        int n = this.emitters.size;
        for (int i = 0; i < n; i++) {
            CCParticleEmitter emitter = this.emitters.get(i);
            String imagePath = emitter.getImagePath();
            if (imagePath != null) {
                String imageName = new File(imagePath.replace('\\', '/')).getName();
                emitter.setSprite(new Sprite(loadTexture(imagesDir.child(imageName))));
            }
        }
    }

    protected Texture loadTexture(FileHandle file) {
        return new Texture(file, false);
    }

    @Override // com.badlogic.gdx.utils.Disposable
    public void dispose() {
        for (CCParticleEmitter emitter : this.emitters) {
            emitter.removeParticles();
        }
    }
}
