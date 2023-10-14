package me.xxgradzix.ageplaydiscordbot.commands;

import me.xxgradzix.ageplaydiscordbot.AgePlayDiscordBot;
import me.xxgradzix.ageplaydiscordbot.database.entities.PlayerEntity;
import me.xxgradzix.ageplaydiscordbot.database.managers.PlayerEntityManager;
import me.xxgradzix.ageplaydiscordbot.managers.RewardManager;
import me.xxgradzix.ageplaydiscordbot.rewards.Reward;
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
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class ExchangePointsCommands extends ListenerAdapter {

    private final PlayerEntityManager entityManager = AgePlayDiscordBot.getPlayerEntityManager();
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        if(!event.getName().equalsIgnoreCase("formsetup")) return;

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
                .setFooter("Aktywość Kanałow Czatu = Codziennie\n" +
                        "Aktywność Kanałów Głosowych = Codziennie\n" +
                        "Posiadanie Rang = Co Miesiąc")

                .setColor(Color.red)
                .build();

        Button exchangeBtn = Button.primary("button_wymien", "Wymień");
        Button balanceBtn = Button.primary("button_balance", "Stan konta");

        event.getChannel().sendMessageEmbeds(embed)
                .addActionRow(exchangeBtn, balanceBtn)
                .queue();
        event.reply("Utworzyles embed").setEphemeral(true).queue();

    }


    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {

        Long userDiscordId = event.getUser().getIdLong();
        PlayerEntity playerEntity = entityManager.getPlayerEntityByDiscordId(userDiscordId);

        if(event.getButton().getId().equalsIgnoreCase("button_wymien")) {
            if(playerEntity == null) {
                //todo musisz miec polaczone konto embed
                event.reply("Musisz miec polaczone koto by odebrac nagrode").setEphemeral(true).queue();
                return;
            }

            TextInput rank = TextInput.create("form-rank", "Ranga", TextInputStyle.SHORT)
                    .setRequired(true)
                    .setLabel("Ranga:")
                    .setPlaceholder("Ranga jaką chcesz odebrać (vip, svip, age")
                    .build();

            Modal modal = Modal.create("form-modal", "Odbierz nagrode")
                    .addActionRow(rank)
                    .build();

            event.replyModal(modal).queue();
            return;
        }
        if(event.getButton().getId().equalsIgnoreCase("button_balance")) {
            if(playerEntity == null) {
                event.reply("Musisz miec polaczone koto by sprawdzić stan konta").setEphemeral(true).queue();
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

        PlayerEntity playerEntity = entityManager.getPlayerEntityByDiscordId(userDiscordId);

        if(playerEntity == null) {
            event.reply("Musisz miec polaczone koto by odebrac nagrode").setEphemeral(true).queue();
            return;
        }

        Player player = Bukkit.getPlayer(playerEntity.getMinecraftId());


        // TODO blad gracz = null
        // TODO gracz offline
        if(player == null || !player.isOnline()) {
            event.reply("Tego gracza nie ma na serwerze").setEphemeral(true).queue();
            return;
        }

        if(event.getValue("form-rank") == null) {
            // TODO musisz podac range
            event.reply("Musisz podac range jaka chcesz odebrac").setEphemeral(true).queue();
            return;
        }

        String rank = event.getValue("form-rank").getAsString().toLowerCase();

        Reward reward = getReward(rank);

        if(reward == null) {
            // TODO zla ranga
            event.reply("Zła ranga").setEphemeral(true).queue();
            return;
        }

        reward.collect(playerEntity);

        if(!reward.canAfford(playerEntity)) {
            event.reply("Nie masz wystarczającej ilosci punktów by odebrać tą nagrode").setEphemeral(true).queue();
            return;
        }

        // TODO odebrales nagrode
        event.reply("Odebrałeś nagrode").setEphemeral(true).queue();
    }

    public Reward getReward(String rank) {
        rank = rank.toLowerCase();
        Reward reward = null;
        switch (rank) {
            case "vip":
                reward = RewardManager.VIP_REWARD;
                break;
            case "svip":
                reward = RewardManager.SVIP_REWARD;
                break;
            case "age":
                reward = RewardManager.AGE_REWARD;
                break;
            default:
                return null;
        }

        return reward;
    }


}