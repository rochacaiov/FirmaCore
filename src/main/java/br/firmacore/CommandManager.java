package br.firmacore;

import br.firmacore.commands.HouseCommand;
import br.firmacore.commands.HouseSubCommands;
import io.github.mrblobman.spigotcommandlib.registry.CommandLib;

public class CommandManager {
    private Main plugin;
    private CommandLib lib;

    public CommandManager(Main plugin) {
        this.plugin = plugin;
        this.lib = new CommandLib(plugin);
    }

    public void registerHouseCommands(){
        this.lib.registerCommandHandler(new HouseCommand());
        this.lib.registerSubCommandHandler(new HouseSubCommands(this.plugin), new String[]{"casa"});
    }
}
