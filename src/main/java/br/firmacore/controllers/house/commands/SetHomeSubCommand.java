package br.firmacore.controllers.house.commands;

import br.firmacore.Main;
import br.firmacore.SubCommand;
import br.firmacore.services.exceptions.PlayerIsntInProperty;
import br.firmacore.services.exceptions.PropertyNotFoundException;
import br.firmacore.services.impl.HouseServiceImpl;
import br.firmacore.utils.MessageUtils;
import org.bukkit.entity.Player;

public class SetHomeSubCommand extends SubCommand {
    private HouseServiceImpl houseService;

    public SetHomeSubCommand(Main plugin){
        this.houseService = plugin.getHouseController().getHouseService();
    }

    @Override
    public void onCommand(Player player, String[] args) {
        try {
            this.houseService.updateHome(player);
            MessageUtils.messageToPlayerWithTag(
                    player,
                    "&aVocê definiu um novo ponto de teleporte da casa."
            );
        } catch (PlayerIsntInProperty e) {
            e.exceptionToPlayer(player);
            e.printStackTrace();
        } catch (PropertyNotFoundException e) {
            e.exceptionToPlayer(player);
            e.printStackTrace();
        }
    }

    @Override
    public String name() {
        return "sethome";
    }

    @Override
    public String permission() {
        return null;
    }

    @Override
    public String info() {
        return "Altera o ponto de teleporte da casa";
    }

    @Override
    public String usage() {
        return "&7Exemplo: /casa sethome";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
