package me.xxgradzix.linkaccountsbot.events;


import me.xxgradzix.linkaccountsbot.LinkAccountsBot;
import me.xxgradzix.linkaccountsbot.managers.PointManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.scheduler.TaskScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class AddPointsSynchronously extends ListenerAdapter {

    private final PointManager pointManager;

    private final Set<Long> chatActivityPlayersWithAddedPoints = new HashSet<>();
    private final Set<Long> voiceChannelActivityPlayersWithAddedPoints = new HashSet<>();

    public AddPointsSynchronously(LinkAccountsBot plugin, PointManager pointManager) {
        this.pointManager = pointManager;

        TaskScheduler scheduler = ProxyServer.getInstance().getScheduler();

        scheduler.schedule(plugin, () -> {
            chatActivityPlayersWithAddedPoints.clear();
            voiceChannelActivityPlayersWithAddedPoints.clear();
        },0 , 1, TimeUnit.DAYS);
    }
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        User user = event.getAuthor();

        if(user.isBot()) return;

        System.out.println(user.getIdLong());
        System.out.println(user.getName());

        long userId = user.getIdLong();

        if(chatActivityPlayersWithAddedPoints.contains(userId)) return;

        pointManager.addPoints(userId, 5);

        chatActivityPlayersWithAddedPoints.add(userId);

    }

    @Override
    public void onGuildVoiceUpdate(@NotNull GuildVoiceUpdateEvent event) {

        Member member = event.getMember();

        long userId = member.getIdLong();

        if(voiceChannelActivityPlayersWithAddedPoints.contains(userId)) return;

        pointManager.addPoints(userId, 5);

        voiceChannelActivityPlayersWithAddedPoints.add(userId);
    }


}
