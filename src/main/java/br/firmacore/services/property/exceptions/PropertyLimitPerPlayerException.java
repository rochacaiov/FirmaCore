package br.firmacore.services.property.exceptions;

import br.firmacore.enums.PropertyType;
import br.firmacore.Exception;
import br.firmacore.utils.MessageUtils;
import org.bukkit.entity.Player;

public class PropertyLimitPerPlayerException extends java.lang.Exception implements Exception {
    private PropertyType propertyType;

    public PropertyLimitPerPlayerException(PropertyType propertyType) {
        super("Limite de propriedade por jogador atingido!");
        this.propertyType = propertyType;
    }


    @Override
    public void exceptionToPlayer(Player player) {
        MessageUtils.errorMessageToPlayer(
                player,
                "Você atingiu o limite máximo de propriedades do tipo &8" +
                        this.propertyType
        );
    }
}
