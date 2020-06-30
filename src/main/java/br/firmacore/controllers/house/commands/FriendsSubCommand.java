package br.firmacore.controllers.house.commands;

import br.firmacore.Main;
import br.firmacore.SubCommand;
import br.firmacore.services.exceptions.MembersEmptyException;
import br.firmacore.services.exceptions.PropertyNotFoundException;
import br.firmacore.services.impl.HouseServiceImpl;
import br.firmacore.utils.PlayerUtils;
import org.bukkit.entity.Player;

public class FriendsSubCommand extends SubCommand {
    private HouseServiceImpl houseService;

    public FriendsSubCommand(Main plugin) {
        this.houseService = plugin.getHouseController().getHouseService();
    }

    @Override
    public void onCommand(Player player, String[] args) {
        try {
            this.houseService.listMembers(player);
            PlayerUtils.spawnFirework(player);
        } catch (PropertyNotFoundException e) {
            e.exceptionToPlayer(player);
            e.printStackTrace();
        } catch (MembersEmptyException e) {
            e.exceptionToPlayer(player);
            e.printStackTrace();
        }
    }

    @Override
    public String name() {
        return "amigos";
    }

    @Override
    public String permission() {
        return null;
    }

    @Override
    public String info() {
        return "Lista os membros da casa";
    }

    @Override
    public String usage() {
        return "&7Exemplo: /casa amigos";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
