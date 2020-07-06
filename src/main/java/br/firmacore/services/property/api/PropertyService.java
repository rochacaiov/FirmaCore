package br.firmacore.services.property.api;

import br.firmacore.enums.PropertyType;
import br.firmacore.services.property.exceptions.*;
import br.firmacore.services.property.vo.PropertyCreateVO;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Set;

public interface PropertyService {
    void createProperty(PropertyCreateVO propertyCreateVO, PropertyType propertyType) throws PropertyLimitPerPlayerException;
    void expandProperty(PropertyCreateVO propertyCreateVO, PropertyType propertyType) throws PlayerIsntInProperty, PropertyNotExistsException, PropertyLimitPerPlayerException;
    void removeProperty(ProtectedRegion region, World world);

    void addMember(ProtectedRegion region, String target, PropertyType propertyType) throws PropertyMemberAlreadyExistsException, PlayerOfflineException;
    void removeMember(ProtectedRegion region, String target, PropertyType propertyType) throws PropertyMemberNotExistsException, PropertyMemberAlreadyExistsException;

    void saveProperty(ProtectedRegion region);



    //Getters
    ProtectedRegion getProperty(String owner, PropertyType propertyType) throws PropertyNotExistsException;
    Set<String> getMembers(ProtectedRegion region, PropertyType propertyType) throws PropertyMembersEmptyException;

    //Setters & Booleans
    void setPVP(ProtectedRegion region);

    boolean isPvP(ProtectedRegion region);
    boolean containsProperty(String owner, PropertyType propertyType);
    boolean playerIsInProperty(Player player, ProtectedRegion _region) throws PlayerIsntInProperty;

}
