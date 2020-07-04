package br.firmacore.services.property.exceptions;

import br.firmacore.enums.PropertyType;
import br.firmacore.services.Exception;
import br.firmacore.utils.MessageUtils;
import org.bukkit.entity.Player;

public class PropertyWorldEnvironmentException extends java.lang.Exception implements Exception {
    private PropertyType propertyType;

    public PropertyWorldEnvironmentException(Player player, PropertyType propertyType){
        super();
        this.propertyType = propertyType;

        exceptionToPlayer(player);
    }

    @Override
    public void exceptionToPlayer(Player player) {
        switch (propertyType){
            case CASA:
                MessageUtils.messageToPlayerWithTag(
                        player,
                        MessageUtils.ERROR_TAG +
                                "Você só pode ter uma casa no mundo normal!"
                );
            break;
        }
    }
}
