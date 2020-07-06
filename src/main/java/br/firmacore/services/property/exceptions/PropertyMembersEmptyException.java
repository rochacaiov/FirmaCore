package br.firmacore.services.property.exceptions;

import br.firmacore.enums.PropertyType;
import br.firmacore.Exception;
import br.firmacore.utils.MessageUtils;
import org.bukkit.entity.Player;

public class PropertyMembersEmptyException extends java.lang.Exception implements Exception {
    private PropertyType propertyType;

    public PropertyMembersEmptyException(PropertyType propertyType){
        super("Nenhum jogador foi encontrado como membro na propriedade");
        this.propertyType = propertyType;
    }

    @Override
    public void exceptionToPlayer(Player player) {
        MessageUtils.errorMessageToPlayer(
                player,
                "Sua &8" +
                        this.propertyType +
                        " &cnão possui nenhum membro adicionado"
        );
    }
}
