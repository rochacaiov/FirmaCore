package br.firmacore.controllers.house.commands;

import br.firmacore.Main;
import br.firmacore.SubCommand;
import br.firmacore.controllers.house.exceptions.HouseMembersLimitException;
import br.firmacore.services.exceptions.MemberAlreadyExistsException;
import br.firmacore.services.exceptions.PropertyNotFoundException;
import br.firmacore.services.impl.HouseServiceImpl;
import br.firmacore.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Objects;

public class AddSubCommand extends SubCommand {
    private HouseServiceImpl houseService;

    public AddSubCommand(Main plugin){
        this.houseService = plugin.getHouseController().getHouseService();
    }

    @Override
    public void onCommand(Player player, String[] args) {
        try {
            Player target = Bukkit.getPlayerExact(args[0]);

            if(target == null){
                MessageUtils.messageToPlayerWithTag(player, "&eEste jogador não está online!");
                return;
            }

            this.houseService.addMember(player, target);
            successMessage(player, target.getName());

        } catch (PropertyNotFoundException e) {
            e.exceptionToPlayer(player);
            e.printStackTrace();
        } catch (MemberAlreadyExistsException e) {
            e.exceptionToPlayer(player);
            e.printStackTrace();
        } catch (HouseMembersLimitException e) {
            e.exceptionToPlayer(player);
            e.printStackTrace();
        }
    }

    private void successMessage(Player player, String target) {
        MessageUtils.messageToPlayerWithTag(player, "&8@" + target + " &6foi adicionado em sua casa!");
        MessageUtils.messageToPlayerWithTag(
                Objects.requireNonNull(Bukkit.getPlayer(target)),
                "&6Você foi adicionado na casa de &8@" + player.getName() + "&6!"
        );
    }

    @Override
    public String name() {
        return "addamigo";
    }

    @Override
    public String permission() {
        return null;
    }

    @Override
    public String info() {
        return "Adiciona um jogador na casa";
    }

    @Override
    public String usage() {
        return "&7Exemplo: /casa addamigo [jogador]";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
