package me.xxgradzix.ageplaydiscordbot.database.managers;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import me.xxgradzix.ageplaydiscordbot.database.entities.PlayerPointEntity;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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



    public List<PlayerPointEntity> getPlayerPointEntities() {
        try {
            return entityDao.queryForAll();
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
