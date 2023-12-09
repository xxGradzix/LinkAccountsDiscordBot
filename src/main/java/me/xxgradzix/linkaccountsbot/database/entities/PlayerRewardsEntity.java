package me.xxgradzix.linkaccountsbot.database.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import me.xxgradzix.linkaccountsbot.database.persisters.HashMapItemStackIntegerType;

import java.util.HashMap;
import java.util.UUID;

@DatabaseTable(tableName = "players_rewards")
public class PlayerRewardsEntity {

    @DatabaseField(unique = true, id = true)
    private UUID id;

    @DatabaseField(persisterClass = HashMapItemStackIntegerType.class, columnDefinition = "BLOB")
    private HashMap<String, Integer> rewards;

    public PlayerRewardsEntity(UUID id, HashMap<String, Integer> rewards) {
        this.id = id;
        this.rewards = rewards;
    }

    public PlayerRewardsEntity() {
        rewards = new HashMap<>();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public HashMap<String, Integer> getRewards() {
        return new HashMap<>(rewards);
    }

    public void setRewards(HashMap<String, Integer> rewards) {
        this.rewards = rewards;
    }
}
