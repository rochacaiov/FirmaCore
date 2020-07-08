package br.firmacore.commands;

import br.firmacore.utils.MessageUtils;
import io.github.mrblobman.spigotcommandlib.CommandHandle;
import io.github.mrblobman.spigotcommandlib.CommandHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HouseCommand implements CommandHandler {

    @CommandHandle(command = "casa", description = "comandos de casa")
    private void home(CommandSender sender){
        if(sender instanceof Player){
            Player player = (Player)sender;
            MessageUtils.clearMessageToPlayer(player, "");
            MessageUtils.informativeMessageToPlayer(player, "&6Sistema de Casas.");
            MessageUtils.clearMessageToPlayer(player, "");
            MessageUtils.clearMessageToPlayer(player, "  &f/casa comprar [X] [Z] &8- &7Compra uma casa");
            MessageUtils.clearMessageToPlayer(player, "  &f/casa info &8- &7Informações da casa");
            MessageUtils.clearMessageToPlayer(player, "  &f/casa sethome &8- &7Altera o ponto de teleporte");
            MessageUtils.clearMessageToPlayer(player, "  &f/casa home &8- &7Teleporta para a casa");
            MessageUtils.clearMessageToPlayer(player, "  &f/casa addamigo &8- &7Adiciona um jogador na casa");
            MessageUtils.clearMessageToPlayer(player, "  &f/casa amigos &8- &7Lista os amigos adicionados");
            MessageUtils.clearMessageToPlayer(player, "  &f/casa delamigo &8- &7Remove um jogador da casa");
            MessageUtils.clearMessageToPlayer(player, "  &f/casa expandir [tamanho] &8- &7Expande a casa");
            MessageUtils.clearMessageToPlayer(player, "  &f/casa vender [valor] &8- &7Coloca a casa à venda");
            MessageUtils.clearMessageToPlayer(player, "");
            return;
        }
        MessageUtils.messageToConsole("Este comando só pode ser executado IN-GAME!");
    }

}
