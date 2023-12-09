package me.xxgradzix.linkaccountsbot;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import me.xxgradzix.linkaccountsbot.commands.AddPointsCommand;
import me.xxgradzix.linkaccountsbot.commands.ExchangePointsCommands;
import me.xxgradzix.linkaccountsbot.commands.LinkAccountsDiscordCommand;
import me.xxgradzix.linkaccountsbot.commands.LinkAccountsMinecraftCommand;
import me.xxgradzix.linkaccountsbot.database.entities.PlayerEntity;
import me.xxgradzix.linkaccountsbot.database.entities.PlayerPointEntity;
import me.xxgradzix.linkaccountsbot.database.entities.PlayerRewardsEntity;
import me.xxgradzix.linkaccountsbot.database.managers.PlayerEntityManager;
import me.xxgradzix.linkaccountsbot.database.managers.PlayerPointEntityManager;
import me.xxgradzix.linkaccountsbot.database.managers.PlayerRewardsEntityManager;
import me.xxgradzix.linkaccountsbot.events.AddPointsEvent;
import me.xxgradzix.linkaccountsbot.events.AddPointsSynchronously;
import me.xxgradzix.linkaccountsbot.managers.PointManager;
import me.xxgradzix.linkaccountsbot.managers.RewardManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.md_5.bungee.api.plugin.Plugin;

import java.sql.SQLException;

public final class LinkAccountsBot extends Plugin {

    /* DISCORD */
    private static JDA jda;
    public static final String TOKEN = "MTE1MzU5NjYyNDUwNTk0NjE4Mg.G2GHAd.iRivxhC_5BnsUH0r1cUMPJlY6YkPM13zkxUMXs";
    public static final Long GUILD_ID = 1144950609481572453L;

    private final ConnectionSource connectionSource;
    private final PlayerEntityManager playerEntityManager;
    private final PlayerPointEntityManager playerPointEntityManager;
    private final PlayerRewardsEntityManager playerRewardsEntityManager;

    private final PointManager pointManager;

    private final RewardManager rewardManager;



    public LinkAccountsBot() throws SQLException {

        /* DATABASE LOCAL */
        String databaseUrl = "jdbc:mysql://localhost:3306/gradzixcore";
        this.connectionSource = new JdbcConnectionSource(databaseUrl, "root", "");

        /* DATABASE AGEPLAY */
//        String databaseUrl = "jdbc:mysql://185.16.39.57:3306/s286_database";
//        this.connectionSource = new JdbcConnectionSource(databaseUrl, "u286_f8T7gXXzU1", "a65qmwbgH8Y@cg3dXm^qgSm6");

        TableUtils.createTableIfNotExists(connectionSource, PlayerEntity.class);
        TableUtils.createTableIfNotExists(connectionSource, PlayerPointEntity.class);
        TableUtils.createTableIfNotExists(connectionSource, PlayerRewardsEntity.class);

        playerEntityManager = new PlayerEntityManager(connectionSource);
        playerPointEntityManager = new PlayerPointEntityManager(connectionSource);
        this.playerRewardsEntityManager = new PlayerRewardsEntityManager(connectionSource);

        this.pointManager = new PointManager(playerEntityManager);
        this.rewardManager = new RewardManager(playerEntityManager, pointManager, playerRewardsEntityManager);


    }

    @Override
    public void onEnable() {
        try {

            getProxy().getPluginManager().registerCommand(this, new LinkAccountsMinecraftCommand(this, playerEntityManager));

            jda = JDABuilder.createDefault(TOKEN)
                    .addEventListeners(
                            new AddPointsCommand(playerEntityManager, pointManager),
                            new ExchangePointsCommands(playerEntityManager, rewardManager),
                            new LinkAccountsDiscordCommand(playerEntityManager),
                            new AddPointsEvent(playerEntityManager, playerPointEntityManager, pointManager),
                            new AddPointsSynchronously(this, pointManager))
                    .build()
                    .awaitReady()
            ;
            Guild guild = jda.getGuildById(GUILD_ID);

            if(guild != null) {
                guild.upsertCommand("exchangesetup", "odbierz").queue();
                guild.upsertCommand("powiazsetup", "powiaz").queue();
                guild.upsertCommand("addpoints", "addpoints").queue();
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
