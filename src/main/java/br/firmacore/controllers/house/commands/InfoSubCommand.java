package br.firmacore.controllers.house.commands;

import br.firmacore.Main;
import br.firmacore.SubCommand;
import br.firmacore.services.exceptions.PropertyNotFoundException;
import br.firmacore.services.impl.HouseServiceImpl;
import org.bukkit.entity.Player;

public class InfoSubCommand extends SubCommand {
    private HouseServiceImpl houseService;

    public InfoSubCommand(Main plugin){
        this.houseService = plugin.getHouseController().getHouseService();
    }

    @Override
    public void onCommand(Player player, String[] args) {
        try {
            this.houseService.infoHouse(player);
        } catch (PropertyNotFoundException e) {
            e.exceptionToPlayer(player);
            e.printStackTrace();
        }

    }

    @Override
    public String name() {
        return "info";
    }

    @Override
    public String permission() {
        return null;
    }

    @Override
    public String info() {
        return "Mostra as informações da casa";
    }

    @Override
    public String usage() {
        return "&7Exemplo: /casa info";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
