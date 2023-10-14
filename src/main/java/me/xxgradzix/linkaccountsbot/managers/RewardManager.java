package me.xxgradzix.linkaccountsbot.managers;


import me.xxgradzix.linkaccountsbot.database.entities.PlayerEntity;
import me.xxgradzix.linkaccountsbot.database.managers.PlayerEntityManager;
import me.xxgradzix.linkaccountsbot.rewards.Reward;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class RewardManager {

    private final PointManager pointManager;
    public RewardManager(PlayerEntityManager playerEntityManager, PointManager pointManager) {

        this.pointManager = pointManager;
    }



    public final Reward vipReward = new Reward("vip", 100) {
        @Override
        public void collect(PlayerEntity playerEntity) {
            if(!canAfford(playerEntity)) return;

            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerEntity.getMinecraftId());

            if(player == null) {
                return;
            }

            pointManager.withdrawPoints(playerEntity, getCost());

            player.sendMessage(TextComponent.fromLegacyText("nagroda vip"));


        }
    };
    public final Reward svipReward = new Reward("svip", 200) {
        @Override
        public void collect(PlayerEntity playerEntity) {
            if(!canAfford(playerEntity)) return;

            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerEntity.getMinecraftId());

            if(player == null) {
                return;
            }
            pointManager.withdrawPoints(playerEntity, getCost());

            player.sendMessage(TextComponent.fromLegacyText("nagroda svip"));

        }
    };
    public final Reward ageReward = new Reward("age", 500) {
        @Override
        public void collect(PlayerEntity playerEntity) {
            if(!canAfford(playerEntity)) return;

            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerEntity.getMinecraftId());

            if(player == null) {
                return;
            }
            pointManager.withdrawPoints(playerEntity, getCost());

            player.sendMessage(TextComponent.fromLegacyText("nagroda age"));

        }
    };


}
