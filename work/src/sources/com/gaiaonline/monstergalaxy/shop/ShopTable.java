package com.gaiaonline.monstergalaxy.shop;

import com.badlogic.gdx.math.Vector2;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.ScreenGroup;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class ShopTable extends ScreenGroup {
    private static final float BOTTOM_TOP_MARGIN = 5.0f;
    private Vector2 size = new Vector2();

    public <T> void addItems(List<TableRow<T>> items) {
        int i = items.size();
        for (ScreenGroup row : items) {
            i--;
            row.setAnchorPoint(ScreenElement.RelPoint.LEFT_BOTTOM);
            row.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, 0.0f, (i * DealTableRow.CELL_SIZE.y) + BOTTOM_TOP_MARGIN);
            add(row);
        }
        this.size.x = DealTableRow.CELL_SIZE.x;
        this.size.y = (DealTableRow.CELL_SIZE.y * items.size()) + 10.0f;
        setSize(this.size.x, this.size.y);
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenElement
    public Vector2 getSize() {
        return this.size;
    }
}
