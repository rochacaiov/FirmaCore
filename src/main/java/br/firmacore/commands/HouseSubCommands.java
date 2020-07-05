package br.firmacore.commands;

import br.firmacore.Main;
import br.firmacore.services.house.repository.model.House;
import br.firmacore.enums.PropertyType;
import br.firmacore.hooks.VaultHook;
import br.firmacore.hooks.exceptions.PlayerHasNoMoneyException;
import br.firmacore.services.house.api.HouseService;
import br.firmacore.services.property.api.PropertyService;
import br.firmacore.services.property.exceptions.*;
import br.firmacore.services.property.vo.PropertyCreateVO;
import br.firmacore.utils.MessageUtils;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import io.github.mrblobman.spigotcommandlib.SubCommandHandle;
import io.github.mrblobman.spigotcommandlib.SubCommandHandler;
import io.github.mrblobman.spigotcommandlib.args.ArgDescription;
import org.bukkit.entity.Player;

public class HouseSubCommands implements SubCommandHandler {
    private HouseService houseService;
    private PropertyService propertyService;

    public HouseSubCommands(Main plugin) {
        this.houseService = plugin.getHouseService();
        this.propertyService = plugin.getPropertyService();
    }

    @SubCommandHandle(command = "comprar", description = "Compra uma casa")
    private void buy(Player player, @ArgDescription(name = "tamanho") int size) {
        double value = this.houseService.getSizeValue(size);
        try {
            PropertyCreateVO propertyCreateVO = new PropertyCreateVO();
            propertyCreateVO.setOwner(player);
            propertyCreateVO.setWorld(player.getWorld());
            propertyCreateVO.setSize(size);
            propertyCreateVO.setX(player.getLocation().getBlockX());
            propertyCreateVO.setZ(player.getLocation().getBlockZ());

            this.houseService.createHouse(player, player.getWorld(), size);
            this.propertyService.createProperty(propertyCreateVO, PropertyType.CASA);

            MessageUtils.sucessMessageToPlayer(
                    player,
                    "Você adquiriu uma casa por &c" +
                            VaultHook.getEconomy().format(value)
            );
        } catch (PropertyLimitSizeException e) {
            e.exceptionToPlayer(player);
            e.printStackTrace();
        } catch (PropertyLimitPerPlayerException e) {
            e.exceptionToPlayer(player);
            e.printStackTrace();
        } catch (PropertyWorldEnvironmentException e) {
            e.exceptionToPlayer(player);
            e.printStackTrace();
        } catch (PlayerHasNoMoneyException e) {
            e.exceptionToPlayer(player);
            e.printStackTrace();
        }
    }

    @SubCommandHandle(command = "expandir", description = "Expande a casa")
    private void expand(Player player, @ArgDescription(name = "tamanho") int size){
        try {
            House house = this.houseService.getHouse(player.getName());
            double value = this.houseService.getSizeValue(size);

            PropertyCreateVO propertyCreateVO = new PropertyCreateVO();
            propertyCreateVO.setOwner(player);
            propertyCreateVO.setWorld(player.getWorld());
            propertyCreateVO.setSize(size);

            this.houseService.expandHouse(house, player, size);
            this.propertyService.expandProperty(propertyCreateVO, PropertyType.CASA);

            MessageUtils.sucessMessageToPlayer(
                    player,
                    "Você expandiu sua casa por &c" +
                            VaultHook.getEconomy().format(value)
            );
        } catch (PlayerHasNoMoneyException e) {
            e.exceptionToPlayer(player);
            e.printStackTrace();
        } catch (PropertyLimitSizeException e) {
            e.exceptionToPlayer(player);
            e.printStackTrace();
        } catch (PropertyNotExistsException e) {
            e.exceptionToPlayer(player);
            e.printStackTrace();
        } catch (PlayerIsntInProperty e) {
            e.exceptionToPlayer(player);
            e.printStackTrace();
        } catch (PropertyLimitPerPlayerException e) {
            e.exceptionToPlayer(player);
            e.printStackTrace();
        }

    }

    @SubCommandHandle(command = "remover", description = "Remove uma casa")
    private void remove(Player player, @ArgDescription(name = "jogador") String target){
        try {
            ProtectedRegion region = this.propertyService.getProperty(target, PropertyType.CASA);
            House house = this.houseService.getHouse(target);

            this.propertyService.removeProperty(region, player.getWorld());
            this.houseService.removeHouse(house);

            MessageUtils.sucessMessageToPlayer(
                    player,
                    "Você removeu a casa do jogador &3@" +
                            target
            );
        } catch (PropertyNotExistsException e) {
            e.exceptionToPlayer(player);
            e.printStackTrace();
        }

    }
}
