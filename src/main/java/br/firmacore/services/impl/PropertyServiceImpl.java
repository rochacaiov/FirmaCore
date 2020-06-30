package br.firmacore.services.impl;

import br.firmacore.enums.PropertyType;
import br.firmacore.services.api.PropertyService;
import br.firmacore.services.exceptions.*;
import br.firmacore.services.vo.PropertyCreateVO;
import br.firmacore.services.vo.PropertyExpandVO;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Set;

public class PropertyServiceImpl implements PropertyService {
    private RegionManager regionManager;

    public PropertyServiceImpl(World world) {
        regionManager = WorldGuard
                .getInstance()
                .getPlatform()
                .getRegionContainer()
                .get(BukkitAdapter.adapt(world));
    }


    @Override
    public ProtectedRegion createProperty(PropertyCreateVO propertyCreateVO, PropertyType propertyType)
            throws PropertyAlreadyExistsException {

        if (!containsProperty(propertyCreateVO.getOwner().getName(), propertyType)) {
            BlockVector3 min = BlockVector3.at(
                    propertyCreateVO.getX() - (propertyCreateVO.getSize() / 2),
                    0,
                    propertyCreateVO.getZ() - (propertyCreateVO.getSize() / 2)
            );
            BlockVector3 max = BlockVector3.at(
                    propertyCreateVO.getX() + (propertyCreateVO.getSize() / 2),
                    256,
                    propertyCreateVO.getZ() + (propertyCreateVO.getSize() / 2)
            );
            return new ProtectedCuboidRegion(
                    propertyCreateVO.getOwner().getName() +
                            "_" +
                            propertyType,
                    min,
                    max
            );
        }
        throw new PropertyAlreadyExistsException();
    }

    @Override
    public ProtectedRegion expandProperty(PropertyExpandVO propertyExpandVO, PropertyType propertyType) throws PropertyNotFoundException {
        String owner = propertyExpandVO.getOwner().getName();
        ProtectedRegion region = getProperty(owner, propertyType);
        int size = propertyExpandVO.getSize();

        BlockVector3 min = BlockVector3.at(
                region.getMinimumPoint().getBlockX() - size,
                0,
                region.getMinimumPoint().getBlockZ() - size
        );
        BlockVector3 max = BlockVector3.at(
                region.getMaximumPoint().getBlockX() + size,
                256,
                region.getMaximumPoint().getBlockZ() + size
        );
        return new ProtectedCuboidRegion(
                owner + "_" + propertyType,
                min,
                max
        );
    }

    @Override
    public ProtectedRegion getProperty(String owner, PropertyType propertyType) throws PropertyNotFoundException {
        if (containsProperty(owner, propertyType)) {
            return this.regionManager.getRegion(owner.toLowerCase() + "_" + propertyType);
        }
        throw new PropertyNotFoundException();
    }

    @Override
    public Set<String> getMembers(Player owner, PropertyType propertyType)
            throws PropertyNotFoundException {

        return getProperty(owner.getName(), propertyType).getMembers().getPlayers();
    }

    @Override
    public void removeProperty(String owner, World world, PropertyType propertyType) throws PropertyNotFoundException {
        ProtectedRegion region = getProperty(owner, propertyType);
        this.regionManager.removeRegion(region.getId());
    }

    @Override
    public void addMember(Player owner, Player target, PropertyType propertyType)
            throws PropertyNotFoundException, MemberAlreadyExistsException {
        String member = getMember(owner, target.getName(), propertyType);
        if (member == null) {
            getProperty(owner.getName(), propertyType).getMembers().addPlayer(target.getName());
            return;
        }
        throw new MemberAlreadyExistsException();
    }

    @Override
    public String getMember(Player owner, String target, PropertyType propertyType)
            throws PropertyNotFoundException {

        for(String member : getProperty(owner.getName(), propertyType).getMembers().getPlayers()){
            if(target.equalsIgnoreCase(member)){
                return member;
            }
        }
        return null;
    }

    @Override
    public void removeMember(Player owner, String target, PropertyType propertyType)
            throws PropertyNotFoundException, MemberAlreadyExistsException {

        String member = getMember(owner, target, propertyType);
        if(member != null){
            getProperty(owner.getName(), propertyType).getMembers().removePlayer(member);
        }
        throw new MemberAlreadyExistsException();
    }

    @Override
    public void saveProperty(ProtectedRegion region) {
        this.regionManager.addRegion(region);
    }

    @Override
    public boolean containsProperty(String owner, PropertyType propertyType) {
        return this.regionManager.getRegion(
                owner.toLowerCase()
                        + "_"
                        + propertyType)
                != null;
    }

    @Override
    public boolean playerIsInProperty(Player owner, PropertyType propertyType) throws PropertyNotFoundException {
        BlockVector3 pos = BlockVector3.at(
                owner.getLocation().getBlockX(),
                owner.getLocation().getBlockY(),
                owner.getLocation().getBlockZ()
        );
        for(ProtectedRegion region : this.regionManager.getApplicableRegions(pos)){
            if(region.getId().equals(getProperty(owner.getName(), propertyType).getId())){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isPvP(ProtectedRegion region) {
        return region.getFlag(Flags.PVP) == StateFlag.State.ALLOW;
    }
}
