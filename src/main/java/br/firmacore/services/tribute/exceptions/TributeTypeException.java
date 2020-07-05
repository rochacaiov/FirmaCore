package br.firmacore.services.tribute.exceptions;

import br.firmacore.utils.MessageUtils;
import org.bukkit.entity.Player;

public class TributeTypeException extends Exception{

    @Override
    public String getMessage() {
        return "Tipo de tributo inexistente!";
    }

    public void exceptionToPlayer(Player player) {
        MessageUtils.informativeMessageToPlayer(
                player,
                "Categoria de tributos inexistente!"
        );
    }
}
