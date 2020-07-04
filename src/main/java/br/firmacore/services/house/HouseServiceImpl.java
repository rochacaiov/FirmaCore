package br.firmacore.services.house;

import br.firmacore.Main;
import br.firmacore.services.house.cache.HouseManager;
import br.firmacore.services.house.exceptions.HouseNotExistsException;
import br.firmacore.services.house.repository.HouseRepository;
import br.firmacore.services.house.repository.model.House;
import br.firmacore.enums.Permissions;
import br.firmacore.enums.PropertyType;
import br.firmacore.hooks.VaultHook;
import br.firmacore.hooks.exceptions.PlayerHasNoMoneyException;
import br.firmacore.services.house.api.HouseService;
import br.firmacore.services.property.exceptions.PropertyLimitPerPlayerException;
import br.firmacore.services.property.exceptions.PropertyLimitSizeException;
import br.firmacore.services.property.exceptions.PropertyWorldEnvironmentException;
import br.firmacore.utils.MessageUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class HouseServiceImpl implements HouseService {
    public static final double BLOCK_VALUE = 1000.00;
    public static final double PVP_VALUE = 10000.00;
    private HouseManager houseManager;
    private HouseRepository houseRepository;

    public HouseServiceImpl(Main plugin){
        this.houseManager = new HouseManager(plugin);
        this.houseRepository = new HouseRepository(plugin);

        this.houseRepository.getAll().forEach(this.houseManager::add);
    }

    @Override
    public void createHouse(Player owner, World world, String _size) throws
            PlayerHasNoMoneyException, PropertyLimitPerPlayerException, PropertyLimitSizeException, PropertyWorldEnvironmentException {

        if(!NumberUtils.isNumber(_size)) throw new NumberFormatException();
        int size = Integer.parseInt(_size);
        if (playerAlreadyHaveHouse(owner.getName())) throw new PropertyLimitPerPlayerException(owner, PropertyType.CASA);
        if (!worldEnvironmentException(world)) throw new PropertyWorldEnvironmentException(owner, PropertyType.CASA);
        if (!limitSizeException(owner, size)) throw new PropertyLimitSizeException(owner, PropertyType.CASA);
        if (!VaultHook.playerHasMoney(owner, getSizeValue(size))) throw new PlayerHasNoMoneyException();


        House house = new House(owner, size);
        this.houseManager.saveOrUpdate(house);
        this.houseRepository.saveOrUpdate(house);
    }

    @Override
    public void expandHouse(House house, Player owner, int size) throws
            PlayerHasNoMoneyException, PropertyLimitSizeException {

        if (!limitSizeException(owner, size + size)) throw new PropertyLimitSizeException(owner, PropertyType.CASA);
        if (!VaultHook.playerHasMoney(owner, getSizeValue(size))) throw new PlayerHasNoMoneyException();

        house.setSize(house.getSize() + size);

        this.houseManager.saveOrUpdate(house);
        this.houseRepository.saveOrUpdate(house); // REMOVER
    }

    @Override
    public void infoHouse(Player owner) throws HouseNotExistsException {
        int size = getHouse(owner.getName()).getSize();

        MessageUtils.messageToPlayer(owner, "");
        MessageUtils.messageToPlayerWithTag(owner, "&6Informações da Casa");
        MessageUtils.messageToPlayer(owner, "");
        MessageUtils.messageToPlayer(owner, "   &6● &7Proprietário: &3" + owner.getName().toUpperCase());
        MessageUtils.messageToPlayer(owner, "   &6● &7Tamanho: &8" + size);
        MessageUtils.messageToPlayer(owner, "");
    }

    @Override
    public void removeHouse(House house) {
        this.houseManager.remove(house);
        this.houseRepository.delete(house);
    }

    @Override
    public void updateHome(House house, Location home) {
        house.setX(home.getBlockX());
        house.setY(home.getBlockY());
        house.setZ(home.getBlockZ());

        this.houseManager.saveOrUpdate(house);
        this.houseRepository.saveOrUpdate(house);
    }


    @Override
    public House getHouse(String owner) throws HouseNotExistsException {
        House house = this.houseManager.getHouseByOwner(owner);
        if(house != null){
            return house;
        }
        throw new HouseNotExistsException();
    }

    @Override
    public double getSizeValue(int size) {
        return BLOCK_VALUE * size;
    }


    // Cache & MySQL
    @Override
    public void updateAllHouses(){
        this.houseManager.getHouses().forEach(house -> this.houseRepository.update(house));
    }


    // Exceptions
    private boolean worldEnvironmentException(World world) {
        return !world.getEnvironment().equals(World.Environment.THE_END) ||
                !world.getEnvironment().equals(World.Environment.NETHER);
    }

    private boolean limitSizeException(Player owner, int size) {
        return size >= 10 && (size <= 50 || !owner.hasPermission(Permissions.VIP.getPermission()));
    }

    private boolean limitMembersException(Player owner, int amountMembers) {
        return amountMembers == 3 || owner.hasPermission(Permissions.VIP.getPermission()) && amountMembers == 6;
    }

    private boolean playerAlreadyHaveHouse(String owner){
        return this.houseManager.contains(owner);
    }

}
