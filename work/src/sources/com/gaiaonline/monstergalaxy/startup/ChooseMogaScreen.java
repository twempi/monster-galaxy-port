package com.gaiaonline.monstergalaxy.startup;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ColorConstants;
import com.gaiaonline.monstergalaxy.app.Constants;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.assets.AssetsCache;
import com.gaiaonline.monstergalaxy.i18n.I18nManager;
import com.gaiaonline.monstergalaxy.model.moga.MogaType;
import com.gaiaonline.monstergalaxy.model.shop.Product;
import com.gaiaonline.monstergalaxy.model.shop.Shop;
import com.gaiaonline.monstergalaxy.platform.PlatformHelper;
import com.gaiaonline.monstergalaxy.screen.ActorScreenElement;
import com.gaiaonline.monstergalaxy.screen.ButtonElement;
import com.gaiaonline.monstergalaxy.screen.Dialog;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.ScreenListener;
import com.gaiaonline.monstergalaxy.screen.TextElement;
import com.gaiaonline.monstergalaxy.screen.TextureElement;
import com.gaiaonline.monstergalaxy.screen.UIEvent;

/* JADX INFO: loaded from: classes.dex */
public class ChooseMogaScreen extends AbstractStartupScreen {
    private TextureElement moga1;
    private TextureElement moga2;
    private TextureElement moga3;
    private int mogaTypeId;

    public ChooseMogaScreen() {
        this.root.addBackground(Assets.loadTexture("startup.choosezodiacmoga.bg"));
        addTitle(I18nManager.getText(I18nManager.I18nKey.CHOOSE_YOUR_MOGA));
        this.moga1 = addMoga(Constants.newPlayerMoga1, new Vector2(-158.0f, 10.0f), "startup.good");
        this.moga2 = addMoga(Constants.newPlayerMoga2, new Vector2(0.0f, 10.0f), "startup.better");
        this.moga3 = addMoga(Constants.newPlayerMoga3, new Vector2(158.0f, 10.0f), "startup.best");
    }

    private TextureElement addMoga(int mogaTypeId, Vector2 position, String ratingLabelAsset) {
        final MogaType mogaType = Game.getStorage().getMogaType(mogaTypeId);
        Product product = Shop.getMogaProduct(mogaTypeId);
        TextureRegion normal = Assets.loadTexture("startup.btn.moga");
        TextureRegion selected = Assets.loadTexture("startup.btn.moga.press");
        ButtonElement button = new ButtonElement(new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.startup.ChooseMogaScreen.1
            @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
            public void onUIEvent(UIEvent event) {
                ChooseMogaScreen.this.chooseMoga(mogaType);
            }
        }, null, normal, selected);
        button.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        button.setPosition(ScreenElement.RelPoint.CENTER, position.x, position.y);
        this.root.add(button);
        TextElement nameLabel = new TextElement(mogaType.getLocalizedName(), 0.55f, ColorConstants.PRIMARY_FONT_COLOR, false);
        nameLabel.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        nameLabel.setPosition(ScreenElement.RelPoint.CENTER, position.x, position.y - 25.0f);
        this.root.add(nameLabel);
        if (product.getPrice() > 0.0f) {
            TextElement prizeLabel = new TextElement("$" + product.getPrice(), 0.55f, ColorConstants.PRIMARY_FONT_COLOR, false);
            prizeLabel.setAnchorPoint(ScreenElement.RelPoint.CENTER);
            prizeLabel.setPosition(ScreenElement.RelPoint.CENTER, position.x + 30.0f, position.y + 95.0f);
            this.root.add(prizeLabel);
        }
        TextElement descriptionLabel = new TextElement(product.getTitle(), 0.45f, new Color(0.44f, 0.8f, 0.93f, 1.0f), true, 120.0f);
        descriptionLabel.setTextAlignment(BitmapFont.HAlignment.CENTER);
        descriptionLabel.setAnchorPoint(ScreenElement.RelPoint.CENTER_TOP);
        descriptionLabel.setPosition(ScreenElement.RelPoint.CENTER, position.x, position.y - 40.0f);
        this.root.add(descriptionLabel);
        TextureElement rating = new TextureElement(Assets.loadLocalizedTexture(ratingLabelAsset));
        rating.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        rating.setPosition(ScreenElement.RelPoint.CENTER, position.x, position.y - 90.0f);
        this.root.add(rating);
        TextureElement mogaElement = new TextureElement(AssetsCache.get(this.root, mogaType.getAsset()));
        mogaElement.setScale((105.0f * ResolutionManager.getScaleFactor()) / mogaElement.getSize().y);
        mogaElement.setAnchorPoint(ScreenElement.RelPoint.CENTER_BOTTOM);
        mogaElement.setPosition(ScreenElement.RelPoint.CENTER, position.x, position.y - 15.0f);
        this.root.add(mogaElement);
        return mogaElement;
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void onResume() {
        super.onResume();
        int mogaTypeId = Constants.newPlayerMoga1;
        MogaType mogaType = Game.getStorage().getMogaType(mogaTypeId);
        this.moga1.setTexture(AssetsCache.get(this.root, mogaType.getAsset()));
        this.moga1.setScale((ResolutionManager.getScaleFactor() * 105.0f) / this.moga1.getSize().y);
        int mogaTypeId2 = Constants.newPlayerMoga2;
        MogaType mogaType2 = Game.getStorage().getMogaType(mogaTypeId2);
        this.moga2.setTexture(AssetsCache.get(this.root, mogaType2.getAsset()));
        this.moga2.setScale((ResolutionManager.getScaleFactor() * 105.0f) / this.moga2.getSize().y);
        int mogaTypeId3 = Constants.newPlayerMoga3;
        MogaType mogaType3 = Game.getStorage().getMogaType(mogaTypeId3);
        this.moga3.setTexture(AssetsCache.get(this.root, mogaType3.getAsset()));
        this.moga3.setScale((ResolutionManager.getScaleFactor() * 105.0f) / this.moga3.getSize().y);
        if (Game.getPlatformHelper().isPurchaseTransactionPending()) {
            showDialog(new PurchaseAlertDialog("Waiting your purchase to be confirmed ..."));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void chooseMoga(MogaType mogaType) {
        this.mogaTypeId = mogaType.getId();
        String productId = String.valueOf(this.mogaTypeId) + ".moga.moga";
        if (Game.getPlatformHelper().getSkin() == PlatformHelper.MGSkin.SK2) {
            productId = String.valueOf(productId) + ".mge";
        }
        Game.getAnalytics().uploadRegistrationEvent("Player did choose her/his Moga");
        if (this.mogaTypeId != Constants.newPlayerMoga1 && Game.getPlatformHelper().isAndroid()) {
            Game.getAndroidPlatformHelper().buyProduct(productId);
        } else {
            Shop.addBoughtProduct(productId);
        }
        Game.getAnalytics().trackEvent("acquisition", "", "", "complete_choose_moga", "", "");
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
    public void onUIEvent(UIEvent event) {
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (this.dialog != null) {
            this.root.update(deltaTime);
            this.dialog.update(deltaTime);
        }
    }

    class PurchaseAlertDialog extends Dialog {
        private static final float MAX_MESSAGE_WIDTH = 335.0f;
        private TextElement messageTextElement;

        public PurchaseAlertDialog(String message) {
            super(null);
            add(Assets.loadTexture("node.info.bg"), 0.0f, 0.0f);
            this.messageTextElement = addLabel("", 0.0f, 0.0f, 0.55f, ColorConstants.PRIMARY_FONT_COLOR, true);
            this.messageTextElement.setPosition(ScreenElement.RelPoint.CENTER, 0.0f, 50.0f);
            this.messageTextElement.setText(message);
            this.messageTextElement.setTextAlignment(BitmapFont.HAlignment.CENTER);
            this.messageTextElement.setWrapWidth(MAX_MESSAGE_WIDTH);
            ActorScreenElement moga = new ActorScreenElement();
            moga.setActor(getMogaActor(ChooseMogaScreen.this.mogaTypeId));
            moga.setAnchorPoint(ScreenElement.RelPoint.CENTER);
            moga.setPosition(ScreenElement.RelPoint.CENTER, 0.0f, -10.0f);
            add(moga);
        }

        private Image getMogaActor(int mogaTypeId) {
            MogaType mogaType = Game.getStorage().getMogaType(mogaTypeId);
            Image image = new Image(AssetsCache.get(ChooseMogaScreen.this.root, mogaType.getAsset()));
            ScaleToAction scaleUp = new ScaleToAction();
            scaleUp.setScale(1.2f);
            scaleUp.setDuration(0.7f);
            ScaleToAction scaleDown = new ScaleToAction();
            scaleUp.setScale(1.0f);
            scaleUp.setDuration(0.7f);
            SequenceAction sequence = new SequenceAction();
            sequence.addAction(scaleUp);
            sequence.addAction(scaleDown);
            RepeatAction forever = new RepeatAction();
            forever.setCount(-1);
            forever.setAction(sequence);
            image.addAction(forever);
            ResolutionManager.scaleActor(image);
            image.setWidth(image.getWidth() * 0.7f);
            image.setHeight(image.getHeight() * 0.7f);
            return image;
        }

        @Override // com.gaiaonline.monstergalaxy.screen.ScreenGroup, com.gaiaonline.monstergalaxy.screen.ScreenElement
        public void dispose() {
        }
    }
}
