package br.firmacore.services.tribute.runnables;

import br.firmacore.services.tribute.cache.TributeManager;
import org.bukkit.scheduler.BukkitRunnable;

public class TributeRunnable extends BukkitRunnable {
    private TributeManager tributeManager;

    public TributeRunnable(TributeManager tributeManager){
        this.tributeManager = tributeManager;
    }


    @Override
    public void run() {
        /**int actualDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        int actualHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        this.tributeManager.getTributes().forEach(tribute -> {
            if(tribute.getDayApplyTribute() == actualDay && tribute.getHourApplyTribute() == actualHour){
                try {
                    House house = this.houseManager.getHouse(tribute.getOwner());
                    Player owner = Bukkit.getPlayer(UUID.fromString(tribute.getUuid()));
                    int size = house.getSize();

                    if(tribute.getAmount() == 6){
                        this.houseService.removeHouse(house);
                        return;
                    }

                    tribute.setAmount(tribute.getAmount() + 1);
                    tribute.setValue(tribute.getValue() + TributeServiceImpl.calculateValue(size, tribute.getPropertyType()));

                    this.tributeManager.saveOrUpdate(tribute);

                    if(owner != null){
                        MessageUtils.messageToPlayer(owner, "TRIBUTO ADICIONADO!");
                        MessageUtils.messageToPlayer(owner, "TYPE: " + tribute.getPropertyType());
                        MessageUtils.messageToPlayer(owner, "AMOUNT: " + tribute.getAmount());
                        MessageUtils.messageToPlayer(owner, "VALUE: " + tribute.getValue());
                    }

                } catch (PropertyNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });**/
    }
}
