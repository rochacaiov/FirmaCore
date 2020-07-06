package br.firmacore.services.property.exceptions;

import br.firmacore.Exception;
import br.firmacore.utils.MessageUtils;
import org.bukkit.entity.Player;

public class PlayerOfflineException extends java.lang.Exception implements Exception {
    private String target;

    public PlayerOfflineException(String target){
        super("Jogador offline!");
        this.target = target;
    }

    @Override
    public void exceptionToPlayer(Player player) {
        MessageUtils.errorMessageToPlayer(
                player,
                "&3@" + target +
                        " &cnão está online!"
        );
    }
}
