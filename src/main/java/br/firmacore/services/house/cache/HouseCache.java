package br.firmacore.services.house.cache;

import br.firmacore.services.house.repository.model.House;

import java.util.HashSet;
import java.util.Set;

public final class HouseCache {
    private static Set<House> houses = new HashSet<>();

    public static void saveOrUpdate(House house){
        remove(house);
        add(house);
    }

    public static void add(House house){
        houses.add(house);
    }

    public static void remove(House house){
        houses.remove(house);
    }

    public static void format(){
        houses.clear();
    }

    public static House getHouse(String owner) {
        for(House house : houses){
            if(house.getOwner().equals(owner)){
                return house;
            }
        }
        return null;
    }

    public static Set<House> getAllHouses(){
        return houses;
    }

    public static boolean contains(String owner){
        return getHouse(owner) != null;
    }
}
