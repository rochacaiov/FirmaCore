package br.firmacore.services.property.exceptions;

import br.firmacore.utils.MessageUtils;
import org.bukkit.entity.Player;

public class PlayerIsntInProperty extends java.lang.Exception {

    public PlayerIsntInProperty(){
        super("Jogador não está no terreno!");
    }

    public void exceptionToPlayer(Player player) {
        MessageUtils.errorMessageToPlayer(
                player,
                "Você não está em seu terreno!"
        );
    }
}
