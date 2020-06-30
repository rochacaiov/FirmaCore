package br.firmacore.controllers.house.commands;

import br.firmacore.Main;
import br.firmacore.SubCommand;
import br.firmacore.controllers.house.exceptions.HouseSizeLimitException;
import br.firmacore.services.api.PropertyService;
import br.firmacore.services.exceptions.PlayerIsntInProperty;
import br.firmacore.services.exceptions.PropertyNotFoundException;
import br.firmacore.services.impl.HouseServiceImpl;
import br.firmacore.services.impl.PropertyServiceImpl;
import br.firmacore.services.vo.PropertyExpandVO;
import br.firmacore.utils.MessageUtils;
import br.firmacore.utils.PlayerUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.entity.Player;

public class ExpandCommand extends SubCommand {
    private HouseServiceImpl houseService;

    public ExpandCommand(Main plugin){
        this.houseService = plugin.getHouseController().getHouseService();
    }

    @Override
    public void onCommand(Player player, String[] args) {
        String stringSize = args[1];
        int size = 0;
        try {
            if(!NumberUtils.isNumber(stringSize)){
                MessageUtils.messageToPlayerWithTag(player, "&eVocê deve inserir apenas números no tamanho!");
                return;
            }

            size = Integer.parseInt(stringSize);

            PropertyExpandVO propertyExpandVO = new PropertyExpandVO();
            propertyExpandVO.setOwner(player);
            propertyExpandVO.setSize(size);
            propertyExpandVO.setWorld(player.getWorld());
            this.houseService.expandHouse(propertyExpandVO);

            MessageUtils.messageToPlayerWithTag(
                    player,
                    "&aSua casa foi expandida em &8" + size + " &ablocos."
            );

            PlayerUtils.spawnFirework(player);
            PlayerUtils.spawnFirework(player);

        } catch (PropertyNotFoundException e) {
            e.exceptionToPlayer(player);
            e.printStackTrace();
        } catch (HouseSizeLimitException e) {
            e.exceptionToPlayer(player);
            e.printStackTrace();
        } catch (PlayerIsntInProperty e) {
            e.exceptionToPlayer(player);
            e.printStackTrace();
        }
    }

    @Override
    public String name() {
        return "expandir";
    }

    @Override
    public String permission() {
        return null;
    }

    @Override
    public String info() {
        return "Expande o tamanho da casa";
    }

    @Override
    public String usage() {
        return "&7Exemplo: /casa expandir [tamanho]";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
