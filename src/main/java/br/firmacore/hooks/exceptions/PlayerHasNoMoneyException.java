package br.firmacore.hooks.exceptions;

import br.firmacore.Exception;
import br.firmacore.hooks.VaultHook;
import br.firmacore.utils.MessageUtils;
import org.bukkit.entity.Player;

public class PlayerHasNoMoneyException extends java.lang.Exception implements Exception {
    private double value;

    public PlayerHasNoMoneyException(double value){
        super("Jogador não tem dinheiro suficiente!");
        this.value = value;
    }

    @Override
    public void exceptionToPlayer(Player player) {
        MessageUtils.errorMessageToPlayer(
                player,
                "&6Você não tem dinheiro suficiente! &c" +
                        VaultHook.getEconomy().format(this.value)
        );
    }
}
