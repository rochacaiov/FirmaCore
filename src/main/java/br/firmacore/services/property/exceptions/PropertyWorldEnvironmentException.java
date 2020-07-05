package br.firmacore.services.property.exceptions;

import br.firmacore.enums.PropertyType;
import br.firmacore.Exception;
import br.firmacore.utils.MessageUtils;
import org.bukkit.entity.Player;

public class PropertyWorldEnvironmentException extends java.lang.Exception implements Exception {
    private PropertyType propertyType;

    public PropertyWorldEnvironmentException(PropertyType propertyType){
        super(propertyType + " não pode ser comprada neste mundo");
        this.propertyType = propertyType;
    }

    @Override
    public void exceptionToPlayer(Player player) {
        MessageUtils.errorMessageToPlayer(
                player,
                "&6Você não pode comprar uma &8" +
                        this.propertyType +
                        " &6neste mundo!"
        );
    }
}
