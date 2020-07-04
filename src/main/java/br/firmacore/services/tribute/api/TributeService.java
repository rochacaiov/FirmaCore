package br.firmacore.services.tribute.api;


import br.firmacore.services.tribute.exceptions.TributeNotExistsException;
import br.firmacore.enums.PropertyType;
import br.firmacore.hooks.exceptions.PlayerHasNoMoneyException;
import br.firmacore.services.tribute.exceptions.TributeTypeException;
import org.bukkit.entity.Player;

public interface TributeService {

    void createTribute(String uuid, int size, PropertyType propertyType);
    double payTribute(Player owner, String propertyType) throws TributeTypeException, TributeNotExistsException, PlayerHasNoMoneyException;
    void removeTribute(String owner, PropertyType propertyType) throws TributeNotExistsException;
}
