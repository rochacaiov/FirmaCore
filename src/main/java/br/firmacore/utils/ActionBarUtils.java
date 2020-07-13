package br.firmacore.utils;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class ActionBarUtils {

    public static void sendMessage(Player player, String message){
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }

    public static void sendCountdownMessage(Player player, int countdown){
        player.spigot().sendMessage(
                ChatMessageType.ACTION_BAR,
                new TextComponent(
                        MessageUtils.color("&eVocê deve confirmar em &c" + countdown + " &esegundos...")
                )
        );
    }
}
