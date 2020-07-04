package br.firmacore.services.property.exceptions;

import br.firmacore.enums.PropertyType;
import br.firmacore.services.Exception;
import br.firmacore.utils.MessageUtils;
import org.bukkit.entity.Player;

public class PropertyLimitSizeException extends java.lang.Exception implements Exception {
    private PropertyType propertyType;

    public PropertyLimitSizeException(Player player, PropertyType propertyType){
        super("Limite de tamanho excedido!");
        this.propertyType = propertyType;

        exceptionToPlayer(player);
    }

    @Override
    public void exceptionToPlayer(Player player) {
        switch (propertyType){
            case CASA:
                MessageUtils.messageToPlayer(player, "");
                MessageUtils.messageToPlayer(
                        player,
                        MessageUtils.ERROR_TAG + "Você deve inserir um tamanho permitido!"
                );
                MessageUtils.messageToPlayer(player, "  &6● &7Tamanho mínimo: &810");
                MessageUtils.messageToPlayer(player, "  &6● &7Tamanho máximo: &850");
                MessageUtils.messageToPlayer(player, "  &6● &7Tamanho máximo: &8100 &a&o(VIP)");
                MessageUtils.messageToPlayer(player, "");
            break;
        }

    }
}
