package com.gaiaonline.monstergalaxy.splash;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.JumpByAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.app.Screen;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;

/* JADX INFO: loaded from: classes.dex */
public class LoadingScreen extends Screen {
    private Texture loadingTexture = new Texture(Gdx.files.internal("data/loading.png"));

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
    public LoadingScreen() {
        this.root.addBackground(this.loadingTexture);
        this.root.addLabel("Loading...", 0.0f, -80.0f, ScreenElement.RelPoint.CENTER, 1.0f, Color.WHITE, true, true);
        Image scaledImage = ResolutionManager.getScaledImage("loading_seed", Assets.starSeed);
        scaledImage.setX((Gdx.graphics.getWidth() / 2) - (scaledImage.getWidth() / 2.0f));
        scaledImage.setY((Gdx.graphics.getHeight() / 2) - (scaledImage.getHeight() / 2.0f));
        RepeatAction forever = new RepeatAction();
        forever.setCount(-1);
        JumpByAction jumpBy = new JumpByAction();
        jumpBy.setX(0);
        jumpBy.setY(0);
        jumpBy.setHeight(40);
        jumpBy.setJumps(1);
        jumpBy.setDuration(1.7f);
        forever.setAction(jumpBy);
        scaledImage.addAction(forever);
        this.stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        this.stage.addActor(scaledImage);
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void update(float delta) {
        super.update(delta);
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void onBackKeyPressed() {
    }

    @Override // com.gaiaonline.monstergalaxy.app.Screen
    public void dispose() {
    }
}
