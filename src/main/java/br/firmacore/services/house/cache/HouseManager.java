package br.firmacore.services.house.cache;

import br.firmacore.Main;
import br.firmacore.services.house.repository.model.House;

import java.util.HashSet;
import java.util.Set;

public class HouseManager {
    private Set<House> houses = new HashSet<>();

    public HouseManager(Main plugin){
        //new TaxRunnable(this).runTaskTimer(plugin, 0, 20 * 60);
        //new AlertLimitTaxRunnable(this).runTaskTimerAsynchronously(plugin, 0, 20 * 10);
    }



    public void add(House house){
        this.houses.add(house);
    }

    public void saveOrUpdate(House house){
        remove(house);
        add(house);
    }

    public void remove(House house) {
        this.houses.remove(house);
    }

    public boolean contains(String owner){
        for(House house : this.houses){
            if(house.getOwner().equals(owner)){
                return true;
            }
        }
        return false;
    }

    // Getters and Setters
    public House getHouseByOwner(String owner) {
        for(House house : this.houses){
            if(house.getOwner().equals(owner)){
                return house;
            }
        }
        return null;
    }

    public Set<House> getHouses() {
        return houses;
    }
}
