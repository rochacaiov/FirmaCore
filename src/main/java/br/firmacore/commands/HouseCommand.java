package br.firmacore.commands;

import io.github.mrblobman.spigotcommandlib.CommandHandle;
import io.github.mrblobman.spigotcommandlib.CommandHandler;
import org.bukkit.command.CommandSender;

public class HouseCommand implements CommandHandler {

    @CommandHandle(
            command = "casa",
            description = "comandos de casa"
    )
    private void home(CommandSender sender, String arg1){
        sender.sendMessage("teste" + arg1);
    }

}
