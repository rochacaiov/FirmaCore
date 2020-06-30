package br.firmacore.services.exceptions;

import br.firmacore.utils.MessageUtils;
import org.bukkit.entity.Player;

public class PropertyAlreadyExistsException extends Exception {

    @Override
    public String getMessage() {
        return "Proteção já existente!";
    }

    public void exceptionToPlayer(Player player) {
        MessageUtils.messageToPlayerWithTag(player, "&eVocê já possui uma casa!");
    }
}
