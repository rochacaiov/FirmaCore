package br.firmacore.services.property.exceptions;

import br.firmacore.utils.MessageUtils;
import org.bukkit.entity.Player;

public class PropertyNotFoundException extends java.lang.Exception {

    @Override
    public String getMessage() {
        return "Proteção não encontrada";
    }

    public void exceptionToPlayer(Player player) {
        MessageUtils.messageToPlayerWithTag(player, "&eVocê não possui uma casa!");
    }
}
