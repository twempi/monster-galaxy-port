package com.gaiaonline.monstergalaxy.shop;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ColorConstants;
import com.gaiaonline.monstergalaxy.model.shop.Product;
import com.gaiaonline.monstergalaxy.screen.ButtonElement;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.TextElement;
import com.gaiaonline.monstergalaxy.screen.TextureElement;
import com.gaiaonline.monstergalaxy.screen.TouchReleasedListener;
import java.text.DecimalFormat;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class ProductTableRow extends TableRow<Product> {
    private static final DecimalFormat PRICE_FORMAT = new DecimalFormat("$###.##");

    public ProductTableRow(Product product, TableRow.Listener<Product> listener, Color color) {
        this(product, color);
        setListener(listener);
    }

    private ProductTableRow(Product product, Color color) {
        super(product);
        setSize(CELL_SIZE.x, CELL_SIZE.y);
        if (color != null) {
            addBackground(color);
        }
        ScreenElement icon = new TextureElement(product.getType().getIconAsset());
        icon.setScale(1.3f);
        icon.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        icon.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, 20.0f, 30.0f);
        add(icon);
        ScreenElement titleElement = new TextElement(product.getTitle(), 0.65f, ColorConstants.FOURTH_FONT_COLOR, true);
        titleElement.setAnchorPoint(ScreenElement.RelPoint.LEFT_TOP);
        titleElement.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, 50.0f, 53.0f);
        add(titleElement);
        ScreenElement descriptionElement = new TextElement(product.getDescription(), 0.4f, ColorConstants.FOURTH_FONT_COLOR, true, 190.0f);
        descriptionElement.setAnchorPoint(ScreenElement.RelPoint.LEFT_TOP);
        descriptionElement.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, 50.0f, 32.0f);
        add(descriptionElement);
        ButtonElement buyButton = new ButtonElement(null, null, Assets.loadTexture("green.btn"), Assets.loadTexture("green.btn.press"));
        buyButton.setScale(0.8f);
        buyButton.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        buyButton.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, 330.0f, 30.0f);
        buyButton.setTouchReleasedListener(new TouchReleasedListener() { // from class: com.gaiaonline.monstergalaxy.shop.ProductTableRow.1
            /* JADX WARN: Type inference incomplete: some casts might be missing */
            @Override // com.gaiaonline.monstergalaxy.screen.TouchReleasedListener
            public void touchReleased(Vector2 vector2, Vector2 vector3) {
                if (ProductTableRow.this.listener != 0) {
                    ProductTableRow.this.listener.itemSelected((T) ((Product) ProductTableRow.this.getData()));
                }
            }
        });
        add(buyButton);
        ScreenElement buyLabel = new TextElement("Buy", 0.65f, ColorConstants.PRIMARY_FONT_COLOR, false);
        buyLabel.setAnchorPoint(ScreenElement.RelPoint.LEFT_TOP);
        buyLabel.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, 287.0f, 42.0f);
        add(buyLabel);
        ScreenElement priceLabel = new TextElement(PRICE_FORMAT.format(product.getPrice()), 0.65f, ColorConstants.SECONARY_FONT_COLOR, false);
        priceLabel.setAnchorPoint(ScreenElement.RelPoint.RIGHT_TOP);
        priceLabel.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, 373.0f, 42.0f);
        add(priceLabel);
    }
}
