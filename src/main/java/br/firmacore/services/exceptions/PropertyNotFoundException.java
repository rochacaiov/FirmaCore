package br.firmacore.services.exceptions;

import br.firmacore.utils.MessageUtils;
import org.bukkit.entity.Player;

public class PropertyNotFoundException extends Exception  {

    @Override
    public String getMessage() {
        return "Proteção não encontrada";
    }

    public void exceptionToPlayer(Player player) {
        MessageUtils.messageToPlayerWithTag(player, "&eVocê não possui uma casa!");
    }
}
