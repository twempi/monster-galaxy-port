package com.gaiaonline.monstergalaxy.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.battle.effects.Effect;
import com.gaiaonline.monstergalaxy.battle.effects.EffectManager;
import com.gaiaonline.monstergalaxy.battle.effects.EffectType;

/* JADX INFO: loaded from: classes.dex */
public abstract class ScreenElement {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$gaiaonline$monstergalaxy$screen$ScreenElement$RelPoint;
    protected ScreenElement alignElement;
    protected Color color;
    private Effect effect;
    protected ScreenGroup parent;
    protected float percPosX;
    protected float percPosY;
    private float scale = 1.0f;
    protected final Vector2 position = new Vector2();
    protected final Vector2 size = new Vector2();
    protected boolean visible = true;
    protected boolean needsRecalc = true;
    protected RelPoint anchorPoint = RelPoint.CENTER;
    protected RelPoint referencePoint = RelPoint.LEFT_BOTTOM;

    public enum RelPoint {
        LEFT_TOP,
        LEFT_CENTER,
        LEFT_BOTTOM,
        CENTER_TOP,
        CENTER,
        CENTER_BOTTOM,
        RIGHT_TOP,
        RIGHT_CENTER,
        RIGHT_BOTTOM;

        /* JADX INFO: renamed from: values, reason: to resolve conflict with enum method */
        public static RelPoint[] valuesCustom() {
            RelPoint[] relPointArrValuesCustom = values();
            int length = relPointArrValuesCustom.length;
            RelPoint[] relPointArr = new RelPoint[length];
            System.arraycopy(relPointArrValuesCustom, 0, relPointArr, 0, length);
            return relPointArr;
        }
    }

    protected abstract void dispose();

    protected abstract void present(Vector2 vector2, SpriteBatch spriteBatch);

    static /* synthetic */ int[] $SWITCH_TABLE$com$gaiaonline$monstergalaxy$screen$ScreenElement$RelPoint() {
        int[] iArr = $SWITCH_TABLE$com$gaiaonline$monstergalaxy$screen$ScreenElement$RelPoint;
        if (iArr == null) {
            iArr = new int[RelPoint.valuesCustom().length];
            try {
                iArr[RelPoint.CENTER.ordinal()] = 5;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[RelPoint.CENTER_BOTTOM.ordinal()] = 6;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[RelPoint.CENTER_TOP.ordinal()] = 4;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[RelPoint.LEFT_BOTTOM.ordinal()] = 3;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[RelPoint.LEFT_CENTER.ordinal()] = 2;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[RelPoint.LEFT_TOP.ordinal()] = 1;
            } catch (NoSuchFieldError e6) {
            }
            try {
                iArr[RelPoint.RIGHT_BOTTOM.ordinal()] = 9;
            } catch (NoSuchFieldError e7) {
            }
            try {
                iArr[RelPoint.RIGHT_CENTER.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                iArr[RelPoint.RIGHT_TOP.ordinal()] = 7;
            } catch (NoSuchFieldError e9) {
            }
            $SWITCH_TABLE$com$gaiaonline$monstergalaxy$screen$ScreenElement$RelPoint = iArr;
        }
        return iArr;
    }

    protected ScreenElement() {
    }

    public void setPosition(RelPoint reference, float x, float y) {
        this.referencePoint = reference;
        this.position.x = x;
        this.position.y = y;
        this.needsRecalc = true;
    }

    public void setReference(RelPoint reference) {
        this.referencePoint = reference;
    }

    public void setPositionX(float x) {
        this.position.x = x;
        this.percPosX = 0.0f;
        this.needsRecalc = true;
    }

    public void setPositionY(float y) {
        this.position.y = y;
        this.percPosY = 0.0f;
        this.needsRecalc = true;
    }

    public void setPosXAsPercentage(int percentage) {
        this.percPosX = percentage / 100.0f;
        this.needsRecalc = true;
    }

    public void setPosYAsPercentage(int percentage) {
        this.percPosY = percentage / 100.0f;
        this.needsRecalc = true;
    }

    public void setSize(float width, float height) {
        this.size.x = ResolutionManager.getScaleFactor() * width * this.scale;
        this.size.y = ResolutionManager.getScaleFactor() * height * this.scale;
        this.needsRecalc = true;
    }

    public void setSize(Vector2 size) {
        setSize(size.x, size.y);
    }

    public Vector2 getSize() {
        return this.size;
    }

    public boolean isAlreadyPositioned() {
        return !this.needsRecalc;
    }

    public void setAnchorPoint(RelPoint anchorPoint) {
        this.anchorPoint = anchorPoint;
        this.needsRecalc = true;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setAlign(ScreenElement element) {
        this.alignElement = element;
        this.needsRecalc = true;
    }

    public void setScale(float scale) {
        this.scale = scale;
        this.size.mul(scale);
        this.needsRecalc = true;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void flip() {
        this.position.x *= -1.0f;
        if (this.referencePoint == RelPoint.LEFT_TOP) {
            this.referencePoint = RelPoint.RIGHT_TOP;
        } else if (this.referencePoint == RelPoint.LEFT_CENTER) {
            this.referencePoint = RelPoint.RIGHT_CENTER;
        } else if (this.referencePoint == RelPoint.LEFT_BOTTOM) {
            this.referencePoint = RelPoint.RIGHT_BOTTOM;
        } else if (this.referencePoint == RelPoint.RIGHT_TOP) {
            this.referencePoint = RelPoint.LEFT_TOP;
        } else if (this.referencePoint == RelPoint.RIGHT_CENTER) {
            this.referencePoint = RelPoint.LEFT_CENTER;
        } else if (this.referencePoint == RelPoint.RIGHT_BOTTOM) {
            this.referencePoint = RelPoint.LEFT_BOTTOM;
        }
        if (this.anchorPoint == RelPoint.LEFT_TOP) {
            this.anchorPoint = RelPoint.RIGHT_TOP;
            return;
        }
        if (this.anchorPoint == RelPoint.LEFT_CENTER) {
            this.anchorPoint = RelPoint.RIGHT_CENTER;
            return;
        }
        if (this.anchorPoint == RelPoint.LEFT_BOTTOM) {
            this.anchorPoint = RelPoint.RIGHT_BOTTOM;
            return;
        }
        if (this.anchorPoint == RelPoint.RIGHT_TOP) {
            this.anchorPoint = RelPoint.LEFT_TOP;
        } else if (this.anchorPoint == RelPoint.RIGHT_CENTER) {
            this.anchorPoint = RelPoint.LEFT_CENTER;
        } else if (this.anchorPoint == RelPoint.RIGHT_BOTTOM) {
            this.anchorPoint = RelPoint.LEFT_BOTTOM;
        }
    }

    public Vector2 getAbsolutePosition() {
        if (this.needsRecalc) {
            throw new IllegalStateException("Element not positioned yet");
        }
        return this.parent.getAbsolutePosition(this);
    }

    public void playEffect(Effect effect, RelPoint where) {
        this.effect = effect;
        Vector2 actualPos = getAbsolutePosition();
        Vector2 effectPos = calculateForRelPoint(actualPos, where);
        this.effect.setPosition(effectPos.x, effectPos.y);
        this.effect.start();
        this.parent.addEffect(this.effect);
    }

    public void playEffect(EffectType effectType, RelPoint where) {
        playEffect(EffectManager.getInstance().getEffect(effectType), where);
    }

    protected void update(float deltaTime) {
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private Vector2 calculateForRelPoint(Vector2 start, RelPoint where) {
        Vector2 result = new Vector2();
        switch ($SWITCH_TABLE$com$gaiaonline$monstergalaxy$screen$ScreenElement$RelPoint()[where.ordinal()]) {
            case 1:
            case 2:
            case 3:
                result.x = start.x;
                break;
            case 4:
            case 5:
            case 6:
                result.x = start.x + (this.size.x / 2.0f);
                break;
            case 7:
            case 8:
            case 9:
                result.x = start.x + this.size.x;
                break;
        }
        switch ($SWITCH_TABLE$com$gaiaonline$monstergalaxy$screen$ScreenElement$RelPoint()[where.ordinal()]) {
            case 1:
            case 4:
            case 7:
                result.y = start.y + this.size.y;
                return result;
            case 2:
            case 5:
            case 8:
                result.y = start.y + (this.size.y / 2.0f);
                return result;
            case 3:
            case 6:
            case 9:
                result.y = start.y;
                return result;
            default:
                return result;
        }
    }
}
