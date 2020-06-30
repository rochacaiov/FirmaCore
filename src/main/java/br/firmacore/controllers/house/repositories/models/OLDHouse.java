package br.firmacore.controllers.house.repositories.models;

import br.firmacore.Main;
import br.firmacore.controllers.house.exceptions.HouseMembersLimitException;
import br.firmacore.controllers.house.exceptions.HouseSizeLimitException;
import br.firmacore.controllers.house.exceptions.HouseWorldException;
import br.firmacore.enums.PropertyType;
import br.firmacore.enums.Permissions;
import br.firmacore.hooks.VaultHook;
import br.firmacore.hooks.exceptions.PlayerHasNoMoneyException;
import br.firmacore.services.WGServiceOLD;
import br.firmacore.services.exceptions.*;
import br.firmacore.utils.MessageUtils;
import br.firmacore.utils.PlayerUtils;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import javax.persistence.*;
import java.util.*;

@Table(name = "house")
public class OLDHouse {
    @Transient public static final long BLOCK_VALUE = 1000;
    @Transient public static final long PVP_VALUE = 10000;
    @Transient public static final int MAX_TAXES = 6;
    @Transient public static final PropertyType AREA_TYPE = PropertyType.CASA;
    @Transient private List<OLDTribute> OLDTributes = new ArrayList<>();
    @Transient private ProtectedRegion region;
    @Transient private Player player;

    @Id public String uuid;
    @Column(name = "x") public double x;
    @Column(name = "y") public double y;
    @Column(name = "z") public double z;
    @Column(name = "size") public int size;
    @Column(name = "dayBuy") public int dayBuy;
    @Column(name = "hourBuy") public int hourBuy;

    public OLDHouse(Player owner, int size) throws PropertyAlreadyExistsException, HouseWorldException, HouseSizeLimitException, PlayerHasNoMoneyException {
        if(!worldIsCorrect(owner)) throw new HouseWorldException();
        if(sizeInLimit(owner, size)) throw new HouseSizeLimitException();
        if(!playerHasMoney(owner, size)) throw new PlayerHasNoMoneyException();
        this.uuid = owner.getUniqueId().toString();
        this.x = owner.getLocation().getX();
        this.y = owner.getLocation().getY();
        this.z = owner.getLocation().getZ();
        this.player = owner;
        this.size = size;
        this.dayBuy = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        this.hourBuy = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        createHouse(size);
    }


    // [#] Funcionalidades
    public void infoHouse(){
        MessageUtils.messageToPlayer(this.player, "");
        MessageUtils.messageToPlayerWithTag(this.player, "&6Informações da Casa");
        MessageUtils.messageToPlayer(this.player, "");
        MessageUtils.messageToPlayer(this.player, "   &6● &7Proprietário: &3" + this.player.getName().toUpperCase());
        MessageUtils.messageToPlayer(this.player, "   &6● &7Tamanho: &8" + this.size);
        MessageUtils.messageToPlayer(
                this.player,
                "   &6● &7Tributos: &8" + this.OLDTributes.size() +
                         " &7(&c-" + VaultHook.getEconomy().format(calculateTotalTributesValue()) + "&7)"
        );
        MessageUtils.messageToPlayer(this.player, "");
    }

    public void removeProtection() throws PropertyNotFoundException {
        WGServiceOLD.removeProtection(this.player.getName(), PropertyType.CASA);
    }

    public void addFriend(String target) throws PropertyNotFoundException, HouseMembersLimitException, MemberAlreadyExistsException {
        if(membersInLimit()) throw new HouseMembersLimitException();
        WGServiceOLD.addMember(this.player.getName(), target, AREA_TYPE);
    }

    public void deleteFriend(String target) throws MemberNotExistsException, PropertyNotFoundException {
        WGServiceOLD.removeMember(this.player.getName(), target, AREA_TYPE);
    }

    public void updateHome(Location location) throws PlayerIsntInProperty, PropertyNotFoundException {
        if(WGServiceOLD.playerIsInRegion(location, this.player.getName(), AREA_TYPE)){
            this.x = location.getX();
            this.y = location.getY();
            this.z = location.getZ();
        }
    }

    public void updatePVP(){
        region.setFlag(
                Flags.GREET_MESSAGE,
                setGreetMessage(isPvp())
        );
        region.setFlag(Flags.PVP, isPvp() ? StateFlag.State.DENY : StateFlag.State.ALLOW);
    }

    public void expandSize(int size) throws HouseSizeLimitException, PropertyNotFoundException {
        if(sizeInLimit(this.player,this.size + size)) throw new HouseSizeLimitException();

        this.region = WGServiceOLD.expandProtection(
                this.player.getName(),
                AREA_TYPE,
                region.getMaximumPoint().getX(),
                region.getMaximumPoint().getZ(),
                region.getMinimumPoint().getX(),
                region.getMinimumPoint().getZ(),
                size
        );
        this.defineHouseFlags(isPvp());
        this.region.getOwners().addPlayer(this.player.getName());
        this.region.setPriority(100);
        this.size += size;

        this.removeProtection();
        WGServiceOLD.getRegionManager().addRegion(this.region);
    }

    public void teleport(Main plugin, Player player){
        PlayerUtils.teleportPlayer(
                plugin,
                player,
                new Location(Bukkit.getWorld("world"), this.x, this.y, this.z)
        );
    }


    // [#] House Configurations
    private String setGreetMessage(boolean pvp){
        String pvpTAG = pvp ? " &7[&a&lPVP OFF&7] " : " &7[&c&lPVP ON&7] ";
        return pvpTAG + "&8&l>> &6Olá, bem-vindo(a) à &8" + AREA_TYPE + " &6de &8" +
                this.player.getName() + "&6!";
    }

    private void createHouse(int size) throws PropertyAlreadyExistsException {
        region = WGServiceOLD.createProtection(
                this.player.getName(),
                AREA_TYPE,
                this.player.getLocation().getBlockX(),
                this.player.getLocation().getBlockZ(),
                size
        );
        this.defineHouseFlags(false);
        region.getOwners().addPlayer(this.player.getName());
        region.setPriority(100);

        WGServiceOLD.getRegionManager().addRegion(region);
    }

    private void defineHouseFlags(boolean pvp) {
        region.setFlag(Flags.GREET_MESSAGE, setGreetMessage(pvp));
        updatePVP();
        region.setFlag(Flags.OTHER_EXPLOSION, StateFlag.State.DENY);
        region.setFlag(Flags.ENDER_BUILD, StateFlag.State.DENY);
        region.setFlag(Flags.BLOCK_BREAK.getRegionGroupFlag(), RegionGroup.MEMBERS);
        region.setFlag(Flags.BLOCK_PLACE.getRegionGroupFlag(), RegionGroup.MEMBERS);
    }


    // [#] Regras de Negócio
    private boolean playerHasMoney(Player owner, int size){
        return (VaultHook.getEconomy().getBalance(owner) >= calculateHouseValue(size));
    }

    private boolean sizeInLimit(Player owner, int size){
        return ((size < 10) || (size > 50 && !owner.hasPermission(Permissions.VIP.getPermission())));
    }

    private boolean worldIsCorrect(Player owner){
        World world = owner.getWorld();
        return !world.getEnvironment().equals(World.Environment.NETHER) ||
                world.getEnvironment().equals(World.Environment.THE_END);
    }

    private boolean membersInLimit() throws PropertyNotFoundException {
        int amountMembers = WGServiceOLD.getAmountMembers(this.player.getName(), AREA_TYPE);
        return (
                amountMembers == 3 ||
                        (this.player.hasPermission(Permissions.VIP.getPermission()) &&
                                amountMembers == 6
                        )
        );
    }


    // [#] Matematica
    public static double calculateHouseValue(int size){
        return (BLOCK_VALUE * size); //
    }

    public double calculateTotalTributesValue(){
        return this.OLDTributes.stream().mapToDouble(OLDTribute::getValue).sum();
    }


    // [#] Tributos
    public void addTribute(OLDTribute OLDTribute){
        this.OLDTributes.add(OLDTribute);
    }
    
    public double payTributes() throws PlayerHasNoMoneyException {
        double total = calculateTotalTributesValue();
        if(VaultHook.getEconomy().getBalance(this.player) >= total){
            VaultHook.getEconomy().withdrawPlayer(this.player, total);
            this.OLDTributes.clear();
            return total;
        }
        throw new PlayerHasNoMoneyException();
    }


    // [#] Getters and Setters
    @Transient public boolean isPvp() {
        return region.getFlag(Flags.PVP) == StateFlag.State.ALLOW;
    }

    @Transient public Set<String> getMembers() throws PropertyNotFoundException, MembersEmptyException {
        return WGServiceOLD.getMembers(this.player.getName(), AREA_TYPE);
    }

    @Transient public List<OLDTribute> getOLDTributes() {
        return OLDTributes;
    }

    @Transient public Player getOwner() {
        return player;
    }

    public int getSize() {
        return size;
    }

    public int getDayBuy() {
        return dayBuy;
    }

    public int getHourBuy() {
        return hourBuy;
    }
}
