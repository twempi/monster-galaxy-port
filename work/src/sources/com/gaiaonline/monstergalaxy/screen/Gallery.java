package com.gaiaonline.monstergalaxy.screen;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.graphics.util.ScrollInputProcessor;
import com.gaiaonline.monstergalaxy.screen.ScreenGroup;
import java.util.LinkedList;
import java.util.Queue;

/* JADX INFO: loaded from: classes.dex */
public class Gallery<T extends ScreenGroup> extends ScreenGroup {
    static final /* synthetic */ boolean $assertionsDisabled;
    private float actualItemDistance;
    private GalleryAdapter<T> adapter;
    float deltaScroll;
    private float itemDistance;
    private Queue<T> items;
    private int itemsOnScreen;
    private float margin;
    private int maxItems;
    private boolean scrollEnabled = true;
    private ScrollInputProcessor scroller;
    private int startIdx;

    static {
        $assertionsDisabled = !Gallery.class.desiredAssertionStatus();
    }

    public float getDeltaScroll() {
        return this.deltaScroll;
    }

    public InputProcessor getInputProcessor() {
        this.scroller = new ScrollInputProcessor();
        this.scroller.setMaxVelocity(this.itemDistance);
        return this.scroller;
    }

    public void setScrollEnabled(boolean scrollEnabled) {
        this.scrollEnabled = scrollEnabled;
    }

    public Gallery() {
        setStretchable(true);
    }

    public void setAdapter(GalleryAdapter<T> adapter) {
        this.adapter = adapter;
        this.maxItems = adapter.getCount();
    }

    public void setDistance(int distance) {
        this.itemDistance = distance;
    }

    public void setMargin(float margin) {
        this.margin = ResolutionManager.getScaleFactor() * margin;
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenGroup, com.gaiaonline.monstergalaxy.screen.ScreenElement
    protected void update(float f) {
        if (!$assertionsDisabled && this.adapter == null) {
            throw new AssertionError();
        }
        super.update(f);
        this.scroller.update(f);
        if (this.items == null) {
            initialize();
        }
        this.deltaScroll = this.scroller.getDeltaX();
        if (this.deltaScroll != 0.0f && this.scrollEnabled) {
            Vector2 vector2 = this.elements.get(this.items.peek());
            if (vector2.x + (this.itemsOnScreen * this.actualItemDistance) + this.deltaScroll < this.size.x) {
                if (this.startIdx + this.itemsOnScreen <= this.maxItems) {
                    this.startIdx++;
                    T tPoll = this.items.poll();
                    this.adapter.configureItem(tPoll, (this.startIdx + this.itemsOnScreen) - 1);
                    this.items.add(tPoll);
                    this.elements.get(tPoll).add(this.actualItemDistance * this.itemsOnScreen, 0.0f);
                } else {
                    return;
                }
            } else if (vector2.x + this.deltaScroll > this.margin) {
                if (this.startIdx != 0) {
                    this.startIdx--;
                    for (int i = 0; i < this.itemsOnScreen - 1; i++) {
                        this.items.add(this.items.poll());
                    }
                    T tPeek = this.items.peek();
                    this.adapter.configureItem(tPeek, this.startIdx);
                    this.elements.get(tPeek).sub(this.actualItemDistance * this.itemsOnScreen, 0.0f);
                } else {
                    return;
                }
            }
            for (T t : this.items) {
                if (this.deltaScroll != 0.0f) {
                    this.elements.get(t).add(this.deltaScroll, 0.0f);
                }
            }
        }
    }

    private void initialize() {
        this.actualItemDistance = this.itemDistance * ResolutionManager.getScaleFactor();
        this.itemsOnScreen = ((int) (this.size.x / this.actualItemDistance)) + 2;
        this.items = new LinkedList();
        for (int i = 0; i < this.itemsOnScreen; i++) {
            ScreenGroup screenGroupNewItem = this.adapter.newItem();
            this.items.add((T) screenGroupNewItem);
            add(screenGroupNewItem);
            this.elements.get(screenGroupNewItem).set(i * this.actualItemDistance, 0.0f);
            screenGroupNewItem.setAnchorPoint(ScreenElement.RelPoint.LEFT_TOP);
            this.adapter.configureItem(screenGroupNewItem, i);
            screenGroupNewItem.needsRecalc = false;
        }
    }
}
