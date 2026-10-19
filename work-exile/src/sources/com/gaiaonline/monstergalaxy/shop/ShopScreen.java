package com.gaiaonline.monstergalaxy.shop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ColorConstants;
import com.gaiaonline.monstergalaxy.app.Constants;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.MonsterGalaxy;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.app.Screen;
import com.gaiaonline.monstergalaxy.app.ScreenCode;
import com.gaiaonline.monstergalaxy.app.SoundManager;
import com.gaiaonline.monstergalaxy.assets.AssetsCache;
import com.gaiaonline.monstergalaxy.battle.EffectActor;
import com.gaiaonline.monstergalaxy.battle.effects.EffectType;
import com.gaiaonline.monstergalaxy.graphics.util.ScrollInputProcessor;
import com.gaiaonline.monstergalaxy.model.shop.Deal;
import com.gaiaonline.monstergalaxy.model.shop.DealListener;
import com.gaiaonline.monstergalaxy.model.shop.Product;
import com.gaiaonline.monstergalaxy.model.shop.ProductType;
import com.gaiaonline.monstergalaxy.model.shop.Products;
import com.gaiaonline.monstergalaxy.model.shop.Shop;
import com.gaiaonline.monstergalaxy.model.trainer.Trainer;
import com.gaiaonline.monstergalaxy.platform.PlatformHelper;
import com.gaiaonline.monstergalaxy.screen.ActorScreenElement;
import com.gaiaonline.monstergalaxy.screen.ButtonElement;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.ScreenGroup;
import com.gaiaonline.monstergalaxy.screen.ScreenListener;
import com.gaiaonline.monstergalaxy.screen.ShadowTextElement;
import com.gaiaonline.monstergalaxy.screen.TextElement;
import com.gaiaonline.monstergalaxy.screen.TextureElement;
import com.gaiaonline.monstergalaxy.screen.UIEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class ShopScreen extends Screen implements Shop.ShopListener, DealListener {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$gaiaonline$monstergalaxy$model$shop$ProductType = null;
    private static final float FONT_SCALE_OVER_MAX_VALUE = 0.42f;
    private static final float FONT_SCALE_REGULAR = 0.52f;
    private static final Vector2 SCROLL_FRAME_SIZE = new Vector2(400.0f, 210.0f);
    private ShadowTextElement blueCoffeesText;
    private ShadowTextElement coinsText;
    private ShopTable currentProductTable;
    private ShopTable dealsTable;
    private ScreenGroup inventory;
    private ActorScreenElement inventoryEffect;
    private boolean isAmazonStore;
    private ShopTable itemsTable;
    private ShopTable mcashTable;
    private ProductType productTypeJustReceived;
    private ScreenGroup scrollFrameGroup;
    private float scrollYOffset;
    private ScrollInputProcessor scroller = new ScrollInputProcessor();
    private TextureElement solidBg;
    private ShadowTextElement starseedsText;
    private int tabCount;

    static /* synthetic */ int[] $SWITCH_TABLE$com$gaiaonline$monstergalaxy$model$shop$ProductType() {
        int[] iArr = $SWITCH_TABLE$com$gaiaonline$monstergalaxy$model$shop$ProductType;
        if (iArr == null) {
            iArr = new int[ProductType.valuesCustom().length];
            try {
                iArr[ProductType.BLUE_COFFEE.ordinal()] = 2;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[ProductType.MOGA.ordinal()] = 4;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[ProductType.MOGA_CASH.ordinal()] = 1;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[ProductType.STARSEEDS.ordinal()] = 3;
            } catch (NoSuchFieldError e4) {
            }
            $SWITCH_TABLE$com$gaiaonline$monstergalaxy$model$shop$ProductType = iArr;
        }
        return iArr;
    }

    public ShopScreen() {
        this.isAmazonStore = false;
        this.isAmazonStore = PlatformHelper.getSourceStore() == PlatformHelper.Store.AMAZON;
        this.stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        this.solidBg = new TextureElement(newSolidBg(), true);
        this.solidBg.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        this.solidBg.setPosition(ScreenElement.RelPoint.CENTER, 0.0f, 0.0f);
        this.root.add(this.solidBg);
        this.scrollFrameGroup = new ScreenGroup();
        this.scrollFrameGroup.setAnchorPoint(ScreenElement.RelPoint.CENTER_BOTTOM);
        this.scrollFrameGroup.setSize(SCROLL_FRAME_SIZE.x, SCROLL_FRAME_SIZE.y);
        this.scrollFrameGroup.setPosition(ScreenElement.RelPoint.CENTER_BOTTOM, 0.0f, 18.0f);
        this.mcashTable = createTable();
        this.itemsTable = createTable();
        this.scrollFrameGroup.add(this.mcashTable);
        this.scrollFrameGroup.add(this.itemsTable);
        if (!this.isAmazonStore && Game.getPlatformHelper().getSkin() != PlatformHelper.MGSkin.SK2) {
            this.dealsTable = createTable();
            this.scrollFrameGroup.add(this.dealsTable);
        }
        addProducts();
        if (!this.isAmazonStore && Game.getPlatformHelper().getSkin() != PlatformHelper.MGSkin.SK2) {
            addDeals();
        }
        this.root.add(this.scrollFrameGroup);
        TextureElement bg = new TextureElement(Assets.loadTexture("shop.bg"));
        bg.setSize(Gdx.graphics.getWidth() / ResolutionManager.getScaleFactor(), (Gdx.graphics.getHeight() / ResolutionManager.getScaleFactor()) + 3.0f);
        bg.setAnchorPoint(ScreenElement.RelPoint.LEFT_TOP);
        bg.setPosition(ScreenElement.RelPoint.LEFT_TOP, 0.0f, 0.0f);
        this.root.add(bg);
        TabSetController tabset = new TabSetController();
        addTab(tabset, "Moga Cash", "cash", this.mcashTable);
        addTab(tabset, "Items", "items", this.itemsTable);
        addTab(tabset, "Lucky Moga", "slotmachine", null);
        if (!this.isAmazonStore && Game.getPlatformHelper().getSkin() != PlatformHelper.MGSkin.SK2) {
            addTab(tabset, "Deals", "deals", this.dealsTable);
        }
        tabset.setListener(new TabSetController.Listener() { // from class: com.gaiaonline.monstergalaxy.shop.ShopScreen.1
            @Override // com.gaiaonline.monstergalaxy.shop.TabSetController.Listener
            public void tabSelected(TabSetController.Tab tab) {
                ShopScreen.this.tabSelected(tab);
            }
        });
        tabset.setSelected("items");
        TextureElement title = new TextureElement(Assets.loadTexture("shop.title"));
        title.setAnchorPoint(ScreenElement.RelPoint.CENTER_TOP);
        title.setPosition(ScreenElement.RelPoint.CENTER_TOP, 0.0f, -12.0f);
        this.root.add(title);
        ButtonElement closeButton = new ButtonElement(new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.shop.ShopScreen.2
            @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
            public void onUIEvent(UIEvent event) {
                MonsterGalaxy.showPreviousScreen();
            }
        }, null, Assets.closeBtn, Assets.closeBtnPress);
        closeButton.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        closeButton.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, 453.0f, 293.0f);
        addInventoryBar();
        this.root.add(closeButton);
        SoundManager.loadSound(SoundManager.SOUND_MOGA_CASH);
    }

    private TextureRegion newSolidBg() {
        return AssetsCache.newSolidRectangle(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), ColorConstants.PRIMARY_FONT_COLOR);
    }

    private void reloadTextures() {
        if (this.solidBg.getTexture().getTexture() != null) {
            this.solidBg.getTexture().getTexture().dispose();
        }
        this.solidBg.setTexture(newSolidBg());
    }

    private void addTab(TabSetController tabset, String labelText, String tabId, ScreenElement tabContent) {
        this.tabCount++;
        int tabIndex = this.tabCount - 1;
        TextElement label = new TextElement(labelText, 0.55f, ColorConstants.FOURTH_FONT_COLOR, false);
        TextureRegion selected = Assets.loadTexture("tab.on");
        TextureRegion normal = Assets.loadTexture("tab.off");
        float aspectRatiofactor = ResolutionManager.getVerticalAR() / ResolutionManager.getHorizontalAR();
        ButtonElement tabButton = new ButtonElement(null, null, normal, selected, selected);
        tabButton.setAnchorPoint(ScreenElement.RelPoint.LEFT_CENTER);
        tabButton.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, 36.0f + (tabIndex * 102 * aspectRatiofactor), 245.0f);
        label.setAlign(tabButton);
        label.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        label.setPosition(ScreenElement.RelPoint.CENTER, 0.0f, 0.0f);
        this.root.add(tabButton);
        this.root.add(label);
        tabset.addTab(tabId, tabButton, tabContent);
    }

    private ShopTable createTable() {
        ShopTable table = new ShopTable();
        table.setAnchorPoint(ScreenElement.RelPoint.CENTER_TOP);
        table.setPosition(ScreenElement.RelPoint.CENTER_TOP, 0.0f, 0.0f);
        return table;
    }

    private void addInventoryBar() {
        this.inventory = new ScreenGroup();
        this.inventory.setSize(169.0f, 20.0f);
        this.blueCoffeesText = new ShadowTextElement("0", FONT_SCALE_REGULAR, ColorConstants.PRIMARY_FONT_COLOR, ColorConstants.PRIMARY_SHADOW_COLOR, true);
        this.blueCoffeesText.setAnchorPoint(ScreenElement.RelPoint.LEFT_CENTER);
        TextureElement bluecoffe = new TextureElement(Assets.loadTexture("blue.coffee.icon"));
        this.blueCoffeesText.setPosition(ScreenElement.RelPoint.LEFT_CENTER, 29.0f, -3.0f);
        bluecoffe.setPosition(ScreenElement.RelPoint.LEFT_CENTER, 15.0f, -5.0f);
        this.inventory.add(this.blueCoffeesText);
        this.inventory.add(bluecoffe);
        this.starseedsText = new ShadowTextElement("0", FONT_SCALE_REGULAR, ColorConstants.PRIMARY_FONT_COLOR, ColorConstants.PRIMARY_SHADOW_COLOR, true);
        this.starseedsText.setAnchorPoint(ScreenElement.RelPoint.LEFT_CENTER);
        TextureElement starseed = new TextureElement(Assets.loadTexture("capture.starseed.icon"));
        this.starseedsText.setPosition(ScreenElement.RelPoint.CENTER, 0.0f, -3.0f);
        starseed.setPosition(ScreenElement.RelPoint.CENTER, -10.0f, -5.0f);
        this.inventory.add(this.starseedsText);
        this.inventory.add(starseed);
        this.coinsText = new ShadowTextElement("0", FONT_SCALE_REGULAR, ColorConstants.PRIMARY_FONT_COLOR, ColorConstants.PRIMARY_SHADOW_COLOR, true);
        this.coinsText.setAnchorPoint(ScreenElement.RelPoint.LEFT_CENTER);
        TextureElement mogacash = new TextureElement(Assets.loadTexture("capture.mcash"));
        this.coinsText.setPosition(ScreenElement.RelPoint.RIGHT_CENTER, -32.0f, -3.0f);
        mogacash.setPosition(ScreenElement.RelPoint.RIGHT_CENTER, -45.0f, -5.0f);
        this.inventory.add(this.coinsText);
        this.inventory.add(mogacash);
        this.inventory.setAnchorPoint(ScreenElement.RelPoint.LEFT_CENTER);
        this.inventory.setPosition(ScreenElement.RelPoint.LEFT_TOP, 7.0f, -14.0f);
        this.root.add(this.inventory);
    }

    private void addProducts() {
        List<Product> products = new Products().getProducts();
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        Color rowColor = ColorConstants.getColor("shop_color_01");
        TableRow.Listener<Product> productListener = new TableRow.Listener<Product>() { // from class: com.gaiaonline.monstergalaxy.shop.ShopScreen.3
            @Override // com.gaiaonline.monstergalaxy.shop.TableRow.Listener
            public void itemSelected(Product product) {
                ShopScreen.this.buyProduct(product);
            }
        };
        Collections.sort(products, new PriceComparator());
        for (Product product : products) {
            ProductType type = product.getType();
            if (type != ProductType.MOGA) {
                if (product.getType() == ProductType.MOGA_CASH) {
                    Color color = arrayList.size() % 2 == 0 ? rowColor : null;
                    arrayList.add(new ProductTableRow(product, productListener, color));
                } else {
                    Color color2 = arrayList2.size() % 2 == 0 ? rowColor : null;
                    arrayList2.add(new ProductTableRow(product, productListener, color2));
                }
            }
        }
        this.mcashTable.addItems(arrayList);
        this.itemsTable.addItems(arrayList2);
    }

    class PriceComparator implements Comparator<Product> {
        PriceComparator() {
        }

        @Override // java.util.Comparator
        public int compare(Product o1, Product o2) {
            return o1.getPrice() < o2.getPrice() ? -1 : 1;
        }
    }

    private void addDeals() {
        ArrayList arrayList = new ArrayList();
        TableRow.Listener<Deal> dealListener = new TableRow.Listener<Deal>() { // from class: com.gaiaonline.monstergalaxy.shop.ShopScreen.4
            @Override // com.gaiaonline.monstergalaxy.shop.TableRow.Listener
            public void itemSelected(Deal deal) {
                ShopScreen.this.checkDeal(deal);
            }
        };
        arrayList.add(new DealTableRow(new Deal(Deal.DealType.STARSEEDS, "Free Starseeds", "Earn Starseeds by watching App trailers.", "See Deal", "capture.starseed.icon"), dealListener, 0));
        this.dealsTable.addItems(arrayList);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void buyProduct(Product product) {
        if (Game.getPlatformHelper().isAndroid()) {
            Game.getAndroidPlatformHelper().buyProduct(product.getId());
        } else {
            Shop.addBoughtProduct(product);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkDeal(Deal deal) {
        if (Game.getPlatformHelper().isAndroid()) {
            if (deal.getType() == Deal.DealType.STARSEEDS) {
                Game.getAndroidPlatformHelper().showTapJoyOffers();
            } else if (deal.getType() == Deal.DealType.MOGA_CASH) {
                Game.getAndroidPlatformHelper().showAdColonyVideo();
            } else {
                Game.getAndroidPlatformHelper().showW3iOfferWall();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void tabSelected(TabSetController.Tab tab) {
        if ("slotmachine".equals(tab.id)) {
            MonsterGalaxy.showScreen(ScreenCode.SLOTMACHINE, false, true);
        }
        this.currentProductTable = (ShopTable) tab.content;
        this.scrollYOffset = 0.0f;
    }

    private void updateField(TextElement field, int value, int maxDisplayValue) {
        if (value <= maxDisplayValue) {
            if (maxDisplayValue == 9999 && value > 999) {
                field.setText(String.valueOf(value));
                field.setFontScale(FONT_SCALE_OVER_MAX_VALUE);
                return;
            } else {
                field.setText(String.valueOf(value));
                field.setFontScale(FONT_SCALE_REGULAR);
                return;
            }
        }
        field.setText("+" + maxDisplayValue);
        field.setFontScale(FONT_SCALE_OVER_MAX_VALUE);
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void update(float deltaTime) {
        float scrollDeltaY = this.scroller.getDeltaY();
        if (scrollDeltaY != 0.0f) {
            setScrollOffset(this.scrollYOffset + scrollDeltaY);
        }
        super.update(deltaTime);
        Trainer trainer = Game.getTrainer();
        updateField(this.blueCoffeesText, trainer.getBlueCoffeeCups(), 999);
        updateField(this.starseedsText, trainer.getStarSeeds(), 999);
        updateField(this.coinsText, trainer.getCoins(), Constants.MAX_DISPLAY_VALUE_MOGA_CASH);
    }

    private void setScrollOffset(float offset) {
        float offset2;
        if (this.currentProductTable.getSize().y < SCROLL_FRAME_SIZE.y) {
            offset2 = 0.0f;
        } else {
            offset2 = Math.max(Math.min(offset, 0.0f), (-this.currentProductTable.getSize().y) + SCROLL_FRAME_SIZE.y + 4.0f);
        }
        this.scrollYOffset = offset2;
        this.currentProductTable.setPositionY(-this.scrollYOffset);
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void present(float deltaTime) {
        if (this.productTypeJustReceived != null) {
            showInventoryEffect(this.productTypeJustReceived);
            this.productTypeJustReceived = null;
        }
        this.stage.act(deltaTime);
        this.root.present(this.batcher);
        if (this.stage != null) {
            this.stage.draw();
        }
    }

    private void showInventoryEffect(ProductType productType) {
        if (this.inventoryEffect == null) {
            EffectActor actor = new EffectActor("inventory");
            actor.setEffectWithType(EffectType.INVENTORY);
            this.inventoryEffect = new ActorScreenElement();
            this.inventoryEffect.setActor(actor);
            this.inventoryEffect.setAnchorPoint(ScreenElement.RelPoint.LEFT_BOTTOM);
            this.inventoryEffect.setPosition(ScreenElement.RelPoint.LEFT_CENTER, 0.0f, 0.0f);
            this.inventoryEffect.setVisible(false);
            this.inventory.add(this.inventoryEffect);
        }
        ScreenElement alignElement = null;
        switch ($SWITCH_TABLE$com$gaiaonline$monstergalaxy$model$shop$ProductType()[productType.ordinal()]) {
            case 1:
                alignElement = this.coinsText;
                break;
            case 2:
                alignElement = this.blueCoffeesText;
                break;
            case 3:
                alignElement = this.starseedsText;
                break;
        }
        if (alignElement != null) {
            this.inventoryEffect.setAlign(alignElement);
            this.inventoryEffect.setVisible(true);
            ((EffectActor) this.inventoryEffect.getActor()).getEffect().start();
        }
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void onEnter(Screen prevScreen) {
        pushMusic();
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void onExit(Screen nextScreen) {
        popMusic();
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void onResume() {
        reloadTextures();
        Shop.setListener(this);
        if (Game.getPlatformHelper().isAndroid()) {
            Game.getAndroidPlatformHelper().setDealListener(this);
            Game.getAndroidPlatformHelper().getDealRewards();
        }
        Gdx.input.setInputProcessor(this.scroller);
    }

    private void pushMusic() {
        if (Game.getPlatformHelper().getSkin() == PlatformHelper.MGSkin.SK2) {
            SoundManager.pushMusic(SoundManager.MUSIC_SHOP_SCREEN);
        }
    }

    private void popMusic() {
        if (Game.getPlatformHelper().getSkin() == PlatformHelper.MGSkin.SK2) {
            SoundManager.popMusic();
        }
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void hideDialog() {
        super.hideDialog();
        Gdx.input.setInputProcessor(this.scroller);
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void dispose() {
        Shop.setListener(null);
        if (Game.getPlatformHelper().isAndroid()) {
            Game.getAndroidPlatformHelper().setDealListener(null);
        }
        super.dispose();
    }

    @Override // com.gaiaonline.monstergalaxy.model.shop.Shop.ShopListener
    public void onProductReceived(ProductType productType) {
        this.productTypeJustReceived = productType;
    }

    @Override // com.gaiaonline.monstergalaxy.model.shop.DealListener
    public void onRewardReceived(Deal.DealType dealType) {
        if (dealType == Deal.DealType.MOGA_CASH) {
            this.productTypeJustReceived = ProductType.MOGA_CASH;
        } else if (dealType == Deal.DealType.STARSEEDS) {
            this.productTypeJustReceived = ProductType.STARSEEDS;
        }
    }
}
