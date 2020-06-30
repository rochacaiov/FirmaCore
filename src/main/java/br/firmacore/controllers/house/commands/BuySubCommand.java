package br.firmacore.controllers.house.commands;

import br.firmacore.Main;
import br.firmacore.SubCommand;
import br.firmacore.controllers.house.exceptions.HouseSizeLimitException;
import br.firmacore.controllers.house.exceptions.HouseWorldException;
import br.firmacore.controllers.house.repositories.models.OLDHouse;
import br.firmacore.hooks.VaultHook;
import br.firmacore.hooks.exceptions.PlayerHasNoMoneyException;
import br.firmacore.services.exceptions.PropertyAlreadyExistsException;
import br.firmacore.services.impl.HouseServiceImpl;
import br.firmacore.services.vo.PropertyCreateVO;
import br.firmacore.utils.MessageUtils;
import br.firmacore.utils.PlayerUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.entity.Player;

public class BuySubCommand extends SubCommand {
    private HouseServiceImpl houseService;

    public BuySubCommand(Main plugin){
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

            PropertyCreateVO propertyCreateVO = new PropertyCreateVO();
            propertyCreateVO.setOwner(player);
            propertyCreateVO.setSize(size);
            propertyCreateVO.setWorld(player.getWorld());
            propertyCreateVO.setX(player.getLocation().getBlockX());
            propertyCreateVO.setZ(player.getLocation().getBlockZ());
            this.houseService.createHouse(propertyCreateVO);

            PlayerUtils.spawnFirework(player);
            successMessage(player, size);

        } catch (PropertyAlreadyExistsException e) {
            e.exceptionToPlayer(player);
            e.printStackTrace();
        } catch (NumberFormatException e){
            MessageUtils.messageToPlayerWithTag(player, "&eVocê deve inserir apenas números inteiros no tamanho!");
            e.printStackTrace();
        } catch (PlayerHasNoMoneyException e) {
            e.exceptionToPlayer(player, OLDHouse.calculateHouseValue(size));
            e.printStackTrace();
        } catch (HouseSizeLimitException e) {
            e.exceptionToPlayer(player);
            e.printStackTrace();
        } catch (HouseWorldException e) {
            e.exceptionToPlayer(player);
            e.printStackTrace();
        }
    }

    private void successMessage(Player player, int size) {
        MessageUtils.messageToPlayer(player, "");
        MessageUtils.messageToPlayerWithTag(player, "&aVocê adquiriu uma casa!");
        MessageUtils.messageToPlayer(player, "");
        MessageUtils.messageToPlayer(player, "   &6● &7TAMANHO: &8" + size);
        MessageUtils.messageToPlayer(player, "   &6● &7VALOR: &c" + VaultHook.getEconomy().format(OLDHouse.calculateHouseValue(size)));
        MessageUtils.messageToPlayer(player, "");
    }

    @Override
    public String name() {
        return "comprar";
    }

    @Override
    public String permission() {
        return null;
    }

    @Override
    public String info() {
        return "Compra uma casa";
    }

    @Override
    public String usage() {
        return "&7Exemplo: /casa comprar [tamanho]";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
