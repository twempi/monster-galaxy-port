package com.gaiaonline.monstergalaxy.shop;

import com.badlogic.gdx.math.Vector2;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ColorConstants;
import com.gaiaonline.monstergalaxy.model.shop.Deal;
import com.gaiaonline.monstergalaxy.screen.ButtonElement;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.TextElement;
import com.gaiaonline.monstergalaxy.screen.TextureElement;
import com.gaiaonline.monstergalaxy.screen.TouchReleasedListener;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class DealTableRow extends TableRow<Deal> {
    public DealTableRow(Deal deal, TableRow.Listener<Deal> listener, int rowIndex) {
        this(deal, rowIndex);
        setListener(listener);
    }

    public DealTableRow(Deal deal, int rowIndex) {
        super(deal);
        setSize(CELL_SIZE.x, CELL_SIZE.y);
        if (rowIndex % 2 == 0) {
            addBackground(ColorConstants.getColor("shop_color_01"));
        }
        if (deal.getIconAssetName() != null) {
            ScreenElement icon = new TextureElement(Assets.loadTexture(deal.getIconAssetName()));
            icon.setScale(1.3f);
            icon.setAnchorPoint(ScreenElement.RelPoint.CENTER);
            icon.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, 20.0f, 30.0f);
            add(icon);
        }
        ScreenElement titleElement = new TextElement(deal.getTitle(), 0.65f, ColorConstants.FOURTH_FONT_COLOR, true);
        titleElement.setAnchorPoint(ScreenElement.RelPoint.LEFT_TOP);
        titleElement.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, 50.0f, 53.0f);
        add(titleElement);
        ScreenElement descriptionElement = new TextElement(deal.getDescription(), 0.4f, ColorConstants.FOURTH_FONT_COLOR, true, 190.0f);
        descriptionElement.setAnchorPoint(ScreenElement.RelPoint.LEFT_TOP);
        descriptionElement.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, 50.0f, 32.0f);
        add(descriptionElement);
        ButtonElement dealButton = new ButtonElement(null, null, Assets.loadTexture("green.btn"), Assets.loadTexture("green.btn.press"));
        dealButton.setScale(0.8f);
        dealButton.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        dealButton.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, 330.0f, 30.0f);
        dealButton.setTouchReleasedListener(new TouchReleasedListener() { // from class: com.gaiaonline.monstergalaxy.shop.DealTableRow.1
            /* JADX WARN: Type inference incomplete: some casts might be missing */
            @Override // com.gaiaonline.monstergalaxy.screen.TouchReleasedListener
            public void touchReleased(Vector2 vector2, Vector2 vector3) {
                if (DealTableRow.this.listener != 0) {
                    DealTableRow.this.listener.itemSelected((T) ((Deal) DealTableRow.this.getData()));
                }
            }
        });
        add(dealButton);
        ScreenElement dealLabel = new TextElement(deal.getButtonTitle(), 0.65f, ColorConstants.PRIMARY_FONT_COLOR, false);
        dealLabel.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        dealLabel.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, 330.0f, 35.0f);
        add(dealLabel);
    }
}
