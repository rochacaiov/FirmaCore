package br.firmacore.hooks.exceptions;

import br.firmacore.hooks.VaultHook;
import br.firmacore.utils.MessageUtils;
import org.bukkit.entity.Player;

public class PlayerHasNoMoneyException extends Exception {

    @Override
    public String getMessage() {
        return "Jogador sem dinheiro suficiente";
    }

    public void exceptionToPlayer(Player player, double value) {
        MessageUtils.messageToPlayerWithTag(
                player,
                "&eVocê não tem dinheiro suficiente! Valor: &c" + VaultHook.getEconomy().format(value)
        );
    }
}
