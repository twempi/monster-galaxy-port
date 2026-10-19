package com.gaiaonline.monstergalaxy.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.FadeTo;
import com.badlogic.gdx.scenes.scene2d.actions.Forever;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleTo;
import com.badlogic.gdx.scenes.scene2d.actions.Sequence;
import com.badlogic.gdx.scenes.scene2d.actors.Image;
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
import com.gaiaonline.monstergalaxy.assets.AssetsCache;
import com.gaiaonline.monstergalaxy.battle.dialogs.AlertDialog;
import com.gaiaonline.monstergalaxy.battle.tutorial.BlueCoffeeTutorial;
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

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
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
        checkForUpdate();
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

    private void addNodes() {
        TextureRegion nodeTexture;
        for (final Node node : this.trainer.getCurrentIsland().getNodes()) {
            ScreenListener nodeListener = new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.map.IslandScreen.2
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
                ActorScreenElement outlineActorScreenElement = new ActorScreenElement();
                Image outlineImage = new Image("", Assets.loadTexture("island.node.unlocked.outline"));
                outlineImage.color.set(nodeColor);
                outlineImage.scaleX = ResolutionManager.getScaleFactor();
                outlineImage.scaleY = ResolutionManager.getScaleFactor();
                outlineActorScreenElement.setActor(outlineImage);
                outlineActorScreenElement.setAnchorPoint(ScreenElement.RelPoint.CENTER);
                outlineActorScreenElement.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, nodeScreenPos.x, nodeScreenPos.y);
                outlineImage.action(Forever.$(Sequence.$(FadeTo.$(1.0f, 0.5f), FadeTo.$(0.6f, 0.5f))));
                this.root.add(outlineActorScreenElement);
            } else {
                nodeTexture = Assets.loadTexture("island.small.lock");
            }
            ButtonElement nodeElement = new ButtonElement(nodeListener, null, nodeTexture);
            nodeElement.setColor(nodeColor);
            nodeElement.setAnchorPoint(ScreenElement.RelPoint.CENTER);
            nodeElement.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, nodeScreenPos.x, nodeScreenPos.y);
            this.root.add(nodeElement);
            if (node.getId() < 13 && !this.trainer.isNodeCompleted(node.getId()) && !this.trainer.isNodeLocked(node)) {
                this.nodeArrow = addLeftArrow(node, nodeElement);
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
        Island island = this.trainer.getCurrentIsland();
        this.islandScrollGroup = new ScreenGroup();
        this.islandScrollGroup.setStretchable(false);
        this.islandScrollGroup.setSize(289.0f, 71.0f);
        this.islandScrollGroup.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        this.islandScrollGroup.setPosition(ScreenElement.RelPoint.CENTER_BOTTOM, 13.0f, 35.0f);
        this.root.add(this.islandScrollGroup);
        TextureElement scroll = new TextureElement(Assets.loadTexture("island.scroll"));
        scroll.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        scroll.setPosition(ScreenElement.RelPoint.CENTER, 0.0f, 0.0f);
        int textLongitude = island.getName().length();
        float fontScale = 0.7f;
        if (textLongitude > 12) {
            fontScale = (float) (((double) 0.7f) - (0.012d * ((double) textLongitude)));
        }
        TextElement islandNameText = new TextElement(island.getName().toUpperCase(), fontScale, ColorConstants.ISLAND_TEXT_NAME_COLOR, true);
        islandNameText.setAnchorPoint(ScreenElement.RelPoint.LEFT_CENTER);
        islandNameText.setPosition(ScreenElement.RelPoint.CENTER, -102.0f, 8.0f);
        TextElement capturedMogasText = new TextElement("", 0.45f, ColorConstants.ISLAND_TEXT_PROGRESS_COLOR, true);
        capturedMogasText.setAnchorPoint(ScreenElement.RelPoint.LEFT_CENTER);
        capturedMogasText.setPosition(ScreenElement.RelPoint.CENTER, -102.0f, -12.0f);
        capturedMogasText.setText(String.valueOf(this.trainer.getNumberOfCapturesInIsland(island)) + "/" + island.getMaxNumberOfMogas() + " Mogas Captured");
        TextureElement starIcon = new TextureElement(Assets.loadTexture("island.star.icon"));
        starIcon.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        starIcon.setPosition(ScreenElement.RelPoint.CENTER, 48.0f, 9.0f);
        TextElement obtainedStarsText = new TextElement("", 0.5f, ColorConstants.FOURTH_FONT_COLOR, true);
        obtainedStarsText.setTextAlignment(BitmapFont.HAlignment.RIGHT);
        obtainedStarsText.setAnchorPoint(ScreenElement.RelPoint.RIGHT_CENTER);
        obtainedStarsText.setPosition(ScreenElement.RelPoint.CENTER, 105.0f, 9.0f);
        obtainedStarsText.setText(String.valueOf(this.trainer.getNumberOfStarsWonInIsland(island)) + "/" + island.getMaxNumberOfStars());
        TextureElement questIcon = new TextureElement(Assets.loadTexture("island.quest.icon.small"));
        questIcon.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        questIcon.setPosition(ScreenElement.RelPoint.CENTER, 48.0f, -12.0f);
        TextElement questsText = new TextElement("", 0.5f, ColorConstants.FOURTH_FONT_COLOR, true);
        questsText.setTextAlignment(BitmapFont.HAlignment.RIGHT);
        questsText.setAnchorPoint(ScreenElement.RelPoint.RIGHT_CENTER);
        questsText.setPosition(ScreenElement.RelPoint.CENTER, 105.0f, -12.0f);
        questsText.setText(String.valueOf(this.trainer.getNumberOfCompletedQuestsInIsland(island)) + "/" + island.getQuestCount());
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
            this.backToWorldButton = new ButtonElement(new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.map.IslandScreen.3
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
        this.toggleHudButton = new ButtonElement(new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.map.IslandScreen.4
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
        this.toggleHudLabel.setText(this.playerHud.isVisible() ? "Hide" : "Show");
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
            Image attackArrow = getArrowActor("leftArrow", "arrow.right");
            arrowElement.setActor(attackArrow);
            arrowElement.setAnchorPoint(ScreenElement.RelPoint.LEFT_BOTTOM);
            arrowElement.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, -35.0f, -7.0f);
        } else {
            Image attackArrow2 = getArrowActor("leftArrow", "arrow.left");
            arrowElement.setActor(attackArrow2);
            arrowElement.setAnchorPoint(ScreenElement.RelPoint.LEFT_BOTTOM);
            arrowElement.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, 18.0f, -5.0f);
        }
        arrowElement.setAlign(element);
        this.root.add(arrowElement);
        return arrowElement;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Image getArrowActor(String name, String arrowAsset) {
        Image arrow = new Image(name, Assets.loadTexture(arrowAsset));
        ScaleTo scaleUp = ScaleTo.$(1.2f, 1.2f, 0.5f);
        ScaleTo scaleDown = ScaleTo.$(1.0f, 1.0f, 0.5f);
        arrow.action(Forever.$(Sequence.$(scaleUp, scaleDown)));
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
                pendingCapture = String.valueOf(count) + " more Mogas";
            } else {
                pendingCapture = "1 more Moga";
            }
            switch ($SWITCH_TABLE$com$gaiaonline$monstergalaxy$platform$PlatformHelper$MGSkin()[Game.getPlatformHelper().getSkin().ordinal()]) {
                case 2:
                    message = "You're making great progress in your quest to escape Otho's exile, but you'll need to capture " + pendingCapture + " before you venture to the next island. Good luck!";
                    break;
                default:
                    message = "Congratulations! You are very close on your epic quest to defeat King Otho. You're very close to unlocking the next island but you need to capture " + pendingCapture + " before continuing your journey.";
                    break;
            }
            ScreenListener listener = new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.map.IslandScreen.5
                @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
                public void onUIEvent(UIEvent event) {
                    IslandScreen.this.hideDialog();
                }
            };
            showDialog(new AlertDialog(listener, message, "Keep Playing", UIEvent.OK_BUTTON, UIEvent.CLOSE_BUTTON, true));
        }
    }

    private void showManageTeamDialog() {
        AlertDialog alertDialog;
        ScreenListener listener = new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.map.IslandScreen.6
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
            alertDialog = new AlertDialog(listener, "You don't have any Mogas on your team! Visit your Team and add some.", "Manage Team", UIEvent.OK_BUTTON, UIEvent.CLOSE_BUTTON, true);
        } else {
            alertDialog = new AlertDialog(listener, "Your mogas are tired! Visit your Team to recharge.", "Manage Team", UIEvent.OK_BUTTON, UIEvent.CLOSE_BUTTON, true);
            alertDialog.setWrapWidth(290.0f);
        }
        showDialog(alertDialog);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showManageTamerDialog() {
        ScreenListener listener = new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.map.IslandScreen.7
            @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
            public void onUIEvent(UIEvent event) {
                if (event == UIEvent.OK_BUTTON) {
                    MonsterGalaxy.showScreen(ScreenCode.TAMER);
                } else {
                    IslandScreen.this.hideDialog();
                }
            }
        };
        showDialog(new AlertDialog(listener, "Your tamer is out of Energy! Visit your Tamer to recharge.", "Manage Tamer", UIEvent.OK_BUTTON, UIEvent.CLOSE_BUTTON, true));
    }

    private void showCaptureDialog() {
        ScreenListener listener = new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.map.IslandScreen.8
            @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
            public void onUIEvent(UIEvent event) {
                IslandScreen.this.hideDialog();
            }
        };
        showDialog(new AlertDialog(listener, "Whoa, tamer! You were supposed to CAPTURE that Moga - not beat it senseless. Try again but remember to capture it this time.", "Keep Playing", UIEvent.OK_BUTTON, UIEvent.CLOSE_BUTTON, true));
    }

    private void showFirstScreenTutorial() {
        this.menuArrowElement = new ActorScreenElement();
        Image attackArrow = getArrowActor("rightArrow", "arrow.left");
        attackArrow.region.flip(true, false);
        this.menuArrowElement.setActorDeprecated(attackArrow);
        this.menuArrowElement.setAnchorPoint(ScreenElement.RelPoint.LEFT_BOTTOM);
        this.menuArrowElement.setPosition(ScreenElement.RelPoint.CENTER_BOTTOM, 142.0f, 8.0f);
        this.root.add(this.menuArrowElement);
        ScreenListener listener = new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.map.IslandScreen.9
            @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
            public void onUIEvent(UIEvent event) {
                IslandScreen.this.hideDialog();
                if (event == UIEvent.OK_BUTTON) {
                    IslandScreen.this.showSecondScreenTutorial();
                }
                IslandScreen.this.menuArrowElement.setVisible(false);
            }
        };
        AlertDialog dialog = new AlertDialog(listener, "You can use the menu to view current quests, buy Blue Coffee and Starseeds in the shop, manage your tamer, and review the game settings.", "More", UIEvent.OK_BUTTON, UIEvent.CLOSE_BUTTON, false);
        showDialog(dialog);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showSecondScreenTutorial() {
        ScreenListener listener = new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.map.IslandScreen.10
            @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
            public void onUIEvent(UIEvent event) {
                IslandScreen.this.hideDialog();
            }
        };
        Dialog alertDialog = new AlertDialog(listener, "You can see the status of your current team on the top-left part of the screen. You can also visit the team screen by tapping the team button where you can view and manage all of your Mogas.", "Keep Playing", UIEvent.OK_BUTTON, UIEvent.CLOSE_BUTTON, false);
        ActorScreenElement arrowElement = new ActorScreenElement();
        Image arrow = getArrowActor("upArrow", "down.arrow");
        arrow.region.flip(false, true);
        arrowElement.setActorDeprecated(arrow);
        arrowElement.setAnchorPoint(ScreenElement.RelPoint.LEFT_CENTER);
        arrowElement.setPosition(ScreenElement.RelPoint.LEFT_TOP, 20.0f, -75.0f);
        alertDialog.add(arrowElement);
        showDialog(alertDialog);
    }

    private void showThirdScreenTutorial() {
        ScreenListener listener = new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.map.IslandScreen.11
            @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
            public void onUIEvent(UIEvent event) {
                IslandScreen.this.hideDialog();
            }
        };
        AlertDialog dialog = new AlertDialog(listener, "Congratulations, Tamer. You have successfully assembled a team. The guidance stops here. You are now on your own - good luck!", "Keep Playing", UIEvent.OK_BUTTON, UIEvent.CLOSE_BUTTON, false);
        showDialog(dialog);
    }

    private void showPlayingOrderDialog() {
        ScreenListener listener = new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.map.IslandScreen.12
            @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
            public void onUIEvent(UIEvent event) {
                IslandScreen.this.hideDialog();
            }
        };
        AlertDialog dialog = new AlertDialog(listener, "Hi, Tamer. You must visit the location near the arrow before revisiting other locations.", "Keep Playing", UIEvent.OK_BUTTON, UIEvent.CLOSE_BUTTON, false);
        showDialog(dialog);
    }

    private void showSlotMachineDialog() {
        ScreenListener listener = new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.map.IslandScreen.13
            @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
            public void onUIEvent(UIEvent event) {
                if (event == UIEvent.OK_BUTTON) {
                    MonsterGalaxy.showScreen(ScreenCode.SLOTMACHINE);
                } else {
                    IslandScreen.this.hideDialog();
                }
            }
        };
        showDialog(new AlertDialog(listener, "Hey, Tamer! There are awesome, secret Mogas you can win if you're lucky. Try your hand at Lucky Moga!", "Get Lucky", UIEvent.OK_BUTTON, UIEvent.CLOSE_BUTTON, true));
    }

    private void showRateGameDialog() {
        ScreenListener listener = new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.map.IslandScreen.14
            @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
            public void onUIEvent(UIEvent event) {
                if (event == UIEvent.CANCEL_BUTTON) {
                    IslandScreen.this.showDialog(new AlertDialog(this, 5, "Thank you for your feedback.", UIEvent.CLOSE_BUTTON));
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
            ScreenListener listener = new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.map.IslandScreen.15
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
            addLabel("Cancel", -100.0f, -55.0f, ScreenElement.RelPoint.CENTER, 0.6f, ColorConstants.PRIMARY_FONT_COLOR, true);
            TextureRegion normalButton2 = Assets.loadTexture("green.btn.large");
            TextureRegion selectedButton2 = Assets.loadTexture("green.btn.large.press");
            ButtonElement playButton = addButton(UIEvent.OK_BUTTON, normalButton2, selectedButton2, 70.0f, -60.0f, ScreenElement.RelPoint.CENTER);
            addLabel("Play    " + this.node.getEnergyCost(), 70.0f, -55.0f, ScreenElement.RelPoint.CENTER, 0.6f, ColorConstants.PRIMARY_FONT_COLOR, true);
            if (this.node.getId() == 2 && !IslandScreen.this.trainer.isNodeCompleted(this.node.getId())) {
                ActorScreenElement arrow = new ActorScreenElement();
                arrow.setActor(IslandScreen.this.getArrowActor("arrow", "arrow.left"));
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
            super.dispose();
            if (Game.getPlatformHelper().isAndroid()) {
                Game.getAndroidPlatformHelper().removeTextView();
            }
            IslandScreen.this.stage.dispose();
        }
    }
}
