package me.xxgradzix.linkaccountsbot.commands;


import me.xxgradzix.linkaccountsbot.database.entities.PlayerDiscordRewardEntity;
import me.xxgradzix.linkaccountsbot.database.entities.PlayerEntity;
import me.xxgradzix.linkaccountsbot.database.managers.PlayerDiscordRewardEntityManager;
import me.xxgradzix.linkaccountsbot.database.managers.PlayerEntityManager;
import me.xxgradzix.linkaccountsbot.managers.RewardManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


import java.awt.*;

public class DiscordReward extends ListenerAdapter {
    private final PlayerEntityManager playerEntityManager;
    private final PlayerDiscordRewardEntityManager playerDiscordRewardEntityManager;

    private final RewardManager rewardManager;

    public DiscordReward(PlayerEntityManager playerEntityManager, PlayerDiscordRewardEntityManager playerDiscordRewardEntityManager, RewardManager rewardManager) {
        this.playerEntityManager = playerEntityManager;
        this.playerDiscordRewardEntityManager = playerDiscordRewardEntityManager;
        this.rewardManager = rewardManager;
    }


    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if(!event.getName().equalsIgnoreCase("discordrewardsetup")) return;

        MessageEmbed embed = new EmbedBuilder()
                .setTitle("Odbierz nagrode!")
                .setDescription("Kliknij aby odebrac nagrode z discorda")
                .setColor(Color.red)
                .build();

        net.dv8tion.jda.api.interactions.components.buttons.Button discordRewardCollect = net.dv8tion.jda.api.interactions.components.buttons.Button.primary("button_discord_reward", "Odbierz");
        
        event.getChannel().sendMessageEmbeds(embed)
                .addActionRow(discordRewardCollect)
                .queue();
        event.reply("Utworzyles embed").setEphemeral(true).queue();
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if(!event.getButton().getId().equalsIgnoreCase("button_discord_reward")) {
            return;
        }

        PlayerEntity playerEntity = playerEntityManager.getPlayerEntityByDiscordId(event.getIdLong());
        if(playerEntity == null) {
            event.reply("Nie masz połączonego konta").setEphemeral(true);
            return;
        }
        PlayerDiscordRewardEntity playerDiscordRewardEntity = playerDiscordRewardEntityManager.getPlayerDiscordRewardEntityByMinecraftId(playerEntity.getMinecraftId());
        if(playerDiscordRewardEntity == null) {
            playerDiscordRewardEntity = new PlayerDiscordRewardEntity(playerEntity.getMinecraftId(), playerEntity.getDiscordId(), false);
        }
        if(playerDiscordRewardEntity.isDiscordRewardCollected()) {
            event.reply("Odebrales juz ta nagrode").setEphemeral(true);
            return;
        }
        playerDiscordRewardEntity.setDiscordRewardCollected(true);
        playerDiscordRewardEntityManager.createOrUpdatePlayerDiscordRewardEntity(playerDiscordRewardEntity);

        rewardManager.svipReward.collect(playerEntity);
        // TODO change reward to what is suposed to be and notify player about reward

        event.reply("Odebrales nagorde X, mozesz ja odebrac logujac sie na serwer i wpisujac komende /odbierz").setEphemeral(true).queue();

    }


}
