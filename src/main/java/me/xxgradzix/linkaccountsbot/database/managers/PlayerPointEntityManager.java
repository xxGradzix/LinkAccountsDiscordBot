package me.xxgradzix.linkaccountsbot.database.managers;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import me.xxgradzix.linkaccountsbot.database.entities.PlayerPointEntity;

import java.sql.SQLException;
import java.util.*;

public class PlayerPointEntityManager {

    private Dao<PlayerPointEntity, Long> entityDao;

    public PlayerPointEntityManager(ConnectionSource connectionSource) {
        try {
            entityDao = DaoManager.createDao(connectionSource, PlayerPointEntity.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createPlayerPointEntity(PlayerPointEntity entity) {
        try {
            entityDao.create(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void createPlayerPointEntityIfNotExists(PlayerPointEntity entity) {
        try {
            entityDao.createIfNotExists(entity);
//            entityDao.create(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void createOrUpdatePlayerPointEntity(PlayerPointEntity entity) {
        try {
            entityDao.createOrUpdate(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Map<Long, Set<Long>> getReactedAnnouncementMessagesMap() {
        Map<Long, Set<Long>> reactedMessages = new HashMap<>();

        for (PlayerPointEntity playerPointEntity : getPlayerPointEntities()) {
            reactedMessages.put(playerPointEntity.getDiscordId(), playerPointEntity.getReactedAnnouncementMessagesIds());
        }
        return reactedMessages;

    }
    public Map<Long, Set<Long>> getReactedContestMessagesMap() {
        Map<Long, Set<Long>> reactedMessages = new HashMap<>();

        for (PlayerPointEntity playerPointEntity : getPlayerPointEntities()) {
            reactedMessages.put(playerPointEntity.getDiscordId(), playerPointEntity.getReactedContestMessageIds());
        }
        return reactedMessages;

    }
    public Map<Long, Set<Long>> getReactedPollMessagesMap() {
        Map<Long, Set<Long>> reactedMessages = new HashMap<>();

        for (PlayerPointEntity playerPointEntity : getPlayerPointEntities()) {
            reactedMessages.put(playerPointEntity.getDiscordId(), playerPointEntity.getReactedPollMessagesIds());
        }
        return reactedMessages;

    }

    public Set<Long> getReactedAnnouncementMessagesSet(long userId) {
        try {
            PlayerPointEntity entity = entityDao.queryForId(userId);

            if(entity.getReactedAnnouncementMessagesIds() == null || entity.getReactedAnnouncementMessagesIds().isEmpty()) return new HashSet<>();

            return entity.getReactedAnnouncementMessagesIds();

        } catch (SQLException e) {
            e.printStackTrace();
            return new HashSet<>();
        }

    }
    public Set<Long> getReactedContestMessagesSet(long userId) {
        try {
            PlayerPointEntity entity = entityDao.queryForId(userId);

            if(entity.getReactedContestMessageIds() == null || entity.getReactedContestMessageIds().isEmpty()) return new HashSet<>();

            return entity.getReactedContestMessageIds();

        } catch (SQLException e) {
            e.printStackTrace();
            return new HashSet<>();
        }

    }
    public Set<Long> getReactedPollMessagesSet(long userId) {
        try {
            PlayerPointEntity entity = entityDao.queryForId(userId);

            if(entity.getReactedPollMessagesIds() == null || entity.getReactedPollMessagesIds().isEmpty()) return new HashSet<>();

            return entity.getReactedPollMessagesIds();

        } catch (SQLException e) {
            e.printStackTrace();
            return new HashSet<>();
        }

    }



    public List<PlayerPointEntity> getPlayerPointEntities() {
        try {
            return entityDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public PlayerPointEntity getPlayerPointEntities(long discordId) {
        try {
            PlayerPointEntity entity = entityDao.queryForId(discordId);
            if(entity == null) {
                entity = new PlayerPointEntity(discordId, new HashSet<>(), new HashSet<>(), new HashSet<>());
                createOrUpdatePlayerPointEntity(entity);
            }
            return entity;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updatePlayerPointEntity(PlayerPointEntity entity) {
        try {
            entityDao.update(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePlayerEntity(PlayerPointEntity entity) {
        try {
            entityDao.delete(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
