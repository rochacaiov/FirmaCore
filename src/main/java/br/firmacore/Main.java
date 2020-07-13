package br.firmacore;

import br.firmacore.database.ConnectionFactory;
import br.firmacore.hooks.VaultHook;
import br.firmacore.services.house.api.HouseService;
import br.firmacore.services.property.api.PropertyService;
import br.firmacore.services.house.HouseServiceImpl;
import br.firmacore.services.property.PropertyServiceImpl;
import br.firmacore.utils.MessageUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private ConnectionFactory connectionFactory;
    private CommandManager commandManager;
    private PropertyService propertyService;
    private HouseService houseService;

    @Override
    public void onEnable() {
        this.connectionFactory = new ConnectionFactory();
        if(VaultHook.setupEconomy()) MessageUtils.messageToConsole("Vault API configurado. &a" + (char)0x221A);

        this.propertyService = new PropertyServiceImpl(Bukkit.getWorlds().get(0));
        this.houseService = new HouseServiceImpl(this);
        this.commandManager = new CommandManager(this);

        this.commandManager.registerHouseCommands();

        cape();
    }

    @Override
    public void onDisable() {
        this.houseService.updateAllHouses();
    }

    public ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    public PropertyService getPropertyService() {
        return propertyService;
    }

    public HouseService getHouseService() {
        return houseService;
    }

    private void cape(){
        System.out.println("\n" +
                ChatColor.GOLD + " ___"+ChatColor.RED+"  __   __   __   ___ \n" +
                ChatColor.GOLD + "|__ "+ChatColor.RED+" /  ` /  \\ |__) |__  " +
                ChatColor.GOLD + " Firma" + ChatColor.RED + "CORE " +
                ChatColor.WHITE + "v" + getDescription().getVersion() + "\n" +
                ChatColor.GOLD + "|   "+ChatColor.RED+" \\__, \\__/ |  \\ |___ " +
                ChatColor.DARK_GRAY + " Created By F1rmeza, Huryer....\n" +
                ChatColor.GOLD + "                         \n");
        System.out.println(" » " + ChatColor.GREEN + "Plugin habilitado com sucesso!\n");
    }
}
