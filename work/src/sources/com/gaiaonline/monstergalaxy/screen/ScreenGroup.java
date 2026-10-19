package com.gaiaonline.monstergalaxy.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.assets.AssetsCache;
import com.gaiaonline.monstergalaxy.battle.effects.Effect;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class ScreenGroup extends ScreenElement implements Touchable {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$gaiaonline$monstergalaxy$screen$ScreenElement$RelPoint = null;
    public static final float SCREEN_BASE_X = 480.0f;
    public static final float SCREEN_BASE_Y = 320.0f;
    protected Map<ScreenElement, Vector2> elements;
    private boolean flip;
    private float horMult;
    private TextureRegion overlay;
    protected List<ScreenElement> touchables;
    private float verMult;
    public static final float HOR_AR = ResolutionManager.getHorizontalAR();
    public static final float VER_AR = ResolutionManager.getVerticalAR();
    public static final float SCALE = ResolutionManager.getScaleFactor();
    private Vector2 tmpVector2 = new Vector2();
    private Vector2 tmp2Vector2 = new Vector2();

    static /* synthetic */ int[] $SWITCH_TABLE$com$gaiaonline$monstergalaxy$screen$ScreenElement$RelPoint() {
        int[] iArr = $SWITCH_TABLE$com$gaiaonline$monstergalaxy$screen$ScreenElement$RelPoint;
        if (iArr == null) {
            iArr = new int[ScreenElement.RelPoint.valuesCustom().length];
            try {
                iArr[ScreenElement.RelPoint.CENTER.ordinal()] = 5;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[ScreenElement.RelPoint.CENTER_BOTTOM.ordinal()] = 6;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[ScreenElement.RelPoint.CENTER_TOP.ordinal()] = 4;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[ScreenElement.RelPoint.LEFT_BOTTOM.ordinal()] = 3;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[ScreenElement.RelPoint.LEFT_CENTER.ordinal()] = 2;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[ScreenElement.RelPoint.LEFT_TOP.ordinal()] = 1;
            } catch (NoSuchFieldError e6) {
            }
            try {
                iArr[ScreenElement.RelPoint.RIGHT_BOTTOM.ordinal()] = 9;
            } catch (NoSuchFieldError e7) {
            }
            try {
                iArr[ScreenElement.RelPoint.RIGHT_CENTER.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                iArr[ScreenElement.RelPoint.RIGHT_TOP.ordinal()] = 7;
            } catch (NoSuchFieldError e9) {
            }
            $SWITCH_TABLE$com$gaiaonline$monstergalaxy$screen$ScreenElement$RelPoint = iArr;
        }
        return iArr;
    }

    public ScreenGroup() {
        this.visible = true;
        this.elements = new LinkedHashMap();
        this.touchables = new LinkedList();
        this.horMult = HOR_AR;
        this.verMult = VER_AR;
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenElement
    public void setSize(float width, float height) {
        this.size.x = this.horMult * width;
        this.size.y = this.verMult * height;
        this.needsRecalc = true;
    }

    public void setStretchable(boolean stretchable) {
        if (this.size.x != 0.0f) {
            throw new RuntimeException("Stretchable must be set before size");
        }
        if (stretchable) {
            this.horMult = HOR_AR;
        } else {
            this.horMult = SCALE;
        }
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenElement
    public void flip() {
        this.flip = true;
        super.flip();
        for (ScreenElement e : this.elements.keySet()) {
            e.flip();
        }
    }

    public void add(ScreenElement element) {
        element.parent = this;
        if (this.flip) {
            element.flip();
        }
        this.elements.put(element, new Vector2());
        if (element instanceof Touchable) {
            this.touchables.add(element);
        }
        element.needsRecalc = true;
    }

    public ActorScreenElement add(Actor actor) {
        ActorScreenElement element = new ActorScreenElement();
        element.setActorDeprecated(actor);
        add(element);
        return element;
    }

    public TextElement addLabel(String labelMsg, float x, float y, float scale, Color color, boolean isRegular) {
        return addLabel(labelMsg, x, y, ScreenElement.RelPoint.CENTER, scale, color, isRegular, false);
    }

    public TextElement addLabel(String labelMsg, float x, float y, ScreenElement.RelPoint reference, float scale, Color color, boolean isRegular) {
        return addLabel(labelMsg, x, y, reference, scale, color, isRegular, false);
    }

    public TextElement addLabel(String labelMsg, float x, float y, float scale, Color color, boolean isRegular, boolean shadow) {
        return addLabel(labelMsg, x, y, ScreenElement.RelPoint.CENTER, null, null, scale, color, isRegular, shadow);
    }

    public TextElement addLabel(String labelMsg, float x, float y, ScreenElement.RelPoint reference, float scale, Color color, boolean isRegular, boolean shadow) {
        return addLabel(labelMsg, x, y, reference, null, null, scale, color, isRegular, shadow);
    }

    public TextElement addLabel(String labelMsg, float x, float y, ScreenElement.RelPoint reference, ScreenElement alignedTo, float scale, Color color, boolean isRegular, boolean shadow) {
        return addLabel(labelMsg, x, y, reference, alignedTo, null, scale, color, isRegular, shadow);
    }

    public TextElement addLabel(String labelMsg, float x, float y, ScreenElement.RelPoint reference, ScreenElement alignedTo, ScreenElement.RelPoint anchor, float scale, Color color, boolean isRegular, boolean shadow) {
        TextElement text;
        if (shadow) {
            text = new ShadowTextElement(labelMsg, scale, color, Color.BLACK, isRegular);
        } else {
            text = new TextElement(labelMsg, scale, color, isRegular);
        }
        text.setPosition(reference, x, y);
        if (anchor != null) {
            text.setAnchorPoint(anchor);
        }
        if (alignedTo != null) {
            text.setAlign(alignedTo);
        }
        add(text);
        return text;
    }

    public TextureElement add(TextureRegion textureRegion, float x, float y) {
        return add(textureRegion, x, y, textureRegion.getRegionWidth(), textureRegion.getRegionHeight(), ScreenElement.RelPoint.CENTER, null, ScreenElement.RelPoint.CENTER);
    }

    public TextureElement add(TextureRegion textureRegion, float x, float y, ScreenElement.RelPoint reference) {
        return add(textureRegion, x, y, textureRegion.getRegionWidth(), textureRegion.getRegionHeight(), reference, null, null);
    }

    public TextureElement add(TextureRegion textureRegion, float x, float y, ScreenElement.RelPoint reference, ScreenElement alignedTo) {
        return add(textureRegion, x, y, textureRegion.getRegionWidth(), textureRegion.getRegionHeight(), reference, alignedTo, null);
    }

    public TextureElement add(TextureRegion textureRegion, float x, float y, ScreenElement.RelPoint reference, ScreenElement.RelPoint anchor) {
        return add(textureRegion, x, y, textureRegion.getRegionWidth(), textureRegion.getRegionHeight(), reference, null, anchor);
    }

    public TextureElement add(TextureRegion textureRegion, float x, float y, ScreenElement.RelPoint reference, ScreenElement alignedTo, ScreenElement.RelPoint anchor) {
        return add(textureRegion, x, y, textureRegion.getRegionWidth(), textureRegion.getRegionHeight(), reference, alignedTo, anchor);
    }

    public TextureElement add(TextureRegion textureRegion, float x, float y, float width, float height, ScreenElement.RelPoint reference, ScreenElement alignedTo, ScreenElement.RelPoint anchor) {
        TextureElement element = new TextureElement(textureRegion);
        element.setPosition(reference, x, y);
        element.setSize(width, height);
        if (anchor != null) {
            element.setAnchorPoint(anchor);
        }
        if (alignedTo != null) {
            element.setAlign(alignedTo);
        }
        add(element);
        return element;
    }

    public SpriteElement add(Sprite sprite, float x, float y, float width, float height, float alpha) {
        SpriteElement element = new SpriteElement(sprite, alpha);
        element.setPosition(ScreenElement.RelPoint.CENTER, x, y);
        element.setSize(width, height);
        element.setAnchorPoint(ScreenElement.RelPoint.CENTER);
        add(element);
        return element;
    }

    public void addOverlay(float alphaFactor) {
        this.overlay = AssetsCache.newSolidRectangle(1, 1, new Color(0.0f, 0.0f, 0.0f, alphaFactor));
    }

    public void removeOverlay() {
        this.overlay = null;
    }

    public void addBackground(TextureRegion background) {
        setSize(background.getRegionWidth(), background.getRegionHeight());
        add(background, 0.0f, 0.0f);
    }

    public void addBackground(Color color) {
        TextureRegion bkRegion = AssetsCache.newSolidRectangle((int) this.size.x, (int) this.size.y, color);
        TextureElement bk = add(bkRegion, 0.0f, 0.0f);
        bk.disposeTexture = true;
        bk.size.x = this.size.x;
        bk.size.y = this.size.y;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public boolean touched(Vector2 touchPoint) {
        for (int i = this.touchables.size() - 1; i >= 0; i--) {
            ScreenElement screenElement = this.touchables.get(i);
            if (screenElement.visible && contains(screenElement, touchPoint)) {
                this.tmpVector2.set(touchPoint);
                this.tmpVector2.sub(this.elements.get(screenElement));
                if (((Touchable) screenElement).touched(this.tmpVector2)) {
                    return true;
                }
            }
        }
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.gaiaonline.monstergalaxy.screen.Touchable
    public boolean touchReleased(Vector2 touchPoint, Vector2 releasePoint) {
        for (int i = this.touchables.size() - 1; i >= 0; i--) {
            ScreenElement screenElement = this.touchables.get(i);
            if (screenElement.visible && contains(screenElement, releasePoint)) {
                this.tmpVector2.set(touchPoint);
                this.tmpVector2.sub(this.elements.get(screenElement));
                this.tmp2Vector2.set(releasePoint);
                this.tmp2Vector2.sub(this.elements.get(screenElement));
                if (screenElement instanceof ButtonElement) {
                    ((ButtonElement) screenElement).isPressed = false;
                }
                if (((Touchable) screenElement).touchReleased(this.tmpVector2, this.tmp2Vector2)) {
                    return true;
                }
            }
            if (screenElement instanceof ButtonElement) {
                ((ButtonElement) screenElement).isPressed = false;
            }
        }
        return false;
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenElement
    protected void update(float deltaTime) {
        for (ScreenElement e : this.elements.keySet()) {
            if (e.visible) {
                e.update(deltaTime);
            }
        }
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenElement
    protected void present(Vector2 origin, SpriteBatch batcher) {
        if (this.size.x <= 0.0f || this.size.y <= 0.0f) {
            Gdx.app.log("No SIZE error", getClass().toString());
            this.size.x = 1.0f;
            this.size.y = 1.0f;
        }
        if (this.visible) {
            if (this.overlay != null) {
                batcher.draw(this.overlay, 0.0f, 0.0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            }
            for (ScreenElement element : this.elements.keySet()) {
                if (element.visible) {
                    if (element.needsRecalc) {
                        calculate(element);
                    }
                    Vector2 actualPos = this.elements.get(element);
                    this.tmpVector2.set(actualPos);
                    this.tmpVector2.add(origin);
                    if (element.alignElement != null) {
                        ScreenElement ref = element.alignElement;
                        do {
                            this.tmpVector2.add(this.elements.get(ref));
                            ref = ref.alignElement;
                        } while (ref != null);
                    }
                    element.present(this.tmpVector2, batcher);
                }
            }
        }
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenElement
    protected void dispose() {
        for (ScreenElement screenElement : this.elements.keySet()) {
        }
        this.elements.clear();
        this.touchables.clear();
        this.elements = null;
        this.touchables = null;
        this.overlay = null;
    }

    protected void addEffect(Effect effect) {
        this.parent.addEffect(effect);
    }

    protected Vector2 getAbsolutePosition(ScreenElement e) {
        Vector2 pos = getAbsolutePosition();
        pos.add(this.elements.get(e));
        return pos;
    }

    protected void calculate(ScreenElement element) {
        Vector2 referenceSize;
        if (element.size.x <= 0.0f || element.size.y <= 0.0f) {
            Gdx.app.log("No SIZE error", getClass().toString());
            this.size.x = 1.0f;
            this.size.y = 1.0f;
        }
        if (element.alignElement != null) {
            if (element.alignElement.needsRecalc) {
                calculate(element.alignElement);
            }
            referenceSize = element.alignElement.size;
        } else {
            referenceSize = this.size;
        }
        Vector2 actualPosition = this.elements.get(element);
        calculateFromAnchorToReference(element, referenceSize, this.tmpVector2);
        calculateAnchorPoint(element, this.tmpVector2, actualPosition);
        element.needsRecalc = false;
    }

    private void calculateFromAnchorToReference(ScreenElement element, Vector2 referenceSize, Vector2 pos) {
        if (element.percPosX != 0.0f) {
            pos.x = referenceSize.x * element.percPosX;
        } else {
            switch ($SWITCH_TABLE$com$gaiaonline$monstergalaxy$screen$ScreenElement$RelPoint()[element.referencePoint.ordinal()]) {
                case 1:
                case 2:
                case 3:
                    pos.x = element.position.x * this.horMult;
                    break;
                case 4:
                case 5:
                case 6:
                    pos.x = (referenceSize.x / 2.0f) + (element.position.x * this.horMult);
                    break;
                case 7:
                case 8:
                case 9:
                    pos.x = referenceSize.x + (element.position.x * this.horMult);
                    break;
            }
        }
        if (this.percPosY != 0.0f) {
            pos.y = referenceSize.y * element.percPosY;
        }
        switch ($SWITCH_TABLE$com$gaiaonline$monstergalaxy$screen$ScreenElement$RelPoint()[element.referencePoint.ordinal()]) {
            case 1:
            case 4:
            case 7:
                pos.y = referenceSize.y + (element.position.y * VER_AR);
                break;
            case 2:
            case 5:
            case 8:
                pos.y = (referenceSize.y / 2.0f) + (element.position.y * VER_AR);
                break;
            case 3:
            case 6:
            case 9:
                pos.y = element.position.y * VER_AR;
                break;
        }
    }

    private void calculateAnchorPoint(ScreenElement element, Vector2 start, Vector2 result) {
        switch ($SWITCH_TABLE$com$gaiaonline$monstergalaxy$screen$ScreenElement$RelPoint()[element.anchorPoint.ordinal()]) {
            case 1:
            case 2:
            case 3:
                result.x = start.x;
                break;
            case 4:
            case 5:
            case 6:
                result.x = start.x - (element.size.x / 2.0f);
                break;
            case 7:
            case 8:
            case 9:
                result.x = start.x - element.size.x;
                break;
        }
        switch ($SWITCH_TABLE$com$gaiaonline$monstergalaxy$screen$ScreenElement$RelPoint()[element.anchorPoint.ordinal()]) {
            case 1:
            case 4:
            case 7:
                result.y = start.y - element.size.y;
                break;
            case 2:
            case 5:
            case 8:
                result.y = start.y - (element.size.y / 2.0f);
                break;
            case 3:
            case 6:
            case 9:
                result.y = start.y;
                break;
        }
    }

    protected boolean contains(ScreenElement e, Vector2 touchPoint) {
        Vector2 pos = this.elements.get(e);
        return pos.x < touchPoint.x && pos.y < touchPoint.y && pos.x + e.size.x > touchPoint.x && pos.y + e.size.y > touchPoint.y;
    }
}
