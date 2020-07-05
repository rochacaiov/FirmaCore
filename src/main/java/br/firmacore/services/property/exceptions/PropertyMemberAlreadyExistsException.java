package br.firmacore.services.property.exceptions;

import br.firmacore.enums.PropertyType;
import br.firmacore.Exception;
import br.firmacore.utils.MessageUtils;
import org.bukkit.entity.Player;

public class PropertyMemberAlreadyExistsException extends java.lang.Exception implements Exception {
    private String target;
    private PropertyType propertyType;

    public PropertyMemberAlreadyExistsException(String target, PropertyType propertyType) {
        super("Jogador já existente como membro na propriedade!");
        this.target = target;
        this.propertyType = propertyType;
    }

    @Override
    public void exceptionToPlayer(Player player) {
        MessageUtils.errorMessageToPlayer(
                player,
                "&3@" +
                        this.target +
                        " &6já é membro da sua &8" +
                        propertyType
        );
    }
}
