package br.firmacore.services.property.exceptions;

import br.firmacore.Exception;
import br.firmacore.enums.PropertyType;
import br.firmacore.utils.MessageUtils;
import org.bukkit.entity.Player;

public class PropertyRatioRuleException extends java.lang.Exception implements Exception {

    private PropertyType propertyType;

    public PropertyRatioRuleException(PropertyType propertyType){
        super("Regra de Proporção Violada!");
        this.propertyType = propertyType;

    }

    @Override
    public void exceptionToPlayer(Player player) {
        switch (propertyType){
            case CASA:
                MessageUtils.clearMessageToPlayer(player, "");
                MessageUtils.errorMessageToPlayer(
                        player,
                        "Opa! Um dos tamanhos escolhidos para o terreno é muito grande em relação ao outro!"
                );
                MessageUtils.clearMessageToPlayer(player, "  &6● &7A proporção máxima é de &83:1 &7para criar ou expandir os terrenos.");
                MessageUtils.clearMessageToPlayer(player, "  &6● &7Exemplo [1]: &8/comprar casa 30 10");
                MessageUtils.clearMessageToPlayer(player, "  &6● &7Exemplo [2]: &8/comprar casa 27 81");
                MessageUtils.clearMessageToPlayer(player, "");
            break;
        }
    }
}
