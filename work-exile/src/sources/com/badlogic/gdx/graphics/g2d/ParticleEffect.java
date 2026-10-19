package com.badlogic.gdx.graphics.g2d;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class ParticleEffect implements Disposable {
    private final Array<ParticleEmitter> emitters;

    public ParticleEffect() {
        this.emitters = new Array<>(8);
    }

    public ParticleEffect(ParticleEffect effect) {
        this.emitters = new Array<>(true, effect.emitters.size);
        int n = effect.emitters.size;
        for (int i = 0; i < n; i++) {
            this.emitters.add(new ParticleEmitter(effect.emitters.get(i)));
        }
    }

    public void start() {
        int n = this.emitters.size;
        for (int i = 0; i < n; i++) {
            this.emitters.get(i).start();
        }
    }

    public void update(float delta) {
        int n = this.emitters.size;
        for (int i = 0; i < n; i++) {
            this.emitters.get(i).update(delta);
        }
    }

    public void draw(SpriteBatch spriteBatch) {
        int n = this.emitters.size;
        for (int i = 0; i < n; i++) {
            this.emitters.get(i).draw(spriteBatch);
        }
    }

    public void draw(SpriteBatch spriteBatch, float delta) {
        int n = this.emitters.size;
        for (int i = 0; i < n; i++) {
            this.emitters.get(i).draw(spriteBatch, delta);
        }
    }

    public void allowCompletion() {
        int n = this.emitters.size;
        for (int i = 0; i < n; i++) {
            this.emitters.get(i).allowCompletion();
        }
    }

    public boolean isComplete() {
        int n = this.emitters.size;
        for (int i = 0; i < n; i++) {
            ParticleEmitter emitter = this.emitters.get(i);
            if (emitter.isContinuous() || !emitter.isComplete()) {
                return false;
            }
        }
        return true;
    }

    public void setDuration(int duration) {
        int n = this.emitters.size;
        for (int i = 0; i < n; i++) {
            ParticleEmitter emitter = this.emitters.get(i);
            emitter.setContinuous(false);
            emitter.duration = duration;
            emitter.durationTimer = 0.0f;
        }
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

    public Array<ParticleEmitter> getEmitters() {
        return this.emitters;
    }

    public ParticleEmitter findEmitter(String name) {
        int n = this.emitters.size;
        for (int i = 0; i < n; i++) {
            ParticleEmitter emitter = this.emitters.get(i);
            if (emitter.getName().equals(name)) {
                return emitter;
            }
        }
        return null;
    }

    public void save(File file) throws Throwable {
        Writer output = null;
        try {
            try {
                Writer output2 = new FileWriter(file);
                int i = 0;
                try {
                    int n = this.emitters.size;
                    int index = 0;
                    while (i < n) {
                        ParticleEmitter emitter = this.emitters.get(i);
                        int index2 = index + 1;
                        if (index > 0) {
                            output2.write("\n\n");
                        }
                        emitter.save(output2);
                        output2.write("- Image Path -\n");
                        output2.write(emitter.getImagePath() + "\n");
                        i++;
                        index = index2;
                    }
                    if (output2 != null) {
                        try {
                            output2.close();
                        } catch (IOException e) {
                        }
                    }
                } catch (IOException e2) {
                    ex = e2;
                    throw new GdxRuntimeException("Error saving effect: " + file, ex);
                } catch (Throwable th) {
                    th = th;
                    output = output2;
                    if (output != null) {
                        try {
                            output.close();
                        } catch (IOException e3) {
                        }
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (IOException e4) {
            ex = e4;
        }
    }

    public void load(FileHandle effectFile, FileHandle imagesDir) throws Throwable {
        loadEmitters(effectFile);
        loadEmitterImages(imagesDir);
    }

    public void load(FileHandle effectFile, TextureAtlas atlas) throws Throwable {
        loadEmitters(effectFile);
        loadEmitterImages(atlas);
    }

    public void loadEmitters(FileHandle effectFile) throws Throwable {
        InputStream input = effectFile.read();
        this.emitters.clear();
        BufferedReader reader = null;
        try {
            try {
                BufferedReader reader2 = new BufferedReader(new InputStreamReader(input), 512);
                do {
                    try {
                        ParticleEmitter emitter = new ParticleEmitter(reader2);
                        reader2.readLine();
                        emitter.setImagePath(reader2.readLine());
                        this.emitters.add(emitter);
                        if (reader2.readLine() == null) {
                            break;
                        }
                    } catch (IOException e) {
                        ex = e;
                        throw new GdxRuntimeException("Error loading effect: " + effectFile, ex);
                    } catch (Throwable th) {
                        th = th;
                        reader = reader2;
                        if (reader != null) {
                            try {
                                reader.close();
                            } catch (IOException e2) {
                            }
                        }
                        throw th;
                    }
                } while (reader2.readLine() != null);
                if (reader2 != null) {
                    try {
                        reader2.close();
                    } catch (IOException e3) {
                    }
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (IOException e4) {
            ex = e4;
        }
    }

    public void loadEmitterImages(TextureAtlas atlas) {
        int n = this.emitters.size;
        for (int i = 0; i < n; i++) {
            ParticleEmitter emitter = this.emitters.get(i);
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
            ParticleEmitter emitter = this.emitters.get(i);
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
        int n = this.emitters.size;
        for (int i = 0; i < n; i++) {
            ParticleEmitter emitter = this.emitters.get(i);
            emitter.getSprite().getTexture().dispose();
        }
    }
}
