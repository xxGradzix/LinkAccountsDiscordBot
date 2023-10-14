package me.xxgradzix.linkaccountsbot.commands;

import me.xxgradzix.linkaccountsbot.database.entities.PlayerEntity;
import me.xxgradzix.linkaccountsbot.database.managers.PlayerEntityManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class LinkAccountsDiscordCommand extends ListenerAdapter {

    private final PlayerEntityManager playerEntityManager;

    public LinkAccountsDiscordCommand(PlayerEntityManager playerEntityManager) {
        this.playerEntityManager = playerEntityManager;
    }
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        if(!event.getName().equals("powiazsetup")) return;

        MessageEmbed embed = new EmbedBuilder()
                .setTitle("Powiaz konto!")
                .setDescription("Wpisz w minecraft /discord a nastepnie kliknij ponizszy przycisk i przepisz kod")
                .setColor(Color.red)
                .build();

        net.dv8tion.jda.api.interactions.components.buttons.Button linkBtn = net.dv8tion.jda.api.interactions.components.buttons.Button.primary("button_powiaz", "Powiąż konto");
        net.dv8tion.jda.api.interactions.components.buttons.Button disconnectBtn = Button.primary("button_disconnect", "Rozłącz konto");


        event.getChannel().sendMessageEmbeds(embed)
                .addActionRow(linkBtn, disconnectBtn)
                .queue();
        event.reply("Utworzyles embed").setEphemeral(true).queue();
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {

        if((event.getButton().getId() == null)) {
            return;
        }

        if(event.getButton().getId().equalsIgnoreCase("button_powiaz")) {
            PlayerEntity playerEntity = playerEntityManager.getPlayerEntityByDiscordId(event.getUser().getIdLong());

            if(playerEntity != null) {
                //TODO masz juz powiazane konto
                event.reply("Masz juz powiazane konto z kontem minecraft: " + playerEntity.getDiscordNick()).setEphemeral(true).queue();
                return;
            }

            TextInput kod = TextInput.create("powiaz-form-kod", "Kod", TextInputStyle.SHORT)
                    .setRequired(true)
                    .setLabel("Kod:")
                    .setPlaceholder("Podaj kod wysłany w minecraft")
                    .build();

            Modal modal = Modal.create("powiaz-form-modal", "Powiąż konto")
                    .addActionRow(kod)
                    .build();

            event.replyModal(modal).queue();
        }
        if(event.getButton().getId().equalsIgnoreCase("button_disconnect")) {

            PlayerEntity playerEntity = playerEntityManager.getPlayerEntityByDiscordId(event.getUser().getIdLong());

            if(playerEntity == null) {
                // TODO disconnect message
                event.reply("Nie posiadasz powiązanego konta").setEphemeral(true).queue();
                return;
            }


        }



    }
    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {

        if(!event.getModalId().equalsIgnoreCase("powiaz-form-modal")) {
            return;
        }

        String kod = "";

        if(event.getValue("powiaz-form-kod") != null) {
            kod = event.getValue("powiaz-form-kod").getAsString();
        } else {
            event.reply("niepoprawny kod").setEphemeral(true).queue();
        }

        if(LinkAccountsMinecraftCommand.getCodeAndPlayerCoolDowns().containsKey(kod)) {

            ProxiedPlayer player = LinkAccountsMinecraftCommand.getCodeAndPlayerCoolDowns().get(kod);
            User user = event.getUser();

            Role role = event.getGuild().getRoleById("1160192287964401746");
            if(role != null && event.getMember() != null) {

                event.getGuild().addRoleToMember(event.getMember(), role).queue();
            }

            PlayerEntity playerEntity = new PlayerEntity(user.getIdLong(), player.getUniqueId(), user.getName(), player.getName());

            playerEntityManager.createPlayerEntityIfNotExists(playerEntity);

            event.reply("Powiązałeś konto z kontem minecraft " + player.getName() + " jezeli to nie Twoje konto zgłoś to administradcji").setEphemeral(true).queue();

        } else {
            event.reply("niepoprawny kod").setEphemeral(true).queue();
        }


    }

}
