package br.firmacore.services.property.exceptions;

import br.firmacore.enums.PropertyType;
import br.firmacore.services.Exception;
import br.firmacore.utils.MessageUtils;
import org.bukkit.entity.Player;

public class PropertyLimitPerPlayerException extends java.lang.Exception implements Exception {
    private PropertyType propertyType;

    public PropertyLimitPerPlayerException(Player player, PropertyType propertyType){
        super("Limite de propriedade por jogador atingido!");
        this.propertyType = propertyType;

        exceptionToPlayer(player);
    }


    @Override
    public void exceptionToPlayer(Player player){
        MessageUtils.messageToPlayer(
                player,
                MessageUtils.ERROR_TAG +
                        "Você atingiu o limite máximo de propriedades do tipo " +
                        this.propertyType
        );
    }
}
