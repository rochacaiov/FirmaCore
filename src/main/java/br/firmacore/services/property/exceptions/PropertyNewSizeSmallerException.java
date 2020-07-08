package br.firmacore.services.property.exceptions;

import br.firmacore.Exception;
import br.firmacore.enums.PropertyType;
import br.firmacore.utils.MessageUtils;
import org.bukkit.entity.Player;

public class PropertyNewSizeSmallerException extends java.lang.Exception implements Exception {

    private PropertyType propertyType;

    public PropertyNewSizeSmallerException(PropertyType propertyType){
        super("Regra de Novo Tamanho violada!");
        this.propertyType = propertyType;

    }

    @Override
    public void exceptionToPlayer(Player player) {
        switch (propertyType){
            case CASA:
                MessageUtils.clearMessageToPlayer(player, "");
                MessageUtils.errorMessageToPlayer(
                        player,
                        "Opa! O novo tamanho do terreno deve ser maior que o anterior!"
                );
                MessageUtils.clearMessageToPlayer(player, "  &6● &7A proporção máxima é de &83:1 &7para criar os terrenos.");
                MessageUtils.clearMessageToPlayer(player, "  &6● &7E você não pode informar dimensões &6(X ou Z)&7 menores que as anteriores."); //CHECAR COR
                MessageUtils.clearMessageToPlayer(player, "  &6● &7Exemplo [1]: &8/comprar casa 30 10");
                MessageUtils.clearMessageToPlayer(player, "  &6● &7Exemplo [2]: &8/comprar casa 27 81");
                MessageUtils.clearMessageToPlayer(player, "");
                break;
        }
    }
}
