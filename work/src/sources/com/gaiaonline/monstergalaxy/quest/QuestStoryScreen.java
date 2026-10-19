package com.gaiaonline.monstergalaxy.quest;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ColorConstants;
import com.gaiaonline.monstergalaxy.app.Constants;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.MonsterGalaxy;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.app.Screen;
import com.gaiaonline.monstergalaxy.app.SoundManager;
import com.gaiaonline.monstergalaxy.battle.effects.Effect;
import com.gaiaonline.monstergalaxy.battle.effects.EffectManager;
import com.gaiaonline.monstergalaxy.battle.effects.EffectType;
import com.gaiaonline.monstergalaxy.battle.tutorial.StarSeedTutorial;
import com.gaiaonline.monstergalaxy.i18n.I18nManager;
import com.gaiaonline.monstergalaxy.model.battle.BattleReport;
import com.gaiaonline.monstergalaxy.model.quest.Quest;
import com.gaiaonline.monstergalaxy.model.quest.QuestDialog;
import com.gaiaonline.monstergalaxy.model.quest.QuestDialogLine;
import com.gaiaonline.monstergalaxy.model.quest.RewardItem;
import com.gaiaonline.monstergalaxy.model.trainer.Trainer;
import com.gaiaonline.monstergalaxy.screen.ButtonElement;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.ScreenListener;
import com.gaiaonline.monstergalaxy.screen.TextElement;
import com.gaiaonline.monstergalaxy.screen.TextureElement;
import com.gaiaonline.monstergalaxy.screen.UIEvent;
import twitter4j.internal.http.HttpResponseCode;

/* JADX INFO: loaded from: classes.dex */
public class QuestStoryScreen extends Screen implements ScreenListener {
    private ButtonElement acceptButton;
    private TextElement acceptButtonText;
    private ButtonElement backButton;
    private Color backButtonDisabledColor = ColorConstants.PRIMARY_GRAYED_COLOR;
    private QuestDialog detailsDialog;
    private boolean flowToIslandScreen;
    private boolean isQuestCompleted;
    private TextureElement nextArrow;
    private ButtonElement nextButton;
    private TextElement nextButtonText;
    private Quest quest;
    private TextureElement questCompleted;
    private TextureElement questCompletedIcon;
    private TextElement questCompletedText;
    private TextureElement questIcon;
    private TextElement questName;
    private TextureElement rewardBg;
    private TextElement rewardNumber;
    private TextElement rewardText;
    private TextureElement rewardType;
    private ButtonElement skipButton;
    private TextElement skipButtonText;
    private TextElement speakerName;
    private TextureElement speakerPicture;
    private TextElement speakerText;
    private TextureElement storyBackground;
    private QuestDialog storyDialog;
    private String textLine;
    private Trainer trainer;

    public QuestStoryScreen(Quest quest) {
        this.quest = quest;
    }

    private void setupScreen() {
        SoundManager.loadSound("ui_story_quest_complete");
        SoundManager.loadSound(SoundManager.SOUND_UI_TAP);
        SoundManager.loadSound("ui_confirm_dialogue");
        this.trainer = Game.getTrainer();
        initStory(this.quest);
        this.root.addBackground(this.trainer.getCurrentNode().getBackgroundAsset());
        this.storyBackground = this.root.add(Assets.loadTexture("story.bg"), 10.0f, -10.0f, ScreenElement.RelPoint.LEFT_TOP, ScreenElement.RelPoint.LEFT_TOP);
        addQuestTitle();
        addQuestStory();
        addReward();
        addQuestCompleted();
        addButtons();
        updateQuestTitle();
        updateRewards();
    }

    private void initStory(Quest quest) {
        this.quest = quest;
        this.isQuestCompleted = this.trainer.isQuestCompleted(quest.getId());
        if (this.isQuestCompleted) {
            this.storyDialog = quest.getCompleteDialog();
            this.storyDialog.moveFirst();
            this.detailsDialog = new QuestDialog();
            this.detailsDialog.addLine(new QuestDialogLine("", quest.getCompletedText()));
            return;
        }
        this.storyDialog = quest.getStartDialog();
        this.detailsDialog = quest.getDescriptionDialog();
    }

    private void addQuestTitle() {
        this.questIcon = this.root.add(Assets.loadTexture("quest.icon"), 40.0f, -35.0f, ScreenElement.RelPoint.LEFT_TOP, ScreenElement.RelPoint.CENTER);
        int textLongitude = this.quest.getName().length();
        float fontScale = 0.75f;
        if (textLongitude > 24) {
            fontScale = (float) (((double) 0.75f) - (0.005d * ((double) textLongitude)));
        }
        this.questName = this.root.addLabel(this.quest.getName(), 60.0f, -20.0f, ScreenElement.RelPoint.LEFT_TOP, null, ScreenElement.RelPoint.LEFT_TOP, fontScale, ColorConstants.getColor("quest_color_02"), true, false);
        this.questIcon.setVisible(false);
        this.questName.setVisible(false);
    }

    private void updateQuestTitle() {
        int textLongitude = this.quest.getName().length();
        float fontScale = 0.75f;
        if (textLongitude > 24) {
            fontScale = (float) (((double) 0.75f) - (0.005d * ((double) textLongitude)));
        }
        this.questName.setFontScale(fontScale);
        this.questName.setText(this.quest.getName());
    }

    private void addQuestStory() {
        this.speakerName = this.root.addLabel("", 25.0f, -30.0f, ScreenElement.RelPoint.LEFT_TOP, null, ScreenElement.RelPoint.LEFT_TOP, 0.8f, ColorConstants.getColor("quest_color_01"), true, false);
        this.speakerText = new TextElement("", 0.6f, ColorConstants.getColor("quest_color_01"), true, 260.0f);
        this.speakerText.setAnchorPoint(ScreenElement.RelPoint.LEFT_TOP);
        this.speakerText.setPosition(ScreenElement.RelPoint.LEFT_TOP, 25.0f, -70.0f);
        this.root.add(this.speakerText);
        this.speakerPicture = this.root.add(Assets.loadTexture("quest.icon"), 0.0f, 0.0f, ScreenElement.RelPoint.LEFT_TOP);
        setQuestStoryText(this.storyDialog.nextLine());
    }

    private void addButtons() {
        TextureRegion normalButton = Assets.loadTexture("blue.btn.small");
        TextureRegion selectedButton = Assets.loadTexture("blue.btn.small.press");
        this.backButton = new ButtonElement(this, UIEvent.BACK_BUTTON, normalButton, selectedButton);
        this.backButton.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        this.backButton.setPosition(ScreenElement.RelPoint.LEFT_TOP, 75.0f, -284.0f);
        this.backButton.setEnabled(false);
        this.root.add(this.backButton);
        TextElement backLabel = this.root.addLabel(I18nManager.getText(I18nManager.I18nKey.BACK), 84.0f, -281.0f, ScreenElement.RelPoint.LEFT_TOP, 0.7f, ColorConstants.PRIMARY_FONT_COLOR, false);
        this.nextButton = new ButtonElement(this, UIEvent.NEXT_BUTTON, normalButton, selectedButton);
        this.backButton.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        this.nextButton.setPosition(ScreenElement.RelPoint.LEFT_TOP, 200.0f, -284.0f);
        this.root.add(this.nextButton);
        this.nextButtonText = this.root.addLabel(I18nManager.getText(I18nManager.I18nKey.NEXT), 189.0f, -281.0f, ScreenElement.RelPoint.LEFT_TOP, 0.7f, ColorConstants.PRIMARY_FONT_COLOR, false);
        this.acceptButton = new ButtonElement(this, UIEvent.ACCEPT_BUTTON, Assets.loadTexture("green.btn.small"), Assets.loadTexture("green.btn.small.press"));
        this.acceptButton.setPosition(ScreenElement.RelPoint.LEFT_TOP, 200.0f, -284.0f);
        this.root.add(this.acceptButton);
        this.acceptButtonText = new TextElement(I18nManager.getText(I18nManager.I18nKey.ACCEPT), 0.7f, ColorConstants.PRIMARY_FONT_COLOR, false);
        this.acceptButtonText.setAlign(this.acceptButton);
        this.acceptButtonText.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        this.acceptButtonText.setPosition(ScreenElement.RelPoint.CENTER, 0.0f, 4.0f);
        this.root.add(this.acceptButtonText);
        this.acceptButton.setVisible(false);
        this.acceptButtonText.setVisible(false);
        backLabel.enableHorizontalAutoSize(58.0f);
        this.nextButtonText.enableHorizontalAutoSize(58.0f);
        this.acceptButtonText.enableHorizontalAutoSize(80.0f);
        this.skipButton = new ButtonElement(this, UIEvent.SKIP_BUTTON, Assets.loadTexture("red.btn.small"), Assets.loadTexture("red.btn.small.press"));
        this.skipButton.setPosition(ScreenElement.RelPoint.LEFT_TOP, 405.0f, -40.0f);
        this.root.add(this.skipButton);
        this.skipButtonText = this.root.addLabel(I18nManager.getText(I18nManager.I18nKey.SKIP), 405.0f, -35.0f, ScreenElement.RelPoint.LEFT_TOP, 0.7f, ColorConstants.PRIMARY_FONT_COLOR, false);
        this.skipButtonText.enableHorizontalAutoSize(100.0f);
        TextureRegion backWhiteArrow = Assets.loadTexture("white.arrow");
        int arrowHeight = backWhiteArrow.getRegionHeight();
        int arrowWidth = backWhiteArrow.getRegionWidth();
        this.root.add(backWhiteArrow, 45.0f, -281.0f, ScreenElement.RelPoint.LEFT_TOP);
        TextureRegion nextWhiteArrow = new TextureRegion(Assets.loadTexture("white.arrow"));
        nextWhiteArrow.flip(true, false);
        this.nextArrow = this.root.add(nextWhiteArrow, 226.0f, -281.0f, arrowHeight, arrowWidth, ScreenElement.RelPoint.LEFT_TOP, null, ScreenElement.RelPoint.CENTER);
        this.backButton.setColor(this.backButtonDisabledColor);
    }

    private void addReward() {
        this.rewardBg = this.root.add(Assets.loadTexture("rewards.bg"), 20.0f, -205.0f, ScreenElement.RelPoint.LEFT_TOP, ScreenElement.RelPoint.LEFT_TOP);
        this.rewardText = new TextElement("", 0.65f, ColorConstants.getColor("quest_color_01"), true);
        this.rewardText.enableHorizontalAutoSize(120.0f);
        this.rewardText.setAnchorPoint(ScreenElement.RelPoint.LEFT_CENTER);
        this.rewardText.setPosition(ScreenElement.RelPoint.LEFT_TOP, 30.0f, -225.0f);
        this.root.add(this.rewardText);
        this.rewardType = this.root.add(Assets.loadTexture("blue.coffee.icon"), 155.0f, -224.0f, ScreenElement.RelPoint.LEFT_TOP, ScreenElement.RelPoint.CENTER);
        this.rewardNumber = this.root.addLabel("x", 170.0f, -220.0f, ScreenElement.RelPoint.LEFT_TOP, null, ScreenElement.RelPoint.LEFT_TOP, 0.6f, ColorConstants.getColor("quest_color_03"), false, false);
        this.rewardBg.setVisible(false);
        this.rewardText.setVisible(false);
        this.rewardType.setVisible(false);
        this.rewardNumber.setVisible(false);
    }

    private void addQuestCompleted() {
        float xPos = 175.0f - ((ResolutionManager.getScaleFactor() / ResolutionManager.getHorizontalAR()) * 175.0f);
        this.questCompleted = this.root.add(Assets.loadLocalizedTexture("quest.complete"), 175.0f - xPos, -58.0f, ScreenElement.RelPoint.LEFT_TOP);
        this.questCompletedIcon = this.root.add(Assets.loadTexture("quest.icon"), 175.0f - xPos, -90.0f, ScreenElement.RelPoint.LEFT_TOP);
        this.questCompletedText = new TextElement("", 0.6f, ColorConstants.getColor("quest_color_01"), true, 260.0f);
        this.questCompletedText.setAnchorPoint(ScreenElement.RelPoint.LEFT_TOP);
        this.questCompletedText.setPosition(ScreenElement.RelPoint.LEFT_TOP, 25.0f, -120.0f);
        this.questCompletedText.setTextAlignment(BitmapFont.HAlignment.CENTER);
        this.root.add(this.questCompletedText);
        this.questCompleted.setVisible(false);
        this.questCompletedIcon.setVisible(false);
        this.questCompletedText.setVisible(false);
    }

    private void setQuestStoryText(QuestDialogLine line) {
        String speaker = line.getSpeaker();
        if (Game.getPlatformHelper().isAndroid()) {
            this.speakerText.setText(" ");
            setTextLine(line.getText());
        } else {
            this.speakerText.setText(line.getText());
        }
        if (speaker.equalsIgnoreCase("player")) {
            this.speakerName.setText(this.trainer.getName());
        } else {
            this.speakerName.setText(speaker);
        }
        updateNPCAsset(speaker);
    }

    private void updateRewards() {
        String rewardTextValue;
        if (this.isQuestCompleted) {
            rewardTextValue = I18nManager.getText(I18nManager.I18nKey.YOU_GOT_COLON);
        } else {
            rewardTextValue = I18nManager.getText(I18nManager.I18nKey.REWARDS_COLON);
        }
        this.rewardText.setText(rewardTextValue);
        RewardItem rewardItem = this.quest.getRewardItem();
        if (rewardItem.getType() == RewardItem.Type.REWARD_BLUE_COFFEE) {
            this.rewardType.setTexture(Assets.loadTexture("blue.coffee.icon"));
        } else {
            this.rewardType.setTexture(Assets.loadTexture("capture.starseed.icon"));
        }
        this.rewardNumber.setText("x" + rewardItem.getCount());
    }

    private void updateNPCAsset(String speaker) {
        String speakerAsset;
        if (speaker.equalsIgnoreCase("player")) {
            speakerAsset = this.trainer.getAssetName();
        } else {
            speakerAsset = this.quest.getNpcAsset();
        }
        TextureRegion texture = Assets.loadTexture(speakerAsset);
        float posX = (texture.getRegionWidth() / 2) + HttpResponseCode.MULTIPLE_CHOICES + Constants.getXOffsetForNPCWithName(speakerAsset);
        float posY = Constants.getYOffsetForNPCWithName(speakerAsset) - 195;
        this.speakerPicture.setPosition(ScreenElement.RelPoint.LEFT_TOP, posX, posY);
        this.speakerPicture.setTexture(texture);
    }

    private void showDetails() {
        this.storyDialog.moveLast();
        this.detailsDialog.moveFirst();
        String text = this.detailsDialog.nextLine().getText();
        if (this.isQuestCompleted) {
            Effect blueStarsEffect = EffectManager.getInstance().getEffect(EffectType.BLUE_STARS).getCopy();
            this.storyBackground.playEffect(blueStarsEffect, ScreenElement.RelPoint.CENTER);
            this.questCompletedText.setText(text);
            if (Game.getPlatformHelper().isAndroid()) {
                setTextLine("");
            }
            SoundManager.playSound(SoundManager.SOUND_MOGA_CASH);
            return;
        }
        if (Game.getPlatformHelper().isAndroid()) {
            setTextLine(text);
        } else {
            this.speakerText.setText(text);
        }
    }

    private void setTextLine(String textLine) {
        this.textLine = textLine;
        if (Game.getPlatformHelper().isAndroid()) {
            Game.getAndroidPlatformHelper().setTextViewText(textLine);
        }
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void onBackKeyPressed() {
        if (this.backButton.isVisible() && this.backButton.isEnabled()) {
            onUIEvent(UIEvent.BACK_BUTTON);
        }
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
    public void onUIEvent(UIEvent event) {
        if (event == UIEvent.NEXT_BUTTON) {
            SoundManager.playSound(SoundManager.SOUND_UI_TAP);
            if (this.storyDialog.hasNext()) {
                setQuestStoryText(this.storyDialog.nextLine());
            } else if (this.detailsDialog.hasNext()) {
                showDetails();
            }
        } else if (event == UIEvent.BACK_BUTTON) {
            SoundManager.playSound(SoundManager.SOUND_UI_TAP);
            this.detailsDialog.moveFirst();
            if (this.storyDialog.hasPrevious()) {
                setQuestStoryText(this.storyDialog.previousLine());
            }
        } else if (event == UIEvent.SKIP_BUTTON) {
            SoundManager.playSound(SoundManager.SOUND_UI_TAP);
            showDetails();
        } else if (event == UIEvent.ACCEPT_BUTTON) {
            SoundManager.playSound("ui_confirm_dialogue");
            if (this.isQuestCompleted) {
                this.trainer.setPaidQuest(this.quest);
                Quest nextQuest = this.trainer.getNextQuest(this.quest.getId());
                if (nextQuest != null) {
                    initStory(nextQuest);
                    updateQuestTitle();
                    updateRewards();
                    if (this.storyDialog.hasNext()) {
                        setQuestStoryText(this.storyDialog.nextLine());
                    }
                    this.flowToIslandScreen = true;
                } else {
                    BattleReport report = this.trainer.getBattleReport();
                    if (report != null && report.hasUnlockedNextIsland()) {
                        MonsterGalaxy.backToWorldFromBattleFlow(this.trainer.getCurrentIsland().getNextIsland().getId());
                    } else {
                        MonsterGalaxy.backToIslandFromBattleFlow();
                    }
                }
            } else {
                this.trainer.startQuest(this.quest);
                BattleReport report2 = this.trainer.getBattleReport();
                if (this.trainer.getCurrentNode().getEncounter().getId() == 456) {
                    MonsterGalaxy.showScreen(new StarSeedTutorial());
                } else if (report2 != null && report2.hasUnlockedNextIsland()) {
                    MonsterGalaxy.backToWorldFromBattleFlow(this.trainer.getCurrentIsland().getNextIsland().getId());
                } else if (this.flowToIslandScreen) {
                    MonsterGalaxy.backToIslandFromBattleFlow();
                } else {
                    MonsterGalaxy.showBattleScreen();
                }
            }
        }
        boolean hasNext = this.storyDialog.hasNext() || this.detailsDialog.hasNext();
        this.nextButton.setVisible(hasNext);
        this.nextButtonText.setVisible(hasNext);
        this.nextArrow.setVisible(hasNext);
        this.acceptButton.setVisible(!hasNext);
        this.acceptButtonText.setVisible(!hasNext);
        this.questIcon.setVisible((hasNext || this.isQuestCompleted) ? false : true);
        this.questName.setVisible((hasNext || this.isQuestCompleted) ? false : true);
        this.questCompleted.setVisible(!hasNext && this.isQuestCompleted);
        this.questCompletedIcon.setVisible(!hasNext && this.isQuestCompleted);
        this.questCompletedText.setVisible(!hasNext && this.isQuestCompleted);
        this.speakerName.setVisible(hasNext);
        this.speakerText.setVisible(hasNext || !this.isQuestCompleted);
        this.rewardBg.setVisible(!hasNext);
        this.rewardText.setVisible(!hasNext);
        this.rewardType.setVisible(!hasNext);
        this.rewardNumber.setVisible(hasNext ? false : true);
        this.skipButton.setVisible(hasNext);
        this.skipButtonText.setVisible(hasNext);
        boolean hasPrevious = this.storyDialog.hasPrevious();
        if (hasPrevious) {
            this.backButton.setColor(null);
        } else {
            this.backButton.setColor(this.backButtonDisabledColor);
        }
        this.backButton.setEnabled(hasPrevious);
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void present(float deltaTime) {
        super.present(deltaTime);
        if (getFrameNumberSinceResume() == 1 && Game.getPlatformHelper().isAndroid()) {
            Game.getAndroidPlatformHelper().addTextView(Input.Keys.F2, 27, 63, null, ColorConstants.getColor("quest_color_01"));
            if (this.textLine != null) {
                Game.getAndroidPlatformHelper().setTextViewText(this.textLine);
            }
        }
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void onEnter(Screen prevScreen) {
        setupScreen();
        if (this.quest.getAvailableMusic() != null) {
            SoundManager.playMusic(this.trainer.getCurrentIsland().getStingAudio(), this.quest.getAvailableMusic());
        } else if (this.isQuestCompleted) {
            SoundManager.playSound("ui_story_quest_complete");
        }
        SoundManager.loadSound(SoundManager.SOUND_MOGA_CASH);
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void onResume() {
        this.root.addBackground(this.trainer.getCurrentNode().getBackgroundAsset());
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void onPause() {
        if (Game.getPlatformHelper().isAndroid()) {
            Game.getAndroidPlatformHelper().removeTextView();
        }
    }
}
