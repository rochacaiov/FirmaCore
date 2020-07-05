package br.firmacore.services.property.exceptions;

import br.firmacore.enums.PropertyType;
import br.firmacore.Exception;
import br.firmacore.utils.MessageUtils;
import org.bukkit.entity.Player;

public class PropertyLimitSizeException extends java.lang.Exception implements Exception {
    private PropertyType propertyType;

    public PropertyLimitSizeException(PropertyType propertyType){
        super("Limite de tamanho excedido!");
        this.propertyType = propertyType;

    }

    @Override
    public void exceptionToPlayer(Player player) {
        switch (propertyType){
            case CASA:
                MessageUtils.clearMessageToPlayer(player, "");
                MessageUtils.errorMessageToPlayer(
                        player,
                        "Você deve inserir um tamanho permitido!"
                );
                MessageUtils.clearMessageToPlayer(player, "  &6● &7Tamanho mínimo: &810");
                MessageUtils.clearMessageToPlayer(player, "  &6● &7Tamanho máximo: &850");
                MessageUtils.clearMessageToPlayer(player, "  &6● &7Tamanho máximo: &8100 &a&o(VIP)");
                MessageUtils.clearMessageToPlayer(player, "");
            break;
        }

    }
}
