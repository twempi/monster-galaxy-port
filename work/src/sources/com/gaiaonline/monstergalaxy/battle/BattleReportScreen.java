package com.gaiaonline.monstergalaxy.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.gaiaonline.monstergalaxy.analytics.Analytics;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ColorConstants;
import com.gaiaonline.monstergalaxy.app.Constants;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.MonsterGalaxy;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.app.Screen;
import com.gaiaonline.monstergalaxy.app.SoundManager;
import com.gaiaonline.monstergalaxy.battle.dialogs.AlertDialog;
import com.gaiaonline.monstergalaxy.battle.effects.EffectManager;
import com.gaiaonline.monstergalaxy.battle.effects.EffectType;
import com.gaiaonline.monstergalaxy.graphics.util.ActorUtil;
import com.gaiaonline.monstergalaxy.i18n.I18nManager;
import com.gaiaonline.monstergalaxy.model.battle.BattleReport;
import com.gaiaonline.monstergalaxy.model.battle.MogaChangesReport;
import com.gaiaonline.monstergalaxy.model.moga.Moga;
import com.gaiaonline.monstergalaxy.model.quest.Quest;
import com.gaiaonline.monstergalaxy.model.trainer.Team;
import com.gaiaonline.monstergalaxy.quest.QuestStoryScreen;
import com.gaiaonline.monstergalaxy.screen.AutoFontLabel;
import com.gaiaonline.monstergalaxy.screen.ButtonElement;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.ScreenListener;
import com.gaiaonline.monstergalaxy.screen.ShadowTextElement;
import com.gaiaonline.monstergalaxy.screen.TextElement;
import com.gaiaonline.monstergalaxy.screen.TextureElement;
import com.gaiaonline.monstergalaxy.screen.UIEvent;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class BattleReportScreen extends Screen implements ScreenListener {
    private static final float MOGA_PORTRAIT0_X = 210.0f;
    private static final float MOGA_PORTRAIT_WIDTH = 110.0f;
    private BattleReport battleReport;
    private List<Quest> completedUnpaidQuests = Game.getTrainer().getCompletedUnpaidQuests();
    private int maxlevel;
    private float scaleFactor;
    private float scaleFactorX;

    public BattleReportScreen() {
        Image star1;
        Image star2;
        Image star3;
        final LevelBar aImage1;
        this.scaleFactor = 1.0f;
        this.scaleFactorX = 1.0f;
        SoundManager.loadSound(SoundManager.SOUND_LEVELUP);
        SoundManager.loadSound(SoundManager.SOUND_BLUE_COFFEE);
        this.scaleFactor = ResolutionManager.getScaleFactor();
        this.scaleFactorX = ResolutionManager.getHorizontalAR();
        this.battleReport = Game.getTrainer().getBattleReport();
        this.maxlevel = MonsterGalaxy.getTrainer().getCurrentIsland().getMaxLevel();
        float fontScale = 0.7f * this.scaleFactor;
        AlphaAction fadeIn = new AlphaAction();
        fadeIn.setAlpha(1.0f);
        fadeIn.setDuration(1.0f);
        DelayAction delayedFadeIn = new DelayAction();
        delayedFadeIn.setDuration(2.5f);
        delayedFadeIn.setAction(fadeIn);
        Team aTeam = MonsterGalaxy.getTrainer().getTeam();
        this.stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        Gdx.input.setInputProcessor(this.stage);
        TextureElement bg = new TextureElement(Assets.loadTexture("reportcard.bg"));
        bg.setSize(Gdx.graphics.getWidth() / ResolutionManager.getScaleFactor(), Gdx.graphics.getHeight() / ResolutionManager.getScaleFactor());
        bg.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        bg.setPosition(ScreenElement.RelPoint.CENTER, 0.0f, 0.0f);
        this.root.add(bg);
        for (int i = 0; i < 3; i++) {
            TextureElement mogabg = new TextureElement(Assets.loadTexture("mogabg"));
            mogabg.setPosition(ScreenElement.RelPoint.LEFT_TOP, (i * 110) + 210, -55.0f);
            this.root.add(mogabg);
        }
        int iterator = 0;
        for (Moga aMoga : aTeam.getMogas()) {
            TextureElement mogaAsset = new TextureElement(aMoga.getType().getPortrait());
            mogaAsset.setPosition(ScreenElement.RelPoint.LEFT_TOP, (iterator * 110) + 210, -55.0f);
            this.root.add(mogaAsset);
            iterator++;
        }
        TextureRegion normalButton = Assets.loadTexture("green.btn.small");
        TextureRegion selectedButton = Assets.loadTexture("green.btn.small.press");
        ButtonElement nextButton = new ButtonElement(this, UIEvent.NEXT_BUTTON, normalButton, selectedButton);
        nextButton.setPosition(ScreenElement.RelPoint.RIGHT_BOTTOM, -65.0f, 40.0f);
        this.root.add(nextButton);
        this.root.addLabel(I18nManager.getText(I18nManager.I18nKey.NEXT), 0.0f, 5.0f, ScreenElement.RelPoint.CENTER, nextButton, ScreenElement.RelPoint.CENTER, 0.7f, ColorConstants.PRIMARY_FONT_COLOR, false, false);
        int yBar0 = (int) (185.0f * this.scaleFactor);
        int yBar1 = (int) (145.0f * this.scaleFactor);
        int yBar2 = (int) (105.0f * this.scaleFactor);
        int xBar = (int) (500.0f * this.scaleFactorX);
        Group aBarXp = new Group();
        aBarXp.setWidth(1.0f);
        aBarXp.setHeight(1.0f);
        aBarXp.setX(-xBar);
        aBarXp.setY(yBar0);
        this.stage.addActor(aBarXp);
        Image barImageXP = new Image(Assets.loadTexture("bar"));
        barImageXP.setWidth(barImageXP.getWidth() * this.scaleFactorX);
        barImageXP.setHeight(barImageXP.getHeight() * this.scaleFactor);
        aBarXp.addActor(barImageXP);
        AutoFontLabel aTextXp = new AutoFontLabel(I18nManager.getText(I18nManager.I18nKey.XP_EARNED), fontScale, ColorConstants.PRIMARY_FONT_COLOR, true, ColorConstants.SECONDARY_SHADOW_COLOR);
        aTextXp.setY(10.0f * this.scaleFactor);
        aTextXp.setX(20.0f * this.scaleFactorX);
        ResolutionManager.scaleActor(aTextXp);
        aBarXp.addActor(aTextXp);
        aBarXp.setWidth(aBarXp.getWidth() * this.scaleFactorX);
        aBarXp.setHeight(aBarXp.getHeight() * this.scaleFactor);
        Group aBarTot = new Group();
        aBarTot.setWidth(1.0f);
        aBarTot.setHeight(1.0f);
        aBarTot.setX(0.0f);
        aBarTot.setY(yBar1);
        Image barImageTot = new Image(Assets.loadTexture("bar"));
        barImageTot.setX(-xBar);
        barImageTot.setY(yBar1);
        barImageTot.setWidth(barImageTot.getWidth() * this.scaleFactorX);
        barImageTot.setHeight(barImageTot.getHeight() * this.scaleFactor);
        this.stage.addActor(barImageTot);
        AutoFontLabel aTextTot = new AutoFontLabel(I18nManager.getText(I18nManager.I18nKey.TOTAL).toUpperCase(), fontScale, ColorConstants.PRIMARY_FONT_COLOR, true, ColorConstants.SECONDARY_SHADOW_COLOR);
        aTextTot.setY(10.0f * this.scaleFactor);
        aTextTot.setX(20.0f * this.scaleFactorX);
        ResolutionManager.scaleActor(aTextTot);
        aBarTot.addActor(aTextTot);
        aBarTot.setWidth(aBarTot.getWidth() * this.scaleFactorX);
        aBarTot.setHeight(aBarTot.getHeight() * this.scaleFactor);
        this.stage.addActor(aBarTot);
        Group aBarLv = new Group();
        aBarLv.setWidth(1.0f);
        aBarLv.setHeight(1.0f);
        aBarLv.setX(-xBar);
        aBarLv.setY(yBar2);
        Image barImageLv = new Image(Assets.loadTexture("bar"));
        barImageLv.setWidth(barImageLv.getWidth() * this.scaleFactorX);
        barImageLv.setHeight(barImageLv.getHeight() * this.scaleFactor);
        aBarLv.addActor(barImageLv);
        AutoFontLabel aTextLv = new AutoFontLabel(I18nManager.getText(I18nManager.I18nKey.LEVEL_CAPS), fontScale, ColorConstants.PRIMARY_FONT_COLOR, true, ColorConstants.SECONDARY_SHADOW_COLOR);
        aTextLv.setY(10.0f * this.scaleFactor);
        aTextLv.setX(20.0f * this.scaleFactorX);
        ResolutionManager.scaleActor(aTextLv);
        aBarLv.addActor(aTextLv);
        aBarLv.setWidth(aBarLv.getWidth() * this.scaleFactorX);
        aBarLv.setHeight(aBarLv.getHeight() * this.scaleFactor);
        this.stage.addActor(aBarLv);
        int iterator2 = 0;
        for (Moga aMoga2 : aTeam.getMogas()) {
            MogaChangesReport aMogaChangesReport = this.battleReport.getMogaChangesReport(Integer.valueOf(aMoga2.getType().getId()));
            int xPos = (int) (MOGA_PORTRAIT0_X + (MOGA_PORTRAIT_WIDTH * iterator2));
            AutoFontLabel textXp = new AutoFontLabel(new StringBuilder(String.valueOf(aMogaChangesReport.getVariableXP())).toString(), fontScale, ColorConstants.SECONARY_FONT_COLOR, true, ColorConstants.PRIMARY_SHADOW_COLOR);
            textXp.setX((xPos * this.scaleFactorX) - ((textXp.getWidth() / 2.0f) * this.scaleFactor));
            textXp.setY(10 * this.scaleFactor);
            ResolutionManager.scaleActor(textXp);
            aBarXp.addActor(textXp);
            AutoFontLabel textTot = new AutoFontLabel(new StringBuilder(String.valueOf(aMogaChangesReport.getTotalXP())).toString(), fontScale, ColorConstants.SECONARY_FONT_COLOR, true, ColorConstants.PRIMARY_SHADOW_COLOR);
            textTot.setX((xPos * this.scaleFactorX) - ((textTot.getWidth() / 2.0f) * this.scaleFactor));
            textTot.setY(10 * this.scaleFactor);
            ResolutionManager.scaleActor(textTot);
            aBarTot.addActor(textTot);
            final AutoFontLabel textLevel = new AutoFontLabel(new StringBuilder(String.valueOf(aMogaChangesReport.getLevel())).toString(), fontScale, ColorConstants.PRIMARY_FONT_COLOR, true);
            float delayTime = 2.0f + (0.5f * iterator2);
            int currentCompletion = aMogaChangesReport.getLevelCompletion();
            final int newCompletion = aMogaChangesReport.getNewLevelCompletion();
            final int newLevel = aMogaChangesReport.getNewLevel();
            final float mogaXPos = xPos;
            final int mogaIndex = iterator2;
            final float aImage1x = (xPos * this.scaleFactorX) - (43.0f * this.scaleFactor);
            if (aMogaChangesReport.getLevel() < aMogaChangesReport.getNewLevel()) {
                aImage1 = new LevelBar("aImage1", currentCompletion, 100, aMogaChangesReport.getLevel(), this.maxlevel, delayTime);
                aImage1.setOnConpletionListener(new Runnable() { // from class: com.gaiaonline.monstergalaxy.battle.BattleReportScreen.1
                    @Override // java.lang.Runnable
                    public void run() {
                        aImage1.restartAction(0, newCompletion, newLevel);
                        textLevel.setColor(Color.WHITE);
                        textLevel.setText(new StringBuilder(String.valueOf(newLevel)).toString());
                        BattleReportScreen.this.mogaLevelUp(mogaXPos, mogaIndex);
                        textLevel.setX(aImage1x - textLevel.getWidth());
                    }
                });
            } else {
                aImage1 = new LevelBar("aImage1", currentCompletion, newCompletion, aMogaChangesReport.getLevel(), this.maxlevel, delayTime);
            }
            aImage1.setX(aImage1x);
            aImage1.setY(10 * this.scaleFactor);
            aBarLv.addActor(aImage1);
            textLevel.setX(aImage1.getX() - textLevel.getWidth());
            textLevel.setY(10 * this.scaleFactor);
            ResolutionManager.scaleActor(textLevel);
            aBarLv.addActor(textLevel);
            iterator2++;
        }
        MoveToAction action0 = new MoveToAction();
        action0.setDuration(0.5f);
        action0.setPosition(0.0f, yBar0);
        MoveToAction action1 = new MoveToAction();
        action1.setDuration(0.5f);
        action1.setPosition(0.0f, yBar1);
        MoveToAction action2 = new MoveToAction();
        action2.setDuration(0.5f);
        action2.setPosition(0.0f, yBar2);
        aBarXp.clearActions();
        aBarTot.clearActions();
        aBarLv.clearActions();
        barImageTot.clearActions();
        DelayAction delayedAction0 = new DelayAction();
        delayedAction0.setAction(action0);
        delayedAction0.setDuration(0.3f);
        aBarXp.addAction(delayedAction0);
        aBarTot.getColor().a = 0.0f;
        aBarTot.addAction(delayedFadeIn);
        DelayAction delayedAction1 = new DelayAction();
        delayedAction1.setAction(action1);
        delayedAction1.setDuration(0.8f);
        barImageTot.addAction(delayedAction1);
        DelayAction delayedAction2 = new DelayAction();
        delayedAction2.setAction(action2);
        delayedAction2.setDuration(1.3f);
        aBarLv.addAction(delayedAction2);
        if (this.battleReport.hasCapturedMogas()) {
            List<Moga> capturedMogaList = this.battleReport.getCapturedMogas();
            Moga aCapturedMoga = capturedMogaList.get(capturedMogaList.size() - 1);
            String captureRate = Float.toString(aCapturedMoga.getType().getCaptureBonus());
            String captureText = aCapturedMoga.getType().getRarity().getLocalizedName();
            String bonusText = String.valueOf(captureRate) + "x XP Bonus - " + captureText;
            AutoFontLabel bonusLabel = new AutoFontLabel(bonusText, fontScale, ColorConstants.TERTIARY_FONT_COLOR, true, ColorConstants.PRIMARY_SHADOW_COLOR);
            ResolutionManager.scaleActor(bonusLabel);
            ActorUtil.centerOrigin(bonusLabel);
            float wid = Gdx.graphics.getWidth();
            bonusLabel.setY(yBar1 + (10.0f * this.scaleFactor));
            bonusLabel.setX((wid / 2.0f) - ((bonusLabel.getWidth() / this.scaleFactorX) / 2.0f));
            bonusLabel.setScale(0.0f);
            ScaleToAction scalebonus = new ScaleToAction();
            scalebonus.setDuration(1.0f);
            scalebonus.setScale(1.2f);
            ScaleToAction fadebonus = new ScaleToAction();
            fadebonus.setDuration(0.5f);
            fadebonus.setScale(0.0f);
            DelayAction delayedFadeBonus = new DelayAction();
            delayedFadeBonus.setDuration(0.5f);
            delayedFadeBonus.setAction(fadebonus);
            SequenceAction bonussecuence = new SequenceAction();
            bonussecuence.addAction(scalebonus);
            bonussecuence.addAction(delayedFadeBonus);
            DelayAction delayedBonusSequence = new DelayAction();
            delayedBonusSequence.setDuration(1.0f);
            delayedBonusSequence.setAction(bonussecuence);
            bonusLabel.addAction(delayedBonusSequence);
            this.stage.addActor(bonusLabel);
        }
        if (this.battleReport.isHighScore()) {
            String newHighscoreText = I18nManager.getText(I18nManager.I18nKey.NEW_HIGHSCORE);
            AutoFontLabel highscore = new AutoFontLabel(newHighscoreText, fontScale, ColorConstants.PRIMARY_FONT_COLOR, true, ColorConstants.SECONDARY_SHADOW_COLOR);
            float xText = 340.0f * this.scaleFactorX;
            float yText = 80.0f * this.scaleFactor;
            highscore.setX(xText);
            highscore.setY(-40.0f);
            highscore.getColor().a = 0.8f;
            AlphaAction fadein = new AlphaAction();
            fadein.setDuration(1.0f);
            fadein.setAlpha(1.0f);
            MoveToAction move = new MoveToAction();
            move.setDuration(0.5f);
            move.setPosition(xText, yText);
            ParallelAction parallel = new ParallelAction();
            parallel.addAction(fadein);
            parallel.addAction(move);
            DelayAction delayedParallel = new DelayAction();
            delayedParallel.setDuration(2.0f);
            delayedParallel.setAction(parallel);
            highscore.addAction(delayedParallel);
            ResolutionManager.scaleActor(highscore);
            this.stage.addActor(highscore);
        }
        if (this.battleReport.getNumberOfRewardedBlueCoffees() > 0 || this.battleReport.getNumberOfRewardedStarSeeds() > 0) {
            TextureElement rewardArea = new TextureElement(Assets.loadTexture("found.items.bg"));
            rewardArea.setPosition(ScreenElement.RelPoint.LEFT_TOP, 80.0f, -50.0f);
            this.root.add(rewardArea);
            TextElement itemsFound = new ShadowTextElement(I18nManager.getText(I18nManager.I18nKey.ITEMS_FOUND), 0.5f, ColorConstants.PRIMARY_FONT_COLOR, Color.BLACK, false);
            itemsFound.setAnchorPoint(ScreenElement.RelPoint.CENTER_TOP);
            itemsFound.setPosition(ScreenElement.RelPoint.LEFT_TOP, 80.0f, -22.0f);
            itemsFound.enableHorizontalAutoSize(130.0f);
            this.root.add(itemsFound);
            float offset = 0.0f;
            if (this.battleReport.getNumberOfRewardedBlueCoffees() > 0) {
                TextureElement bluecoffe = new TextureElement(Assets.loadTexture("blue.coffee.icon"));
                bluecoffe.setPosition(ScreenElement.RelPoint.LEFT_TOP, 70.0f, -55.0f);
                this.root.add(bluecoffe);
                this.root.addLabel("x" + this.battleReport.getNumberOfRewardedBlueCoffees(), 87.0f, -55.0f, ScreenElement.RelPoint.LEFT_TOP, 0.6f, ColorConstants.PRIMARY_FONT_COLOR, true, true);
                offset = -25.0f;
            }
            if (this.battleReport.getNumberOfRewardedStarSeeds() > 0) {
                TextureElement starseed = new TextureElement(Assets.loadTexture("capture.starseed.icon"));
                starseed.setPosition(ScreenElement.RelPoint.LEFT_TOP, 70.0f, (-55.0f) + offset);
                this.root.add(starseed);
                this.root.addLabel("x" + this.battleReport.getNumberOfRewardedStarSeeds(), 87.0f, (-55.0f) + offset, ScreenElement.RelPoint.LEFT_TOP, 0.6f, ColorConstants.PRIMARY_FONT_COLOR, true, true);
            }
        }
        TextureElement separator = new TextureElement(Assets.loadTexture("br.separator"));
        separator.setPosition(ScreenElement.RelPoint.CENTER, 0.0f, -65.0f);
        this.root.add(separator);
        Group starsGroup = new Group();
        Image starsBack = new Image(Assets.loadTexture("br.star.bg"));
        starsBack.setWidth(starsBack.getWidth() * this.scaleFactor);
        starsBack.setHeight(starsBack.getHeight() * this.scaleFactor);
        starsGroup.addActor(starsBack);
        starsGroup.setWidth(starsBack.getWidth());
        starsGroup.setHeight(starsBack.getHeight());
        TextureRegion emptyStar = Assets.loadTexture("empty.star");
        TextureRegion fullStar = Assets.loadTexture("full.star");
        String msg = " ";
        if (this.battleReport.hasScoredPerfect()) {
            MonsterGalaxy.getAnalytics().sessionMLogEvent(Analytics.PERFECT_ATTAKS_SESSIONM_EVENT);
            star1 = new Image(fullStar);
            star2 = new Image(fullStar);
            star3 = new Image(fullStar);
            msg = I18nManager.getText(I18nManager.I18nKey.PREFECT_ATTACK);
        } else if (this.battleReport.hasScoredVeryGood()) {
            star1 = new Image(fullStar);
            star2 = new Image(fullStar);
            star3 = new Image(emptyStar);
            msg = I18nManager.getText(I18nManager.I18nKey.VERY_GOOD_ATTACK);
        } else if (this.battleReport.hasScoredNormal()) {
            star1 = new Image(fullStar);
            star2 = new Image(emptyStar);
            star3 = new Image(emptyStar);
            msg = I18nManager.getText(I18nManager.I18nKey.WIN);
        } else {
            star1 = new Image(emptyStar);
            star2 = new Image(emptyStar);
            star3 = new Image(emptyStar);
        }
        ResolutionManager.scaleActor(star1);
        ResolutionManager.scaleActor(star2);
        ResolutionManager.scaleActor(star3);
        star1.setY(13.0f * this.scaleFactor);
        star2.setY(13.0f * this.scaleFactor);
        star3.setY(13.0f * this.scaleFactor);
        star1.setX(25.0f * this.scaleFactor);
        star2.setX(60.0f * this.scaleFactor);
        star3.setX(95.0f * this.scaleFactor);
        starsGroup.addActor(star1);
        starsGroup.addActor(star2);
        starsGroup.addActor(star3);
        AutoFontLabel scoreText = new AutoFontLabel(msg, fontScale, ColorConstants.SECONARY_FONT_COLOR, false, ColorConstants.SECONDARY_SHADOW_COLOR);
        scoreText.setX((starsBack.getWidth() / 2.0f) - (scoreText.getWidth() / 2.0f));
        scoreText.setY(50.0f * this.scaleFactor);
        starsGroup.addActor(scoreText);
        AlphaAction starsfadeIn = new AlphaAction();
        starsfadeIn.setDuration(1.0f);
        starsfadeIn.setAlpha(1.0f);
        DelayAction starsdelay = new DelayAction();
        starsdelay.setDuration(2.5f);
        starsdelay.setAction(starsfadeIn);
        starsGroup.clearActions();
        starsGroup.getColor().a = 0.0f;
        starsGroup.addAction(starsdelay);
        starsGroup.setY(15.0f * this.scaleFactor);
        starsGroup.setX((240.0f * this.scaleFactorX) - (starsGroup.getWidth() / 2.0f));
        this.stage.addActor(starsGroup);
        if (this.battleReport.isMogaMaxLevelReached() && !this.battleReport.isMaxLevelAtStart()) {
            String message = I18nManager.getText(I18nManager.I18nKey.REACHED_MAX_LEVEL);
            showDialog(new AlertDialog(this, message, I18nManager.getText(I18nManager.I18nKey.KEEP_PLAYING), UIEvent.CLOSE_BUTTON, UIEvent.CLOSE_BUTTON));
        }
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void onBackKeyPressed() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void mogaLevelUp(float xPos, int mogaIndex) {
        String levelUpMsg = I18nManager.getText(I18nManager.I18nKey.LEVEL_UP);
        AutoFontLabel levelup = new AutoFontLabel(levelUpMsg, 0.75f, ColorConstants.PRIMARY_FONT_COLOR, false, ColorConstants.PRIMARY_SHADOW_COLOR);
        float xPosition = (this.scaleFactorX * xPos) - (levelup.getWidth() / 2.0f);
        float minX = (MOGA_PORTRAIT0_X * this.scaleFactorX) - ((MOGA_PORTRAIT_WIDTH * this.scaleFactorX) / 2.0f);
        if (xPosition < minX) {
            xPosition = minX;
        }
        float maxX = ((480.0f * this.scaleFactorX) - levelup.getWidth()) - (5.0f * this.scaleFactorX);
        if (xPosition > maxX) {
            xPosition = maxX;
        }
        float yPosition = (270.0f - (((mogaIndex + 1) % 2) * 24)) * this.scaleFactor;
        levelup.setX(xPosition);
        levelup.setY(160.0f * this.scaleFactor);
        AlphaAction fadein = new AlphaAction();
        fadein.setDuration(1.0f);
        fadein.setAlpha(1.0f);
        levelup.addAction(fadein);
        AlphaAction fadeinLevelup = new AlphaAction();
        fadeinLevelup.setDuration(1.0f);
        fadeinLevelup.setAlpha(1.0f);
        MoveToAction movelevelup = new MoveToAction();
        movelevelup.setDuration(0.5f);
        movelevelup.setPosition(xPosition, yPosition);
        ParallelAction parallel = new ParallelAction();
        parallel.addAction(fadeinLevelup);
        parallel.addAction(movelevelup);
        levelup.addAction(parallel);
        this.stage.addActor(levelup);
        EffectActor aEffect = new EffectActor("sparkles");
        aEffect.setEffect(EffectManager.getInstance().getEffect(EffectType.STARS).getCopy());
        aEffect.setX(xPosition);
        aEffect.setY(yPosition);
        this.stage.addActor(aEffect);
        aEffect.getEffect().start();
        SoundManager.playSound(SoundManager.SOUND_LEVELUP);
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void present(float deltaTime) {
        super.present(deltaTime);
        if (this.stage != null) {
            this.stage.draw();
        }
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
    public void onUIEvent(UIEvent event) {
        if (event == UIEvent.NEXT_BUTTON) {
            this.battleReport.commitCaptures();
            Game.getTrainer().verifyIfGameCompleted();
            if (this.completedUnpaidQuests.size() > 0) {
                SoundManager.playMusic(Constants.worldAmbienceAudio);
                MonsterGalaxy.showScreen(new QuestStoryScreen(this.completedUnpaidQuests.get(0)));
                return;
            } else if (this.battleReport != null && this.battleReport.hasUnlockedNextIsland()) {
                MonsterGalaxy.backToWorldFromBattleFlow(Game.getTrainer().getCurrentIsland().getNextIsland().getId());
                return;
            } else {
                MonsterGalaxy.backToIslandFromBattleFlow();
                return;
            }
        }
        if (event == UIEvent.CLOSE_BUTTON) {
            hideDialog();
        }
    }
}
