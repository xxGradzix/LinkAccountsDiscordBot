package me.xxgradzix.linkaccountsbot.managers;


import me.xxgradzix.linkaccountsbot.database.entities.PlayerEntity;
import me.xxgradzix.linkaccountsbot.database.managers.PlayerEntityManager;

import java.util.UUID;

public class PointManager {

    private final PlayerEntityManager playerEntityManager;

    public PointManager(PlayerEntityManager playerEntityManager) {
        this.playerEntityManager = playerEntityManager;
    }

    public int getUserPoints(UUID userMinecraftId) {
        PlayerEntity playerEntity = playerEntityManager.getPlayerEntityByMinecraftId(userMinecraftId);

        if(playerEntity == null) {
            return -1;
        }

        return playerEntity.getPoints();
    }
    public void withdrawPoints(UUID userMinecraftId, int amount) {
        PlayerEntity playerEntity = playerEntityManager.getPlayerEntityByMinecraftId(userMinecraftId);
        if(playerEntity == null) {
            return;
        }

        playerEntity.withdrawPoints(amount);

        playerEntityManager.updatePlayerEntity(playerEntity);
    }
    public void withdrawPoints(Long userDiscordId, int amount) {
        PlayerEntity playerEntity = playerEntityManager.getPlayerEntityByDiscordId(userDiscordId);
        if(playerEntity == null) {
            return;
        }

        playerEntity.withdrawPoints(amount);

        playerEntityManager.updatePlayerEntity(playerEntity);
    }
    public void withdrawPoints(PlayerEntity playerEntity, int amount) {
        if(playerEntity == null) {
            return;
        }
        playerEntity.withdrawPoints(amount);
        playerEntityManager.updatePlayerEntity(playerEntity);
    }


    public void addPoints(UUID userMinecraftId, int amount) {
        PlayerEntity playerEntity = playerEntityManager.getPlayerEntityByMinecraftId(userMinecraftId);
        if(playerEntity == null) {
            return;
        }

        playerEntity.addPoints(amount);

        playerEntityManager.updatePlayerEntity(playerEntity);
    }
    public void addPoints(Long userDiscordId, int amount) {
        PlayerEntity playerEntity = playerEntityManager.getPlayerEntityByDiscordId(userDiscordId);
        if(playerEntity == null) {
            return;
        }
        playerEntity.addPoints(amount);
        playerEntityManager.updatePlayerEntity(playerEntity);
    }

    public void addPoints(PlayerEntity playerEntity, int amount) {
        if(playerEntity == null) {
            return;
        }
        playerEntity.addPoints(amount);
        playerEntityManager.updatePlayerEntity(playerEntity);
    }

}


