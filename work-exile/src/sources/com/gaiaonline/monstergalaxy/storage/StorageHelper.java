package com.gaiaonline.monstergalaxy.storage;

import com.gaiaonline.monstergalaxy.app.notifications.MogaNotification;
import com.gaiaonline.monstergalaxy.model.map.Encounter;
import com.gaiaonline.monstergalaxy.model.map.Island;
import com.gaiaonline.monstergalaxy.model.map.Node;
import com.gaiaonline.monstergalaxy.model.minigame.Slotmachine;
import com.gaiaonline.monstergalaxy.model.moga.MogaType;
import com.gaiaonline.monstergalaxy.model.move.AttackMove;
import com.gaiaonline.monstergalaxy.model.quest.Quest;
import com.gaiaonline.monstergalaxy.model.shop.Product;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface StorageHelper {
    public static final String DATABASE_NAME = "monsterGalaxy.db";
    public static final int DATABASE_VERSION = 22;
    public static final String QUERY_ALL_BATTLE_AUDIO = "SELECT audio_type, audio_asset FROM battle_audio";
    public static final String QUERY_ALL_ISLANDS = "SELECT name, stars, asset, moga_count, quest_ids, max_level, position_x, position_y, island_id, ambience_audio, sting_audio FROM islands";
    public static final String QUERY_ALL_LEVEL_DIFF_MULTIPLIERS = "SELECT level_diff, multiplier FROM level_diff_multipliers";
    public static final String QUERY_ALL_NOTIFICATIONS = "SELECT type, island_id, notification_id FROM notifications";
    public static final String QUERY_ALL_QUESTS = "SELECT quest_id, name, type, npc_name, npc_asset, receive_dialog, description_dialog, complete_dialog, beat_node_id, monster_id, monster_count, reward_item_id, reward_item_count, grant_node_id, grant_quest_id, restriction_node_ids, restriction_quest_id, island_id_to_win_stars, stars_to_win_in_island, locked_node_id, music FROM quests";
    public static final String QUERY_ALL_SHOP_PRODUCTS = "SELECT type, quantity, description, title, price, id FROM shop_products ORDER BY type, price";
    public static final String QUERY_ATTACK_MOVE_BY_ID = "SELECT name, type, power, modifier_p, modifier_q, max_damage, level_multiplier, charge_animation, charge_effect, animation, hit_visual, attack_sound, hit_sound FROM moves WHERE move_id = ?";
    public static final String QUERY_CONSTANT_BY_NAME = "SELECT value FROM constants WHERE name = ?";
    public static final String QUERY_ENCOUNTERS_BY_NODE = "SELECT encounter_id, quest_id, category, capture, music_asset, music_type FROM encounters WHERE node_id = ?";
    public static final String QUERY_ENCOUNTER_APPEARANCES = "SELECT encounter_id, moga_type_id, level, n, probability FROM encounter_appearances WHERE encounter_id = ?";
    public static final String QUERY_ISLAND_BY_ID = "SELECT name, stars, asset, moga_count, quest_ids, max_level, position_x, position_y, island_id, ambience_audio, sting_audio FROM islands WHERE island_id = ? ";
    public static final String QUERY_MOGA_CAPTURE_ISLAND = "SELECT n.island_id FROM encounter_appearances ea, encounters e, nodes n WHERE ea.moga_type_id = ? AND ea.encounter_id = e.encounter_id AND e.node_id = n.node_id AND e.capture = 'enabled' ORDER BY island_id LIMIT 0,1";
    public static final String QUERY_MOGA_TYPES_BY_SUB_TYPE = "SELECT monster_id, name, description, rarity, offense, defense, zodiac_sign, zodiac, max_health, portrait_asset, full_body_asset, physical_attack_id, zodiac_attack_id, island_id, sub_type, purchaseable FROM monsters WHERE sub_type = ?";
    public static final String QUERY_MOGA_TYPES_COUNT = "SELECT COUNT(1) FROM monsters WHERE sub_type = ?";
    public static final String QUERY_MOGA_TYPE_BY_ID = "SELECT monster_id, name, description, rarity, offense, defense, zodiac_sign, zodiac, max_health, portrait_asset, full_body_asset, physical_attack_id, zodiac_attack_id, island_id, sub_type, purchaseable FROM monsters WHERE monster_id = ?";
    public static final String QUERY_NODES_BY_ISLAND_ID = "SELECT node_id, name, description, adjacent_nodes, energy_cost, thumbnail, background, drop_id, drop_probability, position_x, position_y FROM nodes WHERE island_id = ? ";
    public static final String QUERY_NODE_BY_ID = "SELECT node_id, name, description, adjacent_nodes, energy_cost, thumbnail, background, drop_id, drop_probability, position_x, position_y, island_id FROM nodes WHERE node_id = ?";
    public static final String QUERY_QUEST_BY_BEAT_NODE_ID = "SELECT quest_id, name, type, npc_name, npc_asset, receive_dialog, description_dialog, complete_dialog, beat_node_id, monster_id, monster_count, reward_item_id, reward_item_count, grant_node_id, grant_quest_id, restriction_node_ids, restriction_quest_id, island_id_to_win_stars, stars_to_win_in_island, locked_node_id, music FROM quests WHERE beat_node_id = ?";
    public static final String QUERY_QUEST_BY_GRANT_NODE_ID = "SELECT quest_id, name, type, npc_name, npc_asset, receive_dialog, description_dialog, complete_dialog, beat_node_id, monster_id, monster_count, reward_item_id, reward_item_count, grant_node_id, grant_quest_id, restriction_node_ids, restriction_quest_id, island_id_to_win_stars, stars_to_win_in_island, locked_node_id, music FROM quests WHERE grant_node_id = ?";
    public static final String QUERY_QUEST_BY_GRANT_QUEST_ID = "SELECT quest_id, name, type, npc_name, npc_asset, receive_dialog, description_dialog, complete_dialog, beat_node_id, monster_id, monster_count, reward_item_id, reward_item_count, grant_node_id, grant_quest_id, restriction_node_ids, restriction_quest_id, island_id_to_win_stars, stars_to_win_in_island, locked_node_id, music FROM quests WHERE grant_quest_id = ?";
    public static final String QUERY_QUEST_BY_ID = "SELECT quest_id, name, type, npc_name, npc_asset, receive_dialog, description_dialog, complete_dialog, beat_node_id, monster_id, monster_count, reward_item_id, reward_item_count, grant_node_id, grant_quest_id, restriction_node_ids, restriction_quest_id, island_id_to_win_stars, stars_to_win_in_island, locked_node_id, music FROM quests WHERE quest_id = ?";
    public static final String QUERY_REELS = "SELECT name, probability FROM reels";
    public static final String QUERY_REEL_PRIZES = "SELECT prize_id, displayName, bluecoffees, mogaType, mogacash, name, starseeds, type FROM reel_prizes WHERE type = ?";
    public static final String QUERY_SLOT_MACHINE = "SELECT cooldown_seconds, cost FROM slot_machine";
    public static final String QUERY_TEXTS_BY_NOTIFICATION_ID = "SELECT notification_id, title, message, button_text, text_id FROM notification_texts WHERE notification_id = ? ORDER BY text_id";

    List<Island> getAllIslands();

    AttackMove getAttackMove(int i);

    Map<String, List<String>> getBattleAudio();

    String getConstantValue(String str);

    List<Encounter> getEncounters(int i);

    Island getIsland(int i);

    List<Node> getIslandNodes(Island island);

    Map<Integer, Float> getLevelDiffMultipliers();

    int getMogaCaptureIsland(int i);

    MogaType getMogaType(int i);

    int getMogaTypeCount(MogaType.SubType subType);

    List<MogaType> getMogaTypesBySubType(MogaType.SubType subType);

    Node getNode(int i);

    Set<MogaNotification> getNotifications();

    List<Product> getProducts();

    Quest getQuest(int i);

    Quest getQuestByBeatNodeId(int i);

    Quest getQuestByGrantNodeId(int i);

    List<Quest> getQuestByGrantQuestId(int i);

    Slotmachine getSlotMachine();

    float getZodiacSignMultiplier(String str, String str2);
}
