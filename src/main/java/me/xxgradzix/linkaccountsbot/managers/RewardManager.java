package me.xxgradzix.linkaccountsbot.managers;


import me.xxgradzix.linkaccountsbot.database.entities.PlayerEntity;
import me.xxgradzix.linkaccountsbot.database.entities.PlayerRewardsEntity;
import me.xxgradzix.linkaccountsbot.database.managers.PlayerEntityManager;
import me.xxgradzix.linkaccountsbot.database.managers.PlayerRewardsEntityManager;
import me.xxgradzix.linkaccountsbot.rewards.Reward;

import java.util.HashMap;

public class RewardManager {

    private final PointManager pointManager;
    private final PlayerRewardsEntityManager playerRewardsEntityManager;
    public RewardManager(PlayerEntityManager playerEntityManager, PointManager pointManager, PlayerRewardsEntityManager playerRewardsEntityManager) {

        this.pointManager = pointManager;
        this.playerRewardsEntityManager = playerRewardsEntityManager;
    }



    public final Reward vipReward = new Reward("vip", 100) {
        @Override
        public void collect(PlayerEntity playerEntity) {
            if(!canAfford(playerEntity)) return;

            PlayerRewardsEntity entity = playerRewardsEntityManager.getPlayerRewardsEntityByMinecraftId(playerEntity.getMinecraftId());

            if(entity == null) entity = new PlayerRewardsEntity(playerEntity.getMinecraftId(), new HashMap<>());

            HashMap<String, Integer> rewards = entity.getRewards();

            int rewardAmount = rewards.getOrDefault(getName(), 0);

            rewardAmount++;

            rewards.put(getName(), rewardAmount);

            pointManager.withdrawPoints(playerEntity, getCost());

            entity.setRewards(rewards);

            playerRewardsEntityManager.createOrUpdatePlayerRewardsEntity(entity);
        }
    };
    public final Reward svipReward = new Reward("svip", 200) {

        @Override
        public void collect(PlayerEntity playerEntity) {
            if(!canAfford(playerEntity)) return;

            PlayerRewardsEntity entity = playerRewardsEntityManager.getPlayerRewardsEntityByMinecraftId(playerEntity.getMinecraftId());

            if(entity == null) entity = new PlayerRewardsEntity(playerEntity.getMinecraftId(), new HashMap<>());

            HashMap<String, Integer> rewards = entity.getRewards();

            int rewardAmount = rewards.getOrDefault(getName(), 0);

            rewardAmount++;

            rewards.put(getName(), rewardAmount);

            pointManager.withdrawPoints(playerEntity, getCost());

            entity.setRewards(rewards);

            playerRewardsEntityManager.createOrUpdatePlayerRewardsEntity(entity);
        }
    };
    public final Reward ageReward = new Reward("age", 500) {
        @Override
        public void collect(PlayerEntity playerEntity) {
            if(!canAfford(playerEntity)) return;

            PlayerRewardsEntity entity = playerRewardsEntityManager.getPlayerRewardsEntityByMinecraftId(playerEntity.getMinecraftId());

            if(entity == null) entity = new PlayerRewardsEntity(playerEntity.getMinecraftId(), new HashMap<>());

            HashMap<String, Integer> rewards = entity.getRewards();

            int rewardAmount = rewards.getOrDefault(getName(), 0);

            rewardAmount++;

            rewards.put(getName(), rewardAmount);

            pointManager.withdrawPoints(playerEntity, getCost());

            entity.setRewards(rewards);

            playerRewardsEntityManager.createOrUpdatePlayerRewardsEntity(entity);
        }
    };


}
