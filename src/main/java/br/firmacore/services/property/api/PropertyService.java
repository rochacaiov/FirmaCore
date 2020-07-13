package br.firmacore.services.property.api;

import br.firmacore.enums.PropertyType;
import br.firmacore.services.property.exceptions.*;
import br.firmacore.services.property.vo.PropertyCreateVO;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Set;

public interface PropertyService {
    void createDemoProperty(PropertyCreateVO propertyCreateVO, PropertyType propertyType, Material material) throws PropertyLimitPerPlayerException;
    void setupProperty(PropertyCreateVO propertyCreateVO, PropertyType propertyType) throws PropertyLimitPerPlayerException, PropertyNotExistsException;

    void expandProperty(PropertyCreateVO propertyCreateVO, PropertyType propertyType) throws PlayerIsntInProperty, PropertyNotExistsException, PropertyLimitPerPlayerException;
    void removeProperty(ProtectedRegion region, World world, Material materialType);

    void addMember(ProtectedRegion region, String target, PropertyType propertyType) throws PropertyMemberAlreadyExistsException, PlayerOfflineException;
    void removeMember(ProtectedRegion region, String target, PropertyType propertyType) throws PropertyMemberNotExistsException, PropertyMemberAlreadyExistsException;

    void saveProperty(ProtectedRegion region);



    // Deprecated's
    @Deprecated
    void createProperty(PropertyCreateVO propertyCreateVO, PropertyType propertyType) throws PropertyLimitPerPlayerException;



    //Getters
    ProtectedRegion getProperty(String owner, PropertyType propertyType) throws PropertyNotExistsException;
    Set<String> getMembers(ProtectedRegion region, PropertyType propertyType) throws PropertyMembersEmptyException;
    int getPropertySizeX(ProtectedRegion region);
    int getPropertySizeZ(ProtectedRegion region);


    //Setters & Booleans
    void setPVP(ProtectedRegion region);

    boolean isPvP(ProtectedRegion region);
    boolean containsProperty(String owner, PropertyType propertyType);
    boolean playerIsInProperty(Player player, ProtectedRegion _region) throws PlayerIsntInProperty;

}
