package br.firmacore.utils;

import br.firmacore.Main;
import com.google.gson.stream.JsonReader;
import com.sk89q.util.ReflectionUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;


public final class PlayerUtils {

    // Accept
    public static void sendJsonConfirmation(Player player, String message, String command) {
        MessageUtils.clearMessageToPlayer(player, "");
        MessageUtils.normalMessageToPlayer(player, message);
        MessageUtils.clearMessageToPlayer(player, "");
        TextComponent confirmation = new TextComponent("  ➥ CONFIRMAR");
        confirmation.setColor(ChatColor.GREEN);
        confirmation.setBold(true);
        confirmation.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        confirmation.setHoverEvent(
                new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("§bCLIQUE PARA ACEITAR!").create())
        );
        player.spigot().sendMessage(confirmation);
        MessageUtils.clearMessageToPlayer(player, "");
    }

    public static void playSound(Player player, Sound sound){
        player.playSound(player.getLocation(), sound, 2.0F, 1.0F);
    }

    public static void teleportPlayer(Main plugin, Player player, Location location){
        double xPlayer = player.getLocation().getBlockX();
        MessageUtils.informativeMessageToPlayer(player, "&6Você será teleportado em &85 &6segundos!");
        new BukkitRunnable() {
            @Override
            public void run() {
                if(player.getLocation().getBlockX() == xPlayer){
                    MessageUtils.successMessageToPlayer(player, "&eTeleportando...");
                    player.teleport(location, PlayerTeleportEvent.TeleportCause.COMMAND);
                    return;
                }

                MessageUtils.errorMessageToPlayer(
                        player,
                        "&cTeleporte cancelado. Você se moveu!"
                );
            }
        }.runTaskLater(plugin, 20 * 5);
    }

    public static void spawnFirework(Player player){
        Firework firework = (Firework) player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
        FireworkMeta meta = firework.getFireworkMeta();
        meta.setPower(2);
        meta.addEffect(
                FireworkEffect.builder()
                        .withColor(Color.ORANGE)
                        .flicker(true)
                        .trail(false)
                        .with(FireworkEffect.Type.STAR)
                        .withFade(Color.FUCHSIA)
                        .build()
        );

        firework.setFireworkMeta(meta);
    }
}
