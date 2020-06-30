package br.firmacore.controllers.tribute.runnables;

import br.firmacore.Main;
import br.firmacore.services.impl.TributeServiceImpl;
import org.bukkit.scheduler.BukkitRunnable;

public class TributeRunnable extends BukkitRunnable {
    private TributeServiceImpl tributeService;

    public TributeRunnable(TributeServiceImpl tributeService){
        this.tributeService = tributeService;
    }


    @Override
    public void run() {
        this.tributeService.sendTribute();
    }
}
