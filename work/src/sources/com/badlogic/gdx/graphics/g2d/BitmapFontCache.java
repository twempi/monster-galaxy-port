package com.badlogic.gdx.graphics.g2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.NumberUtils;

/* JADX INFO: loaded from: classes.dex */
public class BitmapFontCache {
    private float color;
    private final BitmapFont font;
    private int idx;
    private boolean integer;
    private final Color tempColor;
    private final BitmapFont.TextBounds textBounds;
    private float[] vertices;
    private float x;
    private float y;

    public BitmapFontCache(BitmapFont font) {
        this(font, font.usesIntegerPositions());
    }

    public BitmapFontCache(BitmapFont font, boolean integer) {
        this.vertices = new float[0];
        this.color = Color.WHITE.toFloatBits();
        this.tempColor = new Color(Color.WHITE);
        this.textBounds = new BitmapFont.TextBounds();
        this.integer = true;
        this.font = font;
        this.integer = integer;
    }

    public void setPosition(float x, float y) {
        translate(x - this.x, y - this.y);
    }

    public void translate(float xAmount, float yAmount) {
        if (xAmount != 0.0f || yAmount != 0.0f) {
            if (this.integer) {
                xAmount = Math.round(xAmount);
                yAmount = Math.round(yAmount);
            }
            this.x += xAmount;
            this.y += yAmount;
            float[] vertices = this.vertices;
            int n = this.idx;
            for (int i = 0; i < n; i += 5) {
                vertices[i] = vertices[i] + xAmount;
                int i2 = i + 1;
                vertices[i2] = vertices[i2] + yAmount;
            }
        }
    }

    public void setColor(float color) {
        if (color != this.color) {
            this.color = color;
            float[] vertices = this.vertices;
            int n = this.idx;
            for (int i = 2; i < n; i += 5) {
                vertices[i] = color;
            }
        }
    }

    public void setColor(Color tint) {
        float color = tint.toFloatBits();
        if (color != this.color) {
            this.color = color;
            float[] vertices = this.vertices;
            int n = this.idx;
            for (int i = 2; i < n; i += 5) {
                vertices[i] = color;
            }
        }
    }

    public void setColor(float r, float g, float b, float a) {
        int intBits = (((int) (255.0f * a)) << 24) | (((int) (255.0f * b)) << 16) | (((int) (255.0f * g)) << 8) | ((int) (255.0f * r));
        float color = NumberUtils.intToFloatColor(intBits);
        if (color != this.color) {
            this.color = color;
            float[] vertices = this.vertices;
            int n = this.idx;
            for (int i = 2; i < n; i += 5) {
                vertices[i] = color;
            }
        }
    }

    public void setColor(Color tint, int start, int end) {
        float color = tint.toFloatBits();
        float[] vertices = this.vertices;
        int n = end * 20;
        for (int i = (start * 20) + 2; i < n; i += 5) {
            vertices[i] = color;
        }
    }

    public void draw(SpriteBatch spriteBatch) {
        spriteBatch.draw(this.font.getRegion().getTexture(), this.vertices, 0, this.idx);
    }

    public void draw(SpriteBatch spriteBatch, float alphaModulation) {
        if (alphaModulation == 1.0f) {
            draw(spriteBatch);
            return;
        }
        Color color = getColor();
        float oldAlpha = color.a;
        color.a *= alphaModulation;
        setColor(color);
        draw(spriteBatch);
        color.a = oldAlpha;
        setColor(color);
    }

    public Color getColor() {
        float f = this.color;
        int intBits = NumberUtils.floatToIntColor(this.color);
        Color color = this.tempColor;
        color.r = (intBits & 255) / 255.0f;
        color.g = ((intBits >>> 8) & 255) / 255.0f;
        color.b = ((intBits >>> 16) & 255) / 255.0f;
        color.a = ((intBits >>> 24) & 255) / 255.0f;
        return color;
    }

    public void clear() {
        this.x = 0.0f;
        this.y = 0.0f;
        this.idx = 0;
    }

    private void require(int glyphCount) {
        int vertexCount = this.idx + (glyphCount * 20);
        if (this.vertices == null || this.vertices.length < vertexCount) {
            float[] newVertices = new float[vertexCount];
            System.arraycopy(this.vertices, 0, newVertices, 0, this.idx);
            this.vertices = newVertices;
        }
    }

    private float addToCache(CharSequence str, float x, float y, int start, int end) {
        int start2;
        BitmapFont font = this.font;
        BitmapFont.Glyph lastGlyph = null;
        BitmapFont.BitmapFontData data = font.data;
        if (data.scaleX == 1.0f && data.scaleY == 1.0f) {
            while (true) {
                start2 = start;
                if (start2 >= end) {
                    break;
                }
                start = start2 + 1;
                lastGlyph = data.getGlyph(str.charAt(start2));
                if (lastGlyph != null) {
                    addGlyph(lastGlyph, x + lastGlyph.xoffset, y + lastGlyph.yoffset, lastGlyph.width, lastGlyph.height);
                    x += lastGlyph.xadvance;
                    start2 = start;
                    break;
                }
            }
            while (start2 < end) {
                int start3 = start2 + 1;
                char ch = str.charAt(start2);
                BitmapFont.Glyph g = data.getGlyph(ch);
                if (g != null) {
                    float x2 = x + lastGlyph.getKerning(ch);
                    lastGlyph = g;
                    addGlyph(lastGlyph, x2 + g.xoffset, y + g.yoffset, g.width, g.height);
                    x = x2 + g.xadvance;
                }
                start2 = start3;
            }
        } else {
            float scaleX = data.scaleX;
            float scaleY = data.scaleY;
            while (true) {
                start2 = start;
                if (start2 >= end) {
                    break;
                }
                start = start2 + 1;
                lastGlyph = data.getGlyph(str.charAt(start2));
                if (lastGlyph != null) {
                    addGlyph(lastGlyph, x + (lastGlyph.xoffset * scaleX), y + (lastGlyph.yoffset * scaleY), lastGlyph.width * scaleX, lastGlyph.height * scaleY);
                    x += lastGlyph.xadvance * scaleX;
                    start2 = start;
                    break;
                }
            }
            while (start2 < end) {
                int start4 = start2 + 1;
                char ch2 = str.charAt(start2);
                BitmapFont.Glyph g2 = data.getGlyph(ch2);
                if (g2 != null) {
                    float x3 = x + (lastGlyph.getKerning(ch2) * scaleX);
                    lastGlyph = g2;
                    addGlyph(lastGlyph, x3 + (g2.xoffset * scaleX), y + (g2.yoffset * scaleY), g2.width * scaleX, g2.height * scaleY);
                    x = x3 + (g2.xadvance * scaleX);
                }
                start2 = start4;
            }
        }
        return x - x;
    }

    private void addGlyph(BitmapFont.Glyph glyph, float x, float y, float width, float height) {
        float x2 = x + width;
        float y2 = y + height;
        float u = glyph.u;
        float u2 = glyph.u2;
        float v = glyph.v;
        float v2 = glyph.v2;
        float[] vertices = this.vertices;
        if (this.integer) {
            x = Math.round(x);
            y = Math.round(y);
            x2 = Math.round(x2);
            y2 = Math.round(y2);
        }
        int idx = this.idx;
        this.idx += 20;
        int idx2 = idx + 1;
        vertices[idx] = x;
        int idx3 = idx2 + 1;
        vertices[idx2] = y;
        int idx4 = idx3 + 1;
        vertices[idx3] = this.color;
        int idx5 = idx4 + 1;
        vertices[idx4] = u;
        int idx6 = idx5 + 1;
        vertices[idx5] = v;
        int idx7 = idx6 + 1;
        vertices[idx6] = x;
        int idx8 = idx7 + 1;
        vertices[idx7] = y2;
        int idx9 = idx8 + 1;
        vertices[idx8] = this.color;
        int idx10 = idx9 + 1;
        vertices[idx9] = u;
        int idx11 = idx10 + 1;
        vertices[idx10] = v2;
        int idx12 = idx11 + 1;
        vertices[idx11] = x2;
        int idx13 = idx12 + 1;
        vertices[idx12] = y2;
        int idx14 = idx13 + 1;
        vertices[idx13] = this.color;
        int idx15 = idx14 + 1;
        vertices[idx14] = u2;
        int idx16 = idx15 + 1;
        vertices[idx15] = v2;
        int idx17 = idx16 + 1;
        vertices[idx16] = x2;
        int idx18 = idx17 + 1;
        vertices[idx17] = y;
        int idx19 = idx18 + 1;
        vertices[idx18] = this.color;
        int idx20 = idx19 + 1;
        vertices[idx19] = u2;
        int i = idx20 + 1;
        vertices[idx20] = v;
    }

    public BitmapFont.TextBounds setText(CharSequence str, float x, float y) {
        clear();
        return addText(str, x, y, 0, str.length());
    }

    public BitmapFont.TextBounds setText(CharSequence str, float x, float y, int start, int end) {
        clear();
        return addText(str, x, y, start, end);
    }

    public BitmapFont.TextBounds addText(CharSequence str, float x, float y) {
        return addText(str, x, y, 0, str.length());
    }

    public BitmapFont.TextBounds addText(CharSequence str, float x, float y, int start, int end) {
        require(end - start);
        float y2 = y + this.font.data.ascent;
        this.textBounds.width = addToCache(str, x, y2, start, end);
        this.textBounds.height = this.font.data.capHeight;
        return this.textBounds;
    }

    public BitmapFont.TextBounds setMultiLineText(CharSequence str, float x, float y) {
        clear();
        return addMultiLineText(str, x, y, 0.0f, BitmapFont.HAlignment.LEFT);
    }

    public BitmapFont.TextBounds setMultiLineText(CharSequence str, float x, float y, float alignmentWidth, BitmapFont.HAlignment alignment) {
        clear();
        return addMultiLineText(str, x, y, alignmentWidth, alignment);
    }

    public BitmapFont.TextBounds addMultiLineText(CharSequence str, float x, float y) {
        return addMultiLineText(str, x, y, 0.0f, BitmapFont.HAlignment.LEFT);
    }

    public BitmapFont.TextBounds addMultiLineText(CharSequence str, float x, float y, float alignmentWidth, BitmapFont.HAlignment alignment) {
        BitmapFont font = this.font;
        int length = str.length();
        require(length);
        float y2 = y + font.data.ascent;
        float down = font.data.down;
        float maxWidth = 0.0f;
        int start = 0;
        int numLines = 0;
        while (start < length) {
            int lineEnd = BitmapFont.indexOf(str, '\n', start);
            float xOffset = 0.0f;
            if (alignment != BitmapFont.HAlignment.LEFT) {
                float lineWidth = font.getBounds(str, start, lineEnd).width;
                xOffset = alignmentWidth - lineWidth;
                if (alignment == BitmapFont.HAlignment.CENTER) {
                    xOffset /= 2.0f;
                }
            }
            float lineWidth2 = addToCache(str, x + xOffset, y2, start, lineEnd);
            maxWidth = Math.max(maxWidth, lineWidth2);
            start = lineEnd + 1;
            y2 += down;
            numLines++;
        }
        this.textBounds.width = maxWidth;
        this.textBounds.height = font.data.capHeight + ((numLines - 1) * font.data.lineHeight);
        return this.textBounds;
    }

    public BitmapFont.TextBounds setWrappedText(CharSequence str, float x, float y, float wrapWidth) {
        clear();
        return addWrappedText(str, x, y, wrapWidth, BitmapFont.HAlignment.LEFT);
    }

    public BitmapFont.TextBounds setWrappedText(CharSequence str, float x, float y, float wrapWidth, BitmapFont.HAlignment alignment) {
        clear();
        return addWrappedText(str, x, y, wrapWidth, alignment);
    }

    public BitmapFont.TextBounds addWrappedText(CharSequence str, float x, float y, float wrapWidth) {
        return addWrappedText(str, x, y, wrapWidth, BitmapFont.HAlignment.LEFT);
    }

    public BitmapFont.TextBounds addWrappedText(CharSequence str, float x, float y, float wrapWidth, BitmapFont.HAlignment alignment) {
        BitmapFont font = this.font;
        int length = str.length();
        require(length);
        float y2 = y + font.data.ascent;
        float down = font.data.down;
        if (wrapWidth <= 0.0f) {
            wrapWidth = 2.1474836E9f;
        }
        float maxWidth = 0.0f;
        int start = 0;
        int numLines = 0;
        while (start < length) {
            int newLine = BitmapFont.indexOf(str, '\n', start);
            while (start < newLine && BitmapFont.isWhitespace(str.charAt(start))) {
                start++;
            }
            int lineEnd = start + font.computeVisibleGlyphs(str, start, newLine, wrapWidth);
            int nextStart = lineEnd + 1;
            if (lineEnd < newLine) {
                while (lineEnd > start && !BitmapFont.isWhitespace(str.charAt(lineEnd))) {
                    lineEnd--;
                }
                if (lineEnd == start) {
                    if (nextStart > start + 1) {
                        nextStart--;
                    }
                    lineEnd = nextStart;
                } else {
                    nextStart = lineEnd;
                    while (lineEnd > start && BitmapFont.isWhitespace(str.charAt(lineEnd - 1))) {
                        lineEnd--;
                    }
                }
            }
            if (lineEnd > start) {
                float xOffset = 0.0f;
                if (alignment != BitmapFont.HAlignment.LEFT) {
                    float lineWidth = font.getBounds(str, start, lineEnd).width;
                    xOffset = wrapWidth - lineWidth;
                    if (alignment == BitmapFont.HAlignment.CENTER) {
                        xOffset /= 2.0f;
                    }
                }
                float lineWidth2 = addToCache(str, x + xOffset, y2, start, lineEnd);
                maxWidth = Math.max(maxWidth, lineWidth2);
            }
            start = nextStart;
            y2 += down;
            numLines++;
        }
        this.textBounds.width = maxWidth;
        this.textBounds.height = font.data.capHeight + ((numLines - 1) * font.data.lineHeight);
        return this.textBounds;
    }

    public BitmapFont.TextBounds getBounds() {
        return this.textBounds;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public BitmapFont getFont() {
        return this.font;
    }

    public void setUseIntegerPositions(boolean use) {
        this.integer = use;
    }

    public boolean usesIntegerPositions() {
        return this.integer;
    }

    public float[] getVertices() {
        return this.vertices;
    }
}
