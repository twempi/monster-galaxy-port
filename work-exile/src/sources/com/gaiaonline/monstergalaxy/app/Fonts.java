package com.gaiaonline.monstergalaxy.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Fonts {
    public static BitmapFont italic;
    public static BitmapFont regular;

    public static void initialize() {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("data/fonts/pack"));
        TextureRegion italicTexture = atlas.findRegion("VAGRunds");
        italic = new BitmapFont(Gdx.files.internal("data/fonts/VAGRunds.fnt"), italicTexture, false);
        TextureRegion regularTexture = atlas.findRegion("VAGRundsRegular");
        regular = new BitmapFont(Gdx.files.internal("data/fonts/VAGRundsRegular.fnt"), regularTexture, false);
    }
}
