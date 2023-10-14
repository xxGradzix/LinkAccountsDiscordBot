package me.xxgradzix.ageplaydiscordbot.events;

import me.xxgradzix.ageplaydiscordbot.AgePlayDiscordBot;
import me.xxgradzix.ageplaydiscordbot.managers.PointManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class AddPointsSynchronously extends ListenerAdapter {

    private AgePlayDiscordBot plugin;

    private Set<Long> chatActivityPlayersWithAddedPoints = new HashSet<>();
    private Set<Long> voiceChannelActivityPlayersWithAddedPoints = new HashSet<>();

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        User user = event.getAuthor();

        if(user == null) return;
        if(user.isBot()) return;

        System.out.println(user.getIdLong());
        System.out.println(user.getName());

        long userId = user.getIdLong();

        if(chatActivityPlayersWithAddedPoints.contains(userId)) return;

        PointManager.addPoints(userId, 5);

        chatActivityPlayersWithAddedPoints.add(userId);

    }

    @Override
    public void onGuildVoiceUpdate(@NotNull GuildVoiceUpdateEvent event) {

        Member member = event.getMember();

        long userId = member.getIdLong();

        if(voiceChannelActivityPlayersWithAddedPoints.contains(userId)) return;

        PointManager.addPoints(userId, 5);

        voiceChannelActivityPlayersWithAddedPoints.add(userId);
    }

    public AddPointsSynchronously(AgePlayDiscordBot plugin) {
        this.plugin = plugin;

        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.runTaskTimer(plugin, () -> {

            chatActivityPlayersWithAddedPoints.clear();
            voiceChannelActivityPlayersWithAddedPoints.clear();

        }, 0, 20L *
                60L *
                60L *
                24L
                );
    }
}
