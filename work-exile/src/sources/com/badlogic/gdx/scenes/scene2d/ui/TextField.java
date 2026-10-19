package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.utils.Clipboard;
import com.badlogic.gdx.utils.FloatArray;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class TextField extends Widget {
    float blinkTime;
    final StringBuilder builder;
    Clipboard clipboard;
    int cursor;
    boolean cursorOn;
    final Rectangle fieldBounds;
    final FloatArray glyphAdvances;
    final FloatArray glyphPositions;
    boolean hasSelection;
    final float initialPrefWidth;
    OnscreenKeyboard keyboard;
    long lastBlink;
    TextFieldListener listener;
    float renderOffset;
    final Rectangle scissor;
    int selectionStart;
    float selectionWidth;
    float selectionX;
    final TextFieldStyle style;
    String text;
    final BitmapFont.TextBounds textBounds;
    float textOffset;
    int visibleTextEnd;
    int visibleTextStart;

    public interface OnscreenKeyboard {
        void show(boolean z);
    }

    public interface TextFieldListener {
        void keyTyped(TextField textField, char c);
    }

    public TextField(String name, float prefWidth, TextFieldStyle style) {
        super(name, prefWidth, 0.0f);
        this.fieldBounds = new Rectangle();
        this.textBounds = new BitmapFont.TextBounds();
        this.scissor = new Rectangle();
        this.text = "";
        this.cursor = 0;
        this.renderOffset = 0.0f;
        this.textOffset = 0.0f;
        this.visibleTextStart = 0;
        this.visibleTextEnd = 0;
        this.builder = new StringBuilder();
        this.glyphAdvances = new FloatArray();
        this.glyphPositions = new FloatArray();
        this.blinkTime = 0.5f;
        this.lastBlink = System.nanoTime();
        this.cursorOn = true;
        this.hasSelection = false;
        this.selectionStart = 0;
        this.selectionX = 0.0f;
        this.selectionWidth = 0.0f;
        this.keyboard = new DefaultOnscreenKeyboard();
        this.style = style;
        this.initialPrefWidth = prefWidth;
        this.clipboard = Clipboard.getDefaultClipboard();
        layout();
        this.width = this.prefWidth;
        this.height = this.prefHeight;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Layout
    public void layout() {
        BitmapFont font = this.style.font;
        NinePatch background = this.style.background;
        this.textBounds.set(font.getBounds(this.text));
        this.textBounds.height -= font.getDescent() * 2.0f;
        font.computeGlyphAdvancesAndPositions(this.text, this.glyphAdvances, this.glyphPositions);
        this.prefHeight = background.getBottomHeight() + background.getTopHeight() + this.textBounds.height;
        this.prefWidth = background.getLeftWidth() + background.getRightWidth() + this.initialPrefWidth;
        this.invalidated = false;
    }

    private void blink() {
        long time = System.nanoTime();
        if ((time - this.lastBlink) / 1.0E9f > this.blinkTime) {
            this.cursorOn = !this.cursorOn;
            this.lastBlink = time;
        }
    }

    private void calculateOffsets() {
        NinePatch background = this.style.background;
        float position = this.glyphPositions.get(this.cursor);
        float distance = position - Math.abs(this.renderOffset);
        float visibleWidth = (this.width - background.getLeftWidth()) - background.getRightWidth();
        if (distance <= 0.0f) {
            if (this.cursor > 0) {
                this.renderOffset = -this.glyphPositions.get(this.cursor - 1);
            } else {
                this.renderOffset = 0.0f;
            }
        } else if (distance > visibleWidth) {
            this.renderOffset -= distance - visibleWidth;
        }
        this.visibleTextStart = 0;
        this.textOffset = 0.0f;
        float start = Math.abs(this.renderOffset);
        int len = this.glyphPositions.size;
        float startPos = 0.0f;
        for (int i = 0; i < len; i++) {
            if (this.glyphPositions.items[i] >= start) {
                this.visibleTextStart = i;
                startPos = this.glyphPositions.items[i];
                this.textOffset = this.glyphPositions.items[this.visibleTextStart] - start;
                break;
            }
        }
        this.visibleTextEnd = Math.min(this.text.length(), this.cursor + 1);
        while (this.visibleTextEnd <= this.text.length() && this.glyphPositions.items[this.visibleTextEnd] - startPos <= visibleWidth) {
            this.visibleTextEnd++;
        }
        this.visibleTextEnd = Math.max(0, this.visibleTextEnd - 1);
        if (this.hasSelection) {
            int minIndex = Math.min(this.cursor, this.selectionStart);
            int maxIndex = Math.max(this.cursor, this.selectionStart);
            float minX = Math.max(this.glyphPositions.get(minIndex), this.glyphPositions.get(this.visibleTextStart));
            float maxX = Math.min(this.glyphPositions.get(maxIndex), this.glyphPositions.get(this.visibleTextEnd));
            this.selectionX = minX;
            this.selectionWidth = maxX - minX;
        }
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public void draw(SpriteBatch batch, float parentAlpha) {
        BitmapFont font = this.style.font;
        Color fontColor = this.style.fontColor;
        NinePatch background = this.style.background;
        TextureRegion selection = this.style.selection;
        NinePatch cursorPatch = this.style.cursor;
        if (this.invalidated) {
            layout();
        }
        batch.setColor(this.color.r, this.color.g, this.color.b, this.color.a * parentAlpha);
        background.draw(batch, this.x, this.y, this.width, this.height);
        float textY = ((int) (this.height / 2.0f)) + ((int) (this.textBounds.height / 2.0f)) + (font.getDescent() / 2.0f);
        font.setColor(fontColor.r, fontColor.g, fontColor.b, fontColor.a * parentAlpha);
        calculateOffsets();
        if (this.hasSelection) {
            batch.draw(selection, this.x + this.selectionX + background.getLeftWidth() + this.renderOffset, ((this.y + textY) - this.textBounds.height) - (font.getDescent() / 2.0f), this.selectionWidth, this.textBounds.height);
        }
        font.draw(batch, this.text, this.x + background.getLeftWidth() + this.textOffset, this.y + textY, this.visibleTextStart, this.visibleTextEnd);
        if (this.parent.keyboardFocusedActor == this) {
            blink();
            if (this.cursorOn) {
                cursorPatch.draw(batch, (((this.x + background.getLeftWidth()) + this.glyphPositions.get(this.cursor)) + this.renderOffset) - 1.0f, ((this.y + textY) - this.textBounds.height) - (font.getDescent() / 2.0f), cursorPatch.getTotalWidth(), this.textBounds.height);
            }
        }
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public boolean touchDown(float x, float y, int pointer) {
        if (pointer != 0 || hit(x, y) == null) {
            return false;
        }
        this.parent.keyboardFocus(this);
        this.keyboard.show(true);
        float x2 = x - this.renderOffset;
        for (int i = 0; i < this.glyphPositions.size; i++) {
            float pos = this.glyphPositions.items[i];
            if (pos > x2) {
                this.cursor = Math.max(0, i - 1);
                break;
            }
        }
        return true;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public boolean touchUp(float x, float y, int pointer) {
        return false;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public boolean touchDragged(float x, float y, int pointer) {
        return false;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public boolean keyDown(int keycode) {
        String content;
        BitmapFont font = this.style.font;
        if (this.parent.keyboardFocusedActor != this) {
            return false;
        }
        if (Gdx.input.isKeyPressed(129) || Gdx.input.isKeyPressed(130)) {
            if (keycode == 50 && (content = this.clipboard.getContents()) != null) {
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < content.length(); i++) {
                    char c = content.charAt(i);
                    if (font.containsCharacter(c)) {
                        builder.append(c);
                    }
                }
                String content2 = builder.toString();
                this.text = this.text.substring(0, this.cursor) + content2 + this.text.substring(this.cursor, this.text.length());
                this.cursor += content2.length();
                font.computeGlyphAdvancesAndPositions(this.text, this.glyphAdvances, this.glyphPositions);
            }
            if (keycode != 31 || !this.hasSelection) {
                return true;
            }
            int minIndex = Math.min(this.cursor, this.selectionStart);
            int maxIndex = Math.max(this.cursor, this.selectionStart);
            this.clipboard.setContents(this.text.substring(minIndex, maxIndex));
            return true;
        }
        if (Gdx.input.isKeyPressed(59) || Gdx.input.isKeyPressed(60)) {
            if (keycode == 21) {
                if (!this.hasSelection) {
                    this.selectionStart = this.cursor;
                    this.hasSelection = true;
                }
                this.cursor--;
            }
            if (keycode == 22) {
                if (!this.hasSelection) {
                    this.selectionStart = this.cursor;
                    this.hasSelection = true;
                }
                this.cursor++;
            }
            if (keycode == 3) {
                if (!this.hasSelection) {
                    this.selectionStart = this.cursor;
                    this.hasSelection = true;
                }
                this.cursor = 0;
            }
            if (keycode == 132) {
                if (!this.hasSelection) {
                    this.selectionStart = this.cursor;
                    this.hasSelection = true;
                }
                this.cursor = this.text.length();
            }
            this.cursor = Math.max(0, this.cursor);
            this.cursor = Math.min(this.text.length(), this.cursor);
            return true;
        }
        if (keycode == 21) {
            this.cursor--;
            this.hasSelection = false;
        }
        if (keycode == 22) {
            this.cursor++;
            this.hasSelection = false;
        }
        if (keycode == 3) {
            this.cursor = 0;
            this.hasSelection = false;
        }
        if (keycode == 132) {
            this.cursor = this.text.length();
            this.hasSelection = false;
        }
        this.cursor = Math.max(0, this.cursor);
        this.cursor = Math.min(this.text.length(), this.cursor);
        return true;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public boolean keyTyped(char character) {
        BitmapFont font = this.style.font;
        if (this.parent.keyboardFocusedActor != this) {
            return false;
        }
        if (character == '\b' && (this.cursor > 0 || this.hasSelection)) {
            if (!this.hasSelection) {
                this.text = this.text.substring(0, this.cursor - 1) + this.text.substring(this.cursor);
                this.cursor--;
                font.computeGlyphAdvancesAndPositions(this.text, this.glyphAdvances, this.glyphPositions);
            } else {
                int minIndex = Math.min(this.cursor, this.selectionStart);
                int maxIndex = Math.max(this.cursor, this.selectionStart);
                this.text = (minIndex > 0 ? this.text.substring(0, minIndex) : "") + (maxIndex < this.text.length() ? this.text.substring(maxIndex, this.text.length()) : "");
                this.cursor = minIndex;
                font.computeGlyphAdvancesAndPositions(this.text, this.glyphAdvances, this.glyphPositions);
                this.hasSelection = false;
            }
        }
        if (character == 127 && (this.cursor < this.text.length() || this.hasSelection)) {
            if (!this.hasSelection) {
                this.text = this.text.substring(0, this.cursor) + this.text.substring(this.cursor + 1);
                font.computeGlyphAdvancesAndPositions(this.text, this.glyphAdvances, this.glyphPositions);
            } else {
                int minIndex2 = Math.min(this.cursor, this.selectionStart);
                int maxIndex2 = Math.max(this.cursor, this.selectionStart);
                this.text = (minIndex2 > 0 ? this.text.substring(0, minIndex2) : "") + (maxIndex2 < this.text.length() ? this.text.substring(maxIndex2, this.text.length()) : "");
                this.cursor = minIndex2;
                font.computeGlyphAdvancesAndPositions(this.text, this.glyphAdvances, this.glyphPositions);
                this.hasSelection = false;
            }
        }
        if (font.containsCharacter(character)) {
            if (!this.hasSelection) {
                this.text = this.text.substring(0, this.cursor) + character + this.text.substring(this.cursor, this.text.length());
                this.cursor++;
                font.computeGlyphAdvancesAndPositions(this.text, this.glyphAdvances, this.glyphPositions);
            } else {
                int minIndex3 = Math.min(this.cursor, this.selectionStart);
                int maxIndex3 = Math.max(this.cursor, this.selectionStart);
                this.text = (minIndex3 > 0 ? this.text.substring(0, minIndex3) : "") + (maxIndex3 < this.text.length() ? this.text.substring(maxIndex3, this.text.length()) : "");
                this.cursor = minIndex3;
                this.text = this.text.substring(0, this.cursor) + character + this.text.substring(this.cursor, this.text.length());
                this.cursor++;
                font.computeGlyphAdvancesAndPositions(this.text, this.glyphAdvances, this.glyphPositions);
                this.hasSelection = false;
            }
        }
        if (this.listener != null) {
            this.listener.keyTyped(this, character);
        }
        return true;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.Widget, com.badlogic.gdx.scenes.scene2d.Actor
    public Actor hit(float x, float y) {
        if (x <= 0.0f || x >= this.width || y <= 0.0f || y >= this.height) {
            return null;
        }
        return this;
    }

    public static class TextFieldStyle {
        public final NinePatch background;
        public final NinePatch cursor;
        public final BitmapFont font;
        public final Color fontColor;
        public final TextureRegion selection;

        public TextFieldStyle(BitmapFont font, Color fontColor, NinePatch cursor, TextureRegion selection, NinePatch background) {
            this.background = background;
            this.cursor = cursor;
            this.font = font;
            this.fontColor = fontColor;
            this.selection = selection;
        }
    }

    public void setTextFieldListener(TextFieldListener listener) {
        this.listener = listener;
    }

    public void setText(String text) {
        BitmapFont font = this.style.font;
        if (text == null) {
            throw new IllegalArgumentException("text must not be null");
        }
        this.text = text;
        this.cursor = 0;
        this.hasSelection = false;
        font.computeGlyphAdvancesAndPositions(text, this.glyphAdvances, this.glyphPositions);
        invalidateHierarchy();
    }

    public String getText() {
        return this.text;
    }

    public OnscreenKeyboard getOnscreenKeyboard() {
        return this.keyboard;
    }

    public void setOnscreenKeyboard(OnscreenKeyboard keyboard) {
        this.keyboard = keyboard;
    }

    public static class DefaultOnscreenKeyboard implements OnscreenKeyboard {
        @Override // com.badlogic.gdx.scenes.scene2d.ui.TextField.OnscreenKeyboard
        public void show(boolean visible) {
            Gdx.input.setOnscreenKeyboardVisible(visible);
        }
    }

    public void setClipboard(Clipboard clipboard) {
        this.clipboard = clipboard;
    }
}
