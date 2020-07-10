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
import br.firmacore.utils.PlayerUtils;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import io.github.mrblobman.spigotcommandlib.SubCommandHandle;
import io.github.mrblobman.spigotcommandlib.SubCommandHandler;
import io.github.mrblobman.spigotcommandlib.args.ArgDescription;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class HouseSubCommands implements SubCommandHandler {
    private Main plugin;
    private HouseService houseService;
    private PropertyService propertyService;

    public HouseSubCommands(Main plugin) {
        this.plugin = plugin;
        this.houseService = plugin.getHouseService();
        this.propertyService = plugin.getPropertyService();
    }

    @SubCommandHandle(command = "comprar", description = "Compra uma casa")
    private void buy(Player player, @ArgDescription(name = "Tamanho na orientação Leste-Oeste") int sizeX, @ArgDescription(name = "Tamanho na orientação Norte-Sul") int sizeZ) {
        double value = this.houseService.getSizeValue(sizeX, sizeZ);
        try {
            PropertyCreateVO propertyCreateVO = new PropertyCreateVO();
            propertyCreateVO.setOwner(player);
            propertyCreateVO.setWorld(player.getWorld());
            propertyCreateVO.setSizeX(sizeX);
            propertyCreateVO.setSizeZ(sizeZ);
            propertyCreateVO.setX(player.getLocation().getBlockX());
            propertyCreateVO.setZ(player.getLocation().getBlockZ());


            this.houseService.createHouse(player, player.getWorld(), sizeX, sizeZ);
            this.propertyService.createProperty(propertyCreateVO, PropertyType.CASA);

            MessageUtils.successMessageToPlayer(
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
        }catch (PropertyRatioRuleException e) {
            e.exceptionToPlayer(player);
            e.printStackTrace();
        }
    }

    @SubCommandHandle(command = "expandir", description = "Permite que você refaça o terreno da sua casa com novas dimensões.")
    private void expand(Player player, @ArgDescription(name = "Acréscimo na orientação Leste-Oeste") int sizeX, @ArgDescription(name = "Acréscimo na orientação Norte-Sul") int sizeZ) {
        try {
            House house = this.houseService.getHouse(player.getName());
            double value = this.houseService.getSizeValue(house.getSizeX() + sizeX, house.getSizeZ() + sizeZ) - this.houseService.getSizeValue(house.getSizeX(), house.getSizeZ());

            PropertyCreateVO propertyCreateVO = new PropertyCreateVO();
            propertyCreateVO.setOwner(player);
            propertyCreateVO.setWorld(player.getWorld());
            propertyCreateVO.setSizeX(sizeX);
            propertyCreateVO.setSizeZ(sizeZ);

            int newSizeX = house.getSizeX() + sizeX;
            int newSizeZ = house.getSizeZ() + sizeZ;

            this.houseService.resizeHouse(house, player, newSizeX, newSizeZ);
            this.propertyService.expandProperty(propertyCreateVO, PropertyType.CASA);

            MessageUtils.successMessageToPlayer(
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
        } catch (PlayerIsntInProperty e) {
            e.exceptionToPlayer(player);
            e.printStackTrace();
        } catch (PropertyLimitPerPlayerException e) {
            e.exceptionToPlayer(player);
            e.printStackTrace();
        } catch (PropertyNotExistsException e) {
            e.exceptionToPlayer(player);
            e.printStackTrace();
        } catch (PropertyRatioRuleException e) {
            e.exceptionToPlayer(player);
            e.printStackTrace();
        } catch (PropertyNewSizeSmallerException e) {
            e.exceptionToPlayer(player);
            e.printStackTrace();
        }

    }

    @SubCommandHandle(command = "info", description = "Apresenta informações da casa")
    private void info(Player player) {
        try {
            House house = this.houseService.getHouse(player.getName());
            String owner = house.getOwner();
            int sizeX = house.getSizeX();
            int sizeZ = house.getSizeZ();

            MessageUtils.clearMessageToPlayer(player, "");
            MessageUtils.informativeMessageToPlayer(player, "INFORMAÇÕES DA &8CASA &6DE " + owner);
            MessageUtils.clearMessageToPlayer(player, "");
            MessageUtils.clearMessageToPlayer(player, "   &6● &7Proprietário: &3" + owner);
            MessageUtils.clearMessageToPlayer(player, "   &6● &7Tamanho X (orientação Leste-Oeste): &8" + sizeX);
            MessageUtils.clearMessageToPlayer(player, "   &6● &7Tamanho Z (orientação Norte-Sul): &8" + sizeZ);
            MessageUtils.clearMessageToPlayer(player, "");
        } catch (PropertyNotExistsException e) {
            e.exceptionToPlayer(player);
            e.printStackTrace();
        }
    }

    @SubCommandHandle(command = "remover", description = "Remove uma casa")
    private void remove(Player player) { //IF O JOGADOR ESTIVER DENTRO DO TERRENO
        try {
            String nickname = player.getPlayerListName();
            ProtectedRegion region = this.propertyService.getProperty(nickname, PropertyType.CASA);
            House house = this.houseService.getHouse(nickname);

            this.propertyService.removeProperty(region, player.getWorld());
            this.houseService.removeHouse(house);

            MessageUtils.successMessageToPlayer(
                    player,
                    "Você removeu a casa do jogador &3@" +
                            nickname
            );
        } catch (PropertyNotExistsException e) {
            e.exceptionToPlayer(player);
            e.printStackTrace();
        }

    }

    @SubCommandHandle(command = "home", description = "Teleporta para a casa")
    private void home(Player player) {
        try {
            ProtectedRegion region = this.propertyService.getProperty(player.getName(), PropertyType.CASA);
            House house = this.houseService.getHouse(player.getName());

            PlayerUtils.teleportPlayer(
                    this.plugin,
                    player,
                    new Location(
                            player.getWorld(),
                            house.getX(),
                            house.getY(),
                            house.getZ()
                    )
            );
        } catch (PropertyNotExistsException e) {
            e.exceptionToPlayer(player);
            e.printStackTrace();
        }
    }

    @SubCommandHandle(command = "sethome", description = "Define um novo ponto de teleporte da casa")
    private void setHome(Player player) {
        try {
            ProtectedRegion region = this.propertyService.getProperty(player.getName(), PropertyType.CASA);
            House house = this.houseService.getHouse(player.getName());

            if (this.propertyService.playerIsInProperty(player, region)) {
                this.houseService.updateHome(
                        house,
                        new Location(
                                player.getWorld(),
                                player.getLocation().getX(),
                                player.getLocation().getY(),
                                player.getLocation().getZ()
                        )
                );
                MessageUtils.successMessageToPlayer(
                        player,
                        "Ponto de teleporte definido!"
                );
            }
        } catch (PropertyNotExistsException e) {
            e.exceptionToPlayer(player);
            e.printStackTrace();
        } catch (PlayerIsntInProperty e) {
            e.exceptionToPlayer(player);
            e.printStackTrace();
        }
    }

    @SubCommandHandle(command = "addamigo", description = "Adiciona um jogador na casa")
    private void addFriend(Player player, @ArgDescription(name = "jogador") String target) {
        try {
            ProtectedRegion region = this.propertyService.getProperty(player.getName(), PropertyType.CASA);
            this.propertyService.addMember(region, target, PropertyType.CASA);

            MessageUtils.successMessageToPlayer(
                    player,
                    "&3@" + target +
                            " &7foi adicionado em sua casa!"
            );
        } catch (PropertyNotExistsException e) {
            e.exceptionToPlayer(player);
            e.printStackTrace();
        } catch (PropertyMemberAlreadyExistsException e) {
            e.exceptionToPlayer(player);
            e.printStackTrace();
        } catch (PlayerOfflineException e) {
            e.exceptionToPlayer(player);
            e.printStackTrace();
        }
    }

    @SubCommandHandle(command = "amigos", description = "Lista os amigos da casa")
    private void listFriends(Player player) {
        try {
            ProtectedRegion region = this.propertyService.getProperty(player.getName(), PropertyType.CASA);
            MessageUtils.informativeMessageToPlayer(player, "LISTA DE MEMBROS");
            MessageUtils.clearMessageToPlayer(player, "");
            this.propertyService.getMembers(region, PropertyType.CASA).forEach(member -> {
                MessageUtils.clearMessageToPlayer(player, "    &6● &3" + member.toUpperCase());
            });
            MessageUtils.clearMessageToPlayer(player, "");
        } catch (PropertyNotExistsException e) {
            e.exceptionToPlayer(player);
            e.printStackTrace();
        } catch (PropertyMembersEmptyException e) {
            e.exceptionToPlayer(player);
            e.printStackTrace();
        }
    }

    @SubCommandHandle(command = "delamigo", description = "Remove um amigo da casa")
    private void removeFriend(Player player, @ArgDescription(name = "jogador") String target) {
        try {
            ProtectedRegion region = this.propertyService.getProperty(player.getName(), PropertyType.CASA);
            this.propertyService.removeMember(region, target, PropertyType.CASA);

            MessageUtils.successMessageToPlayer(
                    player,
                    "&3@" + target +
                            " &7foi removido de sua casa!"
            );
        } catch (PropertyNotExistsException e) {
            e.exceptionToPlayer(player);
            e.printStackTrace();
        } catch (PropertyMemberNotExistsException e) {
            e.exceptionToPlayer(player);
            e.printStackTrace();
        } catch (PropertyMemberAlreadyExistsException e) {
            e.exceptionToPlayer(player);
            e.printStackTrace();
        }

    }
}
