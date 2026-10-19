package com.badlogic.gdx.scenes.scene2d.actors;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Layout;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Label extends Actor implements Layout {
    public final BitmapFont.TextBounds bounds;
    public BitmapFontCache cache;
    private BitmapFont.HAlignment halign;
    private float lastWidth;
    public String text;
    public VAlignment valign;
    private WrapType wrapType;

    public enum VAlignment {
        TOP,
        CENTER,
        BOTTOM
    }

    private enum WrapType {
        singleLine,
        multiLine,
        wrapped
    }

    public Label(String name, BitmapFont font) {
        super(name);
        this.valign = VAlignment.BOTTOM;
        this.bounds = new BitmapFont.TextBounds();
        this.lastWidth = -1.0f;
        this.cache = new BitmapFontCache(font);
    }

    public Label(String name, BitmapFont font, String text) {
        this(name, font);
        setText(text);
    }

    public void setText(String text) {
        this.text = text;
        this.wrapType = WrapType.singleLine;
        this.bounds.set(this.cache.setText(text, 0.0f, this.cache.getFont().isFlipped() ? 0.0f : this.cache.getFont().getCapHeight()));
        this.width = this.bounds.width;
        this.height = this.bounds.height;
    }

    public void setMultiLineText(String text) {
        this.text = text;
        this.wrapType = WrapType.multiLine;
        this.bounds.set(this.cache.getFont().getMultiLineBounds(text));
        this.cache.setMultiLineText(text, 0.0f, this.cache.getFont().isFlipped() ? 0.0f : this.bounds.height);
        this.width = this.bounds.width;
        this.height = this.bounds.height;
    }

    public void setWrappedText(String text, BitmapFont.HAlignment halign) {
        this.text = text;
        this.halign = halign;
        this.wrapType = WrapType.wrapped;
        this.bounds.set(this.cache.getFont().getWrappedBounds(text, this.width));
        this.cache.setWrappedText(text, 0.0f, this.cache.getFont().isFlipped() ? 0.0f : this.bounds.height, this.width, halign);
    }

    public void setFont(BitmapFont font) {
        this.cache = new BitmapFontCache(font);
        switch (this.wrapType) {
            case singleLine:
                setText(this.text);
                break;
            case multiLine:
                setMultiLineText(this.text);
                break;
            case wrapped:
                setWrappedText(this.text, this.halign);
                break;
        }
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public void draw(SpriteBatch batch, float parentAlpha) {
        this.cache.setColor(this.color.r, this.color.g, this.color.b, this.color.a * parentAlpha);
        switch (this.valign) {
            case TOP:
                if (this.cache.getFont().isFlipped()) {
                    this.cache.setPosition(this.x, this.y);
                } else {
                    this.cache.setPosition(this.x, (this.y + this.height) - this.bounds.height);
                }
                break;
            case CENTER:
                this.cache.setPosition(this.x, this.y + ((this.height - this.bounds.height) / 2.0f));
                break;
            case BOTTOM:
                if (this.cache.getFont().isFlipped()) {
                    this.cache.setPosition(this.x, (this.y + this.height) - this.bounds.height);
                } else {
                    this.cache.setPosition(this.x, this.y);
                }
                break;
        }
        this.cache.draw(batch);
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public boolean touchDown(float x, float y, int pointer) {
        return this.touchable && x > 0.0f && y > 0.0f && x < this.width && y < this.height;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public boolean touchUp(float x, float y, int pointer) {
        return this.touchable && x > 0.0f && y > 0.0f && x < this.width && y < this.height;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public boolean touchDragged(float x, float y, int pointer) {
        return this.touchable && x > 0.0f && y > 0.0f && x < this.width && y < this.height;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Actor
    public Actor hit(float x, float y) {
        if (x <= 0.0f || y <= 0.0f || x >= this.width || y >= this.height) {
            return null;
        }
        return this;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Layout
    public void layout() {
        if (this.wrapType == WrapType.wrapped && this.lastWidth != this.width) {
            setWrappedText(this.text, this.halign);
        }
        this.lastWidth = this.width;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Layout
    public void invalidate() {
        this.lastWidth = -1.0f;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Layout
    public float getPrefWidth() {
        switch (this.wrapType) {
            case singleLine:
                return this.cache.getFont().getBounds(this.text).width * this.scaleX;
            case multiLine:
                return this.cache.getFont().getMultiLineBounds(this.text).width * this.scaleX;
            default:
                return 0.0f;
        }
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Layout
    public float getPrefHeight() {
        switch (this.wrapType) {
            case singleLine:
                return this.cache.getFont().getBounds(this.text).height * this.scaleY;
            case multiLine:
                return this.cache.getFont().getMultiLineBounds(this.text).height * this.scaleY;
            default:
                return 0.0f;
        }
    }
}
