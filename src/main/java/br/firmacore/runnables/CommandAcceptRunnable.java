package br.firmacore.runnables;

import br.firmacore.enums.PropertyType;
import br.firmacore.services.property.api.PropertyService;
import br.firmacore.services.property.exceptions.PropertyNotExistsException;
import br.firmacore.utils.ActionBarUtils;
import br.firmacore.utils.PlayerUtils;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CommandAcceptRunnable extends BukkitRunnable {
    private PropertyService propertyService;
    private Player player;
    private int countdown = 10;

    public CommandAcceptRunnable(Player player, PropertyService propertyService) {
        this.propertyService = propertyService;
        this.player = player;
    }

    @Override
    public void run() {
        if (this.countdown == 0) {
            try {
                ProtectedRegion region = this.propertyService.getProperty(this.player.getName(), PropertyType.DEMO);
                this.propertyService.removeProperty(region, this.player.getWorld(), Material.DARK_OAK_FENCE);
                PlayerUtils.playSound(this.player, Sound.UI_TOAST_OUT);
            } catch (PropertyNotExistsException e) {
                e.printStackTrace();
            } finally {
                cancel();
            }
        }

        ActionBarUtils.sendCountdownMessage(this.player, this.countdown);

        this.countdown--;
    }
}
