package br.firmacore.controllers.house.exceptions;

import br.firmacore.utils.MessageUtils;
import org.bukkit.entity.Player;

public class HouseMembersLimitException extends Exception {

    @Override
    public String getMessage() {
        return "Esta casa atingiu o limite máximo de membros";
    }

    public void exceptionToPlayer(Player player) {
        MessageUtils.messageToPlayerWithTag(
                player,
                "&eSua casa atingiu o limite máximo de amigos!"
        );
    }
}
