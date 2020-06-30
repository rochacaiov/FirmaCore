package br.firmacore.controllers.house.commands;

import br.firmacore.Main;
import br.firmacore.SubCommand;
import br.firmacore.enums.Permissions;
import br.firmacore.services.exceptions.PropertyNotFoundException;
import br.firmacore.services.impl.HouseServiceImpl;
import br.firmacore.utils.MessageUtils;
import org.bukkit.entity.Player;

public class RemoveSubCommand extends SubCommand {
    private HouseServiceImpl houseService;

    public RemoveSubCommand(Main plugin){
        this.houseService = plugin.getHouseController().getHouseService();
    }

    @Override
    public void onCommand(Player player, String[] args) {
        try {
            this.houseService.deleteHouse(args[1]);
            successMessage(player);
        } catch (PropertyNotFoundException e) {
            e.exceptionToPlayer(player);
            e.printStackTrace();
        }
    }

    private void successMessage(Player player){
        MessageUtils.messageToPlayerWithTag(player, "&aCasa removida com sucessso!");
    }

    @Override
    public String name() {
        return "remover";
    }

    @Override
    public String permission() {
        return Permissions.ADMIN.getPermission();
    }

    @Override
    public String info() {
        return "Deleta a casa de um jogador";
    }

    @Override
    public String usage() {
        return "&7Exemplo: /casa remover [jogador]";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
