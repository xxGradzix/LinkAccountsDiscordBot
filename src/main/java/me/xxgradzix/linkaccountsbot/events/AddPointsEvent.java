package me.xxgradzix.linkaccountsbot.events;

import me.xxgradzix.linkaccountsbot.database.entities.PlayerPointEntity;
import me.xxgradzix.linkaccountsbot.database.managers.PlayerEntityManager;
import me.xxgradzix.linkaccountsbot.database.managers.PlayerPointEntityManager;
import me.xxgradzix.linkaccountsbot.managers.PointManager;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateBoostTimeEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class AddPointsEvent extends ListenerAdapter {

    private final PlayerPointEntityManager playerPointEntityManager;

    private final PointManager pointManager;

    public AddPointsEvent(PlayerEntityManager playerEntityManager, PlayerPointEntityManager playerPointEntityManager, PointManager pointManager) {
        this.playerPointEntityManager = playerPointEntityManager;
        this.pointManager = pointManager;
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

        PlayerPointEntity playerPointEntity = playerPointEntityManager.getPlayerPointEntities(userId);

        if(playerPointEntity == null) throw  new NullPointerException("Zwraca nulla, pewnie try catch rzuca wyjatek");

        Set<Long> userReactedMessages;

        switch(channelId) {
            case ANNOUNCEMENT_CHANNEL_ID: {
//                userReactedMessages = reactedAnnouncementMessages.getOrDefault(userId, new HashSet<>());
                userReactedMessages = playerPointEntity.getReactedAnnouncementMessagesIds();

                if(!userReactedMessages.contains(messageId)) {

                    pointManager.addPoints(userId, MESSAGE_REACTION_POINTS_AMOUNT);
                    userReactedMessages.add(messageId);
//                    reactedAnnouncementMessages.put(userId, userReactedMessages);
                    playerPointEntity.setReactedAnnouncementMessagesIds(userReactedMessages);
                }

                break;
            }
            case CONTEST_CHANNEL_ID: {
//                userReactedMessages = reactedContestMessages.getOrDefault(userId, new HashSet<>());
                userReactedMessages = playerPointEntity.getReactedContestMessageIds();

                if(!userReactedMessages.contains(messageId)) {

                    pointManager.addPoints(userId, MESSAGE_REACTION_POINTS_AMOUNT);

                    userReactedMessages.add(messageId);
//                    reactedContestMessages.put(userId, userReactedMessages);
                    playerPointEntity.setReactedContestMessageIds(userReactedMessages);

                }

                break;
            }
            case POLL_CHANNEL_ID: {
//                userReactedMessages = reactedPollMessages.getOrDefault(userId, new HashSet<>());
                userReactedMessages = playerPointEntity.getReactedPollMessagesIds();

                if(!userReactedMessages.contains(messageId)) {

                    pointManager.addPoints(userId, MESSAGE_REACTION_POINTS_AMOUNT);

                    userReactedMessages.add(messageId);
//                    reactedPollMessages.put(userId, userReactedMessages);
                    playerPointEntity.setReactedPollMessagesIds(userReactedMessages);
                }
                break;
            }
            default: {
            }
            playerPointEntityManager.createOrUpdatePlayerPointEntity(playerPointEntity);
        }
    }

    @Override
    public void onGuildMemberUpdateBoostTime(@NotNull GuildMemberUpdateBoostTimeEvent event) {
        User user = event.getUser();

        if (user.isBot()) return;

        long userId = user.getIdLong();

        pointManager.addPoints(userId, 100);

    }
}
