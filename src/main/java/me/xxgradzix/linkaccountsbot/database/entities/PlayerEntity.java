package me.xxgradzix.ageplaydiscordbot.database.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.UUID;

@DatabaseTable(tableName = "players")
public class PlayerEntity {

    @DatabaseField(unique = true, id = true)
    private UUID minecraftId;
    @DatabaseField
    private Long discordId;
    @DatabaseField
    private String discordNick;
    @DatabaseField
    private String minecraftNick;
    @DatabaseField
    private int points;

    public PlayerEntity() {
    }

    public PlayerEntity(Long id, UUID minecraftId, String discordNick, String minecraftNick) {
        this.discordId = id;
        this.minecraftId = minecraftId;
        this.discordNick = discordNick;
        this.minecraftNick = minecraftNick;
        points = 0;
    }

    public Long getDiscordId() {
        return discordId;
    }

    public void setDiscordId(Long discordId) {
        this.discordId = discordId;
    }

    public UUID getMinecraftId() {
        return minecraftId;
    }

    public void setMinecraftId(UUID minecraftId) {
        this.minecraftId = minecraftId;
    }

    public String getDiscordNick() {
        return discordNick;
    }

    public void setDiscordNick(String discordNick) {
        this.discordNick = discordNick;
    }

    public String getMinecraftNick() {
        return minecraftNick;
    }

    public void setMinecraftNick(String minecraftNick) {
        this.minecraftNick = minecraftNick;
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int amount) {
        this.points += amount;
    }

    public void withdrawPoints(int amount) {
        this.points -= amount;
    }

}
