package com.gaiaonline.monstergalaxy.quest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ColorConstants;
import com.gaiaonline.monstergalaxy.app.Constants;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.MonsterGalaxy;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.app.Screen;
import com.gaiaonline.monstergalaxy.app.SoundManager;
import com.gaiaonline.monstergalaxy.graphics.util.ScrollInputProcessor;
import com.gaiaonline.monstergalaxy.model.quest.QuestProgress;
import com.gaiaonline.monstergalaxy.platform.PlatformHelper;
import com.gaiaonline.monstergalaxy.screen.ButtonElement;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.ScreenGroup;
import com.gaiaonline.monstergalaxy.screen.ScreenListener;
import com.gaiaonline.monstergalaxy.screen.TableElement;
import com.gaiaonline.monstergalaxy.screen.TextElement;
import com.gaiaonline.monstergalaxy.screen.TextureElement;
import com.gaiaonline.monstergalaxy.screen.TouchReleasedListener;
import com.gaiaonline.monstergalaxy.screen.UIEvent;
import java.util.List;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class QuestScreen extends Screen {
    private static final float CELL_HEIGHT = 60.0f;
    private static final float SCROLL_FRAME_HEIGHT = 236.0f;
    private static final float SCROLL_FRAME_WIDTH = 452.4f;
    private QuestTable questTable;
    private ScrollInputProcessor scroller = new ScrollInputProcessor();
    private Color colorRow1 = ColorConstants.getColor("quest_color_04");
    private Color colorRow2 = ColorConstants.getColor("quest_color_05");
    private Color colorQuestName = ColorConstants.getColor("quest_color_06");
    private Color colorQuestProgess = ColorConstants.getColor("quest_color_07");

    class QuestTable extends TableElement<QuestProgress> {
        public QuestTable(List<QuestProgress> items) {
            super(QuestScreen.SCROLL_FRAME_WIDTH, QuestScreen.SCROLL_FRAME_HEIGHT, QuestScreen.CELL_HEIGHT, ColorConstants.getColor("quest_color_08"), items);
        }

        @Override // com.gaiaonline.monstergalaxy.screen.TableElement
        public void makeRow(TableElement<QuestProgress>.TableRow tableRow, QuestProgress item, int index) {
            final QuestProgress aQuestProgress = (QuestProgress) tableRow.getItem();
            if (index % 2 == 0) {
                tableRow.addBackground(QuestScreen.this.colorRow1);
            } else {
                tableRow.addBackground(QuestScreen.this.colorRow2);
            }
            String text = item.getQuest().getName();
            String progress = String.valueOf(item.getCompletedTasks()) + "/" + item.getRequiredTasks();
            ScreenElement icon = new TextureElement(Assets.loadTexture("quest.icon"));
            icon.setAnchorPoint(ScreenElement.RelPoint.LEFT_CENTER);
            icon.setPosition(ScreenElement.RelPoint.LEFT_CENTER, 15.0f, 0.0f);
            tableRow.add(icon);
            ScreenElement titleElement = new TextElement(text, 0.65f, QuestScreen.this.colorQuestName, true);
            titleElement.setAnchorPoint(ScreenElement.RelPoint.LEFT_CENTER);
            titleElement.setPosition(ScreenElement.RelPoint.LEFT_CENTER, 70.0f, 0.0f);
            titleElement.setScale(0.8f);
            tableRow.add(titleElement);
            ScreenElement progressElement = new TextElement(progress, 0.65f, QuestScreen.this.colorQuestProgess, true);
            progressElement.setAnchorPoint(ScreenElement.RelPoint.LEFT_CENTER);
            progressElement.setPosition(ScreenElement.RelPoint.LEFT_CENTER, 285.0f, 0.0f);
            progressElement.setScale(0.8f);
            tableRow.add(progressElement);
            TextureRegion greenBtn = Assets.loadTexture("green.btn");
            ScreenGroup buttonGroup = new ScreenGroup();
            buttonGroup.setAnchorPoint(ScreenElement.RelPoint.RIGHT_CENTER);
            buttonGroup.setPosition(ScreenElement.RelPoint.RIGHT_CENTER, -35.0f, -5.0f);
            buttonGroup.setSize(greenBtn.getRegionHeight(), greenBtn.getRegionWidth());
            ButtonElement buyButton = new ButtonElement(null, null, greenBtn, Assets.loadTexture("green.btn.press"));
            buyButton.setScale(0.7f);
            buyButton.setAnchorPoint(ScreenElement.RelPoint.CENTER);
            buyButton.setPosition(ScreenElement.RelPoint.CENTER, 0.0f, 0.0f);
            buyButton.setTouchReleasedListener(new TouchReleasedListener() { // from class: com.gaiaonline.monstergalaxy.quest.QuestScreen.QuestTable.1
                @Override // com.gaiaonline.monstergalaxy.screen.TouchReleasedListener
                public void touchReleased(Vector2 touchPoint, Vector2 releasePoint) {
                    MonsterGalaxy.showScreen(new QuestInfoScreen(aQuestProgress.getQuest()));
                }
            });
            buttonGroup.add(buyButton);
            TextElement infoLabel = new TextElement("Info", 0.75f, ColorConstants.PRIMARY_FONT_COLOR, false);
            infoLabel.setAnchorPoint(ScreenElement.RelPoint.CENTER);
            infoLabel.setPosition(ScreenElement.RelPoint.CENTER, 0.0f, 5.0f);
            buttonGroup.add(infoLabel);
            tableRow.add(buttonGroup);
        }

        @Override // com.gaiaonline.monstergalaxy.screen.TableElement
        public void makeEmptyListRow(TableElement<QuestProgress>.TableRow row) {
            TextElement titleElement = new TextElement("You have no quests right now.", 0.65f, ColorConstants.FIFTH_FONT_COLOR, true);
            titleElement.setAnchorPoint(ScreenElement.RelPoint.LEFT_CENTER);
            titleElement.setPosition(ScreenElement.RelPoint.LEFT_CENTER, 100.0f, -100.0f);
            row.add(titleElement);
        }
    }

    public QuestScreen() {
        TextureRegion questListBgRegion = Assets.loadTexture("quest.frame.bg");
        if (questListBgRegion != null) {
            TextureElement questListBg = this.root.add(questListBgRegion, 0.4f, -10.0f, ScreenElement.RelPoint.CENTER, ScreenElement.RelPoint.CENTER);
            questListBg.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
        this.questTable = new QuestTable(MonsterGalaxy.getTrainer().getQuestsProgressions());
        this.questTable.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        this.questTable.setPosition(ScreenElement.RelPoint.CENTER, 0.4f, -10.0f);
        Gdx.input.setInputProcessor(this.scroller);
        this.root.add(this.questTable);
        TextureElement bg = this.root.add(Assets.loadTexture("questlist.bg"), 0.0f, 0.0f);
        bg.setSize(Gdx.graphics.getWidth() / ResolutionManager.getScaleFactor(), Gdx.graphics.getHeight() / ResolutionManager.getScaleFactor());
        ButtonElement closeButton = new ButtonElement(new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.quest.QuestScreen.1
            @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
            public void onUIEvent(UIEvent event) {
                MonsterGalaxy.showPreviousScreen();
            }
        }, null, Assets.closeBtn, Assets.closeBtnPress);
        closeButton.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        closeButton.setPosition(ScreenElement.RelPoint.LEFT_BOTTOM, 453.0f, 293.0f);
        this.root.add(closeButton);
        TextureRegion tittleAsset = Assets.loadTexture("quest.title");
        TextureElement title = new TextureElement(tittleAsset);
        title.setAnchorPoint(ScreenElement.RelPoint.CENTER_TOP);
        title.setPosition(ScreenElement.RelPoint.CENTER_TOP, 0.0f, -12.0f);
        this.root.add(title);
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void update(float deltaTime) {
        float scrollDeltaY = this.scroller.getDeltaY();
        if (scrollDeltaY != 0.0f) {
            this.questTable.setScrollOffset(scrollDeltaY);
        }
        this.scroller.update(deltaTime);
        super.update(deltaTime);
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void onEnter(Screen prevScreen) {
        playMusic();
    }

    private void playMusic() {
        if (Game.getPlatformHelper().getSkin() == PlatformHelper.MGSkin.SK2) {
            SoundManager.playMusic(Constants.worldAmbienceAudio);
        }
    }
}
