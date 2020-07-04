package br.firmacore.services.property.exceptions;

import br.firmacore.utils.MessageUtils;
import org.bukkit.entity.Player;

public class PropertyMembersEmptyException extends java.lang.Exception {

    @Override
    public String getMessage() {
        return "Nenhum membro adicionado ao terreno";
    }

    public void exceptionToPlayer(Player player) {
        MessageUtils.messageToPlayerWithTag(player, "&eSua casa não possui nenhum jogador adicionado!");
    }
}
