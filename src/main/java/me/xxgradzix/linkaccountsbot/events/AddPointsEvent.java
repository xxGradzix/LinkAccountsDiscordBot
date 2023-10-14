package me.xxgradzix.ageplaydiscordbot.events;

import me.xxgradzix.ageplaydiscordbot.AgePlayDiscordBot;
import me.xxgradzix.ageplaydiscordbot.database.entities.PlayerPointEntity;
import me.xxgradzix.ageplaydiscordbot.database.managers.PlayerPointEntityManager;
import me.xxgradzix.ageplaydiscordbot.managers.PointManager;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateBoostTimeEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AddPointsEvent extends ListenerAdapter {

    private static final Map<Long, Set<Long>> reactedAnnouncementMessages = AgePlayDiscordBot.getPlayerPointEntityManager().getReactedAnnouncementMessagesMap();
    private static final Map<Long, Set<Long>> reactedContestMessages = AgePlayDiscordBot.getPlayerPointEntityManager().getReactedContestMessagesMap();
    private static final Map<Long, Set<Long>> reactedPollMessages = AgePlayDiscordBot.getPlayerPointEntityManager().getReactedPollMessagesMap();

    public static void saveAllOnDisable() {
        PlayerPointEntityManager playerPointEntityManager = AgePlayDiscordBot.getPlayerPointEntityManager();

        Set<Long> userIds = new HashSet<>();

        userIds.addAll(reactedAnnouncementMessages.keySet());
        userIds.addAll(reactedContestMessages.keySet());
        userIds.addAll(reactedPollMessages.keySet());

        for(long userId : userIds) {
            PlayerPointEntity playerPointEntity = new PlayerPointEntity(userId,
                    reactedAnnouncementMessages.getOrDefault(userId, new HashSet<>()),
                    reactedContestMessages.getOrDefault(userId, new HashSet<>()),
                    reactedPollMessages.getOrDefault(userId, new HashSet<>()));
            playerPointEntityManager.createOrUpdatePlayerPointEntity(playerPointEntity);
        }

    }

    private static final String ANNOUNCEMENT_CHANNEL_ID = "13";
    private static final String CONTEST_CHANNEL_ID = "4536";
    private static final String POLL_CHANNEL_ID = "243";

    private static final int MESSAGE_REACTION_POINTS_AMOUNT = 15;

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {

        MessageReaction reaction = event.getReaction();

        User user = event.getUser();

        if(user == null) return;

        if (user.isBot()) return;

        long userId = user.getIdLong();

        long messageId = reaction.getMessageIdLong();

        String channelId = event.getChannel().getId();

        Set<Long> userReactedMessages;

        switch(channelId) {
            case ANNOUNCEMENT_CHANNEL_ID: {
                userReactedMessages = reactedAnnouncementMessages.getOrDefault(userId, new HashSet<>());

                if(!userReactedMessages.contains(messageId)) {

                    PointManager.addPoints(userId, MESSAGE_REACTION_POINTS_AMOUNT);

                    userReactedMessages.add(messageId);
                    reactedAnnouncementMessages.put(userId, userReactedMessages);
                }

                break;
            }
            case CONTEST_CHANNEL_ID: {
                userReactedMessages = reactedContestMessages.getOrDefault(userId, new HashSet<>());

                if(!userReactedMessages.contains(messageId)) {

                    PointManager.addPoints(userId, MESSAGE_REACTION_POINTS_AMOUNT);

                    userReactedMessages.add(messageId);
                    reactedContestMessages.put(userId, userReactedMessages);
                }

                break;
            }
            case POLL_CHANNEL_ID: {
                userReactedMessages = reactedPollMessages.getOrDefault(userId, new HashSet<>());

                if(!userReactedMessages.contains(messageId)) {

                    PointManager.addPoints(userId, MESSAGE_REACTION_POINTS_AMOUNT);

                    userReactedMessages.add(messageId);
                    reactedPollMessages.put(userId, userReactedMessages);
                }
                break;
            }
            default: {
            }
        }
    }

    @Override
    public void onGuildMemberUpdateBoostTime(@NotNull GuildMemberUpdateBoostTimeEvent event) {
        User user = event.getUser();

        if(user == null) return;

        if (user.isBot()) return;

        long userId = user.getIdLong();

        PointManager.addPoints(userId, 100);

    }
}
