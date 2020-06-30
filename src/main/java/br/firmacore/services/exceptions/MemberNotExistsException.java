package br.firmacore.services.exceptions;

import br.firmacore.utils.MessageUtils;
import org.bukkit.entity.Player;

public class MemberNotExistsException extends Exception {

    @Override
    public String getMessage() {
        return "Jogador não encontrado!";
    }

    public void exceptionToPlayer(Player player) {
        MessageUtils.messageToPlayerWithTag(player, "&eEste jogador não está adicionado em sua casa!");
    }
}
