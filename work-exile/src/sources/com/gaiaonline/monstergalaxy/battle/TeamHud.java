package com.gaiaonline.monstergalaxy.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.AnimationAction;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.OnActionCompleted;
import com.badlogic.gdx.scenes.scene2d.actions.MoveBy;
import com.badlogic.gdx.scenes.scene2d.actions.MoveTo;
import com.badlogic.gdx.scenes.scene2d.actors.Image;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ColorConstants;
import com.gaiaonline.monstergalaxy.app.Fonts;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.app.ShadowLabel;
import com.gaiaonline.monstergalaxy.app.SoundManager;
import com.gaiaonline.monstergalaxy.model.moga.Moga;
import com.gaiaonline.monstergalaxy.model.trainer.Team;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class TeamHud extends Group implements MogaMiniHud.Listener {
    private static final float CLOSE_DURATION = 0.5f;
    private static final float OPEN_DURATION = 0.5f;
    private boolean isLeftHud;
    private boolean isOpen;
    private final float mogaHudStdHeight;
    private List<MogaMiniHud> mogaHuds;
    private Group mogaHudsGroup;
    private boolean playingAnimation;
    private Integer selectedMogaIndex;
    private Group topLeftHudGroup;
    private int deployedMogaIndex = -1;
    private WeakReference<Listener> listenerRef = new WeakReference<>(null);

    public interface Listener {
        void onBlueCoffee(int i);

        void onMogaSelected(int i);

        void onTeamHudClose(boolean z, Integer num);

        void onTeamHudOpen(boolean z);
    }

    public TeamHud(boolean isLeftHud) {
        this.isLeftHud = isLeftHud;
        float scaleFactor = ResolutionManager.getScaleFactor();
        Image topLeftHudBg = ResolutionManager.getScaledImage("team_hud_bg", Assets.hudBackground);
        topLeftHudBg.y = Gdx.graphics.getHeight() - topLeftHudBg.height;
        Fonts.regular.setScale(0.73f * scaleFactor);
        ShadowLabel chooseMogaText = new ShadowLabel("choose_moga_text", Fonts.regular, "Choose Your Moga", ColorConstants.PRIMARY_FONT_COLOR, ColorConstants.PRIMARY_SHADOW_COLOR);
        chooseMogaText.x = ((topLeftHudBg.width - chooseMogaText.width) - (20.0f * scaleFactor)) / 2.0f;
        chooseMogaText.y = (Gdx.graphics.getHeight() - chooseMogaText.height) - (10.0f * scaleFactor);
        Image arrowDownBtn = ResolutionManager.getScaledImage("arrowDown", Assets.arrowDownBtn);
        arrowDownBtn.x = ((chooseMogaText.x + chooseMogaText.width) - (12.0f * scaleFactor)) / 2.0f;
        arrowDownBtn.y = (chooseMogaText.y - chooseMogaText.height) - (4.0f * scaleFactor);
        this.topLeftHudGroup = new Group();
        this.topLeftHudGroup.addActor(topLeftHudBg);
        this.topLeftHudGroup.addActor(chooseMogaText);
        this.topLeftHudGroup.addActor(arrowDownBtn);
        this.mogaHudsGroup = new Group();
        this.mogaHuds = new ArrayList();
        this.mogaHudStdHeight = Assets.mogaBackground.getRegionHeight() * scaleFactor;
        addActor(this.mogaHudsGroup);
        addActor(this.topLeftHudGroup);
    }

    public void setListener(Listener listener) {
        this.listenerRef = new WeakReference<>(listener);
    }

    public void setTeam(Team team) {
        this.mogaHuds.clear();
        for (int i = 0; i < team.getMogas().size(); i++) {
            Moga moga = team.getMogas().get(i);
            MogaMiniHud mogaHud = new MogaMiniHud(moga, i, this);
            this.mogaHuds.add(mogaHud);
        }
    }

    private void addMogaHuds() {
        this.mogaHudsGroup.clear();
        for (int i = this.mogaHuds.size() - 1; i >= 0; i--) {
            if (i != this.deployedMogaIndex) {
                this.mogaHudsGroup.addActor(this.mogaHuds.get(i));
            }
        }
    }

    public int getMogaCount() {
        return this.mogaHuds.size();
    }

    public void setRival(Moga rival) {
        for (MogaMiniHud hud : this.mogaHuds) {
            hud.setRival(rival);
        }
    }

    private Vector2 getOpenHudPosition(int hudIndex) {
        int x = this.isLeftHud ? 0 : (Gdx.graphics.getWidth() - 0) - ((int) (83.0f * ResolutionManager.getScaleFactor()));
        return new Vector2(x, Gdx.graphics.getHeight() - ((hudIndex + 2) * (this.mogaHudStdHeight - (10.0f * ResolutionManager.getScaleFactor()))));
    }

    private Vector2 getClosedHudPosition(int hudIndex) {
        Vector2 position = new Vector2();
        position.x = (hudIndex + 1) * (-8) * ResolutionManager.getScaleFactor();
        if (!this.isLeftHud) {
            position.x = (Gdx.graphics.getWidth() - position.x) - ((int) (83.0f * ResolutionManager.getScaleFactor()));
        }
        position.y = (Gdx.graphics.getHeight() - this.mogaHudStdHeight) - ((hudIndex * 6) * ResolutionManager.getScaleFactor());
        return position;
    }

    public void open(boolean animate, int deployedMogaIndex) {
        this.deployedMogaIndex = deployedMogaIndex;
        this.selectedMogaIndex = null;
        this.isOpen = true;
        this.topLeftHudGroup.visible = deployedMogaIndex == -1;
        addMogaHuds();
        SoundManager.loadSound(SoundManager.SOUND_BLUE_COFFEE);
        AnimationAction hud1Action = null;
        int hudIndex = -1;
        for (int mogaIndex = 0; mogaIndex < this.mogaHuds.size(); mogaIndex++) {
            if (mogaIndex != deployedMogaIndex) {
                hudIndex++;
                MogaMiniHud hud = this.mogaHuds.get(mogaIndex);
                hud.updateHealthbar();
                hud.showAll(animate);
                Vector2 openHudPosition = getOpenHudPosition(hudIndex);
                if (animate) {
                    Vector2 closedHudPosition = getClosedHudPosition(hudIndex);
                    hud.x = closedHudPosition.x;
                    hud.y = closedHudPosition.y;
                    AnimationAction action = MoveTo.$(openHudPosition.x, openHudPosition.y, 0.5f);
                    if (hudIndex == 0) {
                        hud1Action = action;
                    }
                    hud.action(action);
                } else {
                    hud.x = openHudPosition.x;
                    hud.y = openHudPosition.y;
                }
            }
        }
        if (hud1Action != null) {
            hud1Action.setCompletionListener(new OnActionCompleted() { // from class: com.gaiaonline.monstergalaxy.battle.TeamHud.1
                @Override // com.badlogic.gdx.scenes.scene2d.OnActionCompleted
                public void completed(Action action2) {
                    TeamHud.this.playingAnimation = false;
                    if (TeamHud.this.listenerRef.get() != null) {
                        ((Listener) TeamHud.this.listenerRef.get()).onTeamHudOpen(true);
                    }
                }
            });
        }
        if (this.listenerRef.get() != null) {
            this.listenerRef.get().onTeamHudOpen(animate ? false : true);
        }
        this.playingAnimation = animate;
    }

    public boolean isOpen() {
        return this.isOpen;
    }

    public void close(boolean animate, int deployedMogaIndex) {
        this.deployedMogaIndex = deployedMogaIndex;
        addMogaHuds();
        AnimationAction hud1Action = null;
        int hudIndex = -1;
        for (int mogaIndex = 0; mogaIndex < this.mogaHuds.size(); mogaIndex++) {
            if (mogaIndex != deployedMogaIndex) {
                hudIndex++;
                MogaMiniHud hud = this.mogaHuds.get(mogaIndex);
                hud.showPortraitOnly(animate);
                Vector2 closedHudPosition = getClosedHudPosition(hudIndex);
                if (animate) {
                    Vector2 openHudPosition = getOpenHudPosition(hudIndex);
                    hud.x = openHudPosition.x;
                    hud.y = openHudPosition.y;
                    AnimationAction action = MoveTo.$(closedHudPosition.x, closedHudPosition.y, 0.5f);
                    if (hudIndex == 0) {
                        hud1Action = action;
                    }
                    hud.action(action);
                } else {
                    hud.x = closedHudPosition.x;
                    hud.y = closedHudPosition.y;
                }
            }
        }
        if (hud1Action == null) {
            hud1Action = MoveBy.$(0.0f, 0.0f, 0.5f);
            action(hud1Action);
        }
        if (hud1Action != null) {
            hud1Action.setCompletionListener(new OnActionCompleted() { // from class: com.gaiaonline.monstergalaxy.battle.TeamHud.2
                @Override // com.badlogic.gdx.scenes.scene2d.OnActionCompleted
                public void completed(Action action2) {
                    if (TeamHud.this.listenerRef.get() != null) {
                        ((Listener) TeamHud.this.listenerRef.get()).onTeamHudClose(true, TeamHud.this.selectedMogaIndex);
                    }
                    TeamHud.this.playingAnimation = false;
                    TeamHud.this.isOpen = false;
                }
            });
        }
        if (this.listenerRef.get() != null) {
            this.listenerRef.get().onTeamHudClose(!animate, this.selectedMogaIndex);
        }
        this.playingAnimation = animate;
        if (!animate) {
            this.isOpen = false;
        }
    }

    public void setZodiacPowerTypes() {
    }

    public void dispose() {
        for (MogaMiniHud hud : this.mogaHuds) {
            hud.dispose();
        }
    }

    @Override // com.gaiaonline.monstergalaxy.battle.MogaMiniHud.Listener
    public void onMogaSelected(int mogaIndex) {
        if (!this.playingAnimation && this.isOpen) {
            this.selectedMogaIndex = Integer.valueOf(mogaIndex);
            if (this.listenerRef.get() != null) {
                this.listenerRef.get().onMogaSelected(mogaIndex);
            }
        }
    }

    @Override // com.gaiaonline.monstergalaxy.battle.MogaMiniHud.Listener
    public void onBlueCoffee(int mogaIndex) {
        if (!this.playingAnimation && this.isOpen && this.listenerRef.get() != null) {
            this.listenerRef.get().onBlueCoffee(mogaIndex);
        }
    }

    public void blueCoffeeApplied(int mogaIndex) {
        this.mogaHuds.get(mogaIndex).blueCoffeeApplied();
    }
}
