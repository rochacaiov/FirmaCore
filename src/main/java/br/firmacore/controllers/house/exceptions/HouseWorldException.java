package br.firmacore.controllers.house.exceptions;

import br.firmacore.utils.MessageUtils;
import org.bukkit.entity.Player;

public class HouseWorldException extends Exception {

    @Override
    public String getMessage() {
        return "Impossível a criação de casas em submundos";
    }

    public void exceptionToPlayer(Player player) {
        MessageUtils.messageToPlayerWithTag(
                player,
                "&eVocê só pode ter uma casa no mundo normal!"
        );
    }
}
