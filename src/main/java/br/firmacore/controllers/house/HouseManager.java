package br.firmacore.controllers.house;

import br.firmacore.Main;
import br.firmacore.controllers.house.repositories.models.House;
import br.firmacore.controllers.house.repositories.models.OLDHouse;
import br.firmacore.controllers.house.runnables.AlertLimitTaxRunnable;
import br.firmacore.controllers.house.runnables.TaxRunnable;
import br.firmacore.services.exceptions.PropertyNotFoundException;
import br.firmacore.utils.PlayerUtils;

import java.util.HashSet;
import java.util.Set;

public class HouseManager {
    private Set<House> houses = new HashSet<>();

    HouseManager(Main plugin){
        //new TaxRunnable(this).runTaskTimer(plugin, 0, 20 * 60);
        //new AlertLimitTaxRunnable(this).runTaskTimerAsynchronously(plugin, 0, 20 * 10);
    }

    public void add(House house){
        this.houses.add(house);
    }

    public void saveOrUpdate(House house){
        this.houses.remove(house);
        add(house);
    }

    public void removeHouse(House house) {
        this.houses.remove(house);
    }

    private void removeAbandonedHouse(OLDHouse house) throws PropertyNotFoundException {
        /**
        World world = Bukkit.getWorld("world");
        assert world != null;
        ProtectedRegion region = WGServiceOLD.getProtection(house.getOwner().getName(), OLDHouse.AREA_TYPE);
        int tamanho = region.getMaximumPoint().getX() - region.getMinimumPoint().getX();
        int x = region.getMaximumPoint().getX() - (tamanho/2);
        int z = region.getMaximumPoint().getZ() - (tamanho/2);
        int y = world.getHighestBlockYAt(x, z);

        MessageUtils.messageToBroadcast("");
        MessageUtils.messageToBroadcastWithTag(
                "&eUma casa foi desprotegida pelo rei!" +
                        "\n   &6● &7X: &8" + x +
                        "\n   &6● &7Y: &8" + y +
                        "\n   &6● &7Z: &8" + z
        );
        MessageUtils.messageToBroadcast("");
        this.removeHouse(house);
         **/
    }



    // [#] Tributes
    public void alertTribute(){
        /**
        this.houses.forEach(house -> {
            if(!house.getTributes().isEmpty() && house.getTributes().size() >= 4){
                Tribute.tributeLimitMessage(house.getOwner(), house);
            }
        });
         **/
    }
    
    public void checkTributesDay(){
        /**
        this.houses.forEach(house -> {
            int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
            int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            if(house.getDayBuy() == currentDay && house.getHourBuy() == currentHour){
                if(house.getTributes().size() == OLDHouse.MAX_TAXES){
                    try {
                        this.removeAbandonedHouse(house);
                        return;
                    } catch (ProtectionNotFoundException e) {
                        e.exceptionToPlayer(house.getOwner());
                        e.printStackTrace();
                    }
                }
                house.addTribute(new Tribute(house.getSize()));
                if(house.getOwner().isOnline()){
                    Tribute.alertTribute(house);
                }

            }
        });
         **/
    }



    // Getters and Setters
    public House getHouse(String owner) throws PropertyNotFoundException {
        for(House house : this.houses){
            if(PlayerUtils.uuidToPlayer(house.getUuid()).getName().equals(owner)){
                return house;
            }
        }
        throw new PropertyNotFoundException();
    }

    public Set<House> getHouses() {
        return houses;
    }
}
