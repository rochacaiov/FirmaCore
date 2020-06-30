package br.firmacore.controllers.house.commands;

import br.firmacore.Main;
import br.firmacore.SubCommand;
import br.firmacore.controllers.house.HouseController;
import br.firmacore.hooks.VaultHook;
import br.firmacore.services.exceptions.PropertyNotFoundException;
import br.firmacore.services.impl.HouseServiceImpl;
import br.firmacore.utils.MessageUtils;
import org.bukkit.entity.Player;

public class PVPCommand extends SubCommand {
    private HouseServiceImpl houseService;

    public PVPCommand(Main plugin){
        this.houseService = plugin.getHouseController().getHouseService();
    }

    @Override
    public void onCommand(Player player, String[] args) {
        try {
            boolean pvpUpdate = this.houseService.updatePVP(player);
            VaultHook.getEconomy().withdrawPlayer(player, HouseController.PVP_VALUE);
            String message = pvpUpdate ? "&cdesativou" : "&aativou";
            MessageUtils.messageToPlayerWithTag(
                    player,
                    "&6Você " + message + "&6 o PVP da sua casa por &c" +
                            VaultHook.getEconomy().format(HouseController.PVP_VALUE)
            );
        } catch (PropertyNotFoundException e) {
            e.exceptionToPlayer(player);
            e.printStackTrace();
        }
    }

    @Override
    public String name() {
        return "pvp";
    }

    @Override
    public String permission() {
        return null;
    }

    @Override
    public String info() {
        return "Altera o PVP da casa";
    }

    @Override
    public String usage() {
        return "&7Exemplo: /casa pvp";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
