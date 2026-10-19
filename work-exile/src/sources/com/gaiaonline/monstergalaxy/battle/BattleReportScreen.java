package com.gaiaonline.monstergalaxy.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.OnActionCompleted;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Delay;
import com.badlogic.gdx.scenes.scene2d.actions.FadeIn;
import com.badlogic.gdx.scenes.scene2d.actions.FadeTo;
import com.badlogic.gdx.scenes.scene2d.actions.MoveTo;
import com.badlogic.gdx.scenes.scene2d.actions.Parallel;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleTo;
import com.badlogic.gdx.scenes.scene2d.actions.Sequence;
import com.badlogic.gdx.scenes.scene2d.actors.Image;
import com.badlogic.gdx.scenes.scene2d.actors.Label;
import com.gaiaonline.monstergalaxy.analytics.Analytics;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ColorConstants;
import com.gaiaonline.monstergalaxy.app.Constants;
import com.gaiaonline.monstergalaxy.app.Fonts;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.MonsterGalaxy;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.app.Screen;
import com.gaiaonline.monstergalaxy.app.ShadowLabel;
import com.gaiaonline.monstergalaxy.app.SoundManager;
import com.gaiaonline.monstergalaxy.battle.dialogs.AlertDialog;
import com.gaiaonline.monstergalaxy.battle.effects.EffectManager;
import com.gaiaonline.monstergalaxy.battle.effects.EffectType;
import com.gaiaonline.monstergalaxy.model.battle.BattleReport;
import com.gaiaonline.monstergalaxy.model.battle.MogaChangesReport;
import com.gaiaonline.monstergalaxy.model.moga.Moga;
import com.gaiaonline.monstergalaxy.model.quest.Quest;
import com.gaiaonline.monstergalaxy.model.trainer.Team;
import com.gaiaonline.monstergalaxy.quest.QuestStoryScreen;
import com.gaiaonline.monstergalaxy.screen.ButtonElement;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.ScreenListener;
import com.gaiaonline.monstergalaxy.screen.TextureElement;
import com.gaiaonline.monstergalaxy.screen.UIEvent;
import java.util.List;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class BattleReportScreen extends Screen implements ScreenListener {
    private BitmapFont aBmpFont;
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
        this.aBmpFont = Fonts.regular;
        this.aBmpFont.setScale(0.7f * this.scaleFactor);
        FadeTo fadeIn = FadeTo.$(1.0f, 1.0f);
        Action delay = Delay.$(fadeIn, 2.5f);
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
        this.root.addLabel("Next", 0.0f, 5.0f, ScreenElement.RelPoint.CENTER, nextButton, ScreenElement.RelPoint.CENTER, 0.7f, ColorConstants.PRIMARY_FONT_COLOR, false, false);
        int yBar0 = (int) (185.0f * this.scaleFactor);
        int yBar1 = (int) (145.0f * this.scaleFactor);
        int yBar2 = (int) (105.0f * this.scaleFactor);
        int xBar = (int) (500.0f * this.scaleFactorX);
        Group aBarXp = new Group("aGroup");
        aBarXp.width = 1.0f;
        aBarXp.height = 1.0f;
        aBarXp.x = -xBar;
        aBarXp.y = yBar0;
        this.stage.addActor(aBarXp);
        Image barImageXP = new Image("abrImage", Assets.loadTexture("bar"));
        barImageXP.width *= this.scaleFactorX;
        barImageXP.height *= this.scaleFactor;
        aBarXp.addActor(barImageXP);
        ShadowLabel aTextXp = new ShadowLabel("xp_earned_label", Fonts.regular, "XP EARNED", ColorConstants.PRIMARY_FONT_COLOR, ColorConstants.SECONDARY_SHADOW_COLOR);
        aTextXp.y = 10.0f * this.scaleFactor;
        aTextXp.x = 20.0f * this.scaleFactorX;
        ResolutionManager.scaleActor(aTextXp);
        aBarXp.addActor(aTextXp);
        aBarXp.width *= this.scaleFactorX;
        aBarXp.height *= this.scaleFactor;
        Group aBarTot = new Group("a_group");
        aBarTot.width = 1.0f;
        aBarTot.height = 1.0f;
        aBarTot.x = 0.0f;
        aBarTot.y = yBar1;
        Image barImageTot = new Image("bar_image", Assets.loadTexture("bar"));
        barImageTot.x = -xBar;
        barImageTot.y = yBar1;
        barImageTot.width *= this.scaleFactorX;
        barImageTot.height *= this.scaleFactor;
        this.stage.addActor(barImageTot);
        ShadowLabel aTextTot = new ShadowLabel("total_label", Fonts.regular, "TOTAL", ColorConstants.PRIMARY_FONT_COLOR, ColorConstants.SECONDARY_SHADOW_COLOR);
        aTextTot.y = 10.0f * this.scaleFactor;
        aTextTot.x = 20.0f * this.scaleFactorX;
        ResolutionManager.scaleActor(aTextTot);
        aBarTot.addActor(aTextTot);
        aBarTot.width *= this.scaleFactorX;
        aBarTot.height *= this.scaleFactor;
        this.stage.addActor(aBarTot);
        Group aBarLv = new Group("aGroupl");
        aBarLv.width = 1.0f;
        aBarLv.height = 1.0f;
        aBarLv.x = -xBar;
        aBarLv.y = yBar2;
        Image barImageLv = new Image("nlevel_bar_image", Assets.loadTexture("bar"));
        barImageLv.width *= this.scaleFactorX;
        barImageLv.height *= this.scaleFactor;
        aBarLv.addActor(barImageLv);
        ShadowLabel aTextLv = new ShadowLabel("aTextl", Fonts.regular, "LEVEL", ColorConstants.PRIMARY_FONT_COLOR, ColorConstants.SECONDARY_SHADOW_COLOR);
        aTextLv.y = 10.0f * this.scaleFactor;
        aTextLv.x = 20.0f * this.scaleFactorX;
        ResolutionManager.scaleActor(aTextLv);
        aBarLv.addActor(aTextLv);
        aBarLv.width *= this.scaleFactorX;
        aBarLv.height *= this.scaleFactor;
        this.stage.addActor(aBarLv);
        int iterator2 = 0;
        for (Moga aMoga2 : aTeam.getMogas()) {
            MogaChangesReport aMogaChangesReport = this.battleReport.getMogaChangesReport(Integer.valueOf(aMoga2.getType().getId()));
            int xPos = (iterator2 * 110) + 210;
            this.aBmpFont.setScale(0.7f * this.scaleFactor);
            ShadowLabel textXp = new ShadowLabel("text_exp", this.aBmpFont, new StringBuilder(String.valueOf(aMogaChangesReport.getVariableXP())).toString(), ColorConstants.SECONARY_FONT_COLOR, ColorConstants.PRIMARY_SHADOW_COLOR);
            textXp.x = (xPos * this.scaleFactorX) - ((textXp.width / 2.0f) * this.scaleFactor);
            textXp.y = 10 * this.scaleFactor;
            ResolutionManager.scaleActor(textXp);
            aBarXp.addActor(textXp);
            this.aBmpFont.setScale(0.7f * this.scaleFactor);
            ShadowLabel textTot = new ShadowLabel("text_total", this.aBmpFont, new StringBuilder(String.valueOf(aMogaChangesReport.getTotalXP())).toString(), ColorConstants.SECONARY_FONT_COLOR, ColorConstants.PRIMARY_SHADOW_COLOR);
            textTot.x = (xPos * this.scaleFactorX) - ((textTot.width / 2.0f) * this.scaleFactor);
            textTot.y = 10 * this.scaleFactor;
            ResolutionManager.scaleActor(textTot);
            aBarTot.addActor(textTot);
            this.aBmpFont.setScale(0.7f * this.scaleFactor);
            final Label textLevel = new Label("text_level", this.aBmpFont, new StringBuilder(String.valueOf(aMogaChangesReport.getLevel())).toString());
            float delayTime = 2.0f + (0.5f * iterator2);
            int currentCompletion = aMogaChangesReport.getLevelCompletion();
            final int newCompletion = aMogaChangesReport.getNewLevelCompletion();
            final int newLevel = aMogaChangesReport.getNewLevel();
            final float mogaIndex = xPos;
            final float aImage1x = (xPos * this.scaleFactorX) - (43.0f * this.scaleFactor);
            if (aMogaChangesReport.getLevel() < aMogaChangesReport.getNewLevel()) {
                aImage1 = new LevelBar("aImage1", currentCompletion, 100, aMogaChangesReport.getLevel(), this.maxlevel, delayTime);
                aImage1.setOnConpletionListener(new OnActionCompleted() { // from class: com.gaiaonline.monstergalaxy.battle.BattleReportScreen.1
                    @Override // com.badlogic.gdx.scenes.scene2d.OnActionCompleted
                    public void completed(Action action) {
                        aImage1.restartAction(0, newCompletion, newLevel);
                        BattleReportScreen.this.aBmpFont.setScale(0.7f * BattleReportScreen.this.scaleFactor);
                        textLevel.setFont(BattleReportScreen.this.aBmpFont);
                        textLevel.setText(new StringBuilder(String.valueOf(newLevel)).toString());
                        BattleReportScreen.this.mogaLevelUp(mogaIndex);
                        textLevel.x = aImage1x - textLevel.width;
                    }
                });
            } else {
                aImage1 = new LevelBar("aImage1", currentCompletion, newCompletion, aMogaChangesReport.getLevel(), this.maxlevel, delayTime);
            }
            aImage1.x = aImage1x;
            aImage1.y = 10 * this.scaleFactor;
            aBarLv.addActor(aImage1);
            textLevel.x = aImage1.x - textLevel.width;
            textLevel.y = 10 * this.scaleFactor;
            ResolutionManager.scaleActor(textLevel);
            aBarLv.addActor(textLevel);
            iterator2++;
        }
        MoveTo action0 = MoveTo.$(0.0f, yBar0, 0.5f);
        MoveTo action1 = MoveTo.$(0.0f, yBar1, 0.5f);
        MoveTo action2 = MoveTo.$(0.0f, yBar2, 0.5f);
        aBarXp.clearActions();
        aBarTot.clearActions();
        aBarLv.clearActions();
        barImageTot.clearActions();
        aBarXp.action(Delay.$(action0, 0.3f));
        aBarTot.color.a = 0.0f;
        aBarTot.action(delay);
        barImageTot.action(Delay.$(action1, 0.8f));
        aBarLv.action(Delay.$(action2, 1.3f));
        if (this.battleReport.hasCapturedMogas()) {
            List<Moga> capturedMogaList = this.battleReport.getCapturedMogas();
            Moga aCapturedMoga = capturedMogaList.get(capturedMogaList.size() - 1);
            String captureRate = Float.toString(aCapturedMoga.getType().getCaptureBonus());
            String captureText = aCapturedMoga.getType().getRarity().getName();
            String bonusText = String.valueOf(captureRate) + "x XP Bonus - " + captureText;
            this.aBmpFont.setScale(0.7f * this.scaleFactor);
            ShadowLabel bonusLabel = new ShadowLabel(bonusText, this.aBmpFont, bonusText, ColorConstants.TERTIARY_FONT_COLOR, ColorConstants.PRIMARY_SHADOW_COLOR);
            ResolutionManager.scaleActor(bonusLabel);
            float wid = Gdx.graphics.getWidth();
            bonusLabel.y = yBar1 + (10.0f * this.scaleFactor);
            bonusLabel.x = (wid / 2.0f) - ((bonusLabel.width / this.scaleFactorX) / 2.0f);
            bonusLabel.scaleX = 0.0f;
            bonusLabel.scaleY = 0.0f;
            ScaleTo scalebonus = ScaleTo.$(1.2f, 1.2f, 1.0f);
            ScaleTo fadebonus = ScaleTo.$(0.0f, 0.0f, 0.5f);
            Sequence bonussecuence = Sequence.$(scalebonus, Delay.$(fadebonus, 0.5f));
            bonusLabel.action(Delay.$(bonussecuence, 1.0f));
            this.stage.addActor(bonusLabel);
        }
        if (this.battleReport.isHighScore()) {
            BitmapFont aFont = Fonts.italic;
            aFont.setScale(0.6f * this.scaleFactor);
            ShadowLabel highscore = new ShadowLabel("aText2", aFont, "NEW HIGHSCORE!", ColorConstants.PRIMARY_FONT_COLOR, ColorConstants.SECONDARY_SHADOW_COLOR);
            float xText = 340.0f * this.scaleFactorX;
            float yText = 80.0f * this.scaleFactor;
            highscore.x = xText;
            highscore.y = -40.0f;
            highscore.color.a = 0.8f;
            FadeIn fadein = FadeIn.$(1.0f);
            MoveTo move = MoveTo.$(xText, yText, 0.5f);
            highscore.action(Delay.$(Parallel.$(fadein, move), 2.0f));
            ResolutionManager.scaleActor(highscore);
            this.stage.addActor(highscore);
        }
        if (this.battleReport.getNumberOfRewardedBlueCoffees() > 0 || this.battleReport.getNumberOfRewardedStarSeeds() > 0) {
            TextureElement rewardArea = new TextureElement(Assets.loadTexture("found.items.bg"));
            rewardArea.setPosition(ScreenElement.RelPoint.LEFT_TOP, 80.0f, -50.0f);
            this.root.add(rewardArea);
            this.root.addLabel("ITEMS FOUND", 80.0f, -25.0f, ScreenElement.RelPoint.LEFT_TOP, 0.5f, ColorConstants.PRIMARY_FONT_COLOR, false, true);
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
        Image starsBack = new Image("starsback", Assets.loadTexture("br.star.bg"));
        starsBack.width *= this.scaleFactor;
        starsBack.height *= this.scaleFactor;
        starsGroup.addActor(starsBack);
        starsGroup.width = starsBack.width;
        starsGroup.height = starsBack.height;
        TextureRegion emptyStar = Assets.loadTexture("empty.star");
        TextureRegion fullStar = Assets.loadTexture("full.star");
        String msg = " ";
        if (this.battleReport.hasScoredPerfect()) {
            MonsterGalaxy.getAnalytics().sessionMLogEvent(Analytics.PERFECT_ATTAKS_SESSIONM_EVENT);
            star1 = new Image("star1", fullStar);
            star2 = new Image("star2", fullStar);
            star3 = new Image("star3", fullStar);
            msg = Analytics.PERFECT_ATTAKS_SESSIONM_EVENT;
        } else if (this.battleReport.hasScoredVeryGood()) {
            star1 = new Image("star1", fullStar);
            star2 = new Image("star2", fullStar);
            star3 = new Image("star3", emptyStar);
            msg = "Very Good Attacks";
        } else if (this.battleReport.hasScoredNormal()) {
            star1 = new Image("star1", fullStar);
            star2 = new Image("star2", emptyStar);
            star3 = new Image("star3", emptyStar);
            msg = "Win";
        } else {
            star1 = new Image("star1", emptyStar);
            star2 = new Image("star2", emptyStar);
            star3 = new Image("star3", emptyStar);
        }
        ResolutionManager.scaleActor(star1);
        ResolutionManager.scaleActor(star2);
        ResolutionManager.scaleActor(star3);
        star1.y = 13.0f * this.scaleFactor;
        star2.y = 13.0f * this.scaleFactor;
        star3.y = 13.0f * this.scaleFactor;
        star1.x = 25.0f * this.scaleFactor;
        star2.x = 60.0f * this.scaleFactor;
        star3.x = 95.0f * this.scaleFactor;
        starsGroup.addActor(star1);
        starsGroup.addActor(star2);
        starsGroup.addActor(star3);
        BitmapFont scoreFont = Fonts.regular;
        scoreFont.setScale(0.85f * this.scaleFactor);
        ShadowLabel scoreText = new ShadowLabel("scoreTest", scoreFont, msg, ColorConstants.SECONARY_FONT_COLOR, ColorConstants.SECONDARY_SHADOW_COLOR);
        scoreText.x = (starsBack.width / 2.0f) - (scoreText.width / 2.0f);
        scoreText.y = 50.0f * this.scaleFactor;
        starsGroup.addActor(scoreText);
        FadeTo starsfadeIn = FadeTo.$(1.0f, 1.0f);
        Delay starsdelay = Delay.$(starsfadeIn, 2.5f);
        starsGroup.clearActions();
        starsGroup.color.a = 0.0f;
        starsGroup.action(starsdelay);
        starsGroup.y = 15.0f * this.scaleFactor;
        starsGroup.x = (240.0f * this.scaleFactorX) - (starsGroup.width / 2.0f);
        this.stage.addActor(starsGroup);
        if (this.battleReport.isMogaMaxLevelReached() && !this.battleReport.isMaxLevelAtStart()) {
            showDialog(new AlertDialog(this, "Greetings, Tamer! You might have noticed that one of your mogas reached its maximum level in the last battle. You'll need to unlock the next island before your moga can continue to earn experience and level-up.", "Keep Playing", UIEvent.CLOSE_BUTTON, UIEvent.CLOSE_BUTTON));
        }
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void onBackKeyPressed() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void mogaLevelUp(float xPos) {
        this.aBmpFont.setScale(0.85f * this.scaleFactor);
        ShadowLabel levelup = new ShadowLabel("aText2", this.aBmpFont, "LEVEL \n UP!", ColorConstants.PRIMARY_FONT_COLOR, ColorConstants.PRIMARY_SHADOW_COLOR);
        float xPosition = (this.scaleFactorX * xPos) - ((levelup.width * 0.5f) / 2.0f);
        float yPosition = 270.0f * this.scaleFactor;
        levelup.x = xPosition;
        levelup.y = 160.0f * this.scaleFactor;
        FadeIn fadein = FadeIn.$(1.0f);
        levelup.action(fadein);
        FadeIn fadeinLevelup = FadeIn.$(1.0f);
        MoveTo movelevelup = MoveTo.$(xPosition, yPosition, 0.5f);
        levelup.action(Parallel.$(fadeinLevelup, movelevelup));
        this.stage.addActor(levelup);
        EffectActor aEffect = new EffectActor("sparkles");
        aEffect.setEffect(EffectManager.getInstance().getEffect(EffectType.STARS).getCopy());
        aEffect.x = xPosition;
        aEffect.y = yPosition;
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
