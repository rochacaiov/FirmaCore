package br.firmacore.utils;

import br.firmacore.Main;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public final class PlayerUtils {

    public static void teleportPlayer(Main plugin, Player player, Location location){
        double xPlayer = player.getLocation().getBlockX();
        MessageUtils.informativeMessageToPlayer(player, "&6Você será teleportado em &85 &6segundos. Não se mova!");
        new BukkitRunnable() {
            @Override
            public void run() {
                if(player.getLocation().getBlockX() == xPlayer){
                    MessageUtils.successMessageToPlayer(player, "&eTeleportando...");
                    player.teleportAsync(location, PlayerTeleportEvent.TeleportCause.COMMAND);
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
