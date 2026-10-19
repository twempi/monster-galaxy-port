package com.gaiaonline.monstergalaxy.model.moga;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gaiaonline.monstergalaxy.app.Assets;
import com.gaiaonline.monstergalaxy.app.Constants;
import com.gaiaonline.monstergalaxy.assets.Asset;
import com.gaiaonline.monstergalaxy.model.ZodiacSign;
import com.gaiaonline.monstergalaxy.model.move.AttackMove;
import com.gaiaonline.monstergalaxy.model.move.Move;
import java.io.Serializable;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class MogaType implements Serializable {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$gaiaonline$monstergalaxy$model$ZodiacSign = null;
    private static /* synthetic */ int[] $SWITCH_TABLE$com$gaiaonline$monstergalaxy$model$moga$MogaType$Rarity = null;
    private static final long serialVersionUID = -1115666148175782306L;
    private String assetName;
    private int attackRating;
    private AttackMove basicAttack;
    private Move blueCoffeeMove;
    private Move counterMove;
    private Move deadMove;
    private int defense;
    private String description;
    private Move dodgeMove;
    private Move enterBattleMove;
    private Move hitMove;
    private int id;
    private Move idleMove;
    private int islandId;
    private int maxHealth;
    private String name;
    private int offense;
    private String portraitAsset;
    private boolean purchasable;
    private Rarity rarity;
    private Move runawayFailedMove;
    private Move runawayMove;
    private SubType subType;
    private AttackMove zodiacAttack;
    private int zodiacRating;
    private ZodiacSign zodiacSign;

    static /* synthetic */ int[] $SWITCH_TABLE$com$gaiaonline$monstergalaxy$model$ZodiacSign() {
        int[] iArr = $SWITCH_TABLE$com$gaiaonline$monstergalaxy$model$ZodiacSign;
        if (iArr == null) {
            iArr = new int[ZodiacSign.valuesCustom().length];
            try {
                iArr[ZodiacSign.AQUARIUS.ordinal()] = 11;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[ZodiacSign.ARIES.ordinal()] = 1;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[ZodiacSign.CANCER.ordinal()] = 4;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[ZodiacSign.CAPRICORN.ordinal()] = 10;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[ZodiacSign.GEMINI.ordinal()] = 3;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[ZodiacSign.LEO.ordinal()] = 5;
            } catch (NoSuchFieldError e6) {
            }
            try {
                iArr[ZodiacSign.LIBRA.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                iArr[ZodiacSign.PISCES.ordinal()] = 12;
            } catch (NoSuchFieldError e8) {
            }
            try {
                iArr[ZodiacSign.SAGITTARIUS.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                iArr[ZodiacSign.SCORPIO.ordinal()] = 8;
            } catch (NoSuchFieldError e10) {
            }
            try {
                iArr[ZodiacSign.TAURUS.ordinal()] = 2;
            } catch (NoSuchFieldError e11) {
            }
            try {
                iArr[ZodiacSign.VIRGO.ordinal()] = 6;
            } catch (NoSuchFieldError e12) {
            }
            $SWITCH_TABLE$com$gaiaonline$monstergalaxy$model$ZodiacSign = iArr;
        }
        return iArr;
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$gaiaonline$monstergalaxy$model$moga$MogaType$Rarity() {
        int[] iArr = $SWITCH_TABLE$com$gaiaonline$monstergalaxy$model$moga$MogaType$Rarity;
        if (iArr == null) {
            iArr = new int[Rarity.valuesCustom().length];
            try {
                iArr[Rarity.BEGINNER.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[Rarity.COMMON.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[Rarity.EPIC.ordinal()] = 6;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[Rarity.LEGENDARY.ordinal()] = 7;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[Rarity.RARE.ordinal()] = 4;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[Rarity.SUPER_RARE.ordinal()] = 5;
            } catch (NoSuchFieldError e6) {
            }
            try {
                iArr[Rarity.UNCOMMON.ordinal()] = 3;
            } catch (NoSuchFieldError e7) {
            }
            $SWITCH_TABLE$com$gaiaonline$monstergalaxy$model$moga$MogaType$Rarity = iArr;
        }
        return iArr;
    }

    public enum Rarity {
        BEGINNER("BEGINNER", "Beginner", 0),
        COMMON("COMMON", "Common", 1),
        UNCOMMON("UNCOMMON", "Uncommon", 2),
        RARE("RARE", "Rare", 3),
        SUPER_RARE("SUPER RARE", "SuperRare", 4),
        EPIC("EPIC", "Epic", 5),
        LEGENDARY("LEGENDARY", "Legendary", 6);

        private String camelCaseName;
        private String name;
        private int order;

        /* JADX INFO: renamed from: values, reason: to resolve conflict with enum method */
        public static Rarity[] valuesCustom() {
            Rarity[] rarityArrValuesCustom = values();
            int length = rarityArrValuesCustom.length;
            Rarity[] rarityArr = new Rarity[length];
            System.arraycopy(rarityArrValuesCustom, 0, rarityArr, 0, length);
            return rarityArr;
        }

        Rarity(String name, String camelCaseName, int order) {
            this.name = name;
            this.camelCaseName = camelCaseName;
            this.order = order;
        }

        public String getName() {
            return this.name;
        }

        public String getCamelCaseName() {
            return this.camelCaseName;
        }

        public int getOrder() {
            return this.order;
        }

        public static Rarity getValue(String name) {
            for (Rarity rarity : valuesCustom()) {
                if (rarity.getName().equalsIgnoreCase(name)) {
                    return rarity;
                }
            }
            return null;
        }
    }

    public enum SubType {
        NORMAL("normal"),
        LUCKY("lucky");

        private String name;

        /* JADX INFO: renamed from: values, reason: to resolve conflict with enum method */
        public static SubType[] valuesCustom() {
            SubType[] subTypeArrValuesCustom = values();
            int length = subTypeArrValuesCustom.length;
            SubType[] subTypeArr = new SubType[length];
            System.arraycopy(subTypeArrValuesCustom, 0, subTypeArr, 0, length);
            return subTypeArr;
        }

        SubType(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public static SubType getValue(String name) {
            for (SubType subType : valuesCustom()) {
                if (subType.getName().equalsIgnoreCase(name)) {
                    return subType;
                }
            }
            return null;
        }
    }

    protected int getMaxHealth() {
        return this.maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Rarity getRarity() {
        return this.rarity;
    }

    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    public int getMogaCashPrice() {
        return getCaptureCost() * 2;
    }

    public boolean isPurchasable() {
        return this.purchasable;
    }

    public void setPurchasable(boolean purchasable) {
        this.purchasable = purchasable;
    }

    public String getRarityAssetName() {
        switch ($SWITCH_TABLE$com$gaiaonline$monstergalaxy$model$moga$MogaType$Rarity()[this.rarity.ordinal()]) {
            case 1:
                return "beginner";
            case 2:
                return "common";
            case 3:
                return "uncommon";
            case 4:
                return "rare";
            case 5:
                return "superrare";
            case 6:
                return "epic";
            case 7:
                return "legendary";
            default:
                return "beginner";
        }
    }

    public void setRarityColor(Color color) {
        switch ($SWITCH_TABLE$com$gaiaonline$monstergalaxy$model$moga$MogaType$Rarity()[this.rarity.ordinal()]) {
            case 1:
                translateColor(color, 255, 255, 255);
                break;
            case 2:
                translateColor(color, 66, 253, 67);
                break;
            case 3:
                translateColor(color, 58, 213, 252);
                break;
            case 4:
                translateColor(color, 0, 101, 255);
                break;
            case 5:
                translateColor(color, Input.Keys.F8, 96, Input.Keys.F8);
                break;
            case 6:
                translateColor(color, 227, 0, Opcodes.PUTSTATIC);
                break;
            case 7:
                translateColor(color, 255, 48, 48);
                break;
            default:
                Gdx.app.error("Rarity", "Unknown color for rarity: " + this.rarity);
                break;
        }
    }

    public void setZodiacColor(Color color) {
        switch ($SWITCH_TABLE$com$gaiaonline$monstergalaxy$model$ZodiacSign()[this.zodiacSign.ordinal()]) {
            case 1:
            case 5:
            case 9:
                translateColor(color, 255, 0, 0);
                break;
            case 2:
            case 6:
            case 10:
                translateColor(color, 255, Opcodes.IFGE, 0);
                break;
            case 3:
            case 7:
            case 11:
                translateColor(color, 0, 255, Opcodes.IFEQ);
                break;
            case 4:
            case 8:
            case 12:
                translateColor(color, 0, Opcodes.GETSTATIC, 255);
                break;
            default:
                Gdx.app.error("Zodiac", "Unknown color for sign: " + this.zodiacSign);
                break;
        }
    }

    private void translateColor(Color color, int r, int g, int b) {
        color.r = r / 255.0f;
        color.g = g / 255.0f;
        color.b = b / 255.0f;
        color.a = 1.0f;
    }

    public ZodiacSign getZodiacSign() {
        return this.zodiacSign;
    }

    public void setZodiacSign(ZodiacSign zodiacSign) {
        this.zodiacSign = zodiacSign;
    }

    public int getOffense() {
        return this.offense;
    }

    public void setOffense(int offense) {
        this.offense = offense;
    }

    public int getDefense() {
        return this.defense;
    }

    public int getCaptureCost() {
        return Constants.captureCosts.get(getRarity()).intValue();
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public String getPortraitAssetName() {
        return this.portraitAsset.replace(".png", "");
    }

    public TextureRegion getPortrait() {
        return Assets.loadTexture(this.portraitAsset.substring(0, this.portraitAsset.indexOf(".png")).replace('_', '.'));
    }

    public void setPortraitAsset(String portraitAsset) {
        this.portraitAsset = portraitAsset;
    }

    public TextureRegion getGlowImage() {
        return Assets.loadTexture("b." + this.assetName.replaceAll("_", ".").substring(0, this.assetName.indexOf(".png")));
    }

    public String getAssetName() {
        return this.assetName.substring(0, this.assetName.indexOf(".png"));
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public Asset getAsset() {
        return new Asset("entireMogas", getAssetName(), "png");
    }

    public String getDescription() {
        return this.description.replaceAll("\\Q<q>\\E", "\"").replaceAll("\\Q</q>\\E", "\"");
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAttackRating() {
        return this.attackRating;
    }

    public void setAttackRating(int attackRating) {
        this.attackRating = attackRating;
    }

    public int getZodiacRating() {
        return this.zodiacRating;
    }

    public void setZodiacRating(int zodiacRating) {
        this.zodiacRating = zodiacRating;
    }

    public float getZodiacRecharge() {
        Integer turns = Constants.zodiacRecharge.get(Integer.valueOf(getZodiacRating()));
        if (turns == null) {
            turns = 5;
        }
        return 100.0f / Integer.valueOf(Math.max(1, turns.intValue())).intValue();
    }

    public AttackMove getBasicAttack() {
        return this.basicAttack;
    }

    public void setBasicAttack(AttackMove basicAttack) {
        this.basicAttack = basicAttack;
    }

    public AttackMove getZodiacAttack() {
        return this.zodiacAttack;
    }

    public void setZodiacAttack(AttackMove zodiacAttack) {
        this.zodiacAttack = zodiacAttack;
    }

    public Move getDodgeMove() {
        return this.dodgeMove;
    }

    public void setDodgeMove(Move dodgeMove) {
        this.dodgeMove = dodgeMove;
    }

    public Move getCounterMove() {
        return this.counterMove;
    }

    public void setCounterMove(Move counterMove) {
        this.counterMove = counterMove;
    }

    public Move getIdleMove() {
        return this.idleMove;
    }

    public void setIdleMove(Move idleMove) {
        this.idleMove = idleMove;
    }

    public Move getHitMove() {
        return this.hitMove;
    }

    public void setHitMove(Move hitMove) {
        this.hitMove = hitMove;
    }

    public Move getDeadMove() {
        return this.deadMove;
    }

    public void setDeadMove(Move deadMove) {
        this.deadMove = deadMove;
    }

    public Move getEnterBattleMove() {
        return this.enterBattleMove;
    }

    public void setEnterBattleMove(Move enterBattleMove) {
        this.enterBattleMove = enterBattleMove;
    }

    public Move getRunawayMove() {
        return this.runawayMove;
    }

    public void setRunawayMove(Move runawayMove) {
        this.runawayMove = runawayMove;
    }

    public Move getRunawayFailedMove() {
        return this.runawayFailedMove;
    }

    public void setRunawayFailedMove(Move runawayFailedMove) {
        this.runawayFailedMove = runawayFailedMove;
    }

    public Move getBlueCoffeeMove() {
        return this.blueCoffeeMove;
    }

    public void setBlueCoffeeMove(Move blueCoffeeMove) {
        this.blueCoffeeMove = blueCoffeeMove;
    }

    public int getFixedXP() {
        switch ($SWITCH_TABLE$com$gaiaonline$monstergalaxy$model$moga$MogaType$Rarity()[getRarity().ordinal()]) {
            case 1:
                return Constants.fixedXPBeginner;
            case 2:
                return Constants.fixedXPCommon;
            case 3:
                return Constants.fixedXPUncommon;
            case 4:
                return Constants.fixedXPRare;
            case 5:
                return Constants.fixedXPSuperRare;
            case 6:
                return Constants.fixedXPEpic;
            case 7:
                return Constants.fixedXPLegendary;
            default:
                return 0;
        }
    }

    public float getCaptureBonus() {
        switch ($SWITCH_TABLE$com$gaiaonline$monstergalaxy$model$moga$MogaType$Rarity()[this.rarity.ordinal()]) {
            case 1:
                float captureBonus = Constants.captureBonusBeginner;
            case 2:
                float captureBonus2 = Constants.captureBonusCommon;
            case 3:
                float captureBonus3 = Constants.captureBonusUncommon;
            case 4:
                float captureBonus4 = Constants.captureBonusRare;
            case 5:
                float captureBonus5 = Constants.captureBonusSuperRare;
            case 6:
                float captureBonus6 = Constants.captureBonusEpic;
            case 7:
                float captureBonus7 = Constants.captureBonusLegendary;
                return captureBonus7;
            default:
                return 0.0f;
        }
    }

    public Color getRarityColor() {
        switch ($SWITCH_TABLE$com$gaiaonline$monstergalaxy$model$moga$MogaType$Rarity()[this.rarity.ordinal()]) {
            case 1:
                Color color = new Color(1.0f, 1.0f, 1.0f, 1.0f);
                return color;
            case 2:
                Color color2 = new Color(0.26f, 1.0f, 0.27f, 1.0f);
                return color2;
            case 3:
                Color color3 = new Color(0.23f, 0.83f, 0.99f, 1.0f);
                return color3;
            case 4:
                Color color4 = new Color(0.0f, 0.65f, 1.0f, 1.0f);
                return color4;
            case 5:
                Color color5 = new Color(0.98f, 0.38f, 0.98f, 1.0f);
                return color5;
            case 6:
                Color color6 = new Color(0.89f, 0.0f, 0.7f, 1.0f);
                return color6;
            case 7:
                Color color7 = new Color(1.0f, 0.19f, 0.19f, 1.0f);
                return color7;
            default:
                Color color8 = new Color(255.0f, 255.0f, 255.0f, 1.0f);
                return color8;
        }
    }

    public void setIslandId(int id) {
        this.islandId = id;
    }

    public int getIslandId() {
        return this.islandId;
    }

    public void setSubType(SubType subType) {
        this.subType = subType;
    }

    public SubType getSubType() {
        return this.subType;
    }
}
