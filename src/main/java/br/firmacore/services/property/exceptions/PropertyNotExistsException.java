package br.firmacore.services.property.exceptions;

import br.firmacore.enums.PropertyType;
import br.firmacore.Exception;
import br.firmacore.utils.MessageUtils;
import org.bukkit.entity.Player;

public class PropertyNotExistsException extends java.lang.Exception implements Exception {
    private PropertyType propertyType;

    public PropertyNotExistsException(PropertyType propertyType){
        super(propertyType + " inexistente!");
        this.propertyType = propertyType;

    }

    @Override
    public void exceptionToPlayer(Player player) {
        MessageUtils.errorMessageToPlayer(
                player,
                "A &8" +
                        propertyType +
                        " &cnão existe!"
        );
    }
}
