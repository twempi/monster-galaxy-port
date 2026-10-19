package com.gaiaonline.monstergalaxy.model.trainer;

import com.badlogic.gdx.Gdx;
import com.gaiaonline.monstergalaxy.app.Clock;
import com.gaiaonline.monstergalaxy.app.Constants;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.notifications.MogaNotification;
import com.gaiaonline.monstergalaxy.model.ZodiacSign;
import com.gaiaonline.monstergalaxy.model.battle.BattleReport;
import com.gaiaonline.monstergalaxy.model.map.Encounter;
import com.gaiaonline.monstergalaxy.model.map.Island;
import com.gaiaonline.monstergalaxy.model.map.Node;
import com.gaiaonline.monstergalaxy.model.moga.Moga;
import com.gaiaonline.monstergalaxy.model.moga.MogaType;
import com.gaiaonline.monstergalaxy.model.quest.Quest;
import com.gaiaonline.monstergalaxy.model.quest.QuestProgress;
import com.gaiaonline.monstergalaxy.model.quest.RewardItem;
import com.gaiaonline.monstergalaxy.tweak.Tweak;
import com.millennialmedia.android.MMDemographic;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;

/* JADX INFO: loaded from: classes.dex */
public class Trainer implements Serializable {
    public static final String NAME_DISALLOWED_CHARS = " ";
    private static final String TAG = "Trainer";
    private static final long serialVersionUID = -5220879218206683298L;
    private transient BattleReport battleReport;
    private int blueCoffeeCups;
    private int coins;
    private List<Quest> completedUnpaidQuests;
    private Island currentIsland;
    private Node currentNode;
    private float currentScrollInWorld;
    private float energy;
    private List<Friend> friends;
    private Gender gender;
    private Long lastDailyPrizeClaim;
    private Long lastEnergyUpdate;
    private String name;
    private int numberOfEncountersCompleted;
    private List<QuestProgress> questsProgressions;
    private Set<MogaNotification> scheduledAlarms;
    private int starSeeds;
    private ZodiacSign zodiac;
    public static final Integer NAME_MAX_CHARS = 12;
    static final Integer TOTAL_MOGAS = Integer.valueOf(Opcodes.L2F);
    static final Integer TOTAL_QUESTS = Integer.valueOf(Opcodes.INEG);
    public Boolean sentDeviceUniqueId = false;
    private List<Moga> mogas = new ArrayList();
    private Team team = new Team();
    private Set<Integer> completedNodesIds = new HashSet();
    private List<Integer> completedQuests = new ArrayList();
    private Map<Integer, Integer> mogasCapturedByIsland = new HashMap();
    private Map<Integer, Integer> starsPerNodes = new HashMap();
    private Map<Integer, Integer> lastNotificationTexts = new HashMap();

    public Trainer(String name) {
        this.name = name;
        Game.getAnalytics().trackEvent("acquisition", "", "", "install", "", "");
    }

    public Map<Integer, Integer> getLastNotificationTexts() {
        if (this.lastNotificationTexts == null) {
            this.lastNotificationTexts = new HashMap();
        }
        return this.lastNotificationTexts;
    }

    public Set<MogaNotification> getScheduledAlarms() {
        if (this.scheduledAlarms == null) {
            this.scheduledAlarms = new HashSet();
        }
        return this.scheduledAlarms;
    }

    public boolean verifyIfGameCompleted() {
        boolean value = false;
        if (this.completedQuests != null && this.completedQuests.size() >= TOTAL_QUESTS.intValue()) {
            Collection<Integer> capturedMogas = this.mogasCapturedByIsland.values();
            Integer mogaCount = 0;
            for (Integer aInt : capturedMogas) {
                mogaCount = Integer.valueOf(mogaCount.intValue() + aInt.intValue());
            }
            value = mogaCount.intValue() >= TOTAL_MOGAS.intValue();
            if (value) {
                Game.getAnalytics().trackEvent("retention", "games_completed", "", "complete_game", "", new StringBuilder(String.valueOf(Game.getTrainer().getCurrentIsland().getId())).toString());
            }
        }
        return value;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Team getTeam() {
        return this.team;
    }

    public int getStarSeeds() {
        return this.starSeeds;
    }

    public void setStarSeeds(int starSeeds) {
        this.starSeeds = starSeeds;
    }

    public void addStarSeeds(int starSeeds) {
        this.starSeeds += starSeeds;
    }

    public boolean consumeStarseed() {
        if (getStarSeeds() <= 0) {
            return false;
        }
        this.starSeeds--;
        return true;
    }

    public boolean consumeCoins(int amount) {
        if (getCoins() < amount) {
            return false;
        }
        this.coins -= amount;
        return true;
    }

    public void consumeEnergy(int amount) {
        this.energy = Math.max(0.0f, this.energy - amount);
        updateEnergy();
    }

    public int getBlueCoffeeCups() {
        return this.blueCoffeeCups;
    }

    public void setBlueCoffeeCups(int blueCoffeeCups) {
        this.blueCoffeeCups = blueCoffeeCups;
    }

    public void addBlueCoffeeCups(int blueCoffeeCups) {
        this.blueCoffeeCups += blueCoffeeCups;
    }

    private boolean consumeBlueCoffeeCup() {
        if (this.blueCoffeeCups <= 0) {
            return false;
        }
        this.blueCoffeeCups--;
        return true;
    }

    public boolean applyBlueCoffee() {
        if (!consumeBlueCoffeeCup()) {
            return false;
        }
        rechargeEnergy();
        return true;
    }

    public boolean applyBlueCoffee(Moga moga) {
        if (!consumeBlueCoffeeCup()) {
            return false;
        }
        moga.fullyRechargeHealth();
        moga.fullyRechargeZodiac();
        return true;
    }

    private void updateEnergy() {
        long now = Clock.getCurrentTimeSec() * 1000;
        if (this.lastEnergyUpdate == null) {
            this.lastEnergyUpdate = Long.valueOf(now);
        }
        long timeSinceLastUpdate = now - this.lastEnergyUpdate.longValue();
        float accumulatedEnergy = ((timeSinceLastUpdate / 1000) * Constants.trainerEnergyRecoverPerMin) / 60.0f;
        this.energy = Math.min(this.energy + accumulatedEnergy, Constants.maxTrainerEnergy);
        this.lastEnergyUpdate = Long.valueOf(now);
    }

    public int getSecondsUntilNextEnergyRecharge() {
        long now = Clock.getCurrentTimeSec() * 1000;
        if (this.lastEnergyUpdate == null) {
            this.lastEnergyUpdate = Long.valueOf(now);
        }
        long time = (now - this.lastEnergyUpdate.longValue()) / 1000;
        int totalTimeForEnergyRecharge = (int) (60.0f / Constants.trainerEnergyRecoverPerMin);
        int returnValue = (int) (((long) totalTimeForEnergyRecharge) - time);
        if (returnValue > totalTimeForEnergyRecharge) {
            returnValue = totalTimeForEnergyRecharge;
            this.lastEnergyUpdate = Long.valueOf(now);
        }
        if (returnValue >= 0) {
            return returnValue;
        }
        return 0;
    }

    public String getEnergyTimerText() {
        String secondsText;
        int time = getSecondsUntilNextEnergyRecharge();
        int minutes = time / 60;
        int seconds = time % 60;
        if (time == 0) {
            updateEnergy();
        }
        if (seconds < 10) {
            secondsText = "0" + seconds;
        } else {
            secondsText = String.valueOf(seconds);
        }
        return String.valueOf(minutes) + ":" + secondsText;
    }

    public int getEnergy() {
        long time = getSecondsUntilNextEnergyRecharge();
        if (time == 0) {
            updateEnergy();
        }
        return Math.round(this.energy);
    }

    public boolean isEnergyFullyCharged() {
        return getEnergy() >= Constants.maxTrainerEnergy;
    }

    public int getTimeUntilEnergyFullyCharged() {
        updateEnergy();
        return (int) (((Constants.maxTrainerEnergy - this.energy) / Constants.trainerEnergyRecoverPerMin) * 60.0f);
    }

    public void rechargeEnergy() {
        this.energy = Constants.maxTrainerEnergy;
    }

    public List<Moga> getMogas() {
        return this.mogas;
    }

    public void capturedMoga(Moga moga) {
        Integer count = this.mogasCapturedByIsland.get(Integer.valueOf(this.currentIsland.getId()));
        this.mogasCapturedByIsland.put(Integer.valueOf(this.currentIsland.getId()), Integer.valueOf(count == null ? 1 : count.intValue() + 1));
        this.mogas.add(moga);
    }

    public int getNumberOfCapturesInIsland(Island island) {
        Integer count = this.mogasCapturedByIsland.get(Integer.valueOf(island.getId()));
        if (count == null) {
            return 0;
        }
        return count.intValue();
    }

    public void setMogas(List<Moga> mogas) {
        this.mogas = mogas;
    }

    public void addMoga(Moga moga) {
        if (!this.mogas.contains(moga)) {
            this.mogas.add(moga);
        }
    }

    public void addAndDeployMoga(Moga moga) {
        addMoga(moga);
        this.team.addMoga(moga);
        this.team.setDeployedMoga(moga);
    }

    public List<Friend> getFriends() {
        return this.friends;
    }

    public void setFriends(List<Friend> friends) {
        this.friends = friends;
    }

    public int getCoins() {
        return this.coins;
    }

    public void addCoins(int coins) {
        this.coins += coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public ZodiacSign getZodiac() {
        return this.zodiac;
    }

    public void setZodiac(ZodiacSign zodiac) {
        this.zodiac = zodiac;
    }

    public float getCurrentScrollInWorld() {
        return this.currentScrollInWorld;
    }

    public void setCurrentScrollInWorld(float currentScrollInWorld) {
        this.currentScrollInWorld = currentScrollInWorld;
    }

    public List<Integer> getCompletedQuests() {
        return this.completedQuests;
    }

    public List<Quest> getCompletedUnpaidQuests() {
        if (this.completedUnpaidQuests == null) {
            this.completedUnpaidQuests = new ArrayList();
        }
        return this.completedUnpaidQuests;
    }

    public void setPaidQuest(Quest quest) {
        getCompletedUnpaidQuests().remove(quest);
    }

    public void setCompletedQuests(List<Integer> completedQuests) {
        this.completedQuests = completedQuests;
    }

    public List<QuestProgress> getQuestsProgressions() {
        if (this.questsProgressions == null) {
            this.questsProgressions = new ArrayList();
        }
        return this.questsProgressions;
    }

    public Set<Integer> getCompletedNodesIds() {
        return this.completedNodesIds;
    }

    public void setCompletedNodesIds(Set<Integer> completedNodesIds) {
        this.completedNodesIds = completedNodesIds;
    }

    public Island getCurrentIsland() {
        return this.currentIsland;
    }

    public void setCurrentIsland(Island newIsland) {
        if (newIsland != null) {
            if (!newIsland.equals(this.currentIsland)) {
                this.currentIsland = newIsland;
                setCurrentNode(this.currentIsland.getFirstNode());
            } else {
                this.currentIsland = newIsland;
            }
        }
    }

    public void setCurrentNode(Node node) {
        this.currentNode = node;
        this.currentNode.createTeam();
    }

    public Node getCurrentNode() {
        return this.currentNode;
    }

    public Gender getGender() {
        return this.gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Long getLastDailyPrizeClaim() {
        return this.lastDailyPrizeClaim;
    }

    private void setLastDailyPrizeClaim(Long lastDailyPrizeClaim) {
        this.lastDailyPrizeClaim = lastDailyPrizeClaim;
    }

    public long getClaimDailyPrizeAvaiableTime() {
        if (this.lastDailyPrizeClaim == null) {
            return 0L;
        }
        return this.lastDailyPrizeClaim.longValue() + ((long) Constants.dailyPrizeTimeToWait);
    }

    public RewardItem claimDailyPrize() {
        setLastDailyPrizeClaim(Long.valueOf(Game.getGaiaClient().calculateServerTimestamp()));
        RewardItem item = getDailyPrize();
        receiveReward(item, RewardItem.RewardSource.DAILY_PRICE);
        return item;
    }

    private RewardItem getDailyPrize() {
        double randomValue = Math.random();
        float sum = 0.0f;
        for (int i = 0; i < Constants.dailyPrizeIDs.length; i++) {
            sum += Constants.dailyPrizeProbabilities[i];
            if (randomValue <= sum) {
                int rewardId = Constants.dailyPrizeIDs[i];
                int count = (int) Math.ceil(Math.random() * 2.0d);
                return new RewardItem(rewardId, count);
            }
        }
        return null;
    }

    public int getNumberOfEncountersCompleted() {
        return this.numberOfEncountersCompleted;
    }

    public void setNumberOfEncountersCompleted(int numberOfEncountersCompleted) {
        this.numberOfEncountersCompleted = numberOfEncountersCompleted;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setNumberOfWonStarsInNode(int nodeId, int number) {
        this.starsPerNodes.put(Integer.valueOf(nodeId), Integer.valueOf(number));
    }

    public int getNumberOfStarsWonInIsland(Island island) {
        int stars = 0;
        for (Node node : island.getNodes()) {
            stars += getNumberOfStarsWonInNode(node);
        }
        return stars;
    }

    public int getNumberOfStarsWonInNode(Node node) {
        if (this.starsPerNodes.get(Integer.valueOf(node.getId())) == null) {
            return 0;
        }
        int stars = this.starsPerNodes.get(Integer.valueOf(node.getId())).intValue();
        return stars;
    }

    public boolean canGoToIsland(Island island) {
        return island != null && hasCapturedEnoughMogas(island) && isIslandCompleted(island.getPreviousIsland());
    }

    public boolean hasCapturedEnoughMogas(Island island) {
        return this.mogas.size() >= island.getNumberOfRequiredMogasToUnlock();
    }

    public boolean isIslandCompleted(Island island) {
        if (island == null) {
            return true;
        }
        return this.completedNodesIds.contains(Integer.valueOf(island.getBossNode().getId()));
    }

    public int getMaxUnlockedLevel() {
        if (Tweak.getForcedPlayerMogaLevel() != null) {
            return Tweak.getForcedPlayerMogaLevel().intValue();
        }
        int retValue = 1;
        for (Island island : Game.getStorage().getAllIslands()) {
            if (canGoToIsland(island)) {
                retValue = Math.max(retValue, island.getMaxLevel());
            }
        }
        return retValue;
    }

    public boolean isNodeCompleted(int nodeId) {
        return this.completedNodesIds.contains(Integer.valueOf(nodeId));
    }

    public void completedCurrentNode() {
        int nodeId = this.currentNode.getId();
        if (!isNodeCompleted(nodeId)) {
            this.completedNodesIds.add(Integer.valueOf(nodeId));
        }
    }

    public boolean isNodeLocked(Node node) {
        boolean isLocked = true;
        if (node.isFirstNode()) {
            isLocked = false;
        } else {
            int[] adjacentNodes = node.getAdjacentNodes();
            for (int i = 0; i < node.getAdjacentNodes().length; i++) {
                if (isNodeCompleted(adjacentNodes[i])) {
                    isLocked = false;
                    break;
                }
            }
        }
        return isLocked || isActiveQuestBlockingNode(node);
    }

    public boolean canGoToNode(Node node) {
        return !isNodeLocked(node) || node.equals(getCurrentNode()) || isNodeCompleted(node.getId());
    }

    public boolean isNodeSuggested(Node node) {
        return !isNodeCompleted(node.getId()) || hasActiveCaptureOrBeatQuestOnNode(node);
    }

    public int getNumberOfCaughtMogaTypes() {
        return this.mogas.size();
    }

    private boolean isActiveQuestBlockingNode(Node node) {
        for (QuestProgress qp : getQuestsProgressions()) {
            if (qp.getQuest().getBlockedNodeId() == node.getId()) {
                return true;
            }
        }
        return false;
    }

    public Quest getAvailableQuestForNode(Node node) {
        Quest quest = Game.getStorage().getQuestByGrantNodeId(node.getId());
        if (quest == null || !isAbleToStartQuest(quest)) {
            return null;
        }
        return quest;
    }

    public void startQuest(Quest quest) {
        this.questsProgressions.add(new QuestProgress(quest));
        Game.getAnalytics().trackEvent("quest", "started", new StringBuilder(String.valueOf(quest.getId())).toString(), "quest_started", "", new StringBuilder().append(getCurrentIsland().getId()).toString());
        getCurrentNode().createTeam();
    }

    public boolean isQuestCompleted(int questId) {
        return this.completedQuests.contains(Integer.valueOf(questId));
    }

    public int getNumberOfCompletedQuests() {
        return this.completedQuests.size();
    }

    public int getNumberOfCompletedQuestsInIsland(Island island) {
        int questCount = 0;
        for (int id : island.getQuestIds()) {
            if (this.completedQuests.contains(Integer.valueOf(id))) {
                questCount++;
            }
        }
        return questCount;
    }

    private boolean isQuestActive(int questId) {
        for (QuestProgress qp : this.questsProgressions) {
            if (qp.getQuest().getId() == questId) {
                return true;
            }
        }
        return false;
    }

    public Quest getQuestStartingOnCurrentNode() {
        return Game.getStorage().getQuestByBeatNodeId(this.currentNode.getId());
    }

    public Quest getNextQuest(int questId) {
        List<Quest> quests = Game.getStorage().getQuestByGrantQuestId(questId);
        for (Quest quest : quests) {
            if (isAbleToStartQuest(quest)) {
                return quest;
            }
        }
        return null;
    }

    public boolean isAbleToStartQuest(Quest quest) {
        int restrictionQuest = quest.getRestrictionQuestId();
        boolean isRestrictionQuestCompleted = restrictionQuest == 0 || this.completedQuests.contains(Integer.valueOf(restrictionQuest));
        int grantQuest = quest.getGrantQuestId();
        boolean isGrantQuestCompleted = grantQuest == 0 || this.completedQuests.contains(Integer.valueOf(grantQuest));
        boolean isActive = isQuestActive(quest.getId());
        boolean isCompleted = this.completedQuests.contains(Integer.valueOf(quest.getId()));
        return isRestrictionQuestCompleted && isGrantQuestCompleted && !isActive && !isCompleted;
    }

    private void updateActiveQuests(Node completedNode, List<Moga> killedMogas, List<Moga> capturedMogas) {
        List<QuestProgress> questProgressionsToDelete = new ArrayList<>();
        for (QuestProgress questProgress : this.questsProgressions) {
            questProgress.update(completedNode, killedMogas, capturedMogas);
            if (questProgress.isCompleted()) {
                int questId = questProgress.getQuest().getId();
                Gdx.app.log(TAG, "Trainer completed quest: " + questProgress.getQuest().getName());
                Game.getAnalytics().trackEvent("quest", "completed", new StringBuilder(String.valueOf(questId)).toString(), "quest_completed", "", new StringBuilder().append(getCurrentIsland().getId()).toString());
                this.completedQuests.add(Integer.valueOf(questId));
                getCompletedUnpaidQuests().add(questProgress.getQuest());
                questProgressionsToDelete.add(questProgress);
                receiveReward(questProgress.getQuest().getRewardItem(), RewardItem.RewardSource.QUEST);
            }
        }
        this.questsProgressions.removeAll(questProgressionsToDelete);
    }

    public boolean canCaptureMoga(Moga moga) {
        return getCurrentNode().getEncounter().isCaptureEnabled() && !hasMogasOfType(moga.getType()) && getTeam().getLevel() >= moga.getLevel() && moga.canBeCaptured();
    }

    public boolean hasMogasOfType(MogaType mogaType) {
        return hasMogasOfType(mogaType.getId());
    }

    public boolean hasMogasOfType(int mogaType) {
        for (Moga moga : this.mogas) {
            if (moga.getType().getId() == mogaType) {
                return true;
            }
        }
        return false;
    }

    private void receiveReward(RewardItem rewardItem, RewardItem.RewardSource source) {
        String rewardSource;
        int coffee = 0;
        int seeds = 0;
        String rewardType = null;
        int rewardNumber = 0;
        if (rewardItem.getType() == RewardItem.Type.REWARD_BLUE_COFFEE) {
            coffee = rewardItem.getCount();
            rewardNumber = coffee;
            rewardType = "blue_coffee";
        } else if (rewardItem.getType() == RewardItem.Type.REWARD_STAR_SEED) {
            seeds = rewardItem.getCount();
            rewardType = "starseed";
        }
        if (this.starSeeds + seeds <= Constants.itemCapStarseed) {
            this.starSeeds += seeds;
            rewardNumber = seeds;
        } else if (this.starSeeds < Constants.itemCapStarseed) {
            this.starSeeds = Constants.itemCapStarseed;
            rewardNumber = Constants.itemCapStarseed;
        }
        this.blueCoffeeCups += coffee;
        if (rewardNumber > 0) {
            String currentIslandId = String.valueOf(getCurrentIsland().getId());
            if (source == RewardItem.RewardSource.QUEST) {
                rewardSource = "quest_reward";
            } else if (source == RewardItem.RewardSource.NODE) {
                rewardSource = "battle_drop";
            } else {
                rewardSource = "daily_prize";
            }
            Game.getAnalytics().trackEvent("monetization", "item_grant", rewardType, rewardSource, String.valueOf(rewardNumber), currentIslandId);
        }
    }

    public void battleOver() {
        String encounterId = String.valueOf(this.currentNode.getEncounter().getId());
        String islandId = String.valueOf(this.currentIsland.getId());
        if (this.battleReport.areAllMogasKilledOrCaptured(this.currentNode)) {
            Gdx.app.log(TAG, "Trainer completed the node " + this.currentNode.getName());
            if (!this.completedNodesIds.contains(Integer.valueOf(this.currentNode.getId()))) {
                this.numberOfEncountersCompleted++;
            }
            this.completedNodesIds.add(Integer.valueOf(this.currentNode.getId()));
            RewardItem reward = this.currentNode.getRewardItem();
            if (reward != null) {
                receiveReward(reward, RewardItem.RewardSource.NODE);
                if (reward.getType() == RewardItem.Type.REWARD_BLUE_COFFEE) {
                    this.battleReport.setNumberOfRewardedBlueCoffees(reward.getCount());
                } else if (reward.getType() == RewardItem.Type.REWARD_STAR_SEED) {
                    this.battleReport.setNumberOfRewardedStarSeeds(reward.getCount());
                }
            }
            int stars = 0;
            if (this.battleReport.hasScoredPerfect()) {
                stars = 3;
            } else if (this.battleReport.hasScoredVeryGood()) {
                stars = 2;
            } else if (this.battleReport.hasScoredNormal()) {
                stars = 1;
            }
            int origStars = getNumberOfStarsWonInNode(this.currentNode);
            if (origStars < stars) {
                setNumberOfWonStarsInNode(this.currentNode.getId(), stars);
                this.battleReport.setHighScore(true);
            }
            Game.getAnalytics().trackEvent("encounters", "win", encounterId, "encounter_win", "", islandId);
        } else {
            Game.getAnalytics().trackEvent("encounters", "lose", encounterId, "encounter_lose", "", islandId);
        }
        Node completedNode = this.completedNodesIds.contains(Integer.valueOf(this.currentNode.getId())) ? this.currentNode : null;
        List<Moga> capturedMogas = this.battleReport.getCapturedMogas();
        List<Moga> killedMogas = this.battleReport.getKilledMogas();
        updateActiveQuests(completedNode, killedMogas, capturedMogas);
        Game.save();
    }

    public boolean isFullyCharged() {
        return getEnergy() == Constants.maxTrainerEnergy;
    }

    public BattleReport getBattleReport() {
        return this.battleReport;
    }

    public void setBattleReport(BattleReport battleReport) {
        this.battleReport = battleReport;
    }

    public boolean hasActiveCaptureQuestOnCurrentNode() {
        Encounter encounter = this.currentNode.getEncounter();
        for (QuestProgress questProgress : this.questsProgressions) {
            Quest quest = questProgress.getQuest();
            if (quest.getType() == Quest.Type.CAPTURE && encounter.getQuestId() == quest.getId()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasActiveCaptureOrBeatQuestOnNode(Node aNode) {
        Encounter encounter = aNode.getEncounter();
        for (QuestProgress questProgress : this.questsProgressions) {
            Quest quest = questProgress.getQuest();
            if (quest.getType() == Quest.Type.CAPTURE || quest.getType() == Quest.Type.BEAT_NODE) {
                if (encounter.getQuestId() == quest.getId()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasSleepingMogas() {
        for (Moga moga : this.mogas) {
            if (moga.isSleeping()) {
                return true;
            }
        }
        return false;
    }

    public long getFullTeamRestedTime() {
        long fullTeamRestedTime = 0;
        for (Moga moga : this.mogas) {
            if (moga.isSleeping()) {
                fullTeamRestedTime = Math.max(fullTeamRestedTime, moga.getNapEnd());
            }
        }
        return fullTeamRestedTime;
    }

    public String getAssetName() {
        if (this.gender == null || this.gender != Gender.MALE) {
            return "pc.girl";
        }
        return "pc.guy";
    }

    public enum Gender {
        MALE(MMDemographic.GENDER_MALE),
        FEMALE(MMDemographic.GENDER_FEMALE);

        private String name;

        /* JADX INFO: renamed from: values, reason: to resolve conflict with enum method */
        public static Gender[] valuesCustom() {
            Gender[] genderArrValuesCustom = values();
            int length = genderArrValuesCustom.length;
            Gender[] genderArr = new Gender[length];
            System.arraycopy(genderArrValuesCustom, 0, genderArr, 0, length);
            return genderArr;
        }

        Gender(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}
