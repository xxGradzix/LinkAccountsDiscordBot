package me.xxgradzix.linkaccountsbot.rewards;

import me.xxgradzix.linkaccountsbot.database.entities.PlayerEntity;

public abstract class Reward {

    private String name;
    private int cost;


    public boolean canAfford(PlayerEntity playerEntity) {
        if(playerEntity == null) return false;

        return playerEntity.getPoints() >= cost;
    }


    public abstract void collect(PlayerEntity playerEntity);

    public Reward(String name, int cost) {
        this.name = name;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
