package com.gaiaonline.monstergalaxy.storage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.notifications.MogaNotification;
import com.gaiaonline.monstergalaxy.app.notifications.NotificationText;
import com.gaiaonline.monstergalaxy.i18n.I18nManager;
import com.gaiaonline.monstergalaxy.model.ZodiacSign;
import com.gaiaonline.monstergalaxy.model.map.Encounter;
import com.gaiaonline.monstergalaxy.model.map.EncounterAppearance;
import com.gaiaonline.monstergalaxy.model.map.Island;
import com.gaiaonline.monstergalaxy.model.map.Node;
import com.gaiaonline.monstergalaxy.model.minigame.SlotMachinePrize;
import com.gaiaonline.monstergalaxy.model.minigame.SlotMachineReel;
import com.gaiaonline.monstergalaxy.model.minigame.Slotmachine;
import com.gaiaonline.monstergalaxy.model.moga.MogaType;
import com.gaiaonline.monstergalaxy.model.move.AttackMove;
import com.gaiaonline.monstergalaxy.model.quest.Quest;
import com.gaiaonline.monstergalaxy.model.quest.QuestDialog;
import com.gaiaonline.monstergalaxy.model.quest.RewardItem;
import com.gaiaonline.monstergalaxy.model.shop.Product;
import com.gaiaonline.monstergalaxy.model.shop.ProductType;
import com.gaiaonline.monstergalaxy.settings.NotificationType;
import com.sessionm.ui.SessionMActivity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class SQLiteHelper extends SQLiteOpenHelper implements StorageHelper {
    private final String TAG;
    private SQLiteDatabase db;
    private List<Island> islands;
    private Slotmachine slotMachine;

    public SQLiteHelper(Context context) throws Throwable {
        super(context, StorageHelper.DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 28);
        this.TAG = "SQLiteHelper";
        String targetFilePath = Environment.getDataDirectory() + "/data/" + context.getPackageName() + "/databases/" + StorageHelper.DATABASE_NAME;
        File dbFile = new File(targetFilePath);
        String targetLocale = currentLocale(context);
        String sourceDbName = "monsterGalaxy_" + targetLocale + StorageHelper.DATABASE_SUFFIX;
        if (!dbFile.exists()) {
            copyDatabase(context, sourceDbName, targetFilePath);
        }
        this.db = openDatabase(targetFilePath);
        if (this.db.getVersion() != 28 || !targetLocale.equals(getDBLocale())) {
            this.db.close();
            dbFile.delete();
            copyDatabase(context, sourceDbName, targetFilePath);
            this.db = openDatabase(targetFilePath);
        }
    }

    private String currentLocale(Context context) {
        String intendedLocale = I18nManager.getLocale();
        if (intendedLocale != null) {
            return intendedLocale;
        }
        String intendedLocale2 = I18nManager.getSystemLocale();
        String intendedAssetName = "monsterGalaxy_" + intendedLocale2 + StorageHelper.DATABASE_SUFFIX;
        try {
            return Arrays.asList(context.getAssets().list("")).contains(intendedAssetName) ? intendedLocale2 : I18nManager.DEFAULT_LOCALE;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private SQLiteDatabase openDatabase(String targetFilePath) {
        return SQLiteDatabase.openDatabase(targetFilePath, null, 16);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase db) {
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
    }

    private void copyDatabase(Context context, String sourceDbName, String targetDbFile) throws Throwable {
        InputStream is = null;
        OutputStream os = null;
        try {
            try {
                getWritableDatabase().close();
                is = context.getAssets().open(sourceDbName);
                OutputStream os2 = new FileOutputStream(targetDbFile);
                try {
                    byte[] buffer = new byte[1024];
                    while (true) {
                        int length = is.read(buffer);
                        if (length <= 0) {
                            break;
                        } else {
                            os2.write(buffer, 0, length);
                        }
                    }
                    os2.flush();
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            Log.e("SQLiteHelper", "Error while trying to close input stream");
                        }
                    }
                    if (os2 != null) {
                        try {
                            is.close();
                        } catch (IOException e2) {
                            Log.e("SQLiteHelper", "Error while trying to close output stream");
                        }
                    }
                } catch (Exception e3) {
                    e = e3;
                    os = os2;
                    e.printStackTrace();
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e4) {
                            Log.e("SQLiteHelper", "Error while trying to close input stream");
                        }
                    }
                    if (os != null) {
                        try {
                            is.close();
                        } catch (IOException e5) {
                            Log.e("SQLiteHelper", "Error while trying to close output stream");
                        }
                    }
                } catch (Throwable th) {
                    th = th;
                    os = os2;
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e6) {
                            Log.e("SQLiteHelper", "Error while trying to close input stream");
                        }
                    }
                    if (os != null) {
                        try {
                            is.close();
                        } catch (IOException e7) {
                            Log.e("SQLiteHelper", "Error while trying to close output stream");
                        }
                    }
                    throw th;
                }
            } catch (Exception e8) {
                e = e8;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    @Override // com.gaiaonline.monstergalaxy.storage.StorageHelper
    public AttackMove getAttackMove(int id) {
        AttackMove move = null;
        Cursor cursor = this.db.rawQuery(StorageHelper.QUERY_ATTACK_MOVE_BY_ID, new String[]{String.valueOf(id)});
        if (cursor.moveToNext()) {
            move = new AttackMove();
            move.setId(id);
            move.setName(cursor.getString(0));
            move.setType(cursor.getString(1));
            move.setPower(cursor.getString(2));
            move.setModifierP(cursor.getFloat(3));
            move.setModifierQ(cursor.getFloat(4));
            move.setMaxDamage(cursor.getFloat(5));
            move.setLevelMultiplier(cursor.getInt(6));
            move.setChargeAnimation(cursor.getString(7));
            move.setChargeEffect(cursor.getString(8));
            move.setAnimation(cursor.getString(9));
            move.setHitVisual(cursor.getString(10));
            move.setAttackSound(cursor.getString(11));
            move.setHitSound(cursor.getString(12));
        }
        cursor.close();
        return move;
    }

    @Override // com.gaiaonline.monstergalaxy.storage.StorageHelper
    public Map<String, List<String>> getBattleAudio() {
        Map<String, List<String>> battleAudio = new HashMap<>();
        Cursor cursor = null;
        try {
            try {
                cursor = this.db.rawQuery(StorageHelper.QUERY_ALL_BATTLE_AUDIO, new String[0]);
                while (cursor.moveToNext()) {
                    String type = cursor.getString(0);
                    String asset = cursor.getString(1);
                    List<String> audioList = battleAudio.get(type);
                    if (audioList == null) {
                        audioList = new ArrayList<>();
                        battleAudio.put(type, audioList);
                    }
                    audioList.add(asset);
                }
                if (cursor != null) {
                    try {
                        cursor.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return battleAudio;
            } catch (Exception e2) {
                throw new RuntimeException(e2);
            }
        } catch (Throwable th) {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            }
            throw th;
        }
    }

    @Override // com.gaiaonline.monstergalaxy.storage.StorageHelper
    public List<Product> getProducts() {
        List<Product> products = new ArrayList<>();
        Cursor cursor = null;
        try {
            try {
                cursor = this.db.rawQuery(StorageHelper.QUERY_ALL_SHOP_PRODUCTS, new String[0]);
                while (cursor.moveToNext()) {
                    String typeString = cursor.getString(0);
                    int quantity = Integer.parseInt(cursor.getString(1));
                    String description = cursor.getString(2);
                    String title = cursor.getString(3);
                    float price = Float.parseFloat(cursor.getString(4));
                    String id = cursor.getString(5);
                    ProductType type = ProductType.getById(typeString);
                    products.add(new Product(id, title, description, price, type, quantity));
                }
                if (cursor != null) {
                    try {
                        cursor.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return products;
            } catch (Exception e2) {
                throw new RuntimeException(e2);
            }
        } catch (Throwable th) {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            }
            throw th;
        }
    }

    @Override // com.gaiaonline.monstergalaxy.storage.StorageHelper
    public MogaType getMogaType(int id) {
        MogaType mogaType = null;
        Cursor cursor = this.db.rawQuery(StorageHelper.QUERY_MOGA_TYPE_BY_ID, new String[]{String.valueOf(id)});
        if (cursor.moveToNext()) {
            mogaType = buildMogaType(cursor);
        }
        cursor.close();
        return mogaType;
    }

    @Override // com.gaiaonline.monstergalaxy.storage.StorageHelper
    public List<MogaType> getMogaTypesBySubType(MogaType.SubType subType) {
        List<MogaType> mogaTypes = new ArrayList<>();
        Cursor cursor = this.db.rawQuery(StorageHelper.QUERY_MOGA_TYPES_BY_SUB_TYPE, new String[]{subType.getName()});
        while (cursor.moveToNext()) {
            mogaTypes.add(buildMogaType(cursor));
        }
        cursor.close();
        return mogaTypes;
    }

    private MogaType buildMogaType(Cursor cursor) {
        MogaType mogaType = new MogaType();
        mogaType.setId(cursor.getInt(0));
        mogaType.setName(cursor.getString(1));
        mogaType.setDescription(cursor.getString(2));
        mogaType.setRarity(MogaType.Rarity.getValue(cursor.getString(3)));
        mogaType.setOffense(cursor.getInt(4));
        mogaType.setDefense(cursor.getInt(5));
        mogaType.setZodiacSign(ZodiacSign.getValue(cursor.getString(6).toUpperCase()));
        mogaType.setZodiacRating(cursor.getInt(7));
        mogaType.setMaxHealth(cursor.getInt(8));
        mogaType.setPortraitAsset(cursor.getString(9));
        mogaType.setAssetName(cursor.getString(10));
        mogaType.setBasicAttack(getAttackMove(cursor.getInt(11)));
        mogaType.setZodiacAttack(getAttackMove(cursor.getInt(12)));
        mogaType.setIslandId(cursor.getInt(13));
        mogaType.setSubType(MogaType.SubType.getValue(cursor.getString(14)));
        mogaType.setPurchasable(cursor.getInt(15) == 1);
        mogaType.setLocalizedName(cursor.getString(16));
        return mogaType;
    }

    @Override // com.gaiaonline.monstergalaxy.storage.StorageHelper
    public int getMogaTypeCount(MogaType.SubType subType) {
        Cursor cursor = this.db.rawQuery(StorageHelper.QUERY_MOGA_TYPES_COUNT, new String[]{subType.getName()});
        cursor.moveToFirst();
        int numberOfMogaTypes = cursor.getInt(0);
        cursor.close();
        return numberOfMogaTypes;
    }

    @Override // com.gaiaonline.monstergalaxy.storage.StorageHelper
    public Map<Integer, Float> getLevelDiffMultipliers() {
        Map<Integer, Float> map = new HashMap<>();
        Cursor cursor = this.db.rawQuery(StorageHelper.QUERY_ALL_LEVEL_DIFF_MULTIPLIERS, new String[0]);
        while (cursor.moveToNext()) {
            map.put(Integer.valueOf(cursor.getInt(0)), Float.valueOf(cursor.getFloat(1)));
        }
        cursor.close();
        return map;
    }

    @Override // com.gaiaonline.monstergalaxy.storage.StorageHelper
    public Node getNode(int id) {
        Node node = null;
        Cursor cursor = this.db.rawQuery(StorageHelper.QUERY_NODE_BY_ID, new String[]{String.valueOf(id)});
        if (cursor.moveToNext()) {
            node = buildNode(cursor);
        }
        cursor.close();
        return node;
    }

    @Override // com.gaiaonline.monstergalaxy.storage.StorageHelper
    public Quest getQuest(int id) {
        Quest quest = null;
        Cursor cursor = this.db.rawQuery(StorageHelper.QUERY_QUEST_BY_ID, new String[]{String.valueOf(id)});
        if (cursor.moveToNext()) {
            quest = buildQuest(cursor);
        }
        cursor.close();
        return quest;
    }

    @Override // com.gaiaonline.monstergalaxy.storage.StorageHelper
    public List<Quest> getQuestByGrantQuestId(int grantQuestId) {
        List<Quest> quests = new ArrayList<>();
        Cursor cursor = this.db.rawQuery(StorageHelper.QUERY_QUEST_BY_GRANT_QUEST_ID, new String[]{String.valueOf(grantQuestId)});
        if (cursor.moveToNext()) {
            quests.add(buildQuest(cursor));
        }
        cursor.close();
        return quests;
    }

    @Override // com.gaiaonline.monstergalaxy.storage.StorageHelper
    public Quest getQuestByBeatNodeId(int beatNodeId) {
        Quest quest = null;
        Cursor cursor = this.db.rawQuery(StorageHelper.QUERY_QUEST_BY_BEAT_NODE_ID, new String[]{String.valueOf(beatNodeId)});
        if (cursor.moveToNext()) {
            quest = buildQuest(cursor);
        }
        cursor.close();
        return quest;
    }

    @Override // com.gaiaonline.monstergalaxy.storage.StorageHelper
    public Quest getQuestByGrantNodeId(int grantNodeId) {
        Quest quest = null;
        Cursor cursor = this.db.rawQuery(StorageHelper.QUERY_QUEST_BY_GRANT_NODE_ID, new String[]{String.valueOf(grantNodeId)});
        if (cursor.moveToNext()) {
            quest = buildQuest(cursor);
        }
        cursor.close();
        return quest;
    }

    private Quest buildQuest(Cursor cursor) {
        Quest quest = new Quest();
        quest.setId(cursor.getInt(0));
        quest.setName(cursor.getString(1));
        quest.setType(Quest.Type.valueOf(cursor.getString(2).toUpperCase()));
        quest.setNpcName(cursor.getString(3));
        quest.setNpcAsset(cursor.getString(4));
        quest.setStartDialog(new QuestDialog(cursor.getString(5)));
        quest.setDescriptionDialog(new QuestDialog(cursor.getString(6)));
        quest.setCompleteDialog(new QuestDialog(cursor.getString(7)));
        quest.setBeatNodeId(cursor.getInt(8));
        quest.setContenderMogaTypeId(cursor.getInt(9));
        quest.setNumberOfContenders(cursor.getInt(10));
        quest.setRewardItem(new RewardItem(cursor.getInt(11), cursor.getInt(12)));
        quest.setGrantNodeId(cursor.getInt(13));
        quest.setGrantQuestId(cursor.getInt(14));
        quest.setRestrictionNodeIds(cursor.getString(15));
        quest.setRestrictionQuestId(cursor.getInt(16));
        quest.setIslandToWinStars(getIsland(cursor.getInt(17)));
        quest.setNumberOfStarsToWinInIsland(cursor.getInt(18));
        quest.setBlockedNodeId(cursor.getInt(19));
        quest.setMusic(cursor.getString(20));
        return quest;
    }

    @Override // com.gaiaonline.monstergalaxy.storage.StorageHelper
    public Island getIsland(int id) {
        Island island = null;
        Cursor cursor = this.db.rawQuery(StorageHelper.QUERY_ISLAND_BY_ID, new String[]{String.valueOf(id)});
        if (cursor.moveToNext()) {
            island = buildIsland(cursor);
        }
        cursor.close();
        return island;
    }

    @Override // com.gaiaonline.monstergalaxy.storage.StorageHelper
    public synchronized List<Island> getAllIslands() {
        if (this.islands == null) {
            this.islands = new ArrayList();
            Cursor cursor = this.db.rawQuery(StorageHelper.QUERY_ALL_ISLANDS, new String[0]);
            while (cursor.moveToNext()) {
                Island island = buildIsland(cursor);
                this.islands.add(island);
            }
            cursor.close();
        }
        return this.islands;
    }

    private Island buildIsland(Cursor cursor) {
        Island island = new Island();
        island.setId(cursor.getInt(8));
        island.setName(cursor.getString(0));
        island.setLocalizedName(cursor.getString(cursor.getColumnIndex("localized_name")));
        island.setStars(cursor.getInt(1));
        island.setMapAssetName(cursor.getString(2));
        island.setMaxNumberOfMogas(cursor.getInt(3));
        island.setQuestIds(getIntArray(cursor.getString(4)));
        island.setMaxLevel(cursor.getInt(5));
        island.setPosition(cursor.getFloat(6), cursor.getFloat(7));
        island.setAmbienceAudio(cursor.getString(9));
        island.setStingAudio(cursor.getString(10));
        String constantName = "islandUnlockMogas_" + island.getName();
        int constantValue = Integer.parseInt(getConstantValue(constantName));
        island.setNumberOfRequiredMogasToUnlock(constantValue);
        island.setNodes(getIslandNodes(island));
        return island;
    }

    @Override // com.gaiaonline.monstergalaxy.storage.StorageHelper
    public List<Node> getIslandNodes(Island island) {
        List<Node> nodes = new ArrayList<>();
        Cursor cursor = this.db.rawQuery(StorageHelper.QUERY_NODES_BY_ISLAND_ID, new String[]{String.valueOf(island.getId())});
        while (cursor.moveToNext()) {
            Node node = buildNode(cursor);
            node.setIsland(island);
            nodes.add(node);
        }
        cursor.close();
        return nodes;
    }

    private Node buildNode(Cursor cursor) {
        Node node = new Node();
        node.setId(cursor.getInt(0));
        node.setName(cursor.getString(1));
        node.setDescription(cursor.getString(2));
        node.setAdjacentNodeIds(getIntArray(cursor.getString(3)));
        node.setEnergyCost(cursor.getInt(4));
        node.setThumbnailAsset(cursor.getString(5));
        node.setBackgroundAsset(cursor.getString(6));
        node.setDropId(cursor.getInt(7));
        node.setDropProbability(cursor.getFloat(8));
        node.setPosition(cursor.getFloat(9), cursor.getFloat(10));
        return node;
    }

    @Override // com.gaiaonline.monstergalaxy.storage.StorageHelper
    public List<Encounter> getEncounters(int nodeId) {
        List<Encounter> encounters = new ArrayList<>();
        Cursor cursor = this.db.rawQuery(StorageHelper.QUERY_ENCOUNTERS_BY_NODE, new String[]{String.valueOf(nodeId)});
        while (cursor.moveToNext()) {
            Encounter encounter = new Encounter();
            encounter.setId(cursor.getInt(0));
            encounter.setNodeId(nodeId);
            encounter.setQuestId(cursor.getInt(1));
            try {
                encounter.setCategory(Encounter.Category.valueOf(cursor.getString(2).toUpperCase()));
            } catch (Exception e) {
                Log.e(Game.TAG, "Encounter: Error parsing category.");
            }
            encounter.setCapture(cursor.getString(3));
            encounter.setMusicAsset(cursor.getString(4));
            encounter.setMusicType(cursor.getString(5));
            encounter.setAppearances(getEncounterAppearances(cursor.getInt(0)));
            encounters.add(encounter);
        }
        cursor.close();
        return encounters;
    }

    private List<EncounterAppearance> getEncounterAppearances(int encounterId) {
        List<EncounterAppearance> appearances = new ArrayList<>();
        Cursor cursor = this.db.rawQuery(StorageHelper.QUERY_ENCOUNTER_APPEARANCES, new String[]{String.valueOf(encounterId)});
        while (cursor.moveToNext()) {
            EncounterAppearance appearance = new EncounterAppearance();
            appearance.setMogaTypeId(cursor.getInt(1));
            appearance.setLevel(cursor.getInt(2));
            appearance.setN(cursor.getInt(3));
            appearance.setProbability(cursor.getFloat(4));
            appearances.add(appearance);
        }
        cursor.close();
        return appearances;
    }

    @Override // com.gaiaonline.monstergalaxy.storage.StorageHelper
    public String getConstantValue(String name) {
        String value = null;
        Cursor cursor = this.db.rawQuery(StorageHelper.QUERY_CONSTANT_BY_NAME, new String[]{name});
        if (cursor.moveToNext()) {
            value = cursor.getString(0);
        }
        cursor.close();
        return value;
    }

    @Override // com.gaiaonline.monstergalaxy.storage.StorageHelper
    public float getZodiacSignMultiplier(String playerSign, String attackerSign) {
        float value = 0.0f;
        Cursor cursor = this.db.rawQuery("SELECT multiplier FROM zodiac_multiplier WHERE player_name = ? AND attacker_name = ?", new String[]{playerSign, attackerSign});
        if (cursor.moveToNext()) {
            value = cursor.getFloat(0);
        }
        cursor.close();
        return value;
    }

    @Override // com.gaiaonline.monstergalaxy.storage.StorageHelper
    public Set<MogaNotification> getNotifications() {
        Set<MogaNotification> notifications = new HashSet<>();
        Cursor cursor = this.db.rawQuery(StorageHelper.QUERY_ALL_NOTIFICATIONS, new String[0]);
        while (cursor.moveToNext()) {
            MogaNotification notification = MogaNotification.newInstance(NotificationType.getByCode(cursor.getString(0)), cursor.getInt(2));
            int notificationId = cursor.getInt(2);
            notification.setTexts(getTextsByNotificationId(notificationId));
            notification.setIslandId(cursor.getInt(1));
            notifications.add(notification);
        }
        cursor.close();
        return notifications;
    }

    private List<NotificationText> getTextsByNotificationId(int notificationId) {
        Cursor cursor = this.db.rawQuery(StorageHelper.QUERY_TEXTS_BY_NOTIFICATION_ID, new String[]{String.valueOf(notificationId)});
        List<NotificationText> texts = new ArrayList<>();
        while (cursor.moveToNext()) {
            String title = cursor.getString(1);
            String message = cursor.getString(2);
            String buttonText = cursor.getString(3);
            NotificationText text = new NotificationText(0, message, title, buttonText);
            texts.add(text);
        }
        cursor.close();
        return texts;
    }

    private int[] getIntArray(String value) {
        String[] stringArray = value.split(",");
        int[] intArray = new int[stringArray.length];
        for (int i = 0; i < stringArray.length; i++) {
            intArray[i] = Integer.parseInt(stringArray[i].trim());
        }
        return intArray;
    }

    @Override // com.gaiaonline.monstergalaxy.storage.StorageHelper
    public Slotmachine getSlotMachine() {
        if (this.slotMachine != null) {
            return this.slotMachine;
        }
        this.slotMachine = new Slotmachine();
        Cursor cursor = this.db.rawQuery(StorageHelper.QUERY_SLOT_MACHINE, null);
        if (cursor.moveToNext()) {
            this.slotMachine.setCoolDownSeconds(cursor.getInt(cursor.getColumnIndex("cooldown_seconds")));
            this.slotMachine.setCost(cursor.getFloat(cursor.getColumnIndex("cost")));
            this.slotMachine.setReels(getSlotMachineReels());
        }
        this.slotMachine.init();
        cursor.close();
        return this.slotMachine;
    }

    private List<SlotMachineReel> getSlotMachineReels() {
        List<SlotMachineReel> reels = new ArrayList<>();
        Cursor cursor = this.db.rawQuery(StorageHelper.QUERY_REELS, null);
        while (cursor.moveToNext()) {
            SlotMachineReel reel = new SlotMachineReel();
            String name = cursor.getString(cursor.getColumnIndex("name"));
            reel.setName(name);
            reel.setProbability(cursor.getFloat(cursor.getColumnIndex("probability")));
            reel.setPrizes(getSlotMachinePrizes(name.replaceAll(" ", "").toLowerCase()));
            reels.add(reel);
        }
        cursor.close();
        return reels;
    }

    private List<SlotMachinePrize> getSlotMachinePrizes(String reelName) {
        List<SlotMachinePrize> prizes = new ArrayList<>();
        Cursor cursor = this.db.rawQuery(StorageHelper.QUERY_REEL_PRIZES, new String[]{reelName});
        while (cursor.moveToNext()) {
            SlotMachinePrize prize = new SlotMachinePrize();
            prize.setBlueCoffees(cursor.getInt(cursor.getColumnIndex("bluecoffees")));
            prize.setDisplayName(cursor.getString(cursor.getColumnIndex("displayName")));
            prize.setId(cursor.getInt(cursor.getColumnIndex("prize_id")));
            prize.setMogaTypeId(cursor.getInt(cursor.getColumnIndex("mogaType")));
            prize.setMogaCash(cursor.getInt(cursor.getColumnIndex("mogacash")));
            prize.setName(cursor.getString(cursor.getColumnIndex("name")));
            prize.setStarSeeds(cursor.getInt(cursor.getColumnIndex("starseeds")));
            prize.setType(cursor.getString(cursor.getColumnIndex(SessionMActivity.INTENT_PARAM_TYPE)));
            prizes.add(prize);
        }
        cursor.close();
        return prizes;
    }

    @Override // com.gaiaonline.monstergalaxy.storage.StorageHelper
    public int getMogaCaptureIsland(int mogaId) {
        int islandId = -1;
        Cursor cursor = this.db.rawQuery(StorageHelper.QUERY_MOGA_CAPTURE_ISLAND, new String[]{String.valueOf(mogaId)});
        if (cursor.moveToNext()) {
            islandId = cursor.getInt(cursor.getColumnIndex("island_id"));
        }
        cursor.close();
        return islandId;
    }

    private String getDBLocale() {
        String locale = null;
        Cursor cursor = this.db.rawQuery("SELECT value FROM constants WHERE name = ? ", new String[]{"locale"});
        if (cursor.moveToNext()) {
            locale = cursor.getString(cursor.getColumnIndex("value"));
        }
        cursor.close();
        return locale;
    }
}
