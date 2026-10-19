package com.badlogic.gdx.graphics.g2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.NumberUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class BitmapFont implements Disposable {
    private static final int LOG2_PAGE_SIZE = 9;
    private static final int PAGES = 128;
    private static final int PAGE_SIZE = 512;
    private float color;
    final BitmapFontData data;
    private boolean flipped;
    private boolean integer;
    TextureRegion region;
    private Color tempColor;
    private final TextBounds textBounds;
    static final char[] xChars = {'x', 'e', 'a', 'o', 'n', 's', 'r', 'c', 'u', 'm', 'v', 'w', 'z'};
    static final char[] capChars = {'M', 'N', 'B', 'D', 'C', 'E', 'F', 'K', 'A', 'G', 'H', 'I', 'J', 'L', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    public enum HAlignment {
        LEFT,
        CENTER,
        RIGHT
    }

    public static class BitmapFontData {
        float ascent;
        float capHeight;
        float descent;
        float down;
        final boolean flipped;
        String imgFile;
        final float lineHeight;
        float spaceWidth;
        float xHeight;
        float scaleX = 1.0f;
        float scaleY = 1.0f;
        final Glyph[][] glyphs = new Glyph[128][];

        public BitmapFontData(FileHandle fontFile, boolean flip) {
            String imgFilename;
            this.capHeight = 1.0f;
            this.xHeight = 1.0f;
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
                    this.imgFile = fontFile.parent().child(imgFilename).path().replaceAll("\\\\", "/");
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
                                this.descent = Math.min(glyph.yoffset + baseLine, this.descent);
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
                    this.capHeight = capGlyph.height;
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
                } catch (Throwable th) {
                    try {
                        reader.close();
                    } catch (IOException e2) {
                    }
                    throw th;
                }
            } catch (Exception ex) {
                throw new GdxRuntimeException("Error loading font file: " + fontFile, ex);
            }
        }

        private void setGlyph(int ch, Glyph glyph) {
            Glyph[] page = this.glyphs[ch / 512];
            if (page == null) {
                page = new Glyph[512];
                this.glyphs[ch / 512] = page;
            }
            page[ch & 511] = glyph;
        }

        private Glyph getFirstGlyph() {
            Glyph[][] arr$ = this.glyphs;
            for (Glyph[] page : arr$) {
                if (page != null) {
                    for (Glyph glyph : page) {
                        if (glyph != null) {
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

        public String getImageFile() {
            return this.imgFile;
        }
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
    }

    public BitmapFont(BitmapFontData data, TextureRegion region, boolean integer) {
        this.textBounds = new TextBounds();
        this.color = Color.WHITE.toFloatBits();
        this.tempColor = new Color(1.0f, 1.0f, 1.0f, 1.0f);
        this.integer = true;
        this.region = region == null ? new TextureRegion(new Texture(Gdx.files.internal(data.imgFile), false)) : region;
        this.flipped = data.flipped;
        this.integer = integer;
        this.data = data;
        load(data);
    }

    private void load(BitmapFontData data) {
        float invTexWidth = 1.0f / this.region.getTexture().getWidth();
        float invTexHeight = 1.0f / this.region.getTexture().getHeight();
        float u = this.region.u;
        float v = this.region.v;
        Glyph[][] arr$ = data.glyphs;
        for (Glyph[] page : arr$) {
            if (page != null) {
                for (Glyph glyph : page) {
                    if (glyph != null) {
                        glyph.u = (glyph.srcX * invTexWidth) + u;
                        glyph.u2 = ((glyph.srcX + glyph.width) * invTexWidth) + u;
                        if (data.flipped) {
                            glyph.v = (glyph.srcY * invTexHeight) + v;
                            glyph.v2 = ((glyph.srcY + glyph.height) * invTexHeight) + v;
                        } else {
                            glyph.v2 = (glyph.srcY * invTexHeight) + v;
                            glyph.v = ((glyph.srcY + glyph.height) * invTexHeight) + v;
                        }
                    }
                }
            }
        }
    }

    public TextBounds draw(SpriteBatch spriteBatch, CharSequence str, float x, float y) {
        return draw(spriteBatch, str, x, y, 0, str.length());
    }

    public TextBounds draw(SpriteBatch spriteBatch, CharSequence str, float x, float y, int start, int end) {
        int start2;
        float batchColor = spriteBatch.color;
        spriteBatch.setColor(this.color);
        Texture texture = this.region.getTexture();
        float y2 = y + this.data.ascent;
        Glyph lastGlyph = null;
        if (this.data.scaleX == 1.0f && this.data.scaleY == 1.0f) {
            while (true) {
                start2 = start;
                if (start2 >= end) {
                    break;
                }
                start = start2 + 1;
                lastGlyph = this.data.getGlyph(str.charAt(start2));
                if (lastGlyph != null) {
                    if (!this.integer) {
                        spriteBatch.draw(texture, x + lastGlyph.xoffset, y2 + lastGlyph.yoffset, lastGlyph.width, lastGlyph.height, lastGlyph.u, lastGlyph.v, lastGlyph.u2, lastGlyph.v2);
                    } else {
                        spriteBatch.draw(texture, ((int) x) + lastGlyph.xoffset, ((int) y2) + lastGlyph.yoffset, lastGlyph.width, lastGlyph.height, lastGlyph.u, lastGlyph.v, lastGlyph.u2, lastGlyph.v2);
                    }
                    x += lastGlyph.xadvance;
                    start2 = start;
                    break;
                }
            }
            while (start2 < end) {
                start2++;
                char ch = str.charAt(start2);
                Glyph g = this.data.getGlyph(ch);
                if (g != null) {
                    float x2 = x + lastGlyph.getKerning(ch);
                    lastGlyph = g;
                    if (!this.integer) {
                        spriteBatch.draw(texture, x2 + lastGlyph.xoffset, y2 + lastGlyph.yoffset, lastGlyph.width, lastGlyph.height, lastGlyph.u, lastGlyph.v, lastGlyph.u2, lastGlyph.v2);
                    } else {
                        spriteBatch.draw(texture, ((int) x2) + lastGlyph.xoffset, ((int) y2) + lastGlyph.yoffset, lastGlyph.width, lastGlyph.height, lastGlyph.u, lastGlyph.v, lastGlyph.u2, lastGlyph.v2);
                    }
                    x = x2 + g.xadvance;
                }
            }
        } else {
            float scaleX = this.data.scaleX;
            float scaleY = this.data.scaleY;
            while (true) {
                start2 = start;
                if (start2 >= end) {
                    break;
                }
                start = start2 + 1;
                lastGlyph = this.data.getGlyph(str.charAt(start2));
                if (lastGlyph != null) {
                    if (!this.integer) {
                        spriteBatch.draw(texture, x + (lastGlyph.xoffset * scaleX), y2 + (lastGlyph.yoffset * scaleY), lastGlyph.width * scaleX, lastGlyph.height * scaleY, lastGlyph.u, lastGlyph.v, lastGlyph.u2, lastGlyph.v2);
                    } else {
                        spriteBatch.draw(texture, (int) ((lastGlyph.xoffset * scaleX) + x), (int) ((lastGlyph.yoffset * scaleY) + y2), (int) (lastGlyph.width * scaleX), (int) (lastGlyph.height * scaleY), lastGlyph.u, lastGlyph.v, lastGlyph.u2, lastGlyph.v2);
                    }
                    x += lastGlyph.xadvance * scaleX;
                    start2 = start;
                    break;
                }
            }
            while (start2 < end) {
                start2++;
                char ch2 = str.charAt(start2);
                Glyph g2 = this.data.getGlyph(ch2);
                if (g2 != null) {
                    float x3 = x + (lastGlyph.getKerning(ch2) * scaleX);
                    lastGlyph = g2;
                    if (!this.integer) {
                        spriteBatch.draw(texture, x3 + (lastGlyph.xoffset * scaleX), y2 + (lastGlyph.yoffset * scaleY), lastGlyph.width * scaleX, lastGlyph.height * scaleY, lastGlyph.u, lastGlyph.v, lastGlyph.u2, lastGlyph.v2);
                    } else {
                        spriteBatch.draw(texture, (int) ((lastGlyph.xoffset * scaleX) + x3), (int) ((lastGlyph.yoffset * scaleY) + y2), (int) (lastGlyph.width * scaleX), (int) (lastGlyph.height * scaleY), lastGlyph.u, lastGlyph.v, lastGlyph.u2, lastGlyph.v2);
                    }
                    x = x3 + (g2.xadvance * scaleX);
                }
            }
        }
        spriteBatch.setColor(batchColor);
        this.textBounds.width = x - x;
        this.textBounds.height = this.data.capHeight;
        return this.textBounds;
    }

    public TextBounds drawMultiLine(SpriteBatch spriteBatch, CharSequence str, float x, float y) {
        return drawMultiLine(spriteBatch, str, x, y, 0.0f, HAlignment.LEFT);
    }

    public TextBounds drawMultiLine(SpriteBatch spriteBatch, CharSequence str, float x, float y, float alignmentWidth, HAlignment alignment) {
        float batchColor = spriteBatch.color;
        float down = this.data.down;
        int start = 0;
        int numLines = 0;
        int length = str.length();
        float maxWidth = 0.0f;
        while (start < length) {
            int lineEnd = indexOf(str, '\n', start);
            float xOffset = 0.0f;
            if (alignment != HAlignment.LEFT) {
                float lineWidth = getBounds(str, start, lineEnd).width;
                xOffset = alignmentWidth - lineWidth;
                if (alignment == HAlignment.CENTER) {
                    xOffset /= 2.0f;
                }
            }
            float lineWidth2 = draw(spriteBatch, str, x + xOffset, y, start, lineEnd).width;
            maxWidth = Math.max(maxWidth, lineWidth2);
            start = lineEnd + 1;
            y += down;
            numLines++;
        }
        spriteBatch.setColor(batchColor);
        this.textBounds.width = maxWidth;
        this.textBounds.height = this.data.capHeight + ((numLines - 1) * this.data.lineHeight);
        return this.textBounds;
    }

    public TextBounds drawWrapped(SpriteBatch spriteBatch, CharSequence str, float x, float y, float wrapWidth) {
        return drawWrapped(spriteBatch, str, x, y, wrapWidth, HAlignment.LEFT);
    }

    public TextBounds drawWrapped(SpriteBatch spriteBatch, CharSequence str, float x, float y, float wrapWidth, HAlignment alignment) {
        int nextLineStart;
        float batchColor = spriteBatch.color;
        float down = this.data.down;
        int start = 0;
        int numLines = 0;
        int length = str.length();
        float maxWidth = 0.0f;
        while (start < length) {
            int lineEnd = start + computeVisibleGlyphs(str, start, indexOf(str, '\n', start), wrapWidth);
            if (lineEnd < length) {
                while (lineEnd > start) {
                    char ch = str.charAt(lineEnd);
                    if (ch == ' ' || ch == '\n') {
                        break;
                    }
                    lineEnd--;
                }
                if (lineEnd == start) {
                    if (lineEnd == start) {
                        lineEnd = lineEnd;
                        lineEnd++;
                    }
                    lineEnd = lineEnd;
                    nextLineStart = lineEnd;
                } else {
                    nextLineStart = lineEnd + 1;
                }
            } else {
                if (lineEnd == start) {
                    lineEnd++;
                }
                nextLineStart = length;
            }
            float xOffset = 0.0f;
            if (alignment != HAlignment.LEFT) {
                float lineWidth = getBounds(str, start, lineEnd).width;
                xOffset = wrapWidth - lineWidth;
                if (alignment == HAlignment.CENTER) {
                    xOffset /= 2.0f;
                }
            }
            float lineWidth2 = draw(spriteBatch, str, x + xOffset, y, start, lineEnd).width;
            maxWidth = Math.max(maxWidth, lineWidth2);
            start = nextLineStart;
            y += down;
            numLines++;
        }
        spriteBatch.setColor(batchColor);
        this.textBounds.width = maxWidth;
        this.textBounds.height = this.data.capHeight + ((numLines - 1) * this.data.lineHeight);
        return this.textBounds;
    }

    public TextBounds getBounds(CharSequence str) {
        return getBounds(str, 0, str.length());
    }

    public TextBounds getBounds(CharSequence str, int start, int end) {
        int width = 0;
        Glyph lastGlyph = null;
        int start2 = start;
        while (start2 < end) {
            int start3 = start2 + 1;
            lastGlyph = this.data.getGlyph(str.charAt(start2));
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
            Glyph g = this.data.getGlyph(ch);
            if (g != null) {
                int width2 = width + lastGlyph.getKerning(ch);
                lastGlyph = g;
                width = width2 + g.xadvance;
            }
            start2 = start4;
        }
        this.textBounds.width = width * this.data.scaleX;
        this.textBounds.height = this.data.capHeight;
        return this.textBounds;
    }

    public TextBounds getMultiLineBounds(CharSequence str) {
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
        this.textBounds.width = maxWidth;
        this.textBounds.height = this.data.capHeight + ((numLines - 1) * this.data.lineHeight);
        return this.textBounds;
    }

    public TextBounds getWrappedBounds(CharSequence str, float wrapWidth) {
        int nextLineStart;
        int start = 0;
        int numLines = 0;
        int length = str.length();
        float maxWidth = 0.0f;
        while (start < length) {
            int lineEnd = start + computeVisibleGlyphs(str, start, indexOf(str, '\n', start), wrapWidth);
            if (lineEnd < length) {
                while (lineEnd > start) {
                    char ch = str.charAt(lineEnd);
                    if (ch == ' ' || ch == '\n') {
                        break;
                    }
                    lineEnd--;
                }
                if (lineEnd == start) {
                    if (lineEnd == start) {
                        lineEnd = lineEnd;
                        lineEnd++;
                    }
                    lineEnd = lineEnd;
                    nextLineStart = lineEnd;
                } else {
                    nextLineStart = lineEnd + 1;
                }
            } else {
                if (lineEnd == start) {
                    lineEnd++;
                }
                nextLineStart = length;
            }
            float lineWidth = getBounds(str, start, lineEnd).width;
            maxWidth = Math.max(maxWidth, lineWidth);
            start = nextLineStart;
            numLines++;
        }
        this.textBounds.width = maxWidth;
        this.textBounds.height = this.data.capHeight + ((numLines - 1) * this.data.lineHeight);
        return this.textBounds;
    }

    public void computeGlyphAdvancesAndPositions(CharSequence str, FloatArray glyphAdvances, FloatArray glyphPositions) {
        glyphAdvances.clear();
        glyphPositions.clear();
        int index = 0;
        int end = str.length();
        int width = 0;
        Glyph lastGlyph = null;
        if (this.data.scaleX == 1.0f) {
            while (index < end) {
                char ch = str.charAt(index);
                Glyph g = this.data.getGlyph(ch);
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
            Glyph g2 = this.data.getGlyph(ch2);
            if (g2 != null) {
                if (lastGlyph != null) {
                    width = (int) (width + (lastGlyph.getKerning(ch2) * scaleX));
                }
                lastGlyph = g2;
                glyphAdvances.add(g2.xadvance * scaleX);
                glyphPositions.add(width);
                width += g2.xadvance;
            }
            index++;
        }
        glyphAdvances.add(0.0f);
        glyphPositions.add(width);
    }

    public int computeVisibleGlyphs(CharSequence str, int start, int end, float availableWidth) {
        int index = start;
        int width = 0;
        Glyph lastGlyph = null;
        if (this.data.scaleX == 1.0f) {
            while (index < end) {
                char ch = str.charAt(index);
                Glyph g = this.data.getGlyph(ch);
                if (g != null) {
                    if (lastGlyph != null) {
                        width += lastGlyph.getKerning(ch);
                    }
                    lastGlyph = g;
                    if (g.width + width + g.xoffset > availableWidth) {
                        break;
                    }
                    width += g.xadvance;
                }
                index++;
            }
        } else {
            float scaleX = this.data.scaleX;
            while (index < end) {
                char ch2 = str.charAt(index);
                Glyph g2 = this.data.getGlyph(ch2);
                if (g2 != null) {
                    if (lastGlyph != null) {
                        width = (int) (width + (lastGlyph.getKerning(ch2) * scaleX));
                    }
                    lastGlyph = g2;
                    if (width + ((g2.width + g2.xoffset) * scaleX) > availableWidth) {
                        break;
                    }
                    width = (int) (width + (g2.xadvance * scaleX));
                }
                index++;
            }
        }
        return index - start;
    }

    public void setColor(float color) {
        this.color = color;
    }

    public void setColor(Color tint) {
        this.color = tint.toFloatBits();
    }

    public void setColor(float r, float g, float b, float a) {
        int intBits = (((int) (255.0f * a)) << 24) | (((int) (255.0f * b)) << 16) | (((int) (255.0f * g)) << 8) | ((int) (255.0f * r));
        this.color = NumberUtils.intBitsToFloat((-16777217) & intBits);
    }

    public Color getColor() {
        int intBits = NumberUtils.floatToRawIntBits(this.color);
        Color color = this.tempColor;
        color.r = (intBits & 255) / 255.0f;
        color.g = ((intBits >>> 8) & 255) / 255.0f;
        color.b = ((intBits >>> 16) & 255) / 255.0f;
        color.a = ((intBits >>> 24) & 255) / 255.0f;
        return color;
    }

    public void setScale(float scaleX, float scaleY) {
        this.data.spaceWidth = (this.data.spaceWidth / this.data.scaleX) * scaleX;
        this.data.xHeight = (this.data.xHeight / this.data.scaleY) * scaleY;
        this.data.capHeight = (this.data.capHeight / this.data.scaleY) * scaleY;
        this.data.ascent = (this.data.ascent / this.data.scaleY) * scaleY;
        this.data.descent = (this.data.descent / this.data.scaleY) * scaleY;
        this.data.down = (this.data.down / this.data.scaleY) * scaleY;
        this.data.scaleX = scaleX;
        this.data.scaleY = scaleY;
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
        this.region.getTexture().dispose();
    }

    public void setFixedWidthGlyphs(CharSequence glyphs) {
        int maxAdvance = 0;
        int end = glyphs.length();
        for (int index = 0; index < end; index++) {
            Glyph g = this.data.getGlyph(glyphs.charAt(index));
            if (g != null && g.xadvance > maxAdvance) {
                maxAdvance = g.xadvance;
            }
        }
        int end2 = glyphs.length();
        for (int index2 = 0; index2 < end2; index2++) {
            Glyph g2 = this.data.getGlyph(glyphs.charAt(index2));
            if (g2 != null) {
                g2.xoffset += (maxAdvance - g2.xadvance) / 2;
                g2.xadvance = maxAdvance;
                g2.kerning = (byte[][]) null;
            }
        }
    }

    static class Glyph {
        int height;
        byte[][] kerning;
        public int srcX;
        public int srcY;
        float u;
        float u2;
        float v;
        float v2;
        int width;
        int xadvance;
        int xoffset;
        int yoffset;

        Glyph() {
        }

        int getKerning(char ch) {
            byte[] page;
            if (this.kerning == null || (page = this.kerning[ch >>> '\t']) == null) {
                return 0;
            }
            return page[ch & 511];
        }

        void setKerning(int ch, int value) {
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

    public boolean containsCharacter(char character) {
        return this.data.getGlyph(character) != null;
    }

    public void setUseIntegerPositions(boolean use) {
        this.integer = use;
    }

    public boolean usesIntegerPositions() {
        return this.integer;
    }
}
