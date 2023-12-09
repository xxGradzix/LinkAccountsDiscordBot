package me.xxgradzix.linkaccountsbot.database.managers;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import me.xxgradzix.linkaccountsbot.database.entities.PlayerDiscordRewardEntity;

import java.sql.SQLException;
import java.util.UUID;

public class PlayerDiscordRewardEntityManager {
    private Dao<PlayerDiscordRewardEntity, UUID> entityDao;

    public PlayerDiscordRewardEntityManager(ConnectionSource connectionSource) {
        try {
            entityDao = DaoManager.createDao(connectionSource, PlayerDiscordRewardEntity.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void createOrUpdatePlayerDiscordRewardEntity(PlayerDiscordRewardEntity entity) {
        try {
            entityDao.createOrUpdate(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PlayerDiscordRewardEntity getPlayerDiscordRewardEntityByMinecraftId(UUID id) {
        try {
            return entityDao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public PlayerDiscordRewardEntity getPlayerDiscordRewardEntityByDiscordId(Long id) {
        try {
            return entityDao.queryForEq("discordId", id).get(0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean isRewardCollected(UUID playerId) {
        try {
            PlayerDiscordRewardEntity entity = entityDao.queryForId(playerId);
            return entity.isDiscordRewardCollected();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public void setRewardCollected(UUID playerId) {
        try {
            PlayerDiscordRewardEntity entity = entityDao.queryForId(playerId);
            entity.setDiscordRewardCollected(true);
            createOrUpdatePlayerDiscordRewardEntity(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
