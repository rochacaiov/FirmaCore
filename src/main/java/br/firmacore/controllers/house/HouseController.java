package br.firmacore.controllers.house;

import br.firmacore.Main;
import br.firmacore.controllers.house.repositories.HouseRepository;
import br.firmacore.enums.PropertyType;
import br.firmacore.services.impl.HouseServiceImpl;

public class HouseController {
    public static final PropertyType AREA_TYPE = PropertyType.CASA;
    public static final int BLOCK_VALUE = 1000;
    public static final double PVP_VALUE = 10000;

    private HouseRepository houseRepository;
    private HouseManager houseManager;
    private HouseServiceImpl houseService;

    public HouseController(Main plugin){
        this.houseRepository = new HouseRepository(plugin);
        this.houseManager = new HouseManager(plugin);
        this.houseService = new HouseServiceImpl(this);

        this.houseRepository.getAll().forEach(house -> {this.houseManager.add(house);});
    }

    public void updateAllHouses(){
        this.houseManager.getHouses().forEach(house -> this.houseRepository.update(house));
    }

    public HouseManager getHouseManager(){
        return this.houseManager;
    }

    public HouseRepository getHouseRepository(){
        return this.houseRepository;
    }

    public HouseServiceImpl getHouseService() {
        return houseService;
    }
}
