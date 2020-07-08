package br.firmacore.services.property;

import br.firmacore.enums.PropertyType;
import br.firmacore.services.property.api.PropertyService;
import br.firmacore.services.property.exceptions.*;
import br.firmacore.services.property.vo.PropertyCreateVO;
import br.firmacore.services.property.vo.WEBorderVO;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Set;

public class PropertyServiceImpl implements PropertyService {
    private final RegionManager regionManager;

    public PropertyServiceImpl(World world) {
        regionManager = WorldGuard
                .getInstance()
                .getPlatform()
                .getRegionContainer()
                .get(BukkitAdapter.adapt(world));
    }

    @Override
    public void createProperty(PropertyCreateVO propertyCreateVO, PropertyType propertyType) throws PropertyLimitPerPlayerException {
        String owner = propertyCreateVO.getOwner().getName();
        World world = propertyCreateVO.getWorld();
        int sizeX = propertyCreateVO.getSizeX();
        int sizeZ = propertyCreateVO.getSizeZ();
        int x = propertyCreateVO.getX(); //Player X position
        int z = propertyCreateVO.getZ(); //Player Z position


        if (containsProperty(owner, propertyType)) throw new PropertyLimitPerPlayerException(propertyType);

        if (propertyCreateVO.getMax() == null || propertyCreateVO.getMin() == null) {
            BlockVector3 max = BlockVector3.at(
                    sizeX % 2 == 0? x + (sizeX / 2) - 1 : x + (sizeX / 2),
                    256,
                    sizeZ % 2 == 0? z + (sizeZ / 2) - 1 : z + (sizeZ / 2)
            );
            BlockVector3 min = BlockVector3.at(
                    x - (sizeX / 2),
                    0,
                    z - (sizeZ / 2)
            );
            propertyCreateVO.setMax(max);
            propertyCreateVO.setMin(min);
        }


        ProtectedRegion region = new ProtectedCuboidRegion(
                owner.toLowerCase() + "_" + propertyType,
                propertyCreateVO.getMin(),
                propertyCreateVO.getMax()
        );

        setFlagsByPropertyType(owner, region, propertyType);
        region.getOwners().addPlayer(owner);
        region.setPriority(100);
        saveProperty(region);

        WEBorderVO weBorderVO = new WEBorderVO();
        weBorderVO.setWorld(world);
        weBorderVO.setWallXMax(region.getMaximumPoint().getX());
        weBorderVO.setWallZMax(region.getMaximumPoint().getZ());
        weBorderVO.setWallXMin(region.getMinimumPoint().getX());
        weBorderVO.setWallZMin(region.getMinimumPoint().getZ());
        WEServiceImpl.createBorder(weBorderVO);
    }

    @Override
    public void expandProperty(PropertyCreateVO propertyCreateVO, PropertyType propertyType) throws PlayerIsntInProperty, PropertyNotExistsException, PropertyLimitPerPlayerException {
        Player owner = propertyCreateVO.getOwner();
        ProtectedRegion region = getProperty(owner.getName(), propertyType); //Pega proteção atual
        int sizeX = propertyCreateVO.getSizeX();
        int sizeZ = propertyCreateVO.getSizeZ();
        BlockVector3 max = BlockVector3.at(
                region.getMaximumPoint().getBlockX() + sizeX/2,
                256,
                region.getMaximumPoint().getBlockZ() + sizeZ/2
        );
        BlockVector3 min = BlockVector3.at(
                region.getMinimumPoint().getBlockX() - sizeX/2,
                0,
                region.getMinimumPoint().getBlockZ() - sizeZ/2
        );

        if (!playerIsInProperty(owner, region)) throw new PlayerIsntInProperty();

        propertyCreateVO.setMax(max);
        propertyCreateVO.setMin(min);

        removeProperty(region, propertyCreateVO.getWorld()); // Remove a antiga proteção junto das barreiras
        createProperty(propertyCreateVO, propertyType); // Cria uma nova proteção com blockVector max/min
    }

    @Override
    public void removeProperty(ProtectedRegion region, World world) {
        WEBorderVO weBorderVO = new WEBorderVO();
        weBorderVO.setWorld(world);
        weBorderVO.setWallZMax(region.getMaximumPoint().getZ());
        weBorderVO.setWallXMax(region.getMaximumPoint().getX());
        weBorderVO.setWallZMin(region.getMinimumPoint().getZ());
        weBorderVO.setWallXMin(region.getMinimumPoint().getX());
        WEServiceImpl.removeBorder(weBorderVO);

        this.regionManager.removeRegion(region.getId());
    }

    @Override
    public void addMember(ProtectedRegion region, String target, PropertyType propertyType) throws PropertyMemberAlreadyExistsException, PlayerOfflineException {
        if (Bukkit.getPlayer(target) == null) throw new PlayerOfflineException(target);
        if (region.getMembers().contains(target)) throw new PropertyMemberAlreadyExistsException(target, propertyType);
        if (region.getOwners().contains(target)) throw new PropertyMemberAlreadyExistsException(target, propertyType);

        region.getMembers().addPlayer(target);
    }

    @Override
    public void removeMember(ProtectedRegion region, String target, PropertyType propertyType) throws PropertyMemberNotExistsException, PropertyMemberAlreadyExistsException {
        if (!region.getMembers().contains(target)) throw new PropertyMemberNotExistsException(target, propertyType);
        if (region.getOwners().contains(target)) throw new PropertyMemberAlreadyExistsException(target, propertyType);

        region.getMembers().removePlayer(target);
    }

    @Override
    public void saveProperty(ProtectedRegion region) {
        regionManager.addRegion(region);
    }


    // Getters
    @Override
    public ProtectedRegion getProperty(String owner, PropertyType propertyType) throws PropertyNotExistsException {
        ProtectedRegion region = regionManager.getRegion(owner.toLowerCase() + "_" + propertyType);
        if (region == null) throw new PropertyNotExistsException(propertyType);
        return region;
    }

    @Override
    public Set<String> getMembers(ProtectedRegion region, PropertyType propertyType) throws PropertyMembersEmptyException {
        if(region.getMembers().getPlayers().isEmpty()) throw new PropertyMembersEmptyException(propertyType);
        return region.getMembers().getPlayers();
    }


    private String getGreetMessage(String owner, boolean pvp, PropertyType propertyType) {
        String pvpTAG = pvp ? " &7[&a&lPVP OFF&7] " : " &7[&c&lPVP ON&7] ";
        return pvpTAG + "&8&l>> &6Olá, bem-vindo(a) à &8" + propertyType + " &6de &8" +
                owner + "&6!";
    }

    // Setters & Booleans
    private void setFlagsByPropertyType(String owner, ProtectedRegion region, PropertyType propertyType) {
        switch (propertyType) {
            case CASA:
                region.setFlag(
                        Flags.GREET_MESSAGE,
                        getGreetMessage(
                                owner,
                                isPvP(region),
                                propertyType
                        )
                );
                setPVP(region);
                region.setFlag(Flags.OTHER_EXPLOSION, StateFlag.State.DENY);
                region.setFlag(Flags.ENDER_BUILD, StateFlag.State.DENY);
                region.setFlag(Flags.BLOCK_BREAK.getRegionGroupFlag(), RegionGroup.MEMBERS);
                region.setFlag(Flags.BLOCK_PLACE.getRegionGroupFlag(), RegionGroup.MEMBERS);
                break;

            case LOJA:
                break;
        }
    }

    @Override
    public void setPVP(ProtectedRegion region) {
        boolean pvp = region.getFlag(Flags.PVP) == StateFlag.State.ALLOW;
        region.setFlag(Flags.PVP, pvp ? StateFlag.State.DENY : StateFlag.State.ALLOW);
    }

    @Override
    public boolean isPvP(ProtectedRegion region) {
        return region.getFlag(Flags.PVP) == StateFlag.State.ALLOW;
    }

    @Override
    public boolean containsProperty(String owner, PropertyType propertyType) {
        ProtectedRegion region = this.regionManager.getRegion(owner + "_" + propertyType);
        return region != null;
    }

    @Override
    public boolean playerIsInProperty(Player player, ProtectedRegion _region) throws PlayerIsntInProperty {
        BlockVector3 pos = BlockVector3.at(
                player.getLocation().getBlockX(),
                player.getLocation().getBlockY(),
                player.getLocation().getBlockZ()
        );
        for (ProtectedRegion region : regionManager.getApplicableRegions(pos)) {
            if (region.getId().equals(_region.getId())) {
                return true;
            }
        }
        throw new PlayerIsntInProperty();
    }

}
