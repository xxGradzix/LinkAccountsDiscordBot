package me.xxgradzix.ageplaydiscordbot.managers;

import me.xxgradzix.ageplaydiscordbot.database.entities.PlayerEntity;
import me.xxgradzix.ageplaydiscordbot.rewards.Reward;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class RewardManager {

    public static final Reward VIP_REWARD = new Reward("vip", 100) {
        @Override
        public void collect(PlayerEntity playerEntity) {
            if(!canAfford(playerEntity)) return;

            Player player = Bukkit.getPlayer(playerEntity.getMinecraftId());

            if(player == null) {
                return;
            }

            PointManager.withdrawPoints(playerEntity, getCost());

            player.sendMessage("nagroda vip");


        }
    };
    public static final Reward SVIP_REWARD = new Reward("svip", 200) {
        @Override
        public void collect(PlayerEntity playerEntity) {
            if(!canAfford(playerEntity)) return;

            Player player = Bukkit.getPlayer(playerEntity.getMinecraftId());

            if(player == null) {
                return;
            }
            PointManager.withdrawPoints(playerEntity, getCost());

            player.sendMessage("nagroda svip");

        }
    };
    public static final Reward AGE_REWARD = new Reward("age", 500) {
        @Override
        public void collect(PlayerEntity playerEntity) {
            if(!canAfford(playerEntity)) return;

            Player player = Bukkit.getPlayer(playerEntity.getMinecraftId());

            if(player == null) {
                return;
            }
            PointManager.withdrawPoints(playerEntity, getCost());

            player.sendMessage("nagroda age");

        }
    };


}
