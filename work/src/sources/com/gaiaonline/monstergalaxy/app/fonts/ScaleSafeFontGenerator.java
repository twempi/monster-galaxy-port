package com.gaiaonline.monstergalaxy.app.fonts;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/* JADX INFO: loaded from: classes.dex */
public class ScaleSafeFontGenerator extends FreeTypeFontGenerator {
    public ScaleSafeFontGenerator(FileHandle font) {
        super(font);
    }

    @Override // com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
    public BitmapFont generateFont(int size, String characters, boolean flip) {
        FreeTypeFontGenerator.FreeTypeBitmapFontData data = generateData(size, characters, flip);
        return new ScaleSafeBitmapFont((BitmapFont.BitmapFontData) data, data.getTextureRegion(), false);
    }
}
