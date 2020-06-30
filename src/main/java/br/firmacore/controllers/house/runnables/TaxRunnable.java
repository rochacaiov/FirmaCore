package br.firmacore.controllers.house.runnables;

import br.firmacore.controllers.house.HouseManager;
import org.bukkit.scheduler.BukkitRunnable;

public class TaxRunnable extends BukkitRunnable {
    private HouseManager houseManager;

    public TaxRunnable(HouseManager houseManager){
        this.houseManager = houseManager;
    }

    @Override
    public void run() {
        this.houseManager.checkTributesDay();
    }
}
