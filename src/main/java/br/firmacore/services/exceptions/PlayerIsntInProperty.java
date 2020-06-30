package br.firmacore.services.exceptions;

import br.firmacore.utils.MessageUtils;
import org.bukkit.entity.Player;

public class PlayerIsntInProperty extends Exception {

    @Override
    public String getMessage() {
        return "Jogador não está no terreno";
    }

    public void exceptionToPlayer(Player player) {
        MessageUtils.messageToPlayerWithTag(player, "&eVocê não está em seu terreno!");
    }
}
