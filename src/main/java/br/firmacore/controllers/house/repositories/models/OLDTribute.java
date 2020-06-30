package br.firmacore.controllers.house.repositories.models;

import br.firmacore.hooks.VaultHook;
import br.firmacore.utils.MessageUtils;
import org.bukkit.entity.Player;

public class OLDTribute {
    private static final String TRIBUTE_TAG = " &c*&c&lTRIBUTO&c* &8>> ";
    public static final double PERCENTAGE_TRIBUTE = 0.3;   // Porcentagem do TRIBUTO
    public static final int TRIBUTES_LIMIT = 6;                 // 6 Impostos == 6 Semanas

    private double value;

    // Apenas um tributo por casa que irá acumulando o valor de acordo com o tamanho da casa
    public OLDTribute(int size){
        this.value = calculateTributeValue(size);
    }

    private double calculateTributeValue(int size){
        return OLDHouse.calculateHouseValue(size) * PERCENTAGE_TRIBUTE;
    }

    public static void tributeLimitMessage(Player owner, OLDHouse house) {
        MessageUtils.messageToPlayer(owner, "");
        MessageUtils.messageToPlayer(owner,
                TRIBUTE_TAG +
                        "&eVocê será despejado ao acumular 6 tributos!"
        );
        MessageUtils.messageToPlayer(house.getOwner(), "");
        MessageUtils.messageToPlayer(house.getOwner(),
                "   &6● &7QUANTIDADE: &8" + house.getOLDTributes().size() +
                        "\n   &6● &7VALOR: &c" + VaultHook.getEconomy().format(house.calculateTotalTributesValue())
        );
        MessageUtils.messageToPlayer(house.getOwner(), "");
    }

    public static void alertTribute(OLDHouse house){
        MessageUtils.messageToPlayer(house.getOwner(), "");
        MessageUtils.messageToPlayer(
                house.getOwner(),
                TRIBUTE_TAG + "&eVocê recebeu um tributo que deve ser pago!" +
                        "\n " +
                        "\n    &6● &7VALOR: &c" + VaultHook.getEconomy().format(house.calculateTotalTributesValue())
        );
        MessageUtils.messageToPlayer(house.getOwner(), "");
    }


    // Getters and Setters

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
