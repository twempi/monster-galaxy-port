package com.badlogic.gdx.graphics.g2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.NumberUtils;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class BitmapFontCache implements Disposable {
    private float color;
    private final BitmapFont font;
    private int idx;
    private boolean integer;
    private final BitmapFont.TextBounds textBounds;
    private final Color tmpColor;
    private float[] vertices;
    private float x;
    private float y;

    public BitmapFontCache(BitmapFont font) {
        this(font, true);
    }

    public BitmapFontCache(BitmapFont font, boolean integer) {
        this.vertices = new float[0];
        this.color = Color.WHITE.toFloatBits();
        this.tmpColor = new Color(Color.WHITE);
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
                xAmount = (int) xAmount;
                yAmount = (int) yAmount;
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
        float color = NumberUtils.intBitsToFloat((-16777217) & intBits);
        if (color != this.color) {
            this.color = color;
            float[] vertices = this.vertices;
            int n = this.idx;
            for (int i = 2; i < n; i += 5) {
                vertices[i] = color;
            }
        }
    }

    public void draw(SpriteBatch spriteBatch) {
        spriteBatch.draw(this.font.getRegion().getTexture(), this.vertices, 0, this.idx);
    }

    public void draw(SpriteBatch spriteBatch, float alphaModulation) {
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
        int intBits = NumberUtils.floatToRawIntBits(this.color);
        Color color = this.tmpColor;
        color.r = (intBits & 255) / 255.0f;
        color.g = ((intBits >>> 8) & 255) / 255.0f;
        color.b = ((intBits >>> 16) & 255) / 255.0f;
        color.a = ((intBits >>> 24) & 255) / 255.0f;
        return color;
    }

    private void reset(int glyphCount) {
        this.x = 0.0f;
        this.y = 0.0f;
        this.idx = 0;
        int vertexCount = glyphCount * 20;
        if (this.vertices == null || this.vertices.length < vertexCount) {
            this.vertices = new float[vertexCount];
        }
    }

    private float addToCache(CharSequence str, float x, float y, int start, int end) {
        int start2;
        BitmapFont font = this.font;
        BitmapFont.Glyph lastGlyph = null;
        if (font.data.scaleX == 1.0f && font.data.scaleY == 1.0f) {
            while (true) {
                start2 = start;
                if (start2 >= end) {
                    break;
                }
                start = start2 + 1;
                lastGlyph = font.data.getGlyph(str.charAt(start2));
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
                BitmapFont.Glyph g = font.data.getGlyph(ch);
                if (g != null) {
                    float x2 = x + lastGlyph.getKerning(ch);
                    lastGlyph = g;
                    addGlyph(lastGlyph, x2 + g.xoffset, y + g.yoffset, g.width, g.height);
                    x = x2 + g.xadvance;
                }
                start2 = start3;
            }
        } else {
            float scaleX = font.data.scaleX;
            float scaleY = font.data.scaleY;
            while (true) {
                start2 = start;
                if (start2 >= end) {
                    break;
                }
                start = start2 + 1;
                lastGlyph = font.data.getGlyph(str.charAt(start2));
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
                BitmapFont.Glyph g2 = font.data.getGlyph(ch2);
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
        if (!this.integer) {
            int i = this.idx;
            this.idx = i + 1;
            vertices[i] = x;
            int i2 = this.idx;
            this.idx = i2 + 1;
            vertices[i2] = y;
            int i3 = this.idx;
            this.idx = i3 + 1;
            vertices[i3] = this.color;
            int i4 = this.idx;
            this.idx = i4 + 1;
            vertices[i4] = u;
            int i5 = this.idx;
            this.idx = i5 + 1;
            vertices[i5] = v;
            int i6 = this.idx;
            this.idx = i6 + 1;
            vertices[i6] = x;
            int i7 = this.idx;
            this.idx = i7 + 1;
            vertices[i7] = y2;
            int i8 = this.idx;
            this.idx = i8 + 1;
            vertices[i8] = this.color;
            int i9 = this.idx;
            this.idx = i9 + 1;
            vertices[i9] = u;
            int i10 = this.idx;
            this.idx = i10 + 1;
            vertices[i10] = v2;
            int i11 = this.idx;
            this.idx = i11 + 1;
            vertices[i11] = x2;
            int i12 = this.idx;
            this.idx = i12 + 1;
            vertices[i12] = y2;
            int i13 = this.idx;
            this.idx = i13 + 1;
            vertices[i13] = this.color;
            int i14 = this.idx;
            this.idx = i14 + 1;
            vertices[i14] = u2;
            int i15 = this.idx;
            this.idx = i15 + 1;
            vertices[i15] = v2;
            int i16 = this.idx;
            this.idx = i16 + 1;
            vertices[i16] = x2;
            int i17 = this.idx;
            this.idx = i17 + 1;
            vertices[i17] = y;
            int i18 = this.idx;
            this.idx = i18 + 1;
            vertices[i18] = this.color;
            int i19 = this.idx;
            this.idx = i19 + 1;
            vertices[i19] = u2;
            int i20 = this.idx;
            this.idx = i20 + 1;
            vertices[i20] = v;
            return;
        }
        int i21 = this.idx;
        this.idx = i21 + 1;
        vertices[i21] = (int) x;
        int i22 = this.idx;
        this.idx = i22 + 1;
        vertices[i22] = (int) y;
        int i23 = this.idx;
        this.idx = i23 + 1;
        vertices[i23] = this.color;
        int i24 = this.idx;
        this.idx = i24 + 1;
        vertices[i24] = u;
        int i25 = this.idx;
        this.idx = i25 + 1;
        vertices[i25] = v;
        int i26 = this.idx;
        this.idx = i26 + 1;
        vertices[i26] = (int) x;
        int i27 = this.idx;
        this.idx = i27 + 1;
        vertices[i27] = (int) y2;
        int i28 = this.idx;
        this.idx = i28 + 1;
        vertices[i28] = this.color;
        int i29 = this.idx;
        this.idx = i29 + 1;
        vertices[i29] = u;
        int i30 = this.idx;
        this.idx = i30 + 1;
        vertices[i30] = v2;
        int i31 = this.idx;
        this.idx = i31 + 1;
        vertices[i31] = (int) x2;
        int i32 = this.idx;
        this.idx = i32 + 1;
        vertices[i32] = (int) y2;
        int i33 = this.idx;
        this.idx = i33 + 1;
        vertices[i33] = this.color;
        int i34 = this.idx;
        this.idx = i34 + 1;
        vertices[i34] = u2;
        int i35 = this.idx;
        this.idx = i35 + 1;
        vertices[i35] = v2;
        int i36 = this.idx;
        this.idx = i36 + 1;
        vertices[i36] = (int) x2;
        int i37 = this.idx;
        this.idx = i37 + 1;
        vertices[i37] = (int) y;
        int i38 = this.idx;
        this.idx = i38 + 1;
        vertices[i38] = this.color;
        int i39 = this.idx;
        this.idx = i39 + 1;
        vertices[i39] = u2;
        int i40 = this.idx;
        this.idx = i40 + 1;
        vertices[i40] = v;
    }

    public BitmapFont.TextBounds setText(CharSequence str, float x, float y) {
        return setText(str, x, y, 0, str.length());
    }

    public BitmapFont.TextBounds setText(CharSequence str, float x, float y, int start, int end) {
        reset(end - start);
        float y2 = y + this.font.data.ascent;
        this.textBounds.width = addToCache(str, x, y2, start, end);
        this.textBounds.height = this.font.data.capHeight;
        return this.textBounds;
    }

    public BitmapFont.TextBounds setMultiLineText(CharSequence str, float x, float y) {
        return setMultiLineText(str, x, y, 0.0f, BitmapFont.HAlignment.LEFT);
    }

    public BitmapFont.TextBounds setMultiLineText(CharSequence str, float x, float y, float alignmentWidth, BitmapFont.HAlignment alignment) {
        BitmapFont font = this.font;
        int length = str.length();
        reset(length);
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
        return setWrappedText(str, x, y, wrapWidth, BitmapFont.HAlignment.LEFT);
    }

    public BitmapFont.TextBounds setWrappedText(CharSequence str, float x, float y, float wrapWidth, BitmapFont.HAlignment alignment) {
        BitmapFont font = this.font;
        int length = str.length();
        reset(length);
        float y2 = y + font.data.ascent;
        float down = font.data.down;
        float maxWidth = 0.0f;
        int start = 0;
        int numLines = 0;
        while (start < length) {
            int lineEnd = start + font.computeVisibleGlyphs(str, start, BitmapFont.indexOf(str, '\n', start), wrapWidth);
            if (lineEnd < length) {
                while (lineEnd > start) {
                    char ch = str.charAt(lineEnd);
                    if (ch == ' ' || ch == '\n') {
                        break;
                    }
                    lineEnd--;
                }
            }
            if (lineEnd == start) {
                lineEnd++;
            }
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
            start = lineEnd + 1;
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

    @Override // com.badlogic.gdx.utils.Disposable
    public void dispose() {
        this.font.dispose();
    }

    public void setUseIntegerPositions(boolean use) {
        this.integer = use;
    }

    public boolean usesIntegerPositions() {
        return this.integer;
    }
}
