package br.firmacore.controllers.house.exceptions;

import br.firmacore.utils.MessageUtils;
import org.bukkit.entity.Player;

public class HouseSizeLimitException extends Exception {

    @Override
    public String getMessage() {
        return "Tamanho limite atingido";
    }

    public void exceptionToPlayer(Player player) {
        MessageUtils.messageToPlayer(player, "");
        MessageUtils.messageToPlayerWithTag(player, "&eVocê deve inserir um tamanho permitido!");
        MessageUtils.messageToPlayer(player, "");
        MessageUtils.messageToPlayer(player, "  &6● &7Tamanho mínimo: &810");
        MessageUtils.messageToPlayer(player, "  &6● &7Tamanho máximo: &850");
        MessageUtils.messageToPlayer(player, "  &6● &7Tamanho máximo: &8100 &a&o(VIP)");
        MessageUtils.messageToPlayer(player, "");
    }
}
