package br.firmacore.services.property.api;

import br.firmacore.enums.PropertyType;
import br.firmacore.services.property.exceptions.PlayerIsntInProperty;
import br.firmacore.services.property.exceptions.PropertyMemberAlreadyExistsException;
import br.firmacore.services.property.exceptions.PropertyMemberNotExistsException;
import br.firmacore.services.property.exceptions.PropertyMembersEmptyException;
import br.firmacore.services.property.vo.PropertyCreateVO;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.World;
import org.bukkit.entity.Player;

public interface PropertyService {
    void createProperty(PropertyCreateVO propertyCreateVO, PropertyType propertyType);
    void expandProperty(PropertyCreateVO propertyCreateVO, PropertyType propertyType) throws PlayerIsntInProperty;
    void removeProperty(ProtectedRegion region, World world);

    void addMember(ProtectedRegion region, String target) throws PropertyMemberAlreadyExistsException;
    void listMembers(Player owner, ProtectedRegion region) throws PropertyMembersEmptyException;
    void removeMember(ProtectedRegion region, String target) throws PropertyMemberNotExistsException;

    void saveProperty(ProtectedRegion region);



    //Getters
    ProtectedRegion getProperty(String owner, PropertyType propertyType);

    //Setters & Booleans
    void setPVP(ProtectedRegion region);
    boolean isPvP(ProtectedRegion region);

    boolean playerIsInProperty(Player player, ProtectedRegion _region) throws PlayerIsntInProperty;

}
