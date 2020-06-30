package br.firmacore.services.exceptions;

import br.firmacore.utils.MessageUtils;
import org.bukkit.entity.Player;

public class MembersEmptyException extends Exception {

    @Override
    public String getMessage() {
        return "Nenhum membro adicionado ao terreno";
    }

    public void exceptionToPlayer(Player player) {
        MessageUtils.messageToPlayerWithTag(player, "&eSua casa não possui nenhum jogador adicionado!");
    }
}
