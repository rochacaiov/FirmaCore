package br.firmacore.services.tribute.exceptions;

import br.firmacore.utils.MessageUtils;
import org.bukkit.entity.Player;

public class TributeNotExistsException extends Exception{

    @Override
    public String getMessage() {
        return "Tributo inexistente ao jogador!";
    }

    public void exceptionToPlayer(Player player) {
        MessageUtils.messageToPlayerWithTag(
                player,
                "Você não possui tributos registrados!"
        );
    }
}
