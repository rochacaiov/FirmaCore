package br.firmacore.services.property;

import br.firmacore.enums.PropertyType;
import br.firmacore.services.property.api.PropertyService;
import br.firmacore.services.property.exceptions.PlayerIsntInProperty;
import br.firmacore.services.property.exceptions.PropertyMemberAlreadyExistsException;
import br.firmacore.services.property.exceptions.PropertyMemberNotExistsException;
import br.firmacore.services.property.exceptions.PropertyMembersEmptyException;
import br.firmacore.services.property.vo.PropertyCreateVO;
import br.firmacore.services.property.vo.WEBorderVO;
import br.firmacore.utils.MessageUtils;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
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
    public void createProperty(PropertyCreateVO propertyCreateVO, PropertyType propertyType) {
        String owner = propertyCreateVO.getOwner().getName();
        World world = propertyCreateVO.getWorld();
        int size = propertyCreateVO.getSize();
        int x = propertyCreateVO.getX();
        int z = propertyCreateVO.getZ();
        System.out.println(size);
        System.out.println(x);
        System.out.println(z);

        BlockVector3 max = BlockVector3.at(
                x + (size/2),
                256,
                z + (size/2)
        );
        BlockVector3 min = BlockVector3.at(
                x - (size/2),
                0,
                z - (size/2)
        );

        ProtectedRegion region = new ProtectedCuboidRegion(
                owner.toLowerCase() + "_" + propertyType,
                min,
                max
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
    public void expandProperty(PropertyCreateVO propertyCreateVO, PropertyType propertyType) throws PlayerIsntInProperty {
        Player owner = propertyCreateVO.getOwner();
        ProtectedRegion region = getProperty(owner.getName(), propertyType); //Pega proteção atual
        int size = propertyCreateVO.getSize();
        BlockVector3 max = BlockVector3.at(
                region.getMaximumPoint().getBlockX() + size,
                256,
                region.getMaximumPoint().getBlockZ() + size
        );
        BlockVector3 min = BlockVector3.at(
                region.getMinimumPoint().getBlockX() - size,
                0,
                region.getMinimumPoint().getBlockZ() - size
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
    public void addMember(ProtectedRegion region, String target) throws PropertyMemberAlreadyExistsException {
        if(!region.getMembers().contains(target)){
            region.getMembers().addPlayer(target);
            return;
        }
        throw new PropertyMemberAlreadyExistsException();
    }

    @Override
    public void listMembers(Player owner, ProtectedRegion region) throws PropertyMembersEmptyException {
        Set<String> members = region.getMembers().getPlayers();
        if(!members.isEmpty()){
            MessageUtils.messageToPlayerWithTag(owner, "&6Lista de Membros no Terreno");
            MessageUtils.messageToPlayer(owner, "");
            members.forEach(member -> {
                MessageUtils.messageToPlayer(owner, "    &6● &3" + member.toUpperCase());
            });
            MessageUtils.messageToPlayer(owner, "");
        }
        throw new PropertyMembersEmptyException();
    }

    @Override
    public void removeMember(ProtectedRegion region, String target) throws PropertyMemberNotExistsException {
        if(!region.getMembers().contains(target)){
            region.getMembers().removePlayer(target);
            return;
        }
        throw new PropertyMemberNotExistsException();
    }

    @Override
    public void saveProperty(ProtectedRegion region){
        regionManager.addRegion(region);
    }


    // Getters
    @Override
    public ProtectedRegion getProperty(String owner, PropertyType propertyType) {
        return regionManager.getRegion(owner.toLowerCase() + "_" + propertyType);
    }

    private String getGreetMessage(String owner, boolean pvp, PropertyType propertyType) {
        String pvpTAG = pvp ? " &7[&a&lPVP OFF&7] " : " &7[&c&lPVP ON&7] ";
        return pvpTAG + "&8&l>> &6Olá, bem-vindo(a) à &8" + propertyType + " &6de &8" +
                owner + "&6!";
    }

    // Setters & Booleans
    private void setFlagsByPropertyType(String owner, ProtectedRegion region, PropertyType propertyType){
        switch (propertyType){
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
    public void setPVP(ProtectedRegion region){
        boolean pvp = region.getFlag(Flags.PVP) == StateFlag.State.ALLOW;
        region.setFlag(Flags.PVP, pvp ? StateFlag.State.DENY : StateFlag.State.ALLOW);
    }

    @Override
    public boolean isPvP(ProtectedRegion region) {
        return region.getFlag(Flags.PVP) == StateFlag.State.ALLOW;
    }

    @Override
    public boolean playerIsInProperty(Player player, ProtectedRegion _region) throws PlayerIsntInProperty {
        BlockVector3 pos = BlockVector3.at(
                player.getLocation().getBlockX(),
                player.getLocation().getBlockY(),
                player.getLocation().getBlockZ()
        );
        for(ProtectedRegion region : regionManager.getApplicableRegions(pos)){
            if(region.getId().equals(_region.getId())){
                return true;
            }
        }
        throw new PlayerIsntInProperty();
    }

}
