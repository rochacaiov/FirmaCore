package br.firmacore.services.house.api;

import br.firmacore.services.house.repository.model.House;
import br.firmacore.hooks.exceptions.PlayerHasNoMoneyException;
import br.firmacore.services.property.exceptions.*;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public interface HouseService {

    void createHouse(Player owner, World world, int sizeX, int sizeZ)
            throws PlayerHasNoMoneyException, PropertyLimitPerPlayerException,
            PropertyLimitSizeException, PropertyWorldEnvironmentException, PropertyRatioRuleException;

    void resizeHouse(House house, Player owner, int sizeX, int sizeZ)
            throws PlayerHasNoMoneyException, PropertyLimitSizeException, PropertyRatioRuleException, PropertyNewSizeSmallerException;

    void removeHouse(House house);

    void updateHome(House house, Location home);


    // Cache & MySQL
    void updateAllHouses();


    // Getters & Booleans
    House getHouse(String owner) throws PropertyNotExistsException;

    double getSizeValue(int sizeX, int sizeZ);
}
