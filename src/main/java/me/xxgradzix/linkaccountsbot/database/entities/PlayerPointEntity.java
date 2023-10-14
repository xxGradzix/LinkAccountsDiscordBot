package me.xxgradzix.linkaccountsbot.database.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import me.xxgradzix.linkaccountsbot.database.LongSetPersister;

import java.util.HashSet;
import java.util.Set;

@DatabaseTable(tableName = "player_points")
public class PlayerPointEntity {

    @DatabaseField(unique = true, id = true)
    private Long discordId;


    @DatabaseField(persisterClass = LongSetPersister.class, columnDefinition = "LONGBLOB")
    private Set<Long> reactedAnnouncementMessagesIds;

    @DatabaseField(persisterClass = LongSetPersister.class, columnDefinition = "LONGBLOB")
    private Set<Long> reactedContestMessageIds;

    @DatabaseField(persisterClass = LongSetPersister.class, columnDefinition = "LONGBLOB")
    private Set<Long> reactedPollMessagesIds;

    public PlayerPointEntity() {
        reactedAnnouncementMessagesIds = new HashSet<>();
        reactedContestMessageIds = new HashSet<>();
        reactedPollMessagesIds = new HashSet<>();
    }

    public PlayerPointEntity(Long discordId, Set<Long> reactedAnnouncementMessagesIds, Set<Long> reactedContestMessageIds, Set<Long> reactedPollMessagesIds) {
        this.discordId = discordId;
        this.reactedAnnouncementMessagesIds = reactedAnnouncementMessagesIds;
        this.reactedContestMessageIds = reactedContestMessageIds;
        this.reactedPollMessagesIds = reactedPollMessagesIds;
    }

    public Long getDiscordId() {
        return discordId;
    }

    public void setDiscordId(Long discordId) {
        this.discordId = discordId;
    }

    public Set<Long> getReactedAnnouncementMessagesIds() {
        return reactedAnnouncementMessagesIds;
    }

    public void setReactedAnnouncementMessagesIds(Set<Long> reactedAnnouncementMessagesIds) {
        this.reactedAnnouncementMessagesIds = reactedAnnouncementMessagesIds;
    }

    public Set<Long> getReactedContestMessageIds() {
        return reactedContestMessageIds;
    }

    public void setReactedContestMessageIds(Set<Long> reactedContestMessageIds) {
        this.reactedContestMessageIds = reactedContestMessageIds;
    }

    public Set<Long> getReactedPollMessagesIds() {
        return reactedPollMessagesIds;
    }

    public void setReactedPollMessagesIds(Set<Long> reactedPollMessagesIds) {
        this.reactedPollMessagesIds = reactedPollMessagesIds;
    }
}
