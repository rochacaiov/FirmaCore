package br.firmacore.services.property.exceptions;

import br.firmacore.enums.PropertyType;
import br.firmacore.Exception;
import br.firmacore.utils.MessageUtils;
import org.bukkit.entity.Player;

public class PropertyMemberNotExistsException extends java.lang.Exception implements Exception {
    private PropertyType propertyType;
    private String target;

    public PropertyMemberNotExistsException(String target, PropertyType propertyType) {
        super("Jogador não encontrado como membro na propriedade");
        this.target = target;
        this.propertyType = propertyType;
    }

    @Override
    public void exceptionToPlayer(Player player) {
        MessageUtils.errorMessageToPlayer(
                player,
                "&3@" +
                        this.target +
                        " &6não está adicionado em sua " +
                        this.propertyType
                );
    }
}
