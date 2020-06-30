package br.firmacore.controllers.house.commands;

import br.firmacore.Main;
import br.firmacore.SubCommand;
import br.firmacore.controllers.house.repositories.models.House;
import br.firmacore.controllers.house.HouseManager;
import br.firmacore.services.exceptions.PropertyNotFoundException;
import br.firmacore.utils.PlayerUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class HomeSubCommand extends SubCommand {
    private Main plugin;
    private HouseManager houseManager;

    public HomeSubCommand(Main plugin){
        this.plugin = plugin;
        this.houseManager = plugin.getHouseController().getHouseManager();
    }

    @Override
    public void onCommand(Player player, String[] args) {
        try {
            House house = this.houseManager.getHouse(player.getName());
            int x = house.getX();
            int y = house.getY();
            int z = house.getZ();
            PlayerUtils.teleportPlayer(this.plugin, player, new Location(player.getWorld(), x, y, z));
        } catch (PropertyNotFoundException e) {
            e.printStackTrace();
        }


    }

    @Override
    public String name() {
        return "home";
    }

    @Override
    public String permission() {
        return null;
    }

    @Override
    public String info() {
        return "Teleporta para a casa";
    }

    @Override
    public String usage() {
        return "&7Exemplo: /casa home";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
