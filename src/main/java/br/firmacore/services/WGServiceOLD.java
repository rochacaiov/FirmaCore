package br.firmacore.services;

import br.firmacore.enums.PropertyType;
import br.firmacore.services.exceptions.*;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Objects;
import java.util.Set;

public final class WGServiceOLD {

    private static final RegionManager REGION_MANAGER = WorldGuard.getInstance().getPlatform().getRegionContainer()
            .get(BukkitAdapter.adapt(Objects.requireNonNull(Bukkit.getWorld("world"))));

    public static ProtectedRegion createProtection(String owner, PropertyType propertyType, int x, int z, int size) throws PropertyAlreadyExistsException {
        if (!containsProtection(owner, propertyType)) {
            BlockVector3 min = BlockVector3.at(
                    x - (size / 2),
                    0,
                    z - (size / 2)
            );
            BlockVector3 max = BlockVector3.at(
                    x + (size / 2),
                    256,
                    z + (size / 2)
            );
            return new ProtectedCuboidRegion(owner + "_" + propertyType, min, max);
        }
        throw new PropertyAlreadyExistsException();
    }

    public static ProtectedRegion expandProtection(String owner, PropertyType propertyType, int xmax, int zmax, int xmin, int zmin, int size){
        BlockVector3 min = BlockVector3.at(
                xmin - size,
                0,
                zmin - size
        );
        BlockVector3 max = BlockVector3.at(
                xmax + size,
                256,
                zmax + size
        );
        return new ProtectedCuboidRegion(owner + "_" + propertyType, min, max);
    }

    public static void removeProtection(String owner, PropertyType propertyType) throws PropertyNotFoundException {
        assert REGION_MANAGER != null;
        ProtectedRegion region = getProtection(owner, propertyType);
        REGION_MANAGER.removeRegion(region.getId());
    }

    public static void getProtectionCenter(){

    }

    public static void addMember(String owner, String target, PropertyType propertyType) throws PropertyNotFoundException, MemberAlreadyExistsException {
        if (!containsMember(owner, target, propertyType)) {
            getProtection(owner, propertyType).getMembers().addPlayer(target);
            return;
        }
        throw new MemberAlreadyExistsException();
    }

    public static void removeMember(String owner, String target, PropertyType propertyType) throws MemberNotExistsException, PropertyNotFoundException {
        if (containsMember(owner, target, propertyType)) {
            getProtection(owner, propertyType).getMembers().removePlayer(target);
            return;
        }
        throw new MemberNotExistsException();
    }

    public static boolean playerIsInRegion(Location location, String owner, PropertyType propertyType) throws PlayerIsntInProperty, PropertyNotFoundException {
        BlockVector3 pos = BlockVector3.at(location.getX(), location.getY(), location.getZ());
        for(ProtectedRegion region : getRegionManager().getApplicableRegions(pos)){
            if(region.getId().equals(getProtection(owner, propertyType).getId())){
                return true;
            }
        }
        throw new PlayerIsntInProperty();
    }

    private static boolean containsMember(String owner, String target, PropertyType propertyType) throws PropertyNotFoundException {
        return getProtection(owner, propertyType).getMembers().getPlayers().contains(target);
    }

    private static boolean isEmptyMembers(String owner, PropertyType propertyType) throws PropertyNotFoundException {
        return getProtection(owner, propertyType).getMembers().size() == 0;
    }

    public static Set<String> getMembers(String owner, PropertyType propertyType) throws PropertyNotFoundException, MembersEmptyException {
        if (!isEmptyMembers(owner, propertyType)) {
            return getProtection(owner, propertyType).getMembers().getPlayers();
        }
        throw new MembersEmptyException();
    }

    public static int getAmountMembers(String owner, PropertyType propertyType) throws PropertyNotFoundException {
        return getProtection(owner, propertyType).getMembers().size();
    }

    public static ProtectedRegion getProtection(String owner, PropertyType propertyType) throws PropertyNotFoundException {
        if (containsProtection(owner, propertyType)) {
            assert REGION_MANAGER != null;
            return REGION_MANAGER.getRegion(owner.toLowerCase() + "_" + propertyType);
        }
        throw new PropertyNotFoundException();
    }

    public static boolean containsProtection(String owner, PropertyType propertyType) {
        assert REGION_MANAGER != null;
        ProtectedRegion region = REGION_MANAGER.getRegion(owner.toLowerCase() + "_" + propertyType);
        return region != null;
    }

    public static RegionManager getRegionManager() {
        return REGION_MANAGER;
    }
}
