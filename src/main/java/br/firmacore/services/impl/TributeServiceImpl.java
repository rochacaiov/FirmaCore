package br.firmacore.services.impl;

import br.firmacore.Main;
import br.firmacore.controllers.house.HouseController;
import br.firmacore.controllers.house.HouseManager;
import br.firmacore.controllers.tribute.TributeController;
import br.firmacore.controllers.tribute.TributeManager;
import br.firmacore.controllers.tribute.repositories.TributeRepository;
import br.firmacore.controllers.tribute.repositories.models.Tribute;
import br.firmacore.controllers.tribute.runnables.TributeRunnable;
import br.firmacore.enums.PropertyType;
import br.firmacore.services.api.TributeService;
import br.firmacore.utils.MessageUtils;

import java.util.Calendar;

public class TributeServiceImpl implements TributeService {
    TributeManager tributeManager;
    TributeRepository tributeRepository;
    HouseManager houseManager;

    public TributeServiceImpl(Main plugin){
        this.tributeManager = plugin.getTributeController().getTributeManager();
        this.tributeRepository = plugin.getTributeController().getTributeRepository();
        this.houseManager = plugin.getHouseController().getHouseManager();
    }

    @Override
    public void sendTribute() {
        this.houseManager.getHouses().forEach(house -> {
            String uuid = house.getUuid();
            double value = (HouseController.BLOCK_VALUE * house.getSize()) * TributeController.PERCENTAGE_TRIBUTE;
            int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
            int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);


            if(house.getDayBuy() == currentDay && house.getHourBuy() == currentHour){

                if(this.tributeManager.getAmountTributes(house.getUuid(), PropertyType.CASA) ==
                        TributeController.LIMIT_TRIBUTE){

                    MessageUtils.messageToBroadcast("CASA ATINGIU LIMITE MÁXIMO DE TRIBUTOS!!");
                    return;
                }

                this.tributeManager.add(new Tribute(uuid, PropertyType.CASA, value));
            }
        });
    }

    @Override
    public void sendLimitAlert() {

    }

    @Override
    public void listTributes() {

    }
}
