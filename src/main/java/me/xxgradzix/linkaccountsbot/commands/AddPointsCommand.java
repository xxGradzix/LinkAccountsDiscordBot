package me.xxgradzix.ageplaydiscordbot.commands;

import me.xxgradzix.ageplaydiscordbot.AgePlayDiscordBot;
import me.xxgradzix.ageplaydiscordbot.database.entities.PlayerEntity;
import me.xxgradzix.ageplaydiscordbot.database.managers.PlayerEntityManager;
import me.xxgradzix.ageplaydiscordbot.managers.PointManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class AddPointsCommand extends ListenerAdapter {

    private final PlayerEntityManager entityManager = AgePlayDiscordBot.getPlayerEntityManager();
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        if(!event.getName().equalsIgnoreCase("addpoints")) return;

        MessageEmbed embed = new EmbedBuilder()
                .setTitle("Dodaj punkty!")
                .setDescription("Chuj test")
                .setColor(Color.red)
                .build();

        net.dv8tion.jda.api.interactions.components.buttons.Button addPointsBtn = net.dv8tion.jda.api.interactions.components.buttons.Button.primary("button_add_points", "Dodaj punkty");


        // TODO Embed add points

        event.getChannel().sendMessageEmbeds(embed)
                        .addActionRow(addPointsBtn)
                                .queue();
        event.reply("Utworzyles embed").setEphemeral(true).queue();
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {

        if(event.getButton().getId().equalsIgnoreCase("button_add_points")) {

            TextInput playerNick = TextInput.create("form-add-points-nick", "Nick", TextInputStyle.SHORT)
                    .setRequired(true)
                    .setLabel("Nick gracza:")
                    .setPlaceholder("Podaj nick gracza który ma otrzymać punkty")
                    .build();

            TextInput points = TextInput.create("form-add-points-points", "Points", TextInputStyle.SHORT)
                    .setRequired(true)
                    .setLabel("Ile punktow:")
                    .setPlaceholder("Podaj ile ponktow")
                    .build();

            Modal modal = Modal.create("form-add-points", "Odbierz nagrode")
                    .addActionRow(playerNick)
                    .addActionRow(points)
                    .build();

            event.replyModal(modal).queue();
        }
    }
    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {

        if(!event.getModalId().equalsIgnoreCase("form-add-points")) return;

        String nick = event.getValue("form-add-points-nick").getAsString();
        int points = Integer.parseInt(event.getValue("form-add-points-points").getAsString());

        PlayerEntity playerEntity = entityManager.getPlayerEntityByDiscordNick(nick);

        if(playerEntity == null) {
            // TODO Embed nie ma powiazanego konta
            event.reply("Taki gracz nie ma powiązanego konta").setEphemeral(true).queue();
            return;
        }

        PointManager.addPoints(playerEntity, points);

        // TODO Embed adding points
        event.reply("Dodano " + points + " punktow graczowi o nicku dc: " + nick).setEphemeral(true).queue();

    }

}
