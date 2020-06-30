package br.firmacore.controllers.house.runnables;

import br.firmacore.controllers.house.HouseManager;
import org.bukkit.scheduler.BukkitRunnable;

public class AlertLimitTaxRunnable extends BukkitRunnable {
    HouseManager houseManager;

    public AlertLimitTaxRunnable(HouseManager houseManager){
        this.houseManager = houseManager;
    }

    @Override
    public void run() {
        this.houseManager.alertTribute();
    }
}
