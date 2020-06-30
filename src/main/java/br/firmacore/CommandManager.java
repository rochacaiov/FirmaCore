package br.firmacore;

import br.firmacore.controllers.house.commands.*;
import br.firmacore.enums.Commands;
import br.firmacore.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;


@SuppressWarnings("ALL")
public class CommandManager implements CommandExecutor {
    private Main plugin;
    private Set<SubCommand> subCommands = new HashSet<>();
    private final EnumSet<Commands> COMANDOS = EnumSet.noneOf(Commands.class);

    public CommandManager(Main plugin){
        this.plugin = plugin;
        this.setupCasaCommand();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)){
            MessageUtils.messageToConsole("&eComando executável apenas IN-GAME!");
            return true;
        }
        Player player = (Player) sender;

        if(Commands.contains(cmd.getName())){
            if(args.length == 0){
                if(cmd.getName().equalsIgnoreCase(Commands.CASA.toString())){
                    this.houseCommands(player);
                }
                return true;
            }

            SubCommand target = this.get(args[0]);

            if (target == null) {
                MessageUtils.messageToPlayerWithTag(player, "&cComando inexistente!");
                return true;
            }

            if(target.permission() != null){
                if(!sender.hasPermission(target.permission())){
                    MessageUtils.messageToPlayerWithTag(player, "&cVocê não tem permissão.");
                    return true;
                }
            }

            ArrayList<String> arrayList = new ArrayList<String>();

            arrayList.addAll(Arrays.asList(args));
            arrayList.remove(0);


            try{
                target.onCommand(player,args);
                return true;
            }catch (Exception e){
                e.printStackTrace();
                MessageUtils.messageToPlayerWithTag(player, "&cComando incorreto!");
                MessageUtils.messageToPlayerWithTag(player, target.usage());
            }
        }

        return false;
    }

    public void setupCasaCommand(){
        this.plugin.getCommand(Commands.CASA.toString().toLowerCase()).setExecutor(this);

        this.subCommands.add(new BuySubCommand(this.plugin));
        this.subCommands.add(new RemoveSubCommand(this.plugin));
        this.subCommands.add(new AddSubCommand(this.plugin));
        this.subCommands.add(new FriendsSubCommand(this.plugin));
        this.subCommands.add(new DelSubCommand(this.plugin));
        this.subCommands.add(new HomeSubCommand(this.plugin));
        this.subCommands.add(new SetHomeSubCommand(this.plugin));
        this.subCommands.add(new PVPCommand(this.plugin));
        this.subCommands.add(new ExpandCommand(this.plugin));
        this.subCommands.add(new InfoSubCommand(this.plugin));
    }

    private SubCommand get(String name) {

        for (SubCommand sc : this.subCommands) {
            if (sc.name().equalsIgnoreCase(name)) {
                return sc;
            }

            String[] aliases;
            int length = (aliases = sc.aliases()).length;

            for (int var5 = 0; var5 < length; ++var5) {
                String alias = aliases[var5];
                if (name.equalsIgnoreCase(alias)) {
                    return sc;
                }

            }
        }
        return null;
    }

    private void houseCommands(Player sender){
        MessageUtils.messageToPlayer(sender, "");
        MessageUtils.messageToPlayerWithTag(sender, "&6Sistema de Casas.");
        MessageUtils.messageToPlayer(sender, "");
        MessageUtils.messageToPlayer(sender, "  &f/casa comprar [tamanho] &8- &7Compra uma casa");
        MessageUtils.messageToPlayer(sender, "  &f/casa info &8- &7Informações da casa");
        MessageUtils.messageToPlayer(sender, "  &f/casa sethome &8- &7Altera o ponto de teleporte");
        MessageUtils.messageToPlayer(sender, "  &f/casa home &8- &7Teleporta para a casa");
        MessageUtils.messageToPlayer(sender, "  &f/casa addamigo &8- &7Adiciona um jogador na casa");
        MessageUtils.messageToPlayer(sender, "  &f/casa amigos &8- &7Lista os amigos adicionados");
        MessageUtils.messageToPlayer(sender, "  &f/casa delamigo &8- &7Remove um jogador da casa");
        MessageUtils.messageToPlayer(sender, "  &f/casa expandir [tamanho] &8- &7Expande a casa");
        MessageUtils.messageToPlayer(sender, "  &f/casa tributo pagar &8- &7Paga todos os tributos acumulados");
        MessageUtils.messageToPlayer(sender, "  &f/casa vender [valor] &8- &7Coloca a casa à venda");
        MessageUtils.messageToPlayer(sender, "");
    }

}
