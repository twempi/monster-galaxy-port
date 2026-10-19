package com.gaiaonline.monstergalaxy.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ColorConstants;
import com.gaiaonline.monstergalaxy.app.Constants;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.MonsterGalaxy;
import com.gaiaonline.monstergalaxy.app.RateGameDialog;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.app.Screen;
import com.gaiaonline.monstergalaxy.app.ScreenCode;
import com.gaiaonline.monstergalaxy.app.SoundManager;
import com.gaiaonline.monstergalaxy.app.UpdateGameDialog;
import com.gaiaonline.monstergalaxy.app.fonts.Fonts;
import com.gaiaonline.monstergalaxy.assets.AssetsCache;
import com.gaiaonline.monstergalaxy.battle.BattleAssets;
import com.gaiaonline.monstergalaxy.battle.anim.AnimationManager;
import com.gaiaonline.monstergalaxy.battle.anim.MogaActor;
import com.gaiaonline.monstergalaxy.battle.dialogs.AlertDialog;
import com.gaiaonline.monstergalaxy.battle.tutorial.BlueCoffeeTutorial;
import com.gaiaonline.monstergalaxy.i18n.I18nManager;
import com.gaiaonline.monstergalaxy.model.battle.BattleReport;
import com.gaiaonline.monstergalaxy.model.map.Island;
import com.gaiaonline.monstergalaxy.model.map.Node;
import com.gaiaonline.monstergalaxy.model.minigame.Slotmachine;
import com.gaiaonline.monstergalaxy.model.quest.Quest;
import com.gaiaonline.monstergalaxy.model.trainer.Trainer;
import com.gaiaonline.monstergalaxy.platform.PlatformHelper;
import com.gaiaonline.monstergalaxy.quest.QuestStoryScreen;
import com.gaiaonline.monstergalaxy.screen.ActorScreenElement;
import com.gaiaonline.monstergalaxy.screen.ButtonElement;
import com.gaiaonline.monstergalaxy.screen.Dialog;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.ScreenGroup;
import com.gaiaonline.monstergalaxy.screen.ScreenListener;
import com.gaiaonline.monstergalaxy.screen.TextElement;
import com.gaiaonline.monstergalaxy.screen.TextureElement;
import com.gaiaonline.monstergalaxy.screen.UIEvent;
import com.gaiaonline.monstergalaxy.service.AdsService;
import com.gaiaonline.monstergalaxy.settings.SettingsManager;
import com.gaiaonline.monstergalaxy.slotmachine.SlotMachineScreen;
import com.gaiaonline.monstergalaxy.tweak.Tweak;
import java.util.Date;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public class IslandScreen extends Screen {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$gaiaonline$monstergalaxy$platform$PlatformHelper$MGSkin;
    private Quest actualQuest;
    private ButtonElement backToWorldButton;
    private Island island;
    private ScreenGroup islandScrollGroup;
    private Vector2 mapAssetSize;
    private float mapXFactor;
    private int mapXOffset;
    private float mapYFactor;
    private int mapYOffset;
    private ActorScreenElement menuArrowElement;
    private Node node;
    private ActorScreenElement nodeArrow;
    private HashMap<Integer, ButtonElement> nodeElements;
    private PlayerHUD playerHud;
    private ButtonElement toggleHudButton;
    private TextElement toggleHudLabel;
    private Trainer trainer;
    private TextureElement trainerLocation;
    private TextureElement trainerPortrait;
    private WorldMenu worldMenu;
    private boolean flagNextScreen = false;
    int frameCount = 0;
    boolean sessionMShow = false;

    static /* synthetic */ int[] $SWITCH_TABLE$com$gaiaonline$monstergalaxy$platform$PlatformHelper$MGSkin() {
        int[] iArr = $SWITCH_TABLE$com$gaiaonline$monstergalaxy$platform$PlatformHelper$MGSkin;
        if (iArr == null) {
            iArr = new int[PlatformHelper.MGSkin.valuesCustom().length];
            try {
                iArr[PlatformHelper.MGSkin.SK1.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[PlatformHelper.MGSkin.SK2.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            $SWITCH_TABLE$com$gaiaonline$monstergalaxy$platform$PlatformHelper$MGSkin = iArr;
        }
        return iArr;
    }

    public IslandScreen() {
        this.stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        Gdx.input.setInputProcessor(this.stage);
        this.trainer = Game.getTrainer();
        this.island = this.trainer.getCurrentIsland();
        this.root.addBackground(this.island.getMapAsset());
        TextureRegion mapAssetRegion = AssetsCache.get(null, this.island.getMapAsset());
        this.mapAssetSize = new Vector2(mapAssetRegion.getRegionWidth(), mapAssetRegion.getRegionHeight());
        addNodes();
        addTrainer();
        addNodeArrow();
        addBackToWorldButton();
        addToggleHudButton();
        addIslandInfo();
        this.playerHud = new PlayerHUD();
        this.playerHud.setAnchorPoint(ScreenElement.RelPoint.LEFT_BOTTOM);
        this.playerHud.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, 0.0f, 0.0f);
        this.root.add(this.playerHud);
        this.worldMenu = new WorldMenu(this.stage);
        this.worldMenu.setListener(new WorldMenu.Listener() { // from class: com.gaiaonline.monstergalaxy.map.IslandScreen.1
            @Override // com.gaiaonline.monstergalaxy.map.WorldMenu.Listener
            public void onWorldMenuToggled(boolean open) {
                IslandScreen.this.updateToggleHudButton();
            }
        });
        this.root.add(this.worldMenu);
        if (Game.getPlatformHelper().isAndroid()) {
            Game.getAndroidPlatformHelper().removeTextView();
        }
        boolean tutorialAlreadyShown = SettingsManager.getBoolean("tutorialOnFinishedQuest264Shown");
        if (this.trainer.getCompletedQuests().contains(264) && !tutorialAlreadyShown) {
            SettingsManager.setValue("tutorialOnFinishedQuest264Shown", true);
            showFirstScreenTutorial();
        }
        boolean tutorialAlreadyShown2 = SettingsManager.getBoolean("tutorialOnFinishedQuest6Shown");
        if (this.trainer.getCompletedQuests().contains(6) && !tutorialAlreadyShown2) {
            SettingsManager.setValue("tutorialOnFinishedQuest6Shown", true);
            showRateGameDialog();
        }
        if (this.trainer.isNodeCompleted(12) && !SettingsManager.getBoolean("tutorialOnFinishedNode12Shown")) {
            SettingsManager.setValue("tutorialOnFinishedNode12Shown", true);
            showThirdScreenTutorial();
        }
        BattleReport report = this.trainer.getBattleReport();
        if (report != null) {
            if (report.playerHasWon() && !report.hasCapturedMogas() && this.trainer.hasActiveCaptureQuestOnCurrentNode()) {
                showCaptureDialog();
            } else if (report.hasCompletedIsland() && !report.hasUnlockedNextIsland()) {
                showUnlockedIslandDialog();
            } else if (SlotMachineScreen.available && report.hasCompletedNode(14)) {
                Slotmachine slotMachine = Game.getStorage().getSlotMachine();
                if (slotMachine.getReels().get(0).getPrizes().size() > 0) {
                    showSlotMachineDialog();
                }
            }
        }
        this.trainer.setBattleReport(null);
        updateToggleHudButton();
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    private void animTest() {
        Image bg = new Image(new TextureRegionDrawable(AssetsCache.newSolidRectangle(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), ColorConstants.PRIMARY_FONT_COLOR)));
        this.stage.addActor(bg);
        Fonts.regular.setScale(1.0f, 1.0f);
        Label.LabelStyle st = new Label.LabelStyle(Fonts.regular, Color.RED);
        Label lbl = new Label("NEW - WS 5", st);
        lbl.setPosition(250.0f, 200.0f);
        this.stage.addActor(lbl);
        BattleAssets.load();
        final MogaActor mogaActor = new MogaActor(null, Game.getTrainer().getTeam().getFirstMoga(), BattleAssets.getTexture(Game.getTrainer().getTeam().getFirstMoga(), false), false);
        mogaActor.setX(60.0f);
        mogaActor.setY(40.0f);
        mogaActor.setColor(Color.WHITE);
        this.stage.addActor(mogaActor);
        AnimationManager.initialize();
        Action action = getAction();
        RepeatAction repeatAction = new RepeatAction();
        DelayAction d = new DelayAction();
        SequenceAction s = new SequenceAction();
        RunnableAction runa = new RunnableAction();
        runa.setRunnable(new Runnable() { // from class: com.gaiaonline.monstergalaxy.map.IslandScreen.2
            @Override // java.lang.Runnable
            public void run() {
                mogaActor.setX(60.0f);
                mogaActor.setY(40.0f);
                mogaActor.setScale(1.0f);
                mogaActor.setColor(Color.WHITE);
                mogaActor.setRotation(0.0f);
                System.out.println("RESTARTING " + new Date());
            }
        });
        d.setAction(action);
        d.setDuration(0.5f);
        s.addAction(d);
        s.addAction(runa);
        repeatAction.setAction(s);
        repeatAction.setCount(-1);
        mogaActor.addAction(repeatAction);
    }

    private Action getAction() {
        Action anim = AnimationManager.getInstance().getAnimation("zodiac-attack", false);
        System.out.println(anim);
        return anim;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Vector2 toScreenCoords(Node node) {
        if (Game.getPlatformHelper().getSkin() != PlatformHelper.MGSkin.SK2) {
            return node.getPosition();
        }
        if (this.mapXFactor == 0.0f) {
            float mapAssetAR = this.mapAssetSize.x / this.mapAssetSize.y;
            if (mapAssetAR < 1.5f) {
                float assetBaseCroppedHeight = 480.0f / mapAssetAR;
                this.mapXFactor = 1.0f;
                this.mapXOffset = 0;
                this.mapYFactor = 320.0f / assetBaseCroppedHeight;
                this.mapYOffset = Math.round((320.0f - assetBaseCroppedHeight) / 2.0f);
            } else {
                float assetBaseCroppedWidth = mapAssetAR * 320.0f;
                this.mapXFactor = 480.0f / assetBaseCroppedWidth;
                this.mapXOffset = Math.round((480.0f - assetBaseCroppedWidth) / 2.0f);
                this.mapYFactor = 1.0f;
                this.mapYOffset = 0;
            }
        }
        return new Vector2((node.getPosition().x - this.mapXOffset) * this.mapXFactor, (node.getPosition().y - this.mapYOffset) * this.mapYFactor);
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void onMenuKeyPressed() {
        this.worldMenu.toggleMenu();
    }

    private void addTrainer() {
        TextureRegion tammer;
        Node currentNode = this.trainer.getCurrentNode();
        currentNode.getPosition();
        Vector2 nodeScreenPos = toScreenCoords(currentNode);
        this.trainerLocation = new TextureElement(Assets.loadTexture("island.trainer.location"));
        this.trainerLocation.setAnchorPoint(ScreenElement.RelPoint.CENTER_BOTTOM);
        this.trainerLocation.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, nodeScreenPos.x, nodeScreenPos.y);
        this.root.add(this.trainerLocation);
        if (Game.getTrainer().getGender() == Trainer.Gender.MALE) {
            tammer = Assets.loadTexture("island.trainer.portrait.male");
        } else {
            tammer = Assets.loadTexture("island.trainer.portrait.female");
        }
        this.trainerPortrait = new TextureElement(tammer);
        this.trainerPortrait.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        this.trainerPortrait.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, nodeScreenPos.x + 2.0f, nodeScreenPos.y + 32.0f);
        this.root.add(this.trainerPortrait);
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    private void addNodes() {
        TextureRegion nodeTexture;
        this.nodeElements = new HashMap<>();
        for (final Node node : this.trainer.getCurrentIsland().getNodes()) {
            ScreenListener nodeListener = new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.map.IslandScreen.3
                @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
                public void onUIEvent(UIEvent event) {
                    IslandScreen.this.nodeSelected(node);
                }
            };
            Vector2 nodeScreenPos = toScreenCoords(node);
            Color nodeColor = ColorConstants.PRIMARY_FONT_COLOR;
            if (this.trainer.canGoToNode(node)) {
                nodeTexture = Assets.loadTexture("island.node.unlocked.center");
                if (this.trainer.isNodeSuggested(node)) {
                    nodeColor = ColorConstants.SUGESTED_NODE_COLOR;
                }
                ActorScreenElement actorScreenElement = new ActorScreenElement();
                Image image = new Image(Assets.loadTexture("island.node.unlocked.outline"));
                image.setColor(nodeColor);
                image.setWidth(image.getWidth() * ResolutionManager.getScaleFactor());
                image.setHeight(image.getHeight() * ResolutionManager.getScaleFactor());
                actorScreenElement.setActor(image);
                actorScreenElement.setAnchorPoint(ScreenElement.RelPoint.CENTER);
                actorScreenElement.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, nodeScreenPos.x, nodeScreenPos.y);
                RepeatAction repeatAction = new RepeatAction();
                repeatAction.setCount(-1);
                SequenceAction sequenceAction = new SequenceAction();
                AlphaAction alpha1 = new AlphaAction();
                alpha1.setAlpha(1.0f);
                alpha1.setDuration(0.5f);
                AlphaAction alpha2 = new AlphaAction();
                alpha2.setAlpha(0.6f);
                alpha2.setDuration(0.5f);
                sequenceAction.addAction(alpha1);
                sequenceAction.addAction(alpha2);
                repeatAction.setAction(sequenceAction);
                image.addAction(repeatAction);
                this.root.add(actorScreenElement);
            } else {
                nodeTexture = Assets.loadTexture("island.small.lock");
            }
            ButtonElement nodeElement = new ButtonElement(nodeListener, null, nodeTexture);
            nodeElement.setColor(nodeColor);
            nodeElement.setAnchorPoint(ScreenElement.RelPoint.CENTER);
            nodeElement.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, nodeScreenPos.x, nodeScreenPos.y);
            this.root.add(nodeElement);
            this.nodeElements.put(Integer.valueOf(node.getId()), nodeElement);
        }
    }

    private void addNodeArrow() {
        for (Node node : this.trainer.getCurrentIsland().getNodes()) {
            if (node.getId() < 13 && !this.trainer.isNodeCompleted(node.getId()) && !this.trainer.isNodeLocked(node)) {
                this.nodeArrow = addLeftArrow(node, this.nodeElements.get(Integer.valueOf(node.getId())));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void nodeSelected(Node node) {
        if (Tweak.isOverrideNodeAndIslandLocks() || !this.trainer.isNodeLocked(node)) {
            if (!this.trainer.getTeam().hasHealtyMogas() || this.trainer.getTeam().getMogas().size() == 0) {
                showManageTeamDialog();
                return;
            }
            this.node = node;
            if (isUserPlayingInOrder(node.getId())) {
                showDialog(new NodeInfoDialog(node));
            } else {
                showPlayingOrderDialog();
            }
        }
    }

    private void addIslandInfo() {
        this.islandScrollGroup = new ScreenGroup();
        this.islandScrollGroup.setStretchable(false);
        this.islandScrollGroup.setSize(289.0f, 71.0f);
        this.islandScrollGroup.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        this.islandScrollGroup.setPosition(ScreenElement.RelPoint.CENTER_BOTTOM, 13.0f, 35.0f);
        this.root.add(this.islandScrollGroup);
        TextureElement scroll = new TextureElement(Assets.loadTexture("island.scroll"));
        scroll.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        scroll.setPosition(ScreenElement.RelPoint.CENTER, 0.0f, 0.0f);
        TextElement islandNameText = new TextElement(this.island.getLocalizedName().toUpperCase(), 0.7f, ColorConstants.ISLAND_TEXT_NAME_COLOR, true);
        islandNameText.enableHorizontalAutoSize(135.0f);
        islandNameText.setAnchorPoint(ScreenElement.RelPoint.LEFT_CENTER);
        islandNameText.setPosition(ScreenElement.RelPoint.CENTER, -102.0f, 8.0f);
        TextElement capturedMogasText = new TextElement("", 0.45f, ColorConstants.ISLAND_TEXT_PROGRESS_COLOR, true);
        capturedMogasText.setAnchorPoint(ScreenElement.RelPoint.LEFT_CENTER);
        capturedMogasText.setPosition(ScreenElement.RelPoint.CENTER, -102.0f, -12.0f);
        capturedMogasText.setText(String.valueOf(this.trainer.getNumberOfCapturesInIsland(this.island)) + "/" + this.island.getMaxNumberOfMogas() + " " + I18nManager.getText(I18nManager.I18nKey.MOGAS_CAUGHT));
        capturedMogasText.enableHorizontalAutoSize(135.0f);
        TextureElement starIcon = new TextureElement(Assets.loadTexture("island.star.icon"));
        starIcon.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        starIcon.setPosition(ScreenElement.RelPoint.CENTER, 48.0f, 9.0f);
        TextElement obtainedStarsText = new TextElement("", 0.5f, ColorConstants.FOURTH_FONT_COLOR, true);
        obtainedStarsText.setTextAlignment(BitmapFont.HAlignment.RIGHT);
        obtainedStarsText.setAnchorPoint(ScreenElement.RelPoint.RIGHT_CENTER);
        obtainedStarsText.setPosition(ScreenElement.RelPoint.CENTER, 105.0f, 9.0f);
        obtainedStarsText.setText(String.valueOf(this.trainer.getNumberOfStarsWonInIsland(this.island)) + "/" + this.island.getMaxNumberOfStars());
        TextureElement questIcon = new TextureElement(Assets.loadTexture("island.quest.icon.small"));
        questIcon.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        questIcon.setPosition(ScreenElement.RelPoint.CENTER, 48.0f, -12.0f);
        TextElement questsText = new TextElement("", 0.5f, ColorConstants.FOURTH_FONT_COLOR, true);
        questsText.setTextAlignment(BitmapFont.HAlignment.RIGHT);
        questsText.setAnchorPoint(ScreenElement.RelPoint.RIGHT_CENTER);
        questsText.setPosition(ScreenElement.RelPoint.CENTER, 105.0f, -12.0f);
        questsText.setText(String.valueOf(this.trainer.getNumberOfCompletedQuestsInIsland(this.island)) + "/" + this.island.getQuestCount());
        this.islandScrollGroup.add(scroll);
        this.islandScrollGroup.add(islandNameText);
        this.islandScrollGroup.add(capturedMogasText);
        this.islandScrollGroup.add(starIcon);
        this.islandScrollGroup.add(obtainedStarsText);
        this.islandScrollGroup.add(questIcon);
        this.islandScrollGroup.add(questsText);
    }

    private void addBackToWorldButton() {
        if (worldMapIsAccesible()) {
            this.backToWorldButton = new ButtonElement(new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.map.IslandScreen.4
                @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
                public void onUIEvent(UIEvent event) {
                    MonsterGalaxy.showScreen(ScreenCode.WORLD);
                }
            }, null, Assets.loadTexture("island.world.button"), Assets.loadTexture("island.world.button.press"));
            this.backToWorldButton.setAnchorPoint(ScreenElement.RelPoint.CENTER);
            this.backToWorldButton.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, 36.0f, 35.0f);
            this.root.add(this.backToWorldButton);
        }
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void onBackKeyPressed() {
        if (this.dialog != null) {
            hideDialog();
            if (this.menuArrowElement != null) {
                this.menuArrowElement.setVisible(false);
                return;
            }
            return;
        }
        if (worldMapIsAccesible()) {
            MonsterGalaxy.showScreen(ScreenCode.WORLD);
        } else {
            showQuitDialog();
        }
    }

    private boolean worldMapIsAccesible() {
        return Tweak.isOverrideNodeAndIslandLocks() || this.island.getId() != 2 || Game.getTrainer().isIslandCompleted(this.island);
    }

    private void addToggleHudButton() {
        this.toggleHudButton = new ButtonElement(new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.map.IslandScreen.5
            @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
            public void onUIEvent(UIEvent event) {
                IslandScreen.this.toggleHud();
            }
        }, null, Assets.loadTexture("island.showhide.off"), Assets.loadTexture("island.showhide.press"));
        this.toggleHudButton.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        this.toggleHudButton.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, 0.0f, 0.0f);
        this.toggleHudButton.setVisible(false);
        this.root.add(this.toggleHudButton);
        this.toggleHudLabel = new TextElement("Hide", 0.55f, ColorConstants.PRIMARY_FONT_COLOR, false);
        this.toggleHudLabel.setTextAlignment(BitmapFont.HAlignment.CENTER);
        this.toggleHudLabel.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        this.toggleHudLabel.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, 0.0f, 0.0f);
        this.toggleHudLabel.setVisible(false);
        this.root.add(this.toggleHudLabel);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void toggleHud() {
        this.playerHud.setVisible(!this.playerHud.isVisible());
        this.islandScrollGroup.setVisible(this.playerHud.isVisible());
        updateToggleHudButton();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateToggleHudButton() {
        this.toggleHudButton.setVisible(this.worldMenu.isOpen() || !this.playerHud.isVisible());
        this.toggleHudLabel.setVisible(this.toggleHudButton.isVisible());
        String showHideText = this.playerHud.isVisible() ? I18nManager.getText(I18nManager.I18nKey.HIDE) : I18nManager.getText(I18nManager.I18nKey.SHOW);
        this.toggleHudLabel.setText(showHideText);
        this.toggleHudLabel.enableHorizontalAutoSize(45.0f);
        int y = 30;
        if (this.backToWorldButton != null) {
            y = 30 + 64;
        }
        this.toggleHudButton.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, 36, y);
        this.toggleHudLabel.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, 36, y + 3.5f);
    }

    private ActorScreenElement addLeftArrow(Node node, ScreenElement element) {
        ActorScreenElement arrowElement = new ActorScreenElement();
        if (node.getId() == 12) {
            Image attackArrow = getArrowActor("leftArrow", "arrow.left", false, false);
            arrowElement.setActor(attackArrow);
            arrowElement.setAnchorPoint(ScreenElement.RelPoint.LEFT_CENTER);
            arrowElement.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, 35.0f, 17.0f);
        } else {
            Image attackArrow2 = getArrowActor("leftArrow", "arrow.right", false, false);
            arrowElement.setActor(attackArrow2);
            arrowElement.setAnchorPoint(ScreenElement.RelPoint.RIGHT_CENTER);
            arrowElement.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, 0.0f, 15.0f);
        }
        arrowElement.setAlign(element);
        this.root.add(arrowElement);
        return arrowElement;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Image getArrowActor(String name, String arrowAsset, boolean flippedH, boolean flippedV) {
        TextureRegion region = Assets.loadTexture(arrowAsset);
        if (flippedH || flippedV) {
            TextureRegion region2 = new TextureRegion(region);
            region2.flip(flippedV, flippedH);
            region = region2;
        }
        Image arrow = new Image(region);
        arrow.setOrigin(arrow.getWidth() / 2.0f, arrow.getHeight() / 2.0f);
        ScaleToAction scaleUp = new ScaleToAction();
        scaleUp.setScale(1.2f);
        scaleUp.setDuration(0.5f);
        ScaleToAction scaleDown = new ScaleToAction();
        scaleDown.setScale(1.0f);
        scaleDown.setDuration(0.5f);
        SequenceAction sequence = new SequenceAction();
        sequence.addAction(scaleUp);
        sequence.addAction(scaleDown);
        RepeatAction forever = new RepeatAction();
        forever.setCount(-1);
        forever.setAction(sequence);
        arrow.addAction(forever);
        ResolutionManager.scaleActor(arrow);
        return arrow;
    }

    private void showUnlockedIslandDialog() {
        String pendingCapture;
        String message;
        Island nextIsland = this.trainer.getCurrentIsland().getNextIsland();
        if (nextIsland != null) {
            int count = nextIsland.getNumberOfRequiredMogasToUnlock() - this.trainer.getMogas().size();
            if (count > 1) {
                pendingCapture = String.valueOf(count) + I18nManager.getText(I18nManager.I18nKey.MORE_MOGAS);
            } else {
                pendingCapture = I18nManager.getText(I18nManager.I18nKey.ONE_MORE_MOGA);
            }
            switch ($SWITCH_TABLE$com$gaiaonline$monstergalaxy$platform$PlatformHelper$MGSkin()[Game.getPlatformHelper().getSkin().ordinal()]) {
                case 2:
                    message = "You're making great progress in your quest to escape Otho's exile, but you'll need to capture " + pendingCapture + " before you venture to the next island. Good luck!";
                    break;
                default:
                    message = I18nManager.getText(I18nManager.I18nKey.CLOSE_TO_DEFEAT).replace("%@", pendingCapture);
                    break;
            }
            ScreenListener listener = new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.map.IslandScreen.6
                @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
                public void onUIEvent(UIEvent event) {
                    IslandScreen.this.hideDialog();
                }
            };
            showDialog(new AlertDialog(listener, message, I18nManager.getText(I18nManager.I18nKey.KEEP_PLAYING), UIEvent.OK_BUTTON, UIEvent.CLOSE_BUTTON, true));
        }
    }

    private void showManageTeamDialog() {
        AlertDialog alertDialog;
        ScreenListener listener = new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.map.IslandScreen.7
            @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
            public void onUIEvent(UIEvent event) {
                if (event == UIEvent.OK_BUTTON) {
                    MonsterGalaxy.showScreen(ScreenCode.TEAM, false, true);
                } else {
                    IslandScreen.this.hideDialog();
                }
            }
        };
        if (this.trainer.getTeam().getMogas().size() == 0) {
            String message = I18nManager.getText(I18nManager.I18nKey.NO_MOGAS_ON_TEAM);
            alertDialog = new AlertDialog(listener, message, I18nManager.getText(I18nManager.I18nKey.MANAGE_TEAM), UIEvent.OK_BUTTON, UIEvent.CLOSE_BUTTON, true);
        } else {
            String message2 = I18nManager.getText(I18nManager.I18nKey.MOGAS_TIRED);
            alertDialog = new AlertDialog(listener, message2, I18nManager.getText(I18nManager.I18nKey.MANAGE_TEAM), UIEvent.OK_BUTTON, UIEvent.CLOSE_BUTTON, true);
            alertDialog.setWrapWidth(290.0f);
        }
        showDialog(alertDialog);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showManageTamerDialog() {
        String message = I18nManager.getText(I18nManager.I18nKey.TAMER_OUT_ENERGY);
        ScreenListener listener = new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.map.IslandScreen.8
            @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
            public void onUIEvent(UIEvent event) {
                if (event == UIEvent.OK_BUTTON) {
                    MonsterGalaxy.showScreen(ScreenCode.TAMER);
                } else {
                    IslandScreen.this.hideDialog();
                }
            }
        };
        showDialog(new AlertDialog(listener, message, I18nManager.getText(I18nManager.I18nKey.MANAGE_TAMER), UIEvent.OK_BUTTON, UIEvent.CLOSE_BUTTON, true));
    }

    private void showCaptureDialog() {
        String message = I18nManager.getText(I18nManager.I18nKey.REMEMBER_CAPTURE);
        ScreenListener listener = new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.map.IslandScreen.9
            @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
            public void onUIEvent(UIEvent event) {
                IslandScreen.this.hideDialog();
            }
        };
        showDialog(new AlertDialog(listener, message, I18nManager.getText(I18nManager.I18nKey.KEEP_PLAYING), UIEvent.OK_BUTTON, UIEvent.CLOSE_BUTTON, true));
    }

    private void showFirstScreenTutorial() {
        this.menuArrowElement = new ActorScreenElement();
        Image attackArrow = getArrowActor("rightArrow", "arrow.left", false, true);
        this.menuArrowElement.setActorDeprecated(attackArrow);
        this.menuArrowElement.setAnchorPoint(ScreenElement.RelPoint.LEFT_BOTTOM);
        this.menuArrowElement.setPosition(ScreenElement.RelPoint.CENTER_BOTTOM, 142.0f, 8.0f);
        this.root.add(this.menuArrowElement);
        ScreenListener listener = new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.map.IslandScreen.10
            @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
            public void onUIEvent(UIEvent event) {
                IslandScreen.this.hideDialog();
                if (event == UIEvent.OK_BUTTON) {
                    IslandScreen.this.showSecondScreenTutorial();
                }
                IslandScreen.this.menuArrowElement.setVisible(false);
            }
        };
        String message = I18nManager.getText(I18nManager.I18nKey.YOU_CAN_USE_MENU);
        AlertDialog dialog = new AlertDialog(listener, message, I18nManager.getText(I18nManager.I18nKey.MORE), UIEvent.OK_BUTTON, UIEvent.CLOSE_BUTTON, false);
        showDialog(dialog);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showSecondScreenTutorial() {
        ScreenListener listener = new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.map.IslandScreen.11
            @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
            public void onUIEvent(UIEvent event) {
                IslandScreen.this.hideDialog();
            }
        };
        String message = I18nManager.getText(I18nManager.I18nKey.YOU_CAN_SEE_STATUS);
        Dialog dialog = new AlertDialog(listener, message, I18nManager.getText(I18nManager.I18nKey.KEEP_PLAYING), UIEvent.OK_BUTTON, UIEvent.CLOSE_BUTTON, false);
        ActorScreenElement arrowElement = new ActorScreenElement();
        Image arrow = getArrowActor("upArrow", "down.arrow", true, false);
        arrowElement.setActorDeprecated(arrow);
        arrowElement.setAnchorPoint(ScreenElement.RelPoint.LEFT_CENTER);
        arrowElement.setPosition(ScreenElement.RelPoint.LEFT_TOP, 20.0f, -75.0f);
        dialog.add(arrowElement);
        showDialog(dialog);
    }

    private void showThirdScreenTutorial() {
        ScreenListener listener = new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.map.IslandScreen.12
            @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
            public void onUIEvent(UIEvent event) {
                IslandScreen.this.hideDialog();
            }
        };
        String message = I18nManager.getText(I18nManager.I18nKey.YOU_HAVE_A_TEAM);
        AlertDialog dialog = new AlertDialog(listener, message, I18nManager.getText(I18nManager.I18nKey.KEEP_PLAYING), UIEvent.OK_BUTTON, UIEvent.CLOSE_BUTTON, false);
        showDialog(dialog);
    }

    private void showPlayingOrderDialog() {
        ScreenListener listener = new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.map.IslandScreen.13
            @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
            public void onUIEvent(UIEvent event) {
                IslandScreen.this.hideDialog();
            }
        };
        String message = I18nManager.getText(I18nManager.I18nKey.MUST_VISIT_LOCATION);
        AlertDialog dialog = new AlertDialog(listener, message, I18nManager.getText(I18nManager.I18nKey.KEEP_PLAYING), UIEvent.OK_BUTTON, UIEvent.CLOSE_BUTTON, false);
        showDialog(dialog);
    }

    private void showSlotMachineDialog() {
        String message = I18nManager.getText(I18nManager.I18nKey.THERE_ARE_SECRET_MOGAS);
        ScreenListener listener = new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.map.IslandScreen.14
            @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
            public void onUIEvent(UIEvent event) {
                if (event == UIEvent.OK_BUTTON) {
                    MonsterGalaxy.showScreen(ScreenCode.SLOTMACHINE);
                } else {
                    IslandScreen.this.hideDialog();
                }
            }
        };
        showDialog(new AlertDialog(listener, message, I18nManager.getText(I18nManager.I18nKey.GET_LUCKY), UIEvent.OK_BUTTON, UIEvent.CLOSE_BUTTON, true));
    }

    private void showRateGameDialog() {
        ScreenListener listener = new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.map.IslandScreen.15
            @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
            public void onUIEvent(UIEvent event) {
                if (event == UIEvent.CANCEL_BUTTON) {
                    IslandScreen.this.showDialog(new AlertDialog(this, 5, I18nManager.getText(I18nManager.I18nKey.THANK_YOU_FEEDBACK), UIEvent.CLOSE_BUTTON));
                    return;
                }
                if (event == UIEvent.ACCEPT_BUTTON) {
                    IslandScreen.this.hideDialog();
                    if (Game.getPlatformHelper().isAndroid()) {
                        Game.getAndroidPlatformHelper().openMarketProductPage();
                        return;
                    }
                    return;
                }
                IslandScreen.this.hideDialog();
            }
        };
        showDialog(new RateGameDialog(listener));
    }

    private void playMusic() {
        if (this.island.getAvailableAmbienceAudio() != null) {
            if (MonsterGalaxy.getPastScreen() == ScreenCode.WORLD || MonsterGalaxy.getPastScreen() == ScreenCode.CHOOSE_MOGA_NAME) {
                SoundManager.playMusic(this.island.getStingAudio(), this.island.getAvailableAmbienceAudio());
                return;
            } else {
                SoundManager.playMusic(this.island.getAvailableAmbienceAudio());
                return;
            }
        }
        SoundManager.playMusic(Constants.worldStingAudio, Constants.worldAmbienceAudio);
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (getFrameNumberSinceResume() == 1 && Game.getPlatformHelper().shouldShowAds() && Game.getPlatformHelper().getAdsService().isAfterBattleAdPending()) {
            Game.getPlatformHelper().getAdsService().setAfterBattleAdPending(false);
            Game.getPlatformHelper().getAdsService().showInterstitial(AdsService.Location.AFTER_BATTLE, null);
        }
        if (!this.sessionMShow) {
            MonsterGalaxy.getAnalytics().sessionMPresentActivity();
            this.sessionMShow = true;
        }
        if (this.dialog != null) {
            this.root.update(deltaTime);
            this.dialog.update(deltaTime);
        }
        if (this.flagNextScreen) {
            this.frameCount++;
            if (this.frameCount > ((double) Gdx.app.getGraphics().getFramesPerSecond()) * 0.6d) {
                showNextScreen();
                this.frameCount = 0;
                this.flagNextScreen = false;
                this.sessionMShow = false;
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
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void onEnter(Screen prevScreen) {
        playMusic();
        SoundManager.playScreenChangeSound();
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void onResume() {
        this.root.addBackground(this.island.getMapAsset());
        if (this.dialog != null) {
            this.dialog.show();
        }
        if (this.dialog == null && MonsterGalaxy.isMiniGameAvailable()) {
            MonsterGalaxy.showScreen(ScreenCode.SLOTMACHINE, false, true);
        }
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    protected void showDialog(Dialog dialog) {
        if (this.nodeArrow != null) {
            this.nodeArrow.setVisible(false);
        }
        super.showDialog(dialog);
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void hideDialog() {
        if (this.nodeArrow != null && !this.flagNextScreen) {
            this.nodeArrow.setVisible(true);
        }
        super.hideDialog();
    }

    private void showNextScreen() {
        if (this.actualQuest != null) {
            MonsterGalaxy.showScreen(new QuestStoryScreen(this.actualQuest));
            return;
        }
        this.actualQuest = this.trainer.getAvailableQuestForNode(this.node);
        if (this.actualQuest != null) {
            MonsterGalaxy.showScreen(new QuestStoryScreen(this.actualQuest));
        } else if (this.trainer.getCurrentNode().getEncounter().getId() == 460) {
            MonsterGalaxy.showScreen(new BlueCoffeeTutorial());
        } else {
            MonsterGalaxy.showBattleScreen();
        }
    }

    private boolean isUserPlayingInOrder(int nodeId) {
        if (this.trainer.getCurrentIsland().getId() != 2 || nodeId >= 14) {
            return true;
        }
        int currentNodeId = this.trainer.getCurrentNode().getId();
        return (nodeId == 2 && !this.trainer.isNodeCompleted(nodeId)) || this.trainer.isNodeCompleted(12) || this.trainer.isNodeCompleted(currentNodeId + 2) || !this.trainer.isNodeCompleted(nodeId);
    }

    private void checkForUpdate() {
        if (this.dialog == null && Game.isNewVersionAvailable()) {
            ScreenListener listener = new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.map.IslandScreen.16
                @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
                public void onUIEvent(UIEvent event) {
                    IslandScreen.this.hideDialog();
                    Game.setNewVersionAvailable(false);
                    if (event == UIEvent.OK_BUTTON && Game.getPlatformHelper().isAndroid()) {
                        Game.getAndroidPlatformHelper().openMarketProductPage();
                    }
                }
            };
            showDialog(new UpdateGameDialog(listener));
        }
    }

    private class NodeInfoDialog extends Dialog implements ScreenListener {
        private Node node;

        public NodeInfoDialog(Node node) {
            super(null);
            this.node = node;
            addBackground();
            addThumbnail();
            addLabels();
            addStars();
            addButtons();
            setScreenListener(this);
        }

        private void addBackground() {
            add(Assets.loadTexture("node.info.bg"), 0.0f, 0.0f, ScreenElement.RelPoint.CENTER, ScreenElement.RelPoint.CENTER);
        }

        private void addThumbnail() {
            String assetName = this.node.getThumbnailAsset();
            add(Assets.loadTexture(assetName), -114.0f, 20.0f, ScreenElement.RelPoint.CENTER);
        }

        private void addButtons() {
            TextureRegion normalButton = Assets.loadTexture("red.btn.small");
            TextureRegion selectedButton = Assets.loadTexture("red.btn.small.press");
            ButtonElement cancelButton = addButton(UIEvent.CANCEL_BUTTON, normalButton, selectedButton, -100.0f, -60.0f, ScreenElement.RelPoint.CENTER);
            addLabel(I18nManager.getText(I18nManager.I18nKey.CANCEL), -100.0f, -55.0f, ScreenElement.RelPoint.CENTER, 0.6f, ColorConstants.PRIMARY_FONT_COLOR, true);
            TextureRegion normalButton2 = Assets.loadTexture("green.btn.large");
            TextureRegion selectedButton2 = Assets.loadTexture("green.btn.large.press");
            ButtonElement playButton = addButton(UIEvent.OK_BUTTON, normalButton2, selectedButton2, 70.0f, -60.0f, ScreenElement.RelPoint.CENTER);
            ScreenElement playLabel = new TextElement(I18nManager.getText(I18nManager.I18nKey.PLAY), 0.6f, ColorConstants.PRIMARY_FONT_COLOR, true);
            playLabel.setAnchorPoint(ScreenElement.RelPoint.RIGHT_CENTER);
            playLabel.setPosition(ScreenElement.RelPoint.CENTER, 60.0f, -55.0f);
            add(playLabel);
            addLabel(String.valueOf(this.node.getEnergyCost()), 110.0f, -55.0f, ScreenElement.RelPoint.CENTER, 0.6f, ColorConstants.PRIMARY_FONT_COLOR, true);
            if (this.node.getId() == 2 && !IslandScreen.this.trainer.isNodeCompleted(this.node.getId())) {
                ActorScreenElement arrow = new ActorScreenElement();
                arrow.setActor(IslandScreen.this.getArrowActor("arrow", "arrow.left", false, false));
                arrow.setAlign(playButton);
                arrow.setAnchorPoint(ScreenElement.RelPoint.CENTER);
                arrow.setPosition(ScreenElement.RelPoint.RIGHT_CENTER, 0.0f, 0.0f);
                add(arrow);
            }
            add(Assets.loadTexture("island.energy.icon"), 80.0f, -55.0f, ScreenElement.RelPoint.CENTER);
            cancelButton.setScreenListener(this);
            playButton.setScreenListener(this);
        }

        private void addLabels() {
            addLabel(this.node.getName(), -165.0f, 85.0f, ScreenElement.RelPoint.CENTER, null, ScreenElement.RelPoint.LEFT_TOP, 0.6f, ColorConstants.PRIMARY_FONT_COLOR, true, true);
            TextElement descriptionLabel = new TextElement(this.node.getDescription(), 0.47f, ColorConstants.PRIMARY_FONT_COLOR, true, 220.0f);
            descriptionLabel.setAnchorPoint(ScreenElement.RelPoint.LEFT_TOP);
            descriptionLabel.setPosition(ScreenElement.RelPoint.CENTER, -60.0f, 60.0f);
            add(descriptionLabel);
        }

        private void addStars() {
            int stars = IslandScreen.this.trainer.getNumberOfStarsWonInNode(this.node);
            int i = 0;
            while (i < 3) {
                String assetName = stars > i ? "island.star" : "island.no.star";
                add(Assets.loadTexture(assetName), (i * 20) + 130, 73.0f, ScreenElement.RelPoint.CENTER);
                i++;
            }
        }

        @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
        public void onUIEvent(UIEvent event) {
            if (event == UIEvent.CANCEL_BUTTON) {
                IslandScreen.this.hideDialog();
            } else if (event == UIEvent.OK_BUTTON) {
                if (IslandScreen.this.trainer.getEnergy() < this.node.getEnergyCost()) {
                    IslandScreen.this.showManageTamerDialog();
                } else {
                    enterNode();
                }
            }
        }

        private void enterNode() {
            IslandScreen.this.trainer.setCurrentNode(this.node);
            IslandScreen.this.trainer.consumeEnergy(this.node.getEnergyCost());
            if (IslandScreen.this.trainer.getCompletedUnpaidQuests().size() > 0) {
                IslandScreen.this.actualQuest = IslandScreen.this.trainer.getCompletedUnpaidQuests().get(0);
                IslandScreen.this.trainer.setPaidQuest(IslandScreen.this.actualQuest);
                IslandScreen.this.actualQuest = IslandScreen.this.trainer.getNextQuest(IslandScreen.this.actualQuest.getId());
            }
            IslandScreen.this.flagNextScreen = true;
            IslandScreen.this.hideDialog();
            Vector2 nodeScreenPos = IslandScreen.this.toScreenCoords(this.node);
            IslandScreen.this.trainerLocation.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, nodeScreenPos.x, nodeScreenPos.y);
            IslandScreen.this.trainerPortrait.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, nodeScreenPos.x + 2.0f, nodeScreenPos.y + 32.0f);
        }

        @Override // com.gaiaonline.monstergalaxy.screen.ScreenGroup, com.gaiaonline.monstergalaxy.screen.ScreenElement
        public void dispose() {
            if (Game.getPlatformHelper().isAndroid()) {
                Game.getAndroidPlatformHelper().removeTextView();
            }
        }
    }
}
