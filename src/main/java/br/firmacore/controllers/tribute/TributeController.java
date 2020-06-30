package br.firmacore.controllers.tribute;

import br.firmacore.Main;
import br.firmacore.controllers.tribute.repositories.TributeRepository;
import br.firmacore.controllers.tribute.runnables.TributeRunnable;
import br.firmacore.services.impl.TributeServiceImpl;

public class TributeController {
    public static final double PERCENTAGE_TRIBUTE = 0.3;
    public static final int LIMIT_TRIBUTE = 6;

    private TributeRepository tributeRepository;
    private TributeManager tributeManager;
    private TributeServiceImpl tributeService;

    public TributeController(Main plugin){
        this.tributeRepository = new TributeRepository(plugin);
        this.tributeManager = new TributeManager(plugin);
        this.tributeService = new TributeServiceImpl(plugin);

        new TributeRunnable(plugin).runTaskTimer(plugin, 0, 20 * 60);
    }

    public TributeRepository getTributeRepository() {
        return tributeRepository;
    }

    public TributeManager getTributeManager() {
        return tributeManager;
    }

    public TributeServiceImpl getTributeService() {
        return tributeService;
    }
}
