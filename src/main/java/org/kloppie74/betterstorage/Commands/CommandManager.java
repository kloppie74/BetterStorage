package org.kloppie74.betterstorage.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.kloppie74.betterstorage.Commands.SubCommands.About;
import org.kloppie74.betterstorage.Commands.SubCommands.Givestorage;
import org.kloppie74.betterstorage.Commands.SubCommands.Givetransformer;
import org.kloppie74.betterstorage.YMLFiles.MSG;

import java.util.ArrayList;
import java.util.List;

public class CommandManager implements CommandExecutor, @Nullable TabCompleter {

    private final List<SubCommand> subcommands = new ArrayList<>();

    public CommandManager() {
        subcommands.add(new About());
        subcommands.add(new Givestorage());
        subcommands.add(new Givetransformer());
    }

    public SubCommand getSubCommand(String name) {
        for (SubCommand subCommand : subcommands) {
            if (subCommand.getName().equalsIgnoreCase(name)) {
                return subCommand;
            }
        }
        return null;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length > 0) {
                SubCommand subCommand = getSubCommand(args[0]);

                if (subCommand != null) {
                    subCommand.perform(player, args);
                    return true;
                } else {
                    player.sendMessage(MSG.chatColors("&7&l&m-----==&r&7&l[ &eBetterStorage &r&7&l]&7&l&m==-----"));
                    player.sendMessage(MSG.chatColors("&7"));
                    player.sendMessage(MSG.chatColors("&e/betterstorage about &7- &6Get the plugin's information!"));
                    player.sendMessage(MSG.chatColors("&e/betterstorage givestorage &7- &6Give a storage to a player!"));
                    player.sendMessage(MSG.chatColors("&e/betterstorage givetransformer &7- &6Give a transformer to a player!"));
                    player.sendMessage(MSG.chatColors("&7"));
                    return true;
                }
            } else {
                player.sendMessage(MSG.chatColors("&7&l&m-----==&r&7&l[ &eBetterStorage &r&7&l]&7&l&m==-----"));
                player.sendMessage(MSG.chatColors("&7"));
                player.sendMessage(MSG.chatColors("&e/betterstorage about &7- &6Get the plugin's information!"));
                player.sendMessage(MSG.chatColors("&e/betterstorage givestorage &7- &6Give a storage to a player!"));
                player.sendMessage(MSG.chatColors("&e/betterstorage givetransformer &7- &6Give a transformer to a player!"));
                player.sendMessage(MSG.chatColors("&7"));
                return true;
            }
        } else {
            sender.sendMessage("Only players can execute this command.");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            String currentArg = args[0].toLowerCase();

            for (SubCommand subCommand : subcommands) {
                String subCommandName = subCommand.getName().toLowerCase();
                if (subCommandName.startsWith(currentArg)) {
                    completions.add(subCommandName);
                }
            }
        } else if (args.length == 2) {
            // Voeg hier aanvullende tab-completion logica toe voor het specifieke subcommando
        }

        return completions;
    }

}

