package br.firmacore.services.api;

import br.firmacore.enums.PropertyType;
import br.firmacore.services.exceptions.*;
import br.firmacore.services.vo.PropertyCreateVO;
import br.firmacore.services.vo.PropertyExpandVO;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Set;

public interface PropertyService {
    ProtectedRegion createProperty(PropertyCreateVO propertyCreateVO, PropertyType propertyType) throws PropertyAlreadyExistsException;

    ProtectedRegion expandProperty(PropertyExpandVO propertyExpandVO, PropertyType propertyType) throws PropertyNotFoundException;

    ProtectedRegion getProperty(String owner, PropertyType propertyType) throws PropertyNotFoundException;

    Set<String> getMembers(Player owner, PropertyType propertyType) throws PropertyNotFoundException, MembersEmptyException;

    void removeProperty(String owner, World world, PropertyType propertyType) throws PropertyNotFoundException;

    void addMember(Player owner, Player target, PropertyType propertyType) throws PropertyNotFoundException, MemberNotExistsException, MemberAlreadyExistsException;

    String getMember(Player owner, String target, PropertyType propertyType) throws PropertyNotFoundException, MemberNotExistsException;

    void removeMember(Player owner, String target, PropertyType propertyType) throws PropertyNotFoundException, MemberNotExistsException, MemberAlreadyExistsException;

    void saveProperty(ProtectedRegion region);

    boolean containsProperty(String owner, PropertyType propertyType);

    boolean playerIsInProperty(Player owner, PropertyType propertyType) throws PropertyNotFoundException, PlayerIsntInProperty;

    boolean isPvP(ProtectedRegion protectedRegion);
}
