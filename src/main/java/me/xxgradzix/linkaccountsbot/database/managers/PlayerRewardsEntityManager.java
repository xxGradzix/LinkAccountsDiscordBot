package me.xxgradzix.linkaccountsbot.database.managers;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import me.xxgradzix.linkaccountsbot.database.entities.PlayerRewardsEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class PlayerRewardsEntityManager {
    private Dao<PlayerRewardsEntity, UUID> entityDao;

    public PlayerRewardsEntityManager(ConnectionSource connectionSource) {
        try {
            entityDao = DaoManager.createDao(connectionSource, PlayerRewardsEntity.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createPlayerRewardsEntity(PlayerRewardsEntity entity) {
        try {
            entityDao.create(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void createPlayerRewardsEntityIfNotExists(PlayerRewardsEntity entity) {
        try {
            entityDao.createIfNotExists(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void createOrUpdatePlayerRewardsEntity(PlayerRewardsEntity entity) {
        try {
            entityDao.createOrUpdate(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PlayerRewardsEntity getPlayerRewardsEntityByMinecraftId(UUID id) {
        try {
            return entityDao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public List<PlayerRewardsEntity> getPlayerRewardsEntities() {
        try {
            return entityDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updatePlayerRewardsEntity(PlayerRewardsEntity entity) {
        try {
            entityDao.update(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isPlayerRewardsEntityExistsByUUID(UUID uuid) {
        try {
            PlayerRewardsEntity entity = entityDao.queryForId(uuid);
            return entity != null;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public void deletePlayerRewardsEntity(PlayerRewardsEntity entity) {
        try {
            entityDao.delete(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
