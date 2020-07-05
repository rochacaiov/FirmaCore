package br.firmacore.services.property.exceptions;

import br.firmacore.enums.PropertyType;
import br.firmacore.Exception;
import br.firmacore.utils.MessageUtils;
import org.bukkit.entity.Player;

public class PropertyMembersEmptyException extends java.lang.Exception implements Exception {
    private PropertyType propertyType;

    public PropertyMembersEmptyException(){
        super("Nenhum jogador foi encontrado como membro na propriedade");
    }

    @Override
    public void exceptionToPlayer(Player player) {
        MessageUtils.errorMessageToPlayer(
                player,
                "&6Sua &8" +
                        this.propertyType +
                        " &6não possui nenhum membro adicionado"
        );
    }
}
