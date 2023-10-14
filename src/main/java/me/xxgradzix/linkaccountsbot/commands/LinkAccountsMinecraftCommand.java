package me.xxgradzix.linkaccountsbot.commands;

import me.xxgradzix.linkaccountsbot.LinkAccountsBot;
import me.xxgradzix.linkaccountsbot.database.managers.PlayerEntityManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class LinkAccountsMinecraftCommand extends Command {

    private static final HashMap<String, ProxiedPlayer> codeAndPlayerCoolDowns = new HashMap<>();

    private final LinkAccountsBot plugin;
    private final PlayerEntityManager playerEntityManager;

    public LinkAccountsMinecraftCommand(LinkAccountsBot plugin, PlayerEntityManager playerEntityManager) {
        super("discord");
        this.plugin = plugin;
        this.playerEntityManager = playerEntityManager;
    }

    @Override
    public void execute(CommandSender sender, String[] strings) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(TextComponent.fromLegacyText("This command is for players only"));
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;

        if(playerEntityManager.isPlayerExistsByUUID(player.getUniqueId())) {
            sender.sendMessage(TextComponent.fromLegacyText(ChatColor.GRAY + "To konto zostało już połączone z kontem discord"));
            return;
        }

        String code = getCodeByPlayer(player);

        if(code != null) {
            sender.sendMessage(TextComponent.fromLegacyText(ChatColor.GRAY + "Twój kod został już wygenerowany wcześniej: " + ChatColor.GOLD + code));
            return;
        }

        code = generateRandomCode(8);

        addPlayerData(code, player);

        sender.sendMessage(TextComponent.fromLegacyText(ChatColor.GRAY + "Twój kod to " + ChatColor.GOLD + code));
    }

    private void addPlayerData(String code, ProxiedPlayer player) {

        if(getCodeByPlayer(player) != null) {
            System.out.println("Powtarza sie gracz w roznych wartosciach mapy");
            return;
        }

        codeAndPlayerCoolDowns.putIfAbsent(code, player);

        ProxyServer.getInstance().getScheduler().schedule(plugin, () -> {
            removePlayerByCode(code);
            player.sendMessage(TextComponent.fromLegacyText(ChatColor.GRAY + "Ważność Twojego kodu" + ChatColor.RED + " wygasła"));
        }, 30, TimeUnit.SECONDS);
    }

    public static HashMap<String, ProxiedPlayer> getCodeAndPlayerCoolDowns() {
        return new HashMap<>(codeAndPlayerCoolDowns);
    }
    private static void removePlayerByCode(String code) {
        codeAndPlayerCoolDowns.remove(code);
    }
    private static String getCodeByPlayer(ProxiedPlayer player) {
        for(String code : codeAndPlayerCoolDowns.keySet()) {
            if(codeAndPlayerCoolDowns.get(code).equals(player)) {
                return code;
            }
        }
        return null;
    }
    private static boolean doesCodeExists(String code) {
        return codeAndPlayerCoolDowns.containsKey(code);
    }
    public static String generateRandomCode(int length) {

        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        SecureRandom random = new SecureRandom();

        StringBuilder code = new StringBuilder();

        do {
            code.setLength(0);
            for (int i = 0; i < length; i++) {
                int randomIndex = random.nextInt(characters.length());
                char randomChar = characters.charAt(randomIndex);
                code.append(randomChar);
            }
        } while (doesCodeExists(String.valueOf(code)));

        return code.toString();
    }
}
