package me.xxgradzix.ageplaydiscordbot.managers;

import me.xxgradzix.ageplaydiscordbot.AgePlayDiscordBot;
import me.xxgradzix.ageplaydiscordbot.database.entities.PlayerEntity;
import me.xxgradzix.ageplaydiscordbot.database.managers.PlayerEntityManager;

import java.util.UUID;

public class PointManager {

    private final static PlayerEntityManager PLAYER_ENTITY_MANAGER = AgePlayDiscordBot.getPlayerEntityManager();

    public static int getUserPoints(UUID userMinecraftId) {
        PlayerEntity playerEntity = PLAYER_ENTITY_MANAGER.getPlayerEntityByMinecraftId(userMinecraftId);

        if(playerEntity == null) {
            return -1;
        }

        return playerEntity.getPoints();
    }
    public static void withdrawPoints(UUID userMinecraftId, int amount) {
        PlayerEntity playerEntity = PLAYER_ENTITY_MANAGER.getPlayerEntityByMinecraftId(userMinecraftId);
        if(playerEntity == null) {
            return;
        }

        playerEntity.withdrawPoints(amount);

        PLAYER_ENTITY_MANAGER.updatePlayerEntity(playerEntity);
    }
    public static void withdrawPoints(Long userDiscordId, int amount) {
        PlayerEntity playerEntity = PLAYER_ENTITY_MANAGER.getPlayerEntityByDiscordId(userDiscordId);
        if(playerEntity == null) {
            return;
        }

        playerEntity.withdrawPoints(amount);

        PLAYER_ENTITY_MANAGER.updatePlayerEntity(playerEntity);
    }
    public static void withdrawPoints(PlayerEntity playerEntity, int amount) {
        if(playerEntity == null) {
            return;
        }
        playerEntity.withdrawPoints(amount);
        PLAYER_ENTITY_MANAGER.updatePlayerEntity(playerEntity);
    }


    public static void addPoints(UUID userMinecraftId, int amount) {
        PlayerEntity playerEntity = PLAYER_ENTITY_MANAGER.getPlayerEntityByMinecraftId(userMinecraftId);
        if(playerEntity == null) {
            return;
        }

        playerEntity.addPoints(amount);

        PLAYER_ENTITY_MANAGER.updatePlayerEntity(playerEntity);
    }
    public static void addPoints(Long userDiscordId, int amount) {
        PlayerEntity playerEntity = PLAYER_ENTITY_MANAGER.getPlayerEntityByDiscordId(userDiscordId);
        if(playerEntity == null) {
            return;
        }
        playerEntity.addPoints(amount);
        PLAYER_ENTITY_MANAGER.updatePlayerEntity(playerEntity);
    }

    public static void addPoints(PlayerEntity playerEntity, int amount) {
        if(playerEntity == null) {
            return;
        }
        playerEntity.addPoints(amount);
        PLAYER_ENTITY_MANAGER.updatePlayerEntity(playerEntity);
    }

}


