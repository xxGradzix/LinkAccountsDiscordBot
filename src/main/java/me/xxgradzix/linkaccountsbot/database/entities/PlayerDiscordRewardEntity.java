package me.xxgradzix.linkaccountsbot.database.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.UUID;

@DatabaseTable(tableName = "player_discord_reward")
public class PlayerDiscordRewardEntity {

    @DatabaseField(unique = true, id = true)
    private UUID playerUUID;

    @DatabaseField(columnName = "discordId")
    private Long discordId;

    @DatabaseField
    private boolean discordRewardCollected;

    public PlayerDiscordRewardEntity() {
    }

    public PlayerDiscordRewardEntity(UUID playerUUID, Long discordId, boolean discordRewardCollected) {
        this.playerUUID = playerUUID;
        this.discordId = discordId;
        this.discordRewardCollected = discordRewardCollected;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public void setPlayerUUID(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public boolean isDiscordRewardCollected() {
        return discordRewardCollected;
    }

    public void setDiscordRewardCollected(boolean discordRewardCollected) {
        this.discordRewardCollected = discordRewardCollected;
    }

    public Long getDiscordId() {
        return discordId;
    }

    public void setDiscordId(Long discordId) {
        this.discordId = discordId;
    }
}
