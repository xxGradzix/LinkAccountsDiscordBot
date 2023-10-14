package me.xxgradzix.linkaccountsbot.commands;


import me.xxgradzix.linkaccountsbot.database.entities.PlayerEntity;
import me.xxgradzix.linkaccountsbot.database.managers.PlayerEntityManager;
import me.xxgradzix.linkaccountsbot.managers.RewardManager;
import me.xxgradzix.linkaccountsbot.rewards.Reward;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class ExchangePointsCommands extends ListenerAdapter {

    private final PlayerEntityManager playerEntityManager;

    private final RewardManager rewardManager;

    public ExchangePointsCommands(PlayerEntityManager playerEntityManager, RewardManager rewardManager) {
        this.playerEntityManager = playerEntityManager;
        this.rewardManager = rewardManager;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        if(!event.getName().equalsIgnoreCase("exchangesetup")) return;

        MessageEmbed embed = new EmbedBuilder()

                .setTitle("Rynek - AgePlay.PL")
                .setDescription("Zdobywaj Punkty i wymień je potem na rangi lub pakiety na serwerze AgePlay.PL\n" +
                        "\n" +
                        "• Reakcja pod Ogłoszeniami = 15 Pkt\n" +
                        "• Reakcja pod Ankietami = 15 Pkt\n" +
                        "• Reakcja pod Konkursami = 15 Pkt\n" +
                        "• Ulepszenia Serwera = 100 Pkt\n" +
                        "• Dodaniu Pomysłu na Serwer = 300 Pkt\n" +
                        "• Aktywność Czatu = 5 Pkt\n" +
                        "• Aktywność Głosowa =5 Pkt\n" +
                        "• Posiadanie Rangi Twórca/Wspierający = 150 Pkt ( Co Miesiąc ) ")
                .setFooter("Aktywność Kanałow Czatu = Codziennie\n" +
                        "Aktywność Kanałów Głosowych = Codziennie\n" +
                        "Posiadanie Rang = Co Miesiąc")

                .setColor(Color.red)
                .build();

        Button exchangeBtn = Button.primary("button_exchange", "Wymień");
        Button balanceBtn = Button.primary("button_balance", "Stan konta");

        event.getChannel().sendMessageEmbeds(embed)
                .addActionRow(exchangeBtn, balanceBtn)
                .queue();
        event.reply("Utworzyles embed").setEphemeral(true).queue();

    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {

        Long userDiscordId = event.getUser().getIdLong();
        PlayerEntity playerEntity = playerEntityManager.getPlayerEntityByDiscordId(userDiscordId);

        if(event.getButton().getId().equalsIgnoreCase("button_exchange")) {
            if(playerEntity == null) {
                //todo musisz miec polaczone konto embed
                event.reply("Musisz miec polaczone konto by odebrać nagrodę").setEphemeral(true).queue();
                return;
            }

            TextInput rank = TextInput.create("form-rank", "Ranga", TextInputStyle.SHORT)
                    .setRequired(true)
                    .setLabel("Ranga:")
                    .setPlaceholder("Ranga jaką chcesz odebrać (vip, svip, age")
                    .build();

            Modal modal = Modal.create("form-modal", "Odbierz nagrodę")
                    .addActionRow(rank)
                    .build();

            event.replyModal(modal).queue();
            return;
        }
        if(event.getButton().getId().equalsIgnoreCase("button_balance")) {
            if(playerEntity == null) {
                event.reply("Musisz miec polaczone konto by sprawdzić stan konta").setEphemeral(true).queue();
                return;
            }
            //TOdo stan konta embed
            event.reply("Twój stan konta to " + playerEntity.getPoints()).setEphemeral(true).queue();
        }
    }
    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {

        if(!event.getModalId().equalsIgnoreCase("form-modal")) return;

        Long userDiscordId = event.getUser().getIdLong();

        PlayerEntity playerEntity = playerEntityManager.getPlayerEntityByDiscordId(userDiscordId);

        if(playerEntity == null) {
            event.reply("Musisz miec polaczone koto by odebrac nagrode").setEphemeral(true).queue();
            return;
        }
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerEntity.getMinecraftId());



        // TODO blad gracz = null
        // TODO gracz offline
        if(player == null || !player.isConnected()) {
            event.reply("Tego gracza nie ma na serwerze").setEphemeral(true).queue();
            return;
        }

        if(event.getValue("form-rank") == null) {
            // TODO musisz podac range
            event.reply("Musisz podać range jaka chcesz odebrać").setEphemeral(true).queue();
            return;
        }

        String rank = event.getValue("form-rank").getAsString().toLowerCase();

        Reward reward = getReward(rank);

        if(reward == null) {
            // TODO zla ranga
            event.reply("Zła ranga").setEphemeral(true).queue();
            return;
        }

        if(!reward.canAfford(playerEntity)) {
            event.reply("Nie masz wystarczającej ilości punktów by odebrać tą nagrodę").setEphemeral(true).queue();
            return;
        }

        reward.collect(playerEntity);

        // TODO odebrales nagrode
        event.reply("Odebrałeś nagrodę " + reward.getName()).setEphemeral(true).queue();
    }

    public Reward getReward(String rank) {
        rank = rank.toLowerCase();
        Reward reward;
        switch (rank) {
            case "vip":
                reward = rewardManager.vipReward;
                break;
            case "svip":
                reward = rewardManager.svipReward;
                break;
            case "age":
                reward = rewardManager.ageReward;
                break;
            default:
                return null;
        }

        return reward;
    }


}