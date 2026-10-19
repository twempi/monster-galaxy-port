package com.gaiaonline.monstergalaxy.app.fonts;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/* JADX INFO: loaded from: classes.dex */
public class ScaleSafeBitmapFont extends BitmapFont {
    BitmapFont.BitmapFontData unscaledData;

    public ScaleSafeBitmapFont(FileHandle fontFile, TextureRegion region, boolean flip) {
        super(fontFile, region, flip);
    }

    public ScaleSafeBitmapFont(BitmapFont.BitmapFontData data, TextureRegion region, boolean integer) {
        super(data, region, integer);
    }

    @Override // com.badlogic.gdx.graphics.g2d.BitmapFont
    public void setScale(float scaleX, float scaleY) {
        BitmapFont.BitmapFontData data = getData();
        if (this.unscaledData == null) {
            this.unscaledData = new BitmapFont.BitmapFontData();
            this.unscaledData.lineHeight = data.lineHeight;
            this.unscaledData.spaceWidth = data.spaceWidth;
            this.unscaledData.xHeight = data.xHeight;
            this.unscaledData.capHeight = data.capHeight;
            this.unscaledData.ascent = data.ascent;
            this.unscaledData.descent = data.descent;
            this.unscaledData.down = data.down;
            this.unscaledData.scaleX = data.scaleX;
            this.unscaledData.scaleY = data.scaleY;
        }
        float x = this.unscaledData.scaleX * scaleX;
        float y = this.unscaledData.scaleY * scaleY;
        data.lineHeight = this.unscaledData.lineHeight * y;
        data.spaceWidth = this.unscaledData.spaceWidth * x;
        data.xHeight = this.unscaledData.xHeight * y;
        data.capHeight = this.unscaledData.capHeight * y;
        data.ascent = this.unscaledData.ascent * y;
        data.descent = this.unscaledData.descent * y;
        data.down = this.unscaledData.down * y;
        data.scaleX = scaleX;
        data.scaleY = scaleY;
    }
}
