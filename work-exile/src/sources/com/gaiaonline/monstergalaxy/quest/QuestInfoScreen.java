package com.gaiaonline.monstergalaxy.quest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ColorConstants;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.MonsterGalaxy;
import com.gaiaonline.monstergalaxy.app.Screen;
import com.gaiaonline.monstergalaxy.app.ScreenCode;
import com.gaiaonline.monstergalaxy.model.quest.Quest;
import com.gaiaonline.monstergalaxy.model.quest.QuestDialog;
import com.gaiaonline.monstergalaxy.model.quest.RewardItem;
import com.gaiaonline.monstergalaxy.screen.ButtonElement;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.ScreenListener;
import com.gaiaonline.monstergalaxy.screen.TextElement;
import com.gaiaonline.monstergalaxy.screen.UIEvent;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class QuestInfoScreen extends Screen implements ScreenListener {
    private Quest quest;

    public QuestInfoScreen(Quest quest) {
        this.quest = quest;
        this.stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        this.root.addBackground(Assets.loadTexture("questbook.bg"));
        this.root.add(Assets.loadTexture("story.bg"), 10.0f, -10.0f, ScreenElement.RelPoint.LEFT_TOP, ScreenElement.RelPoint.LEFT_TOP);
        ButtonElement closeButton = new ButtonElement(new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.quest.QuestInfoScreen.1
            @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
            public void onUIEvent(UIEvent event) {
                if (Game.getPlatformHelper().isAndroid()) {
                    Game.getAndroidPlatformHelper().removeTextView();
                }
                MonsterGalaxy.showScreen(ScreenCode.QUEST);
            }
        }, null, Assets.closeBtn, Assets.closeBtnPress);
        closeButton.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        closeButton.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, 453.0f, 293.0f);
        this.root.add(closeButton);
        if (Game.getPlatformHelper().isAndroid()) {
            Game.getAndroidPlatformHelper().addTextView(Input.Keys.F2, 27, 65, null, ColorConstants.getColor("quest_color_01"));
        }
        addQuestTitle();
        addReward();
        addQuestStory();
    }

    private void addQuestTitle() {
        this.root.add(Assets.loadTexture("quest.icon"), 40.0f, -35.0f, ScreenElement.RelPoint.LEFT_TOP, ScreenElement.RelPoint.CENTER);
        int textLongitude = this.quest.getName().length();
        float fontScale = 0.75f;
        if (textLongitude > 24) {
            fontScale = (float) (((double) 0.75f) - (0.005d * ((double) textLongitude)));
        }
        this.root.addLabel(this.quest.getName(), 65.0f, -20.0f, ScreenElement.RelPoint.LEFT_TOP, null, ScreenElement.RelPoint.LEFT_TOP, fontScale, ColorConstants.getColor("quest_color_02"), true, false);
    }

    private void addQuestStory() {
        this.root.add(Assets.loadTexture(this.quest.getNpcAsset()), 0.0f, 0.0f, ScreenElement.RelPoint.RIGHT_BOTTOM, ScreenElement.RelPoint.RIGHT_BOTTOM);
        QuestDialog desc = this.quest.getDescriptionDialog();
        String text = "";
        desc.moveFirst();
        while (desc.hasNext()) {
            text = String.valueOf(text) + desc.nextLine().getText();
        }
        if (Game.getPlatformHelper().isAndroid()) {
            String value = text;
            Game.getAndroidPlatformHelper().setTextViewText(value);
        } else {
            TextElement speakerText = new TextElement(text, 0.6f, ColorConstants.getColor("quest_color_01"), true, 260.0f);
            speakerText.setAnchorPoint(ScreenElement.RelPoint.LEFT_TOP);
            speakerText.setPosition(ScreenElement.RelPoint.LEFT_TOP, 25.0f, -100.0f);
            this.root.add(speakerText);
        }
    }

    private void addReward() {
        this.root.add(Assets.loadTexture("rewards.bg"), 20.0f, -205.0f, ScreenElement.RelPoint.LEFT_TOP, ScreenElement.RelPoint.LEFT_TOP);
        this.root.addLabel("Rewards:", 60.0f, -225.0f, ScreenElement.RelPoint.LEFT_TOP, null, ScreenElement.RelPoint.CENTER, 0.5f, ColorConstants.getColor("quest_color_01"), true, false);
        RewardItem rewardItem = this.quest.getRewardItem();
        if (rewardItem.getType() == RewardItem.Type.REWARD_BLUE_COFFEE) {
            this.root.add(Assets.loadTexture("blue.coffee.icon"), 155.0f, -224.0f, ScreenElement.RelPoint.LEFT_TOP, ScreenElement.RelPoint.CENTER);
        } else {
            this.root.add(Assets.loadTexture("capture.starseed.icon"), 155.0f, -224.0f, ScreenElement.RelPoint.LEFT_TOP, ScreenElement.RelPoint.CENTER);
        }
        this.root.addLabel("x" + rewardItem.getCount(), 170.0f, -220.0f, ScreenElement.RelPoint.LEFT_TOP, null, ScreenElement.RelPoint.LEFT_TOP, 0.6f, ColorConstants.getColor("quest_color_03"), false, false);
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
    public void onUIEvent(UIEvent event) {
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void present(float deltaTime) {
        super.present(deltaTime);
    }
}
