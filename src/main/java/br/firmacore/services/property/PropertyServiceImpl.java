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
import org.bukkit.Material;
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


    private BlockVector3 getMaximumBlockVector(int playerX, int playerZ, int sizeX, int sizeZ) {
        return BlockVector3.at(
                sizeX % 2 == 0 ? playerX + (sizeX / 2) - 1 : playerX + (sizeX / 2),
                256,
                sizeZ % 2 == 0 ? playerZ + (sizeZ / 2) - 1 : playerZ + (sizeZ / 2)
        );
    }

    private BlockVector3 getMinimumBlockVector(int playerX, int playerZ, int sizeX, int sizeZ) {
        return BlockVector3.at(
                playerX - (sizeX / 2),
                0,
                playerZ - (sizeZ / 2)
        );
    }

    private WEBorderVO getWeBorderVO(ProtectedRegion region, World world) {
        WEBorderVO weBorderVO = new WEBorderVO();
        weBorderVO.setWorld(world);
        weBorderVO.setWallXMax(region.getMaximumPoint().getX());
        weBorderVO.setWallZMax(region.getMaximumPoint().getZ());
        weBorderVO.setWallXMin(region.getMinimumPoint().getX());
        weBorderVO.setWallZMin(region.getMinimumPoint().getZ());
        return weBorderVO;
    }

    private ProtectedRegion createProtectedRegion(PropertyCreateVO propertyCreateVO, PropertyType propertyType) {
        String owner = propertyCreateVO.getOwner().getName();
        return new ProtectedCuboidRegion(
                owner.toLowerCase() + "_" + propertyType,
                propertyCreateVO.getMin(),
                propertyCreateVO.getMax()
        );
    }

    private String getGreetMessage(String owner, boolean pvp, PropertyType propertyType) {
        String pvpTAG = pvp ? " &7[&a&lPVP OFF&7] " : " &7[&c&lPVP ON&7] ";
        return pvpTAG + "&8&l>> &6Olá, bem-vindo(a) à &8" + propertyType + " &6de &8" +
                owner + "&6!";
    }

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
    public void createDemoProperty(PropertyCreateVO propertyCreateVO, PropertyType _propertyType, Material material) throws PropertyLimitPerPlayerException {
        PropertyType propertyType = PropertyType.DEMO;
        String owner = propertyCreateVO.getOwner().getName();
        World world = propertyCreateVO.getWorld();
        int sizeX = propertyCreateVO.getSizeX();
        int sizeZ = propertyCreateVO.getSizeZ();
        int x = propertyCreateVO.getX();
        int z = propertyCreateVO.getZ();

        if (containsProperty(owner, propertyType)) throw new PropertyLimitPerPlayerException(propertyType);
        if (containsProperty(owner, _propertyType)) throw new PropertyLimitPerPlayerException(_propertyType);

        if (propertyCreateVO.getMax() == null || propertyCreateVO.getMin() == null) {
            propertyCreateVO.setMax(getMaximumBlockVector(x, z, sizeX, sizeZ));
            propertyCreateVO.setMin(getMinimumBlockVector(x, z, sizeX, sizeZ));
        }

        ProtectedRegion region = new ProtectedCuboidRegion(
                owner.toLowerCase() + "_" + propertyType,
                propertyCreateVO.getMin(),
                propertyCreateVO.getMax()
        );

        saveProperty(region);

        WEServiceImpl.createBorder(getWeBorderVO(region, world), material);
    }

    @Override
    public void setupProperty(PropertyCreateVO propertyCreateVO, PropertyType propertyType) throws PropertyLimitPerPlayerException, PropertyNotExistsException {
        String owner = propertyCreateVO.getOwner().getName();
        World world = propertyCreateVO.getWorld();

        ProtectedRegion demoRegion = getProperty(owner, PropertyType.DEMO);

        BlockVector3 max = BlockVector3.at(
                demoRegion.getMaximumPoint().getBlockX(),
                256,
                demoRegion.getMaximumPoint().getBlockZ()
        );
        BlockVector3 min = BlockVector3.at(
                demoRegion.getMinimumPoint().getBlockX(),
                0,
                demoRegion.getMinimumPoint().getBlockZ()
        );

        removeProperty(demoRegion, world, Material.DARK_OAK_FENCE);

        propertyCreateVO.setMax(max);
        propertyCreateVO.setMin(min);

        ProtectedRegion newRegion = createProtectedRegion(propertyCreateVO, propertyType);

        setFlagsByPropertyType(owner, newRegion, propertyType);
        newRegion.getOwners().addPlayer(owner);
        newRegion.setPriority(100);
        saveProperty(newRegion);

        WEServiceImpl.createBorder(getWeBorderVO(newRegion, world), Material.OAK_FENCE);
    }

    @Override
    public void expandProperty(PropertyCreateVO propertyCreateVO, PropertyType propertyType) throws PlayerIsntInProperty, PropertyNotExistsException, PropertyLimitPerPlayerException {
        Player owner = propertyCreateVO.getOwner();
        ProtectedRegion region = getProperty(owner.getName(), propertyType);

        if (!playerIsInProperty(owner, region)) throw new PlayerIsntInProperty();

        int sizeX = propertyCreateVO.getSizeX();
        int sizeZ = propertyCreateVO.getSizeZ();
        int maxX = region.getMaximumPoint().getBlockX();
        int maxZ = region.getMaximumPoint().getBlockZ();
        int minX = region.getMinimumPoint().getBlockX();
        int minZ = region.getMinimumPoint().getBlockZ();

        propertyCreateVO.setMax(getMaximumBlockVector(maxX, maxZ, sizeX, sizeZ));
        propertyCreateVO.setMin(getMinimumBlockVector(minX, minZ, sizeX, sizeZ));

        removeProperty(region, propertyCreateVO.getWorld(), Material.OAK_FENCE);
        createProperty(propertyCreateVO, propertyType);
    }

    @Override
    public void removeProperty(ProtectedRegion region, World world, Material materialType) {
        WEServiceImpl.removeBorder(getWeBorderVO(region, world), materialType);

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


    // Getters, Setters and Booleans
    @Override
    public ProtectedRegion getProperty(String owner, PropertyType propertyType) throws PropertyNotExistsException {
        ProtectedRegion region = regionManager.getRegion(owner.toLowerCase() + "_" + propertyType);
        if (region == null) throw new PropertyNotExistsException(propertyType);
        return region;
    }

    @Override
    public Set<String> getMembers(ProtectedRegion region, PropertyType propertyType) throws PropertyMembersEmptyException {
        if (region.getMembers().getPlayers().isEmpty()) throw new PropertyMembersEmptyException(propertyType);
        return region.getMembers().getPlayers();
    }

    @Override
    public int getPropertySizeX(ProtectedRegion region){
        return region.getMaximumPoint().getBlockX() - region.getMinimumPoint().getBlockX();
    }

    @Override
    public int getPropertySizeZ(ProtectedRegion region){
        return region.getMaximumPoint().getBlockZ() - region.getMinimumPoint().getBlockZ();
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


    // Deprecated's
    @Deprecated
    @Override
    public void createProperty(PropertyCreateVO propertyCreateVO, PropertyType propertyType) throws PropertyLimitPerPlayerException {
        String owner = propertyCreateVO.getOwner().getName();
        World world = propertyCreateVO.getWorld();
        int sizeX = propertyCreateVO.getSizeX();
        int sizeZ = propertyCreateVO.getSizeZ();
        int x = propertyCreateVO.getX();
        int z = propertyCreateVO.getZ();

        if (propertyCreateVO.getMax() == null || propertyCreateVO.getMin() == null) {
            propertyCreateVO.setMax(getMaximumBlockVector(x, z, sizeX, sizeZ));
            propertyCreateVO.setMin(getMinimumBlockVector(x, z, sizeX, sizeZ));
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

        WEServiceImpl.createBorder(getWeBorderVO(region, world), Material.OAK_FENCE);
    }
}
