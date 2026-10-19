package com.badlogic.gdx.graphics.g2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.GdxRuntimeException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/* JADX INFO: loaded from: classes.dex */
public class BitmapFont implements Disposable {
    private static final int LOG2_PAGE_SIZE = 9;
    private static final int PAGES = 128;
    private static final int PAGE_SIZE = 512;
    private final BitmapFontCache cache;
    final BitmapFontData data;
    private boolean flipped;
    private boolean integer;
    private boolean ownsTexture;
    TextureRegion region;
    public static final char[] xChars = {'x', 'e', 'a', 'o', 'n', 's', 'r', 'c', 'u', 'm', 'v', 'w', 'z'};
    public static final char[] capChars = {'M', 'N', 'B', 'D', 'C', 'E', 'F', 'K', 'A', 'G', 'H', 'I', 'J', 'L', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    public enum HAlignment {
        LEFT,
        CENTER,
        RIGHT
    }

    public BitmapFont() {
        this(Gdx.files.classpath("com/badlogic/gdx/utils/arial-15.fnt"), Gdx.files.classpath("com/badlogic/gdx/utils/arial-15.png"), false, true);
    }

    public BitmapFont(boolean flip) {
        this(Gdx.files.classpath("com/badlogic/gdx/utils/arial-15.fnt"), Gdx.files.classpath("com/badlogic/gdx/utils/arial-15.png"), flip, true);
    }

    public BitmapFont(FileHandle fontFile, TextureRegion region, boolean flip) {
        this(new BitmapFontData(fontFile, flip), region, true);
    }

    public BitmapFont(FileHandle fontFile, boolean flip) {
        this(new BitmapFontData(fontFile, flip), (TextureRegion) null, true);
    }

    public BitmapFont(FileHandle fontFile, FileHandle imageFile, boolean flip) {
        this(fontFile, imageFile, flip, true);
    }

    public BitmapFont(FileHandle fontFile, FileHandle imageFile, boolean flip, boolean integer) {
        this(new BitmapFontData(fontFile, flip), new TextureRegion(new Texture(imageFile, false)), integer);
        this.ownsTexture = true;
    }

    public BitmapFont(BitmapFontData data, TextureRegion region, boolean integer) {
        this.cache = new BitmapFontCache(this);
        this.region = region == null ? new TextureRegion(new Texture(Gdx.files.internal(data.imagePath), false)) : region;
        this.flipped = data.flipped;
        this.data = data;
        this.integer = integer;
        this.cache.setUseIntegerPositions(integer);
        load(data);
        this.ownsTexture = region == null;
    }

    private void load(BitmapFontData data) {
        float invTexWidth = 1.0f / this.region.getTexture().getWidth();
        float invTexHeight = 1.0f / this.region.getTexture().getHeight();
        float u = this.region.u;
        float v = this.region.v;
        float offsetX = 0.0f;
        float offsetY = 0.0f;
        float regionWidth = this.region.getRegionWidth();
        float regionHeight = this.region.getRegionHeight();
        if (this.region instanceof TextureAtlas.AtlasRegion) {
            TextureAtlas.AtlasRegion atlasRegion = (TextureAtlas.AtlasRegion) this.region;
            offsetX = atlasRegion.offsetX;
            offsetY = (atlasRegion.originalHeight - atlasRegion.packedHeight) - atlasRegion.offsetY;
        }
        Glyph[][] arr$ = data.glyphs;
        for (Glyph[] page : arr$) {
            if (page != null) {
                for (Glyph glyph : page) {
                    if (glyph != null) {
                        float x = glyph.srcX;
                        float x2 = glyph.srcX + glyph.width;
                        float y = glyph.srcY;
                        float y2 = glyph.srcY + glyph.height;
                        if (offsetX > 0.0f) {
                            x -= offsetX;
                            if (x < 0.0f) {
                                glyph.width = (int) (glyph.width + x);
                                glyph.xoffset = (int) (glyph.xoffset - x);
                                x = 0.0f;
                            }
                            x2 -= offsetX;
                            if (x2 > regionWidth) {
                                glyph.width = (int) (glyph.width - (x2 - regionWidth));
                                x2 = regionWidth;
                            }
                        }
                        if (offsetY > 0.0f) {
                            y -= offsetY;
                            if (y < 0.0f) {
                                glyph.height = (int) (glyph.height + y);
                                y = 0.0f;
                            }
                            y2 -= offsetY;
                            if (y2 > regionHeight) {
                                float amount = y2 - regionHeight;
                                glyph.height = (int) (glyph.height - amount);
                                glyph.yoffset = (int) (glyph.yoffset + amount);
                                y2 = regionHeight;
                            }
                        }
                        glyph.u = (x * invTexWidth) + u;
                        glyph.u2 = (x2 * invTexWidth) + u;
                        if (data.flipped) {
                            glyph.v = (y * invTexHeight) + v;
                            glyph.v2 = (y2 * invTexHeight) + v;
                        } else {
                            glyph.v2 = (y * invTexHeight) + v;
                            glyph.v = (y2 * invTexHeight) + v;
                        }
                    }
                }
            }
        }
    }

    public TextBounds draw(SpriteBatch spriteBatch, CharSequence str, float x, float y) {
        this.cache.clear();
        TextBounds bounds = this.cache.addText(str, x, y, 0, str.length());
        this.cache.draw(spriteBatch);
        return bounds;
    }

    public TextBounds draw(SpriteBatch spriteBatch, CharSequence str, float x, float y, int start, int end) {
        this.cache.clear();
        TextBounds bounds = this.cache.addText(str, x, y, start, end);
        this.cache.draw(spriteBatch);
        return bounds;
    }

    public TextBounds drawMultiLine(SpriteBatch spriteBatch, CharSequence str, float x, float y) {
        this.cache.clear();
        TextBounds bounds = this.cache.addMultiLineText(str, x, y, 0.0f, HAlignment.LEFT);
        this.cache.draw(spriteBatch);
        return bounds;
    }

    public TextBounds drawMultiLine(SpriteBatch spriteBatch, CharSequence str, float x, float y, float alignmentWidth, HAlignment alignment) {
        this.cache.clear();
        TextBounds bounds = this.cache.addMultiLineText(str, x, y, alignmentWidth, alignment);
        this.cache.draw(spriteBatch);
        return bounds;
    }

    public TextBounds drawWrapped(SpriteBatch spriteBatch, CharSequence str, float x, float y, float wrapWidth) {
        this.cache.clear();
        TextBounds bounds = this.cache.addWrappedText(str, x, y, wrapWidth, HAlignment.LEFT);
        this.cache.draw(spriteBatch);
        return bounds;
    }

    public TextBounds drawWrapped(SpriteBatch spriteBatch, CharSequence str, float x, float y, float wrapWidth, HAlignment alignment) {
        this.cache.clear();
        TextBounds bounds = this.cache.addWrappedText(str, x, y, wrapWidth, alignment);
        this.cache.draw(spriteBatch);
        return bounds;
    }

    public TextBounds getBounds(CharSequence str) {
        return getBounds(str, 0, str.length());
    }

    public TextBounds getBounds(CharSequence str, TextBounds textBounds) {
        return getBounds(str, 0, str.length(), textBounds);
    }

    public TextBounds getBounds(CharSequence str, int start, int end) {
        return getBounds(str, start, end, this.cache.getBounds());
    }

    public TextBounds getBounds(CharSequence str, int start, int end, TextBounds textBounds) {
        BitmapFontData data = this.data;
        int width = 0;
        Glyph lastGlyph = null;
        int start2 = start;
        while (start2 < end) {
            int start3 = start2 + 1;
            lastGlyph = data.getGlyph(str.charAt(start2));
            if (lastGlyph != null) {
                width = lastGlyph.xadvance;
                start2 = start3;
                break;
            }
            start2 = start3;
        }
        while (start2 < end) {
            int start4 = start2 + 1;
            char ch = str.charAt(start2);
            Glyph g = data.getGlyph(ch);
            if (g != null) {
                int width2 = width + lastGlyph.getKerning(ch);
                lastGlyph = g;
                width = width2 + g.xadvance;
            }
            start2 = start4;
        }
        textBounds.width = width * data.scaleX;
        textBounds.height = data.capHeight;
        return textBounds;
    }

    public TextBounds getMultiLineBounds(CharSequence str) {
        return getMultiLineBounds(str, this.cache.getBounds());
    }

    public TextBounds getMultiLineBounds(CharSequence str, TextBounds textBounds) {
        int start = 0;
        float maxWidth = 0.0f;
        int numLines = 0;
        int length = str.length();
        while (start < length) {
            int lineEnd = indexOf(str, '\n', start);
            float lineWidth = getBounds(str, start, lineEnd).width;
            maxWidth = Math.max(maxWidth, lineWidth);
            start = lineEnd + 1;
            numLines++;
        }
        textBounds.width = maxWidth;
        textBounds.height = this.data.capHeight + ((numLines - 1) * this.data.lineHeight);
        return textBounds;
    }

    public TextBounds getWrappedBounds(CharSequence str, float wrapWidth) {
        return getWrappedBounds(str, wrapWidth, this.cache.getBounds());
    }

    public TextBounds getWrappedBounds(CharSequence str, float wrapWidth, TextBounds textBounds) {
        if (wrapWidth <= 0.0f) {
            wrapWidth = 2.1474836E9f;
        }
        float f = this.data.down;
        int start = 0;
        int numLines = 0;
        int length = str.length();
        float maxWidth = 0.0f;
        while (start < length) {
            int newLine = indexOf(str, '\n', start);
            while (start < newLine && isWhitespace(str.charAt(start))) {
                start++;
            }
            int lineEnd = start + computeVisibleGlyphs(str, start, newLine, wrapWidth);
            int nextStart = lineEnd + 1;
            if (lineEnd < newLine) {
                while (lineEnd > start && !isWhitespace(str.charAt(lineEnd))) {
                    lineEnd--;
                }
                if (lineEnd == start) {
                    if (nextStart > start + 1) {
                        nextStart--;
                    }
                    lineEnd = nextStart;
                } else {
                    nextStart = lineEnd;
                    while (lineEnd > start && isWhitespace(str.charAt(lineEnd - 1))) {
                        lineEnd--;
                    }
                }
            }
            if (lineEnd > start) {
                float lineWidth = getBounds(str, start, lineEnd).width;
                maxWidth = Math.max(maxWidth, lineWidth);
            }
            start = nextStart;
            numLines++;
        }
        textBounds.width = maxWidth;
        textBounds.height = this.data.capHeight + ((numLines - 1) * this.data.lineHeight);
        return textBounds;
    }

    public void computeGlyphAdvancesAndPositions(CharSequence str, FloatArray glyphAdvances, FloatArray glyphPositions) {
        glyphAdvances.clear();
        glyphPositions.clear();
        int index = 0;
        int end = str.length();
        float width = 0.0f;
        Glyph lastGlyph = null;
        BitmapFontData data = this.data;
        if (data.scaleX == 1.0f) {
            while (index < end) {
                char ch = str.charAt(index);
                Glyph g = data.getGlyph(ch);
                if (g != null) {
                    if (lastGlyph != null) {
                        width += lastGlyph.getKerning(ch);
                    }
                    lastGlyph = g;
                    glyphAdvances.add(g.xadvance);
                    glyphPositions.add(width);
                    width += g.xadvance;
                }
                index++;
            }
            glyphAdvances.add(0.0f);
            glyphPositions.add(width);
            return;
        }
        float scaleX = this.data.scaleX;
        while (index < end) {
            char ch2 = str.charAt(index);
            Glyph g2 = data.getGlyph(ch2);
            if (g2 != null) {
                if (lastGlyph != null) {
                    width += lastGlyph.getKerning(ch2) * scaleX;
                }
                lastGlyph = g2;
                float xadvance = g2.xadvance * scaleX;
                glyphAdvances.add(xadvance);
                glyphPositions.add(width);
                width += xadvance;
            }
            index++;
        }
        glyphAdvances.add(0.0f);
        glyphPositions.add(width);
    }

    public int computeVisibleGlyphs(CharSequence str, int start, int end, float availableWidth) {
        BitmapFontData data = this.data;
        int index = start;
        float width = 0.0f;
        Glyph lastGlyph = null;
        if (data.scaleX == 1.0f) {
            while (index < end) {
                char ch = str.charAt(index);
                Glyph g = data.getGlyph(ch);
                if (g != null) {
                    if (lastGlyph != null) {
                        width += lastGlyph.getKerning(ch);
                    }
                    if ((g.xadvance + width) - availableWidth > 0.001f) {
                        break;
                    }
                    width += g.xadvance;
                    lastGlyph = g;
                }
                index++;
            }
        } else {
            float scaleX = this.data.scaleX;
            while (index < end) {
                char ch2 = str.charAt(index);
                Glyph g2 = data.getGlyph(ch2);
                if (g2 != null) {
                    if (lastGlyph != null) {
                        width += lastGlyph.getKerning(ch2) * scaleX;
                    }
                    float xadvance = g2.xadvance * scaleX;
                    if ((width + xadvance) - availableWidth > 0.001f) {
                        break;
                    }
                    width += xadvance;
                    lastGlyph = g2;
                }
                index++;
            }
        }
        return index - start;
    }

    public void setColor(float color) {
        this.cache.setColor(color);
    }

    public void setColor(Color color) {
        this.cache.setColor(color);
    }

    public void setColor(float r, float g, float b, float a) {
        this.cache.setColor(r, g, b, a);
    }

    public Color getColor() {
        return this.cache.getColor();
    }

    public void setScale(float scaleX, float scaleY) {
        BitmapFontData data = this.data;
        float x = scaleX / data.scaleX;
        float y = scaleY / data.scaleY;
        data.lineHeight *= y;
        data.spaceWidth *= x;
        data.xHeight *= y;
        data.capHeight *= y;
        data.ascent *= y;
        data.descent *= y;
        data.down *= y;
        data.scaleX = scaleX;
        data.scaleY = scaleY;
    }

    public void setScale(float scaleXY) {
        setScale(scaleXY, scaleXY);
    }

    public void scale(float amount) {
        setScale(this.data.scaleX + amount, this.data.scaleY + amount);
    }

    public float getScaleX() {
        return this.data.scaleX;
    }

    public float getScaleY() {
        return this.data.scaleY;
    }

    public TextureRegion getRegion() {
        return this.region;
    }

    public float getLineHeight() {
        return this.data.lineHeight;
    }

    public float getSpaceWidth() {
        return this.data.spaceWidth;
    }

    public float getXHeight() {
        return this.data.xHeight;
    }

    public float getCapHeight() {
        return this.data.capHeight;
    }

    public float getAscent() {
        return this.data.ascent;
    }

    public float getDescent() {
        return this.data.descent;
    }

    public boolean isFlipped() {
        return this.flipped;
    }

    @Override // com.badlogic.gdx.utils.Disposable
    public void dispose() {
        if (this.ownsTexture) {
            this.region.getTexture().dispose();
        }
    }

    public void setFixedWidthGlyphs(CharSequence glyphs) {
        BitmapFontData data = this.data;
        int maxAdvance = 0;
        int end = glyphs.length();
        for (int index = 0; index < end; index++) {
            Glyph g = data.getGlyph(glyphs.charAt(index));
            if (g != null && g.xadvance > maxAdvance) {
                maxAdvance = g.xadvance;
            }
        }
        int end2 = glyphs.length();
        for (int index2 = 0; index2 < end2; index2++) {
            Glyph g2 = data.getGlyph(glyphs.charAt(index2));
            if (g2 != null) {
                g2.xoffset += (maxAdvance - g2.xadvance) / 2;
                g2.xadvance = maxAdvance;
                g2.kerning = (byte[][]) null;
            }
        }
    }

    public boolean containsCharacter(char character) {
        return this.data.getGlyph(character) != null;
    }

    public void setUseIntegerPositions(boolean integer) {
        this.integer = integer;
        this.cache.setUseIntegerPositions(integer);
    }

    public boolean usesIntegerPositions() {
        return this.integer;
    }

    public BitmapFontData getData() {
        return this.data;
    }

    public static class Glyph {
        public int height;
        public byte[][] kerning;
        public int srcX;
        public int srcY;
        public float u;
        public float u2;
        public float v;
        public float v2;
        public int width;
        public int xadvance;
        public int xoffset;
        public int yoffset;

        public int getKerning(char ch) {
            byte[] page;
            if (this.kerning == null || (page = this.kerning[ch >>> '\t']) == null) {
                return 0;
            }
            return page[ch & 511];
        }

        public void setKerning(int ch, int value) {
            if (this.kerning == null) {
                this.kerning = new byte[128][];
            }
            byte[] page = this.kerning[ch >>> 9];
            if (page == null) {
                page = new byte[512];
                this.kerning[ch >>> 9] = page;
            }
            page[ch & 511] = (byte) value;
        }
    }

    static int indexOf(CharSequence text, char ch, int start) {
        int n = text.length();
        while (start < n) {
            if (text.charAt(start) != ch) {
                start++;
            } else {
                return start;
            }
        }
        return n;
    }

    static boolean isWhitespace(char c) {
        switch (c) {
            case '\t':
            case '\n':
            case '\r':
            case ' ':
                return true;
            default:
                return false;
        }
    }

    public static class TextBounds {
        public float height;
        public float width;

        public TextBounds() {
        }

        public TextBounds(TextBounds bounds) {
            set(bounds);
        }

        public void set(TextBounds bounds) {
            this.width = bounds.width;
            this.height = bounds.height;
        }
    }

    public static class BitmapFontData {
        public float ascent;
        public float capHeight;
        public float descent;
        public float down;
        public boolean flipped;
        public FileHandle fontFile;
        public final Glyph[][] glyphs;
        public String imagePath;
        public float lineHeight;
        public float scaleX;
        public float scaleY;
        public float spaceWidth;
        public float xHeight;

        public BitmapFontData() {
            this.capHeight = 1.0f;
            this.scaleX = 1.0f;
            this.scaleY = 1.0f;
            this.glyphs = new Glyph[128][];
            this.xHeight = 1.0f;
        }

        public BitmapFontData(FileHandle fontFile, boolean flip) {
            String imgFilename;
            this.capHeight = 1.0f;
            this.scaleX = 1.0f;
            this.scaleY = 1.0f;
            this.glyphs = new Glyph[128][];
            this.xHeight = 1.0f;
            this.fontFile = fontFile;
            this.flipped = flip;
            BufferedReader reader = new BufferedReader(new InputStreamReader(fontFile.read()), 512);
            try {
                try {
                    reader.readLine();
                    String line = reader.readLine();
                    if (line == null) {
                        throw new GdxRuntimeException("Invalid font file: " + fontFile);
                    }
                    String[] common = line.split(" ", 4);
                    if (common.length < 4) {
                        throw new GdxRuntimeException("Invalid font file: " + fontFile);
                    }
                    if (!common[1].startsWith("lineHeight=")) {
                        throw new GdxRuntimeException("Invalid font file: " + fontFile);
                    }
                    this.lineHeight = Integer.parseInt(common[1].substring(11));
                    if (!common[2].startsWith("base=")) {
                        throw new GdxRuntimeException("Invalid font file: " + fontFile);
                    }
                    int baseLine = Integer.parseInt(common[2].substring(5));
                    String line2 = reader.readLine();
                    if (line2 == null) {
                        throw new GdxRuntimeException("Invalid font file: " + fontFile);
                    }
                    String[] pageLine = line2.split(" ", 4);
                    if (!pageLine[2].startsWith("file=")) {
                        throw new GdxRuntimeException("Invalid font file: " + fontFile);
                    }
                    if (pageLine[2].endsWith("\"")) {
                        imgFilename = pageLine[2].substring(6, pageLine[2].length() - 1);
                    } else {
                        imgFilename = pageLine[2].substring(5, pageLine[2].length());
                    }
                    this.imagePath = fontFile.parent().child(imgFilename).path().replaceAll("\\\\", "/");
                    this.descent = 0.0f;
                    while (true) {
                        String line3 = reader.readLine();
                        if (line3 == null || line3.startsWith("kernings ")) {
                            break;
                        }
                        if (line3.startsWith("char ")) {
                            Glyph glyph = new Glyph();
                            StringTokenizer tokens = new StringTokenizer(line3, " =");
                            tokens.nextToken();
                            tokens.nextToken();
                            int ch = Integer.parseInt(tokens.nextToken());
                            if (ch <= 65535) {
                                setGlyph(ch, glyph);
                                tokens.nextToken();
                                glyph.srcX = Integer.parseInt(tokens.nextToken());
                                tokens.nextToken();
                                glyph.srcY = Integer.parseInt(tokens.nextToken());
                                tokens.nextToken();
                                glyph.width = Integer.parseInt(tokens.nextToken());
                                tokens.nextToken();
                                glyph.height = Integer.parseInt(tokens.nextToken());
                                tokens.nextToken();
                                glyph.xoffset = Integer.parseInt(tokens.nextToken());
                                tokens.nextToken();
                                if (flip) {
                                    glyph.yoffset = Integer.parseInt(tokens.nextToken());
                                } else {
                                    glyph.yoffset = -(glyph.height + Integer.parseInt(tokens.nextToken()));
                                }
                                tokens.nextToken();
                                glyph.xadvance = Integer.parseInt(tokens.nextToken());
                                if (glyph.width > 0 && glyph.height > 0) {
                                    this.descent = Math.min(glyph.yoffset + baseLine, this.descent);
                                }
                            }
                        }
                    }
                    while (true) {
                        String line4 = reader.readLine();
                        if (line4 == null || !line4.startsWith("kerning ")) {
                            break;
                        }
                        StringTokenizer tokens2 = new StringTokenizer(line4, " =");
                        tokens2.nextToken();
                        tokens2.nextToken();
                        int first = Integer.parseInt(tokens2.nextToken());
                        tokens2.nextToken();
                        int second = Integer.parseInt(tokens2.nextToken());
                        if (first >= 0 && first <= 65535 && second >= 0 && second <= 65535) {
                            Glyph glyph2 = getGlyph((char) first);
                            tokens2.nextToken();
                            int amount = Integer.parseInt(tokens2.nextToken());
                            glyph2.setKerning(second, amount);
                        }
                    }
                    Glyph spaceGlyph = getGlyph(' ');
                    if (spaceGlyph == null) {
                        spaceGlyph = new Glyph();
                        Glyph xadvanceGlyph = getGlyph('l');
                        spaceGlyph.xadvance = (xadvanceGlyph == null ? getFirstGlyph() : xadvanceGlyph).xadvance;
                        setGlyph(32, spaceGlyph);
                    }
                    this.spaceWidth = spaceGlyph != null ? spaceGlyph.xadvance + spaceGlyph.width : 1.0f;
                    Glyph xGlyph = null;
                    for (int i = 0; i < BitmapFont.xChars.length && (xGlyph = getGlyph(BitmapFont.xChars[i])) == null; i++) {
                    }
                    this.xHeight = (xGlyph == null ? getFirstGlyph() : xGlyph).height;
                    Glyph capGlyph = null;
                    for (int i2 = 0; i2 < BitmapFont.capChars.length && (capGlyph = getGlyph(BitmapFont.capChars[i2])) == null; i2++) {
                    }
                    if (capGlyph == null) {
                        Glyph[][] arr$ = this.glyphs;
                        for (Glyph[] page : arr$) {
                            if (page != null) {
                                for (Glyph glyph3 : page) {
                                    if (glyph3 != null && glyph3.height != 0 && glyph3.width != 0) {
                                        this.capHeight = Math.max(this.capHeight, glyph3.height);
                                    }
                                }
                            }
                        }
                    } else {
                        this.capHeight = capGlyph.height;
                    }
                    this.ascent = baseLine - this.capHeight;
                    this.down = -this.lineHeight;
                    if (flip) {
                        this.ascent = -this.ascent;
                        this.down = -this.down;
                    }
                    try {
                        reader.close();
                    } catch (IOException e) {
                    }
                } catch (Exception ex) {
                    throw new GdxRuntimeException("Error loading font file: " + fontFile, ex);
                }
            } catch (Throwable th) {
                try {
                    reader.close();
                } catch (IOException e2) {
                }
                throw th;
            }
        }

        public void setGlyph(int ch, Glyph glyph) {
            Glyph[] page = this.glyphs[ch / 512];
            if (page == null) {
                page = new Glyph[512];
                this.glyphs[ch / 512] = page;
            }
            page[ch & 511] = glyph;
        }

        public Glyph getFirstGlyph() {
            Glyph[][] arr$ = this.glyphs;
            for (Glyph[] page : arr$) {
                if (page != null) {
                    for (Glyph glyph : page) {
                        if (glyph != null && glyph.height != 0 && glyph.width != 0) {
                            return glyph;
                        }
                    }
                }
            }
            throw new GdxRuntimeException("No glyphs found!");
        }

        public Glyph getGlyph(char ch) {
            Glyph[] page = this.glyphs[ch / 512];
            if (page != null) {
                return page[ch & 511];
            }
            return null;
        }

        public String getImagePath() {
            return this.imagePath;
        }

        public FileHandle getFontFile() {
            return this.fontFile;
        }
    }
}
