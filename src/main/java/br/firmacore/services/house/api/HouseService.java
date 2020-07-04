package br.firmacore.services.house.api;

import br.firmacore.services.house.exceptions.HouseNotExistsException;
import br.firmacore.services.house.repository.model.House;
import br.firmacore.hooks.exceptions.PlayerHasNoMoneyException;
import br.firmacore.services.property.exceptions.PropertyLimitPerPlayerException;
import br.firmacore.services.property.exceptions.PropertyLimitSizeException;
import br.firmacore.services.property.exceptions.PropertyNotFoundException;
import br.firmacore.services.property.exceptions.PropertyWorldEnvironmentException;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public interface HouseService {

    void createHouse(Player owner, World world, String _size) throws PlayerHasNoMoneyException, PropertyLimitPerPlayerException, PropertyLimitSizeException, PropertyWorldEnvironmentException;
    void expandHouse(House house, Player owner, int size) throws PlayerHasNoMoneyException, PropertyLimitSizeException;
    void infoHouse(Player owner) throws HouseNotExistsException;

    void removeHouse(House house);

    void updateHome(House house, Location home);


    // Cache & MySQL
    void updateAllHouses();


    // Getters
    House getHouse(String owner) throws PropertyNotFoundException, HouseNotExistsException;

    double getSizeValue(int size);
}
