package br.firmacore.controllers.house.commands;

import br.firmacore.Main;
import br.firmacore.SubCommand;
import br.firmacore.services.exceptions.MemberAlreadyExistsException;
import br.firmacore.services.exceptions.PropertyNotFoundException;
import br.firmacore.services.impl.HouseServiceImpl;
import br.firmacore.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Objects;

public class DelSubCommand extends SubCommand {
    private HouseServiceImpl houseService;

    public DelSubCommand(Main plugin){
        this.houseService = plugin.getHouseController().getHouseService();
    }

    @Override
    public void onCommand(Player player, String[] args) {
        try {
            String target = args[1];

            this.houseService.removeMember(player, target);
            successMessage(player, target);

        } catch (PropertyNotFoundException e) {
            e.exceptionToPlayer(player);
            e.printStackTrace();
        } catch (MemberAlreadyExistsException e) {
            e.exceptionToPlayer(player);
            e.printStackTrace();
        }
    }

    private void successMessage(Player player, String target) {
        MessageUtils.messageToPlayerWithTag(player, "&8@" + target + " &6foi removido de sua casa!");
        MessageUtils.messageToPlayerWithTag(
                Objects.requireNonNull(Bukkit.getPlayer(target)),
                "&6Você foi removido da casa de &8@" + player.getName() + "&6!"
        );
    }

    @Override
    public String name() {
        return "delamigo";
    }

    @Override
    public String permission() {
        return null;
    }

    @Override
    public String info() {
        return "Remove um jogador da casa";
    }

    @Override
    public String usage() {
        return "&7Exemplo: /casa delamigo [jogador]";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
