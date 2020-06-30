package br.firmacore.utils;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class MessageUtils {
    private static final String TAG = " §6* F§6irma§c§lCORE §8> ";

    public static void messageToPlayerWithTag(Player sender, String message){
        sender.sendMessage(TAG + color(message));
    }

    public static void messageToPlayer(Player sender, String message){
        sender.sendMessage(color(message));
    }

    public static void messageToBroadcastWithTag(String message){
        Bukkit.broadcastMessage(TAG + color(message));
    }

    public static void messageToBroadcast(String message){
        Bukkit.broadcastMessage(color(message));
    }

    public static void messageToConsole(String message){
        Bukkit.getLogger().info(TAG + color(message));
    }

    private static String color(String linha){
        return ChatColor.translateAlternateColorCodes('&', linha);
    }
}
