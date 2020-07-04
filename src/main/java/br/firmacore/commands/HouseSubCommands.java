package br.firmacore.commands;

import br.firmacore.Main;
import br.firmacore.services.house.exceptions.HouseNotExistsException;
import br.firmacore.services.house.repository.model.House;
import br.firmacore.enums.PropertyType;
import br.firmacore.hooks.VaultHook;
import br.firmacore.hooks.exceptions.PlayerHasNoMoneyException;
import br.firmacore.services.house.api.HouseService;
import br.firmacore.services.property.api.PropertyService;
import br.firmacore.services.property.exceptions.PropertyLimitPerPlayerException;
import br.firmacore.services.property.exceptions.PropertyLimitSizeException;
import br.firmacore.services.property.exceptions.PropertyNotFoundException;
import br.firmacore.services.property.exceptions.PropertyWorldEnvironmentException;
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

    @SubCommandHandle(
            command = "comprar",
            description = "Compra uma casa"
    )
    private void buy(Player player, @ArgDescription(name = "tamanho") int size) {
        try {
            PropertyCreateVO propertyCreateVO = new PropertyCreateVO();
            propertyCreateVO.setOwner(player);
            propertyCreateVO.setWorld(player.getWorld());
            propertyCreateVO.setSize(size);
            propertyCreateVO.setX(player.getLocation().getBlockX());
            propertyCreateVO.setZ(player.getLocation().getBlockZ());

            this.houseService.createHouse(player, player.getWorld(), Integer.toString(size));
            this.propertyService.createProperty(propertyCreateVO, PropertyType.CASA);

            MessageUtils.sucessMessageToPlayer(
                    player,
                    "Você adquiriu uma casa por &c" +
                            VaultHook.getEconomy().format(
                                    this.houseService.getSizeValue(size)
                            )
            );
        } catch (PlayerHasNoMoneyException | PropertyLimitPerPlayerException |
                PropertyLimitSizeException | PropertyWorldEnvironmentException e) {
            e.printStackTrace();
        }
    }

    @SubCommandHandle(
            command = "remover",
            description = "Remove uma casa"
    )
    private void remove(Player player, @ArgDescription(name = "jogador") String target){
        try {
            House house = this.houseService.getHouse(target);
            ProtectedRegion region = this.propertyService.getProperty(target, PropertyType.CASA);

            this.houseService.removeHouse(house);
            this.propertyService.removeProperty(region, player.getWorld());

            MessageUtils.sucessMessageToPlayer(
                    player,
                    "Você removeu a casa do jogador &3@" +
                            target
            );
        } catch (PropertyNotFoundException | HouseNotExistsException e) {
            e.printStackTrace();
        }

    }
}
