package br.firmacore;

import br.firmacore.controllers.house.HouseController;
import br.firmacore.controllers.tribute.TributeController;
import br.firmacore.database.ConnectionFactory;
import br.firmacore.hooks.VaultHook;
import br.firmacore.utils.MessageUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private ConnectionFactory connectionFactory;
    private CommandManager commandManager;
    private HouseController houseController;
    private TributeController tributeController;

    @Override
    public void onEnable() {
        this.connectionFactory = new ConnectionFactory();
        if(VaultHook.setupEconomy()) MessageUtils.messageToConsole("Vault API configurado. &a" + (char)0x221A);

        this.houseController = new HouseController(this);
        this.tributeController = new TributeController(this);
        this.commandManager = new CommandManager(this);
        cape();
    }

    @Override
    public void onDisable() {
        this.houseController.updateAllHouses();
    }

    public ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public HouseController getHouseController(){
        return this.houseController;
    }

    public TributeController getTributeController() {
        return tributeController;
    }

    private void cape(){
        System.out.println("\n" +
                ChatColor.GOLD + " ___"+ChatColor.RED+"  __   __   __   ___ \n" +
                ChatColor.GOLD + "|__ "+ChatColor.RED+" /  ` /  \\ |__) |__  " +
                ChatColor.GOLD + " Firma" + ChatColor.RED + "CORE " +
                ChatColor.WHITE + "v" + getDescription().getVersion() + "\n" +
                ChatColor.GOLD + "|   "+ChatColor.RED+" \\__, \\__/ |  \\ |___ " +
                ChatColor.DARK_GRAY + " Created By F1rmeza\n" +
                ChatColor.GOLD + "                         \n");
        System.out.println(" » " + ChatColor.GREEN + "Plugin habilitado com sucesso!\n");
    }
}
