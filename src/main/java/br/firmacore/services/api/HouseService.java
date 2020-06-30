package br.firmacore.services.api;

import br.firmacore.controllers.house.exceptions.HouseMembersLimitException;
import br.firmacore.controllers.house.exceptions.HouseSizeLimitException;
import br.firmacore.controllers.house.exceptions.HouseWorldException;
import br.firmacore.hooks.exceptions.PlayerHasNoMoneyException;
import br.firmacore.services.exceptions.*;
import br.firmacore.services.vo.PropertyCreateVO;
import br.firmacore.services.vo.PropertyExpandVO;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.World;
import org.bukkit.entity.Player;

public interface HouseService {
    void createHouse(PropertyCreateVO propertyCreateVO) throws PropertyAlreadyExistsException,
            HouseWorldException, HouseSizeLimitException, PlayerHasNoMoneyException, PropertyNotFoundException;

    void expandHouse(PropertyExpandVO propertyExpandVO) throws HouseSizeLimitException, PropertyNotFoundException, PlayerIsntInProperty;

    void infoHouse(Player owner) throws PropertyNotFoundException;

    void deleteHouse(String owner) throws PropertyNotFoundException;

    void addMember(Player owner, Player target) throws PropertyNotFoundException, MemberAlreadyExistsException, MembersEmptyException, HouseMembersLimitException;

    void listMembers(Player owner) throws PropertyNotFoundException, MembersEmptyException;

    void removeMember(Player owner, String target) throws PropertyNotFoundException, MemberNotExistsException, MemberAlreadyExistsException;

    void updateHome(Player owner) throws PropertyNotFoundException, PlayerIsntInProperty;

    boolean updatePVP(Player owner) throws PropertyNotFoundException;

    void setHouseFlags(ProtectedRegion region, Player owner, boolean pvp);


    // Exceptions

    boolean worldIsCorrect(World world);

    boolean sizeInLimit(Player owner, int size);

    boolean playerHasMoney(Player owner, int size);

    boolean membersInLimit(Player owner, int amountMembers);
}
