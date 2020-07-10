package br.firmacore.services.house;

import br.firmacore.Main;
import br.firmacore.services.house.cache.HouseCache;
import br.firmacore.services.house.repository.HouseRepository;
import br.firmacore.services.house.repository.model.House;
import br.firmacore.enums.Permissions;
import br.firmacore.enums.PropertyType;
import br.firmacore.hooks.VaultHook;
import br.firmacore.hooks.exceptions.PlayerHasNoMoneyException;
import br.firmacore.services.house.api.HouseService;
import br.firmacore.services.property.api.PropertyService;
import br.firmacore.services.property.exceptions.*;
import br.firmacore.utils.MessageUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class HouseServiceImpl implements HouseService {
    public static final double BLOCK_VALUE = 20.00;
    public static final double PVP_VALUE = 10000.00;
    public static final int MAX_BLOCKS_MEMBER = 2500;
    public static final int MAX_BLOCKS_VIP = 10000;
    private final HouseRepository HOUSE_REPOSITORY;
    private PropertyService propertyService;

    public HouseServiceImpl(Main plugin){
        HOUSE_REPOSITORY = new HouseRepository(plugin);

        HOUSE_REPOSITORY.getAll().forEach(HouseCache::add);
    }

    @Override
    public void createHouse(Player owner, World world, int sizeX, int sizeZ) throws
            PlayerHasNoMoneyException, PropertyLimitSizeException, PropertyWorldEnvironmentException, PropertyRatioRuleException {

        if (!worldEnvironmentException(world)) throw new PropertyWorldEnvironmentException(PropertyType.CASA);
        if (!sizeLimitException(owner, sizeX, sizeZ)) throw new PropertyLimitSizeException(PropertyType.CASA);
        if (!ratioRuleException(owner, sizeX, sizeZ)) throw new PropertyRatioRuleException(PropertyType.CASA);
        if (!VaultHook.playerHasMoney(owner, getSizeValue(sizeX, sizeZ))) throw new PlayerHasNoMoneyException(getSizeValue(sizeX, sizeZ));


        House house = new House(owner, sizeX, sizeZ);
        HouseCache.saveOrUpdate(house);
        HOUSE_REPOSITORY.insert(house);
    }

    @Override
    public void resizeHouse(House house, Player owner, int sizeX, int sizeZ) throws
            PlayerHasNoMoneyException, PropertyLimitSizeException, PropertyRatioRuleException, PropertyNewSizeSmallerException {

        if (!sizeLimitException(owner, sizeX, sizeZ)) throw new PropertyLimitSizeException(PropertyType.CASA);
        if (!newSizeRuleException(owner, house, sizeX, sizeZ)) throw new PropertyNewSizeSmallerException(PropertyType.CASA);
        if (!ratioRuleException(owner, sizeX, sizeZ)) throw new PropertyRatioRuleException(PropertyType.CASA);
        if (!VaultHook.playerHasMoney(owner, getSizeValue(sizeX, sizeZ))) throw new PlayerHasNoMoneyException(getSizeValue(sizeX, sizeZ));

        //System.out.println(house.getSizeX() + ", " + house.getSizeZ());
        house.setSizeX(sizeX);
        house.setSizeZ(sizeZ);

        //System.out.println(house.getSizeX() + ", " + house.getSizeZ());
        HouseCache.saveOrUpdate(house);
        HOUSE_REPOSITORY.update(house);
    }

    @Override
    public void removeHouse(House house) {
        HouseCache.remove(house);
        HOUSE_REPOSITORY.delete(house);
    }

    @Override
    public void updateHome(House house, Location home) {
        house.setX(home.getBlockX());
        house.setY(home.getBlockY());
        house.setZ(home.getBlockZ());

        HouseCache.saveOrUpdate(house);
        HOUSE_REPOSITORY.update(house);
    }


    @Override
    public House getHouse(String owner) throws PropertyNotExistsException {
        if(!HouseCache.contains(owner)) throw new PropertyNotExistsException(PropertyType.CASA);
        return HouseCache.getHouse(owner);
    }

    @Override
    public double getSizeValue(int sizeX, int sizeZ) {
        return BLOCK_VALUE * sizeX * sizeZ;
    }


    // Cache & MySQL
    @Override
    public void updateAllHouses(){
        HouseCache.getAllHouses().forEach(HOUSE_REPOSITORY::update);
    }


    // Exceptions
    private boolean worldEnvironmentException(World world) {
        return !world.getEnvironment().equals(World.Environment.THE_END) ||
                !world.getEnvironment().equals(World.Environment.NETHER);
    }

    private boolean sizeLimitException(Player owner, int sizeX, int sizeZ) {
        int total = sizeX * sizeZ;
        int smallerSize = Math.min(sizeX, sizeZ);
        if (owner.hasPermission(Permissions.VIP.getPermission()))
            return (total <= MAX_BLOCKS_VIP && smallerSize >= 10);
        if (owner.hasPermission(Permissions.MEMBER.getPermission())) {
            return (total <= MAX_BLOCKS_MEMBER && smallerSize >= 10);
        }
        //ADD ADMINS, MODS, SUPPORT, ETC...
        return false;
    }

    private boolean ratioRuleException(Player owner, int sizeX, int sizeZ) {
        if (owner.hasPermission(Permissions.VIP.getPermission()) ||
            owner.hasPermission(Permissions.MEMBER.getPermission())){

            int greaterSize = Math.max(sizeX, sizeZ);
            int smallerSize = Math.min(sizeX, sizeZ);

            //Verifying the 3:1 Ratio Rule between the chosen sizes;
            return (greaterSize <= (3 * smallerSize));
        }
        //ADD ADMINS, MODS, SUPPORT, ETC...
        return false;
    }

    private boolean newSizeRuleException(Player owner, House house, int newSizeX, int newSizeZ){

        int sizeX = house.getSizeX();
        int sizeZ = house.getSizeZ();

        if (owner.hasPermission(Permissions.VIP.getPermission()) ||
            owner.hasPermission(Permissions.MEMBER.getPermission())){

            return newSizeX >= sizeX && newSizeZ >= sizeZ;
        }
        //ADD ADMINS, MODS, SUPPORT, ETC...
        return false;
    }
}
