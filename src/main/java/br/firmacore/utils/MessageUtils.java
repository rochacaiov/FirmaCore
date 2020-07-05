package br.firmacore.utils;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class MessageUtils {
    public static final String PREFIX = " §6* Firma§c§lCORE ";
    public static final String SUFFIX = "» §6";
    public static final String INFO_TAG = PREFIX + "§b" + SUFFIX;
    public static final String SUCESS_TAG = PREFIX + "§a" + SUFFIX;
    public static final String ERROR_TAG = PREFIX + "§c" + SUFFIX;

    public static void sucessMessageToPlayer(Player sender, String message){
        sender.sendMessage(SUCESS_TAG + color(message));
    }

    public static void errorMessageToPlayer(Player sender, String message){
        sender.sendMessage(ERROR_TAG + color(message));
    }

    public static void informativeMessageToPlayer(Player sender, String message){
        sender.sendMessage(INFO_TAG + color(message));
    }

    public static void clearMessageToPlayer(Player sender, String message){
        sender.sendMessage(color(message));
    }

    public static void messageToBroadcastWithTag(String message){
        Bukkit.broadcastMessage(PREFIX + color(message));
    }

    public static void messageToBroadcast(String message){
        Bukkit.broadcastMessage(color(message));
    }

    public static void messageToConsole(String message){
        Bukkit.getLogger().info(INFO_TAG + color(message));
    }

    private static String color(String linha){
        return ChatColor.translateAlternateColorCodes('&', linha);
    }
}
