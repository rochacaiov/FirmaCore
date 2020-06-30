package br.firmacore.services.exceptions;

import br.firmacore.utils.MessageUtils;
import org.bukkit.entity.Player;

public class MemberAlreadyExistsException extends Exception {

    @Override
    public String getMessage() {
        return "Membro já existente na área";
    }

    public void exceptionToPlayer(Player player) {
        MessageUtils.messageToPlayerWithTag(player, "&eEste jogador já está adicionado em sua casa!");
    }
}
