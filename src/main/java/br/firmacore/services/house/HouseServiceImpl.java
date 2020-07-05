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
import br.firmacore.services.property.exceptions.PropertyLimitSizeException;
import br.firmacore.services.property.exceptions.PropertyWorldEnvironmentException;
import br.firmacore.utils.MessageUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class HouseServiceImpl implements HouseService {
    public static final double BLOCK_VALUE = 1000.00;
    public static final double PVP_VALUE = 10000.00;
    private final HouseRepository HOUSE_REPOSITORY;
    private PropertyService propertyService;

    public HouseServiceImpl(Main plugin){
        HOUSE_REPOSITORY = new HouseRepository(plugin);

        HOUSE_REPOSITORY.getAll().forEach(HouseCache::add);
    }

    @Override
    public void createHouse(Player owner, World world, int size) throws
            PlayerHasNoMoneyException, PropertyLimitSizeException, PropertyWorldEnvironmentException {

        if (!worldEnvironmentException(world)) throw new PropertyWorldEnvironmentException(PropertyType.CASA);
        if (!sizeLimitException(owner, size)) throw new PropertyLimitSizeException(PropertyType.CASA);
        if (!VaultHook.playerHasMoney(owner, getSizeValue(size))) throw new PlayerHasNoMoneyException(getSizeValue(size));


        House house = new House(owner, size);
        HouseCache.saveOrUpdate(house);
        HOUSE_REPOSITORY.insert(house);
    }

    @Override
    public void expandHouse(House house, Player owner, int size) throws
            PlayerHasNoMoneyException, PropertyLimitSizeException {

        if (!sizeLimitException(owner, house.getSize() + size)) throw new PropertyLimitSizeException(PropertyType.CASA);
        if (!VaultHook.playerHasMoney(owner, getSizeValue(size))) throw new PlayerHasNoMoneyException(getSizeValue(size));

        System.out.println(house.getSize());
        house.setSize(house.getSize() + size);

        System.out.println(house.getSize());
        HouseCache.saveOrUpdate(house);
        HOUSE_REPOSITORY.update(house); // REMOVER
    }

    @Override
    public void infoHouse(Player owner) {
        int size = getHouse(owner.getName()).getSize();

        MessageUtils.clearMessageToPlayer(owner, "");
        MessageUtils.informativeMessageToPlayer(owner, "&6Informações da Casa");
        MessageUtils.clearMessageToPlayer(owner, "");
        MessageUtils.clearMessageToPlayer(owner, "   &6● &7Proprietário: &3" + owner.getName().toUpperCase());
        MessageUtils.clearMessageToPlayer(owner, "   &6● &7Tamanho: &8" + size);
        MessageUtils.clearMessageToPlayer(owner, "");
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
        HOUSE_REPOSITORY.insert(house);
    }


    @Override
    public House getHouse(String owner) {
        return HouseCache.getHouse(owner);
    }

    @Override
    public double getSizeValue(int size) {
        return BLOCK_VALUE * size;
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

    private boolean sizeLimitException(Player owner, int size) {
        return size >= 10 && (size <= 50 || !owner.hasPermission(Permissions.VIP.getPermission()));
    }

}
