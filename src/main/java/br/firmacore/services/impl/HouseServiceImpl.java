package br.firmacore.services.impl;

import br.firmacore.controllers.house.repositories.HouseRepository;
import br.firmacore.controllers.house.repositories.models.House;
import br.firmacore.controllers.house.HouseController;
import br.firmacore.controllers.house.HouseManager;
import br.firmacore.controllers.house.exceptions.HouseMembersLimitException;
import br.firmacore.controllers.house.exceptions.HouseSizeLimitException;
import br.firmacore.controllers.house.exceptions.HouseWorldException;
import br.firmacore.enums.Permissions;
import br.firmacore.enums.PropertyType;
import br.firmacore.hooks.VaultHook;
import br.firmacore.hooks.exceptions.PlayerHasNoMoneyException;
import br.firmacore.services.api.HouseService;
import br.firmacore.services.exceptions.*;
import br.firmacore.services.vo.PropertyCreateVO;
import br.firmacore.services.vo.PropertyExpandVO;
import br.firmacore.services.vo.WEBorderVO;
import br.firmacore.utils.MessageUtils;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Set;

public class HouseServiceImpl implements HouseService {
    private final HouseRepository houseRepository;
    private final HouseManager houseManager;
    private final PropertyServiceImpl propertyService;

    public HouseServiceImpl(HouseController houseController) {
        this.houseRepository = houseController.getHouseRepository();
        this.houseManager = houseController.getHouseManager();
        this.propertyService = new PropertyServiceImpl(Bukkit.getWorlds().get(0));
    }

    @Override
    public void createHouse(PropertyCreateVO propertyCreateVO) throws HouseWorldException, HouseSizeLimitException, PropertyAlreadyExistsException, PlayerHasNoMoneyException {
        Player owner = propertyCreateVO.getOwner();
        World world = propertyCreateVO.getWorld();
        int size = propertyCreateVO.getSize();

        if (!worldIsCorrect(world)) throw new HouseWorldException();
        if (!sizeInLimit(owner, size)) throw new HouseSizeLimitException();
        if (!playerHasMoney(owner, size))
            throw new PlayerHasNoMoneyException();


        ProtectedRegion region = propertyService.createProperty(propertyCreateVO, HouseController.AREA_TYPE);

        setHouseFlags(region, owner, propertyService.isPvP(region));
        region.getOwners().addPlayer(owner.getName());
        region.setPriority(100);
        propertyService.saveProperty(region);

        WEBorderVO weBorderVO = new WEBorderVO();
        weBorderVO.setWorld(world);
        weBorderVO.setWallXMax(region.getMaximumPoint().getX());
        weBorderVO.setWallZMax(region.getMaximumPoint().getZ());
        weBorderVO.setWallXMin(region.getMinimumPoint().getX());
        weBorderVO.setWallZMin(region.getMinimumPoint().getZ());
        WEServiceImpl.createBorder(weBorderVO);

        House house = new House(owner, size);
        this.houseManager.saveOrUpdate(house);
        this.houseRepository.saveOrUpdate(house);
    }

    @Override
    public void expandHouse(PropertyExpandVO propertyExpandVO) throws HouseSizeLimitException, PropertyNotFoundException, PlayerIsntInProperty {
        Player owner = propertyExpandVO.getOwner();
        World world = propertyExpandVO.getWorld();
        House house = this.houseManager.getHouse(propertyExpandVO.getOwner().getName());
        ProtectedRegion oldRegion = this.propertyService.getProperty(owner.getName(), HouseController.AREA_TYPE);
        int size = propertyExpandVO.getSize();

        if (!sizeInLimit(propertyExpandVO.getOwner(), size + size)) throw new HouseSizeLimitException();
        if (!propertyService.playerIsInProperty(owner, HouseController.AREA_TYPE)) throw new PlayerIsntInProperty();

        WEBorderVO weBorderVO = new WEBorderVO();
        weBorderVO.setWorld(world);
        weBorderVO.setWallZMax(oldRegion.getMaximumPoint().getZ());
        weBorderVO.setWallXMax(oldRegion.getMaximumPoint().getX());
        weBorderVO.setWallZMin(oldRegion.getMinimumPoint().getZ());
        weBorderVO.setWallXMin(oldRegion.getMinimumPoint().getX());
        WEServiceImpl.removeBorder(weBorderVO);

        ProtectedRegion newRegion = propertyService.expandProperty(propertyExpandVO, HouseController.AREA_TYPE);
        setHouseFlags(newRegion, owner, propertyService.isPvP(newRegion));
        newRegion.getOwners().addPlayer(owner.getName());
        newRegion.setPriority(100);
        house.setSize(house.getSize() + size);

        propertyService.removeProperty(owner.getName(), owner.getWorld(), HouseController.AREA_TYPE);
        propertyService.saveProperty(newRegion);

        weBorderVO.setWorld(world);
        weBorderVO.setWallXMax(newRegion.getMaximumPoint().getX());
        weBorderVO.setWallZMax(newRegion.getMaximumPoint().getZ());
        weBorderVO.setWallXMin(newRegion.getMinimumPoint().getX());
        weBorderVO.setWallZMin(newRegion.getMinimumPoint().getZ());
        WEServiceImpl.createBorder(weBorderVO);

        this.houseManager.saveOrUpdate(house);
        this.houseRepository.saveOrUpdate(house);
    }

    @Override
    public void infoHouse(Player owner) throws PropertyNotFoundException {
        int size = this.houseManager.getHouse(owner.getName()).getSize();
        MessageUtils.messageToPlayer(owner, "");
        MessageUtils.messageToPlayerWithTag(owner, "&6Informações da Casa");
        MessageUtils.messageToPlayer(owner, "");
        MessageUtils.messageToPlayer(owner, "   &6● &7Proprietário: &3" + owner.getName().toUpperCase());
        MessageUtils.messageToPlayer(owner, "   &6● &7Tamanho: &8" + size);
        MessageUtils.messageToPlayer(owner, "");

    }

    @Override
    public void deleteHouse(String owner) throws PropertyNotFoundException {
        ProtectedRegion region = propertyService.getProperty(owner, HouseController.AREA_TYPE);
        House house = this.houseManager.getHouse(owner);
        World world = Bukkit.getWorlds().get(0);

        WEBorderVO weBorderVO = new WEBorderVO();
        weBorderVO.setWorld(world);
        weBorderVO.setWallXMax(region.getMaximumPoint().getX());
        weBorderVO.setWallZMax(region.getMaximumPoint().getZ());
        weBorderVO.setWallXMin(region.getMinimumPoint().getX());
        weBorderVO.setWallZMin(region.getMinimumPoint().getZ());
        WEServiceImpl.removeBorder(weBorderVO);

        propertyService.removeProperty(owner, world, HouseController.AREA_TYPE);
        this.houseManager.removeHouse(house);
        this.houseRepository.delete(house);
    }

    @Override
    public void addMember(Player owner, Player target)
            throws PropertyNotFoundException, MemberAlreadyExistsException, HouseMembersLimitException {

        if(!membersInLimit(owner, propertyService.getMembers(owner, HouseController.AREA_TYPE).size())){
            propertyService.addMember(owner, target, HouseController.AREA_TYPE);
            return;
        }
        throw new HouseMembersLimitException();
    }

    @Override
    public void listMembers(Player owner) throws PropertyNotFoundException, MembersEmptyException {
        Set<String> members = propertyService.getMembers(owner, HouseController.AREA_TYPE);
        if(members.size() != 0){
            MessageUtils.messageToPlayerWithTag(owner, "&6Lista de Amigos");
            MessageUtils.messageToPlayer(owner, "");
            members.forEach(member -> {
                MessageUtils.messageToPlayer(owner, "    &6● &3" + member.toUpperCase());
            });
            MessageUtils.messageToPlayer(owner, "");
            return;
        }
        throw new MembersEmptyException();
    }

    @Override
    public void removeMember(Player owner, String target)
            throws PropertyNotFoundException, MemberAlreadyExistsException {

        propertyService.removeMember(owner, target, HouseController.AREA_TYPE);
    }

    @Override
    public void updateHome(Player owner) throws PropertyNotFoundException, PlayerIsntInProperty {
        House house = this.houseManager.getHouse(owner.getName());
        if(propertyService.playerIsInProperty(owner, HouseController.AREA_TYPE)){
            house.setX(owner.getLocation().getBlockX());
            house.setY(owner.getLocation().getBlockY());
            house.setZ(owner.getLocation().getBlockZ());
            return;
        }
        throw new PlayerIsntInProperty();
    }

    @Override
    public boolean updatePVP(Player owner) throws PropertyNotFoundException {
        ProtectedRegion region = propertyService.getProperty(owner.getName(), HouseController.AREA_TYPE);
        boolean isPVP = propertyService.isPvP(region);
        region.setFlag(
                Flags.GREET_MESSAGE,
                getGreetMessage(owner, isPVP)
        );
        region.setFlag(Flags.PVP, isPVP ? StateFlag.State.DENY : StateFlag.State.ALLOW);
        return isPVP;
    }

    @Override
    public void setHouseFlags(ProtectedRegion region, Player owner, boolean pvp) {
        region.setFlag(
                Flags.GREET_MESSAGE,
                getGreetMessage(
                        owner,
                        pvp
                )
        );
        region.setFlag(Flags.PVP, pvp ? StateFlag.State.DENY : StateFlag.State.ALLOW);
        region.setFlag(Flags.OTHER_EXPLOSION, StateFlag.State.DENY);
        region.setFlag(Flags.ENDER_BUILD, StateFlag.State.DENY);
        region.setFlag(Flags.BLOCK_BREAK.getRegionGroupFlag(), RegionGroup.MEMBERS);
        region.setFlag(Flags.BLOCK_PLACE.getRegionGroupFlag(), RegionGroup.MEMBERS);
    }

    @Override
    public boolean worldIsCorrect(World world) {
        return !world.getEnvironment().equals(World.Environment.NETHER) ||
                world.getEnvironment().equals(World.Environment.THE_END);
    }

    @Override
    public boolean sizeInLimit(Player owner, int size) {
        return size >= 10 && (size <= 50 || !owner.hasPermission(Permissions.VIP.getPermission()));
    }

    @Override
    public boolean playerHasMoney(Player owner, int size) {
        return (VaultHook.getEconomy().getBalance(owner)) >= HouseController.BLOCK_VALUE * size;
    }

    @Override
    public boolean membersInLimit(Player owner, int amountMembers) {
        return amountMembers == 3 || owner.hasPermission(Permissions.VIP.getPermission()) && amountMembers == 6;
    }

    private String getGreetMessage(Player owner, boolean pvp) {
        String pvpTAG = pvp ? " &7[&a&lPVP OFF&7] " : " &7[&c&lPVP ON&7] ";
        return pvpTAG + "&8&l>> &6Olá, bem-vindo(a) à &8" + HouseController.AREA_TYPE + " &6de &8" +
                owner.getName() + "&6!";
    }
}
