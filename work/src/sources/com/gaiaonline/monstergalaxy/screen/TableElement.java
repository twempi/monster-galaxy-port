package com.gaiaonline.monstergalaxy.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public abstract class TableElement<T> extends ScreenGroup {
    private float actualRowHeight;
    private float rowHeight;
    private float rowWidth;
    private List<TableElement<T>.TableRow> rows;
    private int rowsToShow;
    private float scrollYOffset;
    private boolean scrollable;

    public abstract void makeEmptyListRow(TableElement<T>.TableRow tableRow);

    public abstract void makeRow(TableElement<T>.TableRow tableRow, T t, int i);

    public TableElement(float width, float height, float rowHeight, Color bgColor, List<T> items) {
        this.rows = new ArrayList(items.size());
        this.rowWidth = width;
        this.rowHeight = rowHeight;
        this.rowsToShow = (int) Math.min(items.size(), (height / rowHeight) + 1.0f);
        setSize(width, height);
        this.scrollable = ((float) items.size()) > height / rowHeight;
        if (bgColor != null) {
            addBackground(bgColor);
        }
        addItems(items);
        this.actualRowHeight = this.rows.get(0).size.y;
        if (this.scrollable) {
            this.scrollYOffset = this.size.y - (this.rows.size() * this.actualRowHeight);
        }
    }

    private void addItems(List<T> items) {
        int j = 0;
        for (T aItem : items) {
            this.rows.add(new TableRow(aItem, j));
            j++;
        }
        if (items.size() == 0) {
            this.rows.add(new TableRow());
        }
    }

    public void setScrollOffset(float scrollDeltaY) {
        if (this.scrollable) {
            float offset = this.scrollYOffset - scrollDeltaY;
            this.scrollYOffset = Math.max(Math.min(offset, 0.0f), this.size.y - (this.rows.size() * this.actualRowHeight));
        }
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenGroup, com.gaiaonline.monstergalaxy.screen.ScreenElement
    protected void present(Vector2 origin, SpriteBatch batcher) {
        int firstRow = (int) ((-this.scrollYOffset) / this.actualRowHeight);
        float firstRowOffset = this.scrollYOffset + (firstRow * this.actualRowHeight);
        int i = this.rows.size() - 1;
        for (TableElement<T>.TableRow row : this.rows) {
            if (i < firstRow || i > this.rowsToShow + firstRow) {
                row.setVisible(false);
            } else {
                row.setVisible(true);
                this.elements.get(row).x = 0.0f;
                this.elements.get(row).y = (this.size.y - (((this.rowsToShow + firstRow) - i) * row.size.y)) + firstRowOffset;
                row.needsRecalc = false;
            }
            i--;
        }
        super.present(origin, batcher);
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenGroup, com.gaiaonline.monstergalaxy.screen.ScreenElement
    protected void dispose() {
        this.rows.clear();
        this.rows = null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public class TableRow extends ScreenGroup {
        private T rowItem;

        public TableRow() {
            this.rowItem = null;
            setupRow();
            TableElement.this.makeEmptyListRow(this);
        }

        public TableRow(T item, int index) {
            this.rowItem = item;
            setupRow();
            TableElement.this.makeRow(this, item, index);
        }

        private void setupRow() {
            setSize(TableElement.this.rowWidth, TableElement.this.rowHeight);
            setAnchorPoint(ScreenElement.RelPoint.CENTER_TOP);
            setPosition(ScreenElement.RelPoint.CENTER_TOP, 0.0f, 0.0f);
            TableElement.this.add(this);
        }

        public T getItem() {
            return this.rowItem;
        }

        @Override // com.gaiaonline.monstergalaxy.screen.ScreenGroup, com.gaiaonline.monstergalaxy.screen.ScreenElement
        public void dispose() {
            this.rowItem = null;
        }
    }
}
