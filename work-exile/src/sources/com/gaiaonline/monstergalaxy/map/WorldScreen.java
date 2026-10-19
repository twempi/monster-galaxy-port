package com.gaiaonline.monstergalaxy.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
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
import com.gaiaonline.monstergalaxy.battle.dialogs.AlertDialog;
import com.gaiaonline.monstergalaxy.battle.effects.EffectType;
import com.gaiaonline.monstergalaxy.graphics.util.ScrollInputProcessor;
import com.gaiaonline.monstergalaxy.model.map.Island;
import com.gaiaonline.monstergalaxy.model.trainer.Trainer;
import com.gaiaonline.monstergalaxy.platform.PlatformHelper;
import com.gaiaonline.monstergalaxy.screen.ButtonElement;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.ScreenGroup;
import com.gaiaonline.monstergalaxy.screen.ScreenListener;
import com.gaiaonline.monstergalaxy.screen.TextElement;
import com.gaiaonline.monstergalaxy.screen.TextureElement;
import com.gaiaonline.monstergalaxy.screen.TouchReleasedListener;
import com.gaiaonline.monstergalaxy.screen.UIEvent;
import com.gaiaonline.monstergalaxy.service.AdsService;
import com.gaiaonline.monstergalaxy.tweak.Tweak;
import java.util.ArrayList;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class WorldScreen extends Screen {
    private static final float ISLAND_MAX_TOUCH_DISTANCE = 20.0f;
    private ArrayList<TextureRegion> backgroundRegions;
    private boolean hasShownAds;
    private int mapWidth;
    private int maxScrollOffset;
    private int minScrollOffset;
    private PlayerHUD playerHud;
    private float scrollDeltaX;
    private boolean scrollEnabled;
    private float scrollXOffset;
    private ScrollInputProcessor scroller;
    private ScreenGroup scrollingGroup;
    private Trainer trainer;
    private float unlockedIslandAnimationDelay;
    private ButtonElement unlockedIslandButton;
    private WorldMenu worldMenu;

    public WorldScreen() {
        this(0);
    }

    public WorldScreen(int unlockedIslandId) {
        this.scroller = new ScrollInputProcessor();
        this.scrollingGroup = new ScreenGroup();
        this.scrollDeltaX = 0.0f;
        this.hasShownAds = false;
        this.scrollEnabled = true;
        this.trainer = Game.getTrainer();
        this.stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        if (Game.getPlatformHelper().isAndroid()) {
            Game.getAndroidPlatformHelper().removeTextView();
        }
        if (unlockedIslandId > 0) {
            SoundManager.loadSound(SoundManager.SOUND_ISLAND_UNLOCK);
        }
        this.scrollingGroup.setStretchable(false);
        this.scrollingGroup.setAnchorPoint(ScreenElement.RelPoint.LEFT_BOTTOM);
        this.scrollingGroup.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.root.add(this.scrollingGroup);
        PlatformHelper.MGSkin sk = Game.getPlatformHelper().getSkin();
        int lastTileWidth = 370;
        TextureRegion bg4 = null;
        if (sk == PlatformHelper.MGSkin.SK2) {
            lastTileWidth = 1024;
            bg4 = new TextureRegion(new Texture(Gdx.files.internal("data/world.map.no.islands.4.png")), 0, 0, Opcodes.D2F, 320);
        }
        TextureRegion bg1 = new TextureRegion(new Texture(Gdx.files.internal("data/world.map.no.islands.1.png")), 0, 0, 1024, 320);
        TextureRegion bg2 = new TextureRegion(new Texture(Gdx.files.internal("data/world.map.no.islands.2.png")), 0, 0, 1024, 320);
        TextureRegion bg3 = new TextureRegion(new Texture(Gdx.files.internal("data/world.map.no.islands.3.png")), 0, 0, lastTileWidth, 320);
        this.backgroundRegions = new ArrayList<>(4);
        this.backgroundRegions.add(bg1);
        this.backgroundRegions.add(bg2);
        this.backgroundRegions.add(bg3);
        this.backgroundRegions.add(bg4);
        if (bg4 == null) {
            addMapBg(bg1, bg2, bg3);
        } else {
            addMapBg(bg1, bg2, bg3, bg4);
        }
        if (sk == PlatformHelper.MGSkin.SK2 && !this.trainer.isIslandCompleted(Game.getStorage().getIsland(28).getPreviousIsland()) && (!Tweak.isEnabled() || !Tweak.isOverrideNodeAndIslandLocks())) {
            TextureRegion homeIslandSpikesCover = new TextureRegion(new Texture(Gdx.files.internal("data/world.home.locked.png")), 0, 0, 1024, 320);
            TextureElement element = new TextureElement(homeIslandSpikesCover, true);
            element.setAnchorPoint(ScreenElement.RelPoint.LEFT_BOTTOM);
            element.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, 2485.0f, 0.0f);
            this.scrollingGroup.add(element);
        }
        this.scrollingGroup.setSize(this.mapWidth, Gdx.graphics.getHeight() / ScreenGroup.SCALE);
        this.minScrollOffset = (int) ((Gdx.graphics.getWidth() / ResolutionManager.getHorizontalAR()) - ((this.mapWidth / ResolutionManager.getHorizontalAR()) * ResolutionManager.getScaleFactor()));
        this.maxScrollOffset = 0;
        setScrollOffset((0 - ((int) this.trainer.getCurrentIsland().getPosition().x)) + (Gdx.graphics.getWidth() / 2));
        addIslands(unlockedIslandId);
        this.playerHud = new PlayerHUD();
        this.playerHud.setAnchorPoint(ScreenElement.RelPoint.LEFT_BOTTOM);
        this.playerHud.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, 0.0f, 0.0f);
        this.root.add(this.playerHud);
        this.worldMenu = new WorldMenu(this.stage);
        this.worldMenu.setListener(new WorldMenu.Listener() { // from class: com.gaiaonline.monstergalaxy.map.WorldScreen.1
            @Override // com.gaiaonline.monstergalaxy.map.WorldMenu.Listener
            public void onWorldMenuToggled(boolean open) {
                if (!open) {
                    Gdx.input.setInputProcessor(WorldScreen.this.scroller);
                    WorldScreen.this.scrollEnabled = true;
                } else {
                    WorldScreen.this.scrollEnabled = false;
                    Gdx.input.setInputProcessor(WorldScreen.this.stage);
                }
            }
        });
        this.root.add(this.worldMenu);
        Gdx.input.setInputProcessor(this.scroller);
    }

    private void reloadTextures() {
        int regionNum = 0;
        for (TextureRegion region : this.backgroundRegions) {
            regionNum++;
            if (region != null) {
                region.getTexture().dispose();
                region.setTexture(new Texture(Gdx.files.internal("data/world.map.no.islands." + regionNum + ".png")));
            }
        }
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void onMenuKeyPressed() {
        this.worldMenu.toggleMenu();
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void onBackKeyPressed() {
        if (this.dialog != null) {
            hideDialog();
        } else {
            showQuitDialog();
        }
    }

    private void addMapBg(TextureRegion... regions) {
        for (TextureRegion region : regions) {
            TextureElement element = new TextureElement(region, true);
            element.setAnchorPoint(ScreenElement.RelPoint.LEFT_BOTTOM);
            element.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, this.mapWidth, 0.0f);
            this.scrollingGroup.add(element);
            this.mapWidth += region.getRegionWidth();
        }
    }

    private void addIslands(int unlockedIslandId) {
        for (final Island island : Game.getStorage().getAllIslands()) {
            boolean canGoToIsland = false;
            try {
                canGoToIsland = this.trainer.canGoToIsland(island);
                boolean isHomeIsland = island.getId() == 28 && Game.getPlatformHelper().getSkin() == PlatformHelper.MGSkin.SK2;
                boolean canSeeIsland = !isHomeIsland || this.trainer.isIslandCompleted(island.getPreviousIsland()) || (Tweak.isEnabled() && Tweak.isOverrideNodeAndIslandLocks());
                if (canSeeIsland) {
                    TouchReleasedListener islandListener = new TouchReleasedListener() { // from class: com.gaiaonline.monstergalaxy.map.WorldScreen.2
                        @Override // com.gaiaonline.monstergalaxy.screen.TouchReleasedListener
                        public void touchReleased(Vector2 touchPoint, Vector2 releasePoint) {
                            if (Math.abs(WorldScreen.this.scrollDeltaX) <= 5.0f && touchPoint.dst(releasePoint) < WorldScreen.ISLAND_MAX_TOUCH_DISTANCE) {
                                if (WorldScreen.this.worldMenu.isOpen()) {
                                    WorldScreen.this.worldMenu.toggleMenu();
                                } else {
                                    WorldScreen.this.islandSelected(island);
                                }
                            }
                        }
                    };
                    try {
                        TextureRegion islandTexture = Assets.loadTexture(island.getIslandAssetName(!canGoToIsland));
                        ButtonElement islandElement = new ButtonElement(null, null, islandTexture, islandTexture);
                        if (canGoToIsland) {
                            islandElement.setSelectedColor(ColorConstants.SUGESTED_NODE_COLOR);
                        }
                        islandElement.setTouchReleasedListener(islandListener);
                        islandElement.setAnchorPoint(ScreenElement.RelPoint.CENTER);
                        islandElement.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, island.getPosition().x, island.getPosition().y);
                        this.scrollingGroup.add(islandElement);
                        if (canGoToIsland) {
                            ScreenGroup starsStatus = new ScreenGroup();
                            starsStatus.setSize(1.0f, 1.0f);
                            TextureElement starsBg = new TextureElement(Assets.loadTexture("world.star.bg"));
                            TextElement starsText = new TextElement(String.valueOf(this.trainer.getNumberOfStarsWonInIsland(island)) + "/" + island.getMaxNumberOfStars(), 0.5f, ColorConstants.PRIMARY_FONT_COLOR, true);
                            starsBg.setAnchorPoint(ScreenElement.RelPoint.CENTER);
                            starsBg.setPosition(ScreenElement.RelPoint.CENTER, -6.0f, -3.0f);
                            starsText.setAnchorPoint(ScreenElement.RelPoint.CENTER);
                            starsText.setPosition(ScreenElement.RelPoint.CENTER, 0.0f, 0.0f);
                            starsStatus.add(starsBg);
                            starsStatus.add(starsText);
                            starsStatus.setAnchorPoint(ScreenElement.RelPoint.CENTER);
                            starsStatus.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, island.getPosition().x + 8.0f, island.getPosition().y - 28.0f);
                            this.scrollingGroup.add(starsStatus);
                        }
                        if (island.getId() == unlockedIslandId) {
                            this.unlockedIslandButton = islandElement;
                        }
                    } catch (Exception e) {
                        e = e;
                        Gdx.app.log("FAIL!", island.getIslandAssetName(!canGoToIsland), e);
                    }
                }
            } catch (Exception e2) {
                e = e2;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void islandSelected(Island island) {
        if (Tweak.isOverrideNodeAndIslandLocks() || this.trainer.canGoToIsland(island)) {
            enterIsland(island);
            return;
        }
        this.scrollEnabled = false;
        String message = "Careful, tamer - you're not ready for this island! You'll need to beat the last island and tame at least " + island.getNumberOfRequiredMogasToUnlock() + " Mogas.\n\nRight now you have " + this.trainer.getNumberOfCaughtMogaTypes() + " Mogas.";
        createAlertDialog(message);
    }

    private void enterIsland(Island island) {
        this.trainer.setCurrentIsland(island);
        MonsterGalaxy.showScreen(ScreenCode.ISLAND);
    }

    private void setScrollOffset(float offset) {
        this.scrollXOffset = Math.max(Math.min(offset, this.maxScrollOffset), this.minScrollOffset);
        this.scrollingGroup.setPositionX(this.scrollXOffset);
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (this.hasShownAds) {
            if (getFrameNumberSinceResume() == 1 && Game.getPlatformHelper().shouldShowAds() && Game.getPlatformHelper().getAdsService().isAfterBattleAdPending()) {
                Game.getPlatformHelper().getAdsService().setAfterBattleAdPending(false);
                Game.getPlatformHelper().getAdsService().showInterstitial(AdsService.Location.AFTER_BATTLE, null);
            }
        } else if (Game.getPlatformHelper().shouldShowAds()) {
            Game.getPlatformHelper().getAdsService().setAfterBattleAdPending(false);
            Game.getPlatformHelper().getAdsService().showInterstitial(AdsService.Location.APP_START, null);
            this.hasShownAds = true;
        }
        if (this.scrollEnabled) {
            this.scrollDeltaX = this.scroller.getDeltaX();
            if (this.scrollDeltaX != 0.0f) {
                setScrollOffset(this.scrollXOffset + this.scrollDeltaX);
            }
            if (this.scrollXOffset == 0.0f) {
                this.scrollDeltaX = 0.0f;
            }
        }
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void present(float deltaTime) {
        this.stage.act(deltaTime);
        this.root.drawBackground(this.batcher);
        this.root.present(this.batcher);
        if (this.effect != null) {
            this.batcher.begin();
            this.effect.draw(this.batcher, deltaTime);
            this.batcher.end();
        }
        this.root.drawEffects(this.batcher);
        if (this.dialog != null) {
            this.dialog.present(this.batcher);
        }
        if (this.stage != null) {
            this.stage.draw();
        }
        if (this.unlockedIslandButton != null) {
            if (this.unlockedIslandAnimationDelay > 2.0f) {
                this.unlockedIslandButton.playEffect(EffectType.ISLAND_UNLOCKED_PARTICLES, ScreenElement.RelPoint.CENTER);
                SoundManager.playSound(SoundManager.SOUND_ISLAND_UNLOCK);
                this.unlockedIslandButton = null;
                return;
            }
            this.unlockedIslandAnimationDelay += deltaTime;
        }
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void onEnter(Screen prevScreen) {
        playMusic();
        SoundManager.playScreenChangeSound();
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void onResume() {
        reloadTextures();
        if (this.dialog != null) {
            this.dialog.resume();
            this.dialog.show();
        }
        if (this.dialog == null && MonsterGalaxy.isMiniGameAvailable()) {
            MonsterGalaxy.showScreen(ScreenCode.SLOTMACHINE, false, true);
        }
    }

    private void playMusic() {
        if (MonsterGalaxy.getPastScreen() == ScreenCode.WORLD || MonsterGalaxy.getPastScreen() == ScreenCode.ISLAND || this.unlockedIslandButton != null) {
            SoundManager.playMusic(Constants.worldStingAudio, Constants.worldAmbienceAudio);
        } else {
            SoundManager.playMusic(Constants.worldAmbienceAudio);
        }
    }

    private void createAlertDialog(String message) {
        this.scrollEnabled = false;
        ScreenListener listener = new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.map.WorldScreen.3
            @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
            public void onUIEvent(UIEvent event) {
                WorldScreen.this.hideDialog();
                WorldScreen.this.scrollEnabled = true;
            }
        };
        showDialog(new AlertDialog(listener, message, "Keep Playing", UIEvent.OK_BUTTON, UIEvent.CLOSE_BUTTON, true));
    }
}
