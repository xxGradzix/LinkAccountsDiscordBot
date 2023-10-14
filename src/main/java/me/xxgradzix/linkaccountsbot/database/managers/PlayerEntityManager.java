package me.xxgradzix.linkaccountsbot.database.managers;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import me.xxgradzix.linkaccountsbot.database.entities.PlayerEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class PlayerEntityManager {
    private Dao<PlayerEntity, UUID> entityDao;

    public PlayerEntityManager(ConnectionSource connectionSource) {
        try {
            entityDao = DaoManager.createDao(connectionSource, PlayerEntity.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createPlayerEntity(PlayerEntity entity) {
        try {
            entityDao.create(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void createPlayerEntityIfNotExists(PlayerEntity entity) {
        try {
            entityDao.createIfNotExists(entity);
//            entityDao.create(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void createOrUpdatePlayerEntity(PlayerEntity entity) {
        try {
            entityDao.createOrUpdate(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PlayerEntity getPlayerEntityByMinecraftId(UUID id) {
        try {
            return entityDao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public PlayerEntity getPlayerEntityByDiscordId(Long id) {
        for(PlayerEntity playerEntity : getPlayerEntities()) {
            if(playerEntity.getDiscordId().equals(id)) return playerEntity;
        }
        return null;
    }
    public PlayerEntity getPlayerEntityByDiscordNick(String nick) {
        for(PlayerEntity playerEntity : getPlayerEntities()) {
            if(playerEntity.getDiscordNick().equals(nick)) return playerEntity;
        }
        return null;
    }
    public List<PlayerEntity> getPlayerEntities() {
        try {
            return entityDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updatePlayerEntity(PlayerEntity entity) {
        try {
            entityDao.update(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isPlayerExistsByUUID(UUID uuid) {
        try {
            PlayerEntity entity = entityDao.queryForId(uuid);
            return entity != null;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public void deletePlayerEntity(PlayerEntity entity) {
        try {
            entityDao.delete(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
