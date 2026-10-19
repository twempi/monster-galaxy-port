package com.badlogic.gdx.graphics.g2d.freetype;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;

/* JADX INFO: loaded from: classes.dex */
public class FreeTypeFontGenerator implements Disposable {
    public static final String DEFAULT_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890\"!`?'.,;:()[]{}<>|/@\\^$-%+=#_&~*\u007f\u0080\u0081\u0082\u0083\u0084\u0085\u0086\u0087\u0088\u0089\u008a\u008b\u008c\u008d\u008e\u008f\u0090\u0091\u0092\u0093\u0094\u0095\u0096\u0097\u0098\u0099\u009a\u009b\u009c\u009d\u009e\u009f ¡¢£¤¥¦§¨©ª«¬\u00ad®¯°±²³´µ¶·¸¹º»¼½¾¿ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ×ØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõö÷øùúûüýþÿ";
    final FreeType.Face face;
    final FreeType.Library library = FreeType.initFreeType();

    public FreeTypeFontGenerator(FileHandle font) {
        if (this.library == null) {
            throw new GdxRuntimeException("Couldn't initialize FreeType");
        }
        this.face = FreeType.newFace(this.library, font, 0);
        if (this.face == null) {
            throw new GdxRuntimeException("Couldn't create face for font '" + font + "'");
        }
        if (!FreeType.setPixelSizes(this.face, 0, 15)) {
            throw new GdxRuntimeException("Couldn't set size for font '" + font + "'");
        }
    }

    public BitmapFont generateFont(int size, String characters, boolean flip) {
        FreeTypeBitmapFontData data = generateData(size, characters, flip);
        return new BitmapFont((BitmapFont.BitmapFontData) data, data.getTextureRegion(), false);
    }

    public BitmapFont generateFont(int size) {
        return generateFont(size, DEFAULT_CHARS, false);
    }

    public FreeTypeBitmapFontData generateData(int size) {
        return generateData(size, DEFAULT_CHARS, false);
    }

    public FreeTypeBitmapFontData generateData(int size, String characters, boolean flip) {
        int kerning;
        FreeTypeBitmapFontData data = new FreeTypeBitmapFontData();
        if (!FreeType.setPixelSizes(this.face, 0, size)) {
            throw new GdxRuntimeException("Couldn't set size for font");
        }
        FreeType.SizeMetrics fontMetrics = this.face.getSize().getMetrics();
        data.flipped = flip;
        data.ascent = FreeType.toInt(fontMetrics.getAscender());
        data.descent = FreeType.toInt(fontMetrics.getDescender());
        data.lineHeight = FreeType.toInt(fontMetrics.getHeight());
        float baseLine = data.ascent;
        if (FreeType.loadChar(this.face, 32, FreeType.FT_LOAD_DEFAULT)) {
            data.spaceWidth = FreeType.toInt(this.face.getGlyph().getMetrics().getHoriAdvance());
        } else {
            data.spaceWidth = this.face.getMaxAdvanceWidth();
        }
        BitmapFont.Glyph spaceGlyph = new BitmapFont.Glyph();
        spaceGlyph.xadvance = (int) data.spaceWidth;
        data.setGlyph(32, spaceGlyph);
        char[] arr$ = BitmapFont.xChars;
        for (char xChar : arr$) {
            if (FreeType.loadChar(this.face, xChar, FreeType.FT_LOAD_DEFAULT)) {
                data.xHeight = FreeType.toInt(this.face.getGlyph().getMetrics().getHeight());
                break;
            }
        }
        if (data.xHeight == 0.0f) {
            throw new GdxRuntimeException("No x-height character found in font");
        }
        char[] arr$2 = BitmapFont.capChars;
        for (char capChar : arr$2) {
            if (FreeType.loadChar(this.face, capChar, FreeType.FT_LOAD_DEFAULT)) {
                data.capHeight = FreeType.toInt(this.face.getGlyph().getMetrics().getHeight());
                break;
            }
        }
        if (data.capHeight == 1.0f) {
            throw new GdxRuntimeException("No cap character found in font");
        }
        data.ascent -= data.capHeight;
        data.down = -data.lineHeight;
        if (flip) {
            data.ascent = -data.ascent;
            data.down = -data.down;
        }
        int maxGlyphHeight = (int) Math.ceil(data.lineHeight);
        int pageWidth = MathUtils.nextPowerOfTwo((int) Math.sqrt(maxGlyphHeight * maxGlyphHeight * characters.length()));
        PixmapPacker atlas = new PixmapPacker(pageWidth, pageWidth, Pixmap.Format.RGBA8888, 2, false);
        for (int i = 0; i < characters.length(); i++) {
            char c = characters.charAt(i);
            if (!FreeType.loadChar(this.face, c, FreeType.FT_LOAD_DEFAULT)) {
                Gdx.app.log("FreeTypeFontGenerator", "Couldn't load char '" + c + "'");
            } else if (!FreeType.renderGlyph(this.face.getGlyph(), FreeType.FT_RENDER_MODE_NORMAL)) {
                Gdx.app.log("FreeTypeFontGenerator", "Couldn't render char '" + c + "'");
            } else {
                FreeType.GlyphSlot slot = this.face.getGlyph();
                FreeType.GlyphMetrics metrics = slot.getMetrics();
                FreeType.Bitmap bitmap = slot.getBitmap();
                Pixmap pixmap = bitmap.getPixmap(Pixmap.Format.RGBA8888);
                Rectangle rect = atlas.pack("" + c, pixmap);
                BitmapFont.Glyph glyph = new BitmapFont.Glyph();
                glyph.width = pixmap.getWidth();
                glyph.height = pixmap.getHeight();
                glyph.xoffset = slot.getBitmapLeft();
                glyph.yoffset = flip ? (-slot.getBitmapTop()) + ((int) baseLine) : (-(glyph.height - slot.getBitmapTop())) - ((int) baseLine);
                glyph.xadvance = FreeType.toInt(metrics.getHoriAdvance());
                glyph.srcX = (int) rect.x;
                glyph.srcY = (int) rect.y;
                data.setGlyph(c, glyph);
                pixmap.dispose();
            }
        }
        for (int i2 = 0; i2 < characters.length(); i2++) {
            for (int j = 0; j < characters.length(); j++) {
                char firstChar = characters.charAt(i2);
                BitmapFont.Glyph first = data.getGlyph(firstChar);
                if (first != null) {
                    char secondChar = characters.charAt(j);
                    BitmapFont.Glyph second = data.getGlyph(secondChar);
                    if (second != null && (kerning = FreeType.getKerning(this.face, FreeType.getCharIndex(this.face, firstChar), FreeType.getCharIndex(this.face, secondChar), 0)) != 0) {
                        first.setKerning(secondChar, FreeType.toInt(kerning));
                    }
                }
            }
        }
        TextureAtlas textureAtlas = atlas.generateTextureAtlas(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest, false);
        data.region = new TextureRegion(textureAtlas.getRegions().get(0).getTexture());
        return data;
    }

    @Override // com.badlogic.gdx.utils.Disposable
    public void dispose() {
        FreeType.doneFace(this.face);
        FreeType.doneFreeType(this.library);
    }

    public static class FreeTypeBitmapFontData extends BitmapFont.BitmapFontData {
        TextureRegion region;

        public TextureRegion getTextureRegion() {
            return this.region;
        }
    }
}
